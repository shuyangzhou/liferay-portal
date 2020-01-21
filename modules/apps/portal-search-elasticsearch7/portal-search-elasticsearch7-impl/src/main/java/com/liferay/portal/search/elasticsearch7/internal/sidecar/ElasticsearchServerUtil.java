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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.cluster.coordination.ClusterFormationFailureHelper;
import org.elasticsearch.cluster.coordination.Coordinator;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class ElasticsearchServerUtil {

	public static void addShutdownHook() throws Exception {
		synchronized (_hooksField.getDeclaringClass()) {
			Map<Thread, Thread> hooks = (Map<Thread, Thread>)_hooksField.get(
				null);

			Set<Thread> threads = new HashSet<>(hooks.keySet());

			hooks.clear();

			Thread shutdownHook = new Thread(
				() -> {
					try {
						_shutdownCountDownLatch.await();
					}
					catch (InterruptedException ie) {
					}

					for (Thread thread : threads) {
						thread.start();
					}

					for (Thread thread : threads) {
						while (true) {
							try {
								thread.join();

								break;
							}
							catch (InterruptedException ie) {
							}
						}
					}
				},
				"Elasticsearch Server Shutdown Hook");

			hooks.put(shutdownHook, shutdownHook);
		}
	}

	public static void monitorClusterStatus(Node node, long checkInterval)
		throws Exception {

		Injector injector = node.injector();

		ClusterFormationFailureHelper clusterFormationFailureHelper =
			(ClusterFormationFailureHelper)
				_clusterFormationFailureHelperField.get(
					injector.getInstance(Discovery.class));

		while (!clusterFormationFailureHelper.isRunning()) {
			Thread.sleep(checkInterval);
		}

		shutdown();
	}

	public static void shutdown() {
		try {
			_stopMethod.invoke(null);

			_shutdownCountDownLatch.countDown();
		}
		catch (Exception e) {
			_shutdownCountDownLatch.countDown();

			throw new ElasticsearchException("failed to stop node", e);
		}
	}

	public static Node start(String[] arguments) throws Exception {
		_mainMethod.invoke(null, new Object[] {arguments});

		System.setSecurityManager(null);

		_startCountDownLatch.countDown();

		return (Node)_nodeField.get(_instanceField.get(null));
	}

	public static Node waitForStarted() throws Exception {
		_startCountDownLatch.await();

		return (Node)_nodeField.get(_instanceField.get(null));
	}

	private static final Field _clusterFormationFailureHelperField;
	private static final Field _hooksField;
	private static final Field _instanceField;
	private static final Method _mainMethod;
	private static final Field _nodeField;
	private static final CountDownLatch _shutdownCountDownLatch =
		new CountDownLatch(1);
	private static final CountDownLatch _startCountDownLatch =
		new CountDownLatch(1);
	private static final Method _stopMethod;

	static {
		try {
			_clusterFormationFailureHelperField =
				ReflectionUtil.getDeclaredField(
					Coordinator.class, "clusterFormationFailureHelper");

			Thread currentThread = Thread.currentThread();

			ClassLoader classLoader = currentThread.getContextClassLoader();

			_hooksField = ReflectionUtil.getDeclaredField(
				classLoader.loadClass("java.lang.ApplicationShutdownHooks"),
				"hooks");

			_mainMethod = ReflectionUtil.getDeclaredMethod(
				classLoader.loadClass(
					"org.elasticsearch.bootstrap.Elasticsearch"),
				"main", String[].class);

			Class<?> bootstrapClass = classLoader.loadClass(
				"org.elasticsearch.bootstrap.Bootstrap");

			_instanceField = ReflectionUtil.getDeclaredField(
				bootstrapClass, "INSTANCE");

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