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

import com.liferay.portal.aop.context.MethodInterceptorContext;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Preston Crary
 */
public interface MethodInterceptorFactory {

	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext);

	public Class<? extends Annotation> getAnnotationClass();

	public Class<? extends MethodInterceptorFactory> getParentClass();

	public boolean isEnabled();

}