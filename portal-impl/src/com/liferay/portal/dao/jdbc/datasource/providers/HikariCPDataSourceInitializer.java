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

package com.liferay.portal.dao.jdbc.datasource.providers;

import com.liferay.portal.dao.jdbc.functions.IsPresentPropertyFunction;
import com.liferay.portal.dao.jdbc.pool.metrics.HikariConnectionPoolMetrics;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ClassLoaderUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.JarUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import jodd.bean.BeanUtil;

/**
 * @author Manuel de la Peña
 */
public class HikariCPDataSourceInitializer extends BaseDataSourceInitializer {

	@Override
	public DataSource init(Properties properties) throws Exception {
		testLiferayPoolProviderClass(_HIKARICP_DATASOURCE_CLASS_NAME);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		Class<?> hikariDataSourceClazz = contextClassLoader.loadClass(
			_HIKARICP_DATASOURCE_CLASS_NAME);

		Object hikariDataSource = hikariDataSourceClazz.newInstance();

		String connectionPropertiesString = (String)properties.remove(
			"connectionProperties");

		if (connectionPropertiesString != null) {
			Properties connectionProperties = PropertiesUtil.load(
				StringUtil.replace(
					connectionPropertiesString, CharPool.SEMICOLON,
					CharPool.NEW_LINE));

			BeanUtil.setProperty(
				hikariDataSource, "dataSourceProperties", connectionProperties);
		}

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

			// Map org.apache.commons.dbcp.BasicDataSource to Hikari CP

			if (StringUtil.equalsIgnoreCase(key, "url")) {
				key = "jdbcUrl";
			}

			IsPresentPropertyFunction isPresentPropertyFunction =
				new IsPresentPropertyFunction(key);

			// Ignore Liferay property

			if (isPresentPropertyFunction.apply(LIFERAY_PROPERTIES)) {
				continue;
			}

			// Ignore C3P0 property

			if (isPresentPropertyFunction.apply(C3P0_PROPERTIES)) {
				continue;
			}

			// Ignore DBCP property

			if (isPresentPropertyFunction.apply(DBCP_PROPERTIES)) {
				continue;
			}

			// Ignore Tomcat JDBC property

			if (isPresentPropertyFunction.apply(TOMCAT_PROPERTIES)) {
				continue;
			}

			// Set HikariCP property

			try {
				BeanUtil.setProperty(hikariDataSource, key, value);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Property " + key + " is an invalid HikariCP property");
				}
			}
		}

		registerConnectionPoolMetrics(
			new HikariConnectionPoolMetrics(hikariDataSource));

		return (DataSource)hikariDataSource;
	}

	protected void testLiferayPoolProviderClass(String className)
		throws Exception {

		try {
			Class.forName(className);
		}
		catch (ClassNotFoundException cnfe) {
			if (!ServerDetector.isJetty() && !ServerDetector.isTomcat()) {
				throw cnfe;
			}

			String url = PropsUtil.get(
				PropsKeys.SETUP_LIFERAY_POOL_PROVIDER_JAR_URL,
				new Filter(PropsValues.JDBC_DEFAULT_LIFERAY_POOL_PROVIDER));
			String name = PropsUtil.get(
				PropsKeys.SETUP_LIFERAY_POOL_PROVIDER_JAR_NAME,
				new Filter(PropsValues.JDBC_DEFAULT_LIFERAY_POOL_PROVIDER));

			if (Validator.isNull(url) || Validator.isNull(name)) {
				throw cnfe;
			}

			ClassLoader classLoader = ClassLoaderUtil.getPortalClassLoader();

			if (!(classLoader instanceof URLClassLoader)) {
				_log.error(
					"Unable to install JAR because the portal class loader " +
						"is not an instance of URLClassLoader");

				return;
			}

			JarUtil.downloadAndInstallJar(
				new URL(url), PropsValues.LIFERAY_LIB_PORTAL_DIR, name,
				(URLClassLoader)classLoader);
		}
	}

	private static final String _HIKARICP_DATASOURCE_CLASS_NAME =
		"com.zaxxer.hikari.HikariDataSource";

	private static final Log _log = LogFactoryUtil.getLog(
		HikariCPDataSourceInitializer.class);

}