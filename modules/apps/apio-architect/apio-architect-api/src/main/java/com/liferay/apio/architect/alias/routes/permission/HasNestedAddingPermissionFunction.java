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

package com.liferay.apio.architect.alias.routes.permission;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;

/**
 * Defines a type alias for a function that can be used to check for permissions
 * for adding an item to a nested collection.
 *
 * @author Alejandro Hernández
 * @param  <T> the type of the parent model's identifier (e.g., {@code Long},
 *         {@code String}, etc.)
 */
@ConsumerType
@FunctionalInterface
public interface HasNestedAddingPermissionFunction<T>
	extends ThrowableBiFunction<Credentials, T, Boolean> {
}