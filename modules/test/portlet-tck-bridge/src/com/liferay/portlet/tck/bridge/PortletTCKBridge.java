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

import aQute.bnd.annotation.metatype.Configurable;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletContextPool;
import com.liferay.portal.struts.StrutsActionRegistryUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration;

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
 */
@Component(
	configurationPid ="com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration"
)
public class PortletTCKBridge {

	@Activate
	@Modified
	protected void activate(ComponentContext componentContext)
		throws IOException {

		PortletTCKBridgeConfiguration portletTCKBridgeConfiguration =
			Configurable.createConfigurable(
				PortletTCKBridgeConfiguration.class,
				componentContext.getProperties());

		StrutsActionRegistryUtil.register(
			_PATH, new PortletTCKStrutsAction());

		FutureTask<Void> futureTask = new FutureTask<>(
			new HandShakeServerCallable(portletTCKBridgeConfiguration));

		_handShakeServerFuture = futureTask;

		Thread handShakeServerThread = new Thread(
			futureTask, "Hand shake server thread");

		handShakeServerThread.setDaemon(true);

		handShakeServerThread.start();
	}

	@Deactivate
	protected void deactivate() {
		StrutsActionRegistryUtil.unregister(_PATH);

		Future<Void> handShakeServerFuture = _handShakeServerFuture;

		if (handShakeServerFuture != null) {
			handShakeServerFuture.cancel(true);
		}
	}

	@Reference(target = "(original.bean=*)", unbind = "-")
	protected void setServletContext(ServletContext servletContext) {
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletTCKBridge.class);

	private static final String _PATH = "/portal/tck";

	private volatile Future<Void> _handShakeServerFuture;

	private static class HandShakeServerCallable implements Callable<Void> {

		private HandShakeServerCallable(
			PortletTCKBridgeConfiguration portletTCKBridgeConfiguration) {

			_portletTCKBridgeConfiguration = portletTCKBridgeConfiguration;
		}

		@Override
		public Void call() throws Exception {
			long startTime = System.currentTimeMillis();

			try {
				for (String servletContextName :
						_portletTCKBridgeConfiguration.servletContextNames()) {

					_waitForDeployment(
						servletContextName, startTime,
						_portletTCKBridgeConfiguration.timeout());
				}
			}
			finally {
				System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%Initialized");
			}

			try (ServerSocket serverSocket =
				new ServerSocket(
					_portletTCKBridgeConfiguration.handShakeServerPort())) {
				
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

		private void _waitForDeployment(
			String servletContextName, long startTime, long timeout) {

			while ((System.currentTimeMillis() - startTime) < timeout) {
				ServletContext serviceContext = ServletContextPool.get(
					servletContextName);

				if ((serviceContext == null) ||
					(serviceContext.getAttribute(WebKeys.PLUGIN_PORTLETS) ==
						null)) {

					System.out.println(
						"&&&&&&&&&&&&wait on : " + servletContextName);

					try {
						Thread.sleep(100);
					}
					catch (InterruptedException ie) {
					}
				}
				else {
					System.out.println(
						"&&&&&&&&&&&&Ready : " + servletContextName);

					return;
				}
			}

			_log.error("Timeout on waiting " + servletContextName);

			System.out.println(
				"&&&&&&&&&&&&Timeout on : " + servletContextName);
		}

		private final PortletTCKBridgeConfiguration
			_portletTCKBridgeConfiguration;

	}

}