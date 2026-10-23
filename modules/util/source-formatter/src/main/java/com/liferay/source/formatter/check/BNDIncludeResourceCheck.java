/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.source.formatter.check;

import aQute.libg.glob.Glob;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.tools.ImportPackage;
import com.liferay.source.formatter.SourceFormatterExcludes;
import com.liferay.source.formatter.check.util.BNDSourceUtil;
import com.liferay.source.formatter.util.FileUtil;
import com.liferay.source.formatter.util.GradleBuildFile;
import com.liferay.source.formatter.util.GradleDependency;
import com.liferay.source.formatter.util.SourceFormatterUtil;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hugo Huijser
 */
public class BNDIncludeResourceCheck extends BaseFileCheck {

	@Override
	public boolean isLiferaySourceCheck() {
		return true;
	}

	@Override
	protected String doProcess(
			String fileName, String absolutePath, String content)
		throws IOException {

		if (fileName.endsWith("/bnd.bnd") && !absolutePath.contains("/src/")) {
			_checkIncludeResources(fileName, absolutePath, content);
		}

		if (fileName.endsWith("test-bnd.bnd") ||
			!isModulesFile(
				absolutePath, getPluginsInsideModulesDirectoryNames())) {

			return content;
		}

		return _formatIncludeResource(fileName, content);
	}

	private void _checkIncludeResource(
			String fileName, File moduleDir, String buildGradleContent,
			Map<String, File> dependencySourceDirs, String name, int lineNumber)
		throws IOException {

		String source = name;
		String target = null;

		int index = name.indexOf(CharPool.EQUAL);

		if (index != -1) {
			source = StringUtil.trim(name.substring(index + 1));
			target = StringUtil.trim(name.substring(0, index));
		}

		boolean optional = source.startsWith(StringPool.DASH);

		if (optional) {
			source = source.substring(1);
		}

		_checkPackageJSON(fileName, source, lineNumber);

		if (!optional) {
			_checkPath(
				fileName, moduleDir, buildGradleContent, source, target,
				lineNumber);
		}

		_checkTestClassesIntegration(
			fileName, buildGradleContent, source, lineNumber);
		_checkUnpackedClass(
			fileName, moduleDir, dependencySourceDirs, source, lineNumber);
	}

	private void _checkIncludeResources(
			String fileName, String absolutePath, String content)
		throws IOException {

		Matcher matcher = _includeResourceDefinitionPattern.matcher(content);

		if (!matcher.find()) {
			return;
		}

		File file = new File(absolutePath);

		File moduleDir = file.getParentFile();

		String buildGradleContent = FileUtil.read(
			new File(moduleDir, "build.gradle"));

		Map<String, File> dependencySourceDirs = _getDependencySourceDirs(
			moduleDir, buildGradleContent);

		matcher.reset();

		while (matcher.find()) {
			int x = matcher.start();

			for (String clause : BNDSourceUtil.getClauses(matcher.group(2))) {
				String name = _getName(clause);

				int y = content.indexOf(name, x);

				if ((y == -1) || (y > matcher.end())) {
					y = matcher.start();
				}
				else {
					x = y + name.length();
				}

				_checkIncludeResource(
					fileName, moduleDir, buildGradleContent,
					dependencySourceDirs, name, getLineNumber(content, y));
			}
		}
	}

	private void _checkPackageJSON(
		String fileName, String source, int lineNumber) {

		if (!source.startsWith(StringPool.AT) &&
			(source.equals("package.json") ||
			 source.endsWith("/package.json"))) {

			addMessage(
				fileName,
				"Do not include \"package.json\", Gradle already includes it",
				lineNumber);
		}
	}

