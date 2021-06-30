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

package com.liferay.commerce.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.comparator.UserFirstNameComparator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import java.util.List;

import org.frutilla.FrutillaRule;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Riccardo Alberti
 */
@RunWith(Arquillian.class)
public class UserReindexTest {

	@ClassRule
	@Rule
	public static AggregateTestRule aggregateTestRule = new AggregateTestRule(
		new LiferayIntegrationTestRule(),
		PermissionCheckerMethodTestRule.INSTANCE);

	@Test
	public void testCheckUserIsVisibleWithoutReindex() throws Exception {
		frutillaRule.scenario(
			"Users are available in the index"
		).given(
			"Portal is successfully started"
		).when(
			"I search for users using the index"
		).then(
			"I should see the Test Test user"
		);

		Company company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		List<User> companyUsers = UserLocalServiceUtil.getCompanyUsers(
			company.getCompanyId(), -1, -1);

		Assert.assertNotNull(companyUsers);

		Assert.assertEquals(companyUsers.toString(), 1, companyUsers.size());

		List<User> indexUsers = UserLocalServiceUtil.search(
			company.getCompanyId(), StringPool.BLANK,
			WorkflowConstants.STATUS_ANY, null, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, new UserFirstNameComparator());

		Assert.assertNotNull(indexUsers);

		Assert.assertEquals(indexUsers.toString(), 1, indexUsers.size());

		Assert.assertEquals(companyUsers.get(0), indexUsers.get(0));
	}

	@Rule
	public FrutillaRule frutillaRule = new FrutillaRule();
}