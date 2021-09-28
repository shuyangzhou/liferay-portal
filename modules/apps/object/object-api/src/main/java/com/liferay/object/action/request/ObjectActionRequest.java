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

package com.liferay.object.action.request;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Marco Leo
 */
public class ObjectActionRequest {

	public ObjectActionRequest(
		Map<String, Serializable> parameters, long userId) {

		_parameters = parameters;
		_userId = userId;
	}

	public Serializable getParameterValue(String parameterName) {
		return _parameters.get(parameterName);
	}

	public long getUserId() {
		return _userId;
	}

	private final Map<String, Serializable> _parameters;
	private final long _userId;

}