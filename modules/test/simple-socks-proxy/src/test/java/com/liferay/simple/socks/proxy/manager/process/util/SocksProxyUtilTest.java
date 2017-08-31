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

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.io.ByteArrayInputStream;
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
	public void testInstance() {
		SocksProxyUtil socksProxyUtil = new SocksProxyUtil();

		Assert.assertEquals(SocksProxyUtil.class, socksProxyUtil.getClass());
	}

	@Test
	public void testRead() throws IOException {
		byte[] bytes = {Constants.SOCKS5_VERSION};

		InputStream inputStream = new ByteArrayInputStream(bytes);

		byte value = SocksProxyUtil.read(inputStream);

		Assert.assertEquals(Constants.SOCKS5_VERSION, value);

		try {
			SocksProxyUtil.read(inputStream);
		}
		catch (Exception e) {
			Assert.assertEquals(EOFException.class, e.getClass());
		}
	}

	@Test
	public void testReadFully() throws IOException {
		byte[] bytes =
			{Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV};

		InputStream inputStream = new ByteArrayInputStream(bytes);

		byte[] values = new byte[3];

		SocksProxyUtil.readFully(inputStream, values);

		Assert.assertEquals(Constants.SOCKS5_VERSION, values[0]);
		Assert.assertEquals(Constants.CMD_CONNECT, values[1]);
		Assert.assertEquals(Constants.RSV, values[2]);

		try {
			SocksProxyUtil.readFully(inputStream, values);
		}
		catch (Exception e) {
			Assert.assertEquals(EOFException.class, e.getClass());
		}
	}

}