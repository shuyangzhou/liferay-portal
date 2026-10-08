/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.source.formatter.check.util.JavaSourceUtil;
import com.liferay.source.formatter.parser.JavaTerm;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class JavaRedundantAssertMessageCheck extends BaseJavaTermCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
		String fileName, String absolutePath, JavaTerm javaTerm,
		String fileContent) {

		String content = javaTerm.getContent();

		List<String> importNames = getImportNames(javaTerm);

		if (!importNames.contains("org.junit.Assert")) {
			return content;
		}

		String codeContent = _getCodeContent(content);
		String fileCodeContent = null;

		Matcher matcher = _assertPattern.matcher(codeContent);

		while (matcher.find()) {
			String methodCall = JavaSourceUtil.getMethodCall(
				content, matcher.start());

			List<String> parameters = JavaSourceUtil.splitParameters(
				JavaSourceUtil.getParameters(methodCall));

			String methodName = matcher.group(1);

			if (methodName.equals("assertNull")) {
				if (parameters.size() != 2) {
					continue;
				}
			}
			else if (parameters.size() != 3) {
				continue;
			}

			String message = parameters.get(0);

			String value = _getStringifiedValue(
				codeContent, fileContent, javaTerm, message);

			if ((value == null) ||
				!_containsValue(
					parameters.subList(1, parameters.size()), value)) {

				continue;
			}

			if (fileCodeContent == null) {
				fileCodeContent = _getCodeContent(fileContent);
			}

			String trimmedMessage = _removeWhitespace(message);

			if (trimmedMessage.startsWith("String.valueOf(") &&
				_isCharArray(fileCodeContent, value)) {

				continue;
			}

			if (methodName.equals("assertEquals") &&
				!_isNonstringValue(
					codeContent, fileCodeContent, fileContent, absolutePath,
					javaTerm, importNames, parameters.get(1)) &&
				!_isNonstringValue(
					codeContent, fileCodeContent, fileContent, absolutePath,
					javaTerm, importNames, parameters.get(2))) {

				continue;
			}

			int x = methodCall.indexOf(
				message, methodCall.indexOf(CharPool.OPEN_PARENTHESIS));

			int y = methodCall.indexOf(CharPool.COMMA, x + message.length());

			String newMethodCall =
				methodCall.substring(0, x) +
					StringUtil.trimLeading(methodCall.substring(y + 1));

			return StringUtil.replaceFirst(
				content, methodCall, newMethodCall, matcher.start());
		}

		return content;
	}

	@Override
	protected String[] getCheckableJavaTermNames() {
		return new String[] {JAVA_METHOD};
	}

	private boolean _containsValue(List<String> values, String value) {
		for (String curValue : values) {
			if (value.equals(_removeWhitespace(curValue))) {
				return true;
			}
		}

		return false;
	}

	private String _getCodeContent(String content) {
		char[] chars = content.toCharArray();

		int i = 0;

		while (i < chars.length) {
			int end = -1;

			if (content.startsWith("//", i)) {
				end = content.indexOf(CharPool.NEW_LINE, i);
			}
			else if (content.startsWith("/*", i)) {
				end = content.indexOf("*/", i + 2);

				if (end != -1) {
					end += 2;
				}
			}
			else if (content.startsWith("\"\"\"", i)) {
				end = content.indexOf("\"\"\"", i + 3);

				while ((end != -1) && (chars[end - 1] == CharPool.BACK_SLASH)) {
					end = content.indexOf("\"\"\"", end + 1);
				}

				if (end != -1) {
					end += 3;
				}
			}
			else if ((chars[i] == CharPool.APOSTROPHE) ||
					 (chars[i] == CharPool.QUOTE)) {

				end = i + 1;

				while ((end < chars.length) && (chars[end] != chars[i])) {
					if (chars[end] == CharPool.BACK_SLASH) {
						end++;
					}

					end++;
				}

				end++;
			}
			else {
				i++;

				continue;
			}

			if ((end == -1) || (end > chars.length)) {
				end = chars.length;
			}

			for (int j = i; j < end; j++) {
				if (chars[j] != CharPool.NEW_LINE) {
					chars[j] = CharPool.SPACE;
				}
			}

			i = end;
		}

		return new String(chars);
	}

	private String _getStringifiedValue(
		String codeContent, String fileContent, JavaTerm javaTerm,
		String message) {

		message = _removeWhitespace(message);

		String value = null;

		if (message.endsWith(".toString()")) {
			value = message.substring(0, message.length() - 11);
		}
		else if (message.startsWith("String.valueOf(") &&
				 message.endsWith(")")) {

			value = message.substring(15, message.length() - 1);
		}
		else {
			return null;
		}

		Matcher matcher = _sideEffectFreeValuePattern.matcher(value);

		if (!matcher.matches()) {
			return null;
		}

		if (matcher.group(4) == null) {
			return value;
		}

		if (matcher.group(2) != null) {
			return null;
		}

		String variableTypeName = _getVariableTypeName(
			codeContent, fileContent, javaTerm, matcher.group(1));

		if (variableTypeName == null) {
			return null;
		}

		int x = variableTypeName.indexOf(CharPool.LESS_THAN);

		if (x != -1) {
			variableTypeName = variableTypeName.substring(0, x);
		}

		if (ArrayUtil.contains(_GETTER_TYPE_NAMES, variableTypeName)) {
			return value;
		}

		return null;
	}

	private String _getVariableTypeName(
		String codeContent, String fileContent, JavaTerm javaTerm,
		String variableName) {

		List<String> variableTypeNames = _getVariableTypeNames(
			codeContent, fileContent, javaTerm, variableName);

		if (variableTypeNames.isEmpty()) {
			return null;
		}

		String variableTypeName = variableTypeNames.get(0);

		for (String curVariableTypeName : variableTypeNames) {
			if (!variableTypeName.equals(curVariableTypeName)) {
				return null;
			}
		}

		return variableTypeName;
	}

	private List<String> _getVariableTypeNames(
		String codeContent, String fileContent, JavaTerm javaTerm,
		String variableName) {

		List<String> variableTypeNames = new ArrayList<>();

		String quotedVariableName = Pattern.quote(variableName);

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"(?<![\\w$.])", quotedVariableName, "\\s*->|\\(\\s*([\\w$]+",
				"\\s*,\\s*)*", quotedVariableName,
				"\\s*(,\\s*[\\w$]+\\s*)*\\)\\s*->"));

		Matcher matcher = pattern.matcher(codeContent);

		if (matcher.find()) {
			variableTypeNames.add(StringPool.BLANK);

			return variableTypeNames;
		}

		pattern = Pattern.compile(
			"[\\w$\\]>](\\s+)" + quotedVariableName + "\\s*[;=),:&|]");

		matcher = pattern.matcher(codeContent);

		while (matcher.find()) {
			int x = matcher.start(1);

			char c1 = codeContent.charAt(x - 1);
			char c2 = codeContent.charAt(x - 2);

			if (((c1 == CharPool.CLOSE_BRACKET) &&
				 (c2 != CharPool.OPEN_BRACKET)) ||
				((c1 == CharPool.GREATER_THAN) &&
				 !Character.isJavaIdentifierPart(c2) &&
				 (c2 != CharPool.GREATER_THAN) && (c2 != CharPool.QUESTION))) {

				continue;
			}

			String variableTypeName = StringPool.BLANK;

			for (int y = x - 1; y >= 0; y--) {
				if (Character.isJavaIdentifierPart(codeContent.charAt(y)) ||
					!Character.isJavaIdentifierStart(
						codeContent.charAt(y + 1))) {

					continue;
				}

				String s = codeContent.substring(y + 1, x);

				if ((getLevel(s, "<", ">") == 0) &&
					(getLevel(s, "[", "]") == 0)) {

					variableTypeName = _removeWhitespace(s);

					break;
				}
			}

			if (!ArrayUtil.contains(_KEYWORDS, variableTypeName)) {
				variableTypeNames.add(variableTypeName);
			}
		}

		if (!variableTypeNames.isEmpty() ||
			(!variableName.startsWith(StringPool.UNDERLINE) &&
			 !variableName.matches("[A-Z][A-Z0-9_]*"))) {

			return variableTypeNames;
		}

		String variableTypeName = getVariableTypeName(
			StringPool.BLANK, javaTerm, fileContent, null, variableName, true,
			false);

		if (variableTypeName != null) {
			variableTypeNames.add(_removeWhitespace(variableTypeName));
		}

		return variableTypeNames;
	}

	private boolean _isCharArray(String fileCodeContent, String value) {
		if (!Validator.isVariableName(value)) {
			return false;
		}

		String quotedValue = Pattern.quote(value);

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"\\bchar\\s*(\\[\\s*\\]|\\.\\.\\.)\\s*", quotedValue,
				"(?![\\w$])|\\bchar\\s+", quotedValue, "\\s*\\["));

		Matcher matcher = pattern.matcher(fileCodeContent);

		return matcher.find();
	}

	private boolean _isNonstringValue(
		String codeContent, String fileCodeContent, String fileContent,
		String absolutePath, JavaTerm javaTerm, List<String> importNames,
		String value) {

		if (Validator.isNumber(value)) {
			return true;
		}

		if (!Validator.isVariableName(value)) {
			return false;
		}

		String variableTypeName = _getVariableTypeName(
			codeContent, fileContent, javaTerm, value);

		if ((variableTypeName == null) || variableTypeName.isEmpty() ||
			variableTypeName.endsWith("]")) {

			return false;
		}

		if (Character.isLowerCase(variableTypeName.charAt(0))) {
			return ArrayUtil.contains(_PRIMITIVE_TYPE_NAMES, variableTypeName);
		}

		int x = variableTypeName.indexOf(CharPool.LESS_THAN);

		if (x != -1) {
			variableTypeName = variableTypeName.substring(0, x);
		}

		if (ArrayUtil.contains(_STRING_TYPE_NAMES, variableTypeName)) {
			return false;
		}

		if (ArrayUtil.contains(_JAVA_LANG_TYPE_NAMES, variableTypeName)) {
			return true;
		}

		for (String importName : importNames) {
			if (importName.endsWith(StringPool.PERIOD + variableTypeName)) {
				return true;
			}
		}

		Pattern pattern = Pattern.compile(
			"\\b(class|enum|interface|record)\\s+" + variableTypeName + "\\b");

		Matcher matcher = pattern.matcher(fileCodeContent);

		if (matcher.find()) {
			return true;
		}

		File file = new File(absolutePath);

		file = new File(file.getParentFile(), variableTypeName + ".java");

		return file.exists();
	}

	private String _removeWhitespace(String s) {
		return StringUtil.removeChars(
			s, CharPool.NEW_LINE, CharPool.SPACE, CharPool.TAB);
	}

	private static final String[] _GETTER_TYPE_NAMES = {
		"AtomicBoolean", "AtomicInteger", "AtomicLong", "AtomicReference",
		"Optional", "OptionalDouble", "OptionalInt", "OptionalLong",
		"ThreadLocal"
	};

	private static final String[] _JAVA_LANG_TYPE_NAMES = {
		"Boolean", "Byte", "Character", "Class", "Double", "Float", "Integer",
		"Long", "Number", "Short", "Throwable"
	};

	private static final String[] _KEYWORDS = {
		"assert", "break", "case", "continue", "do", "else", "new", "return",
		"throw", "yield"
	};

	private static final String[] _PRIMITIVE_TYPE_NAMES = {
		"boolean", "byte", "char", "double", "float", "int", "long", "short"
	};

	private static final String[] _STRING_TYPE_NAMES = {
		"CharSequence", "Comparable", "Object", "Serializable", "String"
	};

	private static final Pattern _assertPattern = Pattern.compile(
		"(?<![\\w.])Assert\\.(assertEquals|assertNotEquals|assertNull|" +
			"assertSame)\\(");
	private static final Pattern _sideEffectFreeValuePattern = Pattern.compile(
		"([A-Za-z_$][\\w$]*)(\\[(\\d+|[A-Za-z_$][\\w$]*)\\])?" +
			"(\\.get\\(\\))?" +
				"(\\.(get(?!And[A-Z])\\w+|is[A-Z]\\w*|size)\\(\\))*");

}