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

package com.liferay.portal.cache.internal.dao.orm;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.cache.internal.dao.orm.cache.BaseFinderCacheResult;
import com.liferay.portal.cache.internal.dao.orm.cache.FinderCacheListResultImpl;
import com.liferay.portal.cache.internal.dao.orm.cache.FinderCacheModelResultImpl;
import com.liferay.portal.cache.key.HashCodeHexStringCacheKeyGenerator;
import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.cache.FinderCacheListResult;
import com.liferay.portal.kernel.dao.orm.cache.FinderCacheModelResult;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.servlet.filters.threadlocal.ThreadLocalFilterThreadLocal;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.RegistryUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class FinderCacheImplTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor() {

			@Override
			public void appendAssertClasses(List<Class<?>> assertClasses) {
				assertClasses.clear();

				assertClasses.add(BaseFinderCacheResult.class);
				assertClasses.add(FinderCacheListResultImpl.class);
				assertClasses.add(FinderCacheModelResultImpl.class);
			}

		};

	@BeforeClass
	public static void setUpClass() {
		_serializedMultiVMPool = (MultiVMPool)ProxyUtil.newProxyInstance(
			_classLoader, new Class<?>[] {MultiVMPool.class},
			new MultiVMPoolInvocationHandler(_classLoader, true));
		_notSerializedMultiVMPool = (MultiVMPool)ProxyUtil.newProxyInstance(
			_classLoader, new Class<?>[] {MultiVMPool.class},
			new MultiVMPoolInvocationHandler(_classLoader, false));

		RegistryUtil.setRegistry(new BasicRegistryImpl());

		CacheKeyGeneratorUtil cacheKeyGeneratorUtil =
			new CacheKeyGeneratorUtil();

		cacheKeyGeneratorUtil.setDefaultCacheKeyGenerator(_cacheKeyGenerator);
	}

	@Before
	public void setUp() {
		_properties = HashMapBuilder.<String, Object>put(
			PropsKeys.VALUE_OBJECT_ENTITY_BLOCKING_CACHE, "true"
		).put(
			PropsKeys.VALUE_OBJECT_ENTITY_CACHE_ENABLED, "true"
		).put(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_ENABLED, "true"
		).put(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD, "-1"
		).put(
			PropsKeys.VALUE_OBJECT_MVCC_ENTITY_CACHE_ENABLED, "true"
		).build();

		_finderPath = new FinderPath(
			true, true, FinderCacheImplTest.class,
			FinderCacheImplTest.class.getName(), "test",
			new String[] {String.class.getName()});

		ThreadLocalFilterThreadLocal.setFilterInvoked();
	}

	@After
	public void tearDown() {
		CentralizedThreadLocal.clearShortLivedThreadLocals();
	}

	@Test
	public void testGetResult() {
		_properties.put(
			PropsKeys.VALUE_OBJECT_FINDER_THREAD_LOCAL_CACHE_MAX_SIZE, "100");

		FinderCache finderCache = _activateFinderCache(
			_notSerializedMultiVMPool);

		TestBaseModel testBaseModel = new TestBaseModel("a");

		Map<Serializable, TestBaseModel> map =
			HashMapBuilder.<Serializable, TestBaseModel>put(
				testBaseModel.getPrimaryKeyObj(), testBaseModel
			).build();

		TestBasePersistence testBasePersistence = new TestBasePersistence(map);

		FinderCacheModelResult<TestBaseModel> finderCacheListResult =
			finderCache.getResult(_finderPath, _KEY1);

		Assert.assertFalse(finderCacheListResult.isCached());

		finderCacheListResult.setResult(testBaseModel);

		Assert.assertTrue(finderCacheListResult.isCached());
		Assert.assertTrue(finderCacheListResult.exists());
		Assert.assertSame(
			testBaseModel,
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResult(_finderPath, _KEY1);

		Assert.assertTrue(finderCacheListResult.isCached());
		Assert.assertTrue(finderCacheListResult.exists());
		Assert.assertSame(
			testBaseModel,
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.setResult(null);

		Assert.assertTrue(finderCacheListResult.isCached());
		Assert.assertFalse(finderCacheListResult.exists());
		Assert.assertNull(finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.clear();

		Assert.assertFalse(finderCacheListResult.isCached());

		finderCacheListResult = finderCache.getResult(_finderPath, _KEY1);

		Assert.assertFalse(finderCacheListResult.isCached());
	}

	@Test
	public void testGetResults() {
		_properties.put(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD, "2");

		FinderCache finderCache = _activateFinderCache(
			_notSerializedMultiVMPool);

		TestBaseModel testBaseModel1 = new TestBaseModel("a");
		TestBaseModel testBaseModel2 = new TestBaseModel("b");
		TestBaseModel testBaseModel3 = new TestBaseModel("c");

		Map<Serializable, TestBaseModel> map =
			HashMapBuilder.<Serializable, TestBaseModel>put(
				testBaseModel1.getPrimaryKeyObj(), testBaseModel1
			).put(
				testBaseModel2.getPrimaryKeyObj(), testBaseModel2
			).put(
				testBaseModel3.getPrimaryKeyObj(), testBaseModel3
			).build();

		TestBasePersistence testBasePersistence = new TestBasePersistence(map);

		FinderCacheListResult<TestBaseModel> finderCacheListResult =
			finderCache.getResults(_finderPath, _KEY1);

		List<TestBaseModel> result = new ArrayList<>(3);

		result.add(testBaseModel1);
		result.add(testBaseModel2);

		finderCacheListResult.setResult(result);

		Assert.assertEquals(
			result, finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertEquals(
			result, finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.setResult(Collections.emptyList());

		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.clear();

		Assert.assertNull(finderCacheListResult.getCount());

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		finderCacheListResult.setResult(result);

		finderCacheListResult.setCount(0);

		Assert.assertEquals((Long)0L, finderCacheListResult.getCount());
		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertEquals((Long)0L, finderCacheListResult.getCount());
		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.setCount(1);

		Assert.assertEquals((Long)1L, finderCacheListResult.getCount());
		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertEquals((Long)1L, finderCacheListResult.getCount());
		Assert.assertSame(
			Collections.emptyList(),
			finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult.set(result.size(), result);

		Assert.assertEquals(
			Long.valueOf(result.size()), finderCacheListResult.getCount());
		Assert.assertEquals(
			result, finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertEquals(
			Long.valueOf(result.size()), finderCacheListResult.getCount());
		Assert.assertEquals(
			result, finderCacheListResult.getResult(testBasePersistence));

		result.add(testBaseModel3);

		finderCacheListResult.set(result.size(), result);

		Assert.assertEquals(
			Long.valueOf(result.size()), finderCacheListResult.getCount());

		Assert.assertNull(finderCacheListResult.getResult(testBasePersistence));

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		Assert.assertEquals(
			Long.valueOf(result.size()), finderCacheListResult.getCount());
		Assert.assertNull(finderCacheListResult.getResult(testBasePersistence));

		_properties.put(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD, "-1");

		finderCache = _activateFinderCache(_notSerializedMultiVMPool);

		finderCacheListResult = finderCache.getResults(_finderPath, _KEY1);

		finderCacheListResult.setResult(result);

		Assert.assertNull(finderCacheListResult.getCount());
		Assert.assertEquals(
			result, finderCacheListResult.getResult(testBasePersistence));

		map.remove(testBaseModel1.getPrimaryKeyObj());

		Assert.assertNull(finderCacheListResult.getResult(testBasePersistence));
	}

	@Test
	public void testNotifyPortalCacheRemovedPortalCacheName() {
		FinderCacheImpl finderCacheImpl = new FinderCacheImpl();

		finderCacheImpl.setMultiVMPool(
			(MultiVMPool)ProxyUtil.newProxyInstance(
				_classLoader, new Class<?>[] {MultiVMPool.class},
				new MultiVMPoolInvocationHandler(_classLoader, true)));
		finderCacheImpl.setProps(PropsTestUtil.setProps(_properties));

		finderCacheImpl.activate();

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
	public void testTestKeysCollide() {
		Assert.assertEquals(
			_cacheKeyGenerator.getCacheKey(_KEY1),
			_cacheKeyGenerator.getCacheKey(_KEY2));
	}

	@Test
	public void testThreshold() {
		_properties.put(
			PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD, "2");

		FinderCache finderCache = _activateFinderCache(
			_notSerializedMultiVMPool);

		Map<Serializable, TestBaseModel> map =
			HashMapBuilder.<Serializable, TestBaseModel>put(
				"a", new TestBaseModel("a")
			).put(
				"b", new TestBaseModel("b")
			).build();

		List<TestBaseModel> values = new ArrayList<>(map.values());

		finderCache.putResult(_finderPath, _KEY1, values, true);

		Object result = finderCache.getResult(
			_finderPath, _KEY1, new TestBasePersistence(map));

		Assert.assertEquals(values, result);

		map.put("c", new TestBaseModel("c"));

		finderCache.putResult(
			_finderPath, _KEY1, new ArrayList<>(map.values()), true);

		result = finderCache.getResult(
			_finderPath, _KEY1, new TestBasePersistence(null));

		Assert.assertNull(result);
	}

	private FinderCache _activateFinderCache(MultiVMPool multiVMPool) {
		FinderCacheImpl finderCacheImpl = new FinderCacheImpl();

		finderCacheImpl.setMultiVMPool(multiVMPool);

		finderCacheImpl.setProps(PropsTestUtil.setProps(_properties));

		finderCacheImpl.activate();

		return finderCacheImpl;
	}

	private void _assertPutEmptyListInvalid(MultiVMPool multiVMPool) {
		FinderCache finderCache = _activateFinderCache(multiVMPool);

		finderCache.putResult(
			_finderPath, _KEY1, Collections.emptyList(), true);

		Object result = finderCache.getResult(_finderPath, _KEY2, null);

		Assert.assertNull(result);
	}

	private void _assertPutEmptyListValid(MultiVMPool multiVMPool) {
		FinderCache finderCache = _activateFinderCache(multiVMPool);

		finderCache.putResult(
			_finderPath, _KEY1, Collections.emptyList(), true);

		Object result = finderCache.getResult(_finderPath, _KEY1, null);

		Assert.assertSame(Collections.emptyList(), result);
	}

	private static final String[] _KEY1 = {"home"};

	private static final String[] _KEY2 = {"j1me"};

	private static final CacheKeyGenerator _cacheKeyGenerator =
		new HashCodeHexStringCacheKeyGenerator();
	private static final ClassLoader _classLoader =
		FinderCacheImplTest.class.getClassLoader();
	private static MultiVMPool _notSerializedMultiVMPool;
	private static Map<String, Object> _properties;
	private static MultiVMPool _serializedMultiVMPool;

	private FinderPath _finderPath;

	private static class TestBaseModel extends BaseModelImpl<TestBaseModel> {

		@Override
		public Object clone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(TestBaseModel testBaseModel) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<?> getModelClass() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getModelClassName() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Serializable getPrimaryKeyObj() {
			return _primaryKey;
		}

		@Override
		public boolean isEntityCacheEnabled() {
			return true;
		}

		@Override
		public boolean isFinderCacheEnabled() {
			return true;
		}

		@Override
		public void setPrimaryKeyObj(Serializable primaryKeyObj) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toXmlString() {
			throw new UnsupportedOperationException();
		}

		private TestBaseModel(Serializable primaryKey) {
			_primaryKey = primaryKey;
		}

		private final Serializable _primaryKey;

	}

	private static class TestBasePersistence
		extends BasePersistenceImpl<TestBaseModel> {

		@Override
		public TestBaseModel fetchByPrimaryKey(Serializable serializable) {
			return _map.get(serializable);
		}

		@Override
		public Map<Serializable, TestBaseModel> fetchByPrimaryKeys(
			Set<Serializable> primaryKeys) {

			Assert.assertNotNull(_map);

			Map<Serializable, TestBaseModel> map = new HashMap<>();

			for (Serializable serializable : primaryKeys) {
				TestBaseModel testBaseModel = _map.get(serializable);

				if (testBaseModel != null) {
					map.put(serializable, testBaseModel);
				}
			}

			return map;
		}

		private TestBasePersistence(Map<Serializable, TestBaseModel> map) {
			_map = map;
		}

		private final Map<Serializable, TestBaseModel> _map;

	}

}