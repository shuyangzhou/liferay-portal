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

import static com.liferay.apio.architect.sample.internal.converter.CommentConverter.toComment;
import static com.liferay.apio.architect.sample.internal.converter.DemoDataUtil.BLOG_POSTING_COMMENT_MODEL;

import static org.exparity.hamcrest.date.DateMatchers.isToday;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.liferay.apio.architect.sample.internal.type.Comment;

import org.junit.Test;

/**
 * @author Alejandro Hernández
 */
public class CommentConverterTest {

	@Test
	public void testToComment() {
		Comment comment = toComment(BLOG_POSTING_COMMENT_MODEL);

		assertThat(comment.getAuthorId(), is(1L));
		assertThat(comment.getDateCreated(), isToday());
		assertThat(comment.getDateModified(), isToday());
		assertThat(comment.getId(), is(2L));
		assertThat(comment.getText(), is("content"));
	}

}