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

package com.liferay.portlet.brides.mvc;

import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.impl.VirtualLayout;
import com.liferay.portal.kernel.portlet.PortletLayoutFinder;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.sites.kernel.util.SitesUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowState;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shuyang Zhou
 */
public abstract class FindMVCActionCommand extends BaseMVCActionCommand {

	protected abstract void addRequiredParameters(
		ActionRequest actionRequest, String portletId, PortletURL portletURL);

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			long primaryKey = ParamUtil.getLong(
				actionRequest, getPrimaryKeyParameterName());

			long groupId = ParamUtil.getLong(
				actionRequest, "groupId", themeDisplay.getScopeGroupId());

			if (primaryKey > 0) {
				try {
					long overrideGroupId = getGroupId(primaryKey);

					if (overrideGroupId > 0) {
						groupId = overrideGroupId;
					}
				}
				catch (Exception e) {
					if (_log.isDebugEnabled()) {
						_log.debug(e, e);
					}
				}
			}

			PortletLayoutFinder portletLayoutFinder = getPortletLayoutFinder();

			PortletLayoutFinder.Result result = portletLayoutFinder.find(
				themeDisplay, groupId);

			long plid = result.getPlid();

			Layout layout = _setTargetLayout(
				themeDisplay, actionRequest, groupId, plid);

			LayoutPermissionUtil.check(
				themeDisplay.getPermissionChecker(), layout, true,
				ActionKeys.VIEW);

			String portletId = result.getPortletId();

			PortletURL portletURL = PortletURLFactoryUtil.create(
				actionRequest, portletId, layout, PortletRequest.RENDER_PHASE);

			addRequiredParameters(actionRequest, portletId, portletURL);

			boolean inheritRedirect = ParamUtil.getBoolean(
				actionRequest, "inheritRedirect");

			String redirect = null;

			if (inheritRedirect) {
				String noSuchEntryRedirect = ParamUtil.getString(
					actionRequest, "noSuchEntryRedirect");

				redirect = HttpUtil.getParameter(
					noSuchEntryRedirect, "redirect", false);

				redirect = HttpUtil.decodeURL(redirect);
			}
			else {
				redirect = ParamUtil.getString(actionRequest, "redirect");
			}

			if (Validator.isNotNull(redirect)) {
				portletURL.setParameter("redirect", redirect);
			}

			setPrimaryKeyParameter(portletURL, primaryKey);

			portletURL.setPortletMode(PortletMode.VIEW);
			portletURL.setWindowState(WindowState.NORMAL);

			portletURL = processPortletURL(actionRequest, portletURL);

			actionResponse.sendRedirect(portletURL.toString());
		}
		catch (Exception e) {
			String noSuchEntryRedirect = ParamUtil.getString(
				actionRequest, "noSuchEntryRedirect");

			noSuchEntryRedirect = PortalUtil.escapeRedirect(
				noSuchEntryRedirect);

			if (Validator.isNotNull(noSuchEntryRedirect) &&
				(e instanceof NoSuchLayoutException ||
				 e instanceof PrincipalException)) {

				actionResponse.sendRedirect(noSuchEntryRedirect);
			}
			else {
				PortalUtil.sendError(e, actionRequest, actionResponse);
			}
		}
	}

	protected abstract long getGroupId(long primaryKey) throws Exception;

	protected abstract PortletLayoutFinder getPortletLayoutFinder();

	protected abstract String getPrimaryKeyParameterName();

	protected PortletURL processPortletURL(
			ActionRequest actionRequest, PortletURL portletURL)
		throws Exception {

		return portletURL;
	}

	protected void setPrimaryKeyParameter(
			PortletURL portletURL, long primaryKey)
		throws Exception {

		portletURL.setParameter(
			getPrimaryKeyParameterName(), String.valueOf(primaryKey));
	}

	private static Layout _setTargetLayout(
			ThemeDisplay themeDisplay, ActionRequest actionRequest,
			long groupId, long plid)
		throws Exception {

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		Group group = GroupLocalServiceUtil.getGroup(groupId);
		Layout layout = LayoutLocalServiceUtil.getLayout(plid);

		if ((groupId == layout.getGroupId()) ||
			(group.getParentGroupId() == layout.getGroupId()) ||
			(layout.isPrivateLayout() &&
			 !SitesUtil.isUserGroupLayoutSetViewable(
				 permissionChecker, layout.getGroup()))) {

			return layout;
		}

		layout = new VirtualLayout(layout, group);

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			actionRequest);

		request = PortalUtil.getOriginalServletRequest(request);

		request.setAttribute(WebKeys.LAYOUT, layout);

		return layout;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FindMVCActionCommand.class);

}