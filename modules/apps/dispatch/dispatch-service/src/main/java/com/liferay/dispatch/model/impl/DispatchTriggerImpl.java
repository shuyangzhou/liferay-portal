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

import com.liferay.portal.kernel.util.UnicodeProperties;

/**
 * @author Alessio Antonio Rendina
 */
public class DispatchTriggerImpl extends DispatchTriggerBaseImpl {

	public DispatchTriggerImpl() {
	}

	@Override
	public UnicodeProperties getJobUnicodeProperties() {
		if (_jobUnicodeProperties == null) {
			_jobUnicodeProperties = new UnicodeProperties(true);

			_jobUnicodeProperties.fastLoad(getJobProperties());
		}

		return _jobUnicodeProperties;
	}

	@Override
	public void setJobProperties(String jobProperties) {
		super.setJobProperties(jobProperties);

		_jobUnicodeProperties = null;
	}

	@Override
	public void setJobUnicodeProperties(
		UnicodeProperties jobUnicodeProperties) {

		_jobUnicodeProperties = jobUnicodeProperties;

		if (_jobUnicodeProperties == null) {
			_jobUnicodeProperties = new UnicodeProperties();
		}

		super.setJobProperties(_jobUnicodeProperties.toString());
	}

	private transient UnicodeProperties _jobUnicodeProperties;

}