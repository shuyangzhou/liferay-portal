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

package com.liferay.portlet.tck.bridge;

import com.liferay.petra.log4j.Log4JUtil;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.kernel.util.ThreadUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.struts.StrutsActionRegistryUtil;
import com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration;
import com.liferay.portlet.tck.bridge.setup.Setup;
import com.liferay.portlet.tck.bridge.struts.PortletTCKStrutsAction;

import java.io.IOException;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import java.nio.charset.Charset;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletContext;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Tambara
 * @author Vernon Singleton
 */
@Component(
	configurationPid = "com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration"
)
public class PortletTCKBridge {

	@Activate
	@Modified
	protected void activate(ComponentContext componentContext) {
		deactivate();

		if (!_log.isInfoEnabled()) {
			Log4JUtil.setLevel("com.liferay.portlet.tck.bridge", "INFO", true);
		}

		PortletTCKBridgeConfiguration portletTCKBridgeConfiguration =
			ConfigurableUtil.createConfigurable(
				PortletTCKBridgeConfiguration.class,
				componentContext.getProperties());

		String[] servletContextNames =
			portletTCKBridgeConfiguration.servletContextNames();

		// Wait for portlet wars to deploy ...
		// They need to be available before setting up sites and pages

		for (String servletContextName : servletContextNames) {
			_waitForDeployment(
				servletContextName, System.currentTimeMillis(),
				portletTCKBridgeConfiguration.timeout() * Time.SECOND);
		}

		// Wait for the portlet-tck-theme to deploy ...

		_waitForDeployment(
			"portlet-tck-theme", System.currentTimeMillis(),
			portletTCKBridgeConfiguration.timeout() * Time.SECOND);

		Setup.setupPortletTCKSite(portletTCKBridgeConfiguration);

		StrutsActionRegistryUtil.register(_PATH, new PortletTCKStrutsAction());

		FutureTask<Void> futureTask = new FutureTask<>(
			new HandshakeServerCallable(portletTCKBridgeConfiguration));

		_handshakeServerFuture = futureTask;

		Thread handshakeServerThread = new Thread(
			futureTask, "Handshake server thread");

		handshakeServerThread.setDaemon(true);

		handshakeServerThread.start();
	}

	@Deactivate
	protected void deactivate() {
		Future<Void> handshakeServerFuture = _handshakeServerFuture;

		if (handshakeServerFuture != null) {
			handshakeServerFuture.cancel(true);
		}

		StrutsActionRegistryUtil.unregister(_PATH);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private void _waitForDeployment(
		String servletContextName, long startTime, long timeout) {

		while ((System.currentTimeMillis() - startTime) < timeout) {
			ServletContext servletContext = ServletContextPool.get(
				servletContextName);

			if (_log.isInfoEnabled()) {
				_log.info(
					"_waitForDeployment: of servletContextName = " +
						servletContextName + " ...");
			}

			boolean found = false;

			if (servletContext != null) {
				found = true;
			}

			if (found) {
				if (servletContext.getAttribute(WebKeys.PLUGIN_PORTLETS) ==
						null) {

					if (servletContext.getAttribute(WebKeys.PLUGIN_THEMES) ==
							null) {

						found = false;
					}
				}
			}

			if (found) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"_waitForDeployment: found servletContextName = " +
							servletContextName);
				}

				return;
			}
			else {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException ie) {
					if (_log.isWarnEnabled()) {
						_log.warn(ie.getMessage());
					}
				}
			}
		}

		_log.error("Timeout waiting for " + servletContextName);
		_log.error(ThreadUtil.threadDump());
	}

	private static final String _PATH = "/portal/tck";

	private static final Log _log = LogFactoryUtil.getLog(
		PortletTCKBridge.class);

	private volatile Future<Void> _handshakeServerFuture;

	private static class HandshakeServerCallable implements Callable<Void> {

		@Override
		public Void call() throws IOException {
			try (ServerSocket serverSocket = new ServerSocket(
					_portletTCKBridgeConfiguration.handshakeServerPort())) {

				serverSocket.setSoTimeout(100);

				while (!Thread.interrupted()) {
					try (Socket socket = serverSocket.accept();
						OutputStream outputStream = socket.getOutputStream()) {

						outputStream.write(
							"Portlet TCK Bridge is ready".getBytes(
								Charset.defaultCharset()));
					}
					catch (SocketTimeoutException ste) {
					}
				}
			}

			return null;
		}

		private HandshakeServerCallable(
			PortletTCKBridgeConfiguration portletTCKBridgeConfiguration) {

			_portletTCKBridgeConfiguration = portletTCKBridgeConfiguration;
		}

		private final PortletTCKBridgeConfiguration
			_portletTCKBridgeConfiguration;

	}

}