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

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.io.unsync.UnsyncStringReader;

import java.io.Reader;

/**
 * @author Tina Tian
 */
public class StringTemplateResource implements TemplateResource {

	public StringTemplateResource(String templateId, String templateContent) {
		if (templateId == null) {
			throw new IllegalArgumentException("Template ID is null");
		}

		if (templateContent == null) {
			throw new IllegalArgumentException("Template content is null");
		}

		_templateId = templateId;
		_templateContent = templateContent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		StringTemplateResource stringTemplateResource = null;

		try {
			stringTemplateResource = (StringTemplateResource)obj;
		}
		catch (ClassCastException cce) {
			return false;
		}

		if (_templateId.equals(stringTemplateResource._templateId) &&
			_templateContent.equals(stringTemplateResource._templateContent)) {

			return true;
		}
		else {
			return false;
		}
	}

	public String getContent() {
		return _templateContent;
	}

	public long getLastModified() {
		return _lastModified;
	}

	public Reader getReader() {
		if (_templateContent == null) {
			return null;
		}

		return new UnsyncStringReader(_templateContent);
	}

	public String getTemplateId() {
		return _templateId;
	}

	@Override
	public int hashCode() {
		return _templateContent.hashCode() + _templateId.hashCode();
	}

	private long _lastModified = System.currentTimeMillis();
	private String _templateContent;
	private String _templateId;

}