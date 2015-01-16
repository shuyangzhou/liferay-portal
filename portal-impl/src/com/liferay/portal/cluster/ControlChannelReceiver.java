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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tina Tian
 */
public class ControlChannelReceiver extends ChannelReceiver {

	public ControlChannelReceiver(
		String channelName, ClusterManager clusterManager) {

		super(channelName, clusterManager);
	}

	@Override
	protected void doReceive(ChannelMessage channelMessage) {
		ChannelMessageType channelMessageType =
			channelMessage.getChannelMessageType();

		if (!channelMessageType.equals(ChannelMessageType.NOTIFY) &&
			!channelMessageType.equals(ChannelMessageType.UPDATE)) {

			if (_log.isErrorEnabled()) {
				_log.error(
					"Unable to handle channel message type " +
						channelMessageType);
			}

			return;
		}

		Serializable payload = channelMessage.getPayload();

		if (!(payload instanceof ClusterNode)) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to handle payload " + payload);
			}

			return;
		}

		clusterManagerImpl.clusterNodeJoined((ClusterNode)payload);

		if (channelMessageType.equals(ChannelMessageType.UPDATE)) {
			return;
		}

		ChannelMessage responseMessage = ChannelMessage.createChannelMessage(
			ChannelMessageType.UPDATE, localClusterNode, true);

		clusterChannel.sendUnitcastMessage(
			responseMessage, channelMessage.getClusterNodeId());
	}

	@Override
	protected void doUpdateView(
		Set<Address> oldAddresses, Set<Address> newAddresses) {

		Set<Address> addedAddresses = new HashSet<>(newAddresses);

		addedAddresses.removeAll(oldAddresses);

		if (!addedAddresses.isEmpty()) {
			ChannelMessage channelMessage = ChannelMessage.createChannelMessage(
				ChannelMessageType.NOTIFY, localClusterNode, true);

			clusterChannel.sendMulticastMessage(channelMessage);
		}

		Set<Address> departedAddresses = new HashSet<>(oldAddresses);

		departedAddresses.removeAll(newAddresses);

		if (departedAddresses.isEmpty()) {
			return;
		}

		Set<ClusterNode> clusterNodes = clusterManagerImpl.getClusterNodes();

		Set<ClusterNode> departedClusterNodes = new HashSet<>();

		for (ClusterNode clusterNode : clusterNodes) {
			Address address = clusterNode.getChannelAddress(channelName);

			if (departedAddresses.contains(address)) {
				departedClusterNodes.add(clusterNode);
			}
		}

		clusterManagerImpl.clusterNodeDeparted(departedClusterNodes);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ControlChannelReceiver.class);

}