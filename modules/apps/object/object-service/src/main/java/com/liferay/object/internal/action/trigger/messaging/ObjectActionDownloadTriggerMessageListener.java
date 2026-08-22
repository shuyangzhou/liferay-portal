/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.action.trigger.messaging;

import com.liferay.object.action.engine.ObjectActionEngine;
import com.liferay.object.constants.ObjectActionTriggerConstants;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.entry.util.ObjectEntryPayloadUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectDefinitionLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.vulcan.dto.converter.DTOConverterRegistry;

import java.io.Serializable;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leov
 */
@Component(
	property = "destination.name=" + DestinationNames.OBJECT_ENTRY_ATTACHMENT_DOWNLOAD,
	service = MessageListener.class
)
public class ObjectActionDownloadTriggerMessageListener
	extends BaseMessageListener {

	@Activate
	protected void activate(BundleContext bundleContext) {
		DestinationConfiguration destinationConfiguration =
			new DestinationConfiguration(
				DestinationConfiguration.DESTINATION_TYPE_SYNCHRONOUS,
				DestinationNames.OBJECT_ENTRY_ATTACHMENT_DOWNLOAD);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		_serviceRegistration = bundleContext.registerService(
			Destination.class, destination,
			MapUtil.singletonDictionary(
				"destination.name", destination.getName()));
	}

	@Deactivate
	protected void deactivate() {
		_serviceRegistration.unregister();
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		String objectDefinitionExternalReferenceCode = message.getString(
			"objectDefinitionExternalReferenceCode");
		long companyId = message.getLong("companyId");

		ObjectDefinition objectDefinition =
			_objectDefinitionLocalService.
				fetchObjectDefinitionByExternalReferenceCode(
					objectDefinitionExternalReferenceCode, companyId);

		if (objectDefinition == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Object definition is null for external reference ",
						"code ", objectDefinitionExternalReferenceCode,
						" and company ", companyId));
			}

			return;
		}

		String objectEntryExternalReferenceCode = message.getString(
			"objectEntryExternalReferenceCode");

		long groupId = ObjectDefinitionConstants.GROUP_ID_DEFAULT;

		Group group = _groupLocalService.fetchGroupByExternalReferenceCode(
			message.getString("groupExternalReferenceCode"), companyId);

		if (group != null) {
			groupId = group.getGroupId();
		}

		ObjectEntry objectEntry = _objectEntryLocalService.fetchObjectEntry(
			objectEntryExternalReferenceCode, groupId,
			objectDefinition.getObjectDefinitionId());

		if (objectEntry == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Object entry is null for external reference code ",
						objectEntryExternalReferenceCode, ", group ID ",
						groupId, " and object definition ",
						objectDefinitionExternalReferenceCode));
			}

			return;
		}

		long fileEntryId = message.getLong("fileEntryId");

		if (!_hasAttachment(fileEntryId, objectDefinition, objectEntry)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					StringBundler.concat(
						"Object entry ", objectEntry.getObjectEntryId(),
						" has no attachment holding file entry ", fileEntryId));
			}

			return;
		}

		_objectActionEngine.executeObjectActions(
			objectDefinition.getClassName(), message.getLong("companyId"),
			ObjectActionTriggerConstants.KEY_ON_AFTER_ATTACHMENT_DOWNLOAD,
			() -> ObjectEntryPayloadUtil.getPayloadJSONObject(
				_dtoConverterRegistry, _jsonFactory,
				ObjectActionTriggerConstants.KEY_ON_AFTER_ATTACHMENT_DOWNLOAD,
				objectDefinition, objectEntry, null, null,
				_userLocalService.getUser(message.getLong("userId"))),
			message.getLong("userId"));
	}

	private boolean _hasAttachment(
		long fileEntryId, ObjectDefinition objectDefinition,
		ObjectEntry objectEntry) {

		Map<String, Serializable> values = objectEntry.getValues();

		for (ObjectField objectField :
				_objectFieldLocalService.getObjectFieldsByBusinessType(
					objectDefinition.getObjectDefinitionId(),
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			if (fileEntryId == GetterUtil.getLong(
					values.get(objectField.getName()))) {

				return true;
			}

			// A localized field keeps its per locale values in a map of its
			// own and stores only the default locale's value under the plain
			// field name

			Serializable serializable = values.get(
				objectField.getI18nObjectFieldName());

			if (serializable instanceof Map<?, ?> localizedValues) {
				for (Object localizedValue : localizedValues.values()) {
					if (fileEntryId == GetterUtil.getLong(localizedValue)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ObjectActionDownloadTriggerMessageListener.class);

	@Reference
	private DestinationFactory _destinationFactory;

	@Reference
	private DTOConverterRegistry _dtoConverterRegistry;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JSONFactory _jsonFactory;

	@Reference
	private ObjectActionEngine _objectActionEngine;

	@Reference
	private ObjectDefinitionLocalService _objectDefinitionLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

	@Reference
	private ObjectFieldLocalService _objectFieldLocalService;

	private ServiceRegistration<Destination> _serviceRegistration;

	@Reference
	private UserLocalService _userLocalService;

}