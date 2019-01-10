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

package com.liferay.apio.architect.internal.jaxrs.writer;

import static com.liferay.apio.architect.internal.unsafe.Unsafe.unsafeCast;

import com.liferay.apio.architect.credentials.Credentials;
import com.liferay.apio.architect.internal.jaxrs.writer.base.BaseMessageBodyWriter;
import com.liferay.apio.architect.internal.message.json.PageMessageMapper;
import com.liferay.apio.architect.internal.request.RequestInfo;
import com.liferay.apio.architect.internal.wiring.osgi.manager.message.json.PageMessageMapperManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.representable.RepresentableManager;
import com.liferay.apio.architect.internal.wiring.osgi.manager.uri.mapper.PathIdentifierMapperManager;
import com.liferay.apio.architect.internal.writer.PageWriter;
import com.liferay.apio.architect.pagination.Page;

import java.util.Optional;

import javax.ws.rs.core.Request;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Writes collection pages by using the {@link PageMessageMapper} that
 * corresponds to the media type.
 *
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(
	property = {
		"osgi.jaxrs.application.select=(liferay.apio.architect.application=true)",
		"osgi.jaxrs.extension=true", "osgi.jaxrs.name=Writer.Page"
	},
	service = MessageBodyWriter.class
)
@Provider
public class PageMessageBodyWriter<T>
	extends BaseMessageBodyWriter<Page<T>, PageMessageMapper<T>> {

	@Override
	public boolean canWrite(Class<?> clazz) {
		return Page.class.isAssignableFrom(clazz);
	}

	@Override
	public Optional<PageMessageMapper<T>> getMessageMapperOptional(
		Request request) {

		return _pageMessageMapperManager.getPageMessageMapperOptional(request);
	}

	@Override
	protected String write(
		Page<T> page, PageMessageMapper<T> pageMessageMapper,
		RequestInfo requestInfo) {

		Credentials credentials = providerManager.provideMandatory(
			request, Credentials.class);

		PageWriter<T> pageWriter = PageWriter.create(
			builder -> builder.page(
				page
			).pageMessageMapper(
				pageMessageMapper
			).pathFunction(
				_pathIdentifierMapperManager::mapToPath
			).resourceNameFunction(
				nameManager::getNameOptional
			).representorFunction(
				name -> unsafeCast(
					_representableManager.getRepresentorOptional(name))
			).requestInfo(
				requestInfo
			).singleModelFunction(
				this::getSingleModelOptional
			).actionSemanticsFunction(
				resource -> actionManager.getActionSemantics(
					resource, credentials, requestInfo.getHttpServletRequest())
			).build());

		return pageWriter.write();
	}

	@Reference
	private PageMessageMapperManager _pageMessageMapperManager;

	@Reference
	private PathIdentifierMapperManager _pathIdentifierMapperManager;

	@Reference
	private RepresentableManager _representableManager;

}