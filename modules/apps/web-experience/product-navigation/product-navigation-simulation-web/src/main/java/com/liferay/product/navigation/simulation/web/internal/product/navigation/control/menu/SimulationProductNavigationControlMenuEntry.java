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

package com.liferay.product.navigation.simulation.web.internal.product.navigation.control.menu;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.PanelAppRegistry;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Html;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseJSPProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.product.navigation.simulation.application.list.SimulationPanelCategory;
import com.liferay.product.navigation.simulation.web.constants.ProductNavigationSimulationPortletKeys;
import com.liferay.taglib.aui.IconTag;

import java.io.IOException;
import java.io.Writer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.WindowStateException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Julio Camarero
 */
@Component(
	immediate = true,
	property = {
		"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
		"product.navigation.control.menu.entry.order:Integer=300"
	},
	service = ProductNavigationControlMenuEntry.class
)
public class SimulationProductNavigationControlMenuEntry
	extends BaseJSPProductNavigationControlMenuEntry
	implements ProductNavigationControlMenuEntry {

	@Activate
	public void activate() {
		_portletNamespace = _portal.getPortletNamespace(
			ProductNavigationSimulationPortletKeys.
				PRODUCT_NAVIGATION_SIMULATION);
	}

	@Override
	public String getBodyJspPath() {
		return "/portlet/control_menu/simulation_control_menu_body.jsp";
	}

	@Override
	public String getIconJspPath() {
		return null;
	}

	@Override
	public boolean includeIcon(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		PortletURL simulationPanelURL = _portletURLFactory.create(
			request,
			ProductNavigationSimulationPortletKeys.
				PRODUCT_NAVIGATION_SIMULATION,
			PortletRequest.RENDER_PHASE);

		try {
			simulationPanelURL.setWindowState(LiferayWindowState.EXCLUSIVE);
		}
		catch (WindowStateException wse) {
			ReflectionUtil.throwException(wse);
		}

		Map<String, String> values = new HashMap<>();

		try {
			String iconTag = IconTag.doTag(
				"icon-monospaced", "simulation-menu-closed", "lexicon", request,
				response);

			values.put("iconTag", iconTag);
		}
		catch (ServletException se) {
			_log.error(se, se);

			return false;
		}

		values.put("portletNamespace", _portletNamespace);
		values.put("simulationPanelURL", simulationPanelURL.toString());
		values.put("title", _html.escape(_language.get(request, "simulation")));

		Writer writer = response.getWriter();

		writer.write(StringUtil.replace(_TMPL_CONTENT, "${", "}", values));

		return true;
	}

	@Override
	public boolean isShow(HttpServletRequest request) throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		if (layout.isTypeControlPanel()) {
			return false;
		}

		List<PanelApp> panelApps = _panelAppRegistry.getPanelApps(
			SimulationPanelCategory.SIMULATION,
			themeDisplay.getPermissionChecker(), themeDisplay.getScopeGroup());

		if (panelApps.isEmpty()) {
			return false;
		}

		return super.isShow(request);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.product.navigation.simulation.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Reference(unbind = "-")
	public void setSimulationPanelCategory(
		SimulationPanelCategory simulationPanelCategory) {
	}

	@Reference(unbind = "-")
	protected void setPanelAppRegistry(PanelAppRegistry panelAppRegistry) {
		_panelAppRegistry = panelAppRegistry;
	}

	private static final String _TMPL_CONTENT = StringUtil.read(
		SimulationProductNavigationControlMenuEntry.class, "icon.tmpl");

	private static final Log _log = LogFactoryUtil.getLog(
		SimulationProductNavigationControlMenuEntry.class);

	@Reference
	private Html _html;

	@Reference
	private Language _language;

	private PanelAppRegistry _panelAppRegistry;

	@Reference
	private Portal _portal;

	private String _portletNamespace;

	@Reference
	private PortletURLFactory _portletURLFactory;

}