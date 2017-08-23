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

package com.liferay.portal.osgi.debug.declarative.service.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.osgi.debug.declarative.service.DeclarativeServiceUtil;
import com.liferay.portal.osgi.debug.declarative.service.configuration.UnsatisfiedDeclarativeServicesSnapshotConfiguration;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.osgi.debug.declarative.service.configuration.UnsatisfiedDeclarativeServicesSnapshotConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true
)
public class UnsatisfiedDeclarativeServicesSnapshot {

	@Activate
	@Modified
	protected void activate(ComponentContext componentContext) {
		UnsatisfiedDeclarativeServicesSnapshotConfiguration
			unsatisfiedDeclarativeServicesSnapshotConfiguration =
				ConfigurableUtil.createConfigurable(
					UnsatisfiedDeclarativeServicesSnapshotConfiguration.class,
					componentContext.getProperties());

		if (unsatisfiedDeclarativeServicesSnapshotConfiguration.enabled() &&
			_log.isInfoEnabled()) {

			BundleContext bundleContext = componentContext.getBundleContext();

			_log.info(
				DeclarativeServiceUtil.listUnsatisfiedDeclarativeServices(
					_configurationAdmin, _serviceComponentRuntime,
					bundleContext.getBundles()));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UnsatisfiedDeclarativeServicesSnapshot.class);

	@Reference
	private ConfigurationAdmin _configurationAdmin;

	@Reference
	private ServiceComponentRuntime _serviceComponentRuntime;

}