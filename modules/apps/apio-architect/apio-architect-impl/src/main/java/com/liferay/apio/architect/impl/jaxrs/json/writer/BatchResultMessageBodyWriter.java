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

package com.liferay.apio.architect.impl.jaxrs.json.writer;

import static com.liferay.apio.architect.impl.wiring.osgi.util.GenericUtil.getFirstGenericTypeArgumentFromTypeTry;

import com.liferay.apio.architect.batch.BatchResult;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.functional.Try.Success;
import com.liferay.apio.architect.impl.jaxrs.json.writer.base.BaseMessageBodyWriter;
import com.liferay.apio.architect.impl.message.json.BatchResultMessageMapper;
import com.liferay.apio.architect.impl.request.RequestInfo;
import com.liferay.apio.architect.impl.wiring.osgi.manager.message.json.BatchResultMessageMapperManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.impl.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.impl.writer.BatchResultWriter;
import com.liferay.apio.architect.impl.writer.BatchResultWriter.Builder;

import java.lang.reflect.Type;

import java.util.Optional;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Request;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Writes batch results by using the {@link BatchResultMessageMapper} that
 * corresponds to the media type.
 *
 * @author Alejandro Hernández
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(liferay.apio.architect.application=true)",
		"osgi.jaxrs.extension=true"
	},
	service = MessageBodyWriter.class
)
@Provider
public class BatchResultMessageBodyWriter<T>
	extends BaseMessageBodyWriter
		<Success<BatchResult<T>>, BatchResultMessageMapper<T>> {

	@Override
	public boolean canWrite(Class<?> clazz, Type genericType) {
		Try<Class<Object>> classTry = getFirstGenericTypeArgumentFromTypeTry(
			genericType, Try.class);

		return classTry.filter(
			BatchResult.class::equals
		).isSuccess();
	}

	@Override
	public Optional<BatchResultMessageMapper<T>> getMessageMapperOptional(
		Request request) {

		return _batchResultMessageMapperManager.
			getBatchResultMessageMapperOptional(request);
	}

	@Override
	protected String write(
		Success<BatchResult<T>> success,
		BatchResultMessageMapper<T> batchResultMessageMapper,
		RequestInfo requestInfo) {

		BatchResultWriter<T> batchResultWriter = Builder.batchResult(
			success.getValue()
		).batchResultMessageMapper(
			batchResultMessageMapper
		).pathFunction(
			_pathIdentifierMapperManager::mapToPath
		).representorFunction(
			_representableManager::getRepresentorOptional
		).requestInfo(
			requestInfo
		).build();

		Optional<String> optional = batchResultWriter.write();

		return optional.orElseThrow(NotFoundException::new);
	}

	@Reference
	private BatchResultMessageMapperManager _batchResultMessageMapperManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private RepresentableManager _representableManager;

}