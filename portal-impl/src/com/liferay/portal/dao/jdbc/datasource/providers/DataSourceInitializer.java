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

import java.util.Properties;

import javax.sql.DataSource;

/**
 * @author Manuel de la Peña
 */
public interface DataSourceInitializer {

	public DataSource init(Properties properties) throws Exception;

	public String[] C3P0_PROPERTIES = new String[] {
		"acquireIncrement", "acquireRetryAttempts", "acquireRetryDelay",
		"connectionCustomizerClassName", "idleConnectionTestPeriod",
		"initialPoolSize", "maxIdleTime", "maxPoolSize", "minPoolSize",
		"numHelperThreads", "preferredTestQuery"
	};

	public String[] DBCP_PROPERTIES = {
		"defaultTransactionIsolation", "maxActive", "minIdle",
		"removeAbandonedTimeout"
	};

	public String[] HIKARICP_PROPERTIES = {
		"autoCommit", "connectionTestQuery", "connectionTimeout", "idleTimeout",
		"initializationFailFast", "maximumPoolSize", "maxLifetime",
		"minimumIdle", "registerMbeans"
	};

	public String[] LIFERAY_PROPERTIES =
		new String[] {"jndi.name", "liferay.pool.provider"};

	public String[] TOMCAT_PROPERTIES = {
		"fairQueue", "initialSize", "jdbcInterceptors", "jmxEnabled", "maxIdle",
		"testWhileIdle", "timeBetweenEvictionRunsMillis", "useEquals",
		"validationQuery"
	};

}