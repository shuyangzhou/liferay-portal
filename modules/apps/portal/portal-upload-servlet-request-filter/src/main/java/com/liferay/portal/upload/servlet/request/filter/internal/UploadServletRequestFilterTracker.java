/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.upload.servlet.request.filter.internal;

import com.liferay.portal.kernel.util.HashMapDictionary;

import java.util.Dictionary;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Matthew Tambara
 */
@Component(immediate = true, service = {})
public class UploadServletRequestFilterTracker {

	@Activate
	protected void activate(BundleContext bundleContext) {
		UploadServletRequestFilter uploadServletRequestFilter =
			new UploadServletRequestFilter();

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("dispatcher", new String[] {"REQUEST"});
		properties.put("servlet-context-name", "");
		properties.put("servlet-filter-name", "Upload Servlet Request Filter");
		properties.put("url-pattern", "/*");

		_serviceRegistration = bundleContext.registerService(
			Filter.class, uploadServletRequestFilter, properties);
	}

	@Deactivate
	protected void deactivate() {
		try {
			_serviceRegistration.unregister();
		}
		catch (IllegalStateException illegalStateException) {
		}
	}

	private ServiceRegistration<Filter> _serviceRegistration;

}