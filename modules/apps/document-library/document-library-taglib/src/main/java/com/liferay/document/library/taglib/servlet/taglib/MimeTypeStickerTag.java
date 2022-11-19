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

package com.liferay.document.library.taglib.servlet.taglib;

import com.liferay.document.library.taglib.internal.display.context.DLViewFileVersionDisplayContextUtil;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alejandro Tardín
 */
public class MimeTypeStickerTag extends IncludeTag {

	@Override
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	public String getCssClass() {
		return _cssClass;
	}

	public FileVersion getFileVersion() {
		return _fileVersion;
	}

	public void setCssClass(String cssClass) {
		_cssClass = cssClass;
	}

	public void setFileVersion(FileVersion fileVersion) {
		_fileVersion = fileVersion;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_cssClass = null;
		_fileVersion = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		httpServletRequest.setAttribute(
			"liferay-document-library:mime-type-sticker:cssClass", _cssClass);
		httpServletRequest.setAttribute(
			"liferay-document-library:mime-type-sticker:" +
				"dlViewFileVersionDisplayContext",
			DLViewFileVersionDisplayContextUtil.
				getDLViewFileVersionDisplayContext(
					httpServletRequest,
					(HttpServletResponse)pageContext.getResponse(),
					_fileVersion));
	}

	private static final String _PAGE = "/mime_type_sticker/page.jsp";

	private String _cssClass;
	private FileVersion _fileVersion;

}