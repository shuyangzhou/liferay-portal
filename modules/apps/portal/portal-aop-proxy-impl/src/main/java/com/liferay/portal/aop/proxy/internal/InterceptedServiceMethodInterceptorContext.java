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

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.aop.proxy.InterceptedService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.spring.transaction.TransactionExecutor;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Objects;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * @author Preston Crary
 */
public class InterceptedServiceMethodInterceptorContext
	implements MethodInterceptorContext {

	public InterceptedServiceMethodInterceptorContext(
		Bundle bundle, Class<? extends InterceptedService> serviceClass,
		TransactionExecutor portalTransactionExecutor) {

		_bundle = bundle;
		_bundleContext = bundle.getBundleContext();
		_serviceClass = serviceClass;
		_portalTransactionExecutor = portalTransactionExecutor;
	}

	public void close() {
		for (ServiceReference<?> serviceReference : _serviceReferences) {
			_bundleContext.ungetService(serviceReference);
		}
	}

	@Override
	public <T> T getService(Class<T> serviceDependency) {
		try {
			return _getService(serviceDependency);
		}
		catch (InvalidSyntaxException ise) {
			return ReflectionUtil.throwException(ise);
		}
	}

	private <T> T _getService(Class<T> serviceDependency)
		throws InvalidSyntaxException {

		ServiceReference<?>[] serviceReferences =
			_bundleContext.getServiceReferences(
				serviceDependency.getName(),
				"(origin.bundle.symbolic.name=" + _bundle.getSymbolicName() +
					")");

		if ((serviceReferences != null) && (serviceReferences.length != 0)) {
			ServiceReference<T> serviceReference =
				(ServiceReference<T>)serviceReferences[0];

			T service = _bundleContext.getService(serviceReference);

			_serviceReferences.add(serviceReference);

			return service;
		}

		if (serviceDependency == TransactionExecutor.class) {
			if (_log.isInfoEnabled()) {
				Dictionary<String, String> headers = _bundle.getHeaders(
					StringPool.BLANK);

				if (Objects.equals(headers.get("Liferay-Service"), "true")) {
					_log.info(
						StringBundler.concat(
							_serviceClass, " is active before the bundle's ",
							"TransactionExecutor is available falling back to ",
							"the default TransactionExecutor"));
				}
			}

			return (T)_portalTransactionExecutor;
		}

		throw new IllegalStateException(
			StringBundler.concat(
				"Cannot find service dependency ", serviceDependency,
				" for service ", _serviceClass));
	}

	private static final Log _log = LogFactoryUtil.getLog(
		InterceptedServiceMethodInterceptorContext.class);

	private final Bundle _bundle;
	private final BundleContext _bundleContext;
	private final TransactionExecutor _portalTransactionExecutor;
	private final Class<? extends InterceptedService> _serviceClass;
	private final List<ServiceReference<?>> _serviceReferences =
		new ArrayList<>();

}