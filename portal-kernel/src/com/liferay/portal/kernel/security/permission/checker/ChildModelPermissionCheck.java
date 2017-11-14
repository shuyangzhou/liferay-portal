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
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

/**
 * @author Preston Crary
 */
public abstract class ChildModelPermissionCheck
	<C extends GroupedModel, P extends ClassedModel>
		implements ModelPermissionCheck<C> {

	@Override
	public Boolean contains(
			BaseModelPermission<C> baseModelPermission,
			PermissionChecker permissionChecker, String name, C child,
			String actionId)
		throws PortalException {

		if (!ActionKeys.VIEW.equals(actionId)) {
			return null;
		}

		P parent = fetchParentModel(child);

		if (parent == null) {
			PortletPermission portletPermission = getPortletPermission();

			if (!portletPermission.contains(
					permissionChecker, child.getGroupId(), actionId)) {

				return false;
			}

			return null;
		}

		BaseModelPermission<P> parentPermission = getParentPermission();

		if (!parentPermission.contains(
				permissionChecker, parent, ActionKeys.ACCESS) &&
			!parentPermission.contains(
				permissionChecker, parent, ActionKeys.VIEW)) {

			return false;
		}

		return null;
	}

	protected abstract P fetchParentModel(C child) throws PortalException;

	protected abstract BaseModelPermission<P> getParentPermission();

	protected abstract PortletPermission getPortletPermission();

}