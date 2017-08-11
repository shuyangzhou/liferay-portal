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

import java.io.IOException;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Tom Wang
 */
@Component(immediate = true)
public class SocketListener {

	public void close() throws IOException {
		if (_listenSocket != null) {
			_listenSocket.close();
		}

		_listenSocket = null;
	}

	public void start(int port) throws Exception {
		try {
			_listenSocket = new ServerSocket(port);
		}
		catch (BindException be) {
			throw new BindException("The port " + port + " is in use!");
		}
		catch (IOException ioe) {
			throw new IOException("IO error binding at port: " + port);
		}

		while (_listenSocket != null) {
			Socket clientSocket = _listenSocket.accept();

			clientSocket.setSoTimeout(_DEFAULT_SERVER_TIMEOUT);

			Thread thread = new Thread(new ProxyHandler(clientSocket));

			thread.start();
		}

		close();
	}

	@Activate
	protected void activate() throws Exception {
		start(8888);
	}

	@Deactivate
	protected void deactivate() throws IOException {
		close();
	}

	private static final int _DEFAULT_LISTEN_TIMEOUT = 200;

	private static final int _DEFAULT_SERVER_TIMEOUT = 200;

	private ServerSocket _listenSocket;

}