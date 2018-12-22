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
 * This class is a wrapper for {@link DLFileVersion}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLFileVersion
 * @generated
 */
@ProviderType
public class DLFileVersionWrapper implements DLFileVersion,
	ModelWrapper<DLFileVersion> {
	public DLFileVersionWrapper(DLFileVersion dlFileVersion) {
		_dlFileVersion = dlFileVersion;
	}

	@Override
	public Class<?> getModelClass() {
		return DLFileVersion.class;
	}

	@Override
	public String getModelClassName() {
		return DLFileVersion.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<DLFileVersion, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<DLFileVersion, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<DLFileVersion, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<DLFileVersion, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<DLFileVersion, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<DLFileVersion, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<DLFileVersion, Object>> getAttributeGetters() {
		return _dlFileVersion.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<DLFileVersion, Object>> getAttributeSetters() {
		return _dlFileVersion.getAttributeSetters();
	}

	@Override
	public String buildTreePath()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.buildTreePath();
	}

	@Override
	public Object clone() {
		return new DLFileVersionWrapper((DLFileVersion)_dlFileVersion.clone());
	}

	@Override
	public int compareTo(DLFileVersion dlFileVersion) {
		return _dlFileVersion.compareTo(dlFileVersion);
	}

	/**
	* Returns the change log of this document library file version.
	*
	* @return the change log of this document library file version
	*/
	@Override
	public String getChangeLog() {
		return _dlFileVersion.getChangeLog();
	}

	/**
	* Returns the checksum of this document library file version.
	*
	* @return the checksum of this document library file version
	*/
	@Override
	public String getChecksum() {
		return _dlFileVersion.getChecksum();
	}

	/**
	* Returns the company ID of this document library file version.
	*
	* @return the company ID of this document library file version
	*/
	@Override
	public long getCompanyId() {
		return _dlFileVersion.getCompanyId();
	}

	@Override
	public java.io.InputStream getContentStream(boolean incrementCounter)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.getContentStream(incrementCounter);
	}

	/**
	* Returns the create date of this document library file version.
	*
	* @return the create date of this document library file version
	*/
	@Override
	public Date getCreateDate() {
		return _dlFileVersion.getCreateDate();
	}

	@Override
	public java.util.List<com.liferay.dynamic.data.mapping.kernel.DDMStructure> getDDMStructures()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.getDDMStructures();
	}

	/**
	* Returns the description of this document library file version.
	*
	* @return the description of this document library file version
	*/
	@Override
	public String getDescription() {
		return _dlFileVersion.getDescription();
	}

	@Override
	public DLFileEntryType getDLFileEntryType()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.getDLFileEntryType();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _dlFileVersion.getExpandoBridge();
	}

	/**
	* Returns the extension of this document library file version.
	*
	* @return the extension of this document library file version
	*/
	@Override
	public String getExtension() {
		return _dlFileVersion.getExtension();
	}

	/**
	* Returns the extra settings of this document library file version.
	*
	* @return the extra settings of this document library file version
	*/
	@Override
	public String getExtraSettings() {
		return _dlFileVersion.getExtraSettings();
	}

	@Override
	public com.liferay.portal.kernel.util.UnicodeProperties getExtraSettingsProperties() {
		return _dlFileVersion.getExtraSettingsProperties();
	}

	@Override
	public DLFileEntry getFileEntry()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.getFileEntry();
	}

	/**
	* Returns the file entry ID of this document library file version.
	*
	* @return the file entry ID of this document library file version
	*/
	@Override
	public long getFileEntryId() {
		return _dlFileVersion.getFileEntryId();
	}

	/**
	* Returns the file entry type ID of this document library file version.
	*
	* @return the file entry type ID of this document library file version
	*/
	@Override
	public long getFileEntryTypeId() {
		return _dlFileVersion.getFileEntryTypeId();
	}

	/**
	* Returns the file name of this document library file version.
	*
	* @return the file name of this document library file version
	*/
	@Override
	public String getFileName() {
		return _dlFileVersion.getFileName();
	}

	/**
	* Returns the file version ID of this document library file version.
	*
	* @return the file version ID of this document library file version
	*/
	@Override
	public long getFileVersionId() {
		return _dlFileVersion.getFileVersionId();
	}

	@Override
	public DLFolder getFolder()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlFileVersion.getFolder();
	}

	/**
	* Returns the folder ID of this document library file version.
	*
	* @return the folder ID of this document library file version
	*/
	@Override
	public long getFolderId() {
		return _dlFileVersion.getFolderId();
	}

	/**
	* Returns the group ID of this document library file version.
	*
	* @return the group ID of this document library file version
	*/
	@Override
	public long getGroupId() {
		return _dlFileVersion.getGroupId();
	}

	@Override
	public String getIcon() {
		return _dlFileVersion.getIcon();
	}

	/**
	* Returns the last publish date of this document library file version.
	*
	* @return the last publish date of this document library file version
	*/
	@Override
	public Date getLastPublishDate() {
		return _dlFileVersion.getLastPublishDate();
	}

	/**
	* Returns the mime type of this document library file version.
	*
	* @return the mime type of this document library file version
	*/
	@Override
	public String getMimeType() {
		return _dlFileVersion.getMimeType();
	}

	/**
	* Returns the modified date of this document library file version.
	*
	* @return the modified date of this document library file version
	*/
	@Override
	public Date getModifiedDate() {
		return _dlFileVersion.getModifiedDate();
	}

	/**
	* Returns the primary key of this document library file version.
	*
	* @return the primary key of this document library file version
	*/
	@Override
	public long getPrimaryKey() {
		return _dlFileVersion.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _dlFileVersion.getPrimaryKeyObj();
	}

	/**
	* Returns the repository ID of this document library file version.
	*
	* @return the repository ID of this document library file version
	*/
	@Override
	public long getRepositoryId() {
		return _dlFileVersion.getRepositoryId();
	}

	/**
	* Returns the size of this document library file version.
	*
	* @return the size of this document library file version
	*/
	@Override
	public long getSize() {
		return _dlFileVersion.getSize();
	}

	/**
	* Returns the status of this document library file version.
	*
	* @return the status of this document library file version
	*/
	@Override
	public int getStatus() {
		return _dlFileVersion.getStatus();
	}

	/**
	* Returns the status by user ID of this document library file version.
	*
	* @return the status by user ID of this document library file version
	*/
	@Override
	public long getStatusByUserId() {
		return _dlFileVersion.getStatusByUserId();
	}

	/**
	* Returns the status by user name of this document library file version.
	*
	* @return the status by user name of this document library file version
	*/
	@Override
	public String getStatusByUserName() {
		return _dlFileVersion.getStatusByUserName();
	}

	/**
	* Returns the status by user uuid of this document library file version.
	*
	* @return the status by user uuid of this document library file version
	*/
	@Override
	public String getStatusByUserUuid() {
		return _dlFileVersion.getStatusByUserUuid();
	}

	/**
	* Returns the status date of this document library file version.
	*
	* @return the status date of this document library file version
	*/
	@Override
	public Date getStatusDate() {
		return _dlFileVersion.getStatusDate();
	}

	/**
	* Returns the title of this document library file version.
	*
	* @return the title of this document library file version
	*/
	@Override
	public String getTitle() {
		return _dlFileVersion.getTitle();
	}

	/**
	* Returns the tree path of this document library file version.
	*
	* @return the tree path of this document library file version
	*/
	@Override
	public String getTreePath() {
		return _dlFileVersion.getTreePath();
	}

	/**
	* Returns the user ID of this document library file version.
	*
	* @return the user ID of this document library file version
	*/
	@Override
	public long getUserId() {
		return _dlFileVersion.getUserId();
	}

	/**
	* Returns the user name of this document library file version.
	*
	* @return the user name of this document library file version
	*/
	@Override
	public String getUserName() {
		return _dlFileVersion.getUserName();
	}

	/**
	* Returns the user uuid of this document library file version.
	*
	* @return the user uuid of this document library file version
	*/
	@Override
	public String getUserUuid() {
		return _dlFileVersion.getUserUuid();
	}

	/**
	* Returns the uuid of this document library file version.
	*
	* @return the uuid of this document library file version
	*/
	@Override
	public String getUuid() {
		return _dlFileVersion.getUuid();
	}

	/**
	* Returns the version of this document library file version.
	*
	* @return the version of this document library file version
	*/
	@Override
	public String getVersion() {
		return _dlFileVersion.getVersion();
	}

	@Override
	public int hashCode() {
		return _dlFileVersion.hashCode();
	}

	/**
	* Returns <code>true</code> if this document library file version is approved.
	*
	* @return <code>true</code> if this document library file version is approved; <code>false</code> otherwise
	*/
	@Override
	public boolean isApproved() {
		return _dlFileVersion.isApproved();
	}

	@Override
	public boolean isCachedModel() {
		return _dlFileVersion.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this document library file version is denied.
	*
	* @return <code>true</code> if this document library file version is denied; <code>false</code> otherwise
	*/
	@Override
	public boolean isDenied() {
		return _dlFileVersion.isDenied();
	}

	/**
	* Returns <code>true</code> if this document library file version is a draft.
	*
	* @return <code>true</code> if this document library file version is a draft; <code>false</code> otherwise
	*/
	@Override
	public boolean isDraft() {
		return _dlFileVersion.isDraft();
	}

	@Override
	public boolean isEscapedModel() {
		return _dlFileVersion.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this document library file version is expired.
	*
	* @return <code>true</code> if this document library file version is expired; <code>false</code> otherwise
	*/
	@Override
	public boolean isExpired() {
		return _dlFileVersion.isExpired();
	}

	/**
	* Returns <code>true</code> if this document library file version is inactive.
	*
	* @return <code>true</code> if this document library file version is inactive; <code>false</code> otherwise
	*/
	@Override
	public boolean isInactive() {
		return _dlFileVersion.isInactive();
	}

	/**
	* Returns <code>true</code> if this document library file version is incomplete.
	*
	* @return <code>true</code> if this document library file version is incomplete; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomplete() {
		return _dlFileVersion.isIncomplete();
	}

	@Override
	public boolean isNew() {
		return _dlFileVersion.isNew();
	}

	/**
	* Returns <code>true</code> if this document library file version is pending.
	*
	* @return <code>true</code> if this document library file version is pending; <code>false</code> otherwise
	*/
	@Override
	public boolean isPending() {
		return _dlFileVersion.isPending();
	}

	/**
	* Returns <code>true</code> if this document library file version is scheduled.
	*
	* @return <code>true</code> if this document library file version is scheduled; <code>false</code> otherwise
	*/
	@Override
	public boolean isScheduled() {
		return _dlFileVersion.isScheduled();
	}

	@Override
	public void persist() {
		_dlFileVersion.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_dlFileVersion.setCachedModel(cachedModel);
	}

	/**
	* Sets the change log of this document library file version.
	*
	* @param changeLog the change log of this document library file version
	*/
	@Override
	public void setChangeLog(String changeLog) {
		_dlFileVersion.setChangeLog(changeLog);
	}

	/**
	* Sets the checksum of this document library file version.
	*
	* @param checksum the checksum of this document library file version
	*/
	@Override
	public void setChecksum(String checksum) {
		_dlFileVersion.setChecksum(checksum);
	}

	/**
	* Sets the company ID of this document library file version.
	*
	* @param companyId the company ID of this document library file version
	*/
	@Override
	public void setCompanyId(long companyId) {
		_dlFileVersion.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this document library file version.
	*
	* @param createDate the create date of this document library file version
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_dlFileVersion.setCreateDate(createDate);
	}

	/**
	* Sets the description of this document library file version.
	*
	* @param description the description of this document library file version
	*/
	@Override
	public void setDescription(String description) {
		_dlFileVersion.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_dlFileVersion.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_dlFileVersion.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_dlFileVersion.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the extension of this document library file version.
	*
	* @param extension the extension of this document library file version
	*/
	@Override
	public void setExtension(String extension) {
		_dlFileVersion.setExtension(extension);
	}

	/**
	* Sets the extra settings of this document library file version.
	*
	* @param extraSettings the extra settings of this document library file version
	*/
	@Override
	public void setExtraSettings(String extraSettings) {
		_dlFileVersion.setExtraSettings(extraSettings);
	}

	@Override
	public void setExtraSettingsProperties(
		com.liferay.portal.kernel.util.UnicodeProperties extraSettingsProperties) {
		_dlFileVersion.setExtraSettingsProperties(extraSettingsProperties);
	}

	/**
	* Sets the file entry ID of this document library file version.
	*
	* @param fileEntryId the file entry ID of this document library file version
	*/
	@Override
	public void setFileEntryId(long fileEntryId) {
		_dlFileVersion.setFileEntryId(fileEntryId);
	}

	/**
	* Sets the file entry type ID of this document library file version.
	*
	* @param fileEntryTypeId the file entry type ID of this document library file version
	*/
	@Override
	public void setFileEntryTypeId(long fileEntryTypeId) {
		_dlFileVersion.setFileEntryTypeId(fileEntryTypeId);
	}

	/**
	* Sets the file name of this document library file version.
	*
	* @param fileName the file name of this document library file version
	*/
	@Override
	public void setFileName(String fileName) {
		_dlFileVersion.setFileName(fileName);
	}

	/**
	* Sets the file version ID of this document library file version.
	*
	* @param fileVersionId the file version ID of this document library file version
	*/
	@Override
	public void setFileVersionId(long fileVersionId) {
		_dlFileVersion.setFileVersionId(fileVersionId);
	}

	/**
	* Sets the folder ID of this document library file version.
	*
	* @param folderId the folder ID of this document library file version
	*/
	@Override
	public void setFolderId(long folderId) {
		_dlFileVersion.setFolderId(folderId);
	}

	/**
	* Sets the group ID of this document library file version.
	*
	* @param groupId the group ID of this document library file version
	*/
	@Override
	public void setGroupId(long groupId) {
		_dlFileVersion.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this document library file version.
	*
	* @param lastPublishDate the last publish date of this document library file version
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_dlFileVersion.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the mime type of this document library file version.
	*
	* @param mimeType the mime type of this document library file version
	*/
	@Override
	public void setMimeType(String mimeType) {
		_dlFileVersion.setMimeType(mimeType);
	}

	/**
	* Sets the modified date of this document library file version.
	*
	* @param modifiedDate the modified date of this document library file version
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_dlFileVersion.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_dlFileVersion.setNew(n);
	}

	/**
	* Sets the primary key of this document library file version.
	*
	* @param primaryKey the primary key of this document library file version
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_dlFileVersion.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_dlFileVersion.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the repository ID of this document library file version.
	*
	* @param repositoryId the repository ID of this document library file version
	*/
	@Override
	public void setRepositoryId(long repositoryId) {
		_dlFileVersion.setRepositoryId(repositoryId);
	}

	/**
	* Sets the size of this document library file version.
	*
	* @param size the size of this document library file version
	*/
	@Override
	public void setSize(long size) {
		_dlFileVersion.setSize(size);
	}

	/**
	* Sets the status of this document library file version.
	*
	* @param status the status of this document library file version
	*/
	@Override
	public void setStatus(int status) {
		_dlFileVersion.setStatus(status);
	}

	/**
	* Sets the status by user ID of this document library file version.
	*
	* @param statusByUserId the status by user ID of this document library file version
	*/
	@Override
	public void setStatusByUserId(long statusByUserId) {
		_dlFileVersion.setStatusByUserId(statusByUserId);
	}

	/**
	* Sets the status by user name of this document library file version.
	*
	* @param statusByUserName the status by user name of this document library file version
	*/
	@Override
	public void setStatusByUserName(String statusByUserName) {
		_dlFileVersion.setStatusByUserName(statusByUserName);
	}

	/**
	* Sets the status by user uuid of this document library file version.
	*
	* @param statusByUserUuid the status by user uuid of this document library file version
	*/
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		_dlFileVersion.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	* Sets the status date of this document library file version.
	*
	* @param statusDate the status date of this document library file version
	*/
	@Override
	public void setStatusDate(Date statusDate) {
		_dlFileVersion.setStatusDate(statusDate);
	}

	/**
	* Sets the title of this document library file version.
	*
	* @param title the title of this document library file version
	*/
	@Override
	public void setTitle(String title) {
		_dlFileVersion.setTitle(title);
	}

	/**
	* Sets the tree path of this document library file version.
	*
	* @param treePath the tree path of this document library file version
	*/
	@Override
	public void setTreePath(String treePath) {
		_dlFileVersion.setTreePath(treePath);
	}

	/**
	* Sets the user ID of this document library file version.
	*
	* @param userId the user ID of this document library file version
	*/
	@Override
	public void setUserId(long userId) {
		_dlFileVersion.setUserId(userId);
	}

	/**
	* Sets the user name of this document library file version.
	*
	* @param userName the user name of this document library file version
	*/
	@Override
	public void setUserName(String userName) {
		_dlFileVersion.setUserName(userName);
	}

	/**
	* Sets the user uuid of this document library file version.
	*
	* @param userUuid the user uuid of this document library file version
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_dlFileVersion.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this document library file version.
	*
	* @param uuid the uuid of this document library file version
	*/
	@Override
	public void setUuid(String uuid) {
		_dlFileVersion.setUuid(uuid);
	}

	/**
	* Sets the version of this document library file version.
	*
	* @param version the version of this document library file version
	*/
	@Override
	public void setVersion(String version) {
		_dlFileVersion.setVersion(version);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<DLFileVersion> toCacheModel() {
		return _dlFileVersion.toCacheModel();
	}

	@Override
	public DLFileVersion toEscapedModel() {
		return new DLFileVersionWrapper(_dlFileVersion.toEscapedModel());
	}

	@Override
	public String toString() {
		return _dlFileVersion.toString();
	}

	@Override
	public DLFileVersion toUnescapedModel() {
		return new DLFileVersionWrapper(_dlFileVersion.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _dlFileVersion.toXmlString();
	}

	@Override
	public void updateTreePath(String treePath) {
		_dlFileVersion.updateTreePath(treePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DLFileVersionWrapper)) {
			return false;
		}

		DLFileVersionWrapper dlFileVersionWrapper = (DLFileVersionWrapper)obj;

		if (Objects.equals(_dlFileVersion, dlFileVersionWrapper._dlFileVersion)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _dlFileVersion.getStagedModelType();
	}

	@Override
	public DLFileVersion getWrappedModel() {
		return _dlFileVersion;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _dlFileVersion.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _dlFileVersion.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_dlFileVersion.resetOriginalValues();
	}

	private final DLFileVersion _dlFileVersion;
}