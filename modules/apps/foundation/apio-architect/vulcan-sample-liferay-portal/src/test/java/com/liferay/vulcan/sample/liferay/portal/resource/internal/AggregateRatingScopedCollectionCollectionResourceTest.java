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

import com.liferay.vulcan.sample.liferay.portal.rating.AggregateRatingService;
import com.liferay.vulcan.wiring.osgi.internal.resource.builder.RoutesBuilderImpl;

import java.util.function.Function;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Javier Gamarra
 */
public class AggregateRatingScopedCollectionCollectionResourceTest
	extends CollectionResourceTest {

	@Test
	public void testBuildRepresentor() {
		AggregateRatingScopedCollectionResource
			aggregateRatingScopedCollectionResource =
				new AggregateRatingScopedCollectionResource();

		aggregateRatingScopedCollectionResource.buildRepresentor(
			representorBuilderSpy);

		Mockito.verify(
			representorBuilderSpy
		).identifier(
			any(Function.class)
		);

		verifyIdentifier().addNumber(eq("bestRating"), any(Function.class));
		verifyIdentifier().addNumber(eq("ratingCount"), any(Function.class));
		verifyIdentifier().addNumber(eq("ratingValue"), any(Function.class));
		verifyIdentifier().addNumber(eq("worstRating"), any(Function.class));
		verifyIdentifier().addType(eq("AggregateRating"));
	}

	@Test
	public void testRoutes() {
		RoutesBuilderImpl routesBuilderMock = Mockito.mock(
			RoutesBuilderImpl.class);

		Mockito.when(
			routesBuilderMock.addCollectionPageItemGetter(any(Function.class))
		).thenReturn(
			routesBuilderMock
		);

		AggregateRatingScopedCollectionResource
			aggregateRatingScopedCollectionResource =
				new AggregateRatingScopedCollectionResource();

		aggregateRatingScopedCollectionResource.aggregateRatingService =
			Mockito.mock(AggregateRatingService.class);

		aggregateRatingScopedCollectionResource.routes(routesBuilderMock);

		Mockito.verify(
			routesBuilderMock
		).addCollectionPageItemGetter(
			any(Function.class)
		);
	}

}