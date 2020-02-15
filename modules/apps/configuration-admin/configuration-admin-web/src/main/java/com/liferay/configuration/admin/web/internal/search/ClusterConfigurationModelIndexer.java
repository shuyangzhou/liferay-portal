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

package com.liferay.configuration.admin.web.internal.search;

import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.cluster.ClusterMasterTokenTransitionListener;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiServiceUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Tina Tian
 */
@Component(
	immediate = true,
	service = {
		ClusterConfigurationModelIndexer.class, IdentifiableOSGiService.class
	}
)
public class ClusterConfigurationModelIndexer
	implements IdentifiableOSGiService {

	@Override
	public String getOSGiServiceIdentifier() {
		return ClusterConfigurationModelIndexer.class.getName();
	}

	public void start() throws Exception {
		if (!_started) {
			_start();
		}
	}

	public void stop() {
		if (_started) {
			_stop();
		}
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		_componentContext = componentContext;

		if (_clusterExecutor.isEnabled()) {
			_configurationModelsClusterMasterTokenTransitionListener =
				new ConfigurationModelsClusterMasterTokenTransitionListener();

			_clusterMasterExecutor.addClusterMasterTokenTransitionListener(
				_configurationModelsClusterMasterTokenTransitionListener);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_configurationModelsClusterMasterTokenTransitionListener != null) {
			_clusterMasterExecutor.removeClusterMasterTokenTransitionListener(
				_configurationModelsClusterMasterTokenTransitionListener);
		}
	}

	private static void _reset(String osgiServiceIdentifier) {
		ClusterConfigurationModelIndexer clusterConfigurationModelIndexer =
			(ClusterConfigurationModelIndexer)
				IdentifiableOSGiServiceUtil.getIdentifiableOSGiService(
					osgiServiceIdentifier);

		clusterConfigurationModelIndexer._reset();
	}

	private static void _start(String osgiServiceIdentifier) throws Exception {
		ClusterConfigurationModelIndexer clusterConfigurationModelIndexer =
			(ClusterConfigurationModelIndexer)
				IdentifiableOSGiServiceUtil.getIdentifiableOSGiService(
					osgiServiceIdentifier);

		clusterConfigurationModelIndexer.start();
	}

	private synchronized void _reset() {
		_started = false;
	}

	private synchronized void _start() throws Exception {
		if (_started) {
			return;
		}

		if (_clusterMasterExecutor.isMaster()) {
			_componentContext.enableComponent(
				ConfigurationModelIndexer.class.getName());
		}
		else {
			NoticeableFuture<Void> noticeableFuture =
				_clusterMasterExecutor.executeOnMaster(
					new MethodHandler(
						_startMethodKey, getOSGiServiceIdentifier()));

			noticeableFuture.get();
		}

		_started = true;
	}

	private synchronized void _stop() {
		if (_started) {
			_componentContext.disableComponent(
				ConfigurationModelIndexer.class.getName());

			_started = false;
		}
	}

	private static final MethodKey _resetMethodKey = new MethodKey(
		ClusterConfigurationModelIndexer.class, "_reset", String.class);
	private static final MethodKey _startMethodKey = new MethodKey(
		ClusterConfigurationModelIndexer.class, "_start", String.class);

	@Reference
	private ClusterExecutor _clusterExecutor;

	@Reference
	private ClusterMasterExecutor _clusterMasterExecutor;

	private ComponentContext _componentContext;
	private ConfigurationModelsClusterMasterTokenTransitionListener
		_configurationModelsClusterMasterTokenTransitionListener;
	private volatile boolean _started;

	private class ConfigurationModelsClusterMasterTokenTransitionListener
		implements ClusterMasterTokenTransitionListener {

		@Override
		public void masterTokenAcquired() {
			_reset();

			ClusterRequest clusterRequest =
				ClusterRequest.createMulticastRequest(
					new MethodHandler(
						_resetMethodKey, getOSGiServiceIdentifier()),
					true);

			clusterRequest.setFireAndForget(true);

			_clusterExecutor.execute(clusterRequest);
		}

		@Override
		public void masterTokenReleased() {
			_stop();
		}

	}

}