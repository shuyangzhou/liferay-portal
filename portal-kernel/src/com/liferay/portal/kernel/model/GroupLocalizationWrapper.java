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
 * This class is a wrapper for {@link GroupLocalization}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see GroupLocalization
 * @generated
 */
@ProviderType
public class GroupLocalizationWrapper implements GroupLocalization,
	ModelWrapper<GroupLocalization> {
	public GroupLocalizationWrapper(GroupLocalization groupLocalization) {
		_groupLocalization = groupLocalization;
	}

	@Override
	public Class<?> getModelClass() {
		return GroupLocalization.class;
	}

	@Override
	public String getModelClassName() {
		return GroupLocalization.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("groupLocalizationId", getGroupLocalizationId());
		attributes.put("companyId", getCompanyId());
		attributes.put("groupId", getGroupId());
		attributes.put("languageId", getLanguageId());
		attributes.put("name", getName());
		attributes.put("description", getDescription());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long groupLocalizationId = (Long)attributes.get("groupLocalizationId");

		if (groupLocalizationId != null) {
			setGroupLocalizationId(groupLocalizationId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		String languageId = (String)attributes.get("languageId");

		if (languageId != null) {
			setLanguageId(languageId);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}
	}

	@Override
	public CacheModel<GroupLocalization> toCacheModel() {
		return _groupLocalization.toCacheModel();
	}

	@Override
	public GroupLocalization toEscapedModel() {
		return new GroupLocalizationWrapper(_groupLocalization.toEscapedModel());
	}

	@Override
	public GroupLocalization toUnescapedModel() {
		return new GroupLocalizationWrapper(_groupLocalization.toUnescapedModel());
	}

	@Override
	public boolean isCachedModel() {
		return _groupLocalization.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _groupLocalization.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _groupLocalization.isNew();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _groupLocalization.getExpandoBridge();
	}

	@Override
	public int compareTo(GroupLocalization groupLocalization) {
		return _groupLocalization.compareTo(groupLocalization);
	}

	@Override
	public int hashCode() {
		return _groupLocalization.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _groupLocalization.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new GroupLocalizationWrapper((GroupLocalization)_groupLocalization.clone());
	}

	/**
	* Returns the description of this group localization.
	*
	* @return the description of this group localization
	*/
	@Override
	public java.lang.String getDescription() {
		return _groupLocalization.getDescription();
	}

	/**
	* Returns the language ID of this group localization.
	*
	* @return the language ID of this group localization
	*/
	@Override
	public java.lang.String getLanguageId() {
		return _groupLocalization.getLanguageId();
	}

	/**
	* Returns the name of this group localization.
	*
	* @return the name of this group localization
	*/
	@Override
	public java.lang.String getName() {
		return _groupLocalization.getName();
	}

	@Override
	public java.lang.String toString() {
		return _groupLocalization.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _groupLocalization.toXmlString();
	}

	/**
	* Returns the company ID of this group localization.
	*
	* @return the company ID of this group localization
	*/
	@Override
	public long getCompanyId() {
		return _groupLocalization.getCompanyId();
	}

	/**
	* Returns the group ID of this group localization.
	*
	* @return the group ID of this group localization
	*/
	@Override
	public long getGroupId() {
		return _groupLocalization.getGroupId();
	}

	/**
	* Returns the group localization ID of this group localization.
	*
	* @return the group localization ID of this group localization
	*/
	@Override
	public long getGroupLocalizationId() {
		return _groupLocalization.getGroupLocalizationId();
	}

	/**
	* Returns the mvcc version of this group localization.
	*
	* @return the mvcc version of this group localization
	*/
	@Override
	public long getMvccVersion() {
		return _groupLocalization.getMvccVersion();
	}

	/**
	* Returns the primary key of this group localization.
	*
	* @return the primary key of this group localization
	*/
	@Override
	public long getPrimaryKey() {
		return _groupLocalization.getPrimaryKey();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_groupLocalization.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this group localization.
	*
	* @param companyId the company ID of this group localization
	*/
	@Override
	public void setCompanyId(long companyId) {
		_groupLocalization.setCompanyId(companyId);
	}

	/**
	* Sets the description of this group localization.
	*
	* @param description the description of this group localization
	*/
	@Override
	public void setDescription(java.lang.String description) {
		_groupLocalization.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_groupLocalization.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_groupLocalization.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_groupLocalization.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this group localization.
	*
	* @param groupId the group ID of this group localization
	*/
	@Override
	public void setGroupId(long groupId) {
		_groupLocalization.setGroupId(groupId);
	}

	/**
	* Sets the group localization ID of this group localization.
	*
	* @param groupLocalizationId the group localization ID of this group localization
	*/
	@Override
	public void setGroupLocalizationId(long groupLocalizationId) {
		_groupLocalization.setGroupLocalizationId(groupLocalizationId);
	}

	/**
	* Sets the language ID of this group localization.
	*
	* @param languageId the language ID of this group localization
	*/
	@Override
	public void setLanguageId(java.lang.String languageId) {
		_groupLocalization.setLanguageId(languageId);
	}

	/**
	* Sets the mvcc version of this group localization.
	*
	* @param mvccVersion the mvcc version of this group localization
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_groupLocalization.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this group localization.
	*
	* @param name the name of this group localization
	*/
	@Override
	public void setName(java.lang.String name) {
		_groupLocalization.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_groupLocalization.setNew(n);
	}

	/**
	* Sets the primary key of this group localization.
	*
	* @param primaryKey the primary key of this group localization
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_groupLocalization.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_groupLocalization.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof GroupLocalizationWrapper)) {
			return false;
		}

		GroupLocalizationWrapper groupLocalizationWrapper = (GroupLocalizationWrapper)obj;

		if (Objects.equals(_groupLocalization,
					groupLocalizationWrapper._groupLocalization)) {
			return true;
		}

		return false;
	}

	@Override
	public GroupLocalization getWrappedModel() {
		return _groupLocalization;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _groupLocalization.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _groupLocalization.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_groupLocalization.resetOriginalValues();
	}

	private final GroupLocalization _groupLocalization;
}