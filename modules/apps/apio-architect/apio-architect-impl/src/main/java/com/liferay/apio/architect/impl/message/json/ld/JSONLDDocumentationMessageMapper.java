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

package com.liferay.apio.architect.impl.message.json.ld;

import static com.liferay.apio.architect.impl.message.json.ld.JSONLDMessageMapperUtil.getOperationTypes;
import static com.liferay.apio.architect.operation.HTTPMethod.DELETE;
import static com.liferay.apio.architect.operation.HTTPMethod.GET;

import com.liferay.apio.architect.impl.documentation.Documentation;
import com.liferay.apio.architect.impl.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.impl.message.json.JSONObjectBuilder;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;

import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * Represents documentation in JSON-LD + Hydra format.
 *
 * <p>
 * For more information, see <a href="https://json-ld.org/">JSON-LD </a> and <a
 * href="https://www.hydra-cg.com/">Hydra </a> .
 * </p>
 *
 * @author Alejandro Hernández
 * @author Javier Gamarra
 * @author Zoltán Takács
 */
@Component
public class JSONLDDocumentationMessageMapper
	implements DocumentationMessageMapper {

	@Override
	public String getMediaType() {
		return "application/ld+json";
	}

	@Override
	public void mapDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {

		jsonObjectBuilder.field(
			"description"
		).stringValue(
			description
		);
	}

	@Override
	public void mapEntryPoint(
		JSONObjectBuilder jsonObjectBuilder, String entryPoint) {

		jsonObjectBuilder.field(
			"entrypoint"
		).stringValue(
			entryPoint
		);
	}

	@Override
	public void mapOperation(
		JSONObjectBuilder jsonObjectBuilder, String resourceName, String type,
		Operation operation) {

		jsonObjectBuilder.field(
			"@id"
		).stringValue(
			"_:" + operation.getName()
		);

		jsonObjectBuilder.field(
			"@type"
		).arrayValue(
		).addAllStrings(
			getOperationTypes(operation)
		);

		jsonObjectBuilder.field(
			"method"
		).stringValue(
			operation.getHttpMethod().toString()
		);

		jsonObjectBuilder.field(
			"returns"
		).stringValue(
			_getReturnValue(type, operation)
		);
	}

	@Override
	public void mapProperty(
		JSONObjectBuilder jsonObjectBuilder, String fieldName) {

		jsonObjectBuilder.field(
			"@type"
		).stringValue(
			"SupportedProperty"
		);

		jsonObjectBuilder.field(
			"property"
		).stringValue(
			fieldName
		);
	}

	@Override
	public void mapResource(
		JSONObjectBuilder jsonObjectBuilder, String resourceType) {

		jsonObjectBuilder.field(
			"@id"
		).stringValue(
			resourceType
		);

		jsonObjectBuilder.field(
			"@type"
		).stringValue(
			"Class"
		);

		jsonObjectBuilder.field(
			"title"
		).stringValue(
			resourceType
		);
	}

	@Override
	public void mapResourceCollection(
		JSONObjectBuilder jsonObjectBuilder, String resourceType) {

		jsonObjectBuilder.field(
			"@id"
		).stringValue(
			"vocab:" + resourceType + "Collection"
		);

		jsonObjectBuilder.field(
			"@type"
		).stringValue(
			"Class"
		);

		jsonObjectBuilder.field(
			"subClassOf"
		).stringValue(
			"Collection"
		);

		jsonObjectBuilder.field(
			"description"
		).stringValue(
			"A collection of " + resourceType
		);

		jsonObjectBuilder.field(
			"title"
		).stringValue(
			resourceType + "Collection"
		);

		Stream.of(
			"totalItems", "member", "numberOfItems"
		).forEach(
			fieldName -> {
				JSONObjectBuilder propertyJsonObjectBuilder =
					new JSONObjectBuilder();

				mapProperty(propertyJsonObjectBuilder, fieldName);

				onFinishProperty(
					jsonObjectBuilder, propertyJsonObjectBuilder, fieldName);
			}
		);
	}

	@Override
	public void mapTitle(JSONObjectBuilder jsonObjectBuilder, String title) {
		jsonObjectBuilder.field(
			"title"
		).stringValue(
			title
		);
	}

	@Override
	public void onFinish(
		JSONObjectBuilder jsonObjectBuilder, Documentation documentation) {

		jsonObjectBuilder.field(
			"@context"
		).arrayValue(
			arrayBuilder -> arrayBuilder.add(
				builder -> builder.field(
					"@vocab"
				).stringValue(
					"http://schema.org/"
				)
			),
			arrayBuilder -> arrayBuilder.addString(
				"https://www.w3.org/ns/hydra/core#"),
			arrayBuilder -> arrayBuilder.add(
				builder -> builder.field(
					"expects"
				).fields(
					nestedBuilder -> nestedBuilder.field(
						"@type"
					).stringValue(
						"@id"
					),
					nestedBuilder -> nestedBuilder.field(
						"@id"
					).stringValue(
						"hydra:expects"
					)
				),
				builder -> builder.field(
					"returns"
				).fields(
					nestedBuilder -> nestedBuilder.field(
						"@id"
					).stringValue(
						"hydra:returns"
					),
					nestedBuilder -> nestedBuilder.field(
						"@type"
					).stringValue(
						"@id"
					)
				)
			)
		);

		jsonObjectBuilder.field(
			"@id"
		).stringValue(
			"/doc"
		);

		jsonObjectBuilder.field(
			"@type"
		).stringValue(
			"ApiDocumentation"
		);
	}

	@Override
	public void onFinishOperation(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder operationJsonObjectBuilder, Operation operation) {

		documentationJsonObjectBuilder.field(
			"supportedOperation"
		).arrayValue(
		).add(
			operationJsonObjectBuilder
		);
	}

	@Override
	public void onFinishProperty(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder, String formField) {

		documentationJsonObjectBuilder.field(
			"supportedProperty"
		).arrayValue(
		).add(
			propertyJsonObjectBuilder
		);
	}

	@Override
	public void onFinishResource(
		JSONObjectBuilder documentationJsonObjectBuilder,
		JSONObjectBuilder resourceJsonObjectBuilder, String type) {

		documentationJsonObjectBuilder.field(
			"supportedClass"
		).arrayValue(
		).add(
			resourceJsonObjectBuilder
		);
	}

	private String _getReturnValue(String type, Operation operation) {
		String value = null;

		HTTPMethod httpMethod = operation.getHttpMethod();

		if (DELETE.equals(httpMethod)) {
			value = "http://www.w3.org/2002/07/owl#Nothing";
		}
		else if (operation.isCollection() && httpMethod.equals(GET)) {
			value = "Collection";
		}
		else {
			value = type;
		}

		return value;
	}

}