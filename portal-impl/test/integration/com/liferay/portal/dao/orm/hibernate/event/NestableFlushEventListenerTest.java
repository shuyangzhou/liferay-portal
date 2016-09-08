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

package com.liferay.portal.dao.orm.hibernate.event;

import com.liferay.portal.dao.orm.hibernate.SessionFactoryImpl;
import com.liferay.portal.dao.orm.hibernate.SessionImpl;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.model.impl.ClassNameImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.concurrent.Callable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.StaleStateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.event.AutoFlushEventListener;
import org.hibernate.event.EventListeners;
import org.hibernate.event.FlushEventListener;
import org.hibernate.event.def.DefaultAutoFlushEventListener;
import org.hibernate.event.def.DefaultFlushEventListener;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class NestableFlushEventListenerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		SessionFactoryImpl sessionFactoryImpl =
			(SessionFactoryImpl)PortalBeanLocatorUtil.locate(
				"liferaySessionFactory");

		_sessionFactoryImplementor =
			sessionFactoryImpl.getSessionFactoryImplementor();
	}

	@After
	public void tearDown() {
		_session.close();
	}

	@Test
	public void testDefaultAutoFlushEventListener() throws Throwable {
		org.hibernate.impl.SessionFactoryImpl sessionFactoryImpl =
			(org.hibernate.impl.SessionFactoryImpl)_sessionFactoryImplementor;

		EventListeners eventListeners = sessionFactoryImpl.getEventListeners();

		eventListeners.setAutoFlushEventListeners(new AutoFlushEventListener[] {
			new DefaultAutoFlushEventListener()
		});

		try {
			_testAutoFlushEventListener();

			Assert.fail();
		}
		catch (ORMException orme) {
			Assert.assertTrue(
				orme.getMessage(),
				_isCausedByException(orme, IndexOutOfBoundsException.class));
		}
		finally {
			eventListeners.setAutoFlushEventListeners(
				new AutoFlushEventListener[] {
					NestableAutoFlushEventListener.INSTANCE
			});
		}
	}

	@Test
	public void testDefaultFlushEventListener() throws Throwable {
		org.hibernate.impl.SessionFactoryImpl sessionFactoryImpl =
			(org.hibernate.impl.SessionFactoryImpl)_sessionFactoryImplementor;

		EventListeners eventListeners = sessionFactoryImpl.getEventListeners();

		eventListeners.setFlushEventListeners(new FlushEventListener[] {
			new DefaultFlushEventListener()
		});

		try {
			_testFlushEventListener();

			Assert.fail();
		}
		catch (ORMException orme) {
			Assert.assertTrue(
				orme.getMessage(),
				_isCausedByException(orme, IndexOutOfBoundsException.class));
		}
		finally {
			eventListeners.setFlushEventListeners(
				new FlushEventListener[] {
					NestableFlushEventListener.INSTANCE
			});
		}
	}

	@Test
	public void testNestableAutoFlushEventListener() throws Throwable {
		_testAutoFlushEventListener();
	}

	@Test
	public void testNestableFlushEventListener() throws Throwable {
		_testFlushEventListener();
	}

	private void _flushTest(Callable<Void> callable) throws Throwable {
		_session = new SessionImpl(
			_sessionFactoryImplementor.openSession(
				new EmptyInterceptor() {

					@Override
					public String getEntityName(Object object) {
						if (object instanceof TestClassNameImpl) {
							return ClassNameImpl.class.getName();
						}

						return super.getEntityName(object);
					}

		}));

		_className1 = new TestClassNameImpl();

		_className1.setPrimaryKey(RandomTestUtil.nextLong());

		_session.save(_className1);

		_className2 = new ClassNameImpl();

		_className2.setPrimaryKey(RandomTestUtil.nextLong());

		_session.save(_className2);

		_session.flush();

		_className1.setValue(RandomTestUtil.randomString());

		_className1.setMvccVersion(_className1.getMvccVersion() + 1);

		_className2.setValue(RandomTestUtil.randomString());

		_className2.setMvccVersion(_className1.getMvccVersion() + 1);

		TransactionInvokerUtil.invoke(_transactionConfig, callable);
	}

	private boolean _isCausedByException(Throwable t, Class expectedCause) {
		while (true) {
			if (expectedCause.isInstance(t)) {
				return true;
			}

			Throwable cause = t.getCause();

			if ((t == cause) || (cause == null)) {
				break;
			}

			t = cause;
		}

		return false;
	}

	private void _testAutoFlushEventListener() throws Throwable {
		_flushTest(
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					_session.merge(_className1);
					_session.merge(_className2);

					SQLQuery query = _session.createSynchronizedSQLQuery(
						"SELECT * FROM ClassName_");

					query.list();

					return null;
				}

		});
	}

	private void _testFlushEventListener() throws Throwable {
		_flushTest(
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					_session.merge(_className1);
					_session.merge(_className2);

					_session.flush();

					return null;
				}

		});
	}

	private static SessionFactoryImplementor _sessionFactoryImplementor;
	private static final TransactionConfig _transactionConfig;

	static {
		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		_transactionConfig = builder.build();
	}

	@DeleteAfterTestRun
	private ClassName _className1;

	@DeleteAfterTestRun
	private ClassName _className2;

	private Session _session;

	private class TestClassNameImpl extends ClassNameImpl {

		@Override
			public CacheModel<ClassName> toCacheModel() {
				SQLQuery query = _session.createSynchronizedSQLQuery(
					"SELECT * FROM Release_");

				try {
					query.list();
				}
				catch (ORMException orme) {
					if (!_isCausedByException(
							orme, StaleStateException.class)) {

						throw orme;
					}
				}

				return super.toCacheModel();
			}

	}

}