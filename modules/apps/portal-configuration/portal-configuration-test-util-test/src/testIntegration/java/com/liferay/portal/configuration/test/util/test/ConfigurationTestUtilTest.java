/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.test.util.test;

import com.liferay.portal.configuration.test.util.ConfigurationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.MapUtil;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ManagedService;

/**
 * @author Drew Brokke
 */
public class ConfigurationTestUtilTest
	extends BaseConfigurationTestUtilTestCase {

	@Test
	public void testDeleteConfiguration() throws Exception {
		getConfiguration();

		Assert.assertTrue(testConfigurationExists());

		ConfigurationTestUtil.deleteConfiguration(configurationPid);

		Assert.assertFalse(testConfigurationExists());

		Configuration configuration = getConfiguration();

		Assert.assertTrue(testConfigurationExists());

		ConfigurationTestUtil.deleteConfiguration(configuration);

		Assert.assertFalse(testConfigurationExists());
	}

	@Test
	public void testMarkerConfigurationOutlivesItsOwnUpdate() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			ConfigurationTestUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		List<Dictionary<String, ?>> deliveries = new ArrayList<>();

		ManagedService managedService = properties -> {
			if (properties != null) {
				deliveries.add(properties);
			}
		};

		ServiceRegistration<ManagedService> serviceRegistration =
			bundleContext.registerService(
				ManagedService.class, managedService,
				MapUtil.singletonDictionary(
					Constants.SERVICE_PID,
					ConfigurationTestUtil.class.getName()));

		try {
			for (int i = 0; i < 10; i++) {
				ConfigurationTestUtil.saveConfiguration(
					configurationPid,
					HashMapDictionaryBuilder.<String, Object>put(
						_TEST_KEY, RandomTestUtil.randomString()
					).build());

				ConfigurationTestUtil.deleteConfiguration(configurationPid);
			}
		}
		finally {
			serviceRegistration.unregister();
		}

		// Every configuration change above rides a marker configuration whose
		// own update has to be delivered before that marker is deleted. A
		// missing delivery means the marker was deleted while Configuration
		// Admin was still reading it, which loses the update and can fail it
		// outright with a logged error.

		Assert.assertEquals(deliveries.toString(), 20, deliveries.size());
	}

	@Test
	public void testSaveConfiguration() throws Exception {
		String value1 = RandomTestUtil.randomString();

		Dictionary<String, Object> properties =
			HashMapDictionaryBuilder.<String, Object>put(
				_TEST_KEY, value1
			).build();

		ConfigurationTestUtil.saveConfiguration(configurationPid, properties);

		Configuration configuration = _assertConfigurationValue(value1);

		String value2 = RandomTestUtil.randomString();

		properties.put(_TEST_KEY, value2);

		ConfigurationTestUtil.saveConfiguration(configuration, properties);

		_assertConfigurationValue(value2);
	}

	private Configuration _assertConfigurationValue(String value)
		throws Exception {

		Assert.assertTrue(testConfigurationExists());

		Configuration configuration = getConfiguration();

		Dictionary<String, Object> properties = configuration.getProperties();

		Assert.assertEquals(value, properties.get(_TEST_KEY));

		return configuration;
	}

	private static final String _TEST_KEY =
		"ConfigurationTestUtilTest_TEST_KEY";

}