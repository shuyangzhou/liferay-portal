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

package com.liferay.portal.aop.proxy;

/**
 * Marker interface used to declare a service should be intercepted and proxied
 * by MethodInterceptors and registered as all of service's interfaces.
 *
 * <p>
 * It's important that services required to be intercepted are only registered
 * as a <code>InterceptedService</code> so service listeners do not see the
 * service before it is intercepted.
 * </p>
 *
 * @author Preston Crary
 * @see com.liferay.portal.aop.MethodInterceptorFactory
 */
public interface InterceptedService {

	/**
	 * Optional property allowing an <code>InterceptedService</code> to have a
	 * reference field to it's own proxy.
	 */
	public static final String PROXY_REFERENCE_FIELD = "proxy.reference.field";

}