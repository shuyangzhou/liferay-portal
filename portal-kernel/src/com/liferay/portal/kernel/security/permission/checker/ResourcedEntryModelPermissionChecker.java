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

import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ResourcedModel;

import java.io.Serializable;

/**
 * @author Preston Crary
 */
public abstract class ResourcedEntryModelPermissionChecker
	<T extends GroupedModel> extends EntryModelPermissionChecker<T> {

	@SafeVarargs
	public ResourcedEntryModelPermissionChecker(
		String modelName, ModelPermissionCheck<T>... modelPermissionChecks) {

		super(modelName, modelPermissionChecks);
	}

	@Override
	protected Serializable getPrimKey(T model) {
		ResourcedModel resourcedModel = (ResourcedModel)model;

		return resourcedModel.getResourcePrimKey();
	}

}