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
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.KMPSearch;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;
import com.liferay.simple.socks.proxy.manager.process.util.SocksProxyUtil;

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
public class SocksProxyConnection implements Runnable {

	public SocksProxyConnection(
		List<String> allowedIPAddresses, Socket internalSocket,
		ExecutorService executorService) {

		_allowedIPAddresses = allowedIPAddresses;
		_internalSocket = internalSocket;
		_executorService = executorService;
	}

	@Override
	public void run() {
		try {
			InputStream internalInputStream = _internalSocket.getInputStream();
			OutputStream internalOutputStream =
				_internalSocket.getOutputStream();

			Socket externalSocket = _setUpExternalConnection(
				internalInputStream, internalOutputStream);

			try {
				InputStream externalInputStream =
					externalSocket.getInputStream();
				OutputStream externalOutputStream =
					externalSocket.getOutputStream();

				Future<?> future = _executorService.submit(
					new Runnable() {

						@Override
						public void run() {
							_relayData(
								internalInputStream, externalOutputStream,
								externalSocket, Constants.DEFAULT_BUFFER_SIZE);
						}

					});

				_relayData(
					externalInputStream, internalOutputStream, _internalSocket,
					Constants.DEFAULT_BUFFER_SIZE);

				future.get();
			}
			finally {
				externalSocket.close();
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
		finally {
			try {
				_internalSocket.close();
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
			InputStream internalInputStream, OutputStream internalOutputStream)
		throws IOException {

		byte nMethodsByte = SocksProxyUtil.read(internalInputStream);

		boolean acceptFlag = false;

		List<Byte> methodsList = new ArrayList<>();

		for (int i = 0; i < nMethodsByte; i++) {
			byte readByte = SocksProxyUtil.read(internalInputStream);

			if (readByte == Constants.METHOD_NO_AUTHENTICATION_REQUIRED) {
				acceptFlag = true;
			}

			methodsList.add(readByte);
		}

		if (!acceptFlag) {
			_write(
				internalOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException(
				"No acceptable methods found, given methods are: " +
					methodsList.toString());
		}

		_write(
			internalOutputStream,
			Constants.METHOD_SELECTION_NO_AUTHENTICATION_REQUIRED);
	}

	private void _checkSocksVersion(
			InputStream internalInputStream, OutputStream internalOutputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(internalInputStream);

		if (verByte != Constants.SOCKS5_VERSION) {
			_write(
				internalOutputStream,
				Constants.METHOD_SELECTION_NO_ACCEPTABLE_METHODS);

			throw new IOException("Incorrect SOCKS version");
		}
	}

	private Socket _createExternalSocket(SocksProxyRequest request)
		throws IOException {

		String remoteAddress = request.getHostAddress();

		if (!_allowedIPAddresses.contains(remoteAddress)) {
			throw new IOException(
				"Trying to access host (" + remoteAddress + ") not listed in " +
					"allowedIPAddresses property");
		}

		Socket externalSocket = new Socket(
			remoteAddress, request.calculateServerPort());

		externalSocket.setSoTimeout(0);

		return externalSocket;
	}

	private byte[] _createReply(byte replyCode, Socket externalSocket) {
		InetAddress inetAddress = externalSocket.getLocalAddress();

		byte[] ip = inetAddress.getAddress();

		int port = externalSocket.getLocalPort();

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

	private SocksProxyRequest _readRequest(InputStream internalInputStream)
		throws IOException {

		byte verByte = SocksProxyUtil.read(internalInputStream);

		byte cmdByte = SocksProxyUtil.read(internalInputStream);

		byte rsvByte = SocksProxyUtil.read(internalInputStream);

		byte atypByte = SocksProxyUtil.read(internalInputStream);

		int addressLength = -1;

		switch (atypByte) {
			case Constants.ATYP_IPV4:
				addressLength = 4;
				break;
			case Constants.ATYP_DOMAIN_NAME:
				addressLength = SocksProxyUtil.read(internalInputStream);
				break;
			default:
				throw new IOException("Invalid atype: " + atypByte);
		}

		byte[] dstAddrBytes = new byte[addressLength];

		SocksProxyUtil.readFully(internalInputStream, dstAddrBytes);

		byte[] dstPortBytes = new byte[2];

		SocksProxyUtil.readFully(internalInputStream, dstPortBytes);

		return new SocksProxyRequest(
			verByte, cmdByte, rsvByte, atypByte, dstAddrBytes, dstPortBytes);
	}

	private void _relayData(
		InputStream inputStream, OutputStream outputStream, Socket socket,
		int bufferSize) {

		UnsyncByteArrayOutputStream ubaos = new UnsyncByteArrayOutputStream();

		int length = 0;

		byte[] buffer = new byte[bufferSize];

		try {
			while ((length = inputStream.read(buffer)) != -1) {
				if (ubaos == null) {
					outputStream.write(buffer, 0, length);
				}
				else {
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

	private Socket _setUpExternalConnection(
			InputStream internalInputStream, OutputStream internalOutputStream)
		throws IOException {

		_checkSocksVersion(internalInputStream, internalOutputStream);

		_authenticate(internalInputStream, internalOutputStream);

		SocksProxyRequest request = _readRequest(internalInputStream);

		Socket externalSocket = _createExternalSocket(request);

		_validateRequest(request, externalSocket, internalOutputStream);

		return externalSocket;
	}

	private void _validateRequest(
			SocksProxyRequest request, Socket externalSocket,
			OutputStream internalOutputStream)
		throws IOException {

		if (request.getCmd() != Constants.CMD_CONNECT) {
			byte[] reply = _createReply(
				Constants.REP_UNSUPPORTED_COMMAND, externalSocket);

			_write(internalOutputStream, reply);

			throw new IOException(
				"Received unsupported command in the request");
		}

		byte[] reply = _createReply(Constants.REP_SUCCEEDED, externalSocket);

		_write(internalOutputStream, reply);
	}

	private void _write(OutputStream outputStream, byte[] buffer)
		throws IOException {

		outputStream.write(buffer);
		outputStream.flush();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyConnection.class);

	private final List<String> _allowedIPAddresses;
	private final ExecutorService _executorService;
	private final Socket _internalSocket;

}