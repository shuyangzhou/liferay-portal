/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.layout.page.template.admin.web.internal.portlet.action;

import com.liferay.layout.admin.constants.LayoutAdminPortletKeys;
import com.liferay.layout.page.template.admin.constants.LayoutPageTemplateAdminPortletKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.constants.MVCRenderConstants;
import com.liferay.portal.kernel.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import jakarta.portlet.PortletException;
import jakarta.portlet.PortletRequest;
import jakarta.portlet.RenderRequest;
import jakarta.portlet.RenderResponse;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Javier Moral
 */
@Component(
	property = {
		"jakarta.portlet.name=" + LayoutPageTemplateAdminPortletKeys.LAYOUT_PAGE_TEMPLATES,
		"mvc.command.name=/layout_page_template_admin/configure_display_page"
	},
	service = MVCRenderCommand.class
)
public class ConfigureDisplayPageMVCRenderCommand implements MVCRenderCommand {

	@Override
	public String render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			LayoutPageTemplateEntry layoutPageTemplateEntry =
				_fetchLayoutPageTemplateEntry(renderRequest, themeDisplay);

			if (layoutPageTemplateEntry == null) {
				return "/view.jsp";
			}

			HttpServletResponse httpServletResponse =
				_portal.getHttpServletResponse(renderResponse);

			httpServletResponse.sendRedirect(
				_getConfigureDisplayPageURL(
					layoutPageTemplateEntry, renderRequest, themeDisplay));

			return MVCRenderConstants.MVC_PATH_VALUE_SKIP_DISPATCH;
		}
		catch (IOException | PortalException exception) {
			throw new PortletException(exception);
		}
	}

	private LayoutPageTemplateEntry _fetchLayoutPageTemplateEntry(
			RenderRequest renderRequest, ThemeDisplay themeDisplay)
		throws PortalException {

		String externalReferenceCode = ParamUtil.getString(
			renderRequest, "displayPageTemplateExternalReferenceCode");

		if (Validator.isNull(externalReferenceCode)) {
			return null;
		}

		return _layoutPageTemplateEntryService.
			fetchLayoutPageTemplateEntryByExternalReferenceCode(
				externalReferenceCode, themeDisplay.getScopeGroupId());
	}

	private String _getBackURL(
		RenderRequest renderRequest, ThemeDisplay themeDisplay) {

		String backURL = _portal.escapeRedirect(
			ParamUtil.getString(renderRequest, "redirect"));

		if (Validator.isNull(backURL)) {
			return themeDisplay.getURLCurrent();
		}

		return backURL;
	}

	private String _getConfigureDisplayPageURL(
		LayoutPageTemplateEntry layoutPageTemplateEntry,
		RenderRequest renderRequest, ThemeDisplay themeDisplay) {

		String backURL = _getBackURL(renderRequest, themeDisplay);

		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				renderRequest, themeDisplay.getScopeGroup(),
				LayoutAdminPortletKeys.GROUP_PAGES, 0, 0,
				PortletRequest.RENDER_PHASE)
		).setMVCRenderCommandName(
			"/layout_admin/edit_layout"
		).setRedirect(
			backURL
		).setBackURL(
			backURL
		).setParameter(
			"groupId", layoutPageTemplateEntry.getGroupId()
		).setParameter(
			"selPlid", layoutPageTemplateEntry.getPlid()
		).buildString();
	}

	@Reference
	private LayoutPageTemplateEntryService _layoutPageTemplateEntryService;

	@Reference
	private Portal _portal;

}