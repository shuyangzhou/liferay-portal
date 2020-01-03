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

import com.liferay.petra.concurrent.FutureListener;
import com.liferay.petra.concurrent.NoticeableFuture;
import com.liferay.petra.process.ProcessChannel;
import com.liferay.petra.process.ProcessConfig;
import com.liferay.petra.process.ProcessExecutor;
import com.liferay.petra.process.ProcessLog;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.elasticsearch7.internal.util.ResourceUtil;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.Field;

import java.net.URL;

import java.nio.file.Path;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.cluster.ClusterModule;
import org.elasticsearch.cluster.coordination.CoordinationMetaData;
import org.elasticsearch.cluster.metadata.Manifest;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.gateway.MetaDataStateFormat;

/**
 * @author Tina Tian
 */
public class Sidecar {

	public Sidecar(
		ProcessExecutor processExecutor,
		FutureListener<Serializable> futureListener,
		SidecarConfig sidecarConfig) {

		_processExecutor = processExecutor;
		_futureListener = futureListener;
		_sidecarConfig = sidecarConfig;
	}

	public synchronized String getNetworkHostAddress() throws Exception {
		if (_processChannel == null) {
			throw new IllegalStateException(
				"Unable to find sidecar process, it may not be started");
		}

		SidecarStatusProcessCallable sidecarStatusProcessCallable;

		if (_sidecarConfig.isClustered()) {
			sidecarStatusProcessCallable = new SidecarStatusProcessCallable(
				_sidecarConfig.getHeartbeatInterval());
		}
		else {
			sidecarStatusProcessCallable = new SidecarStatusProcessCallable();
		}

		NoticeableFuture<String> noticeableFuture = _processChannel.write(
			sidecarStatusProcessCallable);

		return noticeableFuture.get();
	}

	public synchronized void start() {
		if (_processChannel != null) {
			return;
		}

		if (_sidecarConfig.isClustered()) {
			try {
				_cleanUpClusterMetaData();
			}
			catch (Exception e) {
				_log.error("Unable to clean up cluster meta data", e);
			}
		}

		try {
			_processChannel = _processExecutor.execute(
				_createProcessConfig(),
				new SidecarProcessCallable(
					_getSidecarArguments(),
					_sidecarConfig.getHeartbeatInterval()));

			NoticeableFuture<Serializable> noticeableFuture =
				_processChannel.getProcessNoticeableFuture();

			noticeableFuture.addFutureListener(_futureListener);
		}
		catch (Exception e) {
			_log.error("Unable to start sidecar", e);
		}
	}

	public synchronized void stop() {
		if (_processChannel == null) {
			return;
		}

		NoticeableFuture<Serializable> noticeableFuture =
			_processChannel.getProcessNoticeableFuture();

		noticeableFuture.removeFutureListener(_futureListener);

		noticeableFuture.cancel(true);

		_processChannel = null;
	}

	private void _cleanUpClusterMetaData() throws Exception {
		File homeFolder = _sidecarConfig.getHomeFolder();

		Path homePath = homeFolder.toPath();

		Path nodePath = NodeEnvironment.resolveNodePath(
			homePath.resolve("data"), 0);

		Path statePath = nodePath.resolve(MetaDataStateFormat.STATE_DIR_NAME);

		File stateFolder = statePath.toFile();

		if (!stateFolder.exists()) {
			return;
		}

		MetaDataStateFormat<MetaData> metaDataMetaDataStateFormat =
			MetaData.FORMAT;
		MetaDataStateFormat<Manifest> manifestMetaDataStateFormat =
			Manifest.FORMAT;

		File globalFile = null;
		File manifestFile = null;

		for (File file : stateFolder.listFiles()) {
			String fileName = file.getName();

			if (fileName.startsWith(metaDataMetaDataStateFormat.getPrefix())) {
				globalFile = file;
			}
			else if (fileName.startsWith(
						manifestMetaDataStateFormat.getPrefix())) {

				manifestFile = file;
			}
		}

		if ((globalFile == null) || (manifestFile == null)) {
			return;
		}

		NamedXContentRegistry namedXContentRegistry = new NamedXContentRegistry(
			ClusterModule.getNamedXWriteables());

		MetaData metaData = metaDataMetaDataStateFormat.read(
			namedXContentRegistry, globalFile.toPath());

		CoordinationMetaData coordinationMetaData =
			metaData.coordinationMetaData();

		_setFieldValue(
			coordinationMetaData, "lastCommittedConfiguration",
			CoordinationMetaData.VotingConfiguration.EMPTY_CONFIG);
		_setFieldValue(
			coordinationMetaData, "lastAcceptedConfiguration",
			CoordinationMetaData.VotingConfiguration.EMPTY_CONFIG);

		Manifest manifest = manifestMetaDataStateFormat.read(
			namedXContentRegistry, manifestFile.toPath());

		_setFieldValue(
			manifest, "globalGeneration",
			metaDataMetaDataStateFormat.write(metaData, nodePath));

		manifestMetaDataStateFormat.write(manifest, nodePath);
	}

