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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mika Koivisto
 */
public class FreeMarkerContextImpl implements TemplateEngineContext {

	public FreeMarkerContextImpl() {
		_context = new ConcurrentHashMap<String, Object>();
	}

	public FreeMarkerContextImpl(Map<String, Object> context) {
		_context = new ConcurrentHashMap<String, Object>();

		_context.putAll(context);
	}

	public Object get(String key) {
		return _context.get(key);
	}

	public Object getWrappedTemplateContext() {
		return _context;
	}

	public void put(String key, Object value) {
		_context.put(key, value);
	}

	private Map<String, Object> _context;

}