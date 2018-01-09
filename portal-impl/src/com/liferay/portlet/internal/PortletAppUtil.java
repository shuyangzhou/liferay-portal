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

import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.Map;

/**
 * @author Neil Griffin
 */
public class PortletAppUtil {

	public static int getPortletSpecMajorVersion(PortletApp portletApp) {
		String[] portletSpecVersion = getPortletSpecVersion(portletApp);

		if ((portletSpecVersion != null) && (portletSpecVersion.length > 0)) {
			return GetterUtil.getInteger(portletSpecVersion[0], 2);
		}

		return 2;
	}

	public static String[] getPortletSpecVersion(PortletApp portletApp) {
		Map<String, String[]> containerRuntimeOptions =
			portletApp.getContainerRuntimeOptions();

		return containerRuntimeOptions.get("com.liferay.portlet.spec.version");
	}

	public static boolean isPortletSpec3(PortletApp portletApp) {
		if (getPortletSpecMajorVersion(portletApp) == 3) {
			return true;
		}

		return false;
	}

}