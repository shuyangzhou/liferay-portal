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
import com.liferay.petra.process.ProcessExecutor;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.File;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.connection.ElasticsearchConnection;
import com.liferay.portal.search.elasticsearch7.internal.connection.OperationMode;
import com.liferay.portal.search.elasticsearch7.internal.connection.SidecarElasticsearchConnection;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.Future;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConfiguration",
	immediate = true, service = {}
)
public class SidecarManager {

	@Activate
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		ElasticsearchConfiguration elasticsearchConfiguration =
			ConfigurableUtil.createConfigurable(
				ElasticsearchConfiguration.class, properties);

		_restartFutureListener = new RestartFutureListener(
			bundleContext, properties,
			elasticsearchConfiguration.sidecarHeartbeatInterval());

		_serviceRegistration = bundleContext.registerService(
			ElasticsearchConnection.class,
			new SidecarElasticsearchConnection(
				new Sidecar(
					_clusterExecutor, _processExecutor, _file, _props,
					elasticsearchConfiguration, _restartFutureListener)),
			MapUtil.singletonDictionary(
				"operation.mode", String.valueOf(OperationMode.EMBEDDED)));
	}

	@Deactivate
	protected void deactivate() {
		synchronized (_restartFutureListener) {
			_serviceRegistration.unregister();

			_serviceRegistration = null;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(SidecarManager.class);

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private File _file;

	@Reference
	private ProcessExecutor _processExecutor;

	@Reference
	private Props _props;

	private RestartFutureListener _restartFutureListener;
	private ServiceRegistration<ElasticsearchConnection> _serviceRegistration;

	private class RestartFutureListener
		implements FutureListener<Serializable> {

		@Override
		public void complete(Future<Serializable> future) {
			try {
				future.get();
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug("Sidecar process is aborted", e);
				}
			}

			synchronized (this) {
				if (_serviceRegistration == null) {
					return;
				}

				_serviceRegistration.unregister();

				if (_log.isInfoEnabled()) {
					_log.info(
						"Sidecar process exited, will restart in " +
							_restartInterval + " milliseconds if needed");
				}

				try {
					Thread.sleep(_restartInterval);
				}
				catch (InterruptedException ie) {
					throw new RuntimeException(
						"Unable to wait for " + _restartInterval +
							" milliseconds to restart sidecar process",
						ie);
				}

				activate(_bundleContext, _properties);
			}
		}

		private RestartFutureListener(
			BundleContext bundleContext, Map<String, Object> properties,
			long restartInterval) {

			_bundleContext = bundleContext;
			_properties = properties;
			_restartInterval = restartInterval;
		}

		private final BundleContext _bundleContext;
		private final Map<String, Object> _properties;
		private final long _restartInterval;

	}

}