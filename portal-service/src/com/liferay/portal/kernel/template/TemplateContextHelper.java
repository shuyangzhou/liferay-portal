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

package com.liferay.portal.kernel.template;

import com.liferay.portal.kernel.templateparser.TemplateContext;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Tina Tian
 */
public interface TemplateContextHelper {

	public Map<String, Object> getHelperUtilities();

	public Map<String, Object> getRestrictedHelperUtilities();

	public List<String> getRestrictedVariables();

	public void prepare(
			TemplateContext templateContext, HttpServletRequest request)
		throws TemplateException;

}