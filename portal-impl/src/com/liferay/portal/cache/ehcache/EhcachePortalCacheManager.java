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

import com.liferay.portal.cache.transactional.TransactionalPortalCache;
import com.liferay.portal.kernel.cache.BlockingPortalCache;
import com.liferay.portal.kernel.cache.BootstrapLoader;
import com.liferay.portal.kernel.cache.CacheListener;
import com.liferay.portal.kernel.cache.CacheListenerScope;
import com.liferay.portal.kernel.cache.CacheManagerListener;
import com.liferay.portal.kernel.cache.ListenerFactory;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheProvider;
import com.liferay.portal.kernel.cache.PortalCacheWrapper;
import com.liferay.portal.kernel.cache.configuration.ConfigurationManager;
import com.liferay.portal.kernel.cache.configuration.ListenerConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.kernel.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.ClassLoaderUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.net.URL;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.MBeanServer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.event.CacheManagerEventListener;
import net.sf.ehcache.event.CacheManagerEventListenerRegistry;
import net.sf.ehcache.management.ManagementService;
import net.sf.ehcache.util.FailSafeTimer;

/**
 * @author Joseph Shum
 * @author Raymond Augé
 * @author Michael C. Han
 * @author Shuyang Zhou
 * @author Edward Han
 */
