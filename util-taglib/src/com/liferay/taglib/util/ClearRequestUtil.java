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

package com.liferay.taglib.util;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Matthew Tambara
 */
public class ClearRequestUtil {

	public static Set<ParamAndPropertyAncestorTagImpl> getTags() {
		return _paramAndPropertyAncestorTagImpls;
	}

	public static void registerTag(
		ParamAndPropertyAncestorTagImpl paramAndPropertyAncestorTagImpl) {

		_paramAndPropertyAncestorTagImpls.add(paramAndPropertyAncestorTagImpl);
	}

	private static final Set<ParamAndPropertyAncestorTagImpl>
		_paramAndPropertyAncestorTagImpls = Collections.newSetFromMap(
			new ConcurrentHashMap<ParamAndPropertyAncestorTagImpl, Boolean>());

}