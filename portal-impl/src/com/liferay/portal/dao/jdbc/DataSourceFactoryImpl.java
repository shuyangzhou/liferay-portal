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

package com.liferay.portal.dao.jdbc;

import com.liferay.portal.dao.jdbc.datasource.providers.C3P0DataSourceInitializer;
import com.liferay.portal.dao.jdbc.datasource.providers.DBCPDataSourceInitializer;
import com.liferay.portal.dao.jdbc.datasource.providers.DataSourceInitializer;
import com.liferay.portal.dao.jdbc.datasource.providers.HikariCPDataSourceInitializer;
import com.liferay.portal.dao.jdbc.datasource.providers.TomcatDataSourceInitializer;
import com.liferay.portal.dao.jdbc.functions.RetryDataSourceFunction;
import com.liferay.portal.dao.jdbc.util.DataSourceWrapper;
import com.liferay.portal.dao.jdbc.util.RetryDataSourceWrapper;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.DataSourceFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.jndi.JNDIUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.SortedProperties;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.hibernate.DialectDetector;
import com.liferay.portal.util.JarUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.ServiceTracker;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.Properties;
import java.util.function.Function;

import javax.management.MBeanServer;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
@DoPrivileged
public class DataSourceFactoryImpl implements DataSourceFactory {

	@Override
	public void destroyDataSource(DataSource dataSource) throws Exception {
		while (dataSource instanceof DataSourceWrapper) {
			DataSourceWrapper dataSourceWrapper = (DataSourceWrapper)dataSource;

			dataSource = dataSourceWrapper.getWrappedDataSource();
		}

		if (dataSource instanceof ComboPooledDataSource) {
			ComboPooledDataSource comboPooledDataSource =
				(ComboPooledDataSource)dataSource;

			comboPooledDataSource.close();
		}
		else if (dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
			org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource =
				(org.apache.tomcat.jdbc.pool.DataSource)dataSource;

			ServiceTracker<MBeanServer, MBeanServer> serviceTracker =
				_tomcatDataSourceInitializer.getServiceTracker();

			if (serviceTracker != null) {
				serviceTracker.close();
			}

			tomcatDataSource.close();
		}
	}

	@Override
	public DataSource initDataSource(Properties properties) throws Exception {
		RetryDataSourceFunction retryDataSourceFunction =
			new RetryDataSourceFunction(
				properties, PropsValues.RETRY_DATA_SOURCE_DELAY_SECONDS,
				PropsValues.RETRY_DATA_SOURCE_MAX_RETRIES);

		return retryDataSourceFunction.apply(_getDataSourceFunction);
	}

	@Override
	public DataSource initDataSource(
			String driverClassName, String url, String userName,
			String password, String jndiName)
		throws Exception {

		Properties properties = new Properties();

		properties.setProperty("driverClassName", driverClassName);
		properties.setProperty("url", url);
		properties.setProperty("username", userName);
		properties.setProperty("password", password);
		properties.setProperty("jndi.name", jndiName);

		return initDataSource(properties);
	}

	public interface PACL {

		public DataSource getDataSource(DataSource dataSource);

	}

	protected void testDatabaseClass(Properties properties) throws Exception {
		String driverClassName = properties.getProperty("driverClassName");

		try {
			Class.forName(driverClassName);
		}
		catch (ClassNotFoundException cnfe) {
			if (!ServerDetector.isJetty() && !ServerDetector.isTomcat()) {
				throw cnfe;
			}

			String url = PropsUtil.get(
				PropsKeys.SETUP_DATABASE_JAR_URL, new Filter(driverClassName));
			String name = PropsUtil.get(
				PropsKeys.SETUP_DATABASE_JAR_NAME, new Filter(driverClassName));

			if (Validator.isNull(url) || Validator.isNull(name)) {
				throw cnfe;
			}

			ClassLoader classLoader = SystemException.class.getClassLoader();

			if (!(classLoader instanceof URLClassLoader)) {
				_log.error(
					"Unable to install JAR because the system class loader " +
						"is not an instance of URLClassLoader");

				return;
			}

			JarUtil.downloadAndInstallJar(
				new URL(url), PropsValues.LIFERAY_LIB_GLOBAL_DIR, name,
				(URLClassLoader)classLoader);
		}
	}

