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

package com.liferay.portal.kernel.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Brian Wing Shun Chan
 * @author Rachael Koestartyo
 */
public class JSONUtil {

	public static boolean hasValue(JSONArray jsonArray, Object value) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (Objects.equals(value, jsonArray.get(i))) {
				return true;
			}
		}

		return false;
	}

	public static JSONArray put(Object value) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		jsonArray.put(value);

		return jsonArray;
	}

	public static JSONObject put(String key, Object value) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		return jsonObject.put(key, value);
	}

	public static List<String> toStringList(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return Collections.emptyList();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			String value = jsonObject.getString(jsonObjectKey);

			values.add(value);
		}

		return values;
	}

}