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

import com.liferay.vulcan.consumer.DecaConsumer;
import com.liferay.vulcan.consumer.EnneaConsumer;
import com.liferay.vulcan.consumer.HeptaConsumer;
import com.liferay.vulcan.consumer.HexaConsumer;
import com.liferay.vulcan.consumer.OctaConsumer;
import com.liferay.vulcan.consumer.PentaConsumer;
import com.liferay.vulcan.consumer.TetraConsumer;
import com.liferay.vulcan.consumer.TriConsumer;
import com.liferay.vulcan.function.DecaFunction;
import com.liferay.vulcan.function.EnneaFunction;
import com.liferay.vulcan.function.HeptaFunction;
import com.liferay.vulcan.function.HexaFunction;
import com.liferay.vulcan.function.OctaFunction;
import com.liferay.vulcan.function.PentaFunction;
import com.liferay.vulcan.function.TetraFunction;
import com.liferay.vulcan.function.TriFunction;
import com.liferay.vulcan.function.UndecaFunction;
import com.liferay.vulcan.pagination.PageItems;
import com.liferay.vulcan.pagination.Pagination;
import com.liferay.vulcan.resource.Routes;
import com.liferay.vulcan.resource.identifier.Identifier;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Creates {@link Routes} of a {@link
 * com.liferay.vulcan.resource.CollectionResource}.
 *
 * @author Alejandro Hernández
 */
@ProviderType
@SuppressWarnings("unused")
public interface RoutesBuilder<T, U extends Identifier> {

	/**
	 * Adds a route to a collection page function with two parameters.
	 *
	 * @param  biFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @return the updated builder
	 */
	public <V extends Identifier> RoutesBuilder<T, U> addCollectionPageGetter(
		BiFunction<Pagination, V, PageItems<T>> biFunction,
		Class<V> identifierClass);

	/**
	 * Adds a route to a collection page function with ten parameters.
	 *
	 * @param  decaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @param  dClass the class of the page function's sixth parameter
	 * @param  eClass the class of the page function's seventh parameter
	 * @param  fClass the class of the page function's eighth parameter
	 * @param  gClass the class of the page function's ninth parameter
	 * @param  hClass the class of the page function's tenth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C, D, E, F, G, H> RoutesBuilder<T, U>
		addCollectionPageGetter(
			DecaFunction<Pagination, V, A, B, C, D, E, F, G, H,
				PageItems<T>> decaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass,
			Class<G> gClass, Class<H> hClass);

	/**
	 * Adds a route to a collection page function with nine parameters.
	 *
	 * @param  enneaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @param  dClass the class of the page function's sixth parameter
	 * @param  eClass the class of the page function's seventh parameter
	 * @param  fClass the class of the page function's eighth parameter
	 * @param  gClass the class of the page function's ninth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C, D, E, F, G> RoutesBuilder<T, U>
		addCollectionPageGetter(
			EnneaFunction<Pagination, V, A, B, C, D, E, F, G, PageItems<T>>
				enneaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass,
			Class<G> gClass);

	/**
	 * Adds a route to a collection page function with seven parameters.
	 *
	 * @param  heptaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @param  dClass the class of the page function's sixth parameter
	 * @param  eClass the class of the page function's seventh parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C, D, E> RoutesBuilder<T, U>
		addCollectionPageGetter(
			HeptaFunction<Pagination, V, A, B, C, D, E, PageItems<T>>
				heptaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass);

	/**
	 * Adds a route to a collection page function with six parameters.
	 *
	 * @param  hexaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @param  dClass the class of the page function's sixth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C, D> RoutesBuilder<T, U>
		addCollectionPageGetter(
			HexaFunction<Pagination, V, A, B, C, D, PageItems<T>> hexaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass);

	/**
	 * Adds a route to a collection page function with eight parameters.
	 *
	 * @param  octaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @param  dClass the class of the page function's sixth parameter
	 * @param  eClass the class of the page function's seventh parameter
	 * @param  fClass the class of the page function's eighth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C, D, E, F> RoutesBuilder<T, U>
		addCollectionPageGetter(
			OctaFunction<Pagination, V, A, B, C, D, E, F, PageItems<T>>
				octaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass);

	/**
	 * Adds a route to a collection page function with five parameters.
	 *
	 * @param  pentaFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @param  cClass the class of the page function's fifth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B, C> RoutesBuilder<T, U>
		addCollectionPageGetter(
			PentaFunction<Pagination, V, A, B, C, PageItems<T>> pentaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass);

	/**
	 * Adds a route to a collection page function with four parameters.
	 *
	 * @param  tetraFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @param  bClass the class of the page function's fourth parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A, B> RoutesBuilder<T, U>
		addCollectionPageGetter(
			TetraFunction<Pagination, V, A, B, PageItems<T>> tetraFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass);

	/**
	 * Adds a route to a collection page function with three parameters.
	 *
	 * @param  triFunction the function that calculates the page
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the page function's third parameter
	 * @return the updated builder
	 */
	public <V extends Identifier, A> RoutesBuilder<T, U>
		addCollectionPageGetter(
			TriFunction<Pagination, V, A, PageItems<T>> triFunction,
			Class<V> identifierClass, Class<A> aClass);

