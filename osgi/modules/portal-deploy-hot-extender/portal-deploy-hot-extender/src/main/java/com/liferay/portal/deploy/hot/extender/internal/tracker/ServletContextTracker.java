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

package com.liferay.portal.deploy.hot.extender.internal.tracker;

import com.liferay.portal.deploy.hot.extender.internal.handler.ApplicationHandler;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Miguel Pastor
 */
public class ServletContextTracker
	extends ServiceTracker<ServletContext, ServletContext> {

	public ServletContextTracker(BundleContext bundleContext) {
		super(bundleContext, ServletContext.class, null);
	}

	@Override
	public ServletContext addingService(
		ServiceReference<ServletContext> serviceReference) {

		Bundle bundle = serviceReference.getBundle();

		if (bundle.getBundleId() == 0) {
			return null;
		}

		ServletContext servletContext = super.addingService(serviceReference);

		_applicationHandler.registerApplication(bundle, servletContext);

		return servletContext;
	}

	@Override
	public void removedService(
		ServiceReference<ServletContext> serviceReference,
		ServletContext servletContext) {

		Bundle bundle = serviceReference.getBundle();

		if (bundle.getBundleId() == 0) {
			return;
		}

		_applicationHandler.unregisterApplication(bundle, servletContext);

		super.removedService(serviceReference, servletContext);
	}

	private ApplicationHandler _applicationHandler =
		ApplicationHandler.getInstance();

}