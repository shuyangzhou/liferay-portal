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

package com.liferay.change.tracking.internal.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTPreferences;
import com.liferay.change.tracking.service.CTCollectionLocalService;
import com.liferay.change.tracking.service.CTPreferencesLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.layout.test.util.LayoutTestUtil;
import com.liferay.petra.lang.SafeClosable;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Preston Crary
 */
@RunWith(Arquillian.class)
public class LayoutCTTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		long companyId = TestPropsValues.getCompanyId();
		long userId = TestPropsValues.getUserId();

		_companyCTPreferences = _ctPreferencesLocalService.getCTPreferences(
			companyId, 0);

		_userCTPreferences = _ctPreferencesLocalService.getCTPreferences(
			companyId, userId);

		long ctCollectionId = _counterLocalService.increment();

		_ctCollection = _ctCollectionLocalService.createCTCollection(
			ctCollectionId);

		_ctCollection.setName(String.valueOf(ctCollectionId));

		_ctCollection = _ctCollectionLocalService.updateCTCollection(
			_ctCollection);
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_ctPreferencesLocalService.deleteCTPreferences(_userCTPreferences);

		_ctPreferencesLocalService.deleteCTPreferences(_companyCTPreferences);

		_ctCollectionLocalService.deleteCTCollection(_ctCollection);
	}

	@Test
	public void testLayout() throws Exception {
		_group = GroupTestUtil.addGroup();

		try (SafeClosable safeClosable1 =
				CTCollectionThreadLocal.setCTCollectionId(
					_ctCollection.getCtCollectionId())) {

			Layout layout = LayoutTestUtil.addLayout(_group);

			Assert.assertEquals(
				layout, _layoutLocalService.fetchLayout(layout.getPlid()));

			try (SafeClosable safeClosable2 =
					CTCollectionThreadLocal.setCTCollectionId(
						CTConstants.CT_COLLECTION_ID_PRODUCTION)) {

				Assert.assertNull(
					_layoutLocalService.fetchLayout(layout.getPlid()));
			}
		}
	}

	private static CTPreferences _companyCTPreferences;

	@Inject
	private static CounterLocalService _counterLocalService;

	private static CTCollection _ctCollection;

	@Inject
	private static CTCollectionLocalService _ctCollectionLocalService;

	@Inject
	private static CTPreferencesLocalService _ctPreferencesLocalService;

	@Inject
	private static LayoutLocalService _layoutLocalService;

	private static CTPreferences _userCTPreferences;

	@DeleteAfterTestRun
	private Group _group;

}