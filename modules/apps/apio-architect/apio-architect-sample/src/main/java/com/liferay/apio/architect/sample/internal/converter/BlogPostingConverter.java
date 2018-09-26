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

import com.liferay.apio.architect.sample.internal.dto.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.dto.RatingModel;
import com.liferay.apio.architect.sample.internal.dto.ReviewModel;
import com.liferay.apio.architect.sample.internal.type.BlogPosting;
import com.liferay.apio.architect.sample.internal.type.Rating;
import com.liferay.apio.architect.sample.internal.type.Review;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides methods for creating {@link BlogPosting} instances out of other
 * DTOs.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogPostingConverter {

	/**
	 * Converts a {@link BlogPostingModel} to a {@link BlogPosting}.
	 *
	 * @param  blogPostingModel the internal blog posting model
	 * @return a {@link BlogPosting}
	 * @review
	 */
	public static BlogPosting toBlogPosting(BlogPostingModel blogPostingModel) {
		return new BlogPosting() {

			@Override
			public String getAlternativeHeadline() {
				return blogPostingModel.getSubtitle();
			}

			@Override
			public String getArticleBody() {
				return blogPostingModel.getContent();
			}

			@Override
			public Long getCreatorId() {
				return blogPostingModel.getCreatorId();
			}

			@Override
			public Date getDateCreated() {
				return blogPostingModel.getCreateDate();
			}

			@Override
			public Date getDateModified() {
				return blogPostingModel.getModifiedDate();
			}

			@Override
			public String getFileFormat() {
				return "text/html";
			}

			@Override
			public String getHeadline() {
				return blogPostingModel.getTitle();
			}

			@Override
			public Long getId() {
				return blogPostingModel.getId();
			}

			@Override
			public List<Review> getReviews() {
				List<ReviewModel> reviewModels =
					blogPostingModel.getReviewModels();

				Stream<ReviewModel> stream = reviewModels.stream();

				return stream.map(
					BlogPostingConverter::_toReview
				).collect(
					Collectors.toList()
				);
			}

		};
	}

	/**
	 * Converts a {@link RatingModel} to a {@link Rating}.
	 *
	 * @param  ratingModel the internal rating model
	 * @return a {@link Rating}
	 * @review
	 */
	private static Rating _toRating(RatingModel ratingModel) {
		return new Rating() {

			@Override
			public Long getAuthorId() {
				return ratingModel.getAuthorId();
			}

			@Override
			public Long getRatingValue() {
				return ratingModel.getValue();
			}

		};
	}

	/**
	 * Converts a {@link ReviewModel} to a {@link Review}.
	 *
	 * @param  reviewModel the internal review model
	 * @return a {@link Review}
	 * @review
	 */
	private static Review _toReview(ReviewModel reviewModel) {
		return new Review() {

			@Override
			public Rating getRating() {
				return _toRating(reviewModel.getRatingModel());
			}

			@Override
			public String getReviewBody() {
				return reviewModel.getBody();
			}

		};
	}

}