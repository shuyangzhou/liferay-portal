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
import com.liferay.apio.architect.alias.routes.BatchCreateItemFunction;
import com.liferay.apio.architect.alias.routes.CreateItemFunction;
import com.liferay.apio.architect.alias.routes.GetPageFunction;
import com.liferay.apio.architect.alias.routes.permission.HasAddingPermissionFunction;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTetraFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;
import com.liferay.apio.architect.pagination.PageItems;
import com.liferay.apio.architect.pagination.Pagination;

import java.util.List;
import java.util.Optional;

/**
 * Holds information about the routes supported for a {@link
 * com.liferay.apio.architect.router.CollectionRouter}.
 *
 * <p>
 * This interface's methods return functions that get the collection resource's
 * different endpoints. You should always use a {@link Builder} to create
 * instances of this interface.
 * </p>
 *
 * @author Alejandro Hernández
 * @param  <T> the model's type
 * @param  <S> the type of the model's identifier (e.g., {@code Long}, {@code
 *         String}, etc.)
 * @see    Builder
 */
@ProviderType
public interface CollectionRoutes<T, S> {

	/**
	 * Returns the function that is used to create multiple collection items, if
	 * the endpoint was added through the {@link Builder} and the function
	 * therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to create a collection item, if the function
	 *         exists; {@code Optional#empty()} otherwise
	 * @review
	 */
	public Optional<BatchCreateItemFunction<S>>
		getBatchCreateItemFunctionOptional();

	/**
	 * Returns the function that is used to create a collection item, if the
	 * endpoint was added through the {@link CollectionRoutes.Builder} and the
	 * function therefore exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to create a collection item, if the function
	 *         exists; {@code Optional#empty()} otherwise
	 */
	public Optional<CreateItemFunction<T>> getCreateItemFunctionOptional();

	/**
	 * Returns the form that is used to create a collection item, if it was
	 * added through the {@link CollectionRoutes.Builder}. Returns {@code
	 * Optional#empty()} otherwise.
	 *
	 * @return the form used to create a collection item; {@code
	 *         Optional#empty()} otherwise
	 */
	public Optional<Form> getFormOptional();

	/**
	 * Returns the function used to obtain the page, if the endpoint was added
	 * through the {@link CollectionRoutes.Builder} and the function therefore
	 * exists. Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to obtain the page, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<GetPageFunction<T>> getGetPageFunctionOptional();

	/**
	 * Creates the {@link CollectionRoutes} of a {@link
	 * com.liferay.apio.architect.router.CollectionRouter}.
	 */
	@ProviderType
	public interface Builder<T, S> {

