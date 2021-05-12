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

package com.liferay.portal.module.framework;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel Pastor
 * @author Raymond Augé
 */
public class ModuleFrameworkAdapterHelper {

	public ModuleFrameworkAdapterHelper(String className) {
		try {
			_adaptedObject = InstanceFactory.newInstance(
				PortalClassLoaderUtil.getClassLoader(), className);
		}
		catch (Exception exception) {
			_log.error("Unable to load the module framework", exception);

			throw new RuntimeException(exception);
		}
	}

	public Object exec(
		String methodName, Class<?>[] parameterTypes, Object... parameters) {

		try {
			Method method = searchMethod(methodName, parameterTypes);

			return method.invoke(_adaptedObject, parameters);
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			throw new RuntimeException(exception);
		}
	}

	protected Method searchMethod(String methodName, Class<?>[] parameterTypes)
		throws Exception {

		MethodKey methodKey = new MethodKey(
			_adaptedObject.getClass(), methodName, parameterTypes);

		if (_methods.containsKey(methodKey)) {
			return _methods.get(methodKey);
		}

		Method method = ReflectionUtil.getDeclaredMethod(
			_adaptedObject.getClass(), methodName, parameterTypes);

		_methods.put(methodKey, method);

		return method;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ModuleFrameworkAdapterHelper.class);

	private static final Map<MethodKey, Method> _methods = new HashMap<>();

	private final Object _adaptedObject;

}