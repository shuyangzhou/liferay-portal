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

package com.liferay.dispatch.web.internal.portlet.action;

import com.liferay.dispatch.constants.DispatchConstants;
import com.liferay.dispatch.constants.DispatchPortletKeys;
import com.liferay.dispatch.model.DispatchTask;
import com.liferay.dispatch.service.DispatchTaskService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DispatchPortletKeys.DISPATCH,
		"mvc.command.name=editDispatchTask"
	},
	service = MVCActionCommand.class
)
public class EditDispatchTaskMVCActionCommand extends BaseMVCActionCommand {

	protected void deleteDispatchTask(ActionRequest actionRequest)
		throws PortalException {

		long[] deleteDispatchTaskIds = null;

		long dispatchTaskId = ParamUtil.getLong(
			actionRequest, "dispatchTaskId");

		if (dispatchTaskId > 0) {
			deleteDispatchTaskIds = new long[] {dispatchTaskId};
		}
		else {
			deleteDispatchTaskIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "deleteDispatchTaskIds"),
				0L);
		}

		for (long deleteDispatchTaskId : deleteDispatchTaskIds) {
			_dispatchTaskService.deleteDispatchTask(deleteDispatchTaskId);
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateDispatchTask(actionRequest, actionResponse);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteDispatchTask(actionRequest);
			}
			else if (cmd.equals("runTask")) {
				HttpServletResponse httpServletResponse =
					_portal.getHttpServletResponse(actionResponse);

				httpServletResponse.setContentType(
					ContentTypes.APPLICATION_JSON);

				writeJSON(actionResponse, runTask(actionRequest));

				hideDefaultSuccessMessage(actionRequest);
			}
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			SessionErrors.add(actionRequest, exception.getClass());
		}
	}

	protected JSONObject runTask(ActionRequest actionRequest) {
		JSONObject jsonObject = _jsonFactory.createJSONObject();

		long dispatchTaskId = ParamUtil.getLong(
			actionRequest, "dispatchTaskId");

		try {
			_sendMessage(dispatchTaskId);
		}
		catch (Exception exception) {
			hideDefaultErrorMessage(actionRequest);

			_log.error(exception, exception);

			jsonObject.put(
				"error", exception.getMessage()
			).put(
				"success", false
			);
		}

		jsonObject.put("success", true);

		return jsonObject;
	}

	protected DispatchTask updateDispatchTask(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long dispatchTaskId = ParamUtil.getLong(
			actionRequest, "dispatchTaskId");

		String name = ParamUtil.getString(actionRequest, "name");

		String dispatchTaskType = ParamUtil.getString(
			actionRequest, "dispatchTaskType");

		UnicodeProperties typeSettingsUnicodeProperties = new UnicodeProperties(
			true);

		typeSettingsUnicodeProperties.fastLoad(
			ParamUtil.getString(actionRequest, "typeSettings"));

		DispatchTask dispatchTask = null;

		if (dispatchTaskId > 0) {
			dispatchTask = _dispatchTaskService.updateDispatchTask(
				dispatchTaskId, name, typeSettingsUnicodeProperties);
		}
		else {
			dispatchTask = _dispatchTaskService.addDispatchTask(
				_portal.getUserId(actionRequest), name, dispatchTaskType,
				typeSettingsUnicodeProperties);
		}

		return dispatchTask;
	}

	protected void writeJSON(ActionResponse actionResponse, Object object)
		throws IOException {

		HttpServletResponse httpServletResponse =
			_portal.getHttpServletResponse(actionResponse);

		httpServletResponse.setContentType(ContentTypes.APPLICATION_JSON);

		ServletResponseUtil.write(httpServletResponse, object.toString());

		httpServletResponse.flushBuffer();
	}

	private void _sendMessage(long dispatchTaskId) {
		Message message = new Message();

		message.setPayload(JSONUtil.put("dispatchTaskId", dispatchTaskId));

		_destination.send(message);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditDispatchTaskMVCActionCommand.class);

	@Reference(
		target = "(destination.name=" + DispatchConstants.EXECUTOR_DESTINATION_NAME + ")"
	)
	private Destination _destination;

	@Reference
	private DispatchTaskService _dispatchTaskService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private Portal _portal;

}