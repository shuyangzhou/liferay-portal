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

package com.liferay.dispatch.internal.security.permission.resource;

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.security.permission.DispatchTaskPermission;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.dispatch.model.DispatchTask",
	service = ModelResourcePermission.class
)
public class DispatchTaskModelResourcePermission
	implements ModelResourcePermission<DispatchTask> {

	@Override
	public void check(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		dispatchTaskPermission.check(permissionChecker, dispatchTask, actionId);
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, long primaryKey,
			String actionId)
		throws PortalException {

		dispatchTaskPermission.check(permissionChecker, primaryKey, actionId);
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		return dispatchTaskPermission.contains(
			permissionChecker, dispatchTask, actionId);
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, long dispatchTaskId,
			String actionId)
		throws PortalException {

		return dispatchTaskPermission.contains(
			permissionChecker, dispatchTaskId, actionId);
	}

	@Override
	public String getModelName() {
		return DispatchTask.class.getName();
	}

	@Override
	public PortletResourcePermission getPortletResourcePermission() {
		return null;
	}

	@Reference
	protected DispatchTaskPermission dispatchTaskPermission;

}