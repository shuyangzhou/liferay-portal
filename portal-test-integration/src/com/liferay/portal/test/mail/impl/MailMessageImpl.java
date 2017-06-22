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

package com.liferay.portal.test.mail.impl;

import com.liferay.portal.test.mail.MailMessage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Adam Brandizzi
 */
public class MailMessageImpl implements MailMessage {

	public MailMessageImpl(String body, Map<String, List<String>> headers) {
		_body = body;
		_headers = Collections.unmodifiableMap(headers);
	}

	@Override
	public String getBody() {
		return _body;
	}

	@Override
	public String getFirstHeaderValue(String headerName) {
		List<String> headerValues = getHeaderValues(headerName);

		return headerValues.get(0);
	}

	@Override
	public Set<String> getHeaderNames() {
		return _headers.keySet();
	}

	@Override
	public List<String> getHeaderValues(String headerName) {
		return _headers.get(headerName);
	}

	private final String _body;
	private final Map<String, List<String>> _headers;

}