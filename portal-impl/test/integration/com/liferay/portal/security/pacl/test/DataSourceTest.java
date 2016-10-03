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

package com.liferay.portal.security.pacl.test;

import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.test.rule.PACLTestRule;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.jdbc.datasource.DelegatingDataSource;

/**
 * @author Raymond Augé
 */
public class DataSourceTest {

	@ClassRule
	@Rule
	public static final PACLTestRule paclTestRule = new PACLTestRule();

	@Test
	public void testDataSource() throws Exception {
		DelegatingDataSource delegatingDataSource =
			(DelegatingDataSource)InfrastructureUtil.getDataSource();

		DataSource targetDataSource =
			delegatingDataSource.getTargetDataSource();

		Class<?> targetDataSourceClass = targetDataSource.getClass();

		Assert.assertEquals(
			"com.liferay.portal.security.pacl.dao.jdbc.PACLDataSource",
			targetDataSourceClass.getName());
	}

}