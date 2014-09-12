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

import com.liferay.portal.cache.cluster.ClusterLinkListenerFactory;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.configuration.ConfigurationManager;
import com.liferay.portal.kernel.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;
import java.io.StringReader;

import java.net.URL;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.BootstrapCacheLoaderFactoryConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheEventListenerFactoryConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.FactoryConfiguration;
import net.sf.ehcache.event.NotificationScope;

/**
 * @author Tina Tian
 */
public class EhcacheConfigurationManager
	implements ConfigurationManager<Configuration> {

	public EhcacheConfigurationManager(String configurationPath) {
		this(configurationPath, false);
	}

	public EhcacheConfigurationManager(
		String configurationPath, boolean clusterAware) {

		this(configurationPath, clusterAware, false);
	}

	public EhcacheConfigurationManager(
		String configurationPath, boolean clusterAware, boolean usingDefault) {

		if (configurationPath == null) {
			throw new NullPointerException("Configuration path is null");
		}

		URL configurationURL = EhcacheConfigurationManager.class.getResource(
			configurationPath);

		if (configurationURL == null) {
			throw new NullPointerException("Configuration URL is null");
		}

		_initialize(configurationURL, clusterAware, usingDefault);
	}

	public EhcacheConfigurationManager(URL configurationURL) {
		this(configurationURL, false);
	}

	public EhcacheConfigurationManager(
		URL configurationURL, boolean clusterAware) {

		this(configurationURL, clusterAware, false);
	}

	public EhcacheConfigurationManager(
		URL configurationURL, boolean clusterAware, boolean usingDefault) {

		if (configurationURL == null) {
			throw new NullPointerException("Configuration URL is null");
		}

		_initialize(configurationURL, clusterAware, usingDefault);
	}

	@Override
	public PortalCacheManagerConfiguration
		getPortalCacheManagerConfiguration() {

		return _portalCacheManagerConfiguration;
	}

	@Override
	public Configuration getVendorConfiguration() {
		return _ehcacheConfiguration;
	}

	private CacheListenerScope _getCacheListenerScope(
		NotificationScope notificationScope) {

		switch(notificationScope) {
			case ALL:
				return CacheListenerScope.ALL;
			case LOCAL:
				return CacheListenerScope.LOCAL;
			case REMOTE:
				return CacheListenerScope.REMOTE;
		}

		throw new IllegalArgumentException(
			"Unable to parse NotificationScope " + notificationScope);
	}

	private void _initialize(
		URL configurationURL, boolean clusterAware, boolean usingDefault) {

		Configuration configuration = ConfigurationFactory.parseConfiguration(
			configurationURL);

		List<?> peerProviderConfiguration =
			configuration.getCacheManagerPeerProviderFactoryConfiguration();

		if (!peerProviderConfiguration.isEmpty() &&
			(!clusterAware || !PropsValues.CLUSTER_LINK_ENABLED ||
			 PropsValues.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED)) {

			peerProviderConfiguration.clear();
		}

		peerProviderConfiguration =
			configuration.getCacheManagerPeerListenerFactoryConfigurations();

		if (!peerProviderConfiguration.isEmpty() &&
			(!clusterAware || !PropsValues.CLUSTER_LINK_ENABLED ||
			 PropsValues.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED)) {

			peerProviderConfiguration.clear();
		}

		FactoryConfiguration<?> factoryConfiguration =
			configuration.getCacheManagerEventListenerFactoryConfiguration();

		PortalCacheManagerConfiguration portalCacheManagerConfiguration =
			new PortalCacheManagerConfiguration();

		if (factoryConfiguration != null) {
			Properties properties = _parseProperties(
				factoryConfiguration.getProperties(),
				factoryConfiguration.getPropertySeparator());

			properties.put(
				EhcacheConstants.FACTORY_CLASS_NAME,
				factoryConfiguration.getFullyQualifiedClassPath());
			properties.put(
				EhcacheConstants.PORTAL_CACHE_MANAGER_NAME,
				configuration.getName());

			portalCacheManagerConfiguration.
				addCacheManagerListenerConfiguration(
					EhcacheListenerFactory.class.getName(), properties);
		}

		CacheConfiguration defaultCacheConfiguration =
			configuration.getDefaultCacheConfiguration();

		portalCacheManagerConfiguration.setDefaultPortalCacheConfiguration(
			_parseCacheConfiguration(
				defaultCacheConfiguration, clusterAware, usingDefault));

		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (Map.Entry<String, CacheConfiguration> entry :
				cacheConfigurations.entrySet()) {

			CacheConfiguration cacheConfiguration = entry.getValue();

			portalCacheManagerConfiguration.addPortalCacheConfiguration(
				entry.getKey(),
				_parseCacheConfiguration(
					cacheConfiguration, clusterAware, usingDefault));
		}

		_ehcacheConfiguration = configuration;
		_portalCacheManagerConfiguration = portalCacheManagerConfiguration;
	}

	private PortalCacheConfiguration _parseCacheConfiguration(
		CacheConfiguration cacheConfiguration, boolean clusterAware,
		boolean usingDefault) {

		PortalCacheConfiguration portalCacheConfiguration =
			new PortalCacheConfiguration();

		BootstrapCacheLoaderFactoryConfiguration
			bootstrapCacheLoaderFactoryConfiguration =
				cacheConfiguration.
					getBootstrapCacheLoaderFactoryConfiguration();

		if (bootstrapCacheLoaderFactoryConfiguration != null) {
			Properties properties = _parseProperties(
				bootstrapCacheLoaderFactoryConfiguration.getProperties(),
				bootstrapCacheLoaderFactoryConfiguration.
					getPropertySeparator());

			if (clusterAware && PropsValues.CLUSTER_LINK_ENABLED) {
				if (PropsValues.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED) {
					portalCacheConfiguration.setBootstrapLoaderConfiguration(
						ClusterLinkListenerFactory.class.getName(), properties);
				}
				else {
					properties.put(
						EhcacheConstants.FACTORY_CLASS_NAME,
						bootstrapCacheLoaderFactoryConfiguration.
							getFullyQualifiedClassPath());

					portalCacheConfiguration.setBootstrapLoaderConfiguration(
						EhcacheListenerFactory.class.getName(), properties);
				}
			}

			cacheConfiguration.addBootstrapCacheLoaderFactory(null);
		}

		List<CacheEventListenerFactoryConfiguration>
			cacheEventListenerConfigurations =
				cacheConfiguration.getCacheEventListenerConfigurations();

		for (CacheEventListenerFactoryConfiguration
				cacheEventListenerFactoryConfiguration :
					cacheEventListenerConfigurations) {

			String fullyQualifiedClassPath =
				cacheEventListenerFactoryConfiguration.
					getFullyQualifiedClassPath();

			Properties properties = _parseProperties(
				cacheEventListenerFactoryConfiguration.getProperties(),
				cacheEventListenerFactoryConfiguration. getPropertySeparator());

			CacheListenerScope cacheListenerScope = _getCacheListenerScope(
				cacheEventListenerFactoryConfiguration.getListenFor());

			if (fullyQualifiedClassPath.contains(
					"LiferayCacheEventListenerFactory") ||
				fullyQualifiedClassPath.contains(
					"net.sf.ehcache.distribution")) {

				if (clusterAware && PropsValues.CLUSTER_LINK_ENABLED) {
					if (PropsValues.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED) {
						portalCacheConfiguration.addCacheListenerConfiguration(
							ClusterLinkListenerFactory.class.getName(),
							properties, cacheListenerScope);
					}
					else {
						properties.put(
							EhcacheConstants.FACTORY_CLASS_NAME,
							cacheEventListenerFactoryConfiguration.
								getFullyQualifiedClassPath());

						portalCacheConfiguration.addCacheListenerConfiguration(
							EhcacheListenerFactory.class.getName(), properties,
							cacheListenerScope);
					}
				}
			}
			else if (!usingDefault) {
				properties.put(
					EhcacheConstants.FACTORY_CLASS_NAME,
					cacheEventListenerFactoryConfiguration.
						getFullyQualifiedClassPath());

				portalCacheConfiguration.addCacheListenerConfiguration(
					EhcacheListenerFactory.class.getName(), properties,
					cacheListenerScope);
			}
		}

		cacheEventListenerConfigurations.clear();

		return portalCacheConfiguration;
	}

	private Properties _parseProperties(
		String propertiesString, String propertySeparator) {

		Properties properties = new Properties();

		if (propertiesString == null) {
			return properties;
		}

		if (propertySeparator == null) {
			propertySeparator = StringPool.COMMA;
		}

		String propertyLines = propertiesString.trim();

		propertyLines = propertyLines.replaceAll(
			propertySeparator, StringPool.NEW_LINE);

		try {
			properties.load(new StringReader(propertyLines));
		}
		catch (IOException e) {
		}

		return properties;
	}

	private Configuration _ehcacheConfiguration;
	private PortalCacheManagerConfiguration
		_portalCacheManagerConfiguration;

}