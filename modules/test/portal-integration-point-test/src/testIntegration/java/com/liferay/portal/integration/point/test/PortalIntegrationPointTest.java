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

package com.liferay.portal.integration.point.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Leon Chi
 */
@RunWith(Arquillian.class)
public class PortalIntegrationPointTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testPortalIntegrationPoint() {
		_testPortalIntegrationPoint(true);
		_testPortalIntegrationPoint(false);
	}

	private void _testPortalIntegrationPoint(
		boolean useServiceTrackerCustomizer) {

		Registry registry = RegistryUtil.getRegistry();

		ServiceTracker<PortalIntegrationPoint, PortalIntegrationPoint>
			serviceTracker;

		if (useServiceTrackerCustomizer) {
			serviceTracker = registry.trackServices(
				PortalIntegrationPoint.class,
				new PortalIntegrationPointServiceTrackerCustomizer());
		}
		else {
			serviceTracker = registry.trackServices(
				PortalIntegrationPoint.class);
		}

		serviceTracker.open();

		try {
			Assert.assertSame(
				_expectedPortalIntegrationPoint, serviceTracker.getService());
		}
		finally {
			serviceTracker.close();
		}
	}

	@Inject
	private PortalIntegrationPoint _expectedPortalIntegrationPoint;

	private static class PortalIntegrationPointServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<PortalIntegrationPoint, PortalIntegrationPoint> {

		@Override
		public PortalIntegrationPoint addingService(
			ServiceReference<PortalIntegrationPoint> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			return registry.getService(serviceReference);
		}

		@Override
		public void modifiedService(
			ServiceReference<PortalIntegrationPoint> serviceReference,
			PortalIntegrationPoint portalIntegrationPoint) {
		}

		@Override
		public void removedService(
			ServiceReference<PortalIntegrationPoint> serviceReference,
			PortalIntegrationPoint portalIntegrationPoint) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

}