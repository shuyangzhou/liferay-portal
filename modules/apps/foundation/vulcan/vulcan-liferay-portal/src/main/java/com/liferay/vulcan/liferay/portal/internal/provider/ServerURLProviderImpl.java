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

package com.liferay.vulcan.liferay.portal.internal.provider;

import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.vulcan.provider.ServerURLProvider;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

/**
 * Creates the server's URL based on the HTTP request and the forwarded header,
 * to account for proxies.
 *
 * @author Javier Gamarra
 */
@Component(immediate = true)
public class ServerURLProviderImpl implements ServerURLProvider {

	/**
	 * Returns the request's original URL without proxy redirection. This method
	 * does this by inspecting the request's forwarded host header, if it
	 * exists. Otherwise, this method uses the request's {@code serverName}
	 * method to return the URL.
	 *
	 * @param  httpServletRequest the HTTP request
	 * @return the request's original URL without proxy redirection
	 */
	@Override
	public String getServerURL(HttpServletRequest httpServletRequest) {
		StringBuilder sb = new StringBuilder(httpServletRequest.getScheme());

		sb.append(Http.PROTOCOL_DELIMITER);

		String forwardedHost = httpServletRequest.getHeader("X-Forwarded-Host");

		if (forwardedHost == null) {
			sb.append(httpServletRequest.getServerName());
			sb.append(StringPool.COLON);
			sb.append(httpServletRequest.getServerPort());
		}
		else {
			sb.append(forwardedHost);
		}

		sb.append(httpServletRequest.getContextPath());

		return sb.toString();
	}

}