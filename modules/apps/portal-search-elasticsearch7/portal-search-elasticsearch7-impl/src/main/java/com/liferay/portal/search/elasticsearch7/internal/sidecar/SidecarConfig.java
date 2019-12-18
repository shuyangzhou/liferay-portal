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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tina Tian
 */
public class SidecarConfig {

	public SidecarConfig(File homeFolder, ClusterExecutor clusterExecutor) {
		_homeFolder = homeFolder;

		_configFolder = new File(homeFolder, "config");
		_libFolder = new File(homeFolder, "lib");

		ClusterNode localClusterNode = clusterExecutor.getLocalClusterNode();

		if (localClusterNode == null) {
			_localNodeName = null;
			_localHostAddress = null;
		}
		else {
			_localNodeName = _generateNodeName(localClusterNode);

			InetAddress inetAddress = localClusterNode.getBindInetAddress();

			_localHostAddress = inetAddress.getHostAddress();
		}

		List<ClusterNode> clusterNodes = clusterExecutor.getClusterNodes();

		if ((clusterNodes == null) || clusterNodes.isEmpty()) {
			_initialMasterNodes = null;
			_discoverySeedHosts = null;
		}
		else {
			Set<String> nodeNames = new HashSet<>();
			Set<String> hostAddresses = new HashSet<>();

			for (ClusterNode clusterNode : clusterNodes) {
				nodeNames.add(_generateNodeName(clusterNode));

				InetAddress inetAddress = clusterNode.getBindInetAddress();

				hostAddresses.add(inetAddress.getHostAddress());
			}

			_initialMasterNodes = _generateConfigString(nodeNames);
			_discoverySeedHosts = _generateConfigString(hostAddresses);
		}
	}

	public File getConfigFolder() {
		return _configFolder;
	}

	public String getDiscoverySeedHosts() {
		return _discoverySeedHosts;
	}

	public File getHomeFolder() {
		return _homeFolder;
	}

	public String getInitialMasterNodes() {
		return _initialMasterNodes;
	}

	public File getLibFolder() {
		return _libFolder;
	}

	public String getLocalHostAddress() {
		return _localHostAddress;
	}

	public String getLocalNodeName() {
		return _localNodeName;
	}

	private String _generateConfigString(Set<String> values) {
		StringBundler sb = new StringBundler(2 * values.size() - 1);

		for (String value : values) {
			sb.append(value);
			sb.append(StringPool.COMMA);
		}

		sb.setIndex(sb.index() - 1);

		return sb.toString();
	}

	private String _generateNodeName(ClusterNode clusterNode) {
		return _NODE_NAME_PREFIX.concat(clusterNode.getClusterNodeId());
	}

	private static final String _NODE_NAME_PREFIX = "NODE_";

	private final File _configFolder;
	private final String _discoverySeedHosts;
	private final File _homeFolder;
	private final String _initialMasterNodes;
	private final File _libFolder;
	private final String _localHostAddress;
	private final String _localNodeName;

}