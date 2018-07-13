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

package com.liferay.simple.socks.proxy.manager.test.util;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.util.InetAddressUtil;
import com.liferay.portal.kernel.util.SocketUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.nio.channels.ServerSocketChannel;

/**
 * @author Tom Wang
 */
public class SocksProxyTestUtil {

	public static String content;
	public static String contentWithoutKeepAlive;

	static {
		StringBundler sb = new StringBundler(9);

		sb.append(
			"POST /web/guest/blogs-page/-/blogs/trackback/test-1 HTTP/1.1\n");
		sb.append("User-Agent: Liferay Community Edition Portal / 7.0.4\n");
		sb.append(
			"Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n");
		sb.append("Content-Length: 126\r\n");
		sb.append("Host: www.able.com:8080\r\n");
		sb.append("Connection: Keep-Alive\r\n");
		sb.append("Accept-Encoding: gzip,deflate\r\n");
		sb.append("\r\n");
		sb.append(
			"excerpt=test&title=test&blog_name=Test+Test&url=http%3A%2F%2F");
		sb.append(
			"localhost%3A8080%2Fweb%2Fguest%2Fblogs-page%2F-%2Fblogs%2Fdsaf-");
		sb.append("37");

		content = sb.toString();

		contentWithoutKeepAlive = StringUtil.replace(
			content, "Connection: Keep-Alive\r\n", "");
	}

	public static Socket createTestSocket(byte[] bytes) {
		return createTestSocket(bytes, null, null);
	}

	public static Socket createTestSocket(
		byte[] bytes, final IOException ioe, final SocketException se) {

		final UnsyncByteArrayInputStream ubais = new UnsyncByteArrayInputStream(
			bytes);

		final UnsyncByteArrayOutputStream ubaos =
			new UnsyncByteArrayOutputStream();

		return new Socket() {

			@Override
			public synchronized void close() throws IOException {
				if (se != null) {
					throw se;
				}
			}

			@Override
			public InputStream getInputStream() {
				return ubais;
			}

			@Override
			public OutputStream getOutputStream() {
				return ubaos;
			}

			@Override
			public void shutdownOutput() throws IOException {
				if (ioe != null) {
					throw ioe;
				}
			}

		};
	}

	public static int findOpenPort(int startingPort) throws IOException {
		try (ServerSocketChannel serverSocketChannel =
				SocketUtil.createServerSocketChannel(
					InetAddressUtil.getLoopbackInetAddress(), startingPort,
					null);
			ServerSocket serverSocket = serverSocketChannel.socket()) {

			return serverSocket.getLocalPort();
		}
	}

	public static class TestRequestInput {

		public TestRequestInput(InetAddress inetAddress, int port)
			throws IOException {

			_inetAddressBytes = inetAddress.getAddress();
			_portBytes = new byte[] {(byte)(port >>> 8), (byte)port};

			byte[] bytes = new byte[7];

			bytes[0] = Constants.SOCKS5_VERSION;
			bytes[1] = 0x01;
			bytes[2] = Constants.METHOD_NO_AUTHENTICATION_REQUIRED;
			bytes[3] = Constants.SOCKS5_VERSION;
			bytes[4] = Constants.CMD_CONNECT;
			bytes[5] = Constants.RSV;
			bytes[6] = Constants.ATYP_IPV4;

			_standardRequest = bytes;
		}

		public byte[] getBytes() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos.write(_standardRequest);

			baos.write(_inetAddressBytes);

			baos.write(_portBytes);

			baos.write(_contentBytes);

			return baos.toByteArray();
		}

		public void setAType(byte b) {
			_standardRequest[6] = b;
		}

		public void setCMDByte(byte b) {
			_standardRequest[4] = b;
		}

		public void setContentBytes(byte[] bytes) {
			_contentBytes = bytes;
		}

		public void setDomain(String domain) throws IOException {
			_standardRequest[6] = Constants.ATYP_DOMAIN_NAME;

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			byte[] domainBytes = domain.getBytes();

			baos.write((byte)domainBytes.length);

			baos.write(domainBytes);

			_inetAddressBytes = baos.toByteArray();
		}

		public void setSocksVersion(byte b) {
			_standardRequest[0] = b;
		}

		private byte[] _contentBytes = new byte[0];
		private byte[] _inetAddressBytes;
		private final byte[] _portBytes;
		private final byte[] _standardRequest;

	}

}