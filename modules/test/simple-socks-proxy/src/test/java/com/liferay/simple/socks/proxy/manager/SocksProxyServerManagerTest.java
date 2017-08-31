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
	public void testNormalStartStop() throws Exception {
		SocksProxyServerManager socksProxyServerManager =
			new SocksProxyServerManager(
				new LocalProcessExecutor(), Collections.EMPTY_LIST, 20000,
				8888);

		try {
			socksProxyServerManager.start();

			Thread.sleep(5000);

			socksProxyServerManager.stop();
		}
		catch (Exception e) {
			Assert.fail();
		}
	}

}