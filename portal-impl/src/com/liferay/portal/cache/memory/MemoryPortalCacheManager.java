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

package com.liferay.portal.cache.memory;

import com.liferay.portal.cache.AbstractPortalCacheManager;
import com.liferay.portal.kernel.cache.BootstrapLoader;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.configuration.ConfigurationParser;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Serializable;

import java.net.URL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 */
public class MemoryPortalCacheManager<K extends Serializable, V>
	extends AbstractPortalCacheManager<K, V> {

	public void setCacheInitialCapacity(int cacheInitialCapacity) {
		_cacheInitialCapacity = cacheInitialCapacity;
	}

	public void setCacheManagerInitialCapacity(
		int cacheManagerInitialCapacity) {

		_cacheManagerInitialCapacity = cacheManagerInitialCapacity;
	}

	public void setName(String name) {
		cacheManagerName = name;
	}

	@Override
	protected void doClearAll() {
		for (MemoryPortalCache<K, V> memoryPortalCache :
				_memoryPortalCaches.values()) {

			memoryPortalCache.removeAll();
		}
	}

	@Override
	protected PortalCache<K, V> doCreatePortalCache(
		String cacheName, BootstrapLoader bootstrapLoader) {

		MemoryPortalCache<K, V> portalCache = _memoryPortalCaches.get(
			cacheName);

		if (portalCache == null) {
			portalCache = new MemoryPortalCache<K, V>(
				this, cacheName, _cacheInitialCapacity);

			_memoryPortalCaches.put(cacheName, portalCache);

			aggregatedCacheManagerListener.notifyCacheAdded(cacheName);
		}

		return portalCache;
	}

	@Override
	protected void doDestroy() {
		for (MemoryPortalCache<K, V> memoryPortalCache :
				_memoryPortalCaches.values()) {

			memoryPortalCache.destroy();
		}

		aggregatedCacheManagerListener.dispose();
	}

	@Override
	protected void doRemoveCache(String cacheName) {
		MemoryPortalCache<K, V> memoryPortalCache = _memoryPortalCaches.remove(
			cacheName);

		memoryPortalCache.destroy();

		aggregatedCacheManagerListener.notifyCacheRemoved(cacheName);
	}

	@Override
	protected ConfigurationParser<?> getConfigurationParser(
		String configurationPath, boolean clusterAware, boolean usingDefault) {

		return new MemoryConfigurationParser(clusterAware);
	}

	@Override
	protected ConfigurationParser<?> getConfigurationParser(
		URL configurationURL, boolean clusterAware, boolean usingDefault) {

		return new MemoryConfigurationParser(clusterAware);
	}

	@Override
	protected String getDefaultConfigurationPath() {
		return StringPool.BLANK;
	}

	@Override
	protected void initVendorManager(
		ConfigurationParser<?> configurationParser) {

		if (cacheManagerName == null) {
			throw new NullPointerException("Name is null");
		}

		_memoryPortalCaches =
			new ConcurrentHashMap<String, MemoryPortalCache<K, V>>(
				_cacheManagerInitialCapacity);

		aggregatedCacheManagerListener.init();
	}

	@Override
	protected void reconfigVendorCache(
		ConfigurationParser<?> configurationParser) {
	}

	private int _cacheInitialCapacity = 10000;
	private int _cacheManagerInitialCapacity = 10000;
	private Map<String, MemoryPortalCache<K, V>> _memoryPortalCaches;

}