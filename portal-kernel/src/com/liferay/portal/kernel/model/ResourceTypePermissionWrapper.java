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
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link ResourceTypePermission}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ResourceTypePermission
 * @deprecated As of Judson (7.1.x), with no direct replacement
 * @generated
 */
@Deprecated
@ProviderType
public class ResourceTypePermissionWrapper implements ResourceTypePermission,
	ModelWrapper<ResourceTypePermission> {
	public ResourceTypePermissionWrapper(
		ResourceTypePermission resourceTypePermission) {
		_resourceTypePermission = resourceTypePermission;
	}

	@Override
	public Class<?> getModelClass() {
		return ResourceTypePermission.class;
	}

	@Override
	public String getModelClassName() {
		return ResourceTypePermission.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<ResourceTypePermission, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<ResourceTypePermission, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<ResourceTypePermission, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<ResourceTypePermission, Object>> attributeSetters =
			getAttributeSetters();

		for (Map.Entry<String, BiConsumer<ResourceTypePermission, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<ResourceTypePermission, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<ResourceTypePermission, Object>> getAttributeGetters() {
		return _resourceTypePermission.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<ResourceTypePermission, Object>> getAttributeSetters() {
		return _resourceTypePermission.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new ResourceTypePermissionWrapper((ResourceTypePermission)_resourceTypePermission.clone());
	}

	@Override
	public int compareTo(ResourceTypePermission resourceTypePermission) {
		return _resourceTypePermission.compareTo(resourceTypePermission);
	}

	/**
	* Returns the action IDs of this resource type permission.
	*
	* @return the action IDs of this resource type permission
	*/
	@Override
	public long getActionIds() {
		return _resourceTypePermission.getActionIds();
	}

	/**
	* Returns the company ID of this resource type permission.
	*
	* @return the company ID of this resource type permission
	*/
	@Override
	public long getCompanyId() {
		return _resourceTypePermission.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _resourceTypePermission.getExpandoBridge();
	}

	/**
	* Returns the group ID of this resource type permission.
	*
	* @return the group ID of this resource type permission
	*/
	@Override
	public long getGroupId() {
		return _resourceTypePermission.getGroupId();
	}

	/**
	* Returns the mvcc version of this resource type permission.
	*
	* @return the mvcc version of this resource type permission
	*/
	@Override
	public long getMvccVersion() {
		return _resourceTypePermission.getMvccVersion();
	}

	/**
	* Returns the name of this resource type permission.
	*
	* @return the name of this resource type permission
	*/
	@Override
	public String getName() {
		return _resourceTypePermission.getName();
	}

	/**
	* Returns the primary key of this resource type permission.
	*
	* @return the primary key of this resource type permission
	*/
	@Override
	public long getPrimaryKey() {
		return _resourceTypePermission.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _resourceTypePermission.getPrimaryKeyObj();
	}

	/**
	* Returns the resource type permission ID of this resource type permission.
	*
	* @return the resource type permission ID of this resource type permission
	*/
	@Override
	public long getResourceTypePermissionId() {
		return _resourceTypePermission.getResourceTypePermissionId();
	}

	/**
	* Returns the role ID of this resource type permission.
	*
	* @return the role ID of this resource type permission
	*/
	@Override
	public long getRoleId() {
		return _resourceTypePermission.getRoleId();
	}

	@Override
	public boolean hasAction(ResourceAction resourceAction) {
		return _resourceTypePermission.hasAction(resourceAction);
	}

	@Override
	public int hashCode() {
		return _resourceTypePermission.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _resourceTypePermission.isCachedModel();
	}

	@Override
	public boolean isCompanyScope() {
		return _resourceTypePermission.isCompanyScope();
	}

	@Override
	public boolean isEscapedModel() {
		return _resourceTypePermission.isEscapedModel();
	}

	@Override
	public boolean isGroupScope() {
		return _resourceTypePermission.isGroupScope();
	}

	@Override
	public boolean isNew() {
		return _resourceTypePermission.isNew();
	}

	@Override
	public void persist() {
		_resourceTypePermission.persist();
	}

	/**
	* Sets the action IDs of this resource type permission.
	*
	* @param actionIds the action IDs of this resource type permission
	*/
	@Override
	public void setActionIds(long actionIds) {
		_resourceTypePermission.setActionIds(actionIds);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_resourceTypePermission.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this resource type permission.
	*
	* @param companyId the company ID of this resource type permission
	*/
	@Override
	public void setCompanyId(long companyId) {
		_resourceTypePermission.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_resourceTypePermission.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_resourceTypePermission.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_resourceTypePermission.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this resource type permission.
	*
	* @param groupId the group ID of this resource type permission
	*/
	@Override
	public void setGroupId(long groupId) {
		_resourceTypePermission.setGroupId(groupId);
	}

	/**
	* Sets the mvcc version of this resource type permission.
	*
	* @param mvccVersion the mvcc version of this resource type permission
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_resourceTypePermission.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this resource type permission.
	*
	* @param name the name of this resource type permission
	*/
	@Override
	public void setName(String name) {
		_resourceTypePermission.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_resourceTypePermission.setNew(n);
	}

	/**
	* Sets the primary key of this resource type permission.
	*
	* @param primaryKey the primary key of this resource type permission
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_resourceTypePermission.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_resourceTypePermission.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the resource type permission ID of this resource type permission.
	*
	* @param resourceTypePermissionId the resource type permission ID of this resource type permission
	*/
	@Override
	public void setResourceTypePermissionId(long resourceTypePermissionId) {
		_resourceTypePermission.setResourceTypePermissionId(resourceTypePermissionId);
	}

	/**
	* Sets the role ID of this resource type permission.
	*
	* @param roleId the role ID of this resource type permission
	*/
	@Override
	public void setRoleId(long roleId) {
		_resourceTypePermission.setRoleId(roleId);
	}

	@Override
	public CacheModel<ResourceTypePermission> toCacheModel() {
		return _resourceTypePermission.toCacheModel();
	}

	@Override
	public ResourceTypePermission toEscapedModel() {
		return new ResourceTypePermissionWrapper(_resourceTypePermission.toEscapedModel());
	}

	@Override
	public String toString() {
		return _resourceTypePermission.toString();
	}

	@Override
	public ResourceTypePermission toUnescapedModel() {
		return new ResourceTypePermissionWrapper(_resourceTypePermission.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _resourceTypePermission.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ResourceTypePermissionWrapper)) {
			return false;
		}

		ResourceTypePermissionWrapper resourceTypePermissionWrapper = (ResourceTypePermissionWrapper)obj;

		if (Objects.equals(_resourceTypePermission,
					resourceTypePermissionWrapper._resourceTypePermission)) {
			return true;
		}

		return false;
	}

	@Override
	public ResourceTypePermission getWrappedModel() {
		return _resourceTypePermission;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _resourceTypePermission.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _resourceTypePermission.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_resourceTypePermission.resetOriginalValues();
	}

	private final ResourceTypePermission _resourceTypePermission;
}