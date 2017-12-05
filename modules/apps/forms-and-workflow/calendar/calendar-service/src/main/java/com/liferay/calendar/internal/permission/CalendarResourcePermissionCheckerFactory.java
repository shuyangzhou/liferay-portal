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

import com.liferay.calendar.constants.CalendarPortletKeys;
import com.liferay.calendar.model.CalendarResource;
import com.liferay.calendar.service.CalendarResourceLocalService;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.checker.ModelResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.checker.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.checker.StagedModelPermissionCheck;

/**
 * @author Preston Crary
 */
public class CalendarResourcePermissionCheckerFactory {

	public static ModelResourcePermission<CalendarResource> create(
		CalendarResourceLocalService calendarResourceLocalService,
		PortletResourcePermission portletResourcePermission) {

		return ModelResourcePermissionChecker.create(
			CalendarResource.class.getName(),
			CalendarResource::getCalendarResourceId,
			calendarResourceLocalService::getCalendarResource,
			portletResourcePermission,
			(modelResourcePermission, checksCollector) ->
				checksCollector.accept(
					new StagedModelPermissionCheck<>(
						CalendarPortletKeys.CALENDAR,
						CalendarResource::getCalendarResourceId)));
	}

}