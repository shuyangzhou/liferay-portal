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

package com.liferay.apio.architect.test.util.form;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.apio.architect.form.Body;
import com.liferay.apio.architect.internal.form.JSONBodyImpl;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class JSONFormConditionsTest {

	@Test
	public void testRetrievingAListReturnsTheJsonValue() throws IOException {
		Body body = _createBody("{\"list\":[\"value\"]}");

		Optional<List<String>> valueListOptional = body.getValueListOptional(
			"list");

		assertThat(valueListOptional.isPresent(), is(true));

		String value = valueListOptional.get().get(0);

		assertThat(value, is("value"));
	}

	@Test
	public void testRetrievingANestedListReturnsTheJsonValue()
		throws IOException {

		Body body = _createBody("{\"key\":[{\"nestedKey\":true}]}");

		Optional<List<Body>> nestedBodyOptional =
			body.getNestedBodyListOptional("key");

		assertThat(nestedBodyOptional.isPresent(), is(true));

		List<Body> bodies = nestedBodyOptional.get();

		Body nestedBody = bodies.get(0);

		Optional<String> valueOptional = nestedBody.getValueOptional(
			"nestedKey");

		assertThat(valueOptional.isPresent(), is(true));
		assertThat(valueOptional.get(), is("true"));
	}

	@Test
	public void testRetrievingANestedValueReturnsTheJsonValue()
		throws IOException {

		Body body = _createBody("{\"key\":{\"nestedKey\":true}}");

		Optional<Body> nestedBodyOptional = body.getNestedBodyOptional("key");

		assertThat(nestedBodyOptional.isPresent(), is(true));

		Body nestedBody = nestedBodyOptional.get();

		Optional<String> valueOptional = nestedBody.getValueOptional(
			"nestedKey");

		assertThat(valueOptional.isPresent(), is(true));
		assertThat(valueOptional.get(), is("true"));
	}

	@Test
	public void testRetrievingAValueReturnsTheJsonValue() throws IOException {
		Body body = _createBody("{\"key\":true}");

		Optional<String> valueOptional = body.getValueOptional("key");

		assertThat(valueOptional.isPresent(), is(true));
		assertThat(valueOptional.get(), is("true"));
	}

	private Body _createBody(String s) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode jsonNode = objectMapper.readTree(s);

		return new JSONBodyImpl(jsonNode);
	}

}