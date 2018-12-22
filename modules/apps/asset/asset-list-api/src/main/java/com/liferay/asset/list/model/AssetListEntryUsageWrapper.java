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

package com.liferay.asset.list.model;

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
 * This class is a wrapper for {@link AssetListEntryUsage}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetListEntryUsage
 * @generated
 */
@ProviderType
public class AssetListEntryUsageWrapper implements AssetListEntryUsage,
	ModelWrapper<AssetListEntryUsage> {
	public AssetListEntryUsageWrapper(AssetListEntryUsage assetListEntryUsage) {
		_assetListEntryUsage = assetListEntryUsage;
	}

	@Override
	public Class<?> getModelClass() {
		return AssetListEntryUsage.class;
	}

	@Override
	public String getModelClassName() {
		return AssetListEntryUsage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AssetListEntryUsage, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<AssetListEntryUsage, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetListEntryUsage, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AssetListEntryUsage, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<AssetListEntryUsage, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<AssetListEntryUsage, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<AssetListEntryUsage, Object>> getAttributeGetters() {
		return _assetListEntryUsage.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<AssetListEntryUsage, Object>> getAttributeSetters() {
		return _assetListEntryUsage.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AssetListEntryUsageWrapper((AssetListEntryUsage)_assetListEntryUsage.clone());
	}

	@Override
	public int compareTo(AssetListEntryUsage assetListEntryUsage) {
		return _assetListEntryUsage.compareTo(assetListEntryUsage);
	}

	/**
	* Returns the asset list entry ID of this asset list entry usage.
	*
	* @return the asset list entry ID of this asset list entry usage
	*/
	@Override
	public long getAssetListEntryId() {
		return _assetListEntryUsage.getAssetListEntryId();
	}

	/**
	* Returns the asset list entry usage ID of this asset list entry usage.
	*
	* @return the asset list entry usage ID of this asset list entry usage
	*/
	@Override
	public long getAssetListEntryUsageId() {
		return _assetListEntryUsage.getAssetListEntryUsageId();
	}

	/**
	* Returns the fully qualified class name of this asset list entry usage.
	*
	* @return the fully qualified class name of this asset list entry usage
	*/
	@Override
	public String getClassName() {
		return _assetListEntryUsage.getClassName();
	}

	/**
	* Returns the class name ID of this asset list entry usage.
	*
	* @return the class name ID of this asset list entry usage
	*/
	@Override
	public long getClassNameId() {
		return _assetListEntryUsage.getClassNameId();
	}

	/**
	* Returns the class pk of this asset list entry usage.
	*
	* @return the class pk of this asset list entry usage
	*/
	@Override
	public long getClassPK() {
		return _assetListEntryUsage.getClassPK();
	}

	/**
	* Returns the company ID of this asset list entry usage.
	*
	* @return the company ID of this asset list entry usage
	*/
	@Override
	public long getCompanyId() {
		return _assetListEntryUsage.getCompanyId();
	}

	/**
	* Returns the create date of this asset list entry usage.
	*
	* @return the create date of this asset list entry usage
	*/
	@Override
	public Date getCreateDate() {
		return _assetListEntryUsage.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _assetListEntryUsage.getExpandoBridge();
	}

	/**
	* Returns the group ID of this asset list entry usage.
	*
	* @return the group ID of this asset list entry usage
	*/
	@Override
	public long getGroupId() {
		return _assetListEntryUsage.getGroupId();
	}

	/**
	* Returns the last publish date of this asset list entry usage.
	*
	* @return the last publish date of this asset list entry usage
	*/
	@Override
	public Date getLastPublishDate() {
		return _assetListEntryUsage.getLastPublishDate();
	}

	/**
	* Returns the modified date of this asset list entry usage.
	*
	* @return the modified date of this asset list entry usage
	*/
	@Override
	public Date getModifiedDate() {
		return _assetListEntryUsage.getModifiedDate();
	}

	/**
	* Returns the portlet ID of this asset list entry usage.
	*
	* @return the portlet ID of this asset list entry usage
	*/
	@Override
	public String getPortletId() {
		return _assetListEntryUsage.getPortletId();
	}

	/**
	* Returns the primary key of this asset list entry usage.
	*
	* @return the primary key of this asset list entry usage
	*/
	@Override
	public long getPrimaryKey() {
		return _assetListEntryUsage.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _assetListEntryUsage.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this asset list entry usage.
	*
	* @return the user ID of this asset list entry usage
	*/
	@Override
	public long getUserId() {
		return _assetListEntryUsage.getUserId();
	}

	/**
	* Returns the user name of this asset list entry usage.
	*
	* @return the user name of this asset list entry usage
	*/
	@Override
	public String getUserName() {
		return _assetListEntryUsage.getUserName();
	}

	/**
	* Returns the user uuid of this asset list entry usage.
	*
	* @return the user uuid of this asset list entry usage
	*/
	@Override
	public String getUserUuid() {
		return _assetListEntryUsage.getUserUuid();
	}

	/**
	* Returns the uuid of this asset list entry usage.
	*
	* @return the uuid of this asset list entry usage
	*/
	@Override
	public String getUuid() {
		return _assetListEntryUsage.getUuid();
	}

	@Override
	public int hashCode() {
		return _assetListEntryUsage.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _assetListEntryUsage.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _assetListEntryUsage.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _assetListEntryUsage.isNew();
	}

	@Override
	public void persist() {
		_assetListEntryUsage.persist();
	}

	/**
	* Sets the asset list entry ID of this asset list entry usage.
	*
	* @param assetListEntryId the asset list entry ID of this asset list entry usage
	*/
	@Override
	public void setAssetListEntryId(long assetListEntryId) {
		_assetListEntryUsage.setAssetListEntryId(assetListEntryId);
	}

	/**
	* Sets the asset list entry usage ID of this asset list entry usage.
	*
	* @param assetListEntryUsageId the asset list entry usage ID of this asset list entry usage
	*/
	@Override
	public void setAssetListEntryUsageId(long assetListEntryUsageId) {
		_assetListEntryUsage.setAssetListEntryUsageId(assetListEntryUsageId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_assetListEntryUsage.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_assetListEntryUsage.setClassName(className);
	}

	/**
	* Sets the class name ID of this asset list entry usage.
	*
	* @param classNameId the class name ID of this asset list entry usage
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_assetListEntryUsage.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this asset list entry usage.
	*
	* @param classPK the class pk of this asset list entry usage
	*/
	@Override
	public void setClassPK(long classPK) {
		_assetListEntryUsage.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this asset list entry usage.
	*
	* @param companyId the company ID of this asset list entry usage
	*/
	@Override
	public void setCompanyId(long companyId) {
		_assetListEntryUsage.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this asset list entry usage.
	*
	* @param createDate the create date of this asset list entry usage
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_assetListEntryUsage.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_assetListEntryUsage.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_assetListEntryUsage.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_assetListEntryUsage.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this asset list entry usage.
	*
	* @param groupId the group ID of this asset list entry usage
	*/
	@Override
	public void setGroupId(long groupId) {
		_assetListEntryUsage.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this asset list entry usage.
	*
	* @param lastPublishDate the last publish date of this asset list entry usage
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_assetListEntryUsage.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this asset list entry usage.
	*
	* @param modifiedDate the modified date of this asset list entry usage
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_assetListEntryUsage.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_assetListEntryUsage.setNew(n);
	}

	/**
	* Sets the portlet ID of this asset list entry usage.
	*
	* @param portletId the portlet ID of this asset list entry usage
	*/
	@Override
	public void setPortletId(String portletId) {
		_assetListEntryUsage.setPortletId(portletId);
	}

	/**
	* Sets the primary key of this asset list entry usage.
	*
	* @param primaryKey the primary key of this asset list entry usage
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_assetListEntryUsage.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_assetListEntryUsage.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this asset list entry usage.
	*
	* @param userId the user ID of this asset list entry usage
	*/
	@Override
	public void setUserId(long userId) {
		_assetListEntryUsage.setUserId(userId);
	}

	/**
	* Sets the user name of this asset list entry usage.
	*
	* @param userName the user name of this asset list entry usage
	*/
	@Override
	public void setUserName(String userName) {
		_assetListEntryUsage.setUserName(userName);
	}

	/**
	* Sets the user uuid of this asset list entry usage.
	*
	* @param userUuid the user uuid of this asset list entry usage
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_assetListEntryUsage.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this asset list entry usage.
	*
	* @param uuid the uuid of this asset list entry usage
	*/
	@Override
	public void setUuid(String uuid) {
		_assetListEntryUsage.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<AssetListEntryUsage> toCacheModel() {
		return _assetListEntryUsage.toCacheModel();
	}

	@Override
	public AssetListEntryUsage toEscapedModel() {
		return new AssetListEntryUsageWrapper(_assetListEntryUsage.toEscapedModel());
	}

	@Override
	public String toString() {
		return _assetListEntryUsage.toString();
	}

	@Override
	public AssetListEntryUsage toUnescapedModel() {
		return new AssetListEntryUsageWrapper(_assetListEntryUsage.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _assetListEntryUsage.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetListEntryUsageWrapper)) {
			return false;
		}

		AssetListEntryUsageWrapper assetListEntryUsageWrapper = (AssetListEntryUsageWrapper)obj;

		if (Objects.equals(_assetListEntryUsage,
					assetListEntryUsageWrapper._assetListEntryUsage)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _assetListEntryUsage.getStagedModelType();
	}

	@Override
	public AssetListEntryUsage getWrappedModel() {
		return _assetListEntryUsage;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _assetListEntryUsage.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _assetListEntryUsage.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_assetListEntryUsage.resetOriginalValues();
	}

	private final AssetListEntryUsage _assetListEntryUsage;
}