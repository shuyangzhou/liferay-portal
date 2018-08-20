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

package com.liferay.apio.architect.routes;

import aQute.bnd.annotation.ProviderType;

import com.liferay.apio.architect.alias.form.FormBuilderFunction;
import com.liferay.apio.architect.alias.routes.NestedBatchCreateItemFunction;
import com.liferay.apio.architect.alias.routes.NestedCreateItemFunction;
import com.liferay.apio.architect.alias.routes.NestedGetPageFunction;
import com.liferay.apio.architect.alias.routes.permission.HasNestedAddingPermissionFunction;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTetraFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;

import java.util.List;
import java.util.Optional;

/**
 * Holds information about the routes supported for a {@link
 * com.liferay.apio.architect.router.NestedCollectionRouter}.
 *
 * <p>
 * This interface's methods return functions to get the collection resource's
 * different endpoints. You should always use a {@link Builder} to create
 * instances of this interface.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @param  <S> the type of the model's identifier (e.g., {@code Long}, {@code
 *         String}, etc.)
 * @param  <U> the type of the parent model's identifier (e.g., {@code Long},
 *         {@code String}, etc.)
 * @see    Builder
 */
@ProviderType
public interface NestedCollectionRoutes<T, S, U> {

	/**
	 * Returns the form that is used to create a collection item, if it was
	 * added through the {@link NestedCollectionRoutes.Builder}. Returns {@code
	 * Optional#empty()} otherwise.
	 *
	 * @return the form used to create a collection item; {@code
	 *         Optional#empty()} otherwise
	 */
	public Optional<Form> getFormOptional();

	/**
	 * Returns the function that's used to create multiple collection items, if
	 * the endpoint was added through the builder and the function therefore
	 * exists; returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function, if it exists; {@code Optional#empty()} otherwise
	 */
	public Optional<NestedBatchCreateItemFunction<S, U>>
		getNestedBatchCreateItemFunctionOptional();

	/**
	 * Returns the function that is used to create a collection item, if the
	 * endpoint was added through the {@link NestedCollectionRoutes.Builder} and
	 * the function therefore exists. Returns {@code Optional#empty()}
	 * otherwise.
	 *
	 * @return the function used to create a collection item, if the function
	 *         exists; {@code Optional#empty()} otherwise
	 */
	public Optional<NestedCreateItemFunction<T, U>>
		getNestedCreateItemFunctionOptional();

	/**
	 * Returns the function used to obtain the page, if the endpoint was added
	 * through the {@link NestedCollectionRoutes.Builder} and the function
	 * therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to obtain the page, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<NestedGetPageFunction<T, U>>
		getNestedGetPageFunctionOptional();

	/**
	 * Creates the {@link NestedCollectionRoutes} of a {@link
	 * com.liferay.apio.architect.router.NestedCollectionRouter}.
	 *
	 * @param <T> the model's type
	 * @param <S> the type of the model's identifier (e.g., {@code Long}, {@code
	 *        String}, etc.)
	 * @param <U> the type of the parent model's identifier (e.g., {@code Long},
	 *        {@code String}, etc.)
	 */
	@ProviderType
	public interface Builder<T, S, U> {

