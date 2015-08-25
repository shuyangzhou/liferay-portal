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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.service.PortletPreferencesLocalService;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.spring.transaction.DefaultTransactionExecutor;
import com.liferay.portal.spring.transaction.TransactionInterceptor;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portlet.PortletPreferencesImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.hibernate.util.JDBCExceptionReporter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 *
 * @author Matthew Tambara
 */
public class PortletPreferencesLocalServiceImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Throwable {
		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			_OWNER_ID, _OWNER_TYPE, _PLID);

		_transactionInterceptor =
			(TransactionInterceptor)PortalBeanLocatorUtil.locate(
				"transactionAdvice");

		_originalTransactionExecutor = ReflectionTestUtil.getFieldValue(
			_transactionInterceptor, "transactionExecutor");

		_originalPortletPreferencesLocalService =
			PortletPreferencesLocalServiceUtil.getService();

		_addPreferencesMethod = PortletPreferencesLocalService.class.getMethod(
			"addPortletPreferences", long.class, long.class, int.class,
			long.class, java.lang.String.class,
			com.liferay.portal.model.Portlet.class, java.lang.String.class);

		ReflectionTestUtil.setFieldValue(
			PortletPreferencesLocalServiceUtil.class, "_service",
			ProxyUtil.newProxyInstance(
				PortletPreferencesLocalService.class.getClassLoader(),
				new Class<?>[] {PortletPreferencesLocalService.class},
				new SynchronousInvocationHandler()));

		_synchronizeThreadLocal.set(true);
	}

	@After
	public void tearDown() throws Throwable {
		_synchronizeThreadLocal.set(false);

		PortletPreferencesLocalServiceUtil.deletePortletPreferences(
			_OWNER_ID, _OWNER_TYPE, _PLID);
	}

	@Test
	public void testAddPortletPreferencesConcurrently() throws Exception {
		Callable<PortletPreferencesImpl> callable =
			new Callable<PortletPreferencesImpl>() {

				@Override
				public PortletPreferencesImpl call() {
					return (PortletPreferencesImpl)
						PortletPreferencesLocalServiceUtil.getPreferences(
							_COMPANY_ID, _OWNER_ID, _OWNER_TYPE, _PLID,
							_PORTLET_ID);
				}

			};

		Set<FutureTask<PortletPreferencesImpl>> futureTasks = new HashSet<>();

		try (CaptureAppender captureAppender1 =
				Log4JLoggerTestUtil.configureLog4JLogger(
					DefaultTransactionExecutor.class.getName(), Level.ERROR);
				CaptureAppender captureAppender2 =
					Log4JLoggerTestUtil.configureLog4JLogger(
						JDBCExceptionReporter.class.getName(), Level.ERROR)) {

			for (int i = 0; i < _THREAD_COUNT; i++) {
				FutureTask<PortletPreferencesImpl> futureTask =
					new FutureTask<>(callable);

				Thread thread = new Thread(futureTask, "Add Thread " + i);

				thread.start();

				futureTasks.add(futureTask);
			}

			Set<PortletPreferencesImpl> portletPreferencesImpls =
				new HashSet<>();

			for (FutureTask<PortletPreferencesImpl> futureTask : futureTasks) {
				portletPreferencesImpls.add(futureTask.get());
			}

			List<LoggingEvent> loggingEvents =
				captureAppender1.getLoggingEvents();

			Assert.assertEquals(_THREAD_COUNT - 1, loggingEvents.size());

			for (LoggingEvent loggingEvent : loggingEvents) {
				Assert.assertEquals(
					"Application exception overridden by commit exception",
					loggingEvent.getMessage());
			}

			loggingEvents = captureAppender2.getLoggingEvents();

			Assert.assertEquals(_THREAD_COUNT - 1, loggingEvents.size());

			for (LoggingEvent loggingEvent : loggingEvents) {
				String message = loggingEvent.getRenderedMessage();

				Assert.assertTrue(
					message.startsWith(
						"Duplicate entry '0-3-99999-Test' for key"));
			}

			Assert.assertEquals(1, portletPreferencesImpls.size());
		}
	}

	protected static class SynchronizedTransactionExecutor
		extends DefaultTransactionExecutor {

		@Override
		public void commit(
			PlatformTransactionManager platformTransactionManager,
			TransactionAttribute transactionAttribute,
			TransactionStatus transactionStatus) {

			if (!_synchronizeThreadLocal.get()) {
				_originalTransactionExecutor.commit(
					platformTransactionManager, transactionAttribute,
					transactionStatus);
			}

			try {
				_cyclicBarrier.await();

				_originalTransactionExecutor.commit(
					platformTransactionManager, transactionAttribute,
					transactionStatus);
			}
			catch (Throwable t) {
				ReflectionUtil.throwException(t);
			}
		}

		private final CyclicBarrier _cyclicBarrier = new CyclicBarrier(
			_THREAD_COUNT, new Runnable() {

				@Override
				public void run() {
					_transactionInterceptor.setTransactionExecutor(
						_originalTransactionExecutor);
				}

			});

	}

	protected static class SynchronousInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

			if (_synchronizeThreadLocal.get() &&
				_addPreferencesMethod.equals(method)) {

				_cyclicBarrier.await();
			}

			return method.invoke(_originalPortletPreferencesLocalService, args);
		}

		private final CyclicBarrier _cyclicBarrier = new CyclicBarrier(
			_THREAD_COUNT, new Runnable() {

				@Override
				public void run() {
					_transactionInterceptor.setTransactionExecutor(
						new PortletPreferencesLocalServiceImplTest.
							SynchronizedTransactionExecutor());

					ReflectionTestUtil.setFieldValue(
						PortletPreferencesLocalServiceUtil.class, "_service",
						_originalPortletPreferencesLocalService);
				}

			});

	}

	private static final long _COMPANY_ID = 0;

	private static final long _OWNER_ID = 0;

	private static final int _OWNER_TYPE = 3;

	private static final long _PLID = 99999;

	private static final String _PORTLET_ID = "Test";

	private static final int _THREAD_COUNT = 3;

	private static Method _addPreferencesMethod;
	private static PortletPreferencesLocalService
		_originalPortletPreferencesLocalService;
	private static DefaultTransactionExecutor _originalTransactionExecutor;
	private static final ThreadLocal<Boolean> _synchronizeThreadLocal =
		new InheritableThreadLocal<>();
	private static TransactionInterceptor _transactionInterceptor;

}