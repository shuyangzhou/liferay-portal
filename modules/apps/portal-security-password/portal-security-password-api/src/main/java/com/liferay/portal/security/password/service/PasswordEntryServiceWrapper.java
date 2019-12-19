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

package com.liferay.portal.security.password.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link PasswordEntryService}.
 *
 * @author arthurchan35
 * @see PasswordEntryService
 * @generated
 */
public class PasswordEntryServiceWrapper
	implements PasswordEntryService, ServiceWrapper<PasswordEntryService> {

	public PasswordEntryServiceWrapper(
		PasswordEntryService passwordEntryService) {

		_passwordEntryService = passwordEntryService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _passwordEntryService.getOSGiServiceIdentifier();
	}

	@Override
	public PasswordEntryService getWrappedService() {
		return _passwordEntryService;
	}

	@Override
	public void setWrappedService(PasswordEntryService passwordEntryService) {
		_passwordEntryService = passwordEntryService;
	}

	private PasswordEntryService _passwordEntryService;

}