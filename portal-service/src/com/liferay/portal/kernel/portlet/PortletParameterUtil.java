/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.portlet;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * @author Shuyang Zhou
 * @author Sampsa Sohlman
 */
public class PortletParameterUtil {

	public static String addNamespace(String portletId, String queryString) {
		String[] parameters = StringUtil.split(queryString, CharPool.AMPERSAND);

		StringBundler sb = new StringBundler(2 + parameters.length * 4);

		for (int i = 0; i < parameters.length; i++) {
			if (i > 0) {
				sb.append("&");
			}

			sb.append("_");
			sb.append(portletId);
			sb.append("_");
			sb.append(parameters[i]);
		}

		return sb.toString();
	}

}