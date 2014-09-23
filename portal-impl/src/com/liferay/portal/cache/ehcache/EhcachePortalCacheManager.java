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

import com.liferay.portal.cache.AbstractPortalCacheManager;
import com.liferay.portal.kernel.cache.BootstrapLoader;
import com.liferay.portal.kernel.cache.CacheManagerListener;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.configuration.ConfigurationParser;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.net.URL;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	extends AbstractPortalCacheManager<K, V> {

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
	public boolean registerCacheManagerListener(
		CacheManagerListener cacheManagerListener) {

		CacheManagerEventListenerRegistry cacheManagerEventListenerRegistry =
			_cacheManager.getCacheManagerEventListenerRegistry();

		return cacheManagerEventListenerRegistry.registerListener(
			new PortalCacheManagerEventListener(cacheManagerListener));
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

	@Override
	protected void doClearAll() {
		_cacheManager.clearAll();
	}

	@Override
	protected PortalCache<K, V> doCreatePortalCache(
		String cacheName, BootstrapLoader bootstrapLoader) {

		synchronized (_cacheManager) {
			if (!_cacheManager.cacheExists(cacheName)) {
				_cacheManager.addCache(cacheName);
			}
			else {
				Set<CacheManagerListener> cacheManagerListeners =
					getCacheManagerListeners();

				for (CacheManagerListener cacheManagerListener :
						cacheManagerListeners) {

					cacheManagerListener.notifyCacheAdded(cacheName);
				}
			}

			Cache cache = _cacheManager.getCache(cacheName);

			return new EhcachePortalCache<K, V>(this, cache, bootstrapLoader);
		}
	}

	@Override
	protected void doDestroy() {
		try {
			_cacheManager.shutdown();
		}
		finally {
			if (_managementService != null) {
				_managementService.dispose();
			}
		}
	}

	@Override
	protected void doRemoveCache(String cacheName) {
		_cacheManager.removeCache(cacheName);
	}

	@Override
	protected ConfigurationParser<?> getConfigurationParser(
		String configurationPath, boolean clusterAware, boolean usingDefault) {

		return new EhcacheConfigurationParser(
			configurationPath, clusterAware, usingDefault);
	}

	@Override
	protected ConfigurationParser<?> getConfigurationParser(
		URL configurationURL, boolean clusterAware, boolean usingDefault) {

		return new EhcacheConfigurationParser(
			configurationURL, clusterAware, usingDefault);
	}

	@Override
	protected String getDefaultConfigurationPath() {
		return _DEFAULT_CLUSTERED_EHCACHE_CONFIG_FILE;
	}

	@Override
	protected void initVendorManager(
		ConfigurationParser<?> configurationParser) {

		_cacheManager = CacheManagerUtil.createCacheManager(
			(Configuration)configurationParser.getVendorConfiguration());

		cacheManagerName = _cacheManager.getName();

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
				_cacheManager, mBeanServer, _registerCacheManager,
				_registerCaches, _registerCacheConfigurations,
				_registerCacheStatistics);

			_managementService.init();
		}
	}

	@Override
	protected void reconfigVendorCache(
		ConfigurationParser<?> configurationParser) {

		Configuration ehcacheConfiguration =
			(Configuration)configurationParser.getVendorConfiguration();

		if (!cacheManagerName.equals(ehcacheConfiguration.getName())) {
			return;
		}

		Map<String, CacheConfiguration> cacheConfigurations =
			ehcacheConfiguration.getCacheConfigurations();

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
			}
		}
	}

	private static final String _DEFAULT_CLUSTERED_EHCACHE_CONFIG_FILE =
		"/ehcache/liferay-multi-vm-clustered.xml";

	private static Log _log = LogFactoryUtil.getLog(
		EhcachePortalCacheManager.class);

	private CacheManager _cacheManager;
	private ManagementService _managementService;
	private boolean _registerCacheConfigurations = true;
	private boolean _registerCacheManager = true;
	private boolean _registerCaches = true;
	private boolean _registerCacheStatistics = true;

}