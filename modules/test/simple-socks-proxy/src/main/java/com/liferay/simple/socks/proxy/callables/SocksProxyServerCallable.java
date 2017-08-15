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

package com.liferay.simple.socks.proxy.callables;

import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.local.LocalProcessLauncher.ProcessContext;
import com.liferay.simple.socks.proxy.process.SocksProxyServer;

import java.io.Serializable;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Tom Wang
 */
public class SocksProxyServerCallable implements ProcessCallable<Serializable> {

	public SocksProxyServerCallable(
		List<String> allowedIPAddress, int executorServiceAwaitTimeout,
		int serverSocketPort) {

		_allowedIPAddress = allowedIPAddress;
		_executorServiceAwaitTimeout = executorServiceAwaitTimeout;
		_serverSocketPort = serverSocketPort;
	}

	@Override
	public String call() {
		SocksProxyServer socksProxyServer = new SocksProxyServer(
			_allowedIPAddress, _executorServiceAwaitTimeout, _serverSocketPort);

		socksProxyServer.setName(
			"com.liferay.simple.socks.proxy.SocksProxyServer");

		ConcurrentMap<String, Object> attributes =
			ProcessContext.getAttributes();

		attributes.put(SocksProxyServer.class.getName(), socksProxyServer);

		socksProxyServer.start();

		return null;
	}

	private static final long serialVersionUID = 1L;

	private final List<String> _allowedIPAddress;
	private final int _executorServiceAwaitTimeout;
	private final int _serverSocketPort;

}