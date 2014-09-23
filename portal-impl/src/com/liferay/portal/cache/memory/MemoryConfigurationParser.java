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

import com.liferay.portal.cache.cluster.ClusterLinkCallbackFactory;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.configuration.ConfigurationParser;
import com.liferay.portal.kernel.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.util.PropsValues;

import java.util.Properties;

/**
 * @author Tina Tian
 */
public class MemoryConfigurationParser implements ConfigurationParser<Object> {

	public MemoryConfigurationParser(boolean clusterAware) {
		_portalCacheManagerConfiguration =
			new PortalCacheManagerConfiguration();

		if (clusterAware && PropsValues.CLUSTER_LINK_ENABLED) {
			PortalCacheConfiguration portalCacheConfiguration =
				new PortalCacheConfiguration();

			portalCacheConfiguration.addCacheListenerConfiguration(
				ClusterLinkCallbackFactory.class.getName(), new Properties(),
				CacheListenerScope.ALL);

			_portalCacheManagerConfiguration.setDefaultPortalCacheConfiguration(
				portalCacheConfiguration);
		}
	}

	@Override
	public PortalCacheManagerConfiguration
		getPortalCacheManagerConfiguration() {

		return _portalCacheManagerConfiguration;
	}

	@Override
	public Object getVendorConfiguration() {
		return null;
	}

	private final PortalCacheManagerConfiguration
		_portalCacheManagerConfiguration;

}