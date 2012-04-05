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

package com.liferay.portal.template.engine;

import com.liferay.portal.kernel.template.engine.TemplateEngineContext;

import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.List;

/**
 * @author Tina Tian
 */
public class RestrictedTemplateEngineContext implements TemplateEngineContext {

	public RestrictedTemplateEngineContext(
		TemplateEngineContext templateEngineContext,
		String[] restrictedVariables) {

		_templateEngineContext = templateEngineContext;
		_restrictedVariables = Arrays.asList(restrictedVariables);
	}

	public Object get(String key) {
		return _templateEngineContext.get(key);
	}

	public Object getWrappedTemplateContext() {
		return _templateEngineContext.getWrappedTemplateContext();
	}

	public void put(String key, Object value) {
		if (_restrictedVariables.contains(key)) {
			return;
		}

		_templateEngineContext.put(key, value);
	}

	private List<String> _restrictedVariables;
	private TemplateEngineContext _templateEngineContext;

}