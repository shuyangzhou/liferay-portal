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

import com.liferay.petra.reflect.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import org.elasticsearch.cli.ExitCodes;
import org.elasticsearch.cluster.coordination.ClusterFormationFailureHelper;
import org.elasticsearch.cluster.coordination.Coordinator;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class ElasticsearchServerUtil {

	public static void monitorClusterStatus(Node node, long checkInterval)
		throws Exception {

		Injector injector = node.injector();

		ClusterFormationFailureHelper clusterFormationFailureHelper =
			(ClusterFormationFailureHelper)
				_clusterFormationFailureHelperField.get(
					injector.getInstance(Discovery.class));

		while (true) {
			if (clusterFormationFailureHelper.isRunning()) {
				shutdown();

				return;
			}

			Thread.sleep(checkInterval);
		}
	}

	public static void shutdown() {
		try {
			_stopMethod.invoke(null);
		}
		catch (Exception e) {
			System.setSecurityManager(null);

			System.exit(ExitCodes.TEMP_FAILURE);
		}
	}

	public static void start() throws Exception {
		_mainMethod.invoke(null, new Object[] {new String[0]});
	}

	public static Node waitForStarted() throws Exception {
		while (_instanceField.get(null) == null);

		Object bootstrap = _instanceField.get(null);

		AbstractQueuedSynchronizer abstractQueuedSynchronizer =
			(AbstractQueuedSynchronizer)_syncField.get(
				_keepAliveLatchField.get(bootstrap));

		while (!abstractQueuedSynchronizer.hasContended());

		return (Node)_nodeField.get(bootstrap);
	}

	private static final Field _clusterFormationFailureHelperField;
	private static final Field _instanceField;
	private static final Field _keepAliveLatchField;
	private static final Method _mainMethod;
	private static final Field _nodeField;
	private static final Method _stopMethod;
	private static final Field _syncField;

	static {
		try {
			_syncField = ReflectionUtil.getDeclaredField(
				CountDownLatch.class, "sync");

			_clusterFormationFailureHelperField =
				ReflectionUtil.getDeclaredField(
					Coordinator.class, "clusterFormationFailureHelper");

			Thread currentThread = Thread.currentThread();

			ClassLoader classLoader = currentThread.getContextClassLoader();

			_mainMethod = ReflectionUtil.getDeclaredMethod(
				classLoader.loadClass(
					"org.elasticsearch.bootstrap.Elasticsearch"),
				"main", String[].class);

			Class<?> bootstrapClass = classLoader.loadClass(
				"org.elasticsearch.bootstrap.Bootstrap");

			_instanceField = ReflectionUtil.getDeclaredField(
				bootstrapClass, "INSTANCE");

			_keepAliveLatchField = ReflectionUtil.getDeclaredField(
				bootstrapClass, "keepAliveLatch");

			_nodeField = ReflectionUtil.getDeclaredField(
				bootstrapClass, "node");

			_stopMethod = ReflectionUtil.getDeclaredMethod(
				bootstrapClass, "stop");
		}
		catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

}