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

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;
import java.io.InputStream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tina Tian
 */
public class StringTemplateResourceLoader
	extends TemplateResourceLoader<String> {

	@Override
	public TemplateResource<String> findTemplateReource(String templateId)
		throws IOException {

		String templateContent = _stringTemplateResources.get(templateId);

		if (Validator.isNull(templateContent)) {
			return null;
		}

		return new TemplateResource<String>(templateId, templateContent, this);
	}

	public TemplateResource<String> findTemplateSource(
			String templateId, String templateContent)
		throws IOException {

		if (Validator.isNull(templateContent)) {
			return findTemplateReource(templateId);
		}
		else {
			if (PropsValues.LAYOUT_TEMPLATE_CACHE_ENABLED &&
				!_stringTemplateResources.containsKey(templateId)) {

				_stringTemplateResources.put(templateId, templateContent);
			}

			return new TemplateResource<String>(
				templateId, templateContent, this);
		}
	}

	@Override
	public InputStream getInputStream(String resource) throws IOException {
		return new UnsyncByteArrayInputStream(resource.getBytes(ENCODING));
	}

	private Map<String, String> _stringTemplateResources =
		new ConcurrentHashMap<String, String>();

}