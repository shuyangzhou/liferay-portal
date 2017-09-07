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

package com.liferay.simple.socks.proxy.manager.process;

import com.liferay.portal.kernel.process.local.LocalProcessLauncher.ProcessContext;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.simple.socks.proxy.manager.process.server.SocksProxyServer;

import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerProcessCallableTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testSocksProxyServerProcessCallable() throws Exception {
		try {
			SocksProxyServerProcessCallable socksProxyServerProcessCallable =
				new SocksProxyServerProcessCallable(
					Collections.EMPTY_LIST, 100000, 8888);

			socksProxyServerProcessCallable.call();

			Thread.sleep(5000);
		}
		finally {
			SocksProxyServerCloseProcessCallable
				socksProxyServerCloseProcessCallable =
					new SocksProxyServerCloseProcessCallable();

			socksProxyServerCloseProcessCallable.call();
		}

		ConcurrentMap<String, Object> attributes =
			ProcessContext.getAttributes();

		Object object = attributes.get(SocksProxyServer.class.getName());

		Assert.assertEquals(SocksProxyServer.class, object.getClass());
	}

}