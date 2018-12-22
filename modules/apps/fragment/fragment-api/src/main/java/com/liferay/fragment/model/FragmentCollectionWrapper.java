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

package com.liferay.fragment.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.exportimport.kernel.lar.StagedModelType;

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
 * This class is a wrapper for {@link FragmentCollection}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentCollection
 * @generated
 */
@ProviderType
public class FragmentCollectionWrapper implements FragmentCollection,
	ModelWrapper<FragmentCollection> {
	public FragmentCollectionWrapper(FragmentCollection fragmentCollection) {
		_fragmentCollection = fragmentCollection;
	}

	@Override
	public Class<?> getModelClass() {
		return FragmentCollection.class;
	}

	@Override
	public String getModelClassName() {
		return FragmentCollection.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<FragmentCollection, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<FragmentCollection, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<FragmentCollection, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<FragmentCollection, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<FragmentCollection, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<FragmentCollection, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<FragmentCollection, Object>> getAttributeGetters() {
		return _fragmentCollection.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<FragmentCollection, Object>> getAttributeSetters() {
		return _fragmentCollection.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new FragmentCollectionWrapper((FragmentCollection)_fragmentCollection.clone());
	}

	@Override
	public int compareTo(FragmentCollection fragmentCollection) {
		return _fragmentCollection.compareTo(fragmentCollection);
	}

	/**
	* Returns the company ID of this fragment collection.
	*
	* @return the company ID of this fragment collection
	*/
	@Override
	public long getCompanyId() {
		return _fragmentCollection.getCompanyId();
	}

	/**
	* Returns the create date of this fragment collection.
	*
	* @return the create date of this fragment collection
	*/
	@Override
	public Date getCreateDate() {
		return _fragmentCollection.getCreateDate();
	}

	/**
	* Returns the description of this fragment collection.
	*
	* @return the description of this fragment collection
	*/
	@Override
	public String getDescription() {
		return _fragmentCollection.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _fragmentCollection.getExpandoBridge();
	}

	/**
	* Returns the fragment collection ID of this fragment collection.
	*
	* @return the fragment collection ID of this fragment collection
	*/
	@Override
	public long getFragmentCollectionId() {
		return _fragmentCollection.getFragmentCollectionId();
	}

	/**
	* Returns the fragment collection key of this fragment collection.
	*
	* @return the fragment collection key of this fragment collection
	*/
	@Override
	public String getFragmentCollectionKey() {
		return _fragmentCollection.getFragmentCollectionKey();
	}

	/**
	* Returns the group ID of this fragment collection.
	*
	* @return the group ID of this fragment collection
	*/
	@Override
	public long getGroupId() {
		return _fragmentCollection.getGroupId();
	}

	/**
	* Returns the last publish date of this fragment collection.
	*
	* @return the last publish date of this fragment collection
	*/
	@Override
	public Date getLastPublishDate() {
		return _fragmentCollection.getLastPublishDate();
	}

	/**
	* Returns the modified date of this fragment collection.
	*
	* @return the modified date of this fragment collection
	*/
	@Override
	public Date getModifiedDate() {
		return _fragmentCollection.getModifiedDate();
	}

	/**
	* Returns the name of this fragment collection.
	*
	* @return the name of this fragment collection
	*/
	@Override
	public String getName() {
		return _fragmentCollection.getName();
	}

	/**
	* Returns the primary key of this fragment collection.
	*
	* @return the primary key of this fragment collection
	*/
	@Override
	public long getPrimaryKey() {
		return _fragmentCollection.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fragmentCollection.getPrimaryKeyObj();
	}

	@Override
	public long getResourcesFolderId()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _fragmentCollection.getResourcesFolderId();
	}

	/**
	* Returns the user ID of this fragment collection.
	*
	* @return the user ID of this fragment collection
	*/
	@Override
	public long getUserId() {
		return _fragmentCollection.getUserId();
	}

	/**
	* Returns the user name of this fragment collection.
	*
	* @return the user name of this fragment collection
	*/
	@Override
	public String getUserName() {
		return _fragmentCollection.getUserName();
	}

	/**
	* Returns the user uuid of this fragment collection.
	*
	* @return the user uuid of this fragment collection
	*/
	@Override
	public String getUserUuid() {
		return _fragmentCollection.getUserUuid();
	}

	/**
	* Returns the uuid of this fragment collection.
	*
	* @return the uuid of this fragment collection
	*/
	@Override
	public String getUuid() {
		return _fragmentCollection.getUuid();
	}

	@Override
	public int hashCode() {
		return _fragmentCollection.hashCode();
	}

	@Override
	public boolean hasResources()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _fragmentCollection.hasResources();
	}

	@Override
	public boolean isCachedModel() {
		return _fragmentCollection.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _fragmentCollection.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _fragmentCollection.isNew();
	}

	@Override
	public void persist() {
		_fragmentCollection.persist();
	}

	@Override
	public void populateZipWriter(
		com.liferay.portal.kernel.zip.ZipWriter zipWriter)
		throws Exception {
		_fragmentCollection.populateZipWriter(zipWriter);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_fragmentCollection.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this fragment collection.
	*
	* @param companyId the company ID of this fragment collection
	*/
	@Override
	public void setCompanyId(long companyId) {
		_fragmentCollection.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this fragment collection.
	*
	* @param createDate the create date of this fragment collection
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_fragmentCollection.setCreateDate(createDate);
	}

	/**
	* Sets the description of this fragment collection.
	*
	* @param description the description of this fragment collection
	*/
	@Override
	public void setDescription(String description) {
		_fragmentCollection.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_fragmentCollection.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_fragmentCollection.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_fragmentCollection.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the fragment collection ID of this fragment collection.
	*
	* @param fragmentCollectionId the fragment collection ID of this fragment collection
	*/
	@Override
	public void setFragmentCollectionId(long fragmentCollectionId) {
		_fragmentCollection.setFragmentCollectionId(fragmentCollectionId);
	}

	/**
	* Sets the fragment collection key of this fragment collection.
	*
	* @param fragmentCollectionKey the fragment collection key of this fragment collection
	*/
	@Override
	public void setFragmentCollectionKey(String fragmentCollectionKey) {
		_fragmentCollection.setFragmentCollectionKey(fragmentCollectionKey);
	}

	/**
	* Sets the group ID of this fragment collection.
	*
	* @param groupId the group ID of this fragment collection
	*/
	@Override
	public void setGroupId(long groupId) {
		_fragmentCollection.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this fragment collection.
	*
	* @param lastPublishDate the last publish date of this fragment collection
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_fragmentCollection.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this fragment collection.
	*
	* @param modifiedDate the modified date of this fragment collection
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_fragmentCollection.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this fragment collection.
	*
	* @param name the name of this fragment collection
	*/
	@Override
	public void setName(String name) {
		_fragmentCollection.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_fragmentCollection.setNew(n);
	}

	/**
	* Sets the primary key of this fragment collection.
	*
	* @param primaryKey the primary key of this fragment collection
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_fragmentCollection.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_fragmentCollection.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this fragment collection.
	*
	* @param userId the user ID of this fragment collection
	*/
	@Override
	public void setUserId(long userId) {
		_fragmentCollection.setUserId(userId);
	}

	/**
	* Sets the user name of this fragment collection.
	*
	* @param userName the user name of this fragment collection
	*/
	@Override
	public void setUserName(String userName) {
		_fragmentCollection.setUserName(userName);
	}

	/**
	* Sets the user uuid of this fragment collection.
	*
	* @param userUuid the user uuid of this fragment collection
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_fragmentCollection.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this fragment collection.
	*
	* @param uuid the uuid of this fragment collection
	*/
	@Override
	public void setUuid(String uuid) {
		_fragmentCollection.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<FragmentCollection> toCacheModel() {
		return _fragmentCollection.toCacheModel();
	}

	@Override
	public FragmentCollection toEscapedModel() {
		return new FragmentCollectionWrapper(_fragmentCollection.toEscapedModel());
	}

	@Override
	public String toString() {
		return _fragmentCollection.toString();
	}

	@Override
	public FragmentCollection toUnescapedModel() {
		return new FragmentCollectionWrapper(_fragmentCollection.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _fragmentCollection.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FragmentCollectionWrapper)) {
			return false;
		}

		FragmentCollectionWrapper fragmentCollectionWrapper = (FragmentCollectionWrapper)obj;

		if (Objects.equals(_fragmentCollection,
					fragmentCollectionWrapper._fragmentCollection)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _fragmentCollection.getStagedModelType();
	}

	@Override
	public FragmentCollection getWrappedModel() {
		return _fragmentCollection;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _fragmentCollection.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _fragmentCollection.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_fragmentCollection.resetOriginalValues();
	}

	private final FragmentCollection _fragmentCollection;
}