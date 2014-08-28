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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Tina Tian
 */
public class PortalCacheConfiguration {

	public void addCacheListenerConfiguration(
		String factoryClassName, Properties properties) {

		_cacheListenerConfigurations.put(
			factoryClassName,
			new ListenerConfiguration(factoryClassName, properties));
	}

	public ListenerConfiguration getBootstrapLoaderConfiguration() {
		return _bootstrapLoaderConfiguration;
	}

	public Collection<ListenerConfiguration> getCacheListenerConfigurations() {
		return Collections.unmodifiableCollection(
			_cacheListenerConfigurations.values());
	}

	public void removeCacheListenerConfiguration(String factoryClassName) {
		_cacheListenerConfigurations.remove(factoryClassName);
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
	private Map<String, ListenerConfiguration> _cacheListenerConfigurations =
		new HashMap<String, ListenerConfiguration>();

}