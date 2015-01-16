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

import java.util.Collections;
import java.util.Set;

/**
 * @author Tina Tian
 */
public class ClusterManagerUtil {

	public static void destroy() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return;
		}

		clusterManager.destroy();
	}

	public static ClusterNode getClusterNode(String clusterNodeId) {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return null;
		}

		return clusterManager.getClusterNode(clusterNodeId);
	}

	public static Set<String> getClusterNodeIds() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return Collections.emptySet();
		}

		return clusterManager.getClusterNodeIds();
	}

	public static Set<ClusterNode> getClusterNodes() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return Collections.emptySet();
		}

		return clusterManager.getClusterNodes();
	}

	public static ClusterNode getLocalClusterNode() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return null;
		}

		return clusterManager.getLocalClusterNode();
	}

	public static String getLocalClusterNodeId() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return null;
		}

		return clusterManager.getLocalClusterNodeId();
	}

	public static void initialize() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return;
		}

		clusterManager.initialize();
	}

	public static boolean isEnabled() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return false;
		}

		return true;
	}

	public static boolean isShortcutLocalMethod() {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return false;
		}

		return clusterManager.isShortcutLocalMethod();
	}

	public static void registerClusterEventListener(
		ClusterEventListener clusterEventListener) {

		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return;
		}

		clusterManager.registerClusterEventListener(clusterEventListener);
	}

	public static FutureClusterResponses send(ClusterMessage clusterMessage) {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return null;
		}

		return clusterManager.send(clusterMessage);
	}

	public static FutureClusterResponses send(
		ClusterMessage clusterMessage,
		ClusterResponseCallback clusterResponseCallback) {

		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return null;
		}

		return clusterManager.send(clusterMessage, clusterResponseCallback);
	}

	public static void sendAndForget(ClusterMessage clusterMessage) {
		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return;
		}

		clusterManager.sendAndForget(clusterMessage);
	}

	public static void unregisterClusterEventListener(
		ClusterEventListener clusterEventListener) {

		ClusterManager clusterManager = getClusterManager();

		if (clusterManager == null) {
			return;
		}

		clusterManager.unregisterClusterEventListener(clusterEventListener);
	}

	public void setClusterManager(ClusterManager clusterManager) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_clusterManager = clusterManager;
	}

	protected static ClusterManager getClusterManager() {
		PortalRuntimePermission.checkGetBeanProperty(ClusterManagerUtil.class);

		if ((_clusterManager == null) || !_clusterManager.isEnabled()) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"ClusterManagerUtil is either not initialized or disabled");
			}

			return null;
		}

		return _clusterManager;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterManagerUtil.class);

	private static ClusterManager _clusterManager;

}