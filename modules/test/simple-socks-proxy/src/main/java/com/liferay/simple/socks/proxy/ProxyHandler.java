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

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import java.net.Socket;
import java.net.SocketException;

/**
 * @author Tom Wang
 */
public class ProxyHandler implements Runnable {

	public ProxyHandler(Socket clientSocket) throws SocketException {
		_clientSocket = clientSocket;

		try {
			_clientSocket.setSoTimeout(10);
		}
		catch (SocketException se) {
			throw new SocketException("Error setting clientSocket timeout");
		}

		_buffer = new byte[_DEFAULT_BUF_SIZE];
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
			return;
		}

		try {
			_setUpRelay();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe.getMessage());
			}
		}

		close();
	}

	protected void connectToServer(String server, int port) throws IOException {
		if (server.equals("")) {
			throw new IOException("Invalid remote host name");
		}

		if (!server.equals("127.0.0.1")) {
			throw new IOException("Trying to access outside IP!");
		}

		_serverSocket = new Socket(server, port);

		_serverSocket.setSoTimeout(10);

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
			dataLength = inputStream.read(_buffer, 0, _DEFAULT_BUF_SIZE);
		}
		catch (InterruptedIOException iioe) {
			return 0;
		}
		catch (IOException ioe) {
			return -1;
		}

		return dataLength;
	}

	private void _relay() {
		boolean active = true;

		int dataLength = 0;

		while (active) {
			dataLength = _readInputStream(_clientInputStream);

			if (dataLength < 0) {
				active = false;
			}

			if (dataLength > 0) {
				_sendToOutputStream(_serverOutputStream, _buffer, dataLength);
			}

			dataLength = _readInputStream(_serverInputStream);

			if (dataLength < 0) {
				active = false;
			}

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
			System.err.println("unable to send to outputstream");
		}
	}

	private void _setUpRelay() throws IOException {
		byte socksVersion = getByteFromClient();

		Socks5Impl socks5Impl = new Socks5Impl(this);

		socks5Impl.authenticate(socksVersion);

		socks5Impl.getClientCommandAndConnectToServer();

		_relay();
	}

	private static final int _DEFAULT_BUF_SIZE = 4096;

	private static final Log _log = LogFactoryUtil.getLog(ProxyHandler.class);

	private final byte[] _buffer;
	private InputStream _clientInputStream;
	private OutputStream _clientOutputStream;
	private Socket _clientSocket;
	private InputStream _serverInputStream;
	private OutputStream _serverOutputStream;
	private Socket _serverSocket;

}