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

import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tina Tian
 */
public class StringTemplateResourceLoader
	extends TemplateResourceLoader {

	public void clearCache() {
		_portalCache.removeAll();
	}

	public void clearCache(String templateId) {
		_portalCache.remove(templateId);
	}

	public void destroy() {
		_portalCache.destroy();
	}

	@Override
	public TemplateResource findTemplateReource(String templateId)
		throws IOException {

		String templateContent = (String)_portalCache.get(templateId);

		if (Validator.isNull(templateContent)) {
			return null;
		}

		return new TemplateResource(
			templateId, templateContent, this.getClass().getName());
	}

	public TemplateResource findTemplateSource(
			String templateId, String templateContent)
		throws IOException {

		if (Validator.isNull(templateContent)) {
			return findTemplateReource(templateId);
		}
		else {
			if (PropsValues.LAYOUT_TEMPLATE_CACHE_ENABLED &&
				!(_portalCache.get(templateId) != null)) {

				_portalCache.put(templateId, templateContent);
			}

			return new TemplateResource(
				templateId, templateContent, this.getClass().getName());
		}
	}

	@Override
	public InputStream getInputStream(Object resource) throws IOException {
		if ((resource == null) || !(resource instanceof String)) {
			return null;
		}

		String resourceString = (String)resource;

		return new UnsyncByteArrayInputStream(
			resourceString.getBytes(ENCODING));
	}

	private static PortalCache _portalCache = MultiVMPoolUtil.getCache(
		StringTemplateResourceLoader.class.getName());

}