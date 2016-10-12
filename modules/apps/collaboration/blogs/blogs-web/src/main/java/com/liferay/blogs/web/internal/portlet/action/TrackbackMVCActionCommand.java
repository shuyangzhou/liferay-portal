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

package com.liferay.blogs.web.internal.portlet.action;

import com.liferay.blogs.kernel.exception.NoSuchEntryException;
import com.liferay.blogs.kernel.exception.TrackbackValidationException;
import com.liferay.blogs.model.BlogsEntry;
import com.liferay.blogs.web.constants.BlogsPortletKeys;
import com.liferay.portal.kernel.comment.CommentManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFunction;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Function;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.blogs.linkback.LinkbackConsumerUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexander Chow
 */
@Component(
	immediate = true,
	property = {
		"auth.token.ignore.mvc.action=true",
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_AGGREGATOR,
		"mvc.command.name=/blogs/trackback"
	},
	service = MVCActionCommand.class
)
public class TrackbackMVCActionCommand extends BaseMVCActionCommand {

	public void addTrackback(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			BlogsEntry entry = getBlogsEntry(actionRequest);

			validate(entry);

			ThemeDisplay themeDisplay =
				(ThemeDisplay)actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

			HttpServletRequest request = PortalUtil.getHttpServletRequest(
				actionRequest);

			HttpServletRequest originalRequest =
				PortalUtil.getOriginalServletRequest(request);

			String excerpt = ParamUtil.getString(originalRequest, "excerpt");
			String url = ParamUtil.getString(originalRequest, "url");
			String blogName = ParamUtil.getString(originalRequest, "blog_name");
			String title = ParamUtil.getString(originalRequest, "title");

			validate(actionRequest, request.getRemoteAddr(), url);

			_addTrackback(
				entry, themeDisplay, excerpt, url, blogName, title,
				new ServiceContextFunction(actionRequest));
		}
		catch (TrackbackValidationException tve) {
			sendError(actionRequest, actionResponse, tve.getMessage());

			return;
		}

		sendSuccess(actionRequest, actionResponse);
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		try {
			addTrackback(actionRequest, actionResponse);
		}
		catch (NoSuchEntryException nsee) {
			if (_log.isWarnEnabled()) {
				_log.warn(nsee, nsee);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected BlogsEntry getBlogsEntry(ActionRequest actionRequest)
		throws Exception {

		try {
			ActionUtil.getEntry(actionRequest);
		}
		catch (PrincipalException pe) {
			throw new TrackbackValidationException(
				"Blog entry must have guest view permissions to enable " +
					"trackbacks",
				pe);
		}

		return (BlogsEntry)actionRequest.getAttribute(WebKeys.BLOGS_ENTRY);
	}

	protected boolean isCommentsEnabled(ActionRequest actionRequest)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getExistingPortletSetup(
				actionRequest);

		if (portletPreferences == null) {
			portletPreferences = actionRequest.getPreferences();
		}

		return GetterUtil.getBoolean(
			portletPreferences.getValue("enableComments", null), true);
	}

	protected void sendError(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String msg)
		throws Exception {

		sendResponse(actionRequest, actionResponse, msg, false);
	}

	protected void sendResponse(
			ActionRequest actionRequest, ActionResponse actionResponse,
			String msg, boolean success)
		throws Exception {

		StringBundler sb = new StringBundler(7);

		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<response>");

		if (success) {
			sb.append("<error>0</error>");
		}
		else {
			sb.append("<error>1</error>");
			sb.append("<message>");
			sb.append(msg);
			sb.append("</message>");
		}

		sb.append("</response>");

		HttpServletRequest request = PortalUtil.getHttpServletRequest(
			actionRequest);
		HttpServletResponse response = PortalUtil.getHttpServletResponse(
			actionResponse);

		ServletResponseUtil.sendFile(
			request, response, null, sb.toString().getBytes(StringPool.UTF8),
			ContentTypes.TEXT_XML_UTF8);
	}

	protected void sendSuccess(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		sendResponse(actionRequest, actionResponse, null, true);
	}

	protected void validate(
			ActionRequest actionRequest, String remoteIP, String url)
		throws Exception {

		if (!isCommentsEnabled(actionRequest)) {
			throw new TrackbackValidationException("Comments are disabled");
		}

		if (Validator.isNull(url)) {
			throw new TrackbackValidationException(
				"Trackback requires a valid permanent URL");
		}

		String trackbackIP = HttpUtil.getIpAddress(url);

		if (!remoteIP.equals(trackbackIP)) {
			throw new TrackbackValidationException(
				"Remote IP does not match the trackback URL's IP");
		}
	}

	protected void validate(BlogsEntry entry)
		throws TrackbackValidationException {

		if (!entry.isAllowTrackbacks()) {
			throw new TrackbackValidationException(
				"Trackbacks are not enabled");
		}
	}

	private void _addTrackback(
			BlogsEntry entry, ThemeDisplay themeDisplay, String excerpt,
			String url, String blogName, String title,
			Function<String, ServiceContext> serviceContextFunction)
		throws PortalException {

		long userId = _userLocalService.getDefaultUserId(
			themeDisplay.getCompanyId());
		long groupId = entry.getGroupId();
		String className = BlogsEntry.class.getName();
		long classPK = entry.getEntryId();

		String body = _buildBody(themeDisplay, excerpt, url);

		long commentId = _commentManager.addComment(
			userId, groupId, className, classPK, blogName, title, body,
			serviceContextFunction);

		String entryURL = _buildEntryURL(entry, themeDisplay);

		LinkbackConsumerUtil.addNewTrackback(commentId, url, entryURL);
	}

	private String _buildBBCodeBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		url = StringUtil.replace(
			url, new char[] {CharPool.CLOSE_BRACKET, CharPool.OPEN_BRACKET},
			new String[] {"%5D", "%5B"});

		StringBundler sb = new StringBundler(7);

		sb.append("[...] ");
		sb.append(excerpt);
		sb.append(" [...] [url=");
		sb.append(url);
		sb.append("]");
		sb.append(themeDisplay.translate("read-more"));
		sb.append("[/url]");

		return sb.toString();
	}

	private String _buildBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		if (PropsValues.DISCUSSION_COMMENTS_FORMAT.equals("bbcode")) {
			return _buildBBCodeBody(themeDisplay, excerpt, url);
		}

		return _buildHTMLBody(themeDisplay, excerpt, url);
	}

	private String _buildEntryURL(BlogsEntry entry, ThemeDisplay themeDisplay)
		throws PortalException {

		StringBundler sb = new StringBundler(4);

		sb.append(PortalUtil.getLayoutFullURL(themeDisplay));
		sb.append(Portal.FRIENDLY_URL_SEPARATOR);
		sb.append("blogs/");
		sb.append(entry.getUrlTitle());

		return sb.toString();
	}

	private String _buildHTMLBody(
		ThemeDisplay themeDisplay, String excerpt, String url) {

		StringBundler sb = new StringBundler(7);

		sb.append("[...] ");
		sb.append(excerpt);
		sb.append(" [...] <a href=\"");
		sb.append(url);
		sb.append("\">");
		sb.append(themeDisplay.translate("read-more"));
		sb.append("</a>");

		return sb.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		TrackbackMVCActionCommand.class);

	@Reference
	private CommentManager _commentManager;

	@Reference
	private UserLocalService _userLocalService;

}