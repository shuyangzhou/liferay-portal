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

package com.liferay.portal.aop.spi;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.MethodInterceptorFactory;
import com.liferay.portal.aop.cache.MethodInterceptorCache;
import com.liferay.portal.aop.context.MethodInterceptorContext;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.rule.NewEnv;
import com.liferay.portal.kernel.test.rule.NewEnvTestRule;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.security.access.control.AccessControlMethodInterceptorFactory;
import com.liferay.portal.util.PropsUtil;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.aopalliance.intercept.Joinpoint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class MethodInterceptorCacheManagerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, NewEnvTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(_registry);
	}

	@Test
	public void testConstructor() throws Exception {
		Constructor<?> constructor =
			MethodInterceptorCacheManager.class.getDeclaredConstructor();

		constructor.setAccessible(true);

		constructor.newInstance();
	}

	@Test
	public void testCreateAndDestroy() throws Exception {
		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(_testMethodInterceptorContext);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(
				new TestMethodInvocation(
					MethodInterceptorCacheManagerTest.class.getMethod(
						"testCreateAndDestroy")));

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 11, methodInterceptors.length);

		Map<MethodInterceptorCache, MethodInterceptorContext> map =
			ReflectionTestUtil.getFieldValue(
				MethodInterceptorCacheManager.class,
				"_methodInterceptorCaches");

		Assert.assertSame(
			_testMethodInterceptorContext, map.get(methodInterceptorCache));

		MethodInterceptorCacheManager.destroy(methodInterceptorCache);

		Assert.assertTrue(map.toString(), map.isEmpty());
	}

	@Test
	public void testRegisterMethodInterceptorFactory() throws Exception {
		MethodInvocation methodInvocation = new TestMethodInvocation(
			MethodInterceptorCacheManagerTest.class.getMethod(
				"testRegisterMethodInterceptorFactory"));

		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(_testMethodInterceptorContext);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(methodInvocation);

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 11, methodInterceptors.length);

		MethodInterceptor methodInterceptor = Joinpoint::proceed;

		MethodInterceptorFactory methodInterceptorFactory =
			new TestMethodInterceptorFactory(
				methodInterceptor, AccessControlMethodInterceptorFactory.class,
				true);

		ServiceRegistration<?> serviceRegistration = _registry.registerService(
			MethodInterceptorFactory.class, methodInterceptorFactory);

		methodInterceptors = methodInterceptorCache.getMethodInterceptors(
			methodInvocation);

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 12, methodInterceptors.length);

		Assert.assertEquals(methodInterceptors[2], methodInterceptor);

		Class<?> clazz = methodInterceptors[1].getClass();

		Assert.assertEquals(
			methodInterceptors[1].toString(),
			AccessControlMethodInterceptorFactory.class.getName() +
				"$AccessControlMethodInterceptor",
			clazz.getName());

		serviceRegistration.setProperties(new HashMap<>());

		serviceRegistration.unregister();

		methodInterceptors = methodInterceptorCache.getMethodInterceptors(
			methodInvocation);

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 11, methodInterceptors.length);

		Assert.assertFalse(
			Arrays.toString(methodInterceptors),
			ArrayUtil.contains(methodInterceptors, methodInterceptor));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					MethodInterceptorCacheManager.class.getName(),
					Level.WARNING)) {

			serviceRegistration = _registry.registerService(
				MethodInterceptorFactory.class,
				new TestMethodInterceptorFactory(
					methodInterceptor, TestMethodInterceptorFactory.class,
					true));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				logRecord.toString(), logRecord.getLevel(), Level.WARNING);
			Assert.assertEquals(
				logRecord.toString(), logRecord.getMessage(),
				StringBundler.concat(
					"Parent class com.liferay.portal.aop.",
					"MethodInterceptorCacheManagerTest$",
					"TestMethodInterceptorFactory not found for class ",
					"com.liferay.portal.aop.MethodInterceptorCacheManagerTest$",
					"TestMethodInterceptorFactory"));

			serviceRegistration.unregister();
		}

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					MethodInterceptorCacheManager.class.getName(),
					Level.SEVERE)) {

			serviceRegistration = _registry.registerService(
				MethodInterceptorFactory.class,
				new TestMethodInterceptorFactory(
					methodInterceptor, TestMethodInterceptorFactory.class,
					true));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());

			serviceRegistration.unregister();
		}

		serviceRegistration = _registry.registerService(
			MethodInterceptorFactory.class,
			new TestMethodInterceptorFactory(
				null, TestMethodInterceptorFactory.class, false) {

				@Override
				public MethodInterceptor create(
					MethodInterceptorContext methodInterceptorContext) {

					throw new AssertionError();
				}

			});

		serviceRegistration.unregister();
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStatic() throws Exception {
		String fakeClassName = "Fake class name";

		PropsUtil.set(
			PropsKeys.PORTAL_DEFAULT_METHOD_INTERCEPTOR_FACTORIES,
			fakeClassName);

		try {
			MethodInterceptorCacheManager.create(null);

			Assert.fail();
		}
		catch (ExceptionInInitializerError eiie) {
			Throwable throwable = eiie.getException();

			Assert.assertSame(
				ClassNotFoundException.class, throwable.getClass());
			Assert.assertEquals(fakeClassName, throwable.getMessage());
		}
	}

	private static final Registry _registry = new BasicRegistryImpl();

	private static final MethodInterceptorContext
		_testMethodInterceptorContext = new MethodInterceptorContext() {

			@Override
			public <T> T getService(Class<T> serviceClass) {
				return null;
			}

		};

	private static class TestMethodInterceptorFactory
		implements MethodInterceptorFactory {

		@Override
		public MethodInterceptor create(
			MethodInterceptorContext methodInterceptorContext) {

			return _methodInterceptor;
		}

		@Override
		public Class<TestAnnotation> getAnnotationClass() {
			return TestAnnotation.class;
		}

		@Override
		public Class<? extends MethodInterceptorFactory> getParentClass() {
			return _parentClass;
		}

		@Override
		public boolean isEnabled() {
			return _enabled;
		}

		private TestMethodInterceptorFactory(
			MethodInterceptor methodInterceptor,
			Class<? extends MethodInterceptorFactory> parentClass,
			boolean enabled) {

			_methodInterceptor = methodInterceptor;
			_parentClass = parentClass;
			_enabled = enabled;
		}

		private final boolean _enabled;
		private final MethodInterceptor _methodInterceptor;
		private final Class<? extends MethodInterceptorFactory> _parentClass;

	}

	private static class TestMethodInvocation implements MethodInvocation {

		@Override
		public Object[] getArguments() {
			return null;
		}

		@Override
		public Method getMethod() {
			return _method;
		}

		@Override
		public Method getStaticPart() {
			return _method;
		}

		@Override
		public Object getThis() {
			return null;
		}

		@Override
		public Object proceed() {
			return null;
		}

		private TestMethodInvocation(Method method) {
			_method = method;
		}

		private final Method _method;

	}

	private @interface TestAnnotation {
	}

}