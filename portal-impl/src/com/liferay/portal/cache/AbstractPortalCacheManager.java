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

package com.liferay.portal.cache;

import com.liferay.portal.cache.transactional.TransactionalPortalCache;
import com.liferay.portal.kernel.cache.AggregatedCacheManagerListener;
import com.liferay.portal.kernel.cache.BlockingPortalCache;
import com.liferay.portal.kernel.cache.BootstrapLoader;
import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.CacheManagerListener;
import com.liferay.portal.kernel.cache.CallbackFactory;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheException;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheProvider;
import com.liferay.portal.kernel.cache.configuration.ConfigurationParser;
import com.liferay.portal.kernel.cache.configuration.FactoryConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.ClassLoaderUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.net.URL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;

/**
 * @author Tina Tian
 */
public abstract class AbstractPortalCacheManager<K extends Serializable, V>
	implements PortalCacheManager<K, V> {

	public void afterPropertiesSet() {
		if ((_portalCacheManagerConfiguration != null) ||
			(_mpiOnly && SPIUtil.isSPI())) {

			return;
		}

		String configurationPath = null;

		if (_configPropertyKey != null) {
			configurationPath = PropsUtil.get(_configPropertyKey);
		}

		if (Validator.isNull(configurationPath)) {
			configurationPath = getDefaultConfigurationPath();
		}

		_usingDefault = configurationPath.equals(getDefaultConfigurationPath());

		ConfigurationParser<?> configurationParser = getConfigurationParser(
			configurationPath, _clusterAware, _usingDefault);

		initVendorManager(configurationParser);

		initPortalCacheManager(configurationParser);
	}

	@Override
	public void clearAll() throws PortalCacheException {
		_portalCaches.clear();

		doClearAll();
	}

	@Override
	public void destroy() {
		PortalCacheProvider.unregisterPortalCacheManager(cacheManagerName);

		_portalCaches.clear();

		doDestroy();
	}

	@Override
	public PortalCache<K, V> getCache(String name) throws PortalCacheException {
		return getCache(name, false);
	}

	@Override
	public PortalCache<K, V> getCache(String name, boolean blocking)
		throws PortalCacheException {

		PortalCache<K, V> portalCache = _portalCaches.get(name);

		if (portalCache != null) {
			return portalCache;
		}

		synchronized (_portalCacheManagerConfiguration) {
			portalCache = _portalCaches.get(name);

			if (portalCache != null) {
				return portalCache;
			}

			PortalCacheConfiguration portalCacheConfiguration =
				_portalCacheManagerConfiguration.getPortalCacheConfiguration(
					name);

			if (portalCacheConfiguration == null) {
				portalCacheConfiguration =
					_portalCacheManagerConfiguration.
						getDefaultPortalCacheConfiguration();
			}

			portalCache = createPortalCache(
				name, blocking, portalCacheConfiguration);

			_portalCaches.put(name, portalCache);

			BootstrapLoader bootstrapLoader = portalCache.getBootstrapLoader();

			if (bootstrapLoader != null) {
				bootstrapLoader.load(cacheManagerName, name);
			}

			return portalCache;
		}
	}

	@Override
	public Set<CacheManagerListener> getCacheManagerListeners() {
		return aggregatedCacheManagerListener.getCacheManagerListeners();
	}

	@Override
	public String getName() {
		return cacheManagerName;
	}

	@Override
	public boolean isClusterAware() {
		return _clusterAware;
	}

	@Override
	public void reconfigureCaches(URL configurationURL) {
		ConfigurationParser<?> configurationParser = getConfigurationParser(
			configurationURL, _clusterAware, _usingDefault);

		reconfigVendorCache(configurationParser);

		reconfigPortalCache(configurationParser);
	}

	@Override
	public boolean registerCacheManagerListener(
		CacheManagerListener cacheManagerListener) {

		return aggregatedCacheManagerListener.addCacheListener(
			cacheManagerListener);
	}

	@Override
	public void removeCache(String name) {
		_portalCaches.remove(name);

		doRemoveCache(name);
	}

	public void setClusterAware(boolean clusterAware) {
		_clusterAware = clusterAware;
	}

	public void setConfigPropertyKey(String configPropertyKey) {
		_configPropertyKey = configPropertyKey;
	}

	public void setMBeanServer(MBeanServer mBeanServer) {
		this.mBeanServer = mBeanServer;
	}

	public void setMpiOnly(boolean mpiOnly) {
		_mpiOnly = mpiOnly;
	}

	@Override
	public boolean unregisterCacheManagerListener(
		CacheManagerListener cacheManagerListener) {

		return aggregatedCacheManagerListener.removeCacheListener(
			cacheManagerListener);
	}

	@Override
	public void unregisterCacheManagerListeners() {
		aggregatedCacheManagerListener.clearAll();
	}

	protected PortalCache<K, V> createPortalCache(
		String cacheName, boolean blocking,
		PortalCacheConfiguration portalCacheConfiguration) {

		BootstrapLoader bootstrapLoader = null;

		if (PropsValues.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED &&
			(portalCacheConfiguration != null)) {

			FactoryConfiguration factoryConfiguration =
				portalCacheConfiguration.getBootstrapLoaderConfiguration();

			if (factoryConfiguration != null) {
				CallbackFactory callbackFactory = _getCallbackFactory(
					factoryConfiguration.getFactoryClassName());

				bootstrapLoader = callbackFactory.createBootstrapLoader(
					factoryConfiguration.getProperties());
			}
		}

		PortalCache<K, V> portalCache = doCreatePortalCache(
			cacheName, bootstrapLoader);

		if (portalCacheConfiguration != null) {
			Map<FactoryConfiguration, CacheListenerScope>
				listenerConfigurations =
					portalCacheConfiguration.getCacheListenerConfigurations();

			for (Map.Entry<FactoryConfiguration, CacheListenerScope> entry :
					listenerConfigurations.entrySet()) {

				FactoryConfiguration factoryConfiguration = entry.getKey();

				CallbackFactory callbackFactory = _getCallbackFactory(
					factoryConfiguration.getFactoryClassName());

				CacheListener<K, V> cacheListener =
					(CacheListener<K, V>)callbackFactory.createCacheListener(
						factoryConfiguration.getProperties());

				portalCache.registerCacheListener(
					cacheListener, entry.getValue());
			}
		}

		if (PropsValues.TRANSACTIONAL_CACHE_ENABLED &&
			isTransactionalPortalCache(cacheName)) {

			portalCache = new TransactionalPortalCache<K, V>(portalCache);
		}

		if (PropsValues.EHCACHE_BLOCKING_CACHE_ALLOWED && blocking) {
			portalCache = new BlockingPortalCache<K, V>(portalCache);
		}

		return portalCache;
	}

	protected abstract void doClearAll();

	protected abstract PortalCache<K, V> doCreatePortalCache(
		String cacheName, BootstrapLoader bootstrapLoader);

	protected abstract void doDestroy();

	protected abstract void doRemoveCache(String cacheName);

	protected abstract ConfigurationParser<?> getConfigurationParser(
		String configurationPath, boolean clusterAware, boolean usingDefault);

	protected abstract ConfigurationParser<?> getConfigurationParser(
		URL configurationURL, boolean clusterAware, boolean usingDefault);

	protected abstract String getDefaultConfigurationPath();

	protected void initPortalCacheManager(
		ConfigurationParser<?> configurationParser) {

		_portalCacheManagerConfiguration =
			configurationParser.getPortalCacheManagerConfiguration();

		Collection<FactoryConfiguration> cacheManagerListenerConfigurations =
			_portalCacheManagerConfiguration.
				getCacheManagerListenerConfigurations();

		for (FactoryConfiguration factoryConfiguration :
				cacheManagerListenerConfigurations) {

			CallbackFactory callbackFactory = _getCallbackFactory(
				factoryConfiguration.getFactoryClassName());

			CacheManagerListener cacheManagerListener =
				callbackFactory.createCacheManagerListener(
					factoryConfiguration.getProperties());

			if (cacheManagerListener != null) {
				registerCacheManagerListener(cacheManagerListener);
			}
		}

		PortalCacheProvider.registerPortalCacheManager(this);
	}

	protected abstract void initVendorManager(
		ConfigurationParser<?> configurationParser);

	protected boolean isTransactionalPortalCache(String cacheName) {
		for (String namePattern : PropsValues.TRANSACTIONAL_CACHE_NAMES) {
			if (StringUtil.wildcardMatches(
					cacheName, namePattern, CharPool.QUESTION, CharPool.STAR,
					CharPool.PERCENT, true)) {

				return true;
			}
		}

		return false;
	}

	protected void reconfigPortalCache(
		ConfigurationParser<?> configurationParser) {

		_portalCacheManagerConfiguration =
			configurationParser.getPortalCacheManagerConfiguration();

		for (Map.Entry<String, PortalCacheConfiguration> configurationEntry :
				_portalCacheManagerConfiguration.
					getPortalCacheConfigurations().entrySet()) {

			String portalCacheName = configurationEntry.getKey();
			PortalCacheConfiguration portalCacheConfiguration =
				configurationEntry.getValue();

			PortalCache<K, V> portalCache = _portalCaches.get(portalCacheName);

			if (portalCache == null) {
				continue;
			}

			portalCache.unregisterCacheListeners();

			Map<FactoryConfiguration, CacheListenerScope>
				listenerConfigurations =
					portalCacheConfiguration.getCacheListenerConfigurations();

			for (Map.Entry<FactoryConfiguration, CacheListenerScope> entry :
					listenerConfigurations.entrySet()) {

				FactoryConfiguration factoryConfiguration = entry.getKey();

				CallbackFactory callbackFactory = _getCallbackFactory(
					factoryConfiguration.getFactoryClassName());

				CacheListener<K, V> cacheListener =
					(CacheListener<K, V>)callbackFactory.createCacheListener(
						factoryConfiguration.getProperties());

				if (cacheListener != null) {
					portalCache.registerCacheListener(
						cacheListener, entry.getValue());
				}
			}
		}
	}

	protected abstract void reconfigVendorCache(
		ConfigurationParser<?> configurationParser);

	protected String cacheManagerName;
	protected MBeanServer mBeanServer;

	private CallbackFactory _getCallbackFactory(String factoryClassName) {
		CallbackFactory callbackFactory = _callbackFactories.get(
			factoryClassName);

		if (callbackFactory != null) {
			return callbackFactory;
		}

		try {
			callbackFactory = (CallbackFactory)InstanceFactory.newInstance(
				ClassLoaderUtil.getPortalClassLoader(), factoryClassName);

			_callbackFactories.put(factoryClassName, callbackFactory);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return callbackFactory;
	}

	protected final AggregatedCacheManagerListener
		aggregatedCacheManagerListener = new AggregatedCacheManagerListener();

	private static Log _log = LogFactoryUtil.getLog(
		AbstractPortalCacheManager.class);

	private final Map<String, CallbackFactory> _callbackFactories =
		new ConcurrentHashMap<String, CallbackFactory>();
	private boolean _clusterAware;
	private String _configPropertyKey;
	private boolean _mpiOnly;
	private PortalCacheManagerConfiguration _portalCacheManagerConfiguration;
	private final Map<String, PortalCache<K, V>> _portalCaches =
		new HashMap<String, PortalCache<K, V>>();
	private boolean _usingDefault;

}