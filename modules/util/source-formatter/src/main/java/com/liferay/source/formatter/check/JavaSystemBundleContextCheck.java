/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.portal.tools.ToolsUtil;

import java.util.List;

/**
 * @author Shuyang Zhou
 */
public class JavaSystemBundleContextCheck extends BaseFileCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		if (!content.contains(_GET_BUNDLE_CONTEXT) ||
			absolutePath.contains("/src/test/") ||
			!isModulesFile(absolutePath)) {

			return content;
		}

		List<String> allowedFileNames = getAttributeValues(
			_ALLOWED_FILE_NAMES_KEY, absolutePath);

		for (String allowedFileName : allowedFileNames) {
			if (absolutePath.endsWith(allowedFileName)) {
				return content;
			}
		}

		int x = content.indexOf(_GET_BUNDLE_CONTEXT);

		while (x != -1) {
			if (!ToolsUtil.isInsideQuotes(content, x)) {
				addMessage(
					fileName, _getMessage(absolutePath, content),
					getLineNumber(content, x));
			}

			x = content.indexOf(_GET_BUNDLE_CONTEXT, x + 1);
		}

		return content;
	}

	private String _getMessage(String absolutePath, String content) {
		if (absolutePath.contains("/src/testIntegration/")) {
			return "Use BundleContextTestUtil.getBundleContext(<Class>." +
				"class) instead of SystemBundleUtil.getBundleContext()";
		}

		if (content.contains("@Component")) {
			return "Use the BundleContext of the @Activate method instead of " +
				"SystemBundleUtil.getBundleContext()";
		}

		return "Use FrameworkUtil.getBundle(<Class>.class) and its " +
			"BundleContext instead of SystemBundleUtil.getBundleContext()";
	}

	private static final String _ALLOWED_FILE_NAMES_KEY = "allowedFileNames";

	private static final String _GET_BUNDLE_CONTEXT =
		"SystemBundleUtil.getBundleContext()";

}