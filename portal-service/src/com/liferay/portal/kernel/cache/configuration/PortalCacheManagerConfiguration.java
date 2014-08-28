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
public class PortalCacheManagerConfiguration {

	public void addCacheManagerListenerConfiguration(
		String factoryClassName, Properties properties) {

		_cacheManagerListenerConfigurations.put(
			factoryClassName,
			new ListenerConfiguration(factoryClassName, properties));
	}

	public void addPortalCacheConfiguration(
		String portalCacheName,
		PortalCacheConfiguration portalCacheConfiguration) {

		_portalCacheConfigurations.put(
			portalCacheName, portalCacheConfiguration);
	}

	public Collection<ListenerConfiguration>
		getCacheManagerListenerConfigurations() {

		return Collections.unmodifiableCollection(
			_cacheManagerListenerConfigurations.values());
	}

	public PortalCacheConfiguration getDefaultPortalCacheConfiguration() {
		return _portalCacheConfigurations.get(_DEFAULT_PORTAL_CACHE_NAME);
	}

	public PortalCacheConfiguration getPortalCacheConfiguration(
		String portalCacheName) {

		return _portalCacheConfigurations.get(portalCacheName);
	}

	public Map<String, PortalCacheConfiguration>
		getPortalCacheConfigurations() {

		return Collections.unmodifiableMap(_portalCacheConfigurations);
	}

	public void removeCacheManagerListenerConfiguration(
		String factoryClassName) {

		_cacheManagerListenerConfigurations.remove(factoryClassName);
	}

	public void removeCacheManagerListenerConfigurations() {
		_cacheManagerListenerConfigurations.clear();
	}

	public void removePortalCacheConfiguration(String portalCacheName) {
		_portalCacheConfigurations.remove(portalCacheName);
	}

	public void removePortalCacheConfigurations() {
		_portalCacheConfigurations.clear();
	}

	public void setDefaultPortalCacheConfiguration(
		PortalCacheConfiguration portalCacheConfiguration) {

		_portalCacheConfigurations.put(
			_DEFAULT_PORTAL_CACHE_NAME, portalCacheConfiguration);
	}

	private static final String _DEFAULT_PORTAL_CACHE_NAME =
		"DEFAULT_PORTAL_CACHE_NAME";

	private Map<String, ListenerConfiguration>
		_cacheManagerListenerConfigurations =
			new HashMap<String, ListenerConfiguration>();
	private Map<String, PortalCacheConfiguration> _portalCacheConfigurations =
		new HashMap<String, PortalCacheConfiguration>();

}