	private DataSource _initDataSource(Properties properties) throws Exception {
		Properties defaultProperties = PropsUtil.getProperties(
			"jdbc.default.", true);

		PropertiesUtil.merge(defaultProperties, properties);

		properties = defaultProperties;

		String jndiName = properties.getProperty("jndi.name");

		if (Validator.isNotNull(jndiName)) {
			try {
				Properties jndiEnvironmentProperties = PropsUtil.getProperties(
					PropsKeys.JNDI_ENVIRONMENT, true);

				Context context = new InitialContext(jndiEnvironmentProperties);

				return (DataSource)JNDIUtil.lookup(context, jndiName);
			}
			catch (Exception e) {
				_log.error("Unable to lookup " + jndiName, e);
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Data source properties:\n");

			SortedProperties sortedProperties = new SortedProperties(
				properties);

			_log.debug(PropertiesUtil.toString(sortedProperties));
		}

		testDatabaseClass(properties);

		DataSource dataSource = null;

		String liferayPoolProvider =
			PropsValues.JDBC_DEFAULT_LIFERAY_POOL_PROVIDER;

		if (StringUtil.equalsIgnoreCase(liferayPoolProvider, "c3p0") ||
			StringUtil.equalsIgnoreCase(liferayPoolProvider, "c3po")) {

			if (_log.isDebugEnabled()) {
				_log.debug("Initializing C3P0 data source");
			}

			DataSourceInitializer c3P0DataSourceInitializer =
				new C3P0DataSourceInitializer();

			dataSource = c3P0DataSourceInitializer.init(properties);
		}
		else if (StringUtil.equalsIgnoreCase(liferayPoolProvider, "dbcp")) {
			if (_log.isDebugEnabled()) {
				_log.debug("Initializing DBCP data source");
			}

			DataSourceInitializer dbcpDataSourceInitializer =
				new DBCPDataSourceInitializer();

			dataSource = dbcpDataSourceInitializer.init(properties);
		}
		else if (StringUtil.equalsIgnoreCase(liferayPoolProvider, "hikaricp")) {
			if (_log.isDebugEnabled()) {
				_log.debug("Initializing HikariCP data source");
			}

			DataSourceInitializer hikariCPDataSourceInitializer =
				new HikariCPDataSourceInitializer();

			dataSource = hikariCPDataSourceInitializer.init(properties);
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug("Initializing Tomcat data source");
			}

			dataSource = _tomcatDataSourceInitializer.init(properties);
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Created data source " + dataSource.getClass());
		}

		if (PropsValues.RETRY_DATA_SOURCE_MAX_RETRIES > 0) {
			DBType dbType = DBManagerUtil.getDBType(
				DialectDetector.getDialect(dataSource));

			if (dbType == DBType.SYBASE) {
				dataSource = new RetryDataSourceWrapper(dataSource);
			}
		}

		return _pacl.getDataSource(dataSource);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DataSourceFactoryImpl.class);

	private static final PACL _pacl = new NoPACL();
	private static final TomcatDataSourceInitializer
		_tomcatDataSourceInitializer = new TomcatDataSourceInitializer();

	private final Function<Properties, DataSource> _getDataSourceFunction =
		(Properties properties) -> {
			try {
				return _initDataSource(properties);
			}
			catch (Exception e) {
				throw new RuntimeException("No dialect found", e);
			}
		};

	private static class NoPACL implements PACL {

		@Override
		public DataSource getDataSource(DataSource dataSource) {
			return dataSource;
		}

	}

}