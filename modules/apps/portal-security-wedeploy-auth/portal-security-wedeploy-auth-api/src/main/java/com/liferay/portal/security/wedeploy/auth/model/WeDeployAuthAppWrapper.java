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

package com.liferay.portal.security.wedeploy.auth.model;

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
 * This class is a wrapper for {@link WeDeployAuthApp}.
 * </p>
 *
 * @author Supritha Sundaram
 * @see WeDeployAuthApp
 * @generated
 */
@ProviderType
public class WeDeployAuthAppWrapper implements WeDeployAuthApp,
	ModelWrapper<WeDeployAuthApp> {
	public WeDeployAuthAppWrapper(WeDeployAuthApp weDeployAuthApp) {
		_weDeployAuthApp = weDeployAuthApp;
	}

	@Override
	public Class<?> getModelClass() {
		return WeDeployAuthApp.class;
	}

	@Override
	public String getModelClassName() {
		return WeDeployAuthApp.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WeDeployAuthApp, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<WeDeployAuthApp, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<WeDeployAuthApp, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WeDeployAuthApp, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<WeDeployAuthApp, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<WeDeployAuthApp, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<WeDeployAuthApp, Object>> getAttributeGetters() {
		return _weDeployAuthApp.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<WeDeployAuthApp, Object>> getAttributeSetters() {
		return _weDeployAuthApp.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new WeDeployAuthAppWrapper((WeDeployAuthApp)_weDeployAuthApp.clone());
	}

	@Override
	public int compareTo(WeDeployAuthApp weDeployAuthApp) {
		return _weDeployAuthApp.compareTo(weDeployAuthApp);
	}

	/**
	* Returns the client ID of this we deploy auth app.
	*
	* @return the client ID of this we deploy auth app
	*/
	@Override
	public String getClientId() {
		return _weDeployAuthApp.getClientId();
	}

	/**
	* Returns the client secret of this we deploy auth app.
	*
	* @return the client secret of this we deploy auth app
	*/
	@Override
	public String getClientSecret() {
		return _weDeployAuthApp.getClientSecret();
	}

	/**
	* Returns the company ID of this we deploy auth app.
	*
	* @return the company ID of this we deploy auth app
	*/
	@Override
	public long getCompanyId() {
		return _weDeployAuthApp.getCompanyId();
	}

	/**
	* Returns the create date of this we deploy auth app.
	*
	* @return the create date of this we deploy auth app
	*/
	@Override
	public Date getCreateDate() {
		return _weDeployAuthApp.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _weDeployAuthApp.getExpandoBridge();
	}

	/**
	* Returns the modified date of this we deploy auth app.
	*
	* @return the modified date of this we deploy auth app
	*/
	@Override
	public Date getModifiedDate() {
		return _weDeployAuthApp.getModifiedDate();
	}

	/**
	* Returns the name of this we deploy auth app.
	*
	* @return the name of this we deploy auth app
	*/
	@Override
	public String getName() {
		return _weDeployAuthApp.getName();
	}

	/**
	* Returns the primary key of this we deploy auth app.
	*
	* @return the primary key of this we deploy auth app
	*/
	@Override
	public long getPrimaryKey() {
		return _weDeployAuthApp.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _weDeployAuthApp.getPrimaryKeyObj();
	}

	/**
	* Returns the redirect uri of this we deploy auth app.
	*
	* @return the redirect uri of this we deploy auth app
	*/
	@Override
	public String getRedirectURI() {
		return _weDeployAuthApp.getRedirectURI();
	}

	/**
	* Returns the user ID of this we deploy auth app.
	*
	* @return the user ID of this we deploy auth app
	*/
	@Override
	public long getUserId() {
		return _weDeployAuthApp.getUserId();
	}

	/**
	* Returns the user name of this we deploy auth app.
	*
	* @return the user name of this we deploy auth app
	*/
	@Override
	public String getUserName() {
		return _weDeployAuthApp.getUserName();
	}

	/**
	* Returns the user uuid of this we deploy auth app.
	*
	* @return the user uuid of this we deploy auth app
	*/
	@Override
	public String getUserUuid() {
		return _weDeployAuthApp.getUserUuid();
	}

	/**
	* Returns the we deploy auth app ID of this we deploy auth app.
	*
	* @return the we deploy auth app ID of this we deploy auth app
	*/
	@Override
	public long getWeDeployAuthAppId() {
		return _weDeployAuthApp.getWeDeployAuthAppId();
	}

	@Override
	public int hashCode() {
		return _weDeployAuthApp.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _weDeployAuthApp.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _weDeployAuthApp.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _weDeployAuthApp.isNew();
	}

	@Override
	public void persist() {
		_weDeployAuthApp.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_weDeployAuthApp.setCachedModel(cachedModel);
	}

	/**
	* Sets the client ID of this we deploy auth app.
	*
	* @param clientId the client ID of this we deploy auth app
	*/
	@Override
	public void setClientId(String clientId) {
		_weDeployAuthApp.setClientId(clientId);
	}

	/**
	* Sets the client secret of this we deploy auth app.
	*
	* @param clientSecret the client secret of this we deploy auth app
	*/
	@Override
	public void setClientSecret(String clientSecret) {
		_weDeployAuthApp.setClientSecret(clientSecret);
	}

	/**
	* Sets the company ID of this we deploy auth app.
	*
	* @param companyId the company ID of this we deploy auth app
	*/
	@Override
	public void setCompanyId(long companyId) {
		_weDeployAuthApp.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this we deploy auth app.
	*
	* @param createDate the create date of this we deploy auth app
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_weDeployAuthApp.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_weDeployAuthApp.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_weDeployAuthApp.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_weDeployAuthApp.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the modified date of this we deploy auth app.
	*
	* @param modifiedDate the modified date of this we deploy auth app
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_weDeployAuthApp.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this we deploy auth app.
	*
	* @param name the name of this we deploy auth app
	*/
	@Override
	public void setName(String name) {
		_weDeployAuthApp.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_weDeployAuthApp.setNew(n);
	}

	/**
	* Sets the primary key of this we deploy auth app.
	*
	* @param primaryKey the primary key of this we deploy auth app
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_weDeployAuthApp.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_weDeployAuthApp.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the redirect uri of this we deploy auth app.
	*
	* @param redirectURI the redirect uri of this we deploy auth app
	*/
	@Override
	public void setRedirectURI(String redirectURI) {
		_weDeployAuthApp.setRedirectURI(redirectURI);
	}

	/**
	* Sets the user ID of this we deploy auth app.
	*
	* @param userId the user ID of this we deploy auth app
	*/
	@Override
	public void setUserId(long userId) {
		_weDeployAuthApp.setUserId(userId);
	}

	/**
	* Sets the user name of this we deploy auth app.
	*
	* @param userName the user name of this we deploy auth app
	*/
	@Override
	public void setUserName(String userName) {
		_weDeployAuthApp.setUserName(userName);
	}

	/**
	* Sets the user uuid of this we deploy auth app.
	*
	* @param userUuid the user uuid of this we deploy auth app
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_weDeployAuthApp.setUserUuid(userUuid);
	}

	/**
	* Sets the we deploy auth app ID of this we deploy auth app.
	*
	* @param weDeployAuthAppId the we deploy auth app ID of this we deploy auth app
	*/
	@Override
	public void setWeDeployAuthAppId(long weDeployAuthAppId) {
		_weDeployAuthApp.setWeDeployAuthAppId(weDeployAuthAppId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<WeDeployAuthApp> toCacheModel() {
		return _weDeployAuthApp.toCacheModel();
	}

	@Override
	public WeDeployAuthApp toEscapedModel() {
		return new WeDeployAuthAppWrapper(_weDeployAuthApp.toEscapedModel());
	}

	@Override
	public String toString() {
		return _weDeployAuthApp.toString();
	}

	@Override
	public WeDeployAuthApp toUnescapedModel() {
		return new WeDeployAuthAppWrapper(_weDeployAuthApp.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _weDeployAuthApp.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WeDeployAuthAppWrapper)) {
			return false;
		}

		WeDeployAuthAppWrapper weDeployAuthAppWrapper = (WeDeployAuthAppWrapper)obj;

		if (Objects.equals(_weDeployAuthApp,
					weDeployAuthAppWrapper._weDeployAuthApp)) {
			return true;
		}

		return false;
	}

	@Override
	public WeDeployAuthApp getWrappedModel() {
		return _weDeployAuthApp;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _weDeployAuthApp.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _weDeployAuthApp.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_weDeployAuthApp.resetOriginalValues();
	}

	private final WeDeployAuthApp _weDeployAuthApp;
}