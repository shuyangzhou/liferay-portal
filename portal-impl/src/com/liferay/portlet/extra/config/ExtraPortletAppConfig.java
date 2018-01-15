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

package com.liferay.portlet.extra.config;

import java.util.Map;

/**
 * @author Leon Chi
 * @author Neil Griffin
 */
public class ExtraPortletAppConfig {

	public String getEncoding(String locale) {
		return _localeEncodings.get(locale);
	}

	public Map<String, String> getLocaleEncodings() {
		return _localeEncodings;
	}

	public int getPortletSpecMajorVersion() {
		return _portletSpecMajorVersion;
	}

	public int getPortletSpecMinorVersion() {
		return _portletSpecMinorVersion;
	}

	public void setLocaleEncodings(Map<String, String> localeEncodings) {
		_localeEncodings = localeEncodings;
	}

	public void setPortletSpecMajorVersion(int portletSpecMajorVersion) {
		_portletSpecMajorVersion = portletSpecMajorVersion;
	}

	public void setPortletSpecMinorVersion(int portletSpecMinorVersion) {
		_portletSpecMinorVersion = portletSpecMinorVersion;
	}

	private Map<String, String> _localeEncodings;
	private int _portletSpecMajorVersion;
	private int _portletSpecMinorVersion;

}