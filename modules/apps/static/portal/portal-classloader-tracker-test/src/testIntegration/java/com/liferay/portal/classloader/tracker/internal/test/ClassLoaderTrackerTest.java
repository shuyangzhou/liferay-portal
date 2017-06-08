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

package com.liferay.portal.classloader.tracker.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tina Tian
 */
@RunWith(Arquillian.class)
public class ClassLoaderTrackerTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testClassLoaderTracker() throws Exception {
		Registry registry = RegistryUtil.getRegistry();

		Object classLoaderTrackerService = registry.getService(
			"com.liferay.portal.classloader.tracker.internal." +
				"ClassLoaderTracker");

		Assert.assertNotNull(classLoaderTrackerService);

		Class<?> classLoaderTrackerServiceClass =
			classLoaderTrackerService.getClass();

		Assert.assertEquals(
			classLoaderTrackerServiceClass.getClassLoader(),
			ClassLoaderPool.getClassLoader(
				"com.liferay.portal.classloader.tracker"));
	}

}