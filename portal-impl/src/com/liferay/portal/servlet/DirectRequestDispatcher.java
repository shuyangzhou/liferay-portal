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

package com.liferay.portal.servlet;

import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Shuyang Zhou
 */
public class DirectRequestDispatcher implements RequestDispatcher {

	public DirectRequestDispatcher(
		Servlet servlet, String path, String queryString) {

		_servlet = servlet;
		_path = path;
		_queryString = queryString;
	}

	@Override
	public void forward(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		servletRequest = DynamicServletRequest.addQueryString(
			(HttpServletRequest)servletRequest, _queryString);

		_servlet.service(servletRequest, servletResponse);
	}

	@Override
	public void include(
			ServletRequest servletRequest, ServletResponse servletResponse)
		throws IOException, ServletException {

		String includePathInfo = (String)servletRequest.getAttribute(
			RequestDispatcher.INCLUDE_PATH_INFO);

		servletRequest.setAttribute(RequestDispatcher.INCLUDE_PATH_INFO, null);

		String includeServletPath = (String)servletRequest.getAttribute(
			RequestDispatcher.INCLUDE_SERVLET_PATH);

		servletRequest.setAttribute(
			RequestDispatcher.INCLUDE_SERVLET_PATH, _path);

		String servletPath = (String)servletRequest.getAttribute(
			WebKeys.SERVLET_PATH);

		servletRequest.setAttribute(WebKeys.SERVLET_PATH, _path);

		servletRequest = DynamicServletRequest.addQueryString(
			(HttpServletRequest)servletRequest, _queryString);

		try {
			_servlet.service(servletRequest, servletResponse);
		}
		finally {
			servletRequest.setAttribute(
				RequestDispatcher.INCLUDE_PATH_INFO, includePathInfo);
			servletRequest.setAttribute(
				RequestDispatcher.INCLUDE_SERVLET_PATH, includeServletPath);
			servletRequest.setAttribute(WebKeys.SERVLET_PATH, servletPath);
		}
	}

	private final String _path;
	private final String _queryString;
	private final Servlet _servlet;

}