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

package com.liferay.vulcan.message.hal.internal;

import com.google.gson.JsonObject;

import com.liferay.vulcan.jaxrs.json.internal.JSONObjectBuilderImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class HALPageMessageMapperTest {

	@Before
	public void setUp() {
		_jsonObjectBuilder = new JSONObjectBuilderImpl();

		_halPageMessageMapper = new HALPageMessageMapper();
	}

	@Test
	public void testMapCollectionURL() {
		_halPageMessageMapper.mapCollectionURL(
			_jsonObjectBuilder, "http://localhost:8080");

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals(
			"{\"_links\":{\"collection\":" +
				"{\"href\":\"http://localhost:8080\"}}}",
			build.toString());
	}

	@Test
	public void testMapCurrentPageURL() {
		_halPageMessageMapper.mapCurrentPageURL(
			_jsonObjectBuilder, "http://localhost:8080");

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals(
			"{\"_links\":{\"self\":{\"href\":\"http://localhost:8080\"}}}",
			build.toString());
	}

	@Test
	public void testMapFirstPageURL() {
		_halPageMessageMapper.mapFirstPageURL(
			_jsonObjectBuilder, "http://localhost:8080");

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals(
			"{\"_links\":{\"first\":{\"href\":\"http://localhost:8080\"}}}",
			build.toString());
	}

	@Test
	public void testMapItemBooleanField() {
		_halPageMessageMapper.mapItemBooleanField(
			null, _jsonObjectBuilder, "fieldName", true);

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"fieldName\":true}", build.toString());
	}

	@Test
	public void testMapItemEmbeddedResourceBooleanField() {
		_halPageMessageMapper.mapItemBooleanField(
			null, _jsonObjectBuilder, "fieldName", true);

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"fieldName\":true}", build.toString());
	}

	@Test
	public void testMapPageCount() {
		_halPageMessageMapper.mapPageCount(_jsonObjectBuilder, 1);

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"count\":1}", build.toString());
	}

	private HALPageMessageMapper _halPageMessageMapper;
	private JSONObjectBuilderImpl _jsonObjectBuilder;

}