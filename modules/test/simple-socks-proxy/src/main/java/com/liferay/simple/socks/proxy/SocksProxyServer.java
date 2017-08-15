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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Tom Wang
 */
public class SocksProxyServer extends Thread {

	public SocksProxyServer(
		List<String> allowedHosts, int executorServiceTimeout,
		int serverSocketPort, int socketSOTimeout) {

		setName("SocksProxyServer");
		setDaemon(true);

		_allowedHosts = allowedHosts;
		_executorServiceTimeout = executorServiceTimeout;
		_serverSocketPort = serverSocketPort;
		_socketSOTimeout = socketSOTimeout;
	}

	public void close() throws IOException {
		_serverSocket.close();
	}

	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool();

		try {
			_serverSocket = new ServerSocket(_serverSocketPort);

			_serverSocket.setSoTimeout(0);

			while (true) {
				Socket clientSocket = _serverSocket.accept();

				clientSocket.setSoTimeout(_socketSOTimeout);

				executorService.execute(
					new ClientHandler(
						_allowedHosts, clientSocket, _socketSOTimeout));
			}
		}
		catch (SocketException se) {
			if (_log.isInfoEnabled()) {
				_log.info(se);
			}
		}
		catch (Exception e) {
			if (_log.isInfoEnabled()) {
				_log.info(e);
			}
		}
		finally {
			executorService.shutdownNow();

			try {
				executorService.awaitTermination(
					_executorServiceTimeout, TimeUnit.MINUTES);
			}
			catch (InterruptedException ie) {
				if (_log.isWarnEnabled()) {
					_log.warn(ie);
				}
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServer.class);

	private final List<String> _allowedHosts;
	private final int _executorServiceTimeout;
	private ServerSocket _serverSocket;
	private final int _serverSocketPort;
	private final int _socketSOTimeout;

}