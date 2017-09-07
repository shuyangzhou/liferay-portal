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
import com.liferay.simple.socks.proxy.manager.process.server.SocksProxyConnectionTestUtil.TestRequestInput;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
		_inetAddress = InetAddress.getByName("www.google.com");
	}

	@Test
	public void testConstructor() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		testRequestInput.setContentBytes(
			SocksProxyConnectionTestUtil.content.getBytes());

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

		socksProxyConnection.run();
	}

	@Test
	public void testDomainAType() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		testRequestInput.setDomain("www.liferay.com");

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"java.io.IOException: Trying to access host not listed in " +
					"allowedIPAddresses property",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertEquals(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testIncorrectSocksVersion() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		testRequestInput.setSocksVersion((byte)0x04);

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

			Assert.assertEquals(IOException.class, throwable.getClass());
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
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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
		throws IOException {

		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes(), null, new SocketException());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					SocksProxyConnection.class.getName(), Level.ALL)) {

			socksProxyConnection.run();

			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 1, logRecords.size());

			LogRecord logRecord = logRecords.get(0);

			Assert.assertEquals(
				"Failed to close socket between proxy and remote",
				logRecord.getMessage());

			Throwable throwable = logRecord.getThrown();

			Assert.assertEquals(SocketException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidAType() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		testRequestInput.setAType(Constants.ATYP_IPV6);

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

			Assert.assertEquals(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidAuthentication() throws IOException {
		byte[] bytes = {Constants.SOCKS5_VERSION, 0x02, 0x04, 0x05};

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(bytes);

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

			Assert.assertEquals(IOException.class, throwable.getClass());
		}
	}

	@Test
	public void testInvalidCMDConnect() throws IOException {
		TestRequestInput testRequestInput = new TestRequestInput(
			_inetAddress, 80);

		testRequestInput.setCMDByte((byte)-1);

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			testRequestInput.getBytes());

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

			Assert.assertEquals(IOException.class, throwable.getClass());
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
			Collections.EMPTY_LIST, socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

			Assert.assertEquals(SocketException.class, throwable.getClass());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"Failed to close socket between proxy and remote",
				logRecord.getMessage());

			throwable = logRecord.getThrown();

			Assert.assertEquals(SocketException.class, throwable.getClass());
		}
	}

	@Test
	public void testRelayDataWithBadOutputStream() throws IOException {
		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			SocksProxyConnectionTestUtil.content.getBytes(), new IOException(),
			null);

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
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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

		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			SocksProxyConnectionTestUtil.content.getBytes(), new IOException(),
			null);

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
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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
		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			SocksProxyConnectionTestUtil.content.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

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
		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			SocksProxyConnectionTestUtil.contentWithoutKeepAlive.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

		ReflectionTestUtil.invoke(
			socksProxyConnection, "_relayData",
			new Class<?>[] {
				InputStream.class, OutputStream.class, Socket.class, int.class
			},
			socket.getInputStream(), ubaos, socket, 4096);

		Assert.assertEquals(
			SocksProxyConnectionTestUtil.contentWithoutKeepAlive,
			ubaos.toString());
	}

	@Test
	public void testRelayDataWithSmallBuffer() throws IOException {
		Socket socket = SocksProxyConnectionTestUtil.createTestSocket(
			SocksProxyConnectionTestUtil.content.getBytes());

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		SocksProxyConnection socksProxyConnection = new SocksProxyConnection(
			Collections.singletonList(_inetAddress.getHostAddress()), socket,
			Executors.newCachedThreadPool(new SocksProxyServerThreadFactory()));

		ReflectionTestUtil.invoke(
			socksProxyConnection, "_relayData",
			new Class<?>[] {
				InputStream.class, OutputStream.class, Socket.class, int.class
			},
			socket.getInputStream(), ubaos, socket, 16);

		String ubaosContent = ubaos.toString();

		Assert.assertFalse(ubaosContent.contains("Connection: Keep-Alive"));
	}

	private InetAddress _inetAddress;

}