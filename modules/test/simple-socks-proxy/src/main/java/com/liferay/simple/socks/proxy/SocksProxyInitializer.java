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

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
public class SocksProxyInitializer {

	@Activate
	protected void activate(Map<String, Object> properties) throws Exception {
		String allowedHostsString = GetterUtil.getString(
			properties.get("allowedHosts"), "127.0.0.1");

		int serverSocketPort = GetterUtil.getInteger(
			properties.get("serverSocketPort"), 8888);

		int serverSocketTimeout = GetterUtil.getInteger(
			properties.get("serverSocketTimeout"), 200);

		int socketTimeOut = GetterUtil.getInteger(
			properties.get("socketTimeout"), 10);

		List<String> allowedHosts = new ArrayList<>();

		Collections.addAll(allowedHosts, StringUtil.split(allowedHostsString));

		_serverThread = new Thread(
			new SocksProxyServer(
				allowedHosts, serverSocketPort, serverSocketTimeout,
				socketTimeOut));

		_serverThread.setDaemon(true);

		_serverThread.start();
	}

	@Deactivate
	protected void deactivate() throws IOException {
		_serverThread.interrupt();
	}

	private Thread _serverThread;

}