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

package com.liferay.portal.kernel.cache.key;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael C. Han
 * @author Shuyang Zhou
 */
public class CacheKeyGeneratorUtil {

	/**
	 * @deprecated As of Cavanaugh (7.4.x), with no direct replacement
	 */
	@Deprecated
	public static CacheKeyGenerator getCacheKeyGenerator() {
		return getCacheKeyGenerator(null);
	}

	public static CacheKeyGenerator getCacheKeyGenerator(String cacheName) {
		CacheKeyGenerator cacheKeyGenerator = _cacheKeyGenerators.get(
			cacheName);

		if (cacheKeyGenerator == null) {
			cacheKeyGenerator = _defaultCacheKeyGenerator;
		}

		return cacheKeyGenerator;
	}

	public void setCacheKeyGenerators(
		Map<String, CacheKeyGenerator> cacheKeyGenerators) {

		_cacheKeyGenerators = cacheKeyGenerators;
	}

	public void setDefaultCacheKeyGenerator(
		CacheKeyGenerator defaultCacheKeyGenerator) {

		_defaultCacheKeyGenerator = defaultCacheKeyGenerator;
	}

	private static Map<String, CacheKeyGenerator> _cacheKeyGenerators =
		new HashMap<>();
	private static CacheKeyGenerator _defaultCacheKeyGenerator;

}