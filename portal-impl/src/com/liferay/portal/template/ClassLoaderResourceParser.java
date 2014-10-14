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

package com.liferay.portal.template;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.util.StringPool;

import java.net.URL;

/**
 * @author Tina Tian
 */
public class ClassLoaderResourceParser extends URLResourceParser {

	@Override
	@SuppressWarnings("deprecation")
	public URL getURL(String templateId) {
		if (templateId.contains(TemplateConstants.JOURNAL_SEPARATOR) ||
			templateId.contains(TemplateConstants.SERVLET_SEPARATOR) ||
			templateId.contains(TemplateConstants.TEMPLATE_SEPARATOR) ||
			templateId.contains(TemplateConstants.THEME_LOADER_SEPARATOR)) {

			return null;
		}

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		if (_log.isDebugEnabled()) {
			_log.debug("Loading " + templateId);
		}

		templateId = _normalizePath(templateId);

		return classLoader.getResource(templateId);
	}

	private String _normalizePath(String path) {
		if (path.startsWith(_CURRENT_DIR_PATH_PREFIX)) {
			path = path.substring(_CURRENT_DIR_PATH_PREFIX.length());
		}
		else if (path.startsWith(_PARENT_DIR_PATH_PREFIX)) {
			throw new IllegalArgumentException("Unable to parse path " + path);
		}

		while (true) {
			int index = path.indexOf(_CURRENT_DIR_PATH);

			if (index < 0) {
				break;
			}

			path =
				path.substring(0, index + 1) +
					path.substring(index + _CURRENT_DIR_PATH.length());
		}

		while (true) {
			int index = path.indexOf(_PARENT_DIR_PATH);

			if (index < 0) {
				break;
			}

			int startIndex = path.lastIndexOf(StringPool.SLASH, index - 1);

			int endIndex = index + _PARENT_DIR_PATH.length();

			path = path.substring(0, startIndex + 1) + path.substring(endIndex);
		}

		return path;
	}

	private static final String _CURRENT_DIR_PATH = "/./";

	private static final String _CURRENT_DIR_PATH_PREFIX = "./";

	private static final String _PARENT_DIR_PATH = "/../";

	private static final String _PARENT_DIR_PATH_PREFIX = "../";

	private static Log _log = LogFactoryUtil.getLog(
		ClassLoaderResourceParser.class);

}