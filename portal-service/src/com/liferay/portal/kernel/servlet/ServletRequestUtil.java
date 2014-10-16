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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Brian Wing Shun Chan
 * @author László Csontos
 */
public class ServletRequestUtil {

	public static void logRequestWrappers(HttpServletRequest request) {
		HttpServletRequest tempRequest = request;

		while (true) {
			if (_log.isInfoEnabled()) {
				_log.info("Request class " + tempRequest.getClass().getName());
			}

			if (tempRequest instanceof HttpServletRequestWrapper) {
				HttpServletRequestWrapper requestWrapper =
					(HttpServletRequestWrapper)tempRequest;

				tempRequest = (HttpServletRequest)requestWrapper.getRequest();
			}
			else {
				break;
			}
		}
	}

	public static Object setAttributeIfAbsent(
		HttpServletRequest request, String name, Object newValue) {

		ReadWriteLock attributeLock = (ReadWriteLock)request.getAttribute(
			WebKeys.PARALLEL_RENDERING_ATTRIBUTE_LOCK);

		Lock readLock = null;
		Lock writeLock = null;

		if (attributeLock != null) {
			readLock = attributeLock.readLock();
			writeLock = attributeLock.writeLock();
		}

		if (readLock != null) {
			readLock.lock();
		}

		try {
			Object value = request.getAttribute(name);

			if (value != null) {
				return value;
			}
		}
		finally {
			if (readLock != null) {
				readLock.unlock();
			}
		}

		if (writeLock != null) {
			writeLock.lock();
		}

		try {
			Object value = request.getAttribute(name);

			if (value != null) {
				return value;
			}

			request.setAttribute(name, newValue);
		}
		finally {
			if (writeLock != null) {
				writeLock.unlock();
			}
		}

		return newValue;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ServletRequestUtil.class);

}