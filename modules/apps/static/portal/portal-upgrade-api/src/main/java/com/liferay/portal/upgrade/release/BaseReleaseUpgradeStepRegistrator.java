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

package com.liferay.portal.upgrade.release;

import com.liferay.portal.kernel.model.Release;
import com.liferay.portal.kernel.service.ReleaseLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Shuyang Zhou
 */
public abstract class BaseReleaseUpgradeStepRegistrator
	implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.registerInitialUpgradeSteps(
			new UpgradeProcess() {

				@Override
				protected void doUpgrade() throws Exception {
					Release oldRelease = releaseLocalService.fetchRelease(
						getOldServletContextName());

					if (oldRelease != null) {
						Release newRelease = releaseLocalService.fetchRelease(
							getNewServletContextName());

						newRelease.setSchemaVersion(
							oldRelease.getSchemaVersion());

						releaseLocalService.updateRelease(newRelease);

						releaseLocalService.deleteRelease(oldRelease);
					}

					Bundle bundle = FrameworkUtil.getBundle(getClass());

					BundleContext bundleContext = bundle.getBundleContext();

					_serviceRegistration = bundleContext.registerService(
						UpgradeStepRegistrator.class,
						getUpgradeStepRegistrator(), null);
				}

			});
	}

	@Deactivate
	protected void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	protected abstract String getNewServletContextName();

	protected abstract String getOldServletContextName();

	protected abstract UpgradeStepRegistrator getUpgradeStepRegistrator();

	@Reference
	protected ReleaseLocalService releaseLocalService;

	private ServiceRegistration<?> _serviceRegistration;

}