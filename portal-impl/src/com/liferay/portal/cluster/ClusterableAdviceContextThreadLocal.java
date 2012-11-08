/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.util.CentralizedThreadLocal;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Tina Tian
 */
public class ClusterableAdviceContextThreadLocal {

	public static Map<String, Serializable> getClusterableAdviceContext() {
		return _contextThreadLocal.get();
	}

	public static void setClusterableAdviceContext(
		Map<String, Serializable> clusterableAdviceContext) {

		_contextThreadLocal.set(clusterableAdviceContext);
	}

	private static ThreadLocal<Map<String, Serializable>> _contextThreadLocal =
		new CentralizedThreadLocal<Map<String, Serializable>>(false);

}