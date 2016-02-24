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

package com.liferay.bookmarks.upgrade.v1_0_0;

import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.tools.StopWatchLoggingHelper;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Mate Thurzo
 */
public class UpgradeLastPublishDate
	extends com.liferay.portal.upgrade.v7_0_0.UpgradeLastPublishDate {

	@Override
	protected void doUpgrade() throws Exception {
		StopWatch stopWatch = StopWatchLoggingHelper.startLogging(
			_log,
			"UpgradeLastPublishDate.updateLastPublishDates(BookmarksEntry)");

		runSQL("alter table BookmarksEntry add lastPublishDate DATE null");

		updateLastPublishDates(
			BookmarksPortletKeys.BOOKMARKS, "BookmarksEntry");

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeLastPublishDate.updateLastPublishDates(BookmarksEntry)");

		stopWatch = StopWatchLoggingHelper.startLogging(
			_log,
			"UpgradeLastPublishDate.updateLastPublishDates(BookmarksFolder)");

		runSQL("alter table BookmarksFolder add lastPublishDate DATE null");

		updateLastPublishDates(
			BookmarksPortletKeys.BOOKMARKS, "BookmarksFolder");

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeLastPublishDate.updateLastPublishDates(BookmarksFolder)");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeLastPublishDate.class);

}