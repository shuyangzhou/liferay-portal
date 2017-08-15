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
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.simple.socks.proxy.constants.Constants;
import com.liferay.simple.socks.proxy.utils.SocksProxyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import java.util.List;

/**
 * @author Tom Wang
 */
public class ClientHandler implements Runnable {

	public ClientHandler(
			List<String> allowedHosts, Socket clientSocket, int socketSOTimeout)
		throws SocketException {

		_allowedHosts = allowedHosts;
		_clientSocket = clientSocket;
		_socketSOTimeout = socketSOTimeout;

		_buffer = new byte[4096];
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
			_setUpServerConnection();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe.getMessage());
			}
		}

		_startRelay();

		_close();
	}

	private void _acceptAuthentication() {
		_replyToClient(Constants.METHOD_SELECTION_ACCEPT);
	}

	private boolean _checkAuthentication() throws IOException {
		byte ver = _getByteFromClient();

		if (ver != Constants.SOCKS5_VERSION) {
			_refuseAuthentication("Incorrect SOCKS version");
		}

		byte nMethods = _getByteFromClient();

		StringBundler sb = new StringBundler();

		for (int i = 0; i < nMethods; i++) {
			sb.append(",-");
			sb.append(_getByteFromClient());
			sb.append('-');
		}

		String methods = sb.toString();

		if (methods.contains("-0-") || methods.contains("-00-")) {
			return true;
		}

		return false;
	}

	private void _close() {
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

	private void _connectToServer(String server, int port) throws IOException {
		if (server.equals("")) {
			throw new IOException("Invalid remote host name");
		}

		if (!_allowedHosts.contains(server)) {
			throw new IOException(
				"Trying to access host not listed in allowedHosts property");
		}

		_serverSocket = new Socket(server, port);

		_serverSocket.setSoTimeout(_socketSOTimeout);

		_serverInputStream = _serverSocket.getInputStream();
		_serverOutputStream = _serverSocket.getOutputStream();
	}

	private byte _getByteFromClient() throws IOException {
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

	private RequestDetails _getRequestDetails() throws IOException {
		byte ver = _getByteFromClient();

		byte cmd = _getByteFromClient();

		byte rsv = _getByteFromClient();

		byte atyp = _getByteFromClient();

		byte[] dstAddr = new byte[Constants.MAX_ADDRESS_LENGTH];

		dstAddr[0] = _getByteFromClient();

		int addressLength = -1;

		switch (atyp) {
			case Constants.ATYP_IPV4:
				addressLength = 4;
				break;
			case Constants.ATYP_DOMAIN_NAME:
				addressLength = dstAddr[0] + 1;
				break;
			case Constants.ATYP_IPV6:
				addressLength = 16;
				break;
			default:
				break;
		}

		for (int i = 1; i < addressLength; i++) {
			dstAddr[i] = _getByteFromClient();
		}

		byte[] dstPort = new byte[2];

		dstPort[0] = _getByteFromClient();

		dstPort[1] = _getByteFromClient();

		RequestDetails requestDetails = new RequestDetails(
			ver, cmd, rsv, atyp, dstAddr, dstPort);

		return requestDetails;
	}

	private int _readInputStream(InputStream inputStream) {
		if (inputStream == null) {
			return -1;
		}

		int dataLength = 0;

		try {
			dataLength = inputStream.read(_buffer, 0, 4096);
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

	private void _refuseAuthentication(String message) throws IOException {
		_replyToClient(Constants.METHOD_SELECTION_REFUSE);

		throw new IOException(message);
	}

	private void _replyCommand(byte replyCode) {
		byte[] ip = new byte[4];
		int port = 0;

		if (_serverSocket != null) {
			InetAddress inetAddress = _serverSocket.getLocalAddress();

			ip = inetAddress.getAddress();

			port = _serverSocket.getLocalPort();
		}

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

		_replyToClient(reply);
	}

	private void _replyToClient(byte[] buffer) {
		_sendToOutputStream(_clientOutputStream, buffer, buffer.length);
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

	private void _setUpServerConnection() throws IOException {
		if (!_checkAuthentication()) {
			_refuseAuthentication("Not supported authentication");
		}

		_acceptAuthentication();

		RequestDetails requestDetails = _getRequestDetails();

		InetAddress serverIp = SocksProxyUtil.calculateInetAddress(
			requestDetails.getAtyp(), requestDetails.getDstAddr());

		_connectToServer(
			serverIp.getHostAddress(),
			SocksProxyUtil.calculateServerPort(requestDetails.getDstPort()));

		if (requestDetails.getVer() != Constants.SOCKS5_VERSION) {
			_replyCommand(Constants.REP_INCORRECT_COMMAND);
		}

		if (requestDetails.getCmd() != Constants.CMD_CONNECT) {
			_replyCommand(Constants.REP_UNSUPPORTED_COMMAND);
		}

		if (requestDetails.getAtyp() >= Constants.ATYP_IPV6) {
			_replyCommand(Constants.REP_UNSUPPORTED_ADDRESS_TYPE);
		}

		_replyCommand((byte)00);
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

	private static final Log _log = LogFactoryUtil.getLog(ClientHandler.class);

	private final List<String> _allowedHosts;
	private final byte[] _buffer;
	private InputStream _clientInputStream;
	private OutputStream _clientOutputStream;
	private Socket _clientSocket;
	private InputStream _serverInputStream;
	private OutputStream _serverOutputStream;
	private Socket _serverSocket;
	private final int _socketSOTimeout;

	private class RequestDetails {

		public RequestDetails(
			byte ver, byte cmd, byte rsv, byte atyp, byte[] dstAddr,
			byte[] dstPort) {

			_ver = ver;
			_cmd = cmd;
			_rsv = rsv;
			_atyp = atyp;
			_dstAddr = dstAddr;
			_dstPort = dstPort;
		}

		public byte getAtyp() {
			return _atyp;
		}

		public byte getCmd() {
			return _cmd;
		}

		public byte[] getDstAddr() {
			return _dstAddr;
		}

		public byte[] getDstPort() {
			return _dstPort;
		}

		public byte getVer() {
			return _ver;
		}

		private final byte _atyp;
		private final byte _cmd;
		private final byte[] _dstAddr;
		private final byte[] _dstPort;
		private final byte _rsv;
		private final byte _ver;

	}

}