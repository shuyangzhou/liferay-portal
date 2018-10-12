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

package com.liferay.apio.architect.sample.internal.jaxrs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Alejandro Hernández
 */
public class CORSFilterTest {

	@Test
	public void testFilterAddCORSHeaders() {
		CORSFilter corsFilter = new CORSFilter();

		ContainerResponseContext containerResponseContext = Mockito.mock(
			ContainerResponseContext.class);

		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

		Mockito.when(
			containerResponseContext.getHeaders()
		).thenReturn(
			headers
		);

		corsFilter.filter(null, containerResponseContext);

		assertThat(headers.getFirst("Access-Control-Allow-Origin"), is("*"));
		assertThat(
			headers.getFirst("Access-Control-Allow-Credentials"), is("true"));
		assertThat(
			headers.getFirst("Access-Control-Allow-Headers"),
			is("origin, content-type, accept, authorization"));
		assertThat(
			headers.getFirst("Access-Control-Allow-Methods"),
			is("GET, POST, PUT, DELETE, OPTIONS, HEAD"));
	}

}