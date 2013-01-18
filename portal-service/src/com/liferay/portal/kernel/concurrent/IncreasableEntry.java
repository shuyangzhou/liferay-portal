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

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * @author Shuyang Zhou
 */
public abstract class IncreasableEntry<K, V> {

	public IncreasableEntry(K key, V value) {
		this(key, value, null, -1);
	}

	public IncreasableEntry(K key, V value, long timeOutInMillis) {
		this(key, value, null, timeOutInMillis);
	}

	public IncreasableEntry(K key, V value, V valueThreshold) {
		this(key, value, valueThreshold, -1);
	}

	public IncreasableEntry(
		K key, V value, V valueThreshold, long timeOutInMillis) {

		_key = key;
		_markedValue = new AtomicMarkableReference<V>(value, false);
		_timeOutInMillis = timeOutInMillis;
		_valueThreshold = valueThreshold;
	}

	public abstract V doIncrease(V originalValue, V deltaValue);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof IncreasableEntry<?, ?>)) {
			return false;
		}

		IncreasableEntry<K, V> increasableEntry = (IncreasableEntry<K, V>)obj;

		if (Validator.equals(
				_timeOutInMillis, increasableEntry._timeOutInMillis) &&
			Validator.equals(_key, increasableEntry._key) &&
			Validator.equals(
				_markedValue.getReference(),
				increasableEntry._markedValue.getReference()) &&
			Validator.equals(
				_valueThreshold, increasableEntry._valueThreshold)) {

			return true;
		}

		return false;
	}

	public final K getKey() {
		return _key;
	}

	public final long getTimeOutInMillis() {
		return _timeOutInMillis;
	}

	public final V getValue() {
		while (true) {
			V value = _markedValue.getReference();

			if (_markedValue.attemptMark(value, true)) {
				return value;
			}
		}
	}

	public final V getValueThreshold() {
		return _valueThreshold;
	}

	@Override
	public int hashCode() {
		int hash = 77;

		if (_key != null) {
			hash += _key.hashCode();
		}

		V value = _markedValue.getReference();

		if (value != null) {
			hash = hash * 11 + value.hashCode();
		}

		if (_valueThreshold != null) {
			hash = hash * 11 + _valueThreshold.hashCode();
		}

		if (_timeOutInMillis != -1) {
			hash =
				hash * 11 +
				(int)(_timeOutInMillis ^ (_timeOutInMillis >>> 32));
		}

		return hash;
	}

	public final boolean increase(V deltaValue) {
		boolean[] marked = {false};

		while (true) {
			V originalValue = _markedValue.get(marked);

			if (marked[0]) {
				return false;
			}
			else {
				V newValue = doIncrease(originalValue, deltaValue);

				if (_markedValue.compareAndSet(
						originalValue, newValue, false, false)) {

					return true;
				}
			}
		}
	}

	public boolean isDelayable() {
		if ((_valueThreshold == null) && (_timeOutInMillis == -1)) {
			return false;
		}

		if ((_valueThreshold != null) && isValueAboveThreshold()) {
			return false;
		}

		return true;
	}

	public boolean isDelayable(long startTime) {
		if (!isDelayable()) {
			return false;
		}

		long currentTimeInMillis = System.currentTimeMillis();

		if ((_timeOutInMillis > -1) &&
			(currentTimeInMillis > (startTime + _timeOutInMillis))) {

			return false;
		}

		return true;
	}

	public abstract boolean isValueAboveThreshold();

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(5);

		sb.append("{key=");
		sb.append(String.valueOf(_key.toString()));
		sb.append(", value=");
		sb.append(String.valueOf(_markedValue.getReference()));
		sb.append(", threshold=");
		sb.append(String.valueOf(_valueThreshold));
		sb.append(", timeout=");
		sb.append(String.valueOf(_timeOutInMillis));
		sb.append("}");

		return sb.toString();
	}

	protected V peekValue() {
		return _markedValue.getReference();
	}

	private final K _key;
	private final AtomicMarkableReference<V> _markedValue;
	private final long _timeOutInMillis;
	private final V _valueThreshold;

}