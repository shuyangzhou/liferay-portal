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

package com.liferay.portal.kernel.template.engine;

import java.io.Writer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tina Tian
 */
public class TemplateEngineUtil {

	public static void flushTemplate(
			String templateEngineName, String templateId)
		throws TemplateEngineException {

		_getTemplateEngine(templateEngineName).flushTemplate(templateId);
	}

	public static void flushTemplates(String templateEngineName)
		throws TemplateEngineException {

		_getTemplateEngine(templateEngineName).flushTemplates();
	}

	public static TemplateEngineContext getEmptyContext(
			String templateEngineName)
		throws TemplateEngineException {

		return _getTemplateEngine(templateEngineName).getEmptyContext();
	}

	public static TemplateEngineContext getWrappedRestrictedToolsContext(
			String templateEngineName)
		throws TemplateEngineException {

		return _getTemplateEngine(
			templateEngineName).getWrappedRestrictedToolsContext();
	}

	public static TemplateEngineContext getWrappedStandardToolsContext(
			String templateEngineName)
		throws TemplateEngineException {

		return _getTemplateEngine(
			templateEngineName).getWrappedStandardToolsContext();
	}

	public static void init() throws TemplateEngineException {
		Collection<TemplateEngine> templateEngines = _templateEngines.values();

		for (TemplateEngine templateEngine : templateEngines) {
			templateEngine.init();
		}
	}

	public static void insertRequestVariables(
			String templateEngineName,
			TemplateEngineContext templateEngineContext,
			HttpServletRequest request)
		throws TemplateEngineException {

		TemplateEngine templateEngine = _getTemplateEngine(templateEngineName);

		TemplateEngineVariables templateEngineVariables =
			templateEngine.getTemplateEngineVariables();

		templateEngineVariables.insertRequestVariables(
			templateEngineContext, request);
	}

	public static boolean mergeTemplate(
			String templateEngineName, String templateId,
			String templateContent, TemplateEngineContext templateEngineContext,
			Writer writer)
		throws TemplateEngineException {

		return _getTemplateEngine(templateEngineName).mergeTemplate(
			templateId, templateContent, templateEngineContext, writer);
	}

	public static boolean mergeTemplate(
			String templateEngineName, String templateId,
			TemplateEngineContext templateEngineContext, Writer writer)
		throws TemplateEngineException {

		return _getTemplateEngine(templateEngineName).mergeTemplate(
			templateId, templateEngineContext, writer);
	}

	public static void registerTemplateEngine(TemplateEngine templateEngine) {
		_templateEngines.put(templateEngine.getEngineName(), templateEngine);
	}

	public static boolean templateEngineExists(String templateEngineName) {
		return _templateEngines.containsKey(templateEngineName);
	}

	public static boolean templateExists(
			String templateEngineName, String templateId)
		throws TemplateEngineException {

		return _getTemplateEngine(templateEngineName).templateExists(
			templateId);
	}

	public static void unregisterTemplateEngine(String templateEngineName) {
		_templateEngines.remove(templateEngineName);
	}

	public void setTemplateEngines(List<TemplateEngine> templateEngines) {
		for (TemplateEngine templateEngine : templateEngines) {
			_templateEngines.put(
				templateEngine.getEngineName(), templateEngine);
		}
	}

	private static TemplateEngine _getTemplateEngine(String templateEngineName)
		throws TemplateEngineException {

		TemplateEngine templateEngine = _templateEngines.get(
			templateEngineName);

		if (templateEngine == null) {
			throw new TemplateEngineException(
				"Current system does not support template engine " +
					templateEngineName);
		}

		return templateEngine;
	}

	private static Map<String, TemplateEngine> _templateEngines =
		new ConcurrentHashMap<String, TemplateEngine>();

}