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
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class ChannelMessage implements Serializable {

	public static ChannelMessage createChannelMessage(
		ChannelMessageType channelMessageType, Serializable payload) {

		return createChannelMessage(channelMessageType, payload, false);
	}

	public static ChannelMessage createChannelMessage(
		ChannelMessageType channelMessageType, Serializable payload,
		boolean skiplocal) {

		ChannelMessage channelMessage = new ChannelMessage();

		channelMessage.setChannelMessageType(channelMessageType);
		channelMessage.setClusterNodeId(
			ClusterManagerUtil.getLocalClusterNodeId());
		channelMessage.setPayload(payload);
		channelMessage.setSkipLocal(skiplocal);
		channelMessage.setUuid(PortalUUIDUtil.generate());

		return channelMessage;
	}

	public ChannelMessageType getChannelMessageType() {
		return _channelMessageType;
	}

	public String getClusterNodeId() {
		return _clusterNodeId;
	}

	public Serializable getPayload() {
		return _payload;
	}

	public String getUuid() {
		return _uuid;
	}

	public boolean isSendAndForget() {
		return _sendAndForget;
	}

	public boolean isSkipLocal() {
		return _skipLocal;
	}

	public void setChannelMessageType(ChannelMessageType channelMessageType) {
		_channelMessageType = channelMessageType;
	}

	public void setClusterNodeId(String clusterNodeId) {
		_clusterNodeId = clusterNodeId;
	}

	public void setPayload(Serializable payload) {
		_payload = payload;
	}

	public void setSendAndForget(boolean sendAndForget) {
		_sendAndForget = sendAndForget;
	}

	public void setSkipLocal(boolean skipLocal) {
		_skipLocal = skipLocal;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(11);

		sb.append("{channelMessageType=");
		sb.append(_channelMessageType);
		sb.append(", clusterNodeId=");
		sb.append(_clusterNodeId);
		sb.append(", payload=");
		sb.append(_payload);
		sb.append(", skipLocal=");
		sb.append(_skipLocal);
		sb.append(", uuid=");
		sb.append(_uuid);
		sb.append("}");

		return sb.toString();
	}

	private ChannelMessage() {
	}

	private ChannelMessageType _channelMessageType;
	private String _clusterNodeId;
	private Serializable _payload;
	private boolean _sendAndForget;
	private boolean _skipLocal;
	private String _uuid;

}