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

import com.liferay.portal.kernel.util.GetterUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 * @author Rachael Koestartyo
 */
public class JSONUtil {

	public static Object getValue(Object object, String... paths) {
		Object value = null;

		String[] parts = paths[0].split("/");

		String type = parts[0];
		String key = parts[1];

		if (type.equals("JSONArray")) {
			JSONObject jsonObject = (JSONObject)object;

			value = jsonObject.getJSONArray(key);
		}
		else if (type.equals("JSONObject")) {
			JSONObject jsonObject = (JSONObject)object;

			value = jsonObject.getJSONObject(key);
		}
		else if (type.equals("Object")) {
			if (object instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray)object;

				value = jsonArray.get(GetterUtil.getInteger(key));
			}
			else if (object instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject)object;

				value = jsonObject.get(key);
			}
		}

		if (paths.length == 1) {
			return value;
		}

		return getValue(value, Arrays.copyOfRange(paths, 1, paths.length));
	}

	public static boolean hasValue(JSONArray jsonArray, Object value) {
		for (int i = 0; i < jsonArray.length(); i++) {
			if (Objects.equals(value, jsonArray.get(i))) {
				return true;
			}
		}

		return false;
	}

	public static JSONObject merge(
			JSONObject jsonObject1, JSONObject jsonObject2)
		throws JSONException {

		if (jsonObject1 == null) {
			return JSONFactoryUtil.createJSONObject(jsonObject2.toString());
		}

		if (jsonObject2 == null) {
			return JSONFactoryUtil.createJSONObject(jsonObject1.toString());
		}

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			jsonObject1.toString());

		Iterator<String> iterator = jsonObject2.keys();

		while (iterator.hasNext()) {
			String key = iterator.next();

			jsonObject.put(key, jsonObject2.get(key));
		}

		return jsonObject;
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

	public static long[] toLongArray(JSONArray jsonArray) {
		if (jsonArray == null) {
			return new long[0];
		}

		long[] values = new long[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			values[i] = jsonArray.getLong(i);
		}

		return values;
	}

	public static long[] toLongArray(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return new long[0];
		}

		long[] values = new long[jsonArray.length()];

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			values[i] = jsonObject.getLong(jsonObjectKey);
		}

		return values;
	}

	public static Set<Long> toLongSet(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return Collections.emptySet();
		}

		Set<Long> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			values.add(jsonObject.getLong(jsonObjectKey));
		}

		return values;
	}

	public static List<String> toStringList(JSONArray jsonArray) {
		if (jsonArray == null) {
			return Collections.emptyList();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			values.add(jsonArray.getString(i));
		}

		return values;
	}

	public static List<String> toStringList(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return Collections.emptyList();
		}

		List<String> values = new ArrayList<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			values.add(jsonObject.getString(jsonObjectKey));
		}

		return values;
	}

	public static Set<String> toStringSet(
		JSONArray jsonArray, String jsonObjectKey) {

		if (jsonArray == null) {
			return Collections.emptySet();
		}

		Set<String> values = new HashSet<>(jsonArray.length());

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);

			values.add(jsonObject.getString(jsonObjectKey));
		}

		return values;
	}

}