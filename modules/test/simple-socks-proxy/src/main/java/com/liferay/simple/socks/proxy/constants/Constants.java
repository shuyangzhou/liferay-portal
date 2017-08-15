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

package com.liferay.simple.socks.proxy.constants;

/**
 * @author Tom Wang
 */
public interface Constants {

	public static final byte ATYP_DOMAIN_NAME = 0x03;

	public static final byte ATYP_IPV4 = 0x01;

	public static final byte ATYP_IPV6 = 0x04;

	public static final byte CMD_CONNECT = 0x01;

	public static final int MAX_ADDRESS_LENGTH = 255;

	public static final byte[] METHOD_SELECTION_ACCEPT = {0x05, (byte)0x00};

	public static final byte[] METHOD_SELECTION_REFUSE = {0x05, (byte)0xFF};

	public static final byte REP_INCORRECT_COMMAND = (byte)0xFF;

	public static final byte REP_UNSUPPORTED_ADDRESS_TYPE = 0x08;

	public static final byte REP_UNSUPPORTED_COMMAND = 0x07;

	public static final byte RSV = 0x00;

	public static final byte SOCKS5_VERSION = 0x05;

}