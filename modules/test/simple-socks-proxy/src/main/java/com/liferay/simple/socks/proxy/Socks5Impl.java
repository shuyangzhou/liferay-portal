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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.simple.socks.proxy.constants.Constants;

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Tom Wang
 */
public class Socks5Impl {

	public Socks5Impl(SocksHandler proxyHandler) {
		_proxyHandler = proxyHandler;
	}

	protected void authenticate(byte socksVersion) throws IOException {
		if (socksVersion == Constants.SOCKS5_VERSION) {
			if (!_checkAuthentication()) {
				_refuseAuthentication("Not supported authentication");
			}

			_acceptAuthentication();
		}
		else {
			_refuseAuthentication("Incorrect SOCKS version");
		}
	}

	protected void getClientCommandAndConnectToServer() throws IOException {
		byte socksVersion = _getByte();

		byte socksCommand = _getByte();

		byte rsv = _getByte();

		byte atyp = _getByte();

		byte[] dstAddr = new byte[Constants.MAX_ADDRESS_LENGTH];

		dstAddr[0] = _getByte();

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
			dstAddr[i] = _getByte();
		}

		byte[] dstPort = new byte[2];

		dstPort[0] = _getByte();

		dstPort[1] = _getByte();

		_connectToServerSocket(atyp, dstAddr, dstPort);

		if (socksVersion != Constants.SOCKS5_VERSION) {
			_replyCommand(Constants.REP_INCORRECT_COMMAND);
		}

		if (socksCommand != Constants.CMD_CONNECT) {
			_replyCommand(Constants.REP_UNSUPPORTED_COMMAND);
		}

		if (atyp >= Constants.ATYP_IPV6) {
			_replyCommand(Constants.REP_UNSUPPORTED_ADDRESS_TYPE);
		}
	}

	private void _acceptAuthentication() {
		_proxyHandler.replyToClient(Constants.METHOD_SELECTION_ACCEPT);
	}

	private int _byteToInt(byte b) {
		int res = b;

		if (res < 0) {
			res = (int)(0x100 + res);
		}

		return res;
	}

	private InetAddress _calculateInetAddress(byte atyp, byte[] dstAddr)
		throws IOException {

		if (atyp == Constants.ATYP_IPV4) {
			if (dstAddr.length < 4) {
				throw new IOException("Invalid length of IPv4 Address");
			}

			StringBundler sb = new StringBundler();

			for (int i = 0; i < 4; i++) {
				sb.append(_byteToInt(dstAddr[i]));

				if (i < 3) {
					sb.append('.');
				}
			}

			return InetAddress.getByName(sb.toString());
		}
		else if (atyp == Constants.ATYP_DOMAIN_NAME) {
			if (dstAddr[0] <= 0) {
				throw new IOException("Bad IP size");
			}

			StringBundler sb = new StringBundler();

			for (int i = 1; i <= dstAddr[0]; i++) {
				sb.append((char)dstAddr[i]);
			}

			return InetAddress.getByName(sb.toString());
		}

		throw new IOException("Unsupported ATYPE: " + atyp);
	}

	private boolean _checkAuthentication() {
		byte nMethods = _getByte();

		StringBundler sb = new StringBundler();

		for (int i = 0; i < nMethods; i++) {
			sb.append(",-");
			sb.append(_getByte());
			sb.append('-');
		}

		String methods = sb.toString();

		if (methods.contains("-0-") || methods.contains("-00-")) {
			return true;
		}

		return false;
	}

	private void _connectToServerSocket(
			byte atyp, byte[] dstAddr, byte[] dstPort)
		throws IOException {

		InetAddress serverIp = _calculateInetAddress(atyp, dstAddr);
		int serverPort =
			(_byteToInt(dstPort[0]) << 8) | (_byteToInt(dstPort[1]));

		_proxyHandler.connectToServer(serverIp.getHostAddress(), serverPort);

		_replyCommand((byte)00);
	}

	private byte _getByte() {
		byte b;

		try {
			b = _proxyHandler.getByteFromClient();
		}
		catch (IOException ioe) {
			b = 0;
		}

		return b;
	}

	private void _refuseAuthentication(String message) throws IOException {
		_proxyHandler.replyToClient(Constants.METHOD_SELECTION_REFUSE);

		throw new IOException(message);
	}

	private void _replyCommand(byte replyCode) {
		Socket serverSocket = _proxyHandler.getServerSocket();

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

		_proxyHandler.replyToClient(reply);
	}

	private final SocksHandler _proxyHandler;

}