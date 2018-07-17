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

import static com.liferay.apio.architect.impl.endpoint.ExceptionSupplierUtil.notFound;

import com.liferay.apio.architect.file.BinaryFile;
import com.liferay.apio.architect.function.throwable.ThrowableFunction;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.representor.Representor;
import com.liferay.apio.architect.single.model.SingleModel;

import java.util.function.BiFunction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Declares the endpoint for binary operations.
 *
 * @author Alejandro Hernández
 */
public class BinaryEndpoint {

	public BinaryEndpoint(
		ThrowableFunction<String, Representor<Object>> representorFunction,
		BiFunction<String, String, Try<SingleModel<Object>>>
			singleModelFunction) {

		_representorFunction = representorFunction;
		_singleModelFunction = singleModelFunction;
	}

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
	@Path("{name}/{id}/{binaryId}")
	public Try<BinaryFile> getCollectionItemBinaryFileTry(
		@PathParam("name") String name, @PathParam("id") String id,
		@PathParam("binaryId") String binaryId) {

		return Try.fromFallible(
			() -> _representorFunction.apply(name)
		).mapOptional(
			representor -> representor.getBinaryFunction(binaryId),
			notFound(name, id, binaryId)
		).flatMap(
			binaryFunction -> _singleModelFunction.apply(
				name, id
			).map(
				SingleModel::getModel
			).map(
				binaryFunction::apply
			)
		);
	}

	private final ThrowableFunction<String, Representor<Object>>
		_representorFunction;
	private final BiFunction<String, String, Try<SingleModel<Object>>>
		_singleModelFunction;

}