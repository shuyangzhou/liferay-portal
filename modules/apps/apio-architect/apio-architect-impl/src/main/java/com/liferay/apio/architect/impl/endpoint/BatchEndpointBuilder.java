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

package com.liferay.apio.architect.impl.endpoint;

import static com.liferay.apio.architect.impl.endpoint.ExceptionSupplierUtil.notAllowed;
import static com.liferay.apio.architect.operation.HTTPMethod.POST;

import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.supplier.ThrowableSupplier;

import java.util.NoSuchElementException;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

/**
 * Creates {@link BatchEndpoint} instances.
 *
 * @author Alejandro Hernández
 * @review
 */
public interface BatchEndpointBuilder {

	/**
	 * Adds information about the resource's name to the builder.
	 *
	 * @param  name the resource's name
	 * @return the builder's following step
	 * @review
	 */
	public static <T, S> RequestStep<T, S> name(String name) {
		return httpServletRequest -> singleModelFunction ->
			representorSupplier -> collectionRoutesSupplier ->
				nestedCollectionRoutesFunction -> () -> new BatchEndpointImpl<>(
					name, httpServletRequest, singleModelFunction,
					representorSupplier, collectionRoutesSupplier,
					nestedCollectionRoutesFunction);
	}

	public class BatchEndpointImpl<T, S> implements BatchEndpoint<S> {

		@Override
		public Try<BatchResult<S>> addBatchCollectionItems(Body body) {
			return Try.fromFallible(
				_collectionRoutesSupplier
			).mapOptional(
				CollectionRoutes::
					getBatchCreateItemFunctionOptional,
				notAllowed(POST, _name)
			).map(
				requestFunction -> requestFunction.apply(_httpServletRequest)
			).flatMap(
				bodyFunction -> bodyFunction.apply(body)
			);
		}

		@Override
		public Try<BatchResult<S>> addBatchNestedCollectionItems(
			String id, String nestedName, Body body) {

			return Try.fromFallible(
				() -> _nestedCollectionRoutesFunction.apply(nestedName)
			).mapOptional(
				NestedCollectionRoutes::
					getNestedBatchCreateItemFunctionOptional,
				notAllowed(POST, _name)
			).map(
				requestFunction -> requestFunction.apply(_httpServletRequest)
			).map(
				bodyFunction -> bodyFunction.apply(body)
			).flatMap(
				identifierFunction -> _singleModelFunction.apply(
					id
				).map(
					this::_getIdentifierFunction
				).flatMap(
					identifierFunction::apply
				)
			).mapFailMatching(
				NoSuchElementException.class,
				notAllowed(POST, _name, id, nestedName)
			);
		}

		private BatchEndpointImpl(
			String name, HttpServletRequest httpServletRequest,
			Function<String, Try<SingleModel<T>>> singleModelFunction,
			ThrowableSupplier<Representor<T>> representorSupplier,
			ThrowableSupplier<CollectionRoutes<T, S>> collectionRoutesSupplier,
			ThrowableFunction<String, NestedCollectionRoutes<T, S, Object>>
				nestedCollectionRoutesFunction) {

			_name = name;
			_httpServletRequest = httpServletRequest;
			_singleModelFunction = singleModelFunction;
			_representorSupplier = representorSupplier;
			_collectionRoutesSupplier = collectionRoutesSupplier;
			_nestedCollectionRoutesFunction = nestedCollectionRoutesFunction;
		}

		private Object _getIdentifierFunction(SingleModel<T> singleModel)
			throws Exception {

			Representor<T> representor = _representorSupplier.get();

			return representor.getIdentifier(singleModel.getModel());
		}

		private final ThrowableSupplier<CollectionRoutes<T, S>>
			_collectionRoutesSupplier;
		private final HttpServletRequest _httpServletRequest;
		private final String _name;
		private final ThrowableFunction
			<String, NestedCollectionRoutes<T, S, Object>>
				_nestedCollectionRoutesFunction;
		private final ThrowableSupplier<Representor<T>> _representorSupplier;
		private final Function<String, Try<SingleModel<T>>>
			_singleModelFunction;

	}

	@FunctionalInterface
	public interface BuildStep<T> {

		/**
		 * Constructs the {@link BatchEndpoint} instance with the information
		 * provided to the builder.
		 *
		 * @return the {@code BatchEndpointImpl} instance
		 * @review
		 */
		public BatchEndpoint<T> build();

	}

	@FunctionalInterface
	public interface CollectionRoutesSupplierStep<T, S> {

		/**
		 * Adds information to the builder about the supplier that provides the
		 * {@code CollectionRoutes} of the current resource.
		 *
		 * @param  supplier the supplier that provides the {@code
		 *         CollectionRoutes} of the current resource
		 * @return the builder's following step
		 * @review
		 */
		public NestedCollectionRoutesFunctionStep<T, S>
			collectionRoutesSupplier(
				ThrowableSupplier<CollectionRoutes<T, S>> supplier);

	}

	@FunctionalInterface
	public interface NestedCollectionRoutesFunctionStep<T, S> {

		/**
		 * Adds information to the builder about the function that gets the
		 * {@code NestedCollectionRoutes} of a nested resource of the current
		 * resource.
		 *
		 * @param  function the function that gets the {@code
		 *         NestedCollectionRoutes} of a nested resource of the current
		 *         resource
		 * @return the builder's following step
		 * @review
		 */
		public BuildStep<S> nestedCollectionRoutesFunction(
			ThrowableFunction<String, NestedCollectionRoutes<T, S, Object>>
				function);

	}

	@FunctionalInterface
	public interface RepresentorSupplierStep<T, S> {

		/**
		 * Adds information to the builder about the supplier that provides the
		 * {@code Representor} of the current resource.
		 *
		 * @param  supplier the supplier that provides the {@code Representor}
		 *         of the current resource
		 * @return the builder's following step
		 * @review
		 */
		public CollectionRoutesSupplierStep<T, S> representorSupplier(
			ThrowableSupplier<Representor<T>> supplier);

	}

	@FunctionalInterface
	public interface RequestStep<T, S> {

		/**
		 * Adds information about the current request
		 *
		 * @param  httpServletRequest the current HTTP request
		 * @return the builder's following step
		 * @review
		 */
		public SingleModelFunctionStep<T, S> httpServletRequest(
			HttpServletRequest httpServletRequest);

	}

	@FunctionalInterface
	public interface SingleModelFunctionStep<T, S> {

		/**
		 * Adds information to the builder about the function that gets the
		 * {@code SingleModel} with a certain ID.
		 *
		 * @param  function the function that gets the {@code SingleModel} with
		 *         a certain ID
		 * @return the builder's following step
		 * @review
		 */
		public RepresentorSupplierStep<T, S> singleModelFunction(
			Function<String, Try<SingleModel<T>>> function);

	}

}