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
import com.liferay.portal.kernel.process.ProcessException;
import com.liferay.portal.kernel.process.local.LocalProcessExecutor;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.simple.socks.proxy.manager.SocksProxyServerManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tom Wang
 */
@Component(
	configurationPid = "com.liferay.simple.socks.proxy.SocksProxyConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class SocksProxyServerActivator {

	@Activate
	protected void activate(Map<String, Object> properties)
		throws ProcessException {

		String allowedHostnames = GetterUtil.getString(
			properties.get("allowedHostnames"), "");

		long shutdownWaitTime = GetterUtil.getLong(
			properties.get("shutdownWaitTime"), 1 * Time.MINUTE);

		int serverSocketPort = GetterUtil.getInteger(
			properties.get("serverSocketPort"), 8888);

		List<String> allowedIPAddresses = new ArrayList<>();

		for (String allowedHostname : StringUtil.split(allowedHostnames)) {
			try {
				InetAddress inetAddress = InetAddress.getByName(
					allowedHostname);

				allowedIPAddresses.add(inetAddress.getHostAddress());
			}
			catch (UnknownHostException uhe) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unknown hostname", uhe);
				}
			}
		}

		_socksProxyServerManager = new SocksProxyServerManager(
			_localProcessExecutor, allowedIPAddresses, shutdownWaitTime,
			serverSocketPort);

		_socksProxyServerManager.start();
	}

	@Deactivate
	protected void deactivate() throws Exception {
		_socksProxyServerManager.stop();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SocksProxyServerActivator.class);

	@Reference
	private LocalProcessExecutor _localProcessExecutor;

	private SocksProxyServerManager _socksProxyServerManager;

}