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

package com.liferay.portal.kernel.upgrade;

import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

/**
 * @author Jorge Ferrer
 * @author Brian Wing Shun Chan
 */
public abstract class BaseUpgradePortletPreferences extends UpgradeProcess {

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	protected void deletePortletPreferences(long portletPreferencesId)
		throws Exception {

		runSQL(
			"delete from PortletPreferences where portletPreferencesId = " +
				portletPreferencesId);
	}

	@Override
	protected void doUpgrade() throws Exception {
		updatePortletPreferences();
	}

	protected long getCompanyId(String sql, long primaryKey) throws Exception {
		long companyId = 0;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement(sql);

			ps.setLong(1, primaryKey);

			rs = ps.executeQuery();

			while (rs.next()) {
				companyId = rs.getLong("companyId");
			}
		}
		finally {
			DataAccess.cleanUp(ps, rs);
		}

		return companyId;
	}

	protected Object[] getGroup(long groupId) throws Exception {
		Object[] group = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement(
				"select companyId from Group_ where groupId = ?");

			ps.setLong(1, groupId);

			rs = ps.executeQuery();

			while (rs.next()) {
				long companyId = rs.getLong("companyId");

				group = new Object[] {groupId, companyId};
			}
		}
		finally {
			DataAccess.cleanUp(ps, rs);
		}

		return group;
	}

	protected Object[] getLayout(long plid) throws Exception {
		Object[] layout = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement(
				"select groupId, companyId, privateLayout, layoutId from " +
					"Layout where plid = ?");

			ps.setLong(1, plid);

			rs = ps.executeQuery();

			while (rs.next()) {
				long groupId = rs.getLong("groupId");
				long companyId = rs.getLong("companyId");
				boolean privateLayout = rs.getBoolean("privateLayout");
				long layoutId = rs.getLong("layoutId");

				layout =
					new Object[] {groupId, companyId, privateLayout, layoutId};
			}
		}
		finally {
			DataAccess.cleanUp(ps, rs);
		}

		if (layout == null) {
			layout = getLayoutRevision(plid);
		}

		return layout;
	}

	protected Object[] getLayoutRevision(long layoutRevisionId)
		throws Exception {

		Object[] layoutRevision = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement(
				"select groupId, companyId, privateLayout, layoutRevisionId " +
					"from LayoutRevision where layoutRevisionId = ?");

			ps.setLong(1, layoutRevisionId);

			rs = ps.executeQuery();

			while (rs.next()) {
				long groupId = rs.getLong("groupId");
				long companyId = rs.getLong("companyId");
				boolean privateLayout = rs.getBoolean("privateLayout");
				long layoutId = rs.getLong("layoutRevisionId");

				layoutRevision =
					new Object[] {groupId, companyId, privateLayout, layoutId};
			}
		}
		finally {
			DataAccess.cleanUp(ps, rs);
		}

		return layoutRevision;
	}

	protected String getLayoutUuid(long plid, long layoutId) throws Exception {
		Object[] layout = getLayout(plid);

		if (layout == null) {
			return null;
		}

		String uuid = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement(
				"select uuid_ from Layout where groupId = ? and " +
					"privateLayout = ? and layoutId = ?");

			long groupId = (Long)layout[0];
			boolean privateLayout = (Boolean)layout[2];

			ps.setLong(1, groupId);
			ps.setBoolean(2, privateLayout);
			ps.setLong(3, layoutId);

			rs = ps.executeQuery();

			if (rs.next()) {
				uuid = rs.getString("uuid_");
			}
		}
		finally {
			DataAccess.cleanUp(ps, rs);
		}

		return uuid;
	}

	protected String[] getPortletIds() {
		return new String[0];
	}

	protected String getUpdatePortletPreferencesWhereClause() {
		String[] portletIds = getPortletIds();

		if (portletIds.length == 0) {
			throw new IllegalArgumentException(
				"Subclasses must override getPortletIds or " +
					"getUpdatePortletPreferencesWhereClause");
		}

		StringBundler sb = new StringBundler(portletIds.length * 5 + 3);

		sb.append(StringPool.OPEN_PARENTHESIS);

		for (int i = 0; i < portletIds.length; i++) {
			String portletId = portletIds[i];

			sb.append("PortletPreferences.portletId ");

			if (portletId.contains(StringPool.PERCENT)) {
				sb.append(" like '");
				sb.append(portletId);
				sb.append("'");
			}
			else {
				sb.append(" = '");
				sb.append(portletId);
				sb.append("'");
			}

			if ((i + 1) < portletIds.length) {
				sb.append(" or ");
			}
		}

		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	protected void updatePortletPreferences() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			try (PreparedStatement ps1 =
					AutoBatchPreparedStatementUtil.concurrentAutoBatch(
						connection,
						"update PortletPreferences set preferences = ? where " +
							"portletPreferencesId = ?");
				PreparedStatement ps2 =
					AutoBatchPreparedStatementUtil.concurrentAutoBatch(
						connection,
						"delete from PortletPreferences where " +
							"portletPreferencesId = ?")) {

				StringBundler sb = new StringBundler(7);

				sb.append(_SELECT);
				sb.append(", PortletItem.companyId from PortletPreferences ");
				sb.append(
					"inner join PortletItem on PortletItem.portletItemId");
				sb.append("= PortletPreferences.ownerId");

				String whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_ARCHIVED);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, false);
					}
				}

				sb = new StringBundler(5);

				sb.append(_SELECT);
				sb.append(" from PortletPreferences ");

				whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_COMPANY);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, true);
					}
				}

				sb = new StringBundler(7);

				sb.append(_SELECT);
				sb.append(", Group_.companyId from PortletPreferences ");
				sb.append("inner join Group_ on Group_.groupId");
				sb.append("= PortletPreferences.ownerId");

				whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_GROUP);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, false);
					}
				}

				sb = new StringBundler(7);

				sb.append(_SELECT);
				sb.append(", Layout.companyId from PortletPreferences ");
				sb.append("inner join Layout on Layout.plid");
				sb.append("= PortletPreferences.plid");

				whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_LAYOUT);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, false);
					}
				}

				sb = new StringBundler(7);

				sb.append(_SELECT);
				sb.append(", Organization_.companyId from PortletPreferences ");
				sb.append(
					"inner join Organization_ on Organization_.organizationId");
				sb.append("= PortletPreferences.ownerId");

				whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_ORGANIZATION);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, false);
					}
				}

				sb = new StringBundler(7);

				sb.append(_SELECT);
				sb.append(", User_.companyId from PortletPreferences ");
				sb.append("inner join User_ on User_.userId");
				sb.append("= PortletPreferences.ownerId");

				whereClause = getUpdatePortletPreferencesWhereClause();

				if (Validator.isNotNull(whereClause)) {
					sb.append(" where ");
					sb.append(whereClause);
					sb.append(" and PortletPreferences.ownerType = ?");
				}
				else {
					sb.append(" where PortletPreferences.ownerType = ?");
				}

				try (PreparedStatement ps3 = connection.prepareStatement(
						sb.toString())) {

					ps3.setLong(1, PortletKeys.PREFS_OWNER_TYPE_USER);

					try (ResultSet rs = ps3.executeQuery()) {
						_parseResultSet(rs, ps1, ps2, false);
					}
				}
			}
		}
	}

	/**
	 * @deprecated As of 7.0.0
	 */
	@Deprecated
	protected void updatePortletPreferences(
			long portletPreferencesId, String preferences)
		throws Exception {

		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(
				"update PortletPreferences set preferences = ? where " +
					"portletPreferencesId = " + portletPreferencesId);

			ps.setString(1, preferences);

			ps.executeUpdate();
		}
		finally {
			DataAccess.cleanUp(ps);
		}
	}

	protected void upgradeMultiValuePreference(
			PortletPreferences portletPreferences, String key)
		throws ReadOnlyException {

		String value = portletPreferences.getValue(key, StringPool.BLANK);

		if (Validator.isNotNull(value)) {
			portletPreferences.setValues(key, StringUtil.split(value));
		}
	}

	protected abstract String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception;

	private void _parseResultSet(
			ResultSet rs, PreparedStatement ps1, PreparedStatement ps2,
			boolean companyIdIsOwnerId)
		throws Exception {

		while (rs.next()) {
			long portletPreferencesId = rs.getLong("portletPreferencesId");
			long ownerId = rs.getLong("ownerId");
			int ownerType = rs.getInt("ownerType");
			long plid = rs.getLong("plid");
			String portletId = rs.getString("portletId");
			String preferences = GetterUtil.getString(
				rs.getString("preferences"));

			long companyId = 0;

			if (companyIdIsOwnerId) {
				companyId = ownerId;
			}
			else {
				companyId = rs.getLong("companyId");
			}

			if (companyId > 0) {
				String newPreferences = upgradePreferences(
					companyId, ownerId, ownerType, plid, portletId,
					preferences);

				if (!preferences.equals(newPreferences)) {
					ps1.setString(1, newPreferences);
					ps1.setLong(2, portletPreferencesId);

					ps1.addBatch();
				}
			}
			else {
				ps2.setLong(1, portletPreferencesId);

				ps2.addBatch();
			}

			ps1.executeBatch();

			ps2.executeBatch();
		}
	}

	private static final String _SELECT =
		"select PortletPreferences.portletPreferencesId," +
			" PortletPreferences.ownerId, PortletPreferences.ownerType," +
				" PortletPreferences.plid, PortletPreferences.portletId," +
					" PortletPreferences.preferences";

}