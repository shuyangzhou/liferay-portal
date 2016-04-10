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

package com.liferay.configuration.admin.web.util;

import com.liferay.configuration.admin.web.model.ConfigurationModel;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.storage.FieldConstants;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.ReflectionUtil;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import org.osgi.service.metatype.AttributeDefinition;

/**
 * @author Kamesh Sampath
 * @author Raymond Augé
 * @author Marcellus Tavares
 */
public class DDMFormValuesToPropertiesConverter {

	public static Map<String, Object> getProperties(
		ConfigurationModel configurationModel, DDMFormValues ddmFormValues,
		Locale locale) {

		DDMForm ddmForm = ddmFormValues.getDDMForm();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(false);

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValuesMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Map<String, Object> properties = new HashMap<>();

		AttributeDefinition[] attributeDefinitions =
			configurationModel.getAttributeDefinitions(ConfigurationModel.ALL);

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			Object value = null;

			List<DDMFormFieldValue> ddmFormFieldValues =
				ddmFormFieldValuesMap.get(attributeDefinition.getID());

			if (attributeDefinition.getCardinality() == 0) {
				value = toSimpleValue(
					ddmFormFieldValues.get(0), ddmFormFieldsMap, locale);
			}
			else if (attributeDefinition.getCardinality() > 0) {
				value = toArrayValue(
					ddmFormFieldValues, ddmFormFieldsMap, locale);
			}
			else if (attributeDefinition.getCardinality() < 0) {
				value = toVectorValue(
					ddmFormFieldValues, ddmFormFieldsMap, locale);
			}

			properties.put(attributeDefinition.getID(), value);
		}

		return properties;
	}

	protected static String getDDMFormFieldDataType(
		String fieldName, Map<String, DDMFormField> ddmFormFieldsMap) {

		DDMFormField ddmFormField = ddmFormFieldsMap.get(fieldName);

		return ddmFormField.getDataType();
	}

	protected static String getDDMFormFieldType(
		String fieldName, Map<String, DDMFormField> ddmFormFieldsMap) {

		DDMFormField ddmFormField = ddmFormFieldsMap.get(fieldName);

		return ddmFormField.getType();
	}

	protected static String getDDMFormFieldValueString(
		DDMFormFieldValue ddmFormFieldValue,
		Map<String, DDMFormField> ddmFormFieldsMap, Locale locale) {

		Value value = ddmFormFieldValue.getValue();

		String valueString = value.getString(locale);

		String type = getDDMFormFieldType(
			ddmFormFieldValue.getName(), ddmFormFieldsMap);

		if (type.equals(DDMFormFieldType.SELECT)) {
			try {
				JSONArray jsonArray = JSONFactoryUtil.createJSONArray(
					valueString);

				if (jsonArray.length() == 1) {
					valueString = jsonArray.getString(0);
				}
			}
			catch (JSONException jsone) {
				ReflectionUtil.throwException(jsone);
			}
		}

		return valueString;
	}

	protected static Serializable toArrayValue(
		List<DDMFormFieldValue> ddmFormFieldValues,
		Map<String, DDMFormField> ddmFormFieldsMap, Locale locale) {

		DDMFormFieldValue ddmFormFieldValue = ddmFormFieldValues.get(0);

		String dataType = getDDMFormFieldDataType(
			ddmFormFieldValue.getName(), ddmFormFieldsMap);

		Vector<Serializable> values = toVectorValue(
			ddmFormFieldValues, ddmFormFieldsMap, locale);

		return FieldConstants.getSerializable(dataType, values);
	}

	protected static Serializable toSimpleValue(
		DDMFormFieldValue ddmFormFieldValue,
		Map<String, DDMFormField> ddmFormFieldsMap, Locale locale) {

		String dataType = getDDMFormFieldDataType(
			ddmFormFieldValue.getName(), ddmFormFieldsMap);

		String valueString = getDDMFormFieldValueString(
			ddmFormFieldValue, ddmFormFieldsMap, locale);

		return FieldConstants.getSerializable(dataType, valueString);
	}

	protected static Vector<Serializable> toVectorValue(
		List<DDMFormFieldValue> ddmFormFieldValues,
		Map<String, DDMFormField> ddmFormFieldsMap, Locale locale) {

		Vector<Serializable> values = new Vector<>(ddmFormFieldValues.size());

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			values.add(
				toSimpleValue(ddmFormFieldValue, ddmFormFieldsMap, locale));
		}

		return values;
	}

}