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

package com.liferay.portal.internal.spring.aop;

import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.spring.aop.AdvisedSupportProxy;
import com.liferay.portal.transaction.TransactionsUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Preston Crary
 */
public class CounterMethodInterceptorProxyImpl
	implements AdvisedSupport, AdvisedSupportProxy, InvocationHandler {

	public CounterMethodInterceptorProxyImpl(
		Object target, Class<?>[] proxiedInterfaces,
		MethodInterceptor methodInterceptor) {

		_target = target;
		_proxiedInterfaces = proxiedInterfaces;
		_methodInterceptors = new MethodInterceptor[] {methodInterceptor};
	}

	@Override
	public AdvisedSupport getAdvisedSupport() {
		return this;
	}

	@Override
	public Class<?>[] getProxiedInterfaces() {
		return _proxiedInterfaces;
	}

	@Override
	public Object getTarget() {
		return _target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] arguments)
		throws Throwable {

		MethodInvocationImpl methodInvocation = new MethodInvocationImpl(
			_target, method, arguments);

		if (TransactionsUtil.isEnabled()) {
			methodInvocation.setMethodInterceptors(_methodInterceptors);
		}
		else {
			methodInvocation.setMethodInterceptors(_emptyMethodInterceptors);
		}

		return methodInvocation.proceed();
	}

	@Override
	public void setTarget(Object target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTarget(Object target, Class<?> targetClass) {
		throw new UnsupportedOperationException();
	}

	private static final MethodInterceptor[] _emptyMethodInterceptors =
		new MethodInterceptor[0];

	private final MethodInterceptor[] _methodInterceptors;
	private final Class<?>[] _proxiedInterfaces;
	private final Object _target;

}