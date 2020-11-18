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

package com.liferay.dispatch.talend.web.internal.process;

import com.liferay.dispatch.talend.web.internal.archive.TalendArchive;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.string.StringPool;

import java.io.File;

import java.net.URL;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Igor Beslic
 */
public interface TalendProcess {

	public static final String CONTEXT_PARM_COMPANY_ID_TPL =
		"--context_param companyId=%d";

	public static final String CONTEXT_PARM_JOB_WORK_DIRECTORY_TPL =
		"--context_param jobWorkDirectory=%s";

	public static final String CONTEXT_PARM_LAST_RUN_START_DATE_TPL =
		"--context_param lastRunStartDate=%s";

	public static final String CONTEXT_PARM_NAME_VALUE_TPL =
		"--context_param %s=%s";

	public static final String JVM_OPTION_NAME_VALUE_TPL = "%s=%s";

	public List<String> getJobArguments();

	public List<String> getProcessArguments();

	public ProcessConfig getProcessConfig();

	public static class Builder {

		public TalendProcess build() {
			ProcessConfig.Builder processConfigBuilder =
				new ProcessConfig.Builder();

			String classpath = _talendArchive.getClasspath();

			classpath =
				classpath + File.pathSeparator + _getBundleURL().toString();

			processConfigBuilder.setBootstrapClassPath(classpath);

			processConfigBuilder.setRuntimeClassPath(classpath);

			List<String> talendJobArguments = new ArrayList<>();

			talendJobArguments.add(
				"--context=" + _talendArchive.getContextName());
			talendJobArguments.add(
				String.format(CONTEXT_PARM_COMPANY_ID_TPL, _companyId));
			talendJobArguments.add(
				String.format(
					CONTEXT_PARM_JOB_WORK_DIRECTORY_TPL,
					_talendArchive.getJobDirectory()));

			if (_lastRunStartDate != null) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss'Z'");

				talendJobArguments.add(
					String.format(
						CONTEXT_PARM_LAST_RUN_START_DATE_TPL,
						simpleDateFormat.format(_lastRunStartDate)));
			}

			talendJobArguments.addAll(_getJobArguments(_contextParams));

			processConfigBuilder.setArguments(_getArguments(_jvmOptions));

			return new TalendProcess() {

				@Override
				public List<String> getJobArguments() {
					return _jobArguments;
				}

				@Override
				public List<String> getProcessArguments() {
					return _arguments;
				}

				@Override
				public ProcessConfig getProcessConfig() {
					return _processConfig;
				}

				private final List<String> _arguments =
					Collections.unmodifiableList(_getArguments(_jvmOptions));
				private final List<String> _jobArguments =
					Collections.unmodifiableList(talendJobArguments);
				private final ProcessConfig _processConfig =
					processConfigBuilder.build();

			};
		}

		public Builder companyId(long companyId) {
			_companyId = companyId;

			return this;
		}

		public Builder contextParam(String name, String value) {
			if (name.startsWith("-D") || name.startsWith("-X")) {
				if (_isSimpleJVMOption(name)) {
					_jvmOptions.add(name);

					return this;
				}

				_jvmOptions.add(
					String.format(JVM_OPTION_NAME_VALUE_TPL, name, value));

				return this;
			}

			_contextParams.add(
				String.format(CONTEXT_PARM_NAME_VALUE_TPL, name, value));

			return this;
		}

		public Builder lastRunStartDate(Date lastRunStartDate) {
			_lastRunStartDate = lastRunStartDate;

			return this;
		}

		public Builder talendArchive(TalendArchive talendArchive) {
			_talendArchive = talendArchive;

			return this;
		}

		private List<String> _getArguments(List<String> contextParameters) {
			Stream<String> stream = contextParameters.stream();

			return stream.filter(
				parameter ->
					parameter.startsWith("-X") || parameter.startsWith("-D")
			).map(
				parameter -> {
					if (parameter.contains(StringPool.EQUAL) &&
						(parameter.startsWith("-Ddebug") ||
						 parameter.startsWith("-Xnoagent"))) {

						return parameter.substring(
							0, parameter.indexOf(StringPool.EQUAL));
					}

					return parameter;
				}
			).collect(
				Collectors.toList()
			);
		}

		private URL _getBundleURL() {
			Class<? extends Builder> clazz = getClass();

			ProtectionDomain protectionDomain = clazz.getProtectionDomain();

			CodeSource codeSource = protectionDomain.getCodeSource();

			return codeSource.getLocation();
		}

		private List<String> _getJobArguments(List<String> contextParameters) {
			Stream<String> stream = contextParameters.stream();

			return stream.filter(
				parameter ->
					!parameter.startsWith("-X") || !parameter.startsWith("-D")
			).collect(
				Collectors.toList()
			);
		}

		private boolean _isSimpleJVMOption(String option) {
			Stream<String> stream = Arrays.stream(_SIMPLE_JVM_OPTIONS);

			return stream.anyMatch(value -> Objects.equals(value, option));
		}

		private static final String[] _SIMPLE_JVM_OPTIONS = {
			"-Xdebug", "-Xnoagent"
		};

		private long _companyId;
		private final List<String> _contextParams = new ArrayList<>();
		private final List<String> _jvmOptions = new ArrayList<>();
		private Date _lastRunStartDate;
		private TalendArchive _talendArchive;

	}

}