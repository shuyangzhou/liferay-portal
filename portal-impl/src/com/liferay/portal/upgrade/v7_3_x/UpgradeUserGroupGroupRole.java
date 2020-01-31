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

package com.liferay.portal.upgrade.v7_3_x;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.model.UserGroupGroupRole;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Preston Crary
 */
public class UpgradeUserGroupGroupRole extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		DatabaseMetaData databaseMetaData = connection.getMetaData();

		DBInspector dbInspector = new DBInspector(connection);

		String normalizedTableName = dbInspector.normalizeName(
			"UserGroupGroupRole", databaseMetaData);

		try (ResultSet rs = databaseMetaData.getColumns(
				dbInspector.getCatalog(), dbInspector.getSchema(),
				normalizedTableName,
				dbInspector.normalizeName(
					"userGroupGroupRoleId", databaseMetaData))) {

			if (rs.next()) {
				return;
			}
		}

		removePrimaryKey("UserGroupGroupRole");

		runSQL(
			"alter table UserGroupGroupRole add userGroupGroupRoleId LONG " +
				"default 0 not null");

		runSQL(
			"update UserGroupGroupRole set userGroupGroupRoleId = " +
				"(userGroupId + groupId + roleId)");

		long maxUserGroupGroupRoleId = -1;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select max(userGroupGroupRoleId) from UserGroupGroupRole");
			ResultSet resultSet = preparedStatement.executeQuery()) {

			if (resultSet.next()) {
				maxUserGroupGroupRoleId = resultSet.getLong(1);
			}
		}

		if (maxUserGroupGroupRoleId != -1) {
			maxUserGroupGroupRoleId = _updateConflictingUserGroupGroupRoles(
				"userGroupId", maxUserGroupGroupRoleId);

			maxUserGroupGroupRoleId = _updateConflictingUserGroupGroupRoles(
				"groupId", maxUserGroupGroupRoleId);

			maxUserGroupGroupRoleId = _updateConflictingUserGroupGroupRoles(
				"roleId", maxUserGroupGroupRoleId);

			runSQL(
				StringBundler.concat(
					"insert into Counter (name, currentId) values ('",
					UserGroupGroupRole.class.getName(), "', ",
					maxUserGroupGroupRoleId, ")"));
		}

		runSQL(
			StringBundler.concat(
				"alter table ", normalizedTableName,
				" add primary key (userGroupGroupRoleId)"));
	}

	private long _updateConflictingUserGroupGroupRoles(
			String columnName, long maxUserGroupGroupRoleId)
		throws SQLException {

		try (PreparedStatement selectPreparedStatement =
				connection.prepareStatement(
					StringBundler.concat(
						"select uggr1.userGroupId, uggr1.groupId, ",
						"uggr1.roleId from UserGroupGroupRole uggr1 inner ",
						"join UserGroupGroupRole uggr2 on ",
						"uggr1.userGroupGroupRoleId = ",
						"uggr2.userGroupGroupRoleId and uggr1.", columnName,
						" != uggr2.", columnName));
			PreparedStatement updatePreparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection.prepareStatement(
						"update UserGroupGroupRole set userGroupGroupRoleId " +
							"= ? where userGroupId = ? and groupId = ? and " +
								"roleId = ?"));
			ResultSet resultSet = selectPreparedStatement.executeQuery()) {

			while (resultSet.next()) {
				updatePreparedStatement.setLong(1, ++maxUserGroupGroupRoleId);
				updatePreparedStatement.setLong(2, resultSet.getLong(1));
				updatePreparedStatement.setLong(3, resultSet.getLong(2));
				updatePreparedStatement.setLong(4, resultSet.getLong(3));

				updatePreparedStatement.addBatch();
			}

			updatePreparedStatement.executeBatch();
		}

		return maxUserGroupGroupRoleId;
	}

}