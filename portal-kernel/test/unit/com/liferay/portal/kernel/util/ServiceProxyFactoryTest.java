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

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class ServiceProxyFactoryTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());
	}

	@Test
	public void testBlockingProxy() throws Exception {
		final TestService testService =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestService.class, TestServiceClient.class, "testService",
				true);

		Assert.assertTrue(ProxyUtil.isProxyClass(testService.getClass()));
		Assert.assertNotSame(TestServiceImpl.class, testService.getClass());

		Thread thread = new Thread() {

			@Override
			public void run() {
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

		_waitForBlocked(testService, thread);

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

		_waitForBlocked(testService, thread);

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
	public void testNonblockingProxyWithFilter() throws Exception {
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
	}

	@Test
	public void testNonblockingProxyWithoutFilter() throws Exception {
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

	private void _waitForBlocked(TestService testService, Thread targetThread) {
		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			testService);

		Lock lock = ReflectionTestUtil.getFieldValue(
			invocationHandler, "_lock");

		Object sync = ReflectionTestUtil.getFieldValue(lock, "sync");

		Condition condition = ReflectionTestUtil.getFieldValue(
			invocationHandler, "_isRealServiceSet");

		lock.lock();

		try {
			while (true) {
				Collection<Thread> waitingThreads = ReflectionTestUtil.invoke(
					sync, "getWaitingThreads",
					new Class<?>[] {
						AbstractQueuedSynchronizer.ConditionObject.class
					},
					condition);

				if (waitingThreads.contains(targetThread)) {
					return;
				}
			}
		}
		finally {
			lock.unlock();
		}
	}

	private static final long _TEST_SERVICE_ID = 1234L;

	private static final String _TEST_SERVICE_NAME = "TestServiceName";

	private static class TestServiceClient {

		public static volatile TestService testService;

		public TestService nonStaticField;

	}

}