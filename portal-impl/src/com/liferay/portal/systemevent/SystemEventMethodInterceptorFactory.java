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

package com.liferay.portal.systemevent;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.search.IndexableMethodInterceptorFactory;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Zsolt Berentey
 * @author Preston Crary
 */
public class SystemEventMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new SystemEventAdvice();
	}

	@Override
	public Class<SystemEvent> getAnnotationClass() {
		return SystemEvent.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return IndexableMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}