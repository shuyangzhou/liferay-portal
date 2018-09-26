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

import static com.liferay.apio.architect.sample.internal.converter.BlogPostingConverter.toBlogPosting;
import static com.liferay.apio.architect.sample.internal.converter.DemoDataUtil.BLOG_POSTING_MODEL;

import static org.exparity.hamcrest.date.DateMatchers.isToday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.liferay.apio.architect.sample.internal.type.BlogPosting;
import com.liferay.apio.architect.sample.internal.type.Rating;
import com.liferay.apio.architect.sample.internal.type.Review;

import java.util.List;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class BlogPostingConverterTest {

	@Test
	public void testToBlogPosting() {
		BlogPosting blogPosting = toBlogPosting(BLOG_POSTING_MODEL);

		assertThat(blogPosting.getAlternativeHeadline(), is("subtitle"));
		assertThat(blogPosting.getArticleBody(), is("content"));
		assertThat(blogPosting.getCreatorId(), is(0L));
		assertThat(blogPosting.getDateCreated(), isToday());
		assertThat(blogPosting.getDateModified(), isToday());
		assertThat(blogPosting.getFileFormat(), is("text/html"));
		assertThat(blogPosting.getHeadline(), is("title"));
		assertThat(blogPosting.getId(), is(42L));

		List<Review> reviews = blogPosting.getReviews();

		assertThat(reviews, hasSize(2));

		reviews.forEach(
			review -> {
				assertThat(review.getReviewBody(), is("body"));

				Rating rating = review.getRating();

				assertThat(rating.getAuthorId(), is(2L));
				assertThat(rating.getBestRating(), is(5));
				assertThat(rating.getRatingValue(), is(3L));
				assertThat(rating.getWorstRating(), is(0));
			});
	}

}