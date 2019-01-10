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

package com.liferay.apio.architect.internal.action.converter;

import static com.liferay.apio.architect.internal.action.converter.EntryPointConverter.getEntryPointFrom;

import static java.util.Collections.emptyList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import com.liferay.apio.architect.internal.action.ActionSemantics;
import com.liferay.apio.architect.internal.entrypoint.EntryPoint;
import com.liferay.apio.architect.pagination.Page;
import com.liferay.apio.architect.resource.Resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class EntryPointConverterTest {

	@Test(expected = UnsupportedOperationException.class)
	public void testConstructorThrowsException() throws Throwable {
		Constructor<?> constructor =
			EntryPointConverter.class.getDeclaredConstructors()[0];

		constructor.setAccessible(true);

		try {
			constructor.newInstance();
		}
		catch (InvocationTargetException ite) {
			throw ite.getTargetException();
		}
	}

	@Test
	public void testGetEntryPointFromEmptyStreamCreatesEmptyEntryPoint() {
		Stream<ActionSemantics> stream = Stream.empty();

		EntryPoint entryPoint = getEntryPointFrom(stream);

		assertThat(entryPoint.getResourceNames(), is(empty()));
	}

	@Test
	public void testGetEntryPointFromStreamFiltersActionSemantics() {
		ActionSemantics actionSemantics = ActionSemantics.ofResource(
			Resource.Paged.of("name1")
		).name(
			"retrieve"
		).method(
			"GET"
		).returns(
			Page.class
		).permissionFunction(
		).executeFunction(
			__ -> null
		).annotatedWith(
			() -> com.liferay.apio.architect.annotation.EntryPoint.class
		).build();

		Stream<ActionSemantics> stream = Stream.of(
			actionSemantics, actionSemantics.withName("create"),
			actionSemantics.withAnnotations(emptyList()),
			actionSemantics.withResource(Resource.Paged.of("name2")),
			actionSemantics.withReturnClass(String.class),
			actionSemantics.withResource(Resource.Item.of("item")),
			actionSemantics.withMethod("PUT"),
			actionSemantics.withResource(Resource.Paged.of("name3")));

		EntryPoint entryPoint = getEntryPointFrom(stream);

		List<String> resourceNames = entryPoint.getResourceNames();

		assertThat(resourceNames, contains("name1", "name2", "name3"));
	}

}