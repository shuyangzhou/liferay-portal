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

package com.liferay.dispatch.web.internal.portlet;

import com.liferay.dispatch.constants.DispatchPortletKeys;
import com.liferay.dispatch.constants.DispatchWebKeys;
import com.liferay.dispatch.executor.type.DispatchTaskExecutorTypeRegistry;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.DispatchTaskLocalService;
import com.liferay.dispatch.web.internal.display.context.DispatchTaskDisplayContext;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-dispatch",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"javax.portlet.display-name=Dispatch",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + DispatchPortletKeys.DISPATCH,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class DispatchPortlet extends MVCPortlet {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		DispatchTask dispatchTask = null;

		long dispatchTaskId = ParamUtil.getLong(
			renderRequest, "dispatchTaskId");

		if (dispatchTaskId > 0) {
			dispatchTask = _dispatchTaskLocalService.fetchDispatchTask(
				dispatchTaskId);
		}

		if (dispatchTask != null) {
			renderRequest.setAttribute(
				DispatchWebKeys.DISPATCH_TASK, dispatchTask);
		}

		DispatchTaskDisplayContext dispatchTaskDisplayContext =
			new DispatchTaskDisplayContext(
				_dispatchTaskExecutorTypeRegistry, _dispatchTaskLocalService,
				renderRequest);

		renderRequest.setAttribute(
			WebKeys.PORTLET_DISPLAY_CONTEXT, dispatchTaskDisplayContext);

		super.render(renderRequest, renderResponse);
	}

	@Reference
	private DispatchTaskExecutorTypeRegistry _dispatchTaskExecutorTypeRegistry;

	@Reference
	private DispatchTaskLocalService _dispatchTaskLocalService;

	@Reference
	private Portal _portal;

}