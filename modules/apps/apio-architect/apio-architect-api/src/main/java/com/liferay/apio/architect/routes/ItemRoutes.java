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
import com.liferay.apio.architect.alias.routes.DeleteItemConsumer;
import com.liferay.apio.architect.alias.routes.GetItemFunction;
import com.liferay.apio.architect.alias.routes.UpdateItemFunction;
import com.liferay.apio.architect.alias.routes.permission.HasRemovePermissionFunction;
import com.liferay.apio.architect.alias.routes.permission.HasUpdatePermissionFunction;
import com.liferay.apio.architect.consumer.throwable.ThrowableBiConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowablePentaConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableTetraConsumer;
import com.liferay.apio.architect.consumer.throwable.ThrowableTriConsumer;
import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.function.throwable.ThrowableBiFunction;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.function.throwable.ThrowableHexaFunction;
import com.liferay.apio.architect.function.throwable.ThrowablePentaFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTetraFunction;
import com.liferay.apio.architect.function.throwable.ThrowableTriFunction;

import java.util.Optional;

/**
 * Holds information about the routes supported for an {@link
 * com.liferay.apio.architect.router.ItemRouter}.
 *
 * <p>
 * This interface's methods return functions to get the item resource's
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
public interface ItemRoutes<T, S> {

	/**
	 * Returns the function used to delete the item, if the endpoint was added
	 * through the {@link ItemRoutes.Builder} and the function therefore exists.
	 * Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to delete the item, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<DeleteItemConsumer<S>> getDeleteConsumerOptional();

	/**
	 * Returns the form that is used to update a collection item, if it was
	 * added through the {@link ItemRoutes.Builder}. Returns {@code
	 * Optional#empty()} otherwise.
	 *
	 * @return the form used to update a collection item; {@code
	 *         Optional#empty()} otherwise
	 */
	public Optional<Form> getFormOptional();

	/**
	 * Returns the function used to obtain the item, if the endpoint was added
	 * through the {@link ItemRoutes.Builder} and the function therefore exists.
	 * Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to obtain the item, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<GetItemFunction<T, S>> getItemFunctionOptional();

	/**
	 * Returns the function used to update the item, if the endpoint was added
	 * through the {@link ItemRoutes.Builder} and the function therefore exists.
	 * Returns {@code Optional#empty()} otherwise.
	 *
	 * @return the function used to update the item, if the function exists;
	 *         {@code Optional#empty()} otherwise
	 */
	public Optional<UpdateItemFunction<T, S>> getUpdateItemFunctionOptional();

	/**
	 * Creates the {@code ItemRoutes} of an {@link
	 * com.liferay.apio.architect.router.ItemRouter}.
	 *
	 * @param <T> the model's type
	 * @param <S> the type of the model's identifier (e.g., {@code Long}, {@code
	 *        String}, etc.)
	 */
	@ProviderType
	public interface Builder<T, S> {

		/**
		 * Adds a route to an item function with one extra parameter.
		 *
		 * @param  getterThrowableBiFunction the function that calculates the
		 *         item
		 * @param  aClass the class of the item function's second parameter
		 * @return the updated builder
		 */
		public <A> Builder<T, S> addGetter(
			ThrowableBiFunction<S, A, T> getterThrowableBiFunction,
			Class<A> aClass);

		/**
		 * Adds a route to an item function with none extra parameters.
		 *
		 * @param  getterThrowableFunction the function that calculates the item
		 * @return the updated builder
		 */
		public Builder<T, S> addGetter(
			ThrowableFunction<S, T> getterThrowableFunction);

		/**
		 * Adds a route to an item function with four extra parameters.
		 *
		 * @param  getterThrowablePentaFunction the function that calculates the
		 *         item
		 * @param  aClass the class of the item function's second parameter
		 * @param  bClass the class of the item function's third parameter
		 * @param  cClass the class of the item function's fourth parameter
		 * @param  dClass the class of the item function's fifth parameter
		 * @return the updated builder
		 */
		public <A, B, C, D> Builder<T, S> addGetter(
			ThrowablePentaFunction<S, A, B, C, D, T>
				getterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass);

		/**
		 * Adds a route to an item function with three extra parameters.
		 *
		 * @param  getterThrowableTetraFunction the function that calculates the
		 *         item
		 * @param  aClass the class of the item function's second parameter
		 * @param  bClass the class of the item function's third parameter
		 * @param  cClass the class of the item function's fourth parameter
		 * @return the updated builder
		 */
		public <A, B, C> Builder<T, S> addGetter(
			ThrowableTetraFunction<S, A, B, C, T> getterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass);

		/**
		 * Adds a route to an item function with two extra parameters.
		 *
		 * @param  getterThrowableTriFunction the function that calculates the
		 *         item
		 * @param  aClass the class of the item function's second parameter
		 * @param  bClass the class of the item function's third parameter
		 * @return the updated builder
		 */
		public <A, B> Builder<T, S> addGetter(
			ThrowableTriFunction<S, A, B, T> getterThrowableTriFunction,
			Class<A> aClass, Class<B> bClass);

		/**
		 * Adds a route to a remover function with one extra parameter.
		 *
		 * @param  removerThrowableBiConsumer the remover function
		 * @param  aClass the class of the item remover function's second
		 *         parameter
		 * @param  hasRemovePermissionFunction the permission function for this
		 *         route
		 * @return the updated builder
		 */
		public <A> Builder<T, S> addRemover(
			ThrowableBiConsumer<S, A> removerThrowableBiConsumer,
			Class<A> aClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction);

		/**
		 * Adds a route to a remover function with no extra parameters.
		 *
		 * @param  removerThrowableConsumer the remover function
		 * @param  hasRemovePermissionFunction the permission function for this
		 *         route
		 * @return the updated builder
		 */
		public Builder<T, S> addRemover(
			ThrowableConsumer<S> removerThrowableConsumer,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction);

		/**
		 * Adds a route to a remover function with four extra parameters.
		 *
		 * @param  removerThrowablePentaConsumer the remover function
		 * @param  aClass the class of the item remover function's second
		 *         parameter
		 * @param  bClass the class of the item remover function's third
		 *         parameter
		 * @param  cClass the class of the item remover function's fourth
		 *         parameter
		 * @param  dClass the class of the item remover function's fifth
		 *         parameter
		 * @param  hasRemovePermissionFunction the permission function for this
		 *         route
		 * @return the updated builder
		 */
		public <A, B, C, D> Builder<T, S> addRemover(
			ThrowablePentaConsumer<S, A, B, C, D> removerThrowablePentaConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction);

		/**
		 * Adds a route to a remover function with three extra parameters.
		 *
		 * @param  removerThrowableTetraConsumer the remover function
		 * @param  aClass the class of the item remover function's second
		 *         parameter
		 * @param  bClass the class of the item remover function's third
		 *         parameter
		 * @param  cClass the class of the item remover function's fourth
		 *         parameter
		 * @param  hasRemovePermissionFunction the permission function for this
		 *         route
		 * @return the updated builder
		 */
		public <A, B, C> Builder<T, S> addRemover(
			ThrowableTetraConsumer<S, A, B, C> removerThrowableTetraConsumer,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction);

		/**
		 * Adds a route to a remover function with two extra parameters.
		 *
		 * @param  removerThrowableTriConsumer the remover function
		 * @param  aClass the class of the item remover function's second
		 *         parameter
		 * @param  bClass the class of the item remover function's third
		 *         parameter
		 * @param  hasRemovePermissionFunction the permission function for this
		 *         route
		 * @return the updated builder
		 */
		public <A, B> Builder<T, S> addRemover(
			ThrowableTriConsumer<S, A, B> removerThrowableTriConsumer,
			Class<A> aClass, Class<B> bClass,
			HasRemovePermissionFunction<S> hasRemovePermissionFunction);

		/**
		 * Adds a route to an updater function with no extra parameters.
		 *
		 * @param  updaterThrowableBiFunction the updater function
		 * @param  hasUpdatePermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <R> Builder<T, S> addUpdater(
			ThrowableBiFunction<S, R, T> updaterThrowableBiFunction,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to an updater function with four extra parameters.
		 *
		 * @param  updaterThrowableHexaFunction the updater function
		 * @param  aClass the class of the updater function's third parameter
		 * @param  bClass the class of the updater function's fourth parameter
		 * @param  cClass the class of the updater function's fifth parameter
		 * @param  dClass the class of the updater function's sixth parameter
		 * @param  hasUpdatePermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, D, R> Builder<T, S> addUpdater(
			ThrowableHexaFunction<S, R, A, B, C, D, T>
				updaterThrowableHexaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass, Class<D> dClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to an updater function with three extra parameters.
		 *
		 * @param  updaterThrowablePentaFunction the updater function that
		 *         removes the item
		 * @param  aClass the class of the updater function's third parameter
		 * @param  bClass the class of the updater function's fourth parameter
		 * @param  cClass the class of the updater function's fifth parameter
		 * @param  hasUpdatePermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, C, R> Builder<T, S> addUpdater(
			ThrowablePentaFunction<S, R, A, B, C, T>
				updaterThrowablePentaFunction,
			Class<A> aClass, Class<B> bClass, Class<C> cClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to an updater function with two extra parameters.
		 *
		 * @param  updaterThrowableTetraFunction the updater function
		 * @param  aClass the class of the updater function's third parameter
		 * @param  bClass the class of the updater function's fourth parameter
		 * @param  hasUpdatePermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, B, R> Builder<T, S> addUpdater(
			ThrowableTetraFunction<S, R, A, B, T> updaterThrowableTetraFunction,
			Class<A> aClass, Class<B> bClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Adds a route to an updater function with one extra parameter.
		 *
		 * @param  updaterThrowableTriFunction the updater function that removes
		 *         the item
		 * @param  aClass the class of the updater function's third parameter
		 * @param  hasUpdatePermissionFunction the permission function for this
		 *         route
		 * @param  formBuilderFunction the function that creates the form for
		 *         this operation
		 * @return the updated builder
		 */
		public <A, R> Builder<T, S> addUpdater(
			ThrowableTriFunction<S, R, A, T> updaterThrowableTriFunction,
			Class<A> aClass,
			HasUpdatePermissionFunction<S> hasUpdatePermissionFunction,
			FormBuilderFunction<R> formBuilderFunction);

		/**
		 * Constructs the {@link ItemRoutes} instance with the information
		 * provided to the builder.
		 *
		 * @return the {@code Routes} instance
		 */
		public ItemRoutes<T, S> build();

	}

}