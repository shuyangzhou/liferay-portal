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

package com.liferay.changeset.model;

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
 * This class is a wrapper for {@link ChangesetCollection}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ChangesetCollection
 * @generated
 */
@ProviderType
public class ChangesetCollectionWrapper implements ChangesetCollection,
	ModelWrapper<ChangesetCollection> {
	public ChangesetCollectionWrapper(ChangesetCollection changesetCollection) {
		_changesetCollection = changesetCollection;
	}

	@Override
	public Class<?> getModelClass() {
		return ChangesetCollection.class;
	}

	@Override
	public String getModelClassName() {
		return ChangesetCollection.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<ChangesetCollection, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<ChangesetCollection, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<ChangesetCollection, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<ChangesetCollection, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<ChangesetCollection, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<ChangesetCollection, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<ChangesetCollection, Object>> getAttributeGetters() {
		return _changesetCollection.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<ChangesetCollection, Object>> getAttributeSetters() {
		return _changesetCollection.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new ChangesetCollectionWrapper((ChangesetCollection)_changesetCollection.clone());
	}

	@Override
	public int compareTo(ChangesetCollection changesetCollection) {
		return _changesetCollection.compareTo(changesetCollection);
	}

	/**
	* Returns the changeset collection ID of this changeset collection.
	*
	* @return the changeset collection ID of this changeset collection
	*/
	@Override
	public long getChangesetCollectionId() {
		return _changesetCollection.getChangesetCollectionId();
	}

	/**
	* Returns the company ID of this changeset collection.
	*
	* @return the company ID of this changeset collection
	*/
	@Override
	public long getCompanyId() {
		return _changesetCollection.getCompanyId();
	}

	/**
	* Returns the create date of this changeset collection.
	*
	* @return the create date of this changeset collection
	*/
	@Override
	public Date getCreateDate() {
		return _changesetCollection.getCreateDate();
	}

	/**
	* Returns the description of this changeset collection.
	*
	* @return the description of this changeset collection
	*/
	@Override
	public String getDescription() {
		return _changesetCollection.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _changesetCollection.getExpandoBridge();
	}

	/**
	* Returns the group ID of this changeset collection.
	*
	* @return the group ID of this changeset collection
	*/
	@Override
	public long getGroupId() {
		return _changesetCollection.getGroupId();
	}

	/**
	* Returns the modified date of this changeset collection.
	*
	* @return the modified date of this changeset collection
	*/
	@Override
	public Date getModifiedDate() {
		return _changesetCollection.getModifiedDate();
	}

	/**
	* Returns the name of this changeset collection.
	*
	* @return the name of this changeset collection
	*/
	@Override
	public String getName() {
		return _changesetCollection.getName();
	}

	/**
	* Returns the primary key of this changeset collection.
	*
	* @return the primary key of this changeset collection
	*/
	@Override
	public long getPrimaryKey() {
		return _changesetCollection.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _changesetCollection.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this changeset collection.
	*
	* @return the user ID of this changeset collection
	*/
	@Override
	public long getUserId() {
		return _changesetCollection.getUserId();
	}

	/**
	* Returns the user name of this changeset collection.
	*
	* @return the user name of this changeset collection
	*/
	@Override
	public String getUserName() {
		return _changesetCollection.getUserName();
	}

	/**
	* Returns the user uuid of this changeset collection.
	*
	* @return the user uuid of this changeset collection
	*/
	@Override
	public String getUserUuid() {
		return _changesetCollection.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _changesetCollection.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _changesetCollection.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _changesetCollection.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _changesetCollection.isNew();
	}

	@Override
	public void persist() {
		_changesetCollection.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_changesetCollection.setCachedModel(cachedModel);
	}

	/**
	* Sets the changeset collection ID of this changeset collection.
	*
	* @param changesetCollectionId the changeset collection ID of this changeset collection
	*/
	@Override
	public void setChangesetCollectionId(long changesetCollectionId) {
		_changesetCollection.setChangesetCollectionId(changesetCollectionId);
	}

	/**
	* Sets the company ID of this changeset collection.
	*
	* @param companyId the company ID of this changeset collection
	*/
	@Override
	public void setCompanyId(long companyId) {
		_changesetCollection.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this changeset collection.
	*
	* @param createDate the create date of this changeset collection
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_changesetCollection.setCreateDate(createDate);
	}

	/**
	* Sets the description of this changeset collection.
	*
	* @param description the description of this changeset collection
	*/
	@Override
	public void setDescription(String description) {
		_changesetCollection.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_changesetCollection.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_changesetCollection.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_changesetCollection.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this changeset collection.
	*
	* @param groupId the group ID of this changeset collection
	*/
	@Override
	public void setGroupId(long groupId) {
		_changesetCollection.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this changeset collection.
	*
	* @param modifiedDate the modified date of this changeset collection
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_changesetCollection.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this changeset collection.
	*
	* @param name the name of this changeset collection
	*/
	@Override
	public void setName(String name) {
		_changesetCollection.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_changesetCollection.setNew(n);
	}

	/**
	* Sets the primary key of this changeset collection.
	*
	* @param primaryKey the primary key of this changeset collection
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_changesetCollection.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_changesetCollection.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this changeset collection.
	*
	* @param userId the user ID of this changeset collection
	*/
	@Override
	public void setUserId(long userId) {
		_changesetCollection.setUserId(userId);
	}

	/**
	* Sets the user name of this changeset collection.
	*
	* @param userName the user name of this changeset collection
	*/
	@Override
	public void setUserName(String userName) {
		_changesetCollection.setUserName(userName);
	}

	/**
	* Sets the user uuid of this changeset collection.
	*
	* @param userUuid the user uuid of this changeset collection
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_changesetCollection.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<ChangesetCollection> toCacheModel() {
		return _changesetCollection.toCacheModel();
	}

	@Override
	public ChangesetCollection toEscapedModel() {
		return new ChangesetCollectionWrapper(_changesetCollection.toEscapedModel());
	}

	@Override
	public String toString() {
		return _changesetCollection.toString();
	}

	@Override
	public ChangesetCollection toUnescapedModel() {
		return new ChangesetCollectionWrapper(_changesetCollection.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _changesetCollection.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ChangesetCollectionWrapper)) {
			return false;
		}

		ChangesetCollectionWrapper changesetCollectionWrapper = (ChangesetCollectionWrapper)obj;

		if (Objects.equals(_changesetCollection,
					changesetCollectionWrapper._changesetCollection)) {
			return true;
		}

		return false;
	}

	@Override
	public ChangesetCollection getWrappedModel() {
		return _changesetCollection;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _changesetCollection.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _changesetCollection.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_changesetCollection.resetOriginalValues();
	}

	private final ChangesetCollection _changesetCollection;
}