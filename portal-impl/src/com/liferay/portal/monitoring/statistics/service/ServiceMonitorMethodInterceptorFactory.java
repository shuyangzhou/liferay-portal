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

package com.liferay.portal.monitoring.statistics.service;

import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;
import com.liferay.portal.resiliency.service.PortalResiliencyMethodInterceptorFactory;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Michael C. Han
 * @author Preston Crary
 */
public class ServiceMonitorMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorContext methodInterceptorContext) {

		ServiceMonitorAdvice serviceMonitorAdvice = new ServiceMonitorAdvice();

		serviceMonitorAdvice.setServiceMonitoringControl(
			methodInterceptorContext.getService(
				ServiceMonitoringControl.class));

		return serviceMonitorAdvice;
	}

	@Override
	public Class<? extends Annotation> getAnnotationClass() {
		return null;
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