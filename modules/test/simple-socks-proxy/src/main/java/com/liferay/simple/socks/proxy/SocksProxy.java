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

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.simple.socks.proxy.configuration.SocksProxyConfiguration;

import java.io.IOException;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Tom Wang
 */
@Component(
	configurationPid = "com.liferay.simple.socks.proxy.SocksProxyConfiguration",
	immediate = true
)
public class SocksProxy {

	public void close() throws IOException {
		if (_listenSocket != null) {
			_listenSocket.close();
		}

		_listenSocket = null;
	}

	public void startSocket(int port) throws Exception {
		try {
			_listenSocket = new ServerSocket(port);
		}
		catch (BindException be) {
			if (_log.isWarnEnabled()) {
				_log.warn("The port " + port + " is in use!", be);
			}

			throw be;
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn("IO error binding at port: " + port, ioe);
			}

			throw ioe;
		}

		while (_listenSocket != null) {
			Socket clientSocket = _listenSocket.accept();

			clientSocket.setSoTimeout(_configuration.serverTimeout());

			Thread thread = new Thread(
				new SocksHandler(clientSocket, _configuration));

			thread.start();
		}

		close();
	}

	@Activate
	protected void activate(Map<String, Object> properties) throws Exception {
		_configuration = ConfigurableUtil.createConfigurable(
			SocksProxyConfiguration.class, properties);

		_thread = new Thread(
			new Runnable() {

				@Override
				public void run() {
					try {
						startSocket(_configuration.listeningPort());
					}
					catch (Exception e) {
						if (_log.isWarnEnabled()) {
							_log.warn(e);
						}
					}
				}

			});

		_thread.start();
	}

	@Deactivate
	protected void deactivate() throws IOException {
		close();

		_thread.interrupt();
	}

	private static final Log _log = LogFactoryUtil.getLog(SocksProxy.class);

	private volatile SocksProxyConfiguration _configuration;
	private ServerSocket _listenSocket;
	private Thread _thread;

}