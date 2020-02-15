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

import com.liferay.portal.kernel.dao.orm.cache.FinderCacheListResult;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.util.List;

/**
 * @author Preston Crary
 */
public class FinderCacheDisabledListResult<T extends BaseModel<T>>
	implements FinderCacheListResult<T> {

	@SuppressWarnings("unchecked")
	public static <T extends BaseModel<T>> FinderCacheListResult<T>
		getInstance() {

		return (FinderCacheListResult<T>)_INSTANCE;
	}

	@Override
	public void clear() {
	}

	@Override
	public Long getCount() {
		return null;
	}

	@Override
	public List<T> getResult(BasePersistence<T> basePersistence) {
		return null;
	}

	@Override
	public void set(long count, List<T> baseModels) {
	}

	@Override
	public void setCount(long count) {
	}

	@Override
	public void setResult(List<T> baseModels) {
	}

	private FinderCacheDisabledListResult() {
	}

	private static final FinderCacheListResult<?> _INSTANCE =
		new FinderCacheDisabledListResult();

}