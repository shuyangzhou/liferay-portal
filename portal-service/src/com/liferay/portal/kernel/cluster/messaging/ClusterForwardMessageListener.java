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

import com.liferay.portal.kernel.cluster.ClusterLinkUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.BasePortalLifecycle;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class ClusterForwardMessageListener extends BasePortalLifecycle
	implements ClusterMessageListener {

	public ClusterForwardMessageListener() {
		registerPortalLifecycle(METHOD_INIT);
	}

	@Override
	public void receive(Message message) {
		if (!_portalStartupComplete) {
			synchronized (this) {
				if (!_portalStartupComplete) {
					_startupMessageBuffer.add(message);

					return;
				}
			}
		}

		String destinationName = message.getDestinationName();

		if (Validator.isNotNull(destinationName)) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Forwarding cluster link message " + message + " to " +
						destinationName);
			}

			ClusterLinkUtil.setForwardMessage(message);

			MessageBusUtil.sendMessage(destinationName, message);
		}
		else {
			if (_log.isErrorEnabled()) {
				_log.error(
					"Forwarded cluster link message has no destination " +
						message);
			}
		}
	}

	@Override
	protected void doPortalDestroy() throws Exception {
	}

	@Override
	protected void doPortalInit() throws Exception {
		synchronized (this) {
			_portalStartupComplete = true;
		}

		for (Message message : _startupMessageBuffer) {
			receive(message);
		}

		_startupMessageBuffer.clear();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterForwardMessageListener.class);

	private volatile boolean _portalStartupComplete = false;
	private final List<Message> _startupMessageBuffer =
		new ArrayList<Message>();

}