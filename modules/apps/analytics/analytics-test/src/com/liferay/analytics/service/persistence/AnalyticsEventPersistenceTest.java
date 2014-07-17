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

package com.liferay.analytics.service.persistence;

import com.liferay.analytics.NoSuchEventException;
import com.liferay.analytics.model.AnalyticsEvent;
import com.liferay.analytics.service.AnalyticsEventLocalServiceUtil;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.test.TransactionalTestRule;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.test.RandomTestUtil;

import com.liferay.util.bean.PortletBeanLocatorUtil;

import org.jboss.arquillian.junit.Arquillian;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Brian Wing Shun Chan
 */
@RunWith(Arquillian.class)
public class AnalyticsEventPersistenceTest {
	@BeforeClass
	public static void setupClass() throws TemplateException {
		TemplateManagerUtil.init();

		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = false;
	}

	public static void tearDownClass() {
		PropsValues.SPRING_HIBERNATE_SESSION_DELEGATED = true;
	}

	@ClassRule
	public static TransactionalTestRule transactionalTestRule = new TransactionalTestRule();

	@Before
	public void setUp() {
		_modelListeners = _persistence.getListeners();

		for (ModelListener<AnalyticsEvent> modelListener : _modelListeners) {
			_persistence.unregisterListener(modelListener);
		}
	}

	@After
	public void tearDown() throws Exception {
		Iterator<AnalyticsEvent> iterator = _analyticsEvents.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}

