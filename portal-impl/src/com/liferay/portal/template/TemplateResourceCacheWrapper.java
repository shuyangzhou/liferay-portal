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
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tina Tian
 */
public class TemplateResourceCacheWrapper implements TemplateResource {

	public TemplateResourceCacheWrapper(
		TemplateResource templateResource, long lastModified) {

		_templateResource = templateResource;
		_lastModified = lastModified;
	}

	public long getLastModified() {
		return _lastModified;
	}

	public Reader getReader() throws IOException {
		String templateContent = _templateContent.get();

		if (templateContent == null) {
			Reader reader = _templateResource.getReader();

			if (reader == null) {
				return null;
			}

			UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(reader);

			String line = unsyncBufferedReader.readLine();

			StringBundler sb = new StringBundler();

			while (line != null) {
				sb.append(line);

				line = unsyncBufferedReader.readLine();
			}

			templateContent = sb.toString();

			_templateContent.compareAndSet(templateContent, null);
		}

		return new StringReader(templateContent);
	}

	public String getTemplateId() {
		return _templateResource.getTemplateId();
	}

	private long _lastModified;
	private AtomicReference<String> _templateContent =
		new AtomicReference<String>(null);
	private TemplateResource _templateResource;

}