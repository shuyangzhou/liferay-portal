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

package com.liferay.shielded.container.internal.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author Shuyang Zhou
 */
public class ServletInvocationHandler implements InvocationHandler {

	public ServletInvocationHandler(
		Supplier<? extends Servlet> servletSupplier,
		ServletContext servletContext) {

		_servletSupplier = servletSupplier;
		_servletContext = servletContext;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		if (_methods.contains(method)) {
			if (_initMethod.equals(method)) {
				args[0] = Proxy.newProxyInstance(
					_servletContext.getClassLoader(),
					new Class<?>[] {ServletConfig.class},
					new ServletConfigInvocationHandler(
						(ServletConfig)args[0], _servletContext));
			}

			return method.invoke(_servletSupplier.get(), args);
		}

		return method.invoke(this, args);
	}

	private static final Method _initMethod;
	private static final Set<Method> _methods = new HashSet<>(
		Arrays.asList(Servlet.class.getMethods()));

	static {
		try {
			_initMethod = Servlet.class.getMethod("init", ServletConfig.class);
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new ExceptionInInitializerError(noSuchMethodException);
		}
	}

	private final ServletContext _servletContext;
	private final Supplier<? extends Servlet> _servletSupplier;

}