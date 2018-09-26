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

package com.liferay.apio.architect.internal.provider;

import static com.liferay.apio.architect.internal.provider.util.URLProviderUtil.getServerURL;

import com.liferay.apio.architect.internal.url.ServerURL;
import com.liferay.apio.architect.provider.Provider;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * Creates the server's URL based on the HTTP request and the forwarded header,
 * to account for proxies.
 *
 * @author Javier Gamarra
 */
@Component(service = Provider.class)
public class ServerURLProvider implements Provider<ServerURL> {

	@Override
	public ServerURL createContext(HttpServletRequest httpServletRequest) {
		return () -> getServerURL(httpServletRequest);
	}

}