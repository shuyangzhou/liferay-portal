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

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cluster.ClusterExecutor;
import com.liferay.portal.kernel.cluster.ClusterNode;

import java.io.File;

import java.net.InetAddress;

import java.util.List;

/**
 * @author Tina Tian
 */
public class SidecarConfig {

	public SidecarConfig(
		File homeFolder, long heartbeatInterval, String[] jvmOptions,
		ClusterExecutor clusterExecutor) {

		_homeFolder = homeFolder;
		_heartbeatInterval = heartbeatInterval;
		_jvmOptions = jvmOptions;

		if (!clusterExecutor.isEnabled()) {
			_localNodeName = null;
			_localHostAddress = null;
			_initialMasterNodes = null;
			_discoverySeedHosts = null;
		}
		else {
			ClusterNode localClusterNode =
				clusterExecutor.getLocalClusterNode();

			_localNodeName = _generateNodeName(localClusterNode);
			_localHostAddress = _getHostAddress(localClusterNode);

			List<ClusterNode> clusterNodes = clusterExecutor.getClusterNodes();

			StringBundler initialMasterNodesSB = new StringBundler(
				2 * clusterNodes.size() - 1);
			StringBundler discoverySeedHostsSB = new StringBundler(
				2 * clusterNodes.size() - 1);

			for (ClusterNode clusterNode : clusterNodes) {
				initialMasterNodesSB.append(_generateNodeName(clusterNode));
				initialMasterNodesSB.append(StringPool.COMMA);

				discoverySeedHostsSB.append(_getHostAddress(clusterNode));
				discoverySeedHostsSB.append(StringPool.COMMA);
			}

			initialMasterNodesSB.setIndex(initialMasterNodesSB.index() - 1);
			discoverySeedHostsSB.setIndex(discoverySeedHostsSB.index() - 1);

			_initialMasterNodes = initialMasterNodesSB.toString();
			_discoverySeedHosts = discoverySeedHostsSB.toString();
		}
	}

	public String getDiscoverySeedHosts() {
		return _discoverySeedHosts;
	}

	public long getHeartbeatInterval() {
		return _heartbeatInterval;
	}

	public File getHomeFolder() {
		return _homeFolder;
	}

	public String getInitialMasterNodes() {
		return _initialMasterNodes;
	}

	public String[] getJVMOptions() {
		return _jvmOptions;
	}

	public String getLocalHostAddress() {
		return _localHostAddress;
	}

	public String getLocalNodeName() {
		return _localNodeName;
	}

	private String _generateNodeName(ClusterNode clusterNode) {
		return _NODE_NAME_PREFIX.concat(clusterNode.getClusterNodeId());
	}

	private String _getHostAddress(ClusterNode clusterNode) {
		InetAddress inetAddress = clusterNode.getBindInetAddress();

		return inetAddress.getHostAddress();
	}

	private static final String _NODE_NAME_PREFIX = "NODE_";

	private final String _discoverySeedHosts;
	private final long _heartbeatInterval;
	private final File _homeFolder;
	private final String _initialMasterNodes;
	private final String[] _jvmOptions;
	private final String _localHostAddress;
	private final String _localNodeName;

}