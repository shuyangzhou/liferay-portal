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

package com.liferay.portal.upgrade.v7_0_3;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class UpgradeSybaseTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		DB db = DBManagerUtil.getDB();

		Assume.assumeTrue(DBType.SYBASE.equals(db.getDBType()));

		try (Connection connection = DataAccess.getConnection();
			PreparedStatement ps = connection.prepareStatement(
				"create table TestTable (testTableId decimal(20, 0) not null " +
					"primary key, testValue varchar(1000) null)")) {

			ps.executeUpdate();
		}
	}

	@After
	public void tearDown() throws Exception {
		try (Connection connection = DataAccess.getConnection();
			PreparedStatement ps = connection.prepareStatement(
				"drop table TestTable")) {

			ps.executeUpdate();
		}
	}

	@Test
	public void testUpgrade() throws Exception {
		_assertSize(1000);

		_upgradeSybase.upgrade();

		_assertSize(4000);
	}

	private void _assertSize(int size) throws Exception {
		try (Connection connection = DataAccess.getConnection()) {
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			DBInspector dbInspector = new DBInspector(connection);

			String catalog = dbInspector.getCatalog();
			String schema = dbInspector.getSchema();

			String tableName = dbInspector.normalizeName("TestTable");
			String columnName = dbInspector.normalizeName("testValue");

			try (ResultSet columnRS = databaseMetaData.getColumns(
					catalog, schema, tableName, columnName)) {

				Assert.assertTrue(columnRS.next());

				Assert.assertEquals(
					columnName, columnRS.getString("COLUMN_NAME"));

				Assert.assertEquals(
					dbInspector.normalizeName("varchar"),
					columnRS.getString("TYPE_NAME"));

				Assert.assertEquals(size, columnRS.getInt("COLUMN_SIZE"));

				Assert.assertFalse(columnRS.next());
			}
		}
	}

	private final UpgradeProcess _upgradeSybase = new UpgradeSybase();

}