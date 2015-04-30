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

package com.liferay.portal.kernel.concurrent.test;

import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Preston Crary
 */
public class MappedMethodNameRunnableAdvice {

	public MappedMethodNameRunnableAdvice(
			Object obj, String fieldName, Class fieldClass)
		throws Exception {

		Field field = ReflectionUtil.getDeclaredField(
			obj.getClass(), fieldName);

		_instance = field.get(obj);

		field.set(
			obj,
			ProxyUtil.newProxyInstance(
				ClassLoader.getSystemClassLoader(), new Class[] {fieldClass},
				new MappedInvocationHandler()));
	}

	public void putAfterRunnable(String methodName, Runnable runnable) {
		_afterRunnables.put(methodName, runnable);
	}

	public void putBeforeRunnable(String methodName, Runnable runnable) {
		_beforeRunnables.put(methodName, runnable);
	}

	private final Map<String, Runnable> _afterRunnables = new HashMap<>();
	private final Map<String, Runnable> _beforeRunnables = new HashMap<>();
	private final Object _instance;

	private class MappedInvocationHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			Runnable beforeRunnable = _beforeRunnables.remove(method.getName());

			if (beforeRunnable != null) {
				beforeRunnable.run();
			}

			try {
				return method.invoke(_instance, args);
			}
			catch (InvocationTargetException ite) {
				throw ite.getTargetException();
			}
			finally {
				Runnable afterRunnable = _afterRunnables.remove(
					method.getName());

				if (afterRunnable != null) {
					afterRunnable.run();
				}
			}
		}

	}

}