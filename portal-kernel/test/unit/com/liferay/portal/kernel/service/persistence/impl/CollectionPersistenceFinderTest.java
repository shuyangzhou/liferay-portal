/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.Dialect;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionWrapper;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.sql.Types;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class CollectionPersistenceFinderTest {

	@Test
	public void testBuildSQLWhere() {
		_testBuildSQLWhereArrayable();
		_testBuildSQLWhereOneBlank();
		_testBuildSQLWhereTwoBlanks();
	}

	@Test
	public void testFindDeepPageByOffset() {
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t FROM TestModel t WHERE t.groupId = ? ORDER BY " +
					"t.testModelId ASC",
				"setLong[1, 7]", "setMaxResults[5]", "setFirstResult[9]",
				"list[true]", "[1]"),
			_find(10, _ORDER_BY_PK_ASC, 9, 14, null, Arrays.asList(1L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t FROM TestModel t WHERE t.groupId = ? ORDER BY " +
					"t.testModelId ASC",
				"setLong[1, 7]", "setMaxResults[5]", "setFirstResult[10]",
				"list[true]", "[1]"),
			_find(0, _ORDER_BY_PK_ASC, 10, 15, null, Arrays.asList(1L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t FROM TestModel t WHERE t.groupId = ? ORDER BY " +
					"t.name ASC",
				"setLong[1, 7]", "setMaxResults[5]", "setFirstResult[10]",
				"list[true]", "[1]"),
			_find(10, " ORDER BY t.name ASC", 10, 15, null, Arrays.asList(1L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t FROM TestModel t WHERE t.groupId = ? ORDER BY " +
					"t.name DESC",
				"setLong[1, 7]", "setMaxResults[5]", "setFirstResult[10]",
				"list[true]", "[1]"),
			_find(
				10, _ORDER_BY_PK_ASC, 10, 15,
				_createOrderByComparator("TestModel.name DESC"),
				Arrays.asList(1L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t FROM TestModel t WHERE t.groupId = ? ORDER BY " +
					"t.testModelId ASC, t.name ASC",
				"setLong[1, 7]", "setMaxResults[5]", "setFirstResult[10]",
				"list[true]", "[1]"),
			_find(
				10, _ORDER_BY_PK_ASC, 10, 15,
				_createOrderByComparator(
					"TestModel.testModelId ASC, TestModel.name ASC"),
				Arrays.asList(1L)));
	}

	@Test
	public void testFindDeepPageByPrimaryKey() {
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t.testModelId FROM TestModel t WHERE (t.groupId = ?) " +
					"ORDER BY t.testModelId ASC",
				"setLong[1, 7]", "setMaxResults[1]", "setFirstResult[10]",
				"list[true]",
				"SELECT t FROM TestModel t WHERE (t.groupId = ?) AND " +
					"t.testModelId >= ? ORDER BY t.testModelId ASC",
				"setLong[1, 7]", "setLong[2, 42]", "setMaxResults[5]",
				"setFirstResult[0]", "list[true]", "[42, 43]"),
			_find(
				10, _ORDER_BY_PK_ASC, 10, 15, null, Arrays.asList(42L),
				Arrays.asList(42L, 43L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t.testModelId FROM TestModel t WHERE (t.groupId = ?) " +
					"ORDER BY t.testModelId DESC",
				"setLong[1, 7]", "setMaxResults[1]", "setFirstResult[12]",
				"list[true]",
				"SELECT t FROM TestModel t WHERE (t.groupId = ?) AND " +
					"t.testModelId <= ? ORDER BY t.testModelId DESC",
				"setLong[1, 7]", "setLong[2, 42]", "setMaxResults[3]",
				"setFirstResult[0]", "list[true]", "[42, 41]"),
			_find(
				10, " ORDER BY t.name ASC", 12, 15,
				_createOrderByComparator("TestModel.testModelId DESC"),
				Arrays.asList(42L), Arrays.asList(42L, 41L)));
		Assert.assertEquals(
			Arrays.asList(
				"SELECT t.testModelId FROM TestModel t WHERE (t.groupId = ?) " +
					"ORDER BY t.testModelId ASC",
				"setLong[1, 7]", "setMaxResults[1]", "setFirstResult[10]",
				"list[true]", "[]"),
			_find(10, _ORDER_BY_PK_ASC, 10, 15, null, Collections.emptyList()));
	}

	@Test
	public void testMultiElementArrayableFindUsesPaginatedFinderPath() {
		FinderPath[] recordedFinderPath = new FinderPath[1];

		CollectionPersistenceFinder<TestModel, NoSuchModelException>
			collectionPersistenceFinder = _createCollectionPersistenceFinder();

		collectionPersistenceFinder.find(
			_createRecordingFinderCache(recordedFinderPath),
			new Object[] {new long[] {100L, 200L}}, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null, true);

		Assert.assertSame(_PAGINATED_FIND_PATH, recordedFinderPath[0]);
	}

	@Test
	public void testSingleElementArrayableFindUsesUnpaginatedFinderPath() {
		FinderPath[] recordedFinderPath = new FinderPath[1];

		CollectionPersistenceFinder<TestModel, NoSuchModelException>
			collectionPersistenceFinder = _createCollectionPersistenceFinder();

		collectionPersistenceFinder.find(
			_createRecordingFinderCache(recordedFinderPath),
			new Object[] {new long[] {100L}}, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null, true);

		Assert.assertSame(_UNPAGINATED_FIND_PATH, recordedFinderPath[0]);
	}

	@SafeVarargs
	private String _buildSQLWhere(
		boolean sqlQuery, Object[] values,
		FinderColumn<TestModel>... finderColumns) {

		CollectionPersistenceFinder<TestModel, NoSuchModelException>
			collectionPersistenceFinder = new CollectionPersistenceFinder<>(
				new BasePersistenceImpl<TestModel, NoSuchModelException>() {
				},
				_PAGINATED_FIND_PATH, _UNPAGINATED_FIND_PATH, _COUNT_FIND_PATH,
				"", "", "", "", "", "", null, finderColumns);

		collectionPersistenceFinder.normalizeValues(values);

		return collectionPersistenceFinder.buildSQLWhere(
			"WHERE ", values, sqlQuery);
	}

	private CollectionPersistenceFinder<TestModel, NoSuchModelException>
		_createCollectionPersistenceFinder() {

		return new CollectionPersistenceFinder<>(
			new BasePersistenceImpl<TestModel, NoSuchModelException>() {
			},
			_PAGINATED_FIND_PATH, _UNPAGINATED_FIND_PATH, _COUNT_FIND_PATH, "",
			"", "", "", "", "", null,
			new ArrayableFinderColumn<>(
				"t.", "col", FinderColumn.Type.LONG, "=", false, true, true,
				testModel -> 0L));
	}

	private FinderColumn<TestModel> _createFinderColumn(String columnName) {
		return new FinderColumn<>(
			"t.", columnName, FinderColumn.Type.LONG, "=", true, true,
			testModel -> 0L);
	}

	private OrderByComparator<TestModel> _createOrderByComparator(
		String orderBy) {

		return new OrderByComparator<TestModel>() {

			@Override
			public int compare(TestModel testModel1, TestModel testModel2) {
				return 0;
			}

			@Override
			public String getOrderBy() {
				return orderBy;
			}

		};
	}

	private FinderCache _createRecordingFinderCache(
		FinderPath[] recordedFinderPath) {

		return new FinderCache() {

			@Override
			public void clearCache() {
			}

			@Override
			public void clearCache(Class<?> clazz) {
			}

			@Override
			public void clearDSLQueryCache(String tableName) {
			}

			@Override
			public void clearLocalCache() {
			}

			@Override
			public Object getResult(
				FinderPath finderPath, Object[] args,
				BasePersistence<?> basePersistence) {

				recordedFinderPath[0] = finderPath;

				return Collections.emptyList();
			}

			@Override
			public void invalidate() {
			}

			@Override
			public void putResult(
				FinderPath finderPath, Object[] args, Object result) {
			}

			@Override
			public void removeCache(String className) {
			}

			@Override
			public void removeResult(FinderPath finderPath, Object[] args) {
			}

		};
	}

	private FinderColumn<TestModel> _createStringFinderColumn(
		String columnName) {

		return new FinderColumn<>(
			"t.", columnName, columnName + "_", FinderColumn.Type.STRING, "=",
			true, true, testModel -> null);
	}

	private List<String> _find(
		int threshold, String defaultOrderByJpql, int start, int end,
		OrderByComparator<TestModel> orderByComparator, List<?>... results) {

		List<String> events = new ArrayList<>();
		Deque<List<?>> resultsDeque = new ArrayDeque<>(Arrays.asList(results));

		CollectionPersistenceFinder<TestModel, NoSuchModelException>
			collectionPersistenceFinder = new CollectionPersistenceFinder<>(
				new BasePersistenceImpl<TestModel, NoSuchModelException>() {
					{
						databaseKeysetPaginationOffsetThreshold = threshold;

						setDBColumnNames(Collections.emptyMap());
					}

					@Override
					public void cacheResult(List<TestModel> models) {
					}

					@Override
					public void closeSession(Session session) {
					}

					@Override
					public Dialect getDialect() {
						return () -> true;
					}

					@Override
					public String getEntityAlias() {
						return "t";
					}

					@Override
					public Session openSession() {
						return new SessionWrapper(null) {

							@Override
							public Query createQuery(String queryString) {
								events.add(queryString);

								return (Query)ProxyUtil.newProxyInstance(
									CollectionPersistenceFinderTest.class.
										getClassLoader(),
									new Class<?>[] {Query.class},
									(proxy, method, args) -> {
										events.add(
											method.getName() +
												Arrays.toString(args));

										if (Objects.equals(
												method.getName(), "list")) {

											return resultsDeque.pop();
										}

										return null;
									});
							}

						};
					}

					@Override
					protected String getPKDBName() {
						return "testModelId";
					}

					@Override
					protected Map<String, Integer> getTableColumnsMap() {
						return HashMapBuilder.put(
							"name", Types.VARCHAR
						).put(
							"testModelId", Types.BIGINT
						).build();
					}

				},
				_PAGINATED_FIND_PATH, _UNPAGINATED_FIND_PATH, _COUNT_FIND_PATH,
				"SELECT t FROM TestModel t WHERE ", "", defaultOrderByJpql,
				"t.", "", "", null, _createFinderColumn("groupId"));

		events.add(
			String.valueOf(
				collectionPersistenceFinder.find(
					null, new Object[] {7L}, start, end, orderByComparator,
					false)));

		return events;
	}

	private void _testBuildSQLWhereArrayable() {
		Assert.assertEquals(
			"WHERE ((t.name IS NULL AND t.path IS NULL) OR (t.name IS NULL " +
				"AND t.path = '') OR (t.name = '' AND t.path IS NULL) OR " +
					"(t.name = '' AND t.path = ''))",
			_buildSQLWhere(
				false, new Object[] {new String[] {""}, ""},
				new ArrayableFinderColumn<>(
					"t.", "name", FinderColumn.Type.STRING, "=", false, true,
					true, testModel -> null),
				_createStringFinderColumn("path")));
	}

	private void _testBuildSQLWhereOneBlank() {
		Assert.assertEquals(
			"WHERE t.groupId = ? AND (t.name IS NULL OR t.name = '') AND " +
				"t.path = ?",
			_buildSQLWhere(
				false, new Object[] {1L, null, "x"},
				_createFinderColumn("groupId"),
				_createStringFinderColumn("name"),
				_createStringFinderColumn("path")));
	}

	private void _testBuildSQLWhereTwoBlanks() {
		FinderColumn<TestModel>[] finderColumns = new FinderColumn[] {
			_createFinderColumn("groupId"), _createStringFinderColumn("name"),
			_createFinderColumn("scope"), _createStringFinderColumn("path")
		};

		Assert.assertEquals(
			StringBundler.concat(
				"WHERE t.groupId = ? AND t.scope = ? AND ((t.name IS NULL AND ",
				"t.path IS NULL) OR (t.name IS NULL AND t.path = '') OR ",
				"(t.name = '' AND t.path IS NULL) OR (t.name = '' AND t.path ",
				"= ''))"),
			_buildSQLWhere(
				false, new Object[] {1L, "", 2L, null}, finderColumns));
		Assert.assertEquals(
			StringBundler.concat(
				"WHERE t.groupId = ? AND t.scope = ? AND ((t.name_ IS NULL ",
				"AND t.path_ IS NULL) OR (t.name_ IS NULL AND t.path_ = '') ",
				"OR (t.name_ = '' AND t.path_ IS NULL) OR (t.name_ = '' AND ",
				"t.path_ = ''))"),
			_buildSQLWhere(
				true, new Object[] {1L, "", 2L, null}, finderColumns));
	}

	private static final FinderPath _COUNT_FIND_PATH = new FinderPath(
		"TestModel", "countByCol", new String[] {Long.class.getName()},
		new String[] {"col"}, false);

	private static final String _ORDER_BY_PK_ASC =
		" ORDER BY t.testModelId ASC";

	private static final FinderPath _PAGINATED_FIND_PATH = new FinderPath(
		"TestModel.List1", "findByCol",
		new String[] {
			Long.class.getName(), Integer.class.getName(),
			Integer.class.getName(), OrderByComparator.class.getName()
		},
		new String[] {"col"}, true);

	private static final FinderPath _UNPAGINATED_FIND_PATH = new FinderPath(
		"TestModel.List2", "findByCol", new String[] {Long.class.getName()},
		new String[] {"col"}, true);

	private interface TestModel extends BaseModel<TestModel> {
	}

}