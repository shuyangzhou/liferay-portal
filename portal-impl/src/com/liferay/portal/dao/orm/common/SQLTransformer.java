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

package com.liferay.portal.dao.orm.common;

import com.liferay.portal.dao.sql.transformer.SQLTransformerFactory;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Manuel de la Peña
 */
public class SQLTransformer {

	public static void reloadSQLTransformer() {
		_instance._reloadSQLTransformer();
	}

	public static String transform(String sql) {
		com.liferay.portal.dao.sql.transformer.SQLTransformer sqlTransformer =
			_instance._getSQLTransformer();

		return sqlTransformer.transform(sql);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #transform(String)}
	 */
	@Deprecated
	public static String transformFromHqlToJpql(String sql) {
		return transform(sql);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #transform(String)}
	 */
	@Deprecated
	public static String transformFromJpqlToHql(String sql) {
		return transform(sql);
	}

	private SQLTransformer() {
		_reloadSQLTransformer();
	}

	private com.liferay.portal.dao.sql.transformer.SQLTransformer
		_getSQLTransformer() {

		return _sqlTransformer;
	}

	private void _reloadSQLTransformer() {
		if (_transformedSqls == null) {
			_transformedSqls = new ConcurrentHashMap<>();
		}
		else {
			_transformedSqls.clear();
		}

		DB db = DBManagerUtil.getDB();

		_sqlTransformer = SQLTransformerFactory.getSQLTransformer(db);
	}

	private static final SQLTransformer _instance = new SQLTransformer();

	private com.liferay.portal.dao.sql.transformer.SQLTransformer
		_sqlTransformer;
	private Map<String, String> _transformedSqls;

}