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

package com.liferay.simple.socks.proxy.manager.process;

import com.liferay.portal.kernel.process.ProcessCallable;
import com.liferay.portal.kernel.process.ProcessException;
import com.liferay.portal.kernel.process.local.LocalProcessLauncher.ProcessContext;
import com.liferay.simple.socks.proxy.manager.process.server.SocksProxyServer;

import java.io.Serializable;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Tom Wang
 */
public class SocksProxyServerCloseProcessCallable
	implements ProcessCallable<Serializable> {

	@Override
	public Serializable call() throws ProcessException {
		ConcurrentMap<String, Object> attributes =
			ProcessContext.getAttributes();

		SocksProxyServer socksProxyServer = (SocksProxyServer)attributes.remove(
			SocksProxyServer.class.getName());

		if (socksProxyServer != null) {
			try {
				socksProxyServer.close();

				socksProxyServer.join();
			}
			catch (Exception e) {
				throw new ProcessException(e);
			}
		}

		return null;
	}

	private static final long serialVersionUID = 1L;

}