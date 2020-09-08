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

package com.liferay.dispatch.internal.messaging;

import com.liferay.dispatch.constants.DispatchConstants;
import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.DispatchTaskLocalService;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.messaging.MessageListenerException;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matija Petanjek
 */
@Component(
	immediate = true,
	property = "destination.name=" + DispatchConstants.EXECUTOR_DESTINATION_NAME,
	service = MessageListener.class
)
public class DispatchMessageListener implements MessageListener {

	@Override
	public void receive(Message message) throws MessageListenerException {
		String payloadString = (String)message.getPayload();

		JSONObject payloadJSONObject = null;

		try {
			payloadJSONObject = JSONFactoryUtil.createJSONObject(payloadString);
		}
		catch (JSONException jsonException) {
			_log.error(jsonException, jsonException);

			throw new MessageListenerException(jsonException);
		}

		long dispatchTaskId = payloadJSONObject.getLong("dispatchTaskId");

		DispatchTaskExecutor dispatchTaskExecutor = null;

		try {
			dispatchTaskExecutor = getDispatchTaskExecutor(dispatchTaskId);
		}
		catch (PortalException portalException) {
			if (_log.isDebugEnabled()) {
				_log.debug(portalException, portalException);
			}
		}

		if (dispatchTaskExecutor != null) {
			try {
				dispatchTaskExecutor.execute(dispatchTaskId);
			}
			catch (Exception exception) {
				if (_log.isDebugEnabled()) {
					_log.debug(exception, exception);
				}
			}
		}
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_dispatchTaskExecutorTrackerMap =
			ServiceTrackerMapFactory.openSingleValueMap(
				bundleContext, DispatchTaskExecutor.class,
				"dispatch.task.executor.type");
	}

	protected DispatchTaskExecutor getDispatchTaskExecutor(long dispatchTaskId)
		throws PortalException {

		DispatchTaskExecutor dispatchTaskExecutor = null;

		if (_dispatchTaskExecutorTrackerMap != null) {
			DispatchTask dispatchTask =
				_dispatchTaskLocalService.getDispatchTask(dispatchTaskId);

			for (String key : _dispatchTaskExecutorTrackerMap.keySet()) {
				if (key.equals(dispatchTask.getType())) {
					dispatchTaskExecutor =
						_dispatchTaskExecutorTrackerMap.getService(key);

					break;
				}
			}
		}

		return dispatchTaskExecutor;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DispatchMessageListener.class);

	private ServiceTrackerMap<String, DispatchTaskExecutor>
		_dispatchTaskExecutorTrackerMap;

	@Reference
	private DispatchTaskLocalService _dispatchTaskLocalService;

}