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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

/**
 * @author Preston Crary
 */
public abstract class ParentModelPermissionCheck<T extends GroupedModel>
	implements ModelPermissionCheck<T> {

	public ParentModelPermissionCheck(
		PortletPermissionChecker portletPermissionChecker) {

		_portletPermissionChecker = portletPermissionChecker;
	}

	@Override
	public Boolean contains(
			ModelPermissionChecker<T> modelPermissionChecker,
			PermissionChecker permissionChecker, String name, T model,
			String actionId)
		throws PortalException {

		if (!ActionKeys.VIEW.equals(actionId)) {
			return null;
		}

		T parent = fetchParent(model);

		while (parent != null) {
			if (!_hasViewPermission(permissionChecker, name, model)) {
				return false;
			}

			parent = fetchParent(model);
		}

		return _portletPermissionChecker.contains(
			permissionChecker, model.getGroupId(), actionId);
	}

	protected abstract T fetchParent(T child);

	private boolean _hasViewPermission(
		PermissionChecker permissionChecker, String name, T folder) {

		if (permissionChecker.hasOwnerPermission(
				folder.getCompanyId(), name, (long)folder.getPrimaryKeyObj(),
				folder.getUserId(), ActionKeys.VIEW) ||
			permissionChecker.hasPermission(
				folder.getGroupId(), name, (long)folder.getPrimaryKeyObj(),
				ActionKeys.VIEW)) {

			return true;
		}

		return false;
	}

	private final PortletPermissionChecker _portletPermissionChecker;

}