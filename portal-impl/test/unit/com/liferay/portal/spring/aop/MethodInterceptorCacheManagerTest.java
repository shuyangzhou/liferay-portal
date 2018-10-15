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

package com.liferay.portal.spring.aop;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.dao.jdbc.aop.DefaultDynamicDataSourceTargetSource;
import com.liferay.portal.dao.jdbc.aop.DynamicDataSourceMethodInterceptorFactory;
import com.liferay.portal.internal.cluster.ClusterableMethodInterceptorFactory;
import com.liferay.portal.internal.cluster.SPIClusterableMethodInterceptorFactory;
import com.liferay.portal.kernel.resiliency.spi.MockSPI;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.rule.NewEnv;
import com.liferay.portal.kernel.test.rule.NewEnvTestRule;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.security.access.control.AccessControlMethodInterceptorFactory;
import com.liferay.portal.util.PropsValues;
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
			MethodInterceptorCacheManager.create(
				_dummyMethodInterceptorFactoryHelper);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(
				new TestMethodInvocation(
					MethodInterceptorCacheManagerTest.class.getMethod(
						"testCreateAndDestroy")));

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 11, methodInterceptors.length);

		Map<MethodInterceptorCache, MethodInterceptorFactoryHelper> map =
			ReflectionTestUtil.getFieldValue(
				MethodInterceptorCacheManager.class,
				"_methodInterceptorCaches");

		Assert.assertEquals(
			_dummyMethodInterceptorFactoryHelper,
			map.get(methodInterceptorCache));

		MethodInterceptorCacheManager.destroy(methodInterceptorCache);

		Assert.assertTrue(map.toString(), map.isEmpty());
	}

	@Test
	public void testRegisterMethodInterceptorFactoryProvider()
		throws Exception {

		MethodInvocation methodInvocation = new TestMethodInvocation(
			MethodInterceptorCacheManagerTest.class.getMethod(
				"testRegisterMethodInterceptorFactoryProvider"));

		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(
				_dummyMethodInterceptorFactoryHelper);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(methodInvocation);

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 11, methodInterceptors.length);

		MethodInterceptor methodInterceptor = Joinpoint::proceed;

		MethodInterceptorFactory methodInterceptorFactory =
			new TestMethodInterceptorFactory(methodInterceptor);

		ServiceRegistration<?> serviceRegistration = _registry.registerService(
			MethodInterceptorFactoryProvider.class,
			new TestMethodInterceptorFactoryProvider(
				methodInterceptorFactory,
				AccessControlMethodInterceptorFactory.class));

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
				MethodInterceptorFactoryProvider.class,
				new TestMethodInterceptorFactoryProvider(
					methodInterceptorFactory,
					TestMethodInterceptorFactory.class));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				logRecord.toString(), logRecord.getLevel(), Level.WARNING);
			Assert.assertEquals(
				logRecord.toString(), logRecord.getMessage(),
				StringBundler.concat(
					"Parent class com.liferay.portal.spring.aop.",
					"MethodInterceptorCacheManagerTest$",
					"TestMethodInterceptorFactory not found for class ",
					"com.liferay.portal.spring.aop.",
					"MethodInterceptorCacheManagerTest$",
					"TestMethodInterceptorFactoryProvider"));

			serviceRegistration.unregister();
		}

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					MethodInterceptorCacheManager.class.getName(),
					Level.SEVERE)) {

			serviceRegistration = _registry.registerService(
				MethodInterceptorFactoryProvider.class,
				new TestMethodInterceptorFactoryProvider(
					methodInterceptorFactory,
					TestMethodInterceptorFactory.class));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());

			serviceRegistration.unregister();
		}
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStatic1() throws Exception {
		RegistryUtil.setRegistry(_registry);

		ReflectionTestUtil.setFieldValue(SPIUtil.class, "_spi", new MockSPI());

		InfrastructureUtil infrastructureUtil = new InfrastructureUtil();

		infrastructureUtil.setDynamicDataSourceTargetSource(
			new DefaultDynamicDataSourceTargetSource());

		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(
				_dummyMethodInterceptorFactoryHelper);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(
				new TestMethodInvocation(
					MethodInterceptorCacheManagerTest.class.getMethod(
						"testStatic1")));

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 12, methodInterceptors.length);

		MethodInterceptor methodInterceptor = methodInterceptors[1];

		Class<?> clazz = methodInterceptor.getClass();

		Assert.assertEquals(
			clazz.getName(),
			SPIClusterableMethodInterceptorFactory.class.getName() +
				"$SPIClusterableMethodInterceptor");

		methodInterceptor = methodInterceptors[11];

		clazz = methodInterceptor.getClass();

		Assert.assertEquals(
			clazz.getName(),
			DynamicDataSourceMethodInterceptorFactory.class.getName() +
				"$DynamicDataSourceMethodInterceptor");

		MethodInterceptorCacheManager.destroy(methodInterceptorCache);
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testStatic2() throws Exception {
		RegistryUtil.setRegistry(_registry);

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "CLUSTER_LINK_ENABLED", true);

		MethodInterceptorCache methodInterceptorCache =
			MethodInterceptorCacheManager.create(
				_dummyMethodInterceptorFactoryHelper);

		MethodInterceptor[] methodInterceptors =
			methodInterceptorCache.getMethodInterceptors(
				new TestMethodInvocation(
					MethodInterceptorCacheManagerTest.class.getMethod(
						"testStatic2")));

		Assert.assertEquals(
			Arrays.toString(methodInterceptors), 12, methodInterceptors.length);

		MethodInterceptor methodInterceptor = methodInterceptors[1];

		Class<?> clazz = methodInterceptor.getClass();

		Assert.assertEquals(
			clazz.getName(),
			ClusterableMethodInterceptorFactory.class.getName() +
				"$ClusterableMethodInterceptor");

		MethodInterceptorCacheManager.destroy(methodInterceptorCache);
	}

	private static final MethodInterceptorFactoryHelper
		_dummyMethodInterceptorFactoryHelper =
			new MethodInterceptorFactoryHelper() {

				@Override
				public <T> T getService(Class<T> serviceClass) {
					return null;
				}

			};

	private static final Registry _registry = new BasicRegistryImpl();

	private static class TestMethodInterceptorFactory
		implements MethodInterceptorFactory {

		@Override
		public MethodInterceptor create(
			MethodInterceptorFactoryHelper methodInterceptorFactoryHelper) {

			return _methodInterceptor;
		}

		@Override
		public Class<TestAnnotation> getAnnotationClass() {
			return TestAnnotation.class;
		}

		private TestMethodInterceptorFactory(
			MethodInterceptor methodInterceptor) {

			_methodInterceptor = methodInterceptor;
		}

		private final MethodInterceptor _methodInterceptor;

	}

	private static class TestMethodInterceptorFactoryProvider
		implements MethodInterceptorFactoryProvider {

		@Override
		public MethodInterceptorFactory getMethodInterceptorFactory() {
			return _methodInterceptorFactory;
		}

		@Override
		public Class<? extends MethodInterceptorFactory> getParentClass() {
			return _parentClass;
		}

		private TestMethodInterceptorFactoryProvider(
			MethodInterceptorFactory methodInterceptorFactory,
			Class<? extends MethodInterceptorFactory> parentClass) {

			_methodInterceptorFactory = methodInterceptorFactory;
			_parentClass = parentClass;
		}

		private final MethodInterceptorFactory _methodInterceptorFactory;
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