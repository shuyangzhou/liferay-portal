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

import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.ListenerFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.ClassLoaderUtil;

import java.io.Serializable;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

/**
 * @author Tina Tian
 */
public class EhcacheCacheListenerFactory
	<K extends Serializable, V extends Serializable>
		implements ListenerFactory<CacheListener<K, V>> {

	@Override
	public CacheListener<K, V> createListener(Properties properties) {
		if (properties == null) {
			throw new NullPointerException("Properties is null");
		}

		String factoryName = properties.getProperty(
			EhcacheConstants.FACTORY_NAME);

		if (Validator.isNull(factoryName)) {
			return null;
		}

		try {
			CacheEventListenerFactory cacheEventListenerFactory =
				_cacheEventListenerFactories.get(factoryName);

			if (cacheEventListenerFactory == null) {
				cacheEventListenerFactory =
					(CacheEventListenerFactory)InstanceFactory.newInstance(
						ClassLoaderUtil.getPortalClassLoader(), factoryName);

				_cacheEventListenerFactories.put(
					factoryName, cacheEventListenerFactory);
			}

			CacheEventListener cacheEventListener =
				cacheEventListenerFactory.createCacheEventListener(properties);

			return new EhcacheCacheListenerAdapter<K, V>(cacheEventListener);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return null;
	}

	private static Log _log = LogFactoryUtil.getLog(
		EhcacheCacheListenerFactory.class);

	private static Map<String, CacheEventListenerFactory>
		_cacheEventListenerFactories =
			new ConcurrentHashMap<String, CacheEventListenerFactory>();

}