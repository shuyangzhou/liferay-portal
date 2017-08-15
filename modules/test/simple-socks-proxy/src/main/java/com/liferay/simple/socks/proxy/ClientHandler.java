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
import com.liferay.simple.socks.proxy.constants.Constants;
import com.liferay.simple.socks.proxy.utils.SocksProxyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Tom Wang
 */
public class ClientHandler implements Runnable {

	public ClientHandler(
		List<String> allowedIPAddress, Socket clientSocket,
		ExecutorService executorService) {

		_allowedIPAddress = allowedIPAddress;
		_clientSocket = clientSocket;
		_executorService = executorService;
	}

	@Override
	public void run() {
		try (InputStream clientInputStream = _clientSocket.getInputStream();
			OutputStream clientOutputStream = _clientSocket.getOutputStream();
			Socket serverSocket = _setUpServerConnection(
				clientInputStream, clientOutputStream);
			InputStream serverInputStream = serverSocket.getInputStream();
			OutputStream serverOutputStream = serverSocket.getOutputStream()) {

			Future<?> future = _executorService.submit(
				new Runnable() {

					@Override
					public void run() {
						_relayData(clientInputStream, serverOutputStream);
					}

				});

			_relayData(serverInputStream, clientOutputStream);

			future.get();
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
		finally {
			try {
				_clientSocket.close();
			}
			catch (IOException ioe) {
				if (_log.isWarnEnabled()) {
					_log.warn("Failed to close client socket", ioe);
				}
			}
		}
	}

	private void _authenticate(
			InputStream clientInputStream, OutputStream clientOutputStream)
		throws IOException {

		byte nMethodsByte = SocksProxyUtil.read(clientInputStream);

		boolean acceptFlag = false;

		List<Byte> methodsList = new ArrayList<>();

		for (int i = 0; i < nMethodsByte; i++) {
			byte readByte = SocksProxyUtil.read(clientInputStream);

			if (readByte == Constants.METHOD_NO_AUTHENTICATION_REQUIRED) {
				acceptFlag = true;
			}

			methodsList.add(readByte);
		}

		if (!acceptFlag) {
			_write(
				clientOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException(
				"No acceptable methods found, given methods are: " +
					methodsList.toString());
		}

		_write(
			clientOutputStream,
			Constants.METHOD_SELECTION_NO_AUTHENTICATION_REQUIRED);
	}

	private void _checkSocksVersion(
			InputStream clientInputStream, OutputStream clientOutputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(clientInputStream);

		if (verByte != Constants.SOCKS5_VERSION) {
			_write(
				clientOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException("Incorrect SOCKS version");
		}
	}

	private byte[] _createReply(byte replyCode, Socket serverSocket) {
		InetAddress inetAddress = serverSocket.getLocalAddress();

		byte[] ip = inetAddress.getAddress();

		int port = serverSocket.getLocalPort();

		byte[] reply = new byte[10];

		reply[0] = Constants.SOCKS5_VERSION;
		reply[1] = replyCode;
		reply[2] = Constants.RSV;
		reply[3] = Constants.ATYP_IPV4;
		reply[4] = ip[0];
		reply[5] = ip[1];
		reply[6] = ip[2];
		reply[7] = ip[3];
		reply[8] = (byte)((port & 0xFF00) >> 8);
		reply[9] = (byte)(port & 0x00FF);

		return reply;
	}

	private Socket _createServerSocket(Request requestDetails)
		throws IOException {

		String serverAddress = requestDetails.getHostAddress();

		if (!_allowedIPAddress.contains(serverAddress)) {
			throw new IOException(
				"Trying to access host not listed in allowedHostAddresses " +
					"property");
		}

		Socket serverSocket = new Socket(
			serverAddress, requestDetails.calculateServerPort());

		serverSocket.setSoTimeout(0);

		return serverSocket;
	}

	private Request _readRequest(InputStream clientInputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(clientInputStream);

		byte cmdByte = SocksProxyUtil.read(clientInputStream);

		byte rsvByte = SocksProxyUtil.read(clientInputStream);

		byte atypByte = SocksProxyUtil.read(clientInputStream);

		int addressLength = -1;

		switch (atypByte) {
			case Constants.ATYP_IPV4:
				addressLength = 4;
				break;
			case Constants.ATYP_DOMAIN_NAME:
				addressLength = SocksProxyUtil.read(clientInputStream);
				break;
			case Constants.ATYP_IPV6:
				addressLength = 16;
				break;
			default:
				throw new IOException("Invalid atype: " + atypByte);
		}

		byte[] dstAddrBytes = new byte[addressLength];

		SocksProxyUtil.readFully(clientInputStream, dstAddrBytes);

		byte[] dstPortBytes = new byte[2];

		SocksProxyUtil.readFully(clientInputStream, dstPortBytes);

		return new Request(
			verByte, cmdByte, rsvByte, atypByte, dstAddrBytes, dstPortBytes);
	}

	private void _relayData(
		InputStream inputStream, OutputStream outputStream) {

		int dataLength;

		byte[] bytesRead = new byte[4096];

		try {
			while ((dataLength = inputStream.read(bytesRead)) != -1) {
				outputStream.write(bytesRead, 0, dataLength);
				outputStream.flush();
			}
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe, ioe);
			}
		}
	}

	private Socket _setUpServerConnection(
			InputStream clientInputStream, OutputStream clientOutputStream)
		throws IOException {

		_checkSocksVersion(clientInputStream, clientOutputStream);

		_authenticate(clientInputStream, clientOutputStream);

		Request request = _readRequest(clientInputStream);

		Socket serverSocket = _createServerSocket(request);

		_validateRequest(request, serverSocket, clientOutputStream);

		return serverSocket;
	}

	private void _validateRequest(
			Request request, Socket serverSocket,
			OutputStream clientOutputStream)
		throws IOException {

		byte[] reply;

		if (request.getCmd() != Constants.CMD_CONNECT) {
			reply = _createReply(
				Constants.REP_UNSUPPORTED_COMMAND, serverSocket);

			_write(clientOutputStream, reply);

			throw new IOException(
				"Received unsupported command in the request");
		}

		if (request.getAtyp() >= Constants.ATYP_IPV6) {
			reply = _createReply(
				Constants.REP_UNSUPPORTED_ADDRESS_TYPE, serverSocket);

			_write(clientOutputStream, reply);

			throw new IOException(
				"Received unsupported address type in the request");
		}

		reply = _createReply(Constants.REP_SUCCEEDED, serverSocket);

		_write(clientOutputStream, reply);
	}

	private void _write(OutputStream outputStream, byte[] buffer)
		throws IOException {

		outputStream.write(buffer);
		outputStream.flush();
	}

	private static final Log _log = LogFactoryUtil.getLog(ClientHandler.class);

	private final List<String> _allowedIPAddress;
	private final Socket _clientSocket;
	private final ExecutorService _executorService;

}