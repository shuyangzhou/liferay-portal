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
public abstract class EntryModelPermissionCheck
	<E extends GroupedModel, C extends ClassedModel>
		implements ModelPermissionCheck<E> {

	@Override
	public Boolean contains(
			ModelPermission<E> modelPermission,
			PermissionChecker permissionChecker, String name, E entry,
			String actionId)
		throws PortalException {

		if (!ActionKeys.VIEW.equals(actionId)) {
			return null;
		}

		C container = fetchContainer(entry);

		if (container == null) {
			PortletPermission portletPermission = getPortletPermission();

			if (!portletPermission.contains(
					permissionChecker, entry.getGroupId(), actionId)) {

				return false;
			}

			return null;
		}

		ModelPermission<C> containerModelPermission =
			getContainerModelPermission();

		if (!containerModelPermission.contains(
				permissionChecker, container, ActionKeys.ACCESS) &&
			!containerModelPermission.contains(
				permissionChecker, container, ActionKeys.VIEW)) {

			return false;
		}

		return null;
	}

	protected abstract C fetchContainer(E entry) throws PortalException;

	protected abstract ModelPermission<C> getContainerModelPermission();

	protected abstract PortletPermission getPortletPermission();

}