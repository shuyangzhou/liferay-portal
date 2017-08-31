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

package com.liferay.simple.socks.proxy.manager;

import com.liferay.portal.kernel.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil;

import java.io.IOException;

import java.net.ConnectException;
import java.net.Socket;

import java.util.Collections;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerManagerTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testNormalStartStop() throws IOException {
//		ConcurrentMap<String, Object> attributes =
//		LocalProcessLauncher.ProcessContext.getAttributes();

		int port = SocksProxyTestUtil.findOpenPort(8888);

		SocksProxyServerManager socksProxyServerManager =
			new SocksProxyServerManager(
				new LocalProcessExecutor(), Collections.emptyList(), 1000,
				port);

		try {
			socksProxyServerManager.start();

			while (true) {
				try (Socket socket = new Socket("localhost", port)) {
					break;
				}
				catch (ConnectException ce) {
				}
			}

//			Object object = attributes.get(SocksProxyServer.class.getName());

//			Assert.assertSame(SocksProxyServer.class, object.getClass());

			socksProxyServerManager.stop();
		}
		catch (Exception e) {
			e.printStackTrace();

			Assert.fail();
		}
	}

}