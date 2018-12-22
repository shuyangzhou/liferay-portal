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

package com.liferay.knowledge.base.model;

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
 * This class is a wrapper for {@link KBFolder}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KBFolder
 * @generated
 */
@ProviderType
public class KBFolderWrapper implements KBFolder, ModelWrapper<KBFolder> {
	public KBFolderWrapper(KBFolder kbFolder) {
		_kbFolder = kbFolder;
	}

	@Override
	public Class<?> getModelClass() {
		return KBFolder.class;
	}

	@Override
	public String getModelClassName() {
		return KBFolder.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KBFolder, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<KBFolder, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<KBFolder, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KBFolder, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<KBFolder, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<KBFolder, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<KBFolder, Object>> getAttributeGetters() {
		return _kbFolder.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<KBFolder, Object>> getAttributeSetters() {
		return _kbFolder.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new KBFolderWrapper((KBFolder)_kbFolder.clone());
	}

	@Override
	public int compareTo(KBFolder kbFolder) {
		return _kbFolder.compareTo(kbFolder);
	}

	@Override
	public java.util.List<Long> getAncestorKBFolderIds()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kbFolder.getAncestorKBFolderIds();
	}

	@Override
	public long getClassNameId() {
		return _kbFolder.getClassNameId();
	}

	/**
	* Returns the company ID of this kb folder.
	*
	* @return the company ID of this kb folder
	*/
	@Override
	public long getCompanyId() {
		return _kbFolder.getCompanyId();
	}

	/**
	* Returns the create date of this kb folder.
	*
	* @return the create date of this kb folder
	*/
	@Override
	public Date getCreateDate() {
		return _kbFolder.getCreateDate();
	}

	/**
	* Returns the description of this kb folder.
	*
	* @return the description of this kb folder
	*/
	@Override
	public String getDescription() {
		return _kbFolder.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _kbFolder.getExpandoBridge();
	}

	/**
	* Returns the group ID of this kb folder.
	*
	* @return the group ID of this kb folder
	*/
	@Override
	public long getGroupId() {
		return _kbFolder.getGroupId();
	}

	/**
	* Returns the kb folder ID of this kb folder.
	*
	* @return the kb folder ID of this kb folder
	*/
	@Override
	public long getKbFolderId() {
		return _kbFolder.getKbFolderId();
	}

	/**
	* Returns the last publish date of this kb folder.
	*
	* @return the last publish date of this kb folder
	*/
	@Override
	public Date getLastPublishDate() {
		return _kbFolder.getLastPublishDate();
	}

	/**
	* Returns the modified date of this kb folder.
	*
	* @return the modified date of this kb folder
	*/
	@Override
	public Date getModifiedDate() {
		return _kbFolder.getModifiedDate();
	}

	/**
	* Returns the name of this kb folder.
	*
	* @return the name of this kb folder
	*/
	@Override
	public String getName() {
		return _kbFolder.getName();
	}

	@Override
	public KBFolder getParentKBFolder()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kbFolder.getParentKBFolder();
	}

	/**
	* Returns the parent kb folder ID of this kb folder.
	*
	* @return the parent kb folder ID of this kb folder
	*/
	@Override
	public long getParentKBFolderId() {
		return _kbFolder.getParentKBFolderId();
	}

