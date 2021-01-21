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

package com.liferay.portal.configuration.persistence;

import com.liferay.petra.string.CharPool;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.Constants;

/**
 * @author Shuyang Zhou
 */
public class ConfigurationOverridePropertiesUtil {

	public static Map<String, String> getOverrideProperties(String pid) {
		return _overridePropertiesMap.get(pid);
	}

	public static Map<String, Map<String, String>> getOverridePropertiesMap() {
		return _overridePropertiesMap;
	}

	private static final Map<String, Map<String, String>>
		_overridePropertiesMap;

	static {
		Properties properties = PropsUtil.getProperties(
			"configuration.override.", true);

		Map<String, Map<String, String>> overridePropertiesMap =
			new HashMap<>();

		for (String key : properties.stringPropertyNames()) {
			int index = key.indexOf(CharPool.UNDERLINE);

			if (index > 0) {
				Map<String, String> overrideProperties =
					overridePropertiesMap.computeIfAbsent(
						key.substring(0, index), pid -> new HashMap<>());

				overrideProperties.put(
					key.substring(index + 1), properties.getProperty(key));
			}
		}

		for (Map.Entry<String, Map<String, String>> entry :
				overridePropertiesMap.entrySet()) {

			Map<String, String> map = entry.getValue();

			map.put(Constants.SERVICE_PID, entry.getKey());

			entry.setValue(Collections.unmodifiableMap(map));
		}

		_overridePropertiesMap = Collections.unmodifiableMap(
			overridePropertiesMap);
	}

}