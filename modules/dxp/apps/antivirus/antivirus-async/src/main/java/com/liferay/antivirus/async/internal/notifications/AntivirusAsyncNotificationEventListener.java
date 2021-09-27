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

import com.liferay.antivirus.async.configuration.AntivirusAsyncConfiguration;
import com.liferay.antivirus.async.constants.AntivirusAsyncConstants;
import com.liferay.antivirus.async.events.AntivirusAsyncEvent;
import com.liferay.antivirus.async.internal.util.AntivirusAsyncUtil;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserNotificationDeliveryConstants;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.notifications.UserNotificationManagerUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserNotificationEventLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Raymond Augé
 */
@Component(
	configurationPid = "com.liferay.antivirus.async.configuration.AntivirusAsyncConfiguration",
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	property = {
		"antivirus.async.event=MISSING",
		"antivirus.async.event=PROCESSING_ERROR",
		"antivirus.async.event=SIZE_EXCEEDED", "antivirus.async.event=SUCCESS",
		"antivirus.async.event=VIRUS_FOUND"
	},
	service = BiConsumer.class
)
public class AntivirusAsyncNotificationEventListener
	implements BiConsumer<String, Map.Entry<Message, Object[]>> {

	@Override
	public void accept(
		String eventName, Map.Entry<Message, Object[]> eventData) {

		AntivirusAsyncEvent antivirusAsyncEvent = AntivirusAsyncEvent.withName(
			eventName);

		if ((antivirusAsyncEvent == AntivirusAsyncEvent.MISSING) ||
			(antivirusAsyncEvent == AntivirusAsyncEvent.SUCCESS)) {

			_removeNotifications(eventData.getKey());
		}
		else if (antivirusAsyncEvent == AntivirusAsyncEvent.PROCESSING_ERROR) {
			Message message = eventData.getKey();
			Object[] arguments = eventData.getValue();

			_sendNotification(
				(Exception)arguments[0], eventData.getKey(),
				AntivirusAsyncConstants.KEY_ERROR,
				AntivirusAsyncConstants.KEY_PROCESSING_ERROR,
				AntivirusAsyncUtil.getFileIdentifier(message), _retryInterval);
		}
		else if (antivirusAsyncEvent == AntivirusAsyncEvent.SIZE_EXCEEDED) {
			Message message = eventData.getKey();
			Object[] arguments = eventData.getValue();

			_sendNotification(
				(Exception)arguments[0], message,
				AntivirusAsyncConstants.KEY_CAUTION,
				AntivirusAsyncConstants.KEY_SIZE_EXCEEDED,
				AntivirusAsyncUtil.getFileIdentifier(message));
		}
		else if (antivirusAsyncEvent == AntivirusAsyncEvent.VIRUS_FOUND) {
			Message message = eventData.getKey();
			Object[] arguments = eventData.getValue();

			_sendNotification(
				(Exception)arguments[0], message,
				AntivirusAsyncConstants.KEY_WARNING,
				AntivirusAsyncConstants.KEY_VIRUS_FOUND,
				AntivirusAsyncUtil.getFileIdentifier(message), arguments[1]);
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		AntivirusAsyncConfiguration antivirusAsyncConfiguration =
			ConfigurableUtil.createConfigurable(
				AntivirusAsyncConfiguration.class, properties);

		_retryInterval = antivirusAsyncConfiguration.retryInterval();
	}

	private void _removeNotifications(Message message) {
		long companyId = message.getLong("companyId");
		long repositoryId = message.getLong("repositoryId");
		String fileName = message.getString("fileName");

		long userId = message.getLong("userId");

		ActionableDynamicQuery actionableDynamicQuery =
			_userNotificationEventLocalService.getActionableDynamicQuery();

		actionableDynamicQuery.setCompanyId(companyId);
		actionableDynamicQuery.setAddCriteriaMethod(
			dynamicQuery -> {
				Property property = PropertyFactoryUtil.forName("userId");

				dynamicQuery.add(property.eq(userId));

				property = PropertyFactoryUtil.forName("deliveryType");

				dynamicQuery.add(
					property.eq(
						UserNotificationDeliveryConstants.TYPE_WEBSITE));
			});

		actionableDynamicQuery.setPerformActionMethod(
			(UserNotificationEvent userNotificationEvent) -> {
				JSONObject jsonObject = _jsonFactory.createJSONObject(
					userNotificationEvent.getPayload());

				long curCompanyId = jsonObject.getLong("companyId", 0);
				long curRepositoryId = jsonObject.getLong("repositoryId", 0);
				String curFileName = jsonObject.getString(
					"fileName", StringPool.BLANK);

				if ((curCompanyId == companyId) &&
					(curRepositoryId == repositoryId) &&
					curFileName.equals(fileName)) {

					_userNotificationEventLocalService.
						deleteUserNotificationEvent(userNotificationEvent);
				}
			});

		try {
			actionableDynamicQuery.performActions();
		}
		catch (PortalException portalException) {
			ReflectionUtil.throwException(portalException);
		}
	}

	private void _sendNotification(
		Exception exception, Message message, String severity, String reason,
		Object... arguments) {

		String logMessage = StringBundler.concat(
			LanguageUtil.format(
				LocaleUtil.getMostRelevantLocale(), reason, arguments),
			" from ", message.getValues(), " ", exception.getMessage());

		if (Objects.equals(AntivirusAsyncConstants.KEY_ERROR, severity)) {
			_log.error(logMessage);
		}
		else if (Objects.equals(
					AntivirusAsyncConstants.KEY_WARNING, severity)) {

			if (_log.isWarnEnabled()) {
				_log.warn(logMessage);
			}
		}
		else {
			if (_log.isInfoEnabled()) {
				_log.info(logMessage);
			}
		}

		long userId = message.getLong("userId");

		User user = _userLocalService.fetchUserById(userId);

		if ((user == null) || !user.isActive() || user.isDefaultUser()) {
			return;
		}

		try {
			if (UserNotificationManagerUtil.isDeliver(
					userId, AntivirusAsyncConstants.ANTIVIRUS, 0,
					UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY,
					UserNotificationDeliveryConstants.TYPE_WEBSITE)) {

				String className = message.getString("className");
				long classPK = message.getLong("classPK");
				long companyId = message.getLong("companyId");
				String entryURL = message.getString("entryURL");
				String fileExtension = message.getString("fileExtension");
				String fileName = message.getString("fileName");
				long repositoryId = message.getLong("repositoryId");
				long size = message.getLong("size");
				String sourceFileName = message.getString("sourceFileName");
				String versionLabel = message.getString("versionLabel");

				_userNotificationEventLocalService.sendUserNotificationEvents(
					userId, AntivirusAsyncConstants.ANTIVIRUS,
					UserNotificationDeliveryConstants.TYPE_WEBSITE, false,
					JSONUtil.put(
						"className", className
					).put(
						"classPK", classPK
					).put(
						"companyId", companyId
					).put(
						"entryURL", entryURL
					).put(
						"fileExtension", fileExtension
					).put(
						"fileName", fileName
					).put(
						"reason", reason
					).put(
						"reasonArguments", JSONUtil.putAll(arguments)
					).put(
						"repositoryId", repositoryId
					).put(
						"severity", severity
					).put(
						"size", size
					).put(
						"sourceFileName", sourceFileName
					).put(
						"versionLabel", versionLabel
					));
			}
		}
		catch (PortalException portalException) {
			ReflectionUtil.throwException(portalException);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AntivirusAsyncNotificationEventListener.class);

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private JSONFactory _jsonFactory;

	private volatile int _retryInterval;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private UserLocalService _userLocalService;

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private UserNotificationEventLocalService
		_userNotificationEventLocalService;

}