	private String _createClasspath() throws Exception {
		StringBundler sb = new StringBundler();

		ProtectionDomain protectionDomain = Sidecar.class.getProtectionDomain();

		CodeSource codeSource = protectionDomain.getCodeSource();

		URL url = codeSource.getLocation();

		File file = new File(url.toURI());

		sb.append(file.getAbsolutePath());

		sb.append(File.pathSeparator);

		File libFolder = new File(_sidecarConfig.getHomeFolder(), "lib");

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

	private ProcessConfig _createProcessConfig() throws Exception {
		Map<String, String> environments = new HashMap<>();

		environments.putAll(System.getenv());

		String localHostAddress = _sidecarConfig.getLocalHostAddress();

		if (localHostAddress == null) {
			localHostAddress = "localhost";
		}

		environments.put("HOSTNAME", localHostAddress);

		String classpath = _createClasspath();

		ProcessConfig.Builder processConfigBuilder =
			new ProcessConfig.Builder();

		processConfigBuilder.setArguments(_getJVMArguments());
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

	private List<String> _getJVMArguments() {
		List<String> arguments = new ArrayList<>();

		for (String jvmOption : _sidecarConfig.getJVMOptions()) {
			arguments.add(jvmOption);
		}

		arguments.add("-Des.distribution.flavor=default");
		arguments.add("-Des.distribution.type=tar");

		File homeFolder = _sidecarConfig.getHomeFolder();

		File configFolder = new File(homeFolder, "config");

		arguments.add("-Des.path.conf=" + configFolder.getAbsolutePath());

		arguments.add("-Des.path.home=" + homeFolder.getAbsolutePath());
		arguments.add("-Des.networkaddress.cache.ttl=60");
		arguments.add("-Des.networkaddress.cache.negative.ttl=10");

		arguments.add("-Dlog4j.shutdownHookEnabled=false");
		arguments.add("-Dlog4j2.disable.jmx=true");

		arguments.add("-Dio.netty.allocator.type=unpooled");
		arguments.add("-Dio.netty.allocator.numDirectArenas=0");
		arguments.add("-Dio.netty.noUnsafe=true");
		arguments.add("-Dio.netty.noKeySetOptimization=true");
		arguments.add("-Dio.netty.recycler.maxCapacityPerThread=0");

		arguments.add("-Dfile.encoding=UTF-8");
		arguments.add(
			"-Djava.io.tmpdir" + System.getProperty("java.io.tmpdir"));

		try {
			File policyFile = ResourceUtil.getResourceAsTempFile(
				getClass(), "/META-INF/sidecar.policy");

			arguments.add(
				"-Djava.security.policy=" + policyFile.getAbsolutePath());
		}
		catch (IOException ioe) {
			_log.error(
				"Unable to find policy file : /META-INF/sidecar.policy", ioe);
		}

		arguments.add("-Djna.nosys=true");

		return arguments;
	}

	private String[] _getSidecarArguments() {
		List<String> arguments = new ArrayList<>();

		String localNodeName = _sidecarConfig.getLocalNodeName();

		if (localNodeName != null) {
			arguments.add("-E");
			arguments.add("node.name=" + localNodeName);
		}

		String localHostAddress = _sidecarConfig.getLocalHostAddress();

		if (localHostAddress != null) {
			arguments.add("-E");
			arguments.add("network.host=" + localHostAddress);
		}

		String discoverySeedHosts = _sidecarConfig.getDiscoverySeedHosts();

		if (discoverySeedHosts != null) {
			arguments.add("-E");
			arguments.add("discovery.seed_hosts=" + discoverySeedHosts);
		}

		String initialMasterNodes = _sidecarConfig.getInitialMasterNodes();

		if (initialMasterNodes != null) {
			arguments.add("-E");
			arguments.add("cluster.initial_master_nodes=" + initialMasterNodes);
		}

		return arguments.toArray(new String[0]);
	}

	private void _setFieldValue(Object target, String filedName, Object value)
		throws Exception {

		Field field = ReflectionUtil.getDeclaredField(
			target.getClass(), filedName);

		field.set(target, value);
	}

	private static final Log _log = LogFactoryUtil.getLog(Sidecar.class);

	private final FutureListener<Serializable> _futureListener;
	private ProcessChannel<Serializable> _processChannel;
	private final ProcessExecutor _processExecutor;
	private final SidecarConfig _sidecarConfig;

}