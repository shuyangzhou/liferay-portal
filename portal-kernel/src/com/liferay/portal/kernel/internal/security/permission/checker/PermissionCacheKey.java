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

package com.liferay.portal.kernel.internal.security.permission.checker;

import com.liferay.portal.kernel.util.HashUtil;

import java.util.Objects;

/**
 * @author Preston Crary
 */
public class PermissionCacheKey {

	public PermissionCacheKey(long primaryKey, String actionId) {
		_primaryKey = primaryKey;
		_actionId = actionId;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof PermissionCacheKey)) {
			return false;
		}

		PermissionCacheKey permissionCacheKey = (PermissionCacheKey)object;

		if ((permissionCacheKey._primaryKey == _primaryKey) &&
			Objects.equals(permissionCacheKey._actionId, _actionId)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _primaryKey);

		return HashUtil.hash(hash, _actionId);
	}

	private final String _actionId;
	private final long _primaryKey;

}