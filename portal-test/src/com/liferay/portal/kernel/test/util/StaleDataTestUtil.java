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

package com.liferay.portal.kernel.test.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StackTraceUtil;

import java.util.List;

/**
 * @author Drew Brokke
 */
public class StaleDataTestUtil {

	public static void expectEmpty(List items, String warningMessage) {
		if (ListUtil.isNotEmpty(items) && _log.isWarnEnabled()) {
			_log.warn(
				"LRQA-44602 Stale data found: " +
					StackTraceUtil.getStackTrace(
						new Exception(warningMessage)));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		StaleDataTestUtil.class);

}