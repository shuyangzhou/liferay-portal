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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.ServiceTrackerFieldUpdaterCustomizer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Tina Tian
 */
public class ServiceProxyFactory {

	public static <T> T newServiceTrackedInstance(
		Class<T> serviceClass, Class<?> declaringClass, String fieldName,
		boolean blocking) {

		return newServiceTrackedInstance(
			serviceClass, declaringClass, fieldName, null, blocking);
	}

	public static <T> T newServiceTrackedInstance(
		Class<T> serviceClass, Class<?> declaringClass, String fieldName,
		String filterString, boolean blocking) {

		try {
			Field field = declaringClass.getDeclaredField(fieldName);

			if (!Modifier.isStatic(field.getModifiers())) {
				throw new IllegalArgumentException(field + " is not static");
			}

			field.setAccessible(true);

			ServiceTrackerCustomizer<?, ?> serviceTrackerCustomizer = null;

			if (blocking) {
				ReentrantLock lock = new ReentrantLock();

				Condition isRealServiceSet = lock.newCondition();

				T awaitService = (T)ProxyUtil.newProxyInstance(
					serviceClass.getClassLoader(), new Class[] {serviceClass},
					new AwaitServiceInvocationHandler(
						field, isRealServiceSet, serviceClass, lock));

				field.set(null, awaitService);

				serviceTrackerCustomizer =
					new AwaitServiceTrackerFieldUpdaterCustomizer<>(
						field, null, awaitService, isRealServiceSet, lock);
			}
			else {
				T dummyService = ProxyFactory.newDummyInstance(serviceClass);

				field.set(null, dummyService);

				serviceTrackerCustomizer =
					new ServiceTrackerFieldUpdaterCustomizer<>(
						field, null, dummyService);
			}

			return _newServiceTrackedInstance(
				serviceClass, field, filterString, serviceTrackerCustomizer);
		}
		catch (ReflectiveOperationException roe) {
			return ReflectionUtil.throwException(roe);
		}
	}

	private static <T> T _newServiceTrackedInstance(
			Class<T> serviceClass, Field field, String filterString,
			ServiceTrackerCustomizer<?, ?> serviceTrackerCustomizer)
		throws ReflectiveOperationException {

		ServiceTracker<?, ?> serviceTracker = null;

		String serviceName = serviceClass.getName();

		Registry registry = RegistryUtil.getRegistry();

		if (Validator.isNull(filterString)) {
			serviceTracker = registry.trackServices(
				serviceName, serviceTrackerCustomizer);
		}
		else {
			StringBundler sb = new StringBundler(7);

			sb.append("(&(objectClass=");
			sb.append(serviceName);
			sb.append(StringPool.CLOSE_PARENTHESIS);

			if (!filterString.startsWith(StringPool.OPEN_PARENTHESIS)) {
				sb.append(StringPool.OPEN_PARENTHESIS);
			}

			sb.append(filterString);

			if (!filterString.endsWith(StringPool.CLOSE_PARENTHESIS)) {
				sb.append(StringPool.CLOSE_PARENTHESIS);
			}

			sb.append(StringPool.CLOSE_PARENTHESIS);

			Filter filter = registry.getFilter(sb.toString());

			serviceTracker = registry.trackServices(
				filter, serviceTrackerCustomizer);
		}

		serviceTracker.open();

		return (T)field.get(null);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ServiceProxyFactory.class);

	private static class AwaitServiceInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] arguments)
			throws Throwable {

			while (true) {
				_lock.lock();

				try {
					Object service = _field.get(null);

					if (!ProxyUtil.isProxyClass(service.getClass()) ||
						(ProxyUtil.getInvocationHandler(service) != this)) {

						return method.invoke(service, arguments);
					}

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Waiting for a real service instance " +
								_interfaceClass.getName());
					}

					_isRealServiceSet.await();
				}
				finally {
					_lock.unlock();
				}
			}
		}

		private AwaitServiceInvocationHandler(
			Field field, Condition isRealServiceSet, Class<?> interfaceClass,
			Lock lock) {

			_field = field;
			_isRealServiceSet = isRealServiceSet;
			_interfaceClass = interfaceClass;
			_lock = lock;
		}

		private final Field _field;
		private final Class<?> _interfaceClass;
		private final Condition _isRealServiceSet;
		private final Lock _lock;

	}

	private static class AwaitServiceTrackerFieldUpdaterCustomizer<S, T>
		extends ServiceTrackerFieldUpdaterCustomizer<S, T> {

		public AwaitServiceTrackerFieldUpdaterCustomizer(
			Field serviceField, Object serviceHolder, T dummyTrackedService,
			Condition isRealServiceSet, Lock lock) {

			super(serviceField, serviceHolder, dummyTrackedService);

			_dummyTrackedService = dummyTrackedService;
			_isRealServiceSet = isRealServiceSet;
			_lock = lock;
		}

		@Override
		protected void doServiceUpdate(T newService) {
			_lock.lock();

			try {
				super.doServiceUpdate(newService);

				if (newService != _dummyTrackedService) {
					_isRealServiceSet.signalAll();
				}
			}
			finally {
				_lock.unlock();
			}
		}

		private final T _dummyTrackedService;
		private final Condition _isRealServiceSet;
		private final Lock _lock;

	}

}