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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.UnlocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Mateus Santana
 */
public class DDMFormValuesConverterUtil {

	public static List<DDMFormFieldValue> addMissingDDMFormFieldValues(
		Collection<DDMFormField> ddmFormFields,
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValues) {

		List<DDMFormFieldValue> newDDMFormFieldValues = new ArrayList<>();

		for (DDMFormField ddmFormField : ddmFormFields) {
			Queue<DDMFormField> queue = new LinkedList<>();

			queue.add(ddmFormField);

			DDMFormField currentDDMFormField = null;

			Map<String, String> parentMap = new HashMap<>();

			Map<String, DDMFormFieldValue> ddmFormFieldValueMap =
				new HashMap<>();

			while ((currentDDMFormField = queue.poll()) != null) {
				String currentDDMFormFieldName = currentDDMFormField.getName();

				DDMFormFieldValue currentDDMFormFieldValue =
					_extractDDMFormFieldValue(
						ddmFormFieldValues, currentDDMFormField);

				ddmFormFieldValueMap.put(
					currentDDMFormFieldName, currentDDMFormFieldValue);

				String parentDDMFormFieldName = parentMap.get(
					currentDDMFormFieldName);

				if (parentDDMFormFieldName == null) {
					newDDMFormFieldValues.add(currentDDMFormFieldValue);
				}
				else {
					DDMFormFieldValue parentDDMFormFieldValue =
						ddmFormFieldValueMap.get(parentDDMFormFieldName);

					parentDDMFormFieldValue.addNestedDDMFormFieldValue(
						currentDDMFormFieldValue);
				}

				if (StringUtil.equals(
						ddmFormField.getType(),
						DDMFormFieldTypeConstants.FIELDSET)) {

					for (DDMFormField nestedDDMFormField :
							currentDDMFormField.getNestedDDMFormFields()) {

						parentMap.put(
							nestedDDMFormField.getName(),
							currentDDMFormFieldName);

						queue.add(nestedDDMFormField);
					}
				}
			}
		}

		return newDDMFormFieldValues;
	}

	private static DDMFormFieldValue _createDefaultDDMFormFieldValue(
		DDMFormField ddmFormField) {

		DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

		ddmFormFieldValue.setInstanceId(StringUtil.randomString());
		ddmFormFieldValue.setName(ddmFormField.getName());

		if (ddmFormField.isLocalizable()) {
			ddmFormFieldValue.setValue(new LocalizedValue());
		}
		else {
			ddmFormFieldValue.setValue(new UnlocalizedValue((String)null));
		}

		return ddmFormFieldValue;
	}

	private static DDMFormFieldValue _extractDDMFormFieldValue(
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValues,
		DDMFormField ddmFormField) {

		List<DDMFormFieldValue> ddmFormFieldValueList =
			ddmFormFieldValues.remove(ddmFormField.getName());

		if (ListUtil.isEmpty(ddmFormFieldValueList)) {
			return _createDefaultDDMFormFieldValue(ddmFormField);
		}

		if (ddmFormFieldValueList.size() == 1) {
			DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValueList.get(0);

			List<DDMFormFieldValue> nestedDDMFormFieldValues =
				ddmFormFieldValue.getNestedDDMFormFieldValues();

			nestedDDMFormFieldValues.clear();

			return ddmFormFieldValue;
		}

		DDMFormFieldValue defaultDDMFormFieldValue =
			_createDefaultDDMFormFieldValue(ddmFormField);

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValueList) {
			List<DDMFormFieldValue> nestedDDMFormFieldValues =
				ddmFormFieldValue.getNestedDDMFormFieldValues();

			nestedDDMFormFieldValues.clear();

			defaultDDMFormFieldValue.addNestedDDMFormFieldValue(
				ddmFormFieldValue);
		}

		return defaultDDMFormFieldValue;
	}

}