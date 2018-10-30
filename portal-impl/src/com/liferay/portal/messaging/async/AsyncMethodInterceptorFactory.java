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

package com.liferay.portal.messaging.async;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.messaging.async.Async;
import com.liferay.portal.resiliency.service.PortalResiliencyMethodInterceptorFactory;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 * @author Preston Crary
 */
public class AsyncMethodInterceptorFactory implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new AsyncAdvice();
	}

	@Override
	public Class<Async> getAnnotationClass() {
		return Async.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return PortalResiliencyMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}