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

import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import java.util.Objects;
import java.util.function.ToLongFunction;

/**
 * @author Preston Crary
 */
public class ContainerDynamicInheritanceCheck<C extends GroupedModel>
	implements ModelPermissionCheck<C> {

	public ContainerDynamicInheritanceCheck(
		UnsafeFunction<C, C, ? extends PortalException> fetchParentFunction,
		ToLongFunction<C> primKeyFunction,
		PortletPermission portletPermission) {

		Objects.requireNonNull(fetchParentFunction);
		Objects.requireNonNull(primKeyFunction);
		Objects.requireNonNull(portletPermission);

		_fetchParentFunction = fetchParentFunction;
		_primKeyFunction = primKeyFunction;
		_portletPermission = portletPermission;
	}

	@Override
	public Boolean contains(
			ModelPermission<C> modelPermission,
			PermissionChecker permissionChecker, String name, C container,
			String actionId)
		throws PortalException {

		if (!ActionKeys.VIEW.equals(actionId)) {
			return null;
		}

		C next = container;

		while (next != null) {
			String primKey = String.valueOf(_primKeyFunction.applyAsLong(next));

			if (!permissionChecker.hasOwnerPermission(
					next.getCompanyId(), name, primKey, next.getUserId(),
					ActionKeys.VIEW) &&
				!permissionChecker.hasPermission(
					next.getGroupId(), name, primKey, ActionKeys.VIEW)) {

				return false;
			}

			next = _fetchParentFunction.apply(next);
		}

		return _portletPermission.contains(
			permissionChecker, container.getGroupId(), actionId);
	}

	private final UnsafeFunction<C, C, ? extends PortalException>
		_fetchParentFunction;
	private final PortletPermission _portletPermission;
	private final ToLongFunction<C> _primKeyFunction;

}