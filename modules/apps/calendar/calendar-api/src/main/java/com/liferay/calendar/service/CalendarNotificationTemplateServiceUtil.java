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

package com.liferay.calendar.service;

import com.liferay.calendar.model.CalendarNotificationTemplate;
import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * Provides the remote service utility for CalendarNotificationTemplate. This utility wraps
 * <code>com.liferay.calendar.service.impl.CalendarNotificationTemplateServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author Eduardo Lundgren
 * @see CalendarNotificationTemplateService
 * @generated
 */
public class CalendarNotificationTemplateServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>com.liferay.calendar.service.impl.CalendarNotificationTemplateServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static CalendarNotificationTemplate addCalendarNotificationTemplate(
			long calendarId,
			com.liferay.calendar.notification.NotificationType notificationType,
			String notificationTypeSettings,
			com.liferay.calendar.notification.NotificationTemplateType
				notificationTemplateType,
			String subject, String body,
			com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().addCalendarNotificationTemplate(
			calendarId, notificationType, notificationTypeSettings,
			notificationTemplateType, subject, body, serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static CalendarNotificationTemplate
			updateCalendarNotificationTemplate(
				long calendarNotificationTemplateId,
				String notificationTypeSettings, String subject, String body,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws PortalException {

		return getService().updateCalendarNotificationTemplate(
			calendarNotificationTemplateId, notificationTypeSettings, subject,
			body, serviceContext);
	}

	public static CalendarNotificationTemplateService getService() {
		return _serviceDCLSingleton.getSingleton(
			CalendarNotificationTemplateServiceUtil::_getService);
	}

	private static CalendarNotificationTemplateService _getService() {
		Bundle bundle = FrameworkUtil.getBundle(
			CalendarNotificationTemplateServiceUtil.class);

		BundleContext bundleContext;

		if (bundle == null) {
			bundleContext = SystemBundleUtil.getBundleContext();
		}
		else {
			bundleContext = bundle.getBundleContext();
		}

		ServiceReference<CalendarNotificationTemplateService> serviceReference =
			bundleContext.getServiceReference(
				CalendarNotificationTemplateService.class);

		if (serviceReference == null) {
			return null;
		}

		return bundleContext.getService(serviceReference);
	}

	private static final DCLSingleton<CalendarNotificationTemplateService>
		_serviceDCLSingleton = new DCLSingleton<>();

}