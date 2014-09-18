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

package com.liferay.portal.kernel.cache.configuration;

import com.liferay.portal.kernel.cache.CacheListenerScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Tina Tian
 */
public class PortalCacheConfiguration {

	public void addCacheListenerConfiguration(
		String factoryClassName, Properties properties,
		CacheListenerScope cacheListenerScope) {

		_cacheListenerConfigurations.put(
			new ListenerConfiguration(factoryClassName, properties),
			cacheListenerScope);
	}

	public ListenerConfiguration getBootstrapLoaderConfiguration() {
		return _bootstrapLoaderConfiguration;
	}

	public Map<ListenerConfiguration, CacheListenerScope>
		getCacheListenerConfigurations() {

		return Collections.unmodifiableMap(_cacheListenerConfigurations);
	}

	public void removeCacheListenerConfiguration(
		ListenerConfiguration listenerConfiguration) {

		_cacheListenerConfigurations.remove(listenerConfiguration);
	}

	public void removeCacheListenerConfigurations() {
		_cacheListenerConfigurations.clear();
	}

	public void setBootstrapLoaderConfiguration(
		String factoryClassName, Properties properties) {

		_bootstrapLoaderConfiguration = new ListenerConfiguration(
			factoryClassName, properties);
	}

	private ListenerConfiguration _bootstrapLoaderConfiguration;
	private final Map<ListenerConfiguration, CacheListenerScope>
		_cacheListenerConfigurations =
			new HashMap<ListenerConfiguration, CacheListenerScope>();

}