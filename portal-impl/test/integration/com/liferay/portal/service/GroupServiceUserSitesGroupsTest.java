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

package com.liferay.portal.service;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.util.test.LayoutTestUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class GroupServiceUserSitesGroupsTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Test
	public void testCompanyGroup() throws Exception {
		_user = UserTestUtil.addUser();

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Group group = GroupLocalServiceUtil.getCompanyGroup(
			_user.getCompanyId());

		Assert.assertTrue(groups.contains(group));
	}

	@Test
	public void testControlPanelGroup() throws Exception {
		_user = UserTestUtil.addUser();

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), RoleConstants.ADMINISTRATOR);

		UserLocalServiceUtil.addRoleUser(role.getRoleId(), _user);

		Group controlPanelGroup = GroupLocalServiceUtil.getGroup(
			_user.getCompanyId(), GroupConstants.CONTROL_PANEL);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(controlPanelGroup));

		groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, true, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(controlPanelGroup));
	}

	@Test
	public void testGroupsOrder() throws Exception {
		_group = GroupTestUtil.addGroup();

		LayoutTestUtil.addLayout(_group);

		_user = UserTestUtil.addGroupAdminUser(_group);

		Role administratorRole = RoleLocalServiceUtil.getRole(
			_user.getCompanyId(), RoleConstants.ADMINISTRATOR);

		RoleLocalServiceUtil.addUserRole(
			_user.getUserId(), administratorRole.getRoleId());

		Organization organization = OrganizationTestUtil.addOrganization(true);

		_organizations.add(organization);

		Group organizationGroup = organization.getGroup();

		LayoutTestUtil.addLayout(organizationGroup);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(), new long[] {_user.getUserId()});

		Group controlPanelGroup = GroupLocalServiceUtil.getGroup(
			_user.getCompanyId(), GroupConstants.CONTROL_PANEL);

		Group userGroup = _user.getGroup();

		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			_user.getCompanyId());

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, true, QueryUtil.ALL_POS);

		try {
			Assert.assertEquals(5, groups.size());

			Assert.assertEquals(controlPanelGroup, groups.get(0));
			Assert.assertEquals(userGroup, groups.get(1));
			Assert.assertEquals(organizationGroup, groups.get(2));
			Assert.assertEquals(companyGroup, groups.get(3));
			Assert.assertEquals(_group, groups.get(4));
		}
		finally {
			UserLocalServiceUtil.unsetOrganizationUsers(
				organization.getOrganizationId(),
				new long[] {_user.getUserId()});
		}
	}

	@Test
	public void testGroupsWithoutLayouts() throws Exception {
		_group = GroupTestUtil.addGroup();

		_user = UserTestUtil.addGroupUser(_group, RoleConstants.USER);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(_group));
	}

	@Test
	public void testInactiveGroups() throws Exception {
		_group = GroupTestUtil.addGroup();

		LayoutTestUtil.addLayout(_group);

		_user = UserTestUtil.addGroupUser(_group, RoleConstants.USER);

		_group.setActive(false);

		GroupLocalServiceUtil.updateGroup(_group);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(_group));
	}

	@Test
	public void testInactiveOrganizationGroup() throws Exception {
		_user = UserTestUtil.addUser();

		Organization organization = OrganizationTestUtil.addOrganization(true);

		_organizations.push(organization);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(), new long[] {_user.getUserId()});

		Group group = organization.getGroup();

		LayoutTestUtil.addLayout(group);

		group.setActive(false);

		GroupLocalServiceUtil.updateGroup(group);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(organization.getGroup()));
	}

	@Test
	public void testOrganizationAncestorsGroups() throws Exception {
		_user = UserTestUtil.addUser();

		Organization parentOrganization = OrganizationTestUtil.addOrganization(
			true);

		Group parentOrganizationGroup = parentOrganization.getGroup();

		_organizations.push(parentOrganization);

		Organization organization = OrganizationTestUtil.addOrganization(
			parentOrganization.getOrganizationId(),
			RandomTestUtil.randomString(), false);

		_organizations.push(organization);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(), new long[] {_user.getUserId()});

		try {
			List<Group> groups = GroupServiceUtil.getUserSitesGroups(
				_user.getUserId(), null, false, QueryUtil.ALL_POS);

			Assert.assertFalse(groups.contains(parentOrganizationGroup));
			Assert.assertFalse(groups.contains(organization.getGroup()));

			LayoutTestUtil.addLayout(parentOrganizationGroup);

			groups = GroupServiceUtil.getUserSitesGroups(
				_user.getUserId(), null, false, QueryUtil.ALL_POS);

			Assert.assertTrue(groups.contains(parentOrganizationGroup));
			Assert.assertFalse(groups.contains(organization.getGroup()));
		}
		finally {
			UserLocalServiceUtil.unsetOrganizationUsers(
				organization.getOrganizationId(),
				new long[] {_user.getUserId()});
		}
	}

	@Test
	public void testOrganizationGroups() throws Exception {
		_user = UserTestUtil.addUser();

		Organization organization = OrganizationTestUtil.addOrganization(true);

		_organizations.push(organization);

		UserLocalServiceUtil.addGroupUser(organization.getGroupId(), _user);

		LayoutTestUtil.addLayout(organization.getGroup());

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(organization.getGroup()));
	}

	@Test
	public void testOrganizationWithoutLayouts() throws Exception {
		_user = UserTestUtil.addUser();

		Organization organization = OrganizationTestUtil.addOrganization(true);

		_organizations.push(organization);

		UserLocalServiceUtil.addGroupUser(organization.getGroupId(), _user);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(organization.getGroup()));
	}

	@Test
	public void testUserPersonalSite() throws Exception {
		_user = UserTestUtil.addUser();

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(_user.getGroup()));
	}

	@Test
	public void testUsersGroups() throws Exception {
		_group = GroupTestUtil.addGroup();

		LayoutTestUtil.addLayout(_group);

		_user = UserTestUtil.addGroupUser(_group, RoleConstants.USER);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(_group));
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private final Deque<Organization> _organizations = new ArrayDeque<>();

	@DeleteAfterTestRun
	private User _user;

}