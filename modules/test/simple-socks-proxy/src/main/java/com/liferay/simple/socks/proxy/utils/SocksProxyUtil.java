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

package com.liferay.simple.socks.proxy.utils;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.simple.socks.proxy.constants.Constants;

import java.io.IOException;

import java.net.InetAddress;

/**
 * @author Tom Wang
 */
public class SocksProxyUtil {

	public static int byteToInt(byte b) {
		int result = b;

		if (result < 0) {
			result = (int)(0x100 + result);
		}

		return result;
	}

	public static InetAddress calculateInetAddress(byte atyp, byte[] dstAddr)
		throws IOException {

		if (atyp == Constants.ATYP_IPV4) {
			if (dstAddr.length < 4) {
				throw new IOException("Invalid length of IPv4 Address");
			}

			StringBundler sb = new StringBundler();

			for (int i = 0; i < 4; i++) {
				sb.append(byteToInt(dstAddr[i]));

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

	public static int calculateServerPort(byte[] dstPort) {
		return (byteToInt(dstPort[0]) << 8) | (byteToInt(dstPort[1]));
	}

}