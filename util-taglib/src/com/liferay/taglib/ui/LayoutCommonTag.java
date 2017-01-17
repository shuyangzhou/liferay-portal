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

package com.liferay.taglib.ui;

import static javax.servlet.jsp.tagext.Tag.EVAL_PAGE;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * @author Eudaldo Alonso
 */
public class LayoutCommonTag extends IncludeTag {

	@Override
	protected void cleanUp() {
		_includeStaticPortlets = false;
		_includeWebServerDisplayNode = false;
	}

	@Override
	protected String getPage() {
		if (_includeStaticPortlets || _includeWebServerDisplayNode) {
			return _PAGE;
		}

		return null;
	}

	@Override
	protected boolean isCleanUpSetAttributes() {
		return _CLEAN_UP_SET_ATTRIBUTES;
	}

	@Override
	protected int processEndTag() throws Exception {
		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write(
			"<form action=\"#\" id=\"hrefFm\" method=\"post\" " +
				"name=\"hrefFm\"><span></span></form>");

		return EVAL_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (_hasLayoutStaticPortlets && !themeDisplay.isStateExclusive() &&
			!themeDisplay.isStatePopUp() && !themeDisplay.isWidget()) {

			_includeStaticPortlets = true;
		}

		if (_WEB_SERVER_DISPLAY_NODE && !themeDisplay.isStatePopUp()) {
			_includeWebServerDisplayNode = true;
		}

		if (_includeStaticPortlets || _includeWebServerDisplayNode) {
			request.setAttribute(
				"liferay-ui:layout-common:includeStaticPortlets",
				_includeStaticPortlets);

			request.setAttribute(
				"liferay-ui:layout-common:includeWebServerDisplayNode",
				_includeWebServerDisplayNode);
		}
	}

	private static final boolean _CLEAN_UP_SET_ATTRIBUTES = true;

	private static final String _PAGE =
		"/html/taglib/ui/layout_common/page.jsp";

	private static final boolean _WEB_SERVER_DISPLAY_NODE =
		GetterUtil.getBoolean(PropsUtil.get(PropsKeys.WEB_SERVER_DISPLAY_NODE));

	private static final boolean _hasLayoutStaticPortlets = !ArrayUtil.isEmpty(
		PropsUtil.getArray(PropsKeys.LAYOUT_STATIC_PORTLETS_ALL));

	private boolean _includeStaticPortlets;
	private boolean _includeWebServerDisplayNode;

}