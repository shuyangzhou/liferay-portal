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

import com.liferay.apio.architect.sample.internal.dto.BlogPostingCommentModel;
import com.liferay.apio.architect.sample.internal.dto.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.dto.PersonModel;
import com.liferay.apio.architect.sample.internal.dto.PostalAddressModel;
import com.liferay.apio.architect.sample.internal.dto.RatingModel;
import com.liferay.apio.architect.sample.internal.dto.ReviewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Provides demo data for testing purposes.
 *
 * @author Alejandro Hernández
 * @review
 */
public class DemoDataUtil {

	/**
	 * Demo {@link BlogPostingCommentModel}.
	 *
	 * @review
	 */
	public static final BlogPostingCommentModel BLOG_POSTING_COMMENT_MODEL;

	/**
	 * Demo {@link BlogPostingModel}.
	 *
	 * @review
	 */
	public static final BlogPostingModel BLOG_POSTING_MODEL;

	/**
	 * Demo {@link PersonModel}.
	 *
	 * @review
	 */
	public static final PersonModel PERSON_MODEL;

	/**
	 * Demo {@link PostalAddressModel}.
	 *
	 * @review
	 */
	public static final PostalAddressModel POSTAL_ADDRESS_MODEL;

	/**
	 * Demo {@link RatingModel}.
	 *
	 * @review
	 */
	public static final RatingModel RATING_MODEL;

	/**
	 * Demo {@link ReviewModel}.
	 *
	 * @review
	 */
	public static final ReviewModel REVIEW_MODEL;

	/**
	 * Demo {@link ReviewModel} list.
	 *
	 * @review
	 */
	public static final List<ReviewModel> REVIEW_MODELS;

	static {
		BLOG_POSTING_COMMENT_MODEL = new BlogPostingCommentModel(
			1L, 2L, 3L, "content", new Date(), new Date());

		RATING_MODEL = new RatingModel(2L, 3L);

		REVIEW_MODEL = new ReviewModel("body", RATING_MODEL);

		REVIEW_MODELS = Arrays.asList(REVIEW_MODEL, REVIEW_MODEL);

		BLOG_POSTING_MODEL = new BlogPostingModel(
			42L, "content", new Date(), 0L, new Date(), REVIEW_MODELS,
			"subtitle", "title");

		POSTAL_ADDRESS_MODEL = new PostalAddressModel(
			"country", "state", "city", "zip", "street");

		PERSON_MODEL = new PersonModel(
			"23", new Date(), "email@liferay.com", "Given",
			Arrays.asList("Job 1", "Job 2"), "Family", POSTAL_ADDRESS_MODEL,
			84L);
	}

}