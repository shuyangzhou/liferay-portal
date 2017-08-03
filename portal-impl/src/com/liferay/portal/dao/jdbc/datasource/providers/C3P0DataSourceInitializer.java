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
import com.liferay.portal.dao.jdbc.pool.metrics.C3P0ConnectionPoolMetrics;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.StringUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.Enumeration;
import java.util.Properties;

import javax.sql.DataSource;

import jodd.bean.BeanUtil;

/**
 * @author Manuel de la Peña
 */
public class C3P0DataSourceInitializer extends BaseDataSourceInitializer {

	@Override
	public DataSource init(Properties properties) throws Exception {
		ComboPooledDataSource comboPooledDataSource =
			new ComboPooledDataSource();

		String identityToken = StringUtil.randomString();

		comboPooledDataSource.setIdentityToken(identityToken);

		String connectionPropertiesString = (String)properties.remove(
			"connectionProperties");

		if (connectionPropertiesString != null) {
			Properties connectionProperties = PropertiesUtil.load(
				StringUtil.replace(
					connectionPropertiesString, CharPool.SEMICOLON,
					CharPool.NEW_LINE));

			comboPooledDataSource.setProperties(connectionProperties);
		}

		Enumeration<String> enu =
			(Enumeration<String>)properties.propertyNames();

		while (enu.hasMoreElements()) {
			String key = enu.nextElement();

			String value = properties.getProperty(key);

			// Map org.apache.commons.dbcp.BasicDataSource to C3PO

			if (StringUtil.equalsIgnoreCase(key, "driverClassName")) {
				key = "driverClass";
			}
			else if (StringUtil.equalsIgnoreCase(key, "url")) {
				key = "jdbcUrl";
			}
			else if (StringUtil.equalsIgnoreCase(key, "username")) {
				key = "user";
			}

			IsPresentPropertyFunction isPresentPropertyFunction =
				new IsPresentPropertyFunction(key);

			// Ignore Liferay property

			if (isPresentPropertyFunction.apply(LIFERAY_PROPERTIES)) {
				continue;
			}

			// Ignore DBCP property

			if (isPresentPropertyFunction.apply(DBCP_PROPERTIES)) {
				continue;
			}

			// Ignore HikariCP property

			if (isPresentPropertyFunction.apply(HIKARICP_PROPERTIES)) {
				continue;
			}

			// Ignore Tomcat JDBC property

			if (isPresentPropertyFunction.apply(TOMCAT_PROPERTIES)) {
				continue;
			}

			// Set C3PO property

			try {
				BeanUtil.setProperty(comboPooledDataSource, key, value);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Property " + key + " is an invalid C3PO property");
				}
			}
		}

		registerConnectionPoolMetrics(
			new C3P0ConnectionPoolMetrics(comboPooledDataSource));

		return comboPooledDataSource;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		C3P0DataSourceInitializer.class);

}