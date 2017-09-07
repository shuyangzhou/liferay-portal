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

import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.simple.socks.proxy.manager.process.util.Constants;

import java.io.IOException;

import java.net.InetAddress;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyRequestTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testDomainNameATYPE() throws Exception {
		InetAddress inetAddress = InetAddress.getByName("www.liferay.com");

		int port = 8080;

		byte[] bytes = {(byte)(port >>> 8), (byte)port};

		String address = inetAddress.getHostAddress();

		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_DOMAIN_NAME, address.getBytes(), bytes);

		Assert.assertEquals(address, socksProxyRequest.getHostAddress());
	}

	@Test
	public void testSocksProxyRequest() throws Exception {
		InetAddress inetAddress = InetAddress.getByName("www.liferay.com");

		int port = 8080;

		byte[] bytes = {(byte)(port >>> 8), (byte)port};

		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_IPV4, inetAddress.getAddress(), bytes);

		Assert.assertEquals(port, socksProxyRequest.calculateServerPort());

		Assert.assertEquals(Constants.ATYP_IPV4, socksProxyRequest.getAtyp());
		Assert.assertEquals(Constants.CMD_CONNECT, socksProxyRequest.getCmd());

		byte[] dstAddr = socksProxyRequest.getDstAddr();

		Assert.assertEquals(Arrays.toString(dstAddr), 4, dstAddr.length);

		byte[] inetAddressBytes = inetAddress.getAddress();

		for (int i = 0; i < dstAddr.length; i++) {
			Assert.assertEquals(inetAddressBytes[i], dstAddr[i]);
		}

		byte[] dstPort = socksProxyRequest.getDstPort();

		for (int i = 0; i < dstPort.length; i++) {
			Assert.assertEquals(bytes[i], dstPort[i]);
		}

		Assert.assertEquals(
			inetAddress.getHostAddress(), socksProxyRequest.getHostAddress());
		Assert.assertEquals(
			Constants.SOCKS5_VERSION, socksProxyRequest.getVer());
	}

	@Test
	public void testUnsupportedATYPE() throws Exception {
		InetAddress inetAddress = InetAddress.getByName("www.liferay.com");

		int port = 8080;

		byte[] bytes = {(byte)(port >>> 8), (byte)port};

		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_IPV6, inetAddress.getAddress(), bytes);

		try {
			socksProxyRequest.getHostAddress();
		}
		catch (IOException ioe) {
			Assert.assertEquals(
				"Unsupported ATYPE: " + Constants.ATYP_IPV6, ioe.getMessage());
		}
	}

}