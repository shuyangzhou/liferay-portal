/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.check.util.JavaSourceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class JavaXMLElementTextCheck extends BaseFileCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
		String fileName, String absolutePath, String content) {

		if (absolutePath.contains("/test/") ||
			absolutePath.contains("/testIntegration/") ||
			!content.contains(
				"import com.liferay.portal.kernel.xml.Document;") ||
			content.contains("DocumentException")) {

			return content;
		}

		Matcher matcher = _documentPattern.matcher(content);

		while (matcher.find()) {
			if (ToolsUtil.isInsideQuotes(content, matcher.start())) {
				continue;
			}

			String methodCall = JavaSourceUtil.getMethodCall(
				content, matcher.end());

			List<String> parameterList = JavaSourceUtil.getParameterList(
				methodCall);

			int end = matcher.end() + methodCall.length();

			if ((parameterList.size() != 1) ||
				!content.startsWith(";\n", end)) {

				continue;
			}

			String newContent = _replaceElementTexts(
				content, matcher.group(1), parameterList.get(0),
				matcher.start(), end + 1);

			if (newContent != null) {
				return JavaSourceUtil.addImports(
					newContent, "com.liferay.portal.kernel.xml.XMLUtil");
			}
		}

		return content;
	}

	private int _getCount(String s, String regex) {
		int count = 0;

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(s);

		while (matcher.find()) {
			count++;
		}

		return count;
	}

	private String _getScope(String content, int start) {
		int level = 0;
		int x = start;

		while (true) {
			int y = content.indexOf(CharPool.NEW_LINE, x + 1);

			if (y == -1) {
				return content.substring(start);
			}

			level += ToolsUtil.getLevel(
				content.substring(x, y), StringPool.OPEN_CURLY_BRACE,
				StringPool.CLOSE_CURLY_BRACE);

			if (level < 0) {
				return content.substring(start, y);
			}

			x = y;
		}
	}

	private String _replaceElementTexts(
		String content, String documentName, String xmlName, int documentStart,
		int scopeStart) {

		int lineStart = content.lastIndexOf(CharPool.NEW_LINE, documentStart);

		String indent = content.substring(lineStart + 1, documentStart);

		Pattern pattern = Pattern.compile(
			"\\bString\\s+" + xmlName + "\\s*[,:;=)]");

		Matcher matcher = pattern.matcher(content);

		String scope = _getScope(content, scopeStart);

		if (!Validator.isBlank(StringUtil.trim(indent)) ||
			!xmlName.matches("\\w+") || !matcher.find() ||
			(_getCount(scope, "\\b" + xmlName + "\\s*\\+?=[^=]") > 0)) {

			return null;
		}

		pattern = Pattern.compile(
			StringBundler.concat(
				"\\bElement\\s+(\\w+)\\s*=\\s*", documentName,
				"\\.getRootElement\\(\\);"));

		matcher = pattern.matcher(scope);

		List<String> rootElementNames = new ArrayList<>();

		while (matcher.find()) {
			rootElementNames.add(matcher.group(1));
		}

		if (rootElementNames.isEmpty() ||
			(rootElementNames.size() != _getCount(
				scope, "\\b" + documentName + "\\b"))) {

			return null;
		}

		String newScope = scope;

		for (String rootElementName : rootElementNames) {
			if (_getCount(newScope, "\\b" + rootElementName + "\\b") < 2) {
				return null;
			}

			newScope = newScope.replaceAll(
				StringBundler.concat(
					"\\b", rootElementName,
					"\\.element\\(\\s*(\"[^\"]*\")\\s*\\)\\.getText\\(\\)"),
				"XMLUtil.getElementText(" + xmlName + ", $1)");
			newScope = newScope.replaceAll(
				StringBundler.concat(
					"\\b", rootElementName,
					"\\.elementText\\(\\s*(\"[^\"]*\")\\s*\\)"),
				"XMLUtil.getElementText(" + xmlName + ", $1)");

			if (_getCount(newScope, "\\b" + rootElementName + "\\b") != 1) {
				return null;
			}

			newScope = newScope.replaceFirst(
				StringBundler.concat(
					"(?m)^[\\t ]*Element\\s+", rootElementName, "\\s*=\\s*",
					documentName, "\\.getRootElement\\(\\);\\n\\n?"),
				StringPool.BLANK);
		}

		int x = 1;

		if (newScope.startsWith("\n\n")) {
			x = 2;
		}

		return StringBundler.concat(
			content.substring(0, lineStart + 1), newScope.substring(x),
			content.substring(scopeStart + scope.length()));
	}

	private static final Pattern _documentPattern = Pattern.compile(
		"\\bDocument\\s+(\\w+)\\s*=\\s*((Unsecure)?SAXReaderUtil(\\." +
			"getSAXReader\\(\\))?|_?saxReader)\\.(?=read\\()");

}