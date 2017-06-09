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

package com.liferay.vulcan.jaxrs.writer.json.internal;

import com.liferay.vulcan.error.VulcanDeveloperError;
import com.liferay.vulcan.list.FunctionalList;
import com.liferay.vulcan.response.control.Embedded;
import com.liferay.vulcan.response.control.Fields;
import com.liferay.vulcan.wiring.osgi.RelatedModel;
import com.liferay.vulcan.wiring.osgi.RepresentorManager;
import com.liferay.vulcan.wiring.osgi.URIResolver;

import java.net.URI;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Hernández
 * @author Carlos Sierra Andrés
 * @author Jorge Ferrer
 */
@Component(immediate = true, service = WriterHelper.class)
public class WriterHelper {

	public String getAbsoluteURL(UriInfo uriInfo, String relativeURI) {
		UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();

		uriBuilder = uriBuilder.clone();

		uriBuilder.path(relativeURI);

		URI uri = uriBuilder.build();

		return uri.toString();
	}

	public <T> void writeFields(
		T model, Class<T> modelClass, Fields fields,
		BiConsumer<String, Object> biConsumer) {

		Map<String, Function<T, Object>> fieldFunctions =
			_representorManager.getFieldFunctions(modelClass);

		Predicate<String> isFieldIncludedPredicate = _getFieldIncludedPredicate(
			modelClass, fields);

		for (String field : fieldFunctions.keySet()) {
			if (isFieldIncludedPredicate.test(field)) {
				Object data = fieldFunctions.get(field).apply(model);

				if (data != null) {
					biConsumer.accept(field, data);
				}
			}
		}
	}

	public <T, U> void writeLinkedRelatedModel(
		RelatedModel<T, U> relatedModel, T parentModel,
		Class<T> parentModelClass,
		FunctionalList<String> parentEmbeddedPathElements, UriInfo uriInfo,
		Fields fields, Embedded embedded,
		BiConsumer<String, FunctionalList<String>> biConsumer) {

		writeRelatedModel(
			relatedModel, parentModel, parentModelClass,
			parentEmbeddedPathElements, uriInfo, fields, embedded,
			(model, modelClass, embeddedPathElements) -> {
			},
			biConsumer);
	}

	public <T> void writeLinks(
		Class<T> modelClass, Fields fields,
		BiConsumer<String, String> biConsumer) {

		Map<String, String> links = _representorManager.getLinks(modelClass);

		Predicate<String> isFieldIncludedPredicate = _getFieldIncludedPredicate(
			modelClass, fields);

		for (String key : links.keySet()) {
			if (isFieldIncludedPredicate.test(key)) {
				biConsumer.accept(key, links.get(key));
			}
		}
	}

	public <T, U> void writeRelatedModel(
		RelatedModel<T, U> relatedModel, T parentModel,
		Class<T> parentModelClass,
		FunctionalList<String> parentEmbeddedPathElements, UriInfo uriInfo,
		Fields fields, Embedded embedded,
		TriConsumer<U, Class<U>, FunctionalList<String>> triConsumer,
		BiConsumer<String, FunctionalList<String>> biConsumer) {

		Predicate<String> isFieldIncludedPredicate = _getFieldIncludedPredicate(
			parentModelClass, fields);

		String key = relatedModel.getKey();

		if (!isFieldIncludedPredicate.test(key)) {
			return;
		}

		Optional<U> modelOptional =
			relatedModel.getModelFunction().apply(parentModel);

		if (!modelOptional.isPresent()) {
			return;
		}

		U model = modelOptional.get();

		Class<U> modelClass = relatedModel.getModelClass();

		Optional<String> uriOptional =
			_uriResolver.getSingleResourceURIOptional(modelClass, model);

		uriOptional.ifPresent(
			uri -> {
				String url = getAbsoluteURL(uriInfo, uri);

				FunctionalList<String> embeddedPathElements =
					new StringFunctionalList(parentEmbeddedPathElements, key);

				biConsumer.accept(url, embeddedPathElements);

				Stream<String> stream = Stream.concat(
					Stream.of(embeddedPathElements.head()),
					embeddedPathElements.tail());

				String embeddedPath = String.join(
					".", stream.collect(Collectors.toList()));

				if (embedded.getEmbeddedPredicate().test(embeddedPath)) {
					triConsumer.accept(model, modelClass, embeddedPathElements);
				}
			});
	}

	public <T> void writeSingleResourceURL(
		T model, Class<T> modelClass, UriInfo uriInfo,
		Consumer<String> consumer) {

		Optional<String> optional = _uriResolver.getSingleResourceURIOptional(
			modelClass, model);

		String uri = optional.orElseThrow(
			() -> new VulcanDeveloperError.UnresolvableURI(modelClass));

		String url = getAbsoluteURL(uriInfo, uri);

		consumer.accept(url);
	}

	public <U> void writeTypes(
		Class<U> modelClass, Consumer<List<String>> consumer) {

		List<String> types = _representorManager.getTypes(modelClass);

		consumer.accept(types);
	}

	private <T> Predicate<String> _getFieldIncludedPredicate(
		Class<T> modelClass, Fields fields) {

		List<String> types = _representorManager.getTypes(modelClass);

		return fields.getFieldIncludedPredicate(types);
	}

	@Reference
	private RepresentorManager _representorManager;

	@Reference
	private URIResolver _uriResolver;

}