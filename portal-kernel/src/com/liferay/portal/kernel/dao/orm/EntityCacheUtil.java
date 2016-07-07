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

package com.liferay.portal.kernel.dao.orm;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 */
public class EntityCacheUtil {

	public static void clearCache() {
		_getEntityCache().clearCache();
	}

	public static void clearCache(Class<?> clazz) {
		_getEntityCache().clearCache(clazz);
	}

	public static void clearLocalCache() {
		_getEntityCache().clearLocalCache();
	}

	/**
	 * @deprecated As of 7.1.0, replaced by {@link #_getEntityCache()}
	 */
	@Deprecated
	public static EntityCache getEntityCache() {
		return _getEntityCache();
	}

	public static PortalCache<Serializable, Serializable> getPortalCache(
		Class<?> clazz) {

		return _getEntityCache().getPortalCache(clazz);
	}

	public static Serializable getResult(
		boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {

		return _getEntityCache().getResult(
			entityCacheEnabled, clazz, primaryKey);
	}

	public static void invalidate() {
		_getEntityCache().invalidate();
	}

	public static Serializable loadResult(
		boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey,
		SessionFactory sessionFactory) {

		return _getEntityCache().loadResult(
			entityCacheEnabled, clazz, primaryKey, sessionFactory);
	}

	public static void putResult(
		boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey,
		Serializable result) {

		_getEntityCache().putResult(
			entityCacheEnabled, clazz, primaryKey, result);
	}

	public static void putResult(
		boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey,
		Serializable result, boolean quiet) {

		_getEntityCache().putResult(
			entityCacheEnabled, clazz, primaryKey, result, quiet);
	}

	public static void removeCache(String className) {
		_getEntityCache().removeCache(className);
	}

	public static void removeResult(
		boolean entityCacheEnabled, Class<?> clazz, Serializable primaryKey) {

		_getEntityCache().removeResult(entityCacheEnabled, clazz, primaryKey);
	}

	private static EntityCache _getEntityCache() {
		PortalRuntimePermission.checkGetBeanProperty(EntityCacheUtil.class);

		return _entityCache;
	}

	private static volatile EntityCache _entityCache =
		ProxyFactory.newServiceTrackedInstance(
			EntityCache.class, EntityCacheUtil.class, "_entityCache");

}