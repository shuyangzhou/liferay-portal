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

import com.liferay.portal.kernel.servlet.taglib.ui.QuickAccessEntry;
import com.liferay.portal.kernel.util.ReflectionUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.taglib.aui.ScriptTag;
import com.liferay.taglib.util.IncludeTag;

import java.io.IOException;

import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * @author     Eudaldo Alonso
 */
public class QuickAccessTag extends IncludeTag {

	public void setContentId(String contentId) {
		_contentId = contentId;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected int processEndTag() throws Exception {
		_quickAccessEntries = (List<QuickAccessEntry>)request.getAttribute(
			WebKeys.PORTLET_QUICK_ACCESS_ENTRIES);

		boolean hasQuickAccessEntries = false;

		if ((_quickAccessEntries != null) && !_quickAccessEntries.isEmpty()) {
			hasQuickAccessEntries = true;
		}

		if (!hasQuickAccessEntries && Validator.isNull(_contentId)) {
			return EVAL_PAGE;
		}

		_randomNamespace = StringUtil.randomId();

		_randomNamespace = _randomNamespace.concat(StringPool.UNDERLINE);

		JspWriter jspWriter = pageContext.getOut();

		jspWriter.write("<nav class=\"quick-access-nav\" id=\"");
		jspWriter.write(_randomNamespace);
		jspWriter.write("quickAccessNav\">");
		jspWriter.write("<h1 class=\"hide-accessible\">");

		MessageTag messageTag = new MessageTag();

		messageTag.setKey("navigation");

		messageTag.doTag(pageContext);

		jspWriter.write("</h1><ul>");

		if (Validator.isNotNull(_contentId)) {
			jspWriter.write("<li><a href=\"");
			jspWriter.write(_contentId);
			jspWriter.write("\">");

			messageTag.setKey("skip-to-content");

			messageTag.doTag(pageContext);

			jspWriter.write("</a></li>");
		}

		if (hasQuickAccessEntries) {
			for (QuickAccessEntry quickAccessEntry : _quickAccessEntries) {
				jspWriter.write("<li><a href=\"");
				jspWriter.write(quickAccessEntry.getURL());
				jspWriter.write("\" id=\"");
				jspWriter.write(_randomNamespace);
				jspWriter.write(quickAccessEntry.getId());
				jspWriter.write("\">");
				jspWriter.write(quickAccessEntry.getContent());
				jspWriter.write("</a></li>");
			}
		}

		jspWriter.write("</ul></nav>");

		if (hasQuickAccessEntries) {
			ScriptTag scriptTag = new ScriptTag();

			scriptTag.setSandbox(true);

			scriptTag.doBodyTag(pageContext, this::_processScriptBody);
		}

		return EVAL_PAGE;
	}

	private void _processScriptBody(PageContext pageContext) {
		JspWriter jspWriter = pageContext.getOut();

		try {
			jspWriter.write("var callbacks = {};");

			for (QuickAccessEntry quickAccessEntry : _quickAccessEntries) {
				String onClick = quickAccessEntry.getOnClick();

				if (Validator.isNotNull(onClick)) {
					jspWriter.write("callbacks['");
					jspWriter.write(_randomNamespace);
					jspWriter.write(quickAccessEntry.getId());
					jspWriter.write("'] = function() {");
					jspWriter.write(onClick);
					jspWriter.write("};");
				}
			}

			jspWriter.write("$('#");
			jspWriter.write(_randomNamespace);
			jspWriter.write(
				"quickAccessNav').on('click','li a',function(event) {");
			jspWriter.write("var callbackFn = ");
			jspWriter.write("callbacks[$(event.currentTarget).attr('id')];");
			jspWriter.write("if (_.isFunction(callbackFn)) {callbackFn();}});");
		}
		catch (IOException ioe) {
			ReflectionUtil.throwException(ioe);
		}
	}

	private static final String _PAGE = "/html/taglib/ui/quick_access/page.jsp";

	private String _contentId;
	private List<QuickAccessEntry> _quickAccessEntries;
	private String _randomNamespace;

}