	@Override
	public String getParentTitle(java.util.Locale locale)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kbFolder.getParentTitle(locale);
	}

	/**
	* Returns the primary key of this kb folder.
	*
	* @return the primary key of this kb folder
	*/
	@Override
	public long getPrimaryKey() {
		return _kbFolder.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kbFolder.getPrimaryKeyObj();
	}

	/**
	* Returns the url title of this kb folder.
	*
	* @return the url title of this kb folder
	*/
	@Override
	public String getUrlTitle() {
		return _kbFolder.getUrlTitle();
	}

	/**
	* Returns the user ID of this kb folder.
	*
	* @return the user ID of this kb folder
	*/
	@Override
	public long getUserId() {
		return _kbFolder.getUserId();
	}

	/**
	* Returns the user name of this kb folder.
	*
	* @return the user name of this kb folder
	*/
	@Override
	public String getUserName() {
		return _kbFolder.getUserName();
	}

	/**
	* Returns the user uuid of this kb folder.
	*
	* @return the user uuid of this kb folder
	*/
	@Override
	public String getUserUuid() {
		return _kbFolder.getUserUuid();
	}

	/**
	* Returns the uuid of this kb folder.
	*
	* @return the uuid of this kb folder
	*/
	@Override
	public String getUuid() {
		return _kbFolder.getUuid();
	}

	@Override
	public int hashCode() {
		return _kbFolder.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _kbFolder.isCachedModel();
	}

	@Override
	public boolean isEmpty()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _kbFolder.isEmpty();
	}

	@Override
	public boolean isEscapedModel() {
		return _kbFolder.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _kbFolder.isNew();
	}

	@Override
	public boolean isRoot() {
		return _kbFolder.isRoot();
	}

	@Override
	public void persist() {
		_kbFolder.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_kbFolder.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this kb folder.
	*
	* @param companyId the company ID of this kb folder
	*/
	@Override
	public void setCompanyId(long companyId) {
		_kbFolder.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this kb folder.
	*
	* @param createDate the create date of this kb folder
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_kbFolder.setCreateDate(createDate);
	}

	/**
	* Sets the description of this kb folder.
	*
	* @param description the description of this kb folder
	*/
	@Override
	public void setDescription(String description) {
		_kbFolder.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_kbFolder.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_kbFolder.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_kbFolder.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this kb folder.
	*
	* @param groupId the group ID of this kb folder
	*/
	@Override
	public void setGroupId(long groupId) {
		_kbFolder.setGroupId(groupId);
	}

	/**
	* Sets the kb folder ID of this kb folder.
	*
	* @param kbFolderId the kb folder ID of this kb folder
	*/
	@Override
	public void setKbFolderId(long kbFolderId) {
		_kbFolder.setKbFolderId(kbFolderId);
	}

	/**
	* Sets the last publish date of this kb folder.
	*
	* @param lastPublishDate the last publish date of this kb folder
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_kbFolder.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this kb folder.
	*
	* @param modifiedDate the modified date of this kb folder
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_kbFolder.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this kb folder.
	*
	* @param name the name of this kb folder
	*/
	@Override
	public void setName(String name) {
		_kbFolder.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_kbFolder.setNew(n);
	}

	/**
	* Sets the parent kb folder ID of this kb folder.
	*
	* @param parentKBFolderId the parent kb folder ID of this kb folder
	*/
	@Override
	public void setParentKBFolderId(long parentKBFolderId) {
		_kbFolder.setParentKBFolderId(parentKBFolderId);
	}

	/**
	* Sets the primary key of this kb folder.
	*
	* @param primaryKey the primary key of this kb folder
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_kbFolder.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_kbFolder.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the url title of this kb folder.
	*
	* @param urlTitle the url title of this kb folder
	*/
	@Override
	public void setUrlTitle(String urlTitle) {
		_kbFolder.setUrlTitle(urlTitle);
	}

	/**
	* Sets the user ID of this kb folder.
	*
	* @param userId the user ID of this kb folder
	*/
	@Override
	public void setUserId(long userId) {
		_kbFolder.setUserId(userId);
	}

	/**
	* Sets the user name of this kb folder.
	*
	* @param userName the user name of this kb folder
	*/
	@Override
	public void setUserName(String userName) {
		_kbFolder.setUserName(userName);
	}

	/**
	* Sets the user uuid of this kb folder.
	*
	* @param userUuid the user uuid of this kb folder
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_kbFolder.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this kb folder.
	*
	* @param uuid the uuid of this kb folder
	*/
	@Override
	public void setUuid(String uuid) {
		_kbFolder.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<KBFolder> toCacheModel() {
		return _kbFolder.toCacheModel();
	}

	@Override
	public KBFolder toEscapedModel() {
		return new KBFolderWrapper(_kbFolder.toEscapedModel());
	}

	@Override
	public String toString() {
		return _kbFolder.toString();
	}

	@Override
	public KBFolder toUnescapedModel() {
		return new KBFolderWrapper(_kbFolder.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _kbFolder.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof KBFolderWrapper)) {
			return false;
		}

		KBFolderWrapper kbFolderWrapper = (KBFolderWrapper)obj;

		if (Objects.equals(_kbFolder, kbFolderWrapper._kbFolder)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _kbFolder.getStagedModelType();
	}

	@Override
	public KBFolder getWrappedModel() {
		return _kbFolder;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _kbFolder.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _kbFolder.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_kbFolder.resetOriginalValues();
	}

	private final KBFolder _kbFolder;
}