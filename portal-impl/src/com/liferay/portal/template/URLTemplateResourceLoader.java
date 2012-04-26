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

import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;
import java.net.URLConnection;

/**
 * @author Mika Koivisto
 * @author Tina Tian
 */
public abstract class URLTemplateResourceLoader
	extends TemplateResourceLoader<URL> {

	@Override
	public TemplateResource<URL> findTemplateReource(String templateId)
		throws IOException {

		URL url = getURL(templateId);

		if (url != null) {
			return new TemplateResource<URL>(templateId, url, this);
		}

		return null;
	}

	@Override
	public Reader getReader(URL resource) throws IOException {
		URLConnection urlConnection = resource.openConnection();

		return new UnsyncBufferedReader(
			new InputStreamReader(
				urlConnection.getInputStream(),
				TemplateResourceLoader.ENCODING));
	}

	public abstract URL getURL(String templateId) throws IOException;

}