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

package com.liferay.portal.language.override.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link PLOEntryService}.
 *
 * @author Brian Wing Shun Chan
 * @see PLOEntryService
 * @generated
 */
public class PLOEntryServiceWrapper
	implements PLOEntryService, ServiceWrapper<PLOEntryService> {

	public PLOEntryServiceWrapper(PLOEntryService ploEntryService) {
		_ploEntryService = ploEntryService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _ploEntryService.getOSGiServiceIdentifier();
	}

	@Override
	public PLOEntryService getWrappedService() {
		return _ploEntryService;
	}

	@Override
	public void setWrappedService(PLOEntryService ploEntryService) {
		_ploEntryService = ploEntryService;
	}

	private PLOEntryService _ploEntryService;

}