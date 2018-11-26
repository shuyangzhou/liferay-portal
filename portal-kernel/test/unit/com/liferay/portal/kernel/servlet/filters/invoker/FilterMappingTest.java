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

package com.liferay.portal.kernel.servlet.filters.invoker;

import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Mika Koivisto
 * @author Leon Chi
 */
public class FilterMappingTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() {
		_testConstructor(
			Collections.singleton(Dispatcher.ASYNC), null, null,
			Collections.singletonList(String.valueOf(Dispatcher.ASYNC)));
		_testConstructor(
			Collections.singleton(Dispatcher.REQUEST),
			_URL_REGEX_IGNORE_PATTERN, _URL_REGEX_PATTERN,
			Collections.emptyList());
	}

	@Test
	public void testGetterMethods() {
		FilterMapping filterMapping = new FilterMapping(
			_TEST_FILTER_NAME, _dummyFilter,
			ProxyFactory.newDummyInstance(FilterConfig.class),
			Collections.emptyList(), Collections.emptyList());

		Assert.assertSame(_TEST_FILTER_NAME, filterMapping.getFilterName());
		Assert.assertSame(_dummyFilter, filterMapping.getFilter());
	}

	@Test
	public void testIsMatch() {
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " has a pattern match with /test" +
					"/login.jsp",
				_dummyFilter.getClass() + " has a regex match with /test" +
					"/login.jsp"
			},
			() -> _testIsMatch(
				true, Dispatcher.ASYNC, "/test/login.jsp",
				Collections.singletonList(_URL_PATTERN)));
		_testWithLog(
			new String[0],
			() -> _testIsMatch(
				false, Dispatcher.REQUEST, "/test/login.jsp",
				Collections.singletonList(_URL_PATTERN)));
		_testWithLog(
			new String[0],
			() -> _testIsMatch(
				false, Dispatcher.ASYNC, null,
				Collections.singletonList(_URL_PATTERN)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " does not have a pattern match " +
					"with /login"
			},
			() -> _testIsMatch(
				false, Dispatcher.ASYNC, "/login",
				Collections.singletonList(_URL_PATTERN)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " has a pattern match with /test" +
					"/login.css",
				_dummyFilter.getClass() + " does not have a regex match with " +
					"/test/login.css"
			},
			() -> _testIsMatch(
				false, Dispatcher.ASYNC, "/test/login.css",
				Collections.singletonList(_URL_PATTERN)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " does not have a pattern match " +
					"with /test/login.jsp"
			},
			() -> _testIsMatch(
				false, Dispatcher.ASYNC, "/test/login.jsp",
				Collections.emptyList()));
	}

	@Test
	public void testIsMatchURLPattern() {
		FilterMapping filterMapping = new FilterMapping(
			_TEST_FILTER_NAME, _dummyFilter,
			ProxyFactory.newDummyInstance(FilterConfig.class),
			Collections.emptyList(), Collections.emptyList());

		Assert.assertTrue(
			"True should be returned because uri \"/test/login.jsp\" matches " +
				"urlPattern \"/test/login.jsp\"",
			filterMapping.isMatchURLPattern(
				"/test/login.jsp", "/test/login.jsp"));
		Assert.assertTrue(
			"True should be returned because uri \"/test/login\" matches " +
				"urlPattern \"/*\"",
			filterMapping.isMatchURLPattern("/test/login", "/*"));
		Assert.assertTrue(
			"True should be returned because uri \"/test/login\" matches " +
				"urlPattern \"/test/*\"",
			filterMapping.isMatchURLPattern("/test/login", "/test/*"));
		Assert.assertTrue(
			"True should be returned because uri \"/test\" matches " +
				"urlPattern \"/test/*\"",
			filterMapping.isMatchURLPattern("/test", "/test/*"));
		Assert.assertTrue(
			"True should be returned because uri \"/test/login.jsp\" matches " +
				"urlPattern \"*.jsp\"",
			filterMapping.isMatchURLPattern("/test/login.jsp", "*.jsp"));
		Assert.assertFalse(
			"False should be returned because uri \"/c/test/login\" does not " +
				"match urlPattern \"/test/*\"",
			filterMapping.isMatchURLPattern("/c/test/login", "/test/*"));
		Assert.assertFalse(
			"False should be returned because uri \"/test/login.css\" does " +
				"not match urlPattern \"/test/login.jsp\"",
			filterMapping.isMatchURLPattern(
				"/test/login.css", "/test/login.jsp"));
		Assert.assertFalse(
			"False should be returned because uri \"login.jsp\" does not " +
				"match urlPattern \"*.jsp\"",
			filterMapping.isMatchURLPattern("login.jsp", "*.jsp"));
		Assert.assertFalse(
			"False should be returned because uri \"/test/login.css\" does " +
				"not match urlPattern \"*.jsp\"",
			filterMapping.isMatchURLPattern("/test/login.css", "*.jsp"));
	}

	@Test
	public void testIsMatchURLRegexPattern() {
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " has a regex match with /test" +
					"/login.jsp"
			},
			() -> _testIsMatchURLRegexPattern(
				true, "/test/login.jsp", null,
				new TestFilterConfig(null, null)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " has a regex match with /test" +
					"/login.jsp?key=value"
			},
			() -> _testIsMatchURLRegexPattern(
				true, "/test/login.jsp", "key=value",
				new TestFilterConfig(null, null)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " does not have a regex match with " +
					"/test/ignore/login.jsp?key=value"
			},
			() -> _testIsMatchURLRegexPattern(
				false, "/test/ignore/login.jsp", "key=value",
				new TestFilterConfig(
					_URL_REGEX_IGNORE_PATTERN, _URL_REGEX_PATTERN)));
		_testWithLog(
			new String[] {
				_dummyFilter.getClass() + " does not have a regex match with " +
					"/test/login.css"
			},
			() -> _testIsMatchURLRegexPattern(
				false, "/test/login.css", null,
				new TestFilterConfig(null, _URL_REGEX_PATTERN)));
	}

	@Test
	public void testReplaceFilter() {
		FilterMapping oldFilterMapping = new FilterMapping(
			_TEST_FILTER_NAME, null,
			new TestFilterConfig(_URL_REGEX_IGNORE_PATTERN, _URL_REGEX_PATTERN),
			Collections.emptyList(),
			Collections.singletonList(String.valueOf(Dispatcher.ASYNC)));

		Assert.assertSame(null, oldFilterMapping.getFilter());

		FilterMapping newFilterMapping = oldFilterMapping.replaceFilter(
			_dummyFilter);

		Assert.assertSame(
			oldFilterMapping.getFilterName(), newFilterMapping.getFilterName());
		Assert.assertSame(_dummyFilter, newFilterMapping.getFilter());
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(oldFilterMapping, "_urlPatterns"),
			ReflectionTestUtil.getFieldValue(newFilterMapping, "_urlPatterns"));
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(oldFilterMapping, "_dispatchers"),
			ReflectionTestUtil.getFieldValue(newFilterMapping, "_dispatchers"));
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(
				oldFilterMapping, "_urlRegexIgnorePattern"),
			ReflectionTestUtil.getFieldValue(
				newFilterMapping, "_urlRegexIgnorePattern"));
		Assert.assertSame(
			ReflectionTestUtil.getFieldValue(
				oldFilterMapping, "_urlRegexPattern"),
			ReflectionTestUtil.getFieldValue(
				newFilterMapping, "_urlRegexPattern"));
	}

	private void _assertURLRegex(String expectedURLRegex, Pattern pattern) {
		if (expectedURLRegex == null) {
			Assert.assertNull(pattern);
		}
		else {
			Assert.assertEquals(expectedURLRegex, pattern.pattern());
		}
	}

	private void _testConstructor(
		Set<Dispatcher> expectedDispatchers, String urlRegexIgnorePattern,
		String urlRegexPattern, List<String> dispatchers) {

		FilterMapping filterMapping = new FilterMapping(
			_TEST_FILTER_NAME, _dummyFilter,
			new TestFilterConfig(urlRegexIgnorePattern, urlRegexPattern),
			Collections.singletonList(_URL_PATTERN), dispatchers);

		Assert.assertSame(_TEST_FILTER_NAME, filterMapping.getFilterName());
		Assert.assertSame(_dummyFilter, filterMapping.getFilter());
		Assert.assertArrayEquals(
			new String[] {_URL_PATTERN},
			ReflectionTestUtil.getFieldValue(filterMapping, "_urlPatterns"));
		Assert.assertEquals(
			expectedDispatchers,
			ReflectionTestUtil.getFieldValue(filterMapping, "_dispatchers"));

		_assertURLRegex(
			urlRegexIgnorePattern,
			ReflectionTestUtil.getFieldValue(
				filterMapping, "_urlRegexIgnorePattern"));
		_assertURLRegex(
			urlRegexPattern,
			ReflectionTestUtil.getFieldValue(
				filterMapping, "_urlRegexPattern"));
	}

	private void _testIsMatch(
		boolean expectedResult, Dispatcher dispatcher, String uri,
		List<String> urlPatterns) {

		FilterMapping filterMapping = new FilterMapping(
			_TEST_FILTER_NAME, _dummyFilter,
			new TestFilterConfig(_URL_REGEX_IGNORE_PATTERN, _URL_REGEX_PATTERN),
			urlPatterns,
			Collections.singletonList(String.valueOf(Dispatcher.ASYNC)));

		Assert.assertEquals(
			expectedResult,
			filterMapping.isMatch(
				new MockHttpServletRequest(HttpMethods.GET, uri), dispatcher,
				uri));
	}

	private void _testIsMatchURLRegexPattern(
		boolean expectedResult, String uri, String queryString,
		FilterConfig filterConfig) {

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(HttpMethods.GET, uri);

		mockHttpServletRequest.setQueryString(queryString);

		FilterMapping filterMapping = new FilterMapping(
			_TEST_FILTER_NAME, _dummyFilter, filterConfig,
			Collections.emptyList(), Collections.emptyList());

		Assert.assertEquals(
			expectedResult,
			filterMapping.isMatchURLRegexPattern(mockHttpServletRequest, uri));
	}

	private void _testWithLog(String[] expectedMessages, Runnable runnable) {
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					FilterMapping.class.getName(), Level.OFF)) {

			runnable.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(
				"logRecords should be empty because the log level is OFF",
				logRecords.isEmpty());

			captureHandler.resetLogLevel(Level.ALL);

			runnable.run();

			Assert.assertEquals(
				logRecords.toString(), expectedMessages.length,
				logRecords.size());

			for (int i = 0; i < logRecords.size(); i++) {
				LogRecord logRecord = logRecords.get(i);

				Assert.assertEquals(
					expectedMessages[i], logRecord.getMessage());
			}
		}
	}

	private static final String _TEST_FILTER_NAME = "testFilterName";

	private static final String _URL_PATTERN = "/test/*";

	private static final String _URL_REGEX_IGNORE_PATTERN = ".+/ignore/*";

	private static final String _URL_REGEX_PATTERN = ".+\\.jsp";

	private final Filter _dummyFilter = ProxyFactory.newDummyInstance(
		Filter.class);

	private class TestFilterConfig implements FilterConfig {

		public TestFilterConfig(
			String urlRegexIgnorePattern, String urlRegexPattern) {

			_urlRegexIgnorePattern = urlRegexIgnorePattern;
			_urlRegexPattern = urlRegexPattern;
		}

		@Override
		public String getFilterName() {
			return null;
		}

		@Override
		public String getInitParameter(String parameterName) {
			if ("url-regex-pattern".equals(parameterName)) {
				return _urlRegexPattern;
			}

			if ("url-regex-ignore-pattern".equals(parameterName)) {
				return _urlRegexIgnorePattern;
			}

			return null;
		}

		@Override
		public Enumeration<String> getInitParameterNames() {
			return null;
		}

		@Override
		public ServletContext getServletContext() {
			return null;
		}

		private final String _urlRegexIgnorePattern;
		private final String _urlRegexPattern;

	}

}