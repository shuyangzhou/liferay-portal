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

import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.ProcessChannel;
import com.liferay.portal.kernel.process.ProcessUtil;
import com.liferay.portal.kernel.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.HeapUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.simple.socks.proxy.manager.test.util.SocksProxyTestUtil;

import java.io.Serializable;

import java.util.Collections;
import java.util.concurrent.Future;

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
		int port = SocksProxyTestUtil.findOpenPort(8888);

		SocksProxyServerManager socksProxyServerManager =
			new SocksProxyServerManager(
				new LocalProcessExecutor(), Collections.emptyList(), 10000,
				port);

		socksProxyServerManager.start();

		ProcessChannel<Serializable> processChannel =
			ReflectionTestUtil.getFieldValue(
				socksProxyServerManager, "_processChannel");

		Future<Integer> future = processChannel.write(
			new ReturnProcessIDCallable());

		int processID = future.get();

		Assert.assertNotEquals(HeapUtil.getProcessId(), processID);

		socksProxyServerManager.stop();

		Future<ObjectValuePair<byte[], byte[]>> jpsFuture = ProcessUtil.execute(
			ProcessUtil.COLLECTOR_OUTPUT_PROCESSOR, "jps");

		ObjectValuePair<byte[], byte[]> objectValuePair = jpsFuture.get();

		String stdOutString = new String(objectValuePair.getKey());

		Assert.assertFalse(stdOutString.contains(String.valueOf(processID)));
	}

	private static class ReturnProcessIDCallable
		implements ProcessCallable<Integer> {

		@Override
		public Integer call() {
			return HeapUtil.getProcessId();
		}

		private static final long serialVersionUID = 1L;

	}

}