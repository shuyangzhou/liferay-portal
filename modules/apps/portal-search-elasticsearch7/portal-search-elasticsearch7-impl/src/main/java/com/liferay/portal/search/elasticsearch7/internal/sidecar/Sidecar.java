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
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.settings.SettingsBuilder;
import com.liferay.portal.search.elasticsearch7.internal.util.ResourceUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import java.nio.file.Path;

import java.security.CodeSource;
import java.security.ProtectionDomain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.cluster.ClusterModule;
import org.elasticsearch.cluster.coordination.CoordinationMetaData;
import org.elasticsearch.cluster.metadata.Manifest;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.gateway.MetaDataStateFormat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Tina Tian
 */
public class Sidecar {

	public Sidecar(
		ClusterExecutor clusterExecutor, ProcessExecutor processExecutor,
		com.liferay.portal.kernel.util.File file, Props props,
		ElasticsearchConfiguration elasticsearchConfiguration,
		FutureListener<Serializable> restartFutureListener) {

		String liferayHome = props.get(PropsKeys.LIFERAY_HOME);

		File sidecarHome = new File(
			liferayHome, elasticsearchConfiguration.sidecarHome());

		if (!sidecarHome.exists() || !sidecarHome.isDirectory()) {
			sidecarHome = new File(elasticsearchConfiguration.sidecarHome());

			if (!sidecarHome.exists() || !sidecarHome.isDirectory()) {
				throw new IllegalArgumentException(
					"Sidecar home " + elasticsearchConfiguration.sidecarHome() +
						" does not exist");
			}
		}

		_sidecarHome = sidecarHome;

		_pathLogs = new File(liferayHome, "logs");

		File dataHome = new File(liferayHome, "data/elasticsearch7");

		_pathData = new File(dataHome, "indices");
		_pathRepo = new File(dataHome, "repo");

		_clusterExecutor = clusterExecutor;
		_processExecutor = processExecutor;
		_file = file;
		_props = props;
		_elasticsearchConfiguration = elasticsearchConfiguration;
		_restartFutureListener = restartFutureListener;
	}

	public String getNetworkHostAddress() {
		try {
			NoticeableFuture<String> noticeableFuture = _processChannel.write(
				new GetAddressProcessCallable());

			return noticeableFuture.get();
		}
		catch (Exception e) {
			throw new IllegalStateException(
				"Unable to get network host address", e);
		}
	}

	public void start() {
		try {
			_cleanUpClusterMetaData();
		}
		catch (Exception e) {
			_log.error("Unable to clean up cluster meta data", e);
		}

		try {
			_processChannel = _processExecutor.execute(
				_createProcessConfig(),
				new SidecarProcessCallable(
					_getSidecarArguments(),
					_elasticsearchConfiguration.sidecarHeartbeatInterval(),
					_clusterExecutor.isEnabled(), _MODIFIED_CLASS_NAME,
					_getModifiedClassBytes()));

			NoticeableFuture<Serializable> noticeableFuture =
				_processChannel.getProcessNoticeableFuture();

			noticeableFuture.addFutureListener(_restartFutureListener);
		}
		catch (Exception e) {
			_log.error("Unable to start sidecar", e);
		}
	}

	public void stop() {
		if (_sidecarTempDir != null) {
			_file.deltree(_sidecarTempDir);
		}

		if (_processChannel == null) {
			return;
		}

		NoticeableFuture<Serializable> noticeableFuture =
			_processChannel.getProcessNoticeableFuture();

		noticeableFuture.removeFutureListener(_restartFutureListener);

		noticeableFuture.cancel(true);
	}

	private void _cleanUpClusterMetaData() throws Exception {
		if (!_clusterExecutor.isEnabled()) {
			return;
		}

		Path nodePath = NodeEnvironment.resolveNodePath(_pathData.toPath(), 0);

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

		CoordinationMetaData.Builder coordinationMetaDataBuilder =
			CoordinationMetaData.builder();

		coordinationMetaDataBuilder.term(coordinationMetaData.term());

		MetaData.Builder metaDataBuilder = MetaData.builder(metaData);

		metaDataBuilder.coordinationMetaData(
			coordinationMetaDataBuilder.build());

		Manifest manifest = manifestMetaDataStateFormat.read(
			namedXContentRegistry, manifestFile.toPath());

		manifestMetaDataStateFormat.write(
			new Manifest(
				manifest.getCurrentTerm(), manifest.getClusterStateVersion(),
				metaDataMetaDataStateFormat.write(
					metaDataBuilder.build(), nodePath),
				manifest.getIndexGenerations()),
			nodePath);
	}

