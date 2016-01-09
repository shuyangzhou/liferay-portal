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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.bean.ClassLoaderBeanHandler;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Brian Wing Shun Chan
 */
public class ProxyFactory {

	public static <T> T newDummyInstance(Class<T> interfaceClass) {
		return (T)ProxyUtil.newProxyInstance(
			interfaceClass.getClassLoader(), new Class[] {interfaceClass},
			new DummyInvocationHandler<T>());
	}

	public static Object newInstance(
			ClassLoader classLoader, Class<?> interfaceClass,
			String implClassName)
		throws Exception {

		return newInstance(
			classLoader, new Class[] {interfaceClass}, implClassName);
	}

	public static Object newInstance(
			ClassLoader classLoader, Class<?>[] interfaceClasses,
			String implClassName)
		throws Exception {

		Object instance = InstanceFactory.newInstance(
			classLoader, implClassName);

		return ProxyUtil.newProxyInstance(
			classLoader, interfaceClasses,
			new ClassLoaderBeanHandler(instance, classLoader));
	}

	public static <T> T newServiceTrackedInstance(Class<T> interfaceClass) {
		return (T)ProxyUtil.newProxyInstance(
			interfaceClass.getClassLoader(), new Class[] {interfaceClass},
			new ServiceTrackedInvocationHandler<>(interfaceClass));
	}

	public static <T> T newServiceTrackedInstance(
		Class<T> interfaceClass, boolean waitForInitialization) {

		return (T)ProxyUtil.newProxyInstance(
			interfaceClass.getClassLoader(), new Class[] {interfaceClass},
			new ServiceTrackedInvocationHandler<>(
				interfaceClass, waitForInitialization));
	}

	private static class DummyInvocationHandler<T>
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {

			Class<?> returnType = method.getReturnType();

			if (returnType.equals(boolean.class)) {
				return GetterUtil.DEFAULT_BOOLEAN;
			}
			else if (returnType.equals(byte.class)) {
				return GetterUtil.DEFAULT_BYTE;
			}
			else if (returnType.equals(double.class)) {
				return GetterUtil.DEFAULT_DOUBLE;
			}
			else if (returnType.equals(float.class)) {
				return GetterUtil.DEFAULT_FLOAT;
			}
			else if (returnType.equals(int.class)) {
				return GetterUtil.DEFAULT_INTEGER;
			}
			else if (returnType.equals(long.class)) {
				return GetterUtil.DEFAULT_LONG;
			}
			else if (returnType.equals(short.class)) {
				return GetterUtil.DEFAULT_SHORT;
			}

			return method.getDefaultValue();
		}

	}

	private static class ServiceTrackedInvocationHandler<T>
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {

			if (_waitForInitialization && !_initialized.get()) {
				while (_serviceTracker.getService() == null) {
					Thread.sleep(500);
				}
			}

			T service = _serviceTracker.getService();

			if (service != null) {
				try {
					return method.invoke(service, arguments);
				}
				catch (InvocationTargetException ite) {
					throw ite.getTargetException();
				}
			}

			Class<?> returnType = method.getReturnType();

			if (returnType.equals(boolean.class)) {
				return GetterUtil.DEFAULT_BOOLEAN;
			}
			else if (returnType.equals(byte.class)) {
				return GetterUtil.DEFAULT_BYTE;
			}
			else if (returnType.equals(double.class)) {
				return GetterUtil.DEFAULT_DOUBLE;
			}
			else if (returnType.equals(float.class)) {
				return GetterUtil.DEFAULT_FLOAT;
			}
			else if (returnType.equals(int.class)) {
				return GetterUtil.DEFAULT_INTEGER;
			}
			else if (returnType.equals(long.class)) {
				return GetterUtil.DEFAULT_LONG;
			}
			else if (returnType.equals(short.class)) {
				return GetterUtil.DEFAULT_SHORT;
			}

			return method.getDefaultValue();
		}

		private ServiceTrackedInvocationHandler(Class<T> interfaceClass) {
			this(interfaceClass, false);
		}

		private ServiceTrackedInvocationHandler(
			Class<T> interfaceClass, boolean waitForInitialization) {

			_waitForInitialization = waitForInitialization;
			Registry registry = RegistryUtil.getRegistry();

			if (!waitForInitialization) {
				_serviceTracker = registry.trackServices(interfaceClass);
			}
			else {
				_serviceTracker = registry.trackServices(
					interfaceClass,
					new InitializationAwareServiceTrackerCustomizer());
			}

			_serviceTracker.open();
		}

		private final AtomicBoolean _initialized = new AtomicBoolean(false);
		private final ServiceTracker<T, T> _serviceTracker;
		private final boolean _waitForInitialization;

		private class InitializationAwareServiceTrackerCustomizer
			implements ServiceTrackerCustomizer<T, T> {

			@Override
			public T addingService(ServiceReference<T> serviceReference) {
				_initialized.set(true);

				return null;
			}

			@Override
			public void modifiedService(
				ServiceReference<T> serviceReference, T messageBus) {
			}

			@Override
			public void removedService(
				ServiceReference<T> serviceReference, T service) {
			}

		}

	}

}