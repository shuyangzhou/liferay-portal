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

package com.liferay.portal.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link BrowserTracker}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see BrowserTracker
 * @generated
 */
@ProviderType
public class BrowserTrackerWrapper implements BrowserTracker,
	ModelWrapper<BrowserTracker> {
	public BrowserTrackerWrapper(BrowserTracker browserTracker) {
		_browserTracker = browserTracker;
	}

	@Override
	public Class<?> getModelClass() {
		return BrowserTracker.class;
	}

	@Override
	public String getModelClassName() {
		return BrowserTracker.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<BrowserTracker, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<BrowserTracker, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<BrowserTracker, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<BrowserTracker, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<BrowserTracker, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<BrowserTracker, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<BrowserTracker, Object>> getAttributeGetters() {
		return _browserTracker.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<BrowserTracker, Object>> getAttributeSetters() {
		return _browserTracker.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new BrowserTrackerWrapper((BrowserTracker)_browserTracker.clone());
	}

	@Override
	public int compareTo(BrowserTracker browserTracker) {
		return _browserTracker.compareTo(browserTracker);
	}

	/**
	* Returns the browser key of this browser tracker.
	*
	* @return the browser key of this browser tracker
	*/
	@Override
	public long getBrowserKey() {
		return _browserTracker.getBrowserKey();
	}

	/**
	* Returns the browser tracker ID of this browser tracker.
	*
	* @return the browser tracker ID of this browser tracker
	*/
	@Override
	public long getBrowserTrackerId() {
		return _browserTracker.getBrowserTrackerId();
	}

	/**
	* Returns the company ID of this browser tracker.
	*
	* @return the company ID of this browser tracker
	*/
	@Override
	public long getCompanyId() {
		return _browserTracker.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _browserTracker.getExpandoBridge();
	}

	/**
	* Returns the mvcc version of this browser tracker.
	*
	* @return the mvcc version of this browser tracker
	*/
	@Override
	public long getMvccVersion() {
		return _browserTracker.getMvccVersion();
	}

	/**
	* Returns the primary key of this browser tracker.
	*
	* @return the primary key of this browser tracker
	*/
	@Override
	public long getPrimaryKey() {
		return _browserTracker.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _browserTracker.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this browser tracker.
	*
	* @return the user ID of this browser tracker
	*/
	@Override
	public long getUserId() {
		return _browserTracker.getUserId();
	}

	/**
	* Returns the user uuid of this browser tracker.
	*
	* @return the user uuid of this browser tracker
	*/
	@Override
	public String getUserUuid() {
		return _browserTracker.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _browserTracker.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _browserTracker.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _browserTracker.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _browserTracker.isNew();
	}

	@Override
	public void persist() {
		_browserTracker.persist();
	}

	/**
	* Sets the browser key of this browser tracker.
	*
	* @param browserKey the browser key of this browser tracker
	*/
	@Override
	public void setBrowserKey(long browserKey) {
		_browserTracker.setBrowserKey(browserKey);
	}

	/**
	* Sets the browser tracker ID of this browser tracker.
	*
	* @param browserTrackerId the browser tracker ID of this browser tracker
	*/
	@Override
	public void setBrowserTrackerId(long browserTrackerId) {
		_browserTracker.setBrowserTrackerId(browserTrackerId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_browserTracker.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this browser tracker.
	*
	* @param companyId the company ID of this browser tracker
	*/
	@Override
	public void setCompanyId(long companyId) {
		_browserTracker.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_browserTracker.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_browserTracker.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_browserTracker.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the mvcc version of this browser tracker.
	*
	* @param mvccVersion the mvcc version of this browser tracker
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_browserTracker.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_browserTracker.setNew(n);
	}

	/**
	* Sets the primary key of this browser tracker.
	*
	* @param primaryKey the primary key of this browser tracker
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_browserTracker.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_browserTracker.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this browser tracker.
	*
	* @param userId the user ID of this browser tracker
	*/
	@Override
	public void setUserId(long userId) {
		_browserTracker.setUserId(userId);
	}

	/**
	* Sets the user uuid of this browser tracker.
	*
	* @param userUuid the user uuid of this browser tracker
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_browserTracker.setUserUuid(userUuid);
	}

	@Override
	public CacheModel<BrowserTracker> toCacheModel() {
		return _browserTracker.toCacheModel();
	}

	@Override
	public BrowserTracker toEscapedModel() {
		return new BrowserTrackerWrapper(_browserTracker.toEscapedModel());
	}

	@Override
	public String toString() {
		return _browserTracker.toString();
	}

	@Override
	public BrowserTracker toUnescapedModel() {
		return new BrowserTrackerWrapper(_browserTracker.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _browserTracker.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BrowserTrackerWrapper)) {
			return false;
		}

		BrowserTrackerWrapper browserTrackerWrapper = (BrowserTrackerWrapper)obj;

		if (Objects.equals(_browserTracker,
					browserTrackerWrapper._browserTracker)) {
			return true;
		}

		return false;
	}

	@Override
	public BrowserTracker getWrappedModel() {
		return _browserTracker;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _browserTracker.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _browserTracker.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_browserTracker.resetOriginalValues();
	}

	private final BrowserTracker _browserTracker;
}