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
import com.liferay.petra.reflect.ReflectionUtil;

import java.lang.reflect.Field;

import org.elasticsearch.cluster.coordination.ClusterFormationFailureHelper;
import org.elasticsearch.cluster.coordination.Coordinator;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.transport.BoundTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.http.HttpServerTransport;
import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class SidecarStatusProcessCallable implements ProcessCallable<String> {

	public SidecarStatusProcessCallable() {
		this(0);
	}

	public SidecarStatusProcessCallable(long checkInterval) {
		_checkInterval = checkInterval;
	}

	@Override
	public String call() throws ProcessException {
		try {
			Node node = _waitForNodeStarted();

			Injector injector = node.injector();

			if (_checkInterval > 0) {
				_monitorClusterStatus(injector);
			}

			HttpServerTransport httpServerTransport = injector.getInstance(
				HttpServerTransport.class);

			BoundTransportAddress boundTransportAddress =
				httpServerTransport.boundAddress();

			TransportAddress publishAddress =
				boundTransportAddress.publishAddress();

			return publishAddress.toString();
		}
		catch (Exception e) {
			throw new ProcessException("Unable to get published address ", e);
		}
	}

	private void _monitorClusterStatus(Injector injector) {
		Thread thread = new Thread(
			() -> {
				try {
					Coordinator coordinator = (Coordinator)injector.getInstance(
						Discovery.class);

					Field clusterFormationFailureHelperField =
						ReflectionUtil.getDeclaredField(
							coordinator.getClass(),
							"clusterFormationFailureHelper");

					ClusterFormationFailureHelper
						clusterFormationFailureHelper =
							(ClusterFormationFailureHelper)
								clusterFormationFailureHelperField.get(
									coordinator);

					while (true) {
						if (clusterFormationFailureHelper.isRunning() == true) {
							System.setSecurityManager(null);

							System.exit(0);

							return;
						}

						Thread.sleep(_checkInterval);
					}
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			},
			"Sidecar Cluster Status Monitor");

		thread.setDaemon(true);

		thread.start();
	}

	private Node _waitForNodeStarted() throws Exception {
		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		Class<?> bootstrapClass = classLoader.loadClass(
			"org.elasticsearch.bootstrap.Bootstrap");

		Field instanceField = ReflectionUtil.getDeclaredField(
			bootstrapClass, "INSTANCE");

		while (instanceField.get(null) == null);

		Object bootstrap = instanceField.get(null);

		Field keepAliveThreadField = ReflectionUtil.getDeclaredField(
			bootstrapClass, "keepAliveThread");

		Thread thread = (Thread)keepAliveThreadField.get(bootstrap);

		while (Thread.State.WAITING != thread.getState());

		Field nodeField = ReflectionUtil.getDeclaredField(
			bootstrapClass, "node");

		return (Node)nodeField.get(bootstrap);
	}

	private static final long serialVersionUID = 1L;

	private final long _checkInterval;

}