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

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.KMPSearch;
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
		List<String> allowedIPAddress, Socket portalSocket,
		ExecutorService executorService) {

		_allowedIPAddress = allowedIPAddress;
		_portalSocket = portalSocket;
		_executorService = executorService;
	}

	@Override
	public void run() {
		try (InputStream portalInputStream = _portalSocket.getInputStream();
			OutputStream portalOutputStream = _portalSocket.getOutputStream();
			Socket remoteSocket = _setUpRemoteConnection(
				portalInputStream, portalOutputStream);
			InputStream remoteInputStream = remoteSocket.getInputStream();
			OutputStream remoteOutputStream = remoteSocket.getOutputStream()) {

			Future<?> future = _executorService.submit(
				new Runnable() {

					@Override
					public void run() {
						_relayData(
							portalInputStream, remoteOutputStream,
							remoteSocket);
					}

				});

			_relayData(remoteInputStream, portalOutputStream, _portalSocket);

			if (!future.isDone()) {
				future.get();
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
		finally {
			try {
				_portalSocket.close();
			}
			catch (IOException ioe) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Failed to close socket between proxy and remote", ioe);
				}
			}
		}
	}

	private void _authenticate(
			InputStream portalInputStream, OutputStream portalOutputStream)
		throws IOException {

		byte nMethodsByte = SocksProxyUtil.read(portalInputStream);

		boolean acceptFlag = false;

		List<Byte> methodsList = new ArrayList<>();

		for (int i = 0; i < nMethodsByte; i++) {
			byte readByte = SocksProxyUtil.read(portalInputStream);

			if (readByte == Constants.METHOD_NO_AUTHENTICATION_REQUIRED) {
				acceptFlag = true;
			}

			methodsList.add(readByte);
		}

		if (!acceptFlag) {
			_write(
				portalOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException(
				"No acceptable methods found, given methods are: " +
					methodsList.toString());
		}

		_write(
			portalOutputStream,
			Constants.METHOD_SELECTION_NO_AUTHENTICATION_REQUIRED);
	}

	private void _checkSocksVersion(
			InputStream portalInputStream, OutputStream portalOutputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(portalInputStream);

		if (verByte != Constants.SOCKS5_VERSION) {
			_write(
				portalOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException("Incorrect SOCKS version");
		}
	}

	private Socket _createRemoteSocket(Request requestDetails)
		throws IOException {

		String remoteAddress = requestDetails.getHostAddress();

		if (!_allowedIPAddress.contains(remoteAddress)) {
			throw new IOException(
				"Trying to access host not listed in allowedHostAddresses " +
					"property");
		}

		Socket remoteSocket = new Socket(
			remoteAddress, requestDetails.calculateServerPort());

		remoteSocket.setSoTimeout(0);

		return remoteSocket;
	}

	private byte[] _createReply(byte replyCode, Socket remoteSocket) {
		InetAddress inetAddress = remoteSocket.getLocalAddress();

		byte[] ip = inetAddress.getAddress();

		int port = remoteSocket.getLocalPort();

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

	private Request _readRequest(InputStream portalInputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(portalInputStream);

		byte cmdByte = SocksProxyUtil.read(portalInputStream);

		byte rsvByte = SocksProxyUtil.read(portalInputStream);

		byte atypByte = SocksProxyUtil.read(portalInputStream);

		int addressLength = -1;

		switch (atypByte) {
			case Constants.ATYP_IPV4:
				addressLength = 4;
				break;
			case Constants.ATYP_DOMAIN_NAME:
				addressLength = SocksProxyUtil.read(portalInputStream);
				break;
			case Constants.ATYP_IPV6:
				addressLength = 16;
				break;
			default:
				throw new IOException("Invalid atype: " + atypByte);
		}

		byte[] dstAddrBytes = new byte[addressLength];

		SocksProxyUtil.readFully(portalInputStream, dstAddrBytes);

		byte[] dstPortBytes = new byte[2];

		SocksProxyUtil.readFully(portalInputStream, dstPortBytes);

		return new Request(
			verByte, cmdByte, rsvByte, atypByte, dstAddrBytes, dstPortBytes);
	}

	private void _relayData(
		InputStream inputStream, OutputStream outputStream, Socket socket) {

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		int length;

		byte[] buffer = new byte[4096];

		try {
			while ((length = inputStream.read(buffer)) != -1) {
				if (ubaos != null) {
					ubaos.write(buffer, 0, length);

					int index = KMPSearch.search(
						ubaos.unsafeGetByteArray(), 0, ubaos.size(),
						Constants.HEADER_SEPARATOR_PATTERN,
						Constants.HEADER_SEPARATOR_NEXTS);

					if (index == -1) {
						continue;
					}

					index = KMPSearch.search(
						ubaos.unsafeGetByteArray(), 0, ubaos.size(),
						Constants.KEEP_ALIVE_PATTERN,
						Constants.KEEP_ALIVE_NEXTS);

					if (index == -1) {
						ubaos.writeTo(outputStream);
					}
					else {
						outputStream.write(
							ubaos.unsafeGetByteArray(), 0, index);

						int offset =
							index + Constants.KEEP_ALIVE_PATTERN.length + 2;

						outputStream.write(
							ubaos.unsafeGetByteArray(), offset,
							ubaos.size() - offset);
					}

					ubaos = null;
				}
				else {
					outputStream.write(buffer, 0, length);
				}
			}

			outputStream.flush();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe, ioe);
			}
		}
		finally {
			try {
				socket.shutdownOutput();
			}
			catch (IOException ioe) {
				if (_log.isWarnEnabled()) {
					_log.warn("Error during socket.shutdownOutput", ioe);
				}
			}
		}
	}

	private Socket _setUpRemoteConnection(
			InputStream portalInputStream, OutputStream portalOutputStream)
		throws IOException {

		_checkSocksVersion(portalInputStream, portalOutputStream);

		_authenticate(portalInputStream, portalOutputStream);

		Request request = _readRequest(portalInputStream);

		Socket remoteSocket = _createRemoteSocket(request);

		_validateRequest(request, remoteSocket, portalOutputStream);

		return remoteSocket;
	}

	private void _validateRequest(
			Request request, Socket remoteSocket,
			OutputStream portalOutputStream)
		throws IOException {

		byte[] reply;

		if (request.getCmd() != Constants.CMD_CONNECT) {
			reply = _createReply(
				Constants.REP_UNSUPPORTED_COMMAND, remoteSocket);

			_write(portalOutputStream, reply);

			throw new IOException(
				"Received unsupported command in the request");
		}

		if (request.getAtyp() >= Constants.ATYP_IPV6) {
			reply = _createReply(
				Constants.REP_UNSUPPORTED_ADDRESS_TYPE, remoteSocket);

			_write(portalOutputStream, reply);

			throw new IOException(
				"Received unsupported address type in the request");
		}

		reply = _createReply(Constants.REP_SUCCEEDED, remoteSocket);

		_write(portalOutputStream, reply);
	}

	private void _write(OutputStream outputStream, byte[] buffer)
		throws IOException {

		outputStream.write(buffer);
		outputStream.flush();
	}

	private static final Log _log = LogFactoryUtil.getLog(ClientHandler.class);

	private final List<String> _allowedIPAddress;
	private final ExecutorService _executorService;
	private final Socket _portalSocket;

}