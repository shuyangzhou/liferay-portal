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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Bowerman
 */
public class VerifyResourceActions extends VerifyProcess {

	protected void deleteDuplicateBitwiseValuesOnResource() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			String selectSQL =
				"select name, actionId from ResourceAction where " +
					"bitwiseValue = " + Long.MIN_VALUE;

			String deleteSQL =
				"delete from ResourceAction where name = ? and actionId = ?";

			try (PreparedStatement ps1 = connection.prepareStatement(selectSQL);
				ResultSet rs = ps1.executeQuery();
				PreparedStatement ps2 =
					AutoBatchPreparedStatementUtil.concurrentAutoBatch(
						connection, deleteSQL)) {

				Set<String> names = new HashSet<>();

				while (rs.next()) {
					String name = rs.getString("name");

					if (names.contains(name)) {
						String actionId = rs.getString("actionId");

						if (_log.isInfoEnabled()) {
							StringBundler sb = new StringBundler(7);

							sb.append("Deleting resource action ");
							sb.append(actionId);
							sb.append(" from resource ");
							sb.append(name);
							sb.append(" because its bitwise value is the ");
							sb.append("same as another resource action on ");
							sb.append("the same resource");

							_log.info(sb.toString());
						}

						ps2.setString(1, name);
						ps2.setString(2, actionId);

						ps2.addBatch();
					}
					else {
						names.add(name);
					}

					ps2.executeBatch();
				}
			}
		}
	}

	@Override
	protected void doVerify() throws Exception {
		deleteDuplicateBitwiseValuesOnResource();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyResourceActions.class);

}