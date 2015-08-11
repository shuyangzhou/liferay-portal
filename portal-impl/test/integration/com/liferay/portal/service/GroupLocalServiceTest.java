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

import com.liferay.portal.GroupParentException;
import com.liferay.portal.LocaleException;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.test.LayoutTestUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
@Sync(cleanTransaction = true)
public class GroupLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testAddCompanyStagingGroup() throws Exception {
		Group companyGroup = GroupLocalServiceUtil.getCompanyGroup(
			TestPropsValues.getCompanyId());

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAttribute("staging", Boolean.TRUE);

		Group companyStagingGroup = GroupLocalServiceUtil.addGroup(
			TestPropsValues.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
			companyGroup.getClassName(), companyGroup.getClassPK(),
			companyGroup.getGroupId(), companyGroup.getNameMap(),
			companyGroup.getDescriptionMap(), companyGroup.getType(),
			companyGroup.isManualMembership(),
			companyGroup.getMembershipRestriction(),
			companyGroup.getFriendlyURL(), false, companyGroup.isActive(),
			serviceContext);

		_groups.push(companyStagingGroup);

		Assert.assertTrue(companyStagingGroup.isCompanyStagingGroup());

		Assert.assertEquals(
			companyGroup.getGroupId(), companyStagingGroup.getLiveGroupId());
	}

	@Test
	public void testDeleteSite() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(group.getGroupId());

		int initialTagsCount = AssetTagLocalServiceUtil.getGroupTagsCount(
			group.getGroupId());

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), group.getGroupId(),
			RandomTestUtil.randomString(), serviceContext);

		Assert.assertEquals(
			initialTagsCount + 1,
			AssetTagLocalServiceUtil.getGroupTagsCount(group.getGroupId()));

		_user = UserTestUtil.addUser(group.getGroupId());

		BlogsEntry blogsEntry = BlogsEntryLocalServiceUtil.addEntry(
			_user.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), serviceContext);

		Assert.assertNotNull(
			BlogsEntryLocalServiceUtil.fetchBlogsEntry(
				blogsEntry.getEntryId()));

		GroupLocalServiceUtil.deleteGroup(group.getGroupId());

		_groups.pop();

		Assert.assertEquals(
			initialTagsCount,
			AssetTagLocalServiceUtil.getGroupTagsCount(group.getGroupId()));
		Assert.assertNull(
			BlogsEntryLocalServiceUtil.fetchBlogsEntry(
				blogsEntry.getEntryId()));
	}

	@Test
	public void testFindGroupByDescription() throws Exception {
		Group group = GroupTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groups.push(group);

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null,
				group.getDescription(LocaleUtil.getDefault()), groupParams));
	}

	@Test
	public void testFindGroupByDescriptionWithSpaces() throws Exception {
		Group group = GroupTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groups.push(group);

		group.setDescription(
			RandomTestUtil.randomString() + StringPool.SPACE +
				RandomTestUtil.randomString());

		GroupLocalServiceUtil.updateGroup(group);

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null,
				group.getDescription(LocaleUtil.getDefault()), groupParams));
	}

	@Test
	public void testFindGroupByName() throws Exception {
		Group group = GroupTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groups.push(group);

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null,
				group.getName(LocaleUtil.getDefault()), groupParams));
	}

	@Test
	public void testFindGroupByNameWithSpaces() throws Exception {
		Group group = GroupTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groups.push(group);

		group.setName(
			RandomTestUtil.randomString() + StringPool.SPACE +
				RandomTestUtil.randomString());

		GroupLocalServiceUtil.updateGroup(group);

		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null,
				group.getName(LocaleUtil.getDefault()), groupParams));
	}

	@Test
	public void testFindGuestGroupByCompanyName() throws Exception {
		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null, "liferay", groupParams));
	}

	@Test
	public void testFindGuestGroupByCompanyNameCapitalized() throws Exception {
		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			1,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null, "Liferay", groupParams));
	}

	@Test
	public void testFindNonexistentGroup() throws Exception {
		LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

		groupParams.put("manualMembership", Boolean.TRUE);
		groupParams.put("site", Boolean.TRUE);

		Assert.assertEquals(
			0,
			GroupLocalServiceUtil.searchCount(
				TestPropsValues.getCompanyId(), null, "cabina14", groupParams));
	}

	@Test
	public void testGroupHasCurrentPageScopeDescriptiveName() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(false, true, false);

		themeDisplay.setPlid(group.getClassPK());

		themeDisplay.setScopeGroupId(_group.getGroupId());

		String scopeDescriptiveName = group.getScopeDescriptiveName(
			themeDisplay);

		Assert.assertTrue(scopeDescriptiveName.contains("current-page"));
	}

	@Test
	public void testGroupHasCurrentSiteScopeDescriptiveName() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(true, false, false);

		themeDisplay.setScopeGroupId(group.getGroupId());

		String scopeDescriptiveName = group.getScopeDescriptiveName(
			themeDisplay);

		Assert.assertTrue(scopeDescriptiveName.contains("current-site"));
	}

	@Test
	public void testGroupHasDefaultScopeDescriptiveName() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(false, false, true);

		themeDisplay.setScopeGroupId(_group.getGroupId());

		String scopeDescriptiveName = group.getScopeDescriptiveName(
			themeDisplay);

		Assert.assertTrue(scopeDescriptiveName.contains("default"));
	}

	@Test
	public void testGroupHasLocalizedName() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		String scopeDescriptiveName = group.getScopeDescriptiveName(
			themeDisplay);

		Assert.assertTrue(
			scopeDescriptiveName.equals(
				group.getName(themeDisplay.getLocale())));
	}

	@Test
	public void testGroupIsChildSiteScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		themeDisplay.setScopeGroupId(group.getGroupId());

		Group subgroup = GroupTestUtil.addGroup(group.getGroupId());

		_groups.push(subgroup);

		String scopeLabel = subgroup.getScopeLabel(themeDisplay);

		Assert.assertEquals("child-site", scopeLabel);
	}

	@Test
	public void testGroupIsCurrentSiteScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(true, false, false);

		themeDisplay.setScopeGroupId(group.getGroupId());

		String scopeLabel = group.getScopeLabel(themeDisplay);

		Assert.assertEquals(scopeLabel, "current-site");
	}

	@Test
	public void testGroupIsGlobalScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(false, false, false);

		Company company = CompanyLocalServiceUtil.getCompany(
			group.getCompanyId());

		themeDisplay.setCompany(company);

		Group companyGroup = company.getGroup();

		String scopeLabel = companyGroup.getScopeLabel(themeDisplay);

		Assert.assertEquals("global", scopeLabel);
	}

	@Test
	public void testGroupIsPageScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = addGroup(false, true, false);

		themeDisplay.setPlid(group.getClassPK());

		themeDisplay.setScopeGroupId(_group.getGroupId());

		String scopeLabel = group.getScopeLabel(themeDisplay);

		Assert.assertEquals("page", scopeLabel);
	}

	@Test
	public void testGroupIsParentSiteScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		Group subgroup = GroupTestUtil.addGroup(group.getGroupId());

		_groups.push(subgroup);

		themeDisplay.setScopeGroupId(subgroup.getGroupId());

		String scopeLabel = group.getScopeLabel(themeDisplay);

		Assert.assertEquals("parent-site", scopeLabel);
	}

	@Test
	public void testGroupIsSiteScopeLabel() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		themeDisplay.setScopeGroupId(_group.getGroupId());

		String scopeLabel = group.getScopeLabel(themeDisplay);

		Assert.assertEquals("site", scopeLabel);
	}

	@Test
	public void testIndividualResourcePermission() throws Exception {
		int resourcePermissionsCount =
			ResourcePermissionLocalServiceUtil.getResourcePermissionsCount(
				_group.getCompanyId(), Group.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL,
				String.valueOf(_group.getGroupId()));

		Assert.assertEquals(resourcePermissionsCount, 1);
	}

	@Test
	public void testInheritLocalesByDefault() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		Assert.assertTrue(LanguageUtil.isInheritLocales(group.getGroupId()));
		Assert.assertEquals(
			LanguageUtil.getAvailableLocales(),
			LanguageUtil.getAvailableLocales(group.getGroupId()));
	}

	@Test
	public void testInvalidChangeAvailableLanguageIds() throws Exception {
		testUpdateDisplaySettings(
			Arrays.asList(LocaleUtil.SPAIN, LocaleUtil.US),
			Arrays.asList(LocaleUtil.GERMANY, LocaleUtil.US), null, true);
	}

	@Test
	public void testInvalidChangeDefaultLanguageId() throws Exception {
		testUpdateDisplaySettings(
			Arrays.asList(LocaleUtil.SPAIN, LocaleUtil.US),
			Arrays.asList(LocaleUtil.SPAIN, LocaleUtil.US), LocaleUtil.GERMANY,
			true);
	}

	@Test
	public void testScopes() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		Layout layout = LayoutTestUtil.addLayout(group);

		Assert.assertFalse(layout.hasScopeGroup());

		Map<Locale, String> nameMap = new HashMap<>();

		nameMap.put(
			LocaleUtil.getDefault(), layout.getName(LocaleUtil.getDefault()));

		Group scope = GroupLocalServiceUtil.addGroup(
			TestPropsValues.getUserId(), GroupConstants.DEFAULT_PARENT_GROUP_ID,
			Layout.class.getName(), layout.getPlid(),
			GroupConstants.DEFAULT_LIVE_GROUP_ID, nameMap,
			(Map<Locale, String>)null, 0, true,
			GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, null, false, true,
			null);

		_groups.push(scope);

		Assert.assertFalse(scope.isRoot());
		Assert.assertEquals(scope.getParentGroupId(), group.getGroupId());
	}

	@Test
	public void testSelectableParentSites() throws Exception {
		testSelectableParentSites(false);
	}

	@Test
	public void testSelectableParentSitesStaging() throws Exception {
		testSelectableParentSites(true);
	}

	@Test(expected = GroupParentException.MustNotHaveChildParent.class)
	public void testSelectFirstChildGroupAsParentSite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		GroupLocalServiceUtil.updateGroup(
			group1.getGroupId(), group11.getGroupId(), group1.getNameMap(),
			group1.getDescriptionMap(), group1.getType(),
			group1.isManualMembership(), group1.getMembershipRestriction(),
			group1.getFriendlyURL(), group1.isInheritContent(),
			group1.isActive(), ServiceContextTestUtil.getServiceContext());
	}

	@Test(expected = GroupParentException.MustNotHaveChildParent.class)
	public void testSelectLastChildGroupAsParentSite() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		Group group111 = GroupTestUtil.addGroup(group11.getGroupId());

		_groups.push(group111);

		Group group1111 = GroupTestUtil.addGroup(group111.getGroupId());

		_groups.push(group1111);

		GroupLocalServiceUtil.updateGroup(
			group1.getGroupId(), group1111.getGroupId(), group1.getNameMap(),
			group1.getDescriptionMap(), group1.getType(),
			group1.isManualMembership(), group1.getMembershipRestriction(),
			group1.getFriendlyURL(), group1.isInheritContent(),
			group1.isActive(), ServiceContextTestUtil.getServiceContext());
	}

	@Test(expected = GroupParentException.MustNotHaveStagingParent.class)
	public void testSelectLiveGroupAsParentSite() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		GroupTestUtil.enableLocalStaging(group);

		Assert.assertTrue(group.hasStagingGroup());

		Group stagingGroup = group.getStagingGroup();

		GroupLocalServiceUtil.updateGroup(
			stagingGroup.getGroupId(), group.getGroupId(),
			stagingGroup.getNameMap(), stagingGroup.getDescriptionMap(),
			stagingGroup.getType(), stagingGroup.isManualMembership(),
			stagingGroup.getMembershipRestriction(),
			stagingGroup.getFriendlyURL(), stagingGroup.isInheritContent(),
			stagingGroup.isActive(),
			ServiceContextTestUtil.getServiceContext());
	}

	@Test(expected = GroupParentException.MustNotBeOwnParent.class)
	public void testSelectOwnGroupAsParentSite() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		GroupLocalServiceUtil.updateGroup(
			group.getGroupId(), group.getGroupId(), group.getNameMap(),
			group.getDescriptionMap(), group.getType(),
			group.isManualMembership(), group.getMembershipRestriction(),
			group.getFriendlyURL(), group.isInheritContent(), group.isActive(),
			ServiceContextTestUtil.getServiceContext());
	}

	@Test
	public void testSubsites() throws Exception {
		Group group1 = GroupTestUtil.addGroup();

		_groups.push(group1);

		Group group11 = GroupTestUtil.addGroup(group1.getGroupId());

		_groups.push(group11);

		Group group111 = GroupTestUtil.addGroup(group11.getGroupId());

		_groups.push(group111);

		Assert.assertTrue(group1.isRoot());
		Assert.assertFalse(group11.isRoot());
		Assert.assertFalse(group111.isRoot());
		Assert.assertEquals(group1.getGroupId(), group11.getParentGroupId());
		Assert.assertEquals(group11.getGroupId(), group111.getParentGroupId());
	}

	@Test
	public void testUpdateAvailableLocales() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		List<Locale> availableLocales = Arrays.asList(
			LocaleUtil.GERMANY, LocaleUtil.SPAIN, LocaleUtil.US);

		group = GroupTestUtil.updateDisplaySettings(
			group.getGroupId(), availableLocales, null);

		Assert.assertEquals(
			new HashSet<>(availableLocales),
			LanguageUtil.getAvailableLocales(group.getGroupId()));
	}

	@Test
	public void testUpdateDefaultLocale() throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		group = GroupTestUtil.updateDisplaySettings(
			group.getGroupId(), null, LocaleUtil.SPAIN);

		Assert.assertEquals(
			LocaleUtil.SPAIN,
			PortalUtil.getSiteDefaultLocale(group.getGroupId()));
	}

	@Test
	public void testValidChangeAvailableLanguageIds() throws Exception {
		testUpdateDisplaySettings(
			Arrays.asList(LocaleUtil.GERMANY, LocaleUtil.SPAIN, LocaleUtil.US),
			Arrays.asList(LocaleUtil.SPAIN, LocaleUtil.US), null, false);
	}

	@Test
	public void testValidChangeDefaultLanguageId() throws Exception {
		testUpdateDisplaySettings(
			Arrays.asList(LocaleUtil.GERMANY, LocaleUtil.SPAIN, LocaleUtil.US),
			Arrays.asList(LocaleUtil.GERMANY, LocaleUtil.SPAIN, LocaleUtil.US),
			LocaleUtil.GERMANY, false);
	}

	protected Group addGroup(
			boolean site, boolean layout, boolean layoutPrototype)
		throws Exception {

		Group group = null;

		if (site) {
			group = GroupTestUtil.addGroup();
		}
		else if (layout) {
			Group layoutGroup = GroupTestUtil.addGroup();

			_groups.push(layoutGroup);

			Layout scopeLayout = LayoutTestUtil.addLayout(layoutGroup);

			Map<Locale, String> nameMap = new HashMap<>();

			nameMap.put(LocaleUtil.getDefault(), RandomTestUtil.randomString());

			group = GroupLocalServiceUtil.addGroup(
				TestPropsValues.getUserId(),
				GroupConstants.DEFAULT_PARENT_GROUP_ID, Layout.class.getName(),
				scopeLayout.getPlid(), GroupConstants.DEFAULT_LIVE_GROUP_ID,
				nameMap, (Map<Locale, String>)null, 0, true,
				GroupConstants.DEFAULT_MEMBERSHIP_RESTRICTION, null, false,
				true, null);
		}
		else if (layoutPrototype) {
			group = GroupTestUtil.addGroup();

			group.setClassName(LayoutPrototype.class.getName());
		}
		else {
			group = GroupTestUtil.addGroup();
		}

		_groups.push(group);

		return group;
	}

	protected void testSelectableParentSites(boolean staging) throws Exception {
		Group group = GroupTestUtil.addGroup();

		_groups.push(group);

		Assert.assertTrue(group.isRoot());

		LinkedHashMap<String, Object> params = new LinkedHashMap<>();

		params.put("site", Boolean.TRUE);

		List<Long> excludedGroupIds = new ArrayList<>();

		excludedGroupIds.add(group.getGroupId());

		if (staging) {
			GroupTestUtil.enableLocalStaging(group);

			Assert.assertTrue(group.hasStagingGroup());

			excludedGroupIds.add(group.getStagingGroup().getGroupId());
		}

		params.put("excludedGroupIds", excludedGroupIds);

		List<Group> selectableGroups = GroupLocalServiceUtil.search(
			group.getCompanyId(), null, StringPool.BLANK, params,
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		for (Group selectableGroup : selectableGroups) {
			long selectableGroupId = selectableGroup.getGroupId();

			Assert.assertNotEquals(
				"A group cannot be its own parent", group.getGroupId(),
				selectableGroupId);

			if (staging) {
				Assert.assertNotEquals(
					"A group cannot have its live group as parent",
					group.getLiveGroupId(), selectableGroupId);
			}
		}
	}

	protected void testUpdateDisplaySettings(
			Collection<Locale> portalAvailableLocales,
			Collection<Locale> groupAvailableLocales, Locale groupDefaultLocale,
			boolean expectFailure)
		throws Exception {

		Set<Locale> availableLocales = LanguageUtil.getAvailableLocales();

		CompanyTestUtil.resetCompanyLocales(
			TestPropsValues.getCompanyId(), portalAvailableLocales,
			LocaleUtil.getDefault());

		Group group = GroupTestUtil.addGroup(
			GroupConstants.DEFAULT_PARENT_GROUP_ID);

		_groups.push(group);

		try {
			GroupTestUtil.updateDisplaySettings(
				group.getGroupId(), groupAvailableLocales, groupDefaultLocale);

			Assert.assertFalse(expectFailure);
		}
		catch (LocaleException le) {
			Assert.assertTrue(expectFailure);
		}
		finally {
			CompanyTestUtil.resetCompanyLocales(
				TestPropsValues.getCompanyId(), availableLocales,
				LocaleUtil.getDefault());
		}
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private final Deque<Group> _groups = new ArrayDeque<>();

	@DeleteAfterTestRun
	private final Deque<Organization> _organizations = new ArrayDeque<>();

	@DeleteAfterTestRun
	private User _user;

}