/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.antivirus.async.internal.notifications;

import com.liferay.antivirus.async.constants.AntivirusAsyncConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.BaseModelUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + AntivirusAsyncConstants.ANTIVIRUS,
	service = UserNotificationHandler.class
)
public class AntivirusAsyncUserNotificationHandler
	extends BaseModelUserNotificationHandler {

	public AntivirusAsyncUserNotificationHandler() {
		setActionable(false);
		setPortletId(AntivirusAsyncConstants.ANTIVIRUS);
	}

	@Override
	protected String getBody(
			UserNotificationEvent userNotificationEvent,
			ServiceContext serviceContext)
		throws Exception {

		JSONObject jsonObject = _jsonFactory.createJSONObject(
			userNotificationEvent.getPayload());

		String className = jsonObject.getString("className", StringPool.BLANK);
		String reason = jsonObject.getString("reason");
		JSONArray reasonArgumentsJSONArray = jsonObject.getJSONArray(
			"reasonArguments");
		String severity = jsonObject.getString("severity");

		Object[] arguments = new Object[reasonArgumentsJSONArray.length()];

		for (int i = 0; i < reasonArgumentsJSONArray.length(); i++) {
			arguments[i] = reasonArgumentsJSONArray.get(i);
		}

		Locale locale = serviceContext.getLocale();

		if (Validator.isNotNull(className)) {
			className = StringBundler.concat(
				" [", LanguageUtil.get(locale, className + "[antivirus]"), "]");
		}

		return StringBundler.concat(
			"[", LanguageUtil.get(locale, severity), "] ",
			LanguageUtil.get(locale, "antivirus-event"), ": ",
			LanguageUtil.format(locale, reason, arguments), className);
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private JSONFactory _jsonFactory;

}