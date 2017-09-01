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
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;
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
	public void testListUnsatisfiedDeclarativeServices() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			DeclarativeServiceUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		Method method = _getTargetMethod(bundleContext);

		String unsatisfiedDeclarativeServices = (String)method.invoke(
			null, _serviceComponentRuntime, bundleContext.getBundles());

		Assert.assertTrue(
			"There should be no unsatisfied declarative service, but we have " +
				"unsatisfied declarative services :" +
					unsatisfiedDeclarativeServices,
			unsatisfiedDeclarativeServices.isEmpty());
	}

	private Bundle _getTargetBundle(BundleContext bundleContext) {
		Bundle targetBundle = null;

		for (Bundle bundle : bundleContext.getBundles()) {
			if (_TARGET_BUNDLE_NAME.equals(bundle.getSymbolicName())) {
				if (targetBundle == null) {
					targetBundle = bundle;

					continue;
				}

				Version targetBundleVersion = targetBundle.getVersion();

				if (targetBundleVersion.compareTo(bundle.getVersion()) < 0) {
					targetBundle = bundle;
				}
			}
		}

		if (targetBundle == null) {
			throw new IllegalStateException(
				"Unable to find target bundle " + _TARGET_BUNDLE_NAME);
		}

		return targetBundle;
	}

	private Method _getTargetMethod(BundleContext bundleContext) {
		Bundle targetBundle = _getTargetBundle(bundleContext);

		try {
			Class<?> declarativeServiceUtilClass = targetBundle.loadClass(
				_TARGET_CLASS_NAME);

			return declarativeServiceUtilClass.getDeclaredMethod(
				_TARGET_METHOD_NAME,
				new Class<?>[] {ServiceComponentRuntime.class, Bundle[].class});
		}
		catch (Exception e) {
			throw new IllegalStateException(
				"Unable to find target method " + _TARGET_METHOD_NAME +
					" in target class " + _TARGET_CLASS_NAME);
		}
	}

	private static final String _TARGET_BUNDLE_NAME =
		"com.liferay.portal.osgi.debug.declarative.service";

	private static final String _TARGET_CLASS_NAME =
		"com.liferay.portal.osgi.debug.declarative.service.internal." +
			"DeclarativeServiceUtil";

	private static final String _TARGET_METHOD_NAME =
		"listUnsatisfiedDeclarativeServices";

	@Inject
	private static ServiceComponentRuntime _serviceComponentRuntime;

}