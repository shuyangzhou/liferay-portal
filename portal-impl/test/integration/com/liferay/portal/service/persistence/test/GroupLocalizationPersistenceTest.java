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

package com.liferay.portal.service.persistence.test;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.NoSuchGroupLocalizationException;
import com.liferay.portal.kernel.model.GroupLocalization;
import com.liferay.portal.kernel.service.persistence.GroupLocalizationPersistence;
import com.liferay.portal.kernel.service.persistence.GroupLocalizationUtil;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @generated
 */
public class GroupLocalizationPersistenceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = GroupLocalizationUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<GroupLocalization> iterator = _groupLocalizations.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		GroupLocalization groupLocalization = _persistence.create(pk);

		Assert.assertNotNull(groupLocalization);

		Assert.assertEquals(groupLocalization.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		_persistence.remove(newGroupLocalization);

		GroupLocalization existingGroupLocalization = _persistence.fetchByPrimaryKey(newGroupLocalization.getPrimaryKey());

		Assert.assertNull(existingGroupLocalization);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addGroupLocalization();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		GroupLocalization newGroupLocalization = _persistence.create(pk);

		newGroupLocalization.setMvccVersion(RandomTestUtil.nextLong());

		newGroupLocalization.setCompanyId(RandomTestUtil.nextLong());

		newGroupLocalization.setGroupId(RandomTestUtil.nextLong());

		newGroupLocalization.setLanguageId(RandomTestUtil.randomString());

		newGroupLocalization.setName(RandomTestUtil.randomString());

		newGroupLocalization.setDescription(RandomTestUtil.randomString());

		_groupLocalizations.add(_persistence.update(newGroupLocalization));

		GroupLocalization existingGroupLocalization = _persistence.findByPrimaryKey(newGroupLocalization.getPrimaryKey());

		Assert.assertEquals(existingGroupLocalization.getMvccVersion(),
			newGroupLocalization.getMvccVersion());
		Assert.assertEquals(existingGroupLocalization.getGroupLocalizationId(),
			newGroupLocalization.getGroupLocalizationId());
		Assert.assertEquals(existingGroupLocalization.getCompanyId(),
			newGroupLocalization.getCompanyId());
		Assert.assertEquals(existingGroupLocalization.getGroupId(),
			newGroupLocalization.getGroupId());
		Assert.assertEquals(existingGroupLocalization.getLanguageId(),
			newGroupLocalization.getLanguageId());
		Assert.assertEquals(existingGroupLocalization.getName(),
			newGroupLocalization.getName());
		Assert.assertEquals(existingGroupLocalization.getDescription(),
			newGroupLocalization.getDescription());
	}

	@Test
	public void testCountByGroupId() throws Exception {
		_persistence.countByGroupId(RandomTestUtil.nextLong());

		_persistence.countByGroupId(0L);
	}

	@Test
	public void testCountByGroupId_LanguageId() throws Exception {
		_persistence.countByGroupId_LanguageId(RandomTestUtil.nextLong(),
			StringPool.BLANK);

		_persistence.countByGroupId_LanguageId(0L, StringPool.NULL);

		_persistence.countByGroupId_LanguageId(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		GroupLocalization existingGroupLocalization = _persistence.findByPrimaryKey(newGroupLocalization.getPrimaryKey());

		Assert.assertEquals(existingGroupLocalization, newGroupLocalization);
	}

	@Test(expected = NoSuchGroupLocalizationException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<GroupLocalization> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("GroupLocalization",
			"mvccVersion", true, "groupLocalizationId", true, "companyId",
			true, "groupId", true, "languageId", true, "name", true,
			"description", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		GroupLocalization existingGroupLocalization = _persistence.fetchByPrimaryKey(newGroupLocalization.getPrimaryKey());

		Assert.assertEquals(existingGroupLocalization, newGroupLocalization);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		GroupLocalization missingGroupLocalization = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingGroupLocalization);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		GroupLocalization newGroupLocalization1 = addGroupLocalization();
		GroupLocalization newGroupLocalization2 = addGroupLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newGroupLocalization1.getPrimaryKey());
		primaryKeys.add(newGroupLocalization2.getPrimaryKey());

		Map<Serializable, GroupLocalization> groupLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, groupLocalizations.size());
		Assert.assertEquals(newGroupLocalization1,
			groupLocalizations.get(newGroupLocalization1.getPrimaryKey()));
		Assert.assertEquals(newGroupLocalization2,
			groupLocalizations.get(newGroupLocalization2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, GroupLocalization> groupLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(groupLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newGroupLocalization.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, GroupLocalization> groupLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, groupLocalizations.size());
		Assert.assertEquals(newGroupLocalization,
			groupLocalizations.get(newGroupLocalization.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, GroupLocalization> groupLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(groupLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newGroupLocalization.getPrimaryKey());

		Map<Serializable, GroupLocalization> groupLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, groupLocalizations.size());
		Assert.assertEquals(newGroupLocalization,
			groupLocalizations.get(newGroupLocalization.getPrimaryKey()));
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(GroupLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupLocalizationId",
				newGroupLocalization.getGroupLocalizationId()));

		List<GroupLocalization> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		GroupLocalization existingGroupLocalization = result.get(0);

		Assert.assertEquals(existingGroupLocalization, newGroupLocalization);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(GroupLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("groupLocalizationId",
				RandomTestUtil.nextLong()));

		List<GroupLocalization> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(GroupLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"groupLocalizationId"));

		Object newGroupLocalizationId = newGroupLocalization.getGroupLocalizationId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupLocalizationId",
				new Object[] { newGroupLocalizationId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingGroupLocalizationId = result.get(0);

		Assert.assertEquals(existingGroupLocalizationId, newGroupLocalizationId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(GroupLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"groupLocalizationId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("groupLocalizationId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		GroupLocalization newGroupLocalization = addGroupLocalization();

		_persistence.clearCache();

		GroupLocalization existingGroupLocalization = _persistence.findByPrimaryKey(newGroupLocalization.getPrimaryKey());

		Assert.assertEquals(Long.valueOf(existingGroupLocalization.getGroupId()),
			ReflectionTestUtil.<Long>invoke(existingGroupLocalization,
				"getOriginalGroupId", new Class<?>[0]));
		Assert.assertTrue(Objects.equals(
				existingGroupLocalization.getLanguageId(),
				ReflectionTestUtil.invoke(existingGroupLocalization,
					"getOriginalLanguageId", new Class<?>[0])));
	}

	protected GroupLocalization addGroupLocalization()
		throws Exception {
		long pk = RandomTestUtil.nextLong();

		GroupLocalization groupLocalization = _persistence.create(pk);

		groupLocalization.setMvccVersion(RandomTestUtil.nextLong());

		groupLocalization.setCompanyId(RandomTestUtil.nextLong());

		groupLocalization.setGroupId(RandomTestUtil.nextLong());

		groupLocalization.setLanguageId(RandomTestUtil.randomString());

		groupLocalization.setName(RandomTestUtil.randomString());

		groupLocalization.setDescription(RandomTestUtil.randomString());

		_groupLocalizations.add(_persistence.update(groupLocalization));

		return groupLocalization;
	}

	private List<GroupLocalization> _groupLocalizations = new ArrayList<GroupLocalization>();
	private GroupLocalizationPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}