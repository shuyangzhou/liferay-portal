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

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.verify.test.BaseVerifyProcessTestCase;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class VerifyPropertiesTest extends BaseVerifyProcessTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Test
	public void testMigratedPortalProperty() throws Exception {
		String[][] migratedPortalKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_MIGRATED_PORTAL_KEYS");

		_verifyPortalProperty = migratedPortalKeys[0][0];

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"Portal property \"" + _verifyPortalProperty +
					"\" was migrated to the system property \"" +
						migratedPortalKeys[0][1] + "\"",
				loggingEvent.getMessage());
		}
		finally {
			_verifyPortalProperty = null;
		}
	}

	@Test
	public void testMigratedSystemProperty() throws Exception {
		String[][] migratedSystemKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_MIGRATED_SYSTEM_KEYS");

		String property = migratedSystemKeys[0][0];

		SystemProperties.set(property, "true");

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"System property \"" + property + "\" was migrated to the " +
					"portal property \"" + migratedSystemKeys[0][1] + "\"",
				loggingEvent.getMessage().toString());
		}
		finally {
			SystemProperties.clear(property);
		}
	}

	@Test
	public void testModularizedPortalProperty() throws Exception {
		String[][] modularizedPortalKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_MODULARIZED_PORTAL_KEYS");

		_verifyPortalProperty = modularizedPortalKeys[0][0];

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"Portal property \"" + _verifyPortalProperty +
					"\" was modularized to " + modularizedPortalKeys[0][2] +
						" as \"" + modularizedPortalKeys[0][1],
				loggingEvent.getMessage());
		}
		finally {
			_verifyPortalProperty = null;
		}
	}

	@Test
	public void testObsoletePortalProperty() throws Exception {
		String[] obsoletePortalKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_OBSOLETE_PORTAL_KEYS");

		_verifyPortalProperty = obsoletePortalKeys[0];

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"Portal property \"" + _verifyPortalProperty +
					"\" is obsolete",
				loggingEvent.getMessage().toString());
		}
		finally {
			_verifyPortalProperty = null;
		}
	}

	@Test
	public void testObsoleteSystemProperty() throws Exception {
		String[] obsoleteSystemKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_OBSOLETE_SYSTEM_KEYS");

		String property = obsoleteSystemKeys[0];

		SystemProperties.set(property, "true");

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"System property \"" + property + "\" is obsolete",
				loggingEvent.getMessage());
		}
		finally {
			SystemProperties.clear(property);
		}
	}

	@Test
	public void testRenamedPortalProperty() throws Exception {
		String[][] renamedPortalKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_RENAMED_PORTAL_KEYS");

		_verifyPortalProperty = renamedPortalKeys[0][0];

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"Portal property \"" + _verifyPortalProperty +
					"\" was renamed to \"" + renamedPortalKeys[0][1] + "\"",
				loggingEvent.getMessage());
		}
		finally {
			_verifyPortalProperty = null;
		}
	}

	@Test
	public void testRenamedSystemProperty() throws Exception {
		String[][] renamedSystemKeys = ReflectionTestUtil.getFieldValue(
			getVerifyProcess(), "_RENAMED_SYSTEM_KEYS");

		String property = renamedSystemKeys[0][0];

		SystemProperties.set(property, "true");

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"System property \"" + property + "\" was renamed to \"" +
					renamedSystemKeys[0][1] + "\"",
				loggingEvent.getMessage());
		}
		finally {
			SystemProperties.clear(property);
		}
	}

	@Override
	@Test
	public void testVerify() throws Exception {
		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					VerifyProperties.class.getName(), Level.ERROR)) {

			doVerify();

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			Assert.assertTrue(loggingEvents.isEmpty());
		}
	}

	@Override
	protected VerifyProcess getVerifyProcess() {
		return new VerifyProperties() {

			@Override
			protected Properties loadPortalProperties() {
				Properties properties = super.loadPortalProperties();

				if (_verifyPortalProperty != null) {
					properties.setProperty(_verifyPortalProperty, "true");
				}

				return properties;
			}

		};
	}

	private static String _verifyPortalProperty;

}