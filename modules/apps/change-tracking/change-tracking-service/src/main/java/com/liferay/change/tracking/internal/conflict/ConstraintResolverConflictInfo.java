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

package com.liferay.change.tracking.internal.conflict;

import com.liferay.change.tracking.conflict.ConflictInfo;
import com.liferay.change.tracking.resolver.ConstraintResolver;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Preston Crary
 */
public class ConstraintResolverConflictInfo implements ConflictInfo {

	public ConstraintResolverConflictInfo(
		ConstraintResolver<?> constraintResolver, long productionPrimaryKey,
		long ctPrimaryKey, boolean resolved) {

		_constraintResolver = constraintResolver;
		_productionPrimaryKey = productionPrimaryKey;
		_ctPrimaryKey = ctPrimaryKey;
		_resolved = resolved;
	}

	@Override
	public String getConflictDescription(ResourceBundle resourceBundle) {
		return ResourceBundleUtil.getString(
			resourceBundle, _constraintResolver.getConflictDescriptionKey());
	}

	@Override
	public long getCTPrimaryKey() {
		return _ctPrimaryKey;
	}

	@Override
	public long getProductionPrimaryKey() {
		return _productionPrimaryKey;
	}

	@Override
	public String getResolutionDescription(ResourceBundle resourceBundle) {
		return ResourceBundleUtil.getString(
			resourceBundle, _constraintResolver.getResolutionDescriptionKey());
	}

	@Override
	public ResourceBundle getResourceBundle(Locale locale) {
		return _constraintResolver.getResourceBundle(locale);
	}

	@Override
	public boolean isResolved() {
		return _resolved;
	}

	private final ConstraintResolver<?> _constraintResolver;
	private final long _ctPrimaryKey;
	private final long _productionPrimaryKey;
	private final boolean _resolved;

}