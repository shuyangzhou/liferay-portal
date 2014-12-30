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

package com.liferay.portal.notifications;

import com.liferay.portal.kernel.notifications.ChannelListener;

/**
 * @author Andrew Betts
 */
public class TestChannelListener implements ChannelListener {

	public TestChannelListener() {
		notifyCount = 0;
	}

	@Override
	public void channelListenerRemoved(long channelId) {
	}

	public int getNofityCount() {
		return notifyCount;
	}

	@Override
	public void notificationEventsAvailable(long channelId) {
		notifyCount++;
	}

	public void resetNotifyCount() {
		notifyCount = 0;
	}

	private int notifyCount;

}