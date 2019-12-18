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

package com.liferay.portal.search.elasticsearch7.internal.sidecar;

import com.liferay.petra.concurrent.NoticeableFuture;
import com.liferay.petra.io.StreamUtil;
import com.liferay.petra.process.ProcessChannel;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.process.ProcessExecutor;
import com.liferay.petra.process.ProcessLog;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import java.net.URL;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tina Tian
 */
public class Sidecar {

	public Sidecar(ProcessExecutor processExecutor, SidecarConfig sidecarConfig)
		throws Exception {

		_processChannel = processExecutor.execute(
			_createProcessConfig(sidecarConfig),
			new SidecarProcessCallable(_getSidecarArguments(sidecarConfig)));
	}

	public String getNetworkHostAddress() {
		try {
			NoticeableFuture<String> noticeableFuture = _processChannel.write(
				new GetAddressProcessCallable());

			return noticeableFuture.get();
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to get network host address", e);
		}
	}

	public void stop() {
		NoticeableFuture<Serializable> noticeableFuture =
			_processChannel.getProcessNoticeableFuture();

		noticeableFuture.cancel(true);
	}

	private String _createClasspath(SidecarConfig sidecarConfig)
		throws Exception {

		StringBundler sb = new StringBundler();

		ProtectionDomain protectionDomain = Sidecar.class.getProtectionDomain();

		CodeSource codeSource = protectionDomain.getCodeSource();

		URL url = codeSource.getLocation();

		File file = new File(url.toURI());

		sb.append(file.getAbsolutePath());

		sb.append(File.pathSeparator);

		File libFolder = sidecarConfig.getLibFolder();

		for (File libFile : libFolder.listFiles()) {
			sb.append(libFile.getAbsolutePath());
			sb.append(File.pathSeparator);
		}

		File globalLib = new File(PropsValues.LIFERAY_LIB_GLOBAL_DIR);

		for (File libFile : globalLib.listFiles()) {
			String path = libFile.getAbsolutePath();

			if (path.contains("petra")) {
				sb.append(path);
				sb.append(File.pathSeparator);
			}
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private ProcessConfig _createProcessConfig(SidecarConfig sidecarConfig)
		throws Exception {

		List<String> arguments = new ArrayList<>();

		_parseJVMArguments(arguments, sidecarConfig);

		arguments.add("-Des.path.home=" + sidecarConfig.getHomeFolder());
		arguments.add("-Des.path.conf=" + sidecarConfig.getConfigFolder());
		arguments.add("-Des.distribution.flavor=default");
		arguments.add("-Des.distribution.type=tar");
		arguments.add("-Des.bundled_jdk=true");

		Map<String, String> environments = new HashMap<>();

		environments.putAll(System.getenv());

		String localHostAddress = sidecarConfig.getLocalHostAddress();

		if (localHostAddress == null) {
			localHostAddress = "localhost";
		}

		environments.put("HOSTNAME", localHostAddress);

		String classpath = _createClasspath(sidecarConfig);

		ProcessConfig.Builder processConfigBuilder =
			new ProcessConfig.Builder();

		processConfigBuilder.setArguments(arguments);
		processConfigBuilder.setBootstrapClassPath(classpath);
		processConfigBuilder.setEnvironment(environments);
		processConfigBuilder.setProcessLogConsumer(
			processLog -> {
				if (ProcessLog.Level.DEBUG == processLog.getLevel()) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							processLog.getMessage(), processLog.getThrowable());
					}
				}
				else if (ProcessLog.Level.INFO == processLog.getLevel()) {
					if (_log.isInfoEnabled()) {
						_log.info(
							processLog.getMessage(), processLog.getThrowable());
					}
				}
				else if (ProcessLog.Level.WARN == processLog.getLevel()) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							processLog.getMessage(), processLog.getThrowable());
					}
				}
				else {
					_log.error(
						processLog.getMessage(), processLog.getThrowable());
				}
			});
		processConfigBuilder.setReactClassLoader(
			Sidecar.class.getClassLoader());
		processConfigBuilder.setRuntimeClassPath(classpath);

		return processConfigBuilder.build();
	}

	private String[] _getSidecarArguments(SidecarConfig sidecarConfig) {
		List<String> args = new ArrayList<>();

		String localNodeName = sidecarConfig.getLocalNodeName();

		if (localNodeName != null) {
			args.add("-E");
			args.add("node.name=" + localNodeName);
		}

		String localHostAddress = sidecarConfig.getLocalHostAddress();

		if (localHostAddress != null) {
			args.add("-E");
			args.add("network.host=" + localHostAddress);
		}

		String discoverySeedHosts = sidecarConfig.getDiscoverySeedHosts();

		if (discoverySeedHosts != null) {
			args.add("-E");
			args.add("discovery.seed_hosts=" + discoverySeedHosts);
		}

		String initialMasterNodes = sidecarConfig.getInitialMasterNodes();

		if (initialMasterNodes != null) {
			args.add("-E");
			args.add("cluster.initial_master_nodes=" + initialMasterNodes);
		}

		return args.toArray(new String[0]);
	}

	private void _parseJVMArguments(
			List<String> arguments, SidecarConfig sidecarConfig)
		throws Exception {

		File libFolder = sidecarConfig.getLibFolder();

		File jvmOptions = new File(
			sidecarConfig.getConfigFolder(), "jvm.options");

		List<String> command = new ArrayList<>();

		command.add("java");
		command.add("-cp");
		command.add(
			StringBundler.concat(
				libFolder.getAbsolutePath(), File.separator, StringPool.STAR));
		command.add("org.elasticsearch.tools.launchers.JvmOptionsParser");
		command.add(jvmOptions.getAbsolutePath());

		ProcessBuilder processBuilder = new ProcessBuilder();

		processBuilder.command(command);
		processBuilder.directory(sidecarConfig.getHomeFolder());

		Process process = null;

		try {
			process = processBuilder.start();

			try (InputStream inputStream = process.getInputStream();
				InputStream errorStream = process.getErrorStream()) {

				String output = StreamUtil.toString(inputStream);

				if (output.indexOf("${ES_TMPDIR}") != -1) {
					output = StringUtil.replace(
						output, "${ES_TMPDIR}",
						System.getProperty("java.io.tmpdir"));
				}

				if (_log.isDebugEnabled()) {
					_log.debug(output);
				}

				Collections.addAll(
					arguments, StringUtil.split(output, StringPool.SPACE));

				if (_log.isWarnEnabled()) {
					_log.warn(StreamUtil.toString(errorStream));
				}
			}
		}
		finally {
			if (process != null) {
				process.destroy();

				process.waitFor();
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(Sidecar.class);

	private final ProcessChannel<Serializable> _processChannel;

}