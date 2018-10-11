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

import aQute.bnd.annotation.ProviderType;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Preston Crary
 */
@ProviderType
public interface MethodInterceptorCache {

	public void clear();

	public <T> T findAnnotation(
		MethodInvocation methodInvocation, Class<T> annotationType,
		T defaultValue);

	public MethodInterceptor[] getMethodInterceptors(
		MethodInvocation methodInvocation);

	public void removeMethodInterceptor(
		MethodInvocation methodInvocation, MethodInterceptor methodInterceptor);

	public void setMethodInterceptors(
		MethodInvocation methodInvocation,
		MethodInterceptor[] methodInterceptors);

}