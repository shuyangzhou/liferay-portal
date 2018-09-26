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

package com.liferay.apio.architect.internal.operation;

import com.liferay.apio.architect.form.Form;
import com.liferay.apio.architect.operation.HTTPMethod;
import com.liferay.apio.architect.operation.Operation;

import java.util.Optional;

/**
 * Represents a resource retrieve operation.
 *
 * @author Alejandro Hernández
 */
public class RetrieveOperation implements Operation {

	public RetrieveOperation(String resourceName, boolean collection) {
		this(resourceName, collection, null);
	}

	public RetrieveOperation(
		String resourceName, boolean collection, String uri) {

		this(resourceName, collection, uri, null);
	}

	public RetrieveOperation(
		String resourceName, boolean collection, String uri,
		String customRoute) {

		_resourceName = resourceName;
		_collection = collection;
		_uri = uri;
		_customRoute = customRoute;
	}

	@Override
	public String getCustomRoute() {
		return _customRoute;
	}

	@Override
	public Optional<Form> getFormOptional() {
		return Optional.empty();
	}

	@Override
	public HTTPMethod getHttpMethod() {
		return HTTPMethod.GET;
	}

	@Override
	public String getName() {
		return _resourceName + "/" + (isCustom() ? _customRoute : "retrieve");
	}

	@Override
	public Optional<String> getURIOptional() {
		return Optional.ofNullable(_uri);
	}

	@Override
	public boolean isCollection() {
		return _collection;
	}

	public boolean isCustom() {
		if (_customRoute != null) {
			return true;
		}

		return false;
	}

	private final boolean _collection;
	private final String _customRoute;
	private final String _resourceName;
	private final String _uri;

}