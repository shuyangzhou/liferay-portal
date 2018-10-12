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

package com.liferay.apio.architect.internal.representor;

import com.liferay.apio.architect.identifier.Identifier;
import com.liferay.apio.architect.internal.related.RelatedCollectionImpl;
import com.liferay.apio.architect.related.RelatedCollection;
import com.liferay.apio.architect.representor.Representor;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Alejandro Hernández
 */
public class RepresentorImpl<T>
	extends BaseRepresentorImpl<T> implements Representor<T> {

	@Override
	public Object getIdentifier(T model) {
		return _modelToIdentifierFunction.apply(model);
	}

	@Override
	public boolean isNested() {
		return false;
	}

	public static class BuilderImpl<T, S>
		extends BaseBuilderImpl<T, RepresentorImpl<T>>
		implements Builder<T, S> {

		public BuilderImpl(
			Class<? extends Identifier<S>> identifierClass,
			Function<Class<? extends Identifier<?>>, String> nameFunction) {

			this(
				identifierClass, nameFunction,
				(clazz, relatedCollection) -> {
				},
				Collections::emptyList);
		}

		public BuilderImpl(
			Class<? extends Identifier<S>> identifierClass,
			Function<Class<? extends Identifier<?>>, String> nameFunction,
			BiConsumer<Class<?>, RelatedCollection<T, ?>> biConsumer,
			Supplier<List<RelatedCollection<T, ?>>> supplier) {

			super(new RepresentorImpl<>(nameFunction, supplier));

			_identifierClass = identifierClass;
			_biConsumer = biConsumer;
		}

		@Override
		public IdentifierStepImpl types(String type, String... types) {
			baseRepresentor.addTypes(type, types);

			return new IdentifierStepImpl();
		}

		public class FirstStepImpl
			extends BaseFirstStepImpl<Representor<T>, FirstStep<T>>
			implements FirstStep<T> {

			@Override
			public <U> FirstStep<T> addBidirectionalModel(
				String key, String relatedKey,
				Class<? extends Identifier<U>> identifierClass,
				Function<T, U> modelToIdentifierFunction) {

				RelatedCollection<T, ?> relatedCollection =
					new RelatedCollectionImpl<>(relatedKey, _identifierClass);

				_biConsumer.accept(identifierClass, relatedCollection);

				baseRepresentor.addRelatedModel(
					key, identifierClass, modelToIdentifierFunction);

				return this;
			}

			@Override
			public <U extends Identifier> FirstStep<T> addRelatedCollection(
				String key, Class<U> itemIdentifierClass) {

				baseRepresentor.addRelatedCollection(
					key, itemIdentifierClass, null);

				return this;
			}

			@Override
			public FirstStepImpl getThis() {
				return this;
			}

		}

		public class IdentifierStepImpl implements IdentifierStep<T, S> {

			@Override
			public FirstStep<T> identifier(
				Function<T, S> modelToIdentifierFunction) {

				baseRepresentor._setIdentifierFunction(
					modelToIdentifierFunction);

				return new FirstStepImpl();
			}

			private IdentifierStepImpl() {
			}

		}

		private final BiConsumer<Class<?>, RelatedCollection<T, ?>> _biConsumer;
		private final Class<? extends Identifier> _identifierClass;

	}

	private RepresentorImpl(
		Function<Class<? extends Identifier<?>>, String> nameFunction,
		Supplier<List<RelatedCollection<T, ?>>> supplier) {

		super(nameFunction, supplier);
	}

	private void _setIdentifierFunction(
		Function<T, ?> modelToIdentifierFunction) {

		_modelToIdentifierFunction = modelToIdentifierFunction;
	}

	private Function<T, ?> _modelToIdentifierFunction;

}