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
 * This class is a wrapper for {@link CTEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see CTEntry
 * @generated
 */
@ProviderType
public class CTEntryWrapper implements CTEntry, ModelWrapper<CTEntry> {
	public CTEntryWrapper(CTEntry ctEntry) {
		_ctEntry = ctEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return CTEntry.class;
	}

	@Override
	public String getModelClassName() {
		return CTEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<CTEntry, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<CTEntry, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<CTEntry, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<CTEntry, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<CTEntry, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<CTEntry, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<CTEntry, Object>> getAttributeGetters() {
		return _ctEntry.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<CTEntry, Object>> getAttributeSetters() {
		return _ctEntry.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new CTEntryWrapper((CTEntry)_ctEntry.clone());
	}

	@Override
	public int compareTo(CTEntry ctEntry) {
		return _ctEntry.compareTo(ctEntry);
	}

	/**
	* Returns the fully qualified class name of this ct entry.
	*
	* @return the fully qualified class name of this ct entry
	*/
	@Override
	public String getClassName() {
		return _ctEntry.getClassName();
	}

	/**
	* Returns the class name ID of this ct entry.
	*
	* @return the class name ID of this ct entry
	*/
	@Override
	public long getClassNameId() {
		return _ctEntry.getClassNameId();
	}

	/**
	* Returns the class pk of this ct entry.
	*
	* @return the class pk of this ct entry
	*/
	@Override
	public long getClassPK() {
		return _ctEntry.getClassPK();
	}

	/**
	* Returns the company ID of this ct entry.
	*
	* @return the company ID of this ct entry
	*/
	@Override
	public long getCompanyId() {
		return _ctEntry.getCompanyId();
	}

	/**
	* Returns the create date of this ct entry.
	*
	* @return the create date of this ct entry
	*/
	@Override
	public Date getCreateDate() {
		return _ctEntry.getCreateDate();
	}

	/**
	* Returns the ct entry ID of this ct entry.
	*
	* @return the ct entry ID of this ct entry
	*/
	@Override
	public long getCtEntryId() {
		return _ctEntry.getCtEntryId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _ctEntry.getExpandoBridge();
	}

	/**
	* Returns the modified date of this ct entry.
	*
	* @return the modified date of this ct entry
	*/
	@Override
	public Date getModifiedDate() {
		return _ctEntry.getModifiedDate();
	}

	/**
	* Returns the primary key of this ct entry.
	*
	* @return the primary key of this ct entry
	*/
	@Override
	public long getPrimaryKey() {
		return _ctEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ctEntry.getPrimaryKeyObj();
	}

	/**
	* Returns the resource prim key of this ct entry.
	*
	* @return the resource prim key of this ct entry
	*/
	@Override
	public long getResourcePrimKey() {
		return _ctEntry.getResourcePrimKey();
	}

	/**
	* Returns the user ID of this ct entry.
	*
	* @return the user ID of this ct entry
	*/
	@Override
	public long getUserId() {
		return _ctEntry.getUserId();
	}

	/**
	* Returns the user name of this ct entry.
	*
	* @return the user name of this ct entry
	*/
	@Override
	public String getUserName() {
		return _ctEntry.getUserName();
	}

	/**
	* Returns the user uuid of this ct entry.
	*
	* @return the user uuid of this ct entry
	*/
	@Override
	public String getUserUuid() {
		return _ctEntry.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _ctEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _ctEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _ctEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _ctEntry.isNew();
	}

	@Override
	public boolean isResourceMain() {
		return _ctEntry.isResourceMain();
	}

	@Override
	public void persist() {
		_ctEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_ctEntry.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_ctEntry.setClassName(className);
	}

	/**
	* Sets the class name ID of this ct entry.
	*
	* @param classNameId the class name ID of this ct entry
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_ctEntry.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this ct entry.
	*
	* @param classPK the class pk of this ct entry
	*/
	@Override
	public void setClassPK(long classPK) {
		_ctEntry.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this ct entry.
	*
	* @param companyId the company ID of this ct entry
	*/
	@Override
	public void setCompanyId(long companyId) {
		_ctEntry.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this ct entry.
	*
	* @param createDate the create date of this ct entry
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_ctEntry.setCreateDate(createDate);
	}

	/**
	* Sets the ct entry ID of this ct entry.
	*
	* @param ctEntryId the ct entry ID of this ct entry
	*/
	@Override
	public void setCtEntryId(long ctEntryId) {
		_ctEntry.setCtEntryId(ctEntryId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_ctEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_ctEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_ctEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the modified date of this ct entry.
	*
	* @param modifiedDate the modified date of this ct entry
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_ctEntry.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_ctEntry.setNew(n);
	}

	/**
	* Sets the primary key of this ct entry.
	*
	* @param primaryKey the primary key of this ct entry
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_ctEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_ctEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the resource prim key of this ct entry.
	*
	* @param resourcePrimKey the resource prim key of this ct entry
	*/
	@Override
	public void setResourcePrimKey(long resourcePrimKey) {
		_ctEntry.setResourcePrimKey(resourcePrimKey);
	}

	/**
	* Sets the user ID of this ct entry.
	*
	* @param userId the user ID of this ct entry
	*/
	@Override
	public void setUserId(long userId) {
		_ctEntry.setUserId(userId);
	}

	/**
	* Sets the user name of this ct entry.
	*
	* @param userName the user name of this ct entry
	*/
	@Override
	public void setUserName(String userName) {
		_ctEntry.setUserName(userName);
	}

	/**
	* Sets the user uuid of this ct entry.
	*
	* @param userUuid the user uuid of this ct entry
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_ctEntry.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<CTEntry> toCacheModel() {
		return _ctEntry.toCacheModel();
	}

	@Override
	public CTEntry toEscapedModel() {
		return new CTEntryWrapper(_ctEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _ctEntry.toString();
	}

	@Override
	public CTEntry toUnescapedModel() {
		return new CTEntryWrapper(_ctEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _ctEntry.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CTEntryWrapper)) {
			return false;
		}

		CTEntryWrapper ctEntryWrapper = (CTEntryWrapper)obj;

		if (Objects.equals(_ctEntry, ctEntryWrapper._ctEntry)) {
			return true;
		}

		return false;
	}

	@Override
	public CTEntry getWrappedModel() {
		return _ctEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _ctEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _ctEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_ctEntry.resetOriginalValues();
	}

	private final CTEntry _ctEntry;
}