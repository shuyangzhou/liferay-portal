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

package com.liferay.vulcan.resource.builder;

import aQute.bnd.annotation.ProviderType;

import com.liferay.vulcan.alias.BinaryFunction;
import com.liferay.vulcan.language.Language;
import com.liferay.vulcan.resource.Representor;
import com.liferay.vulcan.resource.identifier.Identifier;

import java.util.Date;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Creates generic representations of your domain models that Vulcan hypermedia
 * writers can understand.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@ProviderType
public interface RepresentorBuilder<T, U extends Identifier> {

	/**
	 * Provides a lambda function that can be used to obtain a model's {@link
	 * Identifier}.
	 *
	 * @param  identifierFunction lambda function used to obtain a model's
	 *         identifier
	 * @return the builder's next step
	 */
	public FirstStep<T, U> identifier(Function<T, U> identifierFunction);

	public interface FirstStep<T, U extends Identifier> {

		/**
		 * Adds information about the bidirectional relation of a linked model
		 * in the resource and a collection of {@link
		 * com.liferay.vulcan.resource.CollectionResource} items in the related
		 * resource.
		 *
		 * @param  key the relation's name in the resource
		 * @param  relatedKey the relation's name in the related resource
		 * @param  modelClass the related model's class
		 * @param  modelFunction the function used to get the related model
		 * @param  identifierFunction the function used to get the collection's
		 *         identifier
		 * @return the builder's step
		 */
		public <S> FirstStep<T, U> addBidirectionalModel(
			String key, String relatedKey, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction,
			Function<S, Identifier> identifierFunction);

		/**
		 * Adds binary files to a resource.
		 *
		 * @param  key the binary resource's name
		 * @param  binaryFunction the function used to get the binaries
		 * @return the builder's step
		 */
		public FirstStep<T, U> addBinary(
			String key, BinaryFunction<T> binaryFunction);

		/**
		 * Adds information about a resource's boolean field.
		 *
		 * @param  key the field's name
		 * @param  booleanFunction the function used to get the boolean value
		 * @return the builder's step
		 */
		public FirstStep<T, U> addBoolean(
			String key, Function<T, Boolean> booleanFunction);

		/**
		 * Adds information about a resource's date field.
		 *
		 * @param  key the field's name
		 * @param  dateFunction the function used to get the date value
		 * @return the builder's step
		 */
		public FirstStep<T, U> addDate(
			String key, Function<T, Date> dateFunction);

		/**
		 * Adds information about an embeddable related model.
		 *
		 * @param  key the relation's name
		 * @param  modelClass the related model's class
		 * @param  modelFunction the function used to get the related model
		 * @return the builder's step
		 */
		public <S> FirstStep<T, U> addEmbeddedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction);

		/**
		 * Adds information about a resource link.
		 *
		 * @param  key the field's name
		 * @param  url the link's URL
		 * @return the builder's step
		 */
		public FirstStep<T, U> addLink(String key, String url);

		/**
		 * Adds information about a non-embeddable related model.
		 *
		 * @param  key the relation's name
		 * @param  modelClass the related model's class
		 * @param  modelFunction the function used to get the related model
		 * @return the builder's step
		 */
		public <S> FirstStep<T, U> addLinkedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction);

		/**
		 * Use this method to provide information of a resource localized string
		 * field.
		 *
		 * @param  key name of the field.
		 * @param  stringFunction function used to obtain the string value.
		 * @return builder's actual step.
		 * @review
		 */
		public FirstStep<T, U> addLocalizedString(
			String key, BiFunction<T, Language, String> stringFunction);

		/**
		 * Adds information about a resource's number field.
		 *
		 * @param  key the field's name
		 * @param  numberFunction the function used to get the number's value
		 * @return the builder's step
		 */
		public FirstStep<T, U> addNumber(
			String key, Function<T, Number> numberFunction);

		/**
		 * Adds information about a related collection.
		 *
		 * @param  key the relation's name
		 * @param  modelClass the class of the collection's related models
		 * @param  identifierFunction the function used to get the collection's
		 *         identifier
		 * @return the builder's step
		 */
		public <S> FirstStep<T, U> addRelatedCollection(
			String key, Class<S> modelClass,
			Function<T, Identifier> identifierFunction);

		/**
		 * Adds information about a resource's string field.
		 *
		 * @param  key the field's name
		 * @param  stringFunction the function used to get the string's value
		 * @return the builder's step
		 */
		public FirstStep<T, U> addString(
			String key, Function<T, String> stringFunction);

		/**
		 * Adds a type for the model. Multiple types are allowed.
		 *
		 * @param  type the type name
		 * @return the builder's step
		 */
		public FirstStep<T, U> addType(String type);

		/**
		 * Constructs and returns a {@link Representor} instance with the
		 * information provided to the builder.
		 *
		 * @return the {@code Representor} instance
		 */
		public Representor<T, U> build();

	}

}