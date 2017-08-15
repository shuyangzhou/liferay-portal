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

package com.liferay.simple.socks.proxy;

import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.io.InterruptedIOException;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;

/**
 * @author Tom Wang
 */
public class SocksProxyServer implements Runnable {

	public SocksProxyServer(
		List<String> allowedHosts, int serverSocketPort,
		int serverSocketTimeout, int socketTimeout) {

		_allowedHosts = allowedHosts;
		_serverSocketPort = serverSocketPort;
		_serverSocketTimeout = serverSocketTimeout;
		_socketTimeout = serverSocketTimeout;
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(_serverSocketPort)) {
			serverSocket.setSoTimeout(_serverSocketTimeout);

			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				0, 100);

			while (serverSocket != null) {
				try {
					Socket clientSocket = serverSocket.accept();

					clientSocket.setSoTimeout(_socketTimeout);

					threadPoolExecutor.execute(
						new ClientHandler(
							_allowedHosts, clientSocket, _socketTimeout));
				}
				catch (InterruptedIOException iioe) {
					if (_log.isInfoEnabled()) {
						_log.info("ServerSocket waiting to accept", iioe);
					}
				}
			}
		}
		catch (BindException be) {
			if (_log.isWarnEnabled()) {
				_log.warn("The port " + _serverSocketPort + " is in use!", be);
			}
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"IO error binding at port: " + _serverSocketPort, ioe);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServer.class);

	private final List<String> _allowedHosts;
	private final int _serverSocketPort;
	private final int _serverSocketTimeout;
	private final int _socketTimeout;

}