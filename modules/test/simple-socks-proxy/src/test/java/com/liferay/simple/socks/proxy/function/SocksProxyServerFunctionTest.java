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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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

		_testHttpServerPort = SocksProxyTestUtil.findOpenPort(9999);

		_url = new URL("http://www.able.com:" + _testHttpServerPort + "/test");

		_httpServer = HttpServer.create(
			new InetSocketAddress(_testHttpServerPort), 0);

		_httpServer.createContext(
			"/test",
			(HttpExchange httpExchange) -> {
				httpExchange.sendResponseHeaders(200, _CONTENT.length());

				try (OutputStream outputStream =
						httpExchange.getResponseBody()) {

					outputStream.write(_CONTENT.getBytes());
				}
			});

		_httpServer.start();
	}

	@After
	public void tearDown() {
		_httpServer.stop(0);
	}

	@Test
	public void testNormalBehavior() throws Exception {
		String content = _getContent(_url);

		Assert.assertTrue(content.contains(_CONTENT));

		System.setProperty("socksProxyHost", "localhost");
		System.setProperty("socksProxyPort", String.valueOf(_port));

		try {
			_getContent(_url);
		}
		catch (SocketException se) {
			String message = se.getMessage();

			Assert.assertTrue(
				message.contains(
					"Can't connect to SOCKS proxy:Connection refused"));
		}

		InetAddress inetAddress = InetAddress.getByName(_url.getHost());

		SocksProxyServerManager socksProxyServerManager =
			new SocksProxyServerManager(
				new LocalProcessExecutor(),
				Arrays.asList(new String[] {inetAddress.getHostAddress()}),
				100000, _port);

		UnsyncByteArrayOutputStream ubaos = ConsoleTestUtil.hijackStdErr();

		try {
			socksProxyServerManager.start();

			_connectSocket();

			content = _getContent(_url);

			Assert.assertTrue(content.contains(_CONTENT));
		}
		finally {
			socksProxyServerManager.stop();

			System.setProperty("socksProxyHost", "");
			System.setProperty("socksProxyPort", "");

			String errorOutputString = new String(ubaos.toByteArray());

			Assert.assertTrue(
				errorOutputString.contains("WARNING: java.io.EOFException"));

			ConsoleTestUtil.restoreStdErr(ubaos);
		}
	}

	@Test
	public void testUnallowedDomain() throws Exception {
		String content = _getContent(_url);

		Assert.assertTrue(content.contains(_CONTENT));

		System.setProperty("socksProxyHost", "localhost");
		System.setProperty("socksProxyPort", String.valueOf(_port));

		try {
			_getContent(_url);
		}
		catch (SocketException se) {
			String message = se.getMessage();

			Assert.assertTrue(
				message.contains(
					"Can't connect to SOCKS proxy:Connection refused"));
		}

		SocksProxyServerManager socksProxyServerManager =
			new SocksProxyServerManager(
				new LocalProcessExecutor(), Collections.emptyList(), 500000,
				_port);

		UnsyncByteArrayOutputStream ubaos = ConsoleTestUtil.hijackStdErr();

		try {
			socksProxyServerManager.start();

			_connectSocket();

			try {
				_getContent(_url);
			}
			catch (SocketException se) {
				Assert.assertEquals(
					"Malformed reply from SOCKS server", se.getMessage());
			}

			String errorOutputString = new String(ubaos.toByteArray());

			Assert.assertTrue(
				errorOutputString.contains(
					"WARNING: java.io.IOException: Trying to access host " +
						"(127.0.0.1) not listed in allowedIPAddresses " +
							"property"));
		}
		finally {
			socksProxyServerManager.stop();

			System.setProperty("socksProxyHost", "");
			System.setProperty("socksProxyPort", "");

			String errorOutputString = new String(ubaos.toByteArray());

			Assert.assertTrue(
				errorOutputString.contains("WARNING: java.io.EOFException"));

			ConsoleTestUtil.restoreStdErr(ubaos);
		}
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

	private static final String _CONTENT = "This is the response";

	private HttpServer _httpServer;
	private int _port;
	private int _testHttpServerPort;
	private URL _url;

}