	private void _checkPath(
		String fileName, File moduleDir, String buildGradleContent,
		String source, String target, int lineNumber) {

		String path = source;

		if (path.startsWith(StringPool.AT)) {
			path = _getJarName(path);

			if (!path.contains(StringPool.SLASH)) {
				return;
			}
		}
		else if (_isClasspathPattern(path)) {
			return;
		}

		if (path.contains("${") || path.contains("test-classes/integration")) {
			return;
		}

		Path filePath = Paths.get(moduleDir.getPath(), path);

		filePath = filePath.normalize();

		File file = filePath.toFile();

		if (_isAntJar(file)) {
			return;
		}

		File buildOutputDir = _getBuildOutputDir(moduleDir, file);

		if (buildOutputDir == null) {
			if (file.exists()) {
				_checkResourcesPath(
					fileName, moduleDir, file, path, target, lineNumber);

				return;
			}
		}
		else if (_isModuleBuildOutput(
					moduleDir, buildGradleContent, buildOutputDir, file)) {

			return;
		}

		addMessage(
			fileName,
			"Included path \"" + path + "\" does not exist in the repository",
			lineNumber);
	}

	private void _checkResourcesPath(
		String fileName, File moduleDir, File file, String path, String target,
		int lineNumber) {

		String resourcesDirName = "src/main/resources";

		if (fileName.endsWith("-test/bnd.bnd")) {
			resourcesDirName = "src/testIntegration/resources";
		}

		File resourcesDir = new File(moduleDir, resourcesDirName);

		Path resourcesDirPath = resourcesDir.toPath();

		Path filePath = file.toPath();

		if (!filePath.startsWith(resourcesDirPath)) {
			return;
		}

		if (target == null) {
			target = StringPool.BLANK;

			if (!file.isDirectory()) {
				target = file.getName();
			}
		}

		Path relativePath = resourcesDirPath.relativize(filePath);

		if (StringUtil.equals(
				StringUtil.trim(target, CharPool.SLASH),
				StringUtil.replace(
					relativePath.toString(), CharPool.BACK_SLASH,
					CharPool.SLASH))) {

			addMessage(
				fileName,
				StringBundler.concat(
					"Do not include \"", path, "\", Gradle already includes ",
					"it from \"", resourcesDirName, "\""),
				lineNumber);
		}
	}

	private void _checkTestClassesIntegration(
		String fileName, String buildGradleContent, String source,
		int lineNumber) {

		if (!source.contains("test-classes/integration") ||
			!fileName.endsWith("-test/bnd.bnd")) {

			return;
		}

		if (buildGradleContent != null) {
			Matcher matcher = _jarEnabledPattern.matcher(buildGradleContent);

			if (matcher.find()) {
				return;
			}
		}

		addMessage(
			fileName,
			"Do not use \"test-classes/integration\" in bnd.bnd in test " +
				"modules",
			lineNumber);
	}

	private void _checkUnpackedClass(
			String fileName, File moduleDir,
			Map<String, File> dependencySourceDirs, String source,
			int lineNumber)
		throws IOException {

		if (!source.startsWith(StringPool.AT) ||
			fileName.contains("/modules/sdk/") ||
			fileName.contains("/modules/util/")) {

			return;
		}

		int index = source.indexOf("!/");

		if (index == -1) {
			return;
		}

		String entryName = source.substring(index + 2);

		Matcher matcher = _singleClassPattern.matcher(entryName);

		if (!matcher.matches()) {
			return;
		}

		String className = StringUtil.replace(
			matcher.group(1), CharPool.SLASH, CharPool.PERIOD);

		File sourceDir = _getSourceDir(
			moduleDir, dependencySourceDirs, _getJarName(source));

		if (sourceDir != null) {
			File sourceFile = new File(sourceDir, matcher.group(1) + ".java");

			if (!sourceFile.exists()) {
				addMessage(
					fileName,
					StringBundler.concat(
						"Unpacked class \"", className,
						"\" does not exist in its jar"),
					lineNumber);

				return;
			}
		}

		if (!_isReferenced(moduleDir, fileName, className)) {
			addMessage(
				fileName,
				StringBundler.concat(
					"Unpacked class \"", className,
					"\" is not referenced by the module"),
				lineNumber);
		}
	}

