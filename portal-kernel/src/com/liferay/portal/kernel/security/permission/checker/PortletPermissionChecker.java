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

import com.liferay.portal.kernel.internal.security.permission.checker.PermissionCacheKey;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;

import java.util.Map;

/**
 * @author Preston Crary
 */
public class PortletPermissionChecker implements PortletPermission {

	public PortletPermissionChecker(
		String resourceName,
		PortletPermissionCheck... portletPermissionChecks) {

		_resourceName = resourceName;
		_portletPermissionChecks = portletPermissionChecks;
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, Group group, String actionId)
		throws PrincipalException {

		if (group == null) {
			check(permissionChecker, 0, actionId);
		}
		else if (!contains(permissionChecker, group, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker.getUserId(), _resourceName,
				group.getGroupId(), actionId);
		}
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, long groupId, String actionId)
		throws PrincipalException {

		if (!contains(permissionChecker, groupId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker.getUserId(), _resourceName, groupId,
				actionId);
		}
	}

	@Override
	public boolean contains(
		PermissionChecker permissionChecker, Group group, String actionId) {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		long groupId = 0;

		if (group != null) {
			groupId = group.getGroupId();
		}

		final long finalGroupId = groupId;

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			groupId, actionId);

		return (Boolean)permissionChecksMap.computeIfAbsent(
			permissionCacheKey,
			key -> _contains(permissionChecker, group, finalGroupId, actionId));
	}

	@Override
	public boolean contains(
		PermissionChecker permissionChecker, long groupId, String actionId) {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			groupId, actionId);

		return (Boolean)permissionChecksMap.computeIfAbsent(
			permissionCacheKey,
			key -> {
				Group group = null;

				if (groupId > 0) {
					group = GroupLocalServiceUtil.fetchGroup(groupId);
				}

				return _contains(permissionChecker, group, groupId, actionId);
			});
	}

	private boolean _contains(
		PermissionChecker permissionChecker, Group group, long groupId,
		String actionId) {

		for (PortletPermissionCheck portletPermissionCheck :
				_portletPermissionChecks) {

			Boolean contains = portletPermissionCheck.contains(
				permissionChecker, _resourceName, group, actionId);

			if (contains != null) {
				return contains;
			}
		}

		if ((group != null) && group.isStagingGroup()) {
			group = group.getLiveGroup();
		}

		return permissionChecker.hasPermission(
			group, _resourceName, groupId, actionId);
	}

	private final PortletPermissionCheck[] _portletPermissionChecks;
	private final String _resourceName;

}