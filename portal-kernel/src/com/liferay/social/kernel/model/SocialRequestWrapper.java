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

package com.liferay.social.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link SocialRequest}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SocialRequest
 * @generated
 */
@ProviderType
public class SocialRequestWrapper implements SocialRequest,
	ModelWrapper<SocialRequest> {
	public SocialRequestWrapper(SocialRequest socialRequest) {
		_socialRequest = socialRequest;
	}

	@Override
	public Class<?> getModelClass() {
		return SocialRequest.class;
	}

	@Override
	public String getModelClassName() {
		return SocialRequest.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SocialRequest, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<SocialRequest, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<SocialRequest, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SocialRequest, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<SocialRequest, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<SocialRequest, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<SocialRequest, Object>> getAttributeGetters() {
		return _socialRequest.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<SocialRequest, Object>> getAttributeSetters() {
		return _socialRequest.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new SocialRequestWrapper((SocialRequest)_socialRequest.clone());
	}

	@Override
	public int compareTo(SocialRequest socialRequest) {
		return _socialRequest.compareTo(socialRequest);
	}

	/**
	* Returns the fully qualified class name of this social request.
	*
	* @return the fully qualified class name of this social request
	*/
	@Override
	public String getClassName() {
		return _socialRequest.getClassName();
	}

	/**
	* Returns the class name ID of this social request.
	*
	* @return the class name ID of this social request
	*/
	@Override
	public long getClassNameId() {
		return _socialRequest.getClassNameId();
	}

	/**
	* Returns the class pk of this social request.
	*
	* @return the class pk of this social request
	*/
	@Override
	public long getClassPK() {
		return _socialRequest.getClassPK();
	}

	/**
	* Returns the company ID of this social request.
	*
	* @return the company ID of this social request
	*/
	@Override
	public long getCompanyId() {
		return _socialRequest.getCompanyId();
	}

	/**
	* Returns the create date of this social request.
	*
	* @return the create date of this social request
	*/
	@Override
	public long getCreateDate() {
		return _socialRequest.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _socialRequest.getExpandoBridge();
	}

	/**
	* Returns the extra data of this social request.
	*
	* @return the extra data of this social request
	*/
	@Override
	public String getExtraData() {
		return _socialRequest.getExtraData();
	}

	/**
	* Returns the group ID of this social request.
	*
	* @return the group ID of this social request
	*/
	@Override
	public long getGroupId() {
		return _socialRequest.getGroupId();
	}

	/**
	* Returns the modified date of this social request.
	*
	* @return the modified date of this social request
	*/
	@Override
	public long getModifiedDate() {
		return _socialRequest.getModifiedDate();
	}

	/**
	* Returns the primary key of this social request.
	*
	* @return the primary key of this social request
	*/
	@Override
	public long getPrimaryKey() {
		return _socialRequest.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _socialRequest.getPrimaryKeyObj();
	}

	/**
	* Returns the receiver user ID of this social request.
	*
	* @return the receiver user ID of this social request
	*/
	@Override
	public long getReceiverUserId() {
		return _socialRequest.getReceiverUserId();
	}

	/**
	* Returns the receiver user uuid of this social request.
	*
	* @return the receiver user uuid of this social request
	*/
	@Override
	public String getReceiverUserUuid() {
		return _socialRequest.getReceiverUserUuid();
	}

	/**
	* Returns the request ID of this social request.
	*
	* @return the request ID of this social request
	*/
	@Override
	public long getRequestId() {
		return _socialRequest.getRequestId();
	}

	/**
	* Returns the status of this social request.
	*
	* @return the status of this social request
	*/
	@Override
	public int getStatus() {
		return _socialRequest.getStatus();
	}

	/**
	* Returns the type of this social request.
	*
	* @return the type of this social request
	*/
	@Override
	public int getType() {
		return _socialRequest.getType();
	}

	/**
	* Returns the user ID of this social request.
	*
	* @return the user ID of this social request
	*/
	@Override
	public long getUserId() {
		return _socialRequest.getUserId();
	}

	/**
	* Returns the user uuid of this social request.
	*
	* @return the user uuid of this social request
	*/
	@Override
	public String getUserUuid() {
		return _socialRequest.getUserUuid();
	}

	/**
	* Returns the uuid of this social request.
	*
	* @return the uuid of this social request
	*/
	@Override
	public String getUuid() {
		return _socialRequest.getUuid();
	}

	@Override
	public int hashCode() {
		return _socialRequest.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _socialRequest.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _socialRequest.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _socialRequest.isNew();
	}

	@Override
	public void persist() {
		_socialRequest.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_socialRequest.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_socialRequest.setClassName(className);
	}

	/**
	* Sets the class name ID of this social request.
	*
	* @param classNameId the class name ID of this social request
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_socialRequest.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this social request.
	*
	* @param classPK the class pk of this social request
	*/
	@Override
	public void setClassPK(long classPK) {
		_socialRequest.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this social request.
	*
	* @param companyId the company ID of this social request
	*/
	@Override
	public void setCompanyId(long companyId) {
		_socialRequest.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this social request.
	*
	* @param createDate the create date of this social request
	*/
	@Override
	public void setCreateDate(long createDate) {
		_socialRequest.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_socialRequest.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_socialRequest.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_socialRequest.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the extra data of this social request.
	*
	* @param extraData the extra data of this social request
	*/
	@Override
	public void setExtraData(String extraData) {
		_socialRequest.setExtraData(extraData);
	}

	/**
	* Sets the group ID of this social request.
	*
	* @param groupId the group ID of this social request
	*/
	@Override
	public void setGroupId(long groupId) {
		_socialRequest.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this social request.
	*
	* @param modifiedDate the modified date of this social request
	*/
	@Override
	public void setModifiedDate(long modifiedDate) {
		_socialRequest.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_socialRequest.setNew(n);
	}

	/**
	* Sets the primary key of this social request.
	*
	* @param primaryKey the primary key of this social request
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_socialRequest.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_socialRequest.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the receiver user ID of this social request.
	*
	* @param receiverUserId the receiver user ID of this social request
	*/
	@Override
	public void setReceiverUserId(long receiverUserId) {
		_socialRequest.setReceiverUserId(receiverUserId);
	}

	/**
	* Sets the receiver user uuid of this social request.
	*
	* @param receiverUserUuid the receiver user uuid of this social request
	*/
	@Override
	public void setReceiverUserUuid(String receiverUserUuid) {
		_socialRequest.setReceiverUserUuid(receiverUserUuid);
	}

	/**
	* Sets the request ID of this social request.
	*
	* @param requestId the request ID of this social request
	*/
	@Override
	public void setRequestId(long requestId) {
		_socialRequest.setRequestId(requestId);
	}

	/**
	* Sets the status of this social request.
	*
	* @param status the status of this social request
	*/
	@Override
	public void setStatus(int status) {
		_socialRequest.setStatus(status);
	}

	/**
	* Sets the type of this social request.
	*
	* @param type the type of this social request
	*/
	@Override
	public void setType(int type) {
		_socialRequest.setType(type);
	}

	/**
	* Sets the user ID of this social request.
	*
	* @param userId the user ID of this social request
	*/
	@Override
	public void setUserId(long userId) {
		_socialRequest.setUserId(userId);
	}

	/**
	* Sets the user uuid of this social request.
	*
	* @param userUuid the user uuid of this social request
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_socialRequest.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this social request.
	*
	* @param uuid the uuid of this social request
	*/
	@Override
	public void setUuid(String uuid) {
		_socialRequest.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<SocialRequest> toCacheModel() {
		return _socialRequest.toCacheModel();
	}

	@Override
	public SocialRequest toEscapedModel() {
		return new SocialRequestWrapper(_socialRequest.toEscapedModel());
	}

	@Override
	public String toString() {
		return _socialRequest.toString();
	}

	@Override
	public SocialRequest toUnescapedModel() {
		return new SocialRequestWrapper(_socialRequest.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _socialRequest.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SocialRequestWrapper)) {
			return false;
		}

		SocialRequestWrapper socialRequestWrapper = (SocialRequestWrapper)obj;

		if (Objects.equals(_socialRequest, socialRequestWrapper._socialRequest)) {
			return true;
		}

		return false;
	}

	@Override
	public SocialRequest getWrappedModel() {
		return _socialRequest;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _socialRequest.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _socialRequest.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_socialRequest.resetOriginalValues();
	}

	private final SocialRequest _socialRequest;
}