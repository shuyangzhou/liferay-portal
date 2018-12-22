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
 * This class is a wrapper for {@link PluginSetting}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PluginSetting
 * @generated
 */
@ProviderType
public class PluginSettingWrapper implements PluginSetting,
	ModelWrapper<PluginSetting> {
	public PluginSettingWrapper(PluginSetting pluginSetting) {
		_pluginSetting = pluginSetting;
	}

	@Override
	public Class<?> getModelClass() {
		return PluginSetting.class;
	}

	@Override
	public String getModelClassName() {
		return PluginSetting.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PluginSetting, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<PluginSetting, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<PluginSetting, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PluginSetting, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<PluginSetting, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<PluginSetting, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<PluginSetting, Object>> getAttributeGetters() {
		return _pluginSetting.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<PluginSetting, Object>> getAttributeSetters() {
		return _pluginSetting.getAttributeSetters();
	}

	/**
	* Adds a role to the list of roles.
	*/
	@Override
	public void addRole(String role) {
		_pluginSetting.addRole(role);
	}

	@Override
	public Object clone() {
		return new PluginSettingWrapper((PluginSetting)_pluginSetting.clone());
	}

	@Override
	public int compareTo(PluginSetting pluginSetting) {
		return _pluginSetting.compareTo(pluginSetting);
	}

	/**
	* Returns the active of this plugin setting.
	*
	* @return the active of this plugin setting
	*/
	@Override
	public boolean getActive() {
		return _pluginSetting.getActive();
	}

	/**
	* Returns the company ID of this plugin setting.
	*
	* @return the company ID of this plugin setting
	*/
	@Override
	public long getCompanyId() {
		return _pluginSetting.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _pluginSetting.getExpandoBridge();
	}

	/**
	* Returns the mvcc version of this plugin setting.
	*
	* @return the mvcc version of this plugin setting
	*/
	@Override
	public long getMvccVersion() {
		return _pluginSetting.getMvccVersion();
	}

	/**
	* Returns the plugin ID of this plugin setting.
	*
	* @return the plugin ID of this plugin setting
	*/
	@Override
	public String getPluginId() {
		return _pluginSetting.getPluginId();
	}

	/**
	* Returns the plugin setting ID of this plugin setting.
	*
	* @return the plugin setting ID of this plugin setting
	*/
	@Override
	public long getPluginSettingId() {
		return _pluginSetting.getPluginSettingId();
	}

	/**
	* Returns the plugin type of this plugin setting.
	*
	* @return the plugin type of this plugin setting
	*/
	@Override
	public String getPluginType() {
		return _pluginSetting.getPluginType();
	}

	/**
	* Returns the primary key of this plugin setting.
	*
	* @return the primary key of this plugin setting
	*/
	@Override
	public long getPrimaryKey() {
		return _pluginSetting.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _pluginSetting.getPrimaryKeyObj();
	}

	/**
	* Returns the roles of this plugin setting.
	*
	* @return the roles of this plugin setting
	*/
	@Override
	public String getRoles() {
		return _pluginSetting.getRoles();
	}

	/**
	* Returns an array of required roles of the plugin.
	*
	* @return an array of required roles of the plugin
	*/
	@Override
	public String[] getRolesArray() {
		return _pluginSetting.getRolesArray();
	}

	@Override
	public int hashCode() {
		return _pluginSetting.hashCode();
	}

	/**
	* Returns <code>true</code> if the user has permission to use this plugin
	*
	* @param userId the primary key of the user
	* @return <code>true</code> if the user has permission to use this plugin
	*/
	@Override
	public boolean hasPermission(long userId) {
		return _pluginSetting.hasPermission(userId);
	}

	/**
	* Returns <code>true</code> if the plugin has a role with the specified
	* name.
	*
	* @param roleName the role name
	* @return <code>true</code> if the plugin has a role with the specified
	name
	*/
	@Override
	public boolean hasRoleWithName(String roleName) {
		return _pluginSetting.hasRoleWithName(roleName);
	}

	/**
	* Returns <code>true</code> if this plugin setting is active.
	*
	* @return <code>true</code> if this plugin setting is active; <code>false</code> otherwise
	*/
	@Override
	public boolean isActive() {
		return _pluginSetting.isActive();
	}

	@Override
	public boolean isCachedModel() {
		return _pluginSetting.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _pluginSetting.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _pluginSetting.isNew();
	}

	@Override
	public void persist() {
		_pluginSetting.persist();
	}

	/**
	* Sets whether this plugin setting is active.
	*
	* @param active the active of this plugin setting
	*/
	@Override
	public void setActive(boolean active) {
		_pluginSetting.setActive(active);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_pluginSetting.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this plugin setting.
	*
	* @param companyId the company ID of this plugin setting
	*/
	@Override
	public void setCompanyId(long companyId) {
		_pluginSetting.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_pluginSetting.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_pluginSetting.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_pluginSetting.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the mvcc version of this plugin setting.
	*
	* @param mvccVersion the mvcc version of this plugin setting
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_pluginSetting.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_pluginSetting.setNew(n);
	}

	/**
	* Sets the plugin ID of this plugin setting.
	*
	* @param pluginId the plugin ID of this plugin setting
	*/
	@Override
	public void setPluginId(String pluginId) {
		_pluginSetting.setPluginId(pluginId);
	}

	/**
	* Sets the plugin setting ID of this plugin setting.
	*
	* @param pluginSettingId the plugin setting ID of this plugin setting
	*/
	@Override
	public void setPluginSettingId(long pluginSettingId) {
		_pluginSetting.setPluginSettingId(pluginSettingId);
	}

	/**
	* Sets the plugin type of this plugin setting.
	*
	* @param pluginType the plugin type of this plugin setting
	*/
	@Override
	public void setPluginType(String pluginType) {
		_pluginSetting.setPluginType(pluginType);
	}

	/**
	* Sets the primary key of this plugin setting.
	*
	* @param primaryKey the primary key of this plugin setting
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_pluginSetting.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_pluginSetting.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the roles of this plugin setting.
	*
	* @param roles the roles of this plugin setting
	*/
	@Override
	public void setRoles(String roles) {
		_pluginSetting.setRoles(roles);
	}

	/**
	* Sets an array of required roles of the plugin.
	*/
	@Override
	public void setRolesArray(String[] rolesArray) {
		_pluginSetting.setRolesArray(rolesArray);
	}

	@Override
	public CacheModel<PluginSetting> toCacheModel() {
		return _pluginSetting.toCacheModel();
	}

	@Override
	public PluginSetting toEscapedModel() {
		return new PluginSettingWrapper(_pluginSetting.toEscapedModel());
	}

	@Override
	public String toString() {
		return _pluginSetting.toString();
	}

	@Override
	public PluginSetting toUnescapedModel() {
		return new PluginSettingWrapper(_pluginSetting.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _pluginSetting.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PluginSettingWrapper)) {
			return false;
		}

		PluginSettingWrapper pluginSettingWrapper = (PluginSettingWrapper)obj;

		if (Objects.equals(_pluginSetting, pluginSettingWrapper._pluginSetting)) {
			return true;
		}

		return false;
	}

	@Override
	public PluginSetting getWrappedModel() {
		return _pluginSetting;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _pluginSetting.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _pluginSetting.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_pluginSetting.resetOriginalValues();
	}

	private final PluginSetting _pluginSetting;
}