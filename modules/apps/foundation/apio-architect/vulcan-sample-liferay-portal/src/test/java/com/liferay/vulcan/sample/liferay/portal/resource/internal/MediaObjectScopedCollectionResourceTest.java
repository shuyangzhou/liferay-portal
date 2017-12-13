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

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.portal.kernel.model.User;
import com.liferay.vulcan.alias.BinaryFunction;

import java.util.function.Function;

import org.junit.Test;

/**
 * @author Javier Gamarra
 */
public class MediaObjectScopedCollectionResourceTest
	extends CollectionResourceTest {

	@Test
	public void testBuildRepresentor() {
		MediaObjectScopedCollectionResource
			mediaObjectScopedCollectionResource =
				new MediaObjectScopedCollectionResource();

		mediaObjectScopedCollectionResource.buildRepresentor(
			representorBuilderSpy);

		verifyIdentifier().addBidirectionalModel(
			eq("folder"), eq("mediaObjects"), eq(DLFolder.class),
			any(Function.class), any(Function.class));
		verifyIdentifier().addBinary(
			eq("contentStream"), any(BinaryFunction.class));
		verifyIdentifier().addDate(eq("dateCreated"), any(Function.class));
		verifyIdentifier().addDate(eq("dateModified"), any(Function.class));
		verifyIdentifier().addDate(eq("datePublished"), any(Function.class));
		verifyIdentifier().addEmbeddedModel(
			eq("author"), eq(User.class), any(Function.class));
		verifyIdentifier().addNumber(eq("contentSize"), any(Function.class));
		verifyIdentifier().addString(eq("fileFormat"), any(Function.class));
		verifyIdentifier().addString(eq("headline"), any(Function.class));
		verifyIdentifier().addString(eq("name"), any(Function.class));
		verifyIdentifier().addString(eq("text"), any(Function.class));
		verifyIdentifier().addType(eq("MediaObject"));
	}

}