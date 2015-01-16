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
import com.liferay.portal.kernel.cluster.ClusterException;
import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterManager;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class TransportChannelReceiver extends ChannelReceiver {

	public TransportChannelReceiver(
		String channelName, ClusterManager clusterManager) {

		super(channelName, clusterManager);
	}

	@Override
	protected void doReceive(ChannelMessage channelMessage) {
		ChannelMessageType channelMessageType =
			channelMessage.getChannelMessageType();

		switch (channelMessageType) {
			case EXECUTE:
				handleExecuteMessage(channelMessage);

				break;
			case FORWARD:
				handleForwardMessage(channelMessage);

				break;
			case RESPONSE:
				handleResponseMessage(channelMessage);

				break;
			case NOTIFY:
			case UPDATE:
				if (_log.isErrorEnabled()) {
					_log.error(
						"Unable to handle channel message type " +
							channelMessageType);
				}
		}
	}

	protected void handleExecuteMessage(ChannelMessage channelMessage) {
		Serializable payload = channelMessage.getPayload();

		Serializable returnValue = null;
		Exception exception = null;

		if (payload instanceof MethodHandler) {
			MethodHandler methodHandler = (MethodHandler)payload;

			try {
				ClusterInvokeThreadLocal.setEnabled(false);

				returnValue = (Serializable)methodHandler.invoke();
			}
			catch (Exception e) {
				exception = e;

				_log.error("Unable to invoke method " + methodHandler, e);
			}
			finally {
				ClusterInvokeThreadLocal.setEnabled(true);
			}
		}
		else {
			exception = new ClusterException(
				"Payload is not of type " + MethodHandler.class.getName());
		}

		if (channelMessage.isSendAndForget()) {
			return;
		}

		ClusterNodeResponse clusterNodeResponse =
			clusterManagerImpl.generateClusterNodeResponse(
				channelMessage, returnValue, exception);

		ChannelMessage responseMessage = ChannelMessage.createChannelMessage(
			ChannelMessageType.RESPONSE, clusterNodeResponse,
			channelMessage.isSkipLocal());

		clusterChannel.sendUnitcastMessage(
			responseMessage, channelMessage.getClusterNodeId());
	}

	protected void handleForwardMessage(ChannelMessage channelMessage) {
		Serializable payload = channelMessage.getPayload();

		if (!(payload instanceof Message)) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to handle payload " + payload);
			}

			return;
		}

		Message message = (Message)payload;

		String destinationName = message.getDestinationName();

		if (Validator.isNull(destinationName)) {
			if (_log.isErrorEnabled()) {
				_log.error(
					"Forwarded cluster message has no destination " + message);
			}

			return;
		}

		message.put(ClusterManager.CLUSTER_FORWARD_MESSAGE, true);

		MessageBusUtil.sendMessage(destinationName, message);

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Forwarding cluster message " + message + " to " +
					destinationName);
		}
	}

	protected void handleResponseMessage(ChannelMessage channelMessage) {
		Serializable payload = channelMessage.getPayload();

		if (!(payload instanceof ClusterNodeResponse)) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to handle payload " + payload);
			}

			return;
		}

		ClusterNodeResponse clusterNodeResponse = (ClusterNodeResponse)payload;

		String uuid = clusterNodeResponse.getUuid();

		FutureClusterResponses futureClusterResponses =
			clusterChannel.getExecutionResults(uuid);

		if (futureClusterResponses == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to find response container for " + uuid);
			}

			return;
		}

		String clusterNodeId = channelMessage.getClusterNodeId();

		if (futureClusterResponses.expectsReply(clusterNodeId)) {
			futureClusterResponses.addClusterNodeResponse(clusterNodeResponse);
		}
		else {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unexpected cluster node ID " + clusterNodeId +
						" for response container with UUID " + uuid);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TransportChannelReceiver.class);

}