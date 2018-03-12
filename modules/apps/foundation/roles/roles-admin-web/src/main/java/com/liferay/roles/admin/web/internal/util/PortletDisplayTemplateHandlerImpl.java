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

package com.liferay.roles.admin.web.internal.util;

import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portlet.display.template.PortletDisplayTemplate;
import com.liferay.portlet.display.template.util.PortletDisplayTemplateHandler;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Lance Ji
 */
@Component(immediate = true)
public class PortletDisplayTemplateHandlerImpl
	implements PortletDisplayTemplateHandler {

	@Override
	public List<TemplateHandler> getPortletDisplayTemplateHandlers() {
		return _portletDisplayTemplate.getPortletDisplayTemplateHandlers();
	}

	@Reference(unbind = "-")
	private PortletDisplayTemplate _portletDisplayTemplate;

}