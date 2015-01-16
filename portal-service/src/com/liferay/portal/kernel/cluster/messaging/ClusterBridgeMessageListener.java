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

package com.liferay.portal.kernel.cluster.messaging;

import com.liferay.portal.kernel.cluster.ChannelMessage;
import com.liferay.portal.kernel.cluster.ChannelMessageType;
import com.liferay.portal.kernel.cluster.ClusterManager;
import com.liferay.portal.kernel.cluster.ClusterManagerUtil;
import com.liferay.portal.kernel.cluster.ClusterMessage;
import com.liferay.portal.kernel.cluster.Priority;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Collection;

/**
 * @author Shuyang Zhou
 */
public class ClusterBridgeMessageListener extends BaseMessageListener {

	public void setPriority(Priority priority) {
		_priority = priority;
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		if (message.getBoolean(ClusterManager.CLUSTER_FORWARD_MESSAGE)) {
			return;
		}

		ChannelMessage channelMessage = ChannelMessage.createChannelMessage(
			ChannelMessageType.FORWARD, message, true);

		Object targetClusterNodeIds = message.get(
			ClusterManager.TARGET_CLUSTER_NODE_IDS);

		ClusterMessage clusterMessage = null;

		if (targetClusterNodeIds == null) {
			if (_log.isInfoEnabled()) {
				_log.info("Bridging cluster link multicast message " + message);
			}

			clusterMessage = ClusterMessage.createMulticastMessage(
				channelMessage, _priority);
		}
		else {
			if (!(targetClusterNodeIds instanceof Collection)) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Unable to parse target cluster node Ids " +
							targetClusterNodeIds);
				}

				return;
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Bridging cluster link unicast message " + message +
						" to " + targetClusterNodeIds);
			}

			clusterMessage = ClusterMessage.createUnicastMessage(
				channelMessage, _priority,
				ArrayUtil.toStringArray(
					(Collection<String>)targetClusterNodeIds));
		}

		ClusterManagerUtil.sendAndForget(clusterMessage);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterBridgeMessageListener.class);

	private Priority _priority;

}