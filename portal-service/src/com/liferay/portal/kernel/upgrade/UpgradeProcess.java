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

package com.liferay.portal.kernel.upgrade;

import com.liferay.portal.kernel.dao.db.BaseDBProcess;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.util.UpgradeTable;
import com.liferay.portal.kernel.upgrade.util.UpgradeTableFactoryUtil;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public abstract class UpgradeProcess
	extends BaseDBProcess implements UpgradeStep {

	public void clearIndexesCache() {
		_portalIndexesSQL = null;
	}

	public int getThreshold() {

		// This upgrade process will only run if the build number is larger than
		// the returned threshold value. Return 0 to always run this upgrade
		// process.

		return 0;
	}

	public void upgrade() throws UpgradeException {
		long start = System.currentTimeMillis();

		if (_log.isInfoEnabled()) {
			_log.info("Upgrading " + ClassUtil.getClassName(this));
		}

		try (Connection con = DataAccess.getUpgradeOptimizedConnection()) {
			connection = con;

			doUpgrade();
		}
		catch (Exception e) {
			throw new UpgradeException(e);
		}
		finally {
			connection = null;

			if (_log.isInfoEnabled()) {
				_log.info(
					"Completed upgrade process " +
						ClassUtil.getClassName(this) + " in " +
							(System.currentTimeMillis() - start) + "ms");
			}
		}
	}

	public void upgrade(Class<?> upgradeProcessClass) throws UpgradeException {
		UpgradeProcess upgradeProcess = null;

		try {
			upgradeProcess = (UpgradeProcess)upgradeProcessClass.newInstance();
		}
		catch (Exception e) {
			throw new UpgradeException(e);
		}

		upgradeProcess.upgrade();
	}

	@Override
	public void upgrade(DBProcessContext dbProcessContext)
		throws UpgradeException {

		upgrade();
	}

	public void upgrade(UpgradeProcess upgradeProcess) throws UpgradeException {
		upgradeProcess.upgrade();
	}

	protected void alterColumnType(
			Class<?> tableClass, String columnName, String columnType)
		throws Exception {

		Field tableNameField = tableClass.getField("TABLE_NAME");

		String tableName = (String)tableNameField.get(null);

		try {
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			ResultSet indexInfo = databaseMetaData.getIndexInfo(
				null, null, normalizeName(tableName, databaseMetaData), false,
				false);

			Map<String, Set<String>> map = new HashMap<>();

			while (indexInfo.next()) {
				String indexName = StringUtil.toUpperCase(
					indexInfo.getString("INDEX_NAME"));

				Set<String> columnNames = map.get(indexName);

				if (columnNames == null) {
					columnNames = new HashSet<>();

					map.put(indexName, columnNames);
				}

				columnNames.add(indexInfo.getString("COLUMN_NAME"));
			}

			for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
				String indexName = entry.getKey();
				Set<String> columnNames = entry.getValue();

				if (columnNames.contains(columnName)) {
					runSQL("drop index " + indexName + " on " + tableName);
				}
			}

			StringBundler sb = new StringBundler(6);

			sb.append("alter_column_type ");
			sb.append(tableName);
			sb.append(" ");
			sb.append(columnName);
			sb.append(" ");
			sb.append(columnType);

			runSQL(sb.toString());

			for (String indexSQL : _getIndexesSQL(tableClass, tableName)) {
				if (indexSQL.contains(columnName)) {
					runSQL(indexSQL);
				}
			}
		}
		catch (SQLException sqle) {
			Field tableColumnsField = tableClass.getField("TABLE_COLUMNS");

			Object[][] tableColumns = (Object[][])tableColumnsField.get(null);

			Field tableSQLCreateField = tableClass.getField("TABLE_SQL_CREATE");

			String tableSQLCreate = (String)tableSQLCreateField.get(null);

			Field tableSQLAddIndexesField = tableClass.getField(
				"TABLE_SQL_ADD_INDEXES");

			String[] tableSQLAddIndexes = (String[])tableSQLAddIndexesField.get(
				null);

			upgradeTable(
				tableName, tableColumns, tableSQLCreate, tableSQLAddIndexes);
		}
	}

	protected abstract void doUpgrade() throws Exception;

	protected long increment() {
		DB db = DBManagerUtil.getDB();

		return db.increment();
	}

	protected long increment(String name) {
		DB db = DBManagerUtil.getDB();

		return db.increment(name);
	}

	protected boolean isSupportsAlterColumnName() {
		DB db = DBManagerUtil.getDB();

		return db.isSupportsAlterColumnName();
	}

	protected boolean isSupportsAlterColumnType() {
		DB db = DBManagerUtil.getDB();

		return db.isSupportsAlterColumnType();
	}

	protected boolean isSupportsStringCaseSensitiveQuery() {
		DB db = DBManagerUtil.getDB();

		return db.isSupportsStringCaseSensitiveQuery();
	}

	protected boolean isSupportsUpdateWithInnerJoin() {
		DB db = DBManagerUtil.getDB();

		return db.isSupportsUpdateWithInnerJoin();
	}

	protected String normalizeName(
			String name, DatabaseMetaData databaseMetaData)
		throws SQLException {

		if (databaseMetaData.storesLowerCaseIdentifiers()) {
			return StringUtil.toLowerCase(name);
		}

		if (databaseMetaData.storesUpperCaseIdentifiers()) {
			return StringUtil.toUpperCase(name);
		}

		return name;
	}

	protected void upgradeTable(String tableName, Object[][] tableColumns)
		throws Exception {

		UpgradeTable upgradeTable = UpgradeTableFactoryUtil.getUpgradeTable(
			tableName, tableColumns);

		upgradeTable.updateTable();
	}

	protected void upgradeTable(
			String tableName, Object[][] tableColumns, String sqlCreate,
			String[] sqlAddIndexes)
		throws Exception {

		UpgradeTable upgradeTable = UpgradeTableFactoryUtil.getUpgradeTable(
			tableName, tableColumns);

		upgradeTable.setCreateSQL(sqlCreate);
		upgradeTable.setIndexesSQL(sqlAddIndexes);

		upgradeTable.updateTable();
	}

	private List<String> _getIndexesSQL(Class<?> tableClass, String tableName)
		throws Exception {

		ClassLoader classLoader = tableClass.getClassLoader();

		if (!PortalClassLoaderUtil.isPortalClassLoader(classLoader)) {
			List<String> indexes = new ArrayList<>();

			String onTableName = " on " + tableName;

			try (InputStream is = classLoader.getResourceAsStream(
					"META-INF/sql/indexes.sql");
				UnsyncBufferedReader unsyncBufferedReader =
					new UnsyncBufferedReader(new InputStreamReader(is))) {

				String line = null;

				while ((line = unsyncBufferedReader.readLine()) != null) {
					if (line.contains(onTableName)) {
						indexes.add(line);
					}
				}
			}

			return indexes;
		}

		if (_portalIndexesSQL != null) {
			return _portalIndexesSQL.get(tableName);
		}

		_portalIndexesSQL = new HashMap<>();

		try (InputStream is = classLoader.getResourceAsStream(
				"com/liferay/portal/tools/sql/dependencies/indexes.sql");
			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(new InputStreamReader(is))) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				int x = line.indexOf(" on ");

				if (x < 0) {
					continue;
				}

				int y = line.indexOf(" ", x);

				String currentTableName = line.substring(x + 4, y);

				List<String> indexes = _portalIndexesSQL.get(currentTableName);

				if (indexes == null) {
					indexes = new ArrayList<>();

					_portalIndexesSQL.put(currentTableName, indexes);
				}

				indexes.add(line);
			}
		}

		return _portalIndexesSQL.get(tableName);
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeProcess.class);

	private static Map<String, List<String>> _portalIndexesSQL;

}