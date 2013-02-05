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

package com.liferay.portlet.journal.util;

import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.templateparser.BaseTemplateParser;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;

import java.util.Map;

/**
 * @author Mika Koivisto
 */
public class FreeMarkerTemplateParser extends BaseTemplateParser {

	public FreeMarkerTemplateParser(
		ThemeDisplay themeDisplay, Map<String, Object> contextObjects,
		String script) {

		super(
			themeDisplay, contextObjects, script,
			PropsValues.JOURNAL_ERROR_TEMPLATE_FREEMARKER,
			TemplateConstants.LANG_TYPE_FTL, TemplateContextType.RESTRICTED);
	}

	public FreeMarkerTemplateParser(
		ThemeDisplay themeDisplay, Map<String, String> tokens, String viewMode,
		String languageId, String xml, String script) {

		super(
			themeDisplay, tokens, viewMode, languageId, xml, script,
			PropsValues.JOURNAL_ERROR_TEMPLATE_FREEMARKER,
			TemplateConstants.LANG_TYPE_FTL, TemplateContextType.RESTRICTED);
	}

}