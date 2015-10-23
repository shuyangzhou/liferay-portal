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

import com.liferay.journal.constants.JournalPortletKeys;
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
				con,
				"alter table JournalArticle add lastPublishDate DATE null");

			updateLastPublishDates(
				con, JournalPortletKeys.JOURNAL, "JournalArticle");

			runSQL(
				con, "alter table JournalFeed add lastPublishDate DATE null");

			updateLastPublishDates(
				con, JournalPortletKeys.JOURNAL, "JournalFeed");

			runSQL(
				con, "alter table JournalFolder add lastPublishDate DATE null");

			updateLastPublishDates(
				con, JournalPortletKeys.JOURNAL, "JournalFolder");
		}
	}

}