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
	configurationPid = "com.liferay.simple.socks.proxy.SocksProxyConfiguration"
)
public class SocksProxyInitializer {

	@Activate
	protected void activate(Map<String, Object> properties) throws Exception {
		String allowedHostsString = GetterUtil.getString(
			properties.get("allowedHosts"), "127.0.0.1");

		int executorServiceTimeout = GetterUtil.getInteger(
			properties.get("executorServiceTimeout"), 10);

		int serverSocketPort = GetterUtil.getInteger(
			properties.get("serverSocketPort"), 8888);

		int socketSOTimeOut = GetterUtil.getInteger(
			properties.get("socketSOTimeout"), 10);

		List<String> allowedHosts = new ArrayList<>();

		Collections.addAll(allowedHosts, StringUtil.split(allowedHostsString));

		_socksProxyServer = new SocksProxyServer(
			allowedHosts, executorServiceTimeout, serverSocketPort,
			socketSOTimeOut);

		_socksProxyServer.start();
	}

	@Deactivate
	protected void deactivate() throws Exception {
		_socksProxyServer.close();

		_socksProxyServer.join();
	}

	private SocksProxyServer _socksProxyServer;

}