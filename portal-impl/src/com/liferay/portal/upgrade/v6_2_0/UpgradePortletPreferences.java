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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LoggingTimer;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Julio Camarero
 */
public class UpgradePortletPreferences extends UpgradeProcess {

	protected void deletePortletPreferences() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			StringBundler sb = new StringBundler(6);

			sb.append("select portletPreferencesId, plid, portletId from ");
			sb.append("PortletPreferences where (ownerType = ");
			sb.append(PortletKeys.PREFS_OWNER_TYPE_LAYOUT);
			sb.append(") and (preferences like '%<portlet-preferences %/>%' ");
			sb.append("or preferences like '' or preferences is null) order ");
			sb.append("by plid");

			long lastPlid = 0;

			String typeSettings = StringPool.BLANK;

			try (PreparedStatement ps = connection.prepareStatement(
					sb.toString(), ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					long portletPreferencesId = rs.getLong(
						"portletPreferencesId");
					long plid = rs.getLong("plid");
					String portletId = GetterUtil.getString(
						rs.getString("portletId"));

					if (lastPlid != plid) {
						typeSettings = _getTypeSettings(plid);

						lastPlid = plid;
					}

					if ((typeSettings != null) &&
						typeSettings.contains(portletId)) {

						continue;
					}

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Deleting portlet preferences " +
								portletPreferencesId);
					}

					rs.deleteRow();
				}
			}
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		deletePortletPreferences();
	}

	private String _getTypeSettings(long plid) throws Exception {
		String sql = "select typeSettings from Layout where plid = " + plid;

		try (PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString("typeSettings");
			}
		}

		sql =
			"select typeSettings from LayoutRevision where layoutRevisionId " +
				"= " + plid;

		try (PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString("typeSettings");
			}
		}

		return null;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradePortletPreferences.class);

}