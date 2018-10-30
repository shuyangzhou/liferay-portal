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

package com.liferay.portal.aop.retry;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.spring.aop.Retry;
import com.liferay.portal.service.ServiceContextMethodInterceptorFactory;
import com.liferay.portal.spring.aop.RetryAdvice;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Matthew Tambara
 * @author Preston Crary
 */
public class RetryMethodInterceptorFactory implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new RetryAdvice();
	}

	@Override
	public Class<Retry> getAnnotationClass() {
		return Retry.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return ServiceContextMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}