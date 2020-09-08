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

import com.liferay.dispatch.constants.DispatchConstants;
import com.liferay.dispatch.exception.DispatchTaskEndDateException;
import com.liferay.dispatch.exception.DispatchTaskNameException;
import com.liferay.dispatch.exception.DispatchTaskStartDateException;
import com.liferay.dispatch.exception.DuplicateDispatchTaskException;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.base.DispatchTaskLocalServiceBaseImpl;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.SystemEventConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.systemevent.SystemEvent;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * The implementation of the dispatch task local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the <code>com.liferay.dispatch.service.DispatchTaskLocalService</code> interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Matija Petanjek
 * @see DispatchTaskLocalServiceBaseImpl
 */
@Component(
	property = "model.class.name=com.liferay.dispatch.model.DispatchTask",
	service = AopService.class
)
public class DispatchTaskLocalServiceImpl
	extends DispatchTaskLocalServiceBaseImpl {

	@Override
	public DispatchTask addDispatchTask(
			long userId, String name, boolean system, String type,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws PortalException {

		User user = userLocalService.getUser(userId);

		validate(0, user.getCompanyId(), name);

		DispatchTask dispatchTask = dispatchTaskPersistence.create(
			counterLocalService.increment());

		dispatchTask.setCompanyId(user.getCompanyId());
		dispatchTask.setUserId(user.getUserId());
		dispatchTask.setUserName(user.getFullName());
		dispatchTask.setName(name);
		dispatchTask.setSystem(system);
		dispatchTask.setType(type);
		dispatchTask.setTypeSettingsUnicodeProperties(
			typeSettingsUnicodeProperties);

		dispatchTask = dispatchTaskPersistence.update(dispatchTask);

		resourceLocalService.addResources(
			user.getCompanyId(), 0, user.getUserId(),
			DispatchTask.class.getName(), dispatchTask.getDispatchTaskId(),
			false, true, true);

		return dispatchTask;
	}

	@Override
	@SystemEvent(type = SystemEventConstants.TYPE_DELETE)
	public DispatchTask deleteDispatchTask(DispatchTask dispatchTask)
		throws PortalException {

		if (dispatchTask.isSystem()) {
			return dispatchTask;
		}

		dispatchLogPersistence.removeByDispatchTaskId(
			dispatchTask.getDispatchTaskId());

		dispatchTaskPersistence.remove(dispatchTask);

		resourceLocalService.deleteResource(
			dispatchTask, ResourceConstants.SCOPE_INDIVIDUAL);

		_deleteSchedulerJob(dispatchTask.getDispatchTaskId());

		return dispatchTask;
	}

	@Override
	public DispatchTask deleteDispatchTask(long dispatchTaskId)
		throws PortalException {

		return dispatchTaskPersistence.remove(dispatchTaskId);
	}

	@Override
	public DispatchTask fetchDispatchTask(long companyId, String name) {
		return dispatchTaskPersistence.fetchByC_N(companyId, name);
	}

	@Override
	public DispatchTask getDispatchTask(long dispatchTaskId)
		throws PortalException {

		return dispatchTaskPersistence.findByPrimaryKey(dispatchTaskId);
	}

	@Override
	public List<DispatchTask> getDispatchTasks(
		long companyId, int start, int end) {

		return dispatchTaskPersistence.findByCompanyId(companyId, start, end);
	}

	@Override
	public int getDispatchTasksCount(long companyId) {
		return dispatchTaskPersistence.countByCompanyId(companyId);
	}

	@Override
	public Date getNextFireDate(long dispatchTaskId) {
		try {
			return _schedulerEngineHelper.getNextFireTime(
				_getJobName(dispatchTaskId), _getGroupName(dispatchTaskId),
				StorageType.PERSISTED);
		}
		catch (SchedulerException schedulerException) {
			_log.error(schedulerException, schedulerException);
		}

		return null;
	}

	@Override
	public Date getPreviousFireDate(long dispatchTaskId) {
		try {
			return _schedulerEngineHelper.getPreviousFireTime(
				_getJobName(dispatchTaskId), _getGroupName(dispatchTaskId),
				StorageType.PERSISTED);
		}
		catch (SchedulerException schedulerException) {
			_log.error(schedulerException, schedulerException);
		}

		return null;
	}

	@Override
	public DispatchTask updateDispatchTask(
			long dispatchTaskId, String name,
			UnicodeProperties typeSettingsUnicodeProperties)
		throws PortalException {

		DispatchTask dispatchTask = dispatchTaskPersistence.findByPrimaryKey(
			dispatchTaskId);

		validate(dispatchTaskId, dispatchTask.getCompanyId(), name);

		dispatchTask.setName(name);
		dispatchTask.setTypeSettingsUnicodeProperties(
			typeSettingsUnicodeProperties);

		return dispatchTaskPersistence.update(dispatchTask);
	}

	@Override
	public DispatchTask updateDispatchTaskTrigger(
			long dispatchTaskId, boolean active, String cronExpression,
			int endDateMonth, int endDateDay, int endDateYear, int endDateHour,
			int endDateMinute, boolean neverEnd, int startDateMonth,
			int startDateDay, int startDateYear, int startDateHour,
			int startDateMinute)
		throws PortalException {

		DispatchTask dispatchTask = dispatchTaskPersistence.fetchByPrimaryKey(
			dispatchTaskId);

		dispatchTask.setActive(active);
		dispatchTask.setCronExpression(cronExpression);

		if (!neverEnd) {
			dispatchTask.setEndDate(
				_portal.getDate(
					endDateMonth, endDateDay, endDateYear, endDateHour,
					endDateMinute, DispatchTaskEndDateException.class));
		}

		dispatchTask.setStartDate(
			_portal.getDate(
				startDateMonth, startDateDay, startDateYear, startDateHour,
				startDateMinute, DispatchTaskStartDateException.class));

		dispatchTask = dispatchTaskPersistence.update(dispatchTask);

		_deleteSchedulerJob(dispatchTaskId);

		if (active) {
			_addSchedulerJob(
				dispatchTaskId, cronExpression, dispatchTask.getStartDate(),
				dispatchTask.getEndDate());
		}

		return dispatchTask;
	}

	protected void validate(long dispatchTaskId, long companyId, String name)
		throws PortalException {

		if (Validator.isNull(name)) {
			throw new DispatchTaskNameException(
				"Dispatch task name is null for company ID " + companyId);
		}

		DispatchTask dispatchTask = dispatchTaskPersistence.fetchByC_N(
			companyId, name);

		if (dispatchTask == null) {
			return;
		}

		if ((dispatchTaskId > 0) &&
			(dispatchTask.getDispatchTaskId() == dispatchTaskId)) {

			return;
		}

		throw new DuplicateDispatchTaskException(
			StringBundler.concat(
				"Dispatch task name \"", name,
				"\" already exists for company ID ", companyId));
	}

	private void _addSchedulerJob(
		long dispatchTaskId, String cronExpression, Date startDate,
		Date endDate) {

		Trigger trigger = _triggerFactory.createTrigger(
			_getJobName(dispatchTaskId), _getGroupName(dispatchTaskId),
			startDate, endDate, cronExpression);

		try {
			_schedulerEngineHelper.schedule(
				trigger, StorageType.PERSISTED, null,
				DispatchConstants.EXECUTOR_DESTINATION_NAME,
				_getPayload(dispatchTaskId), 1000);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Scheduler entry created for dispatch task " +
						dispatchTaskId);
			}
		}
		catch (SchedulerException schedulerException) {
			_log.error(
				"Unable to create scheduler entry for dispatch task " +
					dispatchTaskId,
				schedulerException);
		}
	}

	private void _deleteSchedulerJob(long dispatchTaskId) {
		try {
			_schedulerEngineHelper.delete(
				_getJobName(dispatchTaskId), _getGroupName(dispatchTaskId),
				StorageType.PERSISTED);
		}
		catch (SchedulerException schedulerException) {
			_log.error(
				"Unable to delete scheduler entry for dispatch task " +
					dispatchTaskId,
				schedulerException);
		}
	}

	private String _getGroupName(long dispatchTaskId) {
		return String.format("DISPATCH_GROUP_%07d", dispatchTaskId);
	}

	private String _getJobName(long dispatchTaskId) {
		return String.format("DISPATCH_JOB_%07d", dispatchTaskId);
	}

	private String _getPayload(long dispatchTaskId) {
		return String.format("{\"dispatchTaskId\"=%d}", dispatchTaskId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DispatchTaskLocalServiceImpl.class);

	@Reference
	private Portal _portal;

	@Reference
	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private TriggerFactory _triggerFactory;

}