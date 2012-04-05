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

package com.liferay.portal.freemarker;

import com.liferay.portal.kernel.template.engine.TemplateEngineContext;
import com.liferay.portal.kernel.template.engine.TemplateEngineException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Theme;
import com.liferay.portal.template.engine.DefaultTemplateEngineVariablesImpl;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.velocity.VelocityPortletPreferences;

import freemarker.ext.beans.BeansWrapper;

import freemarker.template.utility.ObjectConstructor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mika Koivisto
 * @author Raymond Augé
 */
public class FreeMarkerVariablesImpl
	extends DefaultTemplateEngineVariablesImpl {

	@Override
	public void insertHelperUtilities(
		TemplateEngineContext templateEngineContext) {

		super.insertHelperUtilities(templateEngineContext);

		// Enum util

		templateEngineContext.put(
			"enumUtil", BeansWrapper.getDefaultInstance().getEnumModels());

		// Object util

		templateEngineContext.put("objectUtil", new ObjectConstructor());

		// Portlet preferences

		templateEngineContext.put(
			"freeMarkerPortletPreferences", new VelocityPortletPreferences());

		// Static class util

		templateEngineContext.put(
			"staticUtil", BeansWrapper.getDefaultInstance().getStaticModels());
	}

	@Override
	public void insertRequestVariables(
			TemplateEngineContext templateEngineContext,
			HttpServletRequest request)
		throws TemplateEngineException {

		super.insertRequestVariables(templateEngineContext, request);

		// Theme display

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			Theme theme = themeDisplay.getTheme();

			// Full css and templates path

			String servletContextName = GetterUtil.getString(
				theme.getServletContextName());

			templateEngineContext.put(
				"fullCssPath",
				StringPool.SLASH + servletContextName +
					theme.getFreeMarkerTemplateLoader() + theme.getCssPath());

			templateEngineContext.put(
				"fullTemplatesPath",
				StringPool.SLASH + servletContextName +
					theme.getFreeMarkerTemplateLoader() +
						theme.getTemplatesPath());

			// Init

			templateEngineContext.put(
				"init",
				StringPool.SLASH + themeDisplay.getPathContext() +
					FreeMarkerTemplateLoader.SERVLET_SEPARATOR +
						"/html/themes/_unstyled/templates/init.ftl");
		}

		// Insert custom ftl variables

		Map<String, Object> ftlVariables =
			(Map<String, Object>)request.getAttribute(WebKeys.FTL_VARIABLES);

		if (ftlVariables != null) {
			for (Map.Entry<String, Object> entry : ftlVariables.entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();

				if (Validator.isNotNull(key)) {
					templateEngineContext.put(key, value);
				}
			}
		}
	}

}