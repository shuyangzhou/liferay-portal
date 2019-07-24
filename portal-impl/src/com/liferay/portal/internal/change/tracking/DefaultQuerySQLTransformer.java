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

package com.liferay.portal.internal.change.tracking;

import com.liferay.portal.kernel.util.ServiceProxyFactory;

import org.hibernate.engine.QuerySQLTransformer;

/**
 * @author Preston Crary
 */
public class DefaultQuerySQLTransformer implements QuerySQLTransformer {

	@Override
	public String transform(String sql) {
		return _querySQLTransformer.transform(sql);
	}

	private static volatile QuerySQLTransformer _querySQLTransformer =
		ServiceProxyFactory.newServiceTrackedInstance(
			QuerySQLTransformer.class, DefaultQuerySQLTransformer.class,
			"_querySQLTransformer", true);

}