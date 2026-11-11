/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.NaturalOrderStringComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.check.util.JSPSourceUtil;
import com.liferay.source.formatter.check.util.JavaSourceUtil;
import com.liferay.source.formatter.check.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class AUIValueFunctionCheck extends BaseJSPTermsCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		if (!content.contains("<aui:input") &&
			!content.contains("<aui:select")) {

			return content;
		}

		populateContentsMap(fileName, content);

		Matcher matcher = _tagPattern.matcher(content);

		while (matcher.find()) {
			String tag = _getTag(content, matcher.start());

			if (tag == null) {
				continue;
			}

			Map<String, String> attributesMap = _getAttributesMap(tag);

			String tagName = matcher.group(1);

			String type = attributesMap.get("type");

			if (tagName.equals("input") &&
				(Objects.equals(type, "hidden") ||
				 Objects.equals(type, "timeZone")) &&
				attributesMap.containsKey("valueFunction") &&
				_hasValue(attributesMap)) {

				addMessage(
					fileName,
					"A hidden or time zone \"aui:input\" with a \"value\" " +
						"does not read its \"valueFunction\", remove one of " +
							"them",
					getLineNumber(content, matcher.start()));

				continue;
			}

			String propertyName = _getPropertyName(attributesMap, tagName);

			if ((propertyName == null) ||
				(tagName.equals("input") && !_isBeanReadType(type) &&
				 _hasValue(attributesMap))) {

				continue;
			}

			String valueFunction = _getValueFunction(
				fileName, absolutePath, content, attributesMap, matcher.start(),
				propertyName, tagName);

			if (valueFunction != null) {
				return StringUtil.replaceFirst(
					content, tag, _addValueFunction(tag, valueFunction),
					matcher.start());
			}
		}

		return content;
	}

	private String _addValueFunction(String tag, String valueFunction) {
		String attribute = StringBundler.concat(
			"valueFunction=\"<%= ", valueFunction, " %>\"");

		NaturalOrderStringComparator naturalOrderStringComparator =
			new NaturalOrderStringComparator();

		int x = -1;
		int y = -1;

		Matcher matcher = _attributePattern.matcher(tag);

		while (matcher.find()) {
			x = matcher.start(1);

			int value = naturalOrderStringComparator.compare(
				matcher.group(1), "valueFunction");

			if (value > 0) {
				return StringBundler.concat(
					tag.substring(0, x), attribute, _getWhitespace(tag, x),
					tag.substring(x));
			}

			y = matcher.end();
		}

		return StringBundler.concat(
			tag.substring(0, y), _getWhitespace(tag, x), attribute,
			tag.substring(y));
	}

	private String _getAttributeValue(
		Map<String, String> attributesMap, String name) {

		String value = attributesMap.get(name);

		if (value == null) {
			return null;
		}

		Matcher matcher = _expressionPattern.matcher(value);

		if (!matcher.matches()) {
			return value;
		}

		value = matcher.group(1);

		if (value.equals("null")) {
			return null;
		}

		return value;
	}

	private Map<String, String> _getAttributesMap(String tag) {
		Map<String, String> attributesMap = new HashMap<>();

		Matcher matcher = _attributePattern.matcher(tag);

		while (matcher.find()) {
			String value = matcher.group(2);

			attributesMap.put(
				matcher.group(1), value.substring(1, value.length() - 1));
		}

		return attributesMap;
	}

	private String _getBeanTypeName(
			String absolutePath, String bean, String content,
			List<String> dependentContents)
		throws IOException {

		bean = bean.trim();

		int x = _getTopLevelIndex(bean, CharPool.QUESTION, 0);

		if (x != -1) {
			int y = _getTopLevelIndex(bean, CharPool.COLON, x);

			if (y == -1) {
				return null;
			}

			String beanTypeName = _getBeanTypeName(
				absolutePath, bean.substring(x + 1, y), content,
				dependentContents);

			if ((beanTypeName != null) &&
				beanTypeName.equals(
					_getBeanTypeName(
						absolutePath, bean.substring(y + 1), content,
						dependentContents))) {

				return beanTypeName;
			}

			return null;
		}

		Matcher matcher = _castPattern.matcher(bean);

		if (matcher.matches()) {
			return matcher.group(1);
		}

		if (bean.matches("\\w+")) {
			return _getVariableTypeName(bean, content, dependentContents);
		}

		matcher = _methodCallPattern.matcher(bean);

		if (!matcher.matches()) {
			return null;
		}

		String variableTypeName = _getVariableTypeName(
			matcher.group(1), content, dependentContents);

		if (variableTypeName == null) {
			return null;
		}

		List<String[]> methods = new ArrayList<>();

		_populateMethods(
			absolutePath, variableTypeName, content, absolutePath,
			dependentContents, matcher.group(2), methods,
			new HashSet<String>());

		Set<String> returnTypeNames = new HashSet<>();

		for (String[] method : methods) {
			returnTypeNames.add(method[0]);
		}

		if (returnTypeNames.size() == 1) {
			for (String returnTypeName : returnTypeNames) {
				return returnTypeName;
			}
		}

		return null;
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

	private String[] _getClassSource(
			String absolutePath, String className, String content,
			String contentAbsolutePath, List<String> dependentContents)
		throws IOException {

		Pattern pattern = Pattern.compile(
			"\\W(class|enum|interface|record)\\s+" + className + "\\W");

		Matcher matcher = pattern.matcher(content);

		if (matcher.find()) {
			return new String[] {content, contentAbsolutePath};
		}

		String fullyQualifiedName = className;

		if (!className.contains(StringPool.PERIOD)) {
			fullyQualifiedName = _getFullyQualifiedName(
				absolutePath, className, content, dependentContents);
		}

		if ((fullyQualifiedName == null) &&
			contentAbsolutePath.endsWith(".java")) {

			File file = new File(
				contentAbsolutePath.substring(
					0, contentAbsolutePath.lastIndexOf(CharPool.SLASH) + 1),
				className + ".java");

			if (file.exists()) {
				return new String[] {
					FileUtil.read(file), SourceUtil.getAbsolutePath(file)
				};
			}

			if (ArrayUtil.contains(_JAVA_LANG_CLASS_NAMES, className)) {
				return _CLASS_SOURCE_NOT_NEEDED;
			}
		}

		if (fullyQualifiedName == null) {
			return null;
		}

		if (fullyQualifiedName.startsWith("jakarta.") ||
			fullyQualifiedName.startsWith("java.") ||
			fullyQualifiedName.startsWith("javax.")) {

			return _CLASS_SOURCE_NOT_NEEDED;
		}

		File file = JavaSourceUtil.getJavaFile(
			fullyQualifiedName, SourceUtil.getRootDirName(absolutePath),
			getBundleSymbolicNamesMap(absolutePath));

		if (file == null) {
			return null;
		}

		return new String[] {
			FileUtil.read(file), SourceUtil.getAbsolutePath(file)
		};
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
		String absolutePath, String className, String content,
		List<String> dependentContents) {

		List<String> contents = new ArrayList<>(dependentContents);

		contents.add(content);

		Set<String> fullyQualifiedNames = new HashSet<>();

		Pattern pattern = Pattern.compile(
			"(\nimport |import=\")([\\w.]+\\." + className + ")[;\"]");

		for (String curContent : contents) {
			Matcher matcher = pattern.matcher(curContent);

			while (matcher.find()) {
				fullyQualifiedNames.add(matcher.group(2));
			}
		}

		if (fullyQualifiedNames.isEmpty()) {
			for (String curContent : contents) {
				Matcher matcher = _wildcardImportPattern.matcher(curContent);

				while (matcher.find()) {
					String fullyQualifiedName =
						matcher.group(2) + StringPool.PERIOD + className;

					File file = JavaSourceUtil.getJavaFile(
						fullyQualifiedName,
						SourceUtil.getRootDirName(absolutePath),
						getBundleSymbolicNamesMap(absolutePath));

					if (file == null) {
						continue;
					}

					fullyQualifiedNames.add(fullyQualifiedName);
				}
			}
		}

		if (fullyQualifiedNames.size() == 1) {
			for (String fullyQualifiedName : fullyQualifiedNames) {
				return fullyQualifiedName;
			}
		}

		return null;
	}

	private String _getIncludeFileName(
		String fileName, String content, int pos) {

		int x = content.indexOf(CharPool.QUOTE, pos);

		int y = content.indexOf(CharPool.QUOTE, x + 1);

		if ((x == -1) || (y == -1)) {
			return null;
		}

		String includeFileName = content.substring(x + 1, y);

		if (!includeFileName.startsWith(StringPool.SLASH)) {
			includeFileName = StringPool.SLASH + includeFileName;
		}

		return JSPSourceUtil.buildFullPathIncludeFileName(
			fileName, includeFileName, getContentsMap());
	}

	private synchronized Map<String, List<Map.Entry<String, Integer>>>
		_getIncludersMap() {

		if (_includersMap != null) {
			return _includersMap;
		}

		Map<String, List<Map.Entry<String, Integer>>> includersMap =
			new HashMap<>();

		Map<String, String> contentsMap = getContentsMap();

		for (Map.Entry<String, String> entry : contentsMap.entrySet()) {
			String content = entry.getValue();

			int x = -1;

			while (true) {
				x = content.indexOf(_INCLUDE_DIRECTIVE, x + 1);

				if (x == -1) {
					break;
				}

				String includeFileName = _getIncludeFileName(
					entry.getKey(), content, x);

				if (includeFileName == null) {
					continue;
				}

				List<Map.Entry<String, Integer>> includers =
					includersMap.computeIfAbsent(
						includeFileName, key -> new ArrayList<>());

				includers.add(Map.entry(entry.getKey(), x));
			}
		}

		_includersMap = includersMap;

		return _includersMap;
	}

	private String _getLambdaParameterName(
		String content, List<String> dependentContents, String typeName) {

		typeName = typeName.substring(
			typeName.lastIndexOf(CharPool.PERIOD) + 1);

		int x = 0;

		while ((x < typeName.length()) &&
			   Character.isUpperCase(typeName.charAt(x))) {

			x++;
		}

		if ((x > 1) && (x < typeName.length())) {
			x--;
		}

		String[] lambdaParameterNames = {
			StringUtil.toLowerCase(typeName.substring(0, x)) +
				typeName.substring(x),
			"cur" + typeName
		};

		for (String lambdaParameterName : lambdaParameterNames) {
			Pattern pattern = Pattern.compile(
				"\\W" + lambdaParameterName + "\\W");

			Matcher matcher = pattern.matcher(content);

			if (matcher.find()) {
				continue;
			}

			boolean used = false;

			for (String dependentContent : dependentContents) {
				matcher = pattern.matcher(dependentContent);

				if (matcher.find()) {
					used = true;

					break;
				}
			}

			if (!used) {
				return lambdaParameterName;
			}
		}

		return null;
	}

	private List<String[]> _getModelContexts(
		String fileName, String content, int pos, boolean checkIncluders) {

		Map<String, String> contentsMap = getContentsMap();

		int x = pos;

		while (x > 0) {
			int includePos = content.lastIndexOf(_INCLUDE_DIRECTIVE, x - 1);
			int modelContextPos = content.lastIndexOf(
				"<aui:model-context", x - 1);

			if ((includePos == -1) && (modelContextPos == -1)) {
				break;
			}

			if (modelContextPos > includePos) {
				String tag = _getTag(content, modelContextPos);

				if (tag == null) {
					return Collections.emptyList();
				}

				Map<String, String> attributesMap = _getAttributesMap(tag);

				String model = _getAttributeValue(attributesMap, "model");

				if (model == null) {
					return Collections.singletonList(
						new String[] {null, null, fileName});
				}

				return Collections.singletonList(
					new String[] {
						_getAttributeValue(attributesMap, "bean"),
						_getModelTypeName(model), fileName
					});
			}

			x = includePos;

			String includeFileName = _getIncludeFileName(
				fileName, content, includePos);

			if (includeFileName == null) {
				continue;
			}

			String includeContent = contentsMap.get(includeFileName);

			if (includeContent == null) {
				continue;
			}

			List<String[]> modelContexts = _getModelContexts(
				includeFileName, includeContent, includeContent.length(),
				false);

			if (!modelContexts.isEmpty()) {
				return modelContexts;
			}
		}

		if (!checkIncluders) {
			return Collections.emptyList();
		}

		Map<String, List<Map.Entry<String, Integer>>> includersMap =
			_getIncludersMap();

		List<Map.Entry<String, Integer>> includers = includersMap.get(fileName);

		if (includers == null) {
			return Collections.emptyList();
		}

		List<String[]> modelContexts = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : includers) {
			modelContexts.addAll(
				_getModelContexts(
					entry.getKey(), contentsMap.get(entry.getKey()),
					entry.getValue(), true));
		}

		return modelContexts;
	}

	private String _getModelTypeName(String model) {
		if (model == null) {
			return null;
		}

		Matcher matcher = _modelPattern.matcher(model);

		if (matcher.matches()) {
			return matcher.group(1);
		}

		return null;
	}

	private String _getPropertyName(
		Map<String, String> attributesMap, String tagName) {

		if (attributesMap.containsKey("valueFunction")) {
			return null;
		}

		String propertyName = null;

		if (tagName.equals("select")) {
			if (_getAttributeValue(attributesMap, "listType") != null) {
				propertyName = attributesMap.get("listTypeFieldName");

				if (propertyName == null) {
					return "listTypeId";
				}
			}
			else {
				propertyName = attributesMap.get("name");
			}
		}
		else {
			String type = attributesMap.get("type");

			if (type == null) {
				return null;
			}

			if (_isBeanReadType(type)) {
				String ignoreRequestValue = _getAttributeValue(
					attributesMap, "ignoreRequestValue");

				if ((ignoreRequestValue != null) &&
					ignoreRequestValue.equals("true")) {

					return null;
				}
			}
			else if (_isType(type, "timeZone")) {
				propertyName = attributesMap.get("field");
			}
			else if (!_isType(type, "hidden")) {
				return null;
			}

			if (propertyName == null) {
				propertyName = attributesMap.get("name");
			}
		}

		if ((propertyName == null) || !propertyName.matches("\\w+")) {
			return null;
		}

		return propertyName;
	}

	private String _getTag(String content, int start) {
		char quote = CharPool.SPACE;

		for (int i = start; i < content.length(); i++) {
			char c = content.charAt(i);

			if (quote != CharPool.SPACE) {
				if (c == CharPool.BACK_SLASH) {
					i++;
				}
				else if (c == quote) {
					quote = CharPool.SPACE;
				}
			}
			else if ((c == CharPool.APOSTROPHE) || (c == CharPool.QUOTE)) {
				quote = c;
			}
			else if (c == CharPool.GREATER_THAN) {
				return content.substring(start, i + 1);
			}
		}

		return null;
	}

	private int _getTopLevelIndex(String s, char delimiter, int start) {
		int level = 0;

		for (int i = start; i < s.length(); i++) {
			char c = s.charAt(i);

			if ((c == CharPool.APOSTROPHE) || (c == CharPool.QUOTE)) {
				i++;

				while ((i < s.length()) && (s.charAt(i) != c)) {
					if (s.charAt(i) == CharPool.BACK_SLASH) {
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
			}
			else if ((c == delimiter) && (level == 0)) {
				return i;
			}
		}

		return -1;
	}

	private String _getValueFunction(
			String fileName, String absolutePath, String content,
			List<String> dependentContents, String bean, boolean longFunction,
			String modelTypeName, String propertyName, String tagName,
			int lineNumber)
		throws IOException {

		String beanTypeName = _getBeanTypeName(
			absolutePath, bean, content, dependentContents);

		if (beanTypeName == null) {
			beanTypeName = modelTypeName;
		}
		else if (_defineObjectsTypeNames.containsValue(beanTypeName)) {
			beanTypeName = beanTypeName.substring(
				beanTypeName.lastIndexOf(CharPool.PERIOD) + 1);
		}

		if ((beanTypeName == null) || beanTypeName.equals("Object")) {
			addMessage(
				fileName,
				StringBundler.concat(
					"Unable to resolve the type of \"", bean,
					"\", set \"valueFunction\" on the \"aui:", tagName,
					"\" for \"", propertyName, "\" by hand"),
				lineNumber);

			return null;
		}

		String methodName =
			"get" + StringUtil.upperCaseFirstLetter(propertyName);

		List<String[]> methods = new ArrayList<>();

		boolean resolved = _populateMethods(
			absolutePath, beanTypeName, content, absolutePath,
			dependentContents, methodName, methods, new HashSet<String>());

		String[] getter = null;
		boolean overloaded = false;

		for (String[] method : methods) {
			if (!method[1].isEmpty()) {
				overloaded = true;
			}
			else if (getter == null) {
				getter = method;
			}
		}

		if (getter == null) {
			if (!resolved) {
				addMessage(
					fileName,
					StringBundler.concat(
						"Unable to find the source of \"", beanTypeName,
						"\", set \"valueFunction\" on the \"aui:", tagName,
						"\" for \"", propertyName, "\" by hand"),
					lineNumber);
			}

			if (resolved && longFunction) {
				addMessage(
					fileName,
					StringBundler.concat(
						"\"", beanTypeName, "\" has no \"", methodName,
						"\" for the list type of the \"aui:", tagName,
						"\", set \"listTypeFieldName\" to the list type ",
						"property of the bean"),
					lineNumber);
			}

			return null;
		}

		if (longFunction && !ArrayUtil.contains(_LONG_TYPE_NAMES, getter[0])) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, ".", methodName, "\" returns \"",
					getter[0],
					"\", which does not fit the \"valueFunction\" of \"aui:",
					tagName, "\", set it by hand"),
				lineNumber);

			return null;
		}

		if (getter[2] != null) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, ".", methodName, "\" throws \"",
					getter[2], "\", set \"valueFunction\" on the \"aui:",
					tagName, "\" by hand"),
				lineNumber);

			return null;
		}

		if (!overloaded) {
			return StringBundler.concat(beanTypeName, "::", methodName);
		}

		String lambdaParameterName = _getLambdaParameterName(
			content, dependentContents, beanTypeName);

		if (lambdaParameterName == null) {
			addMessage(
				fileName,
				StringBundler.concat(
					"\"", beanTypeName, ".", methodName,
					"\" is overloaded, set \"valueFunction\" on the \"aui:",
					tagName, "\" to a lambda by hand"),
				lineNumber);

			return null;
		}

		return StringBundler.concat(
			"(", beanTypeName, " ", lambdaParameterName, ") -> ",
			lambdaParameterName, ".", methodName, "()");
	}

	private String _getValueFunction(
			String fileName, String absolutePath, String content,
			Map<String, String> attributesMap, int pos, String propertyName,
			String tagName)
		throws IOException {

		List<String[]> modelContexts = null;

		String bean = _getAttributeValue(attributesMap, "bean");

		if (bean == null) {
			modelContexts = _getModelContexts(fileName, content, pos, true);
		}
		else {
			modelContexts = Collections.singletonList(
				new String[] {
					bean,
					_getModelTypeName(
						_getAttributeValue(attributesMap, "model")),
					fileName
				});
		}

		boolean longFunction = false;

		if (tagName.equals("select") &&
			(_getAttributeValue(attributesMap, "listType") != null)) {

			longFunction = true;
		}

		Set<String> valueFunctions = new HashSet<>();

		for (String[] modelContext : modelContexts) {
			if (modelContext[0] == null) {
				continue;
			}

			String modelContextContent = content;

			if (!fileName.equals(modelContext[2])) {
				Map<String, String> contentsMap = getContentsMap();

				modelContextContent = contentsMap.get(modelContext[2]);
			}

			valueFunctions.add(
				_getValueFunction(
					fileName, absolutePath, modelContextContent,
					_getDependentContents(modelContext[2]), modelContext[0],
					longFunction, modelContext[1], propertyName, tagName,
					getLineNumber(content, pos)));
		}

		if (valueFunctions.size() == 1) {
			for (String valueFunction : valueFunctions) {
				return valueFunction;
			}
		}

		if (valueFunctions.size() > 1) {
			addMessage(
				fileName,
				StringBundler.concat(
					"The \"aui:", tagName, "\" for \"", propertyName,
					"\" reads beans of different types, set \"valueFunction\" ",
					"by hand"),
				getLineNumber(content, pos));
		}

		return null;
	}

	private String _getVariableTypeName(
		String variableName, String content, List<String> dependentContents) {

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"\\W([\\w.]+)(<[^;={}()]*>)?\\s+", variableName,
				"\\s*([;),:]|=(?![\"']))"));

		Set<String> typeNames = _getVariableTypeNames(content, pattern);

		if (typeNames.isEmpty()) {
			for (String dependentContent : dependentContents) {
				typeNames.addAll(
					_getVariableTypeNames(dependentContent, pattern));
			}
		}

		if (typeNames.size() == 1) {
			for (String typeName : typeNames) {
				return typeName;
			}
		}

		if (typeNames.isEmpty()) {
			Pattern modelVarPattern = Pattern.compile(
				"className=\"([\\w.]+)\".{0,300}?\\smodelVar=\"" +
					variableName + "\"",
				Pattern.DOTALL);

			Matcher matcher = modelVarPattern.matcher(content);

			if (matcher.find()) {
				return matcher.group(1);
			}

			return _defineObjectsTypeNames.get(variableName);
		}

		return null;
	}

	private Set<String> _getVariableTypeNames(String content, Pattern pattern) {
		Set<String> typeNames = new HashSet<>();

		Matcher matcher = pattern.matcher(content);

		while (matcher.find()) {
			String typeName = matcher.group(1);

			if (ArrayUtil.contains(_KEYWORDS, typeName)) {
				continue;
			}

			typeNames.add(typeName);
		}

		return typeNames;
	}

	private String _getWhitespace(String s, int end) {
		int start = end;

		while ((start > 0) && Character.isWhitespace(s.charAt(start - 1))) {
			start--;
		}

		return s.substring(start, end);
	}

	private boolean _hasValue(Map<String, String> attributesMap) {
		String value = attributesMap.get("value");

		if ((value == null) ||
			(value.isEmpty() &&
			 _isType(attributesMap.get("type"), "timeZone"))) {

			return false;
		}

		return true;
	}

	private boolean _isBeanReadType(String type) {
		for (String beanReadType : _BEAN_READ_TYPES) {
			if (_isType(type, beanReadType)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isType(String type, String name) {
		if (type.equals(name) ||
			(type.startsWith("<%") &&
			 type.contains(StringPool.QUOTE + name + StringPool.QUOTE))) {

			return true;
		}

		return false;
	}

	private boolean _populateMethods(
			String absolutePath, String className, String content,
			String contentAbsolutePath, List<String> dependentContents,
			String methodName, List<String[]> methods,
			Set<String> checkedClassNames)
		throws IOException {

		String[] classSource = _getClassSource(
			absolutePath, className, content, contentAbsolutePath,
			dependentContents);

		if (classSource == null) {
			return false;
		}

		className = className.substring(
			className.lastIndexOf(CharPool.PERIOD) + 1);

		if ((classSource.length == 0) ||
			!checkedClassNames.add(classSource[1] + "#" + className)) {

			return true;
		}

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"\n\\s*((public|abstract|default|final|static|synchronized)",
				"\\s+)*(<[^;={}()]*>\\s+)?([\\w.]+(<[^;={}()]*>)?(\\[\\])*)",
				"\\s+", methodName, "\\(([^)]*)\\)(\\s*throws\\s+",
				"([\\w.,\\s]+?))?\\s*[{;]"));

		Matcher matcher = pattern.matcher(classSource[0]);

		while (matcher.find()) {
			String returnTypeName = matcher.group(4);

			if (ArrayUtil.contains(_KEYWORDS, returnTypeName) ||
				returnTypeName.equals("void")) {

				continue;
			}

			String parameters = matcher.group(7);

			methods.add(
				new String[] {
					returnTypeName, parameters.trim(), matcher.group(9)
				});
		}

		pattern = Pattern.compile(
			"\\W(class|interface)\\s+" + className +
				"(<[^{]*?>)?\\s+((extends|implements)\\s[^{]*)\\{");

		matcher = pattern.matcher(classSource[0]);

		if (!matcher.find()) {
			return true;
		}

		boolean resolved = true;

		for (String superClassName : _getClassNames(matcher.group(3))) {
			if (_populateMethods(
					absolutePath, superClassName, classSource[0],
					classSource[1], new ArrayList<String>(), methodName,
					methods, checkedClassNames)) {

				continue;
			}

			resolved = false;
		}

		return resolved;
	}

	private static final String[] _BEAN_READ_TYPES = {
		"color", "email", "number", "range", "tel", "text", "textarea"
	};

	private static final String[] _CLASS_SOURCE_NOT_NEEDED = new String[0];

	private static final String _INCLUDE_DIRECTIVE = "<%@ include file=";

	private static final String[] _JAVA_LANG_CLASS_NAMES = {
		"Cloneable", "Comparable", "Iterable", "Object", "Runnable"
	};

	private static final String[] _KEYWORDS = {
		"case", "const", "else", "let", "new", "return", "throw", "var", "yield"
	};

	private static final String[] _LONG_TYPE_NAMES = {
		"byte", "int", "long", "short"
	};

	private static final Pattern _attributePattern = Pattern.compile(
		"\\s([\\w:-]+)=(\"(?:[^\"\\\\]|\\\\.)*\"|'(?:[^'\\\\]|\\\\.)*')",
		Pattern.DOTALL);
	private static final Pattern _castPattern = Pattern.compile(
		"\\(\\s*([\\w.]+)\\s*\\)\\s*[\\w.]+(\\(.*\\))?", Pattern.DOTALL);
	private static final Map<String, String> _defineObjectsTypeNames =
		HashMapBuilder.put(
			"company", "com.liferay.portal.kernel.model.Company"
		).put(
			"contact", "com.liferay.portal.kernel.model.Contact"
		).put(
			"group", "com.liferay.portal.kernel.model.Group"
		).put(
			"groupId", "long"
		).put(
			"layout", "com.liferay.portal.kernel.model.Layout"
		).put(
			"liveGroup", "com.liferay.portal.kernel.model.Group"
		).put(
			"liveGroupId", "long"
		).put(
			"plid", "long"
		).put(
			"portletGroupId", "long"
		).put(
			"privateLayout", "boolean"
		).put(
			"realUser", "com.liferay.portal.kernel.model.User"
		).put(
			"scopeGroup", "com.liferay.portal.kernel.model.Group"
		).put(
			"scopeGroupId", "long"
		).put(
			"stagingGroup", "com.liferay.portal.kernel.model.Group"
		).put(
			"stagingGroupId", "long"
		).put(
			"themeDisplay", "com.liferay.portal.kernel.theme.ThemeDisplay"
		).put(
			"user", "com.liferay.portal.kernel.model.User"
		).build();
	private static final Pattern _expressionPattern = Pattern.compile(
		"<%=\\s*(.*?)\\s*%>", Pattern.DOTALL);
	private static final Pattern _methodCallPattern = Pattern.compile(
		"(\\w+)\\.(\\w+)\\(.*\\)", Pattern.DOTALL);
	private static final Pattern _modelPattern = Pattern.compile(
		"([\\w.]+)\\.class");
	private static final Pattern _tagPattern = Pattern.compile(
		"<aui:(input|select)\\s");
	private static final Pattern _wildcardImportPattern = Pattern.compile(
		"(\nimport |import=\")([\\w.]+)\\.\\*[;\"]");

	private Map<String, List<Map.Entry<String, Integer>>> _includersMap;

}