	private String _formatIncludeResource(String fileName, String content) {
		if (fileName.endsWith("/bnd.bnd") &&
			!fileName.endsWith("-test/bnd.bnd")) {

			Matcher matcher = _includeDashResourcePattern.matcher(content);

			if (matcher.find()) {
				String replacement = StringUtil.replace(
					matcher.group(), "Include-Resource:", "-includeresource:");

				return StringUtil.replace(
					content, matcher.group(), replacement);
			}
		}

		Matcher matcher1 = _includeResourcePattern.matcher(content);

		if (!matcher1.find()) {
			return content;
		}

		String includeResources = matcher1.group();

		matcher1 = _includeResourceJarPattern.matcher(includeResources);

		if (matcher1.find()) {
			String replacement = StringUtil.replace(
				includeResources, matcher1.group(), "-[0-9]*.jar");

			return StringUtil.replace(content, includeResources, replacement);
		}

		for (String includeResourceDir : _INCLUDE_RESOURCE_DIRS_BLACKLIST) {
			Pattern includeResourceDirPattern = Pattern.compile(
				"(\t|: )" + includeResourceDir + "(,\\\\\n|\n||\\Z)");

			Matcher matcher2 = includeResourceDirPattern.matcher(
				includeResources);

			if (!matcher2.find()) {
				continue;
			}

			String beforeIncludeResourceDir = matcher2.group(1);

			if (!beforeIncludeResourceDir.equals("\t")) {
				return StringUtil.removeSubstring(content, includeResources);
			}

			String afterIncludeResourceDir = matcher2.group(2);

			int x = includeResources.lastIndexOf("\\", matcher2.start());

			String replacement = null;

			if (afterIncludeResourceDir.equals(",\\\n")) {
				int y = matcher2.end();

				replacement =
					includeResources.substring(0, x + 1) +
						includeResources.substring(y - 1);
			}
			else {
				replacement = includeResources.substring(0, x - 1);

				if (afterIncludeResourceDir.equals("\n")) {
					replacement += "\n";
				}
			}

			return StringUtil.replace(content, includeResources, replacement);
		}

		return _sortDefinitionProperties(
			content, includeResources, new IncludeResourceComparator());
	}

	private File _getBuildOutputDir(File moduleDir, File file) {
		Path moduleDirPath = moduleDir.toPath();

		while ((file != null) && !moduleDirPath.startsWith(file.toPath())) {
			if (ArrayUtil.contains(_BUILD_OUTPUT_DIR_NAMES, file.getName()) &&
				_isModuleDir(file.getParentFile())) {

				return file;
			}

			file = file.getParentFile();
		}

		return null;
	}

	private File _getCoreSourceDir(File dir) {
		File buildXMLFile = new File(dir, "build.xml");

		if (buildXMLFile.exists()) {
			return new File(dir, "src");
		}

		return null;
	}

	private File _getCoreSourceDir(GradleDependency gradleDependency) {
		String name = gradleDependency.getName();

		if (!Objects.equals(
				gradleDependency.getGroup(), "com.liferay.portal") ||
			!name.startsWith("com.liferay.")) {

			return null;
		}

		File portalDir = getPortalDir();

		if (portalDir == null) {
			return null;
		}

		File coreDir = new File(
			portalDir,
			StringUtil.replace(
				name.substring(12), CharPool.PERIOD, CharPool.DASH));

		return _getCoreSourceDir(coreDir);
	}

