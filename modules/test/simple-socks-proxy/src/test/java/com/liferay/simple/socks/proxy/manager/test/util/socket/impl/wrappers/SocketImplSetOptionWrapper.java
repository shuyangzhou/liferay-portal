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

package com.liferay.simple.socks.proxy.manager.test.util.socket.impl.wrappers;

import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.simple.socks.proxy.manager.process.server.SocksProxyServer;

import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketImpl;

/**
 * @author Tom Wang
 */
public class SocketImplSetOptionWrapper extends SocketImplWrapper {

	public SocketImplSetOptionWrapper(
		SocksProxyServer socksProxyServer, SocketImpl socketImpl) {

		_sockProxyServer = socksProxyServer;

		_socketImpl = socketImpl;
	}

	@Override
	public void setOption(int optID, Object value) throws SocketException {
		ServerSocket serverSocket = ReflectionTestUtil.getFieldValue(
			_sockProxyServer, "_serverSocket");

		ReflectionTestUtil.setFieldValue(serverSocket, "impl", _socketImpl);

		throw new SocketException();
	};

	private final SocketImpl _socketImpl;
	private final SocksProxyServer _sockProxyServer;

}