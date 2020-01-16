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

import com.liferay.petra.process.ProcessCallable;
import com.liferay.petra.process.ProcessException;
import com.liferay.petra.process.local.LocalProcessLauncher;
import com.liferay.petra.reflect.ReflectionUtil;

import java.io.IOException;
import java.io.Serializable;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class SidecarProcessCallable implements ProcessCallable<Serializable> {

	public SidecarProcessCallable(
		String[] arguments, long heartbeatInterval, boolean clustered,
		String modifiedClassName, byte[] modifiedClassBytes) {

		_arguments = arguments;
		_heartbeatInterval = heartbeatInterval;
		_clustered = clustered;
		_modifiedClassName = modifiedClassName;
		_modifiedClassBytes = modifiedClassBytes;
	}

	@Override
	public Serializable call() throws ProcessException {
		LocalProcessLauncher.ProcessContext.attach(
			"SidecarProcessCallable", _heartbeatInterval,
			(shutdownCode, shutdownThrowable) -> {
				ElasticsearchServerUtil.shutdown();

				return true;
			});

		try {
			LocalProcessLauncher.ProcessContext.writeProcessCallable(
				new NotifyParentProcessCallable("Starting sidecar"));
		}
		catch (IOException ioe) {
			if (_logger.isWarnEnabled()) {
				_logger.warn("Unable to notify parent process", ioe);
			}
		}

		try {
			_loadModifiedClass();
		}
		catch (Exception e) {
			if (_logger.isWarnEnabled()) {
				_logger.warn(
					"Unable to load modified class " + _modifiedClassName, e);
			}
		}

		Thread thread = new Thread(
			new ElasticsearchServerStatusMonitor(),
			"Elasticsearch Server Status Monitor");

		thread.setDaemon(true);

		thread.start();

		try {
			ElasticsearchServerUtil.start(_arguments);
		}
		catch (Exception e) {
			throw new ProcessException(
				"Unable to start Elasticsearch server", e);
		}

		return null;
	}

	private void _loadModifiedClass() throws Exception {
		Thread thread = Thread.currentThread();

		ClassLoader classLoader = thread.getContextClassLoader();

		Method findLoadedClassMethod = ReflectionUtil.getDeclaredMethod(
			ClassLoader.class, "findLoadedClass", String.class);

		Class<?> clazz = (Class<?>)findLoadedClassMethod.invoke(
			classLoader, _modifiedClassName);

		if (clazz != null) {
			throw new IllegalStateException(
				_modifiedClassName + " has been loaded");
		}

		Method defineClassMethod = ReflectionUtil.getDeclaredMethod(
			ClassLoader.class, "defineClass", String.class, byte[].class,
			int.class, int.class);

		defineClassMethod.invoke(
			classLoader, _modifiedClassName, _modifiedClassBytes, 0,
			_modifiedClassBytes.length);
	}

	private static final Logger _logger = LogManager.getLogger(
		ElasticsearchServerStatusMonitor.class);

	private static final long serialVersionUID = 1L;

	private final String[] _arguments;
	private final boolean _clustered;
	private final long _heartbeatInterval;
	private final byte[] _modifiedClassBytes;
	private final String _modifiedClassName;

	private class ElasticsearchServerStatusMonitor implements Runnable {

		@Override
		public void run() {
			Node node;

			try {
				node = ElasticsearchServerUtil.waitForStarted();
			}
			catch (Exception e) {
				if (_logger.isWarnEnabled()) {
					_logger.warn(
						"Elasticsearch server is not fully started", e);
				}

				ElasticsearchServerUtil.shutdown();

				return;
			}

			try {
				LocalProcessLauncher.ProcessContext.writeProcessCallable(
					new NotifyParentProcessCallable("Started sidecar"));
			}
			catch (IOException ioe) {
				if (_logger.isWarnEnabled()) {
					_logger.warn("Unable to notify parent process", ioe);
				}

				ElasticsearchServerUtil.shutdown();

				return;
			}

			if (_clustered) {
				try {
					ElasticsearchServerUtil.monitorClusterStatus(
						node, _heartbeatInterval);
				}
				catch (Exception e) {
					if (_logger.isWarnEnabled()) {
						_logger.warn("Unable to monitor cluster status", e);
					}

					ElasticsearchServerUtil.shutdown();
				}
			}
		}

	}

}