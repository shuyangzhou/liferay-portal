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

package com.liferay.aspectj.hibernate.stale.object.state;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.HashUtil;

import java.io.Serializable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

import org.hibernate.StaleObjectStateException;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.MergeEvent;

/**
 * @author Preston Crary
 */
@Aspect
@SuppressAjWarnings("adviceDidNotMatch")
public class HibernateStaleObjectStateAspect {

	@AfterThrowing(
		throwing = "sose",
		value = "execution(void org.hibernate.event.def.DefaultMergeEventListener.onMerge(org.hibernate.event.MergeEvent)) && args(mergeEvent)"
	)
	public void initMergeCause(
		MergeEvent mergeEvent, StaleObjectStateException sose) {

		Object object = mergeEvent.getOriginal();

		if (!(object instanceof MVCCModel)) {
			return;
		}

		EventKey eventKey = new EventKey((BaseModel<?>)object);

		StaleObjectStateException cause = _previousUpdates.get(eventKey);

		sose.initCause(cause);
	}

	@AfterReturning(
		"execution(void org.hibernate.event.DeleteEventListener.onDelete(" +
			"org.hibernate.event.DeleteEvent)) && args(deleteEvent)"
	)
	public void logDeleteEvent(DeleteEvent deleteEvent) {
		_logEvent(deleteEvent.getObject());
	}

	@AfterReturning(
		"execution(void org.hibernate.event.def.DefaultMergeEventListener." +
			"onMerge(org.hibernate.event.MergeEvent)) && args(mergeEvent)"
	)
	public void logMergeEvent(MergeEvent mergeEvent) {
		_logEvent(mergeEvent.getOriginal());
	}

	private void _logEvent(Object object) {
		if (!(object instanceof MVCCModel)) {
			return;
		}

		BaseModel<?> baseModel = (BaseModel<?>)object;

		EventKey eventKey = new EventKey(baseModel);

		StaleObjectStateException sose = new StaleObjectStateException(
			baseModel.getModelClassName(), baseModel.getPrimaryKeyObj());

		StaleObjectStateException oldCause = _previousUpdates.put(
			eventKey, sose);

		if (oldCause != null) {
			sose.addSuppressed(oldCause);
		}
	}

	private final Map<EventKey, StaleObjectStateException> _previousUpdates =
		new ConcurrentHashMap<>();

	private static class EventKey {

		@Override
		public boolean equals(Object obj) {
			EventKey eventKey = (EventKey)obj;

			if ((eventKey._mvccVersion == _mvccVersion) &&
				Objects.equals(eventKey._primaryKeyObject, _primaryKeyObject) &&
				Objects.equals(eventKey._modelClassName, _modelClassName)) {

				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			int hashCode = HashUtil.hash(0, _mvccVersion);

			hashCode = HashUtil.hash(hashCode, _primaryKeyObject);
			hashCode = HashUtil.hash(hashCode, _modelClassName);

			return hashCode;
		}

		private EventKey(BaseModel<?> baseModel) {
			_modelClassName = baseModel.getModelClassName();
			_primaryKeyObject = baseModel.getPrimaryKeyObj();

			MVCCModel mvccModel = (MVCCModel)baseModel;

			_mvccVersion = mvccModel.getMvccVersion();
		}

		private final String _modelClassName;
		private final long _mvccVersion;
		private final Serializable _primaryKeyObject;

	}

}