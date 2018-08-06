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
import static com.liferay.apio.architect.impl.endpoint.ExceptionSupplierUtil.notFound;
import static com.liferay.apio.architect.operation.HTTPMethod.DELETE;
import static com.liferay.apio.architect.operation.HTTPMethod.POST;
import static com.liferay.apio.architect.operation.HTTPMethod.PUT;

import static javax.ws.rs.core.Response.noContent;

import com.liferay.apio.architect.alias.IdentifierFunction;
import com.liferay.apio.architect.consumer.throwable.ThrowableConsumer;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.routes.NestedCollectionRoutes;
import com.liferay.apio.architect.single.model.SingleModel;
import com.liferay.apio.architect.supplier.ThrowableSupplier;
import com.liferay.apio.architect.uri.Path;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.core.Response;

/**
 * @author Alejandro Hernández
 */
public class PageEndpointImpl<T, S> implements PageEndpoint<T> {

	public PageEndpointImpl(
		String name, HttpServletRequest httpServletRequest,
		Function<String, Try<SingleModel<T>>> singleModelFunction,
		ThrowableSupplier<CollectionRoutes<T, S>> collectionRoutesSupplier,
		ThrowableSupplier<Representor<T>> representorSupplier,
		ThrowableSupplier<ItemRoutes<T, S>> itemRoutesSupplier,
		ThrowableFunction<String, NestedCollectionRoutes<T, S, Object>>
			nestedCollectionRoutesFunction,
		IdentifierFunction<S> pathToIdentifierFunction) {

		_name = name;
		_httpServletRequest = httpServletRequest;
		_singleModelFunction = singleModelFunction;
		_collectionRoutesSupplier = collectionRoutesSupplier;
		_representorSupplier = representorSupplier;
		_itemRoutesSupplier = itemRoutesSupplier;
		_nestedCollectionRoutesFunction = nestedCollectionRoutesFunction;
		_pathToIdentifierFunction = pathToIdentifierFunction;
	}

	@Override
	public Try<SingleModel<T>> addCollectionItem(Body body) {
		return Try.fromFallible(
			_collectionRoutesSupplier
		).mapOptional(
			CollectionRoutes::getCreateItemFunctionOptional,
			notAllowed(POST, _name)
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).flatMap(
			bodyFunction -> bodyFunction.apply(body)
		);
	}

	@Override
	public Try<SingleModel<T>> addNestedCollectionItem(
		String id, String nestedName, Body body) {

		return Try.fromFallible(
			() -> _nestedCollectionRoutesFunction.apply(nestedName)
		).mapOptional(
			NestedCollectionRoutes::getNestedCreateItemFunctionOptional
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).flatMap(
			identifierFunction -> _singleModelFunction.apply(
				id
			).map(
				this::_getIdentifierFunction
			).map(
				identifierFunction::apply
			)
		).flatMap(
			bodyFunction -> bodyFunction.apply(body)
		).mapFailMatching(
			NoSuchElementException.class,
			notAllowed(POST, _name, id, nestedName)
		);
	}

	@Override
	public Response deleteCollectionItem(String id) throws Exception {
		ThrowableConsumer<S> deleteItemThrowableConsumer = Try.fromFallible(
			_itemRoutesSupplier
		).mapOptional(
			ItemRoutes::getDeleteConsumerOptional, notAllowed(DELETE, _name, id)
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).getUnchecked();

		S s = _pathToIdentifierFunction.apply(new Path(_name, id));

		deleteItemThrowableConsumer.accept(s);

		return noContent().build();
	}

	@Override
	public Try<SingleModel<T>> getCollectionItemSingleModelTry(String id) {
		return _singleModelFunction.apply(id);
	}

	@Override
	public Try<Page<T>> getCollectionPageTry() {
		return Try.fromFallible(
			_collectionRoutesSupplier
		).mapOptional(
			CollectionRoutes::getGetPageFunctionOptional, notFound(_name)
		).flatMap(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		);
	}

	@Override
	public Try<Page<T>> getNestedCollectionPageTry(
		String id, String nestedName) {

		return Try.fromFallible(
			() -> _nestedCollectionRoutesFunction.apply(nestedName)
		).map(
			NestedCollectionRoutes::getNestedGetPageFunctionOptional
		).map(
			Optional::get
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).map(
			pathFunction -> pathFunction.apply(new Path(_name, id))
		).flatMap(
			identifierFunction -> _singleModelFunction.apply(
				id
			).map(
				this::_getIdentifierFunction
			).flatMap(
				identifierFunction::apply
			)
		).mapFailMatching(
			NoSuchElementException.class, notFound(id, nestedName)
		);
	}

	@Override
	public Try<SingleModel<T>> updateCollectionItem(String id, Body body) {
		return Try.fromFallible(
			_itemRoutesSupplier
		).mapOptional(
			ItemRoutes::getUpdateItemFunctionOptional,
			notAllowed(PUT, _name, id)
		).map(
			requestFunction -> requestFunction.apply(_httpServletRequest)
		).map(
			identifierFunction -> identifierFunction.compose(
				_pathToIdentifierFunction)
		).map(
			pathFunction -> pathFunction.apply(new Path(_name, id))
		).flatMap(
			bodyFunction -> bodyFunction.apply(body)
		);
	}

	private Object _getIdentifierFunction(SingleModel<T> singleModel)
		throws Exception {

		Representor<T> representor = _representorSupplier.get();

		return representor.getIdentifier(singleModel.getModel());
	}

	private final ThrowableSupplier<CollectionRoutes<T, S>>
		_collectionRoutesSupplier;
	private final HttpServletRequest _httpServletRequest;
	private final ThrowableSupplier<ItemRoutes<T, S>> _itemRoutesSupplier;
	private final String _name;
	private final ThrowableFunction
		<String, NestedCollectionRoutes<T, S, Object>>
			_nestedCollectionRoutesFunction;
	private final IdentifierFunction<S> _pathToIdentifierFunction;
	private final ThrowableSupplier<Representor<T>> _representorSupplier;
	private final Function<String, Try<SingleModel<T>>> _singleModelFunction;

}