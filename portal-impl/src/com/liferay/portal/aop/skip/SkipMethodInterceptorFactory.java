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

package com.liferay.portal.aop.skip;

import com.liferay.portal.aop.AnnotatedMethodInterceptor;
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.internal.aop.MethodInvocationImpl;
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
		MethodInterceptorContext methodInterceptorContext) {

		return new SkipMethodInterceptor();
	}

	@Override
	public Class<Skip> getAnnotationClass() {
		return Skip.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static class SkipMethodInterceptor
		extends AnnotatedMethodInterceptor<Skip> {

		@Override
		protected Object before(MethodInvocation methodInvocation)
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

		private static final MethodInterceptor[] _emptyMethodInterceptors =
			new MethodInterceptor[0];

	}

}