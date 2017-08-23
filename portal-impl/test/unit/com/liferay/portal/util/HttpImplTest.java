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

package com.liferay.portal.util;

import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import org.powermock.api.mockito.PowerMockito;

/**
 * @author Miguel Pastor
 * @author Christopher Kian
 */
public class HttpImplTest extends PowerMockito {

	@BeforeClass
	public static void setUpClass() {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(
			new PortalImpl() {

				@Override
				public String[] stripURLAnchor(String url, String separator) {
					return new String[] {url, StringPool.BLANK};
				}

			});
	}

	@Test
	public void testAddBooleanParameter() {
		_addParameter("http://foo.com", "p", String.valueOf(Boolean.TRUE));
	}

	@Test
	public void testAddDoubleParameter() {
		_addParameter("http://foo.com", "p", String.valueOf(111.1D));
	}

	@Test
	public void testAddIntParameter() {
		_addParameter("http://foo.com", "p", String.valueOf(1));
	}

	@Test
	public void testAddLongParameter() {
		_addParameter("http://foo.com", "p", String.valueOf(111111L));
	}

	@Test
	public void testAddShortParameter() {
		_addParameter("http://foo.com", "p", String.valueOf((short)1));
	}

	@Test
	public void testAddStringParameter() {
		_addParameter("http://foo.com", "p", new String("foo"));
	}

	@Test
	public void testDecodeMultipleCharacterEncodedPath() {
		Assert.assertEquals(
			"http://foo?p=$param",
			_httpImpl.decodePath("http://foo%3Fp%3D%24param"));
	}

	@Test
	public void testDecodeNoCharacterEncodedPath() {
		Assert.assertEquals("http://foo", _httpImpl.decodePath("http://foo"));
	}

	@Test
	public void testDecodeSingleCharacterEncodedPath() {
		Assert.assertEquals(
			"http://foo#anchor", _httpImpl.decodePath("http://foo%23anchor"));
	}

	@Test
	public void testDecodeURLWithInvalidURLEncoding() {
		testDecodeURLWithInvalidURLEncoding("%");
		testDecodeURLWithInvalidURLEncoding("%0");
		testDecodeURLWithInvalidURLEncoding("%00%");
		testDecodeURLWithInvalidURLEncoding("%00%0");
		testDecodeURLWithInvalidURLEncoding("http://localhost:8080/?id=%");
	}

