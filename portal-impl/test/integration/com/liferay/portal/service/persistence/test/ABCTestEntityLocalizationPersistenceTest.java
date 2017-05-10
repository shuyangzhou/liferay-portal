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
import com.liferay.portal.kernel.exception.NoSuchABCTestEntityLocalizationException;
import com.liferay.portal.kernel.model.ABCTestEntityLocalization;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityLocalizationPersistence;
import com.liferay.portal.kernel.service.persistence.ABCTestEntityLocalizationUtil;
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
public class ABCTestEntityLocalizationPersistenceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = ABCTestEntityLocalizationUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<ABCTestEntityLocalization> iterator = _abcTestEntityLocalizations.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ABCTestEntityLocalization abcTestEntityLocalization = _persistence.create(pk);

		Assert.assertNotNull(abcTestEntityLocalization);

		Assert.assertEquals(abcTestEntityLocalization.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		_persistence.remove(newABCTestEntityLocalization);

		ABCTestEntityLocalization existingABCTestEntityLocalization = _persistence.fetchByPrimaryKey(newABCTestEntityLocalization.getPrimaryKey());

		Assert.assertNull(existingABCTestEntityLocalization);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addABCTestEntityLocalization();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ABCTestEntityLocalization newABCTestEntityLocalization = _persistence.create(pk);

		newABCTestEntityLocalization.setMvccVersion(RandomTestUtil.nextLong());

		newABCTestEntityLocalization.setCompanyId(RandomTestUtil.nextLong());

		newABCTestEntityLocalization.setAbcTestEntityId(RandomTestUtil.randomString());

		newABCTestEntityLocalization.setLanguageId(RandomTestUtil.randomString());

		newABCTestEntityLocalization.setName(RandomTestUtil.randomString());

		newABCTestEntityLocalization.setDescription(RandomTestUtil.randomString());

		newABCTestEntityLocalization.setGroupId(RandomTestUtil.nextLong());

		_abcTestEntityLocalizations.add(_persistence.update(
				newABCTestEntityLocalization));

		ABCTestEntityLocalization existingABCTestEntityLocalization = _persistence.findByPrimaryKey(newABCTestEntityLocalization.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntityLocalization.getMvccVersion(),
			newABCTestEntityLocalization.getMvccVersion());
		Assert.assertEquals(existingABCTestEntityLocalization.getAbcTestEntityLocalizationId(),
			newABCTestEntityLocalization.getAbcTestEntityLocalizationId());
		Assert.assertEquals(existingABCTestEntityLocalization.getCompanyId(),
			newABCTestEntityLocalization.getCompanyId());
		Assert.assertEquals(existingABCTestEntityLocalization.getAbcTestEntityId(),
			newABCTestEntityLocalization.getAbcTestEntityId());
		Assert.assertEquals(existingABCTestEntityLocalization.getLanguageId(),
			newABCTestEntityLocalization.getLanguageId());
		Assert.assertEquals(existingABCTestEntityLocalization.getName(),
			newABCTestEntityLocalization.getName());
		Assert.assertEquals(existingABCTestEntityLocalization.getDescription(),
			newABCTestEntityLocalization.getDescription());
		Assert.assertEquals(existingABCTestEntityLocalization.getGroupId(),
			newABCTestEntityLocalization.getGroupId());
	}

	@Test
	public void testCountByAbcTestEntityId() throws Exception {
		_persistence.countByAbcTestEntityId(StringPool.BLANK);

		_persistence.countByAbcTestEntityId(StringPool.NULL);

		_persistence.countByAbcTestEntityId((String)null);
	}

	@Test
	public void testCountByAbcTestEntityId_LanguageId()
		throws Exception {
		_persistence.countByAbcTestEntityId_LanguageId(StringPool.BLANK,
			StringPool.BLANK);

		_persistence.countByAbcTestEntityId_LanguageId(StringPool.NULL,
			StringPool.NULL);

		_persistence.countByAbcTestEntityId_LanguageId((String)null,
			(String)null);
	}

	@Test
	public void testCountByGroupId() throws Exception {
		_persistence.countByGroupId(RandomTestUtil.nextLong());

		_persistence.countByGroupId(0L);
	}

	@Test
	public void testCountByG_N() throws Exception {
		_persistence.countByG_N(RandomTestUtil.nextLong(), StringPool.BLANK);

		_persistence.countByG_N(0L, StringPool.NULL);

		_persistence.countByG_N(0L, (String)null);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		ABCTestEntityLocalization existingABCTestEntityLocalization = _persistence.findByPrimaryKey(newABCTestEntityLocalization.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntityLocalization,
			newABCTestEntityLocalization);
	}

	@Test(expected = NoSuchABCTestEntityLocalizationException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<ABCTestEntityLocalization> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("ABCTestEntityLocalization",
			"mvccVersion", true, "abcTestEntityLocalizationId", true,
			"companyId", true, "abcTestEntityId", true, "languageId", true,
			"name", true, "description", true, "groupId", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		ABCTestEntityLocalization existingABCTestEntityLocalization = _persistence.fetchByPrimaryKey(newABCTestEntityLocalization.getPrimaryKey());

		Assert.assertEquals(existingABCTestEntityLocalization,
			newABCTestEntityLocalization);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		ABCTestEntityLocalization missingABCTestEntityLocalization = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingABCTestEntityLocalization);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization1 = addABCTestEntityLocalization();
		ABCTestEntityLocalization newABCTestEntityLocalization2 = addABCTestEntityLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntityLocalization1.getPrimaryKey());
		primaryKeys.add(newABCTestEntityLocalization2.getPrimaryKey());

		Map<Serializable, ABCTestEntityLocalization> abcTestEntityLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, abcTestEntityLocalizations.size());
		Assert.assertEquals(newABCTestEntityLocalization1,
			abcTestEntityLocalizations.get(
				newABCTestEntityLocalization1.getPrimaryKey()));
		Assert.assertEquals(newABCTestEntityLocalization2,
			abcTestEntityLocalizations.get(
				newABCTestEntityLocalization2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, ABCTestEntityLocalization> abcTestEntityLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(abcTestEntityLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntityLocalization.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, ABCTestEntityLocalization> abcTestEntityLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, abcTestEntityLocalizations.size());
		Assert.assertEquals(newABCTestEntityLocalization,
			abcTestEntityLocalizations.get(
				newABCTestEntityLocalization.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, ABCTestEntityLocalization> abcTestEntityLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(abcTestEntityLocalizations.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newABCTestEntityLocalization.getPrimaryKey());

		Map<Serializable, ABCTestEntityLocalization> abcTestEntityLocalizations = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, abcTestEntityLocalizations.size());
		Assert.assertEquals(newABCTestEntityLocalization,
			abcTestEntityLocalizations.get(
				newABCTestEntityLocalization.getPrimaryKey()));
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntityLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"abcTestEntityLocalizationId",
				newABCTestEntityLocalization.getAbcTestEntityLocalizationId()));

		List<ABCTestEntityLocalization> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		ABCTestEntityLocalization existingABCTestEntityLocalization = result.get(0);

		Assert.assertEquals(existingABCTestEntityLocalization,
			newABCTestEntityLocalization);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntityLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq(
				"abcTestEntityLocalizationId", RandomTestUtil.nextLong()));

		List<ABCTestEntityLocalization> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntityLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"abcTestEntityLocalizationId"));

		Object newAbcTestEntityLocalizationId = newABCTestEntityLocalization.getAbcTestEntityLocalizationId();

		dynamicQuery.add(RestrictionsFactoryUtil.in(
				"abcTestEntityLocalizationId",
				new Object[] { newAbcTestEntityLocalizationId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingAbcTestEntityLocalizationId = result.get(0);

		Assert.assertEquals(existingAbcTestEntityLocalizationId,
			newAbcTestEntityLocalizationId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(ABCTestEntityLocalization.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"abcTestEntityLocalizationId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in(
				"abcTestEntityLocalizationId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		ABCTestEntityLocalization newABCTestEntityLocalization = addABCTestEntityLocalization();

		_persistence.clearCache();

		ABCTestEntityLocalization existingABCTestEntityLocalization = _persistence.findByPrimaryKey(newABCTestEntityLocalization.getPrimaryKey());

		Assert.assertTrue(Objects.equals(
				existingABCTestEntityLocalization.getAbcTestEntityId(),
				ReflectionTestUtil.invoke(existingABCTestEntityLocalization,
					"getOriginalAbcTestEntityId", new Class<?>[0])));
		Assert.assertTrue(Objects.equals(
				existingABCTestEntityLocalization.getLanguageId(),
				ReflectionTestUtil.invoke(existingABCTestEntityLocalization,
					"getOriginalLanguageId", new Class<?>[0])));

		Assert.assertEquals(Long.valueOf(
				existingABCTestEntityLocalization.getGroupId()),
			ReflectionTestUtil.<Long>invoke(existingABCTestEntityLocalization,
				"getOriginalGroupId", new Class<?>[0]));
		Assert.assertTrue(Objects.equals(
				existingABCTestEntityLocalization.getName(),
				ReflectionTestUtil.invoke(existingABCTestEntityLocalization,
					"getOriginalName", new Class<?>[0])));
	}

	protected ABCTestEntityLocalization addABCTestEntityLocalization()
		throws Exception {
		long pk = RandomTestUtil.nextLong();

		ABCTestEntityLocalization abcTestEntityLocalization = _persistence.create(pk);

		abcTestEntityLocalization.setMvccVersion(RandomTestUtil.nextLong());

		abcTestEntityLocalization.setCompanyId(RandomTestUtil.nextLong());

		abcTestEntityLocalization.setAbcTestEntityId(RandomTestUtil.randomString());

		abcTestEntityLocalization.setLanguageId(RandomTestUtil.randomString());

		abcTestEntityLocalization.setName(RandomTestUtil.randomString());

		abcTestEntityLocalization.setDescription(RandomTestUtil.randomString());

		abcTestEntityLocalization.setGroupId(RandomTestUtil.nextLong());

		_abcTestEntityLocalizations.add(_persistence.update(
				abcTestEntityLocalization));

		return abcTestEntityLocalization;
	}

	private List<ABCTestEntityLocalization> _abcTestEntityLocalizations = new ArrayList<ABCTestEntityLocalization>();
	private ABCTestEntityLocalizationPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}