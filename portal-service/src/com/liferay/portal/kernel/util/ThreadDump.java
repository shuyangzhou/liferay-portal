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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.util.PortalUtil;

import java.io.Serializable;

import java.util.Date;

/**
 * @author László Csontos
 */
public class ThreadDump implements Serializable {

	public ThreadDump(String threadDump) {
		if (ClusterExecutorUtil.isEnabled()) {
			Address localClusterNodeAddress =
				ClusterExecutorUtil.getLocalClusterNodeAddress();

			_targetHost = localClusterNodeAddress.getDescription();
		}

		if (Validator.isNull(_targetHost)) {
			_targetHost = PortalUtil.getComputerName();
		}

		_takenAt = new Date();
		_threadDump = threadDump;
	}

	public Date getTakenAt() {
		return _takenAt;
	}

	public String getTargetHost() {
		return _targetHost;
	}

	public String getThreadDump() {
		return _threadDump;
	}

	private Date _takenAt;
	private String _targetHost;
	private String _threadDump;

}