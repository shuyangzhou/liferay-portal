/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.asset.kernel.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.persistence.AssetEntryPersistence;
import com.liferay.asset.kernel.service.persistence.AssetEntryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.transaction.Isolation;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionConfig;
import com.liferay.portal.kernel.transaction.TransactionInvokerUtil;
import com.liferay.portal.kernel.util.StackTraceUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.IntFunction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class AssetEntryKeysetThresholdProbeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		try (Connection connection = DataAccess.getConnection()) {
			DatabaseMetaData databaseMetaData = connection.getMetaData();

			_databaseProductVersion = StringBundler.concat(
				databaseMetaData.getDatabaseProductName(), StringPool.SPACE,
				databaseMetaData.getDatabaseProductVersion());

			connection.setAutoCommit(false);

			try {
				_load(connection);
			}
			finally {
				connection.setAutoCommit(true);
			}

			long startTime = System.currentTimeMillis();

			_gatherStatistics(connection);

			_statisticsTime = System.currentTimeMillis() - startTime;
		}
		catch (Throwable throwable) {
			_setUpThrowable = throwable;
		}
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		long startTime = System.currentTimeMillis();

		try (Connection connection = DataAccess.getConnection()) {
			connection.setAutoCommit(false);

			try (PreparedStatement preparedStatement =
					AutoBatchPreparedStatementUtil.autoBatch(
						connection,
						"delete from AssetEntry where entryId >= ? and " +
							"entryId < ?")) {

				for (long entryId = _ENTRY_ID; entryId <= _lastEntryId;
					 entryId += 50000) {

					preparedStatement.setLong(1, entryId);
					preparedStatement.setLong(2, entryId + 50000);

					preparedStatement.addBatch();

					preparedStatement.executeBatch();

					connection.commit();
				}
			}
			finally {
				connection.setAutoCommit(true);
			}
		}

		AssetEntryUtil.clearCache();

		System.out.println(
			"KEYSET-PROBE cleanup ms " +
				(System.currentTimeMillis() - startTime));
	}

	@Test
	public void testMeasure() {
		long deadline = System.currentTimeMillis() + _MEASURE_TIME;

		DBType dbType = DBManagerUtil.getDBType();

		StringBundler sb = new StringBundler();

		sb.append("KEYSET-PROBE ");
		sb.append(dbType.getName());
		sb.append(" | ");
		sb.append(_databaseProductVersion);
		sb.append("\nrows ");
		sb.append(_productionRows);
		sb.append(" + ");
		sb.append(_ctRows);
		sb.append(" CT drafts, complete ");
		sb.append(_loadComplete);
		sb.append(", load s ");
		sb.append(_loadTime / 1000);
		sb.append(", statistics s ");
		sb.append(_statisticsTime / 1000);

		if (_setUpThrowable != null) {
			sb.append("\nsetUp failed: ");
			sb.append(StackTraceUtil.getStackTrace(_setUpThrowable));
		}
		else {
			Object threshold = ReflectionTestUtil.getFieldValue(
				_assetEntryPersistence, _FIELD_NAME);

			try {
				_measure(deadline, sb);
			}
			catch (Throwable throwable) {
				sb.append("\nmeasure failed: ");
				sb.append(StackTraceUtil.getStackTrace(throwable));
			}
			finally {
				ReflectionTestUtil.setFieldValue(
					_assetEntryPersistence, _FIELD_NAME, threshold);
			}
		}

		String table = sb.toString();

		System.out.println(table);

		throw new AssertionError(table);
	}

	private static void _addBatch(
			PreparedStatement preparedStatement, Random random, long entryId,
			long ctCollectionId, int index)
		throws SQLException {

		long classNameId = 0;
		long companyId = 0;
		long groupId = 0;

		if (random.nextDouble() < 0.85) {
			companyId = _COMPANY_ID;

			if (random.nextDouble() < 0.64) {
				groupId = _GROUP_ID;
			}
			else {
				groupId = _GROUP_ID + 79 + random.nextInt(60);
			}

			if (random.nextDouble() < 0.75) {
				classNameId = _CLASS_NAME_ID;
			}
			else {
				classNameId = _CLASS_NAME_ID + 1 + random.nextInt(12);
			}
		}
		else {
			classNameId = _CLASS_NAME_ID + random.nextInt(13);
			companyId = _COMPANY_ID + 1;
			groupId = _GROUP_ID + 9979 + random.nextInt(20);
		}

		int user = 1 + random.nextInt(200);

		long createTime =
			_BASE_TIME + (((index * 97L) + random.nextInt(61)) * 1000);

		preparedStatement.setLong(1, random.nextInt(6));
		preparedStatement.setLong(2, ctCollectionId);
		preparedStatement.setLong(3, entryId);
		preparedStatement.setLong(4, groupId);
		preparedStatement.setLong(5, companyId);
		preparedStatement.setLong(6, 20123 + user);
		preparedStatement.setString(7, "Test User " + user);
		preparedStatement.setTimestamp(8, new Timestamp(createTime));
		preparedStatement.setTimestamp(
			9,
			new Timestamp(
				createTime + (random.nextInt((86400 * 30) + 1) * 1000L)));
		preparedStatement.setLong(10, classNameId);
		preparedStatement.setLong(11, entryId + 7 + ctCollectionId);
		preparedStatement.setString(12, _uuid(random));
		preparedStatement.setLong(
			13, _CLASS_TYPE_IDS[random.nextInt(_CLASS_TYPE_IDS.length)]);
		preparedStatement.setBoolean(14, true);
		preparedStatement.setBoolean(15, random.nextDouble() < 0.95);
		preparedStatement.setTimestamp(16, new Timestamp(createTime));
		preparedStatement.setString(17, "text/html");
		preparedStatement.setString(18, _xml("Title", _words(random, 3, 8)));
		preparedStatement.setString(
			19, _xml("Description", _words(random, 15, 35)));
		preparedStatement.setString(20, _words(random, 15, 40));

		if (random.nextDouble() < 0.3) {
			preparedStatement.setString(21, _uuid(random));
		}
		else {
			preparedStatement.setString(21, StringPool.BLANK);
		}

		preparedStatement.setInt(22, 0);
		preparedStatement.setInt(23, 0);
		preparedStatement.setDouble(24, 0);

		preparedStatement.addBatch();
	}

	private static void _gatherStatistics(Connection connection)
		throws Exception {

		DBType dbType = DBManagerUtil.getDBType();

		if (dbType == DBType.DB2) {
			String schema = null;

			try (Statement statement = connection.createStatement()) {
				ResultSet resultSet = statement.executeQuery(
					"select current schema from sysibm.sysdummy1");

				resultSet.next();

				schema = StringUtil.trim(resultSet.getString(1));
			}

			try (CallableStatement callableStatement = connection.prepareCall(
					"call sysproc.admin_cmd(?)")) {

				callableStatement.setString(
					1,
					StringBundler.concat(
						"RUNSTATS ON TABLE ", schema,
						".ASSETENTRY WITH DISTRIBUTION AND DETAILED INDEXES ",
						"ALL"));

				callableStatement.execute();
			}

			return;
		}

		String sql = null;

		if ((dbType == DBType.MARIADB) || (dbType == DBType.MYSQL)) {
			sql = "ANALYZE TABLE AssetEntry";
		}
		else if (dbType == DBType.ORACLE) {
			sql =
				"BEGIN DBMS_STATS.GATHER_TABLE_STATS(USER, 'ASSETENTRY', " +
					"cascade => TRUE); END;";
		}
		else if (dbType == DBType.POSTGRESQL) {
			sql = "ANALYZE AssetEntry";
		}
		else if (dbType == DBType.SQLSERVER) {
			sql = "UPDATE STATISTICS AssetEntry WITH FULLSCAN";
		}
		else {
			return;
		}

		try (Statement statement = connection.createStatement()) {
			statement.execute(sql);
		}
	}

	private static void _load(Connection connection) throws Exception {
		long startTime = System.currentTimeMillis();

		Random random = new Random(20261103);

		String sql = StringBundler.concat(
			"insert into AssetEntry (mvccVersion, ctCollectionId, entryId, ",
			"groupId, companyId, userId, userName, createDate, modifiedDate, ",
			"classNameId, classPK, classUuid, classTypeId, listable, visible, ",
			"publishDate, mimeType, title, description, summary, layoutUuid, ",
			"height, width, priority) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ",
			"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement preparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(connection, sql)) {

			int batchCTRows = 0;
			int batchProductionRows = 0;
			long entryId = _ENTRY_ID;

			for (int i = 0; i < _ROWS; i++) {
				entryId += 1 + random.nextInt(4);

				_lastEntryId = entryId;

				_addBatch(preparedStatement, random, entryId, 0, i);

				batchProductionRows++;

				if (random.nextDouble() < 0.01) {
					_addBatch(
						preparedStatement, random, entryId,
						31001 + random.nextInt(10), i);

					batchCTRows++;
				}

				if ((batchCTRows + batchProductionRows) < 1000) {
					continue;
				}

				preparedStatement.executeBatch();

				connection.commit();

				_ctRows += batchCTRows;
				_productionRows += batchProductionRows;

				batchCTRows = 0;
				batchProductionRows = 0;

				if ((System.currentTimeMillis() - startTime) > _LOAD_TIME) {
					break;
				}
			}

			if ((batchCTRows + batchProductionRows) > 0) {
				preparedStatement.executeBatch();

				connection.commit();

				_ctRows += batchCTRows;
				_productionRows += batchProductionRows;
			}
		}

		_loadComplete = _productionRows == _ROWS;
		_loadTime = System.currentTimeMillis() - startTime;
	}

	private static String _uuid(Random random) {
		UUID uuid = new UUID(random.nextLong(), random.nextLong());

		return uuid.toString();
	}

	private static String _words(Random random, int min, int max) {
		int count = min + random.nextInt(max - min + 1);

		StringBundler sb = new StringBundler(count * 2);

		for (int i = 0; i < count; i++) {
			if (i > 0) {
				sb.append(StringPool.SPACE);
			}

			sb.append(_WORDS[random.nextInt(_WORDS.length)]);
		}

		return sb.toString();
	}

	private static String _xml(String tag, String text) {
		return StringBundler.concat(
			"<?xml version='1.0' encoding='UTF-8'?><root available-locales=",
			"\"en_US\" default-locale=\"en_US\"><", tag,
			" language-id=\"en_US\">", text, "</", tag, "></root>");
	}

	private long _find(
			IntFunction<List<AssetEntry>> intFunction, int start, int threshold,
			List<Long> entryIds)
		throws Throwable {

		ReflectionTestUtil.setFieldValue(
			_assetEntryPersistence, _FIELD_NAME, threshold);

		long[] nanoTimes = new long[1];

		List<AssetEntry> assetEntries = _invoke(
			() -> {
				long nanoTime = System.nanoTime();

				List<AssetEntry> list = intFunction.apply(start);

				nanoTimes[0] = System.nanoTime() - nanoTime;

				return list;
			});

		entryIds.clear();

		for (AssetEntry assetEntry : assetEntries) {
			entryIds.add(assetEntry.getEntryId());
		}

		return nanoTimes[0];
	}

	private String _format(double milliseconds) {
		return String.valueOf(Math.round(milliseconds * 100) / 100.0);
	}

	private <T> T _invoke(Callable<T> callable) throws Throwable {
		return TransactionInvokerUtil.invoke(_transactionConfig, callable);
	}

	private void _measure(
			IntFunction<List<AssetEntry>> intFunction, int depth,
			StringBundler sb)
		throws Throwable {

		List<Long> flipEntryIds = new ArrayList<>();
		boolean identical = true;
		List<Long> offsetEntryIds = new ArrayList<>();
		long warmUpNanoTime = 0;

		for (int i = 0; i < 2; i++) {
			warmUpNanoTime = _find(intFunction, depth, 0, offsetEntryIds);

			_find(intFunction, depth, 1, flipEntryIds);

			identical = identical && _same(offsetEntryIds, flipEntryIds);
		}

		int runs = 7;

		if (warmUpNanoTime > 4000000000L) {
			runs = 3;
		}

		long[] flipNanoTimes = new long[runs];
		long[] offsetNanoTimes = new long[runs];

		for (int i = 0; i < runs; i++) {
			if ((i % 2) == 0) {
				offsetNanoTimes[i] = _find(
					intFunction, depth, 0, offsetEntryIds);
				flipNanoTimes[i] = _find(intFunction, depth, 1, flipEntryIds);
			}
			else {
				flipNanoTimes[i] = _find(intFunction, depth, 1, flipEntryIds);
				offsetNanoTimes[i] = _find(
					intFunction, depth, 0, offsetEntryIds);
			}

			identical = identical && _same(offsetEntryIds, flipEntryIds);
		}

		Arrays.sort(flipNanoTimes);
		Arrays.sort(offsetNanoTimes);

		double flipMilliseconds = flipNanoTimes[runs / 2] / 1000000.0;
		double offsetMilliseconds = offsetNanoTimes[runs / 2] / 1000000.0;

		sb.append(runs);
		sb.append(StringPool.SPACE);
		sb.append(_format(offsetMilliseconds));
		sb.append(StringPool.SPACE);
		sb.append(_format(flipMilliseconds));
		sb.append(StringPool.SPACE);
		sb.append(_format(offsetMilliseconds / flipMilliseconds));
		sb.append(StringPool.SPACE);

		if (identical) {
			sb.append("yes");
		}
		else {
			sb.append("NO");
		}
	}

	private void _measure(long deadline, StringBundler sb) throws Throwable {
		String[] names = {"CompanyId", "GroupId", "G_C_V"};
		int[] counts = {
			_invoke(() -> _assetEntryPersistence.countByCompanyId(_COMPANY_ID)),
			_invoke(() -> _assetEntryPersistence.countByGroupId(_GROUP_ID)),
			_invoke(
				() -> _assetEntryPersistence.countByG_C_V(
					_GROUP_ID, _CLASS_NAME_ID, true))
		};

		List<IntFunction<List<AssetEntry>>> intFunctions = new ArrayList<>();

		intFunctions.add(
			start -> _assetEntryPersistence.findByCompanyId(
				_COMPANY_ID, start, start + _PAGE_SIZE, null, false));
		intFunctions.add(
			start -> _assetEntryPersistence.findByGroupId(
				_GROUP_ID, start, start + _PAGE_SIZE, null, false));
		intFunctions.add(
			start -> _assetEntryPersistence.findByG_C_V(
				_GROUP_ID, _CLASS_NAME_ID, true, start, start + _PAGE_SIZE,
				null, false));

		sb.append("\nmatches CompanyId ");
		sb.append(counts[0]);
		sb.append(", GroupId ");
		sb.append(counts[1]);
		sb.append(", G_C_V ");
		sb.append(counts[2]);
		sb.append("\nshape depth n offsetMs flipMs ratio identical");

		for (int depth : _DEPTHS) {
			for (int i = 0; i < names.length; i++) {
				if ((depth + _PAGE_SIZE) > counts[i]) {
					continue;
				}

				sb.append(StringPool.NEW_LINE);
				sb.append(names[i]);
				sb.append(StringPool.SPACE);
				sb.append(depth);
				sb.append(StringPool.SPACE);

				if (System.currentTimeMillis() > deadline) {
					sb.append("skipped (time budget)");

					continue;
				}

				_measure(intFunctions.get(i), depth, sb);
			}
		}
	}

	private boolean _same(List<Long> offsetEntryIds, List<Long> flipEntryIds) {
		if ((offsetEntryIds.size() == _PAGE_SIZE) &&
			offsetEntryIds.equals(flipEntryIds)) {

			return true;
		}

		return false;
	}

	private static final long _BASE_TIME = 1577836800000L;

	private static final long _CLASS_NAME_ID = 900000000020015L;

	private static final long[] _CLASS_TYPE_IDS = {0, 0, 0, 31005, 31007};

	private static final long _COMPANY_ID = 900000000020097L;

	private static final int[] _DEPTHS = {
		500, 1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000
	};

	private static final long _ENTRY_ID = 900000100000000L;

	private static final String _FIELD_NAME =
		"databaseKeysetPaginationOffsetThreshold";

	private static final long _GROUP_ID = 900000000020121L;

	private static final long _LOAD_TIME = 600000;

	private static final long _MEASURE_TIME = 840000;

	private static final int _PAGE_SIZE = 20;

	private static final int _ROWS = 1600000;

	private static final String[] _WORDS = {
		"asset", "content", "journal", "article", "blog", "entry", "document",
		"library", "folder", "page", "layout", "site", "portal", "liferay",
		"object", "definition", "workflow", "instance", "template", "structure",
		"fragment", "collection", "category", "vocabulary", "tag", "comment",
		"rating", "message", "board", "thread", "knowledge", "base", "wiki",
		"node", "calendar", "booking", "user", "group", "organization", "role",
		"permission", "account", "commerce", "product", "channel", "order",
		"invoice", "shipment", "catalog", "price", "list", "discount"
	};

	private static int _ctRows;
	private static String _databaseProductVersion;
	private static long _lastEntryId;
	private static boolean _loadComplete;
	private static long _loadTime;
	private static int _productionRows;
	private static Throwable _setUpThrowable;
	private static long _statisticsTime;
	private static final TransactionConfig _transactionConfig =
		TransactionConfig.Factory.create(
			Isolation.PORTAL, Propagation.SUPPORTS, true, -1,
			new Class<?>[] {Exception.class}, new String[0], new Class<?>[0],
			new String[0]);

	@Inject
	private AssetEntryPersistence _assetEntryPersistence;

}