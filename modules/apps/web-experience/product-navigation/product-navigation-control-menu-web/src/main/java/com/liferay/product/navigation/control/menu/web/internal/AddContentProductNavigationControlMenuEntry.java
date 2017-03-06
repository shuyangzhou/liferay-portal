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

package com.liferay.product.navigation.control.menu.web.internal;

import com.liferay.alloy.taglib.alloy_util.ScriptTag;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypeController;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseJSPProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.product.navigation.control.menu.web.internal.constants.ProductNavigationControlMenuPortletKeys;
import com.liferay.taglib.aui.IconTag;
import com.liferay.taglib.ui.MessageTag;
import com.liferay.taglib.util.BodyBottomTag;

import java.io.IOException;
import java.io.Writer;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 */
@Component(
	immediate = true,
	property = {
		"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
		"product.navigation.control.menu.entry.order:Integer=200"
	},
	service = ProductNavigationControlMenuEntry.class
)
public class AddContentProductNavigationControlMenuEntry
	extends BaseJSPProductNavigationControlMenuEntry
	implements ProductNavigationControlMenuEntry {

	@Override
	public String getBodyJspPath() {
		return "/entries/add_content_body.jsp";
	}

	@Override
	public String getIconJspPath() {
		return "/entries/add_content_icon.jsp";
	}

	@Override
	public boolean includeBody(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		BodyBottomTag bodyBottomTag = new BodyBottomTag();

		bodyBottomTag.setOutputKey("addContentMenu");

		try {
			bodyBottomTag.doBodyTag(
				request, response, this::_processBodyBottomTagBody);
		}
		catch (JspException je) {
			throw new IOException(je);
		}

		return true;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		Writer writer = response.getWriter();

		writer.write("<li class=\"control-menu-nav-item\">");
		writer.write(
			"<a class=\"control-menu-icon lfr-portal-tooltip " +
				"product-menu-toggle sidenav-toggler\" ");
		writer.write(
			"data-content=\"body\" data-open-class=\"open-admin-panel\" " +
				"data-qa-id=\"add\" data-target=\"#");
		writer.write(_portletNamespace);
		writer.write("addPanelId\" data-title=\"");
		writer.write(_html.escape(_language.get(request, "add")));
		writer.write(
			"\" data-toggle=\"sidenav\" data-type=\"fixed-push\" " +
				"data-type-mobile=\"fixed\" data-url=\"");

		PortletURL addPanelURL = _portletURLFactory.create(
			request,
			ProductNavigationControlMenuPortletKeys.
				PRODUCT_NAVIGATION_CONTROL_MENU,
			PortletRequest.RENDER_PHASE);

		addPanelURL.setParameter("mvcPath", "/add_panel.jsp");

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		addPanelURL.setParameter(
			"stateMaximized", String.valueOf(themeDisplay.isStateMaximized()));

		try {
			addPanelURL.setWindowState(LiferayWindowState.EXCLUSIVE);
		}
		catch (WindowStateException wse) {
			throw new IOException(wse);
		}

		addPanelURL.write(writer);

		writer.write("\" href=\"javascript:;\" id=\"");
		writer.write(_portletNamespace);
		writer.write("addToggleId\">");

		IconTag iconTag = new IconTag();

		iconTag.setCssClass("icon-monospaced");
		iconTag.setImage("plus");
		iconTag.setMarkupView("lexicon");

		try {
			iconTag.doTag(request, response);
		}
		catch (JspException je) {
			throw new IOException(je);
		}

		writer.write("</a></li>");

		return true;
	}

	@Override
	public boolean isShow(HttpServletRequest request) throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay.isStateMaximized()) {
			return false;
		}

		Layout layout = themeDisplay.getLayout();

		if (!layout.isTypePortlet()) {
			return false;
		}

		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		LayoutTypeController layoutTypeController =
			layoutTypePortlet.getLayoutTypeController();

		if (layoutTypeController.isFullPageDisplayable()) {
			return false;
		}

		if (!hasAddContentOrApplicationPermission(themeDisplay)) {
			return false;
		}

		if (!(hasCustomizePermission(themeDisplay) ||
			  hasUpdateLayoutPermission(themeDisplay))) {

			return false;
		}

		return super.isShow(request);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.product.navigation.control.menu.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	protected boolean hasAddContentOrApplicationPermission(
		ThemeDisplay themeDisplay) {

		Layout layout = themeDisplay.getLayout();

		if (layout.isLayoutPrototypeLinkActive()) {
			return false;
		}

		return true;
	}

	protected boolean hasCustomizePermission(ThemeDisplay themeDisplay)
		throws PortalException {

		Layout layout = themeDisplay.getLayout();
		LayoutTypePortlet layoutTypePortlet =
			themeDisplay.getLayoutTypePortlet();

		if (!layout.isTypePortlet() || (layoutTypePortlet == null)) {
			return false;
		}

		if (!layoutTypePortlet.isCustomizable() ||
			!layoutTypePortlet.isCustomizedView()) {

			return false;
		}

		if (LayoutPermissionUtil.contains(
				themeDisplay.getPermissionChecker(), layout,
				ActionKeys.CUSTOMIZE)) {

			return true;
		}

		return false;
	}

	protected boolean hasUpdateLayoutPermission(ThemeDisplay themeDisplay)
		throws PortalException {

		return LayoutPermissionUtil.contains(
			themeDisplay.getPermissionChecker(), themeDisplay.getLayout(),
			ActionKeys.UPDATE);
	}

	@Reference(unbind = "-")
	protected void setPortal(Portal portal) {
		_portletNamespace = portal.getPortletNamespace(
			ProductNavigationControlMenuPortletKeys.
				PRODUCT_NAVIGATION_CONTROL_MENU);
	}

	private void _processBodyBottomTagBody(PageContext pageContext) {
		JspWriter jspWriter = pageContext.getOut();

		try {
			jspWriter.write(
				"<div class=\"closed hidden-print lfr-add-panel " +
					"lfr-admin-panel sidenav-fixed sidenav-menu-slider " +
						"sidenav-right\" id=\"");
			jspWriter.write(_portletNamespace);
			jspWriter.write("addPanelId\">");
			jspWriter.write(
				"<div class=\"product-menu sidebar sidebar-inverse " +
					"sidenav-menu\"><div class=\"sidebar-header\"><span>");

			MessageTag messageTag = new MessageTag();

			messageTag.setKey("add");

			messageTag.doTag(pageContext);

			jspWriter.write("</span>");

			IconTag iconTag = new IconTag();

			iconTag.setCssClass("icon-monospaced sidenav-close");
			iconTag.setImage("times");
			iconTag.setMarkupView("lexicon");
			iconTag.setUrl("javascript:;");

			iconTag.doTag(pageContext);

			jspWriter.write(
				"</div><div class=\"sidebar-body\"></div></div></div>");

			ScriptTag scriptTag = new ScriptTag();

			scriptTag.setUse("liferay-store,io-request,parse-content");

			scriptTag.doBodyTag(pageContext, this::_processScriptTagBody);
		}
		catch (Exception e) {
			ReflectionUtil.throwException(e);
		}
	}

	private void _processScriptTagBody(PageContext pageContext) {
		JspWriter jspWriter = pageContext.getOut();

		try {
			jspWriter.write("var addToggle = $('#");
			jspWriter.write(_portletNamespace);
			jspWriter.write("addToggleId');");
			jspWriter.write(
				"addToggle.sideNavigation();Liferay.once('screenLoad'," +
					"function() {var sideNavigation = addToggle.data(" +
						"'lexicon.sidenav');");
			jspWriter.write(
				"if (sideNavigation) {sideNavigation.destroy();}});");
		}
		catch (IOException ioe) {
			ReflectionUtil.throwException(ioe);
		}
	}

	@Reference
	private Html _html;

	@Reference
	private Language _language;

	private String _portletNamespace;

	@Reference
	private PortletURLFactory _portletURLFactory;

}