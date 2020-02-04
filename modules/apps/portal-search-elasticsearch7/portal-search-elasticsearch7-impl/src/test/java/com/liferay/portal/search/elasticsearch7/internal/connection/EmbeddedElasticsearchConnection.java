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

package com.liferay.portal.search.elasticsearch7.internal.connection;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.settings.SettingsBuilder;
import com.liferay.portal.search.elasticsearch7.internal.util.ClassLoaderUtil;
import com.liferay.portal.search.elasticsearch7.internal.util.LogUtil;

import io.netty.buffer.ByteBufUtil;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.threadpool.ThreadPool;

/**
 * @author Michael C. Han
 */
public class EmbeddedElasticsearchConnection
	extends BaseElasticsearchConnection {

	public EmbeddedElasticsearchConnection(
		String liferayHome, String jnaTmpDirName, int httpPort,
		Map<String, Object> properties) {

		_liferayHome = liferayHome;
		_jnaTmpDirName = jnaTmpDirName;
		_httpPort = httpPort;

		_elasticsearchConfiguration = ConfigurableUtil.createConfigurable(
			ElasticsearchConfiguration.class, properties);

		if (_elasticsearchConfiguration.operationMode() ==
				com.liferay.portal.search.elasticsearch7.configuration.
					OperationMode.EMBEDDED) {

			connect();
		}
	}

	@Override
	public void close() {
		super.close();

		if (_node == null) {
			return;
		}

		try {
			Class.forName(ByteBufUtil.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Unable to preload ", ByteBufUtil.class,
						" to prevent Netty shutdown concurrent class loading ",
						"interruption issue"),
					classNotFoundException);
			}
		}

		Injector injector = _node.injector();

		ThreadPool threadPool = injector.getInstance(ThreadPool.class);

		ScheduledExecutorService scheduledExecutorService =
			threadPool.scheduler();

		scheduledExecutorService.shutdown();

		try {
			scheduledExecutorService.awaitTermination(1, TimeUnit.HOURS);
		}
		catch (InterruptedException interruptedException) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Thread pool shutdown wait was interrupted",
					interruptedException);
			}
		}

		try {
			_node.close();
		}
		catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}

		_node = null;
	}

	@Override
	public String getConnectionId() {
		return String.valueOf(OperationMode.EMBEDDED);
	}

	@Override
	public OperationMode getOperationMode() {
		return OperationMode.EMBEDDED;
	}

	protected void _loadConfigurations() {
		_settingsBuilder.put("action.auto_create_index", false);
		_settingsBuilder.put(
			"bootstrap.memory_lock",
			_elasticsearchConfiguration.bootstrapMlockAll());
		_settingsBuilder.put(
			"cluster.name", _elasticsearchConfiguration.clusterName());
		_settingsBuilder.put(
			"cluster.routing.allocation.disk.threshold_enabled", false);
		_settingsBuilder.put(
			"cluster.service.slow_task_logging_threshold", "600s");
		_settingsBuilder.put("discovery.type", "single-node");

		_settingsBuilder.put(
			"http.cors.enabled", _elasticsearchConfiguration.httpCORSEnabled());

		if (_elasticsearchConfiguration.httpCORSEnabled()) {
			_settingsBuilder.put(
				"http.cors.allow-origin",
				_elasticsearchConfiguration.httpCORSAllowOrigin());

			_settingsBuilder.loadFromSource(
				_elasticsearchConfiguration.httpCORSConfigurations());
		}

		_settingsBuilder.put("http.host", "_local_");
		_settingsBuilder.put("http.port", String.valueOf(_httpPort));

		_settingsBuilder.put("monitor.jvm.gc.enabled", "false");

		_settingsBuilder.put("node.data", true);
		_settingsBuilder.put("node.ingest", true);
		_settingsBuilder.put("node.master", true);
		_settingsBuilder.put("node.name", "liferay");

		_settingsBuilder.put(
			"path.data", _liferayHome.concat("/data/elasticsearch7/indices"));
		_settingsBuilder.put(
			"path.home", _liferayHome.concat("/data/elasticsearch7"));
		_settingsBuilder.put("path.logs", _liferayHome.concat("/logs"));
		_settingsBuilder.put(
			"path.repo", _liferayHome.concat("/data/elasticsearch7/repo"));

		_settingsBuilder.put("thread_pool.write.queue_size", "100");
		_settingsBuilder.put("transport.type", "netty4");

		_settingsBuilder.loadFromSource(
			_elasticsearchConfiguration.additionalConfigurations());
	}

	protected Node createNode(Settings settings) {
		Thread thread = Thread.currentThread();

		ClassLoader contextClassLoader = thread.getContextClassLoader();

		Class<?> clazz = getClass();

		thread.setContextClassLoader(clazz.getClassLoader());

		String jnaTmpDir = System.getProperty("jna.tmpdir");

		System.setProperty("jna.tmpdir", _jnaTmpDirName);

		try {
			String[] plugins = {
				"analysis-icu", "analysis-kuromoji", "analysis-smartcn",
				"analysis-stempel"
			};

			for (String plugin : plugins) {
				EmbeddedElasticsearchPluginManager
					embeddedElasticsearchPluginManager =
						new EmbeddedElasticsearchPluginManager(
							plugin,
							_liferayHome + "/data/elasticsearch7/plugins",
							new PluginManagerFactoryImpl(settings),
							new PluginZipFactoryImpl());

				try {
					embeddedElasticsearchPluginManager.install();
				}
				catch (Exception exception) {
					throw new RuntimeException(
						"Unable to install " + plugin + " plugin", exception);
				}
			}

			LogManager.shutdown();

			return EmbeddedElasticsearchNode.newInstance(settings);
		}
		finally {
			thread.setContextClassLoader(contextClassLoader);

			if (jnaTmpDir == null) {
				System.clearProperty("jna.tmpdir");
			}
			else {
				System.setProperty("jna.tmpdir", jnaTmpDir);
			}
		}
	}

	@Override
	protected RestHighLevelClient createRestHighLevelClient() {
		LogUtil.setRestClientLoggerLevel(
			_elasticsearchConfiguration.restClientLoggerLevel());

		_loadConfigurations();

		_node = createNode(_settingsBuilder.build());

		try {
			_node.start();
		}
		catch (NodeValidationException nodeValidationException) {
			throw new RuntimeException(nodeValidationException);
		}

		Class<? extends EmbeddedElasticsearchConnection> clazz = getClass();

		return ClassLoaderUtil.getWithContextClassLoader(
			() -> new RestHighLevelClient(
				RestClient.builder(
					new HttpHost("localhost", _httpPort, "http"))),
			clazz);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EmbeddedElasticsearchConnection.class);

	private final ElasticsearchConfiguration _elasticsearchConfiguration;
	private final int _httpPort;
	private final String _jnaTmpDirName;
	private final String _liferayHome;
	private Node _node;
	private final SettingsBuilder _settingsBuilder = new SettingsBuilder(
		Settings.builder());

}