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

package com.liferay.portal.poller;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.notifications.ChannelException;
import com.liferay.portal.kernel.notifications.ChannelHubManagerUtil;
import com.liferay.portal.kernel.notifications.ChannelListener;
import com.liferay.portal.kernel.notifications.NotificationEvent;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Edward Han
 */
public class SynchronousPollerChannelListener implements ChannelListener {

	@Override
	public synchronized void channelListenerRemoved(long channelId) {
		_complete = true;

		notify();
	}

	public String getNotificationEvents(
			long companyId, long userId,
			JSONObject pollerResponseHeaderJSONObject, long timeout)
		throws ChannelException {

		_lock.lock();

		try {
			_waitForCompletion(timeout);

			List<NotificationEvent> notificationEvents =
				ChannelHubManagerUtil.fetchNotificationEvents(
					companyId, userId, true);

			JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

			jsonArray.put(pollerResponseHeaderJSONObject);

			for (NotificationEvent notificationEvent : notificationEvents) {
				jsonArray.put(notificationEvent.toJSONObject());
			}

			return jsonArray.toString();
		}
		finally {
			_lock.unlock();
		}
	}

	@Override
	public synchronized void notificationEventsAvailable(long channelId) {
		_complete = true;

		notify();
	}

	private synchronized void _waitForCompletion(long timeout) {
		try {
			if (!_complete) {
				wait(timeout);
			}
		}
		catch (InterruptedException ie) {
		}
	}

	private boolean _complete;
	private final Lock _lock = new ReentrantLock();

}