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

package com.liferay.portal.kernel.security.permission;

import com.liferay.exportimport.kernel.staging.permission.StagingPermissionUtil;

/**
 * @author Preston Crary
 */
public class StagingPortletPermissionHelper extends PortletPermissionHelper {

	public StagingPortletPermissionHelper(
		String resourceName, String portletId) {

		super(resourceName);

		_portletId = portletId;
	}

	@Override
	public boolean contains(
		PermissionChecker permissionChecker, long classPK, String actionId) {

		Boolean hasPermission = StagingPermissionUtil.hasPermission(
			permissionChecker, classPK, resourceName, classPK, _portletId,
			actionId);

		if (hasPermission != null) {
			return hasPermission;
		}

		return super.contains(permissionChecker, classPK, actionId);
	}

	private final String _portletId;

}