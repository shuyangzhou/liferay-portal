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

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ServiceRetriever;

/**
 * @author Brian Wing Shun Chan
 */
public class FinderCacheUtil {

	public static void clearCache() {
		_getFinderCache().clearCache();
	}

	public static void clearCache(String className) {
		_getFinderCache().clearCache(className);
	}

	public static void clearLocalCache() {
		_getFinderCache().clearLocalCache();
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #_getFinderCache()}
	 */
	@Deprecated
	public static FinderCache getFinderCache() {
		return _getFinderCache();
	}

	public static Object getResult(
		FinderPath finderPath, Object[] args,
		BasePersistenceImpl<? extends BaseModel<?>> basePersistenceImpl) {

		return _getFinderCache().getResult(
			finderPath, args, basePersistenceImpl);
	}

	public static void invalidate() {
		getFinderCache().invalidate();
	}

	public static void putResult(
		FinderPath finderPath, Object[] args, Object result) {

		_getFinderCache().putResult(finderPath, args, result);
	}

	public static void putResult(
		FinderPath finderPath, Object[] args, Object result, boolean quiet) {

		_getFinderCache().putResult(finderPath, args, result, quiet);
	}

	public static void removeCache(String className) {
		_getFinderCache().removeCache(className);
	}

	public static void removeResult(FinderPath finderPath, Object[] args) {
		_getFinderCache().removeResult(finderPath, args);
	}

	private static FinderCache _getFinderCache() {
		PortalRuntimePermission.checkGetBeanProperty(FinderCacheUtil.class);

		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<FinderCache> _serviceRetriever =
		new ServiceRetriever<>(FinderCache.class);

}