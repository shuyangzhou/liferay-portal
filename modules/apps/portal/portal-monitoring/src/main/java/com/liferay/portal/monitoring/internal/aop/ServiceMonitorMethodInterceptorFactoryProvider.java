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

package com.liferay.portal.monitoring.internal.aop;

import com.liferay.portal.resiliency.service.PortalResiliencyMethodInterceptorFactory;
import com.liferay.portal.spring.aop.MethodInterceptorFactory;
import com.liferay.portal.spring.aop.MethodInterceptorFactoryProvider;

import org.osgi.service.component.annotations.Component;

/**
 * @author Preston Crary
 */
@Component(
	enabled = false, immediate = true,
	service = MethodInterceptorFactoryProvider.class
)
public class ServiceMonitorMethodInterceptorFactoryProvider
	implements MethodInterceptorFactoryProvider {

	@Override
	public MethodInterceptorFactory getMethodInterceptorFactory() {
		return new ServiceMonitorMethodInterceptorFactory();
	}

	@Override
	public Class<? extends MethodInterceptorFactory> getParentClass() {
		return PortalResiliencyMethodInterceptorFactory.class;
	}

}