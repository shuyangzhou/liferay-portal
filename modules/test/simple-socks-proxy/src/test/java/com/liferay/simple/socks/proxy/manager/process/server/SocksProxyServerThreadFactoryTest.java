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

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Tom Wang
 */
public class SocksProxyServerThreadFactoryTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testSocksProxyServerThreadFactory() {
		String prefix = ReflectionTestUtil.getFieldValue(
			SocksProxyServerThreadFactory.class, "_prefix");

		SocksProxyServerThreadFactory socksProxyServerThreadFactory =
			new SocksProxyServerThreadFactory();

		Runnable runnable = () -> {
		};

		Thread thread = socksProxyServerThreadFactory.newThread(runnable);

		Assert.assertTrue(thread.isDaemon());
		Assert.assertEquals(prefix.concat("1"), thread.getName());

		thread = socksProxyServerThreadFactory.newThread(runnable);

		Assert.assertTrue(thread.isDaemon());
		Assert.assertEquals(prefix.concat("2"), thread.getName());
	}

}