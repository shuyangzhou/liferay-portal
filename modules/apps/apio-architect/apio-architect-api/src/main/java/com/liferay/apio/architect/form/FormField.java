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

package com.liferay.apio.architect.form;

import aQute.bnd.annotation.ProviderType;

/**
 * Represents the field of a {@link Form}.
 *
 * @author Alejandro Hernández
 */
@ProviderType
public interface FormField {

	/**
	 * Returns the type of this field.
	 *
	 * @return the type of this field
	 */
	public FieldType getFieldType();

	/**
	 * Returns the name of the field.
	 *
	 * @return the name of the field
	 */
	public String getName();

	/**
	 * Returns {@code true} if the field is required in this {@code Form};
	 * {@code false} otherwise.
	 *
	 * @return @code <code>true</code>} if the field is required in the {@code
	 *         Form}; {@code false} otherwise
	 */
	public boolean isRequired();

}