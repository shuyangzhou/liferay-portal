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

package com.liferay.apio.architect.impl.message.json.plain;

import com.liferay.apio.architect.impl.message.json.EntryPointMessageMapper;
import com.liferay.apio.architect.impl.message.json.JSONObjectBuilder;

import org.osgi.service.component.annotations.Component;

/**
 * Represents the entry point in plain JSON.
 *
 * @author Alejandro Hernández
 */
@Component
public class PlainJSONEntryPointMessageMapper
	implements EntryPointMessageMapper {

	@Override
	public String getMediaType() {
		return "application/json";
	}

	@Override
	public void mapItemSelfURL(
		JSONObjectBuilder entryPointJSONObjectBuilder,
		JSONObjectBuilder itemJSONObjectBuilder, String resourceName,
		String url) {

		entryPointJSONObjectBuilder.field(
			resourceName
		).stringValue(
			url
		);
	}

}