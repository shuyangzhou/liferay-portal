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

/**
 * <p>
 * This class is a wrapper for {@link ABCTestEntity}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntity
 * @generated
 */
@ProviderType
public class ABCTestEntityWrapper implements ABCTestEntity,
	ModelWrapper<ABCTestEntity> {
	public ABCTestEntityWrapper(ABCTestEntity abcTestEntity) {
		_abcTestEntity = abcTestEntity;
	}

	@Override
	public Class<?> getModelClass() {
		return ABCTestEntity.class;
	}

	@Override
	public String getModelClassName() {
		return ABCTestEntity.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("abcTestEntityId", getAbcTestEntityId());
		attributes.put("companyId", getCompanyId());
		attributes.put("groupId", getGroupId());
		attributes.put("defaultLanguageId", getDefaultLanguageId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		String abcTestEntityId = (String)attributes.get("abcTestEntityId");

		if (abcTestEntityId != null) {
			setAbcTestEntityId(abcTestEntityId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		String defaultLanguageId = (String)attributes.get("defaultLanguageId");

		if (defaultLanguageId != null) {
			setDefaultLanguageId(defaultLanguageId);
		}
	}

	@Override
	public ABCTestEntity toEscapedModel() {
		return new ABCTestEntityWrapper(_abcTestEntity.toEscapedModel());
	}

	@Override
	public ABCTestEntity toUnescapedModel() {
		return new ABCTestEntityWrapper(_abcTestEntity.toUnescapedModel());
	}

	@Override
	public CacheModel<ABCTestEntity> toCacheModel() {
		return _abcTestEntity.toCacheModel();
	}

	@Override
	public boolean isCachedModel() {
		return _abcTestEntity.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _abcTestEntity.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _abcTestEntity.isNew();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _abcTestEntity.getExpandoBridge();
	}

	@Override
	public int compareTo(ABCTestEntity abcTestEntity) {
		return _abcTestEntity.compareTo(abcTestEntity);
	}

	@Override
	public int hashCode() {
		return _abcTestEntity.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _abcTestEntity.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new ABCTestEntityWrapper((ABCTestEntity)_abcTestEntity.clone());
	}

	/**
	* Returns the abc test entity ID of this abc test entity.
	*
	* @return the abc test entity ID of this abc test entity
	*/
	@Override
	public java.lang.String getAbcTestEntityId() {
		return _abcTestEntity.getAbcTestEntityId();
	}

	/**
	* Returns the default language ID of this abc test entity.
	*
	* @return the default language ID of this abc test entity
	*/
	@Override
	public java.lang.String getDefaultLanguageId() {
		return _abcTestEntity.getDefaultLanguageId();
	}

	@Override
	public java.lang.String getDescription() {
		return _abcTestEntity.getDescription();
	}

	@Override
	public java.lang.String getDescription(java.lang.String languageId) {
		return _abcTestEntity.getDescription(languageId);
	}

	@Override
	public java.lang.String getDescription(java.lang.String languageId,
		boolean useDefault) {
		return _abcTestEntity.getDescription(languageId, useDefault);
	}

	@Override
	public java.lang.String getDescriptionMapAsXML() {
		return _abcTestEntity.getDescriptionMapAsXML();
	}

	@Override
	public java.lang.String getName() {
		return _abcTestEntity.getName();
	}

	@Override
	public java.lang.String getName(java.lang.String languageId) {
		return _abcTestEntity.getName(languageId);
	}

	@Override
	public java.lang.String getName(java.lang.String languageId,
		boolean useDefault) {
		return _abcTestEntity.getName(languageId, useDefault);
	}

	@Override
	public java.lang.String getNameMapAsXML() {
		return _abcTestEntity.getNameMapAsXML();
	}

	/**
	* Returns the primary key of this abc test entity.
	*
	* @return the primary key of this abc test entity
	*/
	@Override
	public java.lang.String getPrimaryKey() {
		return _abcTestEntity.getPrimaryKey();
	}

	@Override
	public java.lang.String toString() {
		return _abcTestEntity.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _abcTestEntity.toXmlString();
	}

	@Override
	public java.lang.String[] getAvailableLanguageIds() {
		return _abcTestEntity.getAvailableLanguageIds();
	}

	@Override
	public Map<java.lang.String, java.lang.String> getLanguageIdToDescriptionMap() {
		return _abcTestEntity.getLanguageIdToDescriptionMap();
	}

	@Override
	public Map<java.lang.String, java.lang.String> getLanguageIdToNameMap() {
		return _abcTestEntity.getLanguageIdToNameMap();
	}

	/**
	* Returns the company ID of this abc test entity.
	*
	* @return the company ID of this abc test entity
	*/
	@Override
	public long getCompanyId() {
		return _abcTestEntity.getCompanyId();
	}

	/**
	* Returns the group ID of this abc test entity.
	*
	* @return the group ID of this abc test entity
	*/
	@Override
	public long getGroupId() {
		return _abcTestEntity.getGroupId();
	}

	/**
	* Returns the mvcc version of this abc test entity.
	*
	* @return the mvcc version of this abc test entity
	*/
	@Override
	public long getMvccVersion() {
		return _abcTestEntity.getMvccVersion();
	}

	@Override
	public void persist() {
		_abcTestEntity.persist();
	}

	/**
	* Sets the abc test entity ID of this abc test entity.
	*
	* @param abcTestEntityId the abc test entity ID of this abc test entity
	*/
	@Override
	public void setAbcTestEntityId(java.lang.String abcTestEntityId) {
		_abcTestEntity.setAbcTestEntityId(abcTestEntityId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_abcTestEntity.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this abc test entity.
	*
	* @param companyId the company ID of this abc test entity
	*/
	@Override
	public void setCompanyId(long companyId) {
		_abcTestEntity.setCompanyId(companyId);
	}

	/**
	* Sets the default language ID of this abc test entity.
	*
	* @param defaultLanguageId the default language ID of this abc test entity
	*/
	@Override
	public void setDefaultLanguageId(java.lang.String defaultLanguageId) {
		_abcTestEntity.setDefaultLanguageId(defaultLanguageId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_abcTestEntity.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_abcTestEntity.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_abcTestEntity.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this abc test entity.
	*
	* @param groupId the group ID of this abc test entity
	*/
	@Override
	public void setGroupId(long groupId) {
		_abcTestEntity.setGroupId(groupId);
	}

	/**
	* Sets the mvcc version of this abc test entity.
	*
	* @param mvccVersion the mvcc version of this abc test entity
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_abcTestEntity.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_abcTestEntity.setNew(n);
	}

	/**
	* Sets the primary key of this abc test entity.
	*
	* @param primaryKey the primary key of this abc test entity
	*/
	@Override
	public void setPrimaryKey(java.lang.String primaryKey) {
		_abcTestEntity.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_abcTestEntity.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ABCTestEntityWrapper)) {
			return false;
		}

		ABCTestEntityWrapper abcTestEntityWrapper = (ABCTestEntityWrapper)obj;

		if (Objects.equals(_abcTestEntity, abcTestEntityWrapper._abcTestEntity)) {
			return true;
		}

		return false;
	}

	@Override
	public ABCTestEntity getWrappedModel() {
		return _abcTestEntity;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _abcTestEntity.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _abcTestEntity.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_abcTestEntity.resetOriginalValues();
	}

	private final ABCTestEntity _abcTestEntity;
}