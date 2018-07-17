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

package com.liferay.apio.architect.impl.operation;

import static com.liferay.apio.architect.operation.HTTPMethod.POST;
import static com.liferay.apio.architect.test.util.form.MockFormCreator.createForm;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class CreateOperationTest {

	@Test
	public void testCreateOperationWithForm() {
		CreateOperation createOperation = new CreateOperation(
			createForm(), "name");

		assertThat(createOperation.getFormOptional(), is(optionalWithValue()));
		assertThat(createOperation.getHttpMethod(), is(POST));
		assertThat(createOperation.getName(), is("name/create"));
		assertThat(createOperation.isCollection(), is(false));
	}

	@Test
	public void testCreateOperationWithoutForm() {
		CreateOperation createOperation = new CreateOperation(null, "name");

		assertThat(createOperation.getFormOptional(), is(emptyOptional()));
		assertThat(createOperation.getHttpMethod(), is(POST));
		assertThat(createOperation.getName(), is("name/create"));
		assertThat(createOperation.isCollection(), is(false));
	}

}