/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.source.formatter.check.util.SourceUtil;
import com.liferay.source.formatter.parser.JavaClass;
import com.liferay.source.formatter.parser.JavaTerm;
import com.liferay.source.formatter.util.FileUtil;
import com.liferay.source.formatter.util.SourceFormatterUtil;

import java.io.File;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shuyang Zhou
 */
public class JavaTransactionTestUtilCheck extends BaseJavaTermCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
		String fileName, String absolutePath, JavaTerm javaTerm,
		String fileContent) {

		if ((javaTerm.getParentJavaClass() != null) ||
			absolutePath.endsWith("/TransactionTestUtil.java")) {

			return javaTerm.getContent();
		}

		String className = javaTerm.getName();

		if (className.endsWith("TestUtil")) {
			_checkPersistenceLookups(fileName, (JavaClass)javaTerm);
		}

		if (!fileContent.contains("TransactionTestUtil")) {
			return javaTerm.getContent();
		}

		Matcher matcher = _testFilePattern.matcher(absolutePath);

		if (!matcher.find()) {
			return javaTerm.getContent();
		}

		File portalDir = getPortalDir();

		if (portalDir == null) {
			return javaTerm.getContent();
		}

		if (_isTestUtilFile(absolutePath)) {
			_checkTestUtilMethods(
				fileName, absolutePath, (JavaClass)javaTerm,
				SourceUtil.getAbsolutePath(portalDir));
		}
		else {
			_checkRepeatedCalls(
				fileName, absolutePath, fileContent,
				SourceUtil.getAbsolutePath(portalDir));
		}

		return javaTerm.getContent();
	}

	@Override
	protected String[] getCheckableJavaTermNames() {
		return new String[] {JAVA_CLASS};
	}

	private void _checkPersistenceLookups(
		String fileName, JavaClass javaClass) {

		for (JavaTerm childJavaTerm : javaClass.getChildJavaTerms()) {
			if (!childJavaTerm.isJavaMethod()) {
				continue;
			}

			Matcher matcher = _getPersistencePattern.matcher(
				_stripCommentsAndLiterals(childJavaTerm.getContent()));

			while (matcher.find()) {
				String testUtilClassName = matcher.group(1) + "TestUtil";

				if (testUtilClassName.equals(javaClass.getName())) {
					continue;
				}

				addMessage(
					fileName,
					StringBundler.concat(
						"Test util method \"", childJavaTerm.getName(),
						"\" reads \"", matcher.group(1),
						"Util.getPersistence()\", move it into ",
						testUtilClassName),
					childJavaTerm.getLineNumber(matcher.start()));
			}
		}
	}

	private void _checkRepeatedCalls(
		String fileName, String absolutePath, String content,
		String portalDirName) {

		Set<String> calls = new HashSet<>();

		Matcher matcher = _invokePattern.matcher(
			_stripCommentsAndLiterals(content));

		while (matcher.find()) {
			String call = _getCall(matcher);

			if (!calls.add(call)) {
				continue;
			}

			Map<String, Set<String>> testClassFileNamesMap =
				_getTestClassFileNamesMap(portalDirName);

			List<String> classNames = TransformUtil.transform(
				testClassFileNamesMap.getOrDefault(
					call, Collections.emptySet()),
				testClassFileName -> {
					if (testClassFileName.equals(absolutePath)) {
						return null;
					}

					return _getClassName(testClassFileName);
				});

			if (classNames.isEmpty()) {
				continue;
			}

			addMessage(
				fileName,
				StringBundler.concat(
					"\"TransactionTestUtil\" call \"", call,
					"\" is repeated in ",
					StringUtil.merge(classNames, StringPool.COMMA_AND_SPACE),
					", move it into a test util method that they share"),
				getLineNumber(content, matcher.start()));
		}
	}

	private void _checkTestUtilMethods(
		String fileName, String absolutePath, JavaClass javaClass,
		String portalDirName) {

		String strippedClassContent = _stripCommentsAndLiterals(
			javaClass.getContent());

		for (JavaTerm childJavaTerm : javaClass.getChildJavaTerms()) {
			if (!childJavaTerm.isJavaMethod()) {
				continue;
			}

			Matcher matcher = _invokePattern.matcher(
				_stripCommentsAndLiterals(childJavaTerm.getContent()));

			if (!matcher.find()) {
				continue;
			}

			String methodName = childJavaTerm.getName();

			Pattern pattern = Pattern.compile(
				"(?<![\\w$.])" + methodName + "\\s*\\(");

			Matcher methodNameMatcher = pattern.matcher(strippedClassContent);

			if (methodNameMatcher.find() && methodNameMatcher.find()) {
				continue;
			}

			List<String> callerClassNames = _getCallerClassNames(
				absolutePath, javaClass.getName(), methodName,
				javaClass.getPackageName(), portalDirName);

			if (callerClassNames.size() > 1) {
				continue;
			}

			String usage = "no class calls it, remove it";

			if (!callerClassNames.isEmpty()) {
				usage = StringBundler.concat(
					"only ", callerClassNames.get(0),
					" calls it, move it into that class");
			}

			do {
				addMessage(
					fileName,
					StringBundler.concat(
						"Test util method \"", methodName,
						"\" wraps \"TransactionTestUtil\" call \"",
						_getCall(matcher), "\" and ", usage),
					childJavaTerm.getLineNumber(matcher.start()));
			}
			while (matcher.find());
		}
	}

	private String _getCall(Matcher matcher) {
		return StringBundler.concat(
			matcher.group(1), StringPool.PERIOD, matcher.group(3));
	}

	private List<String> _getCallerClassNames(
		String absolutePath, String className, String methodName,
		String packageName, String portalDirName) {

		Pattern referencePattern = Pattern.compile(
			StringBundler.concat(
				"(?<![\\w$.])", Pattern.quote(packageName + "." + className),
				"(?![\\w$])|^package ", Pattern.quote(packageName), ";"),
			Pattern.MULTILINE);

		Pattern pattern = Pattern.compile(
			StringBundler.concat(
				"(?<![\\w$])", className, "\\s*(\\.\\s*", methodName,
				"\\s*\\(|::\\s*", methodName, "(?![\\w$]))"));

		return TransformUtil.transform(
			_referenceFileNamesMap.computeIfAbsent(
				className,
				referenceClassName -> _getFileNames(
					referenceClassName, portalDirName)),
			referenceFileName -> {
				if (referenceFileName.equals(absolutePath)) {
					return null;
				}

				String content = FileUtil.read(new File(referenceFileName));

				Matcher referenceMatcher = referencePattern.matcher(content);

				if (!referenceMatcher.find()) {
					return null;
				}

				Matcher matcher = pattern.matcher(content);

				if (!matcher.find()) {
					return null;
				}

				return _getClassName(referenceFileName);
			});
	}

	private String _getClassName(String fileName) {
		return fileName.substring(
			fileName.lastIndexOf(CharPool.SLASH) + 1, fileName.length() - 5);
	}

	private int _getCommentOrLiteralEnd(String content, int x) {
		if (content.startsWith("//", x)) {
			return _getEnd(content, StringPool.NEW_LINE, x);
		}

		if (content.startsWith("/*", x)) {
			return _getEnd(content, "*/", x + 2);
		}

		if (content.startsWith("\"\"\"", x)) {
			return _getEnd(content, "\"\"\"", x + 3);
		}

		char c = content.charAt(x);

		if ((c != CharPool.APOSTROPHE) && (c != CharPool.QUOTE)) {
			return x;
		}

		for (int i = x + 1; i < content.length(); i++) {
			if (content.charAt(i) == CharPool.BACK_SLASH) {
				i++;
			}
			else if ((content.charAt(i) == c) ||
					 (content.charAt(i) == CharPool.NEW_LINE)) {

				return i + 1;
			}
		}

		return content.length();
	}

	private int _getEnd(String content, String delimiter, int x) {
		int y = content.indexOf(delimiter, x);

		if (y == -1) {
			return content.length();
		}

		return y + delimiter.length();
	}

	private List<String> _getFileNames(String word, String portalDirName) {
		return SourceFormatterUtil.matchFileContentsForFileNames(
			Arrays.asList("-F", "-w", word), portalDirName,
			new String[] {"**/*.java"});
	}

	private synchronized Map<String, Set<String>> _getTestClassFileNamesMap(
		String portalDirName) {

		if (_testClassFileNamesMap != null) {
			return _testClassFileNamesMap;
		}

		_testClassFileNamesMap = new HashMap<>();

		for (String fileName :
				_getFileNames("TransactionTestUtil", portalDirName)) {

			if (fileName.endsWith("/TransactionTestUtil.java") ||
				_isTestUtilFile(fileName)) {

				continue;
			}

			Matcher matcher = _invokePattern.matcher(
				_stripCommentsAndLiterals(FileUtil.read(new File(fileName))));

			while (matcher.find()) {
				Set<String> fileNames = _testClassFileNamesMap.computeIfAbsent(
					_getCall(matcher), call -> new TreeSet<>());

				fileNames.add(fileName);
			}
		}

		return _testClassFileNamesMap;
	}

	private boolean _isTestUtilFile(String absolutePath) {
		if (absolutePath.contains("-test-util/") ||
			absolutePath.contains("/portal-test/") ||
			absolutePath.endsWith("TestUtil.java")) {

			return true;
		}

		return false;
	}

	private String _stripCommentsAndLiterals(String content) {
		char[] chars = content.toCharArray();

		int x = 0;

		while (x < chars.length) {
			int y = _getCommentOrLiteralEnd(content, x);

			if (y == x) {
				x++;

				continue;
			}

			for (int i = x; i < y; i++) {
				if (chars[i] == CharPool.NEW_LINE) {
					continue;
				}

				chars[i] = CharPool.SPACE;
			}

			x = y;
		}

		return new String(chars);
	}

	private static final Pattern _getPersistencePattern = Pattern.compile(
		"(?<![\\w$])([A-Za-z_$][\\w$]*)Util\\s*\\.\\s*getPersistence\\s*\\(" +
			"\\s*\\)");
	private static final Pattern _invokePattern = Pattern.compile(
		StringBundler.concat(
			"(?<![\\w$])TransactionTestUtil\\s*\\.\\s*invoke\\s*\\(\\s*",
			"([A-Za-z_$][\\w$]*)\\s*\\.\\s*getPersistence\\s*\\(\\s*\\)\\s*,",
			"\\s*([A-Za-z_$][\\w$]*)\\s*->\\s*\\2\\s*\\.\\s*",
			"([A-Za-z_$][\\w$]*)\\s*\\("));
	private static final Pattern _testFilePattern = Pattern.compile(
		"(-test[^/]*/|/test/|/testIntegration/).*\\.java$|TestUtil\\.java$");

	private final Map<String, List<String>> _referenceFileNamesMap =
		new ConcurrentHashMap<>();
	private Map<String, Set<String>> _testClassFileNamesMap;

}