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

import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.concurrent.ConcurrentHashSet;

import java.io.Serializable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 * @author Edward Han
 * @author Shuyang Zhou
 */
public class MemoryPortalCache<K extends Serializable, V>
	implements PortalCache<K, V> {

	public MemoryPortalCache(String name, int initialCapacity) {
		_name = name;
		_concurrentHashMap = new ConcurrentHashMap<K, V>(initialCapacity);
	}

	public void destroy() {
		removeAll();

		_cacheListeners = null;
		_concurrentHashMap = null;
		_name = null;
	}

	@Override
	public V get(K key) {
		return _concurrentHashMap.get(key);
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public void put(K key, V value) {
		V oldValue = _concurrentHashMap.put(key, value);

		notifyPutEvents(key, value, oldValue != null);
	}

	@Override
	public void put(K key, V value, int timeToLive) {
		V oldValue = _concurrentHashMap.put(key, value);

		notifyPutEvents(key, value, oldValue != null);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		V oldValue = _concurrentHashMap.putIfAbsent(key, value);

		if (oldValue == null) {
			notifyPutEvents(key, value, false);
		}

		return oldValue;
	}

	@Override
	public V putIfAbsent(K key, V value, int timeToLive) {
		return putIfAbsent(key, value);
	}

	@Override
	public void putQuiet(K key, V value) {
		_concurrentHashMap.put(key, value);
	}

	@Override
	public void putQuiet(K key, V value, int timeToLive) {
		_concurrentHashMap.put(key, value);
	}

	@Override
	public void registerCacheListener(CacheListener<K, V> cacheListener) {
		_cacheListeners.add(cacheListener);
	}

	@Override
	public void registerCacheListener(
		CacheListener<K, V> cacheListener,
		CacheListenerScope cacheListenerScope) {

		registerCacheListener(cacheListener);
	}

	@Override
	public void remove(K key) {
		V value = _concurrentHashMap.remove(key);

		for (CacheListener<K, V> cacheListener : _cacheListeners) {
			cacheListener.notifyEntryRemoved(this, key, value);
		}
	}

	@Override
	public boolean remove(K key, V value) {
		boolean removed = _concurrentHashMap.remove(key, value);

		if (!removed) {
			return false;
		}

		for (CacheListener<K, V> cacheListener : _cacheListeners) {
			cacheListener.notifyEntryRemoved(this, key, value);
		}

		return true;
	}

	@Override
	public void removeAll() {
		_concurrentHashMap.clear();

		for (CacheListener<K, V> cacheListener : _cacheListeners) {
			cacheListener.notifyRemoveAll(this);
		}
	}

	@Override
	public V replace(K key, V value) {
		V oldValue = _concurrentHashMap.replace(key, value);

		if (oldValue == null) {
			return null;
		}

		notifyPutEvents(key, value, true);

		return oldValue;
	}

	@Override
	public V replace(K key, V value, int timeToLive) {
		return replace(key, value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		boolean replaced = _concurrentHashMap.replace(key, oldValue, newValue);

		if (!replaced) {
			return false;
		}

		notifyPutEvents(key, newValue, true);

		return true;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue, int timeToLive) {
		return replace(key, oldValue, newValue);
	}

	@Override
	public void unregisterCacheListener(CacheListener<K, V> cacheListener) {
		_cacheListeners.remove(cacheListener);
	}

	@Override
	public void unregisterCacheListeners() {
		_cacheListeners.clear();
	}

	protected void notifyPutEvents(K key, V value, boolean updated) {
		if (updated) {
			for (CacheListener<K, V> cacheListener : _cacheListeners) {
				cacheListener.notifyEntryUpdated(this, key, value);
			}
		}
		else {
			for (CacheListener<K, V> cacheListener : _cacheListeners) {
				cacheListener.notifyEntryPut(this, key, value);
			}
		}
	}

	private Set<CacheListener<K, V>> _cacheListeners =
		new ConcurrentHashSet<CacheListener<K, V>>();
	private ConcurrentHashMap<K, V> _concurrentHashMap;
	private String _name;

}