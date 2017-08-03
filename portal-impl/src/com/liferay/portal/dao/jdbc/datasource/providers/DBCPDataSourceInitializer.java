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

import com.liferay.portal.dao.jdbc.pool.metrics.DBCPConnectionPoolMetrics;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
 * @author Manuel de la Peña
 */
public class DBCPDataSourceInitializer extends BaseDataSourceInitializer {

	@Override
	public DataSource init(Properties properties) throws Exception {
		DataSource dataSource = BasicDataSourceFactory.createDataSource(
			properties);

		registerConnectionPoolMetrics(
			new DBCPConnectionPoolMetrics((BasicDataSource)dataSource));

		return dataSource;
	}

}