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
			new FactoryConfiguration(factoryClassName, properties),
			cacheListenerScope);
	}

	public FactoryConfiguration getBootstrapLoaderConfiguration() {
		return _bootstrapLoaderConfiguration;
	}

	public Map<FactoryConfiguration, CacheListenerScope>
		getCacheListenerConfigurations() {

		return Collections.unmodifiableMap(_cacheListenerConfigurations);
	}

	public void removeCacheListenerConfiguration(
		FactoryConfiguration factoryConfiguration) {

		_cacheListenerConfigurations.remove(factoryConfiguration);
	}

	public void removeCacheListenerConfigurations() {
		_cacheListenerConfigurations.clear();
	}

	public void setBootstrapLoaderConfiguration(
		String factoryClassName, Properties properties) {

		_bootstrapLoaderConfiguration = new FactoryConfiguration(
			factoryClassName, properties);
	}

	private FactoryConfiguration _bootstrapLoaderConfiguration;
	private final Map<FactoryConfiguration, CacheListenerScope>
		_cacheListenerConfigurations =
			new HashMap<FactoryConfiguration, CacheListenerScope>();

}