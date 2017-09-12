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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;

import java.io.IOException;

import java.net.InetAddress;

/**
 * @author Tom Wang
 */
public class SocksProxyRequest {

	public SocksProxyRequest(
		byte ver, byte cmd, byte rsv, byte atyp, byte[] dstAddr,
		byte[] dstPort) {

		_ver = ver;
		_cmd = cmd;
		_rsv = rsv;
		_atyp = atyp;
		_dstAddr = dstAddr;
		_dstPort = dstPort;
	}

	public int calculateServerPort() {
		return (0xFFFF & (_dstPort[0] << 8)) + (0xFF & _dstPort[1]);
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

	public String getHostAddress() throws IOException {
		if (_atyp == Constants.ATYP_IPV4) {
			StringBundler sb = new StringBundler(8);

			for (int i = 0; i < 4; i++) {
				sb.append(0xFF & _dstAddr[i]);
				sb.append(StringPool.PERIOD);
			}

			sb.setIndex(sb.index() - 1);

			return sb.toString();
		}
		else if (_atyp == Constants.ATYP_DOMAIN_NAME) {
			InetAddress inetAddress = InetAddress.getByName(
				new String(_dstAddr));

			return inetAddress.getHostAddress();
		}

		throw new IOException("Unsupported ATYPE: " + _atyp);
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