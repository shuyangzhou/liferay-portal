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

package com.liferay.portal.kernel.concurrent;

import com.liferay.portal.kernel.util.Consumer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Carlos Sierra Andrés
 */
public class ValueSynchronizer<T> {

	public ValueSynchronizer() {
		_clearConsumer = null;
	}

	public ValueSynchronizer(Consumer<T> clearConsumer) {
		_clearConsumer = clearConsumer;
	}

	public void clear(T resource) {
		Lock lock = _resourceLocks.get(resource);

		if (lock != null) {
			try {
				lock.lock();

				if (_clearConsumer != null) {
					_clearConsumer.accept(resource);
				}

				_resourceLocks.remove(resource, lock);
			}
			finally {
				lock.unlock();
			}
		}
	}

	public void close() {
		if (_closed) {
			return;
		}

		_closed = true;

		if (_clearConsumer != null) {
			for (T key : _resourceLocks.keySet()) {
				clear(key);
			}
		}
	}

	public void synchronizeOn(T resource, Consumer<T> consumer) {
		if (_closed) {
			return;
		}

		Lock lock = _resourceLocks.get(resource);

		if (lock == null) {
			Lock newLock = new ReentrantLock();

			lock = _resourceLocks.putIfAbsent(resource, newLock);

			if (lock == null) {
				lock = newLock;
			}
		}

		try {
			lock.lock();

			if (lock != _resourceLocks.get(resource)) {
				return;
			}

			consumer.accept(resource);
		}
		finally {
			lock.unlock();
		}
	}

	private final Consumer<T> _clearConsumer;
	private volatile boolean _closed = false;
	private final ConcurrentMap<T, Lock> _resourceLocks =
		new ConcurrentHashMap<>();

}