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

package com.liferay.dispatch.internal.security.permission;

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.security.permission.DispatchTaskPermission;
import com.liferay.dispatch.service.DispatchTaskLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
@Component(immediate = true, service = DispatchTaskPermission.class)
public class DispatchTaskPermissionImpl implements DispatchTaskPermission {

	@Override
	public void check(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, dispatchTask, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, DispatchTask.class.getName(),
				dispatchTask.getDispatchTaskId(), actionId);
		}
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, long dispatchTaskId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, dispatchTaskId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, DispatchTask.class.getName(), dispatchTaskId,
				actionId);
		}
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		if (contains(
				permissionChecker, dispatchTask.getDispatchTaskId(),
				actionId)) {

			return true;
		}

		return false;
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long dispatchTaskId,
			String actionId)
		throws PortalException {

		DispatchTask dispatchTask = _dispatchTaskLocalService.fetchDispatchTask(
			dispatchTaskId);

		if (dispatchTask == null) {
			return false;
		}

		return _contains(permissionChecker, dispatchTask, actionId);
	}

	private boolean _contains(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		if (permissionChecker.hasOwnerPermission(
				dispatchTask.getCompanyId(), DispatchTask.class.getName(),
				dispatchTask.getDispatchTaskId(), dispatchTask.getUserId(),
				actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			0, DispatchTask.class.getName(), dispatchTask.getDispatchTaskId(),
			actionId);
	}

	@Reference
	private DispatchTaskLocalService _dispatchTaskLocalService;

}