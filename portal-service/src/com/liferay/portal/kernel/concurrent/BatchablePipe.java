/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

import com.liferay.portal.util.PropsValues;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Shuyang Zhou
 * @author Zsolt Berentey
 */
public class BatchablePipe<K, V> {

	public BatchablePipe() {
		_delayedEntries = new DelayQueue<Entry<K, V>>();
		_delayedEntryKeys = new ConcurrentHashMap<K, Entry<K, V>>(
			16, 0.75f, PropsValues.BUFFERED_INCREMENT_CONCURRENCY_LEVEL);
		_headEntry = new Entry<K, V>(null);
		_lastEntryReference = new AtomicReference<Entry<K, V>>(_headEntry);
	}

	public boolean checkDelayedEntries() {
		if (!_delayedEntriesEnabled) {
			return false;
		}

		int flushedEntries = 0;

		while (true) {
			Entry<K, V> delayedEntry = _delayedEntries.poll();

			if (delayedEntry == null) {
				break;
			}

			Entry<K, V> removedEntry = _delayedEntryKeys.remove(
				delayedEntry._increasableEntry.getKey());

			if (removedEntry != null) {
				_delayedEntries.remove(delayedEntry);

				_doPut(delayedEntry);

				flushedEntries++;
			}
		}

		return (flushedEntries > 0);
	}

	public boolean flushDelayedEntries() {
		int flushedEntries = 0;

		for (Entry<K, V> delayedEntry : _delayedEntryKeys.values()) {
			Entry<K, V> removedEntry = _delayedEntryKeys.remove(
				delayedEntry._increasableEntry.getKey());

			if (removedEntry != null) {
				_delayedEntries.remove(delayedEntry);

				_doPut(delayedEntry);

				flushedEntries++;
			}
		}

		return (flushedEntries > 0);
	}

	public boolean put(IncreasableEntry<K, V> increasableEntry) {
		Entry<K, V> newEntry = new Entry<K, V>(increasableEntry);

		if (_delayedEntriesEnabled) {
			Entry<K, V> delayedEntry = null;

			if (increasableEntry.isDelayable()) {
				delayedEntry =
					_delayedEntryKeys.putIfAbsent(
						increasableEntry.getKey(), newEntry);

				if (delayedEntry == null) {
					_delayedEntries.add(newEntry);

					return false;
				}
			}

			if (delayedEntry != null) {
				delayedEntry._increasableEntry.increase(
					increasableEntry.getValue());

				if (delayedEntry._increasableEntry.isDelayable(
						delayedEntry._startTime)) {

					return false;
				}

				Entry<K, V> removedEntry = _delayedEntryKeys.remove(
					delayedEntry._increasableEntry.getKey());

				if (removedEntry == null) {
					return false;
				}

				_delayedEntries.remove(delayedEntry);

				increasableEntry = delayedEntry._increasableEntry;

				newEntry = delayedEntry;
			}
		}

		return _doPut(newEntry);
	}

	public void setDelayedEntriesEnabled(boolean delayedEntriesEnabled) {
		_delayedEntriesEnabled = delayedEntriesEnabled;
	}

	public IncreasableEntry<K, V> take() {
		boolean[] marked = {false};

		take:
		while (true) {
			Entry<K, V> predecessorEntry = _headEntry;
			Entry<K, V> currentEntry =
				predecessorEntry._nextEntry.getReference();

			while (currentEntry != null) {
				Entry<K, V> successorEntry = currentEntry._nextEntry.get(
					marked);

				if (marked[0]) {
					if (!predecessorEntry._nextEntry.compareAndSet(
							currentEntry, successorEntry, false, false)) {

						continue take;
					}

					currentEntry = predecessorEntry._nextEntry.getReference();

					continue;
				}

				if (currentEntry._nextEntry.compareAndSet(
						successorEntry, successorEntry, false, true)) {

					return currentEntry._increasableEntry;
				}
				else {
					continue take;
				}
			}

			return null;
		}
	}

	private boolean _doPut(Entry<K, V> newEntry) {
		while (true) {
			if (_increaseInPipe(newEntry._increasableEntry)) {
				return false;
			}

			Entry<K, V> lastEntryLink = _lastEntryReference.get();
			Entry<K, V> nextEntryLink = lastEntryLink._nextEntry.getReference();

			if (nextEntryLink == null) {
				if (lastEntryLink._nextEntry.compareAndSet(
						null, newEntry, false, false)) {

					_lastEntryReference.set(newEntry);

					return true;
				}
			}
			else {
				_lastEntryReference.compareAndSet(lastEntryLink, nextEntryLink);
			}
		}
	}

	private boolean _increaseInPipe(IncreasableEntry<K, V> increasableEntry) {
		boolean[] marked = {false};

		Retry:

		while (true) {
			Entry<K, V> predecessorEntry = _headEntry;
			Entry<K, V> currentEntry =
				predecessorEntry._nextEntry.getReference();

			while (currentEntry != null) {
				Entry<K, V> successorEntry = currentEntry._nextEntry.get(
					marked);

				if (marked[0]) {
					if (!predecessorEntry._nextEntry.compareAndSet(
							currentEntry, successorEntry, false, false)) {

						continue Retry;
					}

					currentEntry = predecessorEntry._nextEntry.getReference();

					continue;
				}

				if (currentEntry._increasableEntry.getKey().equals(
						increasableEntry.getKey())) {

					return currentEntry._increasableEntry.increase(
						increasableEntry.getValue());
				}

				predecessorEntry = currentEntry;
				currentEntry = successorEntry;
			}

			_lastEntryReference.set(predecessorEntry);

			return false;
		}
	}

	private final DelayQueue<Entry<K, V>> _delayedEntries;
	private final ConcurrentHashMap<K, Entry<K, V>>
		_delayedEntryKeys;

	private boolean _delayedEntriesEnabled = true;

	private final Entry<K, V> _headEntry;
	private final AtomicReference<Entry<K, V>> _lastEntryReference;

	private static class Entry<K, V> implements Delayed {

		public Entry(IncreasableEntry<K, V> increasableEntry) {
			_increasableEntry = increasableEntry;
			_nextEntry = new AtomicMarkableReference<Entry<K, V>>(null, false);
			_startTime = System.currentTimeMillis();
		}

		@Override
		public int compareTo(Delayed delayed) {
			if (delayed == this) {
				return 0;
			}

			long diff =
				getDelay(TimeUnit.MILLISECONDS) -
					delayed.getDelay(TimeUnit.MILLISECONDS);

			if (diff == 0) {
				return 0;
			}

			if (diff < 0) {
				return -1;
			}

			return 1;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long delayInMillis =
				getFlushTimeInMillis() - System.currentTimeMillis();

			return unit.convert(delayInMillis, TimeUnit.MILLISECONDS);
		}

		private long getFlushTimeInMillis() {
			if (_increasableEntry.getTimeOutInMillis() == -1) {
				return _startTime;
			}

			return _startTime + _increasableEntry.getTimeOutInMillis();
		}

		private IncreasableEntry<K, V> _increasableEntry;
		private AtomicMarkableReference<Entry<K, V>> _nextEntry;
		private final long _startTime;

	}

}