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

package com.liferay.taglib.util;

import com.liferay.kernel.servlet.taglib.DynamicInclude;
import com.liferay.kernel.servlet.taglib.DynamicIncludeUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.taglib.TagSupport;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * @author Carlos Sierra Andrés
 */
public class DynamicIncludeTag extends TagSupport {

	@Override
	public int doEndTag() throws JspException {
		List<DynamicInclude> dynamicIncludes =
			DynamicIncludeUtil.getDynamicIncludes(getKey());

		if ((dynamicIncludes != null) && !dynamicIncludes.isEmpty()) {
			for (DynamicInclude dynamicInclude : dynamicIncludes) {
				try {
					dynamicInclude.include(getRequest(), getResponse());
				}
				catch (Exception e) {
					_log.error(e, e);
				}
			}
		}

		return super.doEndTag();
	}

	@Override
	public int doStartTag() {
		List<DynamicInclude> dynamicIncludes =
			DynamicIncludeUtil.getDynamicIncludes(getKey());

		if ((dynamicIncludes == null) || dynamicIncludes.isEmpty()) {
			return SKIP_BODY;
		}

		return EVAL_BODY_INCLUDE;
	}

	public String getKey() {
		return _key;
	}

	public void setKey(String key) {
		_key = key;
	}

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest)pageContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		HttpServletResponse httpServletResponse =
			(HttpServletResponse)pageContext.getResponse();

		return new HttpServletResponseWrapper(httpServletResponse) {

			@Override
			public ServletOutputStream getOutputStream() {
				return new ServletOutputStream() {

					@Override
					public void write(int b) throws IOException {
						JspWriter jspWriter = pageContext.getOut();

						jspWriter.write(b);
					}

				};
			}

			@Override
			public PrintWriter getWriter() {
				return new PrintWriter(pageContext.getOut(), true);
			}

		};
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DynamicIncludeTag.class);

	private String _key;

}