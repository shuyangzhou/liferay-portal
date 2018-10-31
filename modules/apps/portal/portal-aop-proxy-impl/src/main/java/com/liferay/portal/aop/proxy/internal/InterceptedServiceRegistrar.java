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

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.aop.proxy.InterceptedService;
import com.liferay.portal.aop.proxy.InterceptedServiceConfiguration;
import com.liferay.portal.aop.spi.MethodInterceptorCacheManager;
import com.liferay.portal.aop.spi.MethodInterceptorProxyImpl;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.transaction.TransactionExecutor;

import java.lang.reflect.Field;

import java.util.ArrayDeque;
import java.util.Dictionary;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Preston Crary
 */
@Component(immediate = true, service = {})
public class InterceptedServiceRegistrar
	implements ServiceTrackerCustomizer
		<InterceptedService, InterceptedServiceBag> {

	@Override
	public InterceptedServiceBag addingService(
		ServiceReference<InterceptedService> serviceReference) {

		InterceptedService interceptedService = _bundleContext.getService(
			serviceReference);

		Class<? extends InterceptedService> serviceClass =
			interceptedService.getClass();

		Class<?>[] interfaces = _getInterfaces(serviceClass);

		InterceptedServiceMethodInterceptorContext
			interceptedServiceMethodInterceptorContext =
				new InterceptedServiceMethodInterceptorContext(
					serviceReference.getBundle(), serviceClass,
					_portalTransactionExecutor);

		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(
				interceptedServiceMethodInterceptorContext);

		Object proxy = ProxyUtil.newProxyInstance(
			serviceClass.getClassLoader(), interfaces,
			new MethodInterceptorProxyImpl(
				interceptedService, interfaces, methodInterceptorCache));

		String proxyReference = (String)serviceReference.getProperty(
			InterceptedService.PROXY_REFERENCE_FIELD);

		if (proxyReference != null) {
			try {
				Field field = serviceClass.getField(proxyReference);

				field.setAccessible(true);

				field.set(interceptedService, proxy);
			}
			catch (ReflectiveOperationException roe) {
				MethodInterceptorCacheManager.destroy(methodInterceptorCache);

				interceptedServiceMethodInterceptorContext.close();

				throw new RuntimeException(roe);
			}
		}

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		for (String key : serviceReference.getPropertyKeys()) {
			if (!key.equals(InterceptedService.PROXY_REFERENCE_FIELD)) {
				properties.put(key, serviceReference.getProperty(key));
			}
		}

		String[] classNames = new String[interfaces.length];

		for (int i = 0; i < interfaces.length; i++) {
			classNames[i] = interfaces[i].getName();
		}

		return new InterceptedServiceBag(
			interceptedServiceMethodInterceptorContext, methodInterceptorCache,
			_bundleContext.registerService(classNames, proxy, properties));
	}

	@Override
	public void modifiedService(
		ServiceReference<InterceptedService> serviceReference,
		InterceptedServiceBag interceptedServiceBag) {
	}

	@Override
	public void removedService(
		ServiceReference<InterceptedService> serviceReference,
		InterceptedServiceBag interceptedServiceBag) {

		interceptedServiceBag.close();

		_bundleContext.ungetService(serviceReference);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = new ServiceTracker<>(
			bundleContext, InterceptedService.class, this);

		_serviceTracker.open();
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	private Class<?>[] _getInterfaces(
		Class<? extends InterceptedService> serviceClass) {

		InterceptedServiceConfiguration interceptedServiceConfiguration =
			serviceClass.getAnnotation(InterceptedServiceConfiguration.class);

		if (interceptedServiceConfiguration != null) {
			Class<?>[] interfaces = interceptedServiceConfiguration.service();

			if (interfaces.length > 0) {
				for (Class<?> interfaceClass : interfaces) {
					if (!interfaceClass.isInterface()) {
						throw new IllegalArgumentException(
							StringBundler.concat(
								"Cannot proxy ", serviceClass, " because ",
								interfaceClass, " is not an interface"));
					}
				}

				return interfaces;
			}
		}

		Set<Class<?>> interfaces = new LinkedHashSet<>();

		Queue<Class<?>> queue = new ArrayDeque<>();

		queue.add(serviceClass);

		while (!queue.isEmpty()) {
			Class<?> clazz = queue.remove();

			for (Class<?> interfaceClass : clazz.getInterfaces()) {
				interfaces.add(interfaceClass);

				queue.add(interfaceClass);
			}

			clazz = clazz.getSuperclass();

			if (clazz != null) {
				if (clazz.isInterface()) {
					interfaces.add(clazz);
				}

				queue.add(clazz);
			}
		}

		interfaces.remove(InterceptedService.class);

		return interfaces.toArray(new Class<?>[interfaces.size()]);
	}

	private BundleContext _bundleContext;

	@Reference(target = "(original.bean=true)")
	private TransactionExecutor _portalTransactionExecutor;

	private ServiceTracker<?, ?> _serviceTracker;

}