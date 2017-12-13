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

package com.liferay.vulcan.response.control;

import aQute.bnd.annotation.ProviderType;

import java.util.List;
import java.util.function.Predicate;

/**
 * Defines the fields context selected by clients. An instance of this interface
 * is handed to {@code javax.ws.rs.ext.MessageBodyWriter} to decide which
 * resource fields to write.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@ProviderType
public interface Fields {

	/**
	 * Returns the predicate for the list of types to test if a certain field
	 * should be added to the representation.
	 *
	 * @param  types the resource's list of types
	 * @return the predicate for the list of types
	 */
	public Predicate<String> getFieldsPredicate(List<String> types);

}