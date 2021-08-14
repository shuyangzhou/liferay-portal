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

package com.liferay.portal.web.internal.session.replication;

import com.liferay.petra.process.ClassPathUtil;
import com.liferay.portal.kernel.servlet.HttpSessionWrapper;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.lang.reflect.Constructor;

import java.net.URLClassLoader;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Tina Tian
 */
public class SessionReplicationHttpSessionWrapperTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			CodeCoverageAssertor.INSTANCE, LiferayUnitTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_testHttpSession = new TestHttpSession();

		ClassLoader classLoader = new URLClassLoader(
			ClassPathUtil.getClassPathURLs(_CLASS_PATH), null);

		Class<?> clazz = classLoader.loadClass(
			SessionReplicationHttpSessionWrapperTestValue.class.getName());

		Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);

		_testValue1 = constructor.newInstance(_TEST_VALUE_1);

		_testValue2 = "TEST_VALUE_2";
		_testValue3 = new Object();

		_sessionReplicationHttpSessionWrapper =
			new SessionReplicationHttpSessionWrapper(_testHttpSession);

		_sessionReplicationHttpSessionWrapper.setAttribute(
			_TEST_KEY_1, _testValue1);
		_sessionReplicationHttpSessionWrapper.setAttribute(
			_TEST_KEY_2, _testValue2);
		_sessionReplicationHttpSessionWrapper.setAttribute(
			_TEST_KEY_3, _testValue3);
	}

	@Test
	public void testGetAttribute() {
		_testGetAttribute(_TEST_KEY_1, _testValue1, false);
		_testGetAttribute(_TEST_KEY_2, _testValue2, true);
		_testGetAttribute(_TEST_KEY_3, _testValue3, true);
	}

	@Test
	public void testGetAttributeNames() {
		List<String> keys = Collections.list(
			_sessionReplicationHttpSessionWrapper.getAttributeNames());

		Assert.assertEquals(keys.toString(), 3, keys.size());
		Assert.assertTrue(keys.contains(_TEST_KEY_1));
		Assert.assertTrue(keys.contains(_TEST_KEY_2));
		Assert.assertTrue(keys.contains(_TEST_KEY_3));

		keys = Collections.list(_testHttpSession.getAttributeNames());

		Assert.assertEquals(keys.toString(), 3, keys.size());
		Assert.assertFalse(keys.contains(_TEST_KEY_1));
		Assert.assertTrue(keys.contains(_TEST_KEY_2));
		Assert.assertTrue(keys.contains(_TEST_KEY_3));
	}

	@Test
	public void testGetAttributeWithException() {
		SessionReplicationHttpSessionWrapper
			sessionReplicationHttpSessionWrapper =
				new SessionReplicationHttpSessionWrapper(_testHttpSession);

		_testHttpSession.setAttribute(
			"SERIALIZED_ATTRIBUTE_PREFIX_test.value", new byte[0]);

		try (LogCapture logCapture = LoggerTestUtil.configureJDKLogger(
				SessionReplicationHttpSessionWrapper.class.getName(),
				Level.SEVERE)) {

			sessionReplicationHttpSessionWrapper.getAttribute("test.value");

			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 1, logEntries.size());

			LogEntry logEntry = logEntries.get(0);

			Assert.assertEquals(
				"Unable to deserialize object", logEntry.getMessage());
		}
	}

	@Test
	public void testRemoveAttribute() {
		_testRemoveAttribute(_TEST_KEY_1);
		_testRemoveAttribute(_TEST_KEY_2);
		_testRemoveAttribute(_TEST_KEY_3);
	}

	private void _testGetAttribute(String key, Object value, boolean safe) {
		if (safe) {
			Assert.assertEquals(value, _testHttpSession.getAttribute(key));
		}
		else {
			Assert.assertNull(_testHttpSession.getAttribute(key));

			SessionReplicationHttpSessionWrapperTestValue
				sessionReplicationHttpSessionWrapperTestValue =
					(SessionReplicationHttpSessionWrapperTestValue)
						_sessionReplicationHttpSessionWrapper.getAttribute(key);

			Assert.assertNotNull(sessionReplicationHttpSessionWrapperTestValue);
			Assert.assertNotSame(
				value, sessionReplicationHttpSessionWrapperTestValue);
			Assert.assertEquals(
				_TEST_VALUE_1,
				sessionReplicationHttpSessionWrapperTestValue.getValue());
		}
	}

	private void _testRemoveAttribute(String key) {
		Assert.assertNotNull(
			_sessionReplicationHttpSessionWrapper.getAttribute(key));

		_sessionReplicationHttpSessionWrapper.removeAttribute(key);

		Assert.assertNull(
			_sessionReplicationHttpSessionWrapper.getAttribute(key));
	}

	private static final String _CLASS_PATH = ClassPathUtil.getJVMClassPath(
		true);

	private static final String _TEST_KEY_1 = "TEST_KEY_1";

	private static final String _TEST_KEY_2 = "TEST_KEY_2";

	private static final String _TEST_KEY_3 = "TEST_KEY_3";

	private static final String _TEST_VALUE_1 = "TEST_VALUE_1";

	private SessionReplicationHttpSessionWrapper
		_sessionReplicationHttpSessionWrapper;
	private TestHttpSession _testHttpSession;
	private Object _testValue1;
	private Object _testValue2;
	private Object _testValue3;

	private class TestHttpSession extends HttpSessionWrapper {

		@Override
		public Object getAttribute(String name) {
			return _attributes.get(name);
		}

		@Override
		public Enumeration<String> getAttributeNames() {
			return Collections.enumeration(_attributes.keySet());
		}

		@Override
		public void removeAttribute(String name) {
			_attributes.remove(name);
		}

		@Override
		public void setAttribute(String name, Object value) {
			_attributes.put(name, value);
		}

		private TestHttpSession() {
			super(ProxyFactory.newDummyInstance(HttpSession.class));
		}

		private Map<String, Object> _attributes = new HashMap<>();

	}

}