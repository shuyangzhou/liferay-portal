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

import static com.liferay.apio.architect.operation.HTTPMethod.PUT;
import static com.liferay.apio.architect.test.util.form.MockFormCreator.createForm;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class UpdateOperationTest {

	@Test
	public void testUpdateOperationWithForm() {
		UpdateOperation updateOperation = new UpdateOperation(
			createForm(), "name");

		assertThat(updateOperation.getFormOptional(), is(optionalWithValue()));
		assertThat(updateOperation.getHttpMethod(), is(PUT));
		assertThat(updateOperation.getName(), is("name/update"));
		assertThat(updateOperation.isCollection(), is(false));
	}

	@Test
	public void testUpdateOperationWithoutForm() {
		UpdateOperation updateOperation = new UpdateOperation(null, "name");

		assertThat(updateOperation.getFormOptional(), is(emptyOptional()));
		assertThat(updateOperation.getHttpMethod(), is(PUT));
		assertThat(updateOperation.getName(), is("name/update"));
		assertThat(updateOperation.isCollection(), is(false));
	}

}