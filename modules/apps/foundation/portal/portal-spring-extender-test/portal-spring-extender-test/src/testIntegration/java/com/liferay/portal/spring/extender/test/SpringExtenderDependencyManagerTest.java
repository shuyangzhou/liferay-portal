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

package com.liferay.portal.spring.extender.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.osgi.util.test.OSGiServiceUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.spring.extender.test.reference.SpringExtenderTestComponentReference;

import java.io.IOException;
import java.io.InputStream;

import java.util.Dictionary;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class SpringExtenderDependencyManagerTest {

	@BeforeClass
	public static void setUpClass() throws IOException {
		Bundle bundle = FrameworkUtil.getBundle(
			SpringExtenderDependencyManagerTest.class);

		_bundleContext = bundle.getBundleContext();

		_springExtenderConfiguration = OSGiServiceUtil.callService(
			_bundleContext, ConfigurationAdmin.class,
			configurationAdmin -> configurationAdmin.getConfiguration(
				_CONFIG_NAME, null));

		_properties = _springExtenderConfiguration.getProperties();
	}

	@After
	public void tearDown() throws Exception {
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		ConfigurationListener configurationListener =
			new ConfigurationListener() {

				@Override
				public void configurationEvent(
					ConfigurationEvent configurationEvent) {

					if ((configurationEvent.getType() ==
							ConfigurationEvent.CM_UPDATED) &&
						_CONFIG_NAME.equals(configurationEvent.getPid())) {

						countDownLatch.countDown();
					}
				}

			};

		ServiceRegistration<ConfigurationListener> serviceRegistration =
			_bundleContext.registerService(
				ConfigurationListener.class, configurationListener, null);

		try {
			_springExtenderConfiguration.update(_properties);

			countDownLatch.await();
		}
		finally {
			serviceRegistration.unregister();
		}
	}

	@Test
	public void testSpringExtenderDependencyManagerResolvedDependencies()
		throws Exception {

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					_LOGGER_NAME, Level.INFO)) {

			_testDependencyManager(captureAppender);

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			Assert.assertFalse(loggingEvents.isEmpty());

			for (LoggingEvent loggingEvent : loggingEvents) {
				String logMessage = (String)loggingEvent.getMessage();

				Assert.assertEquals(
					"All Spring extender dependency manager components are " +
						"registered",
					logMessage);
			}
		}
	}

	@Test
	public void testSpringExtenderDependencyManagerUnresolvedDependencies()
		throws Exception {

		Bundle apiBundle = _installBundle(
			"/com.liferay.portal.spring.extender.test.api.jar");
		Bundle serviceBundle = _installBundle(
			"/com.liferay.portal.spring.extender.test.service.jar");

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					_LOGGER_NAME, Level.WARN)) {

			_testDependencyManager(captureAppender);

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			Assert.assertFalse(loggingEvents.isEmpty());

			StringBundler sb = new StringBundler(4);

			sb.append("is unavailable due to missing required dependencies: ");
			sb.append("ServiceDependency[interface ");
			sb.append(SpringExtenderTestComponentReference.class.getName());
			sb.append(" null]");

			String warningMessage = sb.toString();

			for (LoggingEvent loggingEvent : loggingEvents) {
				String logMessage = (String)loggingEvent.getMessage();

				Assert.assertTrue(
					logMessage, logMessage.contains(warningMessage));
			}
		}
		finally {
			serviceBundle.uninstall();

			apiBundle.uninstall();
		}
	}

	private Bundle _installBundle(String path) throws Exception {
		try (InputStream inputStream =
				SpringExtenderDependencyManagerTest.class.getResourceAsStream(
					path)) {

			Bundle bundle = _bundleContext.installBundle(path, inputStream);

			bundle.start();

			return bundle;
		}
	}

	private void _testDependencyManager(CaptureAppender captureAppender)
		throws Exception {

		final CountDownLatch countDownLatch = new CountDownLatch(1);

		ReflectionTestUtil.setFieldValue(
			captureAppender, "_loggingEvents",
			new CopyOnWriteArrayList<LoggingEvent>() {

				@Override
				public boolean add(LoggingEvent loggingEvent) {
					boolean ret = super.add(loggingEvent);

					countDownLatch.countDown();

					return ret;
				}

			});

		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put("unavailableComponentScanningInterval", "1");

		_springExtenderConfiguration.update(properties);

		countDownLatch.await();
	}

	private static final String _CONFIG_NAME =
		"com.liferay.portal.spring.extender.internal.configuration." +
			"SpringExtenderConfiguration";

	private static final String _LOGGER_NAME =
		"com.liferay.portal.spring.extender.internal.context." +
			"ModuleApplicationContextExtender";

	private static BundleContext _bundleContext;
	private static Dictionary<String, Object> _properties;
	private static Configuration _springExtenderConfiguration;

}