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

package com.liferay.gradle.plugins.css.builder;

import com.liferay.css.builder.CSSBuilderArgs;
import com.liferay.gradle.util.FileUtil;
import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.util.Validator;

import java.io.File;

import java.lang.reflect.Method;

import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.SkipWhenEmpty;
import org.gradle.api.tasks.TaskAction;
import org.gradle.util.CollectionUtils;
import org.gradle.util.GUtil;

/**
 * @author Andrea Di Giorgi
 * @author David Truong
 */
@CacheableTask
public class BuildCSSTask extends DefaultTask {

	public BuildCSSTask() {
		System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
		System.setProperty(
			"sass.compiler.jni.clean.temp.dir", Boolean.TRUE.toString());

		setDirNames("/");
	}

	@TaskAction
	public void buildCSS() throws Exception {
		FileCollection classpath = getClasspath();

		_runWithClasspath(
			getProject(), classpath.getAsPath(), _getCompleteArgs());
	}

	public BuildCSSTask dirNames(Iterable<Object> dirNames) {
		GUtil.addToCollection(_dirNames, dirNames);

		return this;
	}

	public BuildCSSTask dirNames(Object... dirNames) {
		return dirNames(Arrays.asList(dirNames));
	}

	public BuildCSSTask excludes(Iterable<Object> excludes) {
		GUtil.addToCollection(_excludes, excludes);

		return this;
	}

	public BuildCSSTask excludes(Object... excludes) {
		return excludes(Arrays.asList(excludes));
	}

	public File getBaseDir() {
		return GradleUtil.toFile(getProject(), _baseDir);
	}

	@InputFiles
	@PathSensitive(PathSensitivity.RELATIVE)
	public FileCollection getClasspath() {
		return _classpath;
	}

	@InputFiles
	@PathSensitive(PathSensitivity.RELATIVE)
	@SkipWhenEmpty
	public FileCollection getCSSFiles() {
		Project project = getProject();

		List<String> dirNames = getDirNames();
		File docrootDir = getBaseDir();

		if (dirNames.isEmpty() || (docrootDir == null)) {
			return project.files();
		}

		Map<String, Object> args = new HashMap<>();

		args.put("dir", docrootDir);
		args.put("exclude", "**/" + _addTrailingSlash(getOutputDirName()));

		for (String dirName : dirNames) {
			dirName = dirName.replace('\\', '/');

			if (dirName.equals("/")) {
				dirName = "";
			}
			else {
				dirName = _removeLeadingSlash(dirName);
				dirName = _removeTrailingSlash(dirName);

				dirName += "/";
			}

			List<String> includes = new ArrayList<>(2);

			includes.add(dirName + "**/*.css");
			includes.add(dirName + "**/*.scss");

			args.put("includes", includes);
		}

		return project.fileTree(args);
	}

	public List<String> getDirNames() {
		return GradleUtil.toStringList(_dirNames);
	}

	@Input
	public List<String> getExcludes() {
		return GradleUtil.toStringList(_excludes);
	}

	@InputFiles
	@Optional
	@PathSensitive(PathSensitivity.RELATIVE)
	public FileCollection getImports() {
		Project project = getProject();

		return project.files(_imports);
	}

	@Input
	public String getOutputDirName() {
		return GradleUtil.toString(_outputDirName);
	}

	@OutputDirectories
	public FileCollection getOutputDirs() {
		Project project = getProject();

		Set<File> outputDirs = new HashSet<>();

		FileCollection cssFiles = getCSSFiles();

		String outputDirName = _removeTrailingSlash(getOutputDirName());

		for (File cssFile : cssFiles) {
			File outputDir = project.file(cssFile + "/../" + outputDirName);

			outputDirs.add(outputDir);
		}

		return project.files(outputDirs);
	}

	@Input
	public int getPrecision() {
		return GradleUtil.toInteger(_precision);
	}

	@Input
	public List<String> getRtlExcludedPathRegexps() {
		return GradleUtil.toStringList(_rtlExcludedPathRegexps);
	}

	@Input
	@Optional
	public String getSassCompilerClassName() {
		return GradleUtil.toString(_sassCompilerClassName);
	}

	@OutputFiles
	public FileCollection getSourceMapFiles() {
		Project project = getProject();

		List<File> sourceMapFiles = new ArrayList<>();

		if (isGenerateSourceMap()) {
			FileCollection cssFiles = getCSSFiles();

			for (File cssFile : cssFiles) {
				File sourceMapFile = project.file(cssFile + ".map");

				sourceMapFiles.add(sourceMapFile);
			}
		}

		return project.files(sourceMapFiles);
	}

	public BuildCSSTask imports(Iterable<Object> imports) {
		GUtil.addToCollection(_imports, imports);

		return this;
	}

	public BuildCSSTask imports(Object... imports) {
		return imports(Arrays.asList(imports));
	}

	@Input
	public boolean isAppendCssImportTimestamps() {
		return _appendCssImportTimestamps;
	}

	@Input
	public boolean isGenerateSourceMap() {
		return _generateSourceMap;
	}

	public BuildCSSTask rtlExcludedPathRegexps(
		Iterable<Object> rtlExcludedPathRegexps) {

		GUtil.addToCollection(_rtlExcludedPathRegexps, rtlExcludedPathRegexps);

		return this;
	}

	public BuildCSSTask rtlExcludedPathRegexps(
		Object... rtlExcludedPathRegexps) {

		return rtlExcludedPathRegexps(Arrays.asList(rtlExcludedPathRegexps));
	}

	public void setAppendCssImportTimestamps(
		boolean appendCssImportTimestamps) {

		_appendCssImportTimestamps = appendCssImportTimestamps;
	}

