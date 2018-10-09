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

package com.liferay.portal.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.spring.aop.BaseMethodInterceptor;
import com.liferay.portal.spring.aop.MethodInterceptorFactory;
import com.liferay.portal.spring.aop.MethodInterceptorFactoryHelper;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Preston Crary
 */
public class ServiceContextMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

		return new ServiceContextMethodInterceptor();
	}

	@Override
	public Class<? extends Annotation> getAnnotationClass() {
		return null;
	}

	private static class ServiceContextMethodInterceptor
		extends BaseMethodInterceptor {

		@Override
		public Object invoke(MethodInvocation methodInvocation)
			throws Throwable {

			int index = _getServiceContextParameterIndex(
				methodInvocation.getMethod());

			if (index < 0) {
				methodInterceptorCache.removeMethodInterceptor(
					methodInvocation, this);

				return methodInvocation.proceed();
			}

			Object[] arguments = methodInvocation.getArguments();

			ServiceContext serviceContext = (ServiceContext)arguments[index];

			if (serviceContext != null) {
				ServiceContextThreadLocal.pushServiceContext(serviceContext);
			}

			try {
				return methodInvocation.proceed();
			}
			finally {
				if (serviceContext != null) {
					ServiceContextThreadLocal.popServiceContext();
				}
			}
		}

		private int _getServiceContextParameterIndex(Method method) {
			Class<?>[] parameterTypes = method.getParameterTypes();

			for (int i = parameterTypes.length - 1; i >= 0; i--) {
				if (ServiceContext.class.isAssignableFrom(parameterTypes[i])) {
					return i;
				}
			}

			return -1;
		}

	}

}