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

import com.liferay.portal.kernel.cluster.ChannelMessage;
import com.liferay.portal.kernel.cluster.ChannelMessageType;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterException;
import com.liferay.portal.kernel.cluster.ClusterManager;
import com.liferay.portal.kernel.cluster.ClusterMessage;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterResponseCallback;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.cluster.Priority;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @author Tina Tian
 */
public class ClusterManagerImpl
	implements ClusterManager, PortalInetSocketAddressEventListener {

	public static final String CLUSTER_MANAGER_CALLBACK_THREAD_POOL =
		"CLUSTER_MANAGER_CALLBACK_THREAD_POOL";

	public void afterPropertiesSet() {
		if (!isEnabled()) {
			return;
		}

		for (String systemProperty :
				PropsValues.CLUSTER_MANAGER_SYSTEM_PROPERTIES) {

			int index = systemProperty.indexOf(CharPool.COLON);

			if (index == -1) {
				continue;
			}

			String key = systemProperty.substring(0, index);
			String value = systemProperty.substring(index + 1);

			System.setProperty(key, value);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Setting system property {key=" + key + ", value=" + value +
						"}");
			}
		}
	}

	@Override
	public void destroy() {
		PortalExecutorManagerUtil.shutdown(
			CLUSTER_MANAGER_CALLBACK_THREAD_POOL, true);

		_controlChannel.close();

		for (ClusterChannel clusterChannel : _transportChannels) {
			clusterChannel.close();
		}

		_transportChannels.clear();
		_clusterEventListeners.clear();
		_clusterNodes.clear();

		_localClusterNode = null;
		_localClusterNodeId = null;
	}

	@Override
	public ClusterNode getClusterNode(String clusterNodeId) {
		return _clusterNodes.get(clusterNodeId);
	}

	@Override
	public Set<String> getClusterNodeIds() {
		return Collections.unmodifiableSet(_clusterNodes.keySet());
	}

	@Override
	public Set<ClusterNode> getClusterNodes() {
		Set<ClusterNode> clusterNodes = new HashSet<>(_clusterNodes.values());

		return Collections.unmodifiableSet(clusterNodes);
	}

	@Override
	public ClusterNode getLocalClusterNode() {
		return _localClusterNode;
	}

	@Override
	public String getLocalClusterNodeId() {
		return _localClusterNodeId;
	}

	@Override
	public void initialize() {
		if (PropsValues.CLUSTER_MANAGER_DEBUG_ENABLED) {
			registerClusterEventListener(
				new DebuggingClusterEventListenerImpl());
		}

		if (PropsValues.LIVE_USERS_ENABLED) {
			registerClusterEventListener(
				new LiveUsersClusterEventListenerImpl());
		}

		PortalUtil.addPortalInetSocketAddressEventListener(this);

		_executorService = PortalExecutorManagerUtil.getPortalExecutor(
			CLUSTER_MANAGER_CALLBACK_THREAD_POOL);

		_localClusterNodeId = PortalUUIDUtil.generate();

		_localClusterNode = new ClusterNode(_localClusterNodeId);

		initPortalAddress(_localClusterNode);

		initControlChannel();

		_localClusterNode.addChannelAddress(
			_controlChannel.getName(), _controlChannel.getLocalAddress());

		initTransportChannels();

		for (ClusterChannel transportChannel : _transportChannels) {
			_localClusterNode.addChannelAddress(
				transportChannel.getName(), transportChannel.getLocalAddress());
		}

		_clusterNodes.put(_localClusterNodeId, _localClusterNode);

		ChannelMessage channelMessage = ChannelMessage.createChannelMessage(
			ChannelMessageType.NOTIFY, _localClusterNode, true);

		_controlChannel.sendMulticastMessage(channelMessage);

		_controlChannel.start();

		for (ClusterChannel transportChannel : _transportChannels) {
			transportChannel.start();
		}
	}

	@Override
	public boolean isEnabled() {
		return PropsValues.CLUSTER_MANAGER_ENABLED;
	}

	@Override
	public boolean isShortcutLocalMethod() {
		return _shortcutLocalMethod;
	}

	@Override
	public void portalLocalInetSockAddressConfigured(
		InetSocketAddress localInetSocketAddress, boolean secure) {

		if (Validator.isNotNull(_localClusterNode.getPortalProtocol())) {
			return;
		}

		_localClusterNode.setPortalInetSocketAddress(localInetSocketAddress);

		if (secure) {
			_localClusterNode.setPortalProtocol(Http.HTTPS);
		}
		else {
			_localClusterNode.setPortalProtocol(Http.HTTP);
		}

		_clusterNodes.put(_localClusterNodeId, _localClusterNode);

		if (_log.isInfoEnabled()) {
			_log.info("Update local cluster node " + _localClusterNode);
		}

		ChannelMessage channelMessage = ChannelMessage.createChannelMessage(
			ChannelMessageType.UPDATE, _localClusterNode, true);

		_controlChannel.sendMulticastMessage(channelMessage);
	}

	@Override
	public void portalServerInetSocketAddressConfigured(
		InetSocketAddress serverInetSocketAddress, boolean secure) {
	}

	@Override
	public void registerClusterEventListener(
		ClusterEventListener clusterEventListener) {

		_clusterEventListeners.add(clusterEventListener);
	}

	@Override
	public FutureClusterResponses send(ClusterMessage clusterMessage) {
		ChannelMessage channelMessage = clusterMessage.getChannelMessage();

		ChannelMessageType channelMessageType =
			channelMessage.getChannelMessageType();

		if (!channelMessageType.equals(ChannelMessageType.EXECUTE)) {
			throw new SystemException(
				"Unable to receive response for channel message type " +
					channelMessageType);
		}

		Priority priority = clusterMessage.getPriority();

		ClusterChannel clusterChannel = selectClusterChannel(priority);

		FutureClusterResponses futureClusterResponses = null;

		if (clusterMessage.isMulticast()) {
			futureClusterResponses = clusterChannel.sendMulticastMessage(
				channelMessage);
		}
		else {
			futureClusterResponses = clusterChannel.sendUnitcastMessage(
				channelMessage,
				ArrayUtil.toStringArray(
					clusterMessage.getTargetClusterNodeIds()));
		}

		ClusterNodeResponse clusterNodeResponse = runLocalMethod(
			clusterMessage, false);

		if (clusterNodeResponse != null) {
			futureClusterResponses.addClusterNodeResponse(clusterNodeResponse);
		}

		return futureClusterResponses;
	}

	@Override
	public FutureClusterResponses send(
		ClusterMessage clusterMessage,
		ClusterResponseCallback clusterResponseCallback) {

		FutureClusterResponses futureClusterResponses = send(clusterMessage);

		ClusterResponseCallbackJob clusterResponseCallbackJob =
			new ClusterResponseCallbackJob(
				clusterResponseCallback, futureClusterResponses);

		_executorService.execute(clusterResponseCallbackJob);

		return futureClusterResponses;
	}

	@Override
	public void sendAndForget(ClusterMessage clusterMessage) {
		Priority priority = clusterMessage.getPriority();

		ClusterChannel clusterChannel = selectClusterChannel(priority);

		ChannelMessage channelMessage = clusterMessage.getChannelMessage();

		channelMessage.setSendAndForget(true);

		if (clusterMessage.isMulticast()) {
			clusterChannel.sendMulticastMessage(channelMessage);
		}
		else {
			clusterChannel.sendUnitcastMessage(
				channelMessage,
				ArrayUtil.toStringArray(
					clusterMessage.getTargetClusterNodeIds()));
		}

		runLocalMethod(clusterMessage, true);
	}

	public void setClusterEventListeners(
		List<ClusterEventListener> clusterEventListeners) {

		_clusterEventListeners.clear();

		_clusterEventListeners.addAll(clusterEventListeners);
	}

	public void setShortcutLocalMethod(boolean shortcutLocalMethod) {
		_shortcutLocalMethod = shortcutLocalMethod;
	}

	@Override
	public void unregisterClusterEventListener(
		ClusterEventListener clusterEventListener) {

		_clusterEventListeners.remove(clusterEventListener);
	}

	protected void clusterNodeDeparted(Set<ClusterNode> clusterNodes) {
		List<ClusterNode> departedClusterNodes = new ArrayList<>();

		for (ClusterNode clusterNode : clusterNodes) {
			String clusterNodeId = clusterNode.getClusterNodeId();

			if (clusterNodeId.equals(_localClusterNodeId)) {
				continue;
			}

			_clusterNodes.remove(clusterNodeId);

			departedClusterNodes.add(clusterNode);
		}

		if (departedClusterNodes.isEmpty()) {
			return;
		}

		ClusterEvent clusterEvent = ClusterEvent.depart(departedClusterNodes);

		for (ClusterEventListener clusterEventListener :
				_clusterEventListeners) {

			clusterEventListener.processClusterEvent(clusterEvent);
		}
	}

	protected void clusterNodeJoined(ClusterNode clusterNode) {
		String clusterNodeId = clusterNode.getClusterNodeId();

		ClusterNode oldClusterNode = _clusterNodes.put(
			clusterNodeId, clusterNode);

		if (oldClusterNode != null) {
			if (!oldClusterNode.equals(clusterNode)) {
				if (_log.isInfoEnabled()) {
					_log.info("Updated cluster node " + clusterNode);
				}
			}

			return;
		}

		if (_localClusterNodeId.equals(clusterNodeId)) {
			return;
		}

		ClusterEvent clusterEvent = ClusterEvent.join(clusterNode);

		for (ClusterEventListener clusterEventListener :
				_clusterEventListeners) {

			clusterEventListener.processClusterEvent(clusterEvent);
		}
	}

	protected ClusterNodeResponse generateClusterNodeResponse(
		ChannelMessage channelMessage, Object returnValue,
		Exception exception) {

		ClusterNodeResponse clusterNodeResponse = new ClusterNodeResponse();

		clusterNodeResponse.setClusterNode(_localClusterNode);
		clusterNodeResponse.setUuid(channelMessage.getUuid());

		if (exception != null) {
			clusterNodeResponse.setException(exception);
		}
		else {
			if (returnValue instanceof Serializable) {
				clusterNodeResponse.setResult(returnValue);
			}
			else if (returnValue != null) {
				clusterNodeResponse.setException(
					new ClusterException("Return value is not serializable"));
			}
		}

		return clusterNodeResponse;
	}

	protected ClusterChannel getClusterChannel(String channelName) {
		if (channelName.equals(PropsValues.CLUSTER_CHANNEL_NAME_CONTROL)) {
			return _controlChannel;
		}

		if (!channelName.startsWith(PropsValues.CLUSTER_CHANNEL_NAME_PREFIX)) {
			throw new SystemException(
				"Unable to find cluster channel " + channelName);
		}

		for (ClusterChannel clusterChannel : _transportChannels) {
			if (channelName.equals(clusterChannel.getName())) {
				return clusterChannel;
			}
		}

		throw new SystemException(
			"Unable to find cluster channel " + channelName);
	}

	protected void initControlChannel() {
		String channelName = PropsValues.CLUSTER_CHANNEL_NAME_CONTROL;

		ChannelReceiver channelReceiver = new ControlChannelReceiver(
			channelName, this);

		_controlChannel = new ClusterChannel(
			channelName, PropsValues.CLUSTER_CHANNEL_PROPERTIES_CONTROL,
			channelReceiver, this);
	}

	protected void initPortalAddress(ClusterNode clusterNode) {
		if (Validator.isNull(PropsValues.PORTAL_INSTANCE_PROTOCOL)) {
			return;
		}

		clusterNode.setPortalProtocol(PropsValues.PORTAL_INSTANCE_PROTOCOL);

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

		InetAddress inetAddress = null;

		try {
			inetAddress = InetAddress.getByName(parts[0]);
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
			new InetSocketAddress(inetAddress, port));
	}

	protected void initTransportChannels() {
		Properties transportProperties = PropsUtil.getProperties(
			PropsKeys.CLUSTER_CHANNEL_PROPERTIES_TRANSPORT, true);

		int channelCount = transportProperties.size();
		int maxChannelCount = Priority.values().length;

		if ((channelCount <= 0) || (channelCount > maxChannelCount)) {
			throw new IllegalArgumentException(
				"Channel count must be between 1 and " + maxChannelCount);
		}

		_transportChannels = new ArrayList<>(channelCount);

		List<String> keys = new ArrayList<>(channelCount);

		for (Object key : transportProperties.keySet()) {
			keys.add((String)key);
		}

		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String property = keys.get(i);

			String channelProperties = transportProperties.getProperty(
				property);

			String channelName = PropsValues.CLUSTER_CHANNEL_NAME_PREFIX + i;

			ChannelReceiver channelReceiver = new TransportChannelReceiver(
				channelName, this);

			ClusterChannel clusterChannel = new ClusterChannel(
				channelName, channelProperties, channelReceiver, this);

			_transportChannels.add(clusterChannel);
		}
	}

	protected ClusterNodeResponse runLocalMethod(
		ClusterMessage clusterMessage, boolean sendAndForget) {

		if (!_shortcutLocalMethod) {
			return null;
		}

		ChannelMessage channelMessage = clusterMessage.getChannelMessage();

		ChannelMessageType channelMessageType =
			channelMessage.getChannelMessageType();

		if (!channelMessageType.equals(ChannelMessageType.EXECUTE) ||
			channelMessage.isSkipLocal()) {

			return null;
		}

		if (!clusterMessage.isMulticast()) {
			Set<String> targetClusterNodeIds =
				clusterMessage.getTargetClusterNodeIds();

			if (!targetClusterNodeIds.contains(_localClusterNodeId)) {
				return null;
			}
		}

		Object returnValue = null;
		Exception exception = null;

		Serializable payload = channelMessage.getPayload();

		if (payload instanceof MethodHandler) {
			MethodHandler methodHandler = (MethodHandler)payload;

			try {
				returnValue = methodHandler.invoke();
			}
			catch (Exception e) {
				exception = e;
			}
		}
		else {
			exception = new ClusterException(
				"Payload is not of type " + MethodHandler.class.getName());
		}

		if (sendAndForget) {
			return null;
		}

		return generateClusterNodeResponse(
			channelMessage, returnValue, exception);
	}

	protected ClusterChannel selectClusterChannel(Priority priority) {
		int channelIndex =
			priority.ordinal() * _transportChannels.size() /
				Priority.values().length;

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Select channel number " + channelIndex + " for priority " +
					priority);
		}

		return _transportChannels.get(channelIndex);
	}

	private static Log _log = LogFactoryUtil.getLog(ClusterManagerImpl.class);

	private final CopyOnWriteArrayList<ClusterEventListener>
		_clusterEventListeners = new CopyOnWriteArrayList<>();
	private final Map<String, ClusterNode> _clusterNodes =
		new ConcurrentHashMap<>();
	private ClusterChannel _controlChannel;
	private ExecutorService _executorService;
	private ClusterNode _localClusterNode;
	private String _localClusterNodeId;
	private boolean _shortcutLocalMethod;
	private List<ClusterChannel> _transportChannels;

	private class ClusterResponseCallbackJob implements Runnable {

		public ClusterResponseCallbackJob(
			ClusterResponseCallback clusterResponseCallback,
			FutureClusterResponses futureClusterResponses) {

			_clusterResponseCallback = clusterResponseCallback;
			_futureClusterResponses = futureClusterResponses;
		}

		@Override
		public void run() {
			BlockingQueue<ClusterNodeResponse> blockingQueue =
				_futureClusterResponses.getPartialResults();

			_clusterResponseCallback.callback(blockingQueue);
		}

		private final ClusterResponseCallback _clusterResponseCallback;
		private final FutureClusterResponses _futureClusterResponses;

	}

}