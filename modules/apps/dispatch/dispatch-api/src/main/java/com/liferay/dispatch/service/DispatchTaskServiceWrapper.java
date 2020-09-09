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

package com.liferay.dispatch.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DispatchTaskService}.
 *
 * @author Matija Petanjek
 * @see DispatchTaskService
 * @generated
 */
public class DispatchTaskServiceWrapper
	implements DispatchTaskService, ServiceWrapper<DispatchTaskService> {

	public DispatchTaskServiceWrapper(DispatchTaskService dispatchTaskService) {
		_dispatchTaskService = dispatchTaskService;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _dispatchTaskService.getOSGiServiceIdentifier();
	}

	@Override
	public DispatchTaskService getWrappedService() {
		return _dispatchTaskService;
	}

	@Override
	public void setWrappedService(DispatchTaskService dispatchTaskService) {
		_dispatchTaskService = dispatchTaskService;
	}

	private DispatchTaskService _dispatchTaskService;

}