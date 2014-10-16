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

package com.liferay.portal.kernel.cache;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class PortalCacheHelperUtil {

	public static <K extends Serializable, V> void put(
		PortalCache<K, V> portalCache, K key, V value, boolean skipReplicator) {

		put(
			portalCache, key, value, PortalCache.DEFAULT_TIME_TO_LIVE,
			skipReplicator);
	}

	public static <K extends Serializable, V> void put(
		PortalCache<K, V> portalCache, K key, V value, int timeToLive,
		boolean skipReplicator) {

		boolean remoteInvoke = AggregatedCacheListener.isRemoteInvoke();

		AggregatedCacheListener.setRemoteInvoke(skipReplicator);

		try {
			portalCache.put(key, value, timeToLive);
		}
		finally {
			AggregatedCacheListener.setRemoteInvoke(remoteInvoke);
		}
	}

	public static <K extends Serializable, V> void remove(
		PortalCache<K, V> portalCache, K key, boolean skipReplicator) {

		boolean remoteInvoke = AggregatedCacheListener.isRemoteInvoke();

		AggregatedCacheListener.setRemoteInvoke(skipReplicator);

		try {
			portalCache.remove(key);
		}
		finally {
			AggregatedCacheListener.setRemoteInvoke(remoteInvoke);
		}
	}

	public static <K extends Serializable, V> void removeAll(
		PortalCache<K, V> portalCache, boolean skipReplicator) {

		boolean remoteInvoke = AggregatedCacheListener.isRemoteInvoke();

		AggregatedCacheListener.setRemoteInvoke(skipReplicator);

		try {
			portalCache.removeAll();
		}
		finally {
			AggregatedCacheListener.setRemoteInvoke(remoteInvoke);
		}
	}

}