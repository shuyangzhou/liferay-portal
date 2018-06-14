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

package com.liferay.portlet;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Dante Wang
 */
public class AsyncPortletServletRequest extends HttpServletRequestWrapper {

	public static AsyncPortletServletRequest getAsyncPortletServletRequest(
		HttpServletRequest httpServletRequest) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public AsyncPortletServletRequest(HttpServletRequest request) {
		super(request);

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public String getContextPath() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public DispatcherType getDispatcherType() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public String getPathInfo() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryString() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public String getRequestURI() {

		// TODO

		throw new UnsupportedOperationException();
	}

	@Override
	public String getServletPath() {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void setContextPath(String contextPath) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void setPathInfo(String pathInfo) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void setQueryString(String queryString) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void setRequestURI(String requestUri) {

		// TODO

		throw new UnsupportedOperationException();
	}

	public void setServletPath(String servletPath) {

		// TODO

		throw new UnsupportedOperationException();
	}

}