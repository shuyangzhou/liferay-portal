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

package com.liferay.portal.cluster;

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.util.HashUtil;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tina Tian
 */
public class ClusterNodeStatus implements Serializable {

	public ClusterNodeStatus(ClusterNode clusterNode) {
		if (clusterNode == null) {
			throw new NullPointerException("Cluster node is null");
		}

		_clusterNode = clusterNode;
	}

	public void addChannelAddress(String channelName, Address address) {
		_channelAddresses.put(channelName, address);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ClusterNodeStatus)) {
			return false;
		}

		ClusterNodeStatus clusterNodeStatus = (ClusterNodeStatus)obj;

		if ( _clusterNode.equals(clusterNodeStatus._clusterNode)&&
			_channelAddresses.equals(clusterNodeStatus._channelAddresses)) {

			return true;
		}

		return false;
	}

	public Address getChannelAddress(String channelName) {
		return _channelAddresses.get(channelName);
	}

	public ClusterNode getClusterNode() {
		return _clusterNode;
	}

	public String getClusterNodeId() {
		return _clusterNode.getClusterNodeId();
	}

	@Override
	public int hashCode() {
		int hash = HashUtil.hash(0, _clusterNode);

		return HashUtil.hash(hash, _channelAddresses);
	}

	public void removeChannelAddress(String clusterName) {
		_channelAddresses.remove(clusterName);
	}

	private final Map<String, Address> _channelAddresses =
		new ConcurrentHashMap<>();
	private final ClusterNode _clusterNode;

}