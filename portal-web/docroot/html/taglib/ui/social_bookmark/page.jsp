<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/taglib/ui/social_bookmark/init.jsp" %>

<c:choose>
	<c:when test='<%= displayStyle.equals("menu") %>'>
		<c:if test="<%= Validator.isNotNull(postUrl) %>">
			<a href="<%= postUrl %>" class=" taglib-icon" role="menuitem" tabindex="0">
				<span>

					<%
						if (type.equals("plusone")) {
							type = "google-plus";
						}

						String iconType = "icon-" + type + "-sign";
					%>

					<i class='<%= iconType %>'></i>
				</span>

				<span class="taglib-text-icon">Share on <%= type %></span>
			</a>

		</c:if>
	</c:when>
	<c:otherwise>
		<liferay-util:html-bottom outputKey='<%= "taglib_ui_social_bookmark_link_" + type %>'>

		</liferay-util:html-bottom>

		<aui:a cssClass="social-bookmark-link" href="<%= postUrl %>" target="<%= target %>"><liferay-ui:message key="<%= messageKey %>" /></aui:a>
	</c:otherwise>
</c:choose>