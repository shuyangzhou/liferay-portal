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

package com.liferay.apio.architect.representor;

import aQute.bnd.annotation.ProviderType;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.related.RelatedCollection;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Holds information about the metadata supported for a resource.
 *
 * <p>
 * Instances of this interface should always be created by using a {@link
 * Representor.Builder}.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @see    Representor.Builder
 */
@ProviderType
public interface Representor<T> extends BaseRepresentor<T> {

	/**
	 * Returns the model's identifier.
	 *
	 * @param  model the model instance
	 * @return the model's identifier
	 */
	public Object getIdentifier(T model);

	/**
	 * Returns the related collections.
	 *
	 * @return the related collections
	 */
	public Stream<RelatedCollection<? extends Identifier>>
		getRelatedCollections();

	/**
	 * Creates generic representations of your domain models that Apio
	 * hypermedia writers can understand.
	 *
	 * @param <T> the model's type
	 * @param <S> the type of the model's identifier (e.g., {@code Long}, {@code
	 *        String}, etc.)
	 */
	@ProviderType
	public interface Builder<T, S> {

		/**
		 * Adds a type for the model.
		 *
		 * @param  type the type name
		 * @param  types the rest of the types
		 * @return the builder's step
		 */
		public IdentifierStep<T, S> types(String type, String... types);

	}

	@ProviderType
	public interface FirstStep<T>
		extends BaseFirstStep<T, Representor<T>, FirstStep<T>> {

		/**
		 * Adds information about the bidirectional relation of a linked
		 * resource in the actual resource and a collection of items in the
		 * related resource.
		 *
		 * @param  key the relation's name in the resource
		 * @param  relatedKey the relation's name in the related resource
		 * @param  identifierClass the related resource identifier's class
		 * @param  modelToIdentifierFunction the function used to get the
		 *         related resource's identifier
		 * @return the builder's step
		 */
		public <S> FirstStep<T> addBidirectionalModel(
			String key, String relatedKey,
			Class<? extends Identifier<S>> identifierClass,
			Function<T, S> modelToIdentifierFunction);

		/**
		 * Adds information about a related collection.
		 *
		 * @param  key the relation's name
		 * @param  itemIdentifierClass the class of the collection items'
		 *         identifier
		 * @return the builder's step
		 */
		public <S extends Identifier> FirstStep<T> addRelatedCollection(
			String key, Class<S> itemIdentifierClass);

	}

	@ProviderType
	public interface IdentifierStep<T, U> {

		/**
		 * Provides a lambda function that can be used to obtain a model's
		 * identifier.
		 *
		 * @param  modelToIdentifierFunction lambda function used to obtain a
		 *         model's identifier
		 * @return the builder's next step
		 */
		public FirstStep<T> identifier(
			Function<T, U> modelToIdentifierFunction);

	}

}