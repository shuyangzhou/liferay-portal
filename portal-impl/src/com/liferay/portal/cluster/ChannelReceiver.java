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

import com.liferay.portal.kernel.cache.Lifecycle;
import com.liferay.portal.kernel.cache.ThreadLocalCacheManager;
import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ChannelMessage;
import com.liferay.portal.kernel.cluster.ClusterManager;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CentralizedThreadLocal;

import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Tina Tian
 */
public abstract class ChannelReceiver {

	public ChannelReceiver(String channelName, ClusterManager clusterManager) {
		this.channelName = channelName;
		this.clusterManagerImpl =(ClusterManagerImpl)clusterManager;
		this.localClusterNode = clusterManagerImpl.getLocalClusterNode();
		this.localClusterNodeId = clusterManagerImpl.getLocalClusterNodeId();
	}

	public void receive(ChannelMessage channelMessage) {
		try {
			try {
				_countDownLatch.await();
			}
			catch (InterruptedException ie) {
				_log.error(
					"Latch opened prematurely by interruption. Dependence " +
						"may not be ready.");
			}

			if (_log.isDebugEnabled()) {
				_log.debug("Received message "  + channelMessage);
			}

			String clusterNodeId = channelMessage.getClusterNodeId();

			if (localClusterNodeId.equals(clusterNodeId) &&
				(channelMessage.isSkipLocal()||
				 clusterManagerImpl.isShortcutLocalMethod())) {

				if (_log.isDebugEnabled()) {
					_log.debug("Skip local message " + channelMessage);
				}

				return;
			}

			doReceive(channelMessage);
		}
		finally {
			ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);

			CentralizedThreadLocal.clearShortLivedThreadLocals();
		}
	}

	public void start() {
		clusterChannel = clusterManagerImpl.getClusterChannel(channelName);

		_countDownLatch.countDown();

		if (_log.isDebugEnabled()) {
			_log.debug("Receiver for channel " + channelName + "is started");
		}
	}

	public void updateView(Set<Address> addresses) {
		if (_addresses == null) {
			_addresses = addresses;

			return;
		}

		try {
			_countDownLatch.await();
		}
		catch (InterruptedException ie) {
			_log.error(
				"Latch opened prematurely by interruption. Dependence may " +
					"not be ready.");
		}

		doUpdateView(_addresses, addresses);

		_addresses = addresses;
	}

	protected abstract void doReceive(ChannelMessage channelMessage);

	protected void doUpdateView(
		Set<Address> oldAddresses, Set<Address> newAddresses) {
	}

	protected final String channelName;
	protected volatile ClusterChannel clusterChannel;
	protected final ClusterManagerImpl clusterManagerImpl;
	protected final ClusterNode localClusterNode;
	protected final String localClusterNodeId;

	private static final Log _log = LogFactoryUtil.getLog(
		ChannelReceiver.class);

	private volatile Set<Address> _addresses;
	private final CountDownLatch _countDownLatch = new CountDownLatch(1);

}