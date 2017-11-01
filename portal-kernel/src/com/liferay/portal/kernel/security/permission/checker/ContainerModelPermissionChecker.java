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
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

/**
 * @author Preston Crary
 */
public abstract class ContainerModelPermissionChecker<T extends GroupedModel>
	extends ModelPermissionChecker<T> implements ContainerModelPermission<T> {

	@SafeVarargs
	public ContainerModelPermissionChecker(
		String modelName, ModelPermissionCheck<T>... modelPermissionChecks) {

		super(modelName, modelPermissionChecks);
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, groupId, primaryKey, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, modelName, primaryKey, actionId);
		}
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException {

		if (primaryKey == _DEFAULT_PARENT_PRIMARY_KEY) {
			PortletPermission portletPermission = getPortletPermission();

			return portletPermission.contains(
				permissionChecker, groupId, actionId);
		}

		return contains(permissionChecker, primaryKey, actionId);
	}

	protected abstract PortletPermission getPortletPermission();

	private static final long _DEFAULT_PARENT_PRIMARY_KEY = 0;

}