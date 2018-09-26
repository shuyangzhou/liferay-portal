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

import static com.liferay.apio.architect.sample.internal.converter.BlogSubscriptionConverter.toBlogSubscription;
import static com.liferay.apio.architect.sample.internal.converter.DemoDataUtil.BLOG_POSTING_MODEL;
import static com.liferay.apio.architect.sample.internal.converter.DemoDataUtil.PERSON_MODEL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.liferay.apio.architect.sample.internal.dto.BlogSubscriptionModel;
import com.liferay.apio.architect.sample.internal.type.BlogSubscription;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class BlogSubscriptionConverterTest {

	@Test
	public void testToBlogSubscription() {
		BlogSubscriptionModel blogSubscriptionModel = new BlogSubscriptionModel(
			21L, BLOG_POSTING_MODEL, PERSON_MODEL);

		BlogSubscription blogSubscription = toBlogSubscription(
			blogSubscriptionModel);

		assertThat(blogSubscription.getBlogPostingId(), is(42L));
		assertThat(blogSubscription.getId(), is(21L));
		assertThat(blogSubscription.getPersonId(), is(84L));
	}

}