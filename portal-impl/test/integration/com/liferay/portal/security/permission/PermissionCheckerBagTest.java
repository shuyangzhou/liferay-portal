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

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.UserGroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.OrganizationConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class PermissionCheckerBagTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_advancedPermissionChecker = new AdvancedPermissionChecker();

		_parentGroup = GroupTestUtil.addGroup();

		_childGroup = GroupTestUtil.addGroup(_parentGroup.getGroupId());

		_parentOrganization = OrganizationTestUtil.addOrganization(
			OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID,
			RandomTestUtil.randomString(), true);

		_childOrganization = OrganizationTestUtil.addOrganization(
			_parentOrganization.getOrganizationId(),
			RandomTestUtil.randomString(), true);

		_userGroup = UserGroupTestUtil.addUserGroup(_childGroup.getGroupId());

		_user = UserTestUtil.addUser();
	}

	@Test
	public void testGetGroups() throws Exception {
		List<Group> userGroups = getUserGroups();

		List<Group> userOrgGroups = getUserOrgGroups();

		List<Group> userUserGroupGroups = getUserUserGroupGroups();

		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		List<Group> groups = userPermissionCheckerBag.getGroups();

		Assert.assertTrue(groups.containsAll(userGroups));
		Assert.assertTrue(groups.containsAll(userOrgGroups));
		Assert.assertTrue(groups.containsAll(userUserGroupGroups));
	}

	@Test
	public void testGetRoles() throws Exception {
		Role regularRole = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		UserLocalServiceUtil.addRoleUser(regularRole.getRoleId(), _user);

		long groupRoleId = RoleTestUtil.addGroupRole(_childGroup.getGroupId());

		UserLocalServiceUtil.addRoleUser(groupRoleId, _user);

		long organizationRoleId = RoleTestUtil.addOrganizationRole(
			_childOrganization.getGroupId());

		UserLocalServiceUtil.addRoleUser(organizationRoleId, _user);

		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		List<Role> roles = new ArrayList<>(userPermissionCheckerBag.getRoles());

		long[] roleIds = ListUtil.toLongArray(roles, Role.ROLE_ID_ACCESSOR);

		Assert.assertTrue(ArrayUtil.contains(roleIds, regularRole.getRoleId()));
		Assert.assertTrue(ArrayUtil.contains(roleIds, groupRoleId));
		Assert.assertTrue(ArrayUtil.contains(roleIds, organizationRoleId));
	}

	@Test
	public void testGetUserGroups() throws Exception {
		List<Group> userGroups = getUserGroups();

		Assert.assertTrue(userGroups.contains(_childGroup));
		Assert.assertFalse(userGroups.contains(_parentGroup));
	}

	@Test
	public void testGetUserId() throws Exception {
		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		Assert.assertEquals(
			_user.getUserId(), userPermissionCheckerBag.getUserId());
	}

	@Test
	public void testGetUserOrgGroups() throws Exception {
		List<Group> groups = getUserOrgGroups();

		Assert.assertTrue(groups.contains(_childOrganization.getGroup()));
		Assert.assertTrue(groups.contains(_parentOrganization.getGroup()));
	}

	@Test
	public void testGetUserOrgs() throws Exception {
		List<Organization> organizations = getUserOrgs();

		Assert.assertTrue(organizations.contains(_childOrganization));
		Assert.assertTrue(organizations.contains(_parentOrganization));
	}

	@Test
	public void testGetUserUserGroupGroups() throws Exception {
		List<Group> groups = getUserUserGroupGroups();

		Assert.assertTrue(groups.contains(_userGroup.getGroup()));
	}

	@Test
	public void testIsGroupAdmin() throws Exception {
		UserLocalServiceUtil.addGroupUser(
			_childGroup.getGroupId(), _user.getUserId());

		UserTestUtil.addUserGroupRole(
			_user.getUserId(), _childGroup.getGroupId(),
			RoleConstants.SITE_ADMINISTRATOR);

		PermissionCheckerBag permissionCheckerBag = getUserBag(_childGroup);

		Assert.assertTrue(
			permissionCheckerBag.isGroupAdmin(
				_advancedPermissionChecker, _childGroup));
	}

	@Test
	public void testIsGroupMember() throws Exception {
		UserLocalServiceUtil.addGroupUser(
			_childGroup.getGroupId(), _user.getUserId());

		UserTestUtil.addUserGroupRole(
			_user.getUserId(), _childGroup.getGroupId(),
			RoleConstants.SITE_MEMBER);

		PermissionCheckerBag permissionCheckerBag = getUserBag(_childGroup);

		Assert.assertTrue(
			permissionCheckerBag.isGroupMember(
				_advancedPermissionChecker, _childGroup));
	}

	@Test
	public void testIsGroupOwner() throws Exception {
		UserLocalServiceUtil.addGroupUser(
			_childGroup.getGroupId(), _user.getUserId());

		UserTestUtil.addUserGroupRole(
			_user.getUserId(), _childGroup.getGroupId(),
			RoleConstants.SITE_OWNER);

		PermissionCheckerBag permissionCheckerBag = getUserBag(_childGroup);

		Assert.assertTrue(
			permissionCheckerBag.isGroupOwner(
				_advancedPermissionChecker, _childGroup));
	}

	@Test
	public void testIsOrganizationAdmin() throws Exception {
		UserLocalServiceUtil.addOrganizationUser(
			_childOrganization.getOrganizationId(), _user.getUserId());

		UserTestUtil.addUserGroupRole(
			_user.getUserId(), _childOrganization.getGroupId(),
			RoleConstants.ORGANIZATION_ADMINISTRATOR);

		PermissionCheckerBag permissionCheckerBag = getUserBag(
			_childOrganization.getGroup());

		Assert.assertTrue(
			permissionCheckerBag.isOrganizationAdmin(
				_advancedPermissionChecker, _childOrganization));
	}

	@Test
	public void testIsOrganizationOwner() throws Exception {
		UserLocalServiceUtil.addOrganizationUser(
			_childOrganization.getOrganizationId(), _user.getUserId());

		UserTestUtil.addUserGroupRole(
			_user.getUserId(), _childOrganization.getGroupId(),
			RoleConstants.ORGANIZATION_OWNER);

		PermissionCheckerBag permissionCheckerBag = getUserBag(
			_childOrganization.getGroup());

		Assert.assertTrue(
			permissionCheckerBag.isOrganizationOwner(
				_advancedPermissionChecker, _childOrganization));
	}

	protected UserPermissionCheckerBag getUserBag() throws Exception {
		_advancedPermissionChecker.init(_user);

		return _advancedPermissionChecker.getUserBag();
	}

	protected PermissionCheckerBag getUserBag(Group group) throws Exception {
		_advancedPermissionChecker.init(_user);

		return _advancedPermissionChecker.getUserBag(
			_user.getUserId(), group.getGroupId());
	}

	protected List<Group> getUserGroups() throws Exception {
		UserLocalServiceUtil.addGroupUser(_childGroup.getGroupId(), _user);

		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		return new ArrayList<>(userPermissionCheckerBag.getUserGroups());
	}

	protected List<Group> getUserOrgGroups() throws Exception {
		return GroupLocalServiceUtil.getOrganizationsGroups(getUserOrgs());
	}

	protected List<Organization> getUserOrgs() throws Exception {
		UserLocalServiceUtil.addOrganizationUser(
			_childOrganization.getOrganizationId(), _user.getUserId());

		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		return userPermissionCheckerBag.getUserOrgs();
	}

	protected List<Group> getUserUserGroupGroups() throws Exception {
		UserLocalServiceUtil.addUserGroupUser(
			_userGroup.getUserGroupId(), _user.getUserId());

		UserPermissionCheckerBag userPermissionCheckerBag = getUserBag();

		return userPermissionCheckerBag.getUserUserGroupGroups();
	}

	private AdvancedPermissionChecker _advancedPermissionChecker;

	@DeleteAfterTestRun
	private Group _childGroup;

	@DeleteAfterTestRun
	private Organization _childOrganization;

	@DeleteAfterTestRun
	private Group _parentGroup;

	@DeleteAfterTestRun
	private Organization _parentOrganization;

	private User _user;

	@DeleteAfterTestRun
	private UserGroup _userGroup;

}