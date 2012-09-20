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

package com.liferay.portal.kernel.scheduler.config;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletClassLoaderUtil;
import com.liferay.portal.kernel.scheduler.SchedulerEngineUtil;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PortletConstants;
import com.liferay.portal.util.PortalUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shuyang Zhou
 * @author Tina Tian
 */
public class SchedulingConfigurator {

	public void afterPropertiesSet() {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		try {
			ClassLoader portalClassLoader =
				PortalClassLoaderUtil.getClassLoader();

			currentThread.setContextClassLoader(portalClassLoader);

			for (SchedulerEntry schedulerEntry : _schedulerEntries) {
				try {
					SchedulerEngineUtil.schedule(
						schedulerEntry, _storageType, _portletName,
						_exceptionsMaxSize);
				}
				catch (Exception e) {
					_log.error("Unable to schedule " + schedulerEntry, e);
				}
			}
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	public void destroy() {
		for (SchedulerEntry schedulerEntry : _schedulerEntries) {
			try {
				SchedulerEngineUtil.delete(schedulerEntry, _storageType);
			}
			catch (Exception e) {
				_log.error("Unable to unschedule " + schedulerEntry, e);
			}
		}

		_schedulerEntries.clear();
	}

	public void setExceptionsMaxSize(int exceptionsMaxSize) {
		_exceptionsMaxSize = exceptionsMaxSize;
	}

	public void setPortletName(String portletName) {
		if (portletName != null) {
			String servletContextName =
				PortletClassLoaderUtil.getServletContextName();

			if (Validator.isNotNull(servletContextName)) {
				portletName =
					portletName.concat(PortletConstants.WAR_SEPARATOR).concat(
						servletContextName);
			}

			portletName = PortalUtil.getJsSafePortletId(portletName);
		}

		_portletName = portletName;
	}

	public void setSchedulerEntries(List<SchedulerEntry> schedulerEntries) {
		_schedulerEntries = schedulerEntries;
	}

	public void setStorageType(StorageType storageType) {
		_storageType = storageType;
	}

	private static Log _log = LogFactoryUtil.getLog(
		SchedulingConfigurator.class);

	private int _exceptionsMaxSize = 0;
	private String _portletName;
	private List<SchedulerEntry> _schedulerEntries =
		new ArrayList<SchedulerEntry>();
	private StorageType _storageType = StorageType.MEMORY_CLUSTERED;

}