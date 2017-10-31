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

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;

/**
 * @author Preston Crary
 */
public class PortletPermissionHelper {

	public PortletPermissionHelper(String resourceName) {
		this.resourceName = resourceName;
	}

	public void check(
			PermissionChecker permissionChecker, long groupId, String actionId)
		throws PrincipalException {

		if (!contains(permissionChecker, groupId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker.getUserId(), resourceName, groupId, actionId);
		}
	}

	public boolean contains(
		PermissionChecker permissionChecker, long classPK, String actionId) {

		Group group = GroupLocalServiceUtil.fetchGroup(classPK);

		if ((group != null) && group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		return permissionChecker.hasPermission(
			group, resourceName, classPK, actionId);
	}

	protected final String resourceName;

}