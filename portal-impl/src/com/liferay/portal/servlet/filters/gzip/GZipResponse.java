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

package com.liferay.portal.servlet.filters.gzip;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletOutputStreamAdapter;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Jayson Falkner
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class GZipResponse extends HttpServletResponseWrapper {

	public GZipResponse(HttpServletResponse response) {
		super(response);

		// Clear previous content length setting. GZip response does not buffer
		// output to get final content length. The response will be chunked
		// unless an outer filter calculates the content length.

		response.setContentLength(-1);

		// Setting the header after finishResponse is too late

		response.addHeader(HttpHeaders.CONTENT_ENCODING, _GZIP);
	}

	public void finishResponse() throws IOException {
		if (_printWriter != null) {
			_printWriter.close();
		}
		else if (_servletOutputStream != null) {
			_servletOutputStream.close();
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		if (_servletOutputStream != null) {
			_servletOutputStream.flush();
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (_printWriter != null) {
			throw new IllegalStateException();
		}

		if (_servletOutputStream == null) {
			_servletOutputStream = _createGZipServletOutputStream(
				super.getOutputStream());
		}

		return _servletOutputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (_printWriter != null) {
			return _printWriter;
		}

		if (_servletOutputStream != null) {
			throw new IllegalStateException();
		}

		if (_log.isWarnEnabled()) {
			_log.warn("Use getOutputStream for optimum performance");
		}

		_servletOutputStream = getOutputStream();

		_printWriter = UnsyncPrintWriterPool.borrow(
			_servletOutputStream, getCharacterEncoding());

		return _printWriter;
	}

	@Override
	public void setContentLength(int contentLength) {
	}

	@Override
	public void setHeader(String name, String value) {
		if (HttpHeaders.CONTENT_LENGTH.equals(name)) {
			return;
		}

		super.setHeader(name, value);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void setStatus(int sc, String sm) {
		super.setStatus(sc, sm);

		if (super.getStatus() != SC_OK) {
			_disableGZip();
		}
	}

	@Override
	public void setStatus(int sc) {
		setStatus(sc, null);
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		_disableGZip();

		super.sendRedirect(location);
	}

	@Override
	public void sendError(int sc) throws IOException {
		sendError(sc, null);
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		_disableGZip();

		super.sendError(sc, msg);
	}

	private void _disableGZip() {
		if ((_servletOutputStream != null) || isCommitted()) {
			return;
		}

		HttpServletResponse rootHttpServletResponse =
			(HttpServletResponse)getResponse();

		while (rootHttpServletResponse instanceof HttpServletResponseWrapper) {
			HttpServletResponseWrapper httpServletResponseWrapper =
				(HttpServletResponseWrapper)rootHttpServletResponse;

			rootHttpServletResponse =
				(HttpServletResponse)httpServletResponseWrapper.getResponse();
		}

		Map<String, Collection<String>> headerMap = new HashMap<>();

		for (String headerName : rootHttpServletResponse.getHeaderNames()) {
			Collection<String> headerValues =
				rootHttpServletResponse.getHeaders(headerName);

			if (headerName.equals(HttpHeaders.CONTENT_ENCODING)) {
				headerValues = new ArrayList<>(headerValues);

				headerValues.remove(_GZIP);

				if (headerValues.isEmpty()) {
					continue;
				}
			}

			headerMap.put(headerName, headerValues);
		}

		int statusCode = rootHttpServletResponse.getStatus();

		super.reset();

		rootHttpServletResponse.setStatus(statusCode);

		for (Entry<String, Collection<String>> entry : headerMap.entrySet()) {
			String headerName = entry.getKey();

			for (String headerValue : entry.getValue()) {
				rootHttpServletResponse.addHeader(headerName, headerValue);
			}
		}

		Collection<String> headerValues = rootHttpServletResponse.getHeaders(
			HttpHeaders.CONTENT_ENCODING);

		if (headerValues.contains(_GZIP)) {

			// Reponse is not committed, but we failed to remove the gzip
			// header, that could only mean we are in include mode which we
			// could not disable gzip.

			return;
		}

		_disabled = true;
	}

	private boolean _disabled;

	private ServletOutputStream _createGZipServletOutputStream(
			ServletOutputStream servletOutputStream)
		throws IOException {

		if (_isGZipContentType() || _disabled) {
			return servletOutputStream;
		}

		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(
			servletOutputStream) {

			{
				def.setLevel(PropsValues.GZIP_COMPRESSION_LEVEL);
			}

		};

		return new ServletOutputStreamAdapter(gzipOutputStream);
	}

	private boolean _isGZipContentType() {
		String contentType = getContentType();

		if (contentType != null) {
			if (contentType.equals(ContentTypes.APPLICATION_GZIP) ||
				contentType.equals(ContentTypes.APPLICATION_X_GZIP)) {

				return true;
			}
		}

		return false;
	}

	private static final String _GZIP = "gzip";

	private static final Log _log = LogFactoryUtil.getLog(GZipResponse.class);

	private PrintWriter _printWriter;
	private ServletOutputStream _servletOutputStream;

}