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

package com.liferay.portal.cache.ehcache;

import com.liferay.portal.cache.cluster.ClusterReplicationThreadLocal;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheWrapper;
import com.liferay.portal.model.MVCCModel;

import java.io.Serializable;

/**
 * @author Shuyang Zhou
 */
public class MVCCEhcachePortalCache<K extends Serializable, V extends MVCCModel>
	extends PortalCacheWrapper<K, V> {

	public MVCCEhcachePortalCache(PortalCache<K, V> portalCache) {
		super(portalCache);
	}

	@Override
	public void put(K key, V value) {
		doPut(key, value, false, -1);
	}

	@Override
	public void put(K key, V value, int timeToLive) {
		doPut(key, value, false, timeToLive);
	}

	@Override
	public void putQuiet(K key, V value) {
		doPut(key, value, true, -1);
	}

	@Override
	public void putQuiet(K key, V value, int timeToLive) {
		doPut(key, value, true, timeToLive);
	}

	protected void doPut(K key, V value, boolean quiet, int timeToLive) {
		boolean replicate = false;

		if (quiet) {
			replicate = ClusterReplicationThreadLocal.isReplicate();

			ClusterReplicationThreadLocal.setReplicate(false);
		}

		try {
			while (true) {
				V oldValue = portalCache.get(key);

				if (oldValue == null) {
					if (timeToLive >= 0) {
						oldValue = portalCache.putIfAbsent(
							key, value, timeToLive);
					}
					else {
						oldValue = portalCache.putIfAbsent(key, value);
					}

					if (oldValue == null) {
						return;
					}
				}

				if (value.getMvccVersion() <= oldValue.getMvccVersion()) {
					return;
				}

				boolean replaced;

				if (timeToLive >= 0) {
					replaced = portalCache.replace(
						key, oldValue, value, timeToLive);
				}
				else {
					replaced = portalCache.replace(key, oldValue, value);
				}

				if (replaced) {
					return;
				}
			}
		}
		finally {
			if (quiet) {
				ClusterReplicationThreadLocal.setReplicate(replicate);
			}
		}
	}

}