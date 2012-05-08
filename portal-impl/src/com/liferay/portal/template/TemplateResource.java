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

import java.io.IOException;
import java.io.Reader;

/**
 * @author Tina Tian
 */
public class TemplateResource<T> {

	public TemplateResource(
		String name, T data, TemplateResourceLoader<T> templateResourceLoader) {

		_data = data;
		_name = name;
		_templateResourceLoader = templateResourceLoader;
	}

	public T getData() {
		return _data;
	}

	public String getName() {
		return _name;
	}

	public Reader getReader() throws IOException {
		return _templateResourceLoader.getReader(_data);
	}

	private T _data;
	private String _name;
	private TemplateResourceLoader<T> _templateResourceLoader;

}