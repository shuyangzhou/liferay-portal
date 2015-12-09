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

package com.liferay.portal.dao.orm.common;

import com.liferay.portal.kernel.dao.db.DBFactory;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.util.InitUtil;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.junit.Before;

/**
 * @author Miguel Pastor
 */
public abstract class BaseSQLTransformerTestCase {

	@Before
	public void setUp() {
		ServiceLoader<DBFactory> serviceLoader = ServiceLoader.load(
			DBFactory.class, InitUtil.class.getClassLoader());

		Iterator<DBFactory> iterator = serviceLoader.iterator();

		DBFactoryUtil.setDBFactory(iterator.next());

		DBFactoryUtil.setDB(getDBType(), null);
	}

	protected abstract String getDBType();

	protected String transformSQL(String sql) {
		return SQLTransformer.transform(sql);
	}

}