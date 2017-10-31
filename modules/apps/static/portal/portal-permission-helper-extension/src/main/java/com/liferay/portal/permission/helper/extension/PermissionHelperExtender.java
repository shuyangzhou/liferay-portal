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

package com.liferay.portal.permission.helper.extension;

import com.liferay.portal.kernel.security.permission.PortletPermissionHelper;
import com.liferay.portal.kernel.security.permission.StagingPortletPermissionHelper;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTracker;

/**
 * @author Preston Crary
 */
public class PermissionHelperExtender implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		_tracker = new BundleTracker<List<ServiceRegistration<?>>>(
			bundleContext, Bundle.ACTIVE, null) {

			@Override
			public List<ServiceRegistration<?>> addingBundle(
				Bundle bundle, BundleEvent bundleEvent) {

				Dictionary<String, String> headers = bundle.getHeaders();

				String liferayServicePortletPermissions = headers.get(
					"Liferay-Service-Portlet-Permissions");

				if (liferayServicePortletPermissions != null) {
					return _registerServicePortletPermissionHelpers(
						bundle.getBundleContext(),
						liferayServicePortletPermissions);
				}

				String liferayWebPortletPermissions = headers.get(
					"Liferay-Web-Portlet-Permissions");

				if (liferayWebPortletPermissions != null) {
					return _registerWebPortletPermissionHelpers(
						bundle.getBundleContext(),
						liferayWebPortletPermissions);
				}

				return null;
			}

			@Override
			public void removedBundle(
				Bundle bundle, BundleEvent event,
				List<ServiceRegistration<?>> serviceRegistrations) {

				if (serviceRegistrations != null) {
					for (ServiceRegistration<?> serviceRegistration :
							serviceRegistrations) {

						serviceRegistration.unregister();
					}
				}
			}

		};

		_tracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_tracker.close();
	}

	private List<ServiceRegistration<?>>
		_registerServicePortletPermissionHelpers(
			BundleContext bundleContext, String header) {

		String[] headerParts = StringUtil.split(header);

		List<ServiceRegistration<?>> serviceRegistrations = new ArrayList<>(
			headerParts.length);

		for (String headerPart : StringUtil.split(header)) {
			Dictionary<String, Object> dictionary = new HashMapDictionary<>();

			dictionary.put("resource.name", headerPart);

			ServiceRegistration<PortletPermissionHelper> serviceRegistration =
				bundleContext.registerService(
					PortletPermissionHelper.class,
					new PortletPermissionHelper(headerPart), dictionary);

			serviceRegistrations.add(serviceRegistration);
		}

		return serviceRegistrations;
	}

	private List<ServiceRegistration<?>> _registerWebPortletPermissionHelpers(
		BundleContext bundleContext, String header) {

		String[] headerParts = StringUtil.split(header);

		List<ServiceRegistration<?>> serviceRegistrations = new ArrayList<>(
			headerParts.length);

		for (String headerPart : StringUtil.split(header)) {
			String[] parts = StringUtil.split(headerPart, CharPool.DASH);

			if (parts.length != 2) {
				throw new IllegalArgumentException(header);
			}

			String resourceName = parts[0];
			String portletId = parts[1];

			Dictionary<String, Object> dictionary = new HashMapDictionary<>();

			dictionary.put("resource.name", resourceName);
			dictionary.put("service.ranking:Integer", 100);

			ServiceRegistration<PortletPermissionHelper> serviceRegistration =
				bundleContext.registerService(
					PortletPermissionHelper.class,
					new StagingPortletPermissionHelper(resourceName, portletId),
					dictionary);

			serviceRegistrations.add(serviceRegistration);
		}

		return serviceRegistrations;
	}

	private volatile BundleTracker<List<ServiceRegistration<?>>> _tracker;

}