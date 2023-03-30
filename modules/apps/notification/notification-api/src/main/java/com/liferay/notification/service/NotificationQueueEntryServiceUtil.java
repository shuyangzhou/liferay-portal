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

package com.liferay.notification.service;

import com.liferay.notification.model.NotificationQueueEntry;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for NotificationQueueEntry. This utility wraps
 * <code>com.liferay.notification.service.impl.NotificationQueueEntryServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Gabriel Albuquerque
 * @see NotificationQueueEntryService
 * @generated
 */
public class NotificationQueueEntryServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.notification.service.impl.NotificationQueueEntryServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static NotificationQueueEntry deleteNotificationQueueEntry(
			long notificationQueueEntryId)
		throws PortalException {

		return getService().deleteNotificationQueueEntry(
			notificationQueueEntryId);
	}

	public static NotificationQueueEntry getNotificationQueueEntry(
			long notificationQueueEntryId)
		throws PortalException {

		return getService().getNotificationQueueEntry(notificationQueueEntryId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static NotificationQueueEntry resendNotificationQueueEntry(
			long notificationQueueEntryId)
		throws PortalException {

		return getService().resendNotificationQueueEntry(
			notificationQueueEntryId);
	}

	public static NotificationQueueEntryService getService() {
		return _serviceDCLSingleton.getSingleton(
			NotificationQueueEntryServiceUtil::_getService);
	}

	private static NotificationQueueEntryService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			NotificationQueueEntryServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<NotificationQueueEntryService> serviceReference =
			bundleContext.getServiceReference(
				NotificationQueueEntryService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<NotificationQueueEntryService>
		_serviceDCLSingleton = new DCLSingleton<>();

}