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

import com.liferay.portal.kernel.cache.Lifecycle;
import com.liferay.portal.kernel.cache.ThreadLocalCacheManager;
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
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.concurrent.ConcurrentReferenceValueHashMap;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.memory.FinalizeManager;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.CentralizedThreadLocal;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.util.PortalInetSocketAddressEventListener;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
	extends BaseClusterReceiver
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
		_clusterNodeAddresses.clear();
		_futureClusterResponses.clear();
		_liveInstances.clear();
		_localAddress = null;
		_localClusterNode = null;
	}

	@Override
	public FutureClusterResponses execute(ClusterRequest clusterRequest) {
		if (!isEnabled()) {
			return null;
		}

		List<Address> addresses = prepareAddresses(clusterRequest);

		Set<String> clusterNodeIds = new HashSet<>();

		for (Address address : addresses) {
			ClusterNode clusterNode = _liveInstances.get(address);

			if (clusterNode != null) {
				clusterNodeIds.add(clusterNode.getClusterNodeId());
			}
		}

		FutureClusterResponses futureClusterResponses =
			new FutureClusterResponses(clusterNodeIds);

		if (!clusterRequest.isFireAndForget()) {
			String uuid = clusterRequest.getUuid();

			_futureClusterResponses.put(uuid, futureClusterResponses);
		}

		if (addresses.remove(_localAddress)) {
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
			for (Address address : addresses) {
				_controlChannel.sendUnicastMessage(clusterRequest, address);
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

		return new ArrayList<>(_liveInstances.values());
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

		if (PropsValues.CLUSTER_EXECUTOR_DEBUG_ENABLED) {
			addClusterEventListener(new DebuggingClusterEventListenerImpl());
		}

		if (PropsValues.LIVE_USERS_ENABLED) {
			addClusterEventListener(new LiveUsersClusterEventListenerImpl());
		}

		try {
			initControlChannel();

			_localAddress = _controlChannel.getLocalAddress();

			initLocalClusterNode();

			memberJoined(_localAddress, _localClusterNode);

			_controlChannel.sendMulticastMessage(
				ClusterRequest.createMulticastRequest(_localClusterNode, true));

			openLatch();
		}
		catch (Exception e) {
			if (_log.isErrorEnabled()) {
				_log.error("Unable to initialize", e);
			}

			throw new IllegalStateException(e);
		}
	}

	@Override
	public boolean isClusterNodeAlive(String clusterNodeId) {
		if (!isEnabled()) {
			return false;
		}

		return _clusterNodeAddresses.containsKey(clusterNodeId);
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

		memberJoined(_localAddress, _localClusterNode);

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

	@Override
	protected void doReceive(
		Object messagePayload, Address srcAddress, Address destAddress) {

		if (srcAddress.equals(_localAddress)) {
			return;
		}

		try {
			if (messagePayload instanceof ClusterRequest) {
				ClusterRequest clusterRequest = (ClusterRequest)messagePayload;

				processClusterRequest(clusterRequest, srcAddress);
			}
			else if (messagePayload instanceof ClusterNodeResponse) {
				ClusterNodeResponse clusterNodeResponse =
					(ClusterNodeResponse)messagePayload;

				processClusterResponse(clusterNodeResponse, srcAddress);
			}
			else if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to process message content of type " +
						messagePayload.getClass());
			}
		}
		finally {
			ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);

			CentralizedThreadLocal.clearShortLivedThreadLocals();
		}
	}

	@Override
	protected void doViewAccepted(
		List<Address> oldView, List<Address> newView) {

		List<Address> newAddresses = new ArrayList<>(newView);

		newAddresses.removeAll(oldView);

		if (!newAddresses.isEmpty()) {
			_controlChannel.sendMulticastMessage(
				ClusterRequest.createMulticastRequest(_localClusterNode, true));
		}

		List<Address> departAddresses = new ArrayList<>(oldView);

		departAddresses.removeAll(newView);

		if (!departAddresses.isEmpty()) {
			memberRemoved(departAddresses);
		}
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

	@Override
	protected ExecutorService getExecutorService() {
		return _executorService;
	}

	protected void initControlChannel() throws Exception {
		Properties controlProperties = PropsUtil.getProperties(
			PropsKeys.CLUSTER_LINK_CHANNEL_PROPERTIES_CONTROL, false);

		String controlProperty = controlProperties.getProperty(
			PropsKeys.CLUSTER_LINK_CHANNEL_PROPERTIES_CONTROL);

		_controlChannel = _clusterChannelFactory.createClusterChannel(
			controlProperty, _DEFAULT_CLUSTER_NAME, this);
	}

	protected void initLocalClusterNode() {
		InetAddress inetAddress = _controlChannel.getBindInetAddress();

		ClusterNode clusterNode = new ClusterNode(
			PortalUUIDUtil.generate(), inetAddress);

		if (Validator.isNull(PropsValues.PORTAL_INSTANCE_PROTOCOL)) {
			_localClusterNode = clusterNode;

			return;
		}

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

		clusterNode.setPortalInetSocketAddress(
			new InetSocketAddress(hostInetAddress, port));

		clusterNode.setPortalProtocol(PropsValues.PORTAL_INSTANCE_PROTOCOL);

		_localClusterNode = clusterNode;
	}

	protected boolean memberJoined(
		Address joinAddress, ClusterNode clusterNode) {

		_liveInstances.put(joinAddress, clusterNode);

		Address previousAddress = _clusterNodeAddresses.put(
			clusterNode.getClusterNodeId(), joinAddress);

		if (previousAddress != null) {
			return false;
		}

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		fireClusterEvent(clusterEvent);

		return true;
	}

	protected void memberRemoved(List<Address> departAddresses) {
		List<ClusterNode> departClusterNodes = new ArrayList<>();

		for (Address departAddress : departAddresses) {
			ClusterNode departClusterNode = _liveInstances.remove(
				departAddress);

			if (departClusterNode == null) {
				continue;
			}

			departClusterNodes.add(departClusterNode);

			_clusterNodeAddresses.remove(departClusterNode.getClusterNodeId());
		}

		if (departClusterNodes.isEmpty()) {
			return;
		}

		ClusterEvent clusterEvent = ClusterEvent.depart(departClusterNodes);

		fireClusterEvent(clusterEvent);
	}

	protected List<Address> prepareAddresses(ClusterRequest clusterRequest) {
		boolean isMulticast = clusterRequest.isMulticast();

		List<Address> addresses = null;

		if (isMulticast) {
			addresses = getView();
		}
		else {
			addresses = new ArrayList<>();

			Collection<String> clusterNodeIds =
				clusterRequest.getTargetClusterNodeIds();

			if (clusterNodeIds != null) {
				for (String clusterNodeId : clusterNodeIds) {
					Address address = _clusterNodeAddresses.get(clusterNodeId);

					addresses.add(address);
				}
			}
		}

		if (clusterRequest.isSkipLocal()) {
			addresses.remove(_localAddress);
		}

		return addresses;
	}

	protected void processClusterRequest(
		ClusterRequest clusterRequest, Address sourceAddress) {

		Serializable responsePayload = null;

		Serializable requestPayload = clusterRequest.getPayload();

		if (requestPayload instanceof ClusterNode) {
			boolean newMember = memberJoined(
				sourceAddress, (ClusterNode)requestPayload);

			if (newMember) {
				responsePayload = ClusterRequest.createMulticastRequest(
					_localClusterNode, true);
			}
		}
		else {
			ClusterNodeResponse clusterNodeResponse = executeClusterRequest(
				clusterRequest);

			if (!clusterRequest.isFireAndForget()) {
				responsePayload = clusterNodeResponse;
			}
		}

		if (responsePayload == null) {
			return;
		}

		try {
			_controlChannel.sendUnicastMessage(responsePayload, sourceAddress);
		}
		catch (Throwable t) {
			_log.error("Unable to send message " + responsePayload, t);
		}
	}

	protected void processClusterResponse(
		ClusterNodeResponse clusterNodeResponse, Address sourceAddress) {

		String uuid = clusterNodeResponse.getUuid();

		FutureClusterResponses futureClusterResponses =
			_futureClusterResponses.get(uuid);

		if (futureClusterResponses == null) {
			if (_log.isInfoEnabled()) {
				_log.info("Unable to find response container for " + uuid);
			}

			return;
		}

		if (!futureClusterResponses.addClusterNodeResponse(
				clusterNodeResponse) &&
			_log.isWarnEnabled()) {

			ClusterNode clusterNode = clusterNodeResponse.getClusterNode();

			_log.warn(
				"Unexpected cluster node ID " + clusterNode.getClusterNodeId() +
					" for response container with UUID " + uuid);
		}
	}

	private static final String _DEFAULT_CLUSTER_NAME =
		"LIFERAY-CONTROL-CHANNEL";

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterExecutorImpl.class);

	private ClusterChannelFactory _clusterChannelFactory;
	private final CopyOnWriteArrayList<ClusterEventListener>
		_clusterEventListeners = new CopyOnWriteArrayList<>();
	private final Map<String, Address> _clusterNodeAddresses =
		new ConcurrentHashMap<>();
	private ClusterChannel _controlChannel;
	private ExecutorService _executorService;
	private final Map<String, FutureClusterResponses> _futureClusterResponses =
		new ConcurrentReferenceValueHashMap<>(
			FinalizeManager.WEAK_REFERENCE_FACTORY);
	private final Map<Address, ClusterNode> _liveInstances =
		new ConcurrentHashMap<>();
	private Address _localAddress;
	private ClusterNode _localClusterNode;

}