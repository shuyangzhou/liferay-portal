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

package com.liferay.portal.osgi.debug.declarative.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.osgi.debug.declarative.service.DeclarativeServiceUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.runtime.ServiceComponentRuntime;

/**
 * @author Tina Tian
 */
@RunWith(Arquillian.class)
public class DeclarativeServiceUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testListUnsatisfiedDeclarativeServices() {
		Registry registry = RegistryUtil.getRegistry();

		ConfigurationAdmin configurationAdmin = registry.getService(
			ConfigurationAdmin.class);

		ServiceComponentRuntime serviceComponentRuntime = registry.getService(
			ServiceComponentRuntime.class);

		Bundle bundle = FrameworkUtil.getBundle(
			DeclarativeServiceUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		String unsatisfiedDeclarativeServices =
			DeclarativeServiceUtil.listUnsatisfiedDeclarativeServices(
				configurationAdmin, serviceComponentRuntime,
				bundleContext.getBundles());

		if (!unsatisfiedDeclarativeServices.isEmpty()) {
			Assert.fail(
				"There should be no unsatisfied declarative service, but we " +
					"have unsatisfied declarative services :" +
						unsatisfiedDeclarativeServices);
		}
	}

}