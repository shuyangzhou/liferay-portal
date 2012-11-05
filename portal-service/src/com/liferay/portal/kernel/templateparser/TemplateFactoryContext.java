/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.templateparser;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;

import java.util.Map;

/**
 * @author Tina Tian
 */
public class TemplateFactoryContext {

	public TemplateFactoryContext(
		ThemeDisplay themeDisplay, Map<String, Object> contextObjects,
		String script, String langType) {

		if (Validator.isNull(script)) {
			throw new IllegalArgumentException("Script is null");
		}

		if (Validator.isNull(langType)) {
			throw new IllegalArgumentException("LangType is null");
		}

		_themeDisplay = themeDisplay;
		_contextObjects = contextObjects;
		_script = script;
		_langType = langType;
	}

	public TemplateFactoryContext(
		ThemeDisplay themeDisplay, Map<String, String> tokens, String viewMode,
		String languageId, String xml, String script, String langType) {
		if (Validator.isNull(script)) {
			throw new IllegalArgumentException("Script is null");
		}

		if (viewMode == null) {
			viewMode = Constants.VIEW;
		}

		_themeDisplay = themeDisplay;
		_tokens = tokens;
		_viewMode = viewMode;
		_languageId = languageId;
		_xml = xml;
		_script = script;
		_langType = langType;
	}

	public Map<String, Object> getContextObjects() {
		return _contextObjects;
	}

	public String getLangType() {
		return _langType;
	}

	public String getLanguageId() {
		return _languageId;
	}

	public String getScript() {
		return _script;
	}

	public ThemeDisplay getThemeDisplay() {
		return _themeDisplay;
	}

	public Map<String, String> getTokens() {
		return _tokens;
	}

	public String getViewMode() {
		return _viewMode;
	}

	public String getXML() {
		return _xml;
	}

	private Map<String, Object> _contextObjects;
	private String _langType;
	private String _languageId;
	private String _script;
	private ThemeDisplay _themeDisplay;
	private Map<String, String> _tokens;
	private String _viewMode;
	private String _xml;

}