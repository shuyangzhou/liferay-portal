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
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.HashUtil;

import java.util.Map;
import java.util.Objects;

/**
 * @author Preston Crary
 */
public abstract class ModelPermissionChecker<T extends GroupedModel> {

	public ModelPermissionChecker(
		String modelName, ModelPermissionCheck<T>... modelPermissionChecks) {

		_modelName = modelName;
		_modelPermissionChecks = modelPermissionChecks;
	}

	public void check(
			PermissionChecker permissionChecker, long primaryKey,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, primaryKey, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, _modelName, primaryKey, actionId);
		}
	}

	public void check(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		if (!contains(permissionChecker, model, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, _modelName, (long)model.getPrimaryKeyObj(),
				actionId);
		}
	}

	public boolean contains(
			PermissionChecker permissionChecker, long primaryKey,
			String actionId)
		throws PortalException {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			primaryKey, actionId);

		Boolean contains = (Boolean)permissionChecksMap.get(permissionCacheKey);

		if (contains == null) {
			contains = doContains(
				permissionChecker, getModel(primaryKey), actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	public boolean contains(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			(long)model.getPrimaryKeyObj(), actionId);

		Boolean contains = (Boolean)permissionChecksMap.get(permissionCacheKey);

		if (contains == null) {
			contains = doContains(permissionChecker, model, actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	protected boolean doContains(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		for (ModelPermissionCheck<T> modelPermissionCheck :
				_modelPermissionChecks) {

			Boolean contains = modelPermissionCheck.contains(
				this, permissionChecker, _modelName, model, actionId);

			if (contains != null) {
				return contains;
			}
		}

		if (permissionChecker.hasOwnerPermission(
				model.getCompanyId(), _modelName,
				(long)model.getPrimaryKeyObj(), model.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			model.getGroupId(), _modelName, (long)model.getPrimaryKeyObj(),
			actionId);
	}

	protected abstract T getModel(long primaryKey) throws PortalException;

	protected void setModelPermissionChecks(
		ModelPermissionCheck<T>... modelPermissionChecks) {

		_modelPermissionChecks = modelPermissionChecks;
	}

	private final String _modelName;
	private ModelPermissionCheck<T>[] _modelPermissionChecks;

	private static class PermissionCacheKey {

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

		private PermissionCacheKey(long primaryKey, String actionId) {
			_primaryKey = primaryKey;
			_actionId = actionId;
		}

		private final String _actionId;
		private final long _primaryKey;

	}

}