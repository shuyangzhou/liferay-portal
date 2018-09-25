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

package com.liferay.petra.concurrent;

import com.liferay.petra.memory.FinalizeAction;
import com.liferay.petra.memory.FinalizeManager;

import java.lang.ref.Reference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Shuyang Zhou
 */
public class ConcurrentReferenceValueHashMap<K, V>
	extends ConcurrentMapperHashMap<K, K, V, Reference<V>> {

	public ConcurrentReferenceValueHashMap(
		ConcurrentMap<K, Reference<V>> innerConcurrentMap,
		FinalizeManager.ReferenceFactory referenceFactory) {

		super(innerConcurrentMap);

		_referenceFactory = referenceFactory;
	}

	public ConcurrentReferenceValueHashMap(
		FinalizeManager.ReferenceFactory referenceFactory) {

		this(new ConcurrentHashMap<K, Reference<V>>(), referenceFactory);
	}

	public ConcurrentReferenceValueHashMap(
		int initialCapacity,
		FinalizeManager.ReferenceFactory referenceFactory) {

		this(
			new ConcurrentHashMap<K, Reference<V>>(initialCapacity),
			referenceFactory);
	}

	public ConcurrentReferenceValueHashMap(
		int initialCapacity, float loadFactor, int concurrencyLevel,
		FinalizeManager.ReferenceFactory referenceFactory) {

		this(
			new ConcurrentHashMap<K, Reference<V>>(
				initialCapacity, loadFactor, concurrencyLevel),
			referenceFactory);
	}

	public ConcurrentReferenceValueHashMap(
		Map<? extends K, ? extends V> map,
		FinalizeManager.ReferenceFactory referenceFactory) {

		this(new ConcurrentHashMap<K, Reference<V>>(), referenceFactory);

		putAll(map);
	}

	@Override
	public V compute(
		K key,
		BiFunction<? super K, ? super V, ? extends V> remappingFunction) {

		if (key == null) {
			throw new NullPointerException("Key is null");
		}

		if (remappingFunction == null) {
			throw new NullPointerException("Remapping function is null");
		}

		Reference<V> innerValue = innerConcurrentMap.compute(
			key,
			(iKey, iValue) -> {
				V value = null;

				if (iValue == null) {
					value = remappingFunction.apply(iKey, null);
				}
				else {
					value = remappingFunction.apply(iKey, unmapValue(iValue));
				}

				if (value == null) {
					return null;
				}

				return mapValue(key, value);
			});

		if (innerValue == null) {
			return null;
		}

		return unmapValueForQuery(innerValue);
	}

	@Override
	public V computeIfAbsent(
		K key, Function<? super K, ? extends V> mappingFunction) {

		if (key == null) {
			throw new NullPointerException("Key is null");
		}

		if (mappingFunction == null) {
			throw new NullPointerException("Mapping function is null");
		}

		Reference<V> innerValue = innerConcurrentMap.computeIfAbsent(
			key,
			iKey -> {
				V value = mappingFunction.apply(iKey);

				if (value == null) {
					return null;
				}

				return mapValue(iKey, value);
			});

		if (innerValue == null) {
			return null;
		}

		return unmapValueForQuery(innerValue);
	}

	@Override
	public V computeIfPresent(
		K key,
		BiFunction<? super K, ? super V, ? extends V> remappingFunction) {

		if (key == null) {
			throw new NullPointerException("Key is null");
		}

		if (remappingFunction == null) {
			throw new NullPointerException("Remapping function is null");
		}

		Reference<V> innerValue = innerConcurrentMap.computeIfPresent(
			key,
			(iKey, iValue) -> {
				V value = remappingFunction.apply(iKey, unmapValue(iValue));

				if (value == null) {
					return null;
				}

				return mapValue(iKey, value);
			});

		if (innerValue == null) {
			return null;
		}

		return unmapValueForQuery(innerValue);
	}

	@Override
	protected K mapKey(K key) {
		return key;
	}

	@Override
	protected K mapKeyForQuery(K key) {
		return key;
	}

	@Override
	protected Reference<V> mapValue(K key, V value) {
		return FinalizeManager.register(
			value, new RemoveEntryFinalizeAction(key), _referenceFactory);
	}

	@Override
	protected Reference<V> mapValueForQuery(V value) {
		return _referenceFactory.createReference(value, null);
	}

	@Override
	protected K unmapKey(K key) {
		return key;
	}

	@Override
	protected K unmapKeyForQuery(K key) {
		return key;
	}

	@Override
	protected V unmapValue(Reference<V> reference) {
		V value = reference.get();

		reference.clear();

		return value;
	}

	@Override
	protected V unmapValueForQuery(Reference<V> reference) {
		return reference.get();
	}

	private final FinalizeManager.ReferenceFactory _referenceFactory;

	private class RemoveEntryFinalizeAction implements FinalizeAction {

		public RemoveEntryFinalizeAction(K key) {
			_key = key;
		}

		@Override
		public void doFinalize(Reference<?> reference) {
			remove(_key);
		}

		private final K _key;

	}

}