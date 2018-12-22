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

package com.liferay.counter.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link Counter}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Counter
 * @generated
 */
@ProviderType
public class CounterWrapper implements Counter, ModelWrapper<Counter> {
	public CounterWrapper(Counter counter) {
		_counter = counter;
	}

	@Override
	public Class<?> getModelClass() {
		return Counter.class;
	}

	@Override
	public String getModelClassName() {
		return Counter.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Counter, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Counter, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Counter, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Counter, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Counter, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Counter, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Counter, Object>> getAttributeGetters() {
		return _counter.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Counter, Object>> getAttributeSetters() {
		return _counter.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new CounterWrapper((Counter)_counter.clone());
	}

	@Override
	public int compareTo(Counter counter) {
		return _counter.compareTo(counter);
	}

	/**
	* Returns the current ID of this counter.
	*
	* @return the current ID of this counter
	*/
	@Override
	public long getCurrentId() {
		return _counter.getCurrentId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _counter.getExpandoBridge();
	}

	/**
	* Returns the name of this counter.
	*
	* @return the name of this counter
	*/
	@Override
	public String getName() {
		return _counter.getName();
	}

	/**
	* Returns the primary key of this counter.
	*
	* @return the primary key of this counter
	*/
	@Override
	public String getPrimaryKey() {
		return _counter.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _counter.getPrimaryKeyObj();
	}

	@Override
	public int hashCode() {
		return _counter.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _counter.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _counter.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _counter.isNew();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_counter.setCachedModel(cachedModel);
	}

	/**
	* Sets the current ID of this counter.
	*
	* @param currentId the current ID of this counter
	*/
	@Override
	public void setCurrentId(long currentId) {
		_counter.setCurrentId(currentId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_counter.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_counter.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_counter.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the name of this counter.
	*
	* @param name the name of this counter
	*/
	@Override
	public void setName(String name) {
		_counter.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_counter.setNew(n);
	}

	/**
	* Sets the primary key of this counter.
	*
	* @param primaryKey the primary key of this counter
	*/
	@Override
	public void setPrimaryKey(String primaryKey) {
		_counter.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_counter.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Counter> toCacheModel() {
		return _counter.toCacheModel();
	}

	@Override
	public Counter toEscapedModel() {
		return new CounterWrapper(_counter.toEscapedModel());
	}

	@Override
	public String toString() {
		return _counter.toString();
	}

	@Override
	public Counter toUnescapedModel() {
		return new CounterWrapper(_counter.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _counter.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CounterWrapper)) {
			return false;
		}

		CounterWrapper counterWrapper = (CounterWrapper)obj;

		if (Objects.equals(_counter, counterWrapper._counter)) {
			return true;
		}

		return false;
	}

	@Override
	public Counter getWrappedModel() {
		return _counter;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _counter.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _counter.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_counter.resetOriginalValues();
	}

	private final Counter _counter;
}