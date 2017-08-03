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
import com.liferay.portal.dao.jdbc.pool.metrics.TomcatConnectionPoolMetrics;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.util.Map;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import javax.sql.DataSource;

import jodd.bean.BeanUtil;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.jmx.ConnectionPool;

/**
 * @author Manuel de la Peña
 */
public class TomcatDataSourceInitializer extends BaseDataSourceInitializer {

	public ServiceTracker<MBeanServer, MBeanServer> getServiceTracker() {
		return _serviceTracker;
	}

	@Override
	public DataSource init(Properties properties) throws Exception {
		PoolProperties poolProperties = new PoolProperties();

		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();

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

			// Ignore HikariCP property

			if (isPresentPropertyFunction.apply(HIKARICP_PROPERTIES)) {
				continue;
			}

			// Set Tomcat JDBC property

			try {
				BeanUtil.setProperty(poolProperties, key, value);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Property " + key + " is an invalid Tomcat JDBC " +
							"property");
				}
			}
		}

		String poolName = StringUtil.randomString();

		poolProperties.setName(poolName);

		org.apache.tomcat.jdbc.pool.DataSource dataSource =
			new org.apache.tomcat.jdbc.pool.DataSource(poolProperties);

		if (poolProperties.isJmxEnabled()) {
			Registry registry = RegistryUtil.getRegistry();

			_serviceTracker = registry.trackServices(
				MBeanServer.class,
				new MBeanServerServiceTrackerCustomizer(dataSource, poolName));

			_serviceTracker.open();
		}

		registerConnectionPoolMetrics(
			new TomcatConnectionPoolMetrics(dataSource));

		return dataSource;
	}

	private static final String _TOMCAT_JDBC_POOL_OBJECT_NAME_PREFIX =
		"TomcatJDBCPool:type=ConnectionPool,name=";

	private static final Log _log = LogFactoryUtil.getLog(
		TomcatDataSourceInitializer.class);

	private ServiceTracker<MBeanServer, MBeanServer> _serviceTracker;

	private static class MBeanServerServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<MBeanServer, MBeanServer> {

		public MBeanServerServiceTrackerCustomizer(
				org.apache.tomcat.jdbc.pool.DataSource dataSource,
				String poolName)
			throws MalformedObjectNameException {

			_dataSource = dataSource;
			_objectName = new ObjectName(
				_TOMCAT_JDBC_POOL_OBJECT_NAME_PREFIX + poolName);
		}

		@Override
		public MBeanServer addingService(
			ServiceReference<MBeanServer> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			MBeanServer mBeanServer = registry.getService(serviceReference);

			try {
				org.apache.tomcat.jdbc.pool.ConnectionPool jdbcConnectionPool =
					_dataSource.createPool();

				ConnectionPool jmxConnectionPool =
					jdbcConnectionPool.getJmxPool();

				mBeanServer.registerMBean(jmxConnectionPool, _objectName);
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			return mBeanServer;
		}

		@Override
		public void modifiedService(
			ServiceReference<MBeanServer> serviceReference,
			MBeanServer mBeanServer) {
		}

		@Override
		public void removedService(
			ServiceReference<MBeanServer> serviceReference,
			MBeanServer mBeanServer) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			try {
				mBeanServer.unregisterMBean(_objectName);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		private final org.apache.tomcat.jdbc.pool.DataSource _dataSource;
		private final ObjectName _objectName;

	}

}