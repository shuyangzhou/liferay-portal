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

package com.liferay.portal.internal.cluster;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.aop.skip.SkipMethodInterceptorFactory;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.util.PropsValues;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class ClusterableMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		if (SPIUtil.isSPI()) {
			return new SPIClusterableAdvice();
		}

		return new ClusterableAdvice();
	}

	@Override
	public Class<Clusterable> getAnnotationClass() {
		return Clusterable.class;
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return SkipMethodInterceptorFactory.class;
	}

	@Override
	public boolean isEnabled() {
		if (SPIUtil.isSPI()) {
			return true;
		}

		if (PropsValues.CLUSTER_LINK_ENABLED) {
			return true;
		}

		return false;
	}

}