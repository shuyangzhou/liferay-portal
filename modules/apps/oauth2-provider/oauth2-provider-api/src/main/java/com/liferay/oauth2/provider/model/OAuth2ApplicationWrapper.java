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

package com.liferay.oauth2.provider.model;

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
 * This class is a wrapper for {@link OAuth2Application}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuth2Application
 * @generated
 */
@ProviderType
public class OAuth2ApplicationWrapper implements OAuth2Application,
	ModelWrapper<OAuth2Application> {
	public OAuth2ApplicationWrapper(OAuth2Application oAuth2Application) {
		_oAuth2Application = oAuth2Application;
	}

	@Override
	public Class<?> getModelClass() {
		return OAuth2Application.class;
	}

	@Override
	public String getModelClassName() {
		return OAuth2Application.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<OAuth2Application, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<OAuth2Application, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<OAuth2Application, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<OAuth2Application, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<OAuth2Application, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<OAuth2Application, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<OAuth2Application, Object>> getAttributeGetters() {
		return _oAuth2Application.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<OAuth2Application, Object>> getAttributeSetters() {
		return _oAuth2Application.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new OAuth2ApplicationWrapper((OAuth2Application)_oAuth2Application.clone());
	}

	@Override
	public int compareTo(OAuth2Application oAuth2Application) {
		return _oAuth2Application.compareTo(oAuth2Application);
	}

	/**
	* Returns the allowed grant types of this o auth2 application.
	*
	* @return the allowed grant types of this o auth2 application
	*/
	@Override
	public String getAllowedGrantTypes() {
		return _oAuth2Application.getAllowedGrantTypes();
	}

	@Override
	public java.util.List<com.liferay.oauth2.provider.constants.GrantType> getAllowedGrantTypesList() {
		return _oAuth2Application.getAllowedGrantTypesList();
	}

	/**
	* Returns the client ID of this o auth2 application.
	*
	* @return the client ID of this o auth2 application
	*/
	@Override
	public String getClientId() {
		return _oAuth2Application.getClientId();
	}

	/**
	* Returns the client profile of this o auth2 application.
	*
	* @return the client profile of this o auth2 application
	*/
	@Override
	public int getClientProfile() {
		return _oAuth2Application.getClientProfile();
	}

	/**
	* Returns the client secret of this o auth2 application.
	*
	* @return the client secret of this o auth2 application
	*/
	@Override
	public String getClientSecret() {
		return _oAuth2Application.getClientSecret();
	}

	/**
	* Returns the company ID of this o auth2 application.
	*
	* @return the company ID of this o auth2 application
	*/
	@Override
	public long getCompanyId() {
		return _oAuth2Application.getCompanyId();
	}

	/**
	* Returns the create date of this o auth2 application.
	*
	* @return the create date of this o auth2 application
	*/
	@Override
	public Date getCreateDate() {
		return _oAuth2Application.getCreateDate();
	}

	/**
	* Returns the description of this o auth2 application.
	*
	* @return the description of this o auth2 application
	*/
	@Override
	public String getDescription() {
		return _oAuth2Application.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _oAuth2Application.getExpandoBridge();
	}

	/**
	* Returns the features of this o auth2 application.
	*
	* @return the features of this o auth2 application
	*/
	@Override
	public String getFeatures() {
		return _oAuth2Application.getFeatures();
	}

	@Override
	public java.util.List<String> getFeaturesList() {
		return _oAuth2Application.getFeaturesList();
	}

	/**
	* Returns the home page url of this o auth2 application.
	*
	* @return the home page url of this o auth2 application
	*/
	@Override
	public String getHomePageURL() {
		return _oAuth2Application.getHomePageURL();
	}

	/**
	* Returns the icon file entry ID of this o auth2 application.
	*
	* @return the icon file entry ID of this o auth2 application
	*/
	@Override
	public long getIconFileEntryId() {
		return _oAuth2Application.getIconFileEntryId();
	}

	/**
	* Returns the modified date of this o auth2 application.
	*
	* @return the modified date of this o auth2 application
	*/
	@Override
	public Date getModifiedDate() {
		return _oAuth2Application.getModifiedDate();
	}

	/**
	* Returns the name of this o auth2 application.
	*
	* @return the name of this o auth2 application
	*/
	@Override
	public String getName() {
		return _oAuth2Application.getName();
	}

	/**
	* Returns the o auth2 application ID of this o auth2 application.
	*
	* @return the o auth2 application ID of this o auth2 application
	*/
	@Override
	public long getOAuth2ApplicationId() {
		return _oAuth2Application.getOAuth2ApplicationId();
	}

	/**
	* Returns the o auth2 application scope aliases ID of this o auth2 application.
	*
	* @return the o auth2 application scope aliases ID of this o auth2 application
	*/
	@Override
	public long getOAuth2ApplicationScopeAliasesId() {
		return _oAuth2Application.getOAuth2ApplicationScopeAliasesId();
	}

	/**
	* Returns the primary key of this o auth2 application.
	*
	* @return the primary key of this o auth2 application
	*/
	@Override
	public long getPrimaryKey() {
		return _oAuth2Application.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _oAuth2Application.getPrimaryKeyObj();
	}

	/**
	* Returns the privacy policy url of this o auth2 application.
	*
	* @return the privacy policy url of this o auth2 application
	*/
	@Override
	public String getPrivacyPolicyURL() {
		return _oAuth2Application.getPrivacyPolicyURL();
	}

	/**
	* Returns the redirect ur is of this o auth2 application.
	*
	* @return the redirect ur is of this o auth2 application
	*/
	@Override
	public String getRedirectURIs() {
		return _oAuth2Application.getRedirectURIs();
	}

	@Override
	public java.util.List<String> getRedirectURIsList() {
		return _oAuth2Application.getRedirectURIsList();
	}

	/**
	* Returns the user ID of this o auth2 application.
	*
	* @return the user ID of this o auth2 application
	*/
	@Override
	public long getUserId() {
		return _oAuth2Application.getUserId();
	}

	/**
	* Returns the user name of this o auth2 application.
	*
	* @return the user name of this o auth2 application
	*/
	@Override
	public String getUserName() {
		return _oAuth2Application.getUserName();
	}

	/**
	* Returns the user uuid of this o auth2 application.
	*
	* @return the user uuid of this o auth2 application
	*/
	@Override
	public String getUserUuid() {
		return _oAuth2Application.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _oAuth2Application.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _oAuth2Application.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _oAuth2Application.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _oAuth2Application.isNew();
	}

	@Override
	public void persist() {
		_oAuth2Application.persist();
	}

	/**
	* Sets the allowed grant types of this o auth2 application.
	*
	* @param allowedGrantTypes the allowed grant types of this o auth2 application
	*/
	@Override
	public void setAllowedGrantTypes(String allowedGrantTypes) {
		_oAuth2Application.setAllowedGrantTypes(allowedGrantTypes);
	}

	@Override
	public void setAllowedGrantTypesList(
		java.util.List<com.liferay.oauth2.provider.constants.GrantType> allowedGrantTypesList) {
		_oAuth2Application.setAllowedGrantTypesList(allowedGrantTypesList);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_oAuth2Application.setCachedModel(cachedModel);
	}

	/**
	* Sets the client ID of this o auth2 application.
	*
	* @param clientId the client ID of this o auth2 application
	*/
	@Override
	public void setClientId(String clientId) {
		_oAuth2Application.setClientId(clientId);
	}

	/**
	* Sets the client profile of this o auth2 application.
	*
	* @param clientProfile the client profile of this o auth2 application
	*/
	@Override
	public void setClientProfile(int clientProfile) {
		_oAuth2Application.setClientProfile(clientProfile);
	}

	/**
	* Sets the client secret of this o auth2 application.
	*
	* @param clientSecret the client secret of this o auth2 application
	*/
	@Override
	public void setClientSecret(String clientSecret) {
		_oAuth2Application.setClientSecret(clientSecret);
	}

	/**
	* Sets the company ID of this o auth2 application.
	*
	* @param companyId the company ID of this o auth2 application
	*/
	@Override
	public void setCompanyId(long companyId) {
		_oAuth2Application.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this o auth2 application.
	*
	* @param createDate the create date of this o auth2 application
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_oAuth2Application.setCreateDate(createDate);
	}

	/**
	* Sets the description of this o auth2 application.
	*
	* @param description the description of this o auth2 application
	*/
	@Override
	public void setDescription(String description) {
		_oAuth2Application.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_oAuth2Application.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_oAuth2Application.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_oAuth2Application.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the features of this o auth2 application.
	*
	* @param features the features of this o auth2 application
	*/
	@Override
	public void setFeatures(String features) {
		_oAuth2Application.setFeatures(features);
	}

	@Override
	public void setFeaturesList(java.util.List<String> featuresList) {
		_oAuth2Application.setFeaturesList(featuresList);
	}

	/**
	* Sets the home page url of this o auth2 application.
	*
	* @param homePageURL the home page url of this o auth2 application
	*/
	@Override
	public void setHomePageURL(String homePageURL) {
		_oAuth2Application.setHomePageURL(homePageURL);
	}

	/**
	* Sets the icon file entry ID of this o auth2 application.
	*
	* @param iconFileEntryId the icon file entry ID of this o auth2 application
	*/
	@Override
	public void setIconFileEntryId(long iconFileEntryId) {
		_oAuth2Application.setIconFileEntryId(iconFileEntryId);
	}

	/**
	* Sets the modified date of this o auth2 application.
	*
	* @param modifiedDate the modified date of this o auth2 application
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_oAuth2Application.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this o auth2 application.
	*
	* @param name the name of this o auth2 application
	*/
	@Override
	public void setName(String name) {
		_oAuth2Application.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_oAuth2Application.setNew(n);
	}

	/**
	* Sets the o auth2 application ID of this o auth2 application.
	*
	* @param oAuth2ApplicationId the o auth2 application ID of this o auth2 application
	*/
	@Override
	public void setOAuth2ApplicationId(long oAuth2ApplicationId) {
		_oAuth2Application.setOAuth2ApplicationId(oAuth2ApplicationId);
	}

	/**
	* Sets the o auth2 application scope aliases ID of this o auth2 application.
	*
	* @param oAuth2ApplicationScopeAliasesId the o auth2 application scope aliases ID of this o auth2 application
	*/
	@Override
	public void setOAuth2ApplicationScopeAliasesId(
		long oAuth2ApplicationScopeAliasesId) {
		_oAuth2Application.setOAuth2ApplicationScopeAliasesId(oAuth2ApplicationScopeAliasesId);
	}

	/**
	* Sets the primary key of this o auth2 application.
	*
	* @param primaryKey the primary key of this o auth2 application
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_oAuth2Application.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_oAuth2Application.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the privacy policy url of this o auth2 application.
	*
	* @param privacyPolicyURL the privacy policy url of this o auth2 application
	*/
	@Override
	public void setPrivacyPolicyURL(String privacyPolicyURL) {
		_oAuth2Application.setPrivacyPolicyURL(privacyPolicyURL);
	}

	/**
	* Sets the redirect ur is of this o auth2 application.
	*
	* @param redirectURIs the redirect ur is of this o auth2 application
	*/
	@Override
	public void setRedirectURIs(String redirectURIs) {
		_oAuth2Application.setRedirectURIs(redirectURIs);
	}

	@Override
	public void setRedirectURIsList(java.util.List<String> redirectURIsList) {
		_oAuth2Application.setRedirectURIsList(redirectURIsList);
	}

	/**
	* Sets the user ID of this o auth2 application.
	*
	* @param userId the user ID of this o auth2 application
	*/
	@Override
	public void setUserId(long userId) {
		_oAuth2Application.setUserId(userId);
	}

	/**
	* Sets the user name of this o auth2 application.
	*
	* @param userName the user name of this o auth2 application
	*/
	@Override
	public void setUserName(String userName) {
		_oAuth2Application.setUserName(userName);
	}

	/**
	* Sets the user uuid of this o auth2 application.
	*
	* @param userUuid the user uuid of this o auth2 application
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_oAuth2Application.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<OAuth2Application> toCacheModel() {
		return _oAuth2Application.toCacheModel();
	}

	@Override
	public OAuth2Application toEscapedModel() {
		return new OAuth2ApplicationWrapper(_oAuth2Application.toEscapedModel());
	}

	@Override
	public String toString() {
		return _oAuth2Application.toString();
	}

	@Override
	public OAuth2Application toUnescapedModel() {
		return new OAuth2ApplicationWrapper(_oAuth2Application.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _oAuth2Application.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OAuth2ApplicationWrapper)) {
			return false;
		}

		OAuth2ApplicationWrapper oAuth2ApplicationWrapper = (OAuth2ApplicationWrapper)obj;

		if (Objects.equals(_oAuth2Application,
					oAuth2ApplicationWrapper._oAuth2Application)) {
			return true;
		}

		return false;
	}

	@Override
	public OAuth2Application getWrappedModel() {
		return _oAuth2Application;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _oAuth2Application.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _oAuth2Application.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_oAuth2Application.resetOriginalValues();
	}

	private final OAuth2Application _oAuth2Application;
}