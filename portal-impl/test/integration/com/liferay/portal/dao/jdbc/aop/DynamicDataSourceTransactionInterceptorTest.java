/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.dao.jdbc.aop;

import com.liferay.portal.NoSuchGroupException;
import com.liferay.portal.NoSuchLockException;
import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LockLocalServiceUtil;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.spring.transaction.MockPlatformTransactionManager;
import com.liferay.portal.spring.transaction.TransactionInterceptor;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsUtil;

import java.lang.reflect.Field;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.reflect.Whitebox;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author László Csontos
 */
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class DynamicDataSourceTransactionInterceptorTest {

	@BeforeClass
	public static void setUpClass() {
		try {
			_transactionInterceptor =
				(TransactionInterceptor)PortalBeanLocatorUtil.locate(
					"transactionAdvice");

			if (_transactionInterceptor instanceof
					DynamicDataSourceTransactionInterceptor) {

				DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
					(DynamicDataSourceTargetSource)PortalBeanLocatorUtil.locate(
						"dynamicDataSourceTargetSource");

				_dynamicDataSourceTargetSourceWrapper =
					new MockDynamicDataSourceTargetSource(
						dynamicDataSourceTargetSource);

				_liferayDataSource =
					(LazyConnectionDataSourceProxy)PortalBeanLocatorUtil.locate(
						"liferayDataSource");
			}
		}
		catch (Exception e) {
			_dynamicDataSourceTargetSourceWrapper = null;

			if (_log.isDebugEnabled()) {
				_log.debug(e);
			}
		}
	}

	@Before
	public void setUp() {

		// Check META-INF/dynamic-data-source-spring.xml

		Assume.assumeNotNull(_dynamicDataSourceTargetSourceWrapper);

		// Check R/W splitting data-source properties

		Properties readProperties = PropsUtil.getProperties("jdbc.read.", true);
		Properties writeProperties = PropsUtil.getProperties(
			"jdbc.write.", true);

		Assume.assumeFalse(readProperties.isEmpty());
		Assume.assumeFalse(writeProperties.isEmpty());
	}

	@Test
	public void testDoNotSplit() throws Exception {
		AbstractTestTemplate testTemplate = new AbstractTestTemplate() {

			@Override
			public void doTest() throws Exception {
				try {
					LockLocalServiceUtil.getLock(0L);
				}
				catch (NoSuchLockException nsle) {
					if (_log.isDebugEnabled()) {
						_log.debug(nsle);
					}
				}
			}

		};

		testTemplate.test();

		Assert.assertEquals(
			0, _dynamicDataSourceTargetSourceWrapper.getReadDataSourceCount());
		Assert.assertEquals(
			1, _dynamicDataSourceTargetSourceWrapper.getWriteDataSourceCount());
	}

	@Test
	public void testRead() throws Exception {
		AbstractTestTemplate testTemplate = new AbstractTestTemplate() {

			@Override
			public void doTest() throws Exception {
				try {
					GroupLocalServiceUtil.getGroup(0L);
				}
				catch (NoSuchGroupException nsge) {
					if (_log.isDebugEnabled()) {
						_log.debug(nsge);
					}
				}
			}

		};

		testTemplate.test();

		Assert.assertEquals(
			1, _dynamicDataSourceTargetSourceWrapper.getReadDataSourceCount());
		Assert.assertEquals(
			0, _dynamicDataSourceTargetSourceWrapper.getWriteDataSourceCount());
	}

	@Test
	public void testWrite() throws Exception {
		AbstractTestTemplate testTemplate = new AbstractTestTemplate() {

			@Override
			public void doTest() throws Exception {
				try {
					String value = ServiceTestUtil.randomString();

					ClassNameLocalServiceUtil.addClassName(value);
				}
				catch (PortalException pe) {
					if (_log.isDebugEnabled()) {
						_log.debug(pe);
					}
				}
			}

		};

		testTemplate.test();

		Assert.assertEquals(
			0, _dynamicDataSourceTargetSourceWrapper.getReadDataSourceCount());
		Assert.assertEquals(
			1, _dynamicDataSourceTargetSourceWrapper.getWriteDataSourceCount());
	}

	@SuppressWarnings("unchecked")
	private static <T> T _getFieldValue(String fieldName, Object target) {
		Field field = Whitebox.getField(target.getClass(), fieldName);

		T value = null;

		try {
			value = (T)field.get(target);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e);
			}
		}

		return value;
	}

	private static Log _log = LogFactoryUtil.getLog(
		DynamicDataSourceTransactionInterceptorTest.class);

	private static MockDynamicDataSourceTargetSource
		_dynamicDataSourceTargetSourceWrapper;
	private static LazyConnectionDataSourceProxy _liferayDataSource;

	private static TransactionInterceptor _transactionInterceptor;

	private static abstract class AbstractTestTemplate {

		public void test() throws Exception {

			PlatformTransactionManager platformTransactionManager =
				(PlatformTransactionManager)
					InfrastructureUtil.getTransactionManager();

			MockPlatformTransactionManager platformTransactionManagerWrapper =
				new MockPlatformTransactionManager(
					platformTransactionManager, false);

			_transactionInterceptor.setPlatformTransactionManager(
				platformTransactionManagerWrapper);

			DynamicDataSourceTransactionInterceptor
				dynamicDataSourceTransactionInterceptor =
					(DynamicDataSourceTransactionInterceptor)
						_transactionInterceptor;

			DynamicDataSourceTargetSource dynamicDataSourceTargetSource =
				_getFieldValue(
					"_dynamicDataSourceTargetSource",
					dynamicDataSourceTransactionInterceptor);

			DataSource targetDataSource =
				_liferayDataSource.getTargetDataSource();

			try {
				dynamicDataSourceTransactionInterceptor.
					setDynamicDataSourceTargetSource(
						_dynamicDataSourceTargetSourceWrapper);

				ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();

				proxyFactoryBean.setTargetSource(
					_dynamicDataSourceTargetSourceWrapper);

				_liferayDataSource.setTargetDataSource(
					(DataSource)proxyFactoryBean.getObject());

				_dynamicDataSourceTargetSourceWrapper.reset();

				doTest();
			}
			finally {
				dynamicDataSourceTransactionInterceptor.
					setDynamicDataSourceTargetSource(
						dynamicDataSourceTargetSource);

				_liferayDataSource.setTargetDataSource(targetDataSource);

				_transactionInterceptor.setPlatformTransactionManager(
					platformTransactionManager);
			}
		}

		protected abstract void doTest() throws Exception;

	}

	private static class MockDynamicDataSourceTargetSource
		extends DynamicDataSourceTargetSource {

		public MockDynamicDataSourceTargetSource(
			DynamicDataSourceTargetSource dynamicDataSourceTargetSource) {

			DataSource readDataSource = _getReadDataSource(
				dynamicDataSourceTargetSource);
			DataSource writeDataSource = _getWriteDataSource(
				dynamicDataSourceTargetSource);

			setReadDataSource(readDataSource);
			setWriteDataSource(writeDataSource);
		}

		public int getReadDataSourceCount() {
			return _readDataSourceCount;
		}

		public int getWriteDataSourceCount() {
			return _writeDataSourceCount;
		}

		@Override
		public Object getTarget() throws Exception {
			Object targetDataSource = super.getTarget();

			if (targetDataSource == _readDataSource) {
				_readDataSourceCount++;
			}

			if (targetDataSource == _writeDataSource) {
				_writeDataSourceCount++;
			}

			return targetDataSource;
		}

		@Override
		public void setReadDataSource(DataSource readDataSource) {
			super.setReadDataSource(readDataSource);

			_readDataSource = readDataSource;
		}

		@Override
		public void setWriteDataSource(DataSource writeDataSource) {
			super.setWriteDataSource(writeDataSource);

			_writeDataSource = writeDataSource;
		}

		public void reset() {
			_readDataSourceCount = 0;
			_writeDataSourceCount = 0;
		}

		private DataSource _getReadDataSource(Object target) {
			return _getFieldValue("_readDataSource", target);
		}

		private DataSource _getWriteDataSource(Object target) {
			return _getFieldValue("_writeDataSource", target);
		}

		private DataSource _readDataSource;
		private DataSource _writeDataSource;

		private int _readDataSourceCount;
		private int _writeDataSourceCount;
	}

}