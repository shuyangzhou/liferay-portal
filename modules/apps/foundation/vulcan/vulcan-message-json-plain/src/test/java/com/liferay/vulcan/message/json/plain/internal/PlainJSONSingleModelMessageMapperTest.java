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

package com.liferay.vulcan.message.json.plain.internal;

import com.google.gson.JsonObject;

import com.liferay.vulcan.jaxrs.json.internal.JSONObjectBuilderImpl;
import com.liferay.vulcan.jaxrs.json.internal.StringFunctionalList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class PlainJSONSingleModelMessageMapperTest {

	@Before
	public void setUp() {
		_plainJSONSingleModelMessageMapper =
			new PlainJSONSingleModelMessageMapper();
		_jsonObjectBuilder = new JSONObjectBuilderImpl();
	}

	@Test
	public void testMapBooleanField() {
		_plainJSONSingleModelMessageMapper.mapBooleanField(
			_jsonObjectBuilder, "fieldName", true);

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"fieldName\":true}", build.toString());
	}

	@Test
	public void testMapEmbeddedResourceBooleanField() {
		_plainJSONSingleModelMessageMapper.mapEmbeddedResourceBooleanField(
			_jsonObjectBuilder, new StringFunctionalList(null, "element"),
			"fieldName", true);

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"element\":true}", build.toString());
	}

	@Test
	public void testMapLink() {
		_plainJSONSingleModelMessageMapper.mapLink(
			_jsonObjectBuilder, "fieldName", "https://localhost:8080");

		JsonObject build = _jsonObjectBuilder.build();

		System.out.println(build.toString());

		Assert.assertEquals(
			"{\"fieldName\":\"https://localhost:8080\"}", build.toString());
	}

	@Test
	public void testMapNumberField() {
		_plainJSONSingleModelMessageMapper.mapNumberField(
			_jsonObjectBuilder, "fieldName", 1);

		JsonObject build = _jsonObjectBuilder.build();

		System.out.println(build.toString());

		Assert.assertEquals("{\"fieldName\":1}", build.toString());
	}

	@Test
	public void testMapSelfURL() {
		_plainJSONSingleModelMessageMapper.mapSelfURL(
			_jsonObjectBuilder, "https://localhost:8080");

		JsonObject build = _jsonObjectBuilder.build();

		System.out.println(build.toString());

		Assert.assertEquals(
			"{\"self\":\"https://localhost:8080\"}", build.toString());
	}

	@Test
	public void testMapStringField() {
		_plainJSONSingleModelMessageMapper.mapStringField(
			_jsonObjectBuilder, "fieldName", "value");

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"fieldName\":\"value\"}", build.toString());
	}

	private JSONObjectBuilderImpl _jsonObjectBuilder;
	private PlainJSONSingleModelMessageMapper
		_plainJSONSingleModelMessageMapper;

}