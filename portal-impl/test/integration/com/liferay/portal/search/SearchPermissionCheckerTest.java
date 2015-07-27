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

package com.liferay.portal.search;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchPermissionChecker;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.DateRangeTermFilter;
import com.liferay.portal.kernel.search.filter.ExistsFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.filter.FilterVisitor;
import com.liferay.portal.kernel.search.filter.GeoBoundingBoxFilter;
import com.liferay.portal.kernel.search.filter.GeoDistanceFilter;
import com.liferay.portal.kernel.search.filter.GeoDistanceRangeFilter;
import com.liferay.portal.kernel.search.filter.GeoPolygonFilter;
import com.liferay.portal.kernel.search.filter.MissingFilter;
import com.liferay.portal.kernel.search.filter.PrefixFilter;
import com.liferay.portal.kernel.search.filter.QueryFilter;
import com.liferay.portal.kernel.search.filter.RangeTermFilter;
import com.liferay.portal.kernel.search.filter.TermFilter;
import com.liferay.portal.kernel.search.filter.TermsFilter;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.TransactionalTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class SearchPermissionCheckerTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			TransactionalTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_organization = OrganizationTestUtil.addOrganization();
	}

	@After
	public void tearDown() throws Exception {
		UserLocalServiceUtil.deleteUser(_user.getUserId());

		GroupLocalServiceUtil.deleteGroup(_group.getGroupId());

		if (_role != null) {
			RoleLocalServiceUtil.deleteRole(_role.getRoleId());
		}

		if (_organization != null) {
			OrganizationLocalServiceUtil.deleteOrganization(
				_organization.getOrganizationId());
		}
	}

	@Test
	public void testAdministratorRolePermissionFilter() throws Exception {
		_user = UserTestUtil.addOmniAdminUser();

		BooleanFilter booleanFilter = getBooleanFilter(null);

		Assert.assertFalse(booleanFilter.hasClauses());
	}

	@Test
	public void testCompanyPermissionFilter() throws Exception {
		_user = UserTestUtil.addUser();

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		UserLocalServiceUtil.addRoleUser(_role.getRoleId(), _user.getUserId());

		addViewPermission(
			ResourceConstants.SCOPE_COMPANY, TestPropsValues.getCompanyId(),
			_role.getRoleId());

		BooleanFilter booleanFilter = getBooleanFilter(null);

		Assert.assertFalse(booleanFilter.hasClauses());
	}

	@Test
	public void testGroupIdsPermissionFilter() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		_user = UserTestUtil.addOrganizationAdminUser(_organization);

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(),
			RoleConstants.ORGANIZATION_ADMINISTRATOR);

		assertFieldValue(
			new long[] {_group.getGroupId()}, Field.GROUP_ROLE_ID,
			_group.getGroupId() + StringPool.DASH + role.getRoleId(), false);
	}

	@Test
	public void testGroupPermissionFilter() throws Exception {
		_user = UserTestUtil.addGroupAdminUser(_group);

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(), RoleConstants.SITE_ADMINISTRATOR);

		addViewPermission(
			ResourceConstants.SCOPE_GROUP, _group.getGroupId(),
			role.getRoleId());

		assertFieldValue(
			null, Field.GROUP_ID, String.valueOf(_group.getGroupId()));
		assertFieldValue(null, Field.ROLE_ID, String.valueOf(role.getRoleId()));
	}

	@Test
	public void testGroupTemplatePermissionFilter() throws Exception {
		_user = UserTestUtil.addUser();

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		UserLocalServiceUtil.addRoleUser(_role.getRoleId(), _user.getUserId());

		addViewPermission(
			ResourceConstants.SCOPE_GROUP_TEMPLATE,
			GroupConstants.DEFAULT_PARENT_GROUP_ID, _role.getRoleId());

		BooleanFilter booleanFilter = getBooleanFilter(null);

		Assert.assertFalse(booleanFilter.hasClauses());
	}

	@Test
	public void testOrganizationRolePermissionFilter() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		_user = UserTestUtil.addOrganizationAdminUser(_organization);

		Role role = RoleLocalServiceUtil.getRole(
			TestPropsValues.getCompanyId(),
			RoleConstants.ORGANIZATION_ADMINISTRATOR);

		addViewPermission(
			ResourceConstants.SCOPE_GROUP, _organization.getGroupId(),
			role.getRoleId());

		assertFieldValue(
			null, Field.GROUP_ID, String.valueOf(_organization.getGroupId()));
		assertFieldValue(null, Field.ROLE_ID, String.valueOf(role.getRoleId()));
	}

	@Test
	public void testUserGroupRolePermissionFilter() throws Exception {
		_user = UserTestUtil.addUser();

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_SITE);

		RoleLocalServiceUtil.addGroupRole(
			_group.getGroupId(), _role.getRoleId());

		UserGroupRoleLocalServiceUtil.addUserGroupRoles(
			_user.getUserId(), _group.getGroupId(),
			new long[] {_role.getRoleId()});

		assertFieldValue(
			null, Field.GROUP_ROLE_ID,
			_group.getGroupId() + StringPool.DASH + _role.getRoleId());
	}

	protected void addViewPermission(int scope, long primKey, long roleId)
		throws Exception {

		ResourcePermissionLocalServiceUtil.addResourcePermission(
			TestPropsValues.getCompanyId(), getClassName(), scope,
			String.valueOf(primKey), roleId, ActionKeys.VIEW);
	}

	protected void assertFieldValue(long[] groupIds, String field, String value)
		throws Exception {

		assertFieldValue(groupIds, field, value, true);
	}

	protected void assertFieldValue(
			long[] groupIds, String field, String value, boolean expected)
		throws Exception {

		BooleanFilter booleanFilter = getBooleanFilter(groupIds);

		TestFilterVisitor testFilterVisitor = new TestFilterVisitor(
			expected, field, value);

		booleanFilter.accept(testFilterVisitor);

		testFilterVisitor.assertField();
	}

	protected BooleanFilter getBooleanFilter(long[] groupIds) throws Exception {
		return _searchPermissionChecker.getPermissionBooleanFilter(
			TestPropsValues.getCompanyId(), groupIds, _user.getUserId(),
			getClassName(), new BooleanFilter(), null);
	}

	protected String getClassName() {
		return DLFileEntry.class.getName();
	}

	private static final SearchPermissionChecker _searchPermissionChecker =
		new SearchPermissionCheckerImpl();

	private Group _group;
	private Organization _organization;
	private Role _role;
	private User _user;

	private class TestFilterVisitor implements FilterVisitor<Void> {

		public TestFilterVisitor(boolean expected, String field, String value) {
			_expected = expected;
			_field = field;
			_value = value;
		}

		public void assertField() {
			Assert.assertEquals(_expected, _found);
		}

		@Override
		public Void visit(BooleanFilter booleanFilter) {
			for (BooleanClause<Filter> booleanClause :
					booleanFilter.getMustBooleanClauses()) {

				Filter filter = booleanClause.getClause();

				filter.accept(this);
			}

			for (BooleanClause<Filter> booleanClause :
					booleanFilter.getShouldBooleanClauses()) {

				Filter filter = booleanClause.getClause();

				filter.accept(this);
			}

			return null;
		}

		@Override
		public Void visit(DateRangeTermFilter dateRangeTermFilter) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Void visit(ExistsFilter existsFilter) {
			return null;
		}

		@Override
		public Void visit(GeoBoundingBoxFilter geoBoundingBoxFilter) {
			return null;
		}

		@Override
		public Void visit(GeoDistanceFilter geoDistanceFilter) {
			return null;
		}

		@Override
		public Void visit(GeoDistanceRangeFilter geoDistanceRangeFilter) {
			return null;
		}

		@Override
		public Void visit(GeoPolygonFilter geoPolygonFilter) {
			return null;
		}

		@Override
		public Void visit(MissingFilter missingFilter) {
			return null;
		}

		@Override
		public Void visit(PrefixFilter prefixFilter) {
			return null;
		}

		@Override
		public Void visit(QueryFilter queryFilter) {
			return null;
		}

		@Override
		public Void visit(RangeTermFilter rangeTermFilter) {
			return null;
		}

		@Override
		public Void visit(TermFilter termFilter) {
			return null;
		}

		@Override
		public Void visit(TermsFilter termsFilter) {
			if (_field.equals(termsFilter.getField())) {
				if (ArrayUtil.contains(termsFilter.getValues(), _value)) {
					_found = true;
				}
			}

			return null;
		}

		private final boolean _expected;
		private final String _field;
		private boolean _found;
		private final String _value;

	}

}