	private Map<String, File> _getDependencySourceDirs(
		File moduleDir, String buildGradleContent) {

		Map<String, File> dependencySourceDirs = new HashMap<>();

		if (buildGradleContent == null) {
			return dependencySourceDirs;
		}

		GradleBuildFile gradleBuildFile = new GradleBuildFile(
			buildGradleContent);

		for (GradleDependency gradleDependency :
				gradleBuildFile.getGradleDependencies()) {

			String version = gradleDependency.getVersion();

			if ((version == null) || version.equals("default") ||
				!_isVersion(version)) {

				version = "1.0.0";
			}

			String dependency = _getLines(
				buildGradleContent, gradleDependency.getLineNumber(),
				gradleDependency.getLastLineNumber());

			String classifier = StringPool.BLANK;

			Matcher matcher = _classifierPattern.matcher(dependency);

			if (matcher.find()) {
				classifier = StringPool.DASH + matcher.group(1);
			}

			String extension = "jar";

			matcher = _extensionPattern.matcher(dependency);

			if (matcher.find()) {
				extension = matcher.group(1);
			}

			dependencySourceDirs.put(
				StringBundler.concat(
					gradleDependency.getName(), StringPool.DASH, version,
					classifier, StringPool.PERIOD, extension),
				_getCoreSourceDir(gradleDependency));
		}

		File rootDir = _getRootDir(moduleDir);

		if (rootDir == null) {
			return dependencySourceDirs;
		}

		Matcher matcher = _projectDependencyPattern.matcher(buildGradleContent);

		while (matcher.find()) {
			File projectDir = new File(
				rootDir,
				StringUtil.replace(
					matcher.group(1), CharPool.COLON, CharPool.SLASH));

			String bndContent = FileUtil.read(new File(projectDir, "bnd.bnd"));

			if (bndContent == null) {
				continue;
			}

			String bundleSymbolicName = BNDSourceUtil.getDefinitionValue(
				bndContent, "Bundle-SymbolicName");

			if (bundleSymbolicName == null) {
				continue;
			}

			int index = bundleSymbolicName.indexOf(CharPool.SEMICOLON);

			if (index != -1) {
				bundleSymbolicName = bundleSymbolicName.substring(0, index);
			}

			String bundleVersion = BNDSourceUtil.getDefinitionValue(
				bndContent, "Bundle-Version");

			if (bundleVersion == null) {
				bundleVersion = "1.0.0";
			}

			bundleSymbolicName = StringUtil.trim(bundleSymbolicName);

			String projectBuildGradleContent = FileUtil.read(
				new File(projectDir, "build.gradle"));

			if ((projectBuildGradleContent != null) &&
				projectBuildGradleContent.contains(
					"archiveFileName = archiveBaseName.get() + \".jar\"")) {

				dependencySourceDirs.put(
					bundleSymbolicName + ".jar",
					new File(projectDir, "src/main/java"));

				continue;
			}

			dependencySourceDirs.put(
				StringBundler.concat(
					bundleSymbolicName, StringPool.DASH,
					StringUtil.trim(bundleVersion), ".jar"),
				new File(projectDir, "src/main/java"));
		}

		return dependencySourceDirs;
	}

	private String _getJarName(String source) {
		String jarName = source.substring(1);

		int index = jarName.indexOf("!/");

		if (index != -1) {
			return jarName.substring(0, index);
		}

		return jarName;
	}

	private String _getLines(
		String content, int lineNumber, int lastLineNumber) {

		StringBundler sb = new StringBundler();

		for (int i = lineNumber; i <= lastLineNumber; i++) {
			sb.append(getLine(content, i));
			sb.append(StringPool.NEW_LINE);
		}

		return sb.toString();
	}

	private String _getName(String clause) {
		String name = clause;

		int index = name.lastIndexOf(StringPool.CLOSE_CURLY_BRACE);

		if (name.startsWith(StringPool.OPEN_CURLY_BRACE) && (index != -1)) {
			name = name.substring(1, index) + name.substring(index + 1);
		}

		index = name.indexOf(StringPool.SEMICOLON);

		if (index != -1) {
			name = name.substring(0, index);
		}

		return StringUtil.trim(name);
	}

	private File _getRootDir(File moduleDir) {
		for (File dir = moduleDir; dir != null; dir = dir.getParentFile()) {
			File settingsGradleFile = new File(dir, "settings.gradle");

			if (settingsGradleFile.exists()) {
				return dir;
			}
		}

		return null;
	}

	private File _getSourceDir(
		File moduleDir, Map<String, File> dependencySourceDirs,
		String jarName) {

		if (jarName.contains(StringPool.SLASH)) {
			Path filePath = Paths.get(moduleDir.getPath(), jarName);

			filePath = filePath.normalize();

			File file = filePath.toFile();

			if (_isAntJar(file)) {
				return _getCoreSourceDir(file.getParentFile());
			}

			return null;
		}

		Glob glob = new Glob(jarName);

		for (Map.Entry<String, File> entry : dependencySourceDirs.entrySet()) {
			if (glob.matches(entry.getKey())) {
				return entry.getValue();
			}
		}

		return null;
	}

	private boolean _isAntJar(File file) {
		File dir = file.getParentFile();

		if (dir == null) {
			return false;
		}

		String fileName = file.getName();

		if (!fileName.equals(dir.getName() + ".jar")) {
			return false;
		}

		File buildXMLFile = new File(dir, "build.xml");

		return buildXMLFile.exists();
	}

