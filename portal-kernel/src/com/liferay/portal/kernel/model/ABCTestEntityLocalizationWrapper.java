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
 * This class is a wrapper for {@link ABCTestEntityLocalization}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntityLocalization
 * @generated
 */
@ProviderType
public class ABCTestEntityLocalizationWrapper
	implements ABCTestEntityLocalization,
		ModelWrapper<ABCTestEntityLocalization> {
	public ABCTestEntityLocalizationWrapper(
		ABCTestEntityLocalization abcTestEntityLocalization) {
		_abcTestEntityLocalization = abcTestEntityLocalization;
	}

	@Override
	public Class<?> getModelClass() {
		return ABCTestEntityLocalization.class;
	}

	@Override
	public String getModelClassName() {
		return ABCTestEntityLocalization.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("abcTestEntityLocalizationId",
			getAbcTestEntityLocalizationId());
		attributes.put("companyId", getCompanyId());
		attributes.put("abcTestEntityId", getAbcTestEntityId());
		attributes.put("languageId", getLanguageId());
		attributes.put("name", getName());
		attributes.put("description", getDescription());
		attributes.put("groupId", getGroupId());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		Long abcTestEntityLocalizationId = (Long)attributes.get(
				"abcTestEntityLocalizationId");

		if (abcTestEntityLocalizationId != null) {
			setAbcTestEntityLocalizationId(abcTestEntityLocalizationId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		String abcTestEntityId = (String)attributes.get("abcTestEntityId");

		if (abcTestEntityId != null) {
			setAbcTestEntityId(abcTestEntityId);
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

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}
	}

	@Override
	public ABCTestEntityLocalization toEscapedModel() {
		return new ABCTestEntityLocalizationWrapper(_abcTestEntityLocalization.toEscapedModel());
	}

	@Override
	public ABCTestEntityLocalization toUnescapedModel() {
		return new ABCTestEntityLocalizationWrapper(_abcTestEntityLocalization.toUnescapedModel());
	}

	@Override
	public CacheModel<ABCTestEntityLocalization> toCacheModel() {
		return _abcTestEntityLocalization.toCacheModel();
	}

	@Override
	public boolean isCachedModel() {
		return _abcTestEntityLocalization.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _abcTestEntityLocalization.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _abcTestEntityLocalization.isNew();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _abcTestEntityLocalization.getExpandoBridge();
	}

	@Override
	public int compareTo(ABCTestEntityLocalization abcTestEntityLocalization) {
		return _abcTestEntityLocalization.compareTo(abcTestEntityLocalization);
	}

	@Override
	public int hashCode() {
		return _abcTestEntityLocalization.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _abcTestEntityLocalization.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new ABCTestEntityLocalizationWrapper((ABCTestEntityLocalization)_abcTestEntityLocalization.clone());
	}

	/**
	* Returns the abc test entity ID of this abc test entity localization.
	*
	* @return the abc test entity ID of this abc test entity localization
	*/
	@Override
	public java.lang.String getAbcTestEntityId() {
		return _abcTestEntityLocalization.getAbcTestEntityId();
	}

	/**
	* Returns the description of this abc test entity localization.
	*
	* @return the description of this abc test entity localization
	*/
	@Override
	public java.lang.String getDescription() {
		return _abcTestEntityLocalization.getDescription();
	}

	/**
	* Returns the language ID of this abc test entity localization.
	*
	* @return the language ID of this abc test entity localization
	*/
	@Override
	public java.lang.String getLanguageId() {
		return _abcTestEntityLocalization.getLanguageId();
	}

	/**
	* Returns the name of this abc test entity localization.
	*
	* @return the name of this abc test entity localization
	*/
	@Override
	public java.lang.String getName() {
		return _abcTestEntityLocalization.getName();
	}

	@Override
	public java.lang.String toString() {
		return _abcTestEntityLocalization.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _abcTestEntityLocalization.toXmlString();
	}

	/**
	* Returns the abc test entity localization ID of this abc test entity localization.
	*
	* @return the abc test entity localization ID of this abc test entity localization
	*/
	@Override
	public long getAbcTestEntityLocalizationId() {
		return _abcTestEntityLocalization.getAbcTestEntityLocalizationId();
	}

	/**
	* Returns the company ID of this abc test entity localization.
	*
	* @return the company ID of this abc test entity localization
	*/
	@Override
	public long getCompanyId() {
		return _abcTestEntityLocalization.getCompanyId();
	}

	/**
	* Returns the group ID of this abc test entity localization.
	*
	* @return the group ID of this abc test entity localization
	*/
	@Override
	public long getGroupId() {
		return _abcTestEntityLocalization.getGroupId();
	}

	/**
	* Returns the mvcc version of this abc test entity localization.
	*
	* @return the mvcc version of this abc test entity localization
	*/
	@Override
	public long getMvccVersion() {
		return _abcTestEntityLocalization.getMvccVersion();
	}

	/**
	* Returns the primary key of this abc test entity localization.
	*
	* @return the primary key of this abc test entity localization
	*/
	@Override
	public long getPrimaryKey() {
		return _abcTestEntityLocalization.getPrimaryKey();
	}

	/**
	* Sets the abc test entity ID of this abc test entity localization.
	*
	* @param abcTestEntityId the abc test entity ID of this abc test entity localization
	*/
	@Override
	public void setAbcTestEntityId(java.lang.String abcTestEntityId) {
		_abcTestEntityLocalization.setAbcTestEntityId(abcTestEntityId);
	}

	/**
	* Sets the abc test entity localization ID of this abc test entity localization.
	*
	* @param abcTestEntityLocalizationId the abc test entity localization ID of this abc test entity localization
	*/
	@Override
	public void setAbcTestEntityLocalizationId(long abcTestEntityLocalizationId) {
		_abcTestEntityLocalization.setAbcTestEntityLocalizationId(abcTestEntityLocalizationId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_abcTestEntityLocalization.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this abc test entity localization.
	*
	* @param companyId the company ID of this abc test entity localization
	*/
	@Override
	public void setCompanyId(long companyId) {
		_abcTestEntityLocalization.setCompanyId(companyId);
	}

	/**
	* Sets the description of this abc test entity localization.
	*
	* @param description the description of this abc test entity localization
	*/
	@Override
	public void setDescription(java.lang.String description) {
		_abcTestEntityLocalization.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_abcTestEntityLocalization.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_abcTestEntityLocalization.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_abcTestEntityLocalization.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this abc test entity localization.
	*
	* @param groupId the group ID of this abc test entity localization
	*/
	@Override
	public void setGroupId(long groupId) {
		_abcTestEntityLocalization.setGroupId(groupId);
	}

	/**
	* Sets the language ID of this abc test entity localization.
	*
	* @param languageId the language ID of this abc test entity localization
	*/
	@Override
	public void setLanguageId(java.lang.String languageId) {
		_abcTestEntityLocalization.setLanguageId(languageId);
	}

	/**
	* Sets the mvcc version of this abc test entity localization.
	*
	* @param mvccVersion the mvcc version of this abc test entity localization
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_abcTestEntityLocalization.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this abc test entity localization.
	*
	* @param name the name of this abc test entity localization
	*/
	@Override
	public void setName(java.lang.String name) {
		_abcTestEntityLocalization.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_abcTestEntityLocalization.setNew(n);
	}

	/**
	* Sets the primary key of this abc test entity localization.
	*
	* @param primaryKey the primary key of this abc test entity localization
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_abcTestEntityLocalization.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_abcTestEntityLocalization.setPrimaryKeyObj(primaryKeyObj);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ABCTestEntityLocalizationWrapper)) {
			return false;
		}

		ABCTestEntityLocalizationWrapper abcTestEntityLocalizationWrapper = (ABCTestEntityLocalizationWrapper)obj;

		if (Objects.equals(_abcTestEntityLocalization,
					abcTestEntityLocalizationWrapper._abcTestEntityLocalization)) {
			return true;
		}

		return false;
	}

	@Override
	public ABCTestEntityLocalization getWrappedModel() {
		return _abcTestEntityLocalization;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _abcTestEntityLocalization.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _abcTestEntityLocalization.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_abcTestEntityLocalization.resetOriginalValues();
	}

	private final ABCTestEntityLocalization _abcTestEntityLocalization;
}