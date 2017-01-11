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

package com.liferay.portal.kernel.upgrade;

import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.PortletItem;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Matthew Tambara
 */
public class BaseUpgradePortletPreferencesTest
	extends BaseUpgradePortletPreferences {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		StringBundler sb = new StringBundler(0);

		_groupId = CounterLocalServiceUtil.increment(Group.class.getName());

		sb.append("INSERT into Group_ values(0, NULL, ");
		sb.append(_groupId);
		sb.append(", ");

		long groupCompanyId = RandomTestUtil.nextLong();

		sb.append(groupCompanyId);

		sb.append(
			", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append("NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)");

		runSQL(sb.toString());

		_companyIdMap.put(PortletKeys.PREFS_OWNER_TYPE_GROUP, groupCompanyId);

		sb = new StringBundler(0);

		_plid = CounterLocalServiceUtil.increment(Layout.class.getName());

		sb.append("INSERT into Layout values(0, NULL, ");
		sb.append(_plid);
		sb.append(", NULL, ");

		long layoutCompanyId = RandomTestUtil.nextLong();

		sb.append(layoutCompanyId);

		sb.append(
			", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append(
			"NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append("NULL, NULL, NULL, NULL, NULL)");

		runSQL(sb.toString());

		_companyIdMap.put(PortletKeys.PREFS_OWNER_TYPE_LAYOUT, layoutCompanyId);

		sb = new StringBundler(0);

		_organizationId = CounterLocalServiceUtil.increment(
			Organization.class.getName());

		sb.append("INSERT into Organization_ values(0, NULL, ");
		sb.append(_organizationId);
		sb.append(", ");

		long organizationCompanyId = RandomTestUtil.nextLong();

		sb.append(organizationCompanyId);

		sb.append(
			", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append("NULL, NULL, NULL, NULL)");

		runSQL(sb.toString());

		_companyIdMap.put(
			PortletKeys.PREFS_OWNER_TYPE_ORGANIZATION, organizationCompanyId);

		sb = new StringBundler(0);

		_portletItemId = CounterLocalServiceUtil.increment(
			PortletItem.class.getName());

		sb.append("INSERT into PortletItem values(0, ");
		sb.append(_portletItemId);
		sb.append(", NULL, ");

		long portletItemCompanyId = RandomTestUtil.nextLong();

		sb.append(portletItemCompanyId);

		sb.append(", NULL, NULL, NULL, NULL, NULL, NULL, NULL)");

		runSQL(sb.toString());

		_companyIdMap.put(
			PortletKeys.PREFS_OWNER_TYPE_ARCHIVED, portletItemCompanyId);

		sb = new StringBundler(0);

		_userId = CounterLocalServiceUtil.increment(User.class.getName());

		sb.append("INSERT into User_ values(0, NULL, ");
		sb.append(_userId);
		sb.append(", ");

		long userCompanyId = RandomTestUtil.nextLong();

		sb.append(userCompanyId);

		sb.append(
			", NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append(
			"NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append(
			"NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, ");
		sb.append("NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)");

		runSQL(sb.toString());

		_companyIdMap.put(PortletKeys.PREFS_OWNER_TYPE_USER, userCompanyId);

		_companyIdMap.put(
			PortletKeys.PREFS_OWNER_TYPE_COMPANY, RandomTestUtil.nextLong());
	}

	@After
	public void tearDown() throws Exception {
		runSQL(
			"DELETE from Group_ where companyId = " +
				_companyIdMap.get(PortletKeys.PREFS_OWNER_TYPE_GROUP));
		runSQL(
			"DELETE from Layout where companyId = " +
				_companyIdMap.get(PortletKeys.PREFS_OWNER_TYPE_LAYOUT));
		runSQL(
			"DELETE from Organization_ where companyId = " +
				_companyIdMap.get(PortletKeys.PREFS_OWNER_TYPE_ORGANIZATION));
		runSQL(
			"DELETE from PortletItem where companyId = " +
				_companyIdMap.get(PortletKeys.PREFS_OWNER_TYPE_ARCHIVED));
		runSQL(
			"DELETE from User_ where companyId = " +
				_companyIdMap.get(PortletKeys.PREFS_OWNER_TYPE_USER));
		runSQL(
			"DELETE from PortletPreferences where portletId = '" + _PORTLET_ID +
				"'");
	}

	@Test
	public void testUpgrade() throws Exception {
		_addPortletPreference(
			PortletKeys.PREFS_OWNER_TYPE_ARCHIVED, _portletItemId);
		_addPortletPreference(PortletKeys.PREFS_OWNER_TYPE_COMPANY, 0);
		_addPortletPreference(PortletKeys.PREFS_OWNER_TYPE_GROUP, _groupId);
		_addPortletPreference(PortletKeys.PREFS_OWNER_TYPE_LAYOUT, _plid);
		_addPortletPreference(
			PortletKeys.PREFS_OWNER_TYPE_ORGANIZATION, _organizationId);
		_addPortletPreference(PortletKeys.PREFS_OWNER_TYPE_USER, _userId);

		upgrade();

		StringBundler sb = new StringBundler();

		sb.append(
			"SELECT preferences, ownerType from PortletPreferences WHERE ");
		sb.append("portletId = '");
		sb.append(_PORTLET_ID);
		sb.append("'");

		try (Connection connection = DataAccess.getUpgradeOptimizedConnection();
				PreparedStatement ps = connection.prepareStatement(
					sb.toString())) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Assert.assertEquals(
					String.valueOf(_companyIdMap.get(rs.getInt("ownerType"))),
					rs.getString("preferences"));
			}
		}
	}

	@Override
	protected String[] getPortletIds() {
		return new String[] {"test"};
	}

	@Override
	protected String upgradePreferences(
			long companyId, long ownerId, int ownerType, long plid,
			String portletId, String xml)
		throws Exception {

		return String.valueOf(companyId);
	}

	private void _addPortletPreference(int ownerType, long ownerId)
		throws Exception {

		StringBundler sb = new StringBundler(0);

		sb.append("INSERT into PortletPreferences values(0, ");
		sb.append(
			CounterLocalServiceUtil.increment(
				PortletPreferences.class.getName()));
		sb.append(", ");
		sb.append(_companyIdMap.get(ownerType));
		sb.append(", ");
		sb.append(ownerId);
		sb.append(", ");
		sb.append(ownerType);
		sb.append(", ");

		if (ownerType == PortletKeys.PREFS_OWNER_TYPE_LAYOUT) {
			sb.append(ownerId);
		}
		else {
			sb.append("NULL");
		}

		sb.append(", '");
		sb.append(_PORTLET_ID);
		sb.append("', 'default')");

		runSQL(sb.toString());
	}

	private static final String _PORTLET_ID = "test";

	private static final Map<Integer, Long> _companyIdMap = new HashMap<>();

	private long _groupId;
	private long _organizationId;
	private long _plid;
	private long _portletItemId;
	private long _userId;

}