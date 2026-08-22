/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.attachment;

import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectField;
import com.liferay.object.service.ObjectFieldLocalService;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Shuyang Zhou
 */
public class ObjectEntryAttachmentMatcher {

	public static ObjectField fetchObjectField(
		long fileEntryId, ObjectDefinition objectDefinition,
		ObjectEntry objectEntry,
		ObjectFieldLocalService objectFieldLocalService) {

		Map<String, Serializable> values = objectEntry.getValues();

		for (ObjectField objectField :
				objectFieldLocalService.getObjectFieldsByBusinessType(
					objectDefinition.getObjectDefinitionId(),
					ObjectFieldConstants.BUSINESS_TYPE_ATTACHMENT)) {

			if (fileEntryId == GetterUtil.getLong(
					values.get(objectField.getName()))) {

				return objectField;
			}

			// A localized field keeps its per locale values in a map of its own
			// and stores only the default locale's value under the plain field
			// name

			Serializable serializable = values.get(
				objectField.getI18nObjectFieldName());

			if (serializable instanceof Map<?, ?> localizedValues) {
				for (Object localizedValue : localizedValues.values()) {
					if (fileEntryId == GetterUtil.getLong(localizedValue)) {
						return objectField;
					}
				}
			}
		}

		return null;
	}

}