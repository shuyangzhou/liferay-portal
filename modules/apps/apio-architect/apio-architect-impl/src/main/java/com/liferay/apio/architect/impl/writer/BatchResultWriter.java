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

package com.liferay.apio.architect.impl.writer;

import static com.liferay.apio.architect.impl.url.URLCreator.createSingleURL;

import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.impl.alias.PathFunction;
import com.liferay.apio.architect.impl.message.json.BatchResultMessageMapper;
import com.liferay.apio.architect.impl.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.request.RequestInfo;
import com.liferay.apio.architect.impl.url.ApplicationURL;
import com.liferay.apio.architect.representor.Representor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Writes a {@link BatchResult}.
 *
 * @author Alejandro Hernández
 * @param  <T> the type of the model's identifier (e.g., {@code Long}, {@code
 *         String}, etc.)
 */
public class BatchResultWriter<T> {

	/**
	 * Writes the handled batch result to a string. If no {@code Representor}
	 * exists for the resource, this method returns {@code Optional#empty()}.
	 *
	 * @return the batch result's representation, if the {@code Representor}
	 *         exists for the resource; returns {@code Optional#empty()}
	 *         otherwise
	 */
	public Optional<String> write() {
		Optional<Representor<Object>> optional = _representorFunction.apply(
			_batchResult.resourceName);

		if (!optional.isPresent()) {
			return Optional.empty();
		}

		Representor<Object> representor = optional.get();

		Collection<T> identifiers = _batchResult.getIdentifiers();

		_batchResultMessageMapper.mapItemTotalCount(
			_jsonObjectBuilder, identifiers.size());

		ApplicationURL applicationURL = _requestInfo.getApplicationURL();

		List<String> types = representor.getTypes();

		for (T identifier : identifiers) {
			JSONObjectBuilder itemJsonObjectBuilder = new JSONObjectBuilder();

			_pathFunction.apply(
				_batchResult.resourceName, identifier
			).ifPresent(
				path -> {
					_batchResultMessageMapper.onStartItem(
						_jsonObjectBuilder, itemJsonObjectBuilder);

					String url = createSingleURL(applicationURL, path);

					_batchResultMessageMapper.mapItemSelfURL(
						_jsonObjectBuilder, itemJsonObjectBuilder, url);

					_batchResultMessageMapper.mapItemTypes(
						_jsonObjectBuilder, itemJsonObjectBuilder, types);

					_batchResultMessageMapper.onFinishItem(
						_jsonObjectBuilder, itemJsonObjectBuilder);
				}
			);
		}

		_batchResultMessageMapper.onFinish(_jsonObjectBuilder, _batchResult);

		return Optional.of(_jsonObjectBuilder.build());
	}

	/**
	 * Creates {@code BatchResultWriter} instances.
	 */
	public interface Builder {

		/**
		 * Adds information to the builder about the page being written.
		 *
		 * @param  batchResult the page being written
		 * @return the updated builder
		 */
		public static <T> BatchResultMessageMapperStep<T> batchResult(
			BatchResult<T> batchResult) {

			return batchResultMessageMapper -> pathFunction ->
				representorFunction -> requestInfo ->
					() -> new BatchResultWriter<>(
						batchResult, batchResultMessageMapper, pathFunction,
						representorFunction, requestInfo);
		}

		public interface BatchResultMessageMapperStep<T> {

			/**
			 * Adds information to the builder about the {@link
			 * BatchResultMessageMapper}.
			 *
			 * @param  batchResultMessageMapper the batch result message mapper
			 * @return the updated builder
			 */
			public PathFunctionStep<T> batchResultMessageMapper(
				BatchResultMessageMapper<T> batchResultMessageMapper);

		}

		public interface BuildStep<T> {

			/**
			 * Constructs and returns a {@code BatchResultWriter} instance with
			 * the information provided to the builder.
			 *
			 * @return the {@code BatchResultWriter}
			 */
			public BatchResultWriter<T> build();

		}

		public interface PathFunctionStep<T> {

			/**
			 * Adds information to the builder about the function that converts
			 * an identifier to a {@link com.liferay.apio.architect.uri.Path}.
			 *
			 * @param  pathFunction the function to map an identifier to a
			 *         {@code Path}
			 * @return the updated builder
			 */
			public RepresentorFunctionStep<T> pathFunction(
				PathFunction pathFunction);

		}

		public interface RepresentorFunctionStep<T> {

			/**
			 * Adds information to the builder about the function that gets a
			 * class's {@link Representor}.
			 *
			 * @param  representorFunction the function that gets a class's
			 *         {@link Representor}
			 * @return the updated builder
			 */
			public RequestInfoStep<T> representorFunction(
				Function<String, Optional<Representor<Object>>>
					representorFunction);

		}

		public interface RequestInfoStep<T> {

			/**
			 * Adds information to the builder about the request.
			 *
			 * @param  requestInfo the information obtained from the request.
			 *         This can be created by using a {@link
			 *         RequestInfo.Builder}
			 * @return the updated builder
			 */
			public BuildStep<T> requestInfo(RequestInfo requestInfo);

		}

	}

	private BatchResultWriter(
		BatchResult<T> batchResult,
		BatchResultMessageMapper<T> batchResultMessageMapper,
		PathFunction pathFunction,
		Function<String, Optional<Representor<Object>>> representorFunction,
		RequestInfo requestInfo) {

		_batchResult = batchResult;
		_batchResultMessageMapper = batchResultMessageMapper;
		_pathFunction = pathFunction;
		_representorFunction = representorFunction;
		_requestInfo = requestInfo;

		_jsonObjectBuilder = new JSONObjectBuilder();
	}

	private final BatchResult<T> _batchResult;
	private final BatchResultMessageMapper<T> _batchResultMessageMapper;
	private final JSONObjectBuilder _jsonObjectBuilder;
	private final PathFunction _pathFunction;
	private final Function<String, Optional<Representor<Object>>>
		_representorFunction;
	private final RequestInfo _requestInfo;

}