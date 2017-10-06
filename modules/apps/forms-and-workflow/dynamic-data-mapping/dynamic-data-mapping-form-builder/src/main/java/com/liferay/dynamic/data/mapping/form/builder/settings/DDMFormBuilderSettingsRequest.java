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

package com.liferay.dynamic.data.mapping.form.builder.settings;

import com.liferay.dynamic.data.mapping.model.DDMForm;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Rafael Praxedes
 */
public class DDMFormBuilderSettingsRequest {

	public static DDMFormBuilderSettingsRequest with(
		DDMForm ddmForm, Locale locale) {

		DDMFormBuilderSettingsRequest ddmFormBuilderSettingsRequest =
			new DDMFormBuilderSettingsRequest();

		ddmFormBuilderSettingsRequest.setDDMForm(ddmForm);
		ddmFormBuilderSettingsRequest.setLocale(locale);

		return ddmFormBuilderSettingsRequest;
	}

	public void addProperty(String key, Object value) {
		_properties.put(key, value);
	}

	public DDMForm getDDMForm() {
		return getProperty("ddmForm");
	}

	public Locale getLocale() {
		return getProperty("locale");
	}

	public <T> T getProperty(String name) {
		return (T)_properties.get(name);
	}

	public void setDDMForm(DDMForm ddmForm) {
		addProperty("ddmForm", ddmForm);
	}

	public void setLocale(Locale locale) {
		addProperty("locale", locale);
	}

	private DDMFormBuilderSettingsRequest() {
	}

	private final Map<String, Object> _properties = new HashMap<>();

}