public class EhcachePortalCacheManager<K extends Serializable, V>
	implements PortalCacheManager<K, V> {

	public void afterPropertiesSet() {
		if ((_cacheManager != null) || (_mpiOnly && SPIUtil.isSPI())) {
			return;
		}

		String configurationPath = PropsUtil.get(_configPropertyKey);

		if (Validator.isNull(configurationPath)) {
			configurationPath = _DEFAULT_CLUSTERED_EHCACHE_CONFIG_FILE;
		}

		_usingDefault = configurationPath.equals(
			_DEFAULT_CLUSTERED_EHCACHE_CONFIG_FILE);

		ConfigurationManager<Configuration> ehcacheConfigurationManager =
			new EhcacheConfigurationManager(
				configurationPath, _clusterAware, _usingDefault);

		initEhcacheManager(
			ehcacheConfigurationManager.getVendorConfiguration());

		initPortalCacheManager(
			ehcacheConfigurationManager.getPortalCacheManagerConfiguration());
	}

	@Override
	public void clearAll() {
		_cacheManager.clearAll();
	}

	@Override
	public void destroy() {
		try {
			PortalCacheProvider.unregisterPortalCacheManager(
				_cacheManager.getName());

			_portalCaches.clear();

			_cacheManager.shutdown();
		}
		finally {
			if (_managementService != null) {
				_managementService.dispose();
			}
		}
	}

	@Override
	public PortalCache<K, V> getCache(String name) {
		return getCache(name, false);
	}

	@Override
	public PortalCache<K, V> getCache(String name, boolean blocking) {
		PortalCache<K, V> portalCache = _portalCaches.get(name);

		if (portalCache != null) {
			return portalCache;
		}

		synchronized (_cacheManager) {
			portalCache = _portalCaches.get(name);

			if (portalCache != null) {
				return portalCache;
			}

			if (!_cacheManager.cacheExists(name)) {
				_cacheManager.addCache(name);
			}
			else {
				Set<CacheManagerListener> cacheManagerListeners =
					getCacheManagerListeners();

				for (CacheManagerListener cacheManagerListener :
						cacheManagerListeners) {

					cacheManagerListener.notifyCacheAdded(name);
				}
			}

			Cache cache = _cacheManager.getCache(name);

			PortalCacheConfiguration portalCacheConfiguration =
				_portalCacheManagerConfiguration.getPortalCacheConfiguration(
					name);

			if (portalCacheConfiguration == null) {
				portalCacheConfiguration =
					_portalCacheManagerConfiguration.
						getDefaultPortalCacheConfiguration();
			}

			portalCache = createPortalCache(
				cache, blocking, portalCacheConfiguration);

			_portalCaches.put(name, portalCache);

			return portalCache;
		}
	}

	@Override
	public Set<CacheManagerListener> getCacheManagerListeners() {
		Set<CacheManagerListener> cacheManagerListeners =
			new HashSet<CacheManagerListener>();

		CacheManagerEventListenerRegistry cacheManagerEventListenerRegistry =
			_cacheManager.getCacheManagerEventListenerRegistry();

		Set<CacheManagerEventListener> cacheManagerEventListeners =
			cacheManagerEventListenerRegistry.getRegisteredListeners();

		for (CacheManagerEventListener cacheManagerEventListener :
				cacheManagerEventListeners) {

			if (!(cacheManagerEventListener instanceof
					PortalCacheManagerEventListener)) {

				continue;
			}

			PortalCacheManagerEventListener portalCacheManagerEventListener =
				(PortalCacheManagerEventListener)cacheManagerEventListener;

			cacheManagerListeners.add(
				portalCacheManagerEventListener.getCacheManagerListener());
		}

		return cacheManagerListeners;
	}

	public CacheManager getEhcacheManager() {
		return _cacheManager;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public boolean isClusterAware() {
		return _clusterAware;
	}

	@Override
	public void reconfigureCaches(URL configurationURL) {
		ConfigurationManager<Configuration> ehcacheConfigurationManager =
			new EhcacheConfigurationManager(
				configurationURL, _clusterAware, _usingDefault);

		Configuration configuration =
			ehcacheConfigurationManager.getVendorConfiguration();

		if (!_name.equals(configuration.getName())) {
			return;
		}

		reconfigEhcache(configuration);

		reconfigPortalCache(
			ehcacheConfigurationManager.getPortalCacheManagerConfiguration());
	}

	@Override
	public boolean registerCacheManagerListener(
		CacheManagerListener cacheManagerListener) {

		CacheManagerEventListenerRegistry cacheManagerEventListenerRegistry =
			_cacheManager.getCacheManagerEventListenerRegistry();

		return cacheManagerEventListenerRegistry.registerListener(
			new PortalCacheManagerEventListener(cacheManagerListener));
	}

	@Override
	public void removeCache(String name) {
		_cacheManager.removeCache(name);
		_portalCaches.remove(name);
	}

	public void setClusterAware(boolean clusterAware) {
		_clusterAware = clusterAware;
	}

	public void setConfigPropertyKey(String configPropertyKey) {
		_configPropertyKey = configPropertyKey;
	}

	public void setMBeanServer(MBeanServer mBeanServer) {
		_mBeanServer = mBeanServer;
	}

	public void setMpiOnly(boolean mpiOnly) {
		_mpiOnly = mpiOnly;
	}

	public void setRegisterCacheConfigurations(
		boolean registerCacheConfigurations) {

		_registerCacheConfigurations = registerCacheConfigurations;
	}

	public void setRegisterCacheManager(boolean registerCacheManager) {
		_registerCacheManager = registerCacheManager;
	}

	public void setRegisterCaches(boolean registerCaches) {
		_registerCaches = registerCaches;
	}

	public void setRegisterCacheStatistics(boolean registerCacheStatistics) {
		_registerCacheStatistics = registerCacheStatistics;
	}

	@Override
	public boolean unregisterCacheManagerListener(
		CacheManagerListener cacheManagerListener) {

		CacheManagerEventListenerRegistry cacheManagerEventListenerRegistry =
			_cacheManager.getCacheManagerEventListenerRegistry();

		return cacheManagerEventListenerRegistry.unregisterListener(
			new PortalCacheManagerEventListener(cacheManagerListener));
	}

	@Override
	public void unregisterCacheManagerListeners() {
		CacheManagerEventListenerRegistry cacheManagerEventListenerRegistry =
			_cacheManager.getCacheManagerEventListenerRegistry();

		Set<CacheManagerEventListener> cacheManagerEventListeners =
			cacheManagerEventListenerRegistry.getRegisteredListeners();

		for (CacheManagerEventListener cacheManagerEventListener :
				cacheManagerEventListeners) {

			if (!(cacheManagerEventListener instanceof
					PortalCacheManagerEventListener)) {

				continue;
			}

			cacheManagerEventListenerRegistry.unregisterListener(
				cacheManagerEventListener);
		}
	}

	protected PortalCache<K, V> createPortalCache(
		Cache cache, boolean blocking,
		PortalCacheConfiguration portalCacheConfiguration) {

		String cacheName = cache.getName();

		BootstrapLoader bootstrapLoader = null;

		if (PropsValues.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED) {
			ListenerConfiguration listenerConfiguration =
				portalCacheConfiguration.getBootstrapLoaderConfiguration();

			if (listenerConfiguration != null) {
				ListenerFactory listenerFactory = _getListenerFactory(
					listenerConfiguration.getFactoryClassName());

				bootstrapLoader = listenerFactory.createBootstrapLoader(
					listenerConfiguration.getProperties());
			}
		}

		PortalCache<K, V> portalCache = new EhcachePortalCache<K, V>(
			this, cache, bootstrapLoader);

		Map<ListenerConfiguration, CacheListenerScope> listenerConfigurations =
			portalCacheConfiguration.getCacheListenerConfigurations();

		for (Map.Entry<ListenerConfiguration, CacheListenerScope> entry :
				listenerConfigurations.entrySet()) {

			ListenerConfiguration listenerConfiguration = entry.getKey();

			ListenerFactory listenerFactory = _getListenerFactory(
				listenerConfiguration.getFactoryClassName());

			CacheListener<K, V> cacheListener =
				(CacheListener<K, V>)listenerFactory.createCacheListener(
					listenerConfiguration.getProperties());

			portalCache.registerCacheListener(cacheListener, entry.getValue());
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

	protected void initEhcacheManager(Configuration configuration) {
		_cacheManager = CacheManagerUtil.createCacheManager(configuration);

		_name = _cacheManager.getName();

		FailSafeTimer failSafeTimer = _cacheManager.getTimer();

		failSafeTimer.cancel();

		try {
			Field cacheManagerTimerField = ReflectionUtil.getDeclaredField(
				CacheManager.class, "cacheManagerTimer");

			cacheManagerTimerField.set(_cacheManager, null);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (PropsValues.EHCACHE_PORTAL_CACHE_MANAGER_JMX_ENABLED) {
			_managementService = new ManagementService(
				_cacheManager, _mBeanServer, _registerCacheManager,
				_registerCaches, _registerCacheConfigurations,
				_registerCacheStatistics);

			_managementService.init();
		}
	}

	protected void initPortalCacheManager(
		PortalCacheManagerConfiguration portalCacheManagerConfiguration) {

		_portalCacheManagerConfiguration = portalCacheManagerConfiguration;

		Collection<ListenerConfiguration> cacheManagerListenerConfigurations =
			portalCacheManagerConfiguration.
				getCacheManagerListenerConfigurations();

		for (ListenerConfiguration listenerConfiguration :
				cacheManagerListenerConfigurations) {

			ListenerFactory listenerFactory = _getListenerFactory(
				listenerConfiguration.getFactoryClassName());

			CacheManagerListener cacheManagerListener =
				listenerFactory.createCacheManagerListener(
					listenerConfiguration.getProperties());

			if (cacheManagerListener != null) {
				registerCacheManagerListener(cacheManagerListener);
			}
		}

		PortalCacheProvider.registerPortalCacheManager(this);

		if (!PropsValues.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED) {
			return;
		}

		Map<String, PortalCacheConfiguration> portalCacheConfigurations =
			portalCacheManagerConfiguration.getPortalCacheConfigurations();

		for (String portalCacheName : portalCacheConfigurations.keySet()) {
			PortalCache<K, V> portalCache = getCache(portalCacheName);

			BootstrapLoader bootstrapLoader = portalCache.getBootstrapLoader();

			if (bootstrapLoader != null) {
				bootstrapLoader.load(_cacheManager.getName(), portalCacheName);
			}
		}
	}

	protected boolean isTransactionalPortalCache(String name) {
		for (String namePattern : PropsValues.TRANSACTIONAL_CACHE_NAMES) {
			if (StringUtil.wildcardMatches(
					name, namePattern, CharPool.QUESTION, CharPool.STAR,
					CharPool.PERCENT, true)) {

				return true;
			}
		}

		return false;
	}

	protected void reconfigEhcache(Configuration configuration) {
		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (CacheConfiguration cacheConfiguration :
				cacheConfigurations.values()) {

			String portalCacheName = cacheConfiguration.getName();

			synchronized (_cacheManager) {
				if (_cacheManager.cacheExists(portalCacheName)) {
					if (_log.isInfoEnabled()) {
						_log.info(
							"Overriding existing cache " + portalCacheName);
					}

					_cacheManager.removeCache(portalCacheName);
				}

				Cache cache = new Cache(cacheConfiguration);

				_cacheManager.addCache(cache);

				EhcachePortalCache<K, V> ehcachePortalCache =
					getEhcachePortalCache(_portalCaches.get(portalCacheName));

				if (ehcachePortalCache != null) {
					ehcachePortalCache.setEhcache(cache);
				}
			}
		}
	}

	protected void reconfigPortalCache(
		PortalCacheManagerConfiguration portalCacheManagerConfiguration) {

		_portalCacheManagerConfiguration = portalCacheManagerConfiguration;

		for (Map.Entry<String, PortalCacheConfiguration> configurationEntry :
				portalCacheManagerConfiguration.
					getPortalCacheConfigurations().entrySet()) {

			String portalCacheName = configurationEntry.getKey();
			PortalCacheConfiguration portalCacheConfiguration =
				configurationEntry.getValue();

			PortalCache<K, V> portalCache = _portalCaches.get(portalCacheName);

			if (portalCache == null) {
				continue;
			}

			portalCache.unregisterCacheListeners();

			Map<ListenerConfiguration, CacheListenerScope>
				listenerConfigurations =
					portalCacheConfiguration.getCacheListenerConfigurations();

			for (Map.Entry<ListenerConfiguration, CacheListenerScope> entry :
					listenerConfigurations.entrySet()) {

				ListenerConfiguration listenerConfiguration = entry.getKey();

				ListenerFactory listenerFactory = _getListenerFactory(
					listenerConfiguration.getFactoryClassName());

				CacheListener<K, V> cacheListener =
					(CacheListener<K, V>)listenerFactory.createCacheListener(
						listenerConfiguration.getProperties());

				if (cacheListener != null) {
					portalCache.registerCacheListener(
						cacheListener, entry.getValue());
				}
			}
		}
	}

	private ListenerFactory _getListenerFactory(String factoryClassName) {
		ListenerFactory listenerFactory = _listenerFactories.get(
			factoryClassName);

		if (listenerFactory != null) {
			return listenerFactory;
		}

		try {
			listenerFactory = (ListenerFactory)InstanceFactory.newInstance(
				ClassLoaderUtil.getPortalClassLoader(), factoryClassName);

			_listenerFactories.put(factoryClassName, listenerFactory);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return listenerFactory;
	}

	private EhcachePortalCache<K, V> getEhcachePortalCache(
		PortalCache<K, V> portalCache) {

		while (portalCache instanceof PortalCacheWrapper) {
			PortalCacheWrapper<K, V> portalCacheWrapper =
				(PortalCacheWrapper<K, V>)portalCache;

			portalCache = portalCacheWrapper.getWrappedPortalCache();
		}

		if (portalCache instanceof EhcachePortalCache) {
			return (EhcachePortalCache<K, V>)portalCache;
		}

		return null;
	}

	private static final String _DEFAULT_CLUSTERED_EHCACHE_CONFIG_FILE =
		"/ehcache/liferay-multi-vm-clustered.xml";

	private static Log _log = LogFactoryUtil.getLog(
		EhcachePortalCacheManager.class);

	private CacheManager _cacheManager;
	private boolean _clusterAware;
	private String _configPropertyKey;
	private Map<String, ListenerFactory> _listenerFactories =
		new ConcurrentHashMap<String, ListenerFactory>();
	private ManagementService _managementService;
	private MBeanServer _mBeanServer;
	private boolean _mpiOnly;
	private String _name;
	private PortalCacheManagerConfiguration _portalCacheManagerConfiguration;
	private Map<String, PortalCache<K, V>> _portalCaches =
		new HashMap<String, PortalCache<K, V>>();
	private boolean _registerCacheConfigurations = true;
	private boolean _registerCacheManager = true;
	private boolean _registerCaches = true;
	private boolean _registerCacheStatistics = true;
	private boolean _usingDefault;

}