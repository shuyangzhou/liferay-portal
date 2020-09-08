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

import com.liferay.dispatch.model.DispatchLog;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.base.DispatchLogServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;

import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matija Petanjek
 */
@Component(
	property = {
		"json.web.service.context.name=dispatch",
		"json.web.service.context.path=DispatchLog"
	},
	service = AopService.class
)
public class DispatchLogServiceImpl extends DispatchLogServiceBaseImpl {

	@Override
	public void deleteDispatchLog(long dispatchLogId) throws PortalException {
		DispatchLog dispatchLog = dispatchLogLocalService.getDispatchLog(
			dispatchLogId);

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchLog.getDispatchTaskId(),
			ActionKeys.UPDATE);

		dispatchLogLocalService.deleteDispatchLog(dispatchLog);
	}

	@Override
	public DispatchLog getDispatchLog(long dispatchLogId)
		throws PortalException {

		DispatchLog dispatchLog = dispatchLogLocalService.getDispatchLog(
			dispatchLogId);

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchLog.getDispatchTaskId(),
			ActionKeys.VIEW);

		return dispatchLog;
	}

	@Override
	public List<DispatchLog> getDispatchLogs(
			long dispatchTaskId, int start, int end)
		throws PortalException {

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchTaskId, ActionKeys.VIEW);

		return dispatchLogLocalService.getDispatchLogs(
			dispatchTaskId, start, end);
	}

	@Override
	public int getDispatchLogsCount(long dispatchTaskId)
		throws PortalException {

		_dispatchTaskModelResourcePermission.check(
			getPermissionChecker(), dispatchTaskId, ActionKeys.VIEW);

		return dispatchLogLocalService.getDispatchLogsCount(dispatchTaskId);
	}

	private static volatile ModelResourcePermission<DispatchTask>
		_dispatchTaskModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				DispatchTaskServiceImpl.class,
				"_dispatchTaskModelResourcePermission", DispatchTask.class);

}