	public void setBaseDir(Object baseDir) {
		_baseDir = baseDir;
	}

	public void setClasspath(FileCollection classpath) {
		_classpath = classpath;
	}

	public void setDirNames(Iterable<Object> dirNames) {
		_dirNames.clear();

		dirNames(dirNames);
	}

	public void setDirNames(Object... dirNames) {
		setDirNames(Arrays.asList(dirNames));
	}

	public void setExcludes(Iterable<Object> excludes) {
		_excludes.clear();

		excludes(excludes);
	}

	public void setExcludes(Object... excludes) {
		setExcludes(Arrays.asList(excludes));
	}

	public void setGenerateSourceMap(boolean generateSourceMap) {
		_generateSourceMap = generateSourceMap;
	}

	public void setImports(Iterable<Object> imports) {
		_imports.clear();

		imports(imports);
	}

	public void setImports(Object... imports) {
		setImports(Arrays.asList(imports));
	}

	public void setOutputDirName(Object outputDirName) {
		_outputDirName = outputDirName;
	}

	public void setPrecision(Object precision) {
		_precision = precision;
	}

	public void setRtlExcludedPathRegexps(
		Iterable<Object> rtlExcludedPathRegexps) {

		_rtlExcludedPathRegexps.clear();

		rtlExcludedPathRegexps(rtlExcludedPathRegexps);
	}

	public void setRtlExcludedPathRegexps(Object... rtlExcludedPathRegexps) {
		setRtlExcludedPathRegexps(Arrays.asList(rtlExcludedPathRegexps));
	}

	public void setSassCompilerClassName(Object sassCompilerClassName) {
		_sassCompilerClassName = sassCompilerClassName;
	}

	private String _addTrailingSlash(String path) {
		if (Validator.isNull(path)) {
			return path;
		}

		path = path.replace('\\', '/');

		if (path.charAt(path.length() - 1) != '/') {
			path += '/';
		}

		return path;
	}

	private List<String> _getCompleteArgs() {
		List<String> args = new ArrayList<>();

		args.add(
			"--append-css-import-timestamps=" + isAppendCssImportTimestamps());

		String baseDir = FileUtil.getAbsolutePath(getBaseDir());

		args.add("--base-dir=" + _removeTrailingSlash(baseDir));

		String sassCompilerClassName = getSassCompilerClassName();

		if (Validator.isNotNull(sassCompilerClassName)) {
			args.add("--compiler=" + sassCompilerClassName);
		}

		args.add("--dir-names=" + _getDirNamesArg());

		String excludes = CollectionUtils.join(",", getExcludes());

		args.add("--excludes=" + excludes);

		args.add("--generate-source-map=" + isGenerateSourceMap());

		FileCollection imports = getImports();

		args.add("--import-paths=" + imports.getAsPath());

		args.add("--output-dir=" + _addTrailingSlash(getOutputDirName()));

		args.add("--precision=" + getPrecision());

		String rtlExcludedPathRegexps = CollectionUtils.join(
			",", getRtlExcludedPathRegexps());

		args.add("--rtl-excluded-path-regexps=" + rtlExcludedPathRegexps);

		return args;
	}

	private String _getDirNamesArg() {
		StringBuilder sb = new StringBuilder();

		boolean first = true;

		for (String dirName : getDirNames()) {
			if (!first) {
				sb.append(',');
			}

			first = false;

			sb.append('/');
			sb.append(_removeLeadingSlash(dirName));
		}

		return sb.toString();
	}

	private String _removeLeadingSlash(String path) {
		if (Validator.isNull(path)) {
			return path;
		}

		path = path.replace('\\', '/');

		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}

		return path;
	}

	private String _removeTrailingSlash(String path) {
		if (Validator.isNull(path)) {
			return path;
		}

		path = path.replace('\\', '/');

		if (path.charAt(path.length() - 1) == '/') {
			path = path.substring(0, path.length() - 1);
		}

		return path;
	}

	private void _runWithClasspath(
			Project project, String classpath, List<String> args)
		throws Exception {

		classpath =
			System.getProperty("java.class.path") + File.pathSeparator +
				classpath;

		Logger logger = project.getLogger();

		if (logger.isInfoEnabled()) {
			logger.info("Running with classpath {}", classpath);
		}

		String[] files = classpath.split(File.pathSeparator);

		URL[] urls = new URL[files.length];

		for (int i = 0; i < files.length; i++) {
			File file = new File(files[i]);

			URI uri = file.toURI();

			urls[i] = uri.toURL();
		}

		ClassLoader classLoader = new URLClassLoader(urls, null);

		Class<?> clazz = classLoader.loadClass(
			"com.liferay.css.builder.CSSBuilder");

		Method method = clazz.getMethod("main", String[].class);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		currentThread.setContextClassLoader(classLoader);

		try {
			method.invoke(null, (Object)args.toArray(new String[0]));
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	private boolean _appendCssImportTimestamps =
		CSSBuilderArgs.APPEND_CSS_IMPORT_TIMESTAMPS;
	private Object _baseDir;
	private FileCollection _classpath;
	private final Set<Object> _dirNames = new LinkedHashSet<>();
	private final Set<Object> _excludes = new LinkedHashSet<>(
		Arrays.asList(CSSBuilderArgs.EXCLUDES));
	private boolean _generateSourceMap;
	private final List<Object> _imports = new ArrayList<>();
	private Object _outputDirName = CSSBuilderArgs.OUTPUT_DIR_NAME;
	private Object _precision = CSSBuilderArgs.PRECISION;
	private final Set<Object> _rtlExcludedPathRegexps = new LinkedHashSet<>();
	private Object _sassCompilerClassName;

}