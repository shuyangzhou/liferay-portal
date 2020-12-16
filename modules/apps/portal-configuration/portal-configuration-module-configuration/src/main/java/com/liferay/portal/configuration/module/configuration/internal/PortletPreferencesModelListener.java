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

package com.liferay.portal.configuration.module.configuration.internal;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.settings.definition.ConfigurationPidMapping;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Drew Brokke
 */
@Component(immediate = true, service = ModelListener.class)
public class PortletPreferencesModelListener
	extends BaseModelListener<PortletPreferences> {

	@Override
	public void onAfterCreate(PortletPreferences portletPreferences)
		throws ModelListenerException {

		_clearCache(portletPreferences);
	}

	@Override
	public void onAfterUpdate(PortletPreferences portletPreferences)
		throws ModelListenerException {

		_clearCache(portletPreferences);
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_stringConfigurationPidMappingServiceTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, ConfigurationPidMapping.class, null,
				(serviceReference, emitter) -> {
					ConfigurationPidMapping configurationPidMapping =
						bundleContext.getService(serviceReference);

					emitter.emit(configurationPidMapping.getConfigurationPid());
				});
	}

	private void _clearCache(PortletPreferences portletPreferences) {
		ConfigurationPidMapping configurationPidMapping =
			_stringConfigurationPidMappingServiceTrackerMap.getService(
				portletPreferences.getPortletId());

		if (configurationPidMapping == null) {
			return;
		}

		ConfigurationOverrideInstance.clearConfigurationOverrideInstance(
			configurationPidMapping.getConfigurationBeanClass());
	}

	private ServiceTrackerMap<String, ConfigurationPidMapping>
		_stringConfigurationPidMappingServiceTrackerMap;

}