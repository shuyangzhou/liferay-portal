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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.util.ServiceRetriever;

import java.util.Set;

/**
 * @author Raymond Augé
 */
public class IndexerRegistryUtil {

	public static <T> Indexer<T> getIndexer(Class<T> clazz) {
		return _getIndexerRegistry().getIndexer(clazz);
	}

	public static <T> Indexer<T> getIndexer(String className) {
		return _getIndexerRegistry().getIndexer(className);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #_getIndexerRegistry()}
	 */
	@Deprecated
	public static IndexerRegistry getIndexerRegistry() {
		return _getIndexerRegistry();
	}

	public static Set<Indexer<?>> getIndexers() {
		return _getIndexerRegistry().getIndexers();
	}

	public static <T> Indexer<T> nullSafeGetIndexer(Class<T> clazz) {
		return _getIndexerRegistry().nullSafeGetIndexer(clazz);
	}

	public static <T> Indexer<T> nullSafeGetIndexer(String className) {
		return _getIndexerRegistry().nullSafeGetIndexer(className);
	}

	public static void register(Indexer<?> indexer) {
		_getIndexerRegistry().register(indexer);
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link #register(Indexer)}
	 */
	@Deprecated
	public static void register(String className, Indexer<?> indexer) {
		_getIndexerRegistry().register(indexer);
	}

	public static void unregister(Indexer<?> indexer) {
		_getIndexerRegistry().unregister(indexer);
	}

	public static void unregister(String className) {
		_getIndexerRegistry().unregister(className);
	}

	private static IndexerRegistry _getIndexerRegistry() {
		return _serviceRetriever.getService();
	}

	private static final ServiceRetriever<IndexerRegistry> _serviceRetriever =
		new ServiceRetriever<>(IndexerRegistry.class);

}