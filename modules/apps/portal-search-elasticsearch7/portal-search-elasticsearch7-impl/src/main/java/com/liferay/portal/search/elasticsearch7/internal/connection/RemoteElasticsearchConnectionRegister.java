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

import com.liferay.petra.concurrent.FutureListener;
import com.liferay.petra.process.ProcessExecutor;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConnectionConfiguration;
import com.liferay.portal.search.elasticsearch7.internal.settings.BaseIndexSettingsContributor;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.Sidecar;
import com.liferay.portal.search.elasticsearch7.internal.sidecar.SidecarConfig;
import com.liferay.portal.search.elasticsearch7.settings.IndexSettingsContributor;
import com.liferay.portal.search.elasticsearch7.settings.IndexSettingsHelper;
import com.liferay.portal.util.PropsValues;

import java.io.File;
import java.io.Serializable;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Future;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(
	configurationPid = "com.liferay.portal.search.elasticsearch7.configuration.ElasticsearchConnectionConfiguration",
	immediate = true, service = {}
)
public class RemoteElasticsearchConnectionRegister {

	@Activate
	@Modified
	protected synchronized void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		ElasticsearchConnectionConfiguration
			elasticsearchConnectionConfiguration =
				ConfigurableUtil.createConfigurable(
					ElasticsearchConnectionConfiguration.class, properties);

		Sidecar sidecar = null;

		if (elasticsearchConnectionConfiguration.active() &&
			Arrays.equals(
				elasticsearchConnectionConfiguration.networkHostAddresses(),
				new String[] {"http://localhost:9200"})) {

			String sidecarHome =
				elasticsearchConnectionConfiguration.sidecarHome();

			File sidecarHomeFolder = new File(
				PropsValues.LIFERAY_HOME, sidecarHome);

			if (!sidecarHomeFolder.exists()) {
				sidecarHomeFolder = new File(sidecarHome);

				if (!sidecarHomeFolder.exists()) {
					throw new IllegalStateException(
						"Sidecar home " + sidecarHome + " does not exist");
				}
			}

			sidecar = new Sidecar(
				_processExecutor,
				new RestartFutureListener(
					bundleContext, properties,
					elasticsearchConnectionConfiguration.
						sidecarHeartbeatInterval()),
				new SidecarConfig(
					sidecarHomeFolder,
					elasticsearchConnectionConfiguration.
						sidecarHeartbeatInterval(),
					elasticsearchConnectionConfiguration.sidecarJVMOptions(),
					_clusterExecutor));

			if (_clusterExecutor.isEnabled()) {
				bundleContext.registerService(
					IndexSettingsContributor.class,
					new BaseIndexSettingsContributor(Integer.MAX_VALUE) {

						@Override
						public void populate(
							IndexSettingsHelper indexSettingsHelper) {

							indexSettingsHelper.put(
								"index.auto_expand_replicas", "0-all");
						}

					},
					null);
			}
		}

		_serviceRegistration = bundleContext.registerService(
			ElasticsearchConnection.class,
			new RemoteElasticsearchConnection(
				sidecar, elasticsearchConnectionConfiguration),
			MapUtil.singletonDictionary("operation.mode", "REMOTE"));
	}

	@Deactivate
	protected synchronized void deactivate() {
		if (_serviceRegistration != null) {
			_serviceRegistration.unregister();
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RemoteElasticsearchConnectionRegister.class);

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private ProcessExecutor _processExecutor;

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

			deactivate();

			if (_log.isInfoEnabled()) {
				_log.info(
					"Sidecar process exited, will restart in " +
						_restartInterval + " milliseconds");
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