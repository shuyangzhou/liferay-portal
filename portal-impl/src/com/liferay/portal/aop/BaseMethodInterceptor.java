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

package com.liferay.portal.aop;

import com.liferay.portal.aop.cache.MethodInterceptorCache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Preston Crary
 */
public abstract class BaseMethodInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object returnValue = before(methodInvocation);

		if (returnValue != null) {
			if (returnValue == nullResult) {
				return null;
			}

			return returnValue;
		}

		try {
			returnValue = methodInvocation.proceed();

			afterReturning(methodInvocation, returnValue);
		}
		catch (Throwable throwable) {
			afterThrowing(methodInvocation, throwable);

			throw throwable;
		}
		finally {
			duringFinally(methodInvocation);
		}

		return returnValue;
	}

	public void setMethodInterceptorCache(
		MethodInterceptorCache methodInterceptorCache) {

		this.methodInterceptorCache = methodInterceptorCache;
	}

	protected void afterReturning(
			MethodInvocation methodInvocation, Object result)
		throws Throwable {
	}

	protected void afterThrowing(
			MethodInvocation methodInvocation, Throwable throwable)
		throws Throwable {
	}

	protected Object before(MethodInvocation methodInvocation)
		throws Throwable {

		return null;
	}

	protected void duringFinally(MethodInvocation methodInvocation) {
	}

	protected static final Object nullResult = new Object();

	protected MethodInterceptorCache methodInterceptorCache;

}