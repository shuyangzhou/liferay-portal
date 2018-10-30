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

package com.liferay.portal.internal.aop;

import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.kernel.spring.aop.AdvisedSupport;
import com.liferay.portal.spring.aop.AdvisedSupportProxy;
import com.liferay.portal.transaction.TransactionsUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class MethodInterceptorProxyImpl
	implements AdvisedSupport, AdvisedSupportProxy, InvocationHandler {

	public MethodInterceptorProxyImpl(
		Object target, Class<?>[] proxiedInterfaces,
		MethodInterceptorCache methodInterceptorCache) {

		_target = target;
		_proxiedInterfaces = proxiedInterfaces;
		_methodInterceptorCache = methodInterceptorCache;
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
	public Object invoke(Object proxy, Method method, Object[] args)
		throws Throwable {

		MethodInvocationImpl methodInvocation = new MethodInvocationImpl(
			_target, method, args);

		if (TransactionsUtil.isEnabled()) {
			methodInvocation.setMethodInterceptors(
				_methodInterceptorCache.getMethodInterceptors(
					methodInvocation));
		}
		else {
			methodInvocation.setMethodInterceptors(_emptyMethodInterceptors);
		}

		return methodInvocation.proceed();
	}

	@Override
	public synchronized void setTarget(Object target) {
		_target = target;

		_methodInterceptorCache.clear();
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	@Override
	public void setTarget(Object target, Class<?> targetClass) {
		setTarget(target);
	}

	private static final MethodInterceptor[] _emptyMethodInterceptors =
		new MethodInterceptor[0];

	private volatile MethodInterceptorCache _methodInterceptorCache;
	private final Class<?>[] _proxiedInterfaces;
	private volatile Object _target;

}