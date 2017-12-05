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

package com.liferay.calendar.internal.permission;

import com.liferay.calendar.constants.CalendarActionKeys;
import com.liferay.calendar.constants.CalendarPortletKeys;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.service.CalendarLocalService;
import com.liferay.exportimport.kernel.staging.permission.StagingPermissionUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermissionCheck;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.PortletResourcePermission;

/**
 * @author Preston Crary
 */
public class CalendarPermissionCheckerFactory {

	public static ModelResourcePermission<Calendar> create(
		CalendarLocalService calendarLocalService,
		PortletResourcePermission portletResourcePermission) {

		return ModelResourcePermissionChecker.create(
			Calendar.class.getName(), Calendar::getCalendarId,
			calendarLocalService::getCalendar, portletResourcePermission,
			(modelResourcePermission, checksCollector) ->
				checksCollector.accept(new StagingPermissionCheck()));
	}

	private static class StagingPermissionCheck
		implements ModelResourcePermissionCheck<Calendar> {

		@Override
		public Boolean contains(
				PermissionChecker permissionChecker, String name,
				Calendar calendar, String actionId)
			throws PortalException {

			if (CalendarActionKeys.VIEW_BOOKING_DETAILS.equals(actionId)) {
				return null;
			}

			return StagingPermissionUtil.hasPermission(
				permissionChecker, calendar.getGroupId(),
				Calendar.class.getName(), calendar.getCalendarId(),
				CalendarPortletKeys.CALENDAR, actionId);
		}

	}

}