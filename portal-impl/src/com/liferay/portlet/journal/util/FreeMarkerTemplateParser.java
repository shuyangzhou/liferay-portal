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

import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.templateparser.TemplateContext;
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

		super(themeDisplay, contextObjects, script);
	}

	public FreeMarkerTemplateParser(
		ThemeDisplay themeDisplay, Map<String, String> tokens, String viewMode,
		String languageId, String xml, String script) {

		super(themeDisplay, tokens, viewMode, languageId, xml, script);
	}

	@Override
	protected String getErrorTemplateId() {
		return PropsValues.JOURNAL_ERROR_TEMPLATE_FREEMARKER;
	}

	@Override
	protected TemplateContext getTemplateContext() throws Exception {
		TemplateResource templateResource = new StringTemplateResource(
			getTemplateId(), script);

		return TemplateManagerUtil.getTemplate(
			TemplateConstants.LANG_TYPE_FTL, templateResource,
			getErrorTemplateResource(), TemplateContextType.RESTRICTED);
	}

}