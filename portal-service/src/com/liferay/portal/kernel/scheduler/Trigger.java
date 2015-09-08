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

package com.liferay.portal.kernel.scheduler;

import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;

import java.util.Date;

/**
 * @author Shuyang Zhou
 */
public class Trigger {

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate, Date endDate,
		int interval, TimeUnit timeUnit) {

		return new Trigger(
			jobName, groupName, startDate, endDate,
			new IntervalTriggerContent(interval, timeUnit));
	}

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate, Date endDate,
		String cronExpression) {

		return new Trigger(
			jobName, groupName, startDate, endDate,
			new CronTriggerContent(cronExpression));
	}

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate, Date endDate,
		TriggerContent<? extends Serializable> triggerContent) {

		return new Trigger(
			jobName, groupName, startDate, endDate, triggerContent);
	}

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate, int interval,
		TimeUnit timeUnit) {

		return new Trigger(
			jobName, groupName, startDate, null,
			new IntervalTriggerContent(interval, timeUnit));
	}

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate,
		String cronExpression) {

		return new Trigger(
			jobName, groupName, startDate, null,
			new CronTriggerContent(cronExpression));
	}

	public static Trigger createTrigger(
		String jobName, String groupName, Date startDate,
		TriggerContent<? extends Serializable> triggerContent) {

		return new Trigger(jobName, groupName, startDate, null, triggerContent);
	}

	public static Trigger createTrigger(
		String jobName, String groupName, int interval, TimeUnit timeUnit) {

		return new Trigger(
			jobName, groupName, null, null,
			new IntervalTriggerContent(interval, timeUnit));
	}

	public static Trigger createTrigger(
		String jobName, String groupName, String cronExpression) {

		return new Trigger(
			jobName, groupName, null, null,
			new CronTriggerContent(cronExpression));
	}

	public static Trigger createTrigger(
		String jobName, String groupName,
		TriggerContent<? extends Serializable> triggerContent) {

		return new Trigger(jobName, groupName, null, null, triggerContent);
	}

	public Date getEndDate() {
		return _endDate;
	}

	public String getGroupName() {
		return _groupName;
	}

	public String getJobName() {
		return _jobName;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public TriggerContent<? extends Serializable> getTriggerContent() {
		return _triggerContent;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(11);

		sb.append("{endDate=");
		sb.append(_endDate);
		sb.append(", groupName=");
		sb.append(_groupName);
		sb.append(", jobName=");
		sb.append(_jobName);
		sb.append(", startDate=");
		sb.append(_startDate);
		sb.append(", triggerContent=");
		sb.append(_triggerContent);
		sb.append("}");

		return sb.toString();
	}

	private Trigger(
		String jobName, String groupsName, Date startDate, Date endDate,
		TriggerContent<? extends Serializable> triggerContent) {

		_jobName = jobName;
		_groupName = groupsName;
		_startDate = startDate;
		_endDate = endDate;
		_triggerContent = triggerContent;
	}

	private final Date _endDate;
	private final String _groupName;
	private final String _jobName;
	private final Date _startDate;
	private final TriggerContent<? extends Serializable> _triggerContent;

}