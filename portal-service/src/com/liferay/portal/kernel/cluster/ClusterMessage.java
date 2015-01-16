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

package com.liferay.portal.kernel.cluster;

import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tina Tian
 */
public class ClusterMessage implements Serializable {

	public static ClusterMessage createMulticastMessage(
		ChannelMessage channelMessage) {

		return createMulticastMessage(channelMessage, _DEFAULT_PRIORITY);
	}

	public static ClusterMessage createMulticastMessage(
		ChannelMessage channelMessage, Priority priority) {

		ClusterMessage clusterMessage = new ClusterMessage();

		clusterMessage.setChannelMessage(channelMessage);
		clusterMessage.setMulticast(true);
		clusterMessage.setPriority(priority);

		return clusterMessage;
	}

	public static ClusterMessage createUnicastMessage(
		ChannelMessage channelMessage, Priority priority,
		String... targetClusterNodeIds) {

		ClusterMessage clusterMessage = new ClusterMessage();

		clusterMessage.addTargetClusterNodeIds(targetClusterNodeIds);
		clusterMessage.setChannelMessage(channelMessage);
		clusterMessage.setMulticast(false);
		clusterMessage.setPriority(priority);

		return clusterMessage;
	}

	public static ClusterMessage createUnicastMessage(
		ChannelMessage channelMessage, String... targetClusterNodeIds) {

		return createUnicastMessage(
			channelMessage, _DEFAULT_PRIORITY, targetClusterNodeIds);
	}

	public void addTargetClusterNodeIds(String... targetClusterNodeIds) {
		if (_targetClusterNodeIds == null) {
			_targetClusterNodeIds = new HashSet<>(targetClusterNodeIds.length);
		}

		for (String targetClusterNodeId : targetClusterNodeIds) {
			_targetClusterNodeIds.add(targetClusterNodeId);
		}
	}

	public ChannelMessage getChannelMessage() {
		return _channelMessage;
	}

	public Priority getPriority() {
		return _priority;
	}

	public Set<String> getTargetClusterNodeIds() {
		return Collections.unmodifiableSet(_targetClusterNodeIds);
	}

	public boolean isMulticast() {
		return _multicast;
	}

	public void setChannelMessage(ChannelMessage channelMessage) {
		_channelMessage = channelMessage;
	}

	public void setMulticast(boolean multicast) {
		_multicast = multicast;
	}

	public void setPriority(Priority priority) {
		_priority = priority;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(9);

		sb.append("{channelMessage=");
		sb.append(_channelMessage);
		sb.append(", multicast=");
		sb.append(_multicast);
		sb.append(", _priority=");
		sb.append(_priority);
		sb.append(", _targetClusterNodeIds=");
		sb.append(_targetClusterNodeIds);
		sb.append("}");

		return sb.toString();
	}

	private ClusterMessage() {
	}

	private static final Priority _DEFAULT_PRIORITY = Priority.LEVEL5;

	private ChannelMessage _channelMessage;
	private boolean _multicast;
	private Priority _priority;
	private Set<String> _targetClusterNodeIds;

}