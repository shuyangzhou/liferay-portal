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

package com.liferay.apio.architect.sample.internal.type;

import com.liferay.apio.architect.annotation.Id;
import com.liferay.apio.architect.annotation.Vocabulary.Field;
import com.liferay.apio.architect.annotation.Vocabulary.LinkedModel;
import com.liferay.apio.architect.annotation.Vocabulary.Type;
import com.liferay.apio.architect.identifier.Identifier;

/**
 * Instances of this interface represent a person's blog post subscription
 * exposed through the API.
 *
 * @author Alejandro Hernández
 * @review
 */
@Type(description = "A blog-person subscription", value = "BlogSubscription")
public interface BlogSubscription extends Identifier<Long> {

	/**
	 * Returns the blog's ID.
	 *
	 * @return the blog's ID
	 * @review
	 */
	@Field(
		description = "The blog being subscribed", readOnly = true,
		value = "blog"
	)
	@LinkedModel(BlogPosting.class)
	public Long getBlogPostingId();

	/**
	 * Returns the subscription's ID.
	 *
	 * @return the subscription's ID
	 * @review
	 */
	@Id
	public Long getId();

	/**
	 * Returns the person's ID.
	 *
	 * @return the person's ID
	 * @review
	 */
	@Field(
		description = "The person being subscribed to the blog",
		value = "person"
	)
	@LinkedModel(Person.class)
	public Long getPersonId();

}