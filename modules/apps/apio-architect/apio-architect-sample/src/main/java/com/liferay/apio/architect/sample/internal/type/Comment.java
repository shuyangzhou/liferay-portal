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

import java.util.Date;

/**
 * Instances of this interface represent a comment exposed through the API.
 *
 * @author Alejandro Hernández
 * @see    <a href="https://schema.org/Comment">Comment</a>
 * @review
 */
@Type("Comment")
public interface Comment extends Identifier<Long> {

	/**
	 * Returns the comment's author.
	 *
	 * @return the comment's author
	 * @see    <a href="https://schema.org/author">author</a>
	 * @review
	 */
	@Field("author")
	@LinkedModel(Person.class)
	public Long getAuthorId();

	/**
	 * Returns the comment's creation date.
	 *
	 * @return the comment's creation date
	 * @see    <a href="https://schema.org/dateCreated">dateCreated</a>
	 * @review
	 */
	@Field("dateCreated")
	public Date getDateCreated();

	/**
	 * Returns the comment's modification date.
	 *
	 * @return the comment's modification date
	 * @see    <a href="https://schema.org/dateModified">dateModified</a>
	 * @review
	 */
	@Field("dateModified")
	public Date getDateModified();

	/**
	 * Returns the comment's ID.
	 *
	 * @return the comment's ID
	 * @review
	 */
	@Id
	public Long getId();

	/**
	 * Returns the comment's text.
	 *
	 * @return the comment's text
	 * @see    <a href="https://schema.org/text">text</a>
	 * @review
	 */
	@Field("text")
	public String getText();

}