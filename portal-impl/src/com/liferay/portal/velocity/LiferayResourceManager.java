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

package com.liferay.portal.velocity;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoaderUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;

import java.io.IOException;
import java.io.Reader;

import java.lang.reflect.Field;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceManager;
import org.apache.velocity.runtime.resource.ResourceManagerImpl;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class LiferayResourceManager extends ResourceManagerImpl {

	@Override
	public String getLoaderNameForResource(String source) {

		// Velocity's default implementation makes its cache useless because
		// getResourceStream is called to test the availability of a template

		if ((globalCache.get(ResourceManager.RESOURCE_CONTENT + source) !=
				null) ||
			(globalCache.get(ResourceManager.RESOURCE_TEMPLATE + source) !=
				null)) {

			return LiferayResourceLoader.class.getName();
		}
		else {
			return super.getLoaderNameForResource(source);
		}
	}

	public Resource getResource(
			final String resourceName, final int resourceType,
			final String encoding)
		throws ResourceNotFoundException, ParseErrorException, Exception {

		if (resourceType != ResourceManager.RESOURCE_TEMPLATE) {
			return super.getResource(resourceName, resourceType, encoding);
		}

		Object object = _portalCache.get(resourceName);

		Resource resource = null;

		if (object == null) {
			try {
				TemplateResource templateResource =
					TemplateResourceLoaderUtil.getTemplateResource(
						TemplateManager.VELOCITY, resourceName);

				resource = new LiferayTemplate(templateResource.getReader());

				resource.setEncoding(encoding);
				resource.setName(resourceName);
				resource.setResourceLoader(new LiferayResourceLoader());
				resource.setRuntimeServices(rsvc);

				resource.process();

				_portalCache.put(resourceName, resource);
			}
			catch (Exception te) {
			}
		}
		else if (object instanceof Resource) {
			resource = (Resource)object;
		}

		return resource;
	}

	@Override
	public synchronized void initialize(RuntimeServices runtimeServices)
		throws Exception {

		ExtendedProperties extendedProperties =
			runtimeServices.getConfiguration();

		Field field = ReflectionUtil.getDeclaredField(
			RuntimeInstance.class, "configuration");

		field.set(
			runtimeServices, new FastExtendedProperties(extendedProperties));

		super.initialize(runtimeServices);
	}

	private PortalCache _portalCache = SingleVMPoolUtil.getCache(
		LiferayResourceManager.class.getName());

	private class LiferayTemplate extends Template {

		public LiferayTemplate(Reader reader) {
			_reader = reader;
		}

		@Override
		public boolean process() throws ParseErrorException, IOException {
			data = null;

			try {
				data = rsvc.parse(_reader, name);

				initDocument();

				return true;
			}
			catch (Exception e) {
				throw new ParseErrorException("Unable to parse tempalte");
			}
			finally {
				if (_reader != null) {
					_reader.close();
				}
			}
		}

		private Reader _reader;

	}

}