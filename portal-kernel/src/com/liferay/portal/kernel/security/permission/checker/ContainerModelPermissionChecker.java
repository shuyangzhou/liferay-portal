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

import com.liferay.petra.function.UnsafeLongFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.ToLongFunction;

/**
 * @author Preston Crary
 */
public class ContainerModelPermissionChecker<T extends GroupedModel>
	extends ModelPermissionChecker<T> implements ContainerModelPermission<T> {

	public ContainerModelPermissionChecker(
		String modelName,
		UnsafeLongFunction<T, ? extends PortalException> getModelFunction,
		ToLongFunction<T> primKeyFunction,
		List<ModelPermissionCheck<T>> modelPermissionChecks,
		PortletPermission portletPermission) {

		super(
			modelName, getModelFunction, primKeyFunction,
			modelPermissionChecks);

		_portletPermission = portletPermission;
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
			return _portletPermission.contains(
				permissionChecker, groupId, actionId);
		}

		return contains(permissionChecker, primaryKey, actionId);
	}

	@Override
	public Collection<Class<?>> getServices() {
		return Arrays.asList(
			ContainerModelPermission.class, ModelPermission.class);
	}

	private static final long _DEFAULT_PARENT_PRIMARY_KEY = 0;

	private final PortletPermission _portletPermission;

}