	private boolean _isClasspathPattern(String source) {
		if (source.contains(StringPool.SLASH)) {
			return false;
		}

		Matcher matcher = _classpathPatternPattern.matcher(source);

		return matcher.find();
	}

	private boolean _isModuleBuildOutput(
		File moduleDir, String buildGradleContent, File buildOutputDir,
		File file) {

		if (!moduleDir.equals(buildOutputDir.getParentFile())) {
			return false;
		}

		String buildOutputDirName = buildOutputDir.getName();

		if (!buildOutputDirName.equals("lib")) {
			return ArrayUtil.contains(
				new String[] {"build", "classes", "tmp"}, buildOutputDirName);
		}

		String fileName = file.getName();

		if (fileName.endsWith("-ws.jar")) {
			File wsdlFile = new File(
				moduleDir,
				"wsdl/" + StringUtil.removeSubstring(fileName, "-ws.jar") +
					".wsdl");

			if (wsdlFile.exists()) {
				return true;
			}
		}

		if (fileName.endsWith("-xbean.jar")) {
			File xsdDir = new File(moduleDir, "xsd");

			if (xsdDir.isDirectory()) {
				return true;
			}
		}

		if (buildGradleContent == null) {
			return false;
		}

		Matcher matcher = _copyIntoLibPattern.matcher(buildGradleContent);

		return matcher.find();
	}

	private boolean _isModuleDir(File dir) {
		if (dir == null) {
			return false;
		}

		for (String buildFileName :
				new String[] {"bnd.bnd", "build.gradle", "build.xml"}) {

			File buildFile = new File(dir, buildFileName);

			if (buildFile.exists()) {
				return true;
			}
		}

		return false;
	}

