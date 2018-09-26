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

package com.liferay.apio.architect.sample.internal.dao;

import com.liferay.apio.architect.sample.internal.dto.BlogPostingModel;
import com.liferay.apio.architect.sample.internal.dto.BlogSubscriptionModel;
import com.liferay.apio.architect.sample.internal.dto.PersonModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.osgi.service.component.annotations.Component;

/**
 * Contains methods for creating blog subscriptions in an in-memory database.
 *
 * @author Alejandro Hernández
 * @review
 */
@Component(immediate = true, service = BlogSubscriptionModelService.class)
public class BlogSubscriptionModelService {

	/**
	 * Subscribes a person to a blog posting.
	 *
	 * @param  blogPostingModel the blog posting's
	 * @param  personModel the person being subscribed
	 * @return the new blog posting subscription
	 */
	public BlogSubscriptionModel create(
		BlogPostingModel blogPostingModel, PersonModel personModel) {

		BlogSubscriptionModel blogSubscriptionModel = new BlogSubscriptionModel(
			_count.getAndIncrement(), blogPostingModel, personModel);

		_blogSubscriptionModels.put(
			blogSubscriptionModel.getId(), blogSubscriptionModel);

		return blogSubscriptionModel;
	}

	private static final Map<Long, BlogSubscriptionModel>
		_blogSubscriptionModels = new ConcurrentHashMap<>();
	private static final AtomicLong _count = new AtomicLong(0);

}