/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cache.internal.dao.orm;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.petra.lang.SafeCloseable;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.cache.TransactionalPortalCache;
import com.liferay.portal.cache.key.HashCodeHexStringCacheKeyGenerator;
import com.liferay.portal.cache.test.util.TestPortalCache;
import com.liferay.portal.cache.test.util.TestPortalCacheListener;
import com.liferay.portal.cache.test.util.TestPortalCacheReplicator;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.cache.transactional.TransactionalPortalCacheUtil;
import com.liferay.portal.kernel.change.tracking.CTCollectionThreadLocal;
import com.liferay.portal.kernel.dao.orm.ArgumentsResolver;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.ModelRemovalThreadLocal;
import com.liferay.portal.kernel.exception.NoSuchModelException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;

/**
 * @author Preston Crary
 * @author Shuyang Zhou
 */
public class FinderCacheImplTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@BeforeClass
	public static void setUpClass() throws Exception {
		_argumentsResolverHolderClass = Class.forName(
			FinderCacheImpl.class.getName() + "$ArgumentsResolverHolder");
		_countKeyClass = Class.forName(
			FinderCacheImpl.class.getName() + "$CountKey");
		_countValueClass = Class.forName(
			FinderCacheImpl.class.getName() + "$CountValue");

		_serializedMultiVMPool = (MultiVMPool)ProxyUtil.newProxyInstance(
			_classLoader, new Class<?>[] {MultiVMPool.class},
			new MultiVMPoolInvocationHandler(_classLoader, true));
		_notSerializedMultiVMPool = (MultiVMPool)ProxyUtil.newProxyInstance(
			_classLoader, new Class<?>[] {MultiVMPool.class},
			new MultiVMPoolInvocationHandler(_classLoader, false));

		CacheKeyGeneratorUtil cacheKeyGeneratorUtil =
			new CacheKeyGeneratorUtil();

		cacheKeyGeneratorUtil.setDefaultCacheKeyGenerator(_cacheKeyGenerator);
	}

	@Before
	public void setUp() {
		PropsUtil.set(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_COUNT_TIME_TO_LIVE,
			String.valueOf(_COUNT_TIME_TO_LIVE));

		ReflectionTestUtil.setFieldValue(
			TransactionalPortalCacheUtil.class, "_transactionalCacheEnabled",
			Boolean.TRUE);

		_finderPath = new FinderPath(
			FinderCacheImplTest.class.getName() + ".List1", "test",
			new String[0], new String[0], true);

		_testPortalCache = new TestPortalCache<>(_COUNT_CACHE_NAME);

		_testPortalCacheListener = new TestPortalCacheListener<>();

		_testPortalCache.registerPortalCacheListener(_testPortalCacheListener);

		_transactionalPortalCache = new TransactionalPortalCache<>(
			_testPortalCache, false);

		_finderCacheImpl = _activateFinderCache(
			_createMultiVMPool(_transactionalPortalCache));

		_countAdjustmentPortalCache = ReflectionTestUtil.getFieldValue(
			_finderCacheImpl, "_countAdjustmentPortalCache");
	}

	@After
	public void tearDown() {
		ThreadLocal<?> portalCacheMapsThreadLocal =
			ReflectionTestUtil.getFieldValue(
				TransactionalPortalCacheUtil.class, "_portalCacheMaps");

		portalCacheMapsThreadLocal.remove();

		ReflectionTestUtil.setFieldValue(
			TransactionalPortalCacheUtil.class, "_transactionalCacheEnabled",
			Boolean.FALSE);
	}

	@Test
	public void testCountAdjustAbsent() {
		FinderPath finderPath = _createCountFinderPath();

		TransactionalPortalCacheUtil.begin();

		try {
			_adjustResult(_finderCacheImpl, finderPath, true);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		_testPortalCacheListener.assertRemoved(
			_encodeCacheKey(_finderCacheImpl, finderPath), null);
		_testPortalCacheListener.assertActionsCount(1);
	}

	@Test
	public void testCountAdjustCommitted() {
		FinderPath finderPath = _createCountFinderPath();

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Serializable cacheKey = _encodeCacheKey(_finderCacheImpl, finderPath);

		Serializable countValue = _testPortalCache.get(cacheKey);

		TransactionalPortalCacheUtil.begin();

		try {
			_adjustResult(_finderCacheImpl, finderPath, true);

			Assert.assertEquals(5, _getCount(countValue));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(6, _getCount(countValue));
		Assert.assertSame(countValue, _testPortalCache.get(cacheKey));

		_testPortalCacheListener.assertPut(cacheKey, countValue);
		_testPortalCacheListener.assertUpdated(
			cacheKey, countValue, _COUNT_TIME_TO_LIVE);
		_testPortalCacheListener.assertActionsCount(2);
	}

	@Test
	public void testCountAdjustPrivate() {
		FinderPath finderPath = _createCountFinderPath();

		TransactionalPortalCacheUtil.begin();

		try {
			_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

			_adjustResult(_finderCacheImpl, finderPath, true);

			Assert.assertNull(
				_testPortalCache.get(
					_encodeCacheKey(_finderCacheImpl, finderPath)));
			Assert.assertEquals(
				Long.valueOf(6),
				_finderCacheImpl.getResult(finderPath, _KEY1, null));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			Long.valueOf(6),
			_finderCacheImpl.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testCountChangeTrackedCollection() {
		MultiVMPool multiVMPool = _createPortalCacheMultiVMPool();

		FinderCacheImpl finderCacheImpl = _activateFinderCache(multiVMPool);

		_setUpCTAwarePortalCache(finderCacheImpl, multiVMPool);

		FinderPath finderPath = _createCountFinderPath();

		finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		try (SafeCloseable safeCloseable =
				CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
					_CT_COLLECTION_ID)) {

			finderCacheImpl.putResult(finderPath, _KEY1, 7L);

			TransactionalPortalCacheUtil.begin();

			try {
				_adjustResult(finderCacheImpl, finderPath, true);
			}
			finally {
				TransactionalPortalCacheUtil.commit(false);
			}

			Assert.assertEquals(
				Long.valueOf(8),
				finderCacheImpl.getResult(finderPath, _KEY1, null));
		}

		Assert.assertEquals(
			Long.valueOf(5),
			finderCacheImpl.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testCountChangeTrackedProduction() {
		MultiVMPool multiVMPool = _createPortalCacheMultiVMPool();

		FinderCacheImpl finderCacheImpl = _activateFinderCache(multiVMPool);

		_setUpCTAwarePortalCache(finderCacheImpl, multiVMPool);

		FinderPath finderPath = _createCountFinderPath();

		finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		try (SafeCloseable safeCloseable =
				CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
					_CT_COLLECTION_ID)) {

			finderCacheImpl.putResult(finderPath, _KEY1, 7L);
		}

		TransactionalPortalCacheUtil.begin();

		try {
			_adjustResult(finderCacheImpl, finderPath, true);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			Long.valueOf(6),
			finderCacheImpl.getResult(finderPath, _KEY1, null));

		try (SafeCloseable safeCloseable =
				CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
					_CT_COLLECTION_ID)) {

			Assert.assertNull(
				finderCacheImpl.getResult(finderPath, _KEY1, null));
		}
	}

	@Test
	public void testCountChangeTrackedProductionInCollection() {
		MultiVMPool multiVMPool = _createPortalCacheMultiVMPool();

		FinderCacheImpl finderCacheImpl = _activateFinderCache(multiVMPool);

		CTAwarePortalCache ctAwarePortalCache = _setUpCTAwarePortalCache(
			finderCacheImpl, multiVMPool);

		FinderPath finderPath = _createCountFinderPath();

		finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		try (SafeCloseable ctCollectionIdSafeCloseable =
				CTCollectionThreadLocal.setCTCollectionIdWithSafeCloseable(
					_CT_COLLECTION_ID)) {

			finderCacheImpl.putResult(finderPath, _KEY1, 7L);

			TransactionalPortalCacheUtil.begin();

			try (SafeCloseable productionModeSafeCloseable =
					CTCollectionThreadLocal.
						setProductionModeWithSafeCloseable()) {

				_adjustResult(finderCacheImpl, finderPath, true);

				PortalCache<Serializable, Serializable> productionPortalCache =
					ctAwarePortalCache.getProductionPortalCache();

				Serializable countValue = productionPortalCache.get(
					_encodeCacheKey(finderCacheImpl, finderPath));

				Assert.assertEquals(5, _getCount(countValue));
			}
			finally {
				TransactionalPortalCacheUtil.commit(false);
			}

			Assert.assertNull(
				finderCacheImpl.getResult(finderPath, _KEY1, null));
		}

		Assert.assertEquals(
			Long.valueOf(6),
			finderCacheImpl.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testCountFlushAbsentKey() {
		_countAdjustmentPortalCache.put(
			_createCountKey(_transactionalPortalCache, _CACHE_KEY), 3L,
			PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertNull(_testPortalCache.get(_CACHE_KEY));

		_testPortalCacheListener.assertActionsCount(0);
	}

	@Test
	public void testCountFlushPresentKey() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		_countAdjustmentPortalCache.put(
			_createCountKey(_transactionalPortalCache, _CACHE_KEY), 3L,
			PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertEquals(8, _getCount(countValue));
		Assert.assertSame(countValue, _testPortalCache.get(_CACHE_KEY));

		_testPortalCacheListener.assertPut(_CACHE_KEY, countValue);
		_testPortalCacheListener.assertUpdated(
			_CACHE_KEY, countValue, _COUNT_TIME_TO_LIVE);
		_testPortalCacheListener.assertActionsCount(2);
	}

	@Test
	public void testCountMissBuffersRemove() {
		FinderPath finderPath = _createCountFinderPath();

		TransactionalPortalCacheUtil.begin();

		try {
			Assert.assertNull(
				_finderCacheImpl.getResult(finderPath, _KEY1, null));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		_testPortalCacheListener.assertRemoved(
			_encodeCacheKey(_finderCacheImpl, finderPath), null);
		_testPortalCacheListener.assertActionsCount(1);
	}

	@Test
	public void testCountPendingCount() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, _CACHE_KEY);

		TransactionalPortalCacheUtil.begin();

		try {
			Assert.assertNull(_getPendingCount(countKey));

			_putPendingCount(countKey, 1);

			Long pendingCount = _getPendingCount(countKey);

			Assert.assertEquals(Long.valueOf(1), pendingCount);

			_putPendingCount(countKey, pendingCount + 1);

			Assert.assertEquals(Long.valueOf(2), _getPendingCount(countKey));
			Assert.assertEquals(5, _getCount(countValue));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(7, _getCount(countValue));

		_testPortalCacheListener.assertPut(_CACHE_KEY, countValue);
		_testPortalCacheListener.assertUpdated(
			_CACHE_KEY, countValue, _COUNT_TIME_TO_LIVE);
		_testPortalCacheListener.assertActionsCount(2);
	}

	@Test
	public void testCountPendingCountCancelled() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, _CACHE_KEY);

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(countKey, 1);

			_putPendingCount(countKey, 0);

			Assert.assertEquals(Long.valueOf(0), _getPendingCount(countKey));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(5, _getCount(countValue));
	}

	@Test
	public void testCountPendingCountNetZero() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, _CACHE_KEY);

		_testPortalCacheListener.reset();

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(countKey, 1);

			_putPendingCount(countKey, _getPendingCount(countKey) - 1);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(5, _getCount(countValue));

		_testPortalCacheListener.assertActionsCount(0);

		_countAdjustmentPortalCache.put(
			countKey, 1L, PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertEquals(6, _getCount(countValue));

		_testPortalCacheListener.assertUpdated(
			_CACHE_KEY, countValue, _COUNT_TIME_TO_LIVE);
		_testPortalCacheListener.assertActionsCount(1);
	}

	@Test
	public void testCountPendingCountRolledBack() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, _CACHE_KEY);

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(countKey, 1);
		}
		finally {
			TransactionalPortalCacheUtil.rollback();
		}

		Assert.assertEquals(5, _getCount(countValue));
	}

	@Test
	public void testCountPendingCountSavepoint() throws Exception {
		Serializable countValue = _createCountValue(5);

		_testPortalCache.put(_CACHE_KEY, countValue);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, _CACHE_KEY);

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(countKey, 1);

			ReflectionTestUtil.invoke(
				TransactionalPortalCacheUtil.class, "_begin",
				new Class<?>[] {boolean.class}, true);

			Long pendingCount = _getPendingCount(countKey);

			Assert.assertEquals(Long.valueOf(1), pendingCount);

			_putPendingCount(countKey, pendingCount + 2);

			TransactionalPortalCacheUtil.commitSavepoint();

			Assert.assertEquals(Long.valueOf(3), _getPendingCount(countKey));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(8, _getCount(countValue));
	}

	@Test
	public void testCountPlusPendingCount() {
		FinderPath finderPath = _createCountFinderPath();

		Assert.assertNull(_finderCacheImpl.getResult(finderPath, _KEY1, null));

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Assert.assertEquals(
			Long.valueOf(5),
			_finderCacheImpl.getResult(finderPath, _KEY1, null));

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(_createCountKey(finderPath), 1);

			Assert.assertEquals(
				Long.valueOf(6),
				_finderCacheImpl.getResult(finderPath, _KEY1, null));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			Long.valueOf(6),
			_finderCacheImpl.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testCountPutResultCancelsPendingCount() {
		FinderPath finderPath = _createCountFinderPath();

		Assert.assertNull(_finderCacheImpl.getResult(finderPath, _KEY1, null));

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		TransactionalPortalCacheUtil.begin();

		try {
			_putPendingCount(_createCountKey(finderPath), 1);

			_finderCacheImpl.putResult(finderPath, _KEY1, 9L);

			Assert.assertEquals(
				Long.valueOf(9),
				_finderCacheImpl.getResult(finderPath, _KEY1, null));
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			Long.valueOf(9),
			_finderCacheImpl.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testCountPutResultWithoutTransaction() {
		FinderPath finderPath = _createCountFinderPath();

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Serializable cacheValue = _testPortalCache.get(
			_encodeCacheKey(_finderCacheImpl, finderPath));

		Assert.assertTrue(
			String.valueOf(cacheValue),
			_countValueClass.isInstance(cacheValue));
	}

	@Test
	public void testCountReadAfterDeleteFlushes() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateRemovalFinderCache();

		FinderPath finderPath = _createCountFinderPath("countByName", "name");

		Object[] arguments = {"away"};

		finderCacheImpl.putResult(finderPath, arguments, 7L);

		TestBaseModel testBaseModel = new TestBaseModel(1L);

		testBaseModel.setColumnValue("name", "home");

		AtomicInteger flushCount = new AtomicInteger();

		TestBasePersistence testBasePersistence = new TestBasePersistence(
			null) {

			@Override
			public void flush() {
				flushCount.incrementAndGet();
			}

		};

		TransactionalPortalCacheUtil.begin();

		try {
			Assert.assertEquals(
				7L,
				finderCacheImpl.getResult(
					finderPath, arguments, testBasePersistence));
			Assert.assertEquals(0, flushCount.get());

			try (SafeCloseable safeCloseable =
					ModelRemovalThreadLocal.
						setRemovingBaseModelWithSafeCloseable(testBaseModel)) {

				finderCacheImpl.removeByEntityCache(_CLASS_NAME, testBaseModel);
			}

			Assert.assertEquals(
				7L,
				finderCacheImpl.getResult(
					finderPath, arguments, testBasePersistence));
			Assert.assertEquals(1, flushCount.get());

			Assert.assertEquals(
				7L,
				finderCacheImpl.getResult(
					finderPath, arguments, testBasePersistence));
			Assert.assertEquals(1, flushCount.get());
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}
	}

	@Test
	public void testCountRemoveByEntityCacheChangedColumn() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateRemovalFinderCache();

		FinderPath nameFinderPath = _createCountFinderPath(
			"countByName", "name");
		FinderPath typeFinderPath = _createCountFinderPath(
			"countByType", "type");

		TestBaseModel testBaseModel = new TestBaseModel(1L);

		testBaseModel.setColumnValue("name", "home");
		testBaseModel.setColumnValue("type", "work");

		Object[] currentArguments = {"away"};
		Object[] originalArguments = {"home"};
		Object[] typeArguments = {"work"};

		finderCacheImpl.putResult(nameFinderPath, originalArguments, 5L);
		finderCacheImpl.putResult(nameFinderPath, currentArguments, 9L);
		finderCacheImpl.putResult(typeFinderPath, typeArguments, 3L);

		testBaseModel.updateColumnValue("name", "away");

		TransactionalPortalCacheUtil.begin();

		try (SafeCloseable safeCloseable =
				ModelRemovalThreadLocal.setRemovingBaseModelWithSafeCloseable(
					testBaseModel)) {

			finderCacheImpl.removeByEntityCache(_CLASS_NAME, testBaseModel);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			4,
			_getCount(
				_getCacheValue(
					finderCacheImpl, nameFinderPath, originalArguments)));
		Assert.assertEquals(
			9,
			_getCount(
				_getCacheValue(
					finderCacheImpl, nameFinderPath, currentArguments)));
		Assert.assertEquals(
			2,
			_getCount(
				_getCacheValue(
					finderCacheImpl, typeFinderPath, typeArguments)));
	}

	@Test
	public void testCountRemoveByEntityCacheCountAll() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateRemovalFinderCache();

		FinderPath finderPath = _createCountFinderPath("countAll");

		Object[] arguments = new Object[0];

		finderCacheImpl.putResult(finderPath, arguments, 5L);

		TestBaseModel testBaseModel = new TestBaseModel(1L);

		TransactionalPortalCacheUtil.begin();

		try (SafeCloseable safeCloseable =
				ModelRemovalThreadLocal.setRemovingBaseModelWithSafeCloseable(
					testBaseModel)) {

			finderCacheImpl.removeByEntityCache(_CLASS_NAME, testBaseModel);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			4,
			_getCount(_getCacheValue(finderCacheImpl, finderPath, arguments)));
	}

	@Test
	public void testCountRemoveByEntityCacheDelete() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateRemovalFinderCache();

		FinderPath countFinderPath = _createCountFinderPath(
			"countByName", "name");
		FinderPath listFinderPath = _createListFinderPath("findByName", "name");

		TestBaseModel testBaseModel = new TestBaseModel(1L);

		testBaseModel.setColumnValue("name", "home");

		Object[] arguments = {"home"};
		Object[] otherArguments = {"away"};

		finderCacheImpl.putResult(countFinderPath, arguments, 5L);
		finderCacheImpl.putResult(countFinderPath, otherArguments, 7L);
		finderCacheImpl.putResult(
			listFinderPath, arguments,
			Collections.singletonList(testBaseModel));
		finderCacheImpl.putResult(
			listFinderPath, otherArguments,
			Collections.singletonList(new TestBaseModel(2L)));

		TransactionalPortalCacheUtil.begin();

		try (SafeCloseable safeCloseable =
				ModelRemovalThreadLocal.setRemovingBaseModelWithSafeCloseable(
					testBaseModel)) {

			finderCacheImpl.removeByEntityCache(_CLASS_NAME, testBaseModel);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertEquals(
			4,
			_getCount(
				_getCacheValue(finderCacheImpl, countFinderPath, arguments)));
		Assert.assertEquals(
			7,
			_getCount(
				_getCacheValue(
					finderCacheImpl, countFinderPath, otherArguments)));
		Assert.assertNull(
			_getCacheValue(finderCacheImpl, listFinderPath, arguments));
		Assert.assertNull(
			_getCacheValue(finderCacheImpl, listFinderPath, otherArguments));
	}

	@Test
	public void testCountRemoveByEntityCacheEviction() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateRemovalFinderCache();

		FinderPath countFinderPath = _createCountFinderPath(
			"countByName", "name");
		FinderPath listFinderPath = _createListFinderPath("findByName", "name");

		TestBaseModel testBaseModel = new TestBaseModel(1L);

		testBaseModel.setColumnValue("name", "home");

		Object[] arguments = {"home"};
		Object[] otherArguments = {"away"};

		finderCacheImpl.putResult(countFinderPath, arguments, 5L);
		finderCacheImpl.putResult(countFinderPath, otherArguments, 7L);
		finderCacheImpl.putResult(
			listFinderPath, arguments,
			Collections.singletonList(testBaseModel));

		TransactionalPortalCacheUtil.begin();

		try {
			finderCacheImpl.removeByEntityCache(_CLASS_NAME, testBaseModel);
		}
		finally {
			TransactionalPortalCacheUtil.commit(false);
		}

		Assert.assertNull(
			_getCacheValue(finderCacheImpl, countFinderPath, arguments));
		Assert.assertEquals(
			7,
			_getCount(
				_getCacheValue(
					finderCacheImpl, countFinderPath, otherArguments)));
		Assert.assertNull(
			_getCacheValue(finderCacheImpl, listFinderPath, arguments));
	}

	@Test
	public void testCountTimeToLiveDisabled() {
		PropsUtil.set(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_COUNT_TIME_TO_LIVE, "0");

		FinderCacheImpl finderCacheImpl = _activateFinderCache(
			_createMultiVMPool(_transactionalPortalCache));

		FinderPath finderPath = _createCountFinderPath();

		finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Serializable cacheKey = _encodeCacheKey(finderCacheImpl, finderPath);

		Serializable countValue = _testPortalCache.get(cacheKey);

		_testPortalCacheListener.reset();

		PortalCache<Serializable, Long> countAdjustmentPortalCache =
			ReflectionTestUtil.getFieldValue(
				finderCacheImpl, "_countAdjustmentPortalCache");

		countAdjustmentPortalCache.put(
			_createCountKey(_transactionalPortalCache, cacheKey), 3L,
			PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertEquals(8, _getCount(countValue));
		Assert.assertSame(countValue, _testPortalCache.get(cacheKey));

		_testPortalCacheListener.assertActionsCount(0);
	}

	@Test
	public void testCountTimeToLiveOnFirstAdjustment() {
		FinderPath finderPath = _createCountFinderPath();

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Serializable cacheKey = _encodeCacheKey(_finderCacheImpl, finderPath);

		Serializable countValue = _testPortalCache.get(cacheKey);

		_testPortalCacheListener.reset();

		TestPortalCacheReplicator<Serializable, Serializable>
			testPortalCacheReplicator = new TestPortalCacheReplicator<>();

		_testPortalCache.registerPortalCacheListener(testPortalCacheReplicator);

		_countAdjustmentPortalCache.put(
			_createCountKey(_transactionalPortalCache, cacheKey), 3L,
			PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertEquals(8, _getCount(countValue));
		Assert.assertSame(countValue, _testPortalCache.get(cacheKey));

		_testPortalCacheListener.assertUpdated(
			cacheKey, countValue, _COUNT_TIME_TO_LIVE);
		_testPortalCacheListener.assertActionsCount(1);

		testPortalCacheReplicator.assertActionsCount(0);

		_testPortalCache.put(cacheKey, countValue);

		testPortalCacheReplicator.assertUpdated(cacheKey, countValue);
		testPortalCacheReplicator.assertActionsCount(1);
	}

	@Test
	public void testCountTimeToLiveOnSecondAdjustment() {
		FinderPath finderPath = _createCountFinderPath();

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		Serializable cacheKey = _encodeCacheKey(_finderCacheImpl, finderPath);

		Serializable countKey = _createCountKey(
			_transactionalPortalCache, cacheKey);
		Serializable countValue = _testPortalCache.get(cacheKey);

		_countAdjustmentPortalCache.put(
			countKey, 3L, PortalCache.DEFAULT_TIME_TO_LIVE);

		_testPortalCacheListener.reset();

		_countAdjustmentPortalCache.put(
			countKey, 3L, PortalCache.DEFAULT_TIME_TO_LIVE);

		Assert.assertEquals(11, _getCount(countValue));

		_testPortalCacheListener.assertActionsCount(0);
	}

	@Test
	public void testCountTimeToLiveWithoutAdjustment() {
		FinderPath finderPath = _createCountFinderPath();

		_finderCacheImpl.putResult(finderPath, _KEY1, 5L);

		_testPortalCacheListener.reset();

		Assert.assertEquals(
			Long.valueOf(5),
			_finderCacheImpl.getResult(finderPath, _KEY1, null));

		_testPortalCacheListener.assertActionsCount(0);
	}

	@Test
	public void testModelRemovalMatch() {
		TestBaseModel testBaseModel = new TestBaseModel(1L);

		Assert.assertFalse(ModelRemovalThreadLocal.isRemoving(testBaseModel));

		try (SafeCloseable safeCloseable =
				ModelRemovalThreadLocal.setRemovingBaseModelWithSafeCloseable(
					testBaseModel)) {

			Assert.assertTrue(
				ModelRemovalThreadLocal.isRemoving(new TestBaseModel(1L)));
			Assert.assertFalse(
				ModelRemovalThreadLocal.isRemoving(new TestBaseModel(2L)));
			Assert.assertFalse(
				ModelRemovalThreadLocal.isRemoving(
					new TestBaseModel(FinderCacheImplTest.class, 1L)));
		}

		Assert.assertFalse(ModelRemovalThreadLocal.isRemoving(testBaseModel));
	}

	@Test
	public void testModelRemovalScope() {
		TestBaseModel testBaseModel = new TestBaseModel(1L);

		TestBasePersistence testBasePersistence = new TestBasePersistence(
			Collections.emptyMap());

		TestBaseModel removedTestBaseModel =
			testBasePersistence.removeByFunction(
				testBaseModel,
				model -> {
					Assert.assertTrue(
						ModelRemovalThreadLocal.isRemoving(model));

					return model;
				});

		Assert.assertSame(testBaseModel, removedTestBaseModel);

		Assert.assertFalse(ModelRemovalThreadLocal.isRemoving(testBaseModel));
	}

	@Test
	public void testNotifyPortalCacheRemovedPortalCacheName() {
		FinderCacheImpl finderCacheImpl = _activateFinderCache(
			(MultiVMPool)ProxyUtil.newProxyInstance(
				_classLoader, new Class<?>[] {MultiVMPool.class},
				new MultiVMPoolInvocationHandler(_classLoader, true)));

		PortalCache<Serializable, Serializable> portalCache =
			ReflectionTestUtil.invoke(
				finderCacheImpl, "_getPortalCache",
				new Class<?>[] {String.class},
				FinderCacheImplTest.class.getName());

		Map<String, PortalCache<Serializable, Serializable>> portalCaches =
			ReflectionTestUtil.getFieldValue(finderCacheImpl, "_portalCaches");

		Assert.assertEquals(portalCaches.toString(), 1, portalCaches.size());
		Assert.assertSame(
			portalCache, portalCaches.get(FinderCacheImplTest.class.getName()));

		finderCacheImpl.notifyPortalCacheRemoved(
			portalCache.getPortalCacheName());

		Assert.assertTrue(portalCaches.toString(), portalCaches.isEmpty());
	}

	@Test
	public void testPutEmptyListInvalid() {
		_assertPutEmptyListInvalid(_notSerializedMultiVMPool);
		_assertPutEmptyListInvalid(_serializedMultiVMPool);
	}

	@Test
	public void testPutEmptyListValid() {
		_assertPutEmptyListValid(_notSerializedMultiVMPool);
		_assertPutEmptyListValid(_serializedMultiVMPool);
	}

	@Test
	public void testPutNonbaseModelList() {
		FinderPath finderPath = new FinderPath(
			FinderCacheImplTest.class.getName() + ".List1",
			"test-nonbase-model", new String[0], new String[0], false);

		FinderCache finderCache = _activateFinderCache(
			_notSerializedMultiVMPool);

		// Empty list

		finderCache.putResult(finderPath, _KEY1, Collections.emptyList());

		Assert.assertSame(
			Collections.emptyList(),
			finderCache.getResult(finderPath, _KEY1, null));

		// Not empty list

		List<Long> list = Collections.singletonList(1L);

		finderCache.putResult(finderPath, _KEY1, list);

		Assert.assertSame(list, finderCache.getResult(finderPath, _KEY1, null));
	}

	@Test
	public void testTestKeysCollide() {
		Assert.assertEquals(
			_cacheKeyGenerator.getCacheKey(_KEY1),
			_cacheKeyGenerator.getCacheKey(_KEY2));
	}

	@Test
	public void testThreshold() {
		PropsUtil.set(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD, "2");

		FinderCache finderCache = _activateFinderCache(
			_notSerializedMultiVMPool);

		Map<Serializable, TestBaseModel> map =
			HashMapBuilder.<Serializable, TestBaseModel>put(
				"a", new TestBaseModel("a")
			).put(
				"b", new TestBaseModel("b")
			).build();

		List<TestBaseModel> values = new ArrayList<>(map.values());

		finderCache.putResult(_finderPath, _KEY1, values);

		Assert.assertEquals(
			values,
			finderCache.getResult(
				_finderPath, _KEY1, new TestBasePersistence(map)));

		map.put("c", new TestBaseModel("c"));

		finderCache.putResult(
			_finderPath, _KEY1, new ArrayList<>(map.values()));

		Assert.assertNull(
			finderCache.getResult(
				_finderPath, _KEY1, new TestBasePersistence(map)));
	}

	private FinderCacheImpl _activateFinderCache(MultiVMPool multiVMPool) {
		return _activateFinderCache(multiVMPool, null);
	}

	private FinderCacheImpl _activateFinderCache(
		MultiVMPool multiVMPool, ArgumentsResolver argumentsResolver) {

		FinderCacheImpl finderCacheImpl = new FinderCacheImpl();

		ReflectionTestUtil.setFieldValue(
			finderCacheImpl, "_multiVMPool", multiVMPool);

		finderCacheImpl.activate(
			(BundleContext)ProxyUtil.newProxyInstance(
				BundleContext.class.getClassLoader(),
				new Class<?>[] {BundleContext.class},
				(proxy, method, args) -> {
					String methodName = method.getName();

					if (Objects.equals(methodName, "createFilter")) {
						return ProxyFactory.newDummyInstance(Filter.class);
					}

					if (Objects.equals(methodName, "getService")) {
						return argumentsResolver;
					}

					return null;
				}));

		return finderCacheImpl;
	}

	private FinderCacheImpl _activateRemovalFinderCache() throws Exception {
		FinderCacheImpl finderCacheImpl = _activateFinderCache(
			_createPortalCacheMultiVMPool(), new TestArgumentsResolver());

		Constructor<?> constructor =
			_argumentsResolverHolderClass.getDeclaredConstructor(
				FinderCacheImpl.class, ServiceReference.class);

		constructor.setAccessible(true);

		Object argumentsResolverHolder = constructor.newInstance(
			finderCacheImpl,
			ProxyUtil.newProxyInstance(
				_classLoader, new Class<?>[] {ServiceReference.class},
				(proxy, method, args) -> {
					if (Objects.equals(method.getName(), "getProperty")) {
						return _TABLE_NAME;
					}

					return null;
				}));

		ReflectionTestUtil.setFieldValue(
			finderCacheImpl, "_serviceTrackerMap",
			ProxyUtil.newProxyInstance(
				_classLoader, new Class<?>[] {ServiceTrackerMap.class},
				(proxy, method, args) -> {
					if (Objects.equals(method.getName(), "getService")) {
						return argumentsResolverHolder;
					}

					return null;
				}));

		return finderCacheImpl;
	}

	private void _adjustResult(
		FinderCacheImpl finderCacheImpl, FinderPath finderPath,
		boolean increment) {

		ReflectionTestUtil.invoke(
			finderCacheImpl, "_adjustResult",
			new Class<?>[] {FinderPath.class, Object[].class, boolean.class},
			finderPath, _KEY1, increment);
	}

	private void _assertPutEmptyListInvalid(MultiVMPool multiVMPool) {
		FinderCache finderCache = _activateFinderCache(multiVMPool);

		finderCache.putResult(_finderPath, _KEY1, Collections.emptyList());

		Assert.assertNull(finderCache.getResult(_finderPath, _KEY2, null));
	}

	private void _assertPutEmptyListValid(MultiVMPool multiVMPool) {
		FinderCache finderCache = _activateFinderCache(multiVMPool);

		finderCache.putResult(_finderPath, _KEY1, Collections.emptyList());

		Assert.assertSame(
			Collections.emptyList(),
			finderCache.getResult(_finderPath, _KEY1, null));
	}

	private FinderPath _createCountFinderPath() {
		return _createCountFinderPath("count");
	}

	private FinderPath _createCountFinderPath(
		String methodName, String... columnNames) {

		return new FinderPath(
			_COUNT_CACHE_NAME, methodName, new String[0], columnNames, false);
	}

	private Serializable _createCountKey(FinderPath finderPath) {
		return _createCountKey(
			_transactionalPortalCache,
			_encodeCacheKey(_finderCacheImpl, finderPath));
	}

	private Serializable _createCountKey(
		PortalCache<Serializable, Serializable> portalCache,
		Serializable cacheKey) {

		TransactionalPortalCacheUtil.begin();

		try {
			return ReflectionTestUtil.invoke(
				_countKeyClass, "create",
				new Class<?>[] {PortalCache.class, Serializable.class},
				portalCache, cacheKey);
		}
		finally {
			TransactionalPortalCacheUtil.rollback();
		}
	}

	private Serializable _createCountValue(long count) throws Exception {
		Constructor<?> constructor = _countValueClass.getDeclaredConstructor(
			long.class);

		constructor.setAccessible(true);

		return (Serializable)constructor.newInstance(count);
	}

	private FinderPath _createListFinderPath(
		String methodName, String... columnNames) {

		return new FinderPath(
			_COUNT_CACHE_NAME, methodName, new String[0], columnNames, true);
	}

	private MultiVMPool _createMultiVMPool(
		Function<String, PortalCache<Serializable, Serializable>>
			portalCacheFunction) {

		MultiVMPoolInvocationHandler multiVMPoolInvocationHandler =
			new MultiVMPoolInvocationHandler(_classLoader, false);

		return (MultiVMPool)ProxyUtil.newProxyInstance(
			_classLoader, new Class<?>[] {MultiVMPool.class},
			(proxy, method, args) -> {
				String methodName = method.getName();

				if (methodName.equals("getPortalCache")) {
					return portalCacheFunction.apply((String)args[0]);
				}

				return multiVMPoolInvocationHandler.invoke(proxy, method, args);
			});
	}

	private MultiVMPool _createMultiVMPool(
		PortalCache<Serializable, Serializable> portalCache) {

		return _createMultiVMPool(cacheName -> portalCache);
	}

	private MultiVMPool _createPortalCacheMultiVMPool() {
		Map<String, PortalCache<Serializable, Serializable>> portalCaches =
			new HashMap<>();

		return _createMultiVMPool(
			cacheName -> portalCaches.computeIfAbsent(
				cacheName,
				portalCacheName -> new TransactionalPortalCache<>(
					new TestPortalCache<>(portalCacheName), false)));
	}

	private Serializable _encodeCacheKey(
		FinderCacheImpl finderCacheImpl, FinderPath finderPath) {

		return _encodeCacheKey(finderCacheImpl, finderPath, _KEY1);
	}

	private Serializable _encodeCacheKey(
		FinderCacheImpl finderCacheImpl, FinderPath finderPath,
		Object[] arguments) {

		return ReflectionTestUtil.invoke(
			finderCacheImpl, "_encodeCacheKey",
			new Class<?>[] {FinderPath.class, Object[].class}, finderPath,
			arguments);
	}

	private Serializable _getCacheValue(
		FinderCacheImpl finderCacheImpl, FinderPath finderPath,
		Object[] arguments) {

		PortalCache<Serializable, Serializable> portalCache =
			ReflectionTestUtil.invoke(
				finderCacheImpl, "_getPortalCache",
				new Class<?>[] {String.class}, finderPath.getCacheName());

		return portalCache.get(
			_encodeCacheKey(finderCacheImpl, finderPath, arguments));
	}

	private long _getCount(Serializable countValue) {
		return ReflectionTestUtil.invoke(countValue, "get", new Class<?>[0]);
	}

	private Long _getPendingCount(Serializable countKey) {
		return TransactionalPortalCacheUtil.get(
			_countAdjustmentPortalCache, countKey);
	}

	private void _putPendingCount(Serializable countKey, long pendingCount) {
		TransactionalPortalCacheUtil.put(
			_countAdjustmentPortalCache, countKey, pendingCount,
			PortalCache.DEFAULT_TIME_TO_LIVE, true);
	}

	private CTAwarePortalCache _setUpCTAwarePortalCache(
		FinderCacheImpl finderCacheImpl, MultiVMPool multiVMPool) {

		CTAwarePortalCache ctAwarePortalCache = new CTAwarePortalCache(
			multiVMPool,
			FinderCache.class.getName() + StringPool.PERIOD + _CLASS_NAME,
			false, false);

		Map<String, PortalCache<Serializable, Serializable>> portalCaches =
			ReflectionTestUtil.getFieldValue(finderCacheImpl, "_portalCaches");

		portalCaches.put(_CLASS_NAME, ctAwarePortalCache);

		return ctAwarePortalCache;
	}

	private static final String _CACHE_KEY = "Test Cache Key";

	private static final String _CLASS_NAME =
		FinderCacheImplTest.class.getName();

	private static final String _COUNT_CACHE_NAME = _CLASS_NAME + ".List2";

	private static final int _COUNT_TIME_TO_LIVE = 600;

	private static final long _CT_COLLECTION_ID = 1;

	private static final String[] _KEY1 = {"home"};

	private static final String[] _KEY2 = {"j1me"};

	private static final String _TABLE_NAME = "TestBaseModel";

	private static Class<?> _argumentsResolverHolderClass;
	private static final CacheKeyGenerator _cacheKeyGenerator =
		new HashCodeHexStringCacheKeyGenerator();
	private static final ClassLoader _classLoader =
		FinderCacheImplTest.class.getClassLoader();
	private static Class<?> _countKeyClass;
	private static Class<?> _countValueClass;
	private static MultiVMPool _notSerializedMultiVMPool;
	private static MultiVMPool _serializedMultiVMPool;

	private PortalCache<Serializable, Long> _countAdjustmentPortalCache;
	private FinderCacheImpl _finderCacheImpl;
	private FinderPath _finderPath;
	private TestPortalCache<Serializable, Serializable> _testPortalCache;
	private TestPortalCacheListener<Serializable, Serializable>
		_testPortalCacheListener;
	private TransactionalPortalCache<Serializable, Serializable>
		_transactionalPortalCache;

	private static class TestArgumentsResolver implements ArgumentsResolver {

		@Override
		public Object[] getArguments(
			FinderPath finderPath, BaseModel<?> baseModel, boolean checkColumn,
			boolean original) {

			String[] columnNames = finderPath.getColumnNames();

			if (columnNames.length == 0) {
				if (baseModel.isNew()) {
					return new Object[0];
				}

				return null;
			}

			TestBaseModel testBaseModel = (TestBaseModel)baseModel;

			if (checkColumn && testBaseModel.isChanged() &&
				!_isChanged(testBaseModel, columnNames)) {

				return null;
			}

			Object[] arguments = new Object[columnNames.length];

			for (int i = 0; i < columnNames.length; i++) {
				arguments[i] = testBaseModel.getColumnValue(
					columnNames[i], original);
			}

			return arguments;
		}

		@Override
		public String getClassName() {
			return _CLASS_NAME;
		}

		@Override
		public String getTableName() {
			return _TABLE_NAME;
		}

		private boolean _isChanged(
			TestBaseModel testBaseModel, String[] columnNames) {

			for (String columnName : columnNames) {
				if (testBaseModel.isColumnChanged(columnName)) {
					return true;
				}
			}

			return false;
		}

	}

	private static class TestBaseModel extends BaseModelImpl<TestBaseModel> {

		@Override
		public Object clone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TestBaseModel cloneWithOriginalValues() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(TestBaseModel testBaseModel) {
			throw new UnsupportedOperationException();
		}

		public Object getColumnValue(String columnName, boolean original) {
			if (original) {
				return _originalColumnValues.get(columnName);
			}

			return _columnValues.get(columnName);
		}

		@Override
		public Class<?> getModelClass() {
			return _modelClass;
		}

		@Override
		public String getModelClassName() {
			return StringPool.BLANK;
		}

		@Override
		public Serializable getPrimaryKeyObj() {
			return _primaryKey;
		}

		public boolean isChanged() {
			for (String columnName : _columnValues.keySet()) {
				if (isColumnChanged(columnName)) {
					return true;
				}
			}

			return false;
		}

		public boolean isColumnChanged(String columnName) {
			return !Objects.equals(
				_columnValues.get(columnName),
				_originalColumnValues.get(columnName));
		}

		@Override
		public boolean isEntityCacheEnabled() {
			return true;
		}

		@Override
		public boolean isFinderCacheEnabled() {
			return true;
		}

		public void setColumnValue(String columnName, Object value) {
			_columnValues.put(columnName, value);
			_originalColumnValues.put(columnName, value);
		}

		@Override
		public void setPrimaryKeyObj(Serializable primaryKeyObj) {
			throw new UnsupportedOperationException();
		}

		public void updateColumnValue(String columnName, Object value) {
			_columnValues.put(columnName, value);
		}

		private TestBaseModel(Class<?> modelClass, Serializable primaryKey) {
			_modelClass = modelClass;
			_primaryKey = primaryKey;
		}

		private TestBaseModel(Serializable primaryKey) {
			this(TestBaseModel.class, primaryKey);
		}

		private final Map<String, Object> _columnValues = new HashMap<>();
		private final Class<?> _modelClass;
		private final Map<String, Object> _originalColumnValues =
			new HashMap<>();
		private final Serializable _primaryKey;

	}

	private static class TestBasePersistence
		extends BasePersistenceImpl<TestBaseModel, NoSuchModelException> {

		@Override
		public TestBaseModel fetchByPrimaryKey(Serializable serializable) {
			return _map.get(serializable);
		}

		@Override
		public Map<Serializable, TestBaseModel> fetchByPrimaryKeys(
			Set<Serializable> primaryKeys) {

			Assert.assertNotNull(_map);
			Assert.assertEquals(_map.keySet(), primaryKeys);

			return _map;
		}

		@Override
		@SuppressWarnings("unchecked")
		public ModelListener<TestBaseModel>[] getListeners() {
			return new ModelListener[0];
		}

		private TestBasePersistence(Map<Serializable, TestBaseModel> map) {
			_map = map;
		}

		private final Map<Serializable, TestBaseModel> _map;

	}

}