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

package com.liferay.portal.increment;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.cache.thread.local.ThreadLocalCacheMethodInterceptorFactory;
import com.liferay.portal.kernel.increment.BufferedIncrement;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Zsolt Berentey
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class BufferedIncrementMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		return new BufferedIncrementAdvice();
	}

	@Override
	public Class<BufferedIncrement> getAnnotationClass() {
		return BufferedIncrement.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return ThreadLocalCacheMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}