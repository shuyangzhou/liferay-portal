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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashSet;
import java.util.Set;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;

/**
 * @author Tina Tian
 */
public class ReceiverAdapter implements Receiver {

	public ReceiverAdapter(ChannelReceiver channelReceiver) {
		_channelReceiver = channelReceiver;
	}

	@Override
	public void block() {
	}

	@Override
	public void getState(OutputStream outputStream) {
	}

	@Override
	public void receive(Message message) {
		Object object = message.getObject();

		if (object == null) {
			if (_log.isWarnEnabled()) {
				_log.warn("Message content is null");
			}

			return;
		}

		if (object instanceof ChannelMessage) {
			_channelReceiver.receive((ChannelMessage)object);
		}
		else if (_log.isWarnEnabled()) {
			_log.warn(
				"Unable to process message content of type " +
					object.getClass());
		}
	}

	@Override
	public void setState(InputStream inputStream) {
	}

	@Override
	public void suspect(org.jgroups.Address address) {
	}

	@Override
	public void unblock() {
	}

	@Override
	public void viewAccepted(View view) {
		if (_log.isInfoEnabled()) {
			_log.info("Accepted view " + view);
		}

		Set<Address> addresses = new HashSet<>(view.size());

		for (org.jgroups.Address jgroupsAddress : view.getMembers()) {
			addresses.add(new AddressImpl(jgroupsAddress));
		}

		_channelReceiver.updateView(addresses);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ReceiverAdapter.class);

	private final ChannelReceiver _channelReceiver;

}