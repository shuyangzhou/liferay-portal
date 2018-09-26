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
import com.liferay.apio.architect.sample.internal.dto.BlogSubscriptionModel;
import com.liferay.apio.architect.sample.internal.dto.PersonModel;
import com.liferay.apio.architect.sample.internal.type.BlogSubscription;

/**
 * Provides methods for creating {@link BlogSubscription} instances out of other
 * DTOs.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogSubscriptionConverter {

	/**
	 * Converts a {@link BlogSubscriptionModel} to a {@link BlogSubscription}.
	 *
	 * @param  blogSubscriptionModel the internal blog subscription model
	 * @return a {@link BlogSubscription}
	 * @review
	 */
	public static BlogSubscription toBlogSubscription(
		BlogSubscriptionModel blogSubscriptionModel) {

		return new BlogSubscription() {

			@Override
			public Long getBlogPostingId() {
				BlogPostingModel blogPostingModel =
					blogSubscriptionModel.getBlogPostingModel();

				return blogPostingModel.getId();
			}

			@Override
			public Long getId() {
				return blogSubscriptionModel.getId();
			}

			@Override
			public Long getPersonId() {
				PersonModel personModel =
					blogSubscriptionModel.getPersonModel();

				return personModel.getId();
			}

		};
	}

}