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

package com.liferay.vulcan.message.json.ld.internal;

import com.google.gson.JsonObject;

import com.liferay.vulcan.jaxrs.json.internal.JSONObjectBuilderImpl;
import com.liferay.vulcan.jaxrs.json.internal.StringFunctionalList;

import java.util.Arrays;

import org.apache.poi.ss.formula.functions.T;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class JSONLDSingleModelMessageMapperTest {

	@Before
	public void setUp() {
		_jsonLDSingleModelMessageMapper =
			new JSONLDSingleModelMessageMapper<>();
		_jsonObjectBuilder = new JSONObjectBuilderImpl();
	}

	@Test
	public void testMapLinkedResourceURL() {
		_jsonLDSingleModelMessageMapper.mapLinkedResourceURL(
			_jsonObjectBuilder, new StringFunctionalList(null, "fieldValue"),
			"url");

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals(
			"{\"fieldValue\":\"url\",\"@context\":" +
				"{\"fieldValue\":{\"@type\":\"@id\"}}}",
			build.toString());
	}

	@Test
	public void testMapTypes() {
		_jsonLDSingleModelMessageMapper.mapTypes(
			_jsonObjectBuilder, Arrays.asList("fieldName"));

		JsonObject build = _jsonObjectBuilder.build();

		Assert.assertEquals("{\"@type\":[\"fieldName\"]}", build.toString());
	}

	private JSONLDSingleModelMessageMapper<T> _jsonLDSingleModelMessageMapper;
	private JSONObjectBuilderImpl _jsonObjectBuilder;

}