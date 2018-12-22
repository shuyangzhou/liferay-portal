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

package com.liferay.sync.model;

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
 * This class is a wrapper for {@link SyncDLObject}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SyncDLObject
 * @generated
 */
@ProviderType
public class SyncDLObjectWrapper implements SyncDLObject,
	ModelWrapper<SyncDLObject> {
	public SyncDLObjectWrapper(SyncDLObject syncDLObject) {
		_syncDLObject = syncDLObject;
	}

	@Override
	public Class<?> getModelClass() {
		return SyncDLObject.class;
	}

	@Override
	public String getModelClassName() {
		return SyncDLObject.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SyncDLObject, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<SyncDLObject, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<SyncDLObject, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SyncDLObject, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<SyncDLObject, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<SyncDLObject, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<SyncDLObject, Object>> getAttributeGetters() {
		return _syncDLObject.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<SyncDLObject, Object>> getAttributeSetters() {
		return _syncDLObject.getAttributeSetters();
	}

	@Override
	public String buildTreePath() {
		return _syncDLObject.buildTreePath();
	}

	@Override
	public Object clone() {
		return new SyncDLObjectWrapper((SyncDLObject)_syncDLObject.clone());
	}

	@Override
	public int compareTo(SyncDLObject syncDLObject) {
		return _syncDLObject.compareTo(syncDLObject);
	}

	/**
	* Returns the change log of this sync dl object.
	*
	* @return the change log of this sync dl object
	*/
	@Override
	public String getChangeLog() {
		return _syncDLObject.getChangeLog();
	}

	/**
	* Returns the checksum of this sync dl object.
	*
	* @return the checksum of this sync dl object
	*/
	@Override
	public String getChecksum() {
		return _syncDLObject.getChecksum();
	}

	/**
	* Returns the company ID of this sync dl object.
	*
	* @return the company ID of this sync dl object
	*/
	@Override
	public long getCompanyId() {
		return _syncDLObject.getCompanyId();
	}

	/**
	* Returns the create time of this sync dl object.
	*
	* @return the create time of this sync dl object
	*/
	@Override
	public long getCreateTime() {
		return _syncDLObject.getCreateTime();
	}

	/**
	* Returns the description of this sync dl object.
	*
	* @return the description of this sync dl object
	*/
	@Override
	public String getDescription() {
		return _syncDLObject.getDescription();
	}

	/**
	* Returns the event of this sync dl object.
	*
	* @return the event of this sync dl object
	*/
	@Override
	public String getEvent() {
		return _syncDLObject.getEvent();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _syncDLObject.getExpandoBridge();
	}

	/**
	* Returns the extension of this sync dl object.
	*
	* @return the extension of this sync dl object
	*/
	@Override
	public String getExtension() {
		return _syncDLObject.getExtension();
	}

	/**
	* Returns the extra settings of this sync dl object.
	*
	* @return the extra settings of this sync dl object
	*/
	@Override
	public String getExtraSettings() {
		return _syncDLObject.getExtraSettings();
	}

	/**
	* Returns the lan token key of this sync dl object.
	*
	* @return the lan token key of this sync dl object
	*/
	@Override
	public String getLanTokenKey() {
		return _syncDLObject.getLanTokenKey();
	}

	/**
	* Returns the last permission change date of this sync dl object.
	*
	* @return the last permission change date of this sync dl object
	*/
	@Override
	public Date getLastPermissionChangeDate() {
		return _syncDLObject.getLastPermissionChangeDate();
	}

	/**
	* Returns the lock expiration date of this sync dl object.
	*
	* @return the lock expiration date of this sync dl object
	*/
	@Override
	public Date getLockExpirationDate() {
		return _syncDLObject.getLockExpirationDate();
	}

	/**
	* Returns the lock user ID of this sync dl object.
	*
	* @return the lock user ID of this sync dl object
	*/
	@Override
	public long getLockUserId() {
		return _syncDLObject.getLockUserId();
	}

	/**
	* Returns the lock user name of this sync dl object.
	*
	* @return the lock user name of this sync dl object
	*/
	@Override
	public String getLockUserName() {
		return _syncDLObject.getLockUserName();
	}

	/**
	* Returns the lock user uuid of this sync dl object.
	*
	* @return the lock user uuid of this sync dl object
	*/
	@Override
	public String getLockUserUuid() {
		return _syncDLObject.getLockUserUuid();
	}

	/**
	* Returns the mime type of this sync dl object.
	*
	* @return the mime type of this sync dl object
	*/
	@Override
	public String getMimeType() {
		return _syncDLObject.getMimeType();
	}

	/**
	* Returns the modified time of this sync dl object.
	*
	* @return the modified time of this sync dl object
	*/
	@Override
	public long getModifiedTime() {
		return _syncDLObject.getModifiedTime();
	}

	/**
	* Returns the name of this sync dl object.
	*
	* @return the name of this sync dl object
	*/
	@Override
	public String getName() {
		return _syncDLObject.getName();
	}

	/**
	* Returns the parent folder ID of this sync dl object.
	*
	* @return the parent folder ID of this sync dl object
	*/
	@Override
	public long getParentFolderId() {
		return _syncDLObject.getParentFolderId();
	}

	/**
	* Returns the primary key of this sync dl object.
	*
	* @return the primary key of this sync dl object
	*/
	@Override
	public long getPrimaryKey() {
		return _syncDLObject.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _syncDLObject.getPrimaryKeyObj();
	}

	/**
	* Returns the repository ID of this sync dl object.
	*
	* @return the repository ID of this sync dl object
	*/
	@Override
	public long getRepositoryId() {
		return _syncDLObject.getRepositoryId();
	}

	/**
	* Returns the size of this sync dl object.
	*
	* @return the size of this sync dl object
	*/
	@Override
	public long getSize() {
		return _syncDLObject.getSize();
	}

	/**
	* Returns the sync dl object ID of this sync dl object.
	*
	* @return the sync dl object ID of this sync dl object
	*/
	@Override
	public long getSyncDLObjectId() {
		return _syncDLObject.getSyncDLObjectId();
	}

	/**
	* Returns the tree path of this sync dl object.
	*
	* @return the tree path of this sync dl object
	*/
	@Override
	public String getTreePath() {
		return _syncDLObject.getTreePath();
	}

	/**
	* Returns the type of this sync dl object.
	*
	* @return the type of this sync dl object
	*/
	@Override
	public String getType() {
		return _syncDLObject.getType();
	}

	/**
	* Returns the type pk of this sync dl object.
	*
	* @return the type pk of this sync dl object
	*/
	@Override
	public long getTypePK() {
		return _syncDLObject.getTypePK();
	}

	/**
	* Returns the type uuid of this sync dl object.
	*
	* @return the type uuid of this sync dl object
	*/
	@Override
	public String getTypeUuid() {
		return _syncDLObject.getTypeUuid();
	}

	/**
	* Returns the user ID of this sync dl object.
	*
	* @return the user ID of this sync dl object
	*/
	@Override
	public long getUserId() {
		return _syncDLObject.getUserId();
	}

	/**
	* Returns the user name of this sync dl object.
	*
	* @return the user name of this sync dl object
	*/
	@Override
	public String getUserName() {
		return _syncDLObject.getUserName();
	}

	/**
	* Returns the user uuid of this sync dl object.
	*
	* @return the user uuid of this sync dl object
	*/
	@Override
	public String getUserUuid() {
		return _syncDLObject.getUserUuid();
	}

	/**
	* Returns the version of this sync dl object.
	*
	* @return the version of this sync dl object
	*/
	@Override
	public String getVersion() {
		return _syncDLObject.getVersion();
	}

	/**
	* Returns the version ID of this sync dl object.
	*
	* @return the version ID of this sync dl object
	*/
	@Override
	public long getVersionId() {
		return _syncDLObject.getVersionId();
	}

	@Override
	public int hashCode() {
		return _syncDLObject.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _syncDLObject.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _syncDLObject.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _syncDLObject.isNew();
	}

	@Override
	public void persist() {
		_syncDLObject.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_syncDLObject.setCachedModel(cachedModel);
	}

	/**
	* Sets the change log of this sync dl object.
	*
	* @param changeLog the change log of this sync dl object
	*/
	@Override
	public void setChangeLog(String changeLog) {
		_syncDLObject.setChangeLog(changeLog);
	}

	/**
	* Sets the checksum of this sync dl object.
	*
	* @param checksum the checksum of this sync dl object
	*/
	@Override
	public void setChecksum(String checksum) {
		_syncDLObject.setChecksum(checksum);
	}

	/**
	* Sets the company ID of this sync dl object.
	*
	* @param companyId the company ID of this sync dl object
	*/
	@Override
	public void setCompanyId(long companyId) {
		_syncDLObject.setCompanyId(companyId);
	}

	@Override
	public void setCreateDate(Date createDate) {
		_syncDLObject.setCreateDate(createDate);
	}

	/**
	* Sets the create time of this sync dl object.
	*
	* @param createTime the create time of this sync dl object
	*/
	@Override
	public void setCreateTime(long createTime) {
		_syncDLObject.setCreateTime(createTime);
	}

	/**
	* Sets the description of this sync dl object.
	*
	* @param description the description of this sync dl object
	*/
	@Override
	public void setDescription(String description) {
		_syncDLObject.setDescription(description);
	}

	/**
	* Sets the event of this sync dl object.
	*
	* @param event the event of this sync dl object
	*/
	@Override
	public void setEvent(String event) {
		_syncDLObject.setEvent(event);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_syncDLObject.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_syncDLObject.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_syncDLObject.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the extension of this sync dl object.
	*
	* @param extension the extension of this sync dl object
	*/
	@Override
	public void setExtension(String extension) {
		_syncDLObject.setExtension(extension);
	}

	/**
	* Sets the extra settings of this sync dl object.
	*
	* @param extraSettings the extra settings of this sync dl object
	*/
	@Override
	public void setExtraSettings(String extraSettings) {
		_syncDLObject.setExtraSettings(extraSettings);
	}

	/**
	* Sets the lan token key of this sync dl object.
	*
	* @param lanTokenKey the lan token key of this sync dl object
	*/
	@Override
	public void setLanTokenKey(String lanTokenKey) {
		_syncDLObject.setLanTokenKey(lanTokenKey);
	}

	/**
	* Sets the last permission change date of this sync dl object.
	*
	* @param lastPermissionChangeDate the last permission change date of this sync dl object
	*/
	@Override
	public void setLastPermissionChangeDate(Date lastPermissionChangeDate) {
		_syncDLObject.setLastPermissionChangeDate(lastPermissionChangeDate);
	}

	/**
	* Sets the lock expiration date of this sync dl object.
	*
	* @param lockExpirationDate the lock expiration date of this sync dl object
	*/
	@Override
	public void setLockExpirationDate(Date lockExpirationDate) {
		_syncDLObject.setLockExpirationDate(lockExpirationDate);
	}

	/**
	* Sets the lock user ID of this sync dl object.
	*
	* @param lockUserId the lock user ID of this sync dl object
	*/
	@Override
	public void setLockUserId(long lockUserId) {
		_syncDLObject.setLockUserId(lockUserId);
	}

	/**
	* Sets the lock user name of this sync dl object.
	*
	* @param lockUserName the lock user name of this sync dl object
	*/
	@Override
	public void setLockUserName(String lockUserName) {
		_syncDLObject.setLockUserName(lockUserName);
	}

	/**
	* Sets the lock user uuid of this sync dl object.
	*
	* @param lockUserUuid the lock user uuid of this sync dl object
	*/
	@Override
	public void setLockUserUuid(String lockUserUuid) {
		_syncDLObject.setLockUserUuid(lockUserUuid);
	}

	/**
	* Sets the mime type of this sync dl object.
	*
	* @param mimeType the mime type of this sync dl object
	*/
	@Override
	public void setMimeType(String mimeType) {
		_syncDLObject.setMimeType(mimeType);
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_syncDLObject.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the modified time of this sync dl object.
	*
	* @param modifiedTime the modified time of this sync dl object
	*/
	@Override
	public void setModifiedTime(long modifiedTime) {
		_syncDLObject.setModifiedTime(modifiedTime);
	}

	/**
	* Sets the name of this sync dl object.
	*
	* @param name the name of this sync dl object
	*/
	@Override
	public void setName(String name) {
		_syncDLObject.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_syncDLObject.setNew(n);
	}

	/**
	* Sets the parent folder ID of this sync dl object.
	*
	* @param parentFolderId the parent folder ID of this sync dl object
	*/
	@Override
	public void setParentFolderId(long parentFolderId) {
		_syncDLObject.setParentFolderId(parentFolderId);
	}

	/**
	* Sets the primary key of this sync dl object.
	*
	* @param primaryKey the primary key of this sync dl object
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_syncDLObject.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_syncDLObject.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the repository ID of this sync dl object.
	*
	* @param repositoryId the repository ID of this sync dl object
	*/
	@Override
	public void setRepositoryId(long repositoryId) {
		_syncDLObject.setRepositoryId(repositoryId);
	}

	/**
	* Sets the size of this sync dl object.
	*
	* @param size the size of this sync dl object
	*/
	@Override
	public void setSize(long size) {
		_syncDLObject.setSize(size);
	}

	/**
	* Sets the sync dl object ID of this sync dl object.
	*
	* @param syncDLObjectId the sync dl object ID of this sync dl object
	*/
	@Override
	public void setSyncDLObjectId(long syncDLObjectId) {
		_syncDLObject.setSyncDLObjectId(syncDLObjectId);
	}

	/**
	* Sets the tree path of this sync dl object.
	*
	* @param treePath the tree path of this sync dl object
	*/
	@Override
	public void setTreePath(String treePath) {
		_syncDLObject.setTreePath(treePath);
	}

	/**
	* Sets the type of this sync dl object.
	*
	* @param type the type of this sync dl object
	*/
	@Override
	public void setType(String type) {
		_syncDLObject.setType(type);
	}

	/**
	* Sets the type pk of this sync dl object.
	*
	* @param typePK the type pk of this sync dl object
	*/
	@Override
	public void setTypePK(long typePK) {
		_syncDLObject.setTypePK(typePK);
	}

	/**
	* Sets the type uuid of this sync dl object.
	*
	* @param typeUuid the type uuid of this sync dl object
	*/
	@Override
	public void setTypeUuid(String typeUuid) {
		_syncDLObject.setTypeUuid(typeUuid);
	}

	/**
	* Sets the user ID of this sync dl object.
	*
	* @param userId the user ID of this sync dl object
	*/
	@Override
	public void setUserId(long userId) {
		_syncDLObject.setUserId(userId);
	}

	/**
	* Sets the user name of this sync dl object.
	*
	* @param userName the user name of this sync dl object
	*/
	@Override
	public void setUserName(String userName) {
		_syncDLObject.setUserName(userName);
	}

	/**
	* Sets the user uuid of this sync dl object.
	*
	* @param userUuid the user uuid of this sync dl object
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_syncDLObject.setUserUuid(userUuid);
	}

	/**
	* Sets the version of this sync dl object.
	*
	* @param version the version of this sync dl object
	*/
	@Override
	public void setVersion(String version) {
		_syncDLObject.setVersion(version);
	}

	/**
	* Sets the version ID of this sync dl object.
	*
	* @param versionId the version ID of this sync dl object
	*/
	@Override
	public void setVersionId(long versionId) {
		_syncDLObject.setVersionId(versionId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<SyncDLObject> toCacheModel() {
		return _syncDLObject.toCacheModel();
	}

	@Override
	public SyncDLObject toEscapedModel() {
		return new SyncDLObjectWrapper(_syncDLObject.toEscapedModel());
	}

	@Override
	public String toString() {
		return _syncDLObject.toString();
	}

	@Override
	public SyncDLObject toUnescapedModel() {
		return new SyncDLObjectWrapper(_syncDLObject.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _syncDLObject.toXmlString();
	}

	@Override
	public void updateTreePath(String treePath) {
		_syncDLObject.updateTreePath(treePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SyncDLObjectWrapper)) {
			return false;
		}

		SyncDLObjectWrapper syncDLObjectWrapper = (SyncDLObjectWrapper)obj;

		if (Objects.equals(_syncDLObject, syncDLObjectWrapper._syncDLObject)) {
			return true;
		}

		return false;
	}

	@Override
	public SyncDLObject getWrappedModel() {
		return _syncDLObject;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _syncDLObject.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _syncDLObject.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_syncDLObject.resetOriginalValues();
	}

	private final SyncDLObject _syncDLObject;
}