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

package com.liferay.portal.workflow.kaleo.internal.upgrade.v1_4_1;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.upgrade.AutoBatchPreparedStatementUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Inácio Nery
 */
public class UpgradeKaleoDefinitionVersion extends UpgradeProcess {

	protected void addBatch(
			PreparedStatement ps, long kaleoDefinitionId,
			long kaleoDefinitionVersionId)
		throws SQLException {

		ps.setLong(1, kaleoDefinitionVersionId);
		ps.setLong(2, kaleoDefinitionId);

		ps.addBatch();
	}

	@Override
	protected void doUpgrade() throws Exception {
		upgradeKaleoDefinitionVersion();

		removeKaleoDefinitionId();
		removeStartKaleoNodeId();
	}

	protected String getVersion(int version) {
		return version + StringPool.PERIOD + 0;
	}

	protected void removeKaleoDefinitionId() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			if (hasColumn("KaleoAction", "kaleoDefinitionId")) {
				runSQL("drop index IX_F95A622 on KaleoAction");

				runSQL("alter table KaleoAction drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoCondition", "kaleoDefinitionId")) {
				runSQL("drop index IX_DC978A5D on KaleoCondition");

				runSQL(
					"alter table KaleoCondition drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoInstance", "kaleoDefinitionId")) {
				runSQL("drop index IX_ACF16238 on KaleoInstance");

				runSQL(
					"alter table KaleoInstance drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoInstanceToken", "kaleoDefinitionId")) {
				runSQL("drop index IX_7BDB04B4 on KaleoInstanceToken");

				runSQL(
					"alter table KaleoInstanceToken drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoLog", "kaleoDefinitionId")) {
				runSQL("drop index IX_6C64B7D4 on KaleoLog");

				runSQL("alter table KaleoLog drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoNode", "kaleoDefinitionId")) {
				runSQL("drop index IX_F28C443E on KaleoNode");
				runSQL("drop index IX_32E94DD6 on KaleoNode");

				runSQL("alter table KaleoNode drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoNotification", "kaleoDefinitionId")) {
				runSQL("drop index IX_4B968E8D on KaleoNotification");

				runSQL(
					"alter table KaleoNotification drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoNotificationRecipient", "kaleoDefinitionId")) {
				runSQL("drop index IX_AA6697EA on KaleoNotificationRecipient");

				runSQL(
					"alter table KaleoNotificationRecipient drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTask", "kaleoDefinitionId")) {
				runSQL("drop index IX_3FFA633 on KaleoTask");

				runSQL("alter table KaleoTask drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoTaskAssignment", "kaleoDefinitionId")) {
				runSQL("drop index IX_575C03A6 on KaleoTaskAssignment");

				runSQL(
					"alter table KaleoTaskAssignment drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTaskAssignmentInstance", "kaleoDefinitionId")) {
				runSQL("drop index IX_C851011 on KaleoTaskAssignmentInstance");

				runSQL(
					"alter table KaleoTaskAssignmentInstance drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTaskForm", "kaleoDefinitionId")) {
				runSQL("drop index IX_60D1964F on KaleoTaskForm");

				runSQL(
					"alter table KaleoTaskForm drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoTaskFormInstance", "kaleoDefinitionId")) {
				runSQL("drop index IX_B975E9BA on KaleoTaskFormInstance");

				runSQL(
					"alter table KaleoTaskFormInstance drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTaskInstanceToken", "kaleoDefinitionId")) {
				runSQL("drop index IX_608E9519 on KaleoTaskInstanceToken");

				runSQL(
					"alter table KaleoTaskInstanceToken drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTimer", "kaleoDefinitionId")) {
				runSQL("alter table KaleoTimer drop column kaleoDefinitionId");
			}

			if (hasColumn("KaleoTimerInstanceToken", "kaleoDefinitionId")) {
				runSQL(
					"alter table KaleoTimerInstanceToken drop column " +
						"kaleoDefinitionId");
			}

			if (hasColumn("KaleoTransition", "kaleoDefinitionId")) {
				runSQL("drop index IX_479F3063 on KaleoTransition");

				runSQL(
					"alter table KaleoTransition drop column " +
						"kaleoDefinitionId");
			}
		}
	}

	protected void removeStartKaleoNodeId() throws Exception {
		if (hasColumn("KaleoDefinition", "startKaleoNodeId")) {
			runSQL("alter table KaleoDefinition drop column startKaleoNodeId");
		}
	}

	protected void upgradeKaleoDefinitionVersion() throws SQLException {
		StringBundler sb1 = new StringBundler(3);

		sb1.append("select * from KaleoDefinition kd where not exists ");
		sb1.append("(select 1 from KaleoDefinitionVersion kdv where ");
		sb1.append("kdv.name = kd.name and kdv.companyId = kd.companyId)");

		StringBundler sb2 = new StringBundler(6);

		sb2.append("insert into KaleoDefinitionVersion ");
		sb2.append("(kaleoDefinitionVersionId, groupId, companyId, userId, ");
		sb2.append("userName, statusByUserId, statusByUserName, statusDate, ");
		sb2.append("createDate, modifiedDate, name, title, description, ");
		sb2.append("content, version, startKaleoNodeId, status) values (?, ");
		sb2.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps1 = connection.prepareStatement(sb1.toString());
			PreparedStatement ps2 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, sb2.toString());
			PreparedStatement ps3 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoAction set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			PreparedStatement ps4 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoCondition set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			PreparedStatement ps5 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoInstance set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			PreparedStatement ps6 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoInstanceToken set kaleoDefinitionVersionId " +
						"= ? where kaleoDefinitionId = ?");
			PreparedStatement ps7 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoLog set kaleoDefinitionVersionId = ? where " +
						"kaleoDefinitionId = ?");
			PreparedStatement ps8 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoNode set kaleoDefinitionVersionId = ? where " +
						"kaleoDefinitionId = ?");
			PreparedStatement ps9 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoNotification set kaleoDefinitionVersionId = " +
						"? where kaleoDefinitionId = ?");
			PreparedStatement ps10 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoNotificationRecipient set " +
						"kaleoDefinitionVersionId = ? where " +
							"kaleoDefinitionId = ?");
			PreparedStatement ps11 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTask set kaleoDefinitionVersionId = ? where " +
						"kaleoDefinitionId = ?");
			PreparedStatement ps12 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTaskAssignment set kaleoDefinitionVersionId " +
						"= ? where kaleoDefinitionId = ?");
			PreparedStatement ps13 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTaskAssignmentInstance set " +
						"kaleoDefinitionVersionId = ? where " +
							"kaleoDefinitionId = ?");
			PreparedStatement ps14 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTaskForm set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			PreparedStatement ps15 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTaskFormInstance set " +
						"kaleoDefinitionVersionId = ? where kaleoDefinitionId" +
							"= ?");
			PreparedStatement ps16 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTaskInstanceToken set " +
						"kaleoDefinitionVersionId = ? where kaleoDefinitionId" +
							"= ?");
			PreparedStatement ps17 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTimer set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			PreparedStatement ps18 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTimerInstanceToken set " +
						"kaleoDefinitionVersionId = ? where kaleoDefinitionId" +
							"= ?");
			PreparedStatement ps19 =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update KaleoTransition set kaleoDefinitionVersionId = ? " +
						"where kaleoDefinitionId = ?");
			ResultSet rs = ps1.executeQuery()) {

			while (rs.next()) {
				long kaleoDefinitionId = rs.getLong("kaleoDefinitionId");
				long groupId = rs.getLong("groupId");
				long companyId = rs.getLong("companyId");
				long userId = rs.getLong("userId");
				String userName = rs.getString("userName");
				Timestamp createDate = rs.getTimestamp("createDate");
				Timestamp modifiedDate = rs.getTimestamp("modifiedDate");
				String name = rs.getString("name");
				String title = rs.getString("title");
				String description = rs.getString("description");
				String content = rs.getString("content");
				int version = rs.getInt("version");
				long startKaleoNodeId = rs.getLong("startKaleoNodeId");

				long kaleoDefinitionVersionId = increment();

				ps2.setLong(1, kaleoDefinitionVersionId);

				ps2.setLong(2, groupId);
				ps2.setLong(3, companyId);
				ps2.setLong(4, userId);
				ps2.setString(5, userName);
				ps2.setLong(6, userId);
				ps2.setString(7, userName);
				ps2.setTimestamp(8, modifiedDate);
				ps2.setTimestamp(9, createDate);
				ps2.setTimestamp(10, modifiedDate);
				ps2.setString(11, name);
				ps2.setString(12, title);
				ps2.setString(13, description);
				ps2.setString(14, content);
				ps2.setString(15, getVersion(version));
				ps2.setLong(16, startKaleoNodeId);
				ps2.setInt(17, WorkflowConstants.STATUS_APPROVED);

				ps2.addBatch();

				addBatch(ps3, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps4, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps5, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps6, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps7, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps8, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps9, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps10, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps11, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps12, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps13, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps14, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps15, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps16, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps17, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps18, kaleoDefinitionId, kaleoDefinitionVersionId);
				addBatch(ps19, kaleoDefinitionId, kaleoDefinitionVersionId);
			}

			ps2.executeBatch();
			ps3.executeBatch();
			ps4.executeBatch();
			ps5.executeBatch();
			ps6.executeBatch();
			ps7.executeBatch();
			ps8.executeBatch();
			ps9.executeBatch();
			ps10.executeBatch();
			ps11.executeBatch();
			ps12.executeBatch();
			ps13.executeBatch();
			ps14.executeBatch();
			ps15.executeBatch();
			ps16.executeBatch();
			ps17.executeBatch();
			ps18.executeBatch();
			ps19.executeBatch();
		}
	}

}