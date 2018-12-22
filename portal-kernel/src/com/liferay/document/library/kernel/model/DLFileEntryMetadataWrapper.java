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

package com.liferay.document.library.kernel.model;

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
 * This class is a wrapper for {@link DLFileEntryMetadata}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLFileEntryMetadata
 * @generated
 */
@ProviderType
public class DLFileEntryMetadataWrapper implements DLFileEntryMetadata,
	ModelWrapper<DLFileEntryMetadata> {
	public DLFileEntryMetadataWrapper(DLFileEntryMetadata dlFileEntryMetadata) {
		_dlFileEntryMetadata = dlFileEntryMetadata;
	}

	@Override
	public Class<?> getModelClass() {
		return DLFileEntryMetadata.class;
	}

	@Override
	public String getModelClassName() {
		return DLFileEntryMetadata.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DLFileEntryMetadata, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<DLFileEntryMetadata, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<DLFileEntryMetadata, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DLFileEntryMetadata, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<DLFileEntryMetadata, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<DLFileEntryMetadata, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<DLFileEntryMetadata, Object>> getAttributeGetters() {
		return _dlFileEntryMetadata.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<DLFileEntryMetadata, Object>> getAttributeSetters() {
		return _dlFileEntryMetadata.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new DLFileEntryMetadataWrapper((DLFileEntryMetadata)_dlFileEntryMetadata.clone());
	}

	@Override
	public int compareTo(DLFileEntryMetadata dlFileEntryMetadata) {
		return _dlFileEntryMetadata.compareTo(dlFileEntryMetadata);
	}

	/**
	* Returns the company ID of this document library file entry metadata.
	*
	* @return the company ID of this document library file entry metadata
	*/
	@Override
	public long getCompanyId() {
		return _dlFileEntryMetadata.getCompanyId();
	}

	/**
	* Returns the ddm storage ID of this document library file entry metadata.
	*
	* @return the ddm storage ID of this document library file entry metadata
	*/
	@Override
	public long getDDMStorageId() {
		return _dlFileEntryMetadata.getDDMStorageId();
	}

	/**
	* Returns the ddm structure ID of this document library file entry metadata.
	*
	* @return the ddm structure ID of this document library file entry metadata
	*/
	@Override
	public long getDDMStructureId() {
		return _dlFileEntryMetadata.getDDMStructureId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _dlFileEntryMetadata.getExpandoBridge();
	}

	/**
	* Returns the file entry ID of this document library file entry metadata.
	*
	* @return the file entry ID of this document library file entry metadata
	*/
	@Override
	public long getFileEntryId() {
		return _dlFileEntryMetadata.getFileEntryId();
	}

	/**
	* Returns the file entry metadata ID of this document library file entry metadata.
	*
	* @return the file entry metadata ID of this document library file entry metadata
	*/
	@Override
	public long getFileEntryMetadataId() {
		return _dlFileEntryMetadata.getFileEntryMetadataId();
	}

	@Override
	public DLFileVersion getFileVersion()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileEntryMetadata.getFileVersion();
	}

	/**
	* Returns the file version ID of this document library file entry metadata.
	*
	* @return the file version ID of this document library file entry metadata
	*/
	@Override
	public long getFileVersionId() {
		return _dlFileEntryMetadata.getFileVersionId();
	}

	/**
	* Returns the primary key of this document library file entry metadata.
	*
	* @return the primary key of this document library file entry metadata
	*/
	@Override
	public long getPrimaryKey() {
		return _dlFileEntryMetadata.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _dlFileEntryMetadata.getPrimaryKeyObj();
	}

	/**
	* Returns the uuid of this document library file entry metadata.
	*
	* @return the uuid of this document library file entry metadata
	*/
	@Override
	public String getUuid() {
		return _dlFileEntryMetadata.getUuid();
	}

	@Override
	public int hashCode() {
		return _dlFileEntryMetadata.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _dlFileEntryMetadata.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _dlFileEntryMetadata.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _dlFileEntryMetadata.isNew();
	}

	@Override
	public void persist() {
		_dlFileEntryMetadata.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_dlFileEntryMetadata.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this document library file entry metadata.
	*
	* @param companyId the company ID of this document library file entry metadata
	*/
	@Override
	public void setCompanyId(long companyId) {
		_dlFileEntryMetadata.setCompanyId(companyId);
	}

	/**
	* Sets the ddm storage ID of this document library file entry metadata.
	*
	* @param DDMStorageId the ddm storage ID of this document library file entry metadata
	*/
	@Override
	public void setDDMStorageId(long DDMStorageId) {
		_dlFileEntryMetadata.setDDMStorageId(DDMStorageId);
	}

	/**
	* Sets the ddm structure ID of this document library file entry metadata.
	*
	* @param DDMStructureId the ddm structure ID of this document library file entry metadata
	*/
	@Override
	public void setDDMStructureId(long DDMStructureId) {
		_dlFileEntryMetadata.setDDMStructureId(DDMStructureId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_dlFileEntryMetadata.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_dlFileEntryMetadata.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_dlFileEntryMetadata.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the file entry ID of this document library file entry metadata.
	*
	* @param fileEntryId the file entry ID of this document library file entry metadata
	*/
	@Override
	public void setFileEntryId(long fileEntryId) {
		_dlFileEntryMetadata.setFileEntryId(fileEntryId);
	}

	/**
	* Sets the file entry metadata ID of this document library file entry metadata.
	*
	* @param fileEntryMetadataId the file entry metadata ID of this document library file entry metadata
	*/
	@Override
	public void setFileEntryMetadataId(long fileEntryMetadataId) {
		_dlFileEntryMetadata.setFileEntryMetadataId(fileEntryMetadataId);
	}

	/**
	* Sets the file version ID of this document library file entry metadata.
	*
	* @param fileVersionId the file version ID of this document library file entry metadata
	*/
	@Override
	public void setFileVersionId(long fileVersionId) {
		_dlFileEntryMetadata.setFileVersionId(fileVersionId);
	}

	@Override
	public void setNew(boolean n) {
		_dlFileEntryMetadata.setNew(n);
	}

	/**
	* Sets the primary key of this document library file entry metadata.
	*
	* @param primaryKey the primary key of this document library file entry metadata
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_dlFileEntryMetadata.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_dlFileEntryMetadata.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the uuid of this document library file entry metadata.
	*
	* @param uuid the uuid of this document library file entry metadata
	*/
	@Override
	public void setUuid(String uuid) {
		_dlFileEntryMetadata.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<DLFileEntryMetadata> toCacheModel() {
		return _dlFileEntryMetadata.toCacheModel();
	}

	@Override
	public DLFileEntryMetadata toEscapedModel() {
		return new DLFileEntryMetadataWrapper(_dlFileEntryMetadata.toEscapedModel());
	}

	@Override
	public String toString() {
		return _dlFileEntryMetadata.toString();
	}

	@Override
	public DLFileEntryMetadata toUnescapedModel() {
		return new DLFileEntryMetadataWrapper(_dlFileEntryMetadata.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _dlFileEntryMetadata.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DLFileEntryMetadataWrapper)) {
			return false;
		}

		DLFileEntryMetadataWrapper dlFileEntryMetadataWrapper = (DLFileEntryMetadataWrapper)obj;

		if (Objects.equals(_dlFileEntryMetadata,
					dlFileEntryMetadataWrapper._dlFileEntryMetadata)) {
			return true;
		}

		return false;
	}

	@Override
	public DLFileEntryMetadata getWrappedModel() {
		return _dlFileEntryMetadata;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _dlFileEntryMetadata.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _dlFileEntryMetadata.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_dlFileEntryMetadata.resetOriginalValues();
	}

	private final DLFileEntryMetadata _dlFileEntryMetadata;
}