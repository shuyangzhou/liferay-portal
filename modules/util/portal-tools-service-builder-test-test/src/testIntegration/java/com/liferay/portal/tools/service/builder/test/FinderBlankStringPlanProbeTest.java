/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.tools.service.builder.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.security.xml.SecureXMLFactoryProviderUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.StringReader;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class FinderBlankStringPlanProbeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_db = DBManagerUtil.getDB();

		_dbType = _db.getDBType();

		try (Connection connection = DataAccess.getConnection()) {
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			_header = StringBundler.concat(
				"[", _dbType, " | ", databaseMetaData.getDatabaseProductName(),
				" ", databaseMetaData.getDatabaseProductVersion(), " | driver ",
				databaseMetaData.getDriverName(), " ",
				databaseMetaData.getDriverVersion(), " | rows ", _ROWS, "]");

			_appendSetUpLog(
				"environment", _queryTexts(connection, _getEnvironmentSQLs()));

			_dropTables(connection);

			long startTime = System.currentTimeMillis();

			_db.runSQL(connection, _getCreateTableSQL(_TABLE_NAME_M));

			_load(connection);

			_appendSetUpLog(
				"load " + _TABLE_NAME_M + " ms",
				System.currentTimeMillis() - startTime);

			startTime = System.currentTimeMillis();

			_createIndexes(connection, _TABLE_NAME_M, "M", _UNIQUE_COLUMNS_M);

			_appendSetUpLog(
				"index " + _TABLE_NAME_M + " ms",
				System.currentTimeMillis() - startTime);

			startTime = System.currentTimeMillis();

			_db.runSQL(connection, _getCreateTableSQL(_TABLE_NAME_B));

			_execute(
				connection,
				StringBundler.concat(
					"INSERT INTO ", _TABLE_NAME_B, " SELECT * FROM ",
					_TABLE_NAME_M));

			_createIndexes(connection, _TABLE_NAME_B, "B", _UNIQUE_COLUMNS_B);

			_appendSetUpLog(
				"copy and index " + _TABLE_NAME_B + " ms",
				System.currentTimeMillis() - startTime);

			if (_dbType == DBType.DB2) {
				_db2Schema = StringUtil.trim(
					_queryValue(connection, "VALUES CURRENT SCHEMA"));

				_setUpDB2ExplainTables(connection);
			}

			startTime = System.currentTimeMillis();

			_gatherStatistics(connection, _TABLE_NAME_M);
			_gatherStatistics(connection, _TABLE_NAME_B);

			_appendSetUpLog(
				"statistics ms", System.currentTimeMillis() - startTime);

			_appendSetUpLog(
				"value shape",
				_queryTexts(
					connection,
					new String[] {
						"SELECT COUNT(*), SUM(CASE WHEN attributeName IS NULL " +
							"THEN 1 ELSE 0 END), SUM(CASE WHEN attributeName = " +
								"'' THEN 1 ELSE 0 END), SUM(CASE WHEN languageId " +
									"IS NULL THEN 1 ELSE 0 END), SUM(CASE WHEN " +
										"languageId = '' THEN 1 ELSE 0 END) FROM " +
											_TABLE_NAME_M
					}));

			if (_dbType == DBType.POSTGRESQL) {
				_appendSetUpLog(
					"relation sizes",
					_queryText(
						connection,
						"SELECT relname, relpages, pg_relation_size(oid) FROM " +
							"pg_class WHERE relname LIKE 'probe_ddmfa_%' OR " +
								"relname LIKE 'ix_prb%' ORDER BY relname"));
			}
		}
		catch (Throwable throwable) {
			_setUpThrowable = throwable;
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		try (Connection connection = DataAccess.getConnection()) {
			_dropTables(connection);

			if (_db2ExplainTablesCreated) {
				_call(
					connection,
					"CALL SYSPROC.SYSINSTALLOBJECTS('EXPLAIN', 'D', CAST(NULL " +
						"AS VARCHAR(128)), CAST(NULL AS VARCHAR(128)))");
			}
		}
	}

	@Test
	public void testEnvironment() {
		_fail("ENVIRONMENT", "");
	}

	@Test
	public void testPlansMasterIndex() throws Exception {
		if (_setUpThrowable == null) {
			_fail("PLANS " + _TABLE_NAME_M, _explainAll(_TABLE_NAME_M));
		}
		else {
			_fail("PLANS " + _TABLE_NAME_M, "");
		}
	}

	@Test
	public void testPlansSortedIndex() throws Exception {
		if (_setUpThrowable == null) {
			_fail("PLANS " + _TABLE_NAME_B, _explainAll(_TABLE_NAME_B));
		}
		else {
			_fail("PLANS " + _TABLE_NAME_B, "");
		}
	}

	@Test
	public void testRowsAndTimings() throws Exception {
		if (_setUpThrowable == null) {
			_fail("ROWS AND TIMINGS", _measureAll());
		}
		else {
			_fail("ROWS AND TIMINGS", "");
		}
	}

	private static void _addRow(
			PreparedStatement preparedStatement, long fieldAttributeId,
			long fieldId, String attributeName, String languageId)
		throws SQLException {

		preparedStatement.setLong(1, fieldAttributeId);
		preparedStatement.setLong(2, _COMPANY_ID);
		preparedStatement.setLong(3, fieldId);
		preparedStatement.setLong(4, fieldId / 6);
		_setString(preparedStatement, 5, attributeName);
		_setString(preparedStatement, 6, languageId);
		preparedStatement.setString(
			7, Long.toHexString(fieldAttributeId * 0x9E3779B97F4A7C15L));

		preparedStatement.addBatch();
	}

	private static void _appendSetUpLog(String label, Object value) {
		_setUpLog.append("--- ");
		_setUpLog.append(label);
		_setUpLog.append("\n");
		_setUpLog.append(String.valueOf(value));
		_setUpLog.append("\n");
	}

	private static void _appendShowplan(
		StringBundler sb, Element element, int depth) {

		String name = element.getNodeName();

		int index = name.indexOf(':');

		if (index >= 0) {
			name = name.substring(index + 1);
		}

		if (name.equals("RelOp")) {
			sb.append(_indent(depth));
			sb.append(element.getAttribute("PhysicalOp"));
			sb.append(" (");
			sb.append(element.getAttribute("LogicalOp"));
			sb.append(") estRows=");
			sb.append(element.getAttribute("EstimateRows"));
			sb.append(" estCost=");
			sb.append(element.getAttribute("EstimatedTotalSubtreeCost"));

			Element runTimeInformationElement = _getChildElement(
				element, "RunTimeInformation");

			if (runTimeInformationElement != null) {
				long actualExecutions = 0;
				long actualLogicalReads = 0;
				long actualRows = 0;

				for (Element countersElement :
						_getChildElements(runTimeInformationElement)) {

					actualExecutions += GetterUtil.getLong(
						countersElement.getAttribute("ActualExecutions"));
					actualLogicalReads += GetterUtil.getLong(
						countersElement.getAttribute("ActualLogicalReads"));
					actualRows += GetterUtil.getLong(
						countersElement.getAttribute("ActualRows"));
				}

				sb.append(" actualRows=");
				sb.append(actualRows);
				sb.append(" actualLogicalReads=");
				sb.append(actualLogicalReads);
				sb.append(" executions=");
				sb.append(actualExecutions);
			}

			sb.append("\n");

			for (Element childElement : _getChildElements(element)) {
				_appendShowplan(sb, childElement, depth + 1);
			}

			return;
		}

		if (name.equals("Object")) {
			sb.append(_indent(depth));
			sb.append("object ");
			sb.append(element.getAttribute("Table"));
			sb.append(".");
			sb.append(element.getAttribute("Index"));
			sb.append("\n");

			return;
		}

		if (name.equals("EndRange") || name.equals("Prefix") ||
			name.equals("StartRange")) {

			sb.append(_indent(depth));
			sb.append("seek ");
			sb.append(name);
			sb.append(" ");
			sb.append(element.getAttribute("ScanType"));
			sb.append(":");

			List<Element> columnElements = _getChildElements(
				_getChildElement(element, "RangeColumns"));
			List<Element> expressionElements = _getChildElements(
				_getChildElement(element, "RangeExpressions"));

			for (int i = 0; i < columnElements.size(); i++) {
				Element columnElement = columnElements.get(i);

				sb.append(" ");
				sb.append(columnElement.getAttribute("Column"));
				sb.append("=");

				if (i < expressionElements.size()) {
					Element expressionElement = expressionElements.get(i);

					sb.append(expressionElement.getAttribute("ScalarString"));
				}
			}

			sb.append("\n");

			return;
		}

		if (name.equals("Predicate")) {
			Element scalarOperatorElement = _getChildElement(
				element, "ScalarOperator");

			sb.append(_indent(depth));
			sb.append("predicate: ");

			if (scalarOperatorElement != null) {
				sb.append(
					_shorten(
						scalarOperatorElement.getAttribute("ScalarString"),
						600));
			}

			sb.append("\n");

			return;
		}

		if (name.equals("MissingIndexes") || name.equals("QueryTimeStats") ||
			name.equals("Warnings")) {

			sb.append(_indent(depth));
			sb.append(name);
			sb.append(": ");
			sb.append(_shorten(_getElementText(element), 1000));
			sb.append("\n");

			return;
		}

		if (name.equals("ParameterList")) {
			for (Element columnElement : _getChildElements(element)) {
				sb.append(_indent(depth));
				sb.append("parameter ");
				sb.append(columnElement.getAttribute("Column"));
				sb.append(" type=");
				sb.append(columnElement.getAttribute("ParameterDataType"));
				sb.append(" compiled=");
				sb.append(columnElement.getAttribute("ParameterCompiledValue"));
				sb.append(" runtime=");
				sb.append(columnElement.getAttribute("ParameterRuntimeValue"));
				sb.append("\n");
			}

			return;
		}

		if (name.equals("StmtSimple")) {
			sb.append(_indent(depth));
			sb.append("statement cost=");
			sb.append(element.getAttribute("StatementSubTreeCost"));
			sb.append(" estRows=");
			sb.append(element.getAttribute("StatementEstRows"));
			sb.append(" optmLevel=");
			sb.append(element.getAttribute("StatementOptmLevel"));
			sb.append(" earlyAbort=");
			sb.append(element.getAttribute("StatementOptmEarlyAbortReason"));
			sb.append(" ceModel=");
			sb.append(element.getAttribute("CardinalityEstimationModelVersion"));
			sb.append("\n");
		}

		if (name.equals("DefinedValues") ||
			name.equals("MemoryFractions") ||
			name.equals("OptimizerHardwareDependentProperties") ||
			name.equals("OptimizerStatsUsage") || name.equals("OutputList") ||
			name.equals("RunTimeInformation") ||
			name.equals("ScalarOperator") ||
			name.equals("StatementSetOptions") || name.equals("WaitStats")) {

			return;
		}

		for (Element childElement : _getChildElements(element)) {
			_appendShowplan(sb, childElement, depth);
		}
	}

	private static void _call(Connection connection, String sql)
		throws SQLException {

		try (CallableStatement callableStatement = connection.prepareCall(
				sql)) {

			callableStatement.execute();
		}
	}

	private static String _compactJSON(String json) {
		return json.replaceAll("\\s*\\n\\s*", "");
	}

	private static void _createIndexes(
			Connection connection, String tableName, String suffix,
			String uniqueColumns)
		throws Exception {

		_db.runSQL(
			connection,
			StringBundler.concat(
				"create unique index IX_PRB", suffix, "_U on ", tableName, " (",
				uniqueColumns, ");"));
		_db.runSQL(
			connection,
			StringBundler.concat(
				"create index IX_PRB", suffix, "_1 on ", tableName,
				" (attributeName[$COLUMN_LENGTH:255$], ",
				"smallAttributeValue[$COLUMN_LENGTH:255$]);"));
		_db.runSQL(
			connection,
			StringBundler.concat(
				"create index IX_PRB", suffix, "_2 on ", tableName,
				" (storageId, attributeName[$COLUMN_LENGTH:255$]);"));
		_db.runSQL(
			connection,
			StringBundler.concat(
				"create index IX_PRB", suffix, "_3 on ", tableName,
				" (storageId, languageId[$COLUMN_LENGTH:75$]);"));
	}

	private static void _dropTables(Connection connection) {
		for (String tableName : new String[] {_TABLE_NAME_B, _TABLE_NAME_M}) {
			try {
				_execute(connection, "DROP TABLE " + tableName);
			}
			catch (SQLException sqlException) {
				_droppedTableErrors.add(sqlException.getMessage());
			}
		}
	}

	private static void _execute(Connection connection, String sql)
		throws SQLException {

		try (Statement statement = connection.createStatement()) {
			statement.execute(sql);
		}
	}

	private static List<Long> _executeProbe(
			PreparedStatement preparedStatement, ProbeQuery probeQuery)
		throws SQLException {

		List<Long> values = new ArrayList<>();

		try (ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				if (probeQuery._count) {
					values.add(resultSet.getLong(1));
				}
				else {
					values.add(resultSet.getLong("fieldAttributeId"));
				}
			}
		}

		values.sort(null);

		return values;
	}

	private static String _explain(
			Connection connection, ProbeQuery probeQuery)
		throws Exception {

		if (_dbType == DBType.DB2) {
			return _explainDB2(connection, probeQuery);
		}

		if (_dbType == DBType.HYPERSONIC) {
			return _queryText(
				connection, "EXPLAIN PLAN FOR " + probeQuery.getLiteralSQL());
		}

		if (_dbType == DBType.MARIADB) {
			return StringBundler.concat(
				"--- ANALYZE FORMAT=JSON\n",
				_compactJSON(
					_queryText(
						connection,
						"ANALYZE FORMAT=JSON " + probeQuery.getLiteralSQL())),
				"\n--- EXPLAIN\n",
				_queryText(connection, "EXPLAIN " + probeQuery.getLiteralSQL()),
				"--- handler reads\n", _getHandlerReads(connection, probeQuery));
		}

		if (_dbType == DBType.MYSQL) {
			return StringBundler.concat(
				"--- EXPLAIN ANALYZE\n",
				_queryText(
					connection, "EXPLAIN ANALYZE " + probeQuery.getLiteralSQL()),
				"--- EXPLAIN FORMAT=JSON\n",
				_compactJSON(
					_queryText(
						connection,
						"EXPLAIN FORMAT=JSON " + probeQuery.getLiteralSQL())),
				"\n--- handler reads\n",
				_getHandlerReads(connection, probeQuery));
		}

		if (_dbType == DBType.ORACLE) {
			return _explainOracle(connection, probeQuery);
		}

		if (_dbType == DBType.POSTGRESQL) {
			return _explainPostgreSQL(connection, probeQuery);
		}

		if (_dbType == DBType.SQLSERVER) {
			return _explainSQLServer(connection, probeQuery);
		}

		return "unsupported " + _dbType;
	}

	private static String _explainAll(String tableName) throws Exception {
		StringBundler sb = new StringBundler();

		try (Connection connection = DataAccess.getConnection()) {
			for (ProbeQuery probeQuery : _getProbeQueries(tableName)) {
				if (!probeQuery._explain) {
					continue;
				}

				sb.append("\n=== ");
				sb.append(_dbType);
				sb.append(" | ");
				sb.append(tableName);
				sb.append(" | ");
				sb.append(probeQuery._shape);
				sb.append(" ");
				sb.append(probeQuery._target);
				sb.append(" ===\n");
				sb.append(probeQuery.getSQL("", false));
				sb.append(" -- ");
				sb.append(Arrays.toString(probeQuery._parameters));
				sb.append("\n");

				try {
					sb.append(_explain(connection, probeQuery));
				}
				catch (Throwable throwable) {
					sb.append("EXPLAIN FAILED: ");
					sb.append(throwable.toString());
					sb.append("\n");
				}
			}
		}

		return sb.toString();
	}

	private static String _explainDB2(
			Connection connection, ProbeQuery probeQuery)
		throws Exception {

		int queryNo = ++_db2QueryNo;

		String explainPrefix = "EXPLAIN PLAN SET QUERYNO = " + queryNo + " FOR ";

		String mode = "parameter markers";

		try {
			_execute(connection, explainPrefix + probeQuery.getSQL("", false));
		}
		catch (SQLException sqlException) {
			mode = "literals, markers failed: " + sqlException.getMessage();

			_execute(connection, explainPrefix + probeQuery.getLiteralSQL());
		}

		String prefix = _db2ExplainTablePrefix;

		Timestamp explainTime = null;
		String totalCost = null;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				StringBundler.concat(
					"SELECT EXPLAIN_TIME, TOTAL_COST FROM ", prefix,
					"EXPLAIN_STATEMENT WHERE QUERYNO = ? AND EXPLAIN_LEVEL = ",
					"'P' ORDER BY EXPLAIN_TIME DESC FETCH FIRST 1 ROWS ONLY"))) {

			preparedStatement.setInt(1, queryNo);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					explainTime = resultSet.getTimestamp(1);
					totalCost = resultSet.getString(2);
				}
			}
		}

		if (explainTime == null) {
			return "--- DB2 EXPLAIN (" + mode + ") no EXPLAIN_STATEMENT row\n";
		}

		return StringBundler.concat(
			"--- DB2 EXPLAIN (", mode, ") statement totalCost=", totalCost,
			"\n--- operators: id | type | totalCost | ioCost | cpuCost | ",
			"firstRowCost\n",
			_queryText(
				connection,
				StringBundler.concat(
					"SELECT OPERATOR_ID, OPERATOR_TYPE, TOTAL_COST, IO_COST, ",
					"CPU_COST, FIRST_ROW_COST FROM ", prefix,
					"EXPLAIN_OPERATOR WHERE EXPLAIN_TIME = ? ORDER BY ",
					"OPERATOR_ID"),
				explainTime),
			"--- streams: sourceType | sourceId | targetType | targetId | ",
			"object | rows\n",
			_queryText(
				connection,
				StringBundler.concat(
					"SELECT SOURCE_TYPE, SOURCE_ID, TARGET_TYPE, TARGET_ID, ",
					"OBJECT_NAME, STREAM_COUNT FROM ", prefix,
					"EXPLAIN_STREAM WHERE EXPLAIN_TIME = ? ORDER BY STREAM_ID"),
				explainTime),
			"--- predicates: operatorId | howApplied | text\n",
			_queryText(
				connection,
				StringBundler.concat(
					"SELECT OPERATOR_ID, HOW_APPLIED, PREDICATE_TEXT FROM ",
					prefix,
					"EXPLAIN_PREDICATE WHERE EXPLAIN_TIME = ? ORDER BY ",
					"OPERATOR_ID, PREDICATE_ID"),
				explainTime));
	}

	private static String _explainOracle(
			Connection connection, ProbeQuery probeQuery)
		throws Exception {

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				probeQuery.getSQL("/*+ gather_plan_statistics */ ", false))) {

			probeQuery.setParameters(preparedStatement);

			_executeProbe(preparedStatement, probeQuery);
		}

		String plan = _queryText(
			connection,
			"SELECT PLAN_TABLE_OUTPUT FROM TABLE(DBMS_XPLAN.DISPLAY_CURSOR(" +
				"NULL, NULL, 'ALLSTATS LAST'))");

		if (plan.contains("Plan hash value")) {
			return "--- DISPLAY_CURSOR ALLSTATS LAST (bound)\n" + plan;
		}

		_execute(
			connection,
			"EXPLAIN PLAN SET STATEMENT_ID = 'PROBE' FOR " +
				probeQuery.getLiteralSQL());

		String explainPlan = _queryText(
			connection,
			"SELECT PLAN_TABLE_OUTPUT FROM TABLE(DBMS_XPLAN.DISPLAY(" +
				"'PLAN_TABLE', 'PROBE', 'TYPICAL'))");

		_execute(connection, "DELETE FROM PLAN_TABLE WHERE STATEMENT_ID = 'PROBE'");

		return StringBundler.concat(
			"--- DISPLAY_CURSOR failed\n", plan,
			"--- EXPLAIN PLAN TYPICAL (literal)\n", explainPlan);
	}

	private static String _explainPostgreSQL(
			Connection connection, ProbeQuery probeQuery)
		throws Exception {

		StringBundler sb = new StringBundler();

		String executeSQL = StringBundler.concat(
			"EXPLAIN (ANALYZE, BUFFERS) EXECUTE probe_query(",
			probeQuery.getLiteralParameters(), ")");
		String prepareSQL = StringBundler.concat(
			"PREPARE probe_query(", probeQuery.getPostgreSQLTypes(), ") AS ",
			probeQuery.getSQL("", true));

		try {
			for (String planCacheMode :
					new String[] {"force_custom_plan", "force_generic_plan"}) {

				_execute(connection, "SET plan_cache_mode = " + planCacheMode);
				_execute(connection, prepareSQL);

				try {
					String plan = null;

					for (int i = 0; i < 3; i++) {
						plan = _queryText(connection, executeSQL);
					}

					sb.append("--- ");
					sb.append(planCacheMode);
					sb.append("\n");
					sb.append(plan);
				}
				finally {
					_execute(connection, "DEALLOCATE probe_query");
				}
			}
		}
		finally {
			_execute(connection, "RESET plan_cache_mode");
		}

		return sb.toString();
	}

	private static String _explainSQLServer(
			Connection connection, ProbeQuery probeQuery)
		throws Exception {

		StringBundler sb = new StringBundler();

		connection.clearWarnings();

		_execute(connection, "SET STATISTICS IO ON");

		try {
			_execute(connection, "SET STATISTICS XML ON");

			try (PreparedStatement preparedStatement =
					connection.prepareStatement(probeQuery.getSQL("", false))) {

				probeQuery.setParameters(preparedStatement);

				boolean resultSetAvailable = preparedStatement.execute();

				while (true) {
					if (resultSetAvailable) {
						try (ResultSet resultSet =
								preparedStatement.getResultSet()) {

							ResultSetMetaData resultSetMetaData =
								resultSet.getMetaData();

							String columnLabel =
								resultSetMetaData.getColumnLabel(1);

							if (columnLabel.contains("Showplan")) {
								while (resultSet.next()) {
									String xml = resultSet.getString(1);

									sb.append("--- actual plan summary\n");
									sb.append(_summarizeShowplan(xml));
								}
							}
							else {
								int rows = 0;

								while (resultSet.next()) {
									rows++;
								}

								sb.append("--- result rows=");
								sb.append(rows);
								sb.append("\n");
							}
						}
					}
					else if (preparedStatement.getUpdateCount() == -1) {
						break;
					}

					resultSetAvailable = preparedStatement.getMoreResults();
				}

				sb.append("--- messages\n");
				sb.append(_getWarningsText(preparedStatement.getWarnings()));
				sb.append(_getWarningsText(connection.getWarnings()));
			}
		}
		finally {
			try {
				_execute(connection, "SET STATISTICS XML OFF");
			}
			finally {
				_execute(connection, "SET STATISTICS IO OFF");

				connection.clearWarnings();
			}
		}

		return sb.toString();
	}

	private static void _fail(String section, String body) {
		StringBundler sb = new StringBundler(8);

		sb.append("\n##### LPD-106533 PROBE ");
		sb.append(section);
		sb.append(" ");
		sb.append(_header);
		sb.append("\n");

		if (_setUpThrowable != null) {
			sb.append("SETUP FAILED\n");
			sb.append(StackTraceUtil.getStackTrace(_setUpThrowable));
		}

		if (body.isEmpty() || (_setUpThrowable != null)) {
			sb.append(_setUpLog.toString());
			sb.append("--- drop table errors (expected on a clean schema)\n");
			sb.append(_droppedTableErrors.toString());
		}

		sb.append(body);

		String report = sb.toString();

		System.out.println(report);

		Assert.fail(report);
	}

	private static void _gatherStatistics(
		Connection connection, String tableName) {

		try {
			if (_dbType == DBType.DB2) {
				_call(
					connection,
					StringBundler.concat(
						"CALL SYSPROC.ADMIN_CMD('RUNSTATS ON TABLE ", _db2Schema,
						".", tableName,
						" WITH DISTRIBUTION AND DETAILED INDEXES ALL')"));
			}
			else if ((_dbType == DBType.MARIADB) ||
					 (_dbType == DBType.MYSQL)) {

				_appendSetUpLog(
					"analyze " + tableName,
					_queryText(connection, "ANALYZE TABLE " + tableName));
			}
			else if (_dbType == DBType.ORACLE) {
				_execute(
					connection,
					StringBundler.concat(
						"BEGIN DBMS_STATS.GATHER_TABLE_STATS(ownname => USER, ",
						"tabname => '", tableName,
						"', cascade => TRUE); END;"));
			}
			else if (_dbType == DBType.POSTGRESQL) {
				try {
					_execute(connection, "VACUUM ANALYZE " + tableName);
				}
				catch (SQLException sqlException) {
					_appendSetUpLog(
						"vacuum " + tableName, sqlException.getMessage());

					_execute(connection, "ANALYZE " + tableName);
				}
			}
			else if (_dbType == DBType.SQLSERVER) {
				_execute(
					connection, "UPDATE STATISTICS " + tableName + " WITH FULLSCAN");
			}
		}
		catch (SQLException sqlException) {
			_appendSetUpLog(
				"statistics " + tableName + " FAILED", sqlException.toString());
		}
	}

	private static Element _getChildElement(Element element, String name) {
		for (Element childElement : _getChildElements(element)) {
			String childName = childElement.getNodeName();

			if (childName.equals(name) || childName.endsWith(":" + name)) {
				return childElement;
			}
		}

		return null;
	}

	private static List<Element> _getChildElements(Element element) {
		List<Element> elements = new ArrayList<>();

		if (element == null) {
			return elements;
		}

		NodeList nodeList = element.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node instanceof Element childElement) {
				elements.add(childElement);
			}
		}

		return elements;
	}

	private static String _getCreateTableSQL(String tableName) {
		return StringBundler.concat(
			"create table ", tableName, " (\n",
			"\tmvccVersion LONG default 0 not null,\n",
			"\tctCollectionId LONG default 0 not null,\n",
			"\tfieldAttributeId LONG not null,\n", "\tcompanyId LONG,\n",
			"\tfieldId LONG,\n", "\tstorageId LONG,\n",
			"\tattributeName VARCHAR(255) null,\n",
			"\tlanguageId VARCHAR(75) null,\n",
			"\tlargeAttributeValue TEXT null,\n",
			"\tsmallAttributeValue VARCHAR(255) null,\n",
			"\tprimary key (fieldAttributeId, ctCollectionId)\n", ");");
	}

	private static String _getElementText(Element element) {
		StringBundler sb = new StringBundler();

		NamedNodeMap namedNodeMap = element.getAttributes();

		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node node = namedNodeMap.item(i);

			sb.append(node.getNodeName());
			sb.append("=");
			sb.append(node.getNodeValue());
			sb.append(" ");
		}

		for (Element childElement : _getChildElements(element)) {
			sb.append("<");
			sb.append(childElement.getNodeName());
			sb.append(" ");
			sb.append(_getElementText(childElement));
			sb.append("> ");
		}

		return sb.toString();
	}

	private static String[] _getEnvironmentSQLs() {
		if (_dbType == DBType.DB2) {
			return new String[] {
				"SELECT INST_NAME, SERVICE_LEVEL, FIXPACK_NUM FROM " +
					"SYSIBMADM.ENV_INST_INFO",
				"SELECT NAME, VALUE FROM SYSIBMADM.DBCFG WHERE NAME IN " +
					"('dft_queryopt', 'dft_degree', 'stmt_conc')",
				"VALUES CURRENT SCHEMA", "VALUES CURRENT USER"
			};
		}

		if ((_dbType == DBType.MARIADB) || (_dbType == DBType.MYSQL)) {
			return new String[] {
				"SELECT VERSION()", "SELECT @@optimizer_switch",
				"SELECT @@innodb_buffer_pool_size"
			};
		}

		if (_dbType == DBType.ORACLE) {
			return new String[] {
				"SELECT * FROM PRODUCT_COMPONENT_VERSION",
				"SELECT NAME, VALUE FROM V$PARAMETER WHERE NAME IN " +
					"('optimizer_features_enable', 'optimizer_mode', " +
						"'optimizer_index_cost_adj', 'statistics_level')"
			};
		}

		if (_dbType == DBType.POSTGRESQL) {
			return new String[] {
				"SELECT version()", "SHOW random_page_cost",
				"SHOW effective_cache_size", "SHOW shared_buffers",
				"SHOW plan_cache_mode", "SHOW jit",
				"SHOW max_parallel_workers_per_gather"
			};
		}

		if (_dbType == DBType.SQLSERVER) {
			return new String[] {
				"SELECT @@VERSION",
				"SELECT compatibility_level FROM sys.databases WHERE name = " +
					"DB_NAME()"
			};
		}

		return new String[0];
	}

	private static String _getHandlerReads(
			Connection connection, ProbeQuery probeQuery)
		throws SQLException {

		Map<String, Long> statusMap0 = _getHandlerStatusMap(connection);
		Map<String, Long> statusMap1 = _getHandlerStatusMap(connection);

		try (Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
				probeQuery.getLiteralSQL())) {

			while (resultSet.next()) {
			}
		}

		Map<String, Long> statusMap2 = _getHandlerStatusMap(connection);

		StringBundler sb = new StringBundler();

		long total = 0;

		for (Map.Entry<String, Long> entry : statusMap2.entrySet()) {
			String key = entry.getKey();

			long overhead =
				statusMap1.getOrDefault(key, 0L) -
					statusMap0.getOrDefault(key, 0L);

			long delta =
				entry.getValue() - statusMap1.getOrDefault(key, 0L) - overhead;

			if (delta != 0) {
				sb.append(key);
				sb.append("=");
				sb.append(delta);
				sb.append(" ");

				total += delta;
			}
		}

		sb.append("total=");
		sb.append(total);
		sb.append("\n");

		return sb.toString();
	}

	private static Map<String, Long> _getHandlerStatusMap(Connection connection)
		throws SQLException {

		Map<String, Long> statusMap = new HashMap<>();

		try (Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
				"SHOW SESSION STATUS LIKE 'Handler_read%'")) {

			while (resultSet.next()) {
				statusMap.put(resultSet.getString(1), resultSet.getLong(2));
			}
		}

		return statusMap;
	}

	private static List<ProbeQuery> _getProbeQueries(String tableName) {
		long base = _ROWS / 2;

		long eeFieldId = base + 40;
		long missFieldId = base + 100;
		long nnFieldId = base + 5;

		List<ProbeQuery> probeQueries = new ArrayList<>();

		for (String target : new String[] {"NN", "EE", "MISS", "PF"}) {
			long fieldId = _ROWS + 1;

			if (target.equals("EE")) {
				fieldId = eeFieldId;
			}
			else if (target.equals("MISS")) {
				fieldId = missFieldId;
			}
			else if (target.equals("NN")) {
				fieldId = nnFieldId;
			}

			boolean explain = !target.equals("PF");

			probeQueries.add(
				new ProbeQuery(
					"Q1_CNF2", target, tableName, false, _CNF2, explain,
					fieldId));
			probeQueries.add(
				new ProbeQuery(
					"Q2_DNF2", target, tableName, false, _DNF2, explain,
					fieldId));
		}

		probeQueries.add(
			new ProbeQuery(
				"Q3_ONE_AN", "HIT", tableName, false, _ONE_AN, true,
				missFieldId, "en_US"));
		probeQueries.add(
			new ProbeQuery(
				"Q3_ONE_L", "HIT", tableName, false, _ONE_L, true, base + 825,
				"defaultLanguageId"));
		probeQueries.add(
			new ProbeQuery(
				"Q4_NONBLANK", "HIT", tableName, false, _NON_BLANK, true,
				base + 900, "attr4", "en_US"));
		probeQueries.add(
			new ProbeQuery(
				"Q5_COUNT_CNF2", "NN", tableName, true, _CNF2, true,
				nnFieldId));
		probeQueries.add(
			new ProbeQuery(
				"Q6_COUNT_DNF2", "NN", tableName, true, _DNF2, true,
				nnFieldId));
		probeQueries.add(
			new ProbeQuery(
				"Q7_ISNULL2", "NN", tableName, false, _IS_NULL2, true,
				nnFieldId));
		probeQueries.add(
			new ProbeQuery(
				"Q7_ISNULL2", "EE", tableName, false, _IS_NULL2, true,
				eeFieldId));

		return probeQueries;
	}

	private static String _getWarningsText(SQLWarning sqlWarning) {
		StringBundler sb = new StringBundler();

		while (sqlWarning != null) {
			sb.append(sqlWarning.getMessage());
			sb.append("\n");

			sqlWarning = sqlWarning.getNextWarning();
		}

		return sb.toString();
	}

	private static String _indent(int depth) {
		char[] chars = new char[depth * 2];

		Arrays.fill(chars, ' ');

		return new String(chars);
	}

	private static void _load(Connection connection) throws Exception {
		boolean autoCommit = connection.getAutoCommit();

		connection.setAutoCommit(false);

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				StringBundler.concat(
					"INSERT INTO ", _TABLE_NAME_M,
					" (mvccVersion, ctCollectionId, fieldAttributeId, ",
					"companyId, fieldId, storageId, attributeName, languageId, ",
					"smallAttributeValue) VALUES (0, 0, ?, ?, ?, ?, ?, ?, ?)"))) {

			for (long i = 1; i <= _ROWS; i++) {
				int m = (int)(i % 1000);

				String attributeName = "attr" + (m % 7);
				String languageId = "en_US";

				if (m < 30) {
					attributeName = null;
					languageId = null;
				}
				else if (m < 64) {
					attributeName = "";
					languageId = "";
				}
				else if (m < 797) {
					attributeName = null;
				}
				else if (m < 817) {
					attributeName = "";
				}
				else if (m < 823) {
					attributeName = "defaultLanguageId";
					languageId = null;
				}
				else if (m < 857) {
					attributeName = "defaultLanguageId";

					if ((m % 2) == 0) {
						attributeName = "availableLanguageIds";
					}

					languageId = "";
				}

				_addRow(preparedStatement, i, i, attributeName, languageId);

				if ((i % 1000) == 0) {
					preparedStatement.executeBatch();

					connection.commit();
				}
			}

			_addRow(preparedStatement, _ROWS + 1, _ROWS + 1, null, null);

			if (_dbType != DBType.ORACLE) {
				_addRow(
					preparedStatement, _ROWS + 2, _ROWS + 1, null, "");
				_addRow(
					preparedStatement, _ROWS + 3, _ROWS + 1, "", null);
				_addRow(preparedStatement, _ROWS + 4, _ROWS + 1, "", "");
			}

			_addRow(
				preparedStatement, _ROWS + 5, _ROWS + 1, "attr4",
				"en_US");

			preparedStatement.executeBatch();

			connection.commit();
		}
		finally {
			connection.setAutoCommit(autoCommit);
		}
	}

	private static String _measureAll() throws Exception {
		StringBundler sb = new StringBundler();

		sb.append("table | query | target | rows | median us | max us (");
		sb.append(_TIMING_RUNS);
		sb.append(" warm runs, same PreparedStatement, bound like the ");
		sb.append("finder)\n");

		Map<String, List<Long>> valuesMap = new HashMap<>();

		try (Connection connection = DataAccess.getConnection()) {
			for (String tableName :
					new String[] {_TABLE_NAME_M, _TABLE_NAME_B}) {

				for (ProbeQuery probeQuery : _getProbeQueries(tableName)) {
					sb.append(tableName);
					sb.append(" | ");
					sb.append(probeQuery._shape);
					sb.append(" | ");
					sb.append(probeQuery._target);
					sb.append(" | ");

					try (PreparedStatement preparedStatement =
							connection.prepareStatement(
								probeQuery.getSQL("", false))) {

						probeQuery.setParameters(preparedStatement);

						List<Long> values = null;

						for (int i = 0; i < 3; i++) {
							values = _executeProbe(
								preparedStatement, probeQuery);
						}

						long[] nanos = new long[_TIMING_RUNS];

						for (int i = 0; i < _TIMING_RUNS; i++) {
							long startTime = System.nanoTime();

							_executeProbe(preparedStatement, probeQuery);

							nanos[i] = System.nanoTime() - startTime;
						}

						Arrays.sort(nanos);

						valuesMap.put(
							StringBundler.concat(
								tableName, " ", probeQuery._shape, " ",
								probeQuery._target),
							values);

						sb.append(values.toString());
						sb.append(" | ");
						sb.append(nanos[_TIMING_RUNS / 2] / 1000.0);
						sb.append(" | ");
						sb.append(nanos[_TIMING_RUNS - 1] / 1000.0);
						sb.append("\n");
					}
					catch (Throwable throwable) {
						sb.append("FAILED ");
						sb.append(throwable.toString());
						sb.append("\n");
					}
				}

				for (String target : new String[] {"NN", "EE", "MISS", "PF"}) {
					_appendComparison(
						sb, valuesMap, tableName, "Q1_CNF2", "Q2_DNF2",
						target);
				}

				_appendComparison(
					sb, valuesMap, tableName, "Q5_COUNT_CNF2", "Q6_COUNT_DNF2",
					"NN");
				_appendComparison(
					sb, valuesMap, tableName, "Q1_CNF2", "Q7_ISNULL2", "NN");
				_appendComparison(
					sb, valuesMap, tableName, "Q1_CNF2", "Q7_ISNULL2", "EE");
			}
		}

		return sb.toString();
	}

	private static void _appendComparison(
		StringBundler sb, Map<String, List<Long>> valuesMap, String tableName,
		String shape1, String shape2, String target) {

		List<Long> values1 = valuesMap.get(
			StringBundler.concat(tableName, " ", shape1, " ", target));
		List<Long> values2 = valuesMap.get(
			StringBundler.concat(tableName, " ", shape2, " ", target));

		sb.append("compare ");
		sb.append(tableName);
		sb.append(" ");
		sb.append(target);
		sb.append(" ");
		sb.append(shape1);
		sb.append("=");
		sb.append(String.valueOf(values1));
		sb.append(" ");
		sb.append(shape2);
		sb.append("=");
		sb.append(String.valueOf(values2));

		if (Objects.equals(values1, values2)) {
			sb.append(" MATCH\n");
		}
		else {
			sb.append(" MISMATCH\n");
		}
	}

	private static String _queryText(
		Connection connection, String sql, Object... parameters) {

		StringBundler sb = new StringBundler();

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				sql)) {

			for (int i = 0; i < parameters.length; i++) {
				preparedStatement.setObject(i + 1, parameters[i]);
			}

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

				int columnCount = resultSetMetaData.getColumnCount();

				while (resultSet.next()) {
					for (int i = 1; i <= columnCount; i++) {
						if (i > 1) {
							sb.append(" | ");
						}

						sb.append(String.valueOf(resultSet.getString(i)));
					}

					sb.append("\n");
				}
			}
		}
		catch (SQLException sqlException) {
			sb.append("ERROR: ");
			sb.append(sqlException.toString());
			sb.append("\n");
		}

		return sb.toString();
	}

	private static String _queryTexts(Connection connection, String[] sqls) {
		StringBundler sb = new StringBundler();

		for (String sql : sqls) {
			sb.append("> ");
			sb.append(sql);
			sb.append("\n");
			sb.append(_queryText(connection, sql));
		}

		return sb.toString();
	}

	private static String _queryValue(Connection connection, String sql)
		throws SQLException {

		try (Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql)) {

			if (resultSet.next()) {
				return resultSet.getString(1);
			}
		}

		return null;
	}

	private static void _setString(
			PreparedStatement preparedStatement, int index, String value)
		throws SQLException {

		if (value == null) {
			preparedStatement.setNull(index, Types.VARCHAR);
		}
		else {
			preparedStatement.setString(index, value);
		}
	}

	private static void _setUpDB2ExplainTables(Connection connection)
		throws SQLException {

		String schema = _queryValue(
			connection,
			"SELECT TABSCHEMA FROM SYSCAT.TABLES WHERE TABNAME = " +
				"'EXPLAIN_OPERATOR' AND TABSCHEMA IN (CURRENT USER, " +
					"'SYSTOOLS') ORDER BY CASE WHEN TABSCHEMA = 'SYSTOOLS' " +
						"THEN 1 ELSE 0 END FETCH FIRST 1 ROWS ONLY");

		if (schema == null) {
			_call(
				connection,
				"CALL SYSPROC.SYSINSTALLOBJECTS('EXPLAIN', 'C', CAST(NULL AS " +
					"VARCHAR(128)), CAST(NULL AS VARCHAR(128)))");

			_db2ExplainTablesCreated = true;

			schema = "SYSTOOLS";
		}

		_db2ExplainTablePrefix = StringUtil.trim(schema) + ".";

		_appendSetUpLog(
			"db2 explain tables",
			_db2ExplainTablePrefix + " created=" + _db2ExplainTablesCreated);
	}

	private static String _shorten(String s, int length) {
		if (s.length() <= length) {
			return s;
		}

		return s.substring(0, length) + "...";
	}

	private static String _summarizeShowplan(String xml) {
		try {
			DocumentBuilderFactory documentBuilderFactory =
				SecureXMLFactoryProviderUtil.newDocumentBuilderFactory();

			DocumentBuilder documentBuilder =
				documentBuilderFactory.newDocumentBuilder();

			Document document = documentBuilder.parse(
				new InputSource(new StringReader(xml)));

			StringBundler sb = new StringBundler();

			_appendShowplan(sb, document.getDocumentElement(), 0);

			return sb.toString();
		}
		catch (Exception exception) {
			return StringBundler.concat(
				"SHOWPLAN PARSE FAILED ", exception.toString(), "\n",
				_shorten(xml, 12000), "\n");
		}
	}

	private static final long _COMPANY_ID = 20096;

	private static final String _CNF2 =
		"fieldId = ? AND (attributeName IS NULL OR attributeName = '') AND " +
			"(languageId IS NULL OR languageId = '') AND ctCollectionId = 0";

	private static final String _DNF2 =
		"fieldId = ? AND ((attributeName IS NULL AND languageId IS NULL) OR " +
			"(attributeName IS NULL AND languageId = '') OR (attributeName = " +
				"'' AND languageId IS NULL) OR (attributeName = '' AND " +
					"languageId = '')) AND ctCollectionId = 0";

	private static final String _IS_NULL2 =
		"fieldId = ? AND attributeName IS NULL AND languageId IS NULL AND " +
			"ctCollectionId = 0";

	private static final String _NON_BLANK =
		"fieldId = ? AND attributeName = ? AND languageId = ? AND " +
			"ctCollectionId = 0";

	private static final String _ONE_AN =
		"fieldId = ? AND (attributeName IS NULL OR attributeName = '') AND " +
			"languageId = ? AND ctCollectionId = 0";

	private static final String _ONE_L =
		"fieldId = ? AND attributeName = ? AND (languageId IS NULL OR " +
			"languageId = '') AND ctCollectionId = 0";

	private static final long _ROWS = 500000;

	private static final String _TABLE_NAME_B = "PROBE_DDMFA_B";

	private static final String _TABLE_NAME_M = "PROBE_DDMFA_M";

	private static final int _TIMING_RUNS = 20;

	private static final String _UNIQUE_COLUMNS_B =
		"fieldId, attributeName[$COLUMN_LENGTH:255$], " +
			"languageId[$COLUMN_LENGTH:75$], ctCollectionId";

	private static final String _UNIQUE_COLUMNS_M =
		"attributeName[$COLUMN_LENGTH:255$], languageId[$COLUMN_LENGTH:75$], " +
			"fieldId, ctCollectionId";

	private static DB _db;
	private static String _db2ExplainTablePrefix = "";
	private static boolean _db2ExplainTablesCreated;
	private static int _db2QueryNo;
	private static String _db2Schema;
	private static DBType _dbType;
	private static final List<String> _droppedTableErrors = new ArrayList<>();
	private static String _header = "[no header]";
	private static final StringBundler _setUpLog = new StringBundler();
	private static Throwable _setUpThrowable;

	private static class ProbeQuery {

		public String getLiteralParameters() {
			StringBundler sb = new StringBundler();

			for (int i = 0; i < _parameters.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}

				sb.append(_toLiteral(_parameters[i]));
			}

			return sb.toString();
		}

		public String getLiteralSQL() {
			String sql = getSQL("", false);

			StringBundler sb = new StringBundler();

			int parameterIndex = 0;

			for (int i = 0; i < sql.length(); i++) {
				char c = sql.charAt(i);

				if (c == '?') {
					sb.append(_toLiteral(_parameters[parameterIndex++]));
				}
				else {
					sb.append(String.valueOf(c));
				}
			}

			return sb.toString();
		}

		public String getPostgreSQLTypes() {
			StringBundler sb = new StringBundler();

			for (int i = 0; i < _parameters.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}

				if (_parameters[i] instanceof Long) {
					sb.append("bigint");
				}
				else {
					sb.append("varchar");
				}
			}

			return sb.toString();
		}

		public String getSQL(String hint, boolean dollarParameters) {
			String select = "SELECT " + hint + "* FROM ";

			if (_count) {
				select = "SELECT " + hint + "COUNT(*) FROM ";
			}

			String sql = StringBundler.concat(
				select, _tableName, " WHERE ", _where);

			if (!dollarParameters) {
				return sql;
			}

			StringBundler sb = new StringBundler();

			int parameterIndex = 0;

			for (int i = 0; i < sql.length(); i++) {
				char c = sql.charAt(i);

				if (c == '?') {
					sb.append("$");
					sb.append(++parameterIndex);
				}
				else {
					sb.append(String.valueOf(c));
				}
			}

			return sb.toString();
		}

		public void setParameters(PreparedStatement preparedStatement)
			throws SQLException {

			for (int i = 0; i < _parameters.length; i++) {
				Object parameter = _parameters[i];

				if (parameter instanceof Long longParameter) {
					preparedStatement.setLong(i + 1, longParameter);
				}
				else {
					preparedStatement.setString(i + 1, (String)parameter);
				}
			}
		}

		private ProbeQuery(
			String shape, String target, String tableName, boolean count,
			String where, boolean explain, Object... parameters) {

			_shape = shape;
			_target = target;
			_tableName = tableName;
			_count = count;
			_where = where;
			_explain = explain;
			_parameters = parameters;
		}

		private String _toLiteral(Object parameter) {
			if (parameter instanceof Long) {
				return String.valueOf(parameter);
			}

			String string = (String)parameter;

			return "'" + string.replace("'", "''") + "'";
		}

		private final boolean _count;
		private final boolean _explain;
		private final Object[] _parameters;
		private final String _shape;
		private final String _tableName;
		private final String _target;
		private final String _where;

	}

}