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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.exception.LoggedExceptionInInitializerError;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;

import java.net.URLEncoder;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 */
public class URLCodecTest {

	@Test
	public void testDecodeURLWithInvalidURLEncoding() throws Exception {
		testDecodeURLWithInvalidURLEncoding("%");
		testDecodeURLWithInvalidURLEncoding("%0");
		testDecodeURLWithInvalidURLEncoding("%00%");
		testDecodeURLWithInvalidURLEncoding("%00%0");
	}

	@Test
	public void testDecodeURLWithNotHexChars() throws Exception {
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.NUMBER_0 - 1));
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.NUMBER_9 + 1));
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.UPPER_CASE_A - 1));
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.UPPER_CASE_F + 1));
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.LOWER_CASE_A - 1));
		testDecodeURLWithNotHexChars("%0" + (char) (CharPool.LOWER_CASE_F + 1));
	}

	@Test
	public void testDecodeURLWithPercentageInURLParameters() throws Exception {
		testDecodeURLWithInvalidURLEncoding("http://localhost:8080/?id=%'");
	}

	@Test
	public void testDecodeURLWithRawURLs() throws Exception {
		for (int i = 0; i < _RAW_URLS.length; i++) {
			String result = URLCodec.decodeURL(
				_ENCODED_URLS[i], StringPool.UTF8);

			Assert.assertEquals(_RAW_URLS[i], result);
		}
	}

	@Test
	public void testEncodeURL() throws Exception {
		for (int i = 0; i < _RAW_URLS.length; i++) {
			String result = URLCodec.encodeURL(
				_RAW_URLS[i], StringPool.UTF8, false);

			Assert.assertTrue(
				StringUtil.equalsIgnoreCase(_ENCODED_URLS[i], result));

			result = URLCodec.encodeURL(_RAW_URLS[i], StringPool.UTF8, true);

			Assert.assertTrue(
				StringUtil.equalsIgnoreCase(
					_ESCAPE_SPACES_ENCODED_URLS[i], result));
		}
	}

	@Test
	public void testHandlingFourBytesUTFWithSurrogates() throws Exception {
		StringBundler sb = new StringBundler(
			_UNICODE_CATS_AND_DOGS.length * 4 * 2);

		int[] animalsInts = new int[_UNICODE_CATS_AND_DOGS.length];

		for (int i = 0; i < animalsInts.length; i++) {
			animalsInts[i] = Integer.valueOf(_UNICODE_CATS_AND_DOGS[i], 16);
		}

		String animalsString = new String(animalsInts, 0, animalsInts.length);

		byte[] animalsBytes = animalsString.getBytes(StringPool.UTF8);

		for (int i = 0; i < animalsBytes.length; i++) {
			sb.append(StringPool.PERCENT);
			sb.append(Integer.toHexString(0xFF & animalsBytes[i]));
		}

		String escapedAnimalsString = sb.toString();

		Assert.assertEquals(
			animalsString,
			URLCodec.decodeURL(escapedAnimalsString, StringPool.UTF8));
		Assert.assertEquals(
			StringUtil.toLowerCase(escapedAnimalsString),
			StringUtil.toLowerCase(
				URLCodec.encodeURL(animalsString, StringPool.UTF8, false)));
	}

	protected void testDecodeURLWithInvalidURLEncoding(
		String encodedURLString) {

		String expectedMessage = "Invalid URL encoding " + encodedURLString;

		_testDecodeURL(encodedURLString, expectedMessage);
	}

	protected void testDecodeURLWithNotHexChars(String encodedURLString) {
		_testDecodeURL(encodedURLString, "is not a hex char");
	}

	private void _testDecodeURL(
		String encodedURLString, String expectedMessage) {

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					URLCodec.class.getName(), Level.SEVERE)) {

			String decodeURL = URLCodec.decodeURL(
				encodedURLString, StringPool.UTF8);

			Assert.assertEquals(StringPool.BLANK, decodeURL);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			String message = logRecord.getMessage();

			Assert.assertTrue(message.contains(expectedMessage));
		}
	}

	private static final String[] _ENCODED_URLS = new String[9];

	private static final String[] _ESCAPE_SPACES_ENCODED_URLS = new String[9];

	private static final String[] _RAW_URLS = {
		"abcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
		"0123456789", ".-*_", " ", "~`!@#$%^&()+={[}]|\\:;\"'<,>?/", "中文测试",
		"/abc/def", "abc <def> ghi"
	};

	private static final String[] _UNICODE_CATS_AND_DOGS =
		{"1f408", "1f431", "1f415", "1f436"};

	static {
		try {
			for (int i = 0; i < _RAW_URLS.length; i++) {
				_ENCODED_URLS[i] = URLEncoder.encode(
					_RAW_URLS[i], StringPool.UTF8);

				_ESCAPE_SPACES_ENCODED_URLS[i] = StringUtil.replace(
					_ENCODED_URLS[i], StringPool.PLUS, "%20");
			}
		}
		catch (Exception e) {
			throw new LoggedExceptionInInitializerError(e);
		}
	}

}