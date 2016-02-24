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
			"UpgradeLastPublishDate.updateLastPublishDates#JournalArticle");

		updateLastPublishDates(JournalPortletKeys.JOURNAL, "JournalArticle");

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeLastPublishDate.updateLastPublishDates#JournalArticle");

		stopWatch = StopWatchLoggingHelper.startLogging(
			_log, "UpgradeLastPublishDate.updateLastPublishDates#JournalFeed");

		updateLastPublishDates(JournalPortletKeys.JOURNAL, "JournalFeed");

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeLastPublishDate.updateLastPublishDates#JournalFeed");

		stopWatch = StopWatchLoggingHelper.startLogging(
			_log,
			"UpgradeLastPublishDate.updateLastPublishDates#JournalFolder");

		updateLastPublishDates(JournalPortletKeys.JOURNAL, "JournalFolder");

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeLastPublishDate.updateLastPublishDates#JournalFolder");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeLastPublishDate.class);

}