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

package com.liferay.external.data.source.test.controller.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.AssumeTestRule;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.callback.HypersonicServerTestCallback;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.net.URL;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.server.Server;
import org.hsqldb.server.ServerConstants;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * @author Preston Crary
 */
@RunWith(Arquillian.class)
public class ExternalDataSourceControllerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new AssumeTestRule("assume"), new LiferayIntegrationTestRule());

	public static void assume() {
		DB db = DBManagerUtil.getDB();

		Assume.assumeTrue(DBType.HYPERSONIC.equals(db.getDBType()));
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		DriverManager.registerDriver(JDBCDriver.driverInstance);

		final CountDownLatch startCountDownLatch = new CountDownLatch(1);

		_server = new Server() {

			@Override
			public int stop() {
				try (PrintWriter logPrintWriter = getLogWriter();
					PrintWriter errPrintWriter = getErrWriter()) {

					int state = super.stop();

					if (!_shutdownCountDownLatch.await(1, TimeUnit.MINUTES)) {
						throw new IllegalStateException(
							"Unable to shut down Hypersonic " +
								_EXTERNAL_DATABASE_NAME);
					}

					return state;
				}
				catch (InterruptedException ie) {
					return ReflectionUtil.throwException(ie);
				}
			}

			@Override
			protected synchronized void setState(int state) {
				super.setState(state);

				if (state == ServerConstants.SERVER_STATE_ONLINE) {
					startCountDownLatch.countDown();
				}
				else if (state == ServerConstants.SERVER_STATE_SHUTDOWN) {
					_shutdownCountDownLatch.countDown();
				}
			}

			private final CountDownLatch _shutdownCountDownLatch =
				new CountDownLatch(1);

		};

		File logDir = new File(_HYPERSONIC_TEMP_DIR_NAME);

		if (!logDir.exists()) {
			logDir.mkdirs();
		}

		File errorLogFile = new File(
			_HYPERSONIC_TEMP_DIR_NAME, _EXTERNAL_DATABASE_NAME + ".err.log");

		if (!errorLogFile.exists()) {
			errorLogFile.createNewFile();
		}

		_server.setErrWriter(new UnsyncPrintWriter(errorLogFile));

		File standardLogFile = new File(
			_HYPERSONIC_TEMP_DIR_NAME, _EXTERNAL_DATABASE_NAME + ".std.log");

		if (!standardLogFile.exists()) {
			standardLogFile.createNewFile();
		}

		_server.setLogWriter(new UnsyncPrintWriter(standardLogFile));

		_server.setDatabaseName(0, _EXTERNAL_DATABASE_NAME);
		_server.setDatabasePath(
			0, _HYPERSONIC_TEMP_DIR_NAME + _EXTERNAL_DATABASE_NAME);

		_server.start();

		if (!startCountDownLatch.await(1, TimeUnit.MINUTES)) {
			throw new IllegalStateException(
				"Unable to start up Hypersonic " + _EXTERNAL_DATABASE_NAME);
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		try (Connection con = DriverManager.getConnection(
				HypersonicServerTestCallback.DATABASE_URL_BASE +
					_EXTERNAL_DATABASE_NAME,
				"sa", "");
			Statement statement = con.createStatement()) {

			statement.execute("SHUTDOWN COMPACT");
		}

		_server.stop();

		Path folderPath = Paths.get(_HYPERSONIC_TEMP_DIR_NAME);

		if (!Files.exists(folderPath)) {
			return;
		}

		Files.walkFileTree(
			folderPath,
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult postVisitDirectory(
						Path dirPath, IOException ioe)
					throws IOException {

					if (ioe != null) {
						throw ioe;
					}

					Files.delete(dirPath);

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(
						Path filePath, BasicFileAttributes basicFileAttributes)
					throws IOException {

					Files.delete(filePath);

					return FileVisitResult.CONTINUE;
				}

			});
	}

	@Before
	public void setUp() throws Exception {
		PropsUtil.set("jdbc.test.driverClassName", JDBCDriver.class.getName());
		PropsUtil.set(
			"jdbc.test.url",
			HypersonicServerTestCallback.DATABASE_URL_BASE +
				_EXTERNAL_DATABASE_NAME);
		PropsUtil.set("jdbc.test.username", "sa");
		PropsUtil.set("jdbc.test.password", "");
		PropsUtil.set("jdbc.test.initializationFailTimeout", "0");

		Bundle testBundle = FrameworkUtil.getBundle(
			ExternalDataSourceControllerTest.class);

		_bundleContext = testBundle.getBundleContext();

		_apiBundle = _installBundle(
			"/com.liferay.external.data.source.test.api.jar");
		_serviceBundle = _installBundle(
			"/com.liferay.external.data.source.test.service.jar");

		URL resource = _serviceBundle.getResource("/META-INF/sql/tables.sql");

		DB db = DBManagerUtil.getDB(DBType.HYPERSONIC, null);

		try (InputStream is = resource.openStream();
			Connection con = DriverManager.getConnection(
				HypersonicServerTestCallback.DATABASE_URL_BASE +
					_EXTERNAL_DATABASE_NAME,
				"sa", "")) {

			db.runSQL(con, StringUtil.read(is));
		}

		_apiBundle.start();

		_serviceBundle.start();
	}

	@After
	public void tearDown() throws Exception {
		_serviceBundle.stop();

		_serviceBundle.uninstall();

		_apiBundle.stop();

		_apiBundle.uninstall();
	}

	@Test
	public void testExternalDataSourceTests() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		TestRunListener testRunListener = new TestRunListener();

		Map<String, Object> properties = new HashMap<>();

		properties.put("service.ranking", Integer.MAX_VALUE);

		registry.registerService(
			RunListener.class, testRunListener, properties);

		Bundle bundle = _installBundle(
			"/com.liferay.external.data.source.test.jar");

		try {
			bundle.start();

			Assert.assertTrue(
				testRunListener._failures.toString(),
				testRunListener._failures.isEmpty());
		}
		catch (Exception e) {
			for (Failure failure : testRunListener._failures) {
				e.addSuppressed(failure.getException());
			}

			throw e;
		}
		finally {
			bundle.stop();

			bundle.uninstall();
		}
	}

	private Bundle _installBundle(String path) throws Exception {
		try (InputStream is =
				ExternalDataSourceControllerTest.class.getResourceAsStream(
					path)) {

			return _bundleContext.installBundle(path, is);
		}
	}

	private static final String _EXTERNAL_DATABASE_NAME = "external";

	private static final String _HYPERSONIC_TEMP_DIR_NAME =
		PropsValues.LIFERAY_HOME + "/data/hypersonic_temp/";

	private static Server _server;

	private Bundle _apiBundle;
	private BundleContext _bundleContext;
	private Bundle _serviceBundle;

	private static class TestRunListener extends RunListener {

		@Override
		public void testFailure(Failure failure) throws Exception {
			_failures.add(failure);
		}

		private final List<Failure> _failures = new ArrayList<>();

	}

}