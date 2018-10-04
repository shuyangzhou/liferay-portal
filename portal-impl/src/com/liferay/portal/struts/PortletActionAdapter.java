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

package com.liferay.portal.struts;

import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mika Koivisto
 */
public class PortletActionAdapter extends PortletAction {

	public PortletActionAdapter(StrutsPortletAction strutsPortletAction) {
		_strutsPortletAction = strutsPortletAction;
	}

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		_strutsPortletAction.processAction(
			null, portletConfig, actionRequest, actionResponse);
	}

	@Override
	public ActionForward render(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		String forward = _strutsPortletAction.render(
			null, portletConfig, renderRequest, renderResponse);

		if (Validator.isNull(forward)) {
			return null;
		}

		ActionForward actionForward = actionMapping.findForward(forward);

		if (actionForward == null) {
			actionForward = new ActionForward(forward);
		}

		return actionForward;
	}

	@Override
	public void serveResource(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ResourceRequest resourceRequest,
			ResourceResponse resourceResponse)
		throws Exception {

		_strutsPortletAction.serveResource(
			null, portletConfig, resourceRequest, resourceResponse);
	}

	@Override
	protected boolean isCheckMethodOnProcessAction() {
		return _strutsPortletAction.isCheckMethodOnProcessAction();
	}

	private final StrutsPortletAction _strutsPortletAction;

}