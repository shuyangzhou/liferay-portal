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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.FieldUtils;

/**
 * @author Mariano Alvaro Saiz
 */
public class MVCCReturnedOneLevelWrapperSynchronizer
	extends MVCCReturnedObjectSynchronizer {

	@Override
	public Object updateResult(Object result) {
		return _getOneLevelWrapperUpdatedResult(result);
	}

	private Object _getOneLevelWrapperUpdatedResult(Object returnValue) {
		if (!(returnValue instanceof MVCCModel) ||
			returnValue instanceof BaseModel<?>) {

			return returnValue;
		}

		Class<?> clazz = returnValue.getClass();

		Field[] fields = clazz.getDeclaredFields();

		if (fields.length != 1) {
			return returnValue;
		}

		Field field = fields[0];

		Constructor<?> constructor =
			ConstructorUtils.getMatchingAccessibleConstructor(
				clazz, new Class<?>[] {field.getType()});

		try {
			if (constructor != null) {
				Object fieldValue = FieldUtils.readDeclaredField(
					returnValue, field.getName(), true);

				Object newFieldValue = getUpdatedResult(fieldValue);

				if (newFieldValue != fieldValue) {
					return constructor.newInstance(fieldValue);
				}
			}
		}
		catch (Exception e) {
		}

		return returnValue;
	}

}