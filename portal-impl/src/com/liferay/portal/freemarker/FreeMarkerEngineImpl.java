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

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.engine.TemplateEngine;
import com.liferay.portal.kernel.template.engine.TemplateEngineContext;
import com.liferay.portal.kernel.template.engine.TemplateEngineException;
import com.liferay.portal.kernel.template.engine.TemplateEngineVariables;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.template.engine.RestrictedTemplateEngineContext;
import com.liferay.portal.util.PropsValues;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Writer;

import java.util.Map;

/**
 * @author Mika Koivisto
 */
public class FreeMarkerEngineImpl implements TemplateEngine {

	public void flushTemplate(String templateId) {
		if (_stringTemplateLoader != null) {
			_stringTemplateLoader.removeTemplate(templateId);
		}

		PortalCache portalCache = LiferayCacheStorage.getPortalCache();

		portalCache.remove(templateId);
	}

	public void flushTemplates() {
		PortalCache portalCache = LiferayCacheStorage.getPortalCache();

		portalCache.removeAll();
	}

	public TemplateEngineContext getEmptyContext() {
		return new FreeMarkerContextImpl();
	}

	public String getEngineName() {
		return TemplateEngine.FREE_MARKER;
	}

	public TemplateEngineVariables getTemplateEngineVariables() {
		return _templateEngineVariables;
	}

	public TemplateEngineContext getWrappedRestrictedToolsContext() {
		return new FreeMarkerContextImpl(
			(Map<String, Object>)
				_restrictedToolsContext.getWrappedTemplateContext());
	}

	public TemplateEngineContext getWrappedStandardToolsContext() {
		return new FreeMarkerContextImpl(
			(Map<String, Object>)
				_standardToolsContext.getWrappedTemplateContext());
	}

	public void init() throws TemplateEngineException {
		if (_configuration != null) {
			return;
		}

		LiferayTemplateLoader liferayTemplateLoader =
			new LiferayTemplateLoader();

		liferayTemplateLoader.setTemplateLoaders(
			PropsValues.FREEMARKER_ENGINE_TEMPLATE_LOADERS);

		_stringTemplateLoader = new StringTemplateLoader();

		MultiTemplateLoader multiTemplateLoader =
			new MultiTemplateLoader(
				new TemplateLoader[] {
					new ClassTemplateLoader(getClass(), StringPool.SLASH),
					_stringTemplateLoader, liferayTemplateLoader
				});

		_configuration = new Configuration();

		_configuration.setDefaultEncoding(StringPool.UTF8);
		_configuration.setLocalizedLookup(
			PropsValues.FREEMARKER_ENGINE_LOCALIZED_LOOKUP);
		_configuration.setObjectWrapper(new LiferayObjectWrapper());
		_configuration.setTemplateLoader(multiTemplateLoader);
		_configuration.setTemplateUpdateDelay(
			PropsValues.FREEMARKER_ENGINE_MODIFICATION_CHECK_INTERVAL);

		try {
			_configuration.setSetting(
				"auto_import", PropsValues.FREEMARKER_ENGINE_MACRO_LIBRARY);
			_configuration.setSetting(
				"cache_storage", PropsValues.FREEMARKER_ENGINE_CACHE_STORAGE);
			_configuration.setSetting(
				"template_exception_handler",
				PropsValues.FREEMARKER_ENGINE_TEMPLATE_EXCEPTION_HANDLER);
		}
		catch (Exception e) {
			throw new TemplateEngineException(
				"Unable to config freemarker engine", e);
		}

		_restrictedToolsContext = new RestrictedTemplateEngineContext(
			new FreeMarkerContextImpl(),
			PropsValues.JOURNAL_TEMPLATE_FREEMARKER_RESTRICTED_VARIABLES);

		_templateEngineVariables.insertHelperUtilities(_restrictedToolsContext);

		_standardToolsContext = new FreeMarkerContextImpl();

		_templateEngineVariables.insertHelperUtilities(_standardToolsContext);
	}

	public boolean mergeTemplate(
			String templateId, String templateContent,
			TemplateEngineContext templateEngineContext, Writer writer)
		throws TemplateEngineException {

		if (Validator.isNotNull(templateContent) &&
			(!PropsValues.LAYOUT_TEMPLATE_CACHE_ENABLED ||
			 !stringTemplateExists(templateId))) {

			_stringTemplateLoader.putTemplate(templateId, templateContent);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Added " + templateId + " to the string based FreeMarker " +
						"template repository");
			}
		}

		try {
			Template template = _configuration.getTemplate(
				templateId, StringPool.UTF8);

			template.process(
				templateEngineContext.getWrappedTemplateContext(), writer);
		}
		catch (Exception e) {
			throw new TemplateEngineException(
				"Unable to process template " + templateId + " with content " +
					templateContent,
				e);
		}

		return true;
	}

	public boolean mergeTemplate(
			String templateId, TemplateEngineContext templateEngineContext,
			Writer writer)
		throws TemplateEngineException {

		return mergeTemplate(templateId, null, templateEngineContext, writer);
	}

	public void setTemplateEngineVariables(
		TemplateEngineVariables templateEngineVariables) {

		_templateEngineVariables = templateEngineVariables;
	}

	public boolean templateExists(String templateId) {
		try {
			Template template = _configuration.getTemplate(templateId);

			if (template != null) {
				return true;
			}
			else {
				return false;
			}
		}
		catch (IOException ioe) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioe, ioe);
			}

			return false;
		}
	}

	protected boolean stringTemplateExists(String templateId) {
		Object templateSource = _stringTemplateLoader.findTemplateSource(
			templateId);

		if (templateSource == null) {
			return false;
		}

		return true;
	}

	private static Log _log = LogFactoryUtil.getLog(FreeMarkerEngineImpl.class);

	private Configuration _configuration;
	private TemplateEngineContext _restrictedToolsContext;
	private TemplateEngineContext _standardToolsContext;
	private StringTemplateLoader _stringTemplateLoader;
	private TemplateEngineVariables _templateEngineVariables;

}