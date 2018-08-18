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

package com.liferay.apio.architect.impl.wiring.osgi.validator;

/**
 * Checks if a resource name is valid.
 *
 * @author Víctor Galán
 */
public interface NameValidator {

	/**
	 * Returns the validation error.
	 *
	 * @return the validation error.
	 */
	public String getValidationError();

	/**
	 * Whether a resource name is valid.
	 *
	 * @param  name the name
	 * @return {@code true} if the name is valid; {@code false} otherwise
	 */
	public boolean validate(String name);

}