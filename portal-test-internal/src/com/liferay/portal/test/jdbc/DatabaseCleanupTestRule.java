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

package com.liferay.portal.test.jdbc;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.io.unsync.UnsyncPrintWriter;
import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;
import com.liferay.portal.kernel.test.BaseTestRule;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.FileWriter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;

import org.junit.runner.Description;

/**
 * @author Will Newbury
 */
public class DatabaseCleanupTestRule extends BaseTestRule<Object, Object> {

	public static final DatabaseCleanupTestRule INSTANCE =
		new DatabaseCleanupTestRule();

	public static void processAfterInvoke(
		Connection connection, java.sql.Statement statement) {

		String sql = _statementSQLs.get(statement);

		Statement parserStatement;

		try {
			parserStatement = _jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			return;
		}

		if (parserStatement instanceof Delete) {
			Delete delete = (Delete)parserStatement;

			Table table = delete.getTable();

			String tableName = table.getName();

			_recordAfterExecution(connection, tableName, sql);
		}

		if (parserStatement instanceof Insert) {
			Insert insert = (Insert)parserStatement;

			Table table = insert.getTable();

			String tableName = table.getName();

			_recordAfterExecution(connection, tableName, sql);
		}
	}

	public static boolean processBeforeInvoke(
			Connection connection, java.sql.Statement statement)
		throws Exception {

		if (!_recording) {
			return false;
		}

		String sql = _statementSQLs.get(statement);

		Statement parserStatement;

		try {
			parserStatement = _jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			return false;
		}

		if (parserStatement instanceof Delete) {
			Delete delete = (Delete)parserStatement;

			Table table = delete.getTable();

			String tableName = table.getName();

			_recordBeforeExecution(connection, tableName, sql);

			return true;
		}

		if (parserStatement instanceof Insert) {
			Insert insert = (Insert)parserStatement;

			Table table = insert.getTable();

			String tableName = table.getName();

			_recordBeforeExecution(connection, tableName, sql);

			return true;
		}

		return false;
	}

	public static void recordStatementSQL(
		java.sql.Statement statement, String sql) {

		_statementSQLs.put(statement, sql);
	}

	@Override
	protected void afterClass(Description description, Object c) {
		_recording = false;

		_diffTables();

		System.out.println(
			"Number of deletedObjects = " + _deletedObjects.size());

		System.out.println(
			"Number of insertedObjects = " + _insertedObjects.size());

		System.out.println(
			"Check files insertedObjects and deletedObjects for " +
			"more information");

		_outputResults();
	}

	@Override
	protected Object beforeClass(Description description) throws Throwable {
		_dumpDatabaseMetadata();

		_recording = true;

		return super.beforeClass(description);
	}

	private static void _diffTables() {
		for (String insertedObject : _insertedObjects.keySet()) {
			if (_deletedObjects.remove(insertedObject) != null) {
				_insertedObjects.remove(insertedObject);
			}
		}
	}