	private boolean _isReferenced(
			File moduleDir, String fileName, String className)
		throws IOException {

		List<String> sourceDirNames = new ArrayList<>();

		File buildXMLFile = new File(moduleDir, "build.xml");

		if (buildXMLFile.exists()) {
			sourceDirNames.add("src");
		}
		else {
			sourceDirNames.add("src/main");

			if (fileName.endsWith("-test/bnd.bnd")) {
				sourceDirNames.add("src/testIntegration");
			}
		}

		String packageName = className.substring(
			0, className.lastIndexOf(CharPool.PERIOD));

		Pattern simpleClassNamePattern = Pattern.compile(
			"\\b" +
				Pattern.quote(className.substring(packageName.length() + 1)) +
					"\\b");

		for (String sourceDirName : sourceDirNames) {
			File sourceDir = new File(moduleDir, sourceDirName);

			if (!sourceDir.isDirectory()) {
				continue;
			}

			for (String sourceFileName :
					SourceFormatterUtil.scanForFileNames(
						sourceDir.getPath(), new String[0],
						_SOURCE_FILE_NAME_INCLUDES,
						new SourceFormatterExcludes(), false)) {

				String content = FileUtil.read(new File(sourceFileName));

				if (content == null) {
					continue;
				}

				if (content.contains(className) ||
					content.contains(
						StringUtil.replace(
							className, CharPool.PERIOD, CharPool.SLASH))) {

					return true;
				}

				if (!sourceFileName.endsWith(".java") ||
					!(content.contains("package " + packageName + ";") ||
					  content.contains("import " + packageName + ".*;"))) {

					continue;
				}

				Matcher matcher = simpleClassNamePattern.matcher(content);

				if (matcher.find()) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean _isVersion(String version) {
		Matcher matcher = _versionPattern.matcher(version);

		return matcher.matches();
	}

	private String _sortDefinitionProperties(
		String content, String properties, Comparator<String> comparator) {

		String[] lines = StringUtil.splitLines(properties);

		if (lines.length == 1) {
			return content;
		}

		String previousProperty = null;

		for (int i = 1; i < lines.length; i++) {
			String property = StringUtil.trim(lines[i]);

			if (property.endsWith(",\\")) {
				property = property.substring(0, property.length() - 2);
			}

			if (previousProperty != null) {
				int value = comparator.compare(previousProperty, property);

				if (value > 0) {
					String replacement = StringUtil.replaceFirst(
						properties, previousProperty, property);

					replacement = StringUtil.replaceLast(
						replacement, property, previousProperty);

					return StringUtil.replace(content, properties, replacement);
				}
			}

			previousProperty = property;
		}

		return content;
	}

	private static final String[] _BUILD_OUTPUT_DIR_NAMES = {
		"bin", "build", "classes", "lib", "node_modules", "test-classes", "tmp"
	};

	private static final String[] _INCLUDE_RESOURCE_DIRS_BLACKLIST = {
		"classes", "META-INF/resources=src/main/resources/META-INF/resources",
		"META-INF/resources/content=src/main/resources/content",
		"WEB-INF=src/main/resources/WEB-INF"
	};

	private static final String[] _SOURCE_FILE_NAME_INCLUDES = {
		"**/*.ftl", "**/*.java", "**/*.js", "**/*.jsp", "**/*.jspf",
		"**/*.properties", "**/*.tag", "**/*.tld", "**/*.xml"
	};

	private static final Pattern _classifierPattern = Pattern.compile(
		"\\bclassifier: \"([^\"]+)\"");
	private static final Pattern _classpathPatternPattern = Pattern.compile(
		"[*\\[]|\\.(bin|jar|mmdb|zip)$");
	private static final Pattern _copyIntoLibPattern = Pattern.compile(
		"^\\s*into \"lib\"$", Pattern.MULTILINE);
	private static final Pattern _extensionPattern = Pattern.compile(
		"\\bext: \"([^\"]+)\"");
	private static final Pattern _includeDashResourcePattern = Pattern.compile(
		"^Include-Resource:.+", Pattern.MULTILINE);
	private static final Pattern _includeResourceDefinitionPattern =
		Pattern.compile(
			"^(-includeresource(?:\\.[^\\s:=]+)?|Include-Resource)[:=]" +
				"((?:.*\\\\\n)*.*)",
			Pattern.MULTILINE);
	private static final Pattern _includeResourceJarPattern = Pattern.compile(
		"-[0-9\\.]+\\.jar");
	private static final Pattern _includeResourcePattern = Pattern.compile(
		"^(-includeresource|Include-Resource):[\\s\\S]*?([^\\\\]\n|\\Z)",
		Pattern.MULTILINE);
	private static final Pattern _jarEnabledPattern = Pattern.compile(
		"^jar \\{[^{}]*?\\benabled = true", Pattern.MULTILINE);
	private static final Pattern _projectDependencyPattern = Pattern.compile(
		"project\\((?:path: )?\"(:[^\"]+)\"");
	private static final Pattern _singleClassPattern = Pattern.compile(
		"([\\w/]+)(\\$[\\w$]+)?\\.class");
	private static final Pattern _versionPattern = Pattern.compile(
		"\\d[\\w.-]*");

	private static class IncludeResourceComparator
		implements Comparator<String> {

		@Override
		public int compare(String includeResource1, String includeResource2) {
			if (includeResource1.startsWith(StringPool.AT) ^
				includeResource2.startsWith(StringPool.AT)) {

				if (includeResource1.startsWith(StringPool.AT)) {
					return 1;
				}

				return -1;
			}

			int pos1 = includeResource1.indexOf(".jar!/");
			int pos2 = includeResource2.indexOf(".jar!/");

			if ((pos1 == -1) || (pos2 == -1)) {
				return includeResource1.compareToIgnoreCase(includeResource2);
			}

			String jarFileName1 = includeResource1.substring(0, pos1);
			String jarFileName2 = includeResource1.substring(0, pos2);

			if (!jarFileName1.equals(jarFileName2)) {
				return includeResource1.compareToIgnoreCase(includeResource2);
			}

			String importString1 = includeResource1.substring(pos1 + 6);
			String importString2 = includeResource2.substring(pos2 + 6);

			if (importString1.endsWith(".class")) {
				importString1 = importString1.substring(
					0, importString1.length() - 6);
			}

			if (importString2.endsWith(".class")) {
				importString2 = importString2.substring(
					0, importString2.length() - 6);
			}

			ImportPackage importPackage1 = new ImportPackage(
				importString1, false, includeResource1);
			ImportPackage importPackage2 = new ImportPackage(
				importString2, false, includeResource2);

			return importPackage1.compareTo(importPackage2);
		}

	}

}