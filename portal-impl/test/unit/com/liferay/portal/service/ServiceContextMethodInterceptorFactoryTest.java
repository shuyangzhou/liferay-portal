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

package com.liferay.portal.service;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.spring.aop.BaseMethodInterceptor;
import com.liferay.portal.spring.aop.MethodInterceptorCache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class ServiceContextMethodInterceptorFactoryTest {

	@Before
	public void setUp() {
		_testMethodInterceptorCache = new TestMethodInterceptorCache();

		ServiceContextMethodInterceptorFactory
			serviceContextMethodInterceptorFactory =
				new ServiceContextMethodInterceptorFactory();

		_serviceContextMethodInterceptor =
			(BaseMethodInterceptor)
				serviceContextMethodInterceptorFactory.create(null);

		_serviceContextMethodInterceptor.setMethodInterceptorCache(
			_testMethodInterceptorCache);

		_serviceContext = new ServiceContext();

		ServiceContextThreadLocal.pushServiceContext(_serviceContext);
	}

	@Test
	public void testThreadLocalValue() throws Throwable {
		MethodInvocation methodInvocation = createMethodInvocation(
			new Object[] {new ServiceContext()},
			new Class<?>[] {ServiceContext.class}, false);

		_serviceContextMethodInterceptor.invoke(methodInvocation);

		Assert.assertFalse(
			_testMethodInterceptorCache._removedMethodInterceptor);
	}

	@Test
	public void testWithNoArguments() throws Throwable {
		MethodInvocation methodInvocation = createMethodInvocation(
			new Object[0], new Class<?>[0], true);

		_serviceContextMethodInterceptor.invoke(methodInvocation);

		Assert.assertTrue(
			_testMethodInterceptorCache._removedMethodInterceptor);
	}

	@Test
	public void testWithoutServiceContextParameter() throws Throwable {
		MethodInvocation methodInvocation = createMethodInvocation(
			new Object[] {null}, new Class<?>[] {Object.class}, true);

		_serviceContextMethodInterceptor.invoke(methodInvocation);

		Assert.assertTrue(
			_testMethodInterceptorCache._removedMethodInterceptor);
	}

	@Test
	public void testWithServiceContextWrapper() throws Throwable {
		MethodInvocation methodInvocation = createMethodInvocation(
			new Object[] {new TestServiceContextWrapper()},
			new Class<?>[] {TestServiceContextWrapper.class}, false);

		_serviceContextMethodInterceptor.invoke(methodInvocation);

		Assert.assertFalse(
			_testMethodInterceptorCache._removedMethodInterceptor);
	}

	protected MethodInvocation createMethodInvocation(
			final Object[] arguments, Class<?>[] parameterTypes,
			final boolean expectedOriginalServiceContext)
		throws Throwable {

		final Method method = ReflectionTestUtil.getMethod(
			TestInterceptedClass.class, "method", parameterTypes);

		return new MethodInvocation() {

			@Override
			public Object[] getArguments() {
				return arguments;
			}

			@Override
			public Method getMethod() {
				return method;
			}

			@Override
			public AccessibleObject getStaticPart() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object getThis() {
				throw new UnsupportedOperationException();
			}

			@Override
			public Object proceed() throws Throwable {
				ServiceContext serviceContext =
					ServiceContextThreadLocal.getServiceContext();

				Assert.assertEquals(
					expectedOriginalServiceContext,
					_serviceContext == serviceContext);

				return null;
			}

		};
	}

	private ServiceContext _serviceContext;
	private BaseMethodInterceptor _serviceContextMethodInterceptor;
	private TestMethodInterceptorCache _testMethodInterceptorCache;

	private static class TestInterceptedClass {

		@SuppressWarnings("unused")
		public void method() {
		}

		@SuppressWarnings("unused")
		public void method(Object obj) {
		}

		@SuppressWarnings("unused")
		public void method(ServiceContext serviceContext) {
		}

		@SuppressWarnings("unused")
		public void method(TestServiceContextWrapper serviceContextWrapper) {
		}

	}

	private static class TestServiceContextWrapper extends ServiceContext {
	}

	private class TestMethodInterceptorCache implements MethodInterceptorCache {

		@Override
		public void clear() {
		}

		@Override
		public <T> T findAnnotation(
			MethodInvocation methodInvocation, Class<T> annotationType,
			T defaultValue) {

			return null;
		}

		@Override
		public MethodInterceptor[] getMethodInterceptors(
			MethodInvocation methodInvocation) {

			return new MethodInterceptor[] {_serviceContextMethodInterceptor};
		}

		@Override
		public void removeMethodInterceptor(
			MethodInvocation methodInvocation,
			MethodInterceptor methodInterceptor) {

			Assert.assertSame(
				_serviceContextMethodInterceptor, methodInterceptor);

			_removedMethodInterceptor = true;
		}

		@Override
		public void setMethodInterceptors(
			MethodInvocation methodInvocation,
			MethodInterceptor[] methodInterceptors) {
		}

		private boolean _removedMethodInterceptor;

	}

}