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
	protected void activate(Map<String, Object> properties) {
		String allowedIPAddressString = GetterUtil.getString(
			properties.get("allowedHostAddresses"), "127.0.0.1");

		int executorServiceAwaitTimeout = GetterUtil.getInteger(
			properties.get("executorServiceAwaitTimeout"), 10);

		int serverSocketPort = GetterUtil.getInteger(
			properties.get("serverSocketPort"), 8888);

		List<String> allowedIPAddress = new ArrayList<>();

		Collections.addAll(
			allowedIPAddress, StringUtil.split(allowedIPAddressString));

		_socksProxyServer = new SocksProxyServer(
			allowedIPAddress, executorServiceAwaitTimeout, serverSocketPort);

		_socksProxyServer.start();
	}

	@Deactivate
	protected void deactivate() throws Exception {
		_socksProxyServer.stop();
	}

	private SocksProxyServer _socksProxyServer;

}