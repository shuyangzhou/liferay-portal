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

import com.liferay.portal.cache.SingleVMPoolImpl;
import com.liferay.portal.cache.test.TestPortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;

import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Shuyang Zhou
 */
public class SQLQueryTableNamesUtilTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<>();

		properties.put(
			"portal.cache.manager.name", PortalCacheManagerNames.SINGLE_VM);
		properties.put(
			"portal.cache.manager.type",
			PropsValues.PORTAL_CACHE_MANAGER_TYPE_SINGLE_VM);

		registry.registerService(
			PortalCacheManager.class,
			TestPortalCacheManager.createTestPortalCacheManager(
				PortalCacheManagerNames.SINGLE_VM),
			properties);

		SingleVMPoolUtil singleVMPoolUtil = new SingleVMPoolUtil();

		singleVMPoolUtil.setSingleVMPool(new SingleVMPoolImpl());
	}

	@Test
	public void testConstructor() {
		new SQLQueryTableNamesUtil();
	}

	@Test
	public void testGetTableNames() throws Exception {
		try (UnsyncBufferedReader unsyncBufferedReader =
				new UnsyncBufferedReader(
					new InputStreamReader(
						SQLQueryTableNamesUtilTest.class.getResourceAsStream(
							"dependencies/sql.txt")))) {

			String line = null;

			while ((line = unsyncBufferedReader.readLine()) != null) {
				int index = line.indexOf(CharPool.POUND);

				if (index == -1) {
					continue;
				}

				String sql = line.substring(0, index);

				String[] expectedTableNames = StringUtil.split(
					line.substring(index + 1), CharPool.COMMA);

				String[] actualTableNames =
					SQLQueryTableNamesUtil.getTableNames(sql);

				Arrays.sort(actualTableNames);

				Assert.assertArrayEquals(
					"For SQL " + sql, expectedTableNames, actualTableNames);

				// Access from cache

				actualTableNames = SQLQueryTableNamesUtil.getTableNames(sql);

				Arrays.sort(actualTableNames);

				Assert.assertArrayEquals(
					"For SQL " + sql, expectedTableNames, actualTableNames);
			}
		}
	}

}