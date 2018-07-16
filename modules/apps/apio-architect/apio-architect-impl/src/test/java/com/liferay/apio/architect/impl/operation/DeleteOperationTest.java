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

import static com.liferay.apio.architect.operation.HTTPMethod.DELETE;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class DeleteOperationTest {

	@Test
	public void testDeleteOperation() {
		DeleteOperation deleteOperation = new DeleteOperation("name");

		assertThat(deleteOperation.getFormOptional(), is(emptyOptional()));
		assertThat(deleteOperation.getHttpMethod(), is(DELETE));
		assertThat(deleteOperation.getName(), is("name/delete"));
		assertThat(deleteOperation.isCollection(), is(false));
	}

}