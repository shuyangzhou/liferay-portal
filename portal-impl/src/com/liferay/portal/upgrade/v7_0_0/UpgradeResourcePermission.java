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

package com.liferay.portal.upgrade.v7_0_0;

import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Sampsa Sohlman
 */
public class UpgradeResourcePermission extends UpgradeProcess {

	protected void createIndex() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			runSQLTemplateString(
				"create index IX_D5F1E2A2 on ResourcePermission " +
					"(name[$COLUMN_LENGTH:255$])",
				false, false);
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		createIndex();

		upgradeResourcePermissions();
	}

	protected void upgradeResourcePermissions() throws Exception {
		_upgradePrimKeyId();

		_upgradeViewActionId();
	}

	private List<String> _getNames() throws Exception {
		List<String> names = new ArrayList<>();

		String sql = "select distinct name from ResourcePermission";

		try (PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String name = rs.getString("name");

				names.add(name);
			}
		}

		return names;
	}

	private void _upgradePrimKeyId() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			List<String> names = _getNames();

			String updateSQL =
				"update ResourcePermission set primKeyId = " +
					"CAST_LONG(primKey) where name = ?";

			updateSQL = SQLTransformer.transform(updateSQL);

			Iterator<String> nameIterator = names.iterator();

			while (nameIterator.hasNext()) {
				String name = nameIterator.next();

				try (PreparedStatement ps =
						connection.prepareStatement(updateSQL)) {

					ps.setString(1, name);

					ps.executeUpdate();

					nameIterator.remove();
				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to update resource " + name + " in bulk: " +
								e.getMessage());
					}
				}
			}

			for (String name : names) {
				_upgradePrimKeyIdIndividual(name);
			}
		}
	}

	private void _upgradePrimKeyIdIndividual(String name) throws Exception {
		Map<Long, Long> updates = new HashMap<>();

		try (LoggingTimer loggingTimer = new LoggingTimer(name);
			PreparedStatement ps = connection.prepareStatement(
				"select resourcePermissionId, actionIds, primKey, primKeyId " +
					"from ResourcePermission where name = ? ")) {

			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long actionIds = rs.getLong("actionIds");

					String primKey = rs.getString("primKey");

					long newPrimKeyId = GetterUtil.getLong(primKey);

					long primKeyId = rs.getLong("primKeyId");

					if (primKeyId == newPrimKeyId) {
						continue;
					}

					if ((newPrimKeyId == 0) && !((actionIds % 2) == 1)) {
						continue;
					}

					updates.put(
						rs.getLong("resourcePermissionId"), newPrimKeyId);
				}
			}
		}

		try (LoggingTimer loggingTimer = new LoggingTimer(name);
			PreparedStatement ps =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection,
					"update ResourcePermission set primKeyId = ? where " +
						"resourcePermissionId = ?")) {

			for (Entry<Long, Long> entry : updates.entrySet()) {
				ps.setLong(1, entry.getValue());
				ps.setLong(2, entry.getKey());

				ps.addBatch();
			}

			ps.executeBatch();
		}
	}

	private void _upgradeViewActionId() throws Exception {
		String updateSQL =
			"update ResourcePermission set viewActionId = [$TRUE$] where " +
				"MOD(actionIds, 2) = 1";

		updateSQL = SQLTransformer.transform(updateSQL);

		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps = connection.prepareStatement(updateSQL)) {

			ps.executeUpdate();
		}

		updateSQL =
			"update ResourcePermission set viewActionId = [$FALSE$] where " +
				"MOD(actionIds, 2) = 0";

		updateSQL = SQLTransformer.transform(updateSQL);

		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps = connection.prepareStatement(updateSQL)) {

			ps.executeUpdate();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeResourcePermission.class);

}