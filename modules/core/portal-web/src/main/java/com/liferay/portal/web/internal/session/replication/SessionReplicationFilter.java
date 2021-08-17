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

package com.liferay.portal.web.internal.session.replication;

import com.liferay.portal.kernel.util.StringBundler;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Dante Wang
 */
public class SessionReplicationFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest =
				(HttpServletRequest)servletRequest;

			HttpServletRequestWrapper lastHttpServletRequestWrapper = null;

			while (httpServletRequest instanceof HttpServletRequestWrapper) {
				lastHttpServletRequestWrapper =
					(HttpServletRequestWrapper)httpServletRequest;

				httpServletRequest =
					(HttpServletRequest)
						lastHttpServletRequestWrapper.getRequest();
			}

			Class<?> clazz = httpServletRequest.getClass();

			String className = clazz.getName();

			if (!className.equals(_ASM_WRAPPER_CLASS_NAME)) {
				HttpServletRequest sessionReplicationHttpServletRequest =
					SessionReplicationHttpServletRequestDelegate.create(
						httpServletRequest);

				if (lastHttpServletRequestWrapper == null) {
					servletRequest = sessionReplicationHttpServletRequest;
				}
				else {
					lastHttpServletRequestWrapper.setRequest(
						sessionReplicationHttpServletRequest);
				}
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	private static final String _ASM_WRAPPER_CLASS_NAME;

	static {
		Package pkg =
			SessionReplicationHttpServletRequestDelegate.class.getPackage();

		StringBundler sb = new StringBundler(4);

		sb.append(pkg.getName());
		sb.append(".");
		sb.append(HttpServletRequest.class.getSimpleName());
		sb.append("ASMWrapper");

		_ASM_WRAPPER_CLASS_NAME = sb.toString();
	}

}