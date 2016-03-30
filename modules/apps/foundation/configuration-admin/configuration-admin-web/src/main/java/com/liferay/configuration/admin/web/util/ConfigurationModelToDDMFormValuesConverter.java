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
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.AttributeDefinition;

/**
 * @author Marcellus Tavares
 */
public class ConfigurationModelToDDMFormValuesConverter {

	public static DDMFormValues getDDMFormValues(
		ConfigurationModel configurationModel, DDMForm ddmForm, Locale locale,
		ResourceBundle resourceBundle) {

		DDMFormValues ddmFormValues = new DDMFormValues(ddmForm);

		ddmFormValues.addAvailableLocale(locale);
		ddmFormValues.setDefaultLocale(locale);

		addDDMFormFieldValues(
			configurationModel, ddmFormValues, locale, resourceBundle);

		return ddmFormValues;
	}

	protected static void addDDMFormFieldValues(
		ConfigurationModel configurationModel, DDMFormValues ddmFormValues,
		Locale locale, ResourceBundle resourceBundle) {

		AttributeDefinition[] attributeDefinitions =
			configurationModel.getAttributeDefinitions(ConfigurationModel.ALL);

		if (attributeDefinitions == null) {
			return;
		}

		DDMForm ddmForm = ddmFormValues.getDDMForm();

		Map<String, DDMFormField> ddmFormFieldsMap =
			ddmForm.getDDMFormFieldsMap(false);

		Configuration configuration = configurationModel.getConfiguration();

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String[] values = null;

			if (configuration != null) {
				values = AttributeDefinitionUtil.getProperty(
					attributeDefinition, configuration);
			}
			else {
				values = AttributeDefinitionUtil.getDefaultValue(
					attributeDefinition);
			}

			for (String value : values) {
				DDMFormFieldValue ddmFormFieldValue = new DDMFormFieldValue();

				ddmFormFieldValue.setName(attributeDefinition.getID());
				ddmFormFieldValue.setInstanceId(StringUtil.randomString());

				setDDMFormFieldValueLocalizedValue(
					value, ddmFormFieldValue, ddmFormFieldsMap, locale,
					resourceBundle);

				ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
			}
		}
	}

	protected static void setDDMFormFieldValueLocalizedValue(
		String value, DDMFormFieldValue ddmFormFieldValue,
		Map<String, DDMFormField> ddmFormFieldsMap, Locale locale,
		ResourceBundle resourceBundle) {

		DDMFormField ddmFormField = ddmFormFieldsMap.get(
			ddmFormFieldValue.getName());

		String type = ddmFormField.getType();

		if (type.equals(DDMFormFieldType.SELECT)) {
			value = "[\"" + value + "\"]";
		}

		LocalizedValue localizedValue = new LocalizedValue(locale);

		if ((resourceBundle != null) && (value != null)) {
			String resourceBundleValue = null;

			resourceBundleValue = ResourceBundleUtil.getString(
				resourceBundle, value);

			if (resourceBundleValue != null) {
				value = resourceBundleValue;
			}
		}

		localizedValue.addString(locale, value);

		ddmFormFieldValue.setValue(localizedValue);
	}

}