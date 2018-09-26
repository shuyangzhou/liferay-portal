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

import java.util.Date;

/**
 * Represents a comment on a {@link BlogPostingModel}. This is a mock class for
 * sample purposes only.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogPostingCommentModel {

	public BlogPostingCommentModel(
		Long authorId, Long id, Long blogPostingModelId, String content,
		Date createDate, Date modifiedDate) {

		_authorId = authorId;
		_id = id;
		_blogPostingModelId = blogPostingModelId;
		_content = content;
		_createDate = createDate;
		_modifiedDate = modifiedDate;
	}

	/**
	 * Returns the ID of the current blog posting comment's author.
	 *
	 * @return the ID of the current blog posting comment's author
	 */
	public Long getAuthorId() {
		return _authorId;
	}

	/**
	 * Returns the ID of the current comment's blog posting.
	 *
	 * @return the blog posting ID
	 */
	public Long getBlogPostingModelId() {
		return _blogPostingModelId;
	}

	/**
	 * Returns the current blog posting comment's content.
	 *
	 * @return the current blog posting comment's content
	 */
	public String getContent() {
		return _content;
	}

	/**
	 * Returns the current blog posting comment's creation date.
	 *
	 * @return the current blog posting comment's creation date
	 */
	public Date getCreateDate() {
		return new Date(_createDate.getTime());
	}

	/**
	 * Returns the current blog posting comment's ID.
	 *
	 * @return the current blog posting comment's ID
	 */
	public Long getId() {
		return _id;
	}

	/**
	 * Returns the current blog posting comment's modification date.
	 *
	 * @return the current blog posting comment's modification date
	 */
	public Date getModifiedDate() {
		return new Date(_modifiedDate.getTime());
	}

	private final Long _authorId;
	private final Long _blogPostingModelId;
	private final String _content;
	private final Date _createDate;
	private final Long _id;
	private final Date _modifiedDate;

}