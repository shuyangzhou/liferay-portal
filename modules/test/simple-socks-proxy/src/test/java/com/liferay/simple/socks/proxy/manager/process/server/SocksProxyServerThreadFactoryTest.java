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

import java.lang.reflect.Field;

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
	public void testSocksProxyServerThreadFactory()
		throws IllegalAccessException, IllegalArgumentException {

		SocksProxyServerThreadFactory socksProxyServerThreadFactory =
			new SocksProxyServerThreadFactory();

		Thread thread = socksProxyServerThreadFactory.newThread(
			new Runnable() {

				@Override
				public void run() {
				}

			});

		Assert.assertTrue(thread.isDaemon());

		Field prefix = ReflectionTestUtil.getField(
			SocksProxyServerThreadFactory.class, "_prefix");

		String prefixString = (String)prefix.get(new String());

		Assert.assertEquals(prefixString.concat("1"), thread.getName());

		Thread thread2 = socksProxyServerThreadFactory.newThread(
			new Runnable() {

				@Override
				public void run() {
				}

			});

		Assert.assertEquals(prefixString.concat("2"), thread2.getName());
	}

}