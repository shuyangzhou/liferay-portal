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
import com.liferay.portal.kernel.exception.NoSuchABCTestEntityException;
import com.liferay.portal.kernel.model.ABCTestEntity;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityPersistence;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
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
import java.util.Set;

/**
 * @generated
 */
public class ABCTestEntityPersistenceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = ABCTestEntityUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ABCTestEntity> iterator = _abcTestEntities.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		String pk = RandomTestUtil.randomString();

		ABCTestEntity abcTestEntity = _persistence.create(pk);

		Assert.assertNotNull(abcTestEntity);

		Assert.assertEquals(abcTestEntity.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		_persistence.remove(newABCTestEntity);

		ABCTestEntity existingABCTestEntity = _persistence.fetchByPrimaryKey(newABCTestEntity.getPrimaryKey());

		Assert.assertNull(existingABCTestEntity);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addABCTestEntity();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		String pk = RandomTestUtil.randomString();

		ABCTestEntity newABCTestEntity = _persistence.create(pk);

		newABCTestEntity.setMvccVersion(RandomTestUtil.nextLong());

		newABCTestEntity.setCompanyId(RandomTestUtil.nextLong());

		newABCTestEntity.setGroupId(RandomTestUtil.nextLong());

		newABCTestEntity.setDefaultLanguageId(RandomTestUtil.randomString());

		_abcTestEntities.add(_persistence.update(newABCTestEntity));

		ABCTestEntity existingABCTestEntity = _persistence.findByPrimaryKey(newABCTestEntity.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntity.getMvccVersion(),
			newABCTestEntity.getMvccVersion());
		Assert.assertEquals(existingABCTestEntity.getAbcTestEntityId(),
			newABCTestEntity.getAbcTestEntityId());
		Assert.assertEquals(existingABCTestEntity.getCompanyId(),
			newABCTestEntity.getCompanyId());
		Assert.assertEquals(existingABCTestEntity.getGroupId(),
			newABCTestEntity.getGroupId());
		Assert.assertEquals(existingABCTestEntity.getDefaultLanguageId(),
			newABCTestEntity.getDefaultLanguageId());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		ABCTestEntity existingABCTestEntity = _persistence.findByPrimaryKey(newABCTestEntity.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntity, newABCTestEntity);
	}

	@Test(expected = NoSuchABCTestEntityException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		String pk = RandomTestUtil.randomString();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<ABCTestEntity> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("ABCTestEntity",
			"mvccVersion", true, "abcTestEntityId", true, "companyId", true,
			"groupId", true, "defaultLanguageId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		ABCTestEntity existingABCTestEntity = _persistence.fetchByPrimaryKey(newABCTestEntity.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntity, newABCTestEntity);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		String pk = RandomTestUtil.randomString();

		ABCTestEntity missingABCTestEntity = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingABCTestEntity);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		ABCTestEntity newABCTestEntity1 = addABCTestEntity();
		ABCTestEntity newABCTestEntity2 = addABCTestEntity();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntity1.getPrimaryKey());
		primaryKeys.add(newABCTestEntity2.getPrimaryKey());

		Map<Serializable, ABCTestEntity> abcTestEntities = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, abcTestEntities.size());
		Assert.assertEquals(newABCTestEntity1,
			abcTestEntities.get(newABCTestEntity1.getPrimaryKey()));
		Assert.assertEquals(newABCTestEntity2,
			abcTestEntities.get(newABCTestEntity2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		String pk1 = RandomTestUtil.randomString();

		String pk2 = RandomTestUtil.randomString();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ABCTestEntity> abcTestEntities = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(abcTestEntities.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		String pk = RandomTestUtil.randomString();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntity.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ABCTestEntity> abcTestEntities = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, abcTestEntities.size());
		Assert.assertEquals(newABCTestEntity,
			abcTestEntities.get(newABCTestEntity.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ABCTestEntity> abcTestEntities = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(abcTestEntities.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntity.getPrimaryKey());

		Map<Serializable, ABCTestEntity> abcTestEntities = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, abcTestEntities.size());
		Assert.assertEquals(newABCTestEntity,
			abcTestEntities.get(newABCTestEntity.getPrimaryKey()));
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntity.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("abcTestEntityId",
				newABCTestEntity.getAbcTestEntityId()));

		List<ABCTestEntity> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		ABCTestEntity existingABCTestEntity = result.get(0);

		Assert.assertEquals(existingABCTestEntity, newABCTestEntity);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntity.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("abcTestEntityId",
				RandomTestUtil.randomString()));

		List<ABCTestEntity> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		ABCTestEntity newABCTestEntity = addABCTestEntity();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntity.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"abcTestEntityId"));

		Object newAbcTestEntityId = newABCTestEntity.getAbcTestEntityId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("abcTestEntityId",
				new Object[] { newAbcTestEntityId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingAbcTestEntityId = result.get(0);

		Assert.assertEquals(existingAbcTestEntityId, newAbcTestEntityId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntity.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"abcTestEntityId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("abcTestEntityId",
				new Object[] { RandomTestUtil.randomString() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected ABCTestEntity addABCTestEntity() throws Exception {
		String pk = RandomTestUtil.randomString();

		ABCTestEntity abcTestEntity = _persistence.create(pk);

		abcTestEntity.setMvccVersion(RandomTestUtil.nextLong());

		abcTestEntity.setCompanyId(RandomTestUtil.nextLong());

		abcTestEntity.setGroupId(RandomTestUtil.nextLong());

		abcTestEntity.setDefaultLanguageId(RandomTestUtil.randomString());

		_abcTestEntities.add(_persistence.update(abcTestEntity));

		return abcTestEntity;
	}

	private List<ABCTestEntity> _abcTestEntities = new ArrayList<ABCTestEntity>();
	private ABCTestEntityPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}