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

import aQute.bnd.osgi.Builder;
import aQute.bnd.osgi.Jar;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.ClassLoaderPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.InputStream;

import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.wiring.BundleWiring;

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
		Framework framework =
			(Framework)ModuleFrameworkUtilAdapter.getFramework();

		BundleContext bundleContext = framework.getBundleContext();

		Map<String, ClassLoader> classLoaders =
			(Map<String, ClassLoader>)ReflectionTestUtil.getFieldValue(
				ClassLoaderPool.class, "_classLoaders");

		String bundleSymbolicName = ClassLoaderTrackerTest.class.getName();
		String bundleVersion = "1.0.0";

		String contextName =
			bundleSymbolicName.concat(StringPool.UNDERLINE).concat(
				bundleVersion);

		try {

			// Test 1, install bundle

			Assert.assertNull(classLoaders.get(contextName));

			Bundle bundle = bundleContext.installBundle(
				bundleSymbolicName,
				_createBundle(bundleSymbolicName, bundleVersion));

			Assert.assertEquals(Bundle.INSTALLED, bundle.getState());

			Assert.assertNull(classLoaders.get(contextName));

			// Test 2, start bundle

			bundle.start();

			Assert.assertEquals(Bundle.ACTIVE, bundle.getState());

			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

			Assert.assertEquals(
				bundleWiring.getClassLoader(), classLoaders.get(contextName));

			// Test 3, stop bundle

			bundle.stop();

			Assert.assertEquals(Bundle.RESOLVED, bundle.getState());

			Assert.assertNull(classLoaders.get(contextName));

			// Test 4, uninstall bundle

			bundle.uninstall();

			Assert.assertEquals(Bundle.UNINSTALLED, bundle.getState());

			Assert.assertNull(classLoaders.get(contextName));
		}
		finally {
			Bundle bundle = bundleContext.getBundle(bundleSymbolicName);

			if (bundle != null) {
				bundle.uninstall();
			}
		}
	}

	private InputStream _createBundle(
			String bundleSymbolicName, String bundleVersion)
		throws Exception {

		try (Builder builder = new Builder()) {
			builder.setBundleSymbolicName(bundleSymbolicName);
			builder.setBundleVersion(bundleVersion);

			try (Jar jar = builder.build()) {
				UnsyncByteArrayOutputStream outputStream =
					new UnsyncByteArrayOutputStream();

				jar.write(outputStream);

				return new UnsyncByteArrayInputStream(
					outputStream.unsafeGetByteArray(), 0, outputStream.size());
			}
		}
	}

}