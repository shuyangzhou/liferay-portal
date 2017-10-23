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

package com.liferay.vulcan.sample.liferay.portal.rating;

import aQute.bnd.annotation.ProviderType;

import com.liferay.vulcan.sample.liferay.portal.resource.identifier.AggregateRatingIdentifier;

/**
 * An instance of this interface represents an average rating for a Thing based
 * on multiple ratings or reviews.
 *
 * <p>
 * Conforms with the <a href="http://schema.org/AggregateRating">Aggregate
 * Rating</a> type from schema.org
 * </p>
 *
 * @author Alejandro Hernández
 * @review
 */
@ProviderType
public interface AggregateRating {

	/**
	 * Returns the identifier.
	 *
	 * @return the identifier.
	 * @review
	 */
	public AggregateRatingIdentifier getAggregateRatingIdentifier();

	/**
	 * Returns the count of total number of ratings.
	 *
	 * @return the total number of ratings.
	 * @review
	 */
	public Integer getRatingCount();

	/**
	 * Returns the rating value for the content.
	 *
	 * @return rating value.
	 * @review
	 */
	public Double getRatingValue();

}