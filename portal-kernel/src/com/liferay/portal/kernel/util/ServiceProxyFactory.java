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
import com.liferay.registry.ServiceTrackerFieldUpdaterCustomizer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.concurrent.CountDownLatch;

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

			T placeHolderService = null;
			ResetableCountDownLatch resetableCountDownLatch =
				new ResetableCountDownLatch();

			if (blocking) {
				placeHolderService = (T)ProxyUtil.newProxyInstance(
					serviceClass.getClassLoader(), new Class[] {serviceClass},
					new AwaitServiceInvocationHandler(
						field, resetableCountDownLatch));
			}
			else {
				placeHolderService = ProxyFactory.newDummyInstance(
					serviceClass);
			}

			field.set(null, placeHolderService);

			return _newServiceTrackedInstance(
				serviceClass, field, filterString, resetableCountDownLatch,
				placeHolderService);
		}
		catch (ReflectiveOperationException roe) {
			return ReflectionUtil.throwException(roe);
		}
	}

	private static <T> T _newServiceTrackedInstance(
			Class<T> serviceClass, Field field, String filterString,
			ResetableCountDownLatch resetableCountDownLatch,
			T placeHolderService)
		throws ReflectiveOperationException {

		ServiceTracker<?, ?> serviceTracker = null;

		String serviceName = serviceClass.getName();

		Registry registry = RegistryUtil.getRegistry();

		if (Validator.isNull(filterString)) {
			serviceTracker = registry.trackServices(
				serviceName,
				new AwaitServiceTrackerFieldUpdaterCustomizer<>(
					field, null, resetableCountDownLatch, placeHolderService));
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
				filter,
				new AwaitServiceTrackerFieldUpdaterCustomizer<>(
					field, null, resetableCountDownLatch, placeHolderService));
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

			_resetableCountDownLatch.await();

			Object service = _field.get(null);

			return method.invoke(service, arguments);
		}

		private AwaitServiceInvocationHandler(
			Field field, ResetableCountDownLatch resetableCountDownLatch) {

			_field = field;
			_resetableCountDownLatch = resetableCountDownLatch;
		}

		private final Field _field;
		private final ResetableCountDownLatch _resetableCountDownLatch;

	}

	private static class AwaitServiceTrackerFieldUpdaterCustomizer<S, T>
		extends ServiceTrackerFieldUpdaterCustomizer<S, T> {

		public AwaitServiceTrackerFieldUpdaterCustomizer(
			Field serviceField, Object serviceHolder,
			ResetableCountDownLatch resetableCountDownLatch,
			T dummyTrackedService) {

			super(serviceField, serviceHolder, dummyTrackedService);

			_resetableCountDownLatch = resetableCountDownLatch;
			_dummyTrackedService = dummyTrackedService;
		}

		@Override
		protected void afterServiceUpdate(T oldService, T newService) {
			if (newService != _dummyTrackedService) {
				_resetableCountDownLatch.countDown();
			}
		}

		@Override
		protected void beforeServiceUpdate(T oldService, T newService) {
			if (newService == _dummyTrackedService) {
				_resetableCountDownLatch.reset();
			}
		}

		private final T _dummyTrackedService;
		private final ResetableCountDownLatch _resetableCountDownLatch;

	}

	private static class ResetableCountDownLatch {

		public void await() throws InterruptedException {
			CountDownLatch countDownLatch = _countDownLatch;

			countDownLatch.await();
		}

		public void countDown() {
			CountDownLatch countDownLatch = _countDownLatch;

			countDownLatch.countDown();
		}

		public void reset() {
			_countDownLatch = new CountDownLatch(1);
		}

		private volatile CountDownLatch _countDownLatch = new CountDownLatch(1);

	}

}