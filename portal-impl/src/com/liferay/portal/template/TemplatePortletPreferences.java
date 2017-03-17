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
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.PortletPreferencesImpl;

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

		PortletPreferencesImpl portletPreferencesImpl =
			new PortletPreferencesImpl();

		for (Map.Entry<String, Object> entry : preferences.entrySet()) {
			Object value = entry.getValue();

			if (value instanceof String) {
				portletPreferencesImpl.setValue(entry.getKey(), (String)value);
			}
			else if (value instanceof String[]) {
				portletPreferencesImpl.setValues(
					entry.getKey(), (String[])value);
			}
		}

		try {
			return PortletPreferencesFactoryUtil.toXML(portletPreferencesImpl);
		}
		catch (Exception e) {
			_log.error(e, e);

			return PortletConstants.DEFAULT_PREFERENCES;
		}
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

	private static final Log _log = LogFactoryUtil.getLog(
		TemplatePortletPreferences.class);

}