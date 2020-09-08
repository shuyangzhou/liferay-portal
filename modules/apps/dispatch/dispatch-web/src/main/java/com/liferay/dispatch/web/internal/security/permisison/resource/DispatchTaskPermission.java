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

package com.liferay.dispatch.web.internal.security.permisison.resource;

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 */
@Component(immediate = true, service = {})
public class DispatchTaskPermission {

	public static boolean contains(
			PermissionChecker permissionChecker, DispatchTask dispatchTask,
			String actionId)
		throws PortalException {

		return _dispatchTaskModelResourcePermission.contains(
			permissionChecker, dispatchTask.getDispatchTaskId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long dispatchTaskId,
			String actionId)
		throws PortalException {

		return _dispatchTaskModelResourcePermission.contains(
			permissionChecker, dispatchTaskId, actionId);
	}

	@Reference(
		target = "(model.class.name=com.liferay.dispatch.model.DispatchTask)",
		unbind = "-"
	)
	protected void setModelResourcePermission(
		ModelResourcePermission<DispatchTask> modelResourcePermission) {

		_dispatchTaskModelResourcePermission = modelResourcePermission;
	}

	private static ModelResourcePermission<DispatchTask>
		_dispatchTaskModelResourcePermission;

}