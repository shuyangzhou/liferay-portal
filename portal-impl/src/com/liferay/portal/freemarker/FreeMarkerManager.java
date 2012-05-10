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

import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateContextType;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.template.RestrictedTemplate;
import com.liferay.portal.template.TemplateContextHelper;
import com.liferay.portal.template.TemplateResourceManager;
import com.liferay.portal.util.PropsValues;

import freemarker.template.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mika Koivisto
 * @author Tina Tina
 */
public class FreeMarkerManager implements TemplateManager {

	public void clearCache() {
		_templateResourceManager.clearCache();
	}

	public void clearCache(String templateId) {
		_templateResourceManager.clearCache(templateId);
	}

	public void destroy() {
		if (_configuration == null) {
			return;
		}

		_configuration.clearEncodingMap();
		_configuration.clearSharedVariables();
		_configuration.clearTemplateCache();

		_configuration = null;

		_restrictedHelperUtilities.clear();

		_restrictedHelperUtilities = null;

		_standardHelperUtilities.clear();

		_standardHelperUtilities = null;

		_templateContextHelper = null;

		_templateResourceManager.destroy();

		_templateResourceManager = null;
	}

	public Template getTemplate(
		String templateId, String templateContent, String errorTemplateId,
		String errorTemplateContent, TemplateContextType templateContextType) {

		if (templateContextType.equals(TemplateContextType.EMPTY)) {
			return new FreeMarkerTemplate(
					templateId, templateContent, errorTemplateId,
					errorTemplateContent, null, _configuration,
					_templateContextHelper, _templateResourceManager,
					_autoImportLibraries);
		}
		else if (templateContextType.equals(TemplateContextType.RESTRICTED)) {
			return new RestrictedTemplate(
				new FreeMarkerTemplate(
					templateId, templateContent, errorTemplateId,
					errorTemplateContent, _restrictedHelperUtilities,
					_configuration, _templateContextHelper,
					_templateResourceManager, _autoImportLibraries),
				_templateContextHelper.getRestrictedVariables());
		}
		else if (templateContextType.equals(TemplateContextType.STANDARD)) {
			return new FreeMarkerTemplate(
				templateId, templateContent, errorTemplateId,
				errorTemplateContent, _standardHelperUtilities, _configuration,
				_templateContextHelper, _templateResourceManager,
				_autoImportLibraries);
		}

		return null;
	}

	public Template getTemplate(
		String templateId, String templateContent, String errorTemplateId,
		TemplateContextType templateContextType) {

		return getTemplate(
			templateId, templateContent, errorTemplateId, null,
			templateContextType);
	}

	public Template getTemplate(
		String templateId, String templateContent,
		TemplateContextType templateContextType) {

		return getTemplate(
			templateId, templateContent, null, null, templateContextType);
	}

	public Template getTemplate(
		String templateId, TemplateContextType templateContextType) {

		return getTemplate(templateId, null, null, null, templateContextType);
	}

	public String getTemplateManagerName() {
		return TemplateManager.FREEMARKER;
	}

	public boolean hasTemplate(String templateId) {
		return _templateResourceManager.hasResource(templateId);
	}

	public void init() throws TemplateException {
		if (_configuration != null) {
			return;
		}

		_templateResourceManager = new TemplateResourceManager(
			TemplateManager.FREEMARKER);

		_templateResourceManager.setResourceLoaders(
			PropsValues.FREEMARKER_ENGINE_TEMPLATE_LOADERS);
		_templateResourceManager.setInterval(
			PropsValues.FREEMARKER_ENGINE_MODIFICATION_CHECK_INTERVAL);

		_configuration = new Configuration();

		_configuration.setDefaultEncoding(StringPool.UTF8);
		_configuration.setLocalizedLookup(
			PropsValues.FREEMARKER_ENGINE_LOCALIZED_LOOKUP);
		_configuration.setObjectWrapper(new LiferayObjectWrapper());

		try {
			_configuration.setSetting(
				"template_exception_handler",
				PropsValues.FREEMARKER_ENGINE_TEMPLATE_EXCEPTION_HANDLER);
		}
		catch (Exception e) {
			throw new TemplateException("Unable to init freemarker manager", e);
		}

		for (String macroLibrary :
			PropsValues.FREEMARKER_ENGINE_MACRO_LIBRARY) {

			int index = macroLibrary.indexOf(StringPool.COLON);

			String libraryFile = macroLibrary.substring(0, index).trim();
			String libraryName = macroLibrary.substring(index + 1).trim();

			_autoImportLibraries.put(libraryName, libraryFile);
		}

		_standardHelperUtilities = _templateContextHelper.getHelperUtilities();
		_restrictedHelperUtilities =
			_templateContextHelper.getRestrictedHelperUtilities();
	}

	public void setTemplateContextHelper(
		TemplateContextHelper templateContextHelper) {

		_templateContextHelper = templateContextHelper;
	}

	private Map<String, String> _autoImportLibraries =
		new HashMap<String, String>();
	private Configuration _configuration;
	private Map<String, Object> _restrictedHelperUtilities;
	private Map<String, Object> _standardHelperUtilities;
	private TemplateContextHelper _templateContextHelper;
	private TemplateResourceManager _templateResourceManager;

}