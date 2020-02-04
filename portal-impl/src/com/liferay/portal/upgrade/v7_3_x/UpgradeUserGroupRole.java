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
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Preston Crary
 */
public class UpgradeUserGroupRole extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		DatabaseMetaData databaseMetaData = connection.getMetaData();

		DBInspector dbInspector = new DBInspector(connection);

		String normalizedTableName = dbInspector.normalizeName(
			"UserGroupRole", databaseMetaData);

		try (ResultSet rs = databaseMetaData.getColumns(
				dbInspector.getCatalog(), dbInspector.getSchema(),
				normalizedTableName,
				dbInspector.normalizeName(
					"userGroupRoleId", databaseMetaData))) {

			if (rs.next()) {
				return;
			}
		}

		removePrimaryKey("UserGroupRole");

		runSQL(
			"alter table UserGroupRole add userGroupRoleId LONG default 0 " +
				"not null");

		runSQL(
			"update UserGroupRole set userGroupRoleId = (userId + groupId + " +
				"roleId)");

		long maxUserGroupRoleId = -1;

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select max(userGroupRoleId) from UserGroupRole");
			ResultSet resultSet = preparedStatement.executeQuery()) {

			if (resultSet.next()) {
				maxUserGroupRoleId = resultSet.getLong(1);
			}
		}

		if (maxUserGroupRoleId != -1) {
			maxUserGroupRoleId = _updateConflictingUserGroupRoles(
				"userId", maxUserGroupRoleId);

			maxUserGroupRoleId = _updateConflictingUserGroupRoles(
				"groupId", maxUserGroupRoleId);

			maxUserGroupRoleId = _updateConflictingUserGroupRoles(
				"roleId", maxUserGroupRoleId);

			runSQL(
				StringBundler.concat(
					"insert into Counter (name, currentId) values ('",
					UserGroupRole.class.getName(), "', ", maxUserGroupRoleId,
					")"));
		}

		runSQL(
			StringBundler.concat(
				"alter table ", normalizedTableName,
				" add primary key (userGroupRoleId)"));
	}

	private long _updateConflictingUserGroupRoles(
			String columnName, long maxUserGroupRoleId)
		throws SQLException {

		try (PreparedStatement selectPreparedStatement =
				connection.prepareStatement(
					StringBundler.concat(
						"select ugr1.userId, ugr1.groupId, ugr1.roleId from ",
						"UserGroupRole ugr1 inner join UserGroupRole ugr2 on ",
						"ugr1.userGroupRoleId = ugr2.userGroupRoleId and ugr1.",
						columnName, " != ugr2.", columnName));
			PreparedStatement updatePreparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection.prepareStatement(
						"update UserGroupRole set userGroupRoleId = ? where " +
							"userId = ? and groupId = ? and roleId = ?"));
			ResultSet resultSet = selectPreparedStatement.executeQuery()) {

			while (resultSet.next()) {
				updatePreparedStatement.setLong(1, ++maxUserGroupRoleId);
				updatePreparedStatement.setLong(2, resultSet.getLong(1));
				updatePreparedStatement.setLong(3, resultSet.getLong(2));
				updatePreparedStatement.setLong(4, resultSet.getLong(3));

				updatePreparedStatement.addBatch();
			}

			updatePreparedStatement.executeBatch();
		}

		return maxUserGroupRoleId;
	}

}