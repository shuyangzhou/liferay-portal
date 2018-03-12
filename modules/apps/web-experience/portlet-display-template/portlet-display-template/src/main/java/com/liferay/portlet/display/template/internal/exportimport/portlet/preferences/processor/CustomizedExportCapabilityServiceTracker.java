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

package com.liferay.portlet.display.template.internal.exportimport.portlet.preferences.processor;

import com.liferay.exportimport.portlet.preferences.processor.Capability;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portlet.display.template.exportimport.portlet.preferences.processor.CustomizedExportCapabilityRegister;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Lance Ji
 */
@Component(immediate = true)
public class CustomizedExportCapabilityServiceTracker {

	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceTracker =
			new ServiceTracker<CustomizedExportCapabilityRegister,
				ServiceRegistration<Capability>>(
					bundleContext, CustomizedExportCapabilityRegister.class,
					new ServiceTrackerCustomizer
						<CustomizedExportCapabilityRegister,
							ServiceRegistration<Capability>>() {

						@Override
						public ServiceRegistration<Capability> addingService(
							ServiceReference<CustomizedExportCapabilityRegister>
								serviceReference) {

							return bundleContext.registerService(
								Capability.class,
								new PortletDisplayTemplateExportCapability(
									bundleContext.getService(serviceReference),
									portal, _portletLocalService),
								_getProperties(serviceReference));
						}

						@Override
						public void modifiedService(
							ServiceReference<CustomizedExportCapabilityRegister>
								serviceReference,
							ServiceRegistration<Capability>
							serviceRegistration) {

							serviceRegistration.setProperties(
								_getProperties(serviceReference));
						}

						@Override
						public void removedService(
							ServiceReference<CustomizedExportCapabilityRegister>
								serviceReference,
							ServiceRegistration<Capability>
							serviceRegistration) {

							serviceRegistration.unregister();

							bundleContext.ungetService(serviceReference);
						}

					});

		_serviceTracker.open();
	}

	@Deactivate
	public void deactivate() {
		_serviceTracker.close();
	}

	@Reference(unbind = "-")
	protected void setPortletLocalService(
		PortletLocalService portletLocalService) {

		_portletLocalService = portletLocalService;
	}

	@Reference
	protected Portal portal;

	private Dictionary<String, Object> _getProperties(
		ServiceReference<?> serviceReference) {

		Dictionary<String, Object> dictionary = new HashMapDictionary<>();

		for (String key : serviceReference.getPropertyKeys()) {
			if (key.equals(Constants.OBJECTCLASS) ||
				key.equals(ComponentConstants.COMPONENT_ID) ||
				key.equals(ComponentConstants.COMPONENT_NAME) ||
				key.equals(Constants.SERVICE_BUNDLEID) ||
				key.equals(Constants.SERVICE_ID) ||
				key.equals(Constants.SERVICE_SCOPE)) {

				continue;
			}

			dictionary.put(key, serviceReference.getProperty(key));
		}

		return dictionary;
	}

	private PortletLocalService _portletLocalService;
	private ServiceTracker<CustomizedExportCapabilityRegister,
		ServiceRegistration<Capability>> _serviceTracker;

}