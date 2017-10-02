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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tom Wang
 */
public class SocksProxyUtil {

	public static byte read(InputStream inputStream) throws IOException {
		int value = inputStream.read();

		if (value == -1) {
			throw new EOFException();
		}

		return (byte)value;
	}

	public static void readFully(InputStream inputStream, byte[] data)
		throws IOException {

		int n = 0;

		while (n < data.length) {
			int count = inputStream.read(data, n, data.length - n);

			if (count < 0) {
				throw new EOFException();
			}

			n += count;
		}
	}

}