	/**
	 * Adds a route to a single model POST function with two parameters.
	 *
	 * @param  biFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @return the updated builder
	 */
	public <V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			BiFunction<V, Map<String, Object>, T> biFunction,
			Class<V> identifierClass);

	/**
	 * Adds a route to a single model POST function with ten parameters.
	 *
	 * @param  decaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @param  eClass the class of the single model POST function's seventh
	 *         parameter
	 * @param  fClass the class of the single model POST function's eighth
	 *         parameter
	 * @param  gClass the class of the single model POST function's ninth
	 *         parameter
	 * @param  hClass the class of the single model POST function's tenth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			DecaFunction<V, Map<String, Object>, A, B, C, D, E, F, G, H, T>
				decaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass,
			Class<G> gClass, Class<H> hClass);

	/**
	 * Adds a route to a single model POST function with nine parameters.
	 *
	 * @param  enneaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @param  eClass the class of the single model POST function's seventh
	 *         parameter
	 * @param  fClass the class of the single model POST function's eighth
	 *         parameter
	 * @param  gClass the class of the single model POST function's ninth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			EnneaFunction<V, Map<String, Object>, A, B, C, D, E, F, G, T>
				enneaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass,
			Class<G> gClass);

	/**
	 * Adds a route to a single model POST function with seven parameters.
	 *
	 * @param  heptaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @param  eClass the class of the single model POST function's seventh
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			HeptaFunction<V, Map<String, Object>, A, B, C, D, E, T>
				heptaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass);

	/**
	 * Adds a route to a single model POST function with six parameters.
	 *
	 * @param  hexaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			HexaFunction<V, Map<String, Object>, A, B, C, D, T> hexaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass);

	/**
	 * Adds a route to a single model POST function with eight parameters.
	 *
	 * @param  octaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @param  eClass the class of the single model POST function's seventh
	 *         parameter
	 * @param  fClass the class of the single model POST function's eighth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			OctaFunction<V, Map<String, Object>, A, B, C, D, E, F, T>
				octaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass);

	/**
	 * Adds a route to a single model POST function with five parameters.
	 *
	 * @param  pentaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			PentaFunction<V, Map<String, Object>, A, B, C, T> pentaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass);

	/**
	 * Adds a route to a single model POST function with four parameters.
	 *
	 * @param  tetraFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			TetraFunction<V, Map<String, Object>, A, B, T> tetraFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass);

	/**
	 * Adds a route to a single model POST function with three parameters.
	 *
	 * @param  triFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @return the updated builder
	 */
	public <A, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			TriFunction<V, Map<String, Object>, A, T> triFunction,
			Class<V> identifierClass, Class<A> aClass);

	/**
	 * Adds a route to a single model POST function with eleven parameters.
	 *
	 * @param  undecaFunction the POST function that adds the single model
	 * @param  identifierClass the identifier's class
	 * @param  aClass the class of the single model POST function's third
	 *         parameter
	 * @param  bClass the class of the single model POST function's fourth
	 *         parameter
	 * @param  cClass the class of the single model POST function's fifth
	 *         parameter
	 * @param  dClass the class of the single model POST function's sixth
	 *         parameter
	 * @param  eClass the class of the single model POST function's seventh
	 *         parameter
	 * @param  fClass the class of the single model POST function's eighth
	 *         parameter
	 * @param  gClass the class of the single model POST function's ninth
	 *         parameter
	 * @param  hClass the class of the single model POST function's tenth
	 *         parameter
	 * @param  iClass the class of the single model POST function's eleventh
	 *         parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H, I, V extends Identifier> RoutesBuilder<T, U>
		addCollectionPageItemCreator(
			UndecaFunction<V, Map<String, Object>, A, B, C, D, E, F, G, H, I, T>
				undecaFunction,
			Class<V> identifierClass, Class<A> aClass, Class<B> bClass,
			Class<C> cClass, Class<D> dClass, Class<E> eClass, Class<F> fClass,
			Class<G> gClass, Class<H> hClass, Class<I> iClass);

	/**
	 * Adds a route to a single model function with two parameters.
	 *
	 * @param  biFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @return the updated builder
	 */
	public <A> RoutesBuilder<T, U> addCollectionPageItemGetter(
		BiFunction<U, A, T> biFunction, Class<A> aClass);

	/**
	 * Adds a route to a single model function with ten parameters.
	 *
	 * @param  decaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @param  eClass the class of the single model function's sixth parameter
	 * @param  fClass the class of the single model function's seventh parameter
	 * @param  gClass the class of the single model function's eighth parameter
	 * @param  hClass the class of the single model function's ninth parameter
	 * @param  iClass the class of the single model function's tenth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H, I> RoutesBuilder<T, U>
		addCollectionPageItemGetter(
			DecaFunction<U, A, B, C, D, E, F, G, H, I, T> decaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass,
			Class<I> iClass);

	/**
	 * Adds a route to a single model function with nine parameters.
	 *
	 * @param  enneaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @param  eClass the class of the single model function's sixth parameter
	 * @param  fClass the class of the single model function's seventh parameter
	 * @param  gClass the class of the single model function's eighth parameter
	 * @param  hClass the class of the single model function's ninth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H> RoutesBuilder<T, U>
		addCollectionPageItemGetter(
			EnneaFunction<U, A, B, C, D, E, F, G, H, T> enneaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass);

	/**
	 * Adds a route to a single model function.
	 *
	 * @param  function the function that calculates the single model
	 * @return the updated builder
	 */
	public RoutesBuilder<T, U> addCollectionPageItemGetter(
		Function<U, T> function);

	/**
	 * Adds a route to a single model function with seven parameters.
	 *
	 * @param  heptaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @param  eClass the class of the single model function's sixth parameter
	 * @param  fClass the class of the single model function's seventh parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F> RoutesBuilder<T, U> addCollectionPageItemGetter(
		HeptaFunction<U, A, B, C, D, E, F, T> heptaFunction, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass, Class<E> eClass,
		Class<F> fClass);

	/**
	 * Adds a route to a single model function with six parameters.
	 *
	 * @param  hexaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @param  eClass the class of the single model function's sixth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E> RoutesBuilder<T, U> addCollectionPageItemGetter(
		HexaFunction<U, A, B, C, D, E, T> hexaFunction, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass, Class<E> eClass);

	/**
	 * Adds a route to a single model function with eight parameters.
	 *
	 * @param  octaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @param  eClass the class of the single model function's sixth parameter
	 * @param  fClass the class of the single model function's seventh parameter
	 * @param  gClass the class of the single model function's eighth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G> RoutesBuilder<T, U>
		addCollectionPageItemGetter(
			OctaFunction<U, A, B, C, D, E, F, G, T> octaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass);

	/**
	 * Adds a route to a single model function with five parameters.
	 *
	 * @param  pentaFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @param  dClass the class of the single model function's fifth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D> RoutesBuilder<T, U> addCollectionPageItemGetter(
		PentaFunction<U, A, B, C, D, T> pentaFunction, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass);

	/**
	 * Adds a route to a single model function with four parameters.
	 *
	 * @param  tetraFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @param  cClass the class of the single model function's fourth parameter
	 * @return the updated builder
	 */
	public <A, B, C> RoutesBuilder<T, U> addCollectionPageItemGetter(
		TetraFunction<U, A, B, C, T> tetraFunction, Class<A> aClass,
		Class<B> bClass, Class<C> cClass);

	/**
	 * Adds a route to a single model function with three parameters.
	 *
	 * @param  triFunction the function that calculates the single model
	 * @param  aClass the class of the single model function's second parameter
	 * @param  bClass the class of the single model function's third parameter
	 * @return the updated builder
	 */
	public <A, B> RoutesBuilder<T, U> addCollectionPageItemGetter(
		TriFunction<U, A, B, T> triFunction, Class<A> aClass, Class<B> bClass);

	/**
	 * Adds a route to a collection item remover function with two parameters.
	 *
	 * @param  biConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @return the updated builder
	 */
	public <A> RoutesBuilder<T, U> addCollectionPageItemRemover(
		BiConsumer<U, A> biConsumer, Class<A> aClass);

	/**
	 * Adds a route to a collection item remover function.
	 *
	 * @param  consumer the function that deletes the collection item
	 * @return the updated builder
	 */
	public RoutesBuilder<T, U> addCollectionPageItemRemover(
		Consumer<U> consumer);

	/**
	 * Adds a route to a collection item remover function with ten parameters.
	 *
	 * @param  decaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @param  eClass the class of the remover function's sixth parameter
	 * @param  fClass the class of the remover function's seventh parameter
	 * @param  gClass the class of the remover function's eighth parameter
	 * @param  hClass the class of the remover function's ninth parameter
	 * @param  iClass the class of the remover function's tenth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H, I> RoutesBuilder<T, U>
		addCollectionPageItemRemover(
			DecaConsumer<U, A, B, C, D, E, F, G, H, I> decaConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass,
			Class<I> iClass);

	/**
	 * Adds a route to a collection item remover function with nine parameters.
	 *
	 * @param  enneaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @param  eClass the class of the remover function's sixth parameter
	 * @param  fClass the class of the remover function's seventh parameter
	 * @param  gClass the class of the remover function's eighth parameter
	 * @param  hClass the class of the remover function's ninth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H> RoutesBuilder<T, U>
		addCollectionPageItemRemover(
			EnneaConsumer<U, A, B, C, D, E, F, G, H> enneaConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass);

	/**
	 * Adds a route to a collection item remover function with seven parameters.
	 *
	 * @param  heptaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @param  eClass the class of the remover function's sixth parameter
	 * @param  fClass the class of the remover function's seventh parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F> RoutesBuilder<T, U> addCollectionPageItemRemover(
		HeptaConsumer<U, A, B, C, D, E, F> heptaConsumer, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass, Class<E> eClass,
		Class<F> fClass);

	/**
	 * Adds a route to a collection item remover function with six parameters.
	 *
	 * @param  hexaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @param  eClass the class of the remover function's sixth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E> RoutesBuilder<T, U> addCollectionPageItemRemover(
		HexaConsumer<U, A, B, C, D, E> hexaConsumer, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass, Class<E> eClass);

	/**
	 * Adds a route to a collection item remover function with eight parameters.
	 *
	 * @param  octaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @param  eClass the class of the remover function's sixth parameter
	 * @param  fClass the class of the remover function's seventh parameter
	 * @param  gClass the class of the remover function's eighth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G> RoutesBuilder<T, U>
		addCollectionPageItemRemover(
			OctaConsumer<U, A, B, C, D, E, F, G> octaConsumer, Class<A> aClass,
			Class<B> bClass, Class<C> cClass, Class<D> dClass, Class<E> eClass,
			Class<F> fClass, Class<G> gClass);

	/**
	 * Adds a route to a collection item remover function with five parameters.
	 *
	 * @param  pentaConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @param  dClass the class of the remover function's fifth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D> RoutesBuilder<T, U> addCollectionPageItemRemover(
		PentaConsumer<U, A, B, C, D> pentaConsumer, Class<A> aClass,
		Class<B> bClass, Class<C> cClass, Class<D> dClass);

	/**
	 * Adds a route to a collection item remover function with four parameters.
	 *
	 * @param  tetraConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @param  cClass the class of the remover function's fourth parameter
	 * @return the updated builder
	 */
	public <A, B, C> RoutesBuilder<T, U> addCollectionPageItemRemover(
		TetraConsumer<U, A, B, C> tetraConsumer, Class<A> aClass,
		Class<B> bClass, Class<C> cClass);

	/**
	 * Adds a route to a collection item remover function with three parameters.
	 *
	 * @param  triConsumer the function that deletes the collection item
	 * @param  aClass the class of the remover function's second parameter
	 * @param  bClass the class of the remover function's third parameter
	 * @return the updated builder
	 */
	public <A, B> RoutesBuilder<T, U> addCollectionPageItemRemover(
		TriConsumer<U, A, B> triConsumer, Class<A> aClass, Class<B> bClass);

	/**
	 * Adds a route to a collection item updater function with two parameters.
	 *
	 * @param  biFunction the function that updates the collection item
	 * @return the updated builder
	 */
	public RoutesBuilder<T, U> addCollectionPageItemUpdater(
		BiFunction<U, Map<String, Object>, T> biFunction);

	/**
	 * Adds a route to a collection item updater function with ten parameters.
	 *
	 * @param  decaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @param  eClass the class of the updater function's seventh parameter
	 * @param  fClass the class of the updater function's eighth parameter
	 * @param  gClass the class of the updater function's ninth parameter
	 * @param  hClass the class of the updater function's tenth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H> RoutesBuilder<T, U>
		addCollectionPageItemUpdater(
			DecaFunction<U, Map<String, Object>, A, B, C, D, E, F, G, H, T>
				decaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass);

	/**
	 * Adds a route to a collection item updater function with nine parameters.
	 *
	 * @param  enneaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @param  eClass the class of the updater function's seventh parameter
	 * @param  fClass the class of the updater function's eighth parameter
	 * @param  gClass the class of the updater function's ninth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G> RoutesBuilder<T, U>
		addCollectionPageItemUpdater(
			EnneaFunction<U, Map<String, Object>, A, B, C, D, E, F, G, T>
				enneaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass);

	/**
	 * Adds a route to a collection item updater function with seven parameters.
	 *
	 * @param  heptaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @param  eClass the class of the updater function's seventh parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		HeptaFunction<U, Map<String, Object>, A, B, C, D, E, T> heptaFunction,
		Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
		Class<E> eClass);

	/**
	 * Adds a route to a collection item updater function with six parameters.
	 *
	 * @param  hexaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		HexaFunction<U, Map<String, Object>, A, B, C, D, T> hexaFunction,
		Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass);

	/**
	 * Adds a route to a collection item updater function with eight parameters.
	 *
	 * @param  octaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @param  eClass the class of the updater function's seventh parameter
	 * @param  fClass the class of the updater function's eighth parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		OctaFunction<U, Map<String, Object>, A, B, C, D, E, F, T> octaFunction,
		Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
		Class<E> eClass, Class<F> fClass);

	/**
	 * Adds a route to a collection item updater function with five parameters.
	 *
	 * @param  pentaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @return the updated builder
	 */
	public <A, B, C> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		PentaFunction<U, Map<String, Object>, A, B, C, T> pentaFunction,
		Class<A> aClass, Class<B> bClass, Class<C> cClass);

	/**
	 * Adds a route to a collection item updater function with four parameters.
	 *
	 * @param  tetraFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @return the updated builder
	 */
	public <A, B> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		TetraFunction<U, Map<String, Object>, A, B, T> tetraFunction,
		Class<A> aClass, Class<B> bClass);

	/**
	 * Adds a route to a collection item updater function with three parameters.
	 *
	 * @param  triFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @return the updated builder
	 */
	public <A> RoutesBuilder<T, U> addCollectionPageItemUpdater(
		TriFunction<U, Map<String, Object>, A, T> triFunction, Class<A> aClass);

	/**
	 * Adds a route to a collection item updater function with eleven
	 * parameters.
	 *
	 * @param  undecaFunction the function that updates the collection item
	 * @param  aClass the class of the updater function's third parameter
	 * @param  bClass the class of the updater function's fourth parameter
	 * @param  cClass the class of the updater function's fifth parameter
	 * @param  dClass the class of the updater function's sixth parameter
	 * @param  eClass the class of the updater function's seventh parameter
	 * @param  fClass the class of the updater function's eighth parameter
	 * @param  gClass the class of the updater function's ninth parameter
	 * @param  hClass the class of the updater function's tenth parameter
	 * @param  iClass the class of the updater function's eleventh parameter
	 * @return the updated builder
	 */
	public <A, B, C, D, E, F, G, H, I> RoutesBuilder<T, U>
		addCollectionPageItemUpdater(
			UndecaFunction<U, Map<String, Object>, A, B, C, D, E, F, G, H, I, T>
				undecaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			Class<E> eClass, Class<F> fClass, Class<G> gClass, Class<H> hClass,
			Class<I> iClass);

	/**
	 * Constructs the {@link Routes} instance with the information provided to
	 * the builder.
	 *
	 * @return the {@code Routes} instance
	 */
	public Routes<T> build();

}