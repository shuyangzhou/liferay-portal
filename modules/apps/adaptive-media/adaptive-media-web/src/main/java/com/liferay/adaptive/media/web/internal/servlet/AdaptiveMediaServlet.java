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

package com.liferay.adaptive.media.web.internal.servlet;

import com.liferay.adaptive.media.AdaptiveMedia;
import com.liferay.adaptive.media.AdaptiveMediaAttribute;
import com.liferay.adaptive.media.exception.AdaptiveMediaException;
import com.liferay.adaptive.media.handler.AdaptiveMediaRequestHandler;
import com.liferay.adaptive.media.web.internal.constants.AdaptiveMediaWebConstants;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.servlet.name=com.liferay.adaptive.media.web.internal.servlet.AdaptiveMediaServlet",
		"osgi.http.whiteboard.servlet.pattern=/" + AdaptiveMediaWebConstants.SERVLET_PATH + "/*",
		"servlet.init.httpMethods=GET,HEAD"
	},
	service = Servlet.class
)
public class AdaptiveMediaServlet extends HttpServlet {

	@Reference(unbind = "-")
	public void setAdaptiveMediaRequestHandlerLocator(
		AdaptiveMediaRequestHandlerLocator adaptiveMediaRequestHandlerLocator) {

		_adaptiveMediaRequestHandlerLocator =
			adaptiveMediaRequestHandlerLocator;
	}

	@Override
	protected void doGet(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		try {
			AdaptiveMediaRequestHandler requestHandler =
				_adaptiveMediaRequestHandlerLocator.locateForPattern(
					_getRequestHandlerPattern(request));

			if (requestHandler == null) {
				response.sendError(
					HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());

				return;
			}

			Optional<AdaptiveMedia<?>> adaptiveMediaOptional =
				requestHandler.handleRequest(request);

			AdaptiveMedia<?> adaptiveMedia = adaptiveMediaOptional.orElseThrow(
				AdaptiveMediaException.AdaptiveMediaNotFound::new);

			Optional<Integer> contentLengthOptional =
				adaptiveMedia.getValueOptional(
					AdaptiveMediaAttribute.getContentLength());

			Integer contentLength = contentLengthOptional.orElse(0);

			Optional<String> contentTypeOptional =
				adaptiveMedia.getValueOptional(
					AdaptiveMediaAttribute.getContentType());

			String contentType = contentTypeOptional.orElse(
				ContentTypes.APPLICATION_OCTET_STREAM);

			Optional<String> fileNameOptional = adaptiveMedia.getValueOptional(
				AdaptiveMediaAttribute.getFileName());

			String fileName = fileNameOptional.orElse(null);

			boolean download = ParamUtil.getBoolean(request, "download");

			if (download) {
				ServletResponseUtil.sendFile(
					request, response, fileName, adaptiveMedia.getInputStream(),
					contentLength, contentType,
					HttpHeaders.CONTENT_DISPOSITION_ATTACHMENT);
			}
			else {
				ServletResponseUtil.sendFile(
					request, response, fileName, adaptiveMedia.getInputStream(),
					contentLength, contentType);
			}
		}
		catch (AdaptiveMediaException.AdaptiveMediaNotFound amnf) {
			response.sendError(
				HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
		}
		catch (Exception e) {
			Throwable cause = e.getCause();

			if (cause instanceof PrincipalException) {
				response.sendError(
					HttpServletResponse.SC_FORBIDDEN, request.getRequestURI());
			}
			else {
				response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					request.getRequestURI());
			}
		}
	}

	@Override
	protected void doHead(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		doGet(request, response);
	}

	private String _getRequestHandlerPattern(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();

		Matcher matcher = _REQUEST_HANDLER_PATTERN.matcher(pathInfo);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return StringPool.BLANK;
	}

	private static final Pattern _REQUEST_HANDLER_PATTERN = Pattern.compile(
		"^/([^/]*)");

	private AdaptiveMediaRequestHandlerLocator
		_adaptiveMediaRequestHandlerLocator;

}