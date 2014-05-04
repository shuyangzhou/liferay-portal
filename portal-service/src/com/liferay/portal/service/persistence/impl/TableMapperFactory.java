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

package com.liferay.portal.service.persistence.impl;

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.service.persistence.BasePersistence;

/**
 * @author Shuyang Zhou
 */
public class TableMapperFactory {

	public static
		<L extends BaseModel<L>, R extends BaseModel<R>> TableMapper<L, R>
			getTableMapper(
				String tableName, String leftColumnName, String rightColumnName,
				BasePersistence<L> leftPersistence,
				BasePersistence<R> rightPersistence) {

		TableMapperImpl<L, R> tableMapperImpl =
			new TableMapperImpl<L, R>(
				tableName, leftColumnName, rightColumnName, leftPersistence,
				rightPersistence);

		tableMapperImpl.setReverseTableMapper(
			new ReverseTableMapper<R, L>(tableMapperImpl));

		return tableMapperImpl;
	}

}