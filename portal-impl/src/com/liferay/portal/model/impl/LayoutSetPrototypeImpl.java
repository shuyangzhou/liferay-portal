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

package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.LayoutSet;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutSetLocalServiceUtil;
import com.liferay.portlet.sites.util.Sites;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 */
public class LayoutSetPrototypeImpl extends LayoutSetPrototypeBaseImpl {

	public LayoutSetPrototypeImpl() {
	}

	@Override
	public Group getGroup() throws PortalException {
		return GroupLocalServiceUtil.getLayoutSetPrototypeGroup(
			getCompanyId(), getLayoutSetPrototypeId());
	}

	@Override
	public long getGroupId() throws PortalException {
		Group group = getGroup();

		return group.getGroupId();
	}

	@Override
	public LayoutSet getLayoutSet() throws PortalException {
		return LayoutSetLocalServiceUtil.getLayoutSet(
			getGroup().getGroupId(), true);
	}

	@Override
	public UnicodeProperties getSettingsProperties() {
		if (_settingsProperties == null) {
			_settingsProperties = new UnicodeProperties(true);

			try {
				_settingsProperties.load(super.getSettings());
			}
			catch (IOException ioe) {
				_log.error(ioe, ioe);
			}
		}

		return _settingsProperties;
	}

	@Override
	public String getSettingsProperty(String key) {
		UnicodeProperties settingsProperties = getSettingsProperties();

		return settingsProperties.getProperty(key);
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		if ((modifiedDate == null) || isNew()) {
			super.setModifiedDate(modifiedDate);

			return;
		}

		Date currentModifiedDate = getModifiedDate();

		if (currentModifiedDate != null) {
			if (currentModifiedDate.before(modifiedDate) ) {
				modifiedDate = DateUtil.getDBSafeDate(modifiedDate);
			}
			else {
				modifiedDate = DateUtil.getDBSafeDate(currentModifiedDate);
			}
		}
		else {
			modifiedDate = DateUtil.getDBSafeDate(modifiedDate);
		}

		LayoutSet privateLayoutSet = null;

		try {
			privateLayoutSet = getLayoutSet();
		}
		catch (PortalException e) {
			throw new SystemException(e);
		}

		List<LayoutSet> layoutSets = new ArrayList(
			LayoutSetLocalServiceUtil.getLayoutSetsByLayoutSetPrototypeUuid(
				getUuid()));

		layoutSets.add(privateLayoutSet);

		long maxLastMergedTime = 0;

		for (LayoutSet layoutSet : layoutSets) {
			String lastMergedTimeString = layoutSet.getSettingsProperty(
				Sites.LAST_MERGE_TIME);

			if (lastMergedTimeString != null) {
				long lastMergedTime = GetterUtil.getLong(lastMergedTimeString);

				if (lastMergedTime > maxLastMergedTime) {
					maxLastMergedTime = lastMergedTime;
				}
			}
		}

		if (maxLastMergedTime >= modifiedDate.getTime()) {
			modifiedDate = new Date(maxLastMergedTime + Time.SECOND);
		}

		super.setModifiedDate(modifiedDate);
	}

	@Override
	public void setSettings(String settings) {
		_settingsProperties = null;

		super.setSettings(settings);
	}

	@Override
	public void setSettingsProperties(UnicodeProperties settingsProperties) {
		_settingsProperties = settingsProperties;

		super.setSettings(settingsProperties.toString());
	}

	private static Log _log = LogFactoryUtil.getLog(
		LayoutSetPrototypeImpl.class);

	private UnicodeProperties _settingsProperties;

}