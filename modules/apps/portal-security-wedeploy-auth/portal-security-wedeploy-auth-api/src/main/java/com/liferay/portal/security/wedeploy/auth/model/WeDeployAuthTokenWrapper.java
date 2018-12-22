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
 * This class is a wrapper for {@link WeDeployAuthToken}.
 * </p>
 *
 * @author Supritha Sundaram
 * @see WeDeployAuthToken
 * @generated
 */
@ProviderType
public class WeDeployAuthTokenWrapper implements WeDeployAuthToken,
	ModelWrapper<WeDeployAuthToken> {
	public WeDeployAuthTokenWrapper(WeDeployAuthToken weDeployAuthToken) {
		_weDeployAuthToken = weDeployAuthToken;
	}

	@Override
	public Class<?> getModelClass() {
		return WeDeployAuthToken.class;
	}

	@Override
	public String getModelClassName() {
		return WeDeployAuthToken.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WeDeployAuthToken, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<WeDeployAuthToken, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<WeDeployAuthToken, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WeDeployAuthToken, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<WeDeployAuthToken, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<WeDeployAuthToken, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<WeDeployAuthToken, Object>> getAttributeGetters() {
		return _weDeployAuthToken.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<WeDeployAuthToken, Object>> getAttributeSetters() {
		return _weDeployAuthToken.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new WeDeployAuthTokenWrapper((WeDeployAuthToken)_weDeployAuthToken.clone());
	}

	@Override
	public int compareTo(WeDeployAuthToken weDeployAuthToken) {
		return _weDeployAuthToken.compareTo(weDeployAuthToken);
	}

	/**
	* Returns the client ID of this we deploy auth token.
	*
	* @return the client ID of this we deploy auth token
	*/
	@Override
	public String getClientId() {
		return _weDeployAuthToken.getClientId();
	}

	/**
	* Returns the company ID of this we deploy auth token.
	*
	* @return the company ID of this we deploy auth token
	*/
	@Override
	public long getCompanyId() {
		return _weDeployAuthToken.getCompanyId();
	}

	/**
	* Returns the create date of this we deploy auth token.
	*
	* @return the create date of this we deploy auth token
	*/
	@Override
	public Date getCreateDate() {
		return _weDeployAuthToken.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _weDeployAuthToken.getExpandoBridge();
	}

	/**
	* Returns the modified date of this we deploy auth token.
	*
	* @return the modified date of this we deploy auth token
	*/
	@Override
	public Date getModifiedDate() {
		return _weDeployAuthToken.getModifiedDate();
	}

	/**
	* Returns the primary key of this we deploy auth token.
	*
	* @return the primary key of this we deploy auth token
	*/
	@Override
	public long getPrimaryKey() {
		return _weDeployAuthToken.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _weDeployAuthToken.getPrimaryKeyObj();
	}

	/**
	* Returns the token of this we deploy auth token.
	*
	* @return the token of this we deploy auth token
	*/
	@Override
	public String getToken() {
		return _weDeployAuthToken.getToken();
	}

	/**
	* Returns the type of this we deploy auth token.
	*
	* @return the type of this we deploy auth token
	*/
	@Override
	public int getType() {
		return _weDeployAuthToken.getType();
	}

	/**
	* Returns the user ID of this we deploy auth token.
	*
	* @return the user ID of this we deploy auth token
	*/
	@Override
	public long getUserId() {
		return _weDeployAuthToken.getUserId();
	}

	/**
	* Returns the user name of this we deploy auth token.
	*
	* @return the user name of this we deploy auth token
	*/
	@Override
	public String getUserName() {
		return _weDeployAuthToken.getUserName();
	}

	/**
	* Returns the user uuid of this we deploy auth token.
	*
	* @return the user uuid of this we deploy auth token
	*/
	@Override
	public String getUserUuid() {
		return _weDeployAuthToken.getUserUuid();
	}

	/**
	* Returns the we deploy auth token ID of this we deploy auth token.
	*
	* @return the we deploy auth token ID of this we deploy auth token
	*/
	@Override
	public long getWeDeployAuthTokenId() {
		return _weDeployAuthToken.getWeDeployAuthTokenId();
	}

	@Override
	public int hashCode() {
		return _weDeployAuthToken.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _weDeployAuthToken.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _weDeployAuthToken.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _weDeployAuthToken.isNew();
	}

	@Override
	public void persist() {
		_weDeployAuthToken.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_weDeployAuthToken.setCachedModel(cachedModel);
	}

	/**
	* Sets the client ID of this we deploy auth token.
	*
	* @param clientId the client ID of this we deploy auth token
	*/
	@Override
	public void setClientId(String clientId) {
		_weDeployAuthToken.setClientId(clientId);
	}

	/**
	* Sets the company ID of this we deploy auth token.
	*
	* @param companyId the company ID of this we deploy auth token
	*/
	@Override
	public void setCompanyId(long companyId) {
		_weDeployAuthToken.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this we deploy auth token.
	*
	* @param createDate the create date of this we deploy auth token
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_weDeployAuthToken.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_weDeployAuthToken.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_weDeployAuthToken.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_weDeployAuthToken.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the modified date of this we deploy auth token.
	*
	* @param modifiedDate the modified date of this we deploy auth token
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_weDeployAuthToken.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_weDeployAuthToken.setNew(n);
	}

	/**
	* Sets the primary key of this we deploy auth token.
	*
	* @param primaryKey the primary key of this we deploy auth token
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_weDeployAuthToken.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_weDeployAuthToken.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the token of this we deploy auth token.
	*
	* @param token the token of this we deploy auth token
	*/
	@Override
	public void setToken(String token) {
		_weDeployAuthToken.setToken(token);
	}

	/**
	* Sets the type of this we deploy auth token.
	*
	* @param type the type of this we deploy auth token
	*/
	@Override
	public void setType(int type) {
		_weDeployAuthToken.setType(type);
	}

	/**
	* Sets the user ID of this we deploy auth token.
	*
	* @param userId the user ID of this we deploy auth token
	*/
	@Override
	public void setUserId(long userId) {
		_weDeployAuthToken.setUserId(userId);
	}

	/**
	* Sets the user name of this we deploy auth token.
	*
	* @param userName the user name of this we deploy auth token
	*/
	@Override
	public void setUserName(String userName) {
		_weDeployAuthToken.setUserName(userName);
	}

	/**
	* Sets the user uuid of this we deploy auth token.
	*
	* @param userUuid the user uuid of this we deploy auth token
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_weDeployAuthToken.setUserUuid(userUuid);
	}

	/**
	* Sets the we deploy auth token ID of this we deploy auth token.
	*
	* @param weDeployAuthTokenId the we deploy auth token ID of this we deploy auth token
	*/
	@Override
	public void setWeDeployAuthTokenId(long weDeployAuthTokenId) {
		_weDeployAuthToken.setWeDeployAuthTokenId(weDeployAuthTokenId);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<WeDeployAuthToken> toCacheModel() {
		return _weDeployAuthToken.toCacheModel();
	}

	@Override
	public WeDeployAuthToken toEscapedModel() {
		return new WeDeployAuthTokenWrapper(_weDeployAuthToken.toEscapedModel());
	}

	@Override
	public String toString() {
		return _weDeployAuthToken.toString();
	}

	@Override
	public WeDeployAuthToken toUnescapedModel() {
		return new WeDeployAuthTokenWrapper(_weDeployAuthToken.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _weDeployAuthToken.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WeDeployAuthTokenWrapper)) {
			return false;
		}

		WeDeployAuthTokenWrapper weDeployAuthTokenWrapper = (WeDeployAuthTokenWrapper)obj;

		if (Objects.equals(_weDeployAuthToken,
					weDeployAuthTokenWrapper._weDeployAuthToken)) {
			return true;
		}

		return false;
	}

	@Override
	public WeDeployAuthToken getWrappedModel() {
		return _weDeployAuthToken;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _weDeployAuthToken.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _weDeployAuthToken.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_weDeployAuthToken.resetOriginalValues();
	}

	private final WeDeployAuthToken _weDeployAuthToken;
}