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

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * @author Shuyang Zhou
 */
public class FilterConfigInvocationHandler implements InvocationHandler {

	public FilterConfigInvocationHandler(
		FilterConfig filterConfig, ServletContext servletContext) {

		_filterConfig = filterConfig;
		_servletContext = servletContext;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		if (_getServletContextMethod.equals(method)) {
			return _servletContext;
		}

		return method.invoke(_filterConfig, args);
	}

	private static final Method _getServletContextMethod;

	static {
		try {
			_getServletContextMethod = FilterConfig.class.getMethod(
				"getServletContext");
		}
		catch (NoSuchMethodException noSuchMethodException) {
			throw new ExceptionInInitializerError(noSuchMethodException);
		}
	}

	private final FilterConfig _filterConfig;
	private final ServletContext _servletContext;

}