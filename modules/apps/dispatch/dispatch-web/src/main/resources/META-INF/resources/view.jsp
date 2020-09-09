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

<%@ include file="/init.jsp" %>

<%
DispatchTaskDisplayContext dispatchTaskDisplayContext = (DispatchTaskDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

PortletURL portletURL = dispatchTaskDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "dispatchTasks");

request.setAttribute("view.jsp-portletURL", portletURL);
%>

<liferay-util:include page="/dispatch_task_toolbar.jsp" servletContext="<%= application %>">
	<liferay-util:param name="searchContainerId" value="dispatchTasks" />
</liferay-util:include>

<div id="<portlet:namespace />dispatchTasksContainer">
	<div class="closed container-fluid-1280" id="<portlet:namespace />infoPanelId">
		<div class="container">
			<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
				<aui:input name="<%= Constants.CMD %>" type="hidden" />
				<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
				<aui:input name="deleteDispatchTaskIds" type="hidden" />

				<div class="dispatch-task-lists-container" id="<portlet:namespace />entriesContainer">
					<liferay-ui:search-container
						id="dispatchTasks"
						searchContainer="<%= dispatchTaskDisplayContext.getSearchContainer() %>"
					>
						<liferay-ui:search-container-row
							className="com.liferay.dispatch.model.DispatchTask"
							cssClass="entry-display-style"
							keyProperty="dispatchTaskId"
							modelVar="dispatchTask"
						>

							<%
							PortletURL rowURL = renderResponse.createRenderURL();

							rowURL.setParameter("mvcRenderCommandName", "editDispatchTask");
							rowURL.setParameter("redirect", currentURL);
							rowURL.setParameter("dispatchTaskId", String.valueOf(dispatchTask.getDispatchTaskId()));
							%>

							<liferay-ui:search-container-column-text
								cssClass="important table-cell-content"
								href="<%= rowURL %>"
								property="name"
							/>

							<liferay-ui:search-container-column-text
								cssClass="table-cell-content"
								property="type"
							/>

							<liferay-ui:search-container-column-text
								cssClass="table-cell-content"
								name="system"
								value='<%= dispatchTask.isSystem() ? LanguageUtil.get(request, "yes") : LanguageUtil.get(request, "no") %>'
							/>

							<liferay-ui:search-container-column-text
								cssClass="table-cell-content"
								name="next-fire-date"
								value="<%= dispatchTaskDisplayContext.getNextFireDate(dispatchTask.getDispatchTaskId()) %>"
							/>

							<liferay-ui:search-container-column-jsp
								cssClass="table-cell-content"
								path="/task/buttons.jsp"
							/>

							<liferay-ui:search-container-column-jsp
								cssClass="entry-action-column"
								path="/dispatch_task_action.jsp"
							/>
						</liferay-ui:search-container-row>

						<liferay-ui:search-iterator
							displayStyle="list"
							markupView="lexicon"
						/>
					</liferay-ui:search-container>
				</div>
			</aui:form>
		</div>
	</div>
</div>