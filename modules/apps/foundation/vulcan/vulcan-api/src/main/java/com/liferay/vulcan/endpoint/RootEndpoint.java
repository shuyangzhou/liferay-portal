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

package com.liferay.vulcan.endpoint;

import aQute.bnd.annotation.ProviderType;

import com.liferay.vulcan.pagination.Page;
import com.liferay.vulcan.pagination.SingleModel;
import com.liferay.vulcan.result.Try;

import java.io.InputStream;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Declares the endpoint from which all of your APIs originate. There should
 * only be one {@code RootEndpoint} in the application.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@ProviderType
public interface RootEndpoint {

	/**
	 * Adds a new {@link SingleModel} to the resource specified by {@code name}.
	 * This occurs via a POST request to the resource.
	 *
	 * @param  name the resource's name, extracted from the URL
	 * @param  body the body of the request.
	 * @return the new {@link SingleModel}, or an exception if an error occurred
	 * @review
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/p/{name}")
	@POST
	public <T> Try<SingleModel<T>> addCollectionItem(
		@PathParam("name") String name, Map<String, Object> body);

	/**
	 * Adds a new {@link SingleModel} to the specified nested resource. This
	 * occurs via a POST request to the nested resource.
	 *
	 * @param  name the parent resource's name, extracted from the URL
	 * @param  id the parent resource's ID
	 * @param  nestedName the nested resource's name, extracted from the URL.
	 * @param  body the body of the request.
	 * @return the new {@link SingleModel}, or an exception if an error occurred
	 * @review
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/p/{name}/{id}/{nestedName}")
	@POST
	public <T> Try<SingleModel<T>> addNestedCollectionItem(
		@PathParam("name") String name, @PathParam("id") String id,
		@PathParam("nestedName") String nestedName, Map<String, Object> body);

	/**
	 * Deletes the collection item specified by {@code name}.
	 *
	 * @param  name the name of the resource to delete, extracted from the URL
	 * @param  id the ID of the resource to delete
	 * @return the operation's {@code javax.ws.rs.core.Response}, or an
	 *         exception if an error occurred
	 */
	@DELETE
	@Path("/p/{name}/{id}")
	public Response deleteCollectionItem(
		@PathParam("name") String name, @PathParam("id") String id);

	/**
	 * Returns the {@code InputStream} for the specified resource.
	 *
	 * @param  name the resource's name, extracted from the URL
	 * @param  id the resource's ID
	 * @param  binaryId the binary resource's ID
	 * @return the binary file's {@code java.io.InputStream}, or an exception if
	 *         an error occurred
	 */
	@GET
	@Path("/b/{name}/{id}/{binaryId}")
	public Try<InputStream> getCollectionItemInputStreamTry(
		@PathParam("name") String name, @PathParam("id") String id,
		@PathParam("binaryId") String binaryId);

	/**
	 * Returns the {@link SingleModel} for the specified resource.
	 *
	 * @param  name the resource's name, extracted from the URL
	 * @param  id the resource's ID
	 * @return the {@link SingleModel} for the specified resource, or an
	 *         exception if an error occurred
	 */
	@GET
	@Path("/p/{name}/{id}")
	public <T> Try<SingleModel<T>> getCollectionItemSingleModelTry(
		@PathParam("name") String name, @PathParam("id") String id);

	/**
	 * Returns the collection {@link Page} for the specified resource.
	 *
	 * @param  name the resource's name, extracted from the URL
	 * @return the collection {@link Page} for the specified resource, or an
	 *         exception if an error occurred
	 */
	@GET
	@Path("/p/{name}")
	public <T> Try<Page<T>> getCollectionPageTry(
		@PathParam("name") String name);

	/**
	 * Returns the representation of the "home" of the application.
	 *
	 * @return the representation of the "home" of the application
	 * @review
	 */
	@GET
	@Path("/")
	public String getHome();

	/**
	 * Returns a nested collection {@link Page} for the specified resource.
	 *
	 * @param  name the parent resource's name, extracted from the URL
	 * @param  id the parent resource's ID
	 * @param  nestedName the nested resource's name
	 * @return the nested collection {@link Page} for the specified resource, or
	 *         an exception if an error occurred
	 */
	@GET
	@Path("/p/{name}/{id}/{nestedName}")
	public <T> Try<Page<T>> getNestedCollectionPageTry(
		@PathParam("name") String name, @PathParam("id") String id,
		@PathParam("nestedName") String nestedName);

	/**
	 * Updates the specified collection item.
	 *
	 * @param  name the resource's name, extracted from the URL
	 * @param  id the resource's ID
	 * @param  body the body of the request.
	 * @return the updated {@link SingleModel}, or an exception if there was an
	 *         error
	 * @review
	 */
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/p/{name}/{id}")
	@PUT
	public <T> Try<SingleModel<T>> updateCollectionItem(
		@PathParam("name") String name, @PathParam("id") String id,
		Map<String, Object> body);

}