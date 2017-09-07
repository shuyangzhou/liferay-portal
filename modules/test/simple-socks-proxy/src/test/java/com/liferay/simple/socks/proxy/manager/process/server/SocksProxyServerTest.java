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

package com.liferay.simple.socks.proxy.manager.process.server;

import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.Time;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketImplFactory;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testCloseWithoutStart() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.EMPTY_LIST, 10 * Time.MINUTE, 8888);

		socksProxyServer.close();
	}

	@Test
	public void testNormalStart() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.EMPTY_LIST, 10 * Time.MINUTE, 8888);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			Thread.sleep(10000);

			socksProxyServer.close();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testServerSocketExceptions() throws Exception {
		final SocketImpl socketImpl = new SocketImpl() {

			@Override
			public Object getOption(int optID) throws SocketException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			public void setOption(int optID, Object value)
				throws SocketException {

				throw new SocketException();
			}

			@Override
			protected void accept(SocketImpl s) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected int available() throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void bind(InetAddress host, int port) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void close() throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void connect(InetAddress address, int port)
				throws IOException {

				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void connect(SocketAddress address, int timeout)
				throws IOException {

				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void connect(String host, int port) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void create(boolean stream) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected InputStream getInputStream() throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected OutputStream getOutputStream() throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void listen(int backlog) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

			@Override
			protected void sendUrgentData(int data) throws IOException {
				throw new UnsupportedOperationException("Not supported yet.");
			}

		};

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return socketImpl;
			}

		};

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.EMPTY_LIST, 10 * Time.MINUTE, 8888);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			Thread.sleep(10000);

			socksProxyServer.close();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

}