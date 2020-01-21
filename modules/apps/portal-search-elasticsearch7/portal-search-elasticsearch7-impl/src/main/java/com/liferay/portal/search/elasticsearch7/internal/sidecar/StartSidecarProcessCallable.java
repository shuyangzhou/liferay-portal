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

import java.io.IOException;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.elasticsearch.node.Node;

/**
 * @author Tina Tian
 */
public class StartSidecarProcessCallable
	implements ProcessCallable<Serializable> {

	public StartSidecarProcessCallable(
		String[] arguments, long heartbeatInterval, boolean clustered) {

		_arguments = arguments;
		_heartbeatInterval = heartbeatInterval;
		_clustered = clustered;
	}

	@Override
	public Serializable call() throws ProcessException {
		LocalProcessLauncher.ProcessContext.attach(
			"StartSidecarProcessCallable", _heartbeatInterval,
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
			Node node = ElasticsearchServerUtil.start(_arguments);

			if (_clustered) {
				Thread thread = new Thread(
					() -> {
						try {
							ElasticsearchServerUtil.monitorClusterStatus(
								node, _heartbeatInterval);
						}
						catch (Exception e) {
							if (_logger.isWarnEnabled()) {
								_logger.warn(
									"Unable to monitor cluster status", e);
							}

							ElasticsearchServerUtil.shutdown();
						}
					},
					"Elasticsearch Server Cluster Status Monitor");

				thread.setDaemon(true);

				thread.start();
			}
		}
		catch (Exception e) {
			throw new ProcessException(
				"Unable to start Elasticsearch server", e);
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
		}

		try {
			ElasticsearchServerUtil.addShutdownHook();
		}
		catch (Exception e) {
			if (_logger.isWarnEnabled()) {
				_logger.warn("Unable to add shutdown hook", e);
			}
		}

		return null;
	}

	private static final Logger _logger = LogManager.getLogger(
		StartSidecarProcessCallable.class);

	private static final long serialVersionUID = 1L;

	private final String[] _arguments;
	private final boolean _clustered;
	private final long _heartbeatInterval;

}