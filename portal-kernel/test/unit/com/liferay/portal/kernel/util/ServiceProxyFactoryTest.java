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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class ServiceProxyFactoryTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		_logInvocationHandler = new LogInvocationHandler(
			ReflectionTestUtil.getFieldValue(
				ServiceProxyFactory.class, "_log"));

		ReflectionTestUtil.setFieldValue(
			ServiceProxyFactory.class, "_log",
			ProxyUtil.newProxyInstance(
				ServiceProxyFactoryTest.class.getClassLoader(),
				new Class<?>[] {Log.class}, _logInvocationHandler));
	}

	@AfterClass
	public static void tearDownClass() throws PortalException {
		ReflectionTestUtil.setFieldValue(
			ServiceProxyFactory.class, "_log",
			_logInvocationHandler.getTarget());
	}

	@Test
	public void testBlockingProxyWithDebugDisabled() throws Exception {
		final TestService testService =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				true);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService.getClass());

		_logInvocationHandler.block();

		Thread thread = new Thread() {

			@Override
			public void run() {
				_logInvocationHandler.setDebugEnabled(Boolean.FALSE);

				Assert.assertEquals(
					_TEST_SERVICE_NAME, testService.getTestServiceName());
				Assert.assertEquals(
					_TEST_SERVICE_ID, testService.getTestServiceId());

				TestService newTestService = TestServiceClient.testService;

				Assert.assertFalse(
					ProxyUtil.isProxyClass(newTestService.getClass()));
				Assert.assertSame(
					TestServiceImpl.class, newTestService.getClass());
			}

		};

		thread.start();

		_logInvocationHandler.waitUntilBlock();

		_logInvocationHandler.unblock();

		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<TestService> serviceRegistration =
			registry.registerService(TestService.class, new TestServiceImpl());

		thread.join();

		serviceRegistration.unregister();
	}

	@Test
	public void testBlockingProxyWithDebugEnabled() throws Exception {
		final TestService testService =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				true);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService.getClass());

		_logInvocationHandler.block();

		Thread thread = new Thread() {

			@Override
			public void run() {
				_logInvocationHandler.setDebugEnabled(Boolean.TRUE);

				Assert.assertEquals(
					_TEST_SERVICE_NAME, testService.getTestServiceName());
				Assert.assertEquals(
					_TEST_SERVICE_ID, testService.getTestServiceId());

				Assert.assertEquals(
					"Waiting for a real service instance " +
						TestService.class.getName(),
					_logInvocationHandler.getDebugInfo());

				TestService newTestService = TestServiceClient.testService;

				Assert.assertFalse(
					ProxyUtil.isProxyClass(newTestService.getClass()));
				Assert.assertSame(
					TestServiceImpl.class, newTestService.getClass());
			}

		};

		thread.start();

		_logInvocationHandler.waitUntilBlock();

		_logInvocationHandler.unblock();

		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<TestService> serviceRegistration =
			registry.registerService(TestService.class, new TestServiceImpl());

		thread.join();

		serviceRegistration.unregister();
	}

	@Test
	public void testBlockingProxyWithProxyService() throws Exception {
		final TestService testService =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				true);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService.getClass());

		_logInvocationHandler.block();

		Thread thread = new Thread() {

			@Override
			public void run() {
				Assert.assertEquals(
					_TEST_SERVICE_NAME, testService.getTestServiceName());
				Assert.assertEquals(
					_TEST_SERVICE_ID, testService.getTestServiceId());

				TestService newTestService = TestServiceClient.testService;

				Assert.assertTrue(
					ProxyUtil.isProxyClass(newTestService.getClass()));
				Assert.assertNotSame(
					TestServiceImpl.class, newTestService.getClass());
			}

		};

		thread.start();

		_logInvocationHandler.waitUntilBlock();

		_logInvocationHandler.unblock();

		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<TestService> serviceRegistration =
			registry.registerService(
				TestService.class,
				(TestService)ProxyFactory.newInstance(
					ServiceProxyFactoryTest.class.getClassLoader(),
					new Class<?>[] {TestService.class},
					TestServiceImpl.class.getName()));

		thread.join();

		serviceRegistration.unregister();
	}

	@Test
	public void testMisc() throws Exception {

		// Test 1, wrong field

		try {
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "wrongFieldName",
				false);

			Assert.fail();
		}
		catch (Throwable throwable) {
			Assert.assertEquals(
				NoSuchFieldException.class, throwable.getClass());
			Assert.assertEquals("wrongFieldName", throwable.getMessage());
		}

		// Test 2, field is not static

		try {
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "nonStaticField",
				false);

			Assert.fail();
		}
		catch (Throwable throwable) {
			Assert.assertEquals(
				IllegalArgumentException.class, throwable.getClass());

			Field testServiceField = ReflectionUtil.getDeclaredField(
				TestServiceClient.class, "nonStaticField");

			Assert.assertEquals(
				testServiceField + " is not static", throwable.getMessage());
		}

		// Test 3, test constructor

		new ServiceProxyFactory();
	}

	@Test
	public void testNonblockingProxyWithoutStringFilter() throws Exception {
		TestService testService = ServiceProxyFactory.newServiceTrackedInstance(
			TestService.class, TestServiceClient.class, "testService", false);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService.getClass());

		Assert.assertEquals(0, testService.getTestServiceId());
		Assert.assertEquals(null, testService.getTestServiceName());

		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<TestService> serviceRegistration =
			registry.registerService(TestService.class, new TestServiceImpl());

		TestService newTestService = TestServiceClient.testService;

		Assert.assertEquals(
			_TEST_SERVICE_NAME, newTestService.getTestServiceName());
		Assert.assertEquals(
			_TEST_SERVICE_ID, newTestService.getTestServiceId());

		Assert.assertFalse(ProxyUtil.isProxyClass(newTestService.getClass()));
		Assert.assertSame(TestServiceImpl.class, newTestService.getClass());

		serviceRegistration.unregister();
	}

	@Test
	public void testNonblockingProxyWithStringFilter() throws Exception {

		// Test 1, with open parenthesis

		TestService testService1 =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				"(test.filter=true)", false);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService1.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService1.getClass());

		Assert.assertEquals(0, testService1.getTestServiceId());
		Assert.assertEquals(null, testService1.getTestServiceName());

		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put("test.filter", "true");

		ServiceRegistration<TestService> serviceRegistration =
			registry.registerService(
				TestService.class, new TestServiceImpl(), properties);

		TestService newTestService = TestServiceClient.testService;

		Assert.assertEquals(
			_TEST_SERVICE_NAME, newTestService.getTestServiceName());
		Assert.assertEquals(
			_TEST_SERVICE_ID, newTestService.getTestServiceId());

		Assert.assertFalse(ProxyUtil.isProxyClass(newTestService.getClass()));
		Assert.assertSame(TestServiceImpl.class, newTestService.getClass());

		serviceRegistration.unregister();

		// Test 2, without open parenthesis

		TestService testService2 =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				"test.filter=true", false);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService2.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService2.getClass());

		Assert.assertEquals(0, testService2.getTestServiceId());
		Assert.assertEquals(null, testService2.getTestServiceName());

		serviceRegistration = registry.registerService(
			TestService.class, new TestServiceImpl(), properties);

		newTestService = TestServiceClient.testService;

		Assert.assertEquals(
			_TEST_SERVICE_NAME, newTestService.getTestServiceName());
		Assert.assertEquals(
			_TEST_SERVICE_ID, newTestService.getTestServiceId());

		Assert.assertFalse(ProxyUtil.isProxyClass(newTestService.getClass()));
		Assert.assertSame(TestServiceImpl.class, newTestService.getClass());

		serviceRegistration.unregister();
	}

	public static class TestServiceImpl implements TestService {

		@Override
		public long getTestServiceId() {
			return _TEST_SERVICE_ID;
		}

		@Override
		public String getTestServiceName() {
			return _TEST_SERVICE_NAME;
		}

	}

	public interface TestService {

		public long getTestServiceId();

		public String getTestServiceName();

	}

	private static final long _TEST_SERVICE_ID = 1234L;

	private static final String _TEST_SERVICE_NAME = "TestServiceName";

	private static LogInvocationHandler _logInvocationHandler;

	private static class LogInvocationHandler implements InvocationHandler {

		public void block() {
			_semaphore = new Semaphore(0);
		}

		public String getDebugInfo() {
			return _debugInfo;
		}

		public Object getTarget() {
			return _target;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			String methodName = method.getName();

			if (methodName.equals("isDebugEnabled")) {
				Semaphore semaphore = _semaphore;

				if (semaphore != null) {
					semaphore.acquire();
				}

				return _debugEnabled;
			}
			else if (methodName.equals("debug")) {
				_debugInfo = (String)args[0];

				return null;
			}

			return method.invoke(_target, args);
		}

		public void setDebugEnabled(Boolean debugEnabled) {
			_debugEnabled = debugEnabled;
		}

		public void unblock() {
			Semaphore semaphore = _semaphore;

			_semaphore = null;

			semaphore.release(1);
		}

		public void waitUntilBlock() {
			Semaphore semaphore = _semaphore;

			if (semaphore != null) {
				while (semaphore.getQueueLength() < 1);
			}
		}

		private LogInvocationHandler(Object target) {
			_target = target;
		}

		private static volatile Semaphore _semaphore;

		private Boolean _debugEnabled;
		private String _debugInfo;
		private final Object _target;

	}

	private static class TestServiceClient {

		public static volatile TestService testService;

		public TestService nonStaticField;

	}

}