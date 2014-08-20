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
public class ThreadDumpResult implements Serializable {

	public ThreadDumpResult(String threadDump) {
		if (ClusterExecutorUtil.isEnabled()) {
			Address localClusterNodeAddress =
				ClusterExecutorUtil.getLocalClusterNodeAddress();

			_hostName = localClusterNodeAddress.getDescription();
		}

		if (Validator.isNull(_hostName)) {
			_hostName = PortalUtil.getComputerName();
		}

		_createDate = new Date();
		_threadDump = threadDump;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public String getHostName() {
		return _hostName;
	}

	public String getThreadDump() {
		return _threadDump;
	}

	private Date _createDate;
	private String _hostName;
	private String _threadDump;

}