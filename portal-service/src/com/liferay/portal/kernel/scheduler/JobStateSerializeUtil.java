/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.kernel.util.ObjectValuePair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Tina Tian
 */

public class JobStateSerializeUtil {

	public static JobState deSerialize(Map<String, Object> jobStateMap) {
		return _doDeSerialize1(jobStateMap);
	}

	public static Map<String, Object> serialize(JobState jobState) {
		return _doSerialize1(jobState);
	}

	private static JobState _doDeSerialize1(Map<String, Object> jobStateMap) {
		ObjectValuePair<Exception, Date>[] exceptions =
			(ObjectValuePair<Exception, Date>[])jobStateMap.get(
				_EXCEPTIONS_FIELD);
		int exceptionsMaxSize = (Integer)jobStateMap.get(
			_EXCEPTIONS_MAX_SIZE_FIELD);
		int triggerStateIndex = (Integer)jobStateMap.get(_TRIGGER_STATE_FIELD);
		Map<String, Date> triggerTimeInfomation =
			(Map<String, Date>)jobStateMap.get(_TRIGGER_TIME_INFOMATION_FIELD);

		JobState jobState = new JobState(
			TriggerState.values()[triggerStateIndex], exceptionsMaxSize);

		if (exceptions != null) {
			for (ObjectValuePair<Exception, Date> exception : exceptions) {
				jobState.addException(exception.getKey(), exception.getValue());
			}
		}

		if (triggerTimeInfomation != null) {
			Set<Map.Entry<String, Date>> entries =
				triggerTimeInfomation.entrySet();

			for (Map.Entry<String, Date> entry : entries) {
				jobState.setTriggerTimeInfomation(
					entry.getKey(), entry.getValue());
			}
		}

		return jobState;
	}

	private static Map<String, Object> _doSerialize1(JobState jobState) {
		Map<String, Object> jobStateMap = new HashMap<String, Object>();

		jobStateMap.put(_EXCEPTIONS_FIELD, jobState.getExceptions());
		jobStateMap.put(
			_EXCEPTIONS_MAX_SIZE_FIELD, jobState.getExceptionsMaxSize());
		jobStateMap.put(
			_TRIGGER_STATE_FIELD, jobState.getTriggerState().ordinal());
		jobStateMap.put(
			_TRIGGER_TIME_INFOMATION_FIELD,
			jobState.getTriggerTimeInfomations());

		return jobStateMap;
	}

	private static final String _EXCEPTIONS_FIELD = "exceptions";
	private static final String _EXCEPTIONS_MAX_SIZE_FIELD =
		"exceptionsMaxSize";
	private static final String _TRIGGER_STATE_FIELD = "triggerState";
	private static final String _TRIGGER_TIME_INFOMATION_FIELD =
		"triggerTimeInfomation";

}
