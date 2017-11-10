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

	@Override
	public Boolean contains(
			BaseModelPermission<T> modelPermission,
			PermissionChecker permissionChecker, String name, T model,
			String actionId)
		throws PortalException {

		if (!ActionKeys.VIEW.equals(actionId)) {
			return null;
		}

		T next = model;

		while (next != null) {
			if (!_hasViewPermission(permissionChecker, name, next)) {
				return false;
			}

			next = fetchParent(next);
		}

		PortletPermission portletPermission = getPortletPermission();

		return portletPermission.contains(
			permissionChecker, model.getGroupId(), actionId);
	}

	protected abstract T fetchParent(T child) throws PortalException;

	protected abstract PortletPermission getPortletPermission();

	private boolean _hasViewPermission(
		PermissionChecker permissionChecker, String name, T folder) {

		String primKey = String.valueOf(folder.getPrimaryKeyObj());

		if (permissionChecker.hasOwnerPermission(
				folder.getCompanyId(), name, primKey, folder.getUserId(),
				ActionKeys.VIEW) ||
			permissionChecker.hasPermission(
				folder.getGroupId(), name, primKey, ActionKeys.VIEW)) {

			return true;
		}

		return false;
	}

}