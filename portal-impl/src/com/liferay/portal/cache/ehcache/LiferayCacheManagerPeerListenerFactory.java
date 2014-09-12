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

package com.liferay.portal.cache.ehcache;

import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.util.PropsValues;

import java.util.List;
import java.util.Properties;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.distribution.CacheManagerPeerListener;
import net.sf.ehcache.distribution.CacheManagerPeerListenerFactory;

/**
 * @author Tina Tian
 */
public class LiferayCacheManagerPeerListenerFactory
	extends CacheManagerPeerListenerFactory {

	public LiferayCacheManagerPeerListenerFactory() {
		String className =
			"net.sf.ehcache.distribution.RMICacheManagerPeerListenerFactory";

		try {
			_cacheManagerPeerListenerFactory =
				(CacheManagerPeerListenerFactory)InstanceFactory.newInstance(
					className);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public CacheManagerPeerListener createCachePeerListener(
		CacheManager cacheManager, Properties properties) {

		CacheManagerPeerListener cacheManagerPeerListener =
			_cacheManagerPeerListenerFactory.createCachePeerListener(
				cacheManager, properties);

		if (PropsValues.CLUSTER_LINK_ENABLED &&
			!PropsValues.EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED) {

			return new LiferayCacheManagerPeerListener(
				cacheManager, cacheManagerPeerListener);
		}

		return cacheManagerPeerListener;
	}

	private CacheManagerPeerListenerFactory _cacheManagerPeerListenerFactory;

	private static class LiferayCacheManagerPeerListener
		implements CacheManagerPeerListener {

		public LiferayCacheManagerPeerListener(
			CacheManager cacheManager,
			CacheManagerPeerListener cacheManagerPeerListener) {

			_cacheManager = cacheManager;
			_cacheManagerPeerListener = cacheManagerPeerListener;
		}

		@Override
		public void attemptResolutionOfUniqueResourceConflict()
			throws CacheException, IllegalStateException {

			_cacheManagerPeerListener.
				attemptResolutionOfUniqueResourceConflict();
		}

		@Override
		public void dispose() throws CacheException {
			_cacheManagerPeerListener.dispose();
		}

		@Override
		public List getBoundCachePeers() {
			return _cacheManagerPeerListener.getBoundCachePeers();
		}

		@Override
		public String getScheme() {
			return _cacheManagerPeerListener.getScheme();
		}

		@Override
		public Status getStatus() {
			return _cacheManagerPeerListener.getStatus();
		}

		@Override
		public String getUniqueResourceIdentifier() {
			return _cacheManagerPeerListener.getUniqueResourceIdentifier();
		}

		@Override
		public void init() throws CacheException {
			for (String cacheName : _cacheManager.getCacheNames()) {
				_wrapEhcache(cacheName);
			}

			try {
				_cacheManagerPeerListener.init();
			}
			finally {
				for (String cacheName : _cacheManager.getCacheNames()) {
					_unwrapEhcache(cacheName);
				}
			}
		}

		@Override
		public void notifyCacheAdded(String cacheName) {
			_wrapEhcache(cacheName);

			try {
				_cacheManagerPeerListener.notifyCacheAdded(cacheName);
			}
			finally {
				_unwrapEhcache(cacheName);
			}
		}

		@Override
		public void notifyCacheRemoved(String cacheName) {
			_cacheManagerPeerListener.notifyCacheRemoved(cacheName);
		}

		private void _unwrapEhcache(String cacheName) {
			Ehcache ehcache = _cacheManager.getEhcache(cacheName);

			if (!(ehcache instanceof LiferayCacheDecorator)) {
				return;
			}

			LiferayCacheDecorator liferayCacheDecorator =
				(LiferayCacheDecorator)ehcache;

			_cacheManager.replaceCacheWithDecoratedCache(
				liferayCacheDecorator,
				liferayCacheDecorator.getUnderlyingCache());
		}

		private void _wrapEhcache(String cacheName) {
			Ehcache ehcache = _cacheManager.getEhcache(cacheName);

			if (!(ehcache instanceof LiferayCacheDecorator)) {
				_cacheManager.replaceCacheWithDecoratedCache(
					ehcache, new LiferayCacheDecorator(ehcache));
			}
		}

		private CacheManager _cacheManager;
		private CacheManagerPeerListener _cacheManagerPeerListener;

	}

}