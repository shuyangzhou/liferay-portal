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
import com.liferay.apio.architect.sample.internal.type.Comment;

import java.util.Date;

/**
 * Provides methods for creating {@link Comment} instances out of other DTOs.
 *
 * @author Alejandro Hernández
 * @review
 */
public class CommentConverter {

	/**
	 * Converts a {@link BlogPostingCommentModel} to a {@link Comment}.
	 *
	 * @param  blogPostingCommentModel the internal blog posting comment model
	 * @return a {@link Comment}
	 * @review
	 */
	public static Comment toComment(
		BlogPostingCommentModel blogPostingCommentModel) {

		return new Comment() {

			@Override
			public Long getAuthorId() {
				return blogPostingCommentModel.getAuthorId();
			}

			@Override
			public Date getDateCreated() {
				return blogPostingCommentModel.getCreateDate();
			}

			@Override
			public Date getDateModified() {
				return blogPostingCommentModel.getModifiedDate();
			}

			@Override
			public Long getId() {
				return blogPostingCommentModel.getId();
			}

			@Override
			public String getText() {
				return blogPostingCommentModel.getContent();
			}

		};
	}

}