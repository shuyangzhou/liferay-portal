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
import java.util.List;

/**
 * Represents a blog posting. This is a mock class for sample purposes only.
 *
 * @author Alejandro Hernández
 * @review
 */
public class BlogPostingModel {

	public BlogPostingModel(
		Long id, String content, Date createDate, Long creatorId,
		Date modifiedDate, List<ReviewModel> reviewModels, String subtitle,
		String title) {

		_id = id;
		_content = content;
		_createDate = createDate;
		_creatorId = creatorId;
		_modifiedDate = modifiedDate;
		_reviewModels = reviewModels;
		_subtitle = subtitle;
		_title = title;
	}

	/**
	 * Returns the current blog posting's content.
	 *
	 * @return the current blog posting's content
	 */
	public String getContent() {
		return _content;
	}

	/**
	 * Returns the date that the current blog posting was created.
	 *
	 * @return the current blog posting's creation date
	 */
	public Date getCreateDate() {
		return new Date(_createDate.getTime());
	}

	/**
	 * Returns the ID of the current blog posting's creator.
	 *
	 * @return the ID of the current blog posting's creator
	 */
	public Long getCreatorId() {
		return _creatorId;
	}

	/**
	 * Returns the current blog posting's ID.
	 *
	 * @return the current blog posting's ID
	 */
	public Long getId() {
		return _id;
	}

	/**
	 * Returns the date that the current blog posting was modified.
	 *
	 * @return the current blog posting's modification date
	 */
	public Date getModifiedDate() {
		return new Date(_modifiedDate.getTime());
	}

	/**
	 * Returns the current blog posting's reviews.
	 *
	 * @return the current blog posting's reviews
	 */
	public List<ReviewModel> getReviewModels() {
		return _reviewModels;
	}

	/**
	 * Returns the current blog posting's subtitle.
	 *
	 * @return the current blog posting's subtitle
	 */
	public String getSubtitle() {
		return _subtitle;
	}

	/**
	 * Returns the current blog posting's title.
	 *
	 * @return the current blog posting's title
	 */
	public String getTitle() {
		return _title;
	}

	private final String _content;
	private final Date _createDate;
	private final Long _creatorId;
	private final Long _id;
	private final Date _modifiedDate;
	private final List<ReviewModel> _reviewModels;
	private final String _subtitle;
	private final String _title;

}