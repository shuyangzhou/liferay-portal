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

package com.liferay.portal.cache.ehcache.internal.configurator;

import com.liferay.portal.cache.PortalCacheReplicator;
import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.net.URL;

import java.util.Properties;
import java.util.Set;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.FactoryConfiguration;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Dante Wang
 */
@Component(
	immediate = true,
	service = MultiVMEhcachePortalCacheManagerConfigurator.class
)
public class MultiVMEhcachePortalCacheManagerConfigurator
	extends BaseEhcachePortalCacheManagerConfigurator {

	@Override
	@SuppressWarnings("rawtypes")
	public ObjectValuePair
		<Configuration, PortalCacheManagerConfiguration>
			getConfigurationObjectValuePair(
				String portalCacheManagerName, URL configurationURL,
				boolean usingDefault) {

		ObjectValuePair<Configuration, PortalCacheManagerConfiguration>
			objectValuePair = super.getConfigurationObjectValuePair(
				portalCacheManagerName, configurationURL, usingDefault);

		if (!_clusterEnabled) {
			return objectValuePair;
		}

		String name = ReleaseInfo.getName();

		if (!name.contains("Community")) {
			return objectValuePair;
		}

		Configuration configuration = objectValuePair.getKey();

		FactoryConfiguration peerProviderFactoryConfiguration =
			new FactoryConfiguration();

		peerProviderFactoryConfiguration.setClass(_peerProviderFactoryClass);
		peerProviderFactoryConfiguration.setProperties(
			_peerProviderFactoryPropertiesString);
		peerProviderFactoryConfiguration.setPropertySeparator(StringPool.COMMA);

		configuration.addCacheManagerPeerProviderFactory(
			peerProviderFactoryConfiguration);

		FactoryConfiguration peerListenerFacotryConfiguration =
			new FactoryConfiguration();

		peerListenerFacotryConfiguration.setClass(_peerListenerFactoryClass);
		peerListenerFacotryConfiguration.setProperties(
			_peerListenerFactoryPropertiesString);
		peerListenerFacotryConfiguration.setPropertySeparator(StringPool.COMMA);

		configuration.addCacheManagerPeerListenerFactory(
			peerListenerFacotryConfiguration);

		return objectValuePair;
	}

	@Activate
	protected void activate() {
		_bootstrapLoaderEnabled = GetterUtil.getBoolean(
			props.get(PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED));
		_bootstrapLoaderProperties = props.getProperties(
			PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
				StringPool.PERIOD,
			true);
		_clusterEnabled = GetterUtil.getBoolean(
			props.get(PropsKeys.CLUSTER_LINK_ENABLED));
		_defaultBootstrapLoaderPropertiesString = _getPortalPropertiesString(
			PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES_DEFAULT);
		_defaultReplicatorPropertiesString = _getPortalPropertiesString(
			PropsKeys.EHCACHE_CLUSTER_LINK_REPLICATOR_PROPERTIES_DEFAULT);
		_peerListenerFactoryClass = props.get(
			PropsKeys.EHCACHE_RMI_PEER_LISTENER_FACTORY_CLASS);
		_peerListenerFactoryPropertiesString = _getPortalPropertiesString(
			PropsKeys.EHCACHE_RMI_PEER_LISTENER_FACTORY_PROPERTIES);
		_peerProviderFactoryClass = props.get(
			PropsKeys.EHCACHE_RMI_PEER_PROVIDER_FACTORY_CLASS);
		_peerProviderFactoryPropertiesString = _getPortalPropertiesString(
			PropsKeys.EHCACHE_RMI_PEER_PROVIDER_FACTORY_PROPERTIES);
		_replicatorProperties = props.getProperties(
			PropsKeys.EHCACHE_CLUSTER_LINK_REPLICATOR_PROPERTIES +
				StringPool.PERIOD,
			true);
	}

	@Override
	protected boolean isRequireSerialization(
		CacheConfiguration cacheConfiguration) {

		if (_clusterEnabled) {
			return true;
		}

		return super.isRequireSerialization(cacheConfiguration);
	}

	@Override
	protected PortalCacheConfiguration parseCacheListenerConfigurations(
		CacheConfiguration cacheConfiguration, boolean usingDefault) {

		PortalCacheConfiguration portalCacheConfiguration =
			super.parseCacheListenerConfigurations(
				cacheConfiguration, usingDefault);

		if (!_clusterEnabled) {
			return portalCacheConfiguration;
		}

		String cacheName = cacheConfiguration.getName();

		if (_bootstrapLoaderEnabled) {
			String bootstrapLoaderPropertiesString =
				_bootstrapLoaderProperties.getProperty(cacheName);

			if (Validator.isNull(bootstrapLoaderPropertiesString)) {
				bootstrapLoaderPropertiesString =
					_defaultBootstrapLoaderPropertiesString;
			}

			portalCacheConfiguration.setPortalCacheBootstrapLoaderProperties(
				parseProperties(
					bootstrapLoaderPropertiesString, StringPool.COMMA));
		}

		String replicatorPropertiesString = _replicatorProperties.getProperty(
			cacheName);

		if (Validator.isNull(replicatorPropertiesString)) {
			replicatorPropertiesString = _defaultReplicatorPropertiesString;
		}

		Properties replicatorProperties = parseProperties(
			replicatorPropertiesString, StringPool.COMMA);

		replicatorProperties.put(PortalCacheReplicator.REPLICATOR, true);

		Set<Properties> portalCacheListenerPropertiesSet =
			portalCacheConfiguration.getPortalCacheListenerPropertiesSet();

		portalCacheListenerPropertiesSet.add(replicatorProperties);

		return portalCacheConfiguration;
	}

	@Reference(unbind = "-")
	protected void setProps(Props props) {
		this.props = props;
	}

	private String _getPortalPropertiesString(String portalPropertyKey) {
		String[] array = props.getArray(portalPropertyKey);

		if (array.length == 0) {
			return null;
		}

		if (array.length == 1) {
			return array[0];
		}

		StringBundler sb = new StringBundler(array.length * 2);

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			sb.append(StringPool.COMMA);
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private boolean _bootstrapLoaderEnabled;
	private Properties _bootstrapLoaderProperties;
	private boolean _clusterEnabled;
	private String _defaultBootstrapLoaderPropertiesString;
	private String _defaultReplicatorPropertiesString;
	private String _peerListenerFactoryClass;
	private String _peerListenerFactoryPropertiesString;
	private String _peerProviderFactoryClass;
	private String _peerProviderFactoryPropertiesString;
	private Properties _replicatorProperties;

}