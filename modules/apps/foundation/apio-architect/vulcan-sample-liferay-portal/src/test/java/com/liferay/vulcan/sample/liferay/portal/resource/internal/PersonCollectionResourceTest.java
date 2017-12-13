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

package com.liferay.vulcan.sample.liferay.portal.resource.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import java.util.function.Function;

import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class PersonCollectionResourceTest extends CollectionResourceTest {

	@Test
	public void testBuildRepresentor() {
		PersonCollectionResource personCollectionResource =
			new PersonCollectionResource();

		personCollectionResource.buildRepresentor(representorBuilderSpy);

		verifyIdentifier().addDate(eq("birthDate"), any(Function.class));
		verifyIdentifier().addString(eq("additionalName"), any(Function.class));
		verifyIdentifier().addString(eq("alternateName"), any(Function.class));
		verifyIdentifier().addString(eq("email"), any(Function.class));
		verifyIdentifier().addString(eq("familyName"), any(Function.class));
		verifyIdentifier().addString(eq("gender"), any(Function.class));
		verifyIdentifier().addString(eq("givenName"), any(Function.class));
		verifyIdentifier().addString(eq("jobTitle"), any(Function.class));
		verifyIdentifier().addString(eq("name"), any(Function.class));
		verifyIdentifier().addType(eq("Person"));
	}

}