	@Test
	public void testDecodeURLWithNotHexChars() throws Exception {
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.NUMBER_0 - 1));
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.NUMBER_9 + 1));
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.UPPER_CASE_A - 1));
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.UPPER_CASE_F + 1));
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.LOWER_CASE_A - 1));
		testDecodeURLWithNotHexChars("%0" + (char)(CharPool.LOWER_CASE_F + 1));
	}

	@Test
	public void testEncodeMultipleCharacterEncodedPath() {
		Assert.assertEquals(
			"http%3A//foo%3Fp%3D%24param",
			_httpImpl.encodePath("http://foo?p=$param"));
	}

	@Test
	public void testEncodeNoCharacterEncodedPath() {
		Assert.assertEquals("http%3A//foo", _httpImpl.encodePath("http://foo"));
	}

	@Test
	public void testEncodeSingleCharacterEncodedPath() {
		Assert.assertEquals(
			"http%3A//foo%23anchor", _httpImpl.encodePath("http://foo#anchor"));
	}

	@Test
	public void testGetMaxURLDepth() {
		Assert.assertEquals(3, _httpImpl.getMaxURLDepth(_URL, 1000));
		Assert.assertEquals(3, _httpImpl.getMaxURLDepth(_URL, _URL.length()));
		Assert.assertEquals(
			_URL,
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, _URL, 3));
		Assert.assertEquals(
			2, _httpImpl.getMaxURLDepth(_URL, _URL.length() - 1));
		Assert.assertEquals(
			2, _httpImpl.getMaxURLDepth(_URL, _URL_COUNT_TWO.length()));
		Assert.assertEquals(
			_URL_COUNT_TWO,
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, _URL, 2));
		Assert.assertEquals(
			1, _httpImpl.getMaxURLDepth(_URL, _URL_COUNT_TWO.length() - 1));
		Assert.assertEquals(
			1, _httpImpl.getMaxURLDepth(_URL, _URL_COUNT_ONE.length()));
		Assert.assertEquals(
			_URL_COUNT_ONE,
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, _URL, 1));
		Assert.assertEquals(
			0, _httpImpl.getMaxURLDepth(_URL, _URL_COUNT_ONE.length() - 1));
		Assert.assertEquals(
			0, _httpImpl.getMaxURLDepth(_URL, _URL_COUNT_ZERO.length()));
		Assert.assertEquals(
			_URL_COUNT_ZERO,
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, _URL, 0));
	}

	@Test
	public void testGetParameterMapWithCorrectQuery() {
		Map<String, String[]> parameterMap = _httpImpl.getParameterMap(
			"a=1&b=2");

		Assert.assertNotNull(parameterMap);

		Assert.assertEquals("1", parameterMap.get("a")[0]);
		Assert.assertEquals("2", parameterMap.get("b")[0]);
	}

	@Test
	public void testGetParameterMapWithMultipleBadParameter() {
		Map<String, String[]> parameterMap = _httpImpl.getParameterMap(
			"null&a=1&null");

		Assert.assertNotNull(parameterMap);

		Assert.assertEquals("1", parameterMap.get("a")[0]);
	}

	@Test
	public void testGetParameterMapWithSingleBadParameter() {
		Map<String, String[]> parameterMap = _httpImpl.getParameterMap(
			"null&a=1");

		Assert.assertNotNull(parameterMap);

		Assert.assertEquals("1", parameterMap.get("a")[0]);
	}

	@Test
	public void testNormalizePath() {
		Assert.assertEquals("/api/axis", _httpImpl.normalizePath("/api/axis?"));
		Assert.assertEquals("/", _httpImpl.normalizePath("/.."));
		Assert.assertEquals(
			"/api/axis", _httpImpl.normalizePath("/api/%61xis"));
		Assert.assertEquals(
			"/api/%2561xis", _httpImpl.normalizePath("/api/%2561xis"));
		Assert.assertEquals(
			"/api/ax%3Fs", _httpImpl.normalizePath("/api/ax%3fs"));
		Assert.assertEquals(
			"/api/%2F/axis",
			_httpImpl.normalizePath("/api/%2f/;x=aaa_%2f_y/axis"));
		Assert.assertEquals(
			"/api/axis", _httpImpl.normalizePath("/api/;x=aaa_%2f_y/axis"));
		Assert.assertEquals(
			"/api/axis", _httpImpl.normalizePath("/api/;x=aaa_%5b_y/axis"));
		Assert.assertEquals(
			"/api/axis",
			_httpImpl.normalizePath("/api/;x=aaa_LIFERAY_TEMP_SLASH_y/axis"));
		Assert.assertEquals(
			"/api/axis",
			_httpImpl.normalizePath("/api///////%2e/../;x=y/axis"));
		Assert.assertEquals(
			"/api/axis",
			_httpImpl.normalizePath("/////api///////%2e/a/../;x=y/axis"));
		Assert.assertEquals(
			"/api/axis",
			_httpImpl.normalizePath("/////api///////%2e/../;x=y/axis"));
		Assert.assertEquals(
			"/api/axis", _httpImpl.normalizePath("/api///////%2e/axis"));
		Assert.assertEquals(
			"/api/axis", _httpImpl.normalizePath("./api///////%2e/axis"));
		Assert.assertEquals(
			"/api/axis?foo=bar&bar=foo",
			_httpImpl.normalizePath("./api///////%2e/axis?foo=bar&bar=foo"));
	}

	@Test
	public void testParameterMapFromString() {
		Map<String, String[]> expectedParameterMap = new HashMap<>();

		expectedParameterMap.put("key1", new String[] {"value1", "value2"});
		expectedParameterMap.put("key2", new String[] {"value3"});

		StringBundler sb = new StringBundler(12);

		for (Entry<String, String[]> entry : expectedParameterMap.entrySet()) {
			String key = entry.getKey();

			for (String value : entry.getValue()) {
				sb.append(key);
				sb.append(StringPool.EQUAL);
				sb.append(value);
				sb.append(StringPool.AMPERSAND);
			}
		}

		sb.setIndex(sb.index() - 1);

		Map<String, String[]> actualParameterMap = _httpImpl.getParameterMap(
			sb.toString());

		Assert.assertEquals(
			"Actual parameter map size: " + actualParameterMap.size(),
			expectedParameterMap.size(), actualParameterMap.size());

		for (String key : actualParameterMap.keySet()) {
			Assert.assertArrayEquals(
				expectedParameterMap.get(key), actualParameterMap.get(key));
		}
	}

	@Test
	public void testProtocolizeMalformedURL() {
		Assert.assertEquals(
			"foo.com", _httpImpl.protocolize("foo.com", 8080, true));
	}

	@Test
	public void testProtocolizeNonsecure() {
		Assert.assertEquals(
			"http://foo.com:8080",
			_httpImpl.protocolize("https://foo.com", 8080, false));
	}

	@Test
	public void testProtocolizeSecure() {
		Assert.assertEquals(
			"https://foo.com:8443",
			_httpImpl.protocolize("http://foo.com", 8443, true));
	}

	@Test
	public void testProtocolizeWithoutPort() {
		Assert.assertEquals(
			"http://foo.com:8443/web/guest",
			_httpImpl.protocolize("https://foo.com:8443/web/guest", -1, false));
	}

	@Test
	public void testRemovePathParameters() {
		Assert.assertEquals(
			"/TestServlet/one/two",
			_httpImpl.removePathParameters(
				"/TestServlet;jsessionid=ae01b0f2af/one;test=$one@two/two"));
		Assert.assertEquals(
			"/TestServlet/one/two",
			_httpImpl.removePathParameters(
				"/TestServlet;jsessionid=ae01b0f2af;test2=123,456" +
					"/one;test=$one@two/two"));
		Assert.assertEquals(
			"/TestServlet/one/two",
			_httpImpl.removePathParameters(
				"/TestServlet/one;test=$one@two/two;jsessionid=ae01b0f2af;" +
					"test2=123,456"));
		Assert.assertEquals("/", _httpImpl.removePathParameters("/;?"));
		Assert.assertEquals("/", _httpImpl.removePathParameters("//;/;?"));
		Assert.assertEquals("//", _httpImpl.removePathParameters("///;?"));
		Assert.assertEquals(
			"/TestServlet/one",
			_httpImpl.removePathParameters("/TestServlet/;x=y/one"));
		Assert.assertEquals(
			"/TestServlet/one",
			_httpImpl.removePathParameters("/;x=y/TestServlet/one/;x=y"));

		try {
			_httpImpl.removePathParameters(";x=y");

			Assert.fail();
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals("Unable to handle URI: ;x=y", iae.getMessage());
		}
	}

	@Test
	public void testRemoveProtocol() {
		Assert.assertEquals(
			"#^&://abc.com", _httpImpl.removeProtocol("#^&://abc.com"));
		Assert.assertEquals(
			"^&://abc.com", _httpImpl.removeProtocol("/^&://abc.com"));
		Assert.assertEquals(
			"ftp.foo.com", _httpImpl.removeProtocol("ftp://ftp.foo.com"));
		Assert.assertEquals(
			"foo.com", _httpImpl.removeProtocol("http://///foo.com"));
		Assert.assertEquals("foo.com", _httpImpl.removeProtocol("////foo.com"));
		Assert.assertEquals(
			"foo.com", _httpImpl.removeProtocol("http://http://foo.com"));
		Assert.assertEquals(
			"www.google.com", _httpImpl.removeProtocol("/\\www.google.com"));
		Assert.assertEquals(
			"www.google.com",
			_httpImpl.removeProtocol("/\\//\\/www.google.com"));
		Assert.assertEquals(
			"/path/name", _httpImpl.removeProtocol("/path/name"));
		Assert.assertEquals(
			"./path/name", _httpImpl.removeProtocol("./path/name"));
		Assert.assertEquals(
			"../path/name", _httpImpl.removeProtocol("../path/name"));
		Assert.assertEquals(
			"foo.com?redirect=http%3A%2F%2Ffoo2.com",
			_httpImpl.removeProtocol(
				"http://foo.com?redirect=http%3A%2F%2Ffoo2.com"));
		Assert.assertEquals(
			"www.google.com/://localhost",
			_httpImpl.removeProtocol("http://www.google.com/://localhost"));
	}

	@Test
	public void testShortenURL() {
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, "www.google.com", 0));
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class}, "www.google.com?",
				0));
		Assert.assertEquals(
			"www.google.com?first=foo&second=bar",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?first=foo&second=bar", 0));
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?_backURL=www.yahoo.com", 0));
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?_redirect=www.yahoo.com", 0));
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?_returnToFullPageURL=www.yahoo.com", 0));
		Assert.assertEquals(
			"www.google.com",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com", 0));
		Assert.assertEquals(
			"www.google.com?parameter=foo",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com&parameter=foo", 0));
		Assert.assertEquals(
			"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
				"www.bing.com%26parameter%3Dbar&parameter=foo",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
					"www.bing.com%26parameter%3Dbar&parameter=foo",
				3));
		Assert.assertEquals(
			"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
				"www.bing.com%26parameter%3Dbar&parameter=foo",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
					"www.bing.com%26parameter%3Dbar&parameter=foo",
				2));
		Assert.assertEquals(
			"www.google.com?redirect=www.yahoo.com%3Fparameter%3Dbar&" +
				"parameter=foo",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
					"www.bing.com%26parameter%3Dbar&parameter=foo",
				1));
		Assert.assertEquals(
			"www.google.com?parameter=foo",
			ReflectionTestUtil.invoke(
				_httpImpl, "_shortenURL",
				new Class<?>[] {String.class, int.class},
				"www.google.com?redirect=www.yahoo.com%3Fredirect%3D" +
					"www.bing.com%26parameter%3Dbar&parameter=foo",
				0));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					HttpImpl.class.getName(), Level.FINE)) {

			Assert.assertEquals("", _httpImpl.shortenURL("redirect=%xy", 1));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Skipping undecodable parameter redirect=%xy",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(
				IllegalArgumentException.class, throwable.getClass());
			Assert.assertEquals("x is not a hex char", throwable.getMessage());
		}

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					HttpImpl.class.getName(), Level.FINE)) {

			Assert.assertEquals(
				"www.google.com",
				_httpImpl.shortenURL("www.google.com?redirect=%xy", 1));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Skipping undecodable parameter redirect=%xy",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(
				IllegalArgumentException.class, throwable.getClass());
			Assert.assertEquals("x is not a hex char", throwable.getMessage());
		}

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					HttpImpl.class.getName(), Level.OFF)) {

			Assert.assertEquals(
				"www.google.com",
				_httpImpl.shortenURL("www.google.com?redirect=%xy", 1));

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.toString(), logRecords.isEmpty());
		}
	}

	protected void testDecodeURLWithInvalidURLEncoding(String url) {
		_testDecodeURL(url, "Invalid URL encoding " + url);
	}

	protected void testDecodeURLWithNotHexChars(String url) {
		_testDecodeURL(url, "is not a hex char");
	}

	private void _addParameter(
		String url, String parameterName, String parameterValue) {

		String newURL = _httpImpl.addParameter(
			url, parameterName, parameterValue);

		StringBundler sb = new StringBundler(5);

		sb.append(url);
		sb.append(StringPool.QUESTION);
		sb.append(parameterName);
		sb.append(StringPool.EQUAL);
		sb.append(parameterValue);

		Assert.assertEquals(sb.toString(), newURL);
	}

	private void _testDecodeURL(String url, String expectedMessage) {
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					HttpImpl.class.getName(), Level.SEVERE)) {

			String decodeURL = _httpImpl.decodeURL(url);

			Assert.assertEquals(StringPool.BLANK, decodeURL);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			String message = logRecord.getMessage();

			Assert.assertTrue(message.contains(expectedMessage));
		}
	}

	private static final String _URL;

	private static final String _URL_COUNT_ONE;

	private static final String _URL_COUNT_TWO;

	private static final String _URL_COUNT_ZERO =
		"www.google.com?parameter=foo";

	static {
		StringBundler sb = new StringBundler(5);

		sb.append("www.google.com?redirect=www.yahoo.com%3Fredirect%3D");
		sb.append("www.bing.com%253Fredirect%253Dwww.liferay.com%2526");
		sb.append("parameter%253Dfoofoobar%26parameter%3Dfoobar&");
		sb.append("_backURL=www.ask.com%3Fredirect%3Dwww.zombo.com%26");
		sb.append("parameter%3Dbar&parameter=foo");

		_URL = sb.toString();

		sb.setIndex(0);

		sb.append("www.google.com?redirect=www.yahoo.com%3Fredirect%3D");
		sb.append("www.bing.com%253Fparameter%253Dfoofoobar");
		sb.append("%26parameter%3Dfoobar&_backURL=www.ask.com%3Fredirect");
		sb.append("%3Dwww.zombo.com%26parameter%3Dbar&parameter=foo");

		_URL_COUNT_TWO = sb.toString();

		sb.setIndex(0);

		sb.append("www.google.com?redirect=www.yahoo.com%3F");
		sb.append("parameter%3Dfoobar&_backURL=www.ask.com%3F");
		sb.append("parameter%3Dbar&parameter=foo");

		_URL_COUNT_ONE = sb.toString();
	}

	private final HttpImpl _httpImpl = new HttpImpl();

}