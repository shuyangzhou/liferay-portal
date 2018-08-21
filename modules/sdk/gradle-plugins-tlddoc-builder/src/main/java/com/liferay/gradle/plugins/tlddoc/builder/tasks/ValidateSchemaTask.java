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

package com.liferay.gradle.plugins.tlddoc.builder.tasks;

import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.util.Validator;

import groovy.lang.Closure;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.AntBuilder;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

/**
 * @author Andrea Di Giorgi
 */
public class ValidateSchemaTask extends SourceTask {

	public ValidateSchemaTask dtd(String key, String value) {
		_dtds.put(key, value);

		return this;
	}

	public ValidateSchemaTask dtds(Map<String, String> dtds) {
		_dtds.putAll(dtds);

		return this;
	}

	@Input
	public Map<String, String> getDTDs() {
		return _dtds;
	}

	@Input
	public Map<String, String> getSchemas() {
		return _schemas;
	}

	@Input
	@Optional
	public String getXMLParserClassName() {
		return GradleUtil.toString(_xmlParserClassName);
	}

	@InputFiles
	@Optional
	public FileCollection getXMLParserClasspath() {
		return _xmlParserClasspath;
	}

	@Input
	public boolean isDTDDisabled() {
		return _dtdDisabled;
	}

	@Input
	public boolean isFullChecking() {
		return _fullChecking;
	}

	@Input
	public boolean isLenient() {
		return _lenient;
	}

	public ValidateSchemaTask schema(String key, String value) {
		_schemas.put(key, value);

		return this;
	}

	public ValidateSchemaTask schemas(Map<String, String> schemas) {
		_schemas.putAll(schemas);

		return this;
	}

	public void setDTDDisabled(boolean dtdDisabled) {
		_dtdDisabled = dtdDisabled;
	}

	public void setDTDs(Map<String, String> dtds) {
		_dtds.clear();

		dtds(dtds);
	}

	public void setFullChecking(boolean fullChecking) {
		_fullChecking = fullChecking;
	}

	public void setLenient(boolean lenient) {
		_lenient = lenient;
	}

	public void setSchemas(Map<String, String> schemas) {
		_schemas.clear();

		schemas(schemas);
	}

	public void setXMLParserClassName(Object xmlParserClassName) {
		_xmlParserClassName = xmlParserClassName;
	}

	public void setXMLParserClasspath(FileCollection xmlParserClasspath) {
		_xmlParserClasspath = xmlParserClasspath;
	}

	@TaskAction
	public void validateSchema() {
		Project project = getProject();

		final AntBuilder antBuilder = project.getAnt();

		Map<String, Object> args = new HashMap<>();

		String xmlParserClassName = getXMLParserClassName();

		if (Validator.isNotNull(xmlParserClassName)) {
			args.put("classname", xmlParserClassName);
		}

		FileCollection xmlParserClasspath = getXMLParserClasspath();

		if ((xmlParserClasspath != null) && !xmlParserClasspath.isEmpty()) {
			args.put("classpath", xmlParserClasspath.getAsPath());
		}

		args.put("disableDTD", isDTDDisabled());
		args.put("fullchecking", isFullChecking());
		args.put("lenient", isLenient());

		Closure<Void> closure = new Closure<Void>(antBuilder) {

			@SuppressWarnings("unused")
			public void doCall() {
				FileTree fileTree = getSource();

				fileTree.addToAntBuilder(
					antBuilder, "fileset", FileCollection.AntType.FileSet);

				for (Map.Entry<String, String> entry : _dtds.entrySet()) {
					Map<String, Object> args = new HashMap<>();

					args.put("location", entry.getValue());
					args.put("publicId", entry.getKey());

					antBuilder.invokeMethod("dtd", args);
				}

				for (Map.Entry<String, String> entry : _schemas.entrySet()) {
					Map<String, Object> args = new HashMap<>();

					args.put("file", entry.getValue());
					args.put("namespace", entry.getKey());

					antBuilder.invokeMethod("schema", args);
				}
			}

		};

		antBuilder.invokeMethod("schemavalidate", new Object[] {args, closure});
	}

	private boolean _dtdDisabled;
	private final Map<String, String> _dtds = new HashMap<>();
	private boolean _fullChecking = true;
	private boolean _lenient;
	private final Map<String, String> _schemas = new HashMap<>();
	private Object _xmlParserClassName;
	private FileCollection _xmlParserClasspath;

}