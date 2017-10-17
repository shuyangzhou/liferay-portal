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

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testConstructor() {
		new SocksProxyUtil();
	}

	@Test(expected = EOFException.class)
	public void testRead() throws IOException {
		InputStream inputStream = new UnsyncByteArrayInputStream(
			new byte[] {Constants.SOCKS5_VERSION});

		byte value = SocksProxyUtil.read(inputStream);

		Assert.assertEquals(Constants.SOCKS5_VERSION, value);

		SocksProxyUtil.read(inputStream);
	}

	@Test(expected = EOFException.class)
	public void testReadFully() throws IOException {
		byte[] expectedBytes =
			{Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV};

		InputStream inputStream =
			new UnsyncByteArrayInputStream(expectedBytes) {

				@Override
				public int read(byte[] bytes, int offset, int length) {
					return super.read(bytes, offset, 1);
				}

			};

		byte[] actualBytes = new byte[3];

		SocksProxyUtil.readFully(inputStream, actualBytes);

		Assert.assertArrayEquals(expectedBytes, actualBytes);

		SocksProxyUtil.readFully(inputStream, actualBytes);
	}

}