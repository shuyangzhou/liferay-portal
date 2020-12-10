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
	public static com.liferay.calendar.model.CalendarNotificationTemplate
			addCalendarNotificationTemplate(
				long calendarId,
				com.liferay.calendar.notification.NotificationType
					notificationType,
				java.lang.String notificationTypeSettings,
				com.liferay.calendar.notification.NotificationTemplateType
					notificationTemplateType,
				java.lang.String subject, java.lang.String body,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().addCalendarNotificationTemplate(
			calendarId, notificationType, notificationTypeSettings,
			notificationTemplateType, subject, body, serviceContext);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	public static java.lang.String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.calendar.model.CalendarNotificationTemplate
			updateCalendarNotificationTemplate(
				long calendarNotificationTemplateId,
				java.lang.String notificationTypeSettings,
				java.lang.String subject, java.lang.String body,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {

		return getService().updateCalendarNotificationTemplate(
			calendarNotificationTemplateId, notificationTypeSettings, subject,
			body, serviceContext);
	}

	public static CalendarNotificationTemplateService getService() {
		return _calendarNotificationTemplateService;
	}

	private static volatile CalendarNotificationTemplateService
		_calendarNotificationTemplateService;

}