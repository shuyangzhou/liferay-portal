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

package com.liferay.portal.upgrade.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.IOException;

import java.sql.SQLException;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class UpgradeCompanyId extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		for (TableUpdater tableUpdater : getTableUpdaters()) {
			if (tableHasColumn(tableUpdater.getTableName(), "companyId")) {
				if (_log.isInfoEnabled()) {
					_log.info("Skipping table " + tableUpdater.getTableName());
				}

				continue;
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"Adding column companyId to table " +
						tableUpdater.getTableName());
			}

			runSQL(
				"alter table " + tableUpdater.getTableName() +
					" add companyId LONG");

			tableUpdater.update();
		}
	}

	protected abstract TableUpdater[] getTableUpdaters();

	protected class TableUpdater {

		public TableUpdater(
			String tableName, String foreignTableName,
			String foreignColumnName) {

			_tableName = tableName;
			_foreignTableName = foreignTableName;
			_foreignColumnName = foreignColumnName;
		}

		public String getForeignColumnName() {
			return _foreignColumnName;
		}

		public String getForeignTableName() {
			return _foreignTableName;
		}

		public String getTableName() {
			return _tableName;
		}

		public String getUpdateSQL() {
			StringBundler sb = new StringBundler();

			sb.append("update ");
			sb.append(getTableName());
			sb.append(" set companyId = (select companyId from ");
			sb.append(getForeignTableName());
			sb.append(" where ");
			sb.append(getForeignTableName());
			sb.append(".");
			sb.append(getForeignColumnName());
			sb.append(" = ");
			sb.append(_tableName);
			sb.append(".");
			sb.append(getForeignColumnName());
			sb.append(")");

			return sb.toString();
		}

		public void update() throws IOException, SQLException {
			runSQL(getUpdateSQL());
		}

		private final String _foreignColumnName;
		private final String _foreignTableName;
		private final String _tableName;

	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeCompanyId.class);

}