		for (ModelListener<AnalyticsEvent> modelListener : _modelListeners) {
			_persistence.registerListener(modelListener);
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		AnalyticsEvent analyticsEvent = _persistence.create(pk);

		Assert.assertNotNull(analyticsEvent);

		Assert.assertEquals(analyticsEvent.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		_persistence.remove(newAnalyticsEvent);

		AnalyticsEvent existingAnalyticsEvent = _persistence.fetchByPrimaryKey(newAnalyticsEvent.getPrimaryKey());

		Assert.assertNull(existingAnalyticsEvent);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAnalyticsEvent();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		AnalyticsEvent newAnalyticsEvent = _persistence.create(pk);

		newAnalyticsEvent.setCompanyId(RandomTestUtil.nextLong());

		newAnalyticsEvent.setUserId(RandomTestUtil.nextLong());

		newAnalyticsEvent.setCreateDate(RandomTestUtil.nextDate());

		newAnalyticsEvent.setAnonymousUserId(RandomTestUtil.nextLong());

		newAnalyticsEvent.setClassName(RandomTestUtil.randomString());

		newAnalyticsEvent.setClassPK(RandomTestUtil.nextLong());

		newAnalyticsEvent.setReferrerClassName(RandomTestUtil.randomString());

		newAnalyticsEvent.setReferrerClassPK(RandomTestUtil.nextLong());

		newAnalyticsEvent.setElementKey(RandomTestUtil.randomString());

		newAnalyticsEvent.setType(RandomTestUtil.randomString());

		newAnalyticsEvent.setClientIP(RandomTestUtil.randomString());

		newAnalyticsEvent.setUserAgent(RandomTestUtil.randomString());

		newAnalyticsEvent.setLanguageId(RandomTestUtil.randomString());

		newAnalyticsEvent.setUrl(RandomTestUtil.randomString());

		newAnalyticsEvent.setAdditionalInfo(RandomTestUtil.randomString());

		_analyticsEvents.add(_persistence.update(newAnalyticsEvent));

		AnalyticsEvent existingAnalyticsEvent = _persistence.findByPrimaryKey(newAnalyticsEvent.getPrimaryKey());

		Assert.assertEquals(existingAnalyticsEvent.getAnalyticsEventId(),
			newAnalyticsEvent.getAnalyticsEventId());
		Assert.assertEquals(existingAnalyticsEvent.getCompanyId(),
			newAnalyticsEvent.getCompanyId());
		Assert.assertEquals(existingAnalyticsEvent.getUserId(),
			newAnalyticsEvent.getUserId());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAnalyticsEvent.getCreateDate()),
			Time.getShortTimestamp(newAnalyticsEvent.getCreateDate()));
		Assert.assertEquals(existingAnalyticsEvent.getAnonymousUserId(),
			newAnalyticsEvent.getAnonymousUserId());
		Assert.assertEquals(existingAnalyticsEvent.getClassName(),
			newAnalyticsEvent.getClassName());
		Assert.assertEquals(existingAnalyticsEvent.getClassPK(),
			newAnalyticsEvent.getClassPK());
		Assert.assertEquals(existingAnalyticsEvent.getReferrerClassName(),
			newAnalyticsEvent.getReferrerClassName());
		Assert.assertEquals(existingAnalyticsEvent.getReferrerClassPK(),
			newAnalyticsEvent.getReferrerClassPK());
		Assert.assertEquals(existingAnalyticsEvent.getElementKey(),
			newAnalyticsEvent.getElementKey());
		Assert.assertEquals(existingAnalyticsEvent.getType(),
			newAnalyticsEvent.getType());
		Assert.assertEquals(existingAnalyticsEvent.getClientIP(),
			newAnalyticsEvent.getClientIP());
		Assert.assertEquals(existingAnalyticsEvent.getUserAgent(),
			newAnalyticsEvent.getUserAgent());
		Assert.assertEquals(existingAnalyticsEvent.getLanguageId(),
			newAnalyticsEvent.getLanguageId());
		Assert.assertEquals(existingAnalyticsEvent.getUrl(),
			newAnalyticsEvent.getUrl());
		Assert.assertEquals(existingAnalyticsEvent.getAdditionalInfo(),
			newAnalyticsEvent.getAdditionalInfo());
	}

	@Test
	public void testCountByCompanyId() {
		try {
			_persistence.countByCompanyId(RandomTestUtil.nextLong());

			_persistence.countByCompanyId(0L);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByC_GtCD() {
		try {
			_persistence.countByC_GtCD(RandomTestUtil.nextLong(),
				RandomTestUtil.nextDate());

			_persistence.countByC_GtCD(0L, RandomTestUtil.nextDate());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByC_LtCD() {
		try {
			_persistence.countByC_LtCD(RandomTestUtil.nextLong(),
				RandomTestUtil.nextDate());

			_persistence.countByC_LtCD(0L, RandomTestUtil.nextDate());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByGtCD_E_E() {
		try {
			_persistence.countByGtCD_E_E(RandomTestUtil.nextDate(),
				StringPool.BLANK, StringPool.BLANK);

			_persistence.countByGtCD_E_E(RandomTestUtil.nextDate(),
				StringPool.NULL, StringPool.NULL);

			_persistence.countByGtCD_E_E(RandomTestUtil.nextDate(),
				(String)null, (String)null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByGtCD_C_C_E() {
		try {
			_persistence.countByGtCD_C_C_E(RandomTestUtil.nextDate(),
				StringPool.BLANK, RandomTestUtil.nextLong(), StringPool.BLANK);

			_persistence.countByGtCD_C_C_E(RandomTestUtil.nextDate(),
				StringPool.NULL, 0L, StringPool.NULL);

			_persistence.countByGtCD_C_C_E(RandomTestUtil.nextDate(),
				(String)null, 0L, (String)null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByGtCD_C_C_R_R_E() {
		try {
			_persistence.countByGtCD_C_C_R_R_E(RandomTestUtil.nextDate(),
				StringPool.BLANK, RandomTestUtil.nextLong(), StringPool.BLANK,
				RandomTestUtil.nextLong(), StringPool.BLANK);

			_persistence.countByGtCD_C_C_R_R_E(RandomTestUtil.nextDate(),
				StringPool.NULL, 0L, StringPool.NULL, 0L, StringPool.NULL);

			_persistence.countByGtCD_C_C_R_R_E(RandomTestUtil.nextDate(),
				(String)null, 0L, (String)null, 0L, (String)null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testCountByGtCD_R_R_E_E() {
		try {
			_persistence.countByGtCD_R_R_E_E(RandomTestUtil.nextDate(),
				StringPool.BLANK, RandomTestUtil.nextLong(), StringPool.BLANK,
				StringPool.BLANK);

			_persistence.countByGtCD_R_R_E_E(RandomTestUtil.nextDate(),
				StringPool.NULL, 0L, StringPool.NULL, StringPool.NULL);

			_persistence.countByGtCD_R_R_E_E(RandomTestUtil.nextDate(),
				(String)null, 0L, (String)null, (String)null);
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		AnalyticsEvent existingAnalyticsEvent = _persistence.findByPrimaryKey(newAnalyticsEvent.getPrimaryKey());

		Assert.assertEquals(existingAnalyticsEvent, newAnalyticsEvent);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchEventException");
		}
		catch (NoSuchEventException nsee) {
		}
	}

	@Test
	public void testFindAll() throws Exception {
		try {
			_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
				getOrderByComparator());
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	protected OrderByComparator<AnalyticsEvent> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("Analytics_AnalyticsEvent",
			"analyticsEventId", true, "companyId", true, "userId", true,
			"createDate", true, "anonymousUserId", true, "className", true,
			"classPK", true, "referrerClassName", true, "referrerClassPK",
			true, "elementKey", true, "type", true, "clientIP", true,
			"userAgent", true, "languageId", true, "url", true,
			"additionalInfo", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		AnalyticsEvent existingAnalyticsEvent = _persistence.fetchByPrimaryKey(newAnalyticsEvent.getPrimaryKey());

		Assert.assertEquals(existingAnalyticsEvent, newAnalyticsEvent);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		AnalyticsEvent missingAnalyticsEvent = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAnalyticsEvent);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		AnalyticsEvent newAnalyticsEvent1 = addAnalyticsEvent();
		AnalyticsEvent newAnalyticsEvent2 = addAnalyticsEvent();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newAnalyticsEvent1.getPrimaryKey());
		primaryKeys.add(newAnalyticsEvent2.getPrimaryKey());

		Map<Serializable, AnalyticsEvent> analyticsEvents = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, analyticsEvents.size());
		Assert.assertEquals(newAnalyticsEvent1,
			analyticsEvents.get(newAnalyticsEvent1.getPrimaryKey()));
		Assert.assertEquals(newAnalyticsEvent2,
			analyticsEvents.get(newAnalyticsEvent2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, AnalyticsEvent> analyticsEvents = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(analyticsEvents.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newAnalyticsEvent.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, AnalyticsEvent> analyticsEvents = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, analyticsEvents.size());
		Assert.assertEquals(newAnalyticsEvent,
			analyticsEvents.get(newAnalyticsEvent.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, AnalyticsEvent> analyticsEvents = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(analyticsEvents.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newAnalyticsEvent.getPrimaryKey());

		Map<Serializable, AnalyticsEvent> analyticsEvents = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, analyticsEvents.size());
		Assert.assertEquals(newAnalyticsEvent,
			analyticsEvents.get(newAnalyticsEvent.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = AnalyticsEventLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod() {
				@Override
				public void performAction(Object object) {
					AnalyticsEvent analyticsEvent = (AnalyticsEvent)object;

					Assert.assertNotNull(analyticsEvent);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AnalyticsEvent.class,
				AnalyticsEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("analyticsEventId",
				newAnalyticsEvent.getAnalyticsEventId()));

		List<AnalyticsEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		AnalyticsEvent existingAnalyticsEvent = result.get(0);

		Assert.assertEquals(existingAnalyticsEvent, newAnalyticsEvent);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AnalyticsEvent.class,
				AnalyticsEvent.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("analyticsEventId",
				RandomTestUtil.nextLong()));

		List<AnalyticsEvent> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		AnalyticsEvent newAnalyticsEvent = addAnalyticsEvent();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AnalyticsEvent.class,
				AnalyticsEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"analyticsEventId"));

		Object newAnalyticsEventId = newAnalyticsEvent.getAnalyticsEventId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("analyticsEventId",
				new Object[] { newAnalyticsEventId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingAnalyticsEventId = result.get(0);

		Assert.assertEquals(existingAnalyticsEventId, newAnalyticsEventId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AnalyticsEvent.class,
				AnalyticsEvent.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"analyticsEventId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("analyticsEventId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected AnalyticsEvent addAnalyticsEvent() throws Exception {
		long pk = RandomTestUtil.nextLong();

		AnalyticsEvent analyticsEvent = _persistence.create(pk);

		analyticsEvent.setCompanyId(RandomTestUtil.nextLong());

		analyticsEvent.setUserId(RandomTestUtil.nextLong());

		analyticsEvent.setCreateDate(RandomTestUtil.nextDate());

		analyticsEvent.setAnonymousUserId(RandomTestUtil.nextLong());

		analyticsEvent.setClassName(RandomTestUtil.randomString());

		analyticsEvent.setClassPK(RandomTestUtil.nextLong());

		analyticsEvent.setReferrerClassName(RandomTestUtil.randomString());

		analyticsEvent.setReferrerClassPK(RandomTestUtil.nextLong());

		analyticsEvent.setElementKey(RandomTestUtil.randomString());

		analyticsEvent.setType(RandomTestUtil.randomString());

		analyticsEvent.setClientIP(RandomTestUtil.randomString());

		analyticsEvent.setUserAgent(RandomTestUtil.randomString());

		analyticsEvent.setLanguageId(RandomTestUtil.randomString());

		analyticsEvent.setUrl(RandomTestUtil.randomString());

		analyticsEvent.setAdditionalInfo(RandomTestUtil.randomString());

		_analyticsEvents.add(_persistence.update(analyticsEvent));

		return analyticsEvent;
	}

	private List<AnalyticsEvent> _analyticsEvents = new ArrayList<AnalyticsEvent>();
	private ModelListener<AnalyticsEvent>[] _modelListeners;
	private AnalyticsEventPersistence _persistence = (AnalyticsEventPersistence)PortletBeanLocatorUtil.locate(AnalyticsEventPersistence.class.getName());
}