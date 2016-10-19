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

package com.liferay.portal.upgrade.v7_0_3;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Shuyang Zhou
 */
public class UpgradeBlogs extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer();
			PreparedStatement ps = connection.prepareStatement(
				"select * from Release_ where servletContextName=" +
					"'com.liferay.blogs.service'");
			ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return;
			}

			try (PreparedStatement ps2 = connection.prepareStatement(
				"insert into Release_ (releaseId, createDate, modifiedDate, " +
					"servletContextName, schemaVersion) values (?, ?, ?, ?, " +
						"?)")) {

				Timestamp timestamp = new Timestamp(System.currentTimeMillis());

				ps2.setLong(1, increment());
				ps2.setTimestamp(2, timestamp);
				ps2.setTimestamp(3, timestamp);
				ps2.setString(4, "com.liferay.blogs.service");
				ps2.setString(5, "1.0.0");

				ps2.executeUpdate();
			}
		}
	}

}