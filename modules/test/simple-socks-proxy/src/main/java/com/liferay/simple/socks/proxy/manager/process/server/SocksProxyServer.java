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
		List<String> allowedHosts, long shutdownWaitTime,
		int serverSocketPort) {

		_allowedIPAddresses = allowedHosts;
		_shutdownWaitTime = shutdownWaitTime;
		_serverSocketPort = serverSocketPort;

		setName(SocksProxyServer.class.getName());
	}

	public void close() throws Exception {
		ServerSocket serverSocket = _serverSocket;

		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool(
			new SocksProxyServerThreadFactory());

		try {
			ServerSocket serverSocket = new ServerSocket(_serverSocketPort);

			try {
				_serverSocket = serverSocket;

				try {
					serverSocket.setSoTimeout(0);
				}
				catch (SocketException se) {
					if (_log.isWarnEnabled()) {
						_log.warn("Unable to set server socket so timeout", se);
					}
				}

				while (true) {
					Socket socket = null;

					try {
						socket = serverSocket.accept();
					}
					catch (SocketException se) {
						if (_log.isInfoEnabled()) {
							_log.info(
								"Socks proxy server terminated by module " +
									"deactivation");
						}

						break;
					}
					catch (IOException ioe) {
						_log.error("Unable to accept client socket", ioe);

						continue;
					}

					try {
						socket.setSoTimeout(0);
					}
					catch (SocketException se) {
						if (_log.isWarnEnabled()) {
							_log.warn(
								"Unable to set client socket so timeout", se);
						}
					}

					executorService.execute(
						new SocksProxyConnection(
							_allowedIPAddresses, socket, executorService));
				}
			}
			finally {
				serverSocket.close();

				executorService.shutdown();

				try {
					executorService.awaitTermination(
						_shutdownWaitTime, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException ie) {
					if (_log.isWarnEnabled()) {
						_log.warn("Executor termination interrupted", ie);
					}
				}
			}
		}
		catch (IOException ioe) {
			_log.error("Unable to create server socket", ioe);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServer.class);

	private final List<String> _allowedIPAddresses;
	private volatile ServerSocket _serverSocket;
	private final int _serverSocketPort;
	private final long _shutdownWaitTime;

}