		/**
		 * Adds a route to a creator function that has one extra parameter.
		 *
		 * @param  creatorThrowableBiFunction the creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, R> Builder<T, S> addCreator(
			ThrowableBiFunction<R, A, T> creatorThrowableBiFunction,
			Class<A> aClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has one extra parameter.
		 *
		 * @param  creatorThrowableBiFunction the creator function
		 * @param  batchCreatorThrowableBiFunction the batch creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 * @review
		 */
		public <A, R> Builder<T, S> addCreator(
			ThrowableBiFunction<R, A, T> creatorThrowableBiFunction,
			ThrowableBiFunction<List<R>, A, List<S>>
				batchCreatorThrowableBiFunction,
			Class<A> aClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has no extra parameters.
		 *
		 * @param  creatorThrowableFunction the creator function
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <R> Builder<T, S> addCreator(
			ThrowableFunction<R, T> creatorThrowableFunction,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has no extra parameters.
		 *
		 * @param  creatorThrowableFunction the creator function
		 * @param  batchCreatorThrowableFunction the batch creator function
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 * @review
		 */
		public <R> Builder<T, S> addCreator(
			ThrowableFunction<R, T> creatorThrowableFunction,
			ThrowableFunction<List<R>, List<S>> batchCreatorThrowableFunction,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has four extra parameters.
		 *
		 * @param  creatorThrowablePentaFunction the creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  cClass the class of the creator function's fourth parameter
		 * @param  dClass the class of the creator function's fifth parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, D, R> Builder<T, S> addCreator(
			ThrowablePentaFunction<R, A, B, C, D, T>
				creatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has four extra parameters.
		 *
		 * @param  creatorThrowablePentaFunction the creator function
		 * @param  batchCreatorThrowablePentaFunction the batch creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  cClass the class of the creator function's fourth parameter
		 * @param  dClass the class of the creator function's fifth parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 * @review
		 */
		public <A, B, C, D, R> Builder<T, S> addCreator(
			ThrowablePentaFunction<R, A, B, C, D, T>
				creatorThrowablePentaFunction,
			ThrowablePentaFunction<List<R>, A, B, C, D, List<S>>
				batchCreatorThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has three extra parameters.
		 *
		 * @param  creatorThrowableTetraFunction the creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  cClass the class of the creator function's fourth parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, R> Builder<T, S> addCreator(
			ThrowableTetraFunction<R, A, B, C, T> creatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has three extra parameters.
		 *
		 * @param  creatorThrowableTetraFunction the creator function
		 * @param  batchCreatorThrowableTetraFunction the batch creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  cClass the class of the creator function's fourth parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 * @review
		 */
		public <A, B, C, R> Builder<T, S> addCreator(
			ThrowableTetraFunction<R, A, B, C, T> creatorThrowableTetraFunction,
			ThrowableTetraFunction<List<R>, A, B, C, List<S>>
				batchCreatorThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has two extra parameters.
		 *
		 * @param  creatorThrowableTriFunction the creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, R> Builder<T, S> addCreator(
			ThrowableTriFunction<R, A, B, T> creatorThrowableTriFunction,
			Class<A> aClass, Class<B> bClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a creator function that has two extra parameters.
		 *
		 * @param  creatorThrowableTriFunction the creator function
		 * @param  batchCreatorThrowableTriFunction the batch creator function
		 * @param  aClass the class of the creator function's second parameter
		 * @param  bClass the class of the creator function's third parameter
		 * @param  hasAddingPermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 * @review
		 */
		public <A, B, R> Builder<T, S> addCreator(
			ThrowableTriFunction<R, A, B, T> creatorThrowableTriFunction,
			ThrowableTriFunction<List<R>, A, B, List<S>>
				batchCreatorThrowableTriFunction,
			Class<A> aClass, Class<B> bClass,
			HasAddingPermissionFunction hasAddingPermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to a collection page function with one extra parameter.
		 *
		 * @param  getterThrowableBiFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's third parameter
		 * @return the updated builder
		 */
		public <A> Builder<T, S> addGetter(
			ThrowableBiFunction<Pagination, A, PageItems<T>>
				getterThrowableBiFunction,
			Class<A> aClass);

		/**
		 * Adds a route to a collection page function with none extra
		 * parameters.
		 *
		 * @param  getterThrowableFunction the function that calculates the page
		 * @return the updated builder
		 */
		public Builder<T, S> addGetter(
			ThrowableFunction<Pagination, PageItems<T>>
				getterThrowableFunction);

		/**
		 * Adds a route to a collection page function with four extra
		 * parameters.
		 *
		 * @param  getterThrowablePentaFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @param  cClass the class of the page function's fourth parameter
		 * @param  dClass the class of the page function's fifth parameter
		 * @return the updated builder
		 */
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<Pagination, A, B, C, D, PageItems<T>>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass);

		/**
		 * Adds a route to a collection page function with three extra
		 * parameters.
		 *
		 * @param  getterThrowableTetraFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @param  cClass the class of the page function's fourth parameter
		 * @return the updated builder
		 */
		public <A, B, C> Builder<T, S> addGetter(
			ThrowableTetraFunction<Pagination, A, B, C, PageItems<T>>
				getterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass);

		/**
		 * Adds a route to a collection page function with two extra parameters.
		 *
		 * @param  getterThrowableTriFunction the function that calculates the
		 *         page
		 * @param  aClass the class of the page function's second parameter
		 * @param  bClass the class of the page function's third parameter
		 * @return the updated builder
		 */
		public <A, B> Builder<T, S> addGetter(
			ThrowableTriFunction<Pagination, A, B, PageItems<T>>
				getterThrowableTriFunction,
			Class<A> aClass, Class<B> bClass);

		/**
		 * Constructs the {@link CollectionRoutes} instance with the information
		 * provided to the builder.
		 *
		 * @return the {@code Routes} instance
		 */
		public CollectionRoutes<T, S> build();

	}

}