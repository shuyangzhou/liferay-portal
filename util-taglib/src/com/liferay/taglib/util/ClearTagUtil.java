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

import com.liferay.petra.lang.CentralizedThreadLocal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author Matthew Tambara
 */
public class ClearTagUtil {

	public static void clearTags() {
		Deque<List<ParamAndPropertyAncestorTagImpl>> deque =
			_paramAndPropertyAncestorTagThreadLocal.get();

		List<ParamAndPropertyAncestorTagImpl> paramAndPropertyAncestorTagImpls =
			deque.pop();

		paramAndPropertyAncestorTagImpls.forEach(
			ParamAndPropertyAncestorTagImpl::clear);

		if (deque.isEmpty()) {
			_paramAndPropertyAncestorTagThreadLocal.remove();
		}
	}

	public static void push() {
		Deque<List<ParamAndPropertyAncestorTagImpl>> deque =
			_paramAndPropertyAncestorTagThreadLocal.get();

		deque.push(new ArrayList<>());
	}

	public static void registerTag(
		ParamAndPropertyAncestorTagImpl paramAndPropertyAncestorTagImpl) {

		Deque<List<ParamAndPropertyAncestorTagImpl>> deque =
			_paramAndPropertyAncestorTagThreadLocal.get();

		List<ParamAndPropertyAncestorTagImpl> paramAndPropertyAncestorTagImpls =
			deque.peek();

		paramAndPropertyAncestorTagImpls.add(paramAndPropertyAncestorTagImpl);
	}

	private static final ThreadLocal
		<Deque<List<ParamAndPropertyAncestorTagImpl>>>
			_paramAndPropertyAncestorTagThreadLocal =
				new CentralizedThreadLocal<>(
					ClearTagUtil.class +
						"_paramAndPropertyAncestorTagThreadLocal",
					ArrayDeque::new, false);

}