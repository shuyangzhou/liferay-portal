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

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.DateFormatFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.DeleteAfterTestRun;
import com.liferay.portal.test.Sync;
import com.liferay.portal.test.listeners.MainServletExecutionTestListener;
import com.liferay.portal.test.runners.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.test.GroupTestUtil;
import com.liferay.portal.util.test.RandomTestUtil;
import com.liferay.portal.util.test.SearchContextTestUtil;
import com.liferay.portal.util.test.ServiceContextTestUtil;
import com.liferay.portal.util.test.TestPropsValues;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetVocabulary;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.test.AssetEntryQueryTestUtil;
import com.liferay.portlet.asset.util.AssetUtil;

import java.text.DateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eudaldo Alonso
 */
@ExecutionTestListeners(listeners = {MainServletExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Sync
public abstract class BaseAssetSearchTestCase {

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		AssetVocabulary vocabulary =
			AssetVocabularyLocalServiceUtil.addVocabulary(
				TestPropsValues.getUserId(), RandomTestUtil.randomString(),
				serviceContext);

		_vocabularyId = vocabulary.getVocabularyId();

		AssetCategory fashionCategory =
			AssetCategoryLocalServiceUtil.addCategory(
				TestPropsValues.getUserId(), "Fashion", _vocabularyId,
				serviceContext);

		_fashionCategoryId = fashionCategory.getCategoryId();

		AssetCategory foodCategory = AssetCategoryLocalServiceUtil.addCategory(
			TestPropsValues.getUserId(), "Food", _vocabularyId, serviceContext);

		_foodCategoryId = foodCategory.getCategoryId();

		AssetCategory healthCategory =
			AssetCategoryLocalServiceUtil.addCategory(
				TestPropsValues.getUserId(), "Health", _vocabularyId,
				serviceContext);

		_healthCategoryId = healthCategory.getCategoryId();

		AssetCategory sportCategory = AssetCategoryLocalServiceUtil.addCategory(
			TestPropsValues.getUserId(), "Sport", _vocabularyId,
			serviceContext);

		_sportCategoryId = sportCategory.getCategoryId();

		AssetCategory travelCategory =
			AssetCategoryLocalServiceUtil.addCategory(
				TestPropsValues.getUserId(), "Travel", _vocabularyId,
				serviceContext);

		_travelCategoryId = travelCategory.getCategoryId();

		_assetCategoryIds1 =
			new long[] {_healthCategoryId, _sportCategoryId, _travelCategoryId};
		_assetCategoryIds2 = new long[] {
			_fashionCategoryId, _foodCategoryId, _healthCategoryId,
			_sportCategoryId
		};

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), "liferay", null, serviceContext);

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), "architecture", null, serviceContext);

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), "modularity", null, serviceContext);

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), "osgi", null, serviceContext);

		AssetTagLocalServiceUtil.addTag(
			TestPropsValues.getUserId(), "services", null, serviceContext);

		_assetTagsNames1 =
			new String[] {"liferay", "architecture", "modularity", "osgi"};
		_assetTagsNames2 = new String[] {"liferay", "architecture", "services"};
	}

	@Test
	public void testAssetCategories() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);

		serviceContext.setAssetCategoryIds(_assetCategoryIds1);

		addBaseModel(parentBaseModel, getSearchKeywords(), serviceContext);

		serviceContext.setAssetCategoryIds(_assetCategoryIds2);

		addBaseModel(parentBaseModel, getSearchKeywords(), serviceContext);

		doTestAllAssetCategories1();
		doTestAllAssetCategories2();
		doTestAllAssetCategories3();
		doTestAllAssetCategories4();

		doTestAnyAssetCategories1();
		doTestAnyAssetCategories2();
		doTestAnyAssetCategories3();
		doTestAnyAssetCategories4();

		doTestAssetCategoryAllAndAny();
		doTestAssetCategoryNotAllAndAll();
		doTestAssetCategoryNotAllAndAny();
		doTestAssetCategoryNotAllAndNotAny();
		doTestAssetCategoryNotAnyAndAll();
		doTestAssetCategoryNotAnyAndAny();

		doTestNotAllAssetCategories1();
		doTestNotAllAssetCategories2();
		doTestNotAllAssetCategories3();
		doTestNotAllAssetCategories4();

		doTestAnyAssetCategories1();
		doTestAnyAssetCategories2();
		doTestAnyAssetCategories3();
		doTestAnyAssetCategories4();
	}

	@Test
	public void testAssetTags() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);

		serviceContext.setAssetTagNames(_assetTagsNames1);
		serviceContext.setAssetCategoryIds(_assetCategoryIds1);

		addBaseModel(parentBaseModel, getSearchKeywords(), serviceContext);

		serviceContext.setAssetTagNames(_assetTagsNames2);
		serviceContext.setAssetCategoryIds(_assetCategoryIds2);

		addBaseModel(parentBaseModel, getSearchKeywords(), serviceContext);

		doTestAllAssetTags1();
		doTestAllAssetTags2();
		doTestAllAssetTags3();
		doTestAllAssetTags4();

		doTestAnyAssetTags1();
		doTestAnyAssetTags2();
		doTestAnyAssetTags3();
		doTestAnyAssetTags4();

		doTestAssetTagsAllAndAny();
		doTestAssetTagsNotAllAndAll();
		doTestAssetTagsNotAllAndAny();
		doTestAssetTagsNotAllAndNotAny();
		doTestAssetTagsNotAnyAndAll();
		doTestAssetTagsNotAnyAndAny();

		doTestAllAssetTags1();
		doTestAllAssetTags2();
		doTestAllAssetTags3();
		doTestAllAssetTags4();

		doTestNotAnyAssetTags1();
		doTestNotAnyAssetTags2();
		doTestNotAnyAssetTags2();
		doTestNotAnyAssetTags2();
	}

	@Test
	public void testClassName() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);

		addBaseModel(parentBaseModel, getSearchKeywords(), serviceContext);

		doTestClassName1();
		doTestClassName2();
	}

	@Test
	public void testClassTypeIds() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);

		addBaseModelWithClassType(
			parentBaseModel, getSearchKeywords(), serviceContext);

		doTestClassTypeIds1();
		doTestClassTypeIds2();
	}

	@Test
	public void testGroups() throws Exception {
		AssetEntryQuery assetEntryQuery = new AssetEntryQuery();

		assetEntryQuery.setClassName(getBaseModelClassName());

		Group group1 = GroupTestUtil.addGroup();
		Group group2 = GroupTestUtil.addGroup();

		assetEntryQuery.setGroupIds(
			new long[] {group1.getGroupId(), group2.getGroupId()});

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		int initialEntries = searchCount(assetEntryQuery, searchContext);

		ServiceContext serviceContext1 =
			ServiceContextTestUtil.getServiceContext(group1.getGroupId());

		BaseModel<?> parentBaseModel1 = getParentBaseModel(
			group1, serviceContext1);

		addBaseModel(parentBaseModel1, getSearchKeywords(), serviceContext1);

		ServiceContext serviceContext2 =
			ServiceContextTestUtil.getServiceContext(group2.getGroupId());

		BaseModel<?> parentBaseModel2 = getParentBaseModel(
			group1, serviceContext2);

		addBaseModel(parentBaseModel2, getSearchKeywords(), serviceContext2);

		Assert.assertEquals(
			initialEntries + 2, searchCount(assetEntryQuery, searchContext));
	}

	@Test
	public void testOrderByCreateDate() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);
		String[] titles = {
			"open", "liferay", "social", "osgi", "content", "life"
		};

		BaseModel<?>[] baseModels = new BaseModel[titles.length];

		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];

			baseModels[i] = addBaseModel(
				parentBaseModel, title, serviceContext);
		}

		doTestOrderByCreateDateAsc(titles);
		doTestOrderByCreateDateDesc(titles);
	}

	@Test
	public void testOrderByExpirationDate() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);
		Date[] expirationDates = generateRandomDates(new Date(), 6);

		for (Date expirationDate : expirationDates) {
			addBaseModel(
				parentBaseModel, RandomTestUtil.randomString(), expirationDate,
				serviceContext);
		}

		doTestOrderByExpirationDateAsc(expirationDates);
		doTestOrderByExpirationDateDesc(expirationDates);
	}

	@Test
	public void testOrderByTitle() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);
		String[] titles = {
			"open", "liferay", "social", "osgi", "content", "life"
		};

		for (String title : titles) {
			addBaseModel(parentBaseModel, title, serviceContext);
		}

		doTestOrderByTitleAsc(titles);
		doTestOrderByTitleDesc(titles);
	}

	@Test
	public void testPaginationType() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		BaseModel<?> parentBaseModel = getParentBaseModel(
			_group, serviceContext);

		int size = 5;

		for (int i = 0; i < size; i++) {
			addBaseModel(
				parentBaseModel, RandomTestUtil.randomString(), serviceContext);
		}

		doTestPaginationTypeNone(size);
		doTestPaginationTypeRegular(size);
		doTestPaginationTypeSimple(size);
	}

	protected BaseModel<?> addBaseModel(
			BaseModel<?> parentBaseModel, String keywords, Date expirationDate,
			ServiceContext serviceContext)
		throws Exception {

		return addBaseModel(parentBaseModel, keywords, serviceContext);
	}

	protected abstract BaseModel<?> addBaseModel(
			BaseModel<?> parentBaseModel, String keywords,
			ServiceContext serviceContext)
		throws Exception;

	protected BaseModel<?> addBaseModelWithClassType(
			BaseModel<?> parentBaseModel, String keywords,
			ServiceContext serviceContext)
		throws Exception {

		return addBaseModel(parentBaseModel, keywords, serviceContext);
	}

	protected BaseModel<?> addBaseModelWithWorkflow(
			BaseModel<?> parentBaseModel, String keywords, boolean approved,
			ServiceContext serviceContext)
		throws Exception {

		return addBaseModel(parentBaseModel, keywords, serviceContext);
	}

	protected void doTestAllAssetCategories1() throws Exception {
		long[] allCategoryIds = {_healthCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAllAssetCategories2() throws Exception {
		long[] allCategoryIds = {_healthCategoryId, _sportCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAllAssetCategories3() throws Exception {
		long[] allCategoryIds =
			{_healthCategoryId, _sportCategoryId, _foodCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAllAssetCategories4() throws Exception {
		long[] allCategoryIds = {
			_healthCategoryId, _sportCategoryId, _foodCategoryId,
			_travelCategoryId
		};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestAllAssetTags1() throws Exception {
		String[] allTags = {"liferay"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAllAssetTags2() throws Exception {
		String[] allTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAllAssetTags3() throws Exception {
		String[] allTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAllAssetTags4() throws Exception {
		String[] allTags = {"liferay", "architecture", "services", "osgi"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestAnyAssetCategories1() throws Exception {
		long[] anyCategoryIds = {_healthCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetCategories2() throws Exception {
		long[] anyCategoryIds = {_healthCategoryId, _sportCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetCategories3() throws Exception {
		long[] anyCategoryIds =
			{_healthCategoryId, _sportCategoryId, _foodCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetCategories4() throws Exception {
		long[] anyCategoryIds = {_fashionCategoryId, _foodCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAnyAssetTags1() throws Exception {
		String[] anyTags = {"liferay"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyTags);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetTags2() throws Exception {
		String[] anyTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyTags);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetTags3() throws Exception {
		String[] anyTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyTags);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestAnyAssetTags4() throws Exception {
		String[] anyTags = {"modularity", "osgi"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null, null,
				anyTags);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetCategorization(
			AssetEntryQuery assetEntryQuery, int expectedResults)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		Assert.assertEquals(
			expectedResults, searchCount(assetEntryQuery, searchContext));
	}

	protected void doTestAssetCategoryAllAndAny() throws Exception {
		long[] allCategoryIds =
			{_healthCategoryId, _sportCategoryId, _travelCategoryId};
		long[] anyCategoryIds = {_healthCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allCategoryIds, anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetCategoryNotAllAndAll() throws Exception {
		long[] notAllCategoryIds = {_fashionCategoryId, _foodCategoryId};
		long[] allCategoryIds =
			{_healthCategoryId, _sportCategoryId, _travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetCategoryNotAllAndAny() throws Exception {
		long[] notAllCategoryIds = {_fashionCategoryId};
		long[] anyCategoryIds = {_sportCategoryId, _travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, null, anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetCategoryNotAllAndNotAny() throws Exception {
		long[] notAllCategoryIds = {_fashionCategoryId, _foodCategoryId};
		long[] notAnyCategoryIds = {_travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				notAnyCategoryIds, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestAssetCategoryNotAnyAndAll() throws Exception {
		long[] notAnyCategoryIds = {_fashionCategoryId};
		long[] allCategoryIds = {_healthCategoryId, _sportCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, allCategoryIds, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetCategoryNotAnyAndAny() throws Exception {
		long[] notAnyCategoryIds = {_fashionCategoryId, _foodCategoryId};
		long[] anyCategoryIds =
			{_healthCategoryId, _sportCategoryId, _travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, null, anyCategoryIds);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetTagsAllAndAny() throws Exception {
		String[] allTags = {"liferay", "architecture", "services"};
		String[] anyTags = {"liferay"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, null,
				allTags, anyTags);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetTagsNotAllAndAll() throws Exception {
		String[] notAllTags = {"osgi", "modularity"};
		String[] allTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetTagsNotAllAndAny() throws Exception {
		String[] notAllTags = {"services"};
		String[] anyTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				null, anyTags);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetTagsNotAllAndNotAny() throws Exception {
		String[] notAllTags = {"osgi", "modularity"};
		String[] notAnyTags = {"services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags,
				notAnyTags, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestAssetTagsNotAnyAndAll() throws Exception {
		String[] notAnyTags = {"modularity"};
		String[] allTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				allTags, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestAssetTagsNotAnyAndAny() throws Exception {
		String[] notAnyTags = {"modularity", "osgi"};
		String[] anyTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				null, anyTags);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestClassName1() throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestClassNames(assetEntryQuery, 1);
	}

	protected void doTestClassName2() throws Exception {
		long[] classNameIds =
			AssetRendererFactoryRegistryUtil.getClassNameIds(
				TestPropsValues.getCompanyId());

		classNameIds = ArrayUtil.remove(
			classNameIds, PortalUtil.getClassNameId(getBaseModelClass()));

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), classNameIds);

		doTestClassNames(assetEntryQuery, 0);
	}

	protected void doTestClassNames(
			AssetEntryQuery assetEntryQuery, int expectedResult)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		Assert.assertEquals(
			expectedResult, searchCount(assetEntryQuery, searchContext));
	}

	protected void doTestClassTypeIds(
			AssetEntryQuery assetEntryQuery, boolean classType)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		int initialEntries = searchCount(assetEntryQuery, searchContext);

		if (classType) {
			assetEntryQuery.setClassTypeIds(getClassTypeIds());

			Assert.assertEquals(1, searchCount(assetEntryQuery, searchContext));
		}
		else {
			assetEntryQuery.setClassTypeIds(new long[] {0});

			Assert.assertEquals(0, searchCount(assetEntryQuery, searchContext));
		}
	}

	protected void doTestClassTypeIds1() throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestClassTypeIds(assetEntryQuery, true);
	}

	protected void doTestClassTypeIds2() throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestClassTypeIds(assetEntryQuery, false);
	}

	protected void doTestNotAllAssetCategories1() throws Exception {
		long[] notAllCategoryIds = {_healthCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAllAssetCategories2() throws Exception {
		long[] notAllCategoryIds = {_healthCategoryId, _sportCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAllAssetCategories3() throws Exception {
		long[] notAllCategoryIds = {_fashionCategoryId, _foodCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, null, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestNotAllAssetCategories4() throws Exception {
		long[] notAllCategoryIds =
			{_fashionCategoryId, _foodCategoryId, _travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllCategoryIds,
				null, null, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestNotAllAssetTags1() throws Exception {
		String[] notAllTags = {"liferay"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAllAssetTags2() throws Exception {
		String[] notAllTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAllAssetTags3() throws Exception {
		String[] notAllTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestNotAllAssetTags4() throws Exception {
		String[] notAllTags = {"liferay", "architecture", "services", "osgi"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), notAllTags, null,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 2);
	}

	protected void doTestNotAnyAssetCategories1() throws Exception {
		long[] notAnyCategoryIds = {_healthCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetCategories2() throws Exception {
		long[] notAnyCategoryIds = {_healthCategoryId, _sportCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetCategories3() throws Exception {
		long[] notAnyCategoryIds =
			{_fashionCategoryId, _foodCategoryId, _travelCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetCategories4() throws Exception {
		long[] notAnyCategoryIds = {_fashionCategoryId, _foodCategoryId};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null,
				notAnyCategoryIds, null, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestNotAnyAssetTags1() throws Exception {
		String[] notAnyTags = {"liferay"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetTags2() throws Exception {
		String[] notAnyTags = {"liferay", "architecture"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetTags3() throws Exception {
		String[] notAnyTags = {"liferay", "architecture", "services"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 0);
	}

	protected void doTestNotAnyAssetTags4() throws Exception {
		String[] notAnyTags = {"modularity", "osgi"};

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), getBaseModelClassName(), null, notAnyTags,
				null, null);

		doTestAssetCategorization(assetEntryQuery, 1);
	}

	protected void doTestOrderByCreateDate(
			AssetEntryQuery assetEntryQuery, String orderByType,
			String[] titles, String[] orderedTitles)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		assetEntryQuery.setOrderByCol1("createDate");
		assetEntryQuery.setOrderByType1(orderByType);

		AssetEntry[] assetEntries = search(assetEntryQuery, searchContext);

		for (int i = 0; i < assetEntries.length; i++) {
			AssetEntry assetEntry = assetEntries[i];

			String field = assetEntry.getTitle(LocaleUtil.getDefault());

			Assert.assertEquals(field, orderedTitles[i]);
		}
	}

	protected void doTestOrderByCreateDateAsc(String[] titles)
		throws Exception {

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestOrderByCreateDate(assetEntryQuery, "asc", titles, titles);
	}

	protected void doTestOrderByCreateDateDesc(String[] titles)
		throws Exception {

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		String[] orderedTitles = {
			"life", "content", "osgi", "social", "liferay", "open"
		};

		doTestOrderByCreateDate(assetEntryQuery, "desc", titles, orderedTitles);
	}

	protected void doTestOrderByExpirationDate(
			AssetEntryQuery assetEntryQuery, String orderByType,
			Date[] expirationDates)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		assetEntryQuery.setOrderByCol1("expirationDate");
		assetEntryQuery.setOrderByType1(orderByType);

		Arrays.sort(expirationDates);

		DateFormat dateFormat = DateFormatFactoryUtil.getSimpleDateFormat(
			PropsValues.INDEX_DATE_FORMAT_PATTERN);

		AssetEntry[] assetEntries = search(assetEntryQuery, searchContext);

		for (int i = 0; i < assetEntries.length; i++) {
			AssetEntry assetEntry = assetEntries[i];

			String expirationDate = dateFormat.format(
				assetEntry.getExpirationDate());

			int index = i;

			if (orderByType.equals("desc")) {
				index = assetEntries.length - 1 - i;
			}

			Assert.assertEquals(
				expirationDate, dateFormat.format(expirationDates[index]));
		}
	}

	protected void doTestOrderByExpirationDateAsc(Date[] expirationDates)
		throws Exception {

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestOrderByExpirationDate(assetEntryQuery, "asc", expirationDates);
	}

	protected void doTestOrderByExpirationDateDesc(Date[] expirationDates)
		throws Exception {

		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		doTestOrderByExpirationDate(assetEntryQuery, "desc", expirationDates);
	}

	protected void doTestOrderByTitle(
		AssetEntryQuery assetEntryQuery, String orderByType, String[] titles,
		String[] orderedTitles) throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		assetEntryQuery.setOrderByCol1("title");
		assetEntryQuery.setOrderByType1(orderByType);

		AssetEntry[] assetEntries = search(assetEntryQuery, searchContext);

		for (int i = 0; i < assetEntries.length; i++) {
			AssetEntry assetEntry = assetEntries[i];

			String field = assetEntry.getTitle(LocaleUtil.getDefault());

			Assert.assertEquals(field, orderedTitles[i]);
		}
	}

	protected void doTestOrderByTitleAsc(String[] titles) throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		String[] orderedTitles = {
			"content", "life", "liferay", "open", "osgi", "social"
		};

		doTestOrderByTitle(assetEntryQuery, "asc", titles, orderedTitles);
	}

	protected void doTestOrderByTitleDesc(String[] titles) throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		String[] orderedTitles = {
			"social", "osgi", "open", "liferay", "life", "content"
		};

		doTestOrderByTitle(assetEntryQuery, "desc", titles, orderedTitles);
	}

	protected void doTestPaginationType(
			AssetEntryQuery assetEntryQuery, int size)
		throws Exception {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext();

		searchContext.setGroupIds(assetEntryQuery.getGroupIds());

		Assert.assertEquals(
			size, searchCount(assetEntryQuery, searchContext, 0, 1));
	}

	protected void doTestPaginationTypeNone(int size) throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		assetEntryQuery.setPaginationType("none");

		doTestPaginationType(assetEntryQuery, size);
	}

	protected void doTestPaginationTypeRegular(int size) throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		assetEntryQuery.setPaginationType("regular");

		doTestPaginationType(assetEntryQuery, size);
	}

	protected void doTestPaginationTypeSimple(int size) throws Exception {
		AssetEntryQuery assetEntryQuery =
			AssetEntryQueryTestUtil.createAssetEntryQuery(
				_group.getGroupId(), new String[] {getBaseModelClassName()});

		assetEntryQuery.setPaginationType("simple");

		doTestPaginationType(assetEntryQuery, size);
	}

	protected Date[] generateRandomDates(Date startDate, int size) {
		Date[] dates = new Date[size];

		for (int i = 0; i < size; i++) {
			Date date = new Date(
				startDate.getTime() +
				(long)(Math.random() * 60 * 60 * 24 * 365));

			Calendar calendar = new GregorianCalendar();

			calendar.setTime(date);

			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			dates[i] = calendar.getTime();
		}

		return dates;
	}

	protected abstract Class<?> getBaseModelClass();

	protected String getBaseModelClassName() {
		Class<?> clazz = getBaseModelClass();

		return clazz.getName();
	}

	protected long[] getClassTypeIds() {
		return null;
	}

	protected BaseModel<?> getParentBaseModel(
			Group group, ServiceContext serviceContext)
		throws Exception {

		return group;
	}

	protected abstract String getSearchKeywords();

	protected AssetEntry[] search(
			AssetEntryQuery assetEntryQuery, SearchContext searchContext)
		throws Exception {

		Hits results = AssetUtil.search(
			searchContext, assetEntryQuery, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);

		List<AssetEntry> assetEntries = AssetUtil.getAssetEntries(results);

		return assetEntries.toArray(new AssetEntry[assetEntries.size()]);
	}

	protected int searchCount(
			AssetEntryQuery assetEntryQuery, SearchContext searchContext)
		throws Exception {

		return searchCount(
			assetEntryQuery, searchContext, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);
	}

	protected int searchCount(
			AssetEntryQuery assetEntryQuery, SearchContext searchContext,
			int start, int end)
		throws Exception {

		Hits results = AssetUtil.search(
			searchContext, assetEntryQuery, start, end);

		return results.getLength();
	}

	private long[] _assetCategoryIds1;
	private long[] _assetCategoryIds2;
	private String[] _assetTagsNames1;
	private String[] _assetTagsNames2;
	private long _fashionCategoryId;
	private long _foodCategoryId;

	@DeleteAfterTestRun
	private Group _group;

	private long _healthCategoryId;
	private long _sportCategoryId;
	private long _travelCategoryId;
	private long _vocabularyId;

}