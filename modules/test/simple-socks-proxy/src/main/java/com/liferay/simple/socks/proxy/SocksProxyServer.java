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
public class SocksProxyServer {

	public SocksProxyServer(
		List<String> allowedIPAddress, int executorServiceAwaitTimeout,
		int serverSocketPort) {

		_socksProxyServerThread = new SocksProxyServerThread(
			allowedIPAddress, executorServiceAwaitTimeout, serverSocketPort);

		_socksProxyServerThread.setName("Socks Proxy Server Thread");

		_socksProxyServerThread.setDaemon(true);
	}

	public void start() {
		_socksProxyServerThread.start();
	}

	public void stop() throws Exception {
		if (_socksProxyServerThread != null) {
			_socksProxyServerThread.close();

			_socksProxyServerThread.join();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServer.class);

	private final SocksProxyServerThread _socksProxyServerThread;

	private static class SocksProxyServerThread extends Thread {

		public SocksProxyServerThread(
			List<String> allowedHosts, int executorServiceAwaitTimeout,
			int serverSocketPort) {

			_allowedHosts = allowedHosts;
			_executorServiceTimeout = executorServiceAwaitTimeout;
			_serverSocketPort = serverSocketPort;
		}

		public void close() throws IOException {
			if (_serverSocket != null) {
				_serverSocket.close();
			}
		}

		@Override
		public void run() {
			ExecutorService executorService = Executors.newCachedThreadPool();

			try (ServerSocket serverSocket = new ServerSocket(
					_serverSocketPort)) {

				_serverSocket = serverSocket;

				_serverSocket.setSoTimeout(0);

				while (true) {
					Socket portalSocket = null;

					try {
						portalSocket = _serverSocket.accept();
					}
					catch (SocketException se) {
						if (_log.isInfoEnabled()) {
							_log.info(
								"Socks proxy server terminated by module " +
									"deactivation",
								se);
						}

						break;
					}

					portalSocket.setSoTimeout(60000);

					executorService.execute(
						new ClientHandler(
							_allowedHosts, portalSocket, executorService));
				}
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(e, e);
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
						_log.warn("Executor termination interrupted", ie);
					}
				}
			}
		}

		private final List<String> _allowedHosts;
		private final int _executorServiceTimeout;
		private ServerSocket _serverSocket;
		private final int _serverSocketPort;

	}

}