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

package com.liferay.portal.spring.aop;

import com.liferay.portal.internal.spring.aop.MethodInvocationImpl;
import com.liferay.portal.kernel.spring.aop.Skip;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class SkipMethodInterceptorFactory implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

		return new SkipMethodInterceptor();
	}

	@Override
	public Class<Skip> getAnnotationClass() {
		return Skip.class;
	}

	private static final MethodInterceptor[] _emptyMethodInterceptors =
		new MethodInterceptor[0];

	private static class SkipMethodInterceptor
		extends AnnotatedMethodInterceptor<Skip> {

		@Override
		public Object before(MethodInvocation methodInvocation)
			throws Throwable {

			Skip skip = findAnnotation(methodInvocation);

			if (skip != null) {
				methodInterceptorCache.setMethodInterceptors(
					methodInvocation, _emptyMethodInterceptors);

				MethodInvocationImpl methodInvocationImpl =
					(MethodInvocationImpl)methodInvocation;

				methodInvocationImpl.setMethodInterceptors(
					_emptyMethodInterceptors);
			}

			return null;
		}

	}

}