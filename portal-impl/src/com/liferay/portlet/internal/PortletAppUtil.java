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

package com.liferay.portlet.internal;

import com.liferay.portlet.extra.config.ExtraPortletAppConfig;
import com.liferay.portlet.extra.config.ExtraPortletAppConfigRegistry;

import javax.servlet.ServletContext;

/**
 * @author Neil Griffin
 */
public class PortletAppUtil {

	public static int getPortletSpecMajorVersion(
		ExtraPortletAppConfig extraPortletAppConfig) {

		if (extraPortletAppConfig == null) {
			return 2;
		}

		return extraPortletAppConfig.getPortletSpecMajorVersion();
	}

	public static int getPortletSpecMajorVersion(
		ServletContext servletContext) {

		return getPortletSpecMajorVersion(
			servletContext.getServletContextName());
	}

	public static int getPortletSpecMajorVersion(String servletContextName) {
		ExtraPortletAppConfig extraPortletAppConfig =
			ExtraPortletAppConfigRegistry.getExtraPortletAppConfig(
				servletContextName);

		return getPortletSpecMajorVersion(extraPortletAppConfig);
	}

	public static boolean isPortletSpec3(
		ExtraPortletAppConfig extraPortletAppConfig) {

		if (getPortletSpecMajorVersion(extraPortletAppConfig) == 3) {
			return true;
		}

		return false;
	}

	public static boolean isPortletSpec3(ServletContext servletContext) {
		return isPortletSpec3(servletContext.getServletContextName());
	}

	public static boolean isPortletSpec3(String servletContextName) {
		ExtraPortletAppConfig extraPortletAppConfig =
			ExtraPortletAppConfigRegistry.getExtraPortletAppConfig(
				servletContextName);

		return isPortletSpec3(extraPortletAppConfig);
	}

}