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

package com.liferay.vulcan.sample.liferay.portal.internal.resource;

import com.liferay.vulcan.resource.CollectionResource;
import com.liferay.vulcan.resource.Representor;
import com.liferay.vulcan.resource.Routes;
import com.liferay.vulcan.resource.builder.RepresentorBuilder;
import com.liferay.vulcan.resource.builder.RoutesBuilder;
import com.liferay.vulcan.sample.liferay.portal.rating.AggregateRating;
import com.liferay.vulcan.sample.liferay.portal.rating.AggregateRatingService;
import com.liferay.vulcan.sample.liferay.portal.resource.identifier.AggregateRatingIdentifier;
import com.liferay.vulcan.wiring.osgi.manager.CollectionResourceManager;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides all the necessary information to expose {@link AggregateRating}
 * resources through a web API.
 *
 * @author Alejandro Hernández
 */
@Component(immediate = true)
public class AggregateRatingCollectionResource
	implements CollectionResource<AggregateRating, AggregateRatingIdentifier> {

	@Override
	public Representor<AggregateRating, AggregateRatingIdentifier>
		buildRepresentor(
			RepresentorBuilder<AggregateRating, AggregateRatingIdentifier>
				representorBuilder) {

		return representorBuilder.identifier(
			AggregateRating::getAggregateRatingIdentifier
		).addField(
			"bestRating", aggregateRating -> 1
		).addField(
			"ratingCount", AggregateRating::getRatingCount
		).addField(
			"ratingValue", AggregateRating::getRatingValue
		).addField(
			"worstRating", aggregateRating -> 0
		).addType(
			"AggregateRating"
		).build();
	}

	@Override
	public String getName() {
		return "aggregate-ratings";
	}

	@Override
	public Routes<AggregateRating> routes(
		RoutesBuilder<AggregateRating, AggregateRatingIdentifier>
			routesBuilder) {

		return routesBuilder.addCollectionPageItemGetter(
			_aggregateRatingService::getAggregateRating
		).build();
	}

	@Reference
	private AggregateRatingService _aggregateRatingService;

	@Reference
	private CollectionResourceManager _collectionResourceManager;

}