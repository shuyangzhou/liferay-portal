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

package com.liferay.portal.kernel.security.permission.checker;

import com.liferay.exportimport.kernel.staging.permission.StagingPermissionUtil;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

/**
 * @author Preston Crary
 */
public class StagedPortletPermissionCheck implements PortletPermissionCheck {

	public StagedPortletPermissionCheck(String portletId) {
		_portletId = portletId;
	}

	@Override
	public Boolean contains(
		PermissionChecker permissionChecker, String name, long classPK,
		String actionId) {

		return StagingPermissionUtil.hasPermission(
			permissionChecker, classPK, name, classPK, _portletId, actionId);
	}

	private final String _portletId;

}