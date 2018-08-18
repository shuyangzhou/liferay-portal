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

import static com.liferay.apio.architect.impl.url.URLCreator.createFormURL;
import static com.liferay.apio.architect.impl.url.URLCreator.createOperationURL;

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.impl.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.impl.message.json.OperationMapper;
import com.liferay.apio.architect.impl.request.RequestInfo;
import com.liferay.apio.architect.operation.Operation;

import java.util.Optional;

/**
 * Writes the operations identified by the methods of an {@link
 * OperationMapper}.
 *
 * @author Javier Gamarra
 */
public class OperationWriter {

	public OperationWriter(
		OperationMapper operationMapper, RequestInfo requestInfo,
		JSONObjectBuilder jsonObjectBuilder) {

		_operationMapper = operationMapper;
		_requestInfo = requestInfo;
		_jsonObjectBuilder = jsonObjectBuilder;
	}

	public void write(Operation operation) {
		JSONObjectBuilder operationJSONObjectBuilder = new JSONObjectBuilder();

		Optional<String> urlOptional = createOperationURL(
			_requestInfo.getApplicationURL(), operation);

		urlOptional.ifPresent(
			url -> _operationMapper.mapOperationURL(
				operationJSONObjectBuilder, url));

		Optional<Form> formOptional = operation.getFormOptional();

		formOptional.map(
			form -> createFormURL(_requestInfo.getApplicationURL(), form)
		).ifPresent(
			url -> _operationMapper.mapFormURL(operationJSONObjectBuilder, url)
		);

		_operationMapper.mapHTTPMethod(
			operationJSONObjectBuilder, operation.getHttpMethod());

		_operationMapper.onFinish(
			_jsonObjectBuilder, operationJSONObjectBuilder, operation);
	}

	private final JSONObjectBuilder _jsonObjectBuilder;
	private final OperationMapper _operationMapper;
	private final RequestInfo _requestInfo;

}