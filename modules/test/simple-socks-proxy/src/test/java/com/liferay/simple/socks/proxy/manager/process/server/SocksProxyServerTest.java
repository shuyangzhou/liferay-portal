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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketImplFactory;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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

			_connectSocket();

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

			_connectSocket();

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
		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptReturnSocketWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

		socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplGetInputStreamWrapper(
					socksProxyServer, socketImpl);
			}

		};

		Socket.setSocketImplFactory(socketImplFactory);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyServer.class.getName(), Level.ALL)) {

			socksProxyServer.start();

			_connectSocket();

			socksProxyServer.close();

			socksProxyServer.interrupt();

			socksProxyServer.join();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			_assertModuleDeactivation(logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Executor termination interrupted", logRecord.getMessage());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);

			ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);
		}
	}

	@Test
	public void testExecutorServerInterruptedExceptionWithoutLog()
		throws Exception {

		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptReturnSocketWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

		socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplGetInputStreamWrapper(
					socksProxyServer, socketImpl);
			}

		};

		Socket.setSocketImplFactory(socketImplFactory);

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);

			ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);
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
		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			new SocketImplFactory() {

				@Override
				public SocketImpl createSocketImpl() {
					return new SocketImplSetOptionWrapper(
						socksProxyServer, socketImpl);
				}

			});

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);
		}
	}

	@Test
	public void testServerSocketSetSoTimeoutSocketExceptionWithoutLog()
		throws Exception {

		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		ServerSocket.setSocketFactory(
			new SocketImplFactory() {

				@Override
				public SocketImpl createSocketImpl() {
					return new SocketImplSetOptionWrapper(
						socksProxyServer, socketImpl);
				}

			});

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);
		}
	}

	@Test
	public void testServerSocketUnableToAcceptIOException() throws Exception {
		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptThrowIOExceptionWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);
		}
	}

	@Test
	public void testServerSocketUnableToAcceptIOExceptionWithoutLog()
		throws Exception {

		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptThrowIOExceptionWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);
		}
	}

	@Test
	public void testSocketSetSoTimeoutSocketException() throws Exception {
		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptReturnSocketWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

		socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplSetOptionWrapper(
					socksProxyServer, socketImpl);
			}

		};

		Socket.setSocketImplFactory(socketImplFactory);

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
				"Unable to set client socket so timeout",
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			_assertModuleDeactivation(logRecord.getMessage());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);

			ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);
		}
	}

	@Test
	public void testSocketSetSoTimeoutSocketExceptionWithoutLog()
		throws Exception {

		ServerSocket serverSocket = new ServerSocket(_port);

		SocketImpl socketImpl = ReflectionTestUtil.getFieldValue(
			serverSocket, "impl");

		SocksProxyServer socksProxyServer = new SocksProxyServer(
			Collections.emptyList(), 10 * Time.MINUTE, _port);

		SocketImplFactory socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplAcceptReturnSocketWrapper(
					socksProxyServer, socketImpl);
			}

		};

		ServerSocket.setSocketFactory(socketImplFactory);

		socketImplFactory = new SocketImplFactory() {

			@Override
			public SocketImpl createSocketImpl() {
				return new SocketImplSetOptionWrapper(
					socksProxyServer, socketImpl);
			}

		};

		Socket.setSocketImplFactory(socketImplFactory);

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
		finally {
			ReflectionTestUtil.setFieldValue(
				ServerSocket.class, "factory", null);

			ReflectionTestUtil.setFieldValue(Socket.class, "factory", null);
		}
	}

	private void _assertModuleDeactivation(String message) {
		Assert.assertEquals(
			"Socks proxy server terminated by module deactivation", message);
	}

	private void _connectSocket() throws InterruptedException, IOException {
		while (true) {
			try (Socket socket = new Socket("localhost", _port)) {
				break;
			}
			catch (ConnectException ce) {
			}
		}

		Thread.sleep(1000);
	}

	private int _port;

	private class SocketImplAcceptReturnSocketWrapper
		extends SocketImplWrapper {

		public SocketImplAcceptReturnSocketWrapper(
			SocksProxyServer socksProxyServer, SocketImpl socketImpl) {

			_sockProxyServer = socksProxyServer;
			_socketImpl = socketImpl;
		}

		@Override
		protected void accept(SocketImpl s) throws IOException {
			ServerSocket serverSocket = ReflectionTestUtil.getFieldValue(
				_sockProxyServer, "_serverSocket");

			ReflectionTestUtil.setFieldValue(serverSocket, "impl", _socketImpl);

			s = new SocketImplSetOptionWrapper(_sockProxyServer, _socketImpl);
		}

		private SocketImpl _socketImpl;
		private SocksProxyServer _sockProxyServer;

	}

	private class SocketImplAcceptThrowIOExceptionWrapper
		extends SocketImplWrapper {

		public SocketImplAcceptThrowIOExceptionWrapper(
			SocksProxyServer socksProxyServer, SocketImpl socketImpl) {

			_sockProxyServer = socksProxyServer;
			_socketImpl = socketImpl;
		}

		@Override
		protected void accept(SocketImpl s) throws IOException {
			ServerSocket serverSocket = ReflectionTestUtil.getFieldValue(
				_sockProxyServer, "_serverSocket");

			ReflectionTestUtil.setFieldValue(serverSocket, "impl", _socketImpl);

			throw new IOException();
		}

		private SocketImpl _socketImpl;
		private SocksProxyServer _sockProxyServer;

	}

	private class SocketImplGetInputStreamWrapper extends SocketImplWrapper {

		public SocketImplGetInputStreamWrapper(
			SocksProxyServer socksProxyServer, SocketImpl socketImpl) {

			_sockProxyServer = socksProxyServer;
			_socketImpl = socketImpl;
		}

		@Override
		protected InputStream getInputStream() throws IOException {
			try {
				Thread.sleep(100000000);
			}
			catch (InterruptedException ie) {
			}

			return null;
		}

		private SocketImpl _socketImpl;
		private SocksProxyServer _sockProxyServer;

	}

	private class SocketImplSetOptionWrapper extends SocketImplWrapper {

		public SocketImplSetOptionWrapper(
			SocksProxyServer socksProxyServer, SocketImpl socketImpl) {

			_sockProxyServer = socksProxyServer;
			_socketImpl = socketImpl;
		}

		@Override
		public void setOption(int optID, Object value) throws SocketException {
			ServerSocket serverSocket = ReflectionTestUtil.getFieldValue(
				_sockProxyServer, "_serverSocket");

			ReflectionTestUtil.setFieldValue(serverSocket, "impl", _socketImpl);

			throw new SocketException();
		};

		private SocketImpl _socketImpl;
		private SocksProxyServer _sockProxyServer;

	}

	private class SocketImplWrapper extends SocketImpl {

		@Override
		public Object getOption(int optID) throws SocketException {
			return null;
		}

		@Override
		public void setOption(int optID, Object value) throws SocketException {
		}

		@Override
		protected void accept(SocketImpl s) throws IOException {
		}

		@Override
		protected int available() throws IOException {
			return -1;
		}

		@Override
		protected void bind(InetAddress host, int port) throws IOException {
		}

		@Override
		protected void close() throws IOException {
		}

		@Override
		protected void connect(InetAddress address, int port)
			throws IOException {
		}

		@Override
		protected void connect(SocketAddress address, int timeout)
			throws IOException {
		}

		@Override
		protected void connect(String host, int port) throws IOException {
		}

		@Override
		protected void create(boolean stream) throws IOException {
		}

		@Override
		protected InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		protected OutputStream getOutputStream() throws IOException {
			return null;
		}

		@Override
		protected void listen(int backlog) throws IOException {
		}

		@Override
		protected void sendUrgentData(int data) throws IOException {
		}

	}

}