	private static void _dumpDatabaseMetadata() {
		Connection connection = null;
		ResultSet tableResultSet = null;

		try {
			connection = DataAccess.getUpgradeOptimizedConnection();

			DatabaseMetaData databaseMetaData = connection.getMetaData();

			tableResultSet = databaseMetaData.getTables(null, null, null, null);

			while (tableResultSet.next()) {
				String tableName = tableResultSet.getString("TABLE_NAME");

				ResultSet primaryKeysResultSet =
					databaseMetaData.getPrimaryKeys(null, null, tableName);

				List<String> primaryKeys = new ArrayList<>();

				try {
					while (primaryKeysResultSet.next()) {
						primaryKeys.add(
							primaryKeysResultSet.getString("COLUMN_NAME"));
					}
				}
				finally {
					DataAccess.cleanUp(primaryKeysResultSet);
				}

				_primaryKeyColumns.put(tableName, primaryKeys);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			DataAccess.cleanUp(connection, null, tableResultSet);
		}
	}

	private static void _outputResults() {
		try {
			UnsyncPrintWriter writer = new UnsyncPrintWriter(
				new FileWriter("insertedObjects"));

			for (String insertedObject : _insertedObjects.keySet()) {
				writer.println(
					"Inserted and failed to delete: " + insertedObject);

				List<Object> info = _insertedObjects.get(insertedObject);

				writer.println("Corresponding sql statement: " + info.get(0));

				writer.print("Corresponding stack trace: ");

				for (StackTraceElement ste : (StackTraceElement[])info.get(1)) {
					writer.println(ste.toString());
				}

				writer.println();
			}

			writer.flush();
			writer.close();

			writer = new UnsyncPrintWriter(new FileWriter("deletedObjects"));

			for (String deletedObject : _deletedObjects.keySet()) {
				writer.println("Deleted pre-existing object: " + deletedObject);

				List<Object> info = _deletedObjects.get(deletedObject);

				writer.println("Corresponding sql statement: " + info.get(0));

				writer.print("Corresponding stack trace: ");

				for (StackTraceElement ste : (StackTraceElement[])info.get(1)) {
					writer.println(ste.toString());
				}

				writer.println();
			}

			writer.flush();
			writer.close();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void _recordAfterExecution(
		Connection connection, String tableName, String sql) {

		StringBundler sb = new StringBundler(4);
		sb.append(_SELECT);
		sb.append(StringPool.STAR);
		sb.append(_FROM);
		sb.append(tableName);

		try {
			java.sql.Statement selectResultStatement =
				connection.createStatement(
					ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);

			ResultSet resultSet = selectResultStatement.executeQuery(
				sb.toString());

			List<String> primaryKeyColumns = _primaryKeyColumns.get(tableName);

			try {
				while (resultSet.next()) {
					StringBundler databaseRow = new StringBundler(
						4*primaryKeyColumns.size() + 1);
					databaseRow.append(tableName);
					databaseRow.append(StringPool.COMMA);

					for (String primaryKeyColumn : primaryKeyColumns) {
						databaseRow.append(primaryKeyColumn);
						databaseRow.append(StringPool.EQUAL);
						databaseRow.append(
							resultSet.getObject(primaryKeyColumn));
						databaseRow.append(StringPool.COMMA);
					}

					databaseRow.setIndex(databaseRow.index() - 1);

					List<Object> context = new ArrayList<>();
					context.add(sql);
					context.add(Thread.currentThread().getStackTrace());

					String databaseRowString = databaseRow.toString();

					if (_tempObjects.remove(databaseRowString) == null) {
						_insertedObjects.put(databaseRowString, context);
					}
				}
			}
			finally {
				DataAccess.cleanUp(resultSet);
			}

			for (String databaseRow : _tempObjects.keySet()) {
				_deletedObjects.put(databaseRow, _tempObjects.get(databaseRow));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void _recordBeforeExecution(
		Connection connection, String tableName, String sql) {

		StringBundler sb = new StringBundler(4);

		sb.append(_SELECT);
		sb.append(StringPool.STAR);
		sb.append(_FROM);
		sb.append(tableName);

		try {
			java.sql.Statement selectResultStatement =
				connection.createStatement(
					ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);

			ResultSet resultSet = selectResultStatement.executeQuery(
				sb.toString());

			List<String> primaryKeyColumns = _primaryKeyColumns.get(tableName);

			try {
				while (resultSet.next()) {
					StringBundler databaseRow = new StringBundler(
						4*primaryKeyColumns.size() + 1);
					databaseRow.append(tableName);
					databaseRow.append(StringPool.COMMA);

					for (String primaryKeyColumn : primaryKeyColumns) {
						databaseRow.append(primaryKeyColumn);
						databaseRow.append(StringPool.EQUAL);
						databaseRow.append(
							resultSet.getObject(primaryKeyColumn));
						databaseRow.append(StringPool.COMMA);
					}

					databaseRow.setIndex(databaseRow.index() - 1);

					List<Object> context = new ArrayList<>();
					context.add(sql);
					context.add(Thread.currentThread().getStackTrace());

					_tempObjects.put(databaseRow.toString(), context);
				}
			}
			finally {
				DataAccess.cleanUp(resultSet);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private DatabaseCleanupTestRule() {
	}

	private static final String _FROM = " FROM ";

	private static final String _SELECT = "SELECT ";

	private static final ConcurrentMap<String, List<Object>> _deletedObjects =
		new ConcurrentHashMap<String, List<Object>>();
	private static final ConcurrentMap<String, List<Object>> _insertedObjects =
		new ConcurrentHashMap<String, List<Object>>();
	private static final JSqlParser _jSqlParser = new CCJSqlParserManager();
	private static final ConcurrentMap<String, List<String>>
		_primaryKeyColumns = new ConcurrentHashMap<String, List<String>>();
	private static volatile boolean _recording = false;
	private static final ConcurrentMap<java.sql.Statement, String>
		_statementSQLs = new ConcurrentHashMap<java.sql.Statement, String>();
	private static final ConcurrentMap<String, List<Object>> _tempObjects =
		new ConcurrentHashMap<String, List<Object>>();

}