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

import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupWrapper;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserWrapper;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
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
public class ExpandoBridgeTest {

	@ClassRule
	@Rule
	public static final HypersonicServerTestRule hypersonicServerTestRule =
		new HypersonicServerTestRule("lportal");

	@ClassRule
	@Rule
	public static final PACLTestRule paclTestRule = new PACLTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		List<String> hypersonicJdbcProps =
			hypersonicServerTestRule.getJdbcProperties();

		for (String property : hypersonicJdbcProps) {
			String[] propsArray = StringUtil.split(property, StringPool.EQUAL);

			if (propsArray.length == 2) {
				System.setProperty(propsArray[0], propsArray[1]);
			}
			else {
				System.setProperty(propsArray[0], "");
			}
		}
	}

	@Test
	public void test1() throws Exception {
		try {
			Group group = GroupLocalServiceUtil.getCompanyGroup(
				TestPropsValues.getCompanyId());

			group.getExpandoBridge();

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void test2() throws Exception {
		try {
			Group group = GroupLocalServiceUtil.getCompanyGroup(
				TestPropsValues.getCompanyId());

			ServiceContext serviceContext = new ServiceContext();

			group.setExpandoBridgeAttributes(serviceContext);

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void test3() throws Exception {
		try {
			Group group = GroupLocalServiceUtil.getCompanyGroup(
				TestPropsValues.getCompanyId());

			group = new GroupWrapper(group);

			ServiceContext serviceContext = new ServiceContext();

			group.setExpandoBridgeAttributes(serviceContext);

			Assert.fail();
		}
		catch (SecurityException se) {
		}
	}

	@Test
	public void test4() throws Exception {
		User user = TestPropsValues.getUser();

		user.getExpandoBridge();
	}

	@Test
	public void test5() throws Exception {
		User user = TestPropsValues.getUser();

		ServiceContext serviceContext = new ServiceContext();

		user.setExpandoBridgeAttributes(serviceContext);
	}

	@Test
	public void test6() throws Exception {
		User user = TestPropsValues.getUser();

		user = new UserWrapper(user);

		user.getExpandoBridge();
	}

	@Test
	public void test7() throws Exception {
		User user = TestPropsValues.getUser();

		user = new UserWrapper(user);

		ServiceContext serviceContext = new ServiceContext();

		user.setExpandoBridgeAttributes(serviceContext);
	}

}