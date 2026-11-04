/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.tools.service.builder.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.function.UnsafeSupplier;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.portal.tools.service.builder.test.model.FinderWhereClauseEntry;
import com.liferay.portal.tools.service.builder.test.model.IndexEntry;
import com.liferay.portal.tools.service.builder.test.service.persistence.FinderWhereClauseEntryPersistence;
import com.liferay.portal.tools.service.builder.test.service.persistence.IndexEntryPersistence;

import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Shuyang Zhou
 */
@RunWith(Arquillian.class)
public class CollectionPersistenceFinderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new TransactionalTestRule(
				Propagation.REQUIRED,
				"com.liferay.portal.tools.service.builder.test.service"));

	@Test
	public void testFindDeepPage() throws Exception {
		long ownerId = RandomTestUtil.nextLong();

		for (int i = 0; i < 12; i++) {
			IndexEntry indexEntry = _indexEntryPersistence.create(
				RandomTestUtil.nextLong());

			indexEntry.setOwnerId(ownerId);
			indexEntry.setPlid(RandomTestUtil.nextLong());

			_indexEntryPersistence.update(indexEntry);
		}

		_assertFindDeepPage(
			_indexEntryPersistence, 4,
			() -> _indexEntryPersistence.findByOwnerId(
				ownerId, 5, 9, null, false));

		Object collectionPersistenceFinder = ReflectionTestUtil.getFieldValue(
			_indexEntryPersistence, "_collectionPersistenceFinderByOwnerId");

		Object keysetQueries = ReflectionTestUtil.getFieldValue(
			collectionPersistenceFinder, "_keysetQueries");

		Assert.assertNotNull(
			ReflectionTestUtil.getFieldValue(
				keysetQueries, "_defaultKeysetQuery"));

		_assertFindDeepPage(
			_indexEntryPersistence, 2,
			() -> _indexEntryPersistence.findByOwnerId(
				ownerId, 10, 15, null, false));
		_assertFindDeepPage(
			_indexEntryPersistence, 0,
			() -> _indexEntryPersistence.findByOwnerId(
				ownerId, 12, 15, null, false));
		_assertFindDeepPage(
			_indexEntryPersistence, 4,
			() -> _indexEntryPersistence.findByOwnerId(
				ownerId, 5, 9,
				OrderByComparatorFactoryUtil.create(
					"IndexEntry", "indexEntryId", false),
				false));
		_assertFindDeepPage(
			_indexEntryPersistence, 4,
			() -> _indexEntryPersistence.findByOwnerId(
				ownerId, 5, 9,
				OrderByComparatorFactoryUtil.create("IndexEntry", "plid", true),
				false));

		String name = RandomTestUtil.randomString();

		for (int i = 0; i < 15; i++) {
			FinderWhereClauseEntry finderWhereClauseEntry =
				_finderWhereClauseEntryPersistence.create(
					RandomTestUtil.nextLong());

			finderWhereClauseEntry.setName(name);

			if ((i % 5) != 0) {
				finderWhereClauseEntry.setNickname(
					RandomTestUtil.randomString());
			}

			_finderWhereClauseEntryPersistence.update(finderWhereClauseEntry);
		}

		_assertFindDeepPage(
			_finderWhereClauseEntryPersistence, 4,
			() -> _finderWhereClauseEntryPersistence.findByName_Nickname(
				name, 5, 9, null, false));
	}

	private <T> void _assertFindDeepPage(
			BasePersistence<?> basePersistence, int expectedSize,
			UnsafeSupplier<List<T>, Exception> unsafeSupplier)
		throws Exception {

		List<T> expectedList = unsafeSupplier.get();

		Assert.assertEquals(
			expectedList.toString(), expectedSize, expectedList.size());

		try (AutoCloseable autoCloseable =
				ReflectionTestUtil.setFieldValueWithAutoCloseable(
					basePersistence, "databaseKeysetPaginationOffsetThreshold",
					5)) {

			Assert.assertEquals(expectedList, unsafeSupplier.get());
		}
	}

	@Inject
	private FinderWhereClauseEntryPersistence
		_finderWhereClauseEntryPersistence;

	@Inject
	private IndexEntryPersistence _indexEntryPersistence;

}