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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portlet.display.template.PortletDisplayTemplateConstants;
import com.liferay.portlet.display.template.exportimport.portlet.preferences.processor.PortletDisplayTemplateCapabilityRegister;

import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Lance Ji
 */
@Component(immediate = true)
public class PortletDisplayTemplateCapabilityRegisterTracker {

	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceTracker =
			new ServiceTracker<PortletDisplayTemplateCapabilityRegister,
				ServiceRegistration<Capability>>(
					bundleContext,
					PortletDisplayTemplateCapabilityRegister.class,
					new ServiceTrackerCustomizer
						<PortletDisplayTemplateCapabilityRegister,
							ServiceRegistration<Capability>>() {

						@Override
						public ServiceRegistration<Capability> addingService(
							ServiceReference
								<PortletDisplayTemplateCapabilityRegister>
							serviceReference) {

							Dictionary<String, Object> dictionary =
								_getProperties(serviceReference);

							if (_isImportService(dictionary)) {
								return bundleContext.registerService(
									Capability.class,
									new PortletDisplayTemplateImportCapability(
										bundleContext.getService(
											serviceReference)),
									_getProperties(serviceReference));
							}

							return bundleContext.registerService(
								Capability.class,
								new PortletDisplayTemplateExportCapability(
									bundleContext.getService(serviceReference)),
								_getProperties(serviceReference));
						}

						@Override
						public void modifiedService(
							ServiceReference
								<PortletDisplayTemplateCapabilityRegister>
									serviceReference,
							ServiceRegistration<Capability>
							serviceRegistration) {

							serviceRegistration.setProperties(
								_getProperties(serviceReference));
						}

						@Override
						public void removedService(
							ServiceReference
								<PortletDisplayTemplateCapabilityRegister>
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

	private boolean _isImportService(Dictionary<String, Object> dictionary) {
		String value = GetterUtil.getString(
			dictionary.get(PortletDisplayTemplateConstants.DATA_HANDLE_TYPE));

		return value.equals(PortletDisplayTemplateConstants.DATA_IMPORT_HANDLE);
	}

	private ServiceTracker<PortletDisplayTemplateCapabilityRegister,
		ServiceRegistration<Capability>> _serviceTracker;

}