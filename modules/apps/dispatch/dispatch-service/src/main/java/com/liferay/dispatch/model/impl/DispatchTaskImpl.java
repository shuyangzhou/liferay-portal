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

package com.liferay.dispatch.model.impl;

import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.util.Date;

/**
 * The extended model implementation for the DispatchTask service. Represents a row in the &quot;DispatchTask&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * Helper methods and all application logic should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.dispatch.model.DispatchTask</code> interface.
 * </p>
 *
 * @author Matija Petanjek
 */
public class DispatchTaskImpl extends DispatchTaskBaseImpl {

	public DispatchTaskImpl() {
	}

	@Override
	public Date getEndDate() throws SchedulerException {
		if (_endDate == null) {
			_endDate = SchedulerEngineHelperUtil.getEndTime(
				String.format("DISPATCH_JOB_%07d", getDispatchTaskId()),
				String.format("DISPATCH_GROUP_%07d", getDispatchTaskId()),
				StorageType.PERSISTED);
		}

		return _endDate;
	}

	@Override
	public Date getStartDate() throws SchedulerException {
		if (_startDate == null) {
			_startDate = SchedulerEngineHelperUtil.getStartTime(
				String.format("DISPATCH_JOB_%07d", getDispatchTaskId()),
				String.format("DISPATCH_GROUP_%07d", getDispatchTaskId()),
				StorageType.PERSISTED);
		}

		return _startDate;
	}

	@Override
	public UnicodeProperties getTypeSettingsProperties() {
		if (_typeSettingsUnicodeProperties == null) {
			_typeSettingsUnicodeProperties = new UnicodeProperties(true);

			_typeSettingsUnicodeProperties.fastLoad(getTypeSettings());
		}

		return _typeSettingsUnicodeProperties;
	}

	@Override
	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	@Override
	public void setTypeSettings(String typeSettings) {
		super.setTypeSettings(typeSettings);

		_typeSettingsUnicodeProperties = null;
	}

	@Override
	public void setTypeSettingsUnicodeProperties(
		UnicodeProperties typeSettingsUnicodeProperties) {

		_typeSettingsUnicodeProperties = typeSettingsUnicodeProperties;

		if (_typeSettingsUnicodeProperties == null) {
			_typeSettingsUnicodeProperties = new UnicodeProperties();
		}

		super.setTypeSettings(_typeSettingsUnicodeProperties.toString());
	}

	private Date _endDate;
	private Date _startDate;
	private transient UnicodeProperties _typeSettingsUnicodeProperties;

}