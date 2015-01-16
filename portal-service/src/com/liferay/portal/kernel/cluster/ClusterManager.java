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

import java.util.Set;

/**
 * @author Tina Tian
 */
public interface ClusterManager {

	public static final String CLUSTER_FORWARD_MESSAGE =
		"CLUSTER_FORWARD_MESSAGE";

	public void destroy();

	public ClusterNode getClusterNode(String clusterNodeId);

	public Set<String> getClusterNodeIds();

	public Set<ClusterNode> getClusterNodes();

	public ClusterNode getLocalClusterNode();

	public String getLocalClusterNodeId();

	public void initialize();

	public boolean isEnabled();

	public boolean isShortcutLocalMethod();

	public void registerClusterEventListener(
		ClusterEventListener clusterEventListener);

	public FutureClusterResponses send(ClusterMessage clusterMessage);

	public FutureClusterResponses send(
		ClusterMessage clusterMessage,
		ClusterResponseCallback clusterResponseCallback);

	public void sendAndForget(ClusterMessage clusterMessage);

	public void unregisterClusterEventListener(
		ClusterEventListener clusterEventListener);

}