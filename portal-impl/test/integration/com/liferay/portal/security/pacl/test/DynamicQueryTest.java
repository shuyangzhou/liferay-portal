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

import static com.liferay.portal.security.pacl.test.ExpandoBridgeTest.hypersonicServerTestRule;

import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.rule.HypersonicServerTestRule;
import com.liferay.portal.test.rule.PACLTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class DynamicQueryTest {

	@ClassRule
	@Rule
	public static final HypersonicServerTestRule hypersonicServerTestRule =
		new HypersonicServerTestRule("lportal");

	@ClassRule
	@Rule
	public static final PACLTestRule paclTestRule = new PACLTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		DB db = DBFactoryUtil.getDB();

		String dbType = db.getType();

		if (dbType.equals(DB.TYPE_HYPERSONIC)) {
			List<String> hypersonicJdbcProps =
				hypersonicServerTestRule.getJdbcProperties();

			for (String property : hypersonicJdbcProps) {
				String[] propsArray = StringUtil.split(
					property, StringPool.EQUAL);

				if (propsArray.length == 2) {
					System.setProperty(propsArray[0], propsArray[1]);
				}
				else {
					System.setProperty(propsArray[0], "");
				}
			}
		}
	}

	@Test
	public void test1() throws Exception {
		try {
			DynamicQueryFactoryUtil.forClass(Group.class);

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void test2() throws Exception {
		try {
			DynamicQueryFactoryUtil.forClass(Role.class);

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void test3() throws Exception {
		GroupLocalServiceUtil.dynamicQuery();
	}

	@Test
	public void test4() throws Exception {
		try {
			UserLocalServiceUtil.dynamicQuery();

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

}