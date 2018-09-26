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

package com.liferay.apio.architect.sample.internal.converter;

import com.liferay.apio.architect.sample.internal.dto.RatingModel;
import com.liferay.apio.architect.sample.internal.dto.ReviewModel;
import com.liferay.apio.architect.sample.internal.type.Rating;
import com.liferay.apio.architect.sample.internal.type.Review;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides methods for transforming {@link Review} instances out of other DTOs.
 *
 * @author Alejandro Hernández
 * @review
 */
public class ReviewConverter {

	/**
	 * Converts a list of {@link Review} to a list of {@link ReviewModel}.
	 *
	 * @param  reviews the reviews
	 * @return a list of {@link ReviewModel}
	 * @review
	 */
	public static List<ReviewModel> toReviewModels(List<Review> reviews) {
		Stream<Review> stream = reviews.stream();

		return stream.map(
			ReviewConverter::_toReviewModel
		).collect(
			Collectors.toList()
		);
	}

	private static ReviewModel _toReviewModel(Review review) {
		Rating rating = review.getRating();

		RatingModel ratingModel = new RatingModel(
			rating.getAuthorId(), rating.getRatingValue());

		return new ReviewModel(review.getReviewBody(), ratingModel);
	}

}