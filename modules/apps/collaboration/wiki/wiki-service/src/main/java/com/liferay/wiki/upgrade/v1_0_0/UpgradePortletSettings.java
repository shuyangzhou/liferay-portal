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

package com.liferay.wiki.upgrade.v1_0_0;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.tools.StopWatchLoggingHelper;
import com.liferay.wiki.constants.WikiConstants;
import com.liferay.wiki.constants.WikiPortletKeys;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Iván Zaera
 */
public class UpgradePortletSettings
	extends com.liferay.portal.upgrade.v7_0_0.UpgradePortletSettings {

	public UpgradePortletSettings(SettingsFactory settingsFactory) {
		super(settingsFactory);
	}

	@Override
	protected void doUpgrade() throws Exception {
		StopWatch stopWatch = StopWatchLoggingHelper.startLogging(
			_log, "UpgradePortletSettings.upgradeDisplayPortlet");

		upgradeDisplayPortlet(
			WikiPortletKeys.WIKI_DISPLAY, WikiConstants.SERVICE_NAME,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT);

		StopWatchLoggingHelper.endLogging(
			stopWatch, _log, "UpgradePortletSettings.upgradeDisplayPortlet");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradePortletSettings.class);

}