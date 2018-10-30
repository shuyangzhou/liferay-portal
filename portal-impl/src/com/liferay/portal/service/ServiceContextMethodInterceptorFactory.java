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

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.systemevent.SystemEventMethodInterceptorFactory;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.annotation.Annotation;

/**
 * @author Preston Crary
 */
public class ServiceContextMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new ServiceContextAdvice();
	}

	@Override
	public Class<? extends Annotation> getAnnotationClass() {
		return null;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return SystemEventMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}