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

package com.liferay.portal.dao.jdbc.spring;

import com.liferay.portal.kernel.dao.jdbc.MappingSqlQuery;
import com.liferay.portal.kernel.dao.jdbc.MappingSqlQueryFactoryUtil;
import com.liferay.portal.kernel.dao.jdbc.RowMapper;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.TransactionalTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.Connection;
import java.sql.Types;

import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Matthew Tambara
 */
public class JdbcTemplateTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), TransactionalTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() {
		_dataSource = InfrastructureUtil.getDataSource();
	}

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testClosedConnection() throws Throwable {
		Connection connection = DataSourceUtils.getConnection(_dataSource);

		ConnectionHolder connectionHolder =
			(ConnectionHolder)TransactionSynchronizationManager.getResource(
				_dataSource);

		DataSourceUtils.releaseConnection(connection, _dataSource);

		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		TransactionInvokerUtil.invoke(
			builder.build(),
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					return null;
				}

			});

		Assert.assertNull(connectionHolder.getConnectionHandle());
	}

	@Test
	public void testMappingSqlQuerySpringJdbcTemplate() throws Throwable {
		MappingSqlQuery<Integer> mappingSqlQuery =
			MappingSqlQueryFactoryUtil.getMappingSqlQuery(
				_dataSource, _SELECT_SQL, new int[] {Types.INTEGER},
				RowMapper.COUNT);

		mappingSqlQuery.execute(_group.getGroupId());

		MappingSqlQueryImpl<Integer> mappingSqlQueryImpl =
			(MappingSqlQueryImpl)mappingSqlQuery;

		mappingSqlQueryImpl.setJdbcTemplate(
			new org.springframework.jdbc.core.JdbcTemplate());

		mappingSqlQueryImpl.setDataSource(_dataSource);

		mappingSqlQueryImpl.setSql(_SELECT_SQL);

		mappingSqlQueryImpl.execute(_group.getGroupId());

		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		TransactionInvokerUtil.invoke(
			builder.build(),
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					return null;
				}

			});

		ConnectionHolder connectionHolder =
			(ConnectionHolder)TransactionSynchronizationManager.getResource(
				_dataSource);

		Assert.assertNotNull(connectionHolder);
		Assert.assertNull(connectionHolder.getConnectionHandle());
	}

	@Test
	public void testMappingSqlQueryWithLiferayJdbcTemplate() throws Throwable {
		MappingSqlQuery<Integer> mappingSqlQuery =
			MappingSqlQueryFactoryUtil.getMappingSqlQuery(
				_dataSource, _SELECT_SQL, new int[] {Types.INTEGER},
				RowMapper.COUNT);

		mappingSqlQuery.execute(_group.getGroupId());

		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		TransactionInvokerUtil.invoke(
			builder.build(),
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					return null;
				}

			});

		ConnectionHolder connectionHolder =
			(ConnectionHolder)TransactionSynchronizationManager.getResource(
				_dataSource);

		Assert.assertNull(connectionHolder);
	}

	@Test
	public void testSqlUpdateWithLiferayJdbcTemplate() throws Throwable {
		SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
			_dataSource, _UPDATE_SQL, new int[] {Types.INTEGER, Types.INTEGER});

		sqlUpdate.update(_group.getGroupId(), _group.getGroupId());

		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		TransactionInvokerUtil.invoke(
			builder.build(),
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					return null;
				}

			});

		ConnectionHolder connectionHolder =
			(ConnectionHolder)TransactionSynchronizationManager.getResource(
				_dataSource);

		Assert.assertNull(connectionHolder);
	}

	@Test
	public void testSqlUpdateWithSpringJdbcTemplate() throws Throwable {
		SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
			_dataSource, _UPDATE_SQL, new int[] {Types.INTEGER, Types.INTEGER});

		SqlUpdateImpl sqlUpdateImpl = (SqlUpdateImpl)sqlUpdate;

		sqlUpdateImpl.setJdbcTemplate(
			new org.springframework.jdbc.core.JdbcTemplate());

		sqlUpdateImpl.setDataSource(_dataSource);

		sqlUpdateImpl.setSql(_UPDATE_SQL);

		sqlUpdate.update(_group.getGroupId(), _group.getGroupId());

		TransactionConfig.Builder builder = new TransactionConfig.Builder();

		builder.setPropagation(Propagation.REQUIRED);

		TransactionInvokerUtil.invoke(
			builder.build(),
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					return null;
				}

			});

		ConnectionHolder connectionHolder =
			(ConnectionHolder)TransactionSynchronizationManager.getResource(
				_dataSource);

		Assert.assertNotNull(connectionHolder);
		Assert.assertNull(connectionHolder.getConnectionHandle());
	}

	private static final String _SELECT_SQL =
		"SELECT * from Group_ WHERE groupId=?";

	private static final String _UPDATE_SQL =
		"UPDATE Group_ SET groupId=? WHERE groupId=?";

	private static DataSource _dataSource;

	@DeleteAfterTestRun
	private Group _group;

}