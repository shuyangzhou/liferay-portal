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

package com.liferay.source.formatter.checks;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.tools.JavaImportsFormatter;
import com.liferay.source.formatter.checks.util.JavaSourceUtil;
import com.liferay.source.formatter.checks.util.SourceUtil;
import com.liferay.source.formatter.util.FileUtil;

import java.io.File;

import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Peter Shin
 */
public class JavaUpgradeSchemaCheck extends BaseFileCheck {

	@Override
	public void init() throws Exception {
		_absolutePaths.addAll(_getUpgradeAbsolutePaths());
	}

	@Override
	public boolean isPortalCheck() {
		return true;
	}

	public void setAllowedFileNames(String allowedFileNames) {
		String[] array = StringUtil.split(allowedFileNames);

		Collections.addAll(_allowedFileNames, array);
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws Exception {

		if (!absolutePath.contains("/upgrade/")) {
			return content;
		}

		if (absolutePath.endsWith("Impl.java") ||
			absolutePath.endsWith("Test.java") ||
			absolutePath.endsWith("TestCase.java") ||
			absolutePath.endsWith("UpgradeSchema.java")) {

			return content;
		}

		String className = JavaSourceUtil.getClassName(fileName);
		String extendedClassAbsolutePath = _getExtendedClassAbsolutePath(
			absolutePath, content);

		if (!className.startsWith("Base") && !className.startsWith("Upgrade") &&
			!extendedClassAbsolutePath.matches("[\\w]*Upgrade.*")) {

			return content;
		}

		_checkUpgradeSchema(fileName, absolutePath, content);

		return content;
	}

	private void _checkUpgradeSchema(
			String fileName, String absolutePath, String content)
		throws Exception {

		String upgradeAbsolutePath = absolutePath;
		String upgradeContent = content;

		while (Validator.isNotNull(upgradeAbsolutePath)) {
			for (String allowedFileName : _allowedFileNames) {
				if (upgradeAbsolutePath.endsWith(allowedFileName)) {
					return;
				}
			}

			for (String ignoredFileName : _IGNORED_FILE_NAMES) {
				if (upgradeAbsolutePath.endsWith(ignoredFileName)) {
					return;
				}
			}

			if (!absolutePath.equals(upgradeAbsolutePath)) {
				upgradeContent = _getUpgradeContent(upgradeAbsolutePath);
			}

			int lineCount = _getUpgradeSchemaLineCount(
				upgradeAbsolutePath, upgradeContent);

			if (lineCount != -1) {
				StringBundler sb = new StringBundler(6);

				sb.append("Schema change in ");

				if (!absolutePath.equals(upgradeAbsolutePath)) {
					sb.append("base class ");
				}

				sb.append("'");

				String shortFileName = upgradeAbsolutePath.substring(
					upgradeAbsolutePath.lastIndexOf(StringPool.SLASH) + 1);

				sb.append(shortFileName);

				sb.append("#L");
				sb.append(String.valueOf(lineCount));

				addMessage(fileName, sb.toString(), "upgrade_schema.markdown");

				return;
			}

			upgradeAbsolutePath = _getExtendedClassAbsolutePath(
				upgradeAbsolutePath, upgradeContent);
		}
	}

	private String _getExtendedClassAbsolutePath(
		String absolutePath, String content) {

		Matcher matcher = _extendedClassPattern.matcher(content);

		if (!matcher.find()) {
			return StringPool.BLANK;
		}

		String extendedClassName = matcher.group(1);

		if (!extendedClassName.contains(StringPool.PERIOD)) {
			String[] importLines = StringUtil.splitLines(
				JavaImportsFormatter.getImports(content));

			for (String importLine : importLines) {
				if (Validator.isNull(importLine)) {
					continue;
				}

				String importName = importLine.substring(
					7, importLine.length() - 1);

				if (importName.endsWith(CharPool.PERIOD + extendedClassName)) {
					extendedClassName = importName;

					break;
				}
			}
		}

		String s = StringPool.BLANK;

		if (!extendedClassName.contains(StringPool.PERIOD)) {
			int x = absolutePath.lastIndexOf(CharPool.SLASH);

			s = absolutePath.substring(0, x + 1) + extendedClassName;
		}
		else {
			s = StringUtil.replace(
				extendedClassName, CharPool.PERIOD, CharPool.SLASH);
		}

		for (String upgradeAbsolutePath : _absolutePaths) {
			if (upgradeAbsolutePath.endsWith(s + ".java")) {
				return upgradeAbsolutePath;
			}
		}

		return StringPool.BLANK;
	}

	private List<String> _getUpgradeAbsolutePaths() throws Exception {
		File portalDir = getPortalDir();

		if (portalDir == null) {
			return Collections.emptyList();
		}

		final List<String> upgradeAbsolutePaths = new ArrayList<>();

		Files.walkFileTree(
			portalDir.toPath(), EnumSet.noneOf(FileVisitOption.class), 20,
			new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(
					Path dirPath, BasicFileAttributes basicFileAttributes) {

					String dirName = String.valueOf(dirPath.getFileName());

					if (ArrayUtil.contains(_SKIP_DIR_NAMES, dirName)) {
						return FileVisitResult.SKIP_SUBTREE;
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(
					Path filePath, BasicFileAttributes basicFileAttributes) {

					String absolutePath = SourceUtil.getAbsolutePath(filePath);
					String fileName = String.valueOf(filePath.getFileName());

					if (absolutePath.contains("/upgrade/") &&
						fileName.endsWith(".java")) {

						upgradeAbsolutePaths.add(absolutePath);
					}

					return FileVisitResult.CONTINUE;
				}

			});

		return upgradeAbsolutePaths;
	}

	private String _getUpgradeContent(String absolutePath) throws Exception {
		if (_contentsMap.containsKey(absolutePath)) {
			return _contentsMap.get(absolutePath);
		}

		File file = new File(absolutePath);

		if (!file.exists()) {
			return StringPool.BLANK;
		}

		_contentsMap.put(absolutePath, FileUtil.read(file));

		return _contentsMap.get(absolutePath);
	}

	private int _getUpgradeSchemaLineCount(String absolutePath, String content)
		throws Exception {

		if (_schemaLineCountMap.containsKey(absolutePath)) {
			return _schemaLineCountMap.get(absolutePath);
		}

		String lowerCaseContent = StringUtil.toLowerCase(content);

		int pos = lowerCaseContent.indexOf("alter");

		if (pos == -1) {
			pos = lowerCaseContent.indexOf("create table");
		}

		if (pos == -1) {
			pos = lowerCaseContent.indexOf("drop table");
		}

		if (pos == -1) {
			pos = lowerCaseContent.indexOf(".sql\"");
		}

		int lineCount = -1;

		if (pos != -1) {
			int x = absolutePath.lastIndexOf(CharPool.SLASH);
			int y = absolutePath.lastIndexOf(CharPool.PERIOD);

			String fileLocation = absolutePath.substring(0, x + 1);
			String className = absolutePath.substring(x + 1, y);

			String s = _getUpgradeContent(fileLocation + "UpgradeSchema.java");

			if (!s.contains("upgrade(new " + className + "());")) {
				lineCount = getLineCount(content, pos);
			}
		}

		_schemaLineCountMap.put(absolutePath, lineCount);

		return _schemaLineCountMap.get(absolutePath);
	}

	private static final String[] _IGNORED_FILE_NAMES = {
		"/com/liferay/portal/kernel/upgrade/UpgradeMVCCVersion.java",
		"/com/liferay/portal/kernel/upgrade/UpgradeProcess.java",
		"/com/liferay/portal/upgrade/util/UpgradeMVCCVersion.java"
	};

	private static final String[] _SKIP_DIR_NAMES = {
		".git", ".gradle", ".idea", ".m2", ".settings", "bin", "build",
		"classes", "dependencies", "node_modules", "sql", "test",
		"test-classes", "test-coverage", "test-results", "tmp"
	};

	private static final Pattern _extendedClassPattern = Pattern.compile(
		"\\sextends\\s+([\\w\\.]+)\\W");

	private final List<String> _absolutePaths = new ArrayList<>();
	private final List<String> _allowedFileNames = new ArrayList<>();
	private final Map<String, String> _contentsMap = new HashMap<>();
	private final Map<String, Integer> _schemaLineCountMap = new HashMap<>();

}