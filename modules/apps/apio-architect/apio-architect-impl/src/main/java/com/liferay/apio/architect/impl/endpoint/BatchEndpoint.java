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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.functional.Try;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Declares the endpoints for batch operations.
 *
 * @author Alejandro Hernández
 * @author Zoltán Takács
 * @param  <T> the type of the model's identifier (e.g., {@code Long}, {@code
 *         String}, etc.)
 */
public interface BatchEndpoint<T> {

	/**
	 * Adds multiple {@link com.liferay.apio.architect.single.model.SingleModel}
	 * instances to the resource specified by {@code name}. This occurs via a
	 * POST request to the resource.
	 *
	 * @param  body the request's body
	 * @return the batch result operation, or an exception if an error occurred
	 */
	@Consumes(APPLICATION_JSON)
	@Path("/")
	@POST
	public Try<BatchResult<T>> addBatchCollectionItems(Body body);

	/**
	 * Adds a new {@link com.liferay.apio.architect.single.model.SingleModel} to
	 * the specified nested resource. This occurs via a POST request to the
	 * nested resource.
	 *
	 * @param  id the parent resource's ID
	 * @param  nestedName the nested resource's name, extracted from the URL
	 * @param  body the request's body
	 * @return the new single model, or an exception if an error occurred
	 */
	@Consumes(APPLICATION_JSON)
	@Path("{id}/{nestedName}")
	@POST
	public Try<BatchResult<T>> addBatchNestedCollectionItems(
		@PathParam("id") String id, @PathParam("nestedName") String nestedName,
		Body body);

}