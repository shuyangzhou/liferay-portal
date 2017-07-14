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

import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;

import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Minhchau Dang
 */
public class ResourceUtil {

	public static ServletContext getPathServletContext(
			HttpServletRequest request, ServletContext defaultServletContext)
		throws IOException {

		String requestPath = getRequestPath(request);

		if (requestPath.endsWith(".css") && PortalUtil.isRightToLeft(request)) {
			int pos = requestPath.lastIndexOf(StringPool.PERIOD);

			requestPath =
				requestPath.substring(0, pos) + "_rtl" +
					requestPath.substring(pos);
		}

		String requestURI = request.getRequestURI();

		return getPathServletContext(
			requestPath, requestURI, defaultServletContext);
	}

	public static ServletContext getPathServletContext(
			String requestPath, String requestURI,
			ServletContext defaultServletContext)
		throws IOException {

		URL resourceURL = defaultServletContext.getResource(requestPath);

		if (resourceURL != null) {
			return defaultServletContext;
		}

		ServletContext resourceServletContext =
			PortalWebResourcesUtil.getPathServletContext(requestURI);

		if (resourceServletContext != null) {
			resourceURL = PortalWebResourcesUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceServletContext;
			}
		}

		resourceServletContext = PortletResourcesUtil.getPathServletContext(
			requestURI);

		if (resourceServletContext != null) {
			resourceURL = PortletResourcesUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceServletContext;
			}
		}

		resourceServletContext =
			DynamicResourceIncludeUtil.getPathServletContext(requestURI);

		if (resourceServletContext != null) {
			resourceURL = DynamicResourceIncludeUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceServletContext;
			}
		}

		return defaultServletContext;
	}

	public static URL getResource(
			HttpServletRequest request, ServletContext defaultServletContext)
		throws IOException {

		String requestPath = getRequestPath(request);

		if (requestPath.endsWith(".css") && PortalUtil.isRightToLeft(request)) {
			int pos = requestPath.lastIndexOf(StringPool.PERIOD);

			requestPath =
				requestPath.substring(0, pos) + "_rtl" +
					requestPath.substring(pos);
		}

		String requestURI = request.getRequestURI();

		return getResource(requestPath, requestURI, defaultServletContext);
	}

	public static URL getResource(
			String requestPath, String requestURI,
			ServletContext defaultServletContext)
		throws IOException {

		URL resourceURL = defaultServletContext.getResource(requestPath);

		if (resourceURL != null) {
			return resourceURL;
		}

		ServletContext resourceServletContext =
			PortalWebResourcesUtil.getPathServletContext(requestURI);

		if (resourceServletContext != null) {
			resourceURL = PortalWebResourcesUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceURL;
			}
		}

		resourceServletContext = PortletResourcesUtil.getPathServletContext(
			requestURI);

		if (resourceServletContext != null) {
			resourceURL = PortletResourcesUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceURL;
			}
		}

		resourceServletContext =
			DynamicResourceIncludeUtil.getPathServletContext(requestURI);

		if (resourceServletContext != null) {
			resourceURL = DynamicResourceIncludeUtil.getResource(
				resourceServletContext, requestURI);

			if (resourceURL != null) {
				return resourceURL;
			}
		}

		return null;
	}

	protected static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI();

		String contextPath = request.getContextPath();

		if (!contextPath.equals(StringPool.SLASH)) {
			requestPath = requestPath.substring(contextPath.length());
		}

		return requestPath;
	}

}