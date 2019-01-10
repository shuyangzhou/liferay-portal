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

package com.liferay.change.tracking.internal.configuration;

import com.liferay.change.tracking.configuration.CTConfiguration;
import com.liferay.change.tracking.configuration.CTConfigurationRegistrar;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Gergely Mathe
 * @author Daniel Kocsis
 */
@Component(immediate = true, service = CTConfigurationRegistrar.class)
public class CTConfigurationRegistrarImpl implements CTConfigurationRegistrar {

	public void register(CTConfiguration<?, ?> ctConfiguration) {
		if (ctConfiguration == null) {
			return;
		}

		ServiceRegistration<CTConfiguration> serviceRegistration =
			_bundleContext.registerService(
				CTConfiguration.class, ctConfiguration, new Hashtable<>());

		_serviceRegistrations.put(ctConfiguration, serviceRegistration);
	}

	@Override
	public void unregister(CTConfiguration<?, ?> ctConfiguration) {
		if (ctConfiguration == null) {
			return;
		}

		ServiceRegistration<CTConfiguration> serviceRegistration =
			_serviceRegistrations.remove(ctConfiguration);

		serviceRegistration.unregister();
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;
	}

	@Deactivate
	protected void deactivate() {
		for (ServiceRegistration<CTConfiguration> serviceRegistration :
				_serviceRegistrations.values()) {

			serviceRegistration.unregister();
		}

		_serviceRegistrations.clear();

		_bundleContext = null;
	}

	private BundleContext _bundleContext;
	private final Map<CTConfiguration, ServiceRegistration<CTConfiguration>>
		_serviceRegistrations = new HashMap<>();

}