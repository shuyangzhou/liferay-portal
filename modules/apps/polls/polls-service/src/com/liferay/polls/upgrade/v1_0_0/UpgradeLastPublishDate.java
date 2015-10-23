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

package com.liferay.polls.upgrade.v1_0_0;

import com.liferay.polls.constants.PollsPortletKeys;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;

import java.sql.Connection;

/**
 * @author Mate Thurzo
 */
public class UpgradeLastPublishDate
	extends com.liferay.portal.upgrade.v7_0_0.UpgradeLastPublishDate {

	@Override
	protected void doUpgrade() throws Exception {
		try (Connection con = DataAccess.getUpgradeOptimizedConnection()) {
			runSQL(
				con, "alter table PollsChoice add lastPublishDate DATE null");

			updateLastPublishDates(con, PollsPortletKeys.POLLS, "PollsChoice");

			runSQL(
				con, "alter table PollsQuestion add lastPublishDate DATE null");

			updateLastPublishDates(
				con, PollsPortletKeys.POLLS, "PollsQuestion");

			runSQL(con, "alter table PollsVote add lastPublishDate DATE null");

			updateLastPublishDates(con, PollsPortletKeys.POLLS, "PollsVote");
		}
	}

}