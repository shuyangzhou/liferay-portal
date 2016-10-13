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

package com.liferay.portal.spring.hibernate;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionConfig.Builder;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.spring.transaction.DefaultTransactionExecutor;
import com.liferay.portal.spring.transaction.TransactionInvokerImpl;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.jdbc.Work;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Shuyang Zhou
 */
public class PortletTransactionManagerTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
		_portalSessionFactory = _createSessionFactory();

		_portalTransactionManager = new HibernateTransactionManager(
			_portalSessionFactory);

		_portalTransactionManager.afterPropertiesSet();
	}

	@Test
	public void testPortal() throws Throwable {

		// NOT_SUPPORTED

		_assertSessionHolders(
			Arrays.asList((String)null), Arrays.asList((String)null),
			_runInTransactions(
				Arrays.asList(_portalTransactionManager),
				Arrays.asList(_NOT_SUPPORTED_TRANSACTION_CONFIG)));

		// NOT_SUPPORTED, NOT_SUPPORTED

		_assertSessionHolders(
			Arrays.asList(null, null), Arrays.asList(null, null),
			_runInTransactions(
				Arrays.asList(
					_portalTransactionManager, _portalTransactionManager),
				Arrays.asList(
					_NOT_SUPPORTED_TRANSACTION_CONFIG,
					_NOT_SUPPORTED_TRANSACTION_CONFIG)));

		// NOT_SUPPORTED, REQUIRED

		_assertSessionHolders(
			Arrays.asList(null, "a"), Arrays.asList(null, "a"),
			_runInTransactions(
				Arrays.asList(
					_portalTransactionManager, _portalTransactionManager),
				Arrays.asList(
					_NOT_SUPPORTED_TRANSACTION_CONFIG,
					_REQUIRED_TRANSACTION_CONFIG)));

		// NOT_SUPPORTED, REQUIRES_NEW

		_assertSessionHolders(
			Arrays.asList(null, "a"), Arrays.asList(null, "a"),
			_runInTransactions(
				Arrays.asList(
					_portalTransactionManager, _portalTransactionManager),
				Arrays.asList(
					_NOT_SUPPORTED_TRANSACTION_CONFIG,
					_REQUIRES_NEW_TRANSACTION_CONFIG)));

		// NOT_SUPPORTED, SUPPORTS

		_assertSessionHolders(
			Arrays.asList(null, null), Arrays.asList(null, null),
			_runInTransactions(
				Arrays.asList(
					_portalTransactionManager, _portalTransactionManager),
				Arrays.asList(
					_NOT_SUPPORTED_TRANSACTION_CONFIG,
					_SUPPORTS_TRANSACTION_CONFIG)));

		// REQUIRED, NOT_SUPPORTED

		_assertSessionHolders(
			Arrays.asList("a", null), Arrays.asList("a", null),
			_runInTransactions(
				Arrays.asList(
					_portalTransactionManager, _portalTransactionManager),
				Arrays.asList(
					_REQUIRED_TRANSACTION_CONFIG,
					_NOT_SUPPORTED_TRANSACTION_CONFIG)));

		// REQUIRED

		_assertSessionHolders(
			Arrays.asList("a"), Arrays.asList("a"),
			_runInTransactions(
				Arrays.asList(_portalTransactionManager),
				Arrays.asList(_REQUIRED_TRANSACTION_CONFIG)));

		// REQUIRES_NEW

		_assertSessionHolders(
			Arrays.asList("a"), Arrays.asList("a"),
			_runInTransactions(
				Arrays.asList(_portalTransactionManager),
				Arrays.asList(_REQUIRES_NEW_TRANSACTION_CONFIG)));

		// SUPPORTS

		_assertSessionHolders(
			Arrays.asList((String)null), Arrays.asList((String)null),
			_runInTransactions(
				Arrays.asList(_portalTransactionManager),
				Arrays.asList(_SUPPORTS_TRANSACTION_CONFIG)));
	}

	public static class DummyConnectionProvider implements ConnectionProvider {

		@Override
		public void close() {
		}

		@Override
		public void closeConnection(Connection connection) {
		}

		@Override
		public void configure(Properties properties) {
		}

		@Override
		public Connection getConnection() {
			return (Connection)ProxyUtil.newProxyInstance(
				ClassLoader.getSystemClassLoader(),
				new Class<?>[] {Connection.class}, (proxy, method, args) -> {

					String name = method.getName();

					if ("getAutoCommit".equals(name)) {
						return false;
					}

					if ("isClosed".equals(name)) {
						return false;
					}

					return method.getDefaultValue();
				});
		}

		@Override
		public boolean supportsAggressiveRelease() {
			return true;
		}

	}

	private static SessionFactory _createSessionFactory() throws Exception {
		AbstractSessionFactoryBean abstractSessionFactoryBean =
			new LocalSessionFactoryBean() {

				@Override
				protected void postProcessConfiguration(
					Configuration configuration) {

					configuration.setProperty(
						Environment.DIALECT, MySQL5Dialect.class.getName());
					configuration.setProperty(
						Environment.CONNECTION_PROVIDER,
						DummyConnectionProvider.class.getName());
					configuration.setProperty(
						"hibernate.temp.use_jdbc_metadata_defaults",
						StringPool.FALSE);
				}

			};

		abstractSessionFactoryBean.afterPropertiesSet();

		return abstractSessionFactoryBean.getObject();
	}

	private static TransactionConfig _createTransactionConfig(
		Propagation propagation) {

		Builder builder = new Builder();

		builder.setPropagation(propagation);

		return builder.build();
	}

	private void _assertSessionHolders(
		List<String> sessionHolderTokens, List<String> connectionTokens,
		List<SessionHolder> sessionHolders) {

		Assert.assertEquals(sessionHolderTokens.size(), sessionHolders.size());
		Assert.assertEquals(connectionTokens.size(), sessionHolders.size());

		for (int i = 0; i < sessionHolderTokens.size(); i++) {
			String sessionHolderToken = sessionHolderTokens.get(i);
			SessionHolder sessionHolder = sessionHolders.get(i);

			if (sessionHolderToken == null) {
				Assert.assertNull(
					"SessionHolder at position " + i + " is not null",
					sessionHolder);
			}
			else {
				Assert.assertNotNull(
					"SessionHolder at position " + i + " is null",
					sessionHolder);

				List<String> previousSessionHolderTokens =
					sessionHolderTokens.subList(0, i);
				List<SessionHolder> previousSessionHolders =
					sessionHolders.subList(0, i);

				int sessionHolderTokenIndex =
					previousSessionHolderTokens.indexOf(sessionHolderToken);
				int sessionHolderIndex = previousSessionHolders.indexOf(
					sessionHolder);

				Assert.assertEquals(
					"SessionHolder tokens :" + sessionHolderTokens +
						", SessionHolders :" + sessionHolders + ", fails at " +
							i,
					sessionHolderTokenIndex, sessionHolderIndex);
			}
		}

		List<Connection> connections = new ArrayList<>(sessionHolders.size());

		for (SessionHolder sessionHolder : sessionHolders) {
			connections.add(_getConnection(sessionHolder));
		}

		for (int i = 0; i < connectionTokens.size(); i++) {
			String connectionToken = connectionTokens.get(i);
			Connection connection = connections.get(i);

			if (connectionToken == null) {
				Assert.assertNull(
					"Connection at position " + i + " is not null", connection);
			}
			else {
				Assert.assertNotNull(
					"Connection at position " + i + " is null", connection);

				List<String> previousConnectionTokens =
					connectionTokens.subList(0, i);
				List<Connection> previousConnections = connections.subList(
					0, i);

				int connectionTokenIndex = previousConnectionTokens.indexOf(
					connectionToken);
				int connectionIndex = previousConnections.indexOf(connection);

				Assert.assertEquals(
					"Connection tokens :" + connectionTokens +
						", Connections :" + connections + ", fails at " + i,
					connectionTokenIndex, connectionIndex);
			}
		}
	}

	private Connection _getConnection(SessionHolder sessionHolder) {
		if (sessionHolder == null) {
			return null;
		}

		Session session = sessionHolder.getSession();

		AtomicReference<Connection> connectionReference =
			new AtomicReference<>();

		session.doWork(
			new Work() {

				@Override
				public void execute(Connection connection) {
					connectionReference.set(connection);
				}

			});

		return connectionReference.get();
	}

	private List<SessionHolder> _runInTransactions(
		List<PlatformTransactionManager> platformTransactionManagers,
		List<TransactionConfig> transactionConfigs) {

		Assert.assertEquals(
			platformTransactionManagers.size(), transactionConfigs.size());

		if (platformTransactionManagers.isEmpty()) {
			return new ArrayList<>();
		}

		if (!(platformTransactionManagers instanceof ArrayList)) {
			platformTransactionManagers = new ArrayList<>(
				platformTransactionManagers);
		}

		if (!(transactionConfigs instanceof ArrayList)) {
			transactionConfigs = new ArrayList<>(transactionConfigs);
		}

		PlatformTransactionManager platformTransactionManager =
			platformTransactionManagers.remove(0);

		TransactionConfig transactionConfig = transactionConfigs.remove(0);

		TransactionInvokerImpl transactionInvokerImpl =
			new TransactionInvokerImpl();

		transactionInvokerImpl.setPlatformTransactionManager(
			platformTransactionManager);
		transactionInvokerImpl.setTransactionExecutor(
			new DefaultTransactionExecutor());

		SessionFactory sessionFactory;

		if (platformTransactionManager instanceof HibernateTransactionManager) {
			HibernateTransactionManager hibernateTransactionManager =
				(HibernateTransactionManager)platformTransactionManager;

			sessionFactory = hibernateTransactionManager.getSessionFactory();
		}
		else {
			PortletTransactionManager portletTransactionManager =
				(PortletTransactionManager)platformTransactionManager;

			sessionFactory = ReflectionTestUtil.getFieldValue(
				portletTransactionManager, "_portletSessionFactory");
		}

		try {
			List<PlatformTransactionManager> subPlatformTransactionManagers =
				platformTransactionManagers;
			List<TransactionConfig> subTransactionConfigs = transactionConfigs;

			return transactionInvokerImpl.invoke(
				transactionConfig,
				() -> {
					SessionHolder sessionHolder =
						SpringHibernateThreadLocalUtil.getResource(
							sessionFactory);

					List<SessionHolder> sessionHolders = _runInTransactions(
						subPlatformTransactionManagers, subTransactionConfigs);

					// Check for reset

					Assert.assertSame(
						"Reset check failure at position " +
							subPlatformTransactionManagers.size(),
						sessionHolder,
						SpringHibernateThreadLocalUtil.getResource(
							sessionFactory));

					sessionHolders.add(0, sessionHolder);

					return sessionHolders;
				});
		}
		catch (Throwable t) {
			return ReflectionUtil.throwException(t);
		}
	}

	private static final TransactionConfig _NOT_SUPPORTED_TRANSACTION_CONFIG =
		_createTransactionConfig(Propagation.NOT_SUPPORTED);

	private static final TransactionConfig _REQUIRED_TRANSACTION_CONFIG =
		_createTransactionConfig(Propagation.REQUIRED);

	private static final TransactionConfig _REQUIRES_NEW_TRANSACTION_CONFIG =
		_createTransactionConfig(Propagation.REQUIRES_NEW);

	private static final TransactionConfig _SUPPORTS_TRANSACTION_CONFIG =
		_createTransactionConfig(Propagation.SUPPORTS);

	private static SessionFactory _portalSessionFactory;
	private static HibernateTransactionManager _portalTransactionManager;

}