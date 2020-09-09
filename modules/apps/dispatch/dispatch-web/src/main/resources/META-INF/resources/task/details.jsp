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

long dispatchTaskId = 0;

DispatchTask dispatchTask = dispatchTaskDisplayContext.getDispatchTask();

String dispatchTaskType = ParamUtil.getString(request, "dispatchTaskType");

String typeSettings = StringPool.BLANK;

if (dispatchTask != null) {
	dispatchTaskId = dispatchTask.getDispatchTaskId();
	dispatchTaskType = dispatchTask.getType();
	typeSettings = dispatchTask.getTypeSettings();
}
%>

<portlet:actionURL name="editDispatchTask" var="editDispatchTaskActionURL" />

<div class="closed container-fluid-1280" id="<portlet:namespace />editDispatchTaskId">
	<div class="container main-content-body sheet">
		<liferay-ui:error exception="<%= NoSuchLogException.class %>" message="the-log-could-not-be-found" />
		<liferay-ui:error exception="<%= NoSuchTaskException.class %>" message="the-task-could-not-be-found" />

		<liferay-ui:error-principal />

		<aui:form action="<%= editDispatchTaskActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
			<aui:input name="dispatchTaskId" type="hidden" value="<%= String.valueOf(dispatchTaskId) %>" />
			<aui:input name="dispatchTaskType" type="hidden" value="<%= dispatchTaskType %>" />
			<aui:input name="typeSettings" type="hidden" />

			<div class="lfr-form-content">
				<aui:model-context bean="<%= dispatchTask %>" model="<%= DispatchTask.class %>" />

				<aui:fieldset>
					<aui:input disabled="<%= (dispatchTask != null) && dispatchTask.isSystem() %>" name="name" required="<%= true %>" />
				</aui:fieldset>

				<div id="<portlet:namespace />typeSettingsEditor"></div>

				<aui:button-row>

					<%
					String taglibSaveOnClick = "Liferay.fire('" + liferayPortletResponse.getNamespace() + "saveTask');";
					%>

					<aui:button onClick="<%= taglibSaveOnClick %>" value="save" />

					<aui:button cssClass="btn-lg" href="<%= backURL %>" type="cancel" />
				</aui:button-row>
			</div>
		</aui:form>
	</div>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />selectType',
		function () {
			var A = AUI();

			var dispatchTaskType = A.one(<portlet:namespace />type).val();

			var portletURL = new Liferay.PortletURL.createURL(
				'<%= currentURLObj %>'
			);

			portletURL.setParameter('type', dispatchTaskType);

			window.location.replace(portletURL.toString());
		},
		['liferay-portlet-url']
	);
</aui:script>

<aui:script use="aui-ace-editor,liferay-xml-formatter">
	var STR_VALUE = 'value';

	var contentEditor = new A.AceEditor({
		boundingBox: '#<portlet:namespace />typeSettingsEditor',
		height: 600,
		mode: 'xml',
		tabSize: 4,
		width: '100%',
	}).render();

	var xmlFormatter = new Liferay.XMLFormatter();

	var content = xmlFormatter.format('<%= HtmlUtil.escapeJS(typeSettings) %>');

	if (content) {
		content = content.trim();
	}

	contentEditor.set(STR_VALUE, content);

	Liferay.on('<portlet:namespace />saveTask', function (event) {
		var form = AUI.$('#<portlet:namespace />fm');

		form.fm('typeSettings').val(contentEditor.get(STR_VALUE));

		submitForm(form);
	});
</aui:script>