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

import com.liferay.portal.asm.ASMWrapperUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Dante Wang
 */
public class SessionReplicationHttpServletRequestDelegate {

	public static HttpServletRequest create(
		HttpServletRequest httpServletRequest) {

		return ASMWrapperUtil.createASMWrapper(
			SessionReplicationHttpServletRequestDelegate.class.getClassLoader(),
			HttpServletRequest.class,
			new SessionReplicationHttpServletRequestDelegate(
				httpServletRequest),
			httpServletRequest);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof HttpServletRequest)) {
			return false;
		}

		HttpServletRequest httpServletRequest = (HttpServletRequest)object;

		return httpServletRequest.equals(_httpServletRequest);
	}

	public HttpSession getSession() {
		HttpSession httpSession = _httpServletRequest.getSession();

		if (httpSession == null) {
			return null;
		}

		return new SessionReplicationHttpSessionWrapper(httpSession);
	}

	public HttpSession getSession(boolean create) {
		HttpSession httpSession = _httpServletRequest.getSession(create);

		if (httpSession == null) {
			return null;
		}

		return new SessionReplicationHttpSessionWrapper(httpSession);
	}

	@Override
	public int hashCode() {
		return _httpServletRequest.hashCode();
	}

	private SessionReplicationHttpServletRequestDelegate(
		HttpServletRequest httpServletRequest) {

		_httpServletRequest = httpServletRequest;
	}

	private final HttpServletRequest _httpServletRequest;

}