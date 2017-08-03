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

package com.liferay.portal.dao.jdbc.functions;

import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Manuel de la Peña
 */
public class IsPresentPropertyFunction implements Function<String[], Boolean> {

	public IsPresentPropertyFunction(String key) {
		_key = key;
	}

	@Override
	public Boolean apply(String[] keys) {
		Stream<String> stream = Arrays.stream(keys);

		Optional<String> optionalKey = stream.filter(
			key -> StringUtil.equalsIgnoreCase(_key, key)
		).findFirst();

		return optionalKey.isPresent();
	}

	private final String _key;

}