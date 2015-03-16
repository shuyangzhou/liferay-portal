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
import com.liferay.portal.kernel.cluster.ClusterChannel;
import com.liferay.portal.kernel.cluster.ClusterChannelFactory;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterException;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterReceiver;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.concurrent.ConcurrentReferenceValueHashMap;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.memory.FinalizeManager;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.util.PortalInetSocketAddressEventListener;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 */
@DoPrivileged
public class ClusterExecutorImpl
	implements ClusterExecutor, PortalInetSocketAddressEventListener {

	@Override
	public void addClusterEventListener(
		ClusterEventListener clusterEventListener) {

		if (!isEnabled()) {
			return;
		}

		_clusterEventListeners.addIfAbsent(clusterEventListener);
	}

	@Override
	public void destroy() {
		if (!isEnabled()) {
			return;
		}

		_controlChannel.close();

		_executorService.shutdownNow();

		_clusterEventListeners.clear();
		_clusterNodes.clear();
		_futureClusterResponses.clear();
		_localClusterNode = null;
	}

	@Override
	public FutureClusterResponses execute(ClusterRequest clusterRequest) {
		if (!isEnabled()) {
			return null;
		}

		Set<String> clusterNodeIds = new HashSet<>();

		if (clusterRequest.isMulticast()) {
			clusterNodeIds = new HashSet<>(_clusterNodes.keySet());

			if (clusterRequest.isSkipLocal()) {
				clusterNodeIds.remove(_localClusterNodeId);
			}
		}
		else {
			clusterNodeIds.addAll(clusterRequest.getTargetClusterNodeIds());
		}

		FutureClusterResponses futureClusterResponses =
			new FutureClusterResponses(clusterNodeIds);

		if (!clusterRequest.isFireAndForget()) {
			String uuid = clusterRequest.getUuid();

			_futureClusterResponses.put(uuid, futureClusterResponses);
		}

		if (clusterNodeIds.remove(_localClusterNodeId)) {
			ClusterNodeResponse clusterNodeResponse = executeClusterRequest(
				clusterRequest);

			if (!clusterRequest.isFireAndForget()) {
				futureClusterResponses.addClusterNodeResponse(
					clusterNodeResponse);
			}
		}

		if (clusterRequest.isMulticast()) {
			_controlChannel.sendMulticastMessage(clusterRequest);
		}
		else {
			for (String clusterNodeId : clusterNodeIds) {
				ClusterNode clusterNode = _clusterNodes.get(clusterNodeId);

				if (clusterNode == null) {
					if (_log.isErrorEnabled()) {
						_log.error(
							"Unable to find cluster node " + clusterNodeId);
					}

					continue;
				}

				_controlChannel.sendUnicastMessage(
					clusterRequest,
					clusterNode.getChannelAddress(
						_controlChannel.getClusterName()));
			}
		}

		return futureClusterResponses;
	}

	@Override
	public List<ClusterEventListener> getClusterEventListeners() {
		if (!isEnabled()) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(_clusterEventListeners);
	}

	@Override
	public List<ClusterNode> getClusterNodes() {
		if (!isEnabled()) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(
			new ArrayList<>(_clusterNodes.values()));
	}

	@Override
	public ClusterNode getLocalClusterNode() {
		if (!isEnabled()) {
			return null;
		}

		return _localClusterNode;
	}

	@Override
	public void initialize() {
		if (!isEnabled()) {
			return;
		}

		_executorService = PortalExecutorManagerUtil.getPortalExecutor(
			ClusterExecutorImpl.class.getName());

		PortalUtil.addPortalInetSocketAddressEventListener(this);

		if (PropsValues.CLUSTER_LINK_DEBUG_ENABLED) {
			addClusterEventListener(new DebuggingClusterEventListenerImpl());
		}

		if (PropsValues.LIVE_USERS_ENABLED) {
			addClusterEventListener(new LiveUsersClusterEventListenerImpl());
		}

		_clusterReceiver = new ClusterRequestReceiver(this);

		_controlChannel = _clusterChannelFactory.createClusterChannel(
			PropsValues.CLUSTER_LINK_CHANNEL_PROPERTIES_CONTROL,
			_LIFERAY_CONTROL_CHANNEL_NAME, _clusterReceiver);

		_localClusterNodeId = PortalUUIDUtil.generate();

		_localClusterNode = new ClusterNode(
			_localClusterNodeId, _controlChannel.getBindInetAddress());

		_localClusterNode.addChannelAddress(
			_controlChannel.getClusterName(),
			_controlChannel.getLocalAddress());

		if (Validator.isNotNull(PropsValues.PORTAL_INSTANCE_PROTOCOL)) {
			_localClusterNode.setPortalProtocol(
				PropsValues.PORTAL_INSTANCE_PROTOCOL);

			_localClusterNode.setPortalInetSocketAddress(
				getConfiguredPortalAddress());
		}

		memberJoined(_localClusterNode);

		sendNotifyRequest();

		_clusterReceiver.openLatch();
	}

	@Override
	public boolean isClusterNodeAlive(String clusterNodeId) {
		if (!isEnabled()) {
			return false;
		}

		return _clusterNodes.containsKey(clusterNodeId);
	}

	@Override
	public boolean isEnabled() {
		return PropsValues.CLUSTER_LINK_ENABLED;
	}

	@Override
	public void portalLocalInetSocketAddressConfigured(
		InetSocketAddress inetSocketAddress, boolean secure) {

		if (!isEnabled() || (_localClusterNode.getPortalProtocol() != null)) {
			return;
		}

		_localClusterNode.setPortalInetSocketAddress(inetSocketAddress);

		if (secure) {
			_localClusterNode.setPortalProtocol(Http.HTTPS);
		}
		else {
			_localClusterNode.setPortalProtocol(Http.HTTP);
		}

		memberJoined(_localClusterNode);

		ClusterRequest clusterRequest = ClusterRequest.createMulticastRequest(
			_localClusterNode, true);

		_controlChannel.sendMulticastMessage(clusterRequest);
	}

	@Override
	public void portalServerInetSocketAddressConfigured(
		InetSocketAddress inetSocketAddress, boolean secure) {
	}

	@Override
	public void removeClusterEventListener(
		ClusterEventListener clusterEventListener) {

		if (!isEnabled()) {
			return;
		}

		_clusterEventListeners.remove(clusterEventListener);
	}

	public void setClusterChannelFactory(
		ClusterChannelFactory clusterChannelFactory) {

		_clusterChannelFactory = clusterChannelFactory;
	}

	public void setClusterEventListeners(
		List<ClusterEventListener> clusterEventListeners) {

		if (!isEnabled()) {
			return;
		}

		_clusterEventListeners.addAllAbsent(clusterEventListeners);
	}

	protected ClusterNodeResponse executeClusterRequest(
		ClusterRequest clusterRequest) {

		Serializable payload = clusterRequest.getPayload();

		if (!(payload instanceof MethodHandler)) {
			return ClusterNodeResponse.createExceptionClusterNodeResponse(
				_localClusterNode, clusterRequest.getUuid(),
				new ClusterException(
					"Payload is not of type " + MethodHandler.class.getName()));
		}

		MethodHandler methodHandler = (MethodHandler)payload;

		ClusterInvokeThreadLocal.setEnabled(false);

		try {
			return ClusterNodeResponse.createResultClusterNodeResponse(
				_localClusterNode, clusterRequest.getUuid(),
				methodHandler.invoke());
		}
		catch (Exception e) {
			return ClusterNodeResponse.createExceptionClusterNodeResponse(
				_localClusterNode, clusterRequest.getUuid(), e);
		}
		finally {
			ClusterInvokeThreadLocal.setEnabled(true);
		}
	}

	protected void fireClusterEvent(ClusterEvent clusterEvent) {
		for (ClusterEventListener listener : _clusterEventListeners) {
			listener.processClusterEvent(clusterEvent);
		}
	}

	protected InetSocketAddress getConfiguredPortalAddress() {
		if (Validator.isNull(PropsValues.PORTAL_INSTANCE_INET_SOCKET_ADDRESS)) {
			throw new IllegalArgumentException(
				"Portal instance host name and port needs to be set in the " +
					"property \"portal.instance.inet.socket.address\"");
		}

		String[] parts = StringUtil.split(
			PropsValues.PORTAL_INSTANCE_INET_SOCKET_ADDRESS, CharPool.COLON);

		if (parts.length != 2) {
			throw new IllegalArgumentException(
				"Unable to parse the portal instance host name and port from " +
					PropsValues.PORTAL_INSTANCE_INET_SOCKET_ADDRESS);
		}

		InetAddress hostInetAddress = null;

		try {
			hostInetAddress = InetAddress.getByName(parts[0]);
		}
		catch (UnknownHostException uhe) {
			throw new IllegalArgumentException(
				"Unable to parse the portal instance host name and port from " +
					PropsValues.PORTAL_INSTANCE_INET_SOCKET_ADDRESS, uhe);
		}

		int port = -1;

		try {
			port = GetterUtil.getIntegerStrict(parts[1]);
		}
		catch (NumberFormatException nfe) {
			throw new IllegalArgumentException(
				"Unable to parse portal InetSocketAddress port from " +
					PropsValues.PORTAL_INSTANCE_INET_SOCKET_ADDRESS, nfe);
		}

		return new InetSocketAddress(hostInetAddress, port);
	}

	protected ClusterChannel getControlChannel() {
		return _controlChannel;
	}

	protected FutureClusterResponses getExecutionResults(String uuid) {
		return _futureClusterResponses.get(uuid);
	}

	protected ExecutorService getExecutorService() {
		return _executorService;
	}

	protected boolean memberJoined(ClusterNode clusterNode) {
		ClusterNode oldClusterNode = _clusterNodes.put(
			clusterNode.getClusterNodeId(), clusterNode);

		if (oldClusterNode != null) {
			if (!oldClusterNode.equals(clusterNode)) {
				if (_log.isInfoEnabled()) {
					_log.info("Updated cluster node " + clusterNode);
				}
			}

			return false;
		}

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		fireClusterEvent(clusterEvent);

		return true;
	}

	protected void memberRemoved(List<Address> removedAddresses) {
		List<ClusterNode> departClusterNodes = new ArrayList<>();

		String clusterChannelName = _controlChannel.getClusterName();

		Collection<ClusterNode> clusterNodes = _clusterNodes.values();
		Iterator<ClusterNode> itr = clusterNodes.iterator();

		while (itr.hasNext()) {
			ClusterNode clusterNode = itr.next();

			Address address = clusterNode.getChannelAddress(clusterChannelName);

			if (removedAddresses.contains(address)) {
				departClusterNodes.add(clusterNode);

				itr.remove();
			}
		}

		if (departClusterNodes.isEmpty()) {
			return;
		}

		ClusterEvent clusterEvent = ClusterEvent.depart(departClusterNodes);

		fireClusterEvent(clusterEvent);
	}

	protected void sendNotifyRequest() {
		ClusterRequest clusterRequest = ClusterRequest.createMulticastRequest(
			_localClusterNode, true);

		_controlChannel.sendMulticastMessage(clusterRequest);
	}

	private static final String _LIFERAY_CONTROL_CHANNEL_NAME =
		PropsValues.CLUSTER_LINK_CHANNEL_NAME_PREFIX + "control";

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterExecutorImpl.class);

	private ClusterChannelFactory _clusterChannelFactory;
	private final CopyOnWriteArrayList<ClusterEventListener>
		_clusterEventListeners = new CopyOnWriteArrayList<>();
	private final Map<String, ClusterNode> _clusterNodes =
		new ConcurrentHashMap<>();
	private ClusterReceiver _clusterReceiver;
	private ClusterChannel _controlChannel;
	private ExecutorService _executorService;
	private final Map<String, FutureClusterResponses> _futureClusterResponses =
		new ConcurrentReferenceValueHashMap<>(
			FinalizeManager.WEAK_REFERENCE_FACTORY);
	private ClusterNode _localClusterNode;
	private String _localClusterNodeId;

}