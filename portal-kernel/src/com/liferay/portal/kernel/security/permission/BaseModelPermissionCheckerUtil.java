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

package com.liferay.portal.kernel.security.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.checker.ModelPermission;
import com.liferay.portal.kernel.security.permission.checker.ParentModelPermission;
import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

/**
 * @author Roberto Díaz
 */
public class BaseModelPermissionCheckerUtil {

	public static Boolean containsBaseModelPermission(
		PermissionChecker permissionChecker, long groupId, String className,
		long classPK, String actionId) {

		ModelPermission<?> modelPermission = _modelPermissions.getService(
			className);

		if (modelPermission != null) {
			try {
				return modelPermission.contains(
					permissionChecker, classPK, actionId);
			}
			catch (PortalException pe) {
				if (_log.isWarnEnabled()) {
					_log.warn(pe, pe);
				}

				return false;
			}
		}

		ParentModelPermission<?> parentModelPermission =
			_parentModelPermissions.getService(className);

		if (parentModelPermission != null) {
			try {
				return parentModelPermission.contains(
					permissionChecker, groupId, classPK, actionId);
			}
			catch (PortalException pe) {
				if (_log.isWarnEnabled()) {
					_log.warn(pe, pe);
				}

				return false;
			}
		}

		BaseModelPermissionChecker baseModelPermissionChecker =
			_baseModelPermissionCheckers.getService(className);

		if (baseModelPermissionChecker == null) {
			return null;
		}

		try {
			baseModelPermissionChecker.checkBaseModel(
				permissionChecker, groupId, classPK, actionId);
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseModelPermissionCheckerUtil.class);

	private static final ServiceTrackerMap<String, BaseModelPermissionChecker>
		_baseModelPermissionCheckers =
			ServiceTrackerCollections.openSingleValueMap(
				BaseModelPermissionChecker.class, "model.class.name");
	private static final ServiceTrackerMap<String, ModelPermission>
		_modelPermissions = ServiceTrackerCollections.openSingleValueMap(
			ModelPermission.class, "model.class.name");
	private static final ServiceTrackerMap<String, ParentModelPermission>
		_parentModelPermissions = ServiceTrackerCollections.openSingleValueMap(
			ParentModelPermission.class, "model.class.name");

}