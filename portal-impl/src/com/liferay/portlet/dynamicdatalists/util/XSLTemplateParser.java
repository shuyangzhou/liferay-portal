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

package com.liferay.portlet.dynamicdatalists.util;

import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;

import java.util.Map;

/**
 * @author Marcellus Tavares
 * @author Tina Tian
 */
public class XSLTemplateParser extends
	com.liferay.portlet.journal.util.XSLTemplateParser {

	public XSLTemplateParser(
		ThemeDisplay themeDisplay, Map<String, Object> contextObjects,
		String script) {

		super(themeDisplay, contextObjects, script);
	}

	public XSLTemplateParser(
		ThemeDisplay themeDisplay, Map<String, String> tokens, String viewMode,
		String languageId, String xml, String script) {

		super(themeDisplay, tokens, viewMode, languageId, xml, script);
	}

	@Override
	protected String getErrorTemplateId() {
		return PropsValues.DYNAMIC_DATA_LISTS_ERROR_TEMPLATE_XSL;
	}

}