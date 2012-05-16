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

import com.liferay.portal.deploy.sandbox.SandboxHandler;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class TemplateResourceManager {

	public TemplateResourceManager(String name) {
		_name = name;
	}

	public void clearCache() {
		_portalCache.removeAll();
		_stringResourceLoader.clearCache();
	}

	public void clearCache(String templateId) {
		_portalCache.remove(templateId);
		_stringResourceLoader.clearCache(templateId);
	}

	public void destroy() {
		_portalCache.destroy();
		_stringResourceLoader.destroy();
	}

	public TemplateResource findTemplateResource(String templateId)
		throws IOException {

		TemplateResource templateResource = null;

		Object object = _portalCache.get(templateId);

		if ((object != null) && (object instanceof TemplateResource)) {
			templateResource = (TemplateResource)object;

			if ((templateResource.getLastModified() + _interval) >
				System.currentTimeMillis()) {

				return templateResource;
			}
		}

		for (TemplateResourceLoader templateResourceLoader :
			_templateResourceLoaders.values()) {

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

		_cacheTemplateResource(templateId, templateResource);

		return templateResource;
	}

	public TemplateResource findTemplateResource(
			String templateId, String resourceContent)
		throws IOException {

		if (Validator.isNull(resourceContent)) {
			return findTemplateResource(templateId);
		}

		TemplateResource templateResource =
			_stringResourceLoader.findTemplateSource(
				templateId, resourceContent);

		_cacheTemplateResource(templateId, templateResource);

		return templateResource;
	}

	public InputStream getInputStream(TemplateResource templateResource)
		throws IOException {

		TemplateResourceLoader resourceLoader = null;

		String resourceLoaderName = templateResource.getResourceLoaderName();

		if (StringTemplateResourceLoader.class.getName().equals(
			resourceLoaderName)) {

			resourceLoader = _stringResourceLoader;
		}
		else {
			resourceLoader = _templateResourceLoaders.get(resourceLoaderName);
		}

		if (resourceLoader == null) {
			_log.error(
				"Unable to find resource loader for template " +
					templateResource.getName());

			return null;
		}

		return resourceLoader.getInputStream(templateResource.getData());
	}

	public String getName() {
		return _name;
	}

	public boolean hasResource(String templateId) {
		try {
			TemplateResource templateResource = findTemplateResource(
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
		for (String resourceLoaderClassName : resourceLoaderClassNames) {
			try {
				TemplateResourceLoader resourceLoader =
					(TemplateResourceLoader)InstanceFactory.newInstance(
						resourceLoaderClassName);

				_templateResourceLoaders.put(
					resourceLoaderClassName, resourceLoader);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	private void _cacheTemplateResource(
		String templateId, TemplateResource templateResource) {

		if (templateResource == null) {
			return;
		}

		if (TemplateManager.VELOCITY.equals(_name) &&
			templateId.contains(SandboxHandler.SANDBOX_MARKER)) {

			return;
		}

		_portalCache.put(templateId, templateResource);
	}

	private static Log _log = LogFactoryUtil.getLog(
		TemplateResourceManager.class);

	private int _interval;
	private String _name;
	private PortalCache _portalCache = MultiVMPoolUtil.getCache(
		TemplateResourceManager.class.getName());
	private StringTemplateResourceLoader _stringResourceLoader =
		new StringTemplateResourceLoader();
	private Map<String, TemplateResourceLoader> _templateResourceLoaders =
		new HashMap<String, TemplateResourceLoader>();

}