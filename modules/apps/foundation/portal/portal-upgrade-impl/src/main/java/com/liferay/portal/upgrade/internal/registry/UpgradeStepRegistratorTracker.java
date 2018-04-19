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

package com.liferay.portal.upgrade.internal.registry;

import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.configuration.ConfigurationFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.upgrade.internal.excutor.UpgradeExecutor;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Carlos Sierra Andrés
 */
@Component(immediate = true)
public class UpgradeStepRegistratorTracker {

	protected static List<UpgradeInfo> createUpgradeInfos(
		String fromSchemaVersionString, String toSchemaVersionString,
		int buildNumber, UpgradeStep... upgradeSteps) {

		if (ArrayUtil.isEmpty(upgradeSteps)) {
			return Collections.emptyList();
		}

		List<UpgradeInfo> upgradeInfos = new ArrayList<>();

		String upgradeInfoFromSchemaVersionString = fromSchemaVersionString;

		for (int i = 0; i < upgradeSteps.length - 1; i++) {
			UpgradeStep upgradeStep = upgradeSteps[i];

			String upgradeInfoToSchemaVersionString =
				toSchemaVersionString + "-step" + (i - upgradeSteps.length + 1);

			UpgradeInfo upgradeInfo = new UpgradeInfo(
				upgradeInfoFromSchemaVersionString,
				upgradeInfoToSchemaVersionString, buildNumber, upgradeStep);

			upgradeInfos.add(upgradeInfo);

			upgradeInfoFromSchemaVersionString =
				upgradeInfoToSchemaVersionString;
		}

		UpgradeInfo upgradeInfo = new UpgradeInfo(
			upgradeInfoFromSchemaVersionString, toSchemaVersionString,
			buildNumber, upgradeSteps[upgradeSteps.length - 1]);

		upgradeInfos.add(upgradeInfo);

		return upgradeInfos;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_serviceTracker = ServiceTrackerFactory.open(
			bundleContext, UpgradeStepRegistrator.class,
			new UpgradeStepRegistratorServiceTrackerCustomizer());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTracker.close();
	}

	@Reference(target = ModuleServiceLifecycle.DATABASE_INITIALIZED)
	protected ModuleServiceLifecycle moduleServiceLifecycle;

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeStepRegistratorTracker.class);

	private BundleContext _bundleContext;
	private ServiceTracker<UpgradeStepRegistrator, UpgradeStepRegistrator>
		_serviceTracker;

	@Reference
	private UpgradeExecutor _upgradeExecutor;

	private class UpgradeStepRegistratorServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<UpgradeStepRegistrator, UpgradeStepRegistrator> {

		@Override
		public UpgradeStepRegistrator addingService(
			ServiceReference<UpgradeStepRegistrator> serviceReference) {

			UpgradeStepRegistrator upgradeStepRegistrator =
				_bundleContext.getService(serviceReference);

			if (upgradeStepRegistrator == null) {
				return null;
			}

			upgradeStepRegistrator.register(
				new UpgradeStepRegistry(upgradeStepRegistrator));

			return upgradeStepRegistrator;
		}

		@Override
		public void modifiedService(
			ServiceReference<UpgradeStepRegistrator> serviceReference,
			UpgradeStepRegistrator upgradeStepRegistrator) {

			removedService(serviceReference, upgradeStepRegistrator);

			addingService(serviceReference);
		}

		@Override
		public void removedService(
			ServiceReference<UpgradeStepRegistrator> serviceReference,
			UpgradeStepRegistrator upgradeStepRegistrator) {

			_bundleContext.ungetService(serviceReference);
		}

	}

	private class UpgradeStepRegistry implements Registry {

		@Override
		public void register(
			String bundleSymbolicName, String fromSchemaVersionString,
			String toSchemaVersionString, UpgradeStep... upgradeSteps) {

			register(
				fromSchemaVersionString, toSchemaVersionString, upgradeSteps);
		}

		@Override
		public void register(
			String fromSchemaVersionString, String toSchemaVersionString,
			UpgradeStep... upgradeSteps) {

			Class<? extends UpgradeStepRegistrator> clazz =
				_upgradeStepRegistrator.getClass();

			Bundle bundle = FrameworkUtil.getBundle(clazz);

			String bundleSymbolicName = bundle.getSymbolicName();

			int buildNumber = 0;

			try {
				if (ArrayUtil.isNotEmpty(upgradeSteps)) {
					Configuration configuration =
						ConfigurationFactoryUtil.getConfiguration(
							clazz.getClassLoader(), "service");

					Properties properties = configuration.getProperties();

					buildNumber = GetterUtil.getInteger(
						properties.getProperty("build.number"));
				}
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unable to read service.properties for " +
							bundleSymbolicName);
				}
			}

			List<UpgradeInfo> upgradeInfos = createUpgradeInfos(
				fromSchemaVersionString, toSchemaVersionString, buildNumber,
				upgradeSteps);

			_upgradeExecutor.execute(bundleSymbolicName, upgradeInfos);
		}

		private UpgradeStepRegistry(
			UpgradeStepRegistrator upgradeStepRegistrator) {

			_upgradeStepRegistrator = upgradeStepRegistrator;
		}

		private final UpgradeStepRegistrator _upgradeStepRegistrator;

	}

}