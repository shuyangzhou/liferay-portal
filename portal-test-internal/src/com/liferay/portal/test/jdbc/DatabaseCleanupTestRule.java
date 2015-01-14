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
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
			Connection connection, java.sql.Statement statement)
		throws SQLException {

		String sql = _statementSQLs.get(statement);

		Statement parserStatement;

		try {
			parserStatement = _jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			return;
		}

		String tableName = getTableName(parserStatement);

		if (tableName != null) {
			try (java.sql.Statement selectResultStatement =
					connection.createStatement();
				ResultSet resultSet = selectResultStatement.executeQuery(
					_SELECT.concat(tableName))) {

				while (resultSet.next()) {
					PKObject pkObject = new PKObject(tableName, resultSet);

					if (_tempPKObjects.remove(pkObject) == null) {
						_insertedPKObjects.put(
							pkObject, new SQLExecutionContext(sql));
					}
				}
			}

			_deletedPKObjects.putAll(_tempPKObjects);
		}
	}

	public static boolean processBeforeInvoke(
			Connection connection, java.sql.Statement statement)
		throws SQLException {

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

		String tableName = getTableName(parserStatement);

		if ((tableName == null) || tableName.equals("Counter") ||
			tableName.equals("SystemEvent")) {

			return false;
		}

		try (java.sql.Statement selectStatement =
				connection.createStatement();
			ResultSet resultSet = selectStatement.executeQuery(
				_SELECT.concat(tableName))) {

			while (resultSet.next()) {
				_tempPKObjects.put(
					new PKObject(tableName, resultSet),
					new SQLExecutionContext(sql));
			}
		}

		return true;
	}

	public static void recordStatementSQL(
		java.sql.Statement statement, String sql) {

		_statementSQLs.put(statement, sql);
	}

	protected static String getTableName(Statement parserStatement) {
		Table table = null;

		if (parserStatement instanceof Delete) {
			Delete delete = (Delete)parserStatement;

			table = delete.getTable();
		}

		if (parserStatement instanceof Insert) {
			Insert insert = (Insert)parserStatement;

			table = insert.getTable();
		}

		if (table != null) {
			return table.getName();
		}

		return null;
	}

	@Override
	protected void afterClass(Description description, Object c)
		throws IOException {

		_recording = false;

		Set<PKObject> keySet = _insertedPKObjects.keySet();

		Iterator<PKObject> iterator = keySet.iterator();

		while (iterator.hasNext()) {
			PKObject pkObject = iterator.next();

			if (_deletedPKObjects.remove(pkObject) != null) {
				iterator.remove();
			}
		}

		_outputResults();

		System.out.println(
			"Number of deletedObjects = " + _deletedPKObjects.size());

		System.out.println(
			"Number of insertedObjects = " + _insertedPKObjects.size());

		System.out.println(
			"Check files insertedObjects and deletedObjects for " +
			"more information");
	}

	@Override
	protected Object beforeClass(Description description) throws SQLException {
		try (Connection connection =
				DataAccess.getUpgradeOptimizedConnection()) {

			DatabaseMetaData databaseMetaData = connection.getMetaData();

			try (ResultSet tableResultSet = databaseMetaData.getTables(
					null, null, null, null)) {

				while (tableResultSet.next()) {
					String tableName = tableResultSet.getString("TABLE_NAME");

					try (ResultSet primaryKeysResultSet =
						databaseMetaData.getPrimaryKeys(
							null, null, tableName)) {

						List<String> pkColumnNames = new ArrayList<>();

						while (primaryKeysResultSet.next()) {
							pkColumnNames.add(
								primaryKeysResultSet.getString("COLUMN_NAME"));
						}

						_pkColumnNamesMap.put(tableName, pkColumnNames);
					}
				}
			}
		}

		_recording = true;

		return null;
	}

	protected static class PKObject {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof PKObject)) {
				return false;
			}

			PKObject pkObject = (PKObject)obj;

			if (Validator.equals(tableName, pkObject.tableName) &&
				Validator.equals(pkColumnValues, pkObject.pkColumnValues)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			int hash = HashUtil.hash(0, tableName);

			return HashUtil.hash(hash, pkColumnValues);
		}

		@Override
		public String toString() {
			StringBundler sb = new StringBundler(pkColumnNames.size() * 4 + 3);

			sb.append("{tableName=");
			sb.append(tableName);
			sb.append(", pkValues=[");

			for (int i = 0; i < pkColumnNames.size(); i++) {
				sb.append(pkColumnNames.get(i));
				sb.append(StringPool.EQUAL);
				sb.append(pkColumnValues.get(i));
				sb.append(StringPool.COMMA);
			}

			sb.setIndex(sb.index() - 1);
			sb.append("]}");

			return sb.toString();
		}

		protected PKObject(String tableName, ResultSet resultSet)
			throws SQLException {

			this.tableName = tableName;

			pkColumnNames = _pkColumnNamesMap.get(tableName);

			pkColumnValues = new ArrayList<>(pkColumnNames.size());

			for (String pkName : pkColumnNames) {
				pkColumnValues.add(resultSet.getObject(pkName));
			}
		}

		protected final List<String> pkColumnNames;
		protected final List<Object> pkColumnValues;
		protected final String tableName;

	}

	protected static class SQLExecutionContext {

		public void writeTo(UnsyncPrintWriter unsyncPrintWriter) {
			unsyncPrintWriter.print("Corresponding SQL line: ");

			unsyncPrintWriter.println(sql);

			unsyncPrintWriter.print("Corresponding stack trace: ");

			exception.printStackTrace(unsyncPrintWriter);
		}

		protected SQLExecutionContext(String sql) {
			this.sql = sql;

			exception = new Exception();
		}

		protected final Exception exception;
		protected final String sql;

	}

	private static void _outputResults() throws IOException {
		try (Writer writer = new FileWriter("insertedObjects");
			UnsyncPrintWriter unsyncPrintWriter =
				new UnsyncPrintWriter(writer)) {

			int printedCount = 1;

			for (PKObject pkObject : _insertedPKObjects.keySet()) {
				unsyncPrintWriter.println(
					"Inserted and failed to delete # " + printedCount++ + ": " +
						pkObject);

				SQLExecutionContext sqlExecutionContext =
					_insertedPKObjects.get(pkObject);

				sqlExecutionContext.writeTo(unsyncPrintWriter);

				unsyncPrintWriter.println();
			}
		}

		try (Writer writer = new FileWriter("deletedObjects");
			UnsyncPrintWriter unsyncPrintWriter =
				new UnsyncPrintWriter(writer)) {

			int printedCount = 1;

			for (PKObject pkObject : _deletedPKObjects.keySet()) {
				unsyncPrintWriter.println(
					"Deleted pre-existing object # " + printedCount++ + ": " +
						pkObject);

				SQLExecutionContext sqlExecutionContext = _deletedPKObjects.get(
					pkObject);

				sqlExecutionContext.writeTo(unsyncPrintWriter);

				unsyncPrintWriter.println();
			}
		}
	}

	private DatabaseCleanupTestRule() {
	}

	private static final String _SELECT = "SELECT * FROM ";

	private static final ConcurrentMap<PKObject, SQLExecutionContext>
		_deletedPKObjects = new ConcurrentHashMap<>();
	private static final ConcurrentMap<PKObject, SQLExecutionContext>
		_insertedPKObjects = new ConcurrentHashMap<>();
	private static final JSqlParser _jSqlParser = new CCJSqlParserManager();
	private static final ConcurrentMap<String, List<String>> _pkColumnNamesMap =
		new ConcurrentHashMap<>();
	private static volatile boolean _recording = false;
	private static final ConcurrentMap<java.sql.Statement, String>
		_statementSQLs = new ConcurrentHashMap<>();
	private static final ConcurrentMap<PKObject, SQLExecutionContext>
		_tempPKObjects = new ConcurrentHashMap<>();

}