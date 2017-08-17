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
import java.io.OutputStream;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import java.util.List;

/**
 * @author Tom Wang
 */
public class ClientHandler implements Runnable {

	public ClientHandler(List<String> allowedHosts, Socket clientSocket)
		throws SocketException {

		_allowedHosts = allowedHosts;
		_clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try (InputStream clientInputStream = _clientSocket.getInputStream();
			OutputStream clientOutputStream = _clientSocket.getOutputStream();
			Socket serverSocket = _setUpServerConnection(
				clientInputStream, clientOutputStream);
			InputStream serverInputStream = serverSocket.getInputStream();
			OutputStream serverOutputStream = serverSocket.getOutputStream()) {

			_startRelay(
				clientInputStream, clientOutputStream, serverInputStream,
				serverOutputStream);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}

		try {
			_clientSocket.close();
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("Failed to close client socket", ioe);
			}
		}
	}

	private boolean _checkAuthentication(
			InputStream clientInputStream, OutputStream clientOutputStream)
		throws IOException {

		byte verByte = (byte)clientInputStream.read();

		if (verByte != Constants.SOCKS5_VERSION) {
			_sendToOutputStream(
				clientOutputStream, Constants.METHOD_SELECTION_REFUSE,
				Constants.METHOD_SELECTION_REFUSE.length);

			throw new IOException("Incorrect SOCKS version");
		}

		byte nMethodsByte = (byte)clientInputStream.read();

		StringBundler sb = new StringBundler();

		for (int i = 0; i < nMethodsByte; i++) {
			sb.append(",-");
			sb.append((byte)clientInputStream.read());
			sb.append('-');
		}

		String methods = sb.toString();

		if (methods.contains("-0-") || methods.contains("-00-")) {
			return true;
		}

		return false;
	}

	private byte[] _getReplyByteArray(byte replyCode, Socket serverSocket) {
		byte[] ip = new byte[4];
		int port = 0;

		if (serverSocket != null) {
			InetAddress inetAddress = serverSocket.getLocalAddress();

			ip = inetAddress.getAddress();

			port = serverSocket.getLocalPort();
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

		return reply;
	}

	private RequestDetails _getRequestDetails(InputStream clientInputStream)
		throws IOException {

		byte verByte = (byte)clientInputStream.read();

		byte cmdByte = (byte)clientInputStream.read();

		byte rsvByte = (byte)clientInputStream.read();

		byte atypByte = (byte)clientInputStream.read();

		byte[] dstAddrBytes = new byte[Constants.MAX_ADDRESS_LENGTH];

		dstAddrBytes[0] = (byte)clientInputStream.read();

		int addressLength = -1;

		switch (atypByte) {
			case Constants.ATYP_IPV4:
				addressLength = 4;
				break;
			case Constants.ATYP_DOMAIN_NAME:
				addressLength = dstAddrBytes[0] + 1;
				break;
			case Constants.ATYP_IPV6:
				addressLength = 16;
				break;
			default:
				break;
		}

		for (int i = 1; i < addressLength; i++) {
			dstAddrBytes[i] = (byte)clientInputStream.read();
		}

		byte[] dstPortBytes = new byte[2];

		dstPortBytes[0] = (byte)clientInputStream.read();

		dstPortBytes[1] = (byte)clientInputStream.read();

		return new RequestDetails(
			verByte, cmdByte, rsvByte, atypByte, dstAddrBytes, dstPortBytes);
	}

	private Socket _getServerSocket(RequestDetails requestDetails)
		throws IOException {

		InetAddress inetAddress = SocksProxyUtil.calculateInetAddress(
			requestDetails.getAtyp(), requestDetails.getDstAddr());

		String server = inetAddress.getHostAddress();

		if (server.equals("")) {
			throw new IOException("Invalid remote host name");
		}

		if (!_allowedHosts.contains(server)) {
			throw new IOException(
				"Trying to access host not listed in allowedHosts property");
		}

		int port = SocksProxyUtil.calculateServerPort(
			requestDetails.getDstPort());

		Socket serverSocket = new Socket(server, port);

		serverSocket.setSoTimeout(0);

		return serverSocket;
	}

	private void _relayData(
		InputStream inputStream, OutputStream outputStream) {

		int dataLength;

		byte[] bytesRead = new byte[4096];

		try {
			while ((dataLength = inputStream.read(bytesRead, 0, 4096)) != -1) {
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

	private void _sendToOutputStream(
			OutputStream outputStream, byte[] buffer, int length)
		throws IOException {

		outputStream.write(buffer, 0, length);
		outputStream.flush();
	}

	private Socket _setUpServerConnection(
			InputStream clientInputStream, OutputStream clientOutputStream)
		throws IOException {

		if (!_checkAuthentication(clientInputStream, clientOutputStream)) {
			_sendToOutputStream(
				clientOutputStream, Constants.METHOD_SELECTION_REFUSE,
				Constants.METHOD_SELECTION_REFUSE.length);

			throw new IOException("Unsupported authentication");
		}

		_sendToOutputStream(
			clientOutputStream, Constants.METHOD_SELECTION_ACCEPT,
			Constants.METHOD_SELECTION_ACCEPT.length);

		RequestDetails requestDetails = _getRequestDetails(clientInputStream);

		Socket serverSocket = _getServerSocket(requestDetails);

		byte[] reply;

		if (requestDetails.getVer() != Constants.SOCKS5_VERSION) {
			reply = _getReplyByteArray(
				Constants.REP_INCORRECT_COMMAND, serverSocket);

			_sendToOutputStream(clientOutputStream, reply, reply.length);
		}

		if (requestDetails.getCmd() != Constants.CMD_CONNECT) {
			reply = _getReplyByteArray(
				Constants.REP_UNSUPPORTED_COMMAND, serverSocket);

			_sendToOutputStream(clientOutputStream, reply, reply.length);
		}

		if (requestDetails.getAtyp() >= Constants.ATYP_IPV6) {
			reply = _getReplyByteArray(
				Constants.REP_UNSUPPORTED_ADDRESS_TYPE, serverSocket);

			_sendToOutputStream(clientOutputStream, reply, reply.length);
		}

		reply = _getReplyByteArray(Constants.REP_SUCCEEDED, serverSocket);

		_sendToOutputStream(clientOutputStream, reply, reply.length);

		return serverSocket;
	}

	private void _startRelay(
			final InputStream clientInputStream,
			final OutputStream clientOutputStream,
			final InputStream serverInputStream,
			final OutputStream serverOutputStream)
		throws Exception {

		Thread clientToServerThread = new Thread(
			new Runnable() {

				@Override
				public void run() {
					_relayData(clientInputStream, serverOutputStream);
				}

			},
			"ClientToServerThread");

		clientToServerThread.start();

		_relayData(serverInputStream, clientOutputStream);

		clientToServerThread.join();
	}

	private static final Log _log = LogFactoryUtil.getLog(ClientHandler.class);

	private final List<String> _allowedHosts;
	private final Socket _clientSocket;

	private class RequestDetails {

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

		private RequestDetails(
			byte ver, byte cmd, byte rsv, byte atyp, byte[] dstAddr,
			byte[] dstPort) {

			_ver = ver;
			_cmd = cmd;
			_rsv = rsv;
			_atyp = atyp;
			_dstAddr = dstAddr;
			_dstPort = dstPort;
		}

		private final byte _atyp;
		private final byte _cmd;
		private final byte[] _dstAddr;
		private final byte[] _dstPort;
		private final byte _rsv;
		private final byte _ver;

	}

}