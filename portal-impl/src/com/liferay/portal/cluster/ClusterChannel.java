/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.cluster;

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ChannelMessage;
import com.liferay.portal.kernel.cluster.ChannelMessageType;
import com.liferay.portal.kernel.cluster.ClusterManager;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.concurrent.ConcurrentReferenceValueHashMap;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.memory.FinalizeManager;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.SocketUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;

import java.net.InetAddress;
import java.net.NetworkInterface;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgroups.JChannel;

/**
 * @author Tina Tian
 */
public class ClusterChannel {

	public ClusterChannel(
		String name, String properties, ChannelReceiver channelReceiver,
		ClusterManager clusterManager) {

		_initBindAddress();

		if (Validator.isNull(name)) {
			throw new NullPointerException("Cluster channel name is null");
		}

		if (Validator.isNull(properties)) {
			throw new NullPointerException(
				"Cluster channel properties is null");
		}

		if (channelReceiver == null) {
			throw new NullPointerException("Cluster channel receiver is null");
		}

		if (clusterManager == null) {
			throw new NullPointerException("Cluster channel manager is null");
		}

		_name = name;
		_channelReceiver = channelReceiver;
		_clusterManager = clusterManager;
		_localClusterNodeId = clusterManager.getLocalClusterNodeId();

		try {
			_jChannel = new JChannel(properties);

			_jChannel.setReceiver(new ReceiverAdapter(channelReceiver));

			_jChannel.connect(name);

			if (_log.isInfoEnabled()) {
				_log.info(
					"Create a new jgroups channel with properties " +
						_jChannel.getProperties());
			}

			_localAddress = new AddressImpl(_jChannel.getAddress());
		}
		catch (Exception e) {
			throw new SystemException(
				"Unable to initial cluster channel " + _name, e);
		}
	}

	public void close() {
		_jChannel.setReceiver(null);

		_jChannel.close();

		_futureClusterResponses.clear();
	}

	public Address getLocalAddress() {
		return _localAddress;
	}

	public String getName() {
		return _name;
	}

	public FutureClusterResponses sendMulticastMessage(
		ChannelMessage channelMessage) {

		if (_jChannel.isClosed()) {
			if (_log.isWarnEnabled()) {
				_log.warn("Cluster channel " + _name + " is alreay closed");
			}

			return null;
		}

		FutureClusterResponses futureClusterResponses =
			generateFutureClusterResponses(channelMessage, null);

		try {
			_jChannel.send(null, channelMessage);

			if (_log.isDebugEnabled()) {
				_log.debug("Send multicast message " + channelMessage);
			}
		}
		catch (Exception e) {
			throw new SystemException("Unable to send multicast message", e);
		}

		return futureClusterResponses;
	}

	public FutureClusterResponses sendUnitcastMessage(
		ChannelMessage channelMessage, String... clusterNodeIds) {

		if (_jChannel.isClosed()) {
			if (_log.isWarnEnabled()) {
				_log.warn("Cluster channel " + _name + " is alreay closed");
			}

			return null;
		}

		FutureClusterResponses futureClusterResponses =
			generateFutureClusterResponses(
				channelMessage, SetUtil.fromArray(clusterNodeIds));

		for (String clusterNodeId : clusterNodeIds) {
			if (_localClusterNodeId.equals(clusterNodeId) &&
				(channelMessage.isSkipLocal()||
				 _clusterManager.isShortcutLocalMethod())) {

				continue;
			}

			ClusterNode clusterNode = _clusterManager.getClusterNode(
				clusterNodeId);

			Address address = clusterNode.getChannelAddress(_name);

			try {
				_jChannel.send(
					(org.jgroups.Address)address.getRealAddress(),
					channelMessage);

				if (_log.isDebugEnabled()) {
					_log.debug("Send unicast message " + channelMessage);
				}
			}
			catch (Exception e) {
				throw new SystemException("Unable to send unicast message", e);
			}
		}

		return futureClusterResponses;
	}

	public void start() {
		_channelReceiver.start();
	}

	protected FutureClusterResponses generateFutureClusterResponses(
		ChannelMessage channelMessage, Set<String> clusterNodeIds) {

		if (channelMessage.isSendAndForget()) {
			return null;
		}

		ChannelMessageType channelMessageType =
			channelMessage.getChannelMessageType();

		if (!channelMessageType.equals(ChannelMessageType.EXECUTE)) {
			return null;
		}

		if (clusterNodeIds == null) {
			clusterNodeIds = new HashSet<>();

			clusterNodeIds.addAll(_clusterManager.getClusterNodeIds());
		}

		if (channelMessage.isSkipLocal()) {
			clusterNodeIds.remove(_localClusterNodeId);
		}

		FutureClusterResponses futureClusterResponses =
			new FutureClusterResponses(clusterNodeIds);

		_futureClusterResponses.put(
			channelMessage.getUuid(), futureClusterResponses);

		return futureClusterResponses;
	}

	protected FutureClusterResponses getExecutionResults(String uuid) {
		return _futureClusterResponses.get(uuid);
	}

	private void _initBindAddress() {
		if (_initBindAddress) {
			return;
		}

		String autodetectAddress =
			PropsValues.CLUSTER_CHANNEL_AUTODETECT_BIND_ADDRESS;

		if (Validator.isNull(autodetectAddress)) {
			return;
		}

		String host = autodetectAddress;
		int port = 80;

		int index = autodetectAddress.indexOf(CharPool.COLON);

		if (index != -1) {
			host = autodetectAddress.substring(0, index);
			port = GetterUtil.getInteger(
				autodetectAddress.substring(index + 1), port);
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				"Autodetecting JGroups outgoing IP address and interface for " +
					host + ":" + port);
		}

		try {
			SocketUtil.BindInfo bindInfo = SocketUtil.getBindInfo(host, port);

			InetAddress inetAddress = bindInfo.getInetAddress();

			NetworkInterface networkInterface = bindInfo.getNetworkInterface();

			System.setProperty(
				"jgroups.bind_addr", inetAddress.getHostAddress());
			System.setProperty(
				"jgroups.bind_interface", networkInterface.getName());

			if (_log.isInfoEnabled()) {
				_log.info(
					"Setting JGroups outgoing IP address to " +
						inetAddress.getHostAddress() + " and interface to " +
							networkInterface.getName());
			}
		}
		catch (IOException e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to detect bind address for jgroups", e);
			}
		}

		_initBindAddress = true;
	}

	private static final Log _log = LogFactoryUtil.getLog(ClusterChannel.class);

	private static volatile boolean _initBindAddress;

	private final ChannelReceiver _channelReceiver;
	private final ClusterManager _clusterManager;
	private final Map<String, FutureClusterResponses> _futureClusterResponses =
		new ConcurrentReferenceValueHashMap<>(
			FinalizeManager.WEAK_REFERENCE_FACTORY);
	private final JChannel _jChannel;
	private final Address _localAddress;
	private final String _localClusterNodeId;
	private final String _name;

}