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

package com.liferay.apio.architect.internal.entrypoint;

import static com.liferay.apio.architect.internal.endpoint.ExceptionSupplierUtil.notAllowed;
import static com.liferay.apio.architect.internal.endpoint.ExceptionSupplierUtil.notFound;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;

import com.liferay.apio.architect.custom.actions.CustomRoute;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.routes.CollectionRoutes;
import com.liferay.apio.architect.routes.ItemRoutes;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Declares the endpoint for custom operations.
 *
 * @author Javier Gamarra
 */
public class CustomOperationsEndpoint<T, S, R> {

	public CustomOperationsEndpoint(
		String name, HttpServletRequest httpServletRequest,
		Supplier<Optional<CollectionRoutes<T, S>>> collectionRoutesSupplier,
		Supplier<Optional<ItemRoutes<T, S>>> itemRoutesSupplier,
		Function<com.liferay.apio.architect.uri.Path, S> identifierFunction) {

		_name = name;
		_httpServletRequest = httpServletRequest;
		_collectionRoutesSupplier = collectionRoutesSupplier;
		_itemRoutesSupplier = itemRoutesSupplier;
		_identifierFunction = identifierFunction;
	}

	@DELETE
	@Path("/{operation}/")
	public void addDeleteCollection(@PathParam("operation") String operation) {
		_getCollectionSingleModelTry(operation, HTTPMethod.DELETE, null);
	}

	@DELETE
	@Path("/{id}/{operation}/")
	public void addDeleteItem(
		@PathParam("operation") String operation, @PathParam("id") String id) {

		_getItemSingleModelTry(operation, HTTPMethod.DELETE, id, null);
	}

	@GET
	@Path("/{operation}/")
	public Try<SingleModel<R>> addGetCollection(
		@PathParam("operation") String operation) {

		return _getCollectionSingleModelTry(operation, HTTPMethod.GET, null);
	}

	@GET
	@Path("/{id}/{operation}/")
	public Try<SingleModel<R>> addGetItem(
		@PathParam("operation") String operation, @PathParam("id") String id) {

		return _getItemSingleModelTry(operation, HTTPMethod.GET, id, null);
	}

	@Consumes({APPLICATION_JSON, MULTIPART_FORM_DATA})
	@Path("/{operation}/")
	@POST
	public Try<SingleModel<R>> addPostCollection(
		@PathParam("operation") String operation, Body body) {

		return _getCollectionSingleModelTry(operation, HTTPMethod.POST, body);
	}

	@Consumes({APPLICATION_JSON, MULTIPART_FORM_DATA})
	@Path("/{id}/{operation}/")
	@POST
	public Try<SingleModel<R>> addPostItem(
		@PathParam("operation") String operation, @PathParam("id") String id,
		Body body) {

		return _getItemSingleModelTry(operation, HTTPMethod.POST, id, body);
	}

	@Consumes({APPLICATION_JSON, MULTIPART_FORM_DATA})
	@Path("/{operation}/")
	@PUT
	public Try<SingleModel<R>> addPutCollection(
		@PathParam("operation") String operation, Body body) {

		return _getCollectionSingleModelTry(operation, HTTPMethod.PUT, body);
	}

	@Consumes({APPLICATION_JSON, MULTIPART_FORM_DATA})
	@Path("/{id}/{operation}/")
	@PUT
	public Try<SingleModel<R>> addPutItem(
		@PathParam("operation") String operation, @PathParam("id") String id,
		Body body) {

		return _getItemSingleModelTry(operation, HTTPMethod.PUT, id, body);
	}

	@SuppressWarnings("unchecked")
	private Try<SingleModel<R>> _getCollectionSingleModelTry(
		String operation, HTTPMethod method, Body body) {

		return Try.fromOptional(
			_collectionRoutesSupplier::get, notFound(_name)
		).mapOptional(
			routes -> {
				Map<String, CustomRoute> customRoutes =
					routes.getCustomRoutes();

				CustomRoute customRoute = customRoutes.get(operation);

				if ((customRoute != null) &&
					method.equals(customRoute.getMethod())) {

					return routes.getCustomPageFunctionsOptional();
				}

				return Optional.empty();
			},
			notAllowed(method, _name)
		).map(
			stringRequestFunctionMap -> stringRequestFunctionMap.get(operation)
		).map(
			function -> function.apply(_httpServletRequest)
		).flatMap(
			function -> (Try)function.apply(body)
		);
	}

	@SuppressWarnings("unchecked")
	private Try<SingleModel<R>> _getItemSingleModelTry(
		String operation, HTTPMethod method, String id, Body body) {

		return Try.fromOptional(
			_itemRoutesSupplier::get, notFound(_name)
		).mapOptional(
			routes -> {
				Map<String, CustomRoute> customRoutes =
					routes.getCustomRoutes();

				CustomRoute customRoute = customRoutes.get(operation);

				if (method.equals(customRoute.getMethod())) {
					return routes.getCustomItemFunctionsOptional();
				}

				return Optional.empty();
			},
			notAllowed(method, _name)
		).map(
			stringRequestFunctionMap -> stringRequestFunctionMap.get(operation)
		).map(
			functionRequestFunction -> functionRequestFunction.apply(
				_httpServletRequest)
		).map(
			function -> function.apply(
				_identifierFunction.apply(
					new com.liferay.apio.architect.uri.Path(_name, id)
				)
			)
		).flatMap(
			function -> (Try)function.apply(body)
		);
	}

	private final Supplier<Optional<CollectionRoutes<T, S>>>
		_collectionRoutesSupplier;
	private final HttpServletRequest _httpServletRequest;
	private final Function<com.liferay.apio.architect.uri.Path, S>
		_identifierFunction;
	private final Supplier<Optional<ItemRoutes<T, S>>> _itemRoutesSupplier;
	private final String _name;

}