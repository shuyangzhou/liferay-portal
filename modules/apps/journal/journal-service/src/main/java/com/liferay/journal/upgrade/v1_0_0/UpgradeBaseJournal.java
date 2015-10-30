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

package com.liferay.journal.upgrade.v1_0_0;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.ResourcePermission;
import com.liferay.portal.util.PortalUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Eudaldo Alonso
 */
public abstract class UpgradeBaseJournal extends UpgradeProcess {

	protected void addResourcePermission(
			Connection con, long companyId, String className, long primKey,
			long roleId, long actionIds)
		throws Exception {

		StringBundler sb = new StringBundler(3);

		sb.append("insert into ResourcePermission (resourcePermissionId, ");
		sb.append("companyId, name, scope, primKey, roleId, ownerId, ");
		sb.append("actionIds) values (?, ?, ?, ?, ?, ?, ?, ?)");

		try (PreparedStatement ps = con.prepareStatement(sb.toString())) {
			long resourcePermissionId = increment(
				ResourcePermission.class.getName());

			ps.setLong(1, resourcePermissionId);
			ps.setLong(2, companyId);
			ps.setString(3, className);
			ps.setInt(4, ResourceConstants.SCOPE_INDIVIDUAL);
			ps.setLong(5, primKey);
			ps.setLong(6, roleId);
			ps.setLong(7, 0);
			ps.setLong(8, actionIds);

			ps.executeUpdate();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to add resource permission " + className, e);
			}
		}
	}

	protected long getBitwiseValue(
		Map<String, Long> bitwiseValues, List<String> actionIds) {

		long bitwiseValue = 0;

		for (String actionId : actionIds) {
			Long actionIdBitwiseValue = bitwiseValues.get(actionId);

			if (actionIdBitwiseValue == null) {
				continue;
			}

			bitwiseValue |= actionIdBitwiseValue;
		}

		return bitwiseValue;
	}

	protected Map<String, Long> getBitwiseValues(Connection con, String name)
		throws Exception {

		Map<String, Long> bitwiseValues = _bitwiseValues.get(name);

		if (bitwiseValues != null) {
			return bitwiseValues;
		}

		String sql =
			"select actionId, bitwiseValue from ResourceAction where name = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery()) {
				bitwiseValues = new HashMap<>();

				while (rs.next()) {
					String actionId = rs.getString("actionId");
					long bitwiseValue = rs.getLong("bitwiseValue");

					bitwiseValues.put(actionId, bitwiseValue);
				}
			}

			_bitwiseValues.put(name, bitwiseValues);

			return bitwiseValues;
		}
	}

	protected long getCompanyGroupId(Connection con, long companyId)
		throws Exception {

		String sql =
			"select groupId from Group_ where classNameId = ? and classPK = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, PortalUtil.getClassNameId(Company.class.getName()));
			ps.setLong(2, companyId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("groupId");
				}
			}

			return 0;
		}
	}

	protected long getDefaultUserId(Connection con, long companyId)
		throws Exception {

		String sql =
			"select userId from User_ where companyId = ? and " +
				"defaultUser = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, companyId);
			ps.setBoolean(2, true);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("userId");
				}
			}

			return 0;
		}
	}

	protected long getRoleId(Connection con, long companyId, String name)
		throws Exception {

		String roleIdsKey = companyId + StringPool.POUND + name;

		Long roleId = _roleIds.get(roleIdsKey);

		if (roleId != null) {
			return roleId;
		}

		String sql =
			"select roleId from Role_ where companyId = ? and name = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, companyId);
			ps.setString(2, name);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					roleId = rs.getLong("roleId");
				}
			}

			_roleIds.put(roleIdsKey, roleId);

			return roleId;
		}
	}

	protected String localize(
			long groupId, String key, String defaultLanguageId)
		throws Exception {

		Map<Locale, String> localizationMap = new HashMap<>();

		for (Locale locale : LanguageUtil.getAvailableLocales(groupId)) {
			localizationMap.put(locale, LanguageUtil.get(locale, key));
		}

		return LocalizationUtil.updateLocalization(
			localizationMap, StringPool.BLANK, key, defaultLanguageId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeBaseJournal.class);

	private final Map<String, Map<String, Long>> _bitwiseValues =
		new HashMap<>();
	private final Map<String, Long> _roleIds = new HashMap<>();

}