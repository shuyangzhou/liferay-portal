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

import com.liferay.exportimport.kernel.lar.StagedModelType;

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
 * This class is a wrapper for {@link Repository}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Repository
 * @generated
 */
@ProviderType
public class RepositoryWrapper implements Repository, ModelWrapper<Repository> {
	public RepositoryWrapper(Repository repository) {
		_repository = repository;
	}

	@Override
	public Class<?> getModelClass() {
		return Repository.class;
	}

	@Override
	public String getModelClassName() {
		return Repository.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Repository, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Repository, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Repository, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Repository, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Repository, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Repository, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Repository, Object>> getAttributeGetters() {
		return _repository.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Repository, Object>> getAttributeSetters() {
		return _repository.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new RepositoryWrapper((Repository)_repository.clone());
	}

	@Override
	public int compareTo(Repository repository) {
		return _repository.compareTo(repository);
	}

	/**
	* Returns the fully qualified class name of this repository.
	*
	* @return the fully qualified class name of this repository
	*/
	@Override
	public String getClassName() {
		return _repository.getClassName();
	}

	/**
	* Returns the class name ID of this repository.
	*
	* @return the class name ID of this repository
	*/
	@Override
	public long getClassNameId() {
		return _repository.getClassNameId();
	}

	/**
	* Returns the company ID of this repository.
	*
	* @return the company ID of this repository
	*/
	@Override
	public long getCompanyId() {
		return _repository.getCompanyId();
	}

	/**
	* Returns the create date of this repository.
	*
	* @return the create date of this repository
	*/
	@Override
	public Date getCreateDate() {
		return _repository.getCreateDate();
	}

	/**
	* Returns the description of this repository.
	*
	* @return the description of this repository
	*/
	@Override
	public String getDescription() {
		return _repository.getDescription();
	}

	/**
	* Returns the dl folder ID of this repository.
	*
	* @return the dl folder ID of this repository
	*/
	@Override
	public long getDlFolderId() {
		return _repository.getDlFolderId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _repository.getExpandoBridge();
	}

	/**
	* Returns the group ID of this repository.
	*
	* @return the group ID of this repository
	*/
	@Override
	public long getGroupId() {
		return _repository.getGroupId();
	}

	/**
	* Returns the last publish date of this repository.
	*
	* @return the last publish date of this repository
	*/
	@Override
	public Date getLastPublishDate() {
		return _repository.getLastPublishDate();
	}

	/**
	* Returns the modified date of this repository.
	*
	* @return the modified date of this repository
	*/
	@Override
	public Date getModifiedDate() {
		return _repository.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this repository.
	*
	* @return the mvcc version of this repository
	*/
	@Override
	public long getMvccVersion() {
		return _repository.getMvccVersion();
	}

	/**
	* Returns the name of this repository.
	*
	* @return the name of this repository
	*/
	@Override
	public String getName() {
		return _repository.getName();
	}

	/**
	* Returns the portlet ID of this repository.
	*
	* @return the portlet ID of this repository
	*/
	@Override
	public String getPortletId() {
		return _repository.getPortletId();
	}

	/**
	* Returns the primary key of this repository.
	*
	* @return the primary key of this repository
	*/
	@Override
	public long getPrimaryKey() {
		return _repository.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _repository.getPrimaryKeyObj();
	}

	/**
	* Returns the repository ID of this repository.
	*
	* @return the repository ID of this repository
	*/
	@Override
	public long getRepositoryId() {
		return _repository.getRepositoryId();
	}

	/**
	* Returns the type settings of this repository.
	*
	* @return the type settings of this repository
	*/
	@Override
	public String getTypeSettings() {
		return _repository.getTypeSettings();
	}

	@Override
	public com.liferay.portal.kernel.util.UnicodeProperties getTypeSettingsProperties() {
		return _repository.getTypeSettingsProperties();
	}

	/**
	* Returns the user ID of this repository.
	*
	* @return the user ID of this repository
	*/
	@Override
	public long getUserId() {
		return _repository.getUserId();
	}

	/**
	* Returns the user name of this repository.
	*
	* @return the user name of this repository
	*/
	@Override
	public String getUserName() {
		return _repository.getUserName();
	}

	/**
	* Returns the user uuid of this repository.
	*
	* @return the user uuid of this repository
	*/
	@Override
	public String getUserUuid() {
		return _repository.getUserUuid();
	}

	/**
	* Returns the uuid of this repository.
	*
	* @return the uuid of this repository
	*/
	@Override
	public String getUuid() {
		return _repository.getUuid();
	}

	@Override
	public int hashCode() {
		return _repository.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _repository.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _repository.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _repository.isNew();
	}

	@Override
	public void persist() {
		_repository.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_repository.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_repository.setClassName(className);
	}

	/**
	* Sets the class name ID of this repository.
	*
	* @param classNameId the class name ID of this repository
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_repository.setClassNameId(classNameId);
	}

	/**
	* Sets the company ID of this repository.
	*
	* @param companyId the company ID of this repository
	*/
	@Override
	public void setCompanyId(long companyId) {
		_repository.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this repository.
	*
	* @param createDate the create date of this repository
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_repository.setCreateDate(createDate);
	}

	/**
	* Sets the description of this repository.
	*
	* @param description the description of this repository
	*/
	@Override
	public void setDescription(String description) {
		_repository.setDescription(description);
	}

	/**
	* Sets the dl folder ID of this repository.
	*
	* @param dlFolderId the dl folder ID of this repository
	*/
	@Override
	public void setDlFolderId(long dlFolderId) {
		_repository.setDlFolderId(dlFolderId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_repository.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_repository.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_repository.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this repository.
	*
	* @param groupId the group ID of this repository
	*/
	@Override
	public void setGroupId(long groupId) {
		_repository.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this repository.
	*
	* @param lastPublishDate the last publish date of this repository
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_repository.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this repository.
	*
	* @param modifiedDate the modified date of this repository
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_repository.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this repository.
	*
	* @param mvccVersion the mvcc version of this repository
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_repository.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this repository.
	*
	* @param name the name of this repository
	*/
	@Override
	public void setName(String name) {
		_repository.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_repository.setNew(n);
	}

	/**
	* Sets the portlet ID of this repository.
	*
	* @param portletId the portlet ID of this repository
	*/
	@Override
	public void setPortletId(String portletId) {
		_repository.setPortletId(portletId);
	}

	/**
	* Sets the primary key of this repository.
	*
	* @param primaryKey the primary key of this repository
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_repository.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_repository.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the repository ID of this repository.
	*
	* @param repositoryId the repository ID of this repository
	*/
	@Override
	public void setRepositoryId(long repositoryId) {
		_repository.setRepositoryId(repositoryId);
	}

	/**
	* Sets the type settings of this repository.
	*
	* @param typeSettings the type settings of this repository
	*/
	@Override
	public void setTypeSettings(String typeSettings) {
		_repository.setTypeSettings(typeSettings);
	}

	@Override
	public void setTypeSettingsProperties(
		com.liferay.portal.kernel.util.UnicodeProperties typeSettingsProperties) {
		_repository.setTypeSettingsProperties(typeSettingsProperties);
	}

	/**
	* Sets the user ID of this repository.
	*
	* @param userId the user ID of this repository
	*/
	@Override
	public void setUserId(long userId) {
		_repository.setUserId(userId);
	}

	/**
	* Sets the user name of this repository.
	*
	* @param userName the user name of this repository
	*/
	@Override
	public void setUserName(String userName) {
		_repository.setUserName(userName);
	}

	/**
	* Sets the user uuid of this repository.
	*
	* @param userUuid the user uuid of this repository
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_repository.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this repository.
	*
	* @param uuid the uuid of this repository
	*/
	@Override
	public void setUuid(String uuid) {
		_repository.setUuid(uuid);
	}

	@Override
	public CacheModel<Repository> toCacheModel() {
		return _repository.toCacheModel();
	}

	@Override
	public Repository toEscapedModel() {
		return new RepositoryWrapper(_repository.toEscapedModel());
	}

	@Override
	public String toString() {
		return _repository.toString();
	}

	@Override
	public Repository toUnescapedModel() {
		return new RepositoryWrapper(_repository.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _repository.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RepositoryWrapper)) {
			return false;
		}

		RepositoryWrapper repositoryWrapper = (RepositoryWrapper)obj;

		if (Objects.equals(_repository, repositoryWrapper._repository)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _repository.getStagedModelType();
	}

	@Override
	public Repository getWrappedModel() {
		return _repository;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _repository.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _repository.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_repository.resetOriginalValues();
	}

	private final Repository _repository;
}