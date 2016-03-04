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

import com.liferay.dynamic.data.mapping.model.DDMForm;

import java.lang.annotation.Annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public class DDMFormFactory {

	public static DDMForm create(Class<?> clazz) {
		if (!clazz.isAnnotationPresent(_DDM_FORM_ANNOTATION)) {
			throw new IllegalArgumentException(
				"Unsupported class " + clazz.getName());
		}

		DDMFormFactoryHelper ddmFormFactoryHelper = _ddmFormFactoryHelpers.get(
			clazz);

		if (ddmFormFactoryHelper == null) {
			ddmFormFactoryHelper = new DDMFormFactoryHelper(clazz);

			_ddmFormFactoryHelpers.put(clazz, ddmFormFactoryHelper);
		}

		return ddmFormFactoryHelper.createDDMForm();
	}

	private static final Class<? extends Annotation> _DDM_FORM_ANNOTATION =
		com.liferay.dynamic.data.mapping.annotations.DDMForm.class;

	private static final Map<Class<?>, DDMFormFactoryHelper>
		_ddmFormFactoryHelpers = new HashMap<>();

}