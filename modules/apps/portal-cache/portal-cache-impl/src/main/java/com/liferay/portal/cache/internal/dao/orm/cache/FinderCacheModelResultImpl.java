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
import com.liferay.portal.kernel.dao.orm.cache.FinderCacheModelResult;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.io.Serializable;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Preston Crary
 */
public class FinderCacheModelResultImpl<T extends BaseModel<T>>
	extends BaseFinderCacheResult<Serializable>
	implements FinderCacheModelResult<T> {

	public FinderCacheModelResultImpl(
		Serializable cacheKey, Serializable cacheValue,
		Map<Object, Serializable> localCache, Object localCacheKey,
		Supplier<PortalCache<Serializable, Serializable>> portalCacheSupplier) {

		super(cacheKey, localCache, localCacheKey, portalCacheSupplier);

		_cacheValue = cacheValue;
	}

	@Override
	public void clear() {
		_cacheValue = null;

		remove();
	}

	@Override
	public boolean exists() {
		if (_cacheValue == _EMPTY_RESULT) {
			return false;
		}

		return true;
	}

	@Override
	public T getResult(BasePersistence<T> basePersistence) {
		if (_cacheValue == _EMPTY_RESULT) {
			return null;
		}

		return basePersistence.fetchByPrimaryKey(_cacheValue);
	}

	@Override
	public boolean isCached() {
		if (_cacheValue == null) {
			return false;
		}

		return true;
	}

	@Override
	public void setResult(T baseModel) {
		if (baseModel == null) {
			_cacheValue = _EMPTY_RESULT;
		}
		else {
			_cacheValue = baseModel.getPrimaryKeyObj();
		}

		put(_cacheValue);
	}

	private static final Serializable _EMPTY_RESULT = new Serializable() {
	};

	private Serializable _cacheValue;

}