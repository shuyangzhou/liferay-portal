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

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletOutputStreamAdapter;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.UnsyncPrintWriterPool;
import com.liferay.portal.util.PropsValues;
import com.liferay.util.RSSThreadLocal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author Jayson Falkner
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class GZipResponse extends HttpServletResponseWrapper {

	public GZipResponse(
		HttpServletRequest request, HttpServletResponse response) {

		super(response);

		_response = response;

		// Clear previous content length setting. GZip response does not buffer
		// output to get final content length. The response will be chunked
		// unless an outer filter calculates the content length.

		_response.setContentLength(-1);

		// Setting the header after finishResponse is too late

		_response.addHeader(HttpHeaders.CONTENT_ENCODING, _GZIP);

		_firefox = BrowserSnifferUtil.isFirefox(request);
	}

	public void finishResponse() throws IOException {

		// Is the response committed?

		if (!isCommitted()) {

			// Has the content been GZipped yet?

			if ((_servletOutputStream == null) ||
				((_servletOutputStream != null) &&
				 (_unsyncByteArrayOutputStream != null) &&
				 (_unsyncByteArrayOutputStream.size() == 0))) {

				Map<String, Collection<String>> headerMap = new HashMap<>();

				for (String headerName : _response.getHeaderNames()) {
					Collection<String> headerValues = _response.getHeaders(
						headerName);

					if (headerName.equals(HttpHeaders.CONTENT_ENCODING)) {
						headerValues.remove(_GZIP);

						if (headerValues.isEmpty()) {
							continue;
						}
					}

					headerMap.put(headerName, headerValues);
				}

				_response.reset();

				for (Entry<String, Collection<String>> entry :
						headerMap.entrySet()) {

					String headerName = entry.getKey();

					boolean first = true;

					for (String headerValue : entry.getValue()) {
						if (first) {
							_response.setHeader(headerName, headerValue);

							first = false;
						}
						else {
							_response.addHeader(headerName, headerValue);
						}
					}
				}
			}
		}

		try {
			if (_printWriter != null) {
				_printWriter.close();
			}
			else if (_servletOutputStream != null) {
				_servletOutputStream.close();
			}
		}
		catch (IOException ioe) {
		}

		if (_unsyncByteArrayOutputStream != null) {
			_response.setContentLength(_unsyncByteArrayOutputStream.size());

			_unsyncByteArrayOutputStream.writeTo(_response.getOutputStream());
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
			if (_isGZipContentType()) {
				_servletOutputStream = _response.getOutputStream();
			}
			else {
				if (_firefox && RSSThreadLocal.isExportRSS()) {
					_unsyncByteArrayOutputStream =
						new UnsyncByteArrayOutputStream();

					_servletOutputStream = _createGZipServletOutputStream(
						_unsyncByteArrayOutputStream);
				}
				else {
					_servletOutputStream = _createGZipServletOutputStream(
						_response.getOutputStream());
				}
			}
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

	private ServletOutputStream _createGZipServletOutputStream(
			OutputStream outputStream)
		throws IOException {

		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream) {

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

	private final boolean _firefox;
	private PrintWriter _printWriter;
	private final HttpServletResponse _response;
	private ServletOutputStream _servletOutputStream;
	private UnsyncByteArrayOutputStream _unsyncByteArrayOutputStream;

}