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

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.nio.intraband.CancelingPortalExecutorManagerUtilAdvice;
import com.liferay.portal.kernel.nio.intraband.PortalExecutorManagerUtilAdvice;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.SwappableSecurityManager;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.rule.NewEnv;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.AdviseWith;
import com.liferay.portal.test.rule.AspectJNewEnvTestRule;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 * @author Preston Crary
 */
public class AutoBatchPreparedStatementUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, AspectJNewEnvTestRule.INSTANCE);

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testBatchUpdateConcurrentWaitingForFutures() throws Exception {
		testConcurrentWaitingForFutures(true);
	}

	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testCINITFailure() throws ClassNotFoundException {
		final NoSuchMethodException nsme = new NoSuchMethodException();
		final AtomicInteger counter = new AtomicInteger();

		try (SwappableSecurityManager swappableSecurityManager =
				new SwappableSecurityManager() {

					@Override
					public void checkPackageAccess(String pkg) {
						if (pkg.equals("java.sql") &&
							(counter.getAndIncrement() == 1)) {

							ReflectionUtil.throwException(nsme);
						}
					}

				}) {

			swappableSecurityManager.install();

			Class.forName(AutoBatchPreparedStatementUtil.class.getName());
		}
		catch (ExceptionInInitializerError eiie) {
			Assert.assertSame(nsme, eiie.getCause());
		}
	}

	@Test
	public void testConstructor() throws ReflectiveOperationException {
		Constructor<AutoBatchPreparedStatementUtil> constructor =
			AutoBatchPreparedStatementUtil.class.getDeclaredConstructor();

		Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		constructor.newInstance();
	}

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testNoBatchUpdateConcurrentWaitingForFutures()
		throws Exception {

		testConcurrentWaitingForFutures(false);
	}

	@AdviseWith(
		adviceClasses = {CancelingPortalExecutorManagerUtilAdvice.class}
	)
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testNoSupportBatchUpdatesConcurrentCancellationException()
		throws Exception {

		testConcurrentCancellationException(false);
	}

	@Test
	public void testNotSupportBatchUpdates() throws Exception {
		testNotSupportBatchUpdates(false);
	}

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testNotSupportBatchUpdatesConcurrent() throws Exception {
		testNotSupportBatchUpdates(true);
	}

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testNotSupportBatchUpdatesConcurrentExecutionException()
		throws Exception {

		testConcurrentExecutionExceptions(false);
	}

	@Test
	public void testSupportBatchUpdates() throws Exception {
		testSupportBaseUpdates(false);
	}

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSupportBatchUpdatesConcurrent() throws Exception {
		testSupportBaseUpdates(true);
	}

	@AdviseWith(
		adviceClasses = {CancelingPortalExecutorManagerUtilAdvice.class}
	)
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSupportBatchUpdatesConcurrentCancellationException()
		throws Exception {

		testConcurrentCancellationException(true);
	}

	@AdviseWith(adviceClasses = {PortalExecutorManagerUtilAdvice.class})
	@NewEnv(type = NewEnv.Type.CLASSLOADER)
	@Test
	public void testSupportBatchUpdatesConcurrentExecutionException()
		throws Exception {

		testConcurrentExecutionExceptions(true);
	}

	protected void testConcurrentCancellationException(
			boolean supportBatchUpdates)
		throws Exception {

		ConnectionInvocationHandler connectionInvocationHandler =
			new ConnectionInvocationHandler(supportBatchUpdates);

		PreparedStatement preparedStatement =
			AutoBatchPreparedStatementUtil.concurrentAutoBatch(
				(Connection)ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {Connection.class},
					connectionInvocationHandler),
				StringPool.BLANK);

		preparedStatement.addBatch();

		preparedStatement.executeBatch();

		preparedStatement.addBatch();

		preparedStatement.executeBatch();

		try {
			preparedStatement.close();

			Assert.fail();
		}
		catch (Throwable t) {
			Assert.assertTrue(t.toString(), t instanceof CancellationException);

			Throwable[] throwables = t.getSuppressed();

			Assert.assertEquals(1, throwables.length);

			Assert.assertTrue(throwables[0] instanceof CancellationException);
		}
	}

	protected void testConcurrentExecutionExceptions(
			boolean supportBatchUpdates)
		throws Exception {

		ConnectionInvocationHandler connectionInvocationHandler =
			new ConnectionInvocationHandler(supportBatchUpdates);

		PreparedStatement preparedStatement =
			AutoBatchPreparedStatementUtil.concurrentAutoBatch(
				(Connection)ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {Connection.class},
					connectionInvocationHandler),
				StringPool.BLANK);

		PreparedStatementInvocationHandler preparedStatementInvocationHandler =
			connectionInvocationHandler.getPSInvocationHandler();

		RuntimeException runtimeException = new RuntimeException();

		Set<Throwable> throwablesSet = new HashSet<>();

		throwablesSet.add(runtimeException);

		preparedStatementInvocationHandler.setException(runtimeException);

		preparedStatement.addBatch();

		preparedStatement.executeBatch();

		preparedStatementInvocationHandler =
			connectionInvocationHandler.getPSInvocationHandler();

		RuntimeException suppressedException = new RuntimeException();

		throwablesSet.add(suppressedException);

		preparedStatementInvocationHandler.setException(suppressedException);

		preparedStatement.addBatch();

		preparedStatement.executeBatch();

		try {
			preparedStatement.close();

			Assert.fail();
		}
		catch (RuntimeException re) {
			Assert.assertTrue(throwablesSet.contains(re));

			Throwable[] throwables = re.getSuppressed();

			Assert.assertEquals(1, throwables.length);

			Assert.assertTrue(throwablesSet.contains(throwables[0]));
		}
	}

	protected void testConcurrentWaitingForFutures(boolean supportBatchUpdates)
		throws Exception {

		ConnectionInvocationHandler connectionInvocationHandler =
			new ConnectionInvocationHandler(supportBatchUpdates);

		PreparedStatement preparedStatement =
			AutoBatchPreparedStatementUtil.concurrentAutoBatch(
				(Connection)ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {Connection.class},
					connectionInvocationHandler),
				StringPool.BLANK);

		TestNoticeableFuture<Void> testNoticeableFuture =
			new TestNoticeableFuture<>();

		Set<Future<Void>> futures = new HashSet<>();

		futures.add(testNoticeableFuture);

		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			preparedStatement);

		ReflectionTestUtil.setFieldValue(
			invocationHandler, "_futures", futures);

		preparedStatement.close();

		Assert.assertTrue(testNoticeableFuture.hasCalledGet());
	}

	protected void testNotSupportBatchUpdates(boolean concurrent)
		throws Exception {

		ConnectionInvocationHandler connectionInvocationHandler =
			new ConnectionInvocationHandler(false);

		PreparedStatement preparedStatement = null;

		if (concurrent) {
			preparedStatement =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					(Connection)ProxyUtil.newProxyInstance(
						ClassLoader.getSystemClassLoader(),
						new Class<?>[] {Connection.class},
						connectionInvocationHandler),
					StringPool.BLANK);
		}
		else {
			preparedStatement = AutoBatchPreparedStatementUtil.autoBatch(
				(PreparedStatement)ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {PreparedStatement.class},
					connectionInvocationHandler.getPSInvocationHandler()));
		}

		List<Method> methods = connectionInvocationHandler.getMethods();

		Assert.assertTrue(methods.toString(), methods.isEmpty());

		// Calling addBatch fallbacks to executeUpdate

		preparedStatement.addBatch();

		if (concurrent) {
			Assert.assertEquals(methods.toString(), 2, methods.size());
		}
		else {
			Assert.assertEquals(methods.toString(), 1, methods.size());
		}

		Assert.assertEquals(
			PreparedStatement.class.getMethod("executeUpdate"),
			methods.remove(0));

		if (concurrent) {
			Assert.assertEquals(
				PreparedStatement.class.getMethod("close"), methods.remove(0));
		}

		// Calling executeBatch does nothing

		Assert.assertArrayEquals(new int[0], preparedStatement.executeBatch());
		Assert.assertTrue(methods.toString(), methods.isEmpty());

		// Calling close waits for futures

		if (concurrent) {
			preparedStatement.close();

			methods = connectionInvocationHandler.getMethods();

			Assert.assertEquals(methods.toString(), 1, methods.size());
			Assert.assertEquals(
				PreparedStatement.class.getMethod("close"), methods.remove(0));
		}

		// Other methods like execute pass through

		preparedStatement.execute();

		methods = connectionInvocationHandler.getMethods();

		Assert.assertEquals(methods.toString(), 1, methods.size());
		Assert.assertEquals(
			PreparedStatement.class.getMethod("execute"), methods.remove(0));
	}

	protected void testSupportBaseUpdates(boolean concurrent) throws Exception {
		ConnectionInvocationHandler connectionInvocationHandler =
			new ConnectionInvocationHandler(true);

		PreparedStatement preparedStatement = null;

		if (concurrent) {
			preparedStatement =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					(Connection)ProxyUtil.newProxyInstance(
						ClassLoader.getSystemClassLoader(),
						new Class<?>[] {Connection.class},
						connectionInvocationHandler),
					StringPool.BLANK);
		}
		else {
			preparedStatement = AutoBatchPreparedStatementUtil.autoBatch(
				(PreparedStatement)ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {PreparedStatement.class},
					connectionInvocationHandler.getPSInvocationHandler()));
		}

		InvocationHandler invocationHandler = ProxyUtil.getInvocationHandler(
			preparedStatement);

		Assert.assertEquals(
			0, ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

		List<Method> methods = connectionInvocationHandler.getMethods();

		Assert.assertTrue(methods.toString(), methods.isEmpty());

		int hibernateJDBCBatchSize = PropsValues.HIBERNATE_JDBC_BATCH_SIZE;

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "HIBERNATE_JDBC_BATCH_SIZE", 2);

		try {

			// Protection for executing empty batch

			Assert.assertArrayEquals(
				new int[0], preparedStatement.executeBatch());
			Assert.assertTrue(methods.toString(), methods.isEmpty());
			Assert.assertEquals(
				0,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			// Calling addBatch passes through when within the Hibernate JDBC
			// batch size

			preparedStatement.addBatch();

			Assert.assertEquals(methods.toString(), 1, methods.size());
			Assert.assertEquals(
				PreparedStatement.class.getMethod("addBatch"),
				methods.remove(0));
			Assert.assertEquals(
				1,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			// Calling addBatch passes through and triggers executeBatch when
			// exceeding the Hibernate JDBC batch size

			preparedStatement.addBatch();

			if (concurrent) {
				Assert.assertEquals(methods.toString(), 3, methods.size());
			}
			else {
				Assert.assertEquals(methods.toString(), 2, methods.size());
			}

			Assert.assertEquals(
				PreparedStatement.class.getMethod("addBatch"),
				methods.remove(0));
			Assert.assertEquals(
				PreparedStatement.class.getMethod("executeBatch"),
				methods.remove(0));
			Assert.assertEquals(
				0,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			if (concurrent) {
				Assert.assertEquals(
					PreparedStatement.class.getMethod("close"),
					methods.remove(0));
			}

			// Calling addBatch passes through when within the Hibernate JDBC
			// batch size

			preparedStatement.addBatch();

			methods = connectionInvocationHandler.getMethods();

			Assert.assertEquals(methods.toString(), 1, methods.size());
			Assert.assertEquals(
				PreparedStatement.class.getMethod("addBatch"),
				methods.remove(0));
			Assert.assertEquals(
				1,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			// Calling executeBatch passes through when batch is not empty

			preparedStatement.executeBatch();

			if (concurrent) {
				Assert.assertEquals(methods.toString(), 2, methods.size());
			}
			else {
				Assert.assertEquals(methods.toString(), 1, methods.size());
			}

			Assert.assertEquals(
				PreparedStatement.class.getMethod("executeBatch"),
				methods.remove(0));
			Assert.assertEquals(
				0,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			if (concurrent) {
				Assert.assertEquals(
					PreparedStatement.class.getMethod("close"),
					methods.remove(0));
			}

			// Other methods like execute pass through

			preparedStatement.execute();

			methods = connectionInvocationHandler.getMethods();

			Assert.assertEquals(methods.toString(), 1, methods.size());
			Assert.assertEquals(
				PreparedStatement.class.getMethod("execute"),
				methods.remove(0));
			Assert.assertEquals(
				0,
				ReflectionTestUtil.getFieldValue(invocationHandler, "_count"));

			// Calling close waits for futures

			preparedStatement.close();

			Assert.assertEquals(methods.toString(), 1, methods.size());
			Assert.assertEquals(
				PreparedStatement.class.getMethod("close"), methods.remove(0));
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				PropsValues.class, "HIBERNATE_JDBC_BATCH_SIZE",
				hibernateJDBCBatchSize);
		}
	}

	private static class ConnectionInvocationHandler
		implements InvocationHandler {

		public List<Method> getMethods() {
			return _psInvocationHandler.getMethods();
		}

		public PreparedStatementInvocationHandler getPSInvocationHandler() {
			if (_psInvocationHandler == null) {
				_psInvocationHandler = new PreparedStatementInvocationHandler(
					_supportBatchUpdates);
			}

			return _psInvocationHandler;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws NoSuchMethodException {

			if (method.equals(Connection.class.getMethod("getMetaData"))) {
				return ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {DatabaseMetaData.class},
					new DatabaseMetaDataInvocationHandler(
						_supportBatchUpdates));
			}

			if (method.equals(
					Connection.class.getMethod(
						"prepareStatement", String.class))) {

				return ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {PreparedStatement.class},
					getPSInvocationHandler());
			}

			throw new UnsupportedOperationException();
		}

		private ConnectionInvocationHandler(boolean supportBatchUpdates) {
			_supportBatchUpdates = supportBatchUpdates;
		}

		private PreparedStatementInvocationHandler _psInvocationHandler;
		private final boolean _supportBatchUpdates;

	}

	private static class DatabaseMetaDataInvocationHandler
		implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws NoSuchMethodException {

			if (method.equals(
					DatabaseMetaData.class.getMethod("supportsBatchUpdates"))) {

				return _supportBatchUpdates;
			}

			throw new UnsupportedOperationException();
		}

		private DatabaseMetaDataInvocationHandler(boolean supportBatchUpdates) {
			_supportBatchUpdates = supportBatchUpdates;
		}

		private final boolean _supportBatchUpdates;

	}

	private static class PreparedStatementInvocationHandler
		implements InvocationHandler {

		public List<Method> getMethods() {
			return _methods;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws NoSuchMethodException {

			if (method.equals(
					PreparedStatement.class.getMethod("getConnection"))) {

				return ProxyUtil.newProxyInstance(
					ClassLoader.getSystemClassLoader(),
					new Class<?>[] {Connection.class},
					new ConnectionInvocationHandler(_supportBatchUpdates));
			}

			_methods.add(method);

			if (method.equals(PreparedStatement.class.getMethod("addBatch"))) {
				return null;
			}

			if (method.equals(PreparedStatement.class.getMethod("close"))) {
				return null;
			}

			if (method.equals(PreparedStatement.class.getMethod("execute"))) {
				return false;
			}

			if (method.equals(
					PreparedStatement.class.getMethod("executeBatch"))) {

				if (_runtimeException != null) {
					throw _runtimeException;
				}

				return new int[0];
			}

			if (method.equals(
					PreparedStatement.class.getMethod("executeUpdate"))) {

				if (_runtimeException != null) {
					throw _runtimeException;
				}

				return 0;
			}

			throw new UnsupportedOperationException();
		}

		public void setException(RuntimeException runtimeException) {
			_runtimeException = runtimeException;
		}

		private PreparedStatementInvocationHandler(
			boolean supportBatchUpdates) {

			_supportBatchUpdates = supportBatchUpdates;
		}

		private final List<Method> _methods = new ArrayList<>();
		private RuntimeException _runtimeException;
		private final boolean _supportBatchUpdates;

	}

	private static final class TestNoticeableFuture<T>
		extends DefaultNoticeableFuture<T> {

		@Override
		public T get() throws ExecutionException, InterruptedException {
			_calledGet = true;

			return null;
		}

		public boolean hasCalledGet() {
			return _calledGet;
		}

		private boolean _calledGet;

	}

}