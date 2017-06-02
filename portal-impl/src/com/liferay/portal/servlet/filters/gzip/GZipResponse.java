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

	private ServletOutputStream _createGZipServletOutputStream(
			ServletOutputStream servletOutputStream)
		throws IOException {

		if (_isGZipContentType() || !_setGZipHeader()) {
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

	private boolean _setGZipHeader() {
		if (isCommitted() || getStatus() != SC_OK) {
			return false;
		}

		HttpServletResponse rootHttpServletResponse =
			(HttpServletResponse)getResponse();

		while (rootHttpServletResponse instanceof HttpServletResponseWrapper) {
			HttpServletResponseWrapper httpServletResponseWrapper =
				(HttpServletResponseWrapper)rootHttpServletResponse;

			rootHttpServletResponse =
				(HttpServletResponse)httpServletResponseWrapper.getResponse();
		}

		rootHttpServletResponse.setHeader(HttpHeaders.CONTENT_ENCODING, _GZIP);

		if (!_GZIP.equals(
				rootHttpServletResponse.getHeader(
					HttpHeaders.CONTENT_ENCODING))) {

			// Unable to set header, it means we are in include mode, give up
			// gzip

			return false;
		}

		rootHttpServletResponse.setContentLength(-1);

		return true;
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