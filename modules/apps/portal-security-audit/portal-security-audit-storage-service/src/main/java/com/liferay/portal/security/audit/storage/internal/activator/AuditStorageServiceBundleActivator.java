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

package com.liferay.portal.security.audit.storage.internal.activator;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.upgrade.release.BaseUpgradeServiceModuleRelease;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Alan Huang
 */
public class AuditStorageServiceBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		_serviceTracker = new ServiceTracker<CounterLocalService, Void>(
			bundleContext, CounterLocalService.class, null) {

			@Override
			public Void addingService(
				ServiceReference<CounterLocalService> serviceReference) {

				try {
					AuditUpgradeServiceModuleRelease
						auditUpgradeServiceModuleRelease =
							new AuditUpgradeServiceModuleRelease();

					auditUpgradeServiceModuleRelease.upgrade();

					return null;
				}
				catch (UpgradeException upgradeException) {
					throw new RuntimeException(upgradeException);
				}
			}

		};

		_serviceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_serviceTracker.close();
	}

	private ServiceTracker<?, ?> _serviceTracker;

	private static class AuditUpgradeServiceModuleRelease
		extends BaseUpgradeServiceModuleRelease {

		@Override
		protected String getNamespace() {
			return "Audit";
		}

		@Override
		protected String getNewBundleSymbolicName() {
			return "com.liferay.portal.security.audit.storage.service";
		}

		@Override
		protected String getOldBundleSymbolicName() {
			return "audit-portlet";
		}

	}

}