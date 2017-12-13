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

import com.liferay.portal.kernel.model.User;

import java.util.function.Function;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Javier Gamarra
 */
public class CommentScopedCollectionCollectionResourceTest
	extends CollectionResourceTest {

	@Test
	public void testBuildRepresentor() {
		CommentScopedCollectionResource commentScopedCollectionResource =
			new CommentScopedCollectionResource();

		commentScopedCollectionResource.buildRepresentor(representorBuilderSpy);

		Mockito.verify(
			representorBuilderSpy
		).identifier(
			any(Function.class)
		);

		verifyIdentifier().addEmbeddedModel(
			eq("author"), eq(User.class), any(Function.class));
		verifyIdentifier().addString(eq("text"), any(Function.class));
		verifyIdentifier().addType(eq("Comment"));
	}

}