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

package com.liferay.apio.architect.form;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.Is.is;

import com.liferay.apio.architect.form.Form.Builder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.BadRequestException;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class FormTest {

	@Test
	public void testFormCreatesValidForm() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredString(
			"string1", (map, string) -> map.put("s1", string)
		).addOptionalString(
			"string2", (map, string) -> map.put("s2", string)
		).addRequiredDate(
			"date1", (map, string) -> map.put("d1", string)
		).addOptionalDate(
			"date2", (map, string) -> map.put("d2", string)
		).addRequiredLong(
			"long1", (map, string) -> map.put("l1", string)
		).addOptionalLong(
			"long2", (map, string) -> map.put("l2", string)
		).build();

		Map<String, Object> map = form.get(_body);

		assertThat(map.size(), is(equalTo(6)));
		assertThat(map, hasEntry(equalTo("s1"), equalTo("Apio")));
		assertThat(map, hasEntry(equalTo("s2"), equalTo("Hypermedia")));
		assertThat(
			map, hasEntry(equalTo("d1"), equalTo(new Date(1465981200000L))));
		assertThat(
			map, hasEntry(equalTo("d2"), equalTo(new Date(1491244560000L))));
		assertThat(map, hasEntry(equalTo("l1"), equalTo(42L)));
		assertThat(map, hasEntry(equalTo("l2"), equalTo(2017L)));
	}

	@Test
	public void testFormDoesNotAddMissingOptionals() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredString(
			"string1", (map, string) -> map.put("s1", string)
		).addOptionalString(
			"string3", (map, string) -> map.put("s2", string)
		).addOptionalDate(
			"date3", (map, string) -> map.put("d2", string)
		).build();

		Map<String, Object> map = form.get(_body);

		assertThat(map.size(), is(equalTo(1)));
		assertThat(map, hasEntry(equalTo("s1"), equalTo("Apio")));
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfOptionalDateIsNotDate() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addOptionalDate(
			"long1", (map, string) -> map.put("l1", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfOptionalLongIsNotLong() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addOptionalLong(
			"string1", (map, string) -> map.put("s1", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfOptionalStringIsNotString() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addOptionalString(
			"long1", (map, string) -> map.put("l1", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfRequiredDateIsNotDate() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredDate(
			"long1", (map, string) -> map.put("l1", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfRequiredIsNotPresent() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredString(
			"string1", (map, string) -> map.put("s1", string)
		).addRequiredString(
			"string3", (map, string) -> map.put("s2", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfRequiredLongIsNotLong() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredLong(
			"string1", (map, string) -> map.put("s1", string)
		).build();

		form.get(_body);
	}

	@Test(expected = BadRequestException.class)
	public void testFormFailsIfRequiredStringIsNotString() {
		Builder<Map<String, Object>> builder = new Builder<>();

		Form<Map<String, Object>> form = builder.constructor(
			HashMap::new
		).addRequiredString(
			"long1", (map, string) -> map.put("l1", string)
		).build();

		form.get(_body);
	}

	private final Map<String, Object> _body = new HashMap<String, Object>() {
		{
			put("string1", "Apio");
			put("string2", "Hypermedia");
			put("date1", "2016-06-15T09:00Z");
			put("date2", "2017-04-03T18:36Z");
			put("long1", 42L);
			put("long2", 2017L);
		}
	};

}