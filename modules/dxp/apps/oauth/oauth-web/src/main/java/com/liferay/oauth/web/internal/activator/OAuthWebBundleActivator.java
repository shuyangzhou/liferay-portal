/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.oauth.web.internal.activator;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.upgrade.release.BaseUpgradeWebModuleRelease;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Carlos Sierra Andrés
 */
public class OAuthWebBundleActivator implements BundleActivator {

	@Override
	public void start(BundleContext bundleContext) {
		_serviceTracker = new ServiceTracker<CounterLocalService, Void>(
			bundleContext, CounterLocalService.class, null) {

			@Override
			public Void addingService(
				ServiceReference<CounterLocalService> serviceReference) {

				try {
					OAuthUpgradeWebModuleRelease oAuthUpgradeWebModuleRelease =
						new OAuthUpgradeWebModuleRelease();

					oAuthUpgradeWebModuleRelease.upgrade();
				}
				catch (UpgradeException upgradeException) {
					throw new RuntimeException(upgradeException);
				}

				return null;
			}

		};

		_serviceTracker.open();
	}

	@Override
	public void stop(BundleContext bundleContext) {
		_serviceTracker.close();
	}

	private ServiceTracker<?, ?> _serviceTracker;

	private static class OAuthUpgradeWebModuleRelease
		extends BaseUpgradeWebModuleRelease {

		@Override
		protected String getBundleSymbolicName() {
			return "com.liferay.oauth.web";
		}

		@Override
		protected String[] getPortletIds() {
			return new String[] {
				"1_WAR_oauthportlet", "2_WAR_oauthportlet", "3_WAR_oauthportlet"
			};
		}

	}

}