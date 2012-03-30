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

/**
 * @author Tina Tian
 */
public interface TemplateEngine {

	public static final String FREE_MARKER = "FREE_MARKER";

	public static final String VELOCITY = "VELOCITY";

	public void flushTemplate(String templateId);

	public void flushTemplates();

	public TemplateEngineContext getEmptyContext();

	public String getEngineName();

	public TemplateEngineVariables getTemplateEngineVariables();

	public TemplateEngineContext getWrappedRestrictedToolsContext();

	public TemplateEngineContext getWrappedStandardToolsContext();

	public void init() throws TemplateEngineException;

	public boolean mergeTemplate(
			String templateId, String templateContent,
			TemplateEngineContext templateEngineContext, Writer writer)
		throws TemplateEngineException;

	public boolean mergeTemplate(
			String templateId, TemplateEngineContext templateEngineContext,
			Writer writer)
		throws TemplateEngineException;

	public boolean templateExists(String templateId);

}