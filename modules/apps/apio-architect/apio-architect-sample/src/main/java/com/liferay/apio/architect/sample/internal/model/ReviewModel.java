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

package com.liferay.apio.architect.sample.internal.model;

/**
 * Represents a review. This is a mock class for sample purposes only.
 *
 * @author Alejandro Hernández
 */
public class ReviewModel {

	public ReviewModel(String body, RatingModel ratingModel) {
		_body = body;
		_ratingModel = ratingModel;
	}

	/**
	 * Returns the review's body.
	 *
	 * @return the review's body
	 */
	public String getBody() {
		return _body;
	}

	/**
	 * Returns the review's rating.
	 *
	 * @return the review's rating
	 */
	public RatingModel getRatingModel() {
		return _ratingModel;
	}

	private final String _body;
	private final RatingModel _ratingModel;

}