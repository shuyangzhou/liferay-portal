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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Brian Wing Shun Chan
 */
public class TrackedServletRequest extends HttpServletRequestWrapper {

	public TrackedServletRequest(HttpServletRequest request) {
		super(request);
	}

	public Object getAttribute(String name) {
		Object object = _attributes.get(name);

		if (object != null) {
			return object;
		}

		return super.getAttribute(name);
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public Set<String> getSetAttributes() {
		return _attributes.keySet();
	}

	@Override
	public void setAttribute(String name, Object obj) {
		_attributes.put(name, obj);
	}

	private final Map<String, Object> _attributes = new HashMap<>();

}