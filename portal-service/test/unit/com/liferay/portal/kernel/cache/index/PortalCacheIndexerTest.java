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

package com.liferay.portal.kernel.cache.index;

import com.liferay.portal.kernel.cache.TestPortalCache;
import com.liferay.portal.kernel.concurrent.test.MappedMethodNameRunnableAdvice;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class PortalCacheIndexerTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Before
	public void setUp() throws Exception {
		_portalCache = new TestPortalCache<>();

		_portalCache.put(_KEY_1_1, _VALUE);

		_portalCacheIndexer = new PortalCacheIndexer<>(_portalCache);

		_mappedMethodNameRunnableAdvice = new MappedMethodNameRunnableAdvice(
			_portalCacheIndexer, "_indexedCacheKeys", ConcurrentMap.class);
	}

	@Test
	public void testAddIndexedCacheKeyConcurrentPut() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.put(_KEY_2_1, _VALUE);
			}

		};

		_mappedMethodNameRunnableAdvice.putBeforeRunnable(
			"putIfAbsent", runnable);

		_portalCache.put(_KEY_2_2, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testAddIndexedCacheKeyConcurrentPutRemove() {
		_portalCacheIndexer.removeIndexedCacheKeys(_KEY_1_1.getIndex());

		Runnable beforeRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.put(_KEY_1_1, _VALUE);
			}

		};

		Runnable afterRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_1);
			}

		};

		_mappedMethodNameRunnableAdvice.putAfterRunnable(
			"putIfAbsent", afterRunnable);
		_mappedMethodNameRunnableAdvice.putBeforeRunnable(
			"putIfAbsent", beforeRunnable);

		_portalCache.put(_KEY_1_2, _VALUE);

		_portalCacheIndexer.removeIndexedCacheKeys(_KEY_1_2.getIndex());

		assertIndexCacheSynchronization();
	}

	@Test
	public void testAddIndexedCacheKeyPutSameKey() {
		_portalCache.put(_KEY_1_1, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testAddIndexedCacheKeyWithIndex() {
		_portalCache.put(_KEY_1_2, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testAddIndexedCacheKeyWithoutIndex() {
		_portalCache.put(_KEY_2_1, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testDispose() {
		_portalCache.dispose();

		assertIndexCacheSynchronization();
	}

	@Test
	public void testGetIndexedCacheKeysWithIndex() {
		Set<TestIndexedCacheKey> set = _portalCacheIndexer.getIndexedCacheKeys(
			_KEY_1_1.getIndex());

		set.clear();

		assertIndexCacheSynchronization();
	}

	@Test
	public void testGetIndexedCacheKeysWithoutIndex() {
		Set<TestIndexedCacheKey> set = _portalCacheIndexer.getIndexedCacheKeys(
			_KEY_2_1.getIndex());

		Assert.assertTrue(set.isEmpty());
	}

	@Test
	public void testNotifyEntryEvicted() {
		_portalCache.evict(_KEY_1_1, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testNotifyEntryExpired() {
		_portalCache.expire(_KEY_1_1, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testNotifyEntryRemoved() {
		_portalCache.remove(_KEY_1_1);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testNotifyEntryUpdated() {
		_portalCache.put(_KEY_1_1, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testNotifyRemoveAll() {
		_portalCache.removeAll();

		assertIndexCacheSynchronization();
	}

	@Test
	public void testNotifyRemoveAllConcurrentAdd() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.put(_KEY_1_2, _VALUE);
			}

		};

		_mappedMethodNameRunnableAdvice.putBeforeRunnable("clear", runnable);

		_portalCache.removeAll();

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeyConcurrentAdd() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.put(_KEY_1_1, _VALUE);
			}

		};

		_mappedMethodNameRunnableAdvice.putBeforeRunnable("remove", runnable);

		_portalCache.remove(_KEY_1_1);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeyConcurrentRemove() {
		_portalCache.put(_KEY_1_2, _VALUE);

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_1);
			}

		};

		_mappedMethodNameRunnableAdvice.putBeforeRunnable("replace", runnable);

		_portalCache.remove(_KEY_1_2);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeyConcurrentRemoveAll() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.removeAll();
			}

		};

		_mappedMethodNameRunnableAdvice.putBeforeRunnable("remove", runnable);

		_portalCache.remove(_KEY_1_1);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeysWithIndexKey() {
		_portalCacheIndexer.removeIndexedCacheKeys(_KEY_1_1.getIndex());

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeysWithoutIndexKey() {
		_portalCacheIndexer.removeIndexedCacheKeys(_KEY_2_1.getIndex());

		assertIndexCacheSynchronization();
	}

	@Test
	public void testRemoveIndexedCacheKeysWithoutKey() {
		_portalCache.remove(_KEY_1_2);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testSyncIndexStateAdd() {
		Runnable afterReplaceRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_2);
			}

		};

		Runnable beforeReplaceRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_1);
			}

		};

		Runnable beforePutIfAbsentRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.put(_KEY_1_3, _VALUE);
			}

		};

		_mappedMethodNameRunnableAdvice.putAfterRunnable(
			"replace", afterReplaceRunnable);
		_mappedMethodNameRunnableAdvice.putBeforeRunnable(
			"replace", beforeReplaceRunnable);
		_mappedMethodNameRunnableAdvice.putBeforeRunnable(
			"putIfAbsent", beforePutIfAbsentRunnable);

		_portalCache.put(_KEY_1_2, _VALUE);

		assertIndexCacheSynchronization();
	}

	@Test
	public void testSyncIndexStateRemove() {
		Runnable beforeRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_1);
			}

		};

		Runnable afterRunnable = new Runnable() {

			@Override
			public void run() {
				_portalCache.remove(_KEY_1_2);
			}

		};

		_mappedMethodNameRunnableAdvice.putAfterRunnable(
			"replace", afterRunnable);
		_mappedMethodNameRunnableAdvice.putBeforeRunnable(
			"replace", beforeRunnable);

		_portalCache.put(_KEY_1_2, _VALUE);

		assertIndexCacheSynchronization();
	}

	protected void assertIndexCacheSynchronization() {
		List<TestIndexedCacheKey> keys = _portalCache.getKeys();

		Set<Long> indexes = new HashSet<>();

		for (TestIndexedCacheKey key : keys) {
			indexes.add(key.getIndex());
		}

		Set<TestIndexedCacheKey> indexedCacheKeys = new HashSet<>();

		for (Long index : indexes) {
			indexedCacheKeys.addAll(
				_portalCacheIndexer.getIndexedCacheKeys(index));
		}

		Assert.assertEquals(keys.size(), indexedCacheKeys.size());

		for (TestIndexedCacheKey key : keys) {
			Assert.assertTrue(indexedCacheKeys.contains(key));
		}
	}

	private static final TestIndexedCacheKey _KEY_1_1 = new TestIndexedCacheKey(
		1L, 1L);

	private static final TestIndexedCacheKey _KEY_1_2 = new TestIndexedCacheKey(
		1L, 2L);

	private static final TestIndexedCacheKey _KEY_1_3 = new TestIndexedCacheKey(
		1L, 3L);

	private static final TestIndexedCacheKey _KEY_2_1 = new TestIndexedCacheKey(
		2L, 1L);

	private static final TestIndexedCacheKey _KEY_2_2 = new TestIndexedCacheKey(
		2L, 2L);

	private static final String _VALUE = "VALUE";

	private MappedMethodNameRunnableAdvice _mappedMethodNameRunnableAdvice;
	private TestPortalCache<TestIndexedCacheKey, String> _portalCache;
	private PortalCacheIndexer<Long, TestIndexedCacheKey, String>
		_portalCacheIndexer;

}