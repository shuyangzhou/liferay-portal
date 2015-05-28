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
package com.liferay.portal.verify;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.verify.test.BaseVerifyProcessTestCase;
import com.liferay.portal.util.PropsUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.AfterClass;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.Rule;

/**
 * @author Manuel de la Peña
 */
public class VerifyPropertiesTest extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule
		= new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@BeforeClass
	public static void setUpClass() {
		_captureAppender
			= Log4JLoggerTestUtil.configureLog4JLogger(
				VerifyProperties.class.getName(), Level.ERROR);
	}

	@AfterClass
	public static void tearDownClass() {
		_captureAppender.close();
	}

	@Test
	public void testMigratedSystemKeys() throws Exception {
		_property = "com.liferay.filters.compression.CompressionFilter";

		SystemProperties.set(_property, "True");

		doVerify();

		String newProperty
			= "com.liferay.portal.servlet.filters.gzip.GZipFilter";

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		LoggingEvent loggingEvent = loggingEvents.get(2);

		Assert.assertEquals(
			loggingEvent.getMessage().toString(),
			"System property \"" + _property + "\" was migrated to the "
			+ "portal property \"" + newProperty + "\"");

		SystemProperties.clear(_property);
	}

	@Test
	public void testRenamedSystemProperty() throws Exception {
		_property
			= "com.liferay.portal.kernel.util."
			+ "StringBundler.unsafe.create.threshold";

		SystemProperties.set(_property, "True");

		doVerify();

		String newProperty = "com.liferay.portal.kernel.util."
			+ "StringBundler.threadlocal.buffer.limit";
		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		LoggingEvent loggingEvent = loggingEvents.get(0);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"System property \"" + _property + "\" was renamed to \""
			+ newProperty + "\"");

		SystemProperties.clear(_property);
	}

	@Test
	public void testObsoleteSystemKeys() throws Exception {
		_property = "com.liferay.util.Http.proxy.host";

		SystemProperties.set(_property, "True");

		doVerify();

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		LoggingEvent loggingEvent = loggingEvents.get(5);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"System property \"" + _property + "\" is obsolete");

		SystemProperties.clear(_property);
	}

	@Test
	public void testmigratedPortalProperty() throws Exception {
		PropsUtil.set("testMigratedPortalProperty", "true");

		doVerify();

		String[] migratedPortalProperty = {
			"cookie.http.only.names.excludes",
			"cookie.http.only.names.excludes"};

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		LoggingEvent loggingEvent = loggingEvents.get(3);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"Portal property \"" + migratedPortalProperty[0]
			+ "\" was migrated to the system property \""
			+ migratedPortalProperty[1] + "\"");

		PropsUtil.set("testMigratedPortalProperty", "false");
	}

	@Test
	public void testRenamedPortalProperty() throws Exception {
		PropsUtil.set("testRenamedPortalProperty", "true");

		doVerify();

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		String[] renamedPortalProperty = {
			"amazon.license.0", "amazon.access.key.id"};

		LoggingEvent loggingEvent = loggingEvents.get(4);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"Portal property \"" + renamedPortalProperty[0]
			+ "\" was renamed to \"" + renamedPortalProperty[1] + "\"");

		PropsUtil.set("testRenamedPortalProperty", "false");
	}

	@Test
	public void testObsoletePortalProperty() throws Exception {
		PropsUtil.set("testObsoletePortalProperty", "true");

		doVerify();

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		String obsoletePortalProperty = "amazon.access.key.id";

		LoggingEvent loggingEvent = loggingEvents.get(1);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"Portal property \"" + obsoletePortalProperty
			+ "\" is obsolete");

		PropsUtil.set("testObsoletePortalProperty", "false");
	}

	@Test
	public void testModularizedPortalProperty() throws Exception {
		PropsUtil.set("testModularizedPortalProperty", "true");

		doVerify();

		List<LoggingEvent> loggingEvents = _captureAppender.getLoggingEvents();

		String[] modularizedPortalProperty = {
			"asset.browser.search.with.database", "search.with.database",
			"com.liferay.asset.browser.web"};

		LoggingEvent loggingEvent = loggingEvents.get(6);

		Assert.assertEquals(loggingEvent.getMessage().toString(),
			"Portal property \"" + modularizedPortalProperty[0]
			+ "\" was modularized to " + modularizedPortalProperty[2]
			+ " as \"" + modularizedPortalProperty[1]);

		PropsUtil.set("testModularizedPortalProperty", "false");
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		VerifyProperties verifyProperties = new VerifyProperties() {
			@Override
			protected Properties loadPortalProperties() {
				Properties properties = new Properties();

				List<String> propertiesResourceNames = ListUtil.fromArray(
					PropsUtil.getArray("include-and-override"));

				propertiesResourceNames.add(0, "portal.properties");

				for (String propertyResourceName : propertiesResourceNames) {
					try (InputStream inputStream
						= getPropertiesResourceAsStream(propertyResourceName)) {

						if (inputStream != null) {
							properties.load(inputStream);
						}
					} catch (IOException ioe) {
						_log.error("Unable to load property "
							+ propertyResourceName, ioe);
					}
				}

				String migratedPortalProperty
					= PropsUtil.get("testMigratedPortalProperty");

				if (migratedPortalProperty != null
					&& migratedPortalProperty.equals("true")) {
					properties.setProperty(
						"cookie.http.only.names.excludes", "true");
				}

				String renamedPortalProperty
					= PropsUtil.get("testRenamedPortalProperty");

				if (renamedPortalProperty != null
					&& renamedPortalProperty.equals("true")) {
					properties.setProperty(
						"amazon.license.0", "true");
				}

				String obsoletePortalProperty
					= PropsUtil.get("testObsoletePortalProperty");

				if (obsoletePortalProperty != null
					&& obsoletePortalProperty.equals("true")) {
					properties.setProperty(
						"amazon.access.key.id", "true");
				}

				String modularizedPortalProperty
					= PropsUtil.get("testModularizedPortalProperty");

				if (modularizedPortalProperty != null
					&& modularizedPortalProperty.equals("true")) {
					properties.setProperty(
						"asset.browser.search.with.database", "true");
				}

				return properties;
			}
		};
		return verifyProperties;
	}

	private static CaptureAppender _captureAppender;

	private static String _property;

	private static final Log _log = LogFactoryUtil.getLog(
		VerifyProperties.class);
}
