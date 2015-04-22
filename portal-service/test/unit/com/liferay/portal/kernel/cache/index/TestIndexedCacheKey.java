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

package com.liferay.portal.kernel.cache.index;

import com.liferay.portal.kernel.util.HashUtil;

/**
 * @author Preston Crary
 */
public class TestIndexedCacheKey implements IndexedCacheKey<Long> {

	public TestIndexedCacheKey(long part1, long part2) {
		_part1 = part1;
		_part2 = part2;
	}

	@Override
	public boolean equals(Object obj) {
		TestIndexedCacheKey testIndexedCacheKey = (TestIndexedCacheKey)obj;

		if ((testIndexedCacheKey._part1 == _part1) &&
			(testIndexedCacheKey._part2 == _part2)) {

			return true;
		}

		return false;
	}

	@Override
	public Long getIndex() {
		return _part1;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, _part1);

		return HashUtil.hash(hashCode, _part2);
	}

	private final long _part1;
	private final long _part2;

}