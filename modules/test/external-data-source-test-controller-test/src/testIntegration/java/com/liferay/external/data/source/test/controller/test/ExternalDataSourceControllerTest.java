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
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.InputStream;

import java.net.URL;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hsqldb.jdbc.JDBCDriver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		PropsUtil.set("jdbc.test.driverClassName", JDBCDriver.class.getName());
		PropsUtil.set("jdbc.test.url", _JDBC_URL);
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

		DB db = DBManagerUtil.getDB(DBType.HYPERSONIC, null);

		Properties properties = new Properties();

		properties.put("password", "");
		properties.put("user", "sa");

		URL resource = _serviceBundle.getResource("/META-INF/sql/tables.sql");

		try (Connection con = JDBCDriver.getConnection(_JDBC_URL, properties);
			InputStream is = resource.openStream()) {

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

		FileUtil.deltree(_HYPERSONIC_TEMP_DIR_NAME);
	}

	@Test
	public void testExternalDataSourceTests() throws Exception {
		TestRunListener testRunListener = new TestRunListener();

		Registry registry = RegistryUtil.getRegistry();

		registry.registerService(RunListener.class, testRunListener);

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

	private static final String _JDBC_URL =
		"jdbc:hsqldb:" + _HYPERSONIC_TEMP_DIR_NAME + _EXTERNAL_DATABASE_NAME +
			";hsqldb.write_delay=false;shutdown=true";

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