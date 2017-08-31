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
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyRequestTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() throws UnknownHostException {
		_inetAddress = InetAddress.getByName("localhost");

		_port = 8080;

		_portBytes = new byte[] {(byte)(_port >>> 8), (byte)_port};
	}

	@Test
	public void testDomainNameATYPE() throws IOException {
		String hostName = _inetAddress.getHostName();

		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_DOMAIN_NAME, hostName.getBytes(), _portBytes);

		Assert.assertEquals(
			_inetAddress.getHostAddress(), socksProxyRequest.getHostAddress());
	}

	@Test
	public void testSocksProxyRequest() throws IOException {
		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_IPV4, _inetAddress.getAddress(), _portBytes);

		Assert.assertEquals(_port, socksProxyRequest.calculateServerPort());
		Assert.assertEquals(Constants.ATYP_IPV4, socksProxyRequest.getAtyp());
		Assert.assertEquals(Constants.CMD_CONNECT, socksProxyRequest.getCmd());

		Assert.assertArrayEquals(
			_inetAddress.getAddress(), socksProxyRequest.getDstAddr());

		Assert.assertArrayEquals(_portBytes, socksProxyRequest.getDstPort());

		Assert.assertEquals(
			_inetAddress.getHostAddress(), socksProxyRequest.getHostAddress());

		Assert.assertEquals(
			Constants.SOCKS5_VERSION, socksProxyRequest.getVer());
	}

	@Test
	public void testUnsupportedATYPE() {
		SocksProxyRequest socksProxyRequest = new SocksProxyRequest(
			Constants.SOCKS5_VERSION, Constants.CMD_CONNECT, Constants.RSV,
			Constants.ATYP_IPV6, _inetAddress.getAddress(), _portBytes);

		try {
			socksProxyRequest.getHostAddress();
		}
		catch (IOException ioe) {
			Assert.assertEquals(
				"Unsupported ATYPE: " + Constants.ATYP_IPV6, ioe.getMessage());
		}
	}

	private InetAddress _inetAddress;
	private int _port;
	private byte[] _portBytes;

}