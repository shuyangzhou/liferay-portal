/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ToolsUtil;
import com.liferay.source.formatter.check.util.JSPSourceUtil;
import com.liferay.source.formatter.check.util.JavaSourceUtil;
import com.liferay.source.formatter.check.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class BeanParamUtilGetterCheck extends BaseJSPTermsCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		if (!content.contains("BeanParamUtil.")) {
			return content;
		}

		List<String> dependentContents = new ArrayList<>();

		if (!fileName.endsWith(".java")) {
			populateContentsMap(fileName, content);

			dependentContents = _getDependentContents(fileName);
		}

		Matcher matcher = _beanParamUtilPattern.matcher(content);

		while (matcher.find()) {
			int x = matcher.start();

			if (!isJavaSource(content, x, true) ||
				(fileName.endsWith(".java") &&
				 ToolsUtil.isInsideQuotes(content, x))) {

				continue;
			}

			String methodCall = _getMethodCall(content, x);

			if (methodCall == null) {
				continue;
			}

			String newMethodCall = _getNewMethodCall(
				fileName, absolutePath, content, dependentContents,
				matcher.group(2), matcher.group(1), methodCall,
				getLineNumber(content, x));

			if (newMethodCall != null) {
				return StringUtil.replaceFirst(
					content, methodCall, newMethodCall, x);
			}
		}

		return content;
	}

	private String _getBeanTypeName(
		String content, List<String> dependentContents, String bean) {

		Matcher matcher = _castPattern.matcher(bean);

		if (matcher.matches()) {
			return matcher.group(1);
		}

		matcher = _methodCallPattern.matcher(bean);

		if (matcher.matches()) {
			return _getTypeName(
				content, dependentContents,
				"\\W(\\w+)(<[^;={}()]*>)?\\s+" + matcher.group(1) +
					"\\(\\s*\\)\\s*(throws [\\w.,\\s]+)?\\{");
		}

		if (!bean.matches("\\w+")) {
			return null;
		}

		return _getTypeName(
			content, dependentContents,
			"\\W(\\w+)(<[^;={}()]*>)?\\s+" + bean + "\\s*[;=),:]");
	}

	private Set<String> _getClassNames(String classHeader) {
		Set<String> classNames = new LinkedHashSet<>();

		while (true) {
			String newClassHeader = classHeader.replaceAll("<[^<>]*>", "");

			if (newClassHeader.equals(classHeader)) {
				break;
			}

			classHeader = newClassHeader;
		}

		for (String className : classHeader.split("[\\s,]+")) {
			if (className.isEmpty() || className.equals("extends") ||
				className.equals("implements")) {

				continue;
			}

			classNames.add(className);
		}

		return classNames;
	}

	private List<String> _getDependentContents(String fileName) {
		Map<String, String> contentsMap = getContentsMap();

		List<String> fileNames = new ArrayList<>();

		fileNames.add(fileName);

		return TransformUtil.transform(
			JSPSourceUtil.addIncludedAndReferencedFileNames(
				fileNames, new HashSet<String>(), contentsMap, ".*\\.jspf"),
			dependentFileName -> {
				dependentFileName = StringUtil.replace(
					dependentFileName, CharPool.BACK_SLASH, CharPool.SLASH);

				if (dependentFileName.equals(fileName)) {
					return null;
				}

				return contentsMap.get(dependentFileName);
			});
	}

	private String _getFullyQualifiedName(
		String className, String content, List<String> dependentContents) {

		Set<String> fullyQualifiedNames = new HashSet<>();

		Pattern pattern = Pattern.compile(
			"(\nimport |import=\")([\\w.]+\\." + className + ")[;\"]");

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			fullyQualifiedNames.add(matcher.group(2));
		}

		for (String dependentContent : dependentContents) {
			matcher = pattern.matcher(dependentContent);

			while (matcher.find()) {
				fullyQualifiedNames.add(matcher.group(2));
			}
		}

		if (fullyQualifiedNames.size() == 1) {
			for (String fullyQualifiedName : fullyQualifiedNames) {
				return fullyQualifiedName;
			}
		}

		return null;
	}

	private String[] _getGetter(String classContent, String methodName) {
		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"\n\\s*((public|protected|abstract|default|final|synchronized)",
				"\\s+)*([\\w.]+(<[^;={}()]*>)?(\\[\\])*)\\s+", methodName,
				"\\(\\s*\\)(\\s*throws\\s+([\\w.,\\s]+?))?\\s*[{;]"));

		Matcher matcher = pattern.matcher(classContent);

		while (matcher.find()) {
			String returnTypeName = matcher.group(3);

			if (returnTypeName.equals("new") ||
				returnTypeName.equals("return")) {

				continue;
			}

			return new String[] {methodName, returnTypeName, matcher.group(7)};
		}

		return null;
	}

	private String[] _getGetter(
			String absolutePath, String className, String content,
			String contentAbsolutePath, List<String> dependentContents,
			String getterSuffix, boolean checkIsGetter,
			Set<String> checkedClassNames)
		throws IOException {

		String classContent = null;
		String classAbsolutePath = null;

		Pattern pattern = Pattern.compile(
			"\\W(class|enum|interface|record)\\s+" + className + "\\W");

		Matcher matcher = pattern.matcher(content);

		if (matcher.find()) {
			classContent = content;
			classAbsolutePath = contentAbsolutePath;
		}
		else {
			String fullyQualifiedName = _getFullyQualifiedName(
				className, content, dependentContents);

			if ((fullyQualifiedName == null) &&
				contentAbsolutePath.endsWith(".java")) {

				File file = new File(
					contentAbsolutePath.substring(
						0, contentAbsolutePath.lastIndexOf(CharPool.SLASH) + 1),
					className + ".java");

				if (file.exists()) {
					fullyQualifiedName = className;
					classContent = FileUtil.read(file);
					classAbsolutePath = SourceUtil.getAbsolutePath(file);
				}
				else if (ArrayUtil.contains(
							new String[] {
								"Cloneable", "Comparable", "Iterable", "Object",
								"Runnable"
							},
							className)) {

					return _GETTER_NOT_FOUND;
				}
			}

			if (fullyQualifiedName == null) {
				return null;
			}

			if (fullyQualifiedName.startsWith("jakarta.") ||
				fullyQualifiedName.startsWith("java.") ||
				fullyQualifiedName.startsWith("javax.")) {

				return _GETTER_NOT_FOUND;
			}

			if (classContent == null) {
				File file = JavaSourceUtil.getJavaFile(
					fullyQualifiedName, SourceUtil.getRootDirName(absolutePath),
					getBundleSymbolicNamesMap(absolutePath));

				if (file == null) {
					return null;
				}

				classContent = FileUtil.read(file);
				classAbsolutePath = SourceUtil.getAbsolutePath(file);
			}
		}

		if (!checkedClassNames.add(classAbsolutePath + "#" + className)) {
			return _GETTER_NOT_FOUND;
		}

		if (checkIsGetter) {
			String[] getter = _getGetter(classContent, "is" + getterSuffix);

			if (getter != null) {
				return getter;
			}
		}

		String[] getter = _getGetter(classContent, "get" + getterSuffix);

		if (getter != null) {
			return getter;
		}

		pattern = Pattern.compile(
			"\\W(class|interface)\\s+" + className +
				"(<[^{]*?>)?\\s+((extends|implements)\\s[^{]*)\\{");

		matcher = pattern.matcher(classContent);

		if (!matcher.find()) {
			return _GETTER_NOT_FOUND;
		}

		boolean resolved = true;

		for (String superClassName : _getClassNames(matcher.group(3))) {
			if (superClassName.contains(".")) {
				resolved = false;

				continue;
			}

			String[] superClassGetter = _getGetter(
				absolutePath, superClassName, classContent, classAbsolutePath,
				new ArrayList<String>(), getterSuffix, checkIsGetter,
				checkedClassNames);

			if (superClassGetter == null) {
				resolved = false;
			}
			else if (superClassGetter.length > 0) {
				return superClassGetter;
			}
		}

		if (resolved) {
			return _GETTER_NOT_FOUND;
		}

		return null;
	}

	private String _getGetterSuffix(String param) {
		if ((param.length() > 1) && Character.isUpperCase(param.charAt(1))) {
			if (Character.isUpperCase(param.charAt(0))) {
				return param;
			}

			return null;
		}

		return StringUtil.upperCaseFirstLetter(param);
	}

	private String _getMethodCall(String content, int start) {
		int level = 0;

		for (int i = content.indexOf(CharPool.OPEN_PARENTHESIS, start);
			 i < content.length(); i++) {

			char c = content.charAt(i);

			if ((c == CharPool.APOSTROPHE) || (c == CharPool.QUOTE)) {
				i++;

				while ((i < content.length()) && (content.charAt(i) != c)) {
					if (content.charAt(i) == CharPool.BACK_SLASH) {
						i++;
					}

					i++;
				}
			}
			else if (c == CharPool.OPEN_PARENTHESIS) {
				level++;
			}
			else if (c == CharPool.CLOSE_PARENTHESIS) {
				level--;

				if (level == 0) {
					return content.substring(start, i + 1);
				}
			}
		}

		return null;
	}

	private String _getNewMethodCall(
			String fileName, String absolutePath, String content,
			List<String> dependentContents, String methodName,
			String fullMethodName, String methodCall, int lineNumber)
		throws IOException {

		List<String> parameters = JavaSourceUtil.splitParameters(
			JavaSourceUtil.getParameters(methodCall));

		if (parameters.size() < 3) {
			return null;
		}

		if (parameters.size() > 3) {
			String parameter = parameters.get(3);

			if (parameter.contains("->") || parameter.contains("::")) {
				return null;
			}

			Matcher matcher = _functionPattern.matcher(parameter);

			if (matcher.matches()) {
				return null;
			}
		}

		String param = parameters.get(2);

		if (!param.matches("\"\\w+\"")) {
			addMessage(
				fileName,
				StringBundler.concat(
					"Pass the getter to \"BeanParamUtil.", fullMethodName,
					"\" by hand when the parameter name is not a string ",
					"literal"),
				lineNumber);

			return null;
		}

		param = param.substring(1, param.length() - 1);

		String beanTypeName = _getBeanTypeName(
			content, dependentContents, parameters.get(0));

		if ((beanTypeName == null) || beanTypeName.equals("Object") ||
			beanTypeName.matches("[A-Z]")) {

			addMessage(
				fileName,
				StringBundler.concat(
					"Unable to resolve the type of \"", parameters.get(0),
					"\" in \"BeanParamUtil.", fullMethodName,
					"\", pass the getter by hand"),
				lineNumber);

			return null;
		}

		String getterSuffix = _getGetterSuffix(param);
		String[] getter = _GETTER_NOT_FOUND;

		if (getterSuffix != null) {
			getter = _getGetter(
				absolutePath, beanTypeName, content, absolutePath,
				dependentContents, getterSuffix,
				methodName.equals("getBoolean"), new HashSet<String>());
		}

		if (getter == null) {
			addMessage(
				fileName,
				StringBundler.concat(
					"Unable to find the source of \"", beanTypeName,
					"\" in \"BeanParamUtil.", fullMethodName,
					"\", pass the getter by hand"),
				lineNumber);

			return null;
		}

		if (getter.length == 0) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, "\" has no getter for \"", param,
					"\", so \"BeanParamUtil.", fullMethodName,
					"\" always returns the default value, use \"ParamUtil\" ",
					"instead"),
				lineNumber);

			return null;
		}

		if (!ArrayUtil.contains(_getReturnTypeNames(methodName), getter[1])) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, ".", getter[0], "\" returns \"",
					getter[1], "\", which does not fit \"BeanParamUtil.",
					fullMethodName, "\", pass a function by hand"),
				lineNumber);

			return null;
		}

		if (getter[2] != null) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, ".", getter[0], "\" throws \"",
					getter[2], "\", pass a function to \"BeanParamUtil.",
					fullMethodName, "\" by hand"),
				lineNumber);

			return null;
		}

		int index = methodCall.indexOf(CharPool.OPEN_PARENTHESIS);

		for (int i = 0; i < 3; i++) {
			String parameter = parameters.get(i);

			index = methodCall.indexOf(parameter, index);

			index += parameter.length();
		}

		return StringBundler.concat(
			"BeanParamUtil.", methodName,
			methodCall.substring(
				methodCall.indexOf(CharPool.OPEN_PARENTHESIS), index),
			", ", beanTypeName, "::", getter[0], methodCall.substring(index));
	}

	private String[] _getReturnTypeNames(String methodName) {
		if (methodName.equals("getBoolean")) {
			return new String[] {"boolean"};
		}

		if (methodName.equals("getDouble")) {
			return new String[] {
				"byte", "double", "float", "int", "long", "short"
			};
		}

		if (methodName.equals("getInteger")) {
			return new String[] {"byte", "int", "short"};
		}

		if (methodName.equals("getLong")) {
			return new String[] {"byte", "int", "long", "short"};
		}

		return new String[] {"String"};
	}

	private String _getTypeName(
		String content, List<String> dependentContents, String regex) {

		Pattern pattern = Pattern.compile(regex);

		Set<String> typeNames = _getTypeNames(content, pattern);

		if (typeNames.isEmpty()) {
			for (String dependentContent : dependentContents) {
				typeNames.addAll(_getTypeNames(dependentContent, pattern));
			}
		}

		if (typeNames.size() == 1) {
			for (String typeName : typeNames) {
				return typeName;
			}
		}

		return null;
	}

	private Set<String> _getTypeNames(String content, Pattern pattern) {
		Set<String> typeNames = new HashSet<>();

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String typeName = matcher.group(1);

			if (ArrayUtil.contains(
					new String[] {
						"case", "else", "new", "return", "throw", "yield"
					},
					typeName)) {

				continue;
			}

			typeNames.add(typeName);
		}

		return typeNames;
	}

	private static final String[] _GETTER_NOT_FOUND = new String[0];

	private static final Pattern _beanParamUtilPattern = Pattern.compile(
		"\\bBeanParamUtil\\.((get(?:Boolean|Double|Integer|Long|String))" +
			"(Silent)?)\\(");
	private static final Pattern _castPattern = Pattern.compile(
		"\\(\\s*(\\w+)\\s*\\)\\s*[\\w.]+(\\(.*\\))?", Pattern.DOTALL);
	private static final Pattern _functionPattern = Pattern.compile(
		"\\s*([\\w$]+\\s*\\.\\s*)*[\\w$]*[Ff]unction(\\s*\\(.*\\))?\\s*",
		Pattern.DOTALL);
	private static final Pattern _methodCallPattern = Pattern.compile(
		"(\\w+)\\(\\s*\\)");

}