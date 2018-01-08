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
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.Time;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil;
import com.liferay.simple.socks.proxy.manager.test.util.socket.impl.wrappers.SocketImplAcceptThrowIOExceptionWrapper;
import com.liferay.simple.socks.proxy.manager.test.util.socket.impl.wrappers.SocketImplSetOptionWrapper;
import com.liferay.simple.socks.proxy.manager.test.util.socket.impl.wrappers.SocketImplSetOptionWrapperForSocket;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Constructor;

import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.net.SocketTimeoutException;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() throws IOException {
		_port = SocksProxyTestUtil.findOpenPort(8888);
	}

	@After
	public void tearDown() {
		ReflectionTestUtil.setFieldValue(ServerSocket.class, "factory", null);

		ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);
	}

	@Test
	public void testCloseWithoutStart() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		socksProxyServer.close();
	}

	@Test
	public void testCreateServerSocketFailure() throws Exception {
		ServerSocket serverSocket = new ServerSocket(_port);

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to create server socket", logRecord.getMessage());
		}
		finally {
			serverSocket.close();
		}
	}

	@Test
	public void testCreateServerSocketFailureWithoutLog() throws Exception {
		ServerSocket serverSocket = new ServerSocket(_port);

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
		finally {
			serverSocket.close();
		}
	}

	@Test
	public void testExecutorServerInterruptedException() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.interrupt();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			_assertModuleDeactivation(logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Executor termination interrupted", logRecord.getMessage());
		}
	}

	@Test
	public void testExecutorServerInterruptedExceptionWithoutLog()
		throws Exception {

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.interrupt();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testNormalStart() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			_assertModuleDeactivation(logRecord.getMessage());
		}
	}

	@Test
	public void testNormalStartWithoutLog() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testServerSocketSetSoTimeoutSocketException() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			_createCustomSocketImplFactory(
				new SocketImplSetOptionWrapper(
					socksProxyServer, _getDefaultSocketImpl())));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to set server socket so timeout",
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			_assertModuleDeactivation(logRecord.getMessage());
		}
	}

	@Test
	public void testServerSocketSetSoTimeoutSocketExceptionWithoutLog()
		throws Exception {

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			_createCustomSocketImplFactory(
				new SocketImplSetOptionWrapper(
					socksProxyServer, _getDefaultSocketImpl())));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testServerSocketUnableToAcceptIOException() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			_createCustomSocketImplFactory(
				new SocketImplAcceptThrowIOExceptionWrapper(
					socksProxyServer, _getDefaultSocketImpl())));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to accept client socket", logRecord.getMessage());

			logRecord = logRecords.get(1);

			_assertModuleDeactivation(logRecord.getMessage());
		}
	}

	@Test
	public void testServerSocketUnableToAcceptIOExceptionWithoutLog()
		throws Exception {

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			_createCustomSocketImplFactory(
				new SocketImplAcceptThrowIOExceptionWrapper(
					socksProxyServer, _getDefaultSocketImpl())));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testSocketSetSoTimeoutSocketException() throws Exception {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		Socket.setSocketImplFactory(_createThreadLocalSocketImplFactory());

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectThreadLocalSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Unable to set client socket so timeout",
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			_assertModuleDeactivation(logRecord.getMessage());
		}
	}

	@Test
	public void testSocketSetSoTimeoutSocketExceptionWithoutLog()
		throws Exception {

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		Socket.setSocketImplFactory(_createThreadLocalSocketImplFactory());

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.OFF)) {

			socksProxyServer.start();

			_connectThreadLocalSocket();

			socksProxyServer.close();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	private void _assertModuleDeactivation(String message) {
		Assert.assertEquals(
			"Socks proxy server terminated by module deactivation", message);
	}

	private void _connectSocket() throws InterruptedException, IOException {
		Object factory = ReflectionTestUtil.getFieldValue(
			Socket.class, "factory");

		ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);

		try {
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
		finally {
			ReflectionTestUtil.setFieldValue(Socket.class, "factory", factory);
		}
	}

	private void _connectThreadLocalSocket()
		throws InterruptedException, IOException {

		while (true) {
			try (Socket socket = new Socket("localhost", _port)) {
				socket.shutdownOutput();

				socket.setSoTimeout(2000);

				try (InputStream inputStream = socket.getInputStream()) {
					inputStream.read();
				}

				break;
			}
			catch (ConnectException ce) {
			}
			catch (SocketTimeoutException ste) {
				break;
			}
		}
	}

	private SocketImplFactory _createCustomSocketImplFactory(
		SocketImpl socketImpl) {

		return () -> socketImpl;
	}

	private SocketImplFactory _createThreadLocalSocketImplFactory()
		throws NoSuchMethodException {

		Socket socket = new Socket();

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			socket, "impl");

		Class<?> socketImplClass = socketImpl.getClass();

		Constructor constructor = socketImplClass.getDeclaredConstructor();

		constructor.setAccessible(true);

		SocketImpl customSocketImpl = new SocketImplSetOptionWrapperForSocket();

		final ThreadLocal<Boolean> threadLocal = new ThreadLocal<>();

		threadLocal.set(true);

		return () -> {
			if (threadLocal.get() != null) {
				try {
					return (SocketImpl)constructor.newInstance();
				}
				catch (Exception e) {
					throw new RuntimeException(
						"Failed to create new instance of SocketImpl", e);
				}
			}

			return customSocketImpl;
		};
	}

	private SocketImpl _getDefaultSocketImpl() throws IOException {
		ServerSocket serverSocket = new ServerSocket(_port);

		return ReflectionTestUtil.getFieldValue(serverSocket, "impl");
	}

	private int _port;

}