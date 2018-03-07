/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.dynamic.data.mapping.taglib.internal.util;

import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.portlet.display.template.util.PortletDisplayDDMTemplate;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lance Ji
 */
@Component
public class PortletDisplayDDMTemplateUtil {

	public static long getDDMTemplateGroupId(long groupId) {
		return _portletDisplayDDMTemplate.getDDMTemplateGroupId(groupId);
	}

	public static String getDisplayStyle(String ddmTemplateKey) {
		return _portletDisplayDDMTemplate.getDisplayStyle(ddmTemplateKey);
	}

	public static DDMTemplate getPortletDisplayTemplateDDMTemplate(
		long groupId, long classNameId, String displayStyle,
		boolean useDefault) {

		return _portletDisplayDDMTemplate.getPortletDisplayTemplateDDMTemplate(
			groupId, classNameId, displayStyle, useDefault);
	}

	public static String renderDDMTemplate(
			HttpServletRequest request, HttpServletResponse response,
			long ddmTemplateId, List<?> entries,
			Map<String, Object> contextObjects)
		throws Exception {

		return _portletDisplayDDMTemplate.renderDDMTemplate(
			request, response, ddmTemplateId, entries, contextObjects);
	}

	@Reference(unbind = "-")
	protected void setPortletDisplayDDMTemplate(
		PortletDisplayDDMTemplate portletDisplayDDMTemplate) {

		_portletDisplayDDMTemplate = portletDisplayDDMTemplate;
	}

	private static PortletDisplayDDMTemplate _portletDisplayDDMTemplate;

}