	private String _createClasspath(boolean runtime) throws Exception {
		StringBundler sb = new StringBundler();

		File liferayLibGlobalDir = new File(
			_props.get(PropsKeys.LIFERAY_LIB_GLOBAL_DIR));

		for (File libFile : liferayLibGlobalDir.listFiles()) {
			String path = libFile.getAbsolutePath();

			if (path.contains("petra")) {
				sb.append(path);
				sb.append(File.pathSeparator);
			}
		}

		if (runtime) {
			ProtectionDomain protectionDomain =
				Sidecar.class.getProtectionDomain();

			CodeSource codeSource = protectionDomain.getCodeSource();

			URL url = codeSource.getLocation();

			File file = new File(url.toURI());

			sb.append(file.getAbsolutePath());

			sb.append(File.pathSeparator);

			File libFolder = new File(_sidecarHome, "lib");

			for (File libFile : libFolder.listFiles()) {
				sb.append(libFile.getAbsolutePath());
				sb.append(File.pathSeparator);
			}
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private ProcessConfig _createProcessConfig() throws Exception {
		Map<String, String> environments = new HashMap<>();

		environments.putAll(System.getenv());

		if (_clusterExecutor.isEnabled()) {
			environments.put(
				"HOSTNAME",
				_getHostAddress(_clusterExecutor.getLocalClusterNode()));
		}
		else {
			environments.put("HOSTNAME", "localhost");
		}

		ProcessConfig.Builder processConfigBuilder =
			new ProcessConfig.Builder();

		processConfigBuilder.setArguments(_getJVMArguments());
		processConfigBuilder.setBootstrapClassPath(_createClasspath(false));
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
		processConfigBuilder.setRuntimeClassPath(_createClasspath(true));

		return processConfigBuilder.build();
	}

	private String _generateNodeName(ClusterNode clusterNode) {
		return StringBundler.concat(
			_DEFAULT_NODE_NAME, StringPool.DASH,
			clusterNode.getClusterNodeId());
	}

	private String _getHostAddress(ClusterNode clusterNode) {
		InetAddress inetAddress = clusterNode.getBindInetAddress();

		return inetAddress.getHostAddress();
	}

	private List<String> _getJVMArguments() {
		List<String> arguments = new ArrayList<>();

		for (String jvmOption :
				_elasticsearchConfiguration.sidecarJVMOptions()) {

			arguments.add(jvmOption);
		}

		arguments.add("-Des.distribution.flavor=default");
		arguments.add("-Des.distribution.type=tar");

		try {
			_sidecarTempDir = _file.createTempFolder();
		}
		catch (IOException ioe) {
			throw new IllegalStateException(
				"Unable to create temp folder", ioe);
		}

		File configFolder = new File(_sidecarTempDir, "config");

		try {
			_file.write(
				new File(configFolder, "log4j2.properties"),
				ResourceUtil.getResourceAsString(
					Sidecar.class, "/META-INF/sidecar/log4j2.properties"));
		}
		catch (IOException ioe) {
			_log.error(
				"Unable to copy log4j2.properties to " +
					configFolder.getAbsolutePath(),
				ioe);
		}

		arguments.add("-Des.path.conf=" + configFolder.getAbsolutePath());

		arguments.add("-Des.path.home=" + _sidecarHome.getAbsolutePath());
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
		arguments.add("-Djava.io.tmpdir=" + _sidecarTempDir.getAbsolutePath());

		ProtectionDomain protectionDomain = Sidecar.class.getProtectionDomain();

		CodeSource codeSource = protectionDomain.getCodeSource();

		URLClassLoader urlClassLoader = new URLClassLoader(
			new URL[] {codeSource.getLocation()});

		URL url = urlClassLoader.findResource(
			"META-INF/sidecar/security.policy");

		arguments.add("-Djava.security.policy=" + url.toString());

		arguments.add("-Djna.nosys=true");

		return arguments;
	}

	private byte[] _getModifiedClassBytes() throws Exception {
		List<URL> urls = new ArrayList<>();

		File libFolder = new File(_sidecarHome, "lib");

		for (File libFile : libFolder.listFiles()) {
			URI uri = libFile.toURI();

			urls.add(uri.toURL());
		}

		ClassLoader classLoader = new URLClassLoader(
			urls.toArray(new URL[0]), null);

		Class<?> clazz = classLoader.loadClass(_MODIFIED_CLASS_NAME);

		try (InputStream inputStream = clazz.getResourceAsStream(
				clazz.getSimpleName() + ".class")) {

			ClassReader classReader = new ClassReader(inputStream);

			ClassWriter classWriter = new ClassWriter(
				classReader, ClassWriter.COMPUTE_MAXS);

			classReader.accept(
				new ClassVisitor(Opcodes.ASM5, classWriter) {

					@Override
					public MethodVisitor visitMethod(
						int access, String name, String description,
						String signature, String[] exceptions) {

						MethodVisitor methodVisitor = super.visitMethod(
							access, name, description, signature, exceptions);

						if (!name.equals("definitelyRunningAsRoot")) {
							return methodVisitor;
						}

						return new MethodVisitor(Opcodes.ASM5) {

							@Override
							public void visitCode() {
								methodVisitor.visitCode();
								methodVisitor.visitInsn(Opcodes.ICONST_0);
								methodVisitor.visitInsn(Opcodes.IRETURN);
							}

							@Override
							public void visitMaxs(int maxStack, int maxLocals) {
								methodVisitor.visitMaxs(0, 0);
							}

						};
					}

				},
				0);

			return classWriter.toByteArray();
		}
	}

	private String[] _getSidecarArguments() {
		List<String> properties = new ArrayList<>();

		SettingsBuilder settingsBuilder = new SettingsBuilder(
			Settings.builder());

		settingsBuilder.loadFromSource(
			ResourceUtil.getResourceAsString(
				Sidecar.class, "/META-INF/sidecar/elasticsearch.yml"));

		if (_elasticsearchConfiguration.httpCORSEnabled()) {
			settingsBuilder.loadFromSource(
				_elasticsearchConfiguration.httpCORSConfigurations());
		}

		Settings settings = settingsBuilder.build();

		for (String key : settings.keySet()) {
			String value = settings.get(key);

			if (Validator.isNotNull(value)) {
				properties.add(key + "=" + value);
			}
		}

		properties.add(
			"bootstrap.memory_lock=" +
				_elasticsearchConfiguration.bootstrapMlockAll());
		properties.add(
			"cluster.name=" + _elasticsearchConfiguration.clusterName());
		properties.add(
			"http.cors.enabled=" +
				_elasticsearchConfiguration.httpCORSEnabled());
		properties.add("path.data=" + _pathData.getAbsolutePath());
		properties.add("path.logs=" + _pathLogs.getAbsolutePath());
		properties.add("path.repo=" + _pathRepo.getAbsolutePath());

		if (_elasticsearchConfiguration.httpCORSEnabled()) {
			properties.add(
				"http.cors.allow-origin=" +
					_elasticsearchConfiguration.httpCORSAllowOrigin());
		}

		if (_clusterExecutor.isEnabled()) {
			ClusterNode localClusterNode =
				_clusterExecutor.getLocalClusterNode();

			properties.add("node.name=" + _generateNodeName(localClusterNode));
			properties.add("network.host=" + _getHostAddress(localClusterNode));

			List<ClusterNode> clusterNodes = _clusterExecutor.getClusterNodes();

			StringBundler discoverySeedHostsSB = new StringBundler(
				2 * clusterNodes.size() - 1);
			StringBundler initialMasterNodesSB = new StringBundler(
				2 * clusterNodes.size() - 1);

			for (ClusterNode clusterNode : clusterNodes) {
				discoverySeedHostsSB.append(_getHostAddress(clusterNode));
				discoverySeedHostsSB.append(StringPool.COMMA);

				initialMasterNodesSB.append(_generateNodeName(clusterNode));
				initialMasterNodesSB.append(StringPool.COMMA);
			}

			discoverySeedHostsSB.setIndex(discoverySeedHostsSB.index() - 1);
			initialMasterNodesSB.setIndex(initialMasterNodesSB.index() - 1);

			properties.add(
				"discovery.seed_hosts=" + discoverySeedHostsSB.toString());
			properties.add(
				"cluster.initial_master_nodes=" +
					initialMasterNodesSB.toString());
		}
		else {
			properties.add("node.name=" + _DEFAULT_NODE_NAME);
			properties.add(
				"cluster.initial_master_nodes=" + _DEFAULT_NODE_NAME);
		}

		if (_log.isInfoEnabled()) {
			Collections.sort(properties);

			StringBundler sb = new StringBundler();

			sb.append("Sidecar properties : {");

			for (String property : properties) {
				sb.append(property);
				sb.append(StringPool.COMMA);
			}

			sb.setStringAt(StringPool.CLOSE_CURLY_BRACE, sb.index() - 1);

			_log.info(sb.toString());
		}

		List<String> arguments = new ArrayList<>();

		for (String property : properties) {
			arguments.add("-E");
			arguments.add(property);
		}

		return arguments.toArray(new String[0]);
	}

	private static final String _DEFAULT_NODE_NAME = "liferay";

	private static final String _MODIFIED_CLASS_NAME =
		"org.elasticsearch.bootstrap.Natives";

	private static final Log _log = LogFactoryUtil.getLog(Sidecar.class);

	private final ClusterExecutor _clusterExecutor;
	private final ElasticsearchConfiguration _elasticsearchConfiguration;
	private final com.liferay.portal.kernel.util.File _file;
	private final File _pathData;
	private final File _pathLogs;
	private final File _pathRepo;
	private ProcessChannel<Serializable> _processChannel;
	private final ProcessExecutor _processExecutor;
	private final Props _props;
	private final FutureListener<Serializable> _restartFutureListener;
	private final File _sidecarHome;
	private File _sidecarTempDir;

}