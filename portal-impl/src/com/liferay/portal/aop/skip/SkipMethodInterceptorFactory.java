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

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.spring.aop.Skip;
import com.liferay.portal.spring.aop.SkipAdvice;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class SkipMethodInterceptorFactory implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new SkipAdvice();
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

}