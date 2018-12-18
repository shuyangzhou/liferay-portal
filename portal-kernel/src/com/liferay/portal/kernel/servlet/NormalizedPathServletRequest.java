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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Tomas Polesovsky
 */
public class NormalizedPathServletRequest
	extends PersistentHttpServletRequestWrapper {

	public static HttpServletRequest getNormalizedPathServletRequest(
		HttpServletRequest request) {

		ServletRequest wrappedRequest = request;

		while (wrappedRequest instanceof HttpServletRequestWrapper) {
			if (wrappedRequest instanceof NormalizedPathServletRequest) {
				return request;
			}

			HttpServletRequestWrapper httpServletRequestWrapper =
				(HttpServletRequestWrapper)wrappedRequest;

			wrappedRequest = httpServletRequestWrapper.getRequest();
		}

		return new NormalizedPathServletRequest(request);
	}

	public NormalizedPathServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Object getAttribute(String name) {
		if (JavaConstants.JAVAX_SERVLET_ERROR_REQUEST_URI.equals(name) ||
			JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI.equals(name)) {

			return HttpUtil.normalizePath((String)super.getAttribute(name));
		}

		return super.getAttribute(name);
	}

	@Override
	public String getPathInfo() {
		return HttpUtil.normalizePath(super.getPathInfo());
	}

	@Override
	public String getRequestURI() {
		return HttpUtil.normalizePath(super.getRequestURI());
	}

}