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

package com.liferay.apio.architect.related;

import aQute.bnd.annotation.ProviderType;

import com.liferay.apio.architect.identifier.Identifier;

import java.util.function.Function;

/**
 * Represents the relation between two resources.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 * @param  <T> the model's type
 * @param  <S> the model identifier's type (e.g., {@code Long}, {@code String},
 *         etc.)
 */
@ProviderType
public interface RelatedModel<T, S> {

	/**
	 * Returns the related resource identifier's class.
	 *
	 * @return the related resource identifier's class
	 */
	public Class<? extends Identifier<S>> getIdentifierClass();

	/**
	 * Returns the function you can use to retrieve the related resource's
	 * identifier.
	 *
	 * @return     the function that calculates the related resource's
	 *             identifier
	 * @deprecated As of 1.1.1, replaced by {@link
	 *             #getModelToIdentifierFunction()}
	 */
	@Deprecated
	public Function<T, S> getIdentifierFunction();

	/**
	 * Returns the relation's key.
	 *
	 * @return the relation's key
	 */
	public String getKey();

	/**
	 * Returns the function you can use to retrieve the related resource's
	 * identifier.
	 *
	 * @return the function that calculates the related resource's identifier
	 */
	public Function<T, S> getModelToIdentifierFunction();

}