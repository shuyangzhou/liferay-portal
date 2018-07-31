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

package com.liferay.simple.socks.proxy.manager.process.util;

import com.liferay.portal.kernel.util.KMPSearch;

/**
 * @author Tom Wang
 */
public class Constants {

	public static final byte ATYP_DOMAIN_NAME = 0x03;

	public static final byte ATYP_IPV4 = 0x01;

	public static final byte ATYP_IPV6 = 0x04;

	public static final byte CMD_CONNECT = 0x01;

	public static final int DEFAULT_BUFFER_SIZE = 4096;

	public static final int[] HEADER_SEPARATOR_NEXTS;

	public static final byte[] HEADER_SEPARATOR_PATTERN = "\r\n\r\n".getBytes();

	public static final int[] KEEP_ALIVE_NEXTS;

	public static final byte[] KEEP_ALIVE_PATTERN =
		"Connection: Keep-Alive".getBytes();

	public static final byte METHOD_NO_ACCEPTABLE_METHODS = (byte)0xFF;

	public static final byte METHOD_NO_AUTHENTICATION_REQUIRED = 0x00;

	public static final byte[] METHOD_SELECTION_NO_ACCEPTABLE_METHODS;

	public static final byte[] METHOD_SELECTION_NO_AUTHENTICATION_REQUIRED;

	public static final byte REP_INCORRECT_COMMAND = (byte)0xFF;

	public static final byte REP_SUCCEEDED = 0x00;

	public static final byte REP_UNSUPPORTED_ADDRESS_TYPE = 0x08;

	public static final byte REP_UNSUPPORTED_COMMAND = 0x07;

	public static final byte RSV = 0x00;

	public static final byte SOCKS5_VERSION = 0x05;

	static {
		HEADER_SEPARATOR_NEXTS = KMPSearch.generateNexts(
			HEADER_SEPARATOR_PATTERN);

		KEEP_ALIVE_NEXTS = KMPSearch.generateNexts(KEEP_ALIVE_PATTERN);

		METHOD_SELECTION_NO_ACCEPTABLE_METHODS =
			new byte[] {SOCKS5_VERSION, METHOD_NO_ACCEPTABLE_METHODS};

		METHOD_SELECTION_NO_AUTHENTICATION_REQUIRED =
			new byte[] {SOCKS5_VERSION, METHOD_NO_AUTHENTICATION_REQUIRED};
	}

}