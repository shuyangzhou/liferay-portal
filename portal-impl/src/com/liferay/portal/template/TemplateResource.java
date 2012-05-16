/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.template;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public class TemplateResource implements Serializable {

	public TemplateResource(
		String name, Serializable data, String resourceLoaderName) {

		_data = data;
		_name = name;
		_resourceLoaderName = resourceLoaderName;
		_lastModified = System.currentTimeMillis();
	}

	public Serializable getData() {
		return _data;
	}

	public long getLastModified() {
		return _lastModified;
	}

	public String getName() {
		return _name;
	}

	public String getResourceLoaderName() {
		return _resourceLoaderName;
	}

	private Serializable _data;
	private long _lastModified;
	private String _name;
	private String _resourceLoaderName;

}