/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.dao.orm;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * Represents a single column in a Service Builder finder's WHERE clause. Each
 * FinderColumn holds the SQL fragments for different value conditions (normal
 * value, null value, empty string value) and knows how to append the correct
 * fragment to a query and bind parameters.
 *
 * @author Liferay
 */
public class FinderColumn {

	/**
	 * Creates a FinderColumn for a primitive type column. Primitive columns
	 * always use the SQL fragment with a ? placeholder and always bind.
	 *
	 * @param sqlFragment the SQL fragment with ? placeholder (e.g.,
	 *        "entity.columnName = ?")
	 */
	public FinderColumn(String sqlFragment) {
		_sqlFragment = sqlFragment;
		_nullSqlFragment = null;
		_emptySqlFragment = null;
		_caseInsensitive = false;
	}

	/**
	 * Creates a FinderColumn for a ConvertNull String column. Null values are
	 * normalized to empty string before processing. Empty strings use the
	 * empty SQL fragment (no binding); non-empty strings use the normal SQL
	 * fragment (with binding).
	 *
	 * @param sqlFragment      the SQL fragment with ? placeholder
	 * @param emptySqlFragment the SQL fragment for empty/null strings (e.g.,
	 *        "(entity.col IS NULL OR entity.col = '')")
	 */
	public FinderColumn(String sqlFragment, String emptySqlFragment) {
		_sqlFragment = sqlFragment;
		_nullSqlFragment = null;
		_emptySqlFragment = emptySqlFragment;
		_caseInsensitive = false;
	}

	/**
	 * Creates a FinderColumn with full control over all SQL fragments and
	 * binding behavior.
	 *
	 * @param sqlFragment      the SQL fragment with ? placeholder
	 * @param nullSqlFragment  the SQL fragment for null values (e.g.,
	 *        "entity.col IS NULL"), or {@code null} if null is not a valid
	 *        state (primitives or ConvertNull strings)
	 * @param emptySqlFragment the SQL fragment for empty strings (e.g.,
	 *        "(entity.col IS NULL OR entity.col = '')"), or {@code null} for
	 *        non-String columns
	 * @param caseInsensitive  whether to lowercase String values before binding
	 */
	public FinderColumn(
		String sqlFragment, String nullSqlFragment, String emptySqlFragment,
		boolean caseInsensitive) {

		_sqlFragment = sqlFragment;
		_nullSqlFragment = nullSqlFragment;
		_emptySqlFragment = emptySqlFragment;
		_caseInsensitive = caseInsensitive;
	}

	/**
	 * Appends this column's WHERE clause fragment to the StringBundler based
	 * on the given value.
	 *
	 * @param  sb    the StringBundler to append to
	 * @param  value the column value (may be null for nullable columns)
	 * @return {@code true} if the value should be bound via QueryPos,
	 *         {@code false} if the SQL fragment handles the condition inline
	 *         (null or empty string cases)
	 */
	public boolean appendWhereClause(StringBundler sb, Object value) {
		if (value == null) {
			if (_nullSqlFragment != null) {
				sb.append(_nullSqlFragment);

				return false;
			}

			sb.append(_sqlFragment);

			return true;
		}

		if ((_emptySqlFragment != null) && (value instanceof String) &&
			((String)value).isEmpty()) {

			sb.append(_emptySqlFragment);

			return false;
		}

		sb.append(_sqlFragment);

		return true;
	}

	/**
	 * Binds this column's value to the given QueryPos. Handles
	 * case-insensitive String lowercasing automatically.
	 *
	 * @param queryPos the QueryPos to bind to
	 * @param value    the value to bind
	 */
	public void bindValue(QueryPos queryPos, Object value) {
		if (_caseInsensitive && (value instanceof String)) {
			queryPos.add(StringUtil.toLowerCase((String)value));
		}
		else {
			queryPos.add(value);
		}
	}

	/**
	 * Returns {@code true} if this column normalizes null values to empty
	 * string. This is the case for ConvertNull String columns where
	 * {@code emptySqlFragment} is set but {@code nullSqlFragment} is not.
	 */
	public boolean isConvertNull() {
		if ((_emptySqlFragment != null) && (_nullSqlFragment == null)) {
			return true;
		}

		return false;
	}

	private final boolean _caseInsensitive;
	private final String _emptySqlFragment;
	private final String _nullSqlFragment;
	private final String _sqlFragment;

}