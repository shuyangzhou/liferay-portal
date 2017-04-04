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

package com.liferay.portal.character.encoding.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Matthew Tambara
 */
@RunWith(Arquillian.class)
public class CharacterEncodingTest {

	@Test
	public void testWithCharacterEncoding() throws IOException {
		String output = _testCharacterEncoding(true);

		Assert.assertEquals(output, _JAPANESE_TEST, output);
	}

	@Test
	public void testWithoutCharacterEncoding() throws IOException {
		String output = _testCharacterEncoding(false);

		Assert.assertNotEquals(output, _JAPANESE_TEST, output);
	}

	private String _testCharacterEncoding(boolean addCharacterEncoding)
		throws IOException {

		URL url = new URL(_URL);

		HttpURLConnection httpURLConnection =
			(HttpURLConnection)url.openConnection();

		httpURLConnection.setRequestMethod("POST");

		if (addCharacterEncoding) {
			httpURLConnection.addRequestProperty(
				"Content-Type", "charset=" + _CHARSET);
		}

		httpURLConnection.setDoOutput(true);

		try (DataOutputStream dataOutputStream = new DataOutputStream(
				httpURLConnection.getOutputStream())) {

			dataOutputStream.write(_JAPANESE_TEST.getBytes(_CHARSET));
		}

		httpURLConnection.getResponseCode();

		StringBundler sb = new StringBundler(1);

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(
					httpURLConnection.getInputStream(), _CHARSET))) {

			String output = null;

			while ((output = bufferedReader.readLine()) != null) {
				sb.append(output);
			}
		}

		return sb.toString();
	}

	private static final Charset _CHARSET = Charset.forName("Shift_JIS");

	private static final String _JAPANESE_TEST = "テスト";

	private static final String _URL = "http://localhost:8080";

}