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

package com.liferay.portal.freemarker;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;

import freemarker.cache.TemplateCache;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;

import java.lang.reflect.Method;

import java.util.Locale;

/**
 * @author Tina Tian
 */
public class LiferayTemplateCache extends TemplateCache {

	public LiferayTemplateCache(Configuration configuration)
		throws TemplateException {

		_configuration = configuration;

		try {
			_normalizeNameMethod = ReflectionUtil.getDeclaredMethod(
				TemplateCache.class, "normalizeName", String.class);
		}
		catch (Exception e) {
			throw new TemplateException(e);
		}
	}

	@Override
	public Template getTemplate(
			String templateId, Locale locale, String encoding, boolean parse)
		throws IOException {

		if (templateId == null) {
			throw new IllegalArgumentException(
				"Argument \"name\" cannot be null");
		}

		if (locale == null) {
			throw new IllegalArgumentException(
				"Argument \"locale\" cannot be null");
		}

		if (encoding == null) {
			throw new IllegalArgumentException(
				"Argument \"encoding\" cannot be null");
		}

		try {
			templateId = (String)_normalizeNameMethod.invoke(this, templateId);
		}
		catch (Exception e) {
			return null;
		}

		if (templateId == null) {
			return null;
		}

		Object object = _portalCache.get(templateId);

		Template template = null;

		if (object == null) {
			try {
				TemplateResource templateResource =
					TemplateResourceLoaderUtil.getTemplateResource(
						TemplateManager.FREEMARKER, templateId);

				template = new Template(
					templateResource.getTemplateId(),
					templateResource.getReader(), _configuration,
					TemplateResource.DEFAUT_ENCODING);

				_portalCache.put(templateId, template);
			}
			catch (Exception te) {
			}
		}
		else if (object instanceof Template) {
			template = (Template)object;
		}

		return template;
	}

	private Configuration _configuration;
	private Method _normalizeNameMethod;
	private PortalCache _portalCache = SingleVMPoolUtil.getCache(
		LiferayTemplateCache.class.getName());

}