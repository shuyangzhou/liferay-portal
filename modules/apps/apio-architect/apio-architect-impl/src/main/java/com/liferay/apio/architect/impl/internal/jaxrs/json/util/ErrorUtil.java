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

package com.liferay.apio.architect.impl.internal.jaxrs.json.util;

import static com.liferay.apio.architect.impl.internal.writer.ErrorWriter.writeError;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;
import static org.osgi.service.component.annotations.ReferencePolicyOption.GREEDY;

import com.liferay.apio.architect.error.APIError;
import com.liferay.apio.architect.impl.internal.message.json.ErrorMessageMapper;
import com.liferay.apio.architect.impl.internal.wiring.osgi.manager.exception.mapper.ExceptionMapperManager;
import com.liferay.apio.architect.impl.internal.wiring.osgi.manager.message.json.ErrorMessageMapperManager;
import com.liferay.apio.architect.logger.ApioLogger;

import java.util.Optional;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides utility methods for transforming exceptions into responses.
 *
 * @author Alejandro Hernández
 */
@Component(service = ErrorUtil.class)
public class ErrorUtil {

	/**
	 * Transforms an exception into a {@code Response}.
	 *
	 * @param  exception the exception
	 * @param  request the current request
	 * @return the response
	 */
	public Response getErrorResponse(Exception exception, Request request) {
		Optional<APIError> apiErrorOptional = _exceptionMapperManager.map(
			exception);

		if (!apiErrorOptional.isPresent()) {
			Class<? extends Exception> exceptionClass = exception.getClass();

			if (_apioLogger != null) {
				_apioLogger.warning(
					"No exception mapper found for " + exceptionClass);
			}

			if (exceptionClass.isAssignableFrom(
					WebApplicationException.class)) {

				WebApplicationException webApplicationException =
					(WebApplicationException)exception;

				return webApplicationException.getResponse();
			}

			return Response.serverError().build();
		}

		APIError apiError = apiErrorOptional.get();

		if (_apioLogger != null) {
			_apioLogger.error(apiError);
		}

		int statusCode = apiError.getStatusCode();

		Optional<ErrorMessageMapper> errorMessageMapperOptional =
			_errorMessageMapperManager.getErrorMessageMapperOptional(request);

		return errorMessageMapperOptional.map(
			errorMessageMapper -> Response.status(
				statusCode
			).type(
				errorMessageMapper.getMediaType()
			).entity(
				writeError(errorMessageMapper, apiError)
			).build()
		).orElseGet(
			() -> Response.status(
				statusCode
			).build()
		);
	}

	@Reference(cardinality = OPTIONAL, policyOption = GREEDY)
	private ApioLogger _apioLogger;

	@Reference
	private ErrorMessageMapperManager _errorMessageMapperManager;

	@Reference
	private ExceptionMapperManager _exceptionMapperManager;

}