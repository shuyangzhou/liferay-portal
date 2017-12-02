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
import com.liferay.portal.kernel.internal.security.permission.checker.PermissionCacheKey;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.spring.osgi.OSGIBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ToLongFunction;

/**
 * @author Preston Crary
 */
public class ModelPermissionChecker<T extends GroupedModel>
	implements ModelPermission<T>, OSGIBean {

	public ModelPermissionChecker(
		String modelName,
		UnsafeLongFunction<T, ? extends PortalException> getModelFunction,
		ToLongFunction<T> primKeyFunction,
		List<ModelPermissionCheck<T>> modelPermissionChecks) {

		Objects.requireNonNull(modelName);
		Objects.requireNonNull(getModelFunction);
		Objects.requireNonNull(primKeyFunction);
		Objects.requireNonNull(modelPermissionChecks);

		this.modelName = modelName;
		_getModelFunction = getModelFunction;
		_primKeyFunction = primKeyFunction;
		_modelPermissionChecks = modelPermissionChecks;
	}

	@Override
	public void check(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		if (!contains(permissionChecker, model, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, modelName,
				_primKeyFunction.applyAsLong(model), actionId);
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
				permissionChecker, _getModelFunction.apply(primaryKey),
				actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	@Override
	public boolean contains(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		Map<Object, Object> permissionChecksMap =
			permissionChecker.getPermissionChecksMap();

		PermissionCacheKey permissionCacheKey = new PermissionCacheKey(
			_primKeyFunction.applyAsLong(model), actionId);

		Boolean contains = (Boolean)permissionChecksMap.get(permissionCacheKey);

		if (contains == null) {
			contains = doContains(permissionChecker, model, actionId);

			permissionChecksMap.put(permissionCacheKey, contains);
		}

		return contains;
	}

	@Override
	public Map<String, Object> getProperties() {
		return Collections.singletonMap("model.class.name", modelName);
	}

	@Override
	public Collection<Class<?>> getServices() {
		return Collections.singletonList(ModelPermission.class);
	}

	protected boolean doContains(
			PermissionChecker permissionChecker, T model, String actionId)
		throws PortalException {

		for (ModelPermissionCheck<T> modelPermissionCheck :
				_modelPermissionChecks) {

			Boolean contains = modelPermissionCheck.contains(
				this, permissionChecker, modelName, model, actionId);

			if (contains != null) {
				return contains;
			}
		}

		String primKey = String.valueOf(_primKeyFunction.applyAsLong(model));

		if (permissionChecker.hasOwnerPermission(
				model.getCompanyId(), modelName, primKey, model.getUserId(),
				actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			model.getGroupId(), modelName, primKey, actionId);
	}

	protected final String modelName;

	private final UnsafeLongFunction<T, ? extends PortalException>
		_getModelFunction;
	private final List<ModelPermissionCheck<T>> _modelPermissionChecks;
	private final ToLongFunction<T> _primKeyFunction;

}