		/**
		 * Adds a route to a creator function that has no extra parameters.
		 *
		 * @param  creatorThrowableBiFunction the creator function that adds the
		 *         collection item
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <R> Builder<T, S, U> addCreator(
			ThrowableBiFunction<U, R, T> creatorThrowableBiFunction,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has no extra parameters.
		 *
		 * @param  creatorThrowableBiFunction the creator function that adds the
		 *         collection item
		 * @param  batchCreatorThrowableBiFunction the batch creator function
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <R> Builder<T, S, U> addCreator(
			ThrowableBiFunction<U, R, T> creatorThrowableBiFunction,
			ThrowableBiFunction<U, List<R>, List<S>>
				batchCreatorThrowableBiFunction,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has four extra parameters.
		 *
		 * @param  creatorThrowableHexaFunction the creator function that adds
		 *         the collection item
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  cClass the class of the creator function's fifth parameter
		 * @param  dClass the class of the creator function's sixth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, D, R> Builder<T, S, U> addCreator(
			ThrowableHexaFunction<U, R, A, B, C, D, T>
				creatorThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has four extra parameters.
		 *
		 * @param  creatorThrowableHexaFunction the creator function that adds
		 *         the collection item
		 * @param  batchCreatorThrowableHexaFunction the batch creator function
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  cClass the class of the creator function's fifth parameter
		 * @param  dClass the class of the creator function's sixth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, D, R> Builder<T, S, U> addCreator(
			ThrowableHexaFunction<U, R, A, B, C, D, T>
				creatorThrowableHexaFunction,
			ThrowableHexaFunction<U, List<R>, A, B, C, D, List<S>>
				batchCreatorThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has three extra parameters.
		 *
		 * @param  creatorThrowablePentaFunction the creator function that adds
		 *         the collection item
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  cClass the class of the creator function's fifth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, R> Builder<T, S, U> addCreator(
			ThrowablePentaFunction<U, R, A, B, C, T>
				creatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has three extra parameters.
		 *
		 * @param  creatorThrowablePentaFunction the creator function that adds
		 *         the collection item
		 * @param  batchCreatorThrowablePentaFunction the batch creator function
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  cClass the class of the creator function's fifth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, R> Builder<T, S, U> addCreator(
			ThrowablePentaFunction<U, R, A, B, C, T>
				creatorThrowablePentaFunction,
			ThrowablePentaFunction<U, List<R>, A, B, C, List<S>>
				batchCreatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has two extra parameters.
		 *
		 * @param  creatorThrowableTetraFunction the creator function that adds
		 *         the collection item
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, R> Builder<T, S, U> addCreator(
			ThrowableTetraFunction<U, R, A, B, T> creatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has two extra parameters.
		 *
		 * @param  creatorThrowableTetraFunction the creator function that adds
		 *         the collection item
		 * @param  batchCreatorThrowableTetraFunction the batch creator function
		 * @param  aClass the class of the creator function's third parameter
		 * @param  bClass the class of the creator function's fourth parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, R> Builder<T, S, U> addCreator(
			ThrowableTetraFunction<U, R, A, B, T> creatorThrowableTetraFunction,
			ThrowableTetraFunction<U, List<R>, A, B, List<S>>
				batchCreatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has one extra parameter.
		 *
		 * @param  creatorThrowableTriFunction the creator function that adds
		 *         the collection item
		 * @param  aClass the class of the creator function's third parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, R> Builder<T, S, U> addCreator(
			ThrowableTriFunction<U, R, A, T> creatorThrowableTriFunction,
			Class<A> aClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has one extra parameter.
		 *
		 * @param  creatorThrowableTriFunction the creator function that adds
		 *         the collection item
		 * @param  batchCreatorThrowableTriFunction the batch creator function
		 * @param  aClass the class of the creator function's third parameter
		 * @param  hasNestedAddingPermissionFunction the permission function for
		 *         this route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, R> Builder<T, S, U> addCreator(
			ThrowableTriFunction<U, R, A, T> creatorThrowableTriFunction,
			ThrowableTriFunction<U, List<R>, A, List<S>>
				batchCreatorThrowableTriFunction,
			Class<A> aClass,
			HasNestedAddingPermissionFunction<U>
				hasNestedAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a collection page function with none extra
		 * parameters.
		 *
		 * @param  getterThrowableBiFunction the function that calculates the
		 *         page
		 * @return the updated builder
		 */
		public Builder<T, S, U> addGetter(
			ThrowableBiFunction<Pagination, U, PageItems<T>>
				getterThrowableBiFunction);

		/**
		 * Adds a route to a collection page function with four extra
		 * parameters.
		 *
		 * @param  getterThrowableHexaFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @param  cClass the class of the page function's fourth parameter
		 * @param  dClass the class of the page function's fifth parameter
		 * @return the updated builder
		 */
		public <A, B, C, D> Builder<T, S, U> addGetter(
			ThrowableHexaFunction<Pagination, U, A, B, C, D, PageItems<T>>
				getterThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass);

		/**
		 * Adds a route to a collection page function with three extra
		 * parameters.
		 *
		 * @param  getterThrowablePentaFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @param  cClass the class of the page function's fourth parameter
		 * @return the updated builder
		 */
		public <A, B, C> Builder<T, S, U> addGetter(
			ThrowablePentaFunction<Pagination, U, A, B, C, PageItems<T>>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass);

		/**
		 * Adds a route to a collection page function with two extra parameters.
		 *
		 * @param  getterThrowableTetraFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @return the updated builder
		 */
		public <A, B> Builder<T, S, U> addGetter(
			ThrowableTetraFunction<Pagination, U, A, B, PageItems<T>>
				getterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass);

		/**
		 * Adds a route to a collection page function with one extra parameter.
		 *
		 * @param  getterThrowableTriFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @return the updated builder
		 */
		public <A> Builder<T, S, U> addGetter(
			ThrowableTriFunction<Pagination, U, A, PageItems<T>>
				getterThrowableTriFunction,
			Class<A> aClass);

		/**
		 * Constructs the {@link NestedCollectionRoutes} instance with the
		 * information provided to the builder.
		 *
		 * @return the {@code Routes} instance
		 */
		public NestedCollectionRoutes<T, S, U> build();

	}

}