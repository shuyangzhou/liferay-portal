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

package com.liferay.change.tracking.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link CTCollection}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CTCollection
 * @generated
 */
@ProviderType
public class CTCollectionWrapper implements CTCollection,
	ModelWrapper<CTCollection> {
	public CTCollectionWrapper(CTCollection ctCollection) {
		_ctCollection = ctCollection;
	}

	@Override
	public Class<?> getModelClass() {
		return CTCollection.class;
	}

	@Override
	public String getModelClassName() {
		return CTCollection.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<CTCollection, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<CTCollection, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<CTCollection, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<CTCollection, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<CTCollection, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<CTCollection, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<CTCollection, Object>> getAttributeGetters() {
		return _ctCollection.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<CTCollection, Object>> getAttributeSetters() {
		return _ctCollection.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new CTCollectionWrapper((CTCollection)_ctCollection.clone());
	}

	@Override
	public int compareTo(CTCollection ctCollection) {
		return _ctCollection.compareTo(ctCollection);
	}

	/**
	* Returns the company ID of this ct collection.
	*
	* @return the company ID of this ct collection
	*/
	@Override
	public long getCompanyId() {
		return _ctCollection.getCompanyId();
	}

	/**
	* Returns the create date of this ct collection.
	*
	* @return the create date of this ct collection
	*/
	@Override
	public Date getCreateDate() {
		return _ctCollection.getCreateDate();
	}

	/**
	* Returns the ct collection ID of this ct collection.
	*
	* @return the ct collection ID of this ct collection
	*/
	@Override
	public long getCtCollectionId() {
		return _ctCollection.getCtCollectionId();
	}

	/**
	* Returns the description of this ct collection.
	*
	* @return the description of this ct collection
	*/
	@Override
	public String getDescription() {
		return _ctCollection.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _ctCollection.getExpandoBridge();
	}

	/**
	* Returns the modified date of this ct collection.
	*
	* @return the modified date of this ct collection
	*/
	@Override
	public Date getModifiedDate() {
		return _ctCollection.getModifiedDate();
	}

	/**
	* Returns the name of this ct collection.
	*
	* @return the name of this ct collection
	*/
	@Override
	public String getName() {
		return _ctCollection.getName();
	}

	/**
	* Returns the primary key of this ct collection.
	*
	* @return the primary key of this ct collection
	*/
	@Override
	public long getPrimaryKey() {
		return _ctCollection.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ctCollection.getPrimaryKeyObj();
	}

	/**
	* Returns the status of this ct collection.
	*
	* @return the status of this ct collection
	*/
	@Override
	public int getStatus() {
		return _ctCollection.getStatus();
	}

	/**
	* Returns the status by user ID of this ct collection.
	*
	* @return the status by user ID of this ct collection
	*/
	@Override
	public long getStatusByUserId() {
		return _ctCollection.getStatusByUserId();
	}

	/**
	* Returns the status by user name of this ct collection.
	*
	* @return the status by user name of this ct collection
	*/
	@Override
	public String getStatusByUserName() {
		return _ctCollection.getStatusByUserName();
	}

	/**
	* Returns the status by user uuid of this ct collection.
	*
	* @return the status by user uuid of this ct collection
	*/
	@Override
	public String getStatusByUserUuid() {
		return _ctCollection.getStatusByUserUuid();
	}

	/**
	* Returns the status date of this ct collection.
	*
	* @return the status date of this ct collection
	*/
	@Override
	public Date getStatusDate() {
		return _ctCollection.getStatusDate();
	}

	/**
	* Returns the user ID of this ct collection.
	*
	* @return the user ID of this ct collection
	*/
	@Override
	public long getUserId() {
		return _ctCollection.getUserId();
	}

	/**
	* Returns the user name of this ct collection.
	*
	* @return the user name of this ct collection
	*/
	@Override
	public String getUserName() {
		return _ctCollection.getUserName();
	}

	/**
	* Returns the user uuid of this ct collection.
	*
	* @return the user uuid of this ct collection
	*/
	@Override
	public String getUserUuid() {
		return _ctCollection.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _ctCollection.hashCode();
	}

	/**
	* Returns <code>true</code> if this ct collection is approved.
	*
	* @return <code>true</code> if this ct collection is approved; <code>false</code> otherwise
	*/
	@Override
	public boolean isApproved() {
		return _ctCollection.isApproved();
	}

	@Override
	public boolean isCachedModel() {
		return _ctCollection.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this ct collection is denied.
	*
	* @return <code>true</code> if this ct collection is denied; <code>false</code> otherwise
	*/
	@Override
	public boolean isDenied() {
		return _ctCollection.isDenied();
	}

	/**
	* Returns <code>true</code> if this ct collection is a draft.
	*
	* @return <code>true</code> if this ct collection is a draft; <code>false</code> otherwise
	*/
	@Override
	public boolean isDraft() {
		return _ctCollection.isDraft();
	}

	@Override
	public boolean isEscapedModel() {
		return _ctCollection.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this ct collection is expired.
	*
	* @return <code>true</code> if this ct collection is expired; <code>false</code> otherwise
	*/
	@Override
	public boolean isExpired() {
		return _ctCollection.isExpired();
	}

	/**
	* Returns <code>true</code> if this ct collection is inactive.
	*
	* @return <code>true</code> if this ct collection is inactive; <code>false</code> otherwise
	*/
	@Override
	public boolean isInactive() {
		return _ctCollection.isInactive();
	}

	/**
	* Returns <code>true</code> if this ct collection is incomplete.
	*
	* @return <code>true</code> if this ct collection is incomplete; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomplete() {
		return _ctCollection.isIncomplete();
	}

	@Override
	public boolean isNew() {
		return _ctCollection.isNew();
	}

	/**
	* Returns <code>true</code> if this ct collection is pending.
	*
	* @return <code>true</code> if this ct collection is pending; <code>false</code> otherwise
	*/
	@Override
	public boolean isPending() {
		return _ctCollection.isPending();
	}

	/**
	* Returns <code>true</code> if this ct collection is scheduled.
	*
	* @return <code>true</code> if this ct collection is scheduled; <code>false</code> otherwise
	*/
	@Override
	public boolean isScheduled() {
		return _ctCollection.isScheduled();
	}

	@Override
	public void persist() {
		_ctCollection.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_ctCollection.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this ct collection.
	*
	* @param companyId the company ID of this ct collection
	*/
	@Override
	public void setCompanyId(long companyId) {
		_ctCollection.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this ct collection.
	*
	* @param createDate the create date of this ct collection
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_ctCollection.setCreateDate(createDate);
	}

	/**
	* Sets the ct collection ID of this ct collection.
	*
	* @param ctCollectionId the ct collection ID of this ct collection
	*/
	@Override
	public void setCtCollectionId(long ctCollectionId) {
		_ctCollection.setCtCollectionId(ctCollectionId);
	}

	/**
	* Sets the description of this ct collection.
	*
	* @param description the description of this ct collection
	*/
	@Override
	public void setDescription(String description) {
		_ctCollection.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_ctCollection.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_ctCollection.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_ctCollection.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the modified date of this ct collection.
	*
	* @param modifiedDate the modified date of this ct collection
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_ctCollection.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this ct collection.
	*
	* @param name the name of this ct collection
	*/
	@Override
	public void setName(String name) {
		_ctCollection.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_ctCollection.setNew(n);
	}

	/**
	* Sets the primary key of this ct collection.
	*
	* @param primaryKey the primary key of this ct collection
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_ctCollection.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_ctCollection.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the status of this ct collection.
	*
	* @param status the status of this ct collection
	*/
	@Override
	public void setStatus(int status) {
		_ctCollection.setStatus(status);
	}

	/**
	* Sets the status by user ID of this ct collection.
	*
	* @param statusByUserId the status by user ID of this ct collection
	*/
	@Override
	public void setStatusByUserId(long statusByUserId) {
		_ctCollection.setStatusByUserId(statusByUserId);
	}

	/**
	* Sets the status by user name of this ct collection.
	*
	* @param statusByUserName the status by user name of this ct collection
	*/
	@Override
	public void setStatusByUserName(String statusByUserName) {
		_ctCollection.setStatusByUserName(statusByUserName);
	}

	/**
	* Sets the status by user uuid of this ct collection.
	*
	* @param statusByUserUuid the status by user uuid of this ct collection
	*/
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		_ctCollection.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	* Sets the status date of this ct collection.
	*
	* @param statusDate the status date of this ct collection
	*/
	@Override
	public void setStatusDate(Date statusDate) {
		_ctCollection.setStatusDate(statusDate);
	}

	/**
	* Sets the user ID of this ct collection.
	*
	* @param userId the user ID of this ct collection
	*/
	@Override
	public void setUserId(long userId) {
		_ctCollection.setUserId(userId);
	}

	/**
	* Sets the user name of this ct collection.
	*
	* @param userName the user name of this ct collection
	*/
	@Override
	public void setUserName(String userName) {
		_ctCollection.setUserName(userName);
	}

	/**
	* Sets the user uuid of this ct collection.
	*
	* @param userUuid the user uuid of this ct collection
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_ctCollection.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<CTCollection> toCacheModel() {
		return _ctCollection.toCacheModel();
	}

	@Override
	public CTCollection toEscapedModel() {
		return new CTCollectionWrapper(_ctCollection.toEscapedModel());
	}

	@Override
	public String toString() {
		return _ctCollection.toString();
	}

	@Override
	public CTCollection toUnescapedModel() {
		return new CTCollectionWrapper(_ctCollection.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _ctCollection.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CTCollectionWrapper)) {
			return false;
		}

		CTCollectionWrapper ctCollectionWrapper = (CTCollectionWrapper)obj;

		if (Objects.equals(_ctCollection, ctCollectionWrapper._ctCollection)) {
			return true;
		}

		return false;
	}

	@Override
	public CTCollection getWrappedModel() {
		return _ctCollection;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _ctCollection.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _ctCollection.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_ctCollection.resetOriginalValues();
	}

	private final CTCollection _ctCollection;
}