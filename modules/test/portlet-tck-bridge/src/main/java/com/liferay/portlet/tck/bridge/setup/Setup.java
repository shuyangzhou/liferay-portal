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

package com.liferay.portlet.tck.bridge.setup;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutSetLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portlet.tck.bridge.configuration.PortletTCKBridgeConfiguration;

import java.io.File;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.osgi.framework.Bundle;

/**
 * @author Vernon Singleton
 */
public class Setup {

	public static void setupPortletTCKSite(
		PortletTCKBridgeConfiguration portletTCKBridgeConfiguration) {

		long companyId = 0L;
		long userId = 0L;

		try {
			for (long id : PortalUtil.getCompanyIds()) {
				_setupPermissionChecker(id);
				Company company = CompanyLocalServiceUtil.getCompanyById(id);

				if ("Liferay".equals(company.getName())) {
					companyId = id;
					userId = company.getDefaultUser().getUserId();
				}
			}

			Group group = _getGroupForSite(companyId, userId);

			long groupId = group.getGroupId();

			_addAllUsersToSite(groupId);

			String tckDeployFiles =
				portletTCKBridgeConfiguration.tckDeployFiles();

			URL configFileURL = new File(
				tckDeployFiles +
					"/pluto-portal-driver-config.xml").toURI().toURL();

			if (_log.isInfoEnabled()) {
				_log.info(
					"setupPortletTCKSite: configFileURL = " +
						configFileURL.toString());
			}

			Document document = SAXReaderUtil.read(configFileURL);

			Element rootElement = document.getRootElement();

			Element renderConfigElement = rootElement.element("render-config");

			Iterator<Element> pageElementIterator =
				renderConfigElement.elementIterator("page");

			while (pageElementIterator.hasNext()) {
				Element pageElement = pageElementIterator.next();

				Attribute nameAttribute = pageElement.attribute("name");

				String pageName = nameAttribute.getValue();

				HashMap<String, String> excludedName = new HashMap<>();

				String[] excludeWarNames =
					portletTCKBridgeConfiguration.excludeWarNames();

				String servletContextName =
					portletTCKBridgeConfiguration.testContext();

				String[] servletContextNames =
					portletTCKBridgeConfiguration.servletContextNames();

				for (String warName : excludeWarNames) {
					excludedName.put(warName, "1");
				}

				String prefix = servletContextName.replaceFirst("tck-", "");

				// TODO remove V2.  Later the V2 prefix may go away
				// when we start implementing portlet 3.

				if ("tck-V2*".equals(servletContextName)) {
					prefix = "V2";
				}

				if (pageName.startsWith(prefix)) {
					if (excludedName.get(pageName) == null) {
						List<Element> portletElements = pageElement.elements(
							"portlet");
						List<Portlet> portlets = new LinkedList<>();

						for (Element elem : portletElements) {
							nameAttribute = elem.attribute("name");

							String portletName = nameAttribute.getValue();

							Portlet portlet = new Portlet(
								portletName, pageName, false);

							portlets.add(portlet);
						}

						PortalPage portalPage = new PortalPage(
							pageName, portlets);

						_setupPage(
							userId, groupId, portalPage, servletContextName,
							servletContextNames);
					}
				}
			}
		}
		catch (Exception e) {
			_log.error(e.getMessage(), e);
		}
	}

	private static void _addAllUsersToSite(long groupId) throws Exception {
		List<User> users = UserLocalServiceUtil.getUsers(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		ArrayList<Long> userIdList = new ArrayList<>();

		for (User user : users) {
			if (!user.isDefaultUser()) {
				userIdList.add(user.getUserId());
			}
		}

		long[] userIds = new long[userIdList.size()];

		for (int i = 0; i < userIds.length; i++) {
			userIds[i] = userIdList.get(i);
		}

		UserLocalServiceUtil.addGroupUsers(groupId, userIds);
	}

	private static void _addPortlet(
			LayoutTypePortlet layoutTypePortlet, long userId, int columnNumber,
			String portletId)
		throws PortalException {

		String columnNumberLabel = Integer.toString(columnNumber);

		// Liferay 6.2 changed the expected value for the String-based column
		// number. Previous versions didn't require the "column-" prefix.

		columnNumberLabel = "column-" + columnNumber;

		// NOTE: In Liferay 6.1.x the following call was to setPortletIds()
		// but that method was removed in 6.2.x

		layoutTypePortlet.addPortletId(userId, portletId, columnNumberLabel, 1);

		layoutTypePortlet.resetModes();
		layoutTypePortlet.resetStates();
	}

	private static Group _getGroupForSite(long companyId, long userId)
		throws Exception {

		Group group;
		String name = "Portlet TCK";

		try {
			group = GroupLocalServiceUtil.getGroup(companyId, name);
		}
		catch (NoSuchGroupException nsge) {
			group = ServiceUtil.addActiveOpenGroup(userId, name);
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				"Setting up site name=[" + group.getName() +
					"] publicLayouts=[" + group.hasPublicLayouts() + "]");
		}

		return group;
	}

	private static Layout _getPortalPageLayout(
			long userId, long groupId, String portalPageName)
		throws Exception {

		Layout portalPageLayout = null;

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(groupId, true);

		for (Layout layout : layouts) {
			if (layout.getName(Locale.US).equals(portalPageName)) {
				portalPageLayout = layout;
			}
		}

		if (portalPageLayout == null) {
			long parentLayoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
			String type = LayoutConstants.TYPE_PORTLET;
			String friendlyURL = "/" + StringUtil.toLowerCase(portalPageName);

			portalPageLayout = ServiceUtil.addLayout(
				userId, groupId, true, parentLayoutId, portalPageName,
				portalPageName, portalPageName, type, false, friendlyURL);
		}

		return portalPageLayout;
	}

	private static String _getServletContextName(
		String portalPageName, String[] servletContextNames) {

		if (_log.isInfoEnabled()) {
			_log.info(
				"getServletContextName: portalPageName = " + portalPageName);
		}

		String servletContextName = "";
		int maxLength = 0;

		for (String war : servletContextNames) {
			war = war.replaceFirst("tck-", "");

			if (portalPageName.startsWith(war)) {
				if (maxLength == 0) {
					maxLength = war.length();
					servletContextName = "tck-" + war;
				}
				else {
					if (maxLength < war.length()) {
						maxLength = war.length();
						servletContextName = "tck-" + war;
					}
				}
			}
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				"getServletContextName: servletContextName = " +
					servletContextName);
		}

		return servletContextName;
	}

	private static void _setupPage(
			long userId, long groupId, PortalPage portalPage,
			String servletContextName, String[] servletContextNames)
		throws Exception {

		Bundle[] bundles = BundleUtil.getBundles();

		String portalPageName = portalPage.getPageName();

		if (_log.isInfoEnabled()) {
			_log.info("setupPage: portalPageName = " + portalPageName);
		}

		List<Portlet> portlets = portalPage.getPortlets();
		Layout portalPageLayout = _getPortalPageLayout(
			userId, groupId, portalPageName);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)portalPageLayout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			userId, portalPage.getLayoutTemplateId(), false);

		int columnNumber = 1;

		List<String> addedPortletIds = new ArrayList<>();

		for (Portlet portlet : portlets) {

			// TODO remove V2.  Later the V2 prefix may go away
			// when we start implementing portlet 3.

			// establish the servletContextName for this page, if it is a glob

			if ("tck-V2*".equals(servletContextName)) {
				servletContextName = _getServletContextName(
					portalPageName, servletContextNames);
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					"setupPage: searching for servetContextName = " +
						servletContextName);
			}

			long bundleId = 0L;
			int bundleState = Bundle.UNINSTALLED;

			for (Bundle bundle : bundles) {
				String symbolicName = bundle.getSymbolicName();

				// TODO remove bundle symbolicName magic

				if (symbolicName.startsWith(servletContextName)) {
					bundleId = bundle.getBundleId();
					bundleState = bundle.getState();
				}
			}

			String portletName = portlet.getPortletName();

			if (_log.isInfoEnabled()) {
				_log.info("setupPage: final bundleId = " + bundleId);
			}

			if (bundleId > 0) {
				if (bundleState == Bundle.ACTIVE) {
					String portletId;

					if (portlet.getArtifactType() == Portlet.ArtifactType.WAB) {
						portletId = portletName + portlet.getInstanceToken();
					}
					else {
						String noDashPortletName = portletName.replaceAll(
							"[-]", "");

						String warContext = "_WAR_" + servletContextName;

						portletId =
							noDashPortletName +
								warContext.replaceAll("[.]", "") +
									portlet.getInstanceToken();
					}

					if (_log.isInfoEnabled()) {
						_log.info(
							"adding portletId=[" + portletId +
								"] portletName=[" + portletName + "] ...");
					}

					_addPortlet(
						layoutTypePortlet, userId, columnNumber, portletId);

					// not needed for Liferay 7
					// storePortletPreferences(portalPageLayout, portletId);

					addedPortletIds.add(portletId);
				}
				else {
					_log.error(
						"Unable to add portletName=[" + portletName +
							"] since bundle =[" + servletContextName +
								"] is not active.");
				}
			}
			else {
				_log.error(
					"Unable to add portletName=[" + portletName +
						"] since bundleName=[" + servletContextName +
							"] is not deployed.");
			}

			// columnNumber++;

		}

		LayoutLocalServiceUtil.updateLayout(portalPageLayout);

		if (_log.isInfoEnabled()) {
			_log.info("setupPage: groupId = " + groupId);
			_log.info(
				"setupPage: portalPageLayout.getLayoutId() = " +
					portalPageLayout.getLayoutId());
			_log.info("setupPage: themeId = portlettck_WAR_potlettcktheme");
			_log.info(
				"setupPage: portalPageLayout.getColorSchemetId() = " +
					portalPageLayout.getColorSchemeId());
			_log.info("setupPage: css = ");
			_log.info(
				"setupPage: LayoutLocalServiceUtil.updateLookAndFeel ...");
		}

		LayoutSetLocalServiceUtil.updateLookAndFeel(
			groupId, true, "portlettck_WAR_portlettcktheme",
			portalPageLayout.getColorSchemeId(), null);

		int totalAddedPortlets = addedPortletIds.size();

		if (totalAddedPortlets == 0) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Setting up page name=[" + portalPageName +
						"] without any portlets");
			}
		}
		else if (totalAddedPortlets == 1) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Setting up page name=[" + portalPageName +
						"] with portlet=" +
							addedPortletIds.toString());
			}
		}
		else {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Setting up page name=[" + portalPageName +
						"] with portlets=" +
							addedPortletIds.toString());
			}
		}
	}

	/**
	 * This method sets up the {@link PermissionChecker} {@link ThreadLocal}
	 * prior to performing additional test setup.
	 */
	private static void _setupPermissionChecker(long companyId)
		throws PortalException {

		if (_log.isInfoEnabled()) {
			_log.info("setupPermissionChecker: companyId = " + companyId);
		}

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (permissionChecker == null) {
			Role administratorRole = RoleLocalServiceUtil.getRole(
				companyId, RoleConstants.ADMINISTRATOR);

			User administratorUser = UserLocalServiceUtil.getRoleUsers(
				administratorRole.getRoleId()).get(0);

			try {
				permissionChecker = PermissionCheckerFactoryUtil.create(
					administratorUser);

				PermissionThreadLocal.setPermissionChecker(permissionChecker);
			}
			catch (Exception e) {
				throw new SystemException(e);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(Setup.class);

}