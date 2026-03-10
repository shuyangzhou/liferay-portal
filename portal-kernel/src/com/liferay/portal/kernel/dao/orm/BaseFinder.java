/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.dao.orm;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Encapsulates the shared findBy/fetchBy/countBy logic for a single Service
 * Builder finder definition. Each BaseFinder instance represents one
 * {@code <finder>} from {@code service.xml} and holds the finder's column
 * definitions, FinderPaths, SQL fragments, and cache validation logic.
 *
 * <p>
 * The generated PersistenceImpl creates one BaseFinder per finder in its
 * {@code @Activate} method and delegates findBy/countBy/fetchBy calls to it,
 * eliminating the duplicated boilerplate that was previously generated inline.
 * </p>
 *
 * @author Liferay
 */
public class BaseFinder<T extends BaseModel<T>> {

	/**
	 * Returns the number of entities matching the given column values.
	 *
	 * @param  columnValues the finder column values (autoboxed primitives and
	 *         objects)
	 * @return the count of matching entities
	 */
	public int count(Object[] columnValues) {
		_normalizeStringValues(columnValues);

		Object[] finderArgs = _toFinderArgs(columnValues);

		Long count = (Long)_finderCache.getResult(
			_finderPathCount, finderArgs, _basePersistenceImpl);

		if (count == null) {
			String sql = _buildCountSql(columnValues);

			Session session = null;

			try {
				session = _basePersistenceImpl.openSession();

				Query query = session.createQuery(sql);

				_bindParameters(query, columnValues);

				count = (Long)query.uniqueResult();

				_finderCache.putResult(_finderPathCount, finderArgs, count);
			}
			catch (Exception exception) {
				throw _basePersistenceImpl.processException(exception);
			}
			finally {
				_basePersistenceImpl.closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Fetches a single entity matching the given column values. Used for
	 * unique finders. Returns {@code null} if no match is found.
	 *
	 * @param  columnValues   the finder column values
	 * @param  useFinderCache whether to use the finder cache
	 * @return the matching entity, or {@code null}
	 */
	@SuppressWarnings("unchecked")
	public T fetchOne(Object[] columnValues, boolean useFinderCache) {
		_normalizeStringValues(columnValues);

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = _toFinderArgs(columnValues);
		}

		Object result = null;

		if (useFinderCache) {
			result = _finderCache.getResult(
				_finderPathFetch, finderArgs, _basePersistenceImpl);
		}

		if (result instanceof BaseModel) {
			T model = (T)result;

			if (!_cacheValidator.test(columnValues, model)) {
				result = null;
			}
		}

		if (result == null) {
			String sql = _buildSelectSql(columnValues, null);

			Session session = null;

			try {
				session = _basePersistenceImpl.openSession();

				Query query = session.createQuery(sql);

				_bindParameters(query, columnValues);

				List<T> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						_finderCache.putResult(
							_finderPathFetch, finderArgs, list);
					}
				}
				else {
					T model = list.get(0);

					result = model;

					_basePersistenceImpl.cacheResult(model);
				}
			}
			catch (Exception exception) {
				throw _basePersistenceImpl.processException(exception);
			}
			finally {
				_basePersistenceImpl.closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}

		return (T)result;
	}

	/**
	 * Returns a paginated, optionally ordered list of entities matching the
	 * given column values. Used for collection finders.
	 *
	 * @param  columnValues      the finder column values
	 * @param  start             the lower bound of the range
	 * @param  end               the upper bound of the range (not inclusive)
	 * @param  orderByComparator the comparator to order results (optionally
	 *         {@code null})
	 * @param  useFinderCache    whether to use the finder cache
	 * @return the list of matching entities
	 */
	@SuppressWarnings("unchecked")
	public List<T> findList(
		Object[] columnValues, int start, int end,
		OrderByComparator<T> orderByComparator, boolean useFinderCache) {

		_normalizeStringValues(columnValues);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				if (_finderPathWithoutPagination != null) {
					finderPath = _finderPathWithoutPagination;
					finderArgs = _toFinderArgs(columnValues);
				}
				else {
					finderPath = _finderPathWithPagination;
					finderArgs = _toFinderArgs(
						columnValues, start, end, orderByComparator);
				}
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPagination;
			finderArgs = _toFinderArgs(
				columnValues, start, end, orderByComparator);
		}

		List<T> list = null;

		if (useFinderCache) {
			list = (List<T>)_finderCache.getResult(
				finderPath, finderArgs, _basePersistenceImpl);

			if ((list != null) && !list.isEmpty()) {
				for (T model : list) {
					if (!_cacheValidator.test(columnValues, model)) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			String sql = _buildSelectSql(columnValues, orderByComparator);

			Session session = null;

			try {
				session = _basePersistenceImpl.openSession();

				Query query = session.createQuery(sql);

				_bindParameters(query, columnValues);

				list = (List<T>)QueryUtil.list(
					query, _basePersistenceImpl.getDialect(), start, end);

				_basePersistenceImpl.cacheResult(list);

				if (useFinderCache) {
					_finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw _basePersistenceImpl.processException(exception);
			}
			finally {
				_basePersistenceImpl.closeSession(session);
			}
		}

		return list;
	}

	public static class Builder<T extends BaseModel<T>> {

		public Builder(
			BasePersistenceImpl<T> basePersistenceImpl,
			FinderCache finderCache) {

			_basePersistenceImpl = basePersistenceImpl;
			_finderCache = finderCache;
		}

		public BaseFinder<T> build() {
			return new BaseFinder<>(this);
		}

		public Builder<T> cacheValidator(
			BiPredicate<Object[], T> cacheValidator) {

			_cacheValidator = cacheValidator;

			return this;
		}

		public Builder<T> columns(FinderColumn... columns) {
			_columns = columns;

			return this;
		}

		public Builder<T> defaultOrderByJpql(String defaultOrderByJpql) {
			_defaultOrderByJpql = defaultOrderByJpql;

			return this;
		}

		public Builder<T> finderPathCount(FinderPath finderPathCount) {
			_finderPathCount = finderPathCount;

			return this;
		}

		public Builder<T> finderPathFetch(FinderPath finderPathFetch) {
			_finderPathFetch = finderPathFetch;

			return this;
		}

		public Builder<T> finderPathWithoutPagination(
			FinderPath finderPathWithoutPagination) {

			_finderPathWithoutPagination = finderPathWithoutPagination;

			return this;
		}

		public Builder<T> finderPathWithPagination(
			FinderPath finderPathWithPagination) {

			_finderPathWithPagination = finderPathWithPagination;

			return this;
		}

		public Builder<T> orderByEntityAlias(String orderByEntityAlias) {
			_orderByEntityAlias = orderByEntityAlias;

			return this;
		}

		public Builder<T> sqlCountWhere(String sqlCountWhere) {
			_sqlCountWhere = sqlCountWhere;

			return this;
		}

		public Builder<T> sqlSelectWhere(String sqlSelectWhere) {
			_sqlSelectWhere = sqlSelectWhere;

			return this;
		}

		private BasePersistenceImpl<T> _basePersistenceImpl;
		private BiPredicate<Object[], T> _cacheValidator;
		private FinderColumn[] _columns;
		private String _defaultOrderByJpql;
		private FinderCache _finderCache;
		private FinderPath _finderPathCount;
		private FinderPath _finderPathFetch;
		private FinderPath _finderPathWithoutPagination;
		private FinderPath _finderPathWithPagination;
		private String _orderByEntityAlias;
		private String _sqlCountWhere;
		private String _sqlSelectWhere;

	}

	private BaseFinder(Builder<T> builder) {
		_basePersistenceImpl = builder._basePersistenceImpl;
		_finderCache = builder._finderCache;
		_columns = builder._columns;
		_sqlSelectWhere = builder._sqlSelectWhere;
		_sqlCountWhere = builder._sqlCountWhere;
		_defaultOrderByJpql = builder._defaultOrderByJpql;
		_orderByEntityAlias = builder._orderByEntityAlias;
		_finderPathWithPagination = builder._finderPathWithPagination;
		_finderPathWithoutPagination = builder._finderPathWithoutPagination;
		_finderPathFetch = builder._finderPathFetch;
		_finderPathCount = builder._finderPathCount;
		_cacheValidator = builder._cacheValidator;
	}

	private void _appendWhereClause(
		StringBundler sb, Object[] columnValues, boolean[] bindFlags) {

		for (int i = 0; i < _columns.length; i++) {
			bindFlags[i] = _columns[i].appendWhereClause(sb, columnValues[i]);
		}
	}

	private void _bindParameters(Query query, Object[] columnValues) {
		QueryPos queryPos = QueryPos.getInstance(query);

		boolean[] bindFlags = new boolean[_columns.length];

		for (int i = 0; i < _columns.length; i++) {
			bindFlags[i] = _columns[i].appendWhereClause(
				new StringBundler(), columnValues[i]);
		}

		for (int i = 0; i < _columns.length; i++) {
			if (bindFlags[i]) {
				_columns[i].bindValue(queryPos, columnValues[i]);
			}
		}
	}

	private String _buildCountSql(Object[] columnValues) {
		StringBundler sb = new StringBundler(_columns.length + 1);

		sb.append(_sqlCountWhere);

		boolean[] bindFlags = new boolean[_columns.length];

		_appendWhereClause(sb, columnValues, bindFlags);

		return sb.toString();
	}

	private String _buildSelectSql(
		Object[] columnValues, OrderByComparator<T> orderByComparator) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				_columns.length + 2 +
					(orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(_columns.length + 2);
		}

		sb.append(_sqlSelectWhere);

		boolean[] bindFlags = new boolean[_columns.length];

		_appendWhereClause(sb, columnValues, bindFlags);

		if (orderByComparator != null) {
			_basePersistenceImpl.appendOrderByComparator(
				sb, _orderByEntityAlias, orderByComparator);
		}
		else if (_defaultOrderByJpql != null) {
			sb.append(_defaultOrderByJpql);
		}

		return sb.toString();
	}

	private void _normalizeStringValues(Object[] columnValues) {
		for (int i = 0; i < _columns.length; i++) {
			if (_columns[i].isConvertNull()) {
				columnValues[i] = Objects.toString(columnValues[i], "");
			}
		}
	}

	private Object[] _toFinderArgs(Object[] columnValues) {
		Object[] finderArgs = new Object[columnValues.length];

		for (int i = 0; i < columnValues.length; i++) {
			if (columnValues[i] instanceof Date) {
				Date date = (Date)columnValues[i];

				finderArgs[i] = date.getTime();
			}
			else {
				finderArgs[i] = columnValues[i];
			}
		}

		return finderArgs;
	}

	private Object[] _toFinderArgs(
		Object[] columnValues, int start, int end,
		OrderByComparator<T> orderByComparator) {

		Object[] finderArgs = new Object[columnValues.length + 3];

		for (int i = 0; i < columnValues.length; i++) {
			if (columnValues[i] instanceof Date) {
				Date date = (Date)columnValues[i];

				finderArgs[i] = date.getTime();
			}
			else {
				finderArgs[i] = columnValues[i];
			}
		}

		finderArgs[columnValues.length] = start;
		finderArgs[columnValues.length + 1] = end;
		finderArgs[columnValues.length + 2] = orderByComparator;

		return finderArgs;
	}

	private final BasePersistenceImpl<T> _basePersistenceImpl;
	private final BiPredicate<Object[], T> _cacheValidator;
	private final FinderColumn[] _columns;
	private final String _defaultOrderByJpql;
	private final FinderCache _finderCache;
	private final FinderPath _finderPathCount;
	private final FinderPath _finderPathFetch;
	private final FinderPath _finderPathWithoutPagination;
	private final FinderPath _finderPathWithPagination;
	private final String _orderByEntityAlias;
	private final String _sqlCountWhere;
	private final String _sqlSelectWhere;

}