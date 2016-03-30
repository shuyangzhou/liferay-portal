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
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldType;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.FieldConstants;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Kamesh Sampath
 * @author Raymond Augé
 * @author Marcellus Tavares
 */
public class ConfigurationModelToDDMFormConverter {

	public static DDMForm getDDMForm(
		ConfigurationModel configurationModel, Locale locale,
		ResourceBundle resourceBundle) {

		DDMForm ddmForm = new DDMForm();

		ddmForm.addAvailableLocale(locale);
		ddmForm.setDefaultLocale(locale);

		AttributeDefinition[] attributeDefinitions =
			configurationModel.getAttributeDefinitions(
				ObjectClassDefinition.REQUIRED);

		if (attributeDefinitions != null) {
			addDDMFormFields(
				attributeDefinitions, ddmForm, true, locale, resourceBundle);
		}

		attributeDefinitions = configurationModel.getAttributeDefinitions(
			ObjectClassDefinition.OPTIONAL);

		if (attributeDefinitions != null) {
			addDDMFormFields(
				attributeDefinitions, ddmForm, false, locale, resourceBundle);
		}

		return ddmForm;
	}

	protected static void addDDMFormFields(
		AttributeDefinition[] attributeDefinitions, DDMForm ddmForm,
		boolean required, Locale locale, ResourceBundle resourceBundle) {

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String type = getDDMFormFieldType(attributeDefinition);

			DDMFormField ddmFormField = new DDMFormField(
				attributeDefinition.getID(), type);

			String dataType = getDDMFormFieldDataType(attributeDefinition);

			ddmFormField.setDataType(dataType);

			LocalizedValue label = new LocalizedValue(locale);

			label.addString(
				locale,
				translate(attributeDefinition.getName(), resourceBundle));

			ddmFormField.setLabel(label);

			setDDMFormFieldOptions(
				attributeDefinition, ddmFormField, locale, resourceBundle);
			setDDMFormFieldPredefinedValue(
				ddmFormField, locale, resourceBundle);

			LocalizedValue tip = new LocalizedValue(locale);

			tip.addString(
				locale,
				translate(
					attributeDefinition.getDescription(), resourceBundle));

			ddmFormField.setTip(tip);

			ddmFormField.setLocalizable(true);

			if (!DDMFormFieldType.CHECKBOX.equals(ddmFormField.getType())) {
				ddmFormField.setRequired(required);
			}

			ddmFormField.setShowLabel(true);

			if (attributeDefinition.getCardinality() != 0) {
				ddmFormField.setRepeatable(true);
			}

			if (Validator.equals(dataType, FieldConstants.STRING)) {
				ddmFormField.setProperty("displayStyle", "multiline");
			}

			ddmForm.addDDMFormField(ddmFormField);
		}
	}

	protected static String getDDMFormFieldDataType(
		AttributeDefinition attributeDefinition) {

		int type = attributeDefinition.getType();

		if (type == AttributeDefinition.BOOLEAN) {
			return FieldConstants.BOOLEAN;
		}
		else if (type == AttributeDefinition.DOUBLE) {
			return FieldConstants.DOUBLE;
		}
		else if (type == AttributeDefinition.FLOAT) {
			return FieldConstants.FLOAT;
		}
		else if (type == AttributeDefinition.INTEGER) {
			return FieldConstants.INTEGER;
		}
		else if (type == AttributeDefinition.LONG) {
			return FieldConstants.LONG;
		}
		else if (type == AttributeDefinition.SHORT) {
			return FieldConstants.SHORT;
		}

		return FieldConstants.STRING;
	}

	protected static String getDDMFormFieldPredefinedValue(String dataType) {
		if (dataType.equals(FieldConstants.BOOLEAN)) {
			return "false";
		}
		else if (dataType.equals(FieldConstants.DOUBLE) ||
				 dataType.equals(FieldConstants.FLOAT)) {

			return "0.0";
		}
		else if (dataType.equals(FieldConstants.INTEGER) ||
				 dataType.equals(FieldConstants.LONG) ||
				 dataType.equals(FieldConstants.SHORT)) {

			return "0";
		}

		return StringPool.BLANK;
	}

	protected static String getDDMFormFieldType(
		AttributeDefinition attributeDefinition) {

		int type = attributeDefinition.getType();

		if (type == AttributeDefinition.BOOLEAN) {
			String[] optionLabels = attributeDefinition.getOptionLabels();

			if (ArrayUtil.isEmpty(optionLabels)) {
				return DDMFormFieldType.CHECKBOX;
			}

			return DDMFormFieldType.RADIO;
		}

		if (ArrayUtil.isNotEmpty(attributeDefinition.getOptionLabels()) ||
			ArrayUtil.isNotEmpty(attributeDefinition.getOptionValues())) {

			return DDMFormFieldType.SELECT;
		}

		return DDMFormFieldType.TEXT;
	}

	protected static void setDDMFormFieldOptions(
		AttributeDefinition attributeDefinition, DDMFormField ddmFormField,
		Locale locale, ResourceBundle resourceBundle) {

		DDMFormFieldOptions ddmFormFieldOptions = new DDMFormFieldOptions();

		String[] optionLabels = attributeDefinition.getOptionLabels();
		String[] optionValues = attributeDefinition.getOptionValues();

		if ((optionLabels != null) && (optionValues != null)) {
			for (int i = 0; i < optionLabels.length; i++) {
				ddmFormFieldOptions.addOptionLabel(
					optionValues[i], locale,
					translate(optionLabels[i], resourceBundle));
			}
		}

		ddmFormField.setDDMFormFieldOptions(ddmFormFieldOptions);
	}

	protected static void setDDMFormFieldPredefinedValue(
		DDMFormField ddmFormField, Locale locale,
		ResourceBundle resourceBundle) {

		String dataType = ddmFormField.getDataType();

		String predefinedValueString = getDDMFormFieldPredefinedValue(dataType);

		String type = ddmFormField.getType();

		if (type.equals(DDMFormFieldType.SELECT)) {
			predefinedValueString = "[\"" + predefinedValueString + "\"]";
		}

		LocalizedValue predefinedValue = new LocalizedValue(locale);

		predefinedValue.addString(
			locale, translate(predefinedValueString, resourceBundle));

		ddmFormField.setPredefinedValue(predefinedValue);
	}

	protected static String translate(
		String key, ResourceBundle resourceBundle) {

		if ((resourceBundle == null) || (key == null)) {
			return key;
		}

		String value = ResourceBundleUtil.getString(resourceBundle, key);

		if (value == null) {
			return key;
		}

		return value;
	}

}