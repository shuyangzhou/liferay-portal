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

package com.liferay.portal.template.engine;

import com.liferay.portal.kernel.audit.AuditMessageFactoryUtil;
import com.liferay.portal.kernel.audit.AuditRouterUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.language.UnicodeLanguageUtil;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.template.engine.TemplateEngineContext;
import com.liferay.portal.kernel.template.engine.TemplateEngineException;
import com.liferay.portal.kernel.template.engine.TemplateEngineVariables;
import com.liferay.portal.kernel.util.ArrayUtil_IW;
import com.liferay.portal.kernel.util.DateUtil_IW;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.GetterUtil_IW;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil_IW;
import com.liferay.portal.kernel.util.Randomizer_IW;
import com.liferay.portal.kernel.util.StaticFieldGetter;
import com.liferay.portal.kernel.util.StringUtil_IW;
import com.liferay.portal.kernel.util.TimeZoneUtil_IW;
import com.liferay.portal.kernel.util.UnicodeFormatter_IW;
import com.liferay.portal.kernel.util.Validator_IW;
import com.liferay.portal.kernel.xml.SAXReader;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.permission.AccountPermissionUtil;
import com.liferay.portal.service.permission.CommonPermissionUtil;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.OrganizationPermissionUtil;
import com.liferay.portal.service.permission.PasswordPolicyPermissionUtil;
import com.liferay.portal.service.permission.PortalPermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.service.permission.RolePermissionUtil;
import com.liferay.portal.service.permission.UserGroupPermissionUtil;
import com.liferay.portal.service.permission.UserPermissionUtil;
import com.liferay.portal.theme.NavItem;
import com.liferay.portal.theme.RequestVars;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil_IW;
import com.liferay.portal.util.PropsUtil_IW;
import com.liferay.portal.util.SessionClicks_IW;
import com.liferay.portal.util.WebKeys;
import com.liferay.portal.webserver.WebServerServletTokenUtil;
import com.liferay.portlet.PortletConfigImpl;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoRowLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import com.liferay.portlet.journalcontent.util.JournalContentUtil;
import com.liferay.util.portlet.PortletRequestUtil;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.taglib.tiles.ComponentConstants;
import org.apache.struts.tiles.ComponentContext;

/**
 * @author Tina Tian
 */
public class DefaultTemplateEngineVariablesImpl
	implements TemplateEngineVariables {

	public void insertHelperUtilities(
		TemplateEngineContext templateEngineContext) {

		doInsertHelperUtilities(templateEngineContext);
	}

	public void insertRequestVariables(
			TemplateEngineContext templateEngineContext,
			HttpServletRequest request)
		throws TemplateEngineException {

		doInsertRequestVariables(templateEngineContext, request);
	}

	protected void doInsertHelperUtilities(
		TemplateEngineContext templateEngineContext) {

		// Array util

		templateEngineContext.put("arrayUtil", ArrayUtil_IW.getInstance());

		// Audit message factory

		templateEngineContext.put(
			"auditMessageFactoryUtil",
			AuditMessageFactoryUtil.getAuditMessageFactory());

		// Audit router util

		templateEngineContext.put(
			"auditRouterUtil", AuditRouterUtil.getAuditRouter());

		// Browser sniffer

		templateEngineContext.put(
			"browserSniffer", BrowserSnifferUtil.getBrowserSniffer());

		// Date format

		templateEngineContext.put(
			"dateFormatFactory",
			FastDateFormatFactoryUtil.getFastDateFormatFactory());

		// Date util

		templateEngineContext.put("dateUtil", DateUtil_IW.getInstance());

		// Expando column service

		ServiceLocator serviceLocator = ServiceLocator.getInstance();

		templateEngineContext.put(
			"expandoColumnLocalService",
			serviceLocator.findService(
				ExpandoColumnLocalService.class.getName()));

		// Expando row service

		templateEngineContext.put(
			"expandoRowLocalService",
			serviceLocator.findService(ExpandoRowLocalService.class.getName()));

		// Expando table service

		templateEngineContext.put(
			"expandoTableLocalService",
			serviceLocator.findService(
				ExpandoTableLocalService.class.getName()));

		// Expando value service

		templateEngineContext.put(
			"expandoValueLocalService",
			serviceLocator.findService(
				ExpandoValueLocalService.class.getName()));

		// Getter util

		templateEngineContext.put("getterUtil", GetterUtil_IW.getInstance());

		// Html util

		templateEngineContext.put("htmlUtil", HtmlUtil.getHtml());

		// Http util

		templateEngineContext.put("httpUtil", HttpUtil.getHttp());

		// Journal content util

		templateEngineContext.put(
			"journalContentUtil", JournalContentUtil.getJournalContent());

		// JSON factory util

		templateEngineContext.put(
			"jsonFactoryUtil", JSONFactoryUtil.getJSONFactory());

		// Language util

		templateEngineContext.put("languageUtil", LanguageUtil.getLanguage());

		templateEngineContext.put(
			"unicodeLanguageUtil", UnicodeLanguageUtil.getUnicodeLanguage());

		// Locale util

		templateEngineContext.put("localeUtil", LocaleUtil.getInstance());

		// Param util

		templateEngineContext.put("paramUtil", ParamUtil_IW.getInstance());

		// Portal util

		templateEngineContext.put("portalUtil", PortalUtil.getPortal());

		templateEngineContext.put("portal", PortalUtil.getPortal());

		// Prefs props util

		templateEngineContext.put(
			"prefsPropsUtil", PrefsPropsUtil_IW.getInstance());

		// Props util

		templateEngineContext.put("propsUtil", PropsUtil_IW.getInstance());

		// Portlet URL factory

		templateEngineContext.put(
			"portletURLFactory", PortletURLFactoryUtil.getPortletURLFactory());

		// Randomizer

		templateEngineContext.put(
			"randomizer", Randomizer_IW.getInstance().getWrappedInstance());

		// SAX reader util

		UtilLocator utilLocator = UtilLocator.getInstance();

		templateEngineContext.put(
			"saxReaderUtil", utilLocator.findUtil(SAXReader.class.getName()));

		// Service locator

		templateEngineContext.put("serviceLocator", serviceLocator);

		// Session clicks

		templateEngineContext.put(
			"sessionClicks", SessionClicks_IW.getInstance());

		// Static field getter

		templateEngineContext.put(
			"staticFieldGetter", StaticFieldGetter.getInstance());

		// String util

		templateEngineContext.put("stringUtil", StringUtil_IW.getInstance());

		// Time zone util

		templateEngineContext.put(
			"timeZoneUtil", TimeZoneUtil_IW.getInstance());

		// Util locator

		templateEngineContext.put("utilLocator", utilLocator);

		// Unicode formatter

		templateEngineContext.put(
			"unicodeFormatter", UnicodeFormatter_IW.getInstance());

		// Validator

		templateEngineContext.put("validator", Validator_IW.getInstance());

		// Web server servlet token

		templateEngineContext.put(
			"webServerToken",
			WebServerServletTokenUtil.getWebServerServletToken());

		// Permissions

		templateEngineContext.put(
			"accountPermission", AccountPermissionUtil.getAccountPermission());
		templateEngineContext.put(
			"commonPermission", CommonPermissionUtil.getCommonPermission());
		templateEngineContext.put(
			"groupPermission", GroupPermissionUtil.getGroupPermission());
		templateEngineContext.put(
			"layoutPermission", LayoutPermissionUtil.getLayoutPermission());
		templateEngineContext.put(
			"organizationPermission",
			OrganizationPermissionUtil.getOrganizationPermission());
		templateEngineContext.put(
			"passwordPolicyPermission",
			PasswordPolicyPermissionUtil.getPasswordPolicyPermission());
		templateEngineContext.put(
			"portalPermission", PortalPermissionUtil.getPortalPermission());
		templateEngineContext.put(
			"portletPermission", PortletPermissionUtil.getPortletPermission());
		templateEngineContext.put(
			"rolePermission", RolePermissionUtil.getRolePermission());
		templateEngineContext.put(
			"userGroupPermission",
			UserGroupPermissionUtil.getUserGroupPermission());
		templateEngineContext.put(
			"userPermission", UserPermissionUtil.getUserPermission());

		// Deprecated

		templateEngineContext.put(
			"dateFormats",
			FastDateFormatFactoryUtil.getFastDateFormatFactory());
		templateEngineContext.put(
			"imageToken", WebServerServletTokenUtil.getWebServerServletToken());
		templateEngineContext.put(
			"locationPermission",
			OrganizationPermissionUtil.getOrganizationPermission());

	}

	protected void doInsertRequestVariables(
			TemplateEngineContext templateEngineContext,
			HttpServletRequest request)
		throws TemplateEngineException {

		// Request

		templateEngineContext.put("request", request);

		// Portlet config

		PortletConfigImpl portletConfigImpl =
			(PortletConfigImpl)request.getAttribute(
				JavaConstants.JAVAX_PORTLET_CONFIG);

		if (portletConfigImpl != null) {
			templateEngineContext.put("portletConfig", portletConfigImpl);
		}

		// Render request

		final PortletRequest portletRequest =
			(PortletRequest)request.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST);

		if (portletRequest != null) {
			if (portletRequest instanceof RenderRequest) {
				templateEngineContext.put("renderRequest", portletRequest);
			}
		}

		// Render response

		final PortletResponse portletResponse =
			(PortletResponse)request.getAttribute(
				JavaConstants.JAVAX_PORTLET_RESPONSE);

		if (portletResponse != null) {
			if (portletResponse instanceof RenderResponse) {
				templateEngineContext.put("renderResponse", portletResponse);
			}
		}

		// XML request

		if ((portletRequest != null) && (portletResponse != null)) {
			templateEngineContext.put(
				"xmlRequest",
				new Object() {

					@Override
					public String toString() {
						return PortletRequestUtil.toXML(
							portletRequest, portletResponse);
					}

				}
			);
		}

		// Theme display

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			Layout layout = themeDisplay.getLayout();
			List<Layout> layouts = themeDisplay.getLayouts();

			templateEngineContext.put("themeDisplay", themeDisplay);
			templateEngineContext.put("company", themeDisplay.getCompany());
			templateEngineContext.put("user", themeDisplay.getUser());
			templateEngineContext.put("realUser", themeDisplay.getRealUser());
			templateEngineContext.put("layout", layout);
			templateEngineContext.put("layouts", layouts);
			templateEngineContext.put(
				"plid", String.valueOf(themeDisplay.getPlid()));
			templateEngineContext.put(
				"layoutTypePortlet", themeDisplay.getLayoutTypePortlet());
			templateEngineContext.put(
				"scopeGroupId", new Long(themeDisplay.getScopeGroupId()));
			templateEngineContext.put(
				"permissionChecker", themeDisplay.getPermissionChecker());
			templateEngineContext.put("locale", themeDisplay.getLocale());
			templateEngineContext.put("timeZone", themeDisplay.getTimeZone());
			templateEngineContext.put(
				"colorScheme", themeDisplay.getColorScheme());
			templateEngineContext.put(
				"portletDisplay", themeDisplay.getPortletDisplay());

			// Navigation items

			if (layout != null) {
				RequestVars requestVars = null;

				try {
					requestVars = new RequestVars(
						request, themeDisplay, layout.getAncestorPlid(),
						layout.getAncestorLayoutId());
				}
				catch(Exception e) {
					throw new TemplateEngineException(e);
				}

				List<NavItem> navItems = NavItem.fromLayouts(
					requestVars, layouts);

				templateEngineContext.put("navItems", navItems);
			}

			// Deprecated

			templateEngineContext.put(
				"portletGroupId", new Long(themeDisplay.getScopeGroupId()));
		}

		// Theme

		Theme theme = (Theme)request.getAttribute(WebKeys.THEME);

		if ((theme == null) && (themeDisplay != null)) {
			theme = themeDisplay.getTheme();
		}

		if (theme != null) {
			templateEngineContext.put("theme", theme);
		}

		// Tiles attributes

		insertTilesVariables(templateEngineContext, request);

		// Page title and subtitle

		templateEngineContext.put(
			"pageTitle", request.getAttribute(WebKeys.PAGE_TITLE));
		templateEngineContext.put(
			"pageSubtitle", request.getAttribute(WebKeys.PAGE_SUBTITLE));
	}

	protected void insertTilesVariables(
		TemplateEngineContext templateEngineContext,
		HttpServletRequest request) {

		ComponentContext componentContext =
			(ComponentContext)request.getAttribute(
				ComponentConstants.COMPONENT_CONTEXT);

		if (componentContext == null) {
			return;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		String tilesTitle = (String)componentContext.getAttribute("title");

		themeDisplay.setTilesTitle(tilesTitle);

		templateEngineContext.put("tilesTitle", tilesTitle);

		String tilesContent = (String)componentContext.getAttribute("content");

		themeDisplay.setTilesContent(tilesContent);

		templateEngineContext.put("tilesContent", tilesContent);

		boolean tilesSelectable = GetterUtil.getBoolean(
			(String)componentContext.getAttribute("selectable"));

		themeDisplay.setTilesSelectable(tilesSelectable);

		templateEngineContext.put("tilesSelectable", tilesSelectable);
	}

}