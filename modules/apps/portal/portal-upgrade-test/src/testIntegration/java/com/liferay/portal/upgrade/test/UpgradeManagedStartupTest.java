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

package com.liferay.portal.upgrade.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.tools.DBUpgrader;
import com.liferay.portal.util.PropsValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Luis Ortiz
 */
@RunWith(Arquillian.class)
public class UpgradeManagedStartupTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_bundle = FrameworkUtil.getBundle(UpgradeManagedStartupTest.class);
	}

	@Before
	public void setUp() {
		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_LOCK_REFRESH_TIME",
			_REFRESH_TIME);
	}

	@After
	public void tearDown() throws IllegalAccessException, SQLException {
		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_MANAGED_STARTUP",
			_ORIGINAL_UPGRADE_DATABASE_MANAGED_STARTUP);

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_LOCK_REFRESH_TIME",
			_ORIGINAL_UPGRADE_DATABASE_LOCK_REFRESH_TIME);

		_dropTable();
	}

	@Test(timeout = 6 * _REFRESH_TIME)
	public void testAcquireLockWhenLocksExists() throws Exception {
		Thread thread = null;
		Object lock = null;
		Object internalLock = null;

		try {
			Assert.assertFalse(_hasLockTable());

			Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

			lock = ReflectionTestUtil.invoke(clazz, "_acquireLock", null);

			Assert.assertNotNull(lock);

			Assert.assertTrue(_hasLockTable());

			AtomicReference<Object> internalLockReference =
				new AtomicReference<>();

			CountDownLatch countDownLatch = new CountDownLatch(1);

			thread = new Thread(
				() -> {
					internalLockReference.set(
						ReflectionTestUtil.invoke(clazz, "_acquireLock", null));
					countDownLatch.countDown();
				});

			thread.start();

			countDownLatch.await(2 * _REFRESH_TIME, TimeUnit.MILLISECONDS);

			internalLock = internalLockReference.get();

			Assert.assertNull(internalLock);

			Assert.assertTrue(thread.isAlive());

			ReflectionTestUtil.invoke(
				clazz, "_releaseLock", new Class<?>[] {lock.getClass()}, lock);

			countDownLatch.await(2 * _REFRESH_TIME, TimeUnit.MILLISECONDS);

			internalLock = internalLockReference.get();

			Assert.assertNull(internalLock);

			Assert.assertFalse(thread.isAlive());

			Assert.assertFalse(_hasLockTable());
		}
		finally {
			if ((thread != null) && thread.isAlive()) {
				thread.interrupt();
			}

			if (lock != null) {
				_closeLock(lock);
			}

			if (internalLock != null) {
				_closeLock(internalLock);
			}
		}
	}

	@Test(timeout = 4 * _REFRESH_TIME)
	public void testAcquireLockWhenNoLocksExists() throws Exception {
		Thread thread = null;

		Object lock = null;

		try {
			AtomicReference<Object> internalLockReference =
				new AtomicReference<>();

			Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

			CountDownLatch countDownLatch = new CountDownLatch(1);

			thread = new Thread(
				() -> {
					internalLockReference.set(
						ReflectionTestUtil.invoke(clazz, "_acquireLock", null));
					countDownLatch.countDown();
				});

			thread.start();

			thread.join(2 * _REFRESH_TIME);

			lock = internalLockReference.get();

			Assert.assertNotNull(lock);

			Assert.assertFalse(thread.isAlive());

			ReflectionTestUtil.invoke(
				clazz, "_releaseLock", new Class<?>[] {lock.getClass()}, lock);

			Assert.assertFalse(_hasLockTable());
		}
		finally {
			if ((thread != null) && thread.isAlive()) {
				thread.interrupt();
			}

			if (lock != null) {
				_closeLock(lock);
			}
		}
	}

	@Test(timeout = 4 * _REFRESH_TIME)
	public void testReleaseLocks() throws Exception {
		Assert.assertFalse(_hasLockTable());

		Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

		Object lock = ReflectionTestUtil.invoke(clazz, "_acquireLock", null);

		try {
			Assert.assertNotNull(lock);

			Assert.assertTrue(_hasLockTable());

			ReflectionTestUtil.invoke(
				clazz, "_releaseLock", new Class<?>[] {lock.getClass()}, lock);

			Assert.assertFalse(_hasLockTable());
		}
		finally {
			if (lock != null) {
				_closeLock(lock);
			}
		}
	}

	@Test(timeout = 4 * _REFRESH_TIME)
	public void testWaitForLocksWhenManagedStartupDisabled() throws Exception {
		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_MANAGED_STARTUP", false);

		Thread thread = null;

		Object lock = null;

		try {
			Assert.assertFalse(_hasLockTable());

			Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

			lock = ReflectionTestUtil.invoke(clazz, "_acquireLock", null);

			Assert.assertNotNull(lock);

			Assert.assertTrue(_hasLockTable());

			thread = new Thread(
				() -> {
					try {
						DBUpgrader.waitForLocks();
					}
					catch (Exception exception) {
					}
				});

			thread.start();

			thread.join(_REFRESH_TIME);

			Assert.assertFalse(thread.isAlive());

			ReflectionTestUtil.invoke(
				clazz, "_releaseLock", new Class<?>[] {lock.getClass()}, lock);

			Assert.assertFalse(_hasLockTable());
		}
		finally {
			if ((thread != null) && thread.isAlive()) {
				thread.interrupt();
			}

			if (lock != null) {
				_closeLock(lock);
			}
		}
	}

	@Test(timeout = 6 * _REFRESH_TIME)
	public void testWaitForLocksWhenManagedStartupEnabled()
		throws ClassNotFoundException, InterruptedException, SQLException {

		ReflectionTestUtil.setFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_MANAGED_STARTUP", true);

		Thread thread = null;

		Object lock = null;

		try {
			Assert.assertFalse(_hasLockTable());

			Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

			lock = ReflectionTestUtil.invoke(clazz, "_acquireLock", null);

			Assert.assertNotNull(lock);

			Assert.assertTrue(_hasLockTable());

			thread = new Thread(
				() -> {
					try {
						DBUpgrader.waitForLocks();
					}
					catch (Exception exception) {
					}
				});

			thread.start();

			Thread.sleep(_REFRESH_TIME);

			Assert.assertTrue(thread.isAlive());

			Thread.sleep(2 * _REFRESH_TIME);

			Assert.assertTrue(thread.isAlive());

			ReflectionTestUtil.invoke(
				clazz, "_releaseLock", new Class<?>[] {lock.getClass()}, lock);

			Assert.assertFalse(_hasLockTable());

			thread.join(2 * _REFRESH_TIME);

			Assert.assertFalse(thread.isAlive());
		}
		finally {
			if ((thread != null) && thread.isAlive()) {
				thread.interrupt();
			}

			if (lock != null) {
				_closeLock(lock);
			}
		}
	}

	private void _closeLock(Object lock) throws SQLException {
		Connection connection = ReflectionTestUtil.getFieldValue(
			lock, "_connection");

		if (!connection.isClosed()) {
			boolean autocommit = ReflectionTestUtil.getFieldValue(
				lock, "_autocommit");

			connection.rollback();
			connection.setAutoCommit(autocommit);
			connection.close();
		}
	}

	private void _dropTable() throws SQLException {
		String lockTableName = ReflectionTestUtil.getFieldValue(
			DBUpgrader.class, "_UPGRADES_LOCK_TABLE");

		try (Connection connection = DataAccess.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(
				SQLTransformer.transform(
					StringBundler.concat(
						"DROP_TABLE_IF_EXISTS(", lockTableName, ")")))) {

			preparedStatement.executeUpdate();
		}
	}

	private boolean _hasLockTable() throws ClassNotFoundException {
		Class<?> clazz = _bundle.loadClass(DBUpgrader.class.getName());

		return ReflectionTestUtil.invoke(clazz, "_hasLockTable", null);
	}

	private static final long _ORIGINAL_UPGRADE_DATABASE_LOCK_REFRESH_TIME =
		ReflectionTestUtil.getFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_LOCK_REFRESH_TIME");

	private static final boolean _ORIGINAL_UPGRADE_DATABASE_MANAGED_STARTUP =
		ReflectionTestUtil.getFieldValue(
			PropsValues.class, "UPGRADE_DATABASE_MANAGED_STARTUP");

	private static final long _REFRESH_TIME = 2 * Time.SECOND;

	private static Bundle _bundle;

}