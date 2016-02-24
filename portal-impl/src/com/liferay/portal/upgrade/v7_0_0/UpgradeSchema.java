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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.tools.StopWatchLoggingHelper;
import com.liferay.portal.upgrade.util.UpgradeMVCCVersion;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Julio Camarero
 */
public class UpgradeSchema extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		StopWatch stopWatch = StopWatchLoggingHelper.startLogging(
			_log, "UpgradeSchema.runSQLTemplate");

		runSQLTemplate("update-6.2.0-7.0.0.sql", false);

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log, "UpgradeSchema.runSQLTemplate");

		stopWatch = StopWatchLoggingHelper.startLogging(
			_log, "UpgradeSchema.upgrade((UpgradeMVCCVersion.class)");

		upgrade(UpgradeMVCCVersion.class);

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log,
			"UpgradeSchema.upgrade((UpgradeMVCCVersion.class)");
	}

	private static final Log _log = LogFactoryUtil.getLog(UpgradeSchema.class);

}