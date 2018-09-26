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

package com.liferay.apio.architect.sample.internal.dto;

/**
 * Represents a subscription. This is a mock class for sample purposes only.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogSubscriptionModel {

	public BlogSubscriptionModel(
		Long id, BlogPostingModel blogPostingModel, PersonModel personModel) {

		_id = id;
		_blogPostingModel = blogPostingModel;
		_personModel = personModel;
	}

	public BlogPostingModel getBlogPostingModel() {
		return _blogPostingModel;
	}

	public Long getId() {
		return _id;
	}

	public PersonModel getPersonModel() {
		return _personModel;
	}

	private final BlogPostingModel _blogPostingModel;
	private final Long _id;
	private final PersonModel _personModel;

}