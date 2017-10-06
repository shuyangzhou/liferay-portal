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

package com.liferay.vulcan.wiring.osgi.internal.resource.builder;

import com.liferay.vulcan.alias.BinaryFunction;
import com.liferay.vulcan.consumer.TriConsumer;
import com.liferay.vulcan.resource.RelatedCollection;
import com.liferay.vulcan.resource.RelatedModel;
import com.liferay.vulcan.resource.Representor;
import com.liferay.vulcan.resource.builder.RepresentorBuilder;
import com.liferay.vulcan.resource.identifier.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
public class RepresentorBuilderImpl<T, U extends Identifier>
	implements RepresentorBuilder<T, U> {

	public RepresentorBuilderImpl(
		Class<U> identifierClass,
		TriConsumer<String, Class<?>, Function<Object, Identifier>>
			addRelatedCollectionTriConsumer,
		Supplier<List<RelatedCollection<T, ?>>> relatedCollectionsSupplier) {

		_identifierClass = identifierClass;
		_addRelatedCollectionTriConsumer = addRelatedCollectionTriConsumer;
		_relatedCollectionsSupplier = relatedCollectionsSupplier;
	}

	@Override
	public FirstStep<T, U> identifier(Function<T, U> identifierFunction) {
		return new FirstStepImpl(identifierFunction);
	}

	public class RepresentorImpl implements Representor<T, U> {

		public RepresentorImpl(Function<T, U> identifierFunction) {
			_identifierFunction = identifierFunction;
		}

		@Override
		public Map<String, BinaryFunction<T>> getBinaryFunctions() {
			return _binaryFunctions;
		}

		@Override
		public List<RelatedModel<T, ?>> getEmbeddedRelatedModels() {
			return _embeddedRelatedModels;
		}

		@Override
		public Map<String, Function<T, Object>> getFieldFunctions() {
			return _fieldFunctions;
		}

		@Override
		public U getIdentifier(T model) {
			return _identifierFunction.apply(model);
		}

		@Override
		public Class<U> getIdentifierClass() {
			return _identifierClass;
		}

		@Override
		public List<RelatedModel<T, ?>> getLinkedRelatedModels() {
			return _linkedRelatedModels;
		}

		@Override
		public Map<String, String> getLinks() {
			return _links;
		}

		@Override
		public Stream<RelatedCollection<T, ?>> getRelatedCollections() {
			Stream<List<RelatedCollection<T, ?>>> stream = Stream.of(
				_relatedCollections, _relatedCollectionsSupplier.get());

			return stream.filter(
				Objects::nonNull
			).flatMap(
				Collection::stream
			);
		}

		@Override
		public List<String> getTypes() {
			return _types;
		}

		private void _addBinary(String key, BinaryFunction<T> binaryFunction) {
			_binaryFunctions.put(key, binaryFunction);
		}

		private <S> void _addEmbeddedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction) {

			_embeddedRelatedModels.add(
				new RelatedModel<>(key, modelClass, modelFunction));
		}

		private void _addField(String key, Function<T, Object> fieldFunction) {
			_fieldFunctions.put(key, fieldFunction);
		}

		private void _addLink(String key, String url) {
			_links.put(key, url);
		}

		private <S> void _addLinkedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction) {

			_linkedRelatedModels.add(
				new RelatedModel<>(key, modelClass, modelFunction));
		}

		private <S> void _addRelatedCollection(
			String key, Class<S> modelClass,
			Function<T, Identifier> identifierFunction) {

			_relatedCollections.add(
				new RelatedCollection<>(key, modelClass, identifierFunction));
		}

		private void _addType(String type) {
			_types.add(type);
		}

		private Map<String, BinaryFunction<T>> _binaryFunctions =
			new HashMap<>();
		private List<RelatedModel<T, ?>> _embeddedRelatedModels =
			new ArrayList<>();
		private Map<String, Function<T, Object>> _fieldFunctions =
			new HashMap<>();
		private final Function<T, U> _identifierFunction;
		private List<RelatedModel<T, ?>> _linkedRelatedModels =
			new ArrayList<>();
		private Map<String, String> _links = new HashMap<>();
		private List<RelatedCollection<T, ?>> _relatedCollections =
			new ArrayList<>();
		private List<String> _types = new ArrayList<>();

	}

	private final TriConsumer<String, Class<?>, Function<Object, Identifier>>
		_addRelatedCollectionTriConsumer;
	private final Class<U> _identifierClass;
	private final Supplier<List<RelatedCollection<T, ?>>>
		_relatedCollectionsSupplier;

	private class FirstStepImpl implements FirstStep<T, U> {

		public FirstStepImpl(Function<T, U> identifierFunction) {
			_representor = new RepresentorImpl(identifierFunction);
		}

		@Override
		public <S> FirstStep<T, U> addBidirectionalModel(
			String key, String relatedKey, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction,
			Function<S, Identifier> identifierFunction) {

			_representor._addLinkedModel(key, modelClass, modelFunction);

			_addRelatedCollectionTriConsumer.accept(
				relatedKey, modelClass,
				(Function<Object, Identifier>)identifierFunction);

			return this;
		}

		@Override
		public FirstStep<T, U> addBinary(
			String key, BinaryFunction<T> binaryFunction) {

			_representor._addBinary(key, binaryFunction);

			return this;
		}

		@Override
		public <S> FirstStep<T, U> addEmbeddedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction) {

			_representor._addEmbeddedModel(key, modelClass, modelFunction);

			return this;
		}

		@Override
		public FirstStep<T, U> addField(
			String key, Function<T, Object> fieldFunction) {

			_representor._addField(key, fieldFunction);

			return this;
		}

		@Override
		public FirstStep<T, U> addLink(String key, String url) {
			_representor._addLink(key, url);

			return this;
		}

		@Override
		public <S> FirstStep<T, U> addLinkedModel(
			String key, Class<S> modelClass,
			Function<T, Optional<S>> modelFunction) {

			_representor._addLinkedModel(key, modelClass, modelFunction);

			return this;
		}

		@Override
		public <S> FirstStep<T, U> addRelatedCollection(
			String key, Class<S> modelClass,
			Function<T, Identifier> identifierFunction) {

			_representor._addRelatedCollection(
				key, modelClass, identifierFunction);

			return this;
		}

		@Override
		public FirstStep<T, U> addType(String type) {
			_representor._addType(type);

			return this;
		}

		@Override
		public Representor<T, U> build() {
			return _representor;
		}

		private final RepresentorImpl _representor;

	}

}