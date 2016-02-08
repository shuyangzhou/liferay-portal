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

import com.liferay.portal.kernel.dao.orm.WildcardMode;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.upgrade.AutoBatchPreparedStatementUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Preston Crary
 */
public class UpgradeKernelPackage extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws SQLException {
		upgradeTable("Counter", "name", _CLASS_NAMES, WildcardMode.SURROUND);
		upgradeTable(
			"ClassName_", "value", _CLASS_NAMES, WildcardMode.SURROUND);
		upgradeTable(
			"ResourceBlock", "name", _CLASS_NAMES, WildcardMode.SURROUND);
		upgradeTable(
			"ResourcePermission", "name", _CLASS_NAMES, WildcardMode.SURROUND);

		upgradeTable(
			"ResourceBlock", "name", _RESOURCE_NAMES, WildcardMode.LEADING);
		upgradeTable(
			"ResourcePermission", "name", _RESOURCE_NAMES,
			WildcardMode.LEADING);
	}

	protected void upgradeTable(
			String tableName, String columnName, String[][] names,
			WildcardMode wildcardMode)
		throws SQLException {

		StringBundler updateSB = new StringBundler(7);

		updateSB.append("update ");
		updateSB.append(tableName);
		updateSB.append(" set ");
		updateSB.append(columnName);
		updateSB.append(" = ? where ");
		updateSB.append(columnName);
		updateSB.append(" = ?");

		String updateSQL = updateSB.toString();

		for (String[] name : names) {
			StringBundler selectSB = new StringBundler(11);

			selectSB.append("select ");
			selectSB.append(columnName);
			selectSB.append(" from ");
			selectSB.append(tableName);
			selectSB.append(" where ");
			selectSB.append(columnName);
			selectSB.append(" like '");

			if (wildcardMode.equals(WildcardMode.LEADING) ||
				wildcardMode.equals(WildcardMode.SURROUND)) {

				selectSB.append(StringPool.PERCENT);
			}

			selectSB.append(name[0]);

			if (wildcardMode.equals(WildcardMode.SURROUND) ||
				wildcardMode.equals(WildcardMode.TRAILING)) {

				selectSB.append(StringPool.PERCENT);
			}

			selectSB.append(StringPool.APOSTROPHE);

			upgradeTable(columnName, selectSB.toString(), updateSQL, name);
		}
	}

	protected void upgradeTable(
			String columnName, String selectSQL, String updateSQL,
			String[] name)
		throws SQLException {

		try (PreparedStatement ps1 = connection.prepareStatement(selectSQL);
			ResultSet rs = ps1.executeQuery();
			PreparedStatement ps2 = AutoBatchPreparedStatementUtil.autoBatch(
				connection.prepareStatement(updateSQL))) {

			while (rs.next()) {
				String oldValue = rs.getString(columnName);

				String newValue = StringUtil.replace(
					oldValue, name[0], name[1]);

				ps2.setString(1, newValue);
				ps2.setString(2, oldValue);

				ps2.addBatch();
			}

			ps2.executeBatch();
		}
	}

	private static final String[][] _CLASS_NAMES = new String[][] {
		{
			"com.liferay.portlet.announcements.model.AnnouncementsDelivery",
			"com.liferay.announcements.kernel.model.AnnouncementsDelivery"
		},
		{
			"com.liferay.portlet.announcements.model.AnnouncementsEntry",
			"com.liferay.announcements.kernel.model.AnnouncementsEntry"
		},
		{
			"com.liferay.portlet.announcements.model.AnnouncementsFlag",
			"com.liferay.announcements.kernel.model.AnnouncementsFlag"
		}
	};

	private static final String[][] _RESOURCE_NAMES = new String[][] {
		{
			"com.liferay.portlet.asset", "com.liferay.asset"
		},
		{
			"com.liferay.portlet.blogs", "com.liferay.blogs"
		},
		{
			"com.liferay.portlet.bookmarks", "com.liferay.bookmarks"
		},
		{
			"com.liferay.portlet.calendar", "com.liferay.calendar"
		},
		{
			"com.liferay.portlet.documentlibrary",
			"com.liferay.document.library"
		},
		{
			"com.liferay.portlet.dynamicdatalists",
			"com.liferay.dynamic.data.lists"
		},
		{
			"com.liferay.portlet.dynamicdatamapping",
			"com.liferay.dynamic.data.mapping"
		},
		{
			"com.liferay.portlet.journal", "com.liferay.journal"
		},
		{
			"com.liferay.portlet.messageboards", "com.liferay.message.boards"
		},
		{
			"com.liferay.portlet.polls", "com.liferay.polls"
		},
		{
			"com.liferay.portlet.shopping", "com.liferay.shopping"
		},
		{
			"com.liferay.portlet.wiki", "com.liferay.wiki"
		}
	};

}