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

package com.liferay.portal.cache.internal.dao.orm.cache;

import com.liferay.portal.kernel.cache.PortalCache;

import java.io.Serializable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Preston Crary
 */
public abstract class BaseFinderCacheResult<T extends Serializable> {

	protected BaseFinderCacheResult(
		Serializable cacheKey, Map<Object, Serializable> localCache,
		Object localCacheKey,
		Supplier<PortalCache<Serializable, Serializable>> portalCacheSupplier) {

		_cacheKey = cacheKey;
		_localCache = localCache;
		_localCacheKey = localCacheKey;
		_portalCacheSupplier = portalCacheSupplier;
	}

	protected void put(T value) {
		if (_localCache != null) {
			_localCache.put(_localCacheKey, value);
		}

		PortalCache<Serializable, Serializable> portalCache =
			_portalCacheSupplier.get();

		portalCache.put(_cacheKey, value);
	}

	protected void remove() {
		if (_localCache != null) {
			_localCache.remove(_localCacheKey);
		}

		PortalCache<Serializable, Serializable> portalCache =
			_portalCacheSupplier.get();

		portalCache.remove(_cacheKey);
	}

	private final Serializable _cacheKey;
	private final Map<Object, Serializable> _localCache;
	private final Object _localCacheKey;
	private final Supplier<PortalCache<Serializable, Serializable>>
		_portalCacheSupplier;

}