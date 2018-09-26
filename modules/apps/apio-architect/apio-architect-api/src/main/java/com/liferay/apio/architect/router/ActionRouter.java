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

package com.liferay.apio.architect.router;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.apio.architect.identifier.Identifier;

/**
 * Represents the mapping between HTTP requests for a resource and the functions
 * that perform those requests.
 *
 * @author Alejandro Hernández
 * @param  <T> the type to which the actions defined inside this router are
 *         applied
 * @review
 */
@ConsumerType
public interface ActionRouter<T extends Identifier<?>> {
}