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

import com.liferay.portal.kernel.cache.MultiVMKeyPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Tina Tian
 */
public class TemplateResourceManager {

	public void clearCache() {
		_portalCache.removeAll();
	}

	public void clearCache(String templateId) {
		_portalCache.remove(templateId);
	}

	public void destroy() {
		_portalCache.destroy();
	}

	public TemplateResource<?> findTemplateResource(String templateId)
		throws IOException {

		Object object = _portalCache.get(templateId);

		if ((object != null) && (object instanceof TemplateResource<?>)) {
			return (TemplateResource<?>)object;
		}

		TemplateResource<?> templateResource = null;

		for (TemplateResourceLoader<?> templateResourceLoader :
			_templateResourceLoaders) {

			templateResource = templateResourceLoader.findTemplateReource(
				templateId);

			if (templateResource != null) {
				break;
			}
		}

		if (templateResource == null) {
			templateResource = _stringResourceLoader.findTemplateReource(
				templateId);
		}

		if (templateResource != null) {
			_portalCache.put(templateId, templateResource, _interval);
		}

		return templateResource;
	}

	public TemplateResource<?> findTemplateResource(
			String templateId, String resourceContent)
		throws IOException {

		Object object = _portalCache.get(templateId);

		if ((object != null) && (object instanceof TemplateResource<?>)) {
			return (TemplateResource<?>)object;
		}

		TemplateResource<?> templateResource = null;

		if (Validator.isNull(resourceContent)) {
			templateResource = findTemplateResource(templateId);
		}
		else {
			templateResource = _stringResourceLoader.findTemplateSource(
				templateId, resourceContent);

			if (templateResource != null) {
				_portalCache.put(templateId, templateResource, _interval);
			}
		}

		return templateResource;
	}

	public boolean hasResource(String templateId) {
		try {
			TemplateResource<?> templateResource = findTemplateResource(
				templateId);

			if (templateResource != null) {
				return true;
			}
		}
		catch (Exception ex) {
			_log.warn(ex, ex);
		}

		return false;
	}

	public void setInterval(int interval) {
		_interval = interval;
	}

	public void setResourceLoaders(String[] resourceLoaderClassNames) {
		Set<TemplateResourceLoader<?>> resourceLoaders =
			new HashSet<TemplateResourceLoader<?>>(
				resourceLoaderClassNames.length);

		for (String resourceLoaderClassName : resourceLoaderClassNames) {
			try {
				TemplateResourceLoader<?> resourceLoader =
					(TemplateResourceLoader<?>)InstanceFactory.newInstance(
						resourceLoaderClassName);

				resourceLoaders.add(resourceLoader);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		_templateResourceLoaders = resourceLoaders.toArray(
			new TemplateResourceLoader<?>[resourceLoaders.size()]);
	}

	private static Log _log = LogFactoryUtil.getLog(
		TemplateResourceManager.class);

	private int _interval;
	private PortalCache _portalCache = MultiVMKeyPoolUtil.getCache(
		TemplateResourceManager.class.getName());
	private StringTemplateResourceLoader _stringResourceLoader =
		new StringTemplateResourceLoader();
	private TemplateResourceLoader<?>[] _templateResourceLoaders;

}