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

package com.liferay.portal.template;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.util.xml.XMLUtil;

import java.util.Collections;
import java.util.Map;

import javax.portlet.ReadOnlyException;

/**
 * @author Brian Wing Shun Chan
 * @author László Csontos
 */
public class TemplatePortletPreferences {

	public String getPreferences(Map<String, Object> preferences)
		throws ReadOnlyException {

		StringBundler sb = new StringBundler();

		sb.append("<portlet-preferences>");

		for (Map.Entry<String, Object> entry : preferences.entrySet()) {
			sb.append("<preference><name>");
			sb.append(entry.getKey());
			sb.append("</name>");

			String[] values = _NULL_VALUE;

			Object valueObject = entry.getValue();

			if (valueObject instanceof String) {
				values = new String[] {(String)valueObject};
			}
			else if (valueObject instanceof String[]) {
				values = (String[])valueObject;
			}

			for (String value : values) {
				sb.append("<value>");
				sb.append(XMLUtil.toCompactSafe(value));
				sb.append("</value>");
			}

			sb.append("</preference>");
		}

		sb.append("</portlet-preferences>");

		return sb.toString();
	}

	public String getPreferences(String key, String value)
		throws ReadOnlyException {

		return getPreferences(Collections.singletonMap(key, value));
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public void reset() {
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public void setValue(String key, String value) throws ReadOnlyException {
	}

	/**
	 * @deprecated As of 7.0.0, with no direct replacement
	 */
	@Deprecated
	public void setValues(String key, String[] values)
		throws ReadOnlyException {
	}

	private static final String[] _NULL_VALUE = new String[] {"NULL_VALUE"};

	private static final Log _log = LogFactoryUtil.getLog(
		TemplatePortletPreferences.class);

}