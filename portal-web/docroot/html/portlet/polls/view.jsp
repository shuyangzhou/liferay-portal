<%--
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
--%>
<%@ page import="com.liferay.portal.model.Group" %>
<%@ page import="com.liferay.portal.service.GroupLocalServiceUtil" %>
<%@ page import="com.liferay.portal.model.GroupConstants" %>
<%@ page import="com.liferay.portlet.polls.model.PollsQuestion" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.liferay.portal.kernel.lar.PortletDataHandlerKeys" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.liferay.portal.kernel.util.Time" %>
<%@ page import="com.liferay.portal.service.LayoutLocalServiceUtil" %>
<%@ page import="com.liferay.portal.kernel.zip.ZipReader" %>
<%@ page import="com.liferay.portal.kernel.zip.ZipReaderFactoryUtil" %>
<%@ page import="java.io.ByteArrayInputStream" %>
<%@ page
	import="com.liferay.portlet.polls.service.PollsQuestionLocalServiceUtil" %>
<%@ page import="com.liferay.portal.model.LayoutConstants" %>
<%@ page import="com.liferay.portal.service.ServiceContext" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.liferay.portal.model.Layout" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.liferay.portal.kernel.util.LocaleUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>
<%@ page import="com.liferay.portal.util.PortletKeys" %>

<%@ include file="/html/portlet/polls/init.jsp" %>

<%
	try{
		Group group2 = GroupLocalServiceUtil.getGroup(
			PortalUtil.getCompanyId(renderRequest), GroupConstants.GUEST);
		long groupId2 = group2.getGroupId();

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setCompanyId(PortalUtil.getDefaultCompanyId());
		serviceContext.setScopeGroupId(groupId2);
		serviceContext.setUserId(user.getUserId());

		Map<Locale, String> map = new HashMap<Locale, String>();

		String layout2Name = StringUtil.randomString(10);
		map.put(LocaleUtil.getDefault(), layout2Name);

		Layout layout2 = LayoutLocalServiceUtil.addLayout(
			user.getUserId(), groupId2, false,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, layout2Name, null, layout2Name,
			LayoutConstants.TYPE_PORTLET, false, "/" + layout2Name, serviceContext);

		HashMap<String, String[]> parameterMap = new HashMap<String, String[]>();

		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA,
			new String[]{Boolean.TRUE.toString()});

		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_DATA_ALL,
			new String[]{Boolean.TRUE.toString()});

		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_SETUP_ALL,
			new String[]{Boolean.FALSE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_CONFIGURATION,
			new String[]{Boolean.FALSE.toString()});
		parameterMap.put(
			PortletDataHandlerKeys.PORTLET_CONFIGURATION_ALL,
			new String[]{Boolean.FALSE.toString()});

		Date startDate = new Date(System.currentTimeMillis() - Time.HOUR);
		Date endDate = new Date();

		PollsQuestion pollsQuestion = PollsQuestionLocalServiceUtil.addQuestion(
			user.getUserId(), map, map, 0, 0, 0, 0, 0, true, null,serviceContext);

		byte[] bytes = LayoutLocalServiceUtil.exportPortletInfo(
			layout2.getPlid(), layout2.getGroupId(), PortletKeys.POLLS,
			parameterMap, startDate, endDate);

		ZipReader zipReader = ZipReaderFactoryUtil.getZipReader(
			new ByteArrayInputStream(bytes));

		out.println(zipReader.getEntries());

		PollsQuestionLocalServiceUtil.deleteQuestion(pollsQuestion);

		LayoutLocalServiceUtil.deleteLayout(layout2);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
%>

<aui:form method="post" name="fm">

	<%
	PortletURL portletURL = renderResponse.createRenderURL();

	portletURL.setParameter("struts_action", "/polls/view");

	List<String> headerNames = new ArrayList<String>();

	headerNames.add("title");
	headerNames.add("num-of-votes");
	headerNames.add("last-vote-date");
	headerNames.add("expiration-date");
	headerNames.add(StringPool.BLANK);

	SearchContainer searchContainer = new SearchContainer(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_DELTA, portletURL, headerNames, null);

	int total = PollsQuestionLocalServiceUtil.getQuestionsCount(scopeGroupId);

	searchContainer.setTotal(total);

	List results = PollsQuestionLocalServiceUtil.getQuestions(scopeGroupId, searchContainer.getStart(), searchContainer.getEnd());

	searchContainer.setResults(results);

	List resultRows = searchContainer.getResultRows();

	for (int i = 0; i < results.size(); i++) {
		PollsQuestion question = (PollsQuestion)results.get(i);

		question = question.toEscapedModel();

		ResultRow row = new ResultRow(question, question.getQuestionId(), i);

		PortletURL rowURL = renderResponse.createRenderURL();

		rowURL.setParameter("struts_action", "/polls/view_question");
		rowURL.setParameter("redirect", currentURL);
		rowURL.setParameter("questionId", String.valueOf(question.getQuestionId()));

		// Title

		row.addText(question.getTitle(locale), rowURL);

		// Number of votes

		int votesCount = PollsVoteLocalServiceUtil.getQuestionVotesCount(question.getQuestionId());

		row.addText(String.valueOf(votesCount), rowURL);

		// Last vote date

		if (question.getLastVoteDate() == null) {
			row.addText(LanguageUtil.get(pageContext, "never"), rowURL);
		}
		else {
			row.addDate(question.getLastVoteDate(), rowURL);
		}

		// Expiration date

		if (question.getExpirationDate() == null) {
			row.addText(LanguageUtil.get(pageContext, "never"), rowURL);
		}
		else {
			row.addDate(question.getExpirationDate(), rowURL);
		}

		// Action

		row.addJSP("right", SearchEntry.DEFAULT_VALIGN, "/html/portlet/polls/question_action.jsp");

		// Add result row

		resultRows.add(row);
	}

	boolean showAddPollButton = PollsPermission.contains(permissionChecker, scopeGroupId, ActionKeys.ADD_QUESTION);
	boolean showPermissionsButton = PollsPermission.contains(permissionChecker, scopeGroupId, ActionKeys.PERMISSIONS);
	%>

	<aui:fieldset>
		<c:if test="<%= showAddPollButton || showPermissionsButton %>">
			<aui:button-row>
				<c:if test="<%= showAddPollButton %>">
					<portlet:renderURL var="editQuestionURL">
						<portlet:param name="struts_action" value="/polls/edit_question" />
						<portlet:param name="redirect" value="<%= currentURL %>" />
					</portlet:renderURL>

					<aui:button href="<%= editQuestionURL %>" value="add-poll" />
				</c:if>

				<c:if test="<%= showPermissionsButton %>">
					<liferay-security:permissionsURL
						modelResource="com.liferay.portlet.polls"
						modelResourceDescription="<%= HtmlUtil.escape(themeDisplay.getScopeGroupName()) %>"
						resourcePrimKey="<%= String.valueOf(scopeGroupId) %>"
						var="permissionsURL"
						windowState="<%= LiferayWindowState.POP_UP.toString() %>"
					/>

					<aui:button href="<%= permissionsURL %>" useDialog="<%= true %>" value="permissions" />
				</c:if>
			</aui:button-row>
		</c:if>

		<liferay-ui:search-iterator searchContainer="<%= searchContainer %>" />
	</aui:fieldset>
</aui:form>