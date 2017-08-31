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

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.InetAddressUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.SocketUtil;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil.TestRequestInput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
public class SocksProxyConnectionTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() throws IOException {
		_inetAddress = InetAddress.getByName("localhost");

		_port = SocksProxyTestUtil.findOpenPort(8888);

		_serverSocketChannel = SocketUtil.createServerSocketChannel(
			InetAddressUtil.getLoopbackInetAddress(), _port, null);

		_serverSocket = _serverSocketChannel.socket();

		_port = _serverSocket.getLocalPort();

		_executorService = Executors.newCachedThreadPool(
			new SocksProxyServerThreadFactory());
	}

	@After
	public void tearDown() throws IOException {
		_serverSocket.close();

		_serverSocketChannel.close();
	}

	@Test
	public void testConstructor() {
		new SocksProxyConnection(
			Collections.emptyList(),
			SocksProxyTestUtil.createTestSocket(new byte[0]), _executorService);
	}

	@Test
	public void testDomainAType() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		testRequestInput.setDomain("localhost");

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Trying to access host (127.0.0.1) not " +
					"listed in allowedIPAddresses property",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testIncorrectSocksVersion() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		testRequestInput.setSocksVersion((byte)0x04);

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		UnsyncByteArrayOutputStream baos = (UnsyncByteArrayOutputStream)
			socket.getOutputStream();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			Assert.assertEquals(2, baos.size());

			byte[] baosOutput = baos.toByteArray();

			Assert.assertArrayEquals(
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS, baosOutput);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Incorrect SOCKS version",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testInternalSocketCloseExceptionNoLog() {
		Socket socket = new Socket() {

			@Override
			public synchronized void close() throws IOException {
				throw new IOException("Test Close IOException");
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.OFF)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertTrue(logRecords.isEmpty());
		}
	}

	@Test
	public void testInternalSocketCloseExceptionWithValidStream()
		throws Exception {

		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes(), null, new SocketException());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			Future<?> future = _executorService.submit(socksProxyConnection);

			SocketChannel serverSocket = _serverSocketChannel.accept();

			serverSocket.close();

			future.get();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Failed to close socket between proxy and remote",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(SocketException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidAType() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		testRequestInput.setAType(Constants.ATYP_IPV6);

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Invalid atype: " + Constants.ATYP_IPV6,
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidAuthentication() throws IOException {
		byte[] bytes = {Constants.SOCKS5_VERSION, 0x02, 0x04, 0x05};

		Socket socket = SocksProxyTestUtil.createTestSocket(bytes);

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		UnsyncByteArrayOutputStream baos = (UnsyncByteArrayOutputStream)
			socket.getOutputStream();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			Assert.assertEquals(2, baos.size());

			byte[] baosOutput = baos.toByteArray();

			Assert.assertArrayEquals(
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS, baosOutput);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			String logMessage = logRecord.getMessage();

			Assert.assertTrue(
				logMessage.startsWith(
					"java.io.IOException: No acceptable methods found, given " +
						"methods are: "));

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidCMDConnect() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		testRequestInput.setCMDByte((byte)-1);

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		UnsyncByteArrayOutputStream baos = (UnsyncByteArrayOutputStream)
			socket.getOutputStream();

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			Assert.assertEquals(12, baos.size());

			byte[] baosOutput = baos.toByteArray();

			Assert.assertEquals(
				Constants.REP_UNSUPPORTED_COMMAND, baosOutput[3]);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Received unsupported command in the " +
					"request",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testNormalRun() throws InterruptedException, IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, _port);

		testRequestInput.setContentBytes(SocksProxyTestUtil.content.getBytes());

		Socket socket = SocksProxyTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		_executorService.submit(socksProxyConnection);

		SocketChannel serverSocket = _serverSocketChannel.accept();

		serverSocket.close();
	}

	@Test
	public void testNull() {
		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), null,
			_executorService);

		try {
			socksProxyConnection.run();
			Assert.fail();
		}
		catch (NullPointerException npe) {
		}
	}

	@Test
	public void testNullStreamAndInternalSocketCloseException() {
		Socket socket = new Socket() {

			@Override
			public synchronized void close() throws IOException {
				throw new SocketException("Test Close IOException");
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.net.SocketException: Socket is not connected",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertSame(SocketException.class, throwable.getClass());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Failed to close socket between proxy and remote",
				logRecord.getMessage());

			throwable = logRecord.getThrown();

			Assert.assertSame(SocketException.class, throwable.getClass());
		}
	}

	@Test
	public void testRelayDataWithBadOutputStream() throws IOException {
		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.content.getBytes(), new IOException(), null);

		OutputStream outputStream = new OutputStream() {

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				throw new IOException("Error in OutputStream");
			}

			@Override
			public void write(int b) throws IOException {
				throw new IOException("Error in OutputStream");
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			ReflectionTestUtil.invoke(
				socksProxyConnection, "_relayData",
				new Class<?>[] {
					InputStream.class, OutputStream.class, Socket.class,
					int.class
				},
				socket.getInputStream(), outputStream, socket, 4096);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Error in OutputStream",
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Error during socket.shutdownOutput", logRecord.getMessage());
		}
	}

	@Test
	public void testRelayDataWithBadOutputStreamWithoutLogging()
		throws IOException {

		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.content.getBytes(), new IOException(), null);

		OutputStream outputStream = new OutputStream() {

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				throw new IOException("Error in OutputStream");
			}

			@Override
			public void write(int b) throws IOException {
				throw new IOException("Error in OutputStream");
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.OFF)) {

			ReflectionTestUtil.invoke(
				socksProxyConnection, "_relayData",
				new Class<?>[] {
					InputStream.class, OutputStream.class, Socket.class,
					int.class
				},
				socket.getInputStream(), outputStream, socket, 4096);

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 0, logRecords.size());
		}
	}

	@Test
	public void testRelayDataWithBigBuffer() throws IOException {
		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.content.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		ReflectionTestUtil.invoke(
			socksProxyConnection, "_relayData",
			new Class<?>[] {
				InputStream.class, OutputStream.class, Socket.class, int.class
			},
			socket.getInputStream(), ubaos, socket, 4096);

		String ubaosContent = ubaos.toString();

		Assert.assertFalse(ubaosContent.contains("Connection: Keep-Alive"));
	}

	@Test
	public void testRelayDataWithoutKeepAlive() throws IOException {
		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.contentWithoutKeepAlive.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		ReflectionTestUtil.invoke(
			socksProxyConnection, "_relayData",
			new Class<?>[] {
				InputStream.class, OutputStream.class, Socket.class, int.class
			},
			socket.getInputStream(), ubaos, socket, 4096);

		Assert.assertEquals(
			SocksProxyTestUtil.contentWithoutKeepAlive, ubaos.toString());
	}

	@Test
	public void testRelayDataWithSmallBuffer() throws IOException {
		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.content.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		ReflectionTestUtil.invoke(
			socksProxyConnection, "_relayData",
			new Class<?>[] {
				InputStream.class, OutputStream.class, Socket.class, int.class
			},
			socket.getInputStream(), ubaos, socket, 16);

		String ubaosContent = ubaos.toString();

		Assert.assertFalse(ubaosContent.contains("Connection: Keep-Alive"));
	}

	@Test
	public void testRelayDataWithUncaughtException() throws IOException {
		Socket socket = SocksProxyTestUtil.createTestSocket(
			SocksProxyTestUtil.content.getBytes(), new IOException(), null);

		OutputStream outputStream = new OutputStream() {

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				throw new RuntimeException("Error in OutputStream");
			}

			@Override
			public void write(int b) throws IOException {
				throw new RuntimeException("Error in OutputStream");
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			_executorService);

		try {
			ReflectionTestUtil.invoke(
				socksProxyConnection, "_relayData",
				new Class<?>[] {
					InputStream.class, OutputStream.class, Socket.class,
					int.class
				},
				socket.getInputStream(), outputStream, socket, 4096);

			Assert.fail();
		}
		catch (RuntimeException re) {
			Assert.assertEquals("Error in OutputStream", re.getMessage());
		}
	}

	@Test
	public void testRunWithUncaughtException() {
		Socket socket = new Socket() {

			@Override
			public InputStream getInputStream() {
				Throwable throwable = new Throwable("Error in OutputStream");

				ReflectionUtil.throwException(throwable);

				return null;
			}

		};

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.emptyList(), socket, _executorService);

		try {
			socksProxyConnection.run();

			Assert.fail();
		}
		catch (Throwable t) {
			Assert.assertEquals("Error in OutputStream", t.getMessage());
		}
	}

	private ExecutorService _executorService;
	private InetAddress _inetAddress;
	private int _port;
	private ServerSocket _serverSocket;
	private ServerSocketChannel _serverSocketChannel;

}