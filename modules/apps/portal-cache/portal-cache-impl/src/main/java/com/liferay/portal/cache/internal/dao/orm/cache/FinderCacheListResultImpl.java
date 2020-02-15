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
import com.liferay.portal.kernel.dao.orm.cache.FinderCacheListResult;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;

import java.io.Serializable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Preston Crary
 */
public class FinderCacheListResultImpl<T extends BaseModel<T>>
	extends BaseFinderCacheResult
		<AbstractMap.SimpleImmutableEntry<Long, Serializable[]>>
	implements FinderCacheListResult<T> {

	public FinderCacheListResultImpl(
		Serializable cacheKey, Serializable cacheValue,
		Map<Object, Serializable> localCache, Object localCacheKey,
		Supplier<PortalCache<Serializable, Serializable>> portalCacheSupplier,
		int valueObjectFinderCacheListThreshold) {

		super(cacheKey, localCache, localCacheKey, portalCacheSupplier);

		if (cacheValue == null) {
			_cacheValue = _NULL_ENTRY;
		}
		else {
			_cacheValue = (AbstractMap.SimpleImmutableEntry)cacheValue;
		}

		_valueObjectFinderCacheListThreshold =
			valueObjectFinderCacheListThreshold;
	}

	@Override
	public void clear() {
		_cacheValue = _NULL_ENTRY;

		remove();
	}

	@Override
	public Long getCount() {
		return _cacheValue.getKey();
	}

	@Override
	public List<T> getResult(BasePersistence<T> basePersistence) {
		Serializable[] primaryKeys = _cacheValue.getValue();

		if (primaryKeys == null) {
			return null;
		}

		if (primaryKeys == _EMPTY_RESULT) {
			return Collections.emptyList();
		}

		Set<Serializable> primaryKeysSet = new HashSet<>();

		Collections.addAll(primaryKeysSet, primaryKeys);

		Map<Serializable, T> map = basePersistence.fetchByPrimaryKeys(
			primaryKeysSet);

		if (map.size() < primaryKeysSet.size()) {
			return null;
		}

		List<T> list = new ArrayList<>(primaryKeys.length);

		for (Serializable curPrimaryKey : primaryKeys) {
			list.add(map.get(curPrimaryKey));
		}

		return Collections.unmodifiableList(list);
	}

	@Override
	public void set(long count, List<T> baseModels) {
		_set(count, baseModels);
	}

	@Override
	public void setCount(long count) {
		if (count == 0) {
			_cacheValue = _EMPTY_ENTRY;
		}
		else {
			_cacheValue = new AbstractMap.SimpleImmutableEntry<>(
				count, _cacheValue.getValue());
		}

		put(_cacheValue);
	}

	@Override
	public void setResult(List<T> baseModels) {
		_set(_cacheValue.getKey(), baseModels);
	}

	private void _set(Long count, List<T> baseModels) {
		Serializable[] primaryKeys = null;

		if (baseModels.isEmpty()) {
			primaryKeys = _EMPTY_RESULT;
		}
		else if ((baseModels.size() <= _valueObjectFinderCacheListThreshold) ||
				 (_valueObjectFinderCacheListThreshold <= 0)) {

			primaryKeys = new Serializable[baseModels.size()];

			for (int i = 0; i < primaryKeys.length; i++) {
				T baseModel = baseModels.get(i);

				primaryKeys[i] = baseModel.getPrimaryKeyObj();
			}
		}

		_cacheValue = new AbstractMap.SimpleImmutableEntry<>(
			count, primaryKeys);

		put(_cacheValue);
	}

	private static final AbstractMap.SimpleImmutableEntry<Long, Serializable[]>
		_EMPTY_ENTRY;

	private static final Serializable[] _EMPTY_RESULT;

	private static final AbstractMap.SimpleImmutableEntry<Long, Serializable[]>
		_NULL_ENTRY = new AbstractMap.SimpleImmutableEntry<>(null, null);

	static {
		Serializable[] emptyResult = new Serializable[0];

		_EMPTY_ENTRY = new AbstractMap.SimpleImmutableEntry<>(0L, emptyResult);
		_EMPTY_RESULT = emptyResult;
	}

	private AbstractMap.SimpleImmutableEntry<Long, Serializable[]> _cacheValue;
	private final int _valueObjectFinderCacheListThreshold;

}