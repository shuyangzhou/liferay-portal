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

package com.liferay.portal.kernel.model;

import com.liferay.portal.kernel.util.JavaConstants;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Eduardo Lundgren
 */
public class PortletInfo implements Serializable {

	public PortletInfo() {
		this(null, null, null, null);
	}

	public PortletInfo(
		String title, String shortTitle, String keywords, String description) {

		_title = title;
		_shortTitle = shortTitle;
		_keywords = keywords;
		_description = description;

		Map<String, String> map = new HashMap<>();

		if (_description != null) {
			map.put(JavaConstants.JAVAX_PORTLET_DESCRIPTION, _description);
		}

		if (_keywords != null) {
			map.put(JavaConstants.JAVAX_PORTLET_KEYWORDS, _keywords);
		}

		if (_shortTitle != null) {
			map.put(JavaConstants.JAVAX_PORTLET_SHORT_TITLE, _shortTitle);
		}

		if (_title != null) {
			map.put(JavaConstants.JAVAX_PORTLET_TITLE, _title);
		}

		_map = Collections.unmodifiableMap(map);
	}

	public String getDescription() {
		return _description;
	}

	public String getKeywords() {
		return _keywords;
	}

	public Map<String, String> getPortletInfoMap() {
		return _map;
	}

	public String getShortTitle() {
		return _shortTitle;
	}

	public String getTitle() {
		return _title;
	}

	private final String _description;
	private final String _keywords;
	private final Map<String, String> _map;
	private final String _shortTitle;
	private final String _title;

}