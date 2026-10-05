/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.dao.orm;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Shuyang Zhou
 */
public class CountFinderPathRegistry {

	public static Collection<FinderPath> getCountFinderPaths(String className) {
		Map<String, FinderPath> countFinderPaths = _countFinderPathsMap.get(
			className);

		if (countFinderPaths == null) {
			return Collections.emptySet();
		}

		return countFinderPaths.values();
	}

	public static void register(FinderPath finderPath) {
		Map<String, FinderPath> countFinderPaths =
			_countFinderPathsMap.computeIfAbsent(
				finderPath.getEntityClassName(),
				key -> new ConcurrentHashMap<>());

		countFinderPaths.put(finderPath.getCacheKeyPrefix(), finderPath);
	}

	public static void unregister(String className) {
		_countFinderPathsMap.remove(className);
	}

	private static final Map<String, Map<String, FinderPath>>
		_countFinderPathsMap = new ConcurrentHashMap<>();

}