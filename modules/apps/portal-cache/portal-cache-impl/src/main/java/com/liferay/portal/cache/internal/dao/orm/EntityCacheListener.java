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

package com.liferay.portal.cache.internal.dao.orm;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.model.BaseModel;

import java.util.Collections;
import java.util.List;

/**
 * @author Tina Tian
 */
public class EntityCacheListener {

	public EntityCacheListener(
		FinderCache finderCache,
		ServiceTrackerMap<String, List<FinderPath>> serviceTrackerMap) {

		_finderCache = finderCache;
		_serviceTrackerMap = serviceTrackerMap;
	}

	public void clearCache() {
		_finderCache.clearCache();
	}

	public void clearCache(Class<?> clazz) {
		String cacheName = clazz.getName();

		_finderCache.clearCache(cacheName);
		_finderCache.clearCache(_getCacheNameWithPagination(cacheName));
		_finderCache.clearCache(_getCacheNameWithoutPagination(cacheName));
	}

	public void put(
		Class<?> clazz, BaseModel<?> baseModel, boolean columnBitmaskEnabled,
		long columnBitmask) {

		String cacheName = clazz.getName();

		_finderCache.clearCache(_getCacheNameWithPagination(cacheName));

		if (!columnBitmaskEnabled) {
			_finderCache.clearCache(_getCacheNameWithoutPagination(cacheName));
			_finderCache.clearCache(cacheName);

			return;
		}

		for (FinderPath finderPath :
				_getFinderPaths(_getCacheNameWithoutPagination(cacheName))) {

			if (baseModel.isNew()) {
				Object[] arguments = finderPath.getArguments(baseModel);

				if (arguments == null) {
					arguments = _FINDER_ARGS_EMPTY;
				}

				_removeResult(finderPath, arguments, null);
			}
			else {
				_removeResult(
					finderPath, finderPath.getArguments(baseModel),
					columnBitmask);
				_removeResult(
					finderPath, finderPath.getOriginalArguments(baseModel),
					columnBitmask);
			}
		}

		for (FinderPath finderPath : _getFinderPaths(cacheName)) {
			_removeResult(
				finderPath, finderPath.getOriginalArguments(baseModel),
				columnBitmask);
		}
	}

	public void remove(
		Class<?> clazz, BaseModel<?> baseModel, boolean columnBitmaskEnabled,
		long columnBitmask) {

		if (baseModel == null) {
			clearCache(clazz);

			return;
		}

		String cacheName = clazz.getName();

		_finderCache.clearCache(_getCacheNameWithPagination(cacheName));
		_finderCache.clearCache(_getCacheNameWithoutPagination(cacheName));

		if (!columnBitmaskEnabled) {
			_finderCache.clearCache(cacheName);

			return;
		}

		for (FinderPath finderPath : _getFinderPaths(cacheName)) {
			_removeResult(finderPath, finderPath.getArguments(baseModel), null);
			_removeResult(
				finderPath, finderPath.getOriginalArguments(baseModel),
				columnBitmask);
		}
	}

	public void removeCache(String cacheName) {
		_finderCache.removeCache(cacheName);
		_finderCache.removeCache(_getCacheNameWithPagination(cacheName));
		_finderCache.removeCache(_getCacheNameWithoutPagination(cacheName));
	}

	private String _getCacheNameWithoutPagination(String cacheName) {
		return cacheName.concat(".List2");
	}

	private String _getCacheNameWithPagination(String cacheName) {
		return cacheName.concat(".List1");
	}

	private List<FinderPath> _getFinderPaths(String cacheName) {
		List<FinderPath> finderPaths = _serviceTrackerMap.getService(cacheName);

		if (finderPaths == null) {
			return Collections.emptyList();
		}

		return finderPaths;
	}

	private void _removeResult(
		FinderPath finderPath, Object[] arguments, Long columnBitmask) {

		if ((arguments == null) ||
			((columnBitmask != null) &&
			 ((columnBitmask & finderPath.getColumnBitmask()) == 0))) {

			return;
		}

		_finderCache.removeResult(finderPath, arguments);
	}

	private static final Object[] _FINDER_ARGS_EMPTY = new Object[0];

	private final FinderCache _finderCache;
	private final ServiceTrackerMap<String, List<FinderPath>>
		_serviceTrackerMap;

}