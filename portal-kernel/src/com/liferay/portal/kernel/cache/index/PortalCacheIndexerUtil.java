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

package com.liferay.portal.kernel.cache.index;

import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterInvokeThreadLocal;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Preston Crary
 */
public class PortalCacheIndexerUtil {

	public static PortalCacheIndexer<?, ?, ?> getPortalCacheIndexer(
		String name) {

		return _portalCacheIndexers.get(name);
	}

	public static void registerPortalCacheIndexer(
		String name, PortalCacheIndexer<?, ?, ?> portalCacheIndexer) {

		PortalCacheIndexer<?, ?, ?> oldPortalCacheIndexer =
			_portalCacheIndexers.get(name);

		if (oldPortalCacheIndexer != null) {
			throw new IllegalArgumentException(
				"Duplicate portal cache indexer with name " + name);
		}

		_portalCacheIndexers.put(name, portalCacheIndexer);
	}

	public static void removeKeysFromCluster(String name, Object index) {
		if (!ClusterInvokeThreadLocal.isEnabled()) {
			return;
		}

		MethodHandler methodHandler = new MethodHandler(
			_removeKeysMethodKey, name, index);

		ClusterRequest clusterRequest = ClusterRequest.createMulticastRequest(
			methodHandler, true);

		clusterRequest.setFireAndForget(true);

		ClusterExecutorUtil.execute(clusterRequest);
	}

	@SuppressWarnings("unchecked")
	public static void removeKeysFromNode(String name, Object index) {
		PortalCacheIndexer portalCacheIndexer = _portalCacheIndexers.get(name);

		if (portalCacheIndexer != null) {
			portalCacheIndexer.removeKeysFromNode(index);
		}
	}

	public static PortalCacheIndexer<?, ?, ?> unregisterPortalCacheIndexer(
		String name) {

		return _portalCacheIndexers.remove(name);
	}

	private static final Map<String, PortalCacheIndexer<?, ?, ?>>
		_portalCacheIndexers = new ConcurrentHashMap<>();
	private static final MethodKey _removeKeysMethodKey = new MethodKey(
		PortalCacheIndexerUtil.class, "removeKeysFromNode", String.class,
		Object.class);

}