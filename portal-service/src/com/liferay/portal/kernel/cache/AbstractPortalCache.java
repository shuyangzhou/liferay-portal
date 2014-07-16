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

package com.liferay.portal.kernel.cache;

import java.io.Serializable;

/**
 * @author Tina Tian
 */
public abstract class AbstractPortalCache <K extends Serializable, V>
	implements PortalCache<K, V> {

	@Override
	public void put(K key, V value) {
		put(key, value, _DEFAULT_TIME_TO_LIVE);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return putIfAbsent(key, value, _DEFAULT_TIME_TO_LIVE);
	}

	@Override
	public void putQuiet(K key, V value) {
		putQuiet(key, value, _DEFAULT_TIME_TO_LIVE);
	}

	@Override
	public V replace(K key, V value) {
		return replace(key, value, _DEFAULT_TIME_TO_LIVE);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		return replace(key, oldValue, newValue, _DEFAULT_TIME_TO_LIVE);
	}

	private static final int _DEFAULT_TIME_TO_LIVE = -1;

}