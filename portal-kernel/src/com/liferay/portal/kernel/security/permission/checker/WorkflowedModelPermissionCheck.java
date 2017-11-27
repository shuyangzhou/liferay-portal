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
import com.liferay.portal.kernel.model.WorkflowedModel;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.workflow.permission.WorkflowPermissionUtil;

/**
 * @author Preston Crary
 */
public class WorkflowedModelPermissionCheck<T extends GroupedModel>
	implements ModelPermissionCheck<T> {

	@Override
	public Boolean contains(
			ModelPermission<T> modelPermission,
			PermissionChecker permissionChecker, String name, T model,
			String actionId)
		throws PortalException {

		WorkflowedModel workflowedModel = (WorkflowedModel)model;

		if (workflowedModel.isDraft() || workflowedModel.isScheduled()) {
			if (ActionKeys.VIEW.equals(actionId) &&
				!modelPermission.contains(
					permissionChecker, model, ActionKeys.UPDATE)) {

				return false;
			}
		}
		else if (workflowedModel.isPending()) {
			return WorkflowPermissionUtil.hasPermission(
				permissionChecker, model.getGroupId(), name,
				(long)model.getPrimaryKeyObj(), actionId);
		}

		return null;
	}

}