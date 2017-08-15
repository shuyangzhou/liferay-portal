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

package com.liferay.simple.socks.proxy.process;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tom Wang
 */
public class SocksProxyServer extends Thread {

	public SocksProxyServer(
		List<String> allowedHosts, int executorServiceAwaitTimeout,
		int serverSocketPort) {

		_allowedIPAddresses = allowedHosts;
		_executorServiceTimeout = executorServiceAwaitTimeout;
		_serverSocketPort = serverSocketPort;
	}

	public void close() throws Exception {
		if (_serverSocket != null) {
			_serverSocket.close();
		}
	}

	@Override
	public void run() {
		ExecutorService executorService = Executors.newCachedThreadPool(
			new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread thread = new Thread(r);

					thread.setName(
						"com.liferay.simple.socks.proxy." +
							"SocksProxyServerThreadPool".concat(
								StringPool.MINUS).concat(
									String.valueOf(
										_counter.incrementAndGet())));
					thread.setDaemon(true);

					return thread;
				}

				private final AtomicInteger _counter = new AtomicInteger();

			});

		try (ServerSocket serverSocket = new ServerSocket(_serverSocketPort)) {
			_serverSocket = serverSocket;

			_serverSocket.setSoTimeout(0);

			while (true) {
				Socket internalSocket = null;

				try {
					internalSocket = _serverSocket.accept();
				}
				catch (SocketException se) {
					if (_log.isInfoEnabled()) {
						_log.info(
							"Socks proxy server terminated by module " +
								"deactivation");
					}

					break;
				}

				internalSocket.setSoTimeout(0);

				executorService.execute(
					new SocksConnectionHandler(
						_allowedIPAddresses, internalSocket, executorService));
			}
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
		finally {
			executorService.shutdown();

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

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServer.class);

	private final List<String> _allowedIPAddresses;
	private final int _executorServiceTimeout;
	private ServerSocket _serverSocket;
	private final int _serverSocketPort;

}