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

package com.liferay.portal.aop.proxy.internal;

import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.aop.spi.MethodInterceptorCacheManager;

import org.osgi.framework.ServiceRegistration;

/**
 * @author Preston Crary
 */
public class InterceptedServiceBag {

	public InterceptedServiceBag(
		InterceptedServiceMethodInterceptorContext
			serviceMethodInterceptorContext,
		MethodInterceptorCache methodInterceptorCache,
		ServiceRegistration<?> serviceRegistration) {

		_serviceMethodInterceptorContext = serviceMethodInterceptorContext;
		_methodInterceptorCache = methodInterceptorCache;
		_serviceRegistration = serviceRegistration;
	}

	public void close() {
		_serviceRegistration.unregister();

		MethodInterceptorCacheManager.destroy(_methodInterceptorCache);

		_serviceMethodInterceptorContext.close();
	}

	private final MethodInterceptorCache _methodInterceptorCache;
	private final InterceptedServiceMethodInterceptorContext
		_serviceMethodInterceptorContext;
	private final ServiceRegistration<?> _serviceRegistration;

}