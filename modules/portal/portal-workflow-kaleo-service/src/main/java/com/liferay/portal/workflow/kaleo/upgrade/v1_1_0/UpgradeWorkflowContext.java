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

package com.liferay.portal.workflow.kaleo.upgrade.v1_1_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.workflow.kaleo.util.WorkflowContextUtil;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;

import org.jabsorb.JSONSerializer;

/**
 * @author Jang Kim
 */
public class UpgradeWorkflowContext extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (Connection con = DataAccess.getUpgradeOptimizedConnection()) {
			updateTable(con, "KaleoInstance", "kaleoInstanceId");
			updateTable(con, "KaleoLog", "kaleoLogId");
			updateTable(
				con, "KaleoTaskInstanceToken", "kaleoTaskInstanceTokenId");
		}
	}

	protected void updateTable(
			Connection con, String tableName, String fieldName)
		throws Exception {

		StringBundler sb = new StringBundler(6);

		sb.append("select ");
		sb.append(fieldName);
		sb.append(", workflowContext from ");
		sb.append(tableName);
		sb.append(" where workflowContext is not null and workflowContext ");
		sb.append("not like '%serializable%'");

		try (PreparedStatement ps = con.prepareStatement(sb.toString());
			ResultSet rs = ps.executeQuery()) {

			JSONSerializer jsonSerializer = new JSONSerializer();

			jsonSerializer.registerDefaultSerializers();

			while (rs.next()) {
				long fieldValue = rs.getLong(fieldName);
				String workflowContext = rs.getString("workflowContext");

				if (Validator.isNull(workflowContext)) {
					continue;
				}

				workflowContext = WorkflowContextUtil.convert(
					(Map<String, Serializable>)jsonSerializer.fromJSON(
						workflowContext));

				updateWorkflowContext(
					con, tableName, fieldName, fieldValue, workflowContext);
			}
		}
	}

	protected void updateWorkflowContext(
			Connection con, String tableName, String fieldName, long fieldValue,
			String workflowContext)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append("update ");
		sb.append(tableName);
		sb.append(" set workflowContext = ? where ");
		sb.append(fieldName);
		sb.append(" = ?");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			ps.setString(1, workflowContext);
			ps.setLong(2, fieldValue);

			ps.executeUpdate();
		}
	}

}