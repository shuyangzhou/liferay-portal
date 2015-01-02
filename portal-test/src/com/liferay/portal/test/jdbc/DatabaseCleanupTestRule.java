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
import com.liferay.portal.kernel.util.StringUtil;

import java.io.FileWriter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jodd.util.StringPool;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import org.junit.runner.Description;

/**
 * @author Will Newbury
 */
public class DatabaseCleanupTestRule extends BaseTestRule<Object, Object> {

	public static final DatabaseCleanupTestRule INSTANCE =
		new DatabaseCleanupTestRule();

	public static boolean _initialized = false;
	public static boolean _recording = false;

	public static int processSQL(Connection connection, String sql)
		throws Exception {

		if (!_initialized) {
			dumpDatabaseMetadata();

			_initialized = true;
		}

		_processInsertsAndDeletes(connection, sql);

		return _processAfterInvoke;
	}

	@Override
	protected void afterClass(Description description, Object c) {
		_recording = false;

		diffTables();

		System.out.println("Number of deletedObjects = " +
			_deletedObjects.size());

		System.out.println("Number of insertedObjects = " +
			_insertedObjects.size());

		System.out.println("Check files insertedObjects and " +
			"deletedObjects for more information");

		outputResults();
	}

	@Override
	protected Object beforeClass(Description description) throws Throwable {
		_recording = true;

		return super.beforeClass(description);
	}

	private static void _processInsertsAndDeletes(
		Connection connection, String sql) {

		if (!_recording) {
			return;
		}

		Statement statement = null;

		try {
			statement = _jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			return;
		}

		if (statement instanceof Delete) {
			_processAfterInvoke = _PROCESS_DELETE;

			recordDelete(connection, sql, statement);
		}

		if (statement instanceof Insert) {
			_processAfterInvoke = _PROCESS_INSERT;
		}

		if (statement instanceof Update) {
			//Potentially deal with these
		}
	}

	private static void diffTables() {
		for (String insertedObject : _insertedObjects.keySet()) {
			if (_deletedObjects.containsKey(insertedObject)) {
				_deletedObjects.remove(insertedObject);
				_insertedObjects.remove(insertedObject);
			}
		}
	}

	private static void dumpDatabaseMetadata() {
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

				_primaryKeyColumns.put(StringUtil.toLowerCase(tableName),
					primaryKeys);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			DataAccess.cleanUp(connection, null, tableResultSet);
		}
	}

	public static void processAfterInvoke(Connection connection, String sql) {
		if (_processAfterInvoke == _PROCESS_DELETE) {
			processDeleteAfterInvoke(connection, sql);
		}

		if (_processAfterInvoke == _PROCESS_INSERT) {
			processInsertAfterInvoke(connection, sql);
		}
	}

	private static void processDeleteAfterInvoke(Connection connection,
			String sql) {

		Delete delete = null;

		try {
			delete = (Delete)_jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		String tableName = delete.getTable().getName();

		StringBundler sb = new StringBundler(4);

		sb.append(_SELECT);
		sb.append(StringPool.ASTERISK);
		sb.append(_FROM);
		sb.append(tableName);

		try {
			int oldIsolationLevel = connection.getTransactionIsolation();

			connection.setTransactionIsolation(
				Connection.TRANSACTION_REPEATABLE_READ);

//			connection.setAutoCommit(false);

			java.sql.Statement selectResultStatement =
				connection.createStatement(
					ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);

			ResultSet deleteResultSet = selectResultStatement.executeQuery(
				sb.toString());

			ResultSetMetaData deleteResultSetMetadata =
				deleteResultSet.getMetaData();

			int columnCount = deleteResultSetMetadata.getColumnCount();

			while (deleteResultSet.next()) {
				StringBundler pk = new StringBundler(2*columnCount + 1);
				pk.append(tableName);
				pk.append(StringPool.COMMA);

				for (int i = 2; i <= columnCount; i++) {
					pk.append(deleteResultSet.getObject(i));
					pk.append(StringPool.COMMA);
				}

				pk.setIndex(pk.index() - 1);

				if (_tempObjects.containsKey(pk.toString())) {
					_tempObjects.remove(pk.toString());
				}
			}

//			connection.commit();

			connection.setTransactionIsolation(oldIsolationLevel);

			for (String objectKey : _tempObjects.keySet()) {
				_deletedObjects.put(objectKey, _tempObjects.get(objectKey));
			}

			_tempObjects.clear();

			_processAfterInvoke = _PROCESSING_NOT_REQUIRED;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void processInsertAfterInvoke(Connection connection,
			String sql) {

		Insert insert = null;

		try {
			insert = (Insert)_jSqlParser.parse(new UnsyncStringReader(sql));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

		String tableName = insert.getTable().getName();

		StringBundler sb = new StringBundler(4);

		sb.append(_SELECT);
		sb.append(StringPool.ASTERISK);
		sb.append(_FROM);
		sb.append(tableName);

		try {
			int oldIsolationLevel = connection.getTransactionIsolation();

			connection.setTransactionIsolation(
				Connection.TRANSACTION_REPEATABLE_READ);

//			connection.setAutoCommit(false);

			java.sql.Statement selectResultStatement =
				connection.createStatement(
					ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);

			ResultSet deleteResultSet = selectResultStatement.executeQuery(
				sb.toString());

			ResultSetMetaData deleteResultSetMetadata =
				deleteResultSet.getMetaData();

			int columnCount = deleteResultSetMetadata.getColumnCount();

			StringBundler insertedObject = null;

			while (deleteResultSet.next()) {
				insertedObject = new StringBundler(2*columnCount + 1);
				insertedObject.append(tableName);
				insertedObject.append(StringPool.COMMA);

				for (int i = 2; i <= columnCount; i++) {
					insertedObject.append(deleteResultSet.getObject(i));
					insertedObject.append(StringPool.COMMA);
				}

				insertedObject.setIndex(insertedObject.index() - 1);
			}

			if (insertedObject != null) {
				List<Object> context = new ArrayList<>();
				context.add(sql);
				context.add(Thread.currentThread().getStackTrace());

				_insertedObjects.put(insertedObject.toString(), context);
			}

//			connection.commit();

			connection.setTransactionIsolation(oldIsolationLevel);

			_processAfterInvoke = _PROCESSING_NOT_REQUIRED;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void recordDelete(Connection connection, String sql,
			Statement statement) {

		Delete delete = (Delete)statement;

		String tableName = delete.getTable().getName();

		StringBundler sb = new StringBundler(4);

		sb.append(_SELECT);
		sb.append(StringPool.ASTERISK);
		sb.append(_FROM);
		sb.append(tableName);

		try {
			int oldIsolationLevel = connection.getTransactionIsolation();

			connection.setTransactionIsolation(
				Connection.TRANSACTION_REPEATABLE_READ);

//			connection.setAutoCommit(false);

			java.sql.Statement selectResultStatement =
				connection.createStatement(
					ResultSet.FETCH_FORWARD, ResultSet.CONCUR_READ_ONLY);

			ResultSet deleteResultSet = selectResultStatement.executeQuery(
				sb.toString());

			ResultSetMetaData deleteResultSetMetadata =
				deleteResultSet.getMetaData();

			int columnCount = deleteResultSetMetadata.getColumnCount();

			while (deleteResultSet.next()) {
				StringBundler pk = new StringBundler(2*columnCount + 1);
				pk.append(tableName);
				pk.append(StringPool.COMMA);

				for (int i = 2; i <= columnCount; i++) {
					pk.append(deleteResultSet.getObject(i));
					pk.append(StringPool.COMMA);
				}

				pk.setIndex(pk.index() - 1);

				List<Object> context = new ArrayList<>();
				context.add(sql);
				context.add(Thread.currentThread().getStackTrace());

				_tempObjects.put(pk.toString(), context);
			}

//			connection.commit();

			connection.setTransactionIsolation(oldIsolationLevel);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private DatabaseCleanupTestRule() {
	}

	private void outputResults() {
		try {
			UnsyncPrintWriter writer = new UnsyncPrintWriter(
				new FileWriter("insertedObjects"));

			for (String insertedObject : _insertedObjects.keySet()) {
				writer.println(
					"Inserted and failed to delete: " + insertedObject);

				List<Object> info = _insertedObjects.get(insertedObject);

				writer.println("Corresponding sql statement: " + info.get(0));

				writer.print("Corresponding stack trace: ");

				for (StackTraceElement e : (StackTraceElement[])info.get(1)) {
					writer.println(e.toString());
				}

				writer.println();
			}

			writer.flush();
			writer.close();

			writer = new UnsyncPrintWriter(new FileWriter("deletedObjects"));

			for (String deletedObject : _deletedObjects.keySet()) {
				writer.println("Deleted pre-existing object: " + deletedObject);

				List<Object> info = _insertedObjects.get(deletedObject);

				writer.println("Corresponding sql statement: " + info.get(0));

				writer.print("Corresponding stack trace: ");

				for (StackTraceElement e : (StackTraceElement[])info.get(1)) {
					writer.println(e.toString());
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

	private static final String _FROM = " FROM ";

	private static final int _PROCESS_DELETE = 1;

	private static final int _PROCESS_INSERT = 2;

	private static final int _PROCESSING_NOT_REQUIRED = 0;

	private static final String _SELECT = "SELECT ";

	private static final ConcurrentMap<String, List<Object>> _deletedObjects =
		new ConcurrentHashMap<>();
	private static final ConcurrentMap<String, List<Object>> _insertedObjects =
		new ConcurrentHashMap<>();
	private static final JSqlParser _jSqlParser = new CCJSqlParserManager();
	private static final ConcurrentMap<String, List<String>>
		_primaryKeyColumns = new ConcurrentHashMap<>();
	private static int _processAfterInvoke = _PROCESSING_NOT_REQUIRED;
	private static final ConcurrentMap<String, List<Object>> _tempObjects =
		new ConcurrentHashMap<>();

}