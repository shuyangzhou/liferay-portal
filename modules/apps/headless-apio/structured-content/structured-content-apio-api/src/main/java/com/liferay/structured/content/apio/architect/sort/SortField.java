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

package com.liferay.structured.content.apio.architect.sort;

import com.liferay.structured.content.apio.architect.entity.EntityField;

import java.io.Serializable;

import java.util.Locale;

/**
 * Models a Sort Field.
 *
 * @author Cristina González
 * @review
 */
public class SortField implements Serializable {

	/**
	 * Creates a new sort field.
	 *
	 * @param  entityField - the entity field
	 * @param  asc - if the sort should be ascending
	 * @review
	 */
	public SortField(EntityField entityField, boolean asc) {
		if (entityField == null) {
			throw new IllegalArgumentException("Entity field is null");
		}

		_asc = asc;
		_entityField = entityField;
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #SortField(EntityField, boolean)}
	 */
	@Deprecated
	public SortField(String fieldName, boolean asc) {
		throw new UnsupportedOperationException(
			"This constructor is deprecated and replaced by #SortField(" +
				"EntityField, boolean)");
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #getSortableFieldName}
	 */
	@Deprecated
	public String getFieldName() {
		throw new UnsupportedOperationException(
			"This method is deprecated and replaced by #getSortableFieldName");
	}

	/**
	 * Returns the name of the field.
	 *
	 * @param  locale - the locale
	 * @return - the name of the field
	 * @review
	 */
	public String getSortableFieldName(Locale locale) {
		return _entityField.getSortableName(locale);
	}

	/**
	 * Returns if the sort field is ascending or not.
	 *
	 * @return - if the sort field is ascending or not
	 * @review
	 */
	public boolean isAscending() {
		return _asc;
	}

	private final boolean _asc;
	private final EntityField _entityField;

}