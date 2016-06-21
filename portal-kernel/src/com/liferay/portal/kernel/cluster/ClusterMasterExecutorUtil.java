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

package com.liferay.portal.kernel.cluster;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.concurrent.Future;

/**
 * @author Michael C. Han
 */
@ProviderType
public class ClusterMasterExecutorUtil {

	public static void addClusterMasterTokenTransitionListener(
		ClusterMasterTokenTransitionListener
			clusterMasterTokenTransitionListener) {

		_getClusterMasterExecutor().addClusterMasterTokenTransitionListener(
			clusterMasterTokenTransitionListener);
	}

	public static <T> Future<T> executeOnMaster(MethodHandler methodHandler) {
		return _getClusterMasterExecutor().executeOnMaster(methodHandler);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #_getClusterMasterExecutor()}
	 */
	@Deprecated
	public static ClusterMasterExecutor getClusterMasterExecutor() {
		return _getClusterMasterExecutor();
	}

	public static boolean isEnabled() {
		return _getClusterMasterExecutor().isEnabled();
	}

	public static boolean isMaster() {
		return _getClusterMasterExecutor().isMaster();
	}

	public static void removeClusterMasterTokenTransitionListener(
		ClusterMasterTokenTransitionListener
			clusterMasterTokenTransitionListener) {

		_getClusterMasterExecutor().removeClusterMasterTokenTransitionListener(
			clusterMasterTokenTransitionListener);
	}

	private static ClusterMasterExecutor _getClusterMasterExecutor() {
		return _clusterMasterExecutor;
	}

	private static final ClusterMasterExecutor _clusterMasterExecutor =
		ProxyFactory.newServiceTrackedInstance(ClusterMasterExecutor.class);

}