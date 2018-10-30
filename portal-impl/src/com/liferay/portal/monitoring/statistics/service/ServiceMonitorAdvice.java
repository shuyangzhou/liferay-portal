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

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.aop.BaseMethodInterceptor;
import com.liferay.portal.kernel.monitoring.DataSample;
import com.liferay.portal.kernel.monitoring.DataSampleThreadLocal;
import com.liferay.portal.kernel.monitoring.MethodSignature;
import com.liferay.portal.kernel.monitoring.RequestStatus;
import com.liferay.portal.kernel.monitoring.ServiceMonitoringControl;

import java.lang.reflect.Method;

import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Michael C. Han
 */
public class ServiceMonitorAdvice extends BaseMethodInterceptor {

	@Override
	protected void afterReturning(
			MethodInvocation methodInvocation, Object result)
		throws Throwable {

		DataSample dataSample = _dataSampleThreadLocal.get();

		if (dataSample != null) {
			dataSample.capture(RequestStatus.SUCCESS);
		}
	}

	@Override
	protected void afterThrowing(
			MethodInvocation methodInvocation, Throwable throwable)
		throws Throwable {

		DataSample dataSample = _dataSampleThreadLocal.get();

		if (dataSample != null) {
			dataSample.capture(RequestStatus.ERROR);
		}
	}

	@Override
	protected Object before(MethodInvocation methodInvocation)
		throws Throwable {

		if (!_serviceMonitoringControl.isMonitorServiceRequest()) {
			return null;
		}

		boolean included = _isIncluded(methodInvocation);

		boolean inclusiveMode = _serviceMonitoringControl.isInclusiveMode();

		if ((!inclusiveMode && included) || (inclusiveMode && !included)) {
			return null;
		}

		MethodSignature methodSignature = new MethodSignature(
			methodInvocation.getMethod());

		DataSample dataSample =
			DataSampleFactoryUtil.createServiceRequestDataSample(
				methodSignature);

		dataSample.prepare();

		_dataSampleThreadLocal.set(dataSample);

		DataSampleThreadLocal.initialize();

		return null;
	}

	@Override
	protected void duringFinally(MethodInvocation methodInvocation) {
		DataSample dataSample = _dataSampleThreadLocal.get();

		if (dataSample != null) {
			_dataSampleThreadLocal.remove();

			DataSampleThreadLocal.addDataSample(dataSample);
		}
	}

	protected void setServiceMonitoringControl(
		ServiceMonitoringControl serviceMonitoringControl) {

		_serviceMonitoringControl = serviceMonitoringControl;
	}

	private boolean _isIncluded(MethodInvocation methodInvocation) {
		Method method = methodInvocation.getMethod();

		Class<?> declaringClass = method.getDeclaringClass();

		String className = declaringClass.getName();

		Set<String> serviceClasses =
			_serviceMonitoringControl.getServiceClasses();

		if (serviceClasses.contains(className)) {
			return true;
		}

		MethodSignature methodSignature = new MethodSignature(method);

		Set<MethodSignature> serviceClassMethods =
			_serviceMonitoringControl.getServiceClassMethods();

		if (serviceClassMethods.contains(methodSignature)) {
			return true;
		}

		return false;
	}

	private static final ThreadLocal<DataSample> _dataSampleThreadLocal =
		new CentralizedThreadLocal<>(
			ServiceMonitorAdvice.class + "._dataSampleThreadLocal");

	private ServiceMonitoringControl _serviceMonitoringControl;

}