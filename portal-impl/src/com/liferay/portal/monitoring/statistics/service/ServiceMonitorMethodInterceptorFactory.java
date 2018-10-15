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

import com.liferay.portal.kernel.monitoring.DataSample;
import com.liferay.portal.kernel.monitoring.DataSampleThreadLocal;
import com.liferay.portal.kernel.monitoring.MethodSignature;
import com.liferay.portal.kernel.monitoring.RequestStatus;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;
import com.liferay.portal.spring.aop.MethodInterceptorFactory;
import com.liferay.portal.spring.aop.MethodInterceptorFactoryHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Michael C. Han
 * @author Preston Crary
 */
public class ServiceMonitorMethodInterceptorFactory
	implements MethodInterceptorFactory {

	@Override
	public MethodInterceptor create(
		MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

		return new ServiceMonitorMethodInterceptor(
			methodInterceptorFactoryHelper.getService(
				ServiceMonitoringControl.class));
	}

	@Override
	public Class<? extends Annotation> getAnnotationClass() {
		return null;
	}

	private static class ServiceMonitorMethodInterceptor
		implements MethodInterceptor {

		@Override
		public Object invoke(MethodInvocation methodInvocation)
			throws Throwable {

			if (!_serviceMonitoringControl.isMonitorServiceRequest()) {
				return methodInvocation.proceed();
			}

			Method method = methodInvocation.getMethod();

			MethodSignature methodSignature = new MethodSignature(method);

			boolean included = false;

			Class<?> declaringClass = method.getDeclaringClass();

			Set<String> serviceClasses =
				_serviceMonitoringControl.getServiceClasses();

			if (serviceClasses.contains(declaringClass.getName())) {
				included = true;
			}
			else {
				Set<MethodSignature> serviceClassMethods =
					_serviceMonitoringControl.getServiceClassMethods();

				if (serviceClassMethods.contains(methodSignature)) {
					included = true;
				}
			}

			boolean inclusiveMode = _serviceMonitoringControl.isInclusiveMode();

			if ((!inclusiveMode && included) || (inclusiveMode && !included)) {
				return methodInvocation.proceed();
			}

			DataSample dataSample =
				DataSampleFactoryUtil.createServiceRequestDataSample(
					methodSignature);

			dataSample.prepare();

			DataSampleThreadLocal.initialize();

			Object returnValue = null;

			try {
				returnValue = methodInvocation.proceed();

				dataSample.capture(RequestStatus.SUCCESS);
			}
			catch (Throwable throwable) {
				dataSample.capture(RequestStatus.ERROR);

				throw throwable;
			}
			finally {
				DataSampleThreadLocal.addDataSample(dataSample);
			}

			return returnValue;
		}

		private ServiceMonitorMethodInterceptor(
			ServiceMonitoringControl serviceMonitoringControl) {

			_serviceMonitoringControl = serviceMonitoringControl;
		}

		private final ServiceMonitoringControl _serviceMonitoringControl;

	}

}