/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test.util;

import com.liferay.portal.util.PropsUtil;

import java.util.Collections;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class PropsTestUtil {

	public static void setProps(Map<String, Object> properties) {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			PropsUtil.set(entry.getKey(), entry.getValue());
		}
	}

	public static void setProps(String key, Object value) {
		setProps(Collections.singletonMap(key, value));
	}

}