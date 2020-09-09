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

package com.liferay.dispatch.service.impl;

import com.liferay.dispatch.constants.DispatchActionKeys;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.base.DispatchTaskServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.service.permission.PortalPermissionUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import org.osgi.service.component.annotations.Component;

/**
 * The implementation of the dispatch task remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.dispatch.service.DispatchTaskService</code> interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTaskServiceBaseImpl
 */
@Component(
	property = {
		"json.web.service.context.name=dispatch",
		"json.web.service.context.path=DispatchTask"
	},
	service = AopService.class
)
public class DispatchTaskServiceImpl extends DispatchTaskServiceBaseImpl {

	@Override
	public DispatchTask addDispatchTask(
			long userId, String name, String type,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws PortalException {

		PortalPermissionUtil.check(
			getPermissionChecker(), DispatchActionKeys.ADD_DISPATCH_TASK);

		return dispatchTaskLocalService.addDispatchTask(
			userId, name, false, type, typeSettingsUnicodeProperties);
	}

	@Override
	public void deleteDispatchTask(long dispatchTaskId) throws PortalException {
		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchTaskId, ActionKeys.DELETE);

		dispatchTaskLocalService.deleteDispatchTask(dispatchTaskId);
	}

	@Override
	public DispatchTask updateDispatchTask(
			long dispatchTaskId, String name,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws PortalException {

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchTaskId, ActionKeys.UPDATE);

		return dispatchTaskLocalService.updateDispatchTask(
			dispatchTaskId, name, typeSettingsUnicodeProperties);
	}

	@Override
	public DispatchTask updateDispatchTaskTrigger(
			long dispatchTaskId, boolean active, String cronExpression,
			int endDateMonth, int endDateDay, int endDateYear, int endDateHour,
			int endDateMinute, boolean neverEnd, int startDateMonth,
			int startDateDay, int startDateYear, int startDateHour,
			int startDateMinute)
		throws PortalException {

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchTaskId, ActionKeys.UPDATE);

		return dispatchTaskLocalService.updateDispatchTaskTrigger(
			dispatchTaskId, active, cronExpression, endDateMonth, endDateDay,
			endDateYear, endDateHour, endDateMinute, neverEnd, startDateMonth,
			startDateDay, startDateYear, startDateHour, startDateMinute);
	}

	private static volatile ModelResourcePermission<DispatchTask>
		_dispatchTaskModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				DispatchTaskServiceImpl.class,
				"_dispatchTaskModelResourcePermission", DispatchTask.class);

}