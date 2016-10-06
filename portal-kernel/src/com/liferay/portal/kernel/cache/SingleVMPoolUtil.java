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

package com.liferay.portal.kernel.cache;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 */
@OSGiBeanProperties(service = SingleVMPoolUtil.class)
public class SingleVMPoolUtil {

	public static void clear() {
		if (_singleVMPool == null) {
			return;
		}

		_singleVMPool.clear();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCache(String)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName) {

		return getPortalCache(portalCacheName);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCache(String,
	 *             boolean)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName, boolean blocking) {

		return getPortalCache(portalCacheName, blocking);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #getPortalCacheManager()}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getCacheManager() {

		return getPortalCacheManager();
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName) {

		return (PortalCache<K, V>)_instance._getSingleVMPool().getPortalCache(
			portalCacheName);
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName, boolean blocking) {

		return (PortalCache<K, V>)_instance._getSingleVMPool().getPortalCache(
			portalCacheName, blocking);
	}

	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getPortalCacheManager() {

		return (PortalCacheManager<K, V>)
			_instance._getSingleVMPool().getPortalCacheManager();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #removePortalCache(String)}
	 */
	@Deprecated
	public static void removeCache(String portalCacheName) {
		removePortalCache(portalCacheName);
	}

	public static void removePortalCache(String portalCacheName) {
		if (_singleVMPool == null) {
			return;
		}

		_singleVMPool.removePortalCache(portalCacheName);
	}

	private SingleVMPool _getSingleVMPool() {
		try {
			while (_singleVMPool == null) {
				Registry registry = RegistryUtil.getRegistry();

				_singleVMPool = registry.getService(SingleVMPool.class);

				if (_singleVMPool != null) {
					return _singleVMPool;
				}

				if (_log.isDebugEnabled()) {
					_log.debug("Waiting for a single vm pool");
				}

				Thread.sleep(500);
			}
		}
		catch (InterruptedException ie) {
			throw new IllegalStateException(
				"Unable to initialize SingleVMPoolUtil", ie);
		}

		return _singleVMPool;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SingleVMPoolUtil.class);

	private static final SingleVMPoolUtil _instance = new SingleVMPoolUtil();

	private static volatile SingleVMPool _singleVMPool =
		ProxyFactory.newServiceTrackedInstanceWithoutDummyService(
			SingleVMPool.class, SingleVMPoolUtil.class, "_singleVMPool");

}