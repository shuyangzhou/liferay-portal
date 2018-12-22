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

package com.liferay.dynamic.data.mapping.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link DDMStructureLink}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DDMStructureLink
 * @generated
 */
@ProviderType
public class DDMStructureLinkWrapper implements DDMStructureLink,
	ModelWrapper<DDMStructureLink> {
	public DDMStructureLinkWrapper(DDMStructureLink ddmStructureLink) {
		_ddmStructureLink = ddmStructureLink;
	}

	@Override
	public Class<?> getModelClass() {
		return DDMStructureLink.class;
	}

	@Override
	public String getModelClassName() {
		return DDMStructureLink.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DDMStructureLink, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<DDMStructureLink, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<DDMStructureLink, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DDMStructureLink, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<DDMStructureLink, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<DDMStructureLink, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<DDMStructureLink, Object>> getAttributeGetters() {
		return _ddmStructureLink.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<DDMStructureLink, Object>> getAttributeSetters() {
		return _ddmStructureLink.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new DDMStructureLinkWrapper((DDMStructureLink)_ddmStructureLink.clone());
	}

	@Override
	public int compareTo(DDMStructureLink ddmStructureLink) {
		return _ddmStructureLink.compareTo(ddmStructureLink);
	}

	/**
	* Returns the fully qualified class name of this ddm structure link.
	*
	* @return the fully qualified class name of this ddm structure link
	*/
	@Override
	public String getClassName() {
		return _ddmStructureLink.getClassName();
	}

	/**
	* Returns the class name ID of this ddm structure link.
	*
	* @return the class name ID of this ddm structure link
	*/
	@Override
	public long getClassNameId() {
		return _ddmStructureLink.getClassNameId();
	}

	/**
	* Returns the class pk of this ddm structure link.
	*
	* @return the class pk of this ddm structure link
	*/
	@Override
	public long getClassPK() {
		return _ddmStructureLink.getClassPK();
	}

	/**
	* Returns the company ID of this ddm structure link.
	*
	* @return the company ID of this ddm structure link
	*/
	@Override
	public long getCompanyId() {
		return _ddmStructureLink.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _ddmStructureLink.getExpandoBridge();
	}

	/**
	* Returns the primary key of this ddm structure link.
	*
	* @return the primary key of this ddm structure link
	*/
	@Override
	public long getPrimaryKey() {
		return _ddmStructureLink.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ddmStructureLink.getPrimaryKeyObj();
	}

	@Override
	public DDMStructure getStructure()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _ddmStructureLink.getStructure();
	}

	/**
	* Returns the structure ID of this ddm structure link.
	*
	* @return the structure ID of this ddm structure link
	*/
	@Override
	public long getStructureId() {
		return _ddmStructureLink.getStructureId();
	}

	/**
	* Returns the structure link ID of this ddm structure link.
	*
	* @return the structure link ID of this ddm structure link
	*/
	@Override
	public long getStructureLinkId() {
		return _ddmStructureLink.getStructureLinkId();
	}

	@Override
	public int hashCode() {
		return _ddmStructureLink.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _ddmStructureLink.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _ddmStructureLink.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _ddmStructureLink.isNew();
	}

	@Override
	public void persist() {
		_ddmStructureLink.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_ddmStructureLink.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_ddmStructureLink.setClassName(className);
	}

	/**
	* Sets the class name ID of this ddm structure link.
	*
	* @param classNameId the class name ID of this ddm structure link
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_ddmStructureLink.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this ddm structure link.
	*
	* @param classPK the class pk of this ddm structure link
	*/
	@Override
	public void setClassPK(long classPK) {
		_ddmStructureLink.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this ddm structure link.
	*
	* @param companyId the company ID of this ddm structure link
	*/
	@Override
	public void setCompanyId(long companyId) {
		_ddmStructureLink.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_ddmStructureLink.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_ddmStructureLink.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_ddmStructureLink.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public void setNew(boolean n) {
		_ddmStructureLink.setNew(n);
	}

	/**
	* Sets the primary key of this ddm structure link.
	*
	* @param primaryKey the primary key of this ddm structure link
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_ddmStructureLink.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_ddmStructureLink.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the structure ID of this ddm structure link.
	*
	* @param structureId the structure ID of this ddm structure link
	*/
	@Override
	public void setStructureId(long structureId) {
		_ddmStructureLink.setStructureId(structureId);
	}

	/**
	* Sets the structure link ID of this ddm structure link.
	*
	* @param structureLinkId the structure link ID of this ddm structure link
	*/
	@Override
	public void setStructureLinkId(long structureLinkId) {
		_ddmStructureLink.setStructureLinkId(structureLinkId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<DDMStructureLink> toCacheModel() {
		return _ddmStructureLink.toCacheModel();
	}

	@Override
	public DDMStructureLink toEscapedModel() {
		return new DDMStructureLinkWrapper(_ddmStructureLink.toEscapedModel());
	}

	@Override
	public String toString() {
		return _ddmStructureLink.toString();
	}

	@Override
	public DDMStructureLink toUnescapedModel() {
		return new DDMStructureLinkWrapper(_ddmStructureLink.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _ddmStructureLink.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DDMStructureLinkWrapper)) {
			return false;
		}

		DDMStructureLinkWrapper ddmStructureLinkWrapper = (DDMStructureLinkWrapper)obj;

		if (Objects.equals(_ddmStructureLink,
					ddmStructureLinkWrapper._ddmStructureLink)) {
			return true;
		}

		return false;
	}

	@Override
	public DDMStructureLink getWrappedModel() {
		return _ddmStructureLink;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _ddmStructureLink.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _ddmStructureLink.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_ddmStructureLink.resetOriginalValues();
	}

	private final DDMStructureLink _ddmStructureLink;
}