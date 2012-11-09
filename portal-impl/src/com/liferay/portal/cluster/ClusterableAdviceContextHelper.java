/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.cluster;

import com.liferay.portal.kernel.util.InitialThreadLocal;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.ObjectValuePair;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class ClusterableAdviceContextHelper {

	public static Serializable getThreadLocalContext(String key) {
		Map<String, Serializable> context = _contextThreadLocal.get();

		return context.get(key);
	}

	public static void putAllThreadLocalContext(
		Map<String, Serializable> context) {

		if (context == null) {
			return;
		}

		Map<String, Serializable> localContext = _contextThreadLocal.get();

		localContext.putAll(context);
	}

	public static void putThreadLocalContext(String key, Serializable value) {
		Map<String, Serializable> context = _contextThreadLocal.get();

		context.put(key, value);
	}

	protected static Map<String, Serializable> getThreadLocalContext() {
		return _contextThreadLocal.get();
	}

	protected static void removeThreadLocalContext() {
		_contextThreadLocal.remove();
	}

	protected static ObjectValuePair<MethodHandler, Map<String, Serializable>>
		unwrapMethodhandlerWithContext(MethodHandler methodHandler) {

		MethodKey methodKey = methodHandler.getMethodKey();

		if (!methodKey.equals(_dummyMethodKey)) {
			return
				new ObjectValuePair<MethodHandler, Map<String, Serializable>>(
					methodHandler, null);
		}

		Object[] arguments = methodHandler.getArguments();

		MethodHandler orginalMethodHandler = null;
		Map<String, Serializable> context = null;

		if (arguments[0] != null) {
			orginalMethodHandler = (MethodHandler)arguments[0];
		}

		if (arguments[1] != null) {
			context = (Map<String, Serializable>)arguments[1];
		}

		return new ObjectValuePair<MethodHandler, Map<String, Serializable>>(
			orginalMethodHandler, context);
	}

	protected static MethodHandler wrapMethodhandlerWithContext(
		MethodHandler methodHandler, Map<String, Serializable> context) {

		MethodHandler newMethodHandler = new MethodHandler(
			_dummyMethodKey, methodHandler, context);

		return newMethodHandler;
	}

	private static ThreadLocal<HashMap<String, Serializable>>
		_contextThreadLocal =
			new InitialThreadLocal<HashMap<String, Serializable>>(
				ClusterableAdviceContextHelper.class.getName(),
				new HashMap<String, Serializable>(), true);

	private static MethodKey _dummyMethodKey = new MethodKey(
		ClusterableAdviceContextHelper.class, "dummyMethod");

}