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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* @author Preston Crary
*/
public class TestPortalCache<K extends Serializable, V>
	implements PortalCache<K, V> {

	public TestPortalCache() {
	}

	public void dispose() {
		_cache.clear();

		_cacheListener.dispose();
	}

	public void evict(K key, V value) {
		_cache.remove(key);

		_cacheListener.notifyEntryEvicted(this, key, value, 0);
	}

	public void expire(K key, V value) {
		_cache.remove(key);

		_cacheListener.notifyEntryExpired(this, key, value, 0);
	}

	@Override
	public V get(K key) {
		return _cache.get(key);
	}

	@Override
	public List<K> getKeys() {
		return new ArrayList<>(_cache.keySet());
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public PortalCacheManager<K, V> getPortalCacheManager() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void put(K key, V value) {
		if (_cacheListener == null) {
			_cache.put(key, value);
		}
		else if (_cache.put(key, value) == null) {
			_cacheListener.notifyEntryPut(this, key, value, 0);
		}
		else {
			_cacheListener.notifyEntryUpdated(this, key, value, 0);
		}
	}

	@Override
	public void put(K key, V value, int timeToLive) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void registerCacheListener(CacheListener<K, V> cacheListener) {
		_cacheListener = cacheListener;
	}

	@Override
	public void registerCacheListener(
		CacheListener<K, V> cacheListener,
		CacheListenerScope cacheListenerScope) {

		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(K key) {
		V value = _cache.remove(key);

		_cacheListener.notifyEntryRemoved(this, key, value, 0);
	}

	@Override
	public void removeAll() {
		_cache.clear();

		_cacheListener.notifyRemoveAll(this);
	}

	@Override
	public void unregisterCacheListener(CacheListener<K, V> cacheListener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void unregisterCacheListeners() {
		throw new UnsupportedOperationException();
	}

	private final Map<K, V> _cache = new ConcurrentHashMap<>();
	private CacheListener<K, V> _cacheListener;

}