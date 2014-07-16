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

import com.liferay.portal.kernel.cache.AbstractPortalCache;
import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.NotificationScope;
import net.sf.ehcache.event.RegisteredEventListeners;

/**
 * @author Brian Wing Shun Chan
 * @author Edward Han
 * @author Shuyang Zhou
 */
public class EhcachePortalCache<K extends Serializable, V>
	extends AbstractPortalCache<K, V> {

	public EhcachePortalCache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}

	@Override
	public V get(K key) {
		Element element = ehcache.get(key);

		if (element == null) {
			return null;
		}
		else {
			return (V)element.getObjectValue();
		}
	}

	@Override
	public String getName() {
		return ehcache.getName();
	}

	@Override
	public void put(K key, V value, int timeToLive) {
		Element element = new Element(key, value);

		if (timeToLive >= 0) {
			element.setTimeToLive(timeToLive);
		}

		ehcache.put(element);
	}

	@Override
	public V putIfAbsent(K key, V value, int timeToLive) {
		Element element = new Element(key, value);

		if (timeToLive >= 0) {
			element.setTimeToLive(timeToLive);
		}

		Element oldElement = ehcache.putIfAbsent(element);

		if (oldElement == null) {
			return null;
		}

		return (V)oldElement.getObjectValue();
	}

	@Override
	public void putQuiet(K key, V value, int timeToLive) {
		Element element = new Element(key, value);

		if (timeToLive >= 0) {
			element.setTimeToLive(timeToLive);
		}

		ehcache.putQuiet(element);
	}

	@Override
	public void registerCacheListener(CacheListener<K, V> cacheListener) {
		registerCacheListener(cacheListener, CacheListenerScope.ALL);
	}

	@Override
	public void registerCacheListener(
		CacheListener<K, V> cacheListener,
		CacheListenerScope cacheListenerScope) {

		if (_cacheEventListeners.containsKey(cacheListener)) {
			return;
		}

		CacheEventListener cacheEventListener =
			new PortalCacheCacheEventListener<K, V>(cacheListener, this);

		NotificationScope notificationScope = getNotificationScope(
			cacheListenerScope);

		_cacheEventListeners.put(
			cacheListener,
			new RegistrationPair(cacheEventListener, notificationScope));

		RegisteredEventListeners registeredEventListeners =
			ehcache.getCacheEventNotificationService();

		registeredEventListeners.registerListener(
			cacheEventListener, notificationScope);
	}

	@Override
	public void remove(K key) {
		ehcache.remove(key);
	}

	@Override
	public boolean remove(K key, V value) {
		Element element = new Element(key, value);

		return ehcache.removeElement(element);
	}

	@Override
	public void removeAll() {
		ehcache.removeAll();
	}

	@Override
	public V replace(K key, V value, int timeToLive) {
		Element element = new Element(key, value);

		if (timeToLive >= 0) {
			element.setTimeToLive(timeToLive);
		}

		Element oldElement = ehcache.replace(element);

		if (oldElement == null) {
			return null;
		}

		return (V)oldElement.getObjectValue();
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue, int timeToLive) {
		Element oldElement = new Element(key, oldValue);
		Element newElement = new Element(key, newValue);

		if (timeToLive >= 0) {
			newElement.setTimeToLive(timeToLive);
		}

		return ehcache.replace(oldElement, newElement);
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;

		RegisteredEventListeners registeredEventListeners =
			ehcache.getCacheEventNotificationService();

		for (RegistrationPair registrationPair :
				_cacheEventListeners.values()) {

			registeredEventListeners.registerListener(
				registrationPair._cacheEventListener,
				registrationPair._notificationScope);
		}
	}

	@Override
	public void unregisterCacheListener(CacheListener<K, V> cacheListener) {
		RegistrationPair registrationPair = _cacheEventListeners.remove(
			cacheListener);

		if (registrationPair == null) {
			return;
		}

		RegisteredEventListeners registeredEventListeners =
			ehcache.getCacheEventNotificationService();

		registeredEventListeners.unregisterListener(
			registrationPair._cacheEventListener);
	}

	@Override
	public void unregisterCacheListeners() {
		RegisteredEventListeners registeredEventListeners =
			ehcache.getCacheEventNotificationService();

		for (RegistrationPair registrationPair :
				_cacheEventListeners.values()) {

			registeredEventListeners.unregisterListener(
				registrationPair._cacheEventListener);
		}

		_cacheEventListeners.clear();
	}

	protected NotificationScope getNotificationScope(
		CacheListenerScope cacheListenerScope) {

		if (cacheListenerScope.equals(CacheListenerScope.ALL)) {
			return NotificationScope.ALL;
		}
		else if (cacheListenerScope.equals(CacheListenerScope.LOCAL)) {
			return NotificationScope.LOCAL;
		}
		else {
			return NotificationScope.REMOTE;
		}
	}

	protected Ehcache ehcache;

	private Map<CacheListener<K, V>, RegistrationPair> _cacheEventListeners =
		new ConcurrentHashMap<CacheListener<K, V>, RegistrationPair>();

	private static class RegistrationPair {

		public RegistrationPair(
			CacheEventListener cacheEventListener,
			NotificationScope notificationScope) {

			_cacheEventListener = cacheEventListener;
			_notificationScope = notificationScope;
		}

		private CacheEventListener _cacheEventListener;
		private NotificationScope _notificationScope;

	}

}