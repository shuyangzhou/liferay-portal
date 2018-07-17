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

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.form.FormField;
import com.liferay.apio.architect.impl.message.json.FormMessageMapper;
import com.liferay.apio.architect.impl.message.json.JSONObjectBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * Represents forms in JSON-LD + Hydra format.
 *
 * <p>
 * For more information, see <a href="https://json-ld.org/">JSON-LD </a> and <a
 * href="https://www.hydra-cg.com/">Hydra </a> .
 * </p>
 *
 * @author Alejandro Hernández
 */
@Component
public class JSONLDFormMessageMapper implements FormMessageMapper {

	@Override
	public String getMediaType() {
		return "application/ld+json";
	}

	@Override
	public void mapFormDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {

		jsonObjectBuilder.field(
			"description"
		).stringValue(
			description
		);
	}

	@Override
	public void mapFormField(
		JSONObjectBuilder jsonObjectBuilder, FormField formField) {

		jsonObjectBuilder.field(
			"supportedProperty"
		).arrayValue(
		).add(
			builder -> {
				builder.field(
					"@type"
				).stringValue(
					"SupportedProperty"
				);

				builder.field(
					"property"
				).stringValue(
					formField.getName()
				);

				builder.field(
					"readable"
				).booleanValue(
					false
				);

				builder.field(
					"required"
				).booleanValue(
					formField.isRequired()
				);

				builder.field(
					"writeable"
				).booleanValue(
					true
				);
			}
		);
	}

	@Override
	public void mapFormTitle(
		JSONObjectBuilder jsonObjectBuilder, String title) {

		jsonObjectBuilder.field(
			"title"
		).stringValue(
			title
		);
	}

	@Override
	public void mapFormURL(JSONObjectBuilder jsonObjectBuilder, String url) {
		jsonObjectBuilder.field(
			"@id"
		).stringValue(
			url
		);
	}

	@Override
	public void onFinish(JSONObjectBuilder jsonObjectBuilder, Form form) {
		jsonObjectBuilder.field(
			"@type"
		).stringValue(
			"Class"
		);

		jsonObjectBuilder.field(
			"@context"
		).arrayValue(
			arrayBuilder -> arrayBuilder.add(
				builder -> builder.field(
					"@vocab"
				).stringValue(
					"http://schema.org/"
				)),
			arrayBuilder -> arrayBuilder.addString(
				"https://www.w3.org/ns/hydra/core#")
		);
	}

}