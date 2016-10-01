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

package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergio González
 */
public class UpgradeLayoutFriendlyURL extends UpgradeProcess {

	protected void addLayoutFriendlyURL() throws Exception {
		_bulkAddLayoutFriendlyURL();

		_bulkFixLayoutFriendlyURLUuid();
	}

	protected void addLayoutFriendlyURL(
			long groupId, long companyId, long userId, String userName,
			Timestamp createDate, Timestamp modifiedDate, long plid,
			boolean privateLayout, String friendlyURL)
		throws Exception {

		StringBundler sb = new StringBundler(5);

		sb.append("insert into LayoutFriendlyURL (uuid_, ");
		sb.append("layoutFriendlyURLId, groupId, companyId, userId, ");
		sb.append("userName, createDate, modifiedDate, plid, privateLayout, ");
		sb.append("friendlyURL, languageId) values (?, ?, ?, ?, ?, ?, ?, ?, ");
		sb.append("?, ?, ?, ?)");

		try (PreparedStatement ps = connection.prepareStatement(
				sb.toString())) {

			ps.setString(1, PortalUUIDUtil.generate());
			ps.setLong(2, increment());
			ps.setLong(3, groupId);
			ps.setLong(4, companyId);
			ps.setLong(5, userId);
			ps.setString(6, userName);
			ps.setTimestamp(7, createDate);
			ps.setTimestamp(8, modifiedDate);
			ps.setLong(9, plid);
			ps.setBoolean(10, privateLayout);
			ps.setString(11, friendlyURL);
			ps.setString(
				12, UpgradeProcessUtil.getDefaultLanguageId(companyId));

			ps.executeUpdate();
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		addLayoutFriendlyURL();
	}

	private void _bulkAddLayoutFriendlyURL() throws Exception {
		StringBundler sb = new StringBundler(8);

		sb.append("insert into LayoutFriendlyURL (uuid_, ");
		sb.append("layoutFriendlyURLId, groupId, companyId, userId, ");
		sb.append("userName, createDate, modifiedDate, plid, privateLayout, ");
		sb.append("friendlyURL, languageId) (select uuid_, plid as ");
		sb.append("layoutFriendlyURLId, groupId, companyId, userId, ");
		sb.append("userName, createDate, modifiedDate, plid, privateLayout, ");
		sb.append("friendlyURL, ? as languageId from Layout where ");
		sb.append("companyId = ?)");

		List<Long> companyIds = _getCompanyIds();

		for (long companyId : companyIds) {
			try (LoggingTimer loggingTimer = new LoggingTimer();
				PreparedStatement ps = connection.prepareStatement(
					sb.toString())) {

				ps.setString(
					1, UpgradeProcessUtil.getDefaultLanguageId(companyId));

				ps.setLong(2, companyId);

				ps.executeUpdate();
			}
		}
	}

	private void _bulkFixLayoutFriendlyURLUuid() throws Exception {
		runSQL("create index TMP1 on LayoutFriendlyURL (uuid_, groupId)");
		runSQL("create index TMP2 on LayoutFriendlyURL (uuid_, privateLayout)");

		Set<String> uuids = new HashSet<>();

		String sql =
			"select uuid_, groupId, count(*) from LayoutFriendlyURL group by " +
				"uuid_, groupId having count(*) > 1";

		try (PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String uuid = rs.getString("uuid_");

				uuids.add(uuid);
			}
		}

		for (String uuid : uuids) {
			String newUuid = PortalUUIDUtil.generate();

			try (LoggingTimer loggingTimer = new LoggingTimer();
				PreparedStatement ps = connection.prepareStatement(
					"update LayoutFriendlyURL set uuid_ = ? where uuid_ = ? " +
						"and privateLayout = ?")) {

				ps.setString(1, newUuid);
				ps.setString(2, uuid);
				ps.setBoolean(3, false);

				ps.executeUpdate();
			}
		}

		runSQL("drop index TMP1 on LayoutFriendlyURL");
		runSQL("drop index TMP2 on LayoutFriendlyURL");
	}

	private List<Long> _getCompanyIds() throws Exception {
		List<Long> companyIds = new ArrayList<>();

		String sql = "select distinct companyId from Layout";

		try (PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long companyId = rs.getLong("companyId");

				companyIds.add(companyId);
			}
		}

		return companyIds;
	}

}