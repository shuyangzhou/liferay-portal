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

package com.liferay.portlet.journalcontent.action;

import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.LayoutTypePortletConstants;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.layoutconfiguration.util.xml.PortletLogic;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Douglas Wong
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String[] extensions = actionRequest.getParameterValues("extensions");

		setPreference(actionRequest, "extensions", extensions);

		super.processAction(portletConfig, actionRequest, actionResponse);

		if (SessionErrors.isEmpty(actionRequest)) {
			updateContentSearch(actionRequest);

			updateLayout(actionRequest);
		}
	}

	protected String getArticleId(PortletRequest portletRequest) {
		String articleId = getParameter(portletRequest, "articleId");

		return articleId.toUpperCase();
	}

	protected String getRuntimePortletIds(
			ThemeDisplay themeDisplay, String articleId)
		throws Exception {

		JournalArticle journalArticle = null;

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			themeDisplay.getCompanyId());

		try {
			journalArticle =
				JournalArticleLocalServiceUtil.getDisplayArticle(
					themeDisplay.getScopeGroupId(), articleId);
		}
		catch (NoSuchArticleException nsae) {
			journalArticle =
				JournalArticleLocalServiceUtil.getDisplayArticle(
					companyGroup.getGroupId(), articleId);
		}

		String portletIds = PortletLogic.getRuntimePortletIds(
			journalArticle.getContent());

		if (Validator.isNotNull(journalArticle.getTemplateId())) {
			JournalTemplate journalTemplate = null;

			try {
				journalTemplate = JournalTemplateLocalServiceUtil.getTemplate(
					themeDisplay.getScopeGroupId(),
					journalArticle.getTemplateId());
			}
			catch (NoSuchTemplateException nste) {
				journalTemplate = JournalTemplateLocalServiceUtil.getTemplate(
					companyGroup.getGroupId(), journalArticle.getTemplateId());
			}

			portletIds = StringUtil.add(
				portletIds,
				PortletLogic.getRuntimePortletIds(journalTemplate.getXsl()));
		}

		return portletIds;
	}

	protected void updateContentSearch(PortletRequest portletRequest)
		throws Exception {

		String articleId = getArticleId(portletRequest);

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		String portletResource = ParamUtil.getString(
			portletRequest, "portletResource");

		JournalContentSearchLocalServiceUtil.updateContentSearch(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			portletResource, articleId, true);
	}

	protected void updateLayout(PortletRequest portletRequest)
		throws Exception {

		String articleId = getArticleId(portletRequest);

		if (Validator.isNull(articleId)) {
			return;
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = themeDisplay.getLayout();

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		String portletResource = ParamUtil.getString(
			portletRequest, "portletResource");

		layoutTypePortlet.setPortletIds(
			LayoutTypePortletConstants.RUNTIME_COLUMN_PREFIX +
				portletResource,
			getRuntimePortletIds(themeDisplay, articleId));

		LayoutLocalServiceUtil.updateLayout(
			layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(),
			layout.getTypeSettings());
	}

}