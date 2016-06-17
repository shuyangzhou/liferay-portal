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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.List;

/**
 * @author Tina Tian
 * @author Raymond Augé
 */
public class ClusterExecutorUtil {

	public static void addClusterEventListener(
		ClusterEventListener clusterEventListener) {

		getClusterExecutor().addClusterEventListener(clusterEventListener);
	}

	public static FutureClusterResponses execute(
		ClusterRequest clusterRequest) {

		return getClusterExecutor().execute(clusterRequest);
	}

	public static ClusterExecutor getClusterExecutor() {
		PortalRuntimePermission.checkGetBeanProperty(ClusterExecutorUtil.class);

		return _clusterExecutor;
	}

	public static List<ClusterNode> getClusterNodes() {
		return getClusterExecutor().getClusterNodes();
	}

	public static ClusterNode getLocalClusterNode() {
		return getClusterExecutor().getLocalClusterNode();
	}

	public static boolean isClusterNodeAlive(String clusterNodeId) {
		return getClusterExecutor().isClusterNodeAlive(clusterNodeId);
	}

	public static boolean isEnabled() {
		return getClusterExecutor().isEnabled();
	}

	public static void removeClusterEventListener(
		ClusterEventListener clusterEventListener) {

		getClusterExecutor().removeClusterEventListener(clusterEventListener);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterExecutorUtil.class);

	private static final ClusterExecutor _clusterExecutor =
		ProxyFactory.newServiceTrackedInstance(ClusterExecutor.class);

}