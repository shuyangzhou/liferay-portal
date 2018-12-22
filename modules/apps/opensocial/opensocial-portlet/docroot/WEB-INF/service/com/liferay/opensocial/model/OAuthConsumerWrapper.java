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

package com.liferay.opensocial.model;

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
 * This class is a wrapper for {@link OAuthConsumer}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OAuthConsumer
 * @generated
 */
@ProviderType
public class OAuthConsumerWrapper implements OAuthConsumer,
	ModelWrapper<OAuthConsumer> {
	public OAuthConsumerWrapper(OAuthConsumer oAuthConsumer) {
		_oAuthConsumer = oAuthConsumer;
	}

	@Override
	public Class<?> getModelClass() {
		return OAuthConsumer.class;
	}

	@Override
	public String getModelClassName() {
		return OAuthConsumer.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<OAuthConsumer, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<OAuthConsumer, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<OAuthConsumer, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<OAuthConsumer, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<OAuthConsumer, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<OAuthConsumer, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<OAuthConsumer, Object>> getAttributeGetters() {
		return _oAuthConsumer.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<OAuthConsumer, Object>> getAttributeSetters() {
		return _oAuthConsumer.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new OAuthConsumerWrapper((OAuthConsumer)_oAuthConsumer.clone());
	}

	@Override
	public int compareTo(OAuthConsumer oAuthConsumer) {
		return _oAuthConsumer.compareTo(oAuthConsumer);
	}

	/**
	* Returns the company ID of this o auth consumer.
	*
	* @return the company ID of this o auth consumer
	*/
	@Override
	public long getCompanyId() {
		return _oAuthConsumer.getCompanyId();
	}

	/**
	* Returns the consumer key of this o auth consumer.
	*
	* @return the consumer key of this o auth consumer
	*/
	@Override
	public String getConsumerKey() {
		return _oAuthConsumer.getConsumerKey();
	}

	/**
	* Returns the consumer secret of this o auth consumer.
	*
	* @return the consumer secret of this o auth consumer
	*/
	@Override
	public String getConsumerSecret() {
		return _oAuthConsumer.getConsumerSecret();
	}

	/**
	* Returns the create date of this o auth consumer.
	*
	* @return the create date of this o auth consumer
	*/
	@Override
	public Date getCreateDate() {
		return _oAuthConsumer.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _oAuthConsumer.getExpandoBridge();
	}

	/**
	* Returns the gadget key of this o auth consumer.
	*
	* @return the gadget key of this o auth consumer
	*/
	@Override
	public String getGadgetKey() {
		return _oAuthConsumer.getGadgetKey();
	}

	@Override
	public String getKeyName() {
		return _oAuthConsumer.getKeyName();
	}

	/**
	* Returns the key type of this o auth consumer.
	*
	* @return the key type of this o auth consumer
	*/
	@Override
	public String getKeyType() {
		return _oAuthConsumer.getKeyType();
	}

	/**
	* Returns the modified date of this o auth consumer.
	*
	* @return the modified date of this o auth consumer
	*/
	@Override
	public Date getModifiedDate() {
		return _oAuthConsumer.getModifiedDate();
	}

	/**
	* Returns the o auth consumer ID of this o auth consumer.
	*
	* @return the o auth consumer ID of this o auth consumer
	*/
	@Override
	public long getOAuthConsumerId() {
		return _oAuthConsumer.getOAuthConsumerId();
	}

	/**
	* Returns the primary key of this o auth consumer.
	*
	* @return the primary key of this o auth consumer
	*/
	@Override
	public long getPrimaryKey() {
		return _oAuthConsumer.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _oAuthConsumer.getPrimaryKeyObj();
	}

	/**
	* Returns the service name of this o auth consumer.
	*
	* @return the service name of this o auth consumer
	*/
	@Override
	public String getServiceName() {
		return _oAuthConsumer.getServiceName();
	}

	@Override
	public int hashCode() {
		return _oAuthConsumer.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _oAuthConsumer.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _oAuthConsumer.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _oAuthConsumer.isNew();
	}

	@Override
	public void persist() {
		_oAuthConsumer.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_oAuthConsumer.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this o auth consumer.
	*
	* @param companyId the company ID of this o auth consumer
	*/
	@Override
	public void setCompanyId(long companyId) {
		_oAuthConsumer.setCompanyId(companyId);
	}

	/**
	* Sets the consumer key of this o auth consumer.
	*
	* @param consumerKey the consumer key of this o auth consumer
	*/
	@Override
	public void setConsumerKey(String consumerKey) {
		_oAuthConsumer.setConsumerKey(consumerKey);
	}

	/**
	* Sets the consumer secret of this o auth consumer.
	*
	* @param consumerSecret the consumer secret of this o auth consumer
	*/
	@Override
	public void setConsumerSecret(String consumerSecret) {
		_oAuthConsumer.setConsumerSecret(consumerSecret);
	}

	/**
	* Sets the create date of this o auth consumer.
	*
	* @param createDate the create date of this o auth consumer
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_oAuthConsumer.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_oAuthConsumer.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_oAuthConsumer.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_oAuthConsumer.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the gadget key of this o auth consumer.
	*
	* @param gadgetKey the gadget key of this o auth consumer
	*/
	@Override
	public void setGadgetKey(String gadgetKey) {
		_oAuthConsumer.setGadgetKey(gadgetKey);
	}

	@Override
	public void setKeyName(String keyName) {
		_oAuthConsumer.setKeyName(keyName);
	}

	/**
	* Sets the key type of this o auth consumer.
	*
	* @param keyType the key type of this o auth consumer
	*/
	@Override
	public void setKeyType(String keyType) {
		_oAuthConsumer.setKeyType(keyType);
	}

	/**
	* Sets the modified date of this o auth consumer.
	*
	* @param modifiedDate the modified date of this o auth consumer
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_oAuthConsumer.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_oAuthConsumer.setNew(n);
	}

	/**
	* Sets the o auth consumer ID of this o auth consumer.
	*
	* @param oAuthConsumerId the o auth consumer ID of this o auth consumer
	*/
	@Override
	public void setOAuthConsumerId(long oAuthConsumerId) {
		_oAuthConsumer.setOAuthConsumerId(oAuthConsumerId);
	}

	/**
	* Sets the primary key of this o auth consumer.
	*
	* @param primaryKey the primary key of this o auth consumer
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_oAuthConsumer.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_oAuthConsumer.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the service name of this o auth consumer.
	*
	* @param serviceName the service name of this o auth consumer
	*/
	@Override
	public void setServiceName(String serviceName) {
		_oAuthConsumer.setServiceName(serviceName);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<OAuthConsumer> toCacheModel() {
		return _oAuthConsumer.toCacheModel();
	}

	@Override
	public OAuthConsumer toEscapedModel() {
		return new OAuthConsumerWrapper(_oAuthConsumer.toEscapedModel());
	}

	@Override
	public String toString() {
		return _oAuthConsumer.toString();
	}

	@Override
	public OAuthConsumer toUnescapedModel() {
		return new OAuthConsumerWrapper(_oAuthConsumer.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _oAuthConsumer.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OAuthConsumerWrapper)) {
			return false;
		}

		OAuthConsumerWrapper oAuthConsumerWrapper = (OAuthConsumerWrapper)obj;

		if (Objects.equals(_oAuthConsumer, oAuthConsumerWrapper._oAuthConsumer)) {
			return true;
		}

		return false;
	}

	@Override
	public OAuthConsumer getWrappedModel() {
		return _oAuthConsumer;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _oAuthConsumer.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _oAuthConsumer.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_oAuthConsumer.resetOriginalValues();
	}

	private final OAuthConsumer _oAuthConsumer;
}