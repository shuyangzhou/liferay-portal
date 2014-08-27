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

package com.liferay.portal.cache.bootstrap;

import com.liferay.portal.kernel.cache.BootstrapLoader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InitialThreadLocal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Tina Tian
 */
public class StreamBootstrapLoader implements BootstrapLoader {

	public static void resetSkip() {
		_skipBootstrapThreadLocal.remove();
	}

	public static void setSkip() {
		_skipBootstrapThreadLocal.set(Boolean.TRUE);
	}

	public static synchronized void start() {
		if (!_started) {
			_started = true;
		}

		if (_deferredPortalCaches.isEmpty()) {
			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Loading deferred caches");
		}

		try {
			for (String portalCacheManagerName :
					_deferredPortalCaches.keySet()) {

				List<String> portalCacheNames = _deferredPortalCaches.get(
					portalCacheManagerName);

				if (portalCacheNames.isEmpty()) {
					continue;
				}

				StreamBootstrapHelpUtil.loadCachesFromCluster(
					portalCacheManagerName,
					portalCacheNames.toArray(
						new String[portalCacheNames.size()]));
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to load cache data from the cluster", e);
			}
		}
		finally {
			_deferredPortalCaches.clear();
		}
	}

	public StreamBootstrapLoader(Properties properties) {
		if (properties != null) {
			_bootstrapAsynchronously = GetterUtil.getBoolean(
				properties.getProperty("bootstrapAsynchronously"));
		}
	}

	@Override
	public boolean isAsynchronous() {
		return _bootstrapAsynchronously;
	}

	@Override
	public void load(String portalCacheManagerName, String portalCacheName) {
		if (_bootstrapAsynchronously) {
			BootstrapClientThread streamClientThread =
				new BootstrapClientThread(
					portalCacheManagerName, portalCacheName);

			streamClientThread.start();
		}
		else {
			doLoad(portalCacheManagerName, portalCacheName);
		}
	}

	protected void doLoad(
		String portalCacheManagerName, String portalCacheName) {

		synchronized (StreamBootstrapLoader.class) {
			if (!_started) {
				List<String> portalCacheNames = _deferredPortalCaches.get(
					portalCacheManagerName);

				if (portalCacheNames == null) {
					portalCacheNames = new ArrayList<String>();

					_deferredPortalCaches.put(
						portalCacheManagerName, portalCacheNames);
				}

				portalCacheNames.add(portalCacheName);

				return;
			}
		}

		if (_skipBootstrapThreadLocal.get()) {
			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Bootstraping " + portalCacheName);
		}

		try {
			StreamBootstrapHelpUtil.loadCachesFromCluster(
				portalCacheManagerName, portalCacheName);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to load cache data from the cluster", e);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		StreamBootstrapLoader.class);

	private static Map<String, List<String>> _deferredPortalCaches =
		new HashMap<String, List<String>>();
	private static ThreadLocal<Boolean> _skipBootstrapThreadLocal =
		new InitialThreadLocal<Boolean>(
			StreamBootstrapLoader.class + "._skipBootstrapThreadLocal", false);
	private static boolean _started;

	private boolean _bootstrapAsynchronously = true;

	private class BootstrapClientThread extends Thread {

		public BootstrapClientThread(
			String portalCacheManagerName, String portalCacheName) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Stream client thread for cache " + portalCacheName +
						" from cache manager " + portalCacheManagerName);
			}

			_portalCacheManagerName = portalCacheManagerName;
			_portalCacheName = portalCacheName;

			setDaemon(true);
			setName(
				BootstrapClientThread.class.getName() + " - " +
					portalCacheManagerName + " - " + portalCacheName);
			setPriority(Thread.NORM_PRIORITY);
		}

		@Override
		public void run() {
			try {
				doLoad(_portalCacheManagerName, _portalCacheName);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to asynchronously stream bootstrap", e);
				}
			}
		}

		private String _portalCacheManagerName;
		private String _portalCacheName;

	}

}