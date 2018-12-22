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

package com.liferay.asset.model;

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
 * This class is a wrapper for {@link AssetEntryUsage}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryUsage
 * @generated
 */
@ProviderType
public class AssetEntryUsageWrapper implements AssetEntryUsage,
	ModelWrapper<AssetEntryUsage> {
	public AssetEntryUsageWrapper(AssetEntryUsage assetEntryUsage) {
		_assetEntryUsage = assetEntryUsage;
	}

	@Override
	public Class<?> getModelClass() {
		return AssetEntryUsage.class;
	}

	@Override
	public String getModelClassName() {
		return AssetEntryUsage.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<AssetEntryUsage, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<AssetEntryUsage, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<AssetEntryUsage, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<AssetEntryUsage, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<AssetEntryUsage, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<AssetEntryUsage, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<AssetEntryUsage, Object>> getAttributeGetters() {
		return _assetEntryUsage.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<AssetEntryUsage, Object>> getAttributeSetters() {
		return _assetEntryUsage.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AssetEntryUsageWrapper((AssetEntryUsage)_assetEntryUsage.clone());
	}

	@Override
	public int compareTo(AssetEntryUsage assetEntryUsage) {
		return _assetEntryUsage.compareTo(assetEntryUsage);
	}

	/**
	* Returns the asset entry ID of this asset entry usage.
	*
	* @return the asset entry ID of this asset entry usage
	*/
	@Override
	public long getAssetEntryId() {
		return _assetEntryUsage.getAssetEntryId();
	}

	/**
	* Returns the asset entry usage ID of this asset entry usage.
	*
	* @return the asset entry usage ID of this asset entry usage
	*/
	@Override
	public long getAssetEntryUsageId() {
		return _assetEntryUsage.getAssetEntryUsageId();
	}

	/**
	* Returns the fully qualified class name of this asset entry usage.
	*
	* @return the fully qualified class name of this asset entry usage
	*/
	@Override
	public String getClassName() {
		return _assetEntryUsage.getClassName();
	}

	/**
	* Returns the class name ID of this asset entry usage.
	*
	* @return the class name ID of this asset entry usage
	*/
	@Override
	public long getClassNameId() {
		return _assetEntryUsage.getClassNameId();
	}

	/**
	* Returns the class pk of this asset entry usage.
	*
	* @return the class pk of this asset entry usage
	*/
	@Override
	public long getClassPK() {
		return _assetEntryUsage.getClassPK();
	}

	/**
	* Returns the company ID of this asset entry usage.
	*
	* @return the company ID of this asset entry usage
	*/
	@Override
	public long getCompanyId() {
		return _assetEntryUsage.getCompanyId();
	}

	/**
	* Returns the create date of this asset entry usage.
	*
	* @return the create date of this asset entry usage
	*/
	@Override
	public Date getCreateDate() {
		return _assetEntryUsage.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _assetEntryUsage.getExpandoBridge();
	}

	/**
	* Returns the group ID of this asset entry usage.
	*
	* @return the group ID of this asset entry usage
	*/
	@Override
	public long getGroupId() {
		return _assetEntryUsage.getGroupId();
	}

	/**
	* Returns the last publish date of this asset entry usage.
	*
	* @return the last publish date of this asset entry usage
	*/
	@Override
	public Date getLastPublishDate() {
		return _assetEntryUsage.getLastPublishDate();
	}

	/**
	* Returns the modified date of this asset entry usage.
	*
	* @return the modified date of this asset entry usage
	*/
	@Override
	public Date getModifiedDate() {
		return _assetEntryUsage.getModifiedDate();
	}

	/**
	* Returns the portlet ID of this asset entry usage.
	*
	* @return the portlet ID of this asset entry usage
	*/
	@Override
	public String getPortletId() {
		return _assetEntryUsage.getPortletId();
	}

	/**
	* Returns the primary key of this asset entry usage.
	*
	* @return the primary key of this asset entry usage
	*/
	@Override
	public long getPrimaryKey() {
		return _assetEntryUsage.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _assetEntryUsage.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this asset entry usage.
	*
	* @return the user ID of this asset entry usage
	*/
	@Override
	public long getUserId() {
		return _assetEntryUsage.getUserId();
	}

	/**
	* Returns the user name of this asset entry usage.
	*
	* @return the user name of this asset entry usage
	*/
	@Override
	public String getUserName() {
		return _assetEntryUsage.getUserName();
	}

	/**
	* Returns the user uuid of this asset entry usage.
	*
	* @return the user uuid of this asset entry usage
	*/
	@Override
	public String getUserUuid() {
		return _assetEntryUsage.getUserUuid();
	}

	/**
	* Returns the uuid of this asset entry usage.
	*
	* @return the uuid of this asset entry usage
	*/
	@Override
	public String getUuid() {
		return _assetEntryUsage.getUuid();
	}

	@Override
	public int hashCode() {
		return _assetEntryUsage.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _assetEntryUsage.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _assetEntryUsage.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _assetEntryUsage.isNew();
	}

	@Override
	public void persist() {
		_assetEntryUsage.persist();
	}

	/**
	* Sets the asset entry ID of this asset entry usage.
	*
	* @param assetEntryId the asset entry ID of this asset entry usage
	*/
	@Override
	public void setAssetEntryId(long assetEntryId) {
		_assetEntryUsage.setAssetEntryId(assetEntryId);
	}

	/**
	* Sets the asset entry usage ID of this asset entry usage.
	*
	* @param assetEntryUsageId the asset entry usage ID of this asset entry usage
	*/
	@Override
	public void setAssetEntryUsageId(long assetEntryUsageId) {
		_assetEntryUsage.setAssetEntryUsageId(assetEntryUsageId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_assetEntryUsage.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_assetEntryUsage.setClassName(className);
	}

	/**
	* Sets the class name ID of this asset entry usage.
	*
	* @param classNameId the class name ID of this asset entry usage
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_assetEntryUsage.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this asset entry usage.
	*
	* @param classPK the class pk of this asset entry usage
	*/
	@Override
	public void setClassPK(long classPK) {
		_assetEntryUsage.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this asset entry usage.
	*
	* @param companyId the company ID of this asset entry usage
	*/
	@Override
	public void setCompanyId(long companyId) {
		_assetEntryUsage.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this asset entry usage.
	*
	* @param createDate the create date of this asset entry usage
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_assetEntryUsage.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_assetEntryUsage.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_assetEntryUsage.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_assetEntryUsage.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this asset entry usage.
	*
	* @param groupId the group ID of this asset entry usage
	*/
	@Override
	public void setGroupId(long groupId) {
		_assetEntryUsage.setGroupId(groupId);
	}

	/**
	* Sets the last publish date of this asset entry usage.
	*
	* @param lastPublishDate the last publish date of this asset entry usage
	*/
	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_assetEntryUsage.setLastPublishDate(lastPublishDate);
	}

	/**
	* Sets the modified date of this asset entry usage.
	*
	* @param modifiedDate the modified date of this asset entry usage
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_assetEntryUsage.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_assetEntryUsage.setNew(n);
	}

	/**
	* Sets the portlet ID of this asset entry usage.
	*
	* @param portletId the portlet ID of this asset entry usage
	*/
	@Override
	public void setPortletId(String portletId) {
		_assetEntryUsage.setPortletId(portletId);
	}

	/**
	* Sets the primary key of this asset entry usage.
	*
	* @param primaryKey the primary key of this asset entry usage
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_assetEntryUsage.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_assetEntryUsage.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this asset entry usage.
	*
	* @param userId the user ID of this asset entry usage
	*/
	@Override
	public void setUserId(long userId) {
		_assetEntryUsage.setUserId(userId);
	}

	/**
	* Sets the user name of this asset entry usage.
	*
	* @param userName the user name of this asset entry usage
	*/
	@Override
	public void setUserName(String userName) {
		_assetEntryUsage.setUserName(userName);
	}

	/**
	* Sets the user uuid of this asset entry usage.
	*
	* @param userUuid the user uuid of this asset entry usage
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_assetEntryUsage.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this asset entry usage.
	*
	* @param uuid the uuid of this asset entry usage
	*/
	@Override
	public void setUuid(String uuid) {
		_assetEntryUsage.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<AssetEntryUsage> toCacheModel() {
		return _assetEntryUsage.toCacheModel();
	}

	@Override
	public AssetEntryUsage toEscapedModel() {
		return new AssetEntryUsageWrapper(_assetEntryUsage.toEscapedModel());
	}

	@Override
	public String toString() {
		return _assetEntryUsage.toString();
	}

	@Override
	public AssetEntryUsage toUnescapedModel() {
		return new AssetEntryUsageWrapper(_assetEntryUsage.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _assetEntryUsage.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AssetEntryUsageWrapper)) {
			return false;
		}

		AssetEntryUsageWrapper assetEntryUsageWrapper = (AssetEntryUsageWrapper)obj;

		if (Objects.equals(_assetEntryUsage,
					assetEntryUsageWrapper._assetEntryUsage)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _assetEntryUsage.getStagedModelType();
	}

	@Override
	public AssetEntryUsage getWrappedModel() {
		return _assetEntryUsage;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _assetEntryUsage.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _assetEntryUsage.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_assetEntryUsage.resetOriginalValues();
	}

	private final AssetEntryUsage _assetEntryUsage;
}