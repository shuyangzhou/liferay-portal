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

package com.liferay.vulcan.resource;

import aQute.bnd.annotation.ProviderType;

import com.liferay.vulcan.pagination.Page;
import com.liferay.vulcan.pagination.SingleModel;
import com.liferay.vulcan.resource.identifier.Identifier;
import com.liferay.vulcan.uri.Path;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Holds information about the routes supported for a {@link
 * CollectionResource}.
 *
 * <p>
 * This interface's methods return functions to get the different endpoints of
 * the collection resource. You should always use a {@link
 * com.liferay.vulcan.resource.builder.RoutesBuilder} to create instances of
 * this interface.
 * </p>
 *
 * @author Alejandro Hernández
 * @see    com.liferay.vulcan.resource.builder.RoutesBuilder
 */
@ProviderType
public interface Routes<T> {

	/**
	 * Returns the function used to remove a single model of a {@link
	 * CollectionResource}, if the endpoint was added through the {@link
	 * com.liferay.vulcan.resource.builder.RoutesBuilder} and the function
	 * therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to remove a single model, if the function
	 *         exists; {@code Optional#empty()} otherwise
	 */
	public Optional<Consumer<Path>> getDeleteSingleModelConsumerOptional();

	/**
	 * Returns the function used to create the page of a {@link
	 * CollectionResource}, if the endpoint was added through the {@link
	 * com.liferay.vulcan.resource.builder.RoutesBuilder} and the function
	 * therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to create the page, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<Function<Path, Function<Identifier, Page<T>>>>
		getPageFunctionOptional();

	/**
	 * Returns the function that uses a POST request to create the single model
	 * of a {@link CollectionResource}, if the endpoint was added through the
	 * {@link com.liferay.vulcan.resource.builder.RoutesBuilder} and the
	 * function therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function that uses a POST request to create the single model,
	 *         if the function exists; {@code Optional#empty()} otherwise
	 */
	public Optional<Function<Identifier, Function<Map<String, Object>,
		SingleModel<T>>>> getPostSingleModelFunctionOptional();

	/**
	 * Returns the function that uses a GET request to retrieve the single model
	 * of a {@link CollectionResource}, if the endpoint was added through the
	 * {@link com.liferay.vulcan.resource.builder.RoutesBuilder} and the
	 * function therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function that uses a GET request to retrieve the single
	 *         model, if the function exists; {@code Optional#empty()} otherwise
	 */
	public Optional<Function<Path, SingleModel<T>>>
		getSingleModelFunctionOptional();

	/**
	 * Returns the function used to update the single model of a {@link
	 * CollectionResource}, if the endpoint was added through the {@link
	 * com.liferay.vulcan.resource.builder.RoutesBuilder} and the function
	 * therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to update the single model, if the function
	 *         exists; {@code Optional#empty()} otherwise
	 */
	public Optional<Function<Path, Function<Map<String, Object>,
		SingleModel<T>>>> getUpdateSingleModelFunctionOptional();

}