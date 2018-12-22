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

package com.liferay.fragment.model;

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
 * This class is a wrapper for {@link FragmentEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see FragmentEntry
 * @generated
 */
@ProviderType
public class FragmentEntryWrapper implements FragmentEntry,
	ModelWrapper<FragmentEntry> {
	public FragmentEntryWrapper(FragmentEntry fragmentEntry) {
		_fragmentEntry = fragmentEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return FragmentEntry.class;
	}

	@Override
	public String getModelClassName() {
		return FragmentEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<FragmentEntry, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<FragmentEntry, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<FragmentEntry, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<FragmentEntry, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<FragmentEntry, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<FragmentEntry, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<FragmentEntry, Object>> getAttributeGetters() {
		return _fragmentEntry.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<FragmentEntry, Object>> getAttributeSetters() {
		return _fragmentEntry.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new FragmentEntryWrapper((FragmentEntry)_fragmentEntry.clone());
	}

	@Override
	public int compareTo(FragmentEntry fragmentEntry) {
		return _fragmentEntry.compareTo(fragmentEntry);
	}

	/**
	* Returns the company ID of this fragment entry.
	*
	* @return the company ID of this fragment entry
	*/
	@Override
	public long getCompanyId() {
		return _fragmentEntry.getCompanyId();
	}

	@Override
	public String getContent() {
		return _fragmentEntry.getContent();
	}

	/**
	* Returns the create date of this fragment entry.
	*
	* @return the create date of this fragment entry
	*/
	@Override
	public Date getCreateDate() {
		return _fragmentEntry.getCreateDate();
	}

	/**
	* Returns the css of this fragment entry.
	*
	* @return the css of this fragment entry
	*/
	@Override
	public String getCss() {
		return _fragmentEntry.getCss();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _fragmentEntry.getExpandoBridge();
	}

	/**
	* Returns the fragment collection ID of this fragment entry.
	*
	* @return the fragment collection ID of this fragment entry
	*/
	@Override
	public long getFragmentCollectionId() {
		return _fragmentEntry.getFragmentCollectionId();
	}

	/**
	* Returns the fragment entry ID of this fragment entry.
	*
	* @return the fragment entry ID of this fragment entry
	*/
	@Override
	public long getFragmentEntryId() {
		return _fragmentEntry.getFragmentEntryId();
	}

	/**
	* Returns the fragment entry key of this fragment entry.
	*
	* @return the fragment entry key of this fragment entry
	*/
	@Override
	public String getFragmentEntryKey() {
		return _fragmentEntry.getFragmentEntryKey();
	}

	/**
	* Returns the group ID of this fragment entry.
	*
	* @return the group ID of this fragment entry
	*/
	@Override
	public long getGroupId() {
		return _fragmentEntry.getGroupId();
	}

	/**
	* Returns the html of this fragment entry.
	*
	* @return the html of this fragment entry
	*/
	@Override
	public String getHtml() {
		return _fragmentEntry.getHtml();
	}

	@Override
	public String getImagePreviewURL(
		com.liferay.portal.kernel.theme.ThemeDisplay themeDisplay) {
		return _fragmentEntry.getImagePreviewURL(themeDisplay);
	}

	/**
	* Returns the js of this fragment entry.
	*
	* @return the js of this fragment entry
	*/
	@Override
	public String getJs() {
		return _fragmentEntry.getJs();
	}

	/**
	* Returns the last publish date of this fragment entry.
	*
	* @return the last publish date of this fragment entry
	*/
	@Override
	public Date getLastPublishDate() {
		return _fragmentEntry.getLastPublishDate();
	}

	/**
	* Returns the modified date of this fragment entry.
	*
	* @return the modified date of this fragment entry
	*/
	@Override
	public Date getModifiedDate() {
		return _fragmentEntry.getModifiedDate();
	}

	/**
	* Returns the name of this fragment entry.
	*
	* @return the name of this fragment entry
	*/
	@Override
	public String getName() {
		return _fragmentEntry.getName();
	}

	/**
	* Returns the preview file entry ID of this fragment entry.
	*
	* @return the preview file entry ID of this fragment entry
	*/
	@Override
	public long getPreviewFileEntryId() {
		return _fragmentEntry.getPreviewFileEntryId();
	}

	/**
	* Returns the primary key of this fragment entry.
	*
	* @return the primary key of this fragment entry
	*/
	@Override
	public long getPrimaryKey() {
		return _fragmentEntry.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _fragmentEntry.getPrimaryKeyObj();
	}

	/**
	* Returns the status of this fragment entry.
	*
	* @return the status of this fragment entry
	*/
	@Override
	public int getStatus() {
		return _fragmentEntry.getStatus();
	}

	/**
	* Returns the status by user ID of this fragment entry.
	*
	* @return the status by user ID of this fragment entry
	*/
	@Override
	public long getStatusByUserId() {
		return _fragmentEntry.getStatusByUserId();
	}

	/**
	* Returns the status by user name of this fragment entry.
	*
	* @return the status by user name of this fragment entry
	*/
	@Override
	public String getStatusByUserName() {
		return _fragmentEntry.getStatusByUserName();
	}

	/**
	* Returns the status by user uuid of this fragment entry.
	*
	* @return the status by user uuid of this fragment entry
	*/
	@Override
	public String getStatusByUserUuid() {
		return _fragmentEntry.getStatusByUserUuid();
	}

	/**
	* Returns the status date of this fragment entry.
	*
	* @return the status date of this fragment entry
	*/
	@Override
	public Date getStatusDate() {
		return _fragmentEntry.getStatusDate();
	}

	/**
	* Returns the type of this fragment entry.
	*
	* @return the type of this fragment entry
	*/
	@Override
	public int getType() {
		return _fragmentEntry.getType();
	}

	@Override
	public String getTypeLabel() {
		return _fragmentEntry.getTypeLabel();
	}

	@Override
	public int getUsageCount() {
		return _fragmentEntry.getUsageCount();
	}

	/**
	* Returns the user ID of this fragment entry.
	*
	* @return the user ID of this fragment entry
	*/
	@Override
	public long getUserId() {
		return _fragmentEntry.getUserId();
	}

	/**
	* Returns the user name of this fragment entry.
	*
	* @return the user name of this fragment entry
	*/
	@Override
	public String getUserName() {
		return _fragmentEntry.getUserName();
	}

	/**
	* Returns the user uuid of this fragment entry.
	*
	* @return the user uuid of this fragment entry
	*/
	@Override
	public String getUserUuid() {
		return _fragmentEntry.getUserUuid();
	}

	/**
	* Returns the uuid of this fragment entry.
	*
	* @return the uuid of this fragment entry
	*/
	@Override
	public String getUuid() {
		return _fragmentEntry.getUuid();
	}

	@Override
	public int hashCode() {
		return _fragmentEntry.hashCode();
	}

	/**
	* Returns <code>true</code> if this fragment entry is approved.
	*
	* @return <code>true</code> if this fragment entry is approved; <code>false</code> otherwise
	*/
	@Override
	public boolean isApproved() {
		return _fragmentEntry.isApproved();
	}

	@Override
	public boolean isCachedModel() {
		return _fragmentEntry.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this fragment entry is denied.
	*
	* @return <code>true</code> if this fragment entry is denied; <code>false</code> otherwise
	*/
	@Override
	public boolean isDenied() {
		return _fragmentEntry.isDenied();
	}

	/**
	* Returns <code>true</code> if this fragment entry is a draft.
	*
	* @return <code>true</code> if this fragment entry is a draft; <code>false</code> otherwise
	*/
	@Override
	public boolean isDraft() {
		return _fragmentEntry.isDraft();
	}

	@Override
	public boolean isEscapedModel() {
		return _fragmentEntry.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this fragment entry is expired.
	*
	* @return <code>true</code> if this fragment entry is expired; <code>false</code> otherwise
	*/
	@Override
	public boolean isExpired() {
		return _fragmentEntry.isExpired();
	}

	/**
	* Returns <code>true</code> if this fragment entry is inactive.
	*
	* @return <code>true</code> if this fragment entry is inactive; <code>false</code> otherwise
	*/
	@Override
	public boolean isInactive() {
		return _fragmentEntry.isInactive();
	}

	/**
	* Returns <code>true</code> if this fragment entry is incomplete.
	*
	* @return <code>true</code> if this fragment entry is incomplete; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomplete() {
		return _fragmentEntry.isIncomplete();
	}

	@Override
	public boolean isNew() {
		return _fragmentEntry.isNew();
	}

	/**
	* Returns <code>true</code> if this fragment entry is pending.
	*
	* @return <code>true</code> if this fragment entry is pending; <code>false</code> otherwise
	*/
	@Override
	public boolean isPending() {
		return _fragmentEntry.isPending();
	}

	/**
	* Returns <code>true</code> if this fragment entry is scheduled.
	*
	* @return <code>true</code> if this fragment entry is scheduled; <code>false</code> otherwise
	*/
	@Override
	public boolean isScheduled() {
		return _fragmentEntry.isScheduled();
	}

	@Override
	public void persist() {
		_fragmentEntry.persist();
	}

	@Override
	public void populateZipWriter(
		com.liferay.portal.kernel.zip.ZipWriter zipWriter, String path)
		throws Exception {
		_fragmentEntry.populateZipWriter(zipWriter, path);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_fragmentEntry.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this fragment entry.
	*
	* @param companyId the company ID of this fragment entry
	*/
	@Override
	public void setCompanyId(long companyId) {
		_fragmentEntry.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this fragment entry.
	*
	* @param createDate the create date of this fragment entry
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_fragmentEntry.setCreateDate(createDate);
	}

	/**
	* Sets the css of this fragment entry.
	*
	* @param css the css of this fragment entry
	*/
	@Override
	public void setCss(String css) {
		_fragmentEntry.setCss(css);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_fragmentEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_fragmentEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_fragmentEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the fragment collection ID of this fragment entry.
	*
	* @param fragmentCollectionId the fragment collection ID of this fragment entry
	*/
	@Override
	public void setFragmentCollectionId(long fragmentCollectionId) {
		_fragmentEntry.setFragmentCollectionId(fragmentCollectionId);
	}

	/**
	* Sets the fragment entry ID of this fragment entry.
	*
	* @param fragmentEntryId the fragment entry ID of this fragment entry
	*/
	@Override
	public void setFragmentEntryId(long fragmentEntryId) {
		_fragmentEntry.setFragmentEntryId(fragmentEntryId);
	}

	/**
	* Sets the fragment entry key of this fragment entry.
	*
	* @param fragmentEntryKey the fragment entry key of this fragment entry
	*/
	@Override
	public void setFragmentEntryKey(String fragmentEntryKey) {
		_fragmentEntry.setFragmentEntryKey(fragmentEntryKey);
	}

	/**
	* Sets the group ID of this fragment entry.
	*
	* @param groupId the group ID of this fragment entry
	*/
	@Override
	public void setGroupId(long groupId) {
		_fragmentEntry.setGroupId(groupId);
	}

	/**
	* Sets the html of this fragment entry.
	*
	* @param html the html of this fragment entry
	*/
	@Override
	public void setHtml(String html) {
		_fragmentEntry.setHtml(html);
	}

	/**
	* Sets the js of this fragment entry.
	*
	* @param js the js of this fragment entry
	*/
	@Override
	public void setJs(String js) {
		_fragmentEntry.setJs(js);
	}

	/**
	* Sets the last publish date of this fragment entry.
	*
	* @param lastPublishDate the last publish date of this fragment entry
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_fragmentEntry.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this fragment entry.
	*
	* @param modifiedDate the modified date of this fragment entry
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_fragmentEntry.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this fragment entry.
	*
	* @param name the name of this fragment entry
	*/
	@Override
	public void setName(String name) {
		_fragmentEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_fragmentEntry.setNew(n);
	}

	/**
	* Sets the preview file entry ID of this fragment entry.
	*
	* @param previewFileEntryId the preview file entry ID of this fragment entry
	*/
	@Override
	public void setPreviewFileEntryId(long previewFileEntryId) {
		_fragmentEntry.setPreviewFileEntryId(previewFileEntryId);
	}

	/**
	* Sets the primary key of this fragment entry.
	*
	* @param primaryKey the primary key of this fragment entry
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_fragmentEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_fragmentEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the status of this fragment entry.
	*
	* @param status the status of this fragment entry
	*/
	@Override
	public void setStatus(int status) {
		_fragmentEntry.setStatus(status);
	}

	/**
	* Sets the status by user ID of this fragment entry.
	*
	* @param statusByUserId the status by user ID of this fragment entry
	*/
	@Override
	public void setStatusByUserId(long statusByUserId) {
		_fragmentEntry.setStatusByUserId(statusByUserId);
	}

	/**
	* Sets the status by user name of this fragment entry.
	*
	* @param statusByUserName the status by user name of this fragment entry
	*/
	@Override
	public void setStatusByUserName(String statusByUserName) {
		_fragmentEntry.setStatusByUserName(statusByUserName);
	}

	/**
	* Sets the status by user uuid of this fragment entry.
	*
	* @param statusByUserUuid the status by user uuid of this fragment entry
	*/
	@Override
	public void setStatusByUserUuid(String statusByUserUuid) {
		_fragmentEntry.setStatusByUserUuid(statusByUserUuid);
	}

	/**
	* Sets the status date of this fragment entry.
	*
	* @param statusDate the status date of this fragment entry
	*/
	@Override
	public void setStatusDate(Date statusDate) {
		_fragmentEntry.setStatusDate(statusDate);
	}

	/**
	* Sets the type of this fragment entry.
	*
	* @param type the type of this fragment entry
	*/
	@Override
	public void setType(int type) {
		_fragmentEntry.setType(type);
	}

	/**
	* Sets the user ID of this fragment entry.
	*
	* @param userId the user ID of this fragment entry
	*/
	@Override
	public void setUserId(long userId) {
		_fragmentEntry.setUserId(userId);
	}

	/**
	* Sets the user name of this fragment entry.
	*
	* @param userName the user name of this fragment entry
	*/
	@Override
	public void setUserName(String userName) {
		_fragmentEntry.setUserName(userName);
	}

	/**
	* Sets the user uuid of this fragment entry.
	*
	* @param userUuid the user uuid of this fragment entry
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_fragmentEntry.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this fragment entry.
	*
	* @param uuid the uuid of this fragment entry
	*/
	@Override
	public void setUuid(String uuid) {
		_fragmentEntry.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<FragmentEntry> toCacheModel() {
		return _fragmentEntry.toCacheModel();
	}

	@Override
	public FragmentEntry toEscapedModel() {
		return new FragmentEntryWrapper(_fragmentEntry.toEscapedModel());
	}

	@Override
	public String toString() {
		return _fragmentEntry.toString();
	}

	@Override
	public FragmentEntry toUnescapedModel() {
		return new FragmentEntryWrapper(_fragmentEntry.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _fragmentEntry.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FragmentEntryWrapper)) {
			return false;
		}

		FragmentEntryWrapper fragmentEntryWrapper = (FragmentEntryWrapper)obj;

		if (Objects.equals(_fragmentEntry, fragmentEntryWrapper._fragmentEntry)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _fragmentEntry.getStagedModelType();
	}

	@Override
	public FragmentEntry getWrappedModel() {
		return _fragmentEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _fragmentEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _fragmentEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_fragmentEntry.resetOriginalValues();
	}

	private final FragmentEntry _fragmentEntry;
}