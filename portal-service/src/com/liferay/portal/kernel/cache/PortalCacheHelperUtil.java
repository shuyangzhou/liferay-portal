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

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			portalCache.put(key, value, timeToLive);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> V putIfAbsent(
		LowLevelCache<K, V> lowLevelCache, K key, V value,
		boolean skipReplicator) {

		return putIfAbsent(
			lowLevelCache, key, value, PortalCache.DEFAULT_TIME_TO_LIVE,
			skipReplicator);
	}

	public static <K extends Serializable, V> V putIfAbsent(
		LowLevelCache<K, V> lowLevelCache, K key, V value, int timeToLive,
		boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			return lowLevelCache.putIfAbsent(key, value, timeToLive);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> boolean remove(
		LowLevelCache<K, V> lowLevelCache, K key, V value,
		boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			return lowLevelCache.remove(key, value);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> void remove(
		PortalCache<K, V> portalCache, K key, boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			portalCache.remove(key);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> void removeAll(
		PortalCache<K, V> portalCache, boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			portalCache.removeAll();
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> V replace(
		LowLevelCache<K, V> lowLevelCache, K key, V value,
		boolean skipReplicator) {

		return replace(
			lowLevelCache, key, value, PortalCache.DEFAULT_TIME_TO_LIVE,
			skipReplicator);
	}

	public static <K extends Serializable, V> V replace(
		LowLevelCache<K, V> lowLevelCache, K key, V value, int timeToLive,
		boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			return lowLevelCache.replace(key, value, timeToLive);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

	public static <K extends Serializable, V> boolean replace(
		LowLevelCache<K, V> lowLevelCache, K key, V oldValue, V newValue,
		boolean skipReplicator) {

		return replace(
			lowLevelCache, key, oldValue, newValue,
			PortalCache.DEFAULT_TIME_TO_LIVE, skipReplicator);
	}

	public static <K extends Serializable, V> boolean replace(
		LowLevelCache<K, V> lowLevelCache, K key, V oldValue, V newValue,
		int timeToLive, boolean skipReplicator) {

		boolean remote = AggregatedCacheListener.getRemoteInvokeThreadLocal();

		AggregatedCacheListener.setRemoteInvokeThreadLocal(skipReplicator);

		try {
			return lowLevelCache.replace(key, oldValue, newValue, timeToLive);
		}
		finally {
			AggregatedCacheListener.setRemoteInvokeThreadLocal(remote);
		}
	}

}