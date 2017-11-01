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

/**
 * @author Preston Crary
 */
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;

/**
 * @author Preston Crary
 */
public class PortletPermissionChecker {

	public PortletPermissionChecker(
		String resourceName,
		PortletPermissionCheck... portletPermissionChecks) {

		_resourceName = resourceName;
		_portletPermissionChecks = portletPermissionChecks;
	}

	public void check(
			PermissionChecker permissionChecker, long groupId, String actionId)
		throws PrincipalException {

		if (!contains(permissionChecker, groupId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker.getUserId(), _resourceName, groupId,
				actionId);
		}
	}

	public boolean contains(
		PermissionChecker permissionChecker, long classPK, String actionId) {

		for (PortletPermissionCheck portletPermissionCheck :
				_portletPermissionChecks) {

			Boolean contains = portletPermissionCheck.contains(
				permissionChecker, _resourceName, classPK, actionId);

			if (contains != null) {
				return contains;
			}
		}

		Group group = GroupLocalServiceUtil.fetchGroup(classPK);

		if ((group != null) && group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		return permissionChecker.hasPermission(
			group, _resourceName, classPK, actionId);
	}

	private final PortletPermissionCheck[] _portletPermissionChecks;
	private final String _resourceName;

}