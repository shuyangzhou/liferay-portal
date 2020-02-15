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

import com.liferay.portal.kernel.dao.orm.cache.FinderCacheModelResult;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * @author Preston Crary
 */
public class FinderCacheDisableModelResult<T extends BaseModel<T>>
	implements FinderCacheModelResult<T> {

	@SuppressWarnings("unchecked")
	public static <T extends BaseModel<T>> FinderCacheModelResult<T>
		getInstance() {

		return (FinderCacheModelResult<T>)_INSTANCE;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public T getResult(BasePersistence<T> basePersistence) {
		return null;
	}

	@Override
	public boolean isCached() {
		return false;
	}

	@Override
	public void setResult(T baseModel) {
	}

	private FinderCacheDisableModelResult() {
	}

	private static final FinderCacheModelResult<?> _INSTANCE =
		new FinderCacheDisableModelResult();

}