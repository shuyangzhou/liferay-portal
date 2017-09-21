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

package com.liferay.simple.socks.proxy.function;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.test.ConsoleTestUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.simple.socks.proxy.manager.SocksProxyServerManager;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerFunctionTest {

	@Before
	public void setUp() throws IOException {
		_port = SocksProxyTestUtil.findOpenPort(8888);
	}

	@After
	public void tearDown() throws Exception {
		if (_socksProxyServerManager != null) {
			_socksProxyServerManager.stop();
		}

		System.setProperty("socksProxyHost", "");
		System.setProperty("socksProxyPort", "");
	}

	@Test
	public void testNormalBehavior() throws Exception {
		URL url = new URL("http://www.google.com");

		String content = _getContent(url);

		Assert.assertTrue(content.contains("<title>Google</title>"));

		System.setProperty("socksProxyHost", "localhost");
		System.setProperty("socksProxyPort", String.valueOf(_port));

		try {
			_getContent(url);
		}
		catch (SocketException se) {
			Assert.assertEquals(
				"Can't connect to SOCKS proxy:Connection refused (Connection " +
					"refused)",
				se.getMessage());
		}

		InetAddress inetAddress = InetAddress.getByName(url.getHost());

		_socksProxyServerManager = new SocksProxyServerManager(
			new LocalProcessExecutor(),
			Arrays.asList(new String[] {inetAddress.getHostAddress()}), 100000,
			_port);

		_socksProxyServerManager.start();

		_connectSocket();

		content = _getContent(url);

		Assert.assertTrue(content.contains("<title>Google</title>"));
	}

	@Test
	public void testUnallowedDomain() throws Exception {
		URL url = new URL("http://www.google.com");

		String content = _getContent(url);

		Assert.assertTrue(content.contains("<title>Google</title>"));

		System.setProperty("socksProxyHost", "localhost");
		System.setProperty("socksProxyPort", String.valueOf(_port));

		try {
			_getContent(url);
		}
		catch (SocketException se) {
			Assert.assertEquals(
				"Can't connect to SOCKS proxy:Connection refused (Connection " +
					"refused)",
				se.getMessage());
		}

		_socksProxyServerManager = new SocksProxyServerManager(
			new LocalProcessExecutor(), Collections.emptyList(), 100000, _port);

		_socksProxyServerManager.start();

		_connectSocket();

		UnsyncByteArrayOutputStream ubaos = ConsoleTestUtil.hijackStdErr();

		try {
			_getContent(url);
		}
		catch (SocketException se) {
			Assert.assertEquals(
				"Malformed reply from SOCKS server", se.getMessage());
		}

		String errorOutputString = new String(ubaos.toByteArray());

		Assert.assertTrue(
			errorOutputString.contains(
				"WARNING: java.io.IOException: Trying to access host not " +
					"listed in allowedIPAddresses property"));
	}

	private void _connectSocket() throws InterruptedException, IOException {
		while (true) {
			try (Socket socket = new Socket("localhost", _port)) {
				socket.shutdownOutput();

				try (InputStream inputStream = socket.getInputStream()) {
					inputStream.read();
				}

				break;
			}
			catch (ConnectException ce) {
			}
		}
	}

	private String _getContent(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();

		BufferedReader bufferedReader = new BufferedReader(
			new InputStreamReader(urlConnection.getInputStream()));

		StringBundler sb = new StringBundler();

		String line;

		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}

		return sb.toString();
	}

	private int _port;
	private SocksProxyServerManager _socksProxyServerManager;

}