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

package com.liferay.document.library.web.internal.upgrade.v1_0_0;

import com.liferay.document.library.web.constants.DLPortletKeys;
import com.liferay.document.library.web.internal.settings.DLPortletInstanceSettings;
import com.liferay.portal.kernel.settings.SettingsDescriptor;
import com.liferay.portal.kernel.settings.SettingsFactory;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portlet.documentlibrary.DLGroupServiceSettings;
import com.liferay.portlet.documentlibrary.constants.DLConstants;

/**
 * @author Sergio González
 */
public class UpgradePortletSettings
	extends com.liferay.portal.upgrade.v7_0_0.UpgradePortletSettings {

	public UpgradePortletSettings(SettingsFactory settingsFactory) {
		super(settingsFactory);
	}

	@Override
	protected void doUpgrade() throws Exception {
		DLGroupServiceSettings.registerSettingsMetadata();
		DLPortletInstanceSettings.registerSettingsMetadata();

		SettingsDescriptor settingsDescriptor =
			SettingsFactoryUtil.getSettingsDescriptor(DLConstants.SERVICE_NAME);

		upgradeMainPortlet(
			SettingsFactoryUtil.getSettingsDescriptor(
				DLPortletKeys.DOCUMENT_LIBRARY),
			settingsDescriptor, DLPortletKeys.DOCUMENT_LIBRARY,
			DLConstants.SERVICE_NAME, PortletKeys.PREFS_OWNER_TYPE_GROUP);

		upgradeDisplayPortlet(
			settingsDescriptor, DLPortletKeys.DOCUMENT_LIBRARY,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT);
		upgradeDisplayPortlet(
			settingsDescriptor, DLPortletKeys.MEDIA_GALLERY_DISPLAY,
			PortletKeys.PREFS_OWNER_TYPE_LAYOUT);
	}

}