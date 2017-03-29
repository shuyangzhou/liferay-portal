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

package com.liferay.journal.web.internal.portlet.action;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.exception.ArticleIdException;
import com.liferay.journal.exception.DuplicateArticleIdException;
import com.liferay.journal.exception.NoSuchArticleException;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleService;
import com.liferay.portal.kernel.portlet.PortletProvider;
import com.liferay.portal.kernel.portlet.PortletProviderUtil;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSession;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Eduardo Garcia
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + JournalPortletKeys.JOURNAL,
		"mvc.command.name=copyArticle"
	},
	service = MVCActionCommand.class
)
public class CopyArticleMVCActionCommand extends BaseMVCActionCommand {

	protected void copyArticle(ActionRequest actionRequest) throws Exception {
		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		String oldArticleId = ParamUtil.getString(
			actionRequest, "oldArticleId");
		String newArticleId = ParamUtil.getString(
			actionRequest, "newArticleId");
		boolean autoArticleId = ParamUtil.getBoolean(
			actionRequest, "autoArticleId");
		double version = ParamUtil.getDouble(actionRequest, "version");

		JournalArticle newArticle = _journalArticleService.copyArticle(
			groupId, oldArticleId, newArticleId, autoArticleId, version);

		String redirect = ParamUtil.getString(actionRequest, WebKeys.REDIRECT);

		if (!Validator.isBlank(redirect)) {
			redirect = PortalUtil.escapeRedirect(redirect);
		}
		else {
			PortletURL listURL = PortletURLFactoryUtil.create(
				actionRequest, JournalPortletKeys.JOURNAL,
				PortletRequest.RENDER_PHASE);

			redirect = listURL.toString();
		}

		PortletURL redirectURL = PortletProviderUtil.getPortletURL(
			actionRequest, JournalArticle.class.getName(),
			PortletProvider.Action.EDIT);

		redirectURL.setParameter("articleId", newArticle.getArticleId());
		redirectURL.setParameter("groupId", String.valueOf(groupId));
		redirectURL.setParameter("mvcPath", "/edit_article.jsp");
		redirectURL.setParameter("redirect", redirect);

		actionRequest.setAttribute(WebKeys.REDIRECT, redirectURL.toString());

		hideDefaultSuccessMessage(actionRequest);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			copyArticle(actionRequest);
		}
		catch (Exception e) {
			if (e instanceof NoSuchArticleException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				PortletSession portletSession =
					actionRequest.getPortletSession();

				PortletContext portletContext =
					portletSession.getPortletContext();

				PortletRequestDispatcher portletRequestDispatcher =
					portletContext.getRequestDispatcher("/error.jsp");

				portletRequestDispatcher.include(actionRequest, actionResponse);
			}
			else if (e instanceof ArticleIdException ||
					 e instanceof DuplicateArticleIdException) {

				SessionErrors.add(actionRequest, e.getClass());
			}
			else {
				throw e;
			}
		}
	}

	@Reference(unbind = "-")
	protected void setJournalArticleService(
		JournalArticleService journalArticleService) {

		_journalArticleService = journalArticleService;
	}

	private JournalArticleService _journalArticleService;

}