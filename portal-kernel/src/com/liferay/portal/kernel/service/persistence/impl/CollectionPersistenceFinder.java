/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.service.persistence.impl;

import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.CountFinderPathRegistry;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.lang.reflect.Array;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Shuyang Zhou
 */
public class CollectionPersistenceFinder
	<T extends BaseModel<T>, E extends NoSuchModelException>
		extends BasePersistenceFinder<T, E> {

	@SafeVarargs
	public CollectionPersistenceFinder(
		BasePersistenceImpl<T, E> basePersistenceImpl,
		FinderPath paginatedFindPath, FinderPath unpaginatedFindPath,
		FinderPath countFinderPath, String sqlSelectWhere, String sqlCountWhere,
		String defaultOrderByJpql, String orderByEntityAlias, String where,
		String dbWhere, UniquePersistenceFinder<T, E> uniquePersistenceFinder,
		FinderColumn<T>... finderColumns) {

		super(
			basePersistenceImpl, sqlSelectWhere, where, dbWhere, finderColumns);

		_paginatedFindPath = paginatedFindPath;
		_unpaginatedFindPath = unpaginatedFindPath;
		_countFinderPath = countFinderPath;
		_sqlCountWhere = sqlCountWhere;
		_defaultOrderByJpql = defaultOrderByJpql;
		_orderByEntityAlias = orderByEntityAlias;
		_uniquePersistenceFinder = uniquePersistenceFinder;

		if (countFinderPath.isCountResult()) {
			CountFinderPathRegistry.register(countFinderPath);
		}

		List<Integer> arrayableIndexes = new ArrayList<>();

		for (int i = 0; i < finderColumns.length; i++) {
			if (!(finderColumns[i] instanceof ArrayableFinderColumn)) {
				continue;
			}

			arrayableIndexes.add(i);
		}

		if (arrayableIndexes.isEmpty()) {
			_arrayableIndexes = null;
		}
		else {
			_arrayableIndexes = ArrayUtil.toIntArray(arrayableIndexes);
		}

		_equalityCountFinderPath = _createEqualityCountFinderPath();
	}

	public int count(FinderCache finderCache, Object[] values) {
		try (SafeCloseable safeCloseable =
				setCTCollectionIdWithSafeCloseable()) {

			if ((_uniquePersistenceFinder != null) &&
				_unwrapIfAllArrayableLengthOne(values)) {

				return _uniquePersistenceFinder.count(finderCache, values);
			}

			normalizeValues(values);

			FinderPath countFinderPath = _countFinderPath;

			if ((_equalityCountFinderPath != null) &&
				_isAllArrayableLengthOne(values)) {

				countFinderPath = _equalityCountFinderPath;
			}

			Object[] finderArgs = buildFinderArgs(values);

			Long count = (Long)finderCache.getResult(
				countFinderPath, finderArgs, basePersistenceImpl);

			if (count == null) {
				List<Object[]> valuesList = _paginate(
					values, basePersistenceImpl.databaseInMaxParameters);

				if (valuesList == null) {
					count = _runCountSql(values);
				}
				else {
					count = _countChunked(valuesList);
				}

				finderCache.putResult(countFinderPath, finderArgs, count);
			}

			return count.intValue();
		}
	}

	public T fetchFirst(
		FinderCache finderCache, Object[] values,
		OrderByComparator<T> orderByComparator) {

		List<T> list = find(finderCache, values, 0, 1, orderByComparator, true);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public List<T> find(
		FinderCache finderCache, Object[] values, int start, int end,
		OrderByComparator<T> orderByComparator, boolean useFinderCache) {

		try (SafeCloseable safeCloseable =
				setCTCollectionIdWithSafeCloseable()) {

			if ((_uniquePersistenceFinder != null) &&
				_unwrapIfAllArrayableLengthOne(values)) {

				T entity = _uniquePersistenceFinder.fetch(
					finderCache, values, useFinderCache);

				if (entity == null) {
					return Collections.emptyList();
				}

				return Collections.singletonList(entity);
			}

			normalizeValues(values);

			FinderPath finderPath = null;
			Object[] finderArgs = null;

			if ((_unpaginatedFindPath != null) &&
				(start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null) &&
				!_isMultiElementArrayable(values)) {

				if (useFinderCache) {
					finderPath = _unpaginatedFindPath;
					finderArgs = buildFinderArgs(values);
				}
			}
			else if (useFinderCache) {
				finderPath = _paginatedFindPath;
				finderArgs = _buildPaginatedFinderArgs(
					values, start, end, orderByComparator);
			}

			List<T> list = null;

			if (useFinderCache) {
				list = (List<T>)finderCache.getResult(
					finderPath, finderArgs, basePersistenceImpl);

				if ((list != null) && !list.isEmpty()) {
					for (T entity : list) {
						if (!matchesAll(entity, values)) {
							list = null;

							break;
						}
					}
				}
			}

			if (list == null) {
				List<Object[]> valuesList = _paginate(
					values, basePersistenceImpl.databaseInMaxParameters);

				if (valuesList == null) {
					list = _runFindSql(values, start, end, orderByComparator);

					basePersistenceImpl.cacheResult(list);
				}
				else {
					list = _findChunked(
						valuesList, start, end, orderByComparator);
				}

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}

			return list;
		}
	}

	public T findFirst(
			FinderCache finderCache, Object[] values,
			OrderByComparator<T> orderByComparator)
		throws E {

		T entity = fetchFirst(finderCache, values, orderByComparator);

		if (entity != null) {
			return entity;
		}

		throw basePersistenceImpl.newNoSuchModelException(
			buildNoSuchKeyMessage(values));
	}

	public void remove(FinderCache finderCache, Object[] values) {
		for (T entity :
				find(
					finderCache, values, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null, true)) {

			basePersistenceImpl.remove(entity);
		}
	}

	private String _buildFindSql(
		Object[] values, OrderByComparator<T> orderByComparator) {

		String sqlWhere = buildSQLWhere(sqlSelectWhere, values, false);

		if (orderByComparator == null) {
			return sqlWhere + _defaultOrderByJpql;
		}

		StringBundler sb = new StringBundler(
			(orderByComparator.getOrderByFields().length * 2) + 2);

		sb.append(sqlWhere);

		basePersistenceImpl.appendOrderByComparator(
			sb, _orderByEntityAlias, orderByComparator);

		return sb.toString();
	}

	private Object[] _buildPaginatedFinderArgs(
		Object[] values, int start, int end,
		OrderByComparator<T> orderByComparator) {

		Object[] finderArgs = new Object[finderColumns.length + 3];

		for (int i = 0; i < finderColumns.length; i++) {
			finderArgs[i] = finderColumns[i].toFinderArg(values[i]);
		}

		finderArgs[finderColumns.length] = start;
		finderArgs[finderColumns.length + 1] = end;
		finderArgs[finderColumns.length + 2] = orderByComparator;

		return finderArgs;
	}

	private Long _countChunked(List<Object[]> valuesList) {
		long total = 0L;

		for (Object[] values : valuesList) {
			total += _runCountSql(values);
		}

		return total;
	}

	private FinderPath _createEqualityCountFinderPath() {
		if ((_arrayableIndexes == null) || (_unpaginatedFindPath == null) ||
			(_uniquePersistenceFinder != null)) {

			return null;
		}

		FinderPath equalityCountFinderPath =
			_countFinderPath.createEqualityCountFinderPath();

		CountFinderPathRegistry.register(equalityCountFinderPath);

		return equalityCountFinderPath;
	}

	private KeysetQueries _createKeysetQueries() {
		Map<String, Integer> tableColumnsMap =
			basePersistenceImpl.getTableColumnsMap();

		String selectEntityFrom = StringBundler.concat(
			"SELECT ", basePersistenceImpl.getEntityAlias(), " FROM ");

		if (!Objects.equals(
				tableColumnsMap.get(basePersistenceImpl.getPKDBName()),
				Types.BIGINT) ||
			!sqlSelectWhere.startsWith(selectEntityFrom)) {

			return _NO_KEYSET_QUERIES;
		}

		String pkFieldName = basePersistenceImpl.getPKFieldName();

		String pkColumn = _orderByEntityAlias.concat(pkFieldName);

		String probeSqlSelectWhere = StringBundler.concat(
			"SELECT ", pkColumn, " FROM ",
			sqlSelectWhere.substring(selectEntityFrom.length()));

		KeysetQuery ascendingKeysetQuery = new KeysetQuery(
			true, pkColumn, probeSqlSelectWhere);
		KeysetQuery descendingKeysetQuery = new KeysetQuery(
			false, pkColumn, probeSqlSelectWhere);

		KeysetQuery defaultKeysetQuery = null;

		if (_defaultOrderByJpql.equals(ascendingKeysetQuery._orderBy)) {
			defaultKeysetQuery = ascendingKeysetQuery;
		}
		else if (_defaultOrderByJpql.equals(descendingKeysetQuery._orderBy)) {
			defaultKeysetQuery = descendingKeysetQuery;
		}

		return new KeysetQueries(
			ascendingKeysetQuery, defaultKeysetQuery, descendingKeysetQuery,
			pkFieldName);
	}

	private List<T> _findChunked(
		List<Object[]> valuesList, int start, int end,
		OrderByComparator<T> orderByComparator) {

		List<T> result = new ArrayList<>();

		for (Object[] values : valuesList) {
			result.addAll(
				_runFindSql(
					values, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					orderByComparator));
		}

		Collections.sort(result, orderByComparator);

		basePersistenceImpl.cacheResult(result);

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS)) {
			return Collections.unmodifiableList(result);
		}

		return Collections.unmodifiableList(
			ListUtil.subList(result, start, end));
	}

	private KeysetQuery _getKeysetQuery(
		int start, int end, OrderByComparator<T> orderByComparator) {

		int threshold =
			basePersistenceImpl.databaseKeysetPaginationOffsetThreshold;

		if ((threshold <= 0) || (start < threshold) || (end <= start)) {
			return null;
		}

		KeysetQueries keysetQueries = _keysetQueries;

		if (keysetQueries == null) {
			keysetQueries = _createKeysetQueries();

			_keysetQueries = keysetQueries;
		}

		if (orderByComparator == null) {
			return keysetQueries._defaultKeysetQuery;
		}

		String[] orderByFields = orderByComparator.getOrderByFields();

		if ((orderByFields.length != 1) ||
			!Objects.equals(orderByFields[0], keysetQueries._pkFieldName)) {

			return null;
		}

		if (orderByComparator.isAscending(orderByFields[0])) {
			return keysetQueries._ascendingKeysetQuery;
		}

		return keysetQueries._descendingKeysetQuery;
	}

	private boolean _isAllArrayableLengthOne(Object[] values) {
		if (_arrayableIndexes == null) {
			return false;
		}

		for (int index : _arrayableIndexes) {
			if (Array.getLength(values[index]) != 1) {
				return false;
			}
		}

		return true;
	}

	private boolean _isMultiElementArrayable(Object[] values) {
		if (_arrayableIndexes == null) {
			return false;
		}

		for (int index : _arrayableIndexes) {
			if (Array.getLength(values[index]) > 1) {
				return true;
			}
		}

		return false;
	}

	private List<Object[]> _paginate(Object[] values, int maxParameters) {
		if ((_arrayableIndexes == null) || (maxParameters <= 0)) {
			return null;
		}

		List<Object[]> valuesList = null;

		for (int index : _arrayableIndexes) {
			ArrayableFinderColumn<?> arrayableFinderColumn =
				(ArrayableFinderColumn<?>)finderColumns[index];

			if (arrayableFinderColumn.isAndOperator()) {
				continue;
			}

			Object array = values[index];

			if (Array.getLength(array) <= maxParameters) {
				continue;
			}

			Object[] pages = (Object[])ArrayUtil.split(array, maxParameters);

			if (valuesList == null) {
				valuesList = new ArrayList<>(1);

				valuesList.add(values);
			}

			List<Object[]> expandedValuesList = new ArrayList<>(
				valuesList.size() * pages.length);

			for (Object[] currentValues : valuesList) {
				for (Object page : pages) {
					Object[] newValues = currentValues.clone();

					newValues[index] = page;

					expandedValuesList.add(newValues);
				}
			}

			valuesList = expandedValuesList;
		}

		return valuesList;
	}

	private Long _runCountSql(Object[] values) {
		String sql = buildSQLWhere(_sqlCountWhere, values, false);

		Session session = null;

		try {
			session = basePersistenceImpl.openSession();

			Query query = session.createQuery(sql);

			QueryPos queryPos = QueryPos.getInstance(query);

			bindQueryParams(queryPos, values);

			return (Long)query.uniqueResult();
		}
		catch (Exception exception) {
			throw basePersistenceImpl.processException(exception);
		}
		finally {
			basePersistenceImpl.closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<T> _runFindSql(
		Object[] values, int start, int end,
		OrderByComparator<T> orderByComparator) {

		KeysetQuery keysetQuery = _getKeysetQuery(
			start, end, orderByComparator);

		if (keysetQuery != null) {
			return _runKeysetFindSql(keysetQuery, values, start, end);
		}

		String sql = _buildFindSql(values, orderByComparator);

		Session session = null;

		try {
			session = basePersistenceImpl.openSession();

			Query query = session.createQuery(sql);

			QueryPos queryPos = QueryPos.getInstance(query);

			bindQueryParams(queryPos, values);

			return (List<T>)QueryUtil.list(
				query, basePersistenceImpl.getDialect(), start, end);
		}
		catch (Exception exception) {
			throw basePersistenceImpl.processException(exception);
		}
		finally {
			basePersistenceImpl.closeSession(session);
		}
	}

	@SuppressWarnings("unchecked")
	private List<T> _runKeysetFindSql(
		KeysetQuery keysetQuery, Object[] values, int start, int end) {

		String sqlWhere = buildSQLWhere(
			StringPool.OPEN_PARENTHESIS, values, false);

		Session session = null;

		try {
			session = basePersistenceImpl.openSession();

			Query query = session.createQuery(
				StringBundler.concat(
					keysetQuery._probeSqlSelectWhere, sqlWhere,
					StringPool.CLOSE_PARENTHESIS, keysetQuery._orderBy));

			QueryPos queryPos = QueryPos.getInstance(query);

			bindQueryParams(queryPos, values);

			List<?> primaryKeys = QueryUtil.list(
				query, basePersistenceImpl.getDialect(), start, start + 1);

			if (primaryKeys.isEmpty()) {
				return Collections.emptyList();
			}

			query = session.createQuery(
				StringBundler.concat(
					sqlSelectWhere, sqlWhere, keysetQuery._seekSqlWhere,
					keysetQuery._orderBy));

			queryPos = QueryPos.getInstance(query);

			bindQueryParams(queryPos, values);

			queryPos.add((Long)primaryKeys.get(0));

			return (List<T>)QueryUtil.list(
				query, basePersistenceImpl.getDialect(), 0, end - start);
		}
		catch (Exception exception) {
			throw basePersistenceImpl.processException(exception);
		}
		finally {
			basePersistenceImpl.closeSession(session);
		}
	}

	private boolean _unwrapIfAllArrayableLengthOne(Object[] values) {
		if (!_isAllArrayableLengthOne(values)) {
			return false;
		}

		for (int index : _arrayableIndexes) {
			values[index] = Array.get(values[index], 0);
		}

		return true;
	}

	private static final KeysetQueries _NO_KEYSET_QUERIES = new KeysetQueries(
		null, null, null, null);

	private final int[] _arrayableIndexes;
	private final FinderPath _countFinderPath;
	private final String _defaultOrderByJpql;
	private final FinderPath _equalityCountFinderPath;
	private KeysetQueries _keysetQueries;
	private final String _orderByEntityAlias;
	private final FinderPath _paginatedFindPath;
	private final String _sqlCountWhere;
	private final UniquePersistenceFinder<T, E> _uniquePersistenceFinder;
	private final FinderPath _unpaginatedFindPath;

	private static class KeysetQueries {

		private KeysetQueries(
			KeysetQuery ascendingKeysetQuery, KeysetQuery defaultKeysetQuery,
			KeysetQuery descendingKeysetQuery, String pkFieldName) {

			_ascendingKeysetQuery = ascendingKeysetQuery;
			_defaultKeysetQuery = defaultKeysetQuery;
			_descendingKeysetQuery = descendingKeysetQuery;
			_pkFieldName = pkFieldName;
		}

		private final KeysetQuery _ascendingKeysetQuery;
		private final KeysetQuery _defaultKeysetQuery;
		private final KeysetQuery _descendingKeysetQuery;
		private final String _pkFieldName;

	}

	private static class KeysetQuery {

		private KeysetQuery(
			boolean ascending, String pkColumn, String probeSqlSelectWhere) {

			_probeSqlSelectWhere = probeSqlSelectWhere;

			if (ascending) {
				_orderBy = StringBundler.concat(" ORDER BY ", pkColumn, " ASC");
				_seekSqlWhere = StringBundler.concat(
					") AND ", pkColumn, " >= ?");
			}
			else {
				_orderBy = StringBundler.concat(
					" ORDER BY ", pkColumn, " DESC");
				_seekSqlWhere = StringBundler.concat(
					") AND ", pkColumn, " <= ?");
			}
		}

		private final String _orderBy;
		private final String _probeSqlSelectWhere;
		private final String _seekSqlWhere;

	}

}