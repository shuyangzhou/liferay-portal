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
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.AdvancedPermissionChecker;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
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
 * @author Julio Camarero
 * @author Roberto Díaz
 * @author Sergio González
 */
@Sync(cleanTransaction = true)
public class GroupServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Test
	public void testAddPermissionsCustomRole() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		givePermissionToManageSubsites(_user, group);

		testGroup(
			_user, group, null, null, true, false, false, false, true, true,
			true);
	}

	@Test
	public void testAddPermissionsCustomRoleInSubsite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		_user = UserTestUtil.addUser(null, group11.getGroupId());

		givePermissionToManageSubsites(_user, group11);

		testGroup(
			_user, group1, group11, null, true, false, false, false, false,
			true, true);
	}

	@Test
	public void testAddPermissionsRegularUser() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		testGroup(
			_user, group, null, null, true, false, false, false, false, false,
			false);
	}

	@Test
	public void testAddPermissionsSiteAdmin() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		giveSiteAdminRole(_user, group);

		testGroup(
			_user, group, null, null, true, false, true, false, true, true,
			true);
	}

	@Test
	public void testAddPermissionsSubsiteAdmin() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		_user = UserTestUtil.addUser(null, group11.getGroupId());

		giveSiteAdminRole(_user, group11);

		testGroup(
			_user, group1, group11, null, true, false, false, true, false, true,
			true);
	}

	@Test
	public void testGetUserSitesGroupsControlPanelGroup() throws Exception {
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

		initUserPermissionCheckerBag(_user);

		List<Group> permissionCacheGroups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, true, QueryUtil.ALL_POS);

		Assert.assertEquals(groups.size(), permissionCacheGroups.size());
		Assert.assertTrue(groups.containsAll(permissionCacheGroups));
	}

	@Test
	public void testGetUserSitesGroupsOrder() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addGroupAdminUser(group);

		Role administratorRole = RoleLocalServiceUtil.getRole(
			_user.getCompanyId(), RoleConstants.ADMINISTRATOR);

		RoleLocalServiceUtil.addUserRole(
			_user.getUserId(), administratorRole.getRoleId());

		Organization organization = OrganizationTestUtil.addOrganization(true);

		_organizations.push(organization);

		Group organizationGroup = organization.getGroup();

		LayoutTestUtil.addLayout(organizationGroup);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(), new long[] {_user.getUserId()});

		Group controlPanelGroup = GroupLocalServiceUtil.getGroup(
			_user.getCompanyId(), GroupConstants.CONTROL_PANEL);

		Group userGroup = _user.getGroup();

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, true, QueryUtil.ALL_POS);

		try {
			Assert.assertEquals(4, groups.size());

			Assert.assertTrue(controlPanelGroup.equals(groups.get(0)));
			Assert.assertTrue(userGroup.equals(groups.get(1)));
			Assert.assertTrue(organizationGroup.equals(groups.get(2)));
			Assert.assertTrue(group.equals(groups.get(3)));

			initUserPermissionCheckerBag(_user);

			List<Group> permissionCacheGroups =
				GroupServiceUtil.getUserSitesGroups(
					_user.getUserId(), null, true, QueryUtil.ALL_POS);

			Assert.assertTrue(groups.equals(permissionCacheGroups));
		}
		finally {
			UserLocalServiceUtil.unsetOrganizationUsers(
				organization.getOrganizationId(),
				new long[] {_user.getUserId()});
		}
	}

	@Test
	public void testGetUserSitesGroupsOrganizationGroups() throws Exception {
		_user = UserTestUtil.addUser();

		Organization parentOrganization = OrganizationTestUtil.addOrganization(
			true);

		_organizations.push(parentOrganization);

		Group parentOrganizationGroup = parentOrganization.getGroup();

		LayoutTestUtil.addLayout(parentOrganizationGroup);

		Organization organization = OrganizationTestUtil.addOrganization(
			parentOrganization.getOrganizationId(),
			RandomTestUtil.randomString(), false);

		_organizations.push(organization);

		UserLocalServiceUtil.addOrganizationUsers(
			organization.getOrganizationId(), new long[] {_user.getUserId()});

		try {
			List<Group> groups = GroupServiceUtil.getUserSitesGroups(
				_user.getUserId(), null, false, QueryUtil.ALL_POS);

			Assert.assertTrue(groups.contains(parentOrganizationGroup));
			Assert.assertFalse(groups.contains(organization.getGroup()));

			initUserPermissionCheckerBag(_user);

			List<Group> permissionCacheGroups =
				GroupServiceUtil.getUserSitesGroups(
					_user.getUserId(), null, false, QueryUtil.ALL_POS);

			Assert.assertEquals(groups.size(), permissionCacheGroups.size());
			Assert.assertTrue(groups.containsAll(permissionCacheGroups));
		}
		finally {
			UserLocalServiceUtil.unsetOrganizationUsers(
				organization.getOrganizationId(),
				new long[] {_user.getUserId()});
		}
	}

	@Test
	public void testGetUserSitesGroupsUserPersonalSite() throws Exception {
		_user = UserTestUtil.addUser();

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(_user.getGroup()));

		initUserPermissionCheckerBag(_user);

		List<Group> permissionCacheGroups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertEquals(groups.size(), permissionCacheGroups.size());
		Assert.assertTrue(groups.containsAll(permissionCacheGroups));
	}

	@Test
	public void testGetUserSitesGroupsUsersGroups() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addGroupUser(group, RoleConstants.USER);

		List<Group> groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertTrue(groups.contains(group));

		group.setActive(false);

		GroupLocalServiceUtil.updateGroup(group);

		groups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertFalse(groups.contains(group));

		initUserPermissionCheckerBag(_user);

		List<Group> permissionCacheGroups = GroupServiceUtil.getUserSitesGroups(
			_user.getUserId(), null, false, QueryUtil.ALL_POS);

		Assert.assertEquals(groups.size(), permissionCacheGroups.size());
		Assert.assertTrue(groups.containsAll(permissionCacheGroups));
	}

	@Test
	public void testUpdatePermissionsCustomRole() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		givePermissionToManageSubsites(_user, group);

		testGroup(
			_user, group, null, null, false, true, false, false, true, true,
			true);
	}

	@Test
	public void testUpdatePermissionsCustomRoleInSubsite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		_user = UserTestUtil.addUser(null, group11.getGroupId());

		givePermissionToManageSubsites(_user, group11);

		testGroup(
			_user, group1, group11, null, false, true, false, false, false,
			true, true);
	}

	@Test
	public void testUpdatePermissionsRegularUser() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		testGroup(
			_user, group, null, null, false, true, false, false, false, false,
			false);
	}

	@Test
	public void testUpdatePermissionsSiteAdmin() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		_user = UserTestUtil.addUser(null, group.getGroupId());

		giveSiteAdminRole(_user, group);

		testGroup(
			_user, group, null, null, false, true, true, false, true, true,
			true);
	}

	@Test
	public void testUpdatePermissionsSubsiteAdmin() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		_user = UserTestUtil.addUser(null, group11.getGroupId());

		giveSiteAdminRole(_user, group11);

		testGroup(
			_user, group1, group11, null, false, true, false, true, false, true,
			true);
	}

	protected void givePermissionToManageSubsites(User user, Group group)
		throws Exception {

		Role role = RoleTestUtil.addRole(
			"Subsites Admin", RoleConstants.TYPE_SITE, Group.class.getName(),
			ResourceConstants.SCOPE_GROUP_TEMPLATE,
			String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
			ActionKeys.MANAGE_SUBGROUPS);

		long[] roleIds = new long[] {role.getRoleId()};

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			user.getUserId(), group.getGroupId(), roleIds);
	}

	protected void giveSiteAdminRole(User user, Group group) throws Exception {
		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), RoleConstants.SITE_ADMINISTRATOR);

		long[] roleIds = new long[] {role.getRoleId()};

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			user.getUserId(), group.getGroupId(), roleIds);
	}

	protected void initUserPermissionCheckerBag(User user) throws Exception {
		AdvancedPermissionChecker advancedPermissionChecker =
			new AdvancedPermissionChecker();

		advancedPermissionChecker.init(user);

		advancedPermissionChecker.getUserBag();
	}

	protected void testGroup(
			User user, Group group1, Group group11, Group group111,
			boolean addGroup, boolean updateGroup, boolean hasManageSite1,
			boolean hasManageSite11, boolean hasManageSubsitePermisionOnGroup1,
			boolean hasManageSubsitePermisionOnGroup11,
			boolean hasManageSubsitePermisionOnGroup111)
		throws Exception {

		if (group1 == null) {
			group1 = GroupTestUtil.addGroup();

			_groups.push(group1);
		}

		if (group11 == null) {
			group11 = GroupTestUtil.addGroup(group1.getGroupId());

			_groups.push(group11);
		}

		if (group111 == null) {
			group111 = GroupTestUtil.addGroup(group11.getGroupId());

			_groups.push(group111);
		}

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(permissionChecker);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group1.getGroupId(), user.getUserId());

		if (addGroup) {
			try {
				GroupTestUtil.addGroup(
					GroupConstants.DEFAULT_PARENT_GROUP_ID, serviceContext);

				Assert.fail(
					"The user should not be able to add top level sites");
			}
			catch (PrincipalException pe) {
			}

			try {
				Group group = GroupTestUtil.addGroup(
					group1.getGroupId(), serviceContext);

				_groups.push(group);

				Assert.assertTrue(
					"The user should not be able to add this site",
					hasManageSubsitePermisionOnGroup1 || hasManageSite1);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to add this site",
					hasManageSubsitePermisionOnGroup1 || hasManageSite1);
			}

			try {
				Group group = GroupTestUtil.addGroup(
					group11.getGroupId(), serviceContext);

				_groups.push(group);

				Assert.assertTrue(
					"The user should not be able to add this site",
					hasManageSubsitePermisionOnGroup11 || hasManageSite1);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to add this site",
					hasManageSubsitePermisionOnGroup11 || hasManageSite1);
			}

			try {
				Group group = GroupTestUtil.addGroup(
					group111.getGroupId(), serviceContext);

				_groups.push(group);

				Assert.assertTrue(
					"The user should not be able to add this site",
					hasManageSubsitePermisionOnGroup111 || hasManageSite1);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to add this site",
					hasManageSubsitePermisionOnGroup111 || hasManageSite1);
			}
		}

		if (updateGroup) {
			try {
				GroupServiceUtil.updateGroup(group1.getGroupId(), "");

				Assert.assertTrue(
					"The user should not be able to update this site",
					hasManageSite1);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to update this site",
					hasManageSite1);
			}

			try {
				GroupServiceUtil.updateGroup(group11.getGroupId(), "");

				Assert.assertTrue(
					"The user should not be able to update this site",
					hasManageSubsitePermisionOnGroup1 || hasManageSite1 ||
						hasManageSite11);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to update this site",
					hasManageSubsitePermisionOnGroup1 || hasManageSite1 ||
						hasManageSite11);
			}

			try {
				GroupServiceUtil.updateGroup(group111.getGroupId(), "");

				Assert.assertTrue(
					"The user should not be able to update this site",
					hasManageSubsitePermisionOnGroup11 || hasManageSite1);
			}
			catch (PrincipalException pe) {
				Assert.assertFalse(
					"The user should be able to update this site",
					hasManageSubsitePermisionOnGroup1 || hasManageSite1);
			}
		}
	}

	@DeleteAfterTestRun
	private final Deque<Group> _groups = new ArrayDeque<>();

	@DeleteAfterTestRun
	private final Deque<Organization> _organizations = new ArrayDeque<>();

	@DeleteAfterTestRun
	private User _user;

}