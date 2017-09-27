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

import com.liferay.vulcan.alias.BinaryFunction;
import com.liferay.vulcan.resource.identifier.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Instances of this interface will hold information about the metadata
 * supported for a certain {@link CollectionResource}.
 *
 * <p>
 * Instances of this interface should always be created by using a {@link
 * com.liferay.vulcan.resource.builder.RepresentorBuilder}.
 * </p>
 *
 * @author Alejandro Hernández
 * @see    com.liferay.vulcan.resource.builder.RepresentorBuilder
 */
public interface Representor<T, U extends Identifier> {

	/**
	 * Returns the binary resources linked to a model.
	 *
	 * @return the binary resources.
	 */
	public Map<String, BinaryFunction<T>> getBinaryFunctions();

	/**
	 * Returns the embedded related models.
	 *
	 * @return the embedded related models.
	 */
	public List<RelatedModel<T, ?>> getEmbeddedRelatedModels();

	/**
	 * Returns a map containing the field names and the functions to get those
	 * fields.
	 *
	 * @return the field names and field functions.
	 */
	public Map<String, Function<T, Object>> getFieldFunctions();

	/**
	 * Returns the identifier of the model.
	 *
	 * @param  model the model instance.
	 * @return the identifier of the model.
	 */
	public U getIdentifier(T model);

	/**
	 * Returns the identifier class.
	 *
	 * @return the identifier class.
	 */
	public Class<U> getIdentifierClass();

	/**
	 * Returns the linked related models.
	 *
	 * @return the linked related models.
	 */
	public List<RelatedModel<T, ?>> getLinkedRelatedModels();

	/**
	 * Returns the links.
	 *
	 * @return the links.
	 */
	public Map<String, String> getLinks();

	/**
	 * Returns the related collections.
	 *
	 * @return the related collections.
	 */
	public Stream<RelatedCollection<T, ?>> getRelatedCollections();

	/**
	 * Returns the types.
	 *
	 * @return the types.
	 */
	public List<String> getTypes();

}