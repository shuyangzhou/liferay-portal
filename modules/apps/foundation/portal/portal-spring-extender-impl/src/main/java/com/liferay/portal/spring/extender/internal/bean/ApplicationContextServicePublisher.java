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

package com.liferay.portal.spring.extender.internal.bean;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.spring.aop.ServiceBeanAopProxy;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Miguel Pastor
 */
public class ApplicationContextServicePublisher {

	public ApplicationContextServicePublisher(
		ApplicationContext applicationContext, BundleContext bundleContext) {

		_applicationContext = applicationContext;
		_bundleContext = bundleContext;
	}

	public void register() {
		for (String beanName : _applicationContext.getBeanDefinitionNames()) {
			Object bean = null;

			try {
				bean = _applicationContext.getBean(beanName);
			}
			catch (BeanIsAbstractException biae) {
			}
			catch (Exception e) {
				_log.error("Unable to register service " + beanName, e);
			}

			if (bean != null) {
				registerService(bean);
			}
		}

		Bundle bundle = _bundleContext.getBundle();

		HashMapDictionary<String, Object> properties =
			new HashMapDictionary<>();

		properties.put(
			"org.springframework.context.service.name",
			bundle.getSymbolicName());

		ServiceRegistration<?> serviceRegistration =
			_bundleContext.registerService(
				ApplicationContext.class.getName(), _applicationContext,
				properties);

		_serviceRegistrations.add(serviceRegistration);
	}

	public void unregister() {
		for (ServiceRegistration<?> serviceReference : _serviceRegistrations) {
			serviceReference.unregister();
		}

		_serviceRegistrations.clear();
	}

	protected boolean isIgnoredInterface(String interfaceClassName) {
		for (String ignoredInterfaceClassName :
				PropsValues.MODULE_FRAMEWORK_SERVICES_IGNORED_INTERFACES) {

			if (!ignoredInterfaceClassName.startsWith(StringPool.EXCLAMATION) &&
				(ignoredInterfaceClassName.equals(interfaceClassName) ||
				 (ignoredInterfaceClassName.endsWith(StringPool.STAR) &&
				  interfaceClassName.startsWith(
					  ignoredInterfaceClassName.substring(
						  0, ignoredInterfaceClassName.length() - 1))))) {

				return true;
			}
		}

		return false;
	}

	protected void registerService(Object bean) {
		Set<Class<?>> interfaces = new LinkedHashSet<>();

		Class<?> clazz = bean.getClass();

		try {
			if (ProxyUtil.isProxyClass(clazz)) {
				AdvisedSupport advisedSupport =
					ServiceBeanAopProxy.getAdvisedSupport(bean);

				if (advisedSupport != null) {
					TargetSource targetSource =
						advisedSupport.getTargetSource();

					Object target = targetSource.getTarget();

					clazz = target.getClass();
				}
			}
		}
		catch (Exception e) {
			_log.error("Unable to register service " + bean, e);

			return;
		}

		Class<?>[] serviceClasses = null;

		OSGiBeanProperties osgiBeanProperties = AnnotationUtils.findAnnotation(
			clazz, OSGiBeanProperties.class);

		if (osgiBeanProperties != null) {
			serviceClasses = osgiBeanProperties.service();
		}

		if ((serviceClasses == null) || (serviceClasses.length == 0)) {
			Queue<Class<?>> queue = new ArrayDeque<>();

			queue.add(clazz);

			while (!queue.isEmpty()) {
				clazz = queue.remove();

				for (Class<?> interfaceClass : clazz.getInterfaces()) {
					interfaces.add(interfaceClass);

					queue.add(interfaceClass);
				}

				clazz = clazz.getSuperclass();

				if (clazz != null) {
					queue.add(clazz);
				}
			}
		}
		else {
			for (Class<?> serviceClazz : serviceClasses) {
				serviceClazz.cast(bean);

				interfaces.add(serviceClazz);
			}
		}

		interfaces.add(bean.getClass());

		List<String> names = new ArrayList<>(interfaces.size());

		for (Class<?> interfaceClass : interfaces) {
			String interfaceClassName = interfaceClass.getName();

			if (!isIgnoredInterface(interfaceClassName)) {
				names.add(interfaceClassName);
			}
		}

		if (names.isEmpty()) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Skipping registration because of an empty list of " +
						"interfaces");
			}

			return;
		}

		Bundle bundle = _bundleContext.getBundle();

		HashMapDictionary<String, Object> properties =
			new HashMapDictionary<>();

		properties.put("origin.bundle.symbolic.name", bundle.getSymbolicName());

		if (osgiBeanProperties != null) {
			properties.putAll(
				OSGiBeanProperties.Convert.toMap(osgiBeanProperties));
		}

		registerService(bean, names, properties);
	}

	protected void registerService(
		Object bean, List<String> interfaces,
		Dictionary<String, Object> properties) {

		ServiceRegistration<?> serviceRegistration =
			_bundleContext.registerService(
				interfaces.toArray(new String[interfaces.size()]), bean,
				properties);

		_serviceRegistrations.add(serviceRegistration);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ApplicationContextServicePublisher.class);

	private final ApplicationContext _applicationContext;
	private final BundleContext _bundleContext;
	private final Set<ServiceRegistration<?>> _serviceRegistrations =
		new HashSet<>();

}