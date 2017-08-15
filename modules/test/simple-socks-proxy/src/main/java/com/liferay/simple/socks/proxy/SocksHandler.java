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

package com.liferay.simple.socks.proxy;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.simple.socks.proxy.configuration.SocksProxyConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import java.net.Socket;
import java.net.SocketException;

import java.util.List;

/**
 * @author Tom Wang
 */
public class SocksHandler implements Runnable {

	public SocksHandler(
			Socket clientSocket,
			SocksProxyConfiguration socketProxyConfiguration)
		throws SocketException {

		_configuration = socketProxyConfiguration;
		_clientSocket = clientSocket;

		try {
			_clientSocket.setSoTimeout(_configuration.socketTimeout());
		}
		catch (SocketException se) {
			if (_log.isWarnEnabled()) {
				_log.warn("Error setting clientSocket timeout");
			}
			throw se;
		}

		_buffer = new byte[_configuration.bufferSize()];
	}

	public void close() {
		try {
			if (_clientOutputStream != null) {
				_clientOutputStream.flush();
				_clientOutputStream.close();
			}

			if (_serverOutputStream != null) {
				_serverOutputStream.flush();
				_serverOutputStream.close();
			}

			if (_clientSocket != null) {
				_clientSocket.close();
			}

			if (_serverSocket != null) {
				_serverSocket.close();
			}
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe.getMessage());
			}
		}

		_serverSocket = null;
		_clientSocket = null;
	}

	@Override
	public void run() {
		if (_clientSocket == null) {
			return;
		}

		try {
			_clientInputStream = _clientSocket.getInputStream();
			_clientOutputStream = _clientSocket.getOutputStream();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("failed to get stream from client socket", ioe);
			}

			return;
		}

		try {
			_setUpSocksConnection();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe.getMessage());
			}
		}

		_startRelay();

		close();
	}

	protected void connectToServer(String server, int port) throws IOException {
		if (server.equals("")) {
			throw new IOException("Invalid remote host name");
		}

		List<String> allowedHosts = _configuration.allowedHosts();

		if (!allowedHosts.contains(server)) {
			throw new IOException("Trying to access outside IP!");
		}

		_serverSocket = new Socket(server, port);

		_serverSocket.setSoTimeout(_configuration.socketTimeout());

		_serverInputStream = _serverSocket.getInputStream();
		_serverOutputStream = _serverSocket.getOutputStream();
	}

	protected byte getByteFromClient() throws IOException {
		int b;

		while (_clientSocket != null) {
			try {
				b = _clientInputStream.read();
			}
			catch (InterruptedIOException iioe) {
				if (_log.isInfoEnabled()) {
					_log.info("Waiting for client input stream", iioe);
				}

				continue;
			}

			return (byte)b;
		}

		throw new IOException("Unable to read byte from client");
	}

	protected Socket getServerSocket() {
		return _serverSocket;
	}

	protected void replyToClient(byte[] buffer) {
		_sendToOutputStream(_clientOutputStream, buffer, buffer.length);
	}

	private int _readInputStream(InputStream inputStream) {
		if (inputStream == null) {
			return -1;
		}

		int dataLength = 0;

		try {
			dataLength = inputStream.read(
				_buffer, 0, _configuration.bufferSize());
		}
		catch (InterruptedIOException iioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("InputStream reading interrupted", iioe);
			}

			return 0;
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("InputStream reading failed", ioe);
			}

			return -1;
		}

		return dataLength;
	}

	private void _startRelay() {
		int dataLength = 0;

		while (dataLength >= 0) {
			dataLength = _readInputStream(_clientInputStream);

			if (dataLength > 0) {
				_sendToOutputStream(_serverOutputStream, _buffer, dataLength);
			}

			dataLength = _readInputStream(_serverInputStream);

			if (dataLength > 0) {
				_sendToOutputStream(_clientOutputStream, _buffer, dataLength);
			}
		}
	}

	private void _sendToOutputStream(
		OutputStream outputStream, byte[] buffer, int length) {

		if (outputStream == null) {
			return;
		}

		if ((length <= 0) || (length > buffer.length)) {
			return;
		}

		try {
			outputStream.write(buffer, 0, length);
			outputStream.flush();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("Failed to write to output stream", ioe);
			}
		}
	}

	private void _setUpSocksConnection() throws IOException {
		byte socksVersion = getByteFromClient();

		Socks5Impl socks5Impl = new Socks5Impl(this);

		socks5Impl.authenticate(socksVersion);

		socks5Impl.getClientCommandAndConnectToServer();
	}

	private static final Log _log = LogFactoryUtil.getLog(SocksHandler.class);

	private final byte[] _buffer;
	private InputStream _clientInputStream;
	private OutputStream _clientOutputStream;
	private Socket _clientSocket;
	private final SocksProxyConfiguration _configuration;
	private InputStream _serverInputStream;
	private OutputStream _serverOutputStream;
	private Socket _serverSocket;

}