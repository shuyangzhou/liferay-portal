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

package com.liferay.expando.kernel.model;

import aQute.bnd.annotation.ProviderType;

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
 * This class is a wrapper for {@link ExpandoRow}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ExpandoRow
 * @generated
 */
@ProviderType
public class ExpandoRowWrapper implements ExpandoRow, ModelWrapper<ExpandoRow> {
	public ExpandoRowWrapper(ExpandoRow expandoRow) {
		_expandoRow = expandoRow;
	}

	@Override
	public Class<?> getModelClass() {
		return ExpandoRow.class;
	}

	@Override
	public String getModelClassName() {
		return ExpandoRow.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<ExpandoRow, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<ExpandoRow, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<ExpandoRow, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<ExpandoRow, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<ExpandoRow, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<ExpandoRow, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<ExpandoRow, Object>> getAttributeGetters() {
		return _expandoRow.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<ExpandoRow, Object>> getAttributeSetters() {
		return _expandoRow.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new ExpandoRowWrapper((ExpandoRow)_expandoRow.clone());
	}

	@Override
	public int compareTo(ExpandoRow expandoRow) {
		return _expandoRow.compareTo(expandoRow);
	}

	/**
	* Returns the class pk of this expando row.
	*
	* @return the class pk of this expando row
	*/
	@Override
	public long getClassPK() {
		return _expandoRow.getClassPK();
	}

	/**
	* Returns the company ID of this expando row.
	*
	* @return the company ID of this expando row
	*/
	@Override
	public long getCompanyId() {
		return _expandoRow.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _expandoRow.getExpandoBridge();
	}

	/**
	* Returns the modified date of this expando row.
	*
	* @return the modified date of this expando row
	*/
	@Override
	public Date getModifiedDate() {
		return _expandoRow.getModifiedDate();
	}

	/**
	* Returns the primary key of this expando row.
	*
	* @return the primary key of this expando row
	*/
	@Override
	public long getPrimaryKey() {
		return _expandoRow.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _expandoRow.getPrimaryKeyObj();
	}

	/**
	* Returns the row ID of this expando row.
	*
	* @return the row ID of this expando row
	*/
	@Override
	public long getRowId() {
		return _expandoRow.getRowId();
	}

	/**
	* Returns the table ID of this expando row.
	*
	* @return the table ID of this expando row
	*/
	@Override
	public long getTableId() {
		return _expandoRow.getTableId();
	}

	@Override
	public int hashCode() {
		return _expandoRow.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _expandoRow.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _expandoRow.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _expandoRow.isNew();
	}

	@Override
	public void persist() {
		_expandoRow.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_expandoRow.setCachedModel(cachedModel);
	}

	/**
	* Sets the class pk of this expando row.
	*
	* @param classPK the class pk of this expando row
	*/
	@Override
	public void setClassPK(long classPK) {
		_expandoRow.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this expando row.
	*
	* @param companyId the company ID of this expando row
	*/
	@Override
	public void setCompanyId(long companyId) {
		_expandoRow.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_expandoRow.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_expandoRow.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_expandoRow.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the modified date of this expando row.
	*
	* @param modifiedDate the modified date of this expando row
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_expandoRow.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_expandoRow.setNew(n);
	}

	/**
	* Sets the primary key of this expando row.
	*
	* @param primaryKey the primary key of this expando row
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_expandoRow.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_expandoRow.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the row ID of this expando row.
	*
	* @param rowId the row ID of this expando row
	*/
	@Override
	public void setRowId(long rowId) {
		_expandoRow.setRowId(rowId);
	}

	/**
	* Sets the table ID of this expando row.
	*
	* @param tableId the table ID of this expando row
	*/
	@Override
	public void setTableId(long tableId) {
		_expandoRow.setTableId(tableId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<ExpandoRow> toCacheModel() {
		return _expandoRow.toCacheModel();
	}

	@Override
	public ExpandoRow toEscapedModel() {
		return new ExpandoRowWrapper(_expandoRow.toEscapedModel());
	}

	@Override
	public String toString() {
		return _expandoRow.toString();
	}

	@Override
	public ExpandoRow toUnescapedModel() {
		return new ExpandoRowWrapper(_expandoRow.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _expandoRow.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ExpandoRowWrapper)) {
			return false;
		}

		ExpandoRowWrapper expandoRowWrapper = (ExpandoRowWrapper)obj;

		if (Objects.equals(_expandoRow, expandoRowWrapper._expandoRow)) {
			return true;
		}

		return false;
	}

	@Override
	public ExpandoRow getWrappedModel() {
		return _expandoRow;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _expandoRow.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _expandoRow.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_expandoRow.resetOriginalValues();
	}

	private final ExpandoRow _expandoRow;
}