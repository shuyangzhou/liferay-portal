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

package com.liferay.portal.kernel.editor.config;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class BaseEditorConfigContributor
	implements EditorConfigContributor {

	protected JSONArray toJSONArray(String json) {
		try {
			return JSONFactoryUtil.createJSONArray(json);
		}
		catch (JSONException jsone) {
			_log.error("Unable to create a JSON array from: " + json, jsone);
		}

		return JSONFactoryUtil.createJSONArray();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseEditorConfigContributor.class);

}