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

package com.liferay.invitation.invite.members.model;

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
 * This class is a wrapper for {@link MemberRequest}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MemberRequest
 * @generated
 */
@ProviderType
public class MemberRequestWrapper implements MemberRequest,
	ModelWrapper<MemberRequest> {
	public MemberRequestWrapper(MemberRequest memberRequest) {
		_memberRequest = memberRequest;
	}

	@Override
	public Class<?> getModelClass() {
		return MemberRequest.class;
	}

	@Override
	public String getModelClassName() {
		return MemberRequest.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<MemberRequest, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<MemberRequest, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<MemberRequest, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<MemberRequest, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<MemberRequest, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<MemberRequest, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<MemberRequest, Object>> getAttributeGetters() {
		return _memberRequest.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<MemberRequest, Object>> getAttributeSetters() {
		return _memberRequest.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new MemberRequestWrapper((MemberRequest)_memberRequest.clone());
	}

	@Override
	public int compareTo(MemberRequest memberRequest) {
		return _memberRequest.compareTo(memberRequest);
	}

	/**
	* Returns the company ID of this member request.
	*
	* @return the company ID of this member request
	*/
	@Override
	public long getCompanyId() {
		return _memberRequest.getCompanyId();
	}

	/**
	* Returns the create date of this member request.
	*
	* @return the create date of this member request
	*/
	@Override
	public Date getCreateDate() {
		return _memberRequest.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _memberRequest.getExpandoBridge();
	}

	/**
	* Returns the group ID of this member request.
	*
	* @return the group ID of this member request
	*/
	@Override
	public long getGroupId() {
		return _memberRequest.getGroupId();
	}

	/**
	* Returns the invited role ID of this member request.
	*
	* @return the invited role ID of this member request
	*/
	@Override
	public long getInvitedRoleId() {
		return _memberRequest.getInvitedRoleId();
	}

	/**
	* Returns the invited team ID of this member request.
	*
	* @return the invited team ID of this member request
	*/
	@Override
	public long getInvitedTeamId() {
		return _memberRequest.getInvitedTeamId();
	}

	/**
	* Returns the key of this member request.
	*
	* @return the key of this member request
	*/
	@Override
	public String getKey() {
		return _memberRequest.getKey();
	}

	/**
	* Returns the member request ID of this member request.
	*
	* @return the member request ID of this member request
	*/
	@Override
	public long getMemberRequestId() {
		return _memberRequest.getMemberRequestId();
	}

	/**
	* Returns the modified date of this member request.
	*
	* @return the modified date of this member request
	*/
	@Override
	public Date getModifiedDate() {
		return _memberRequest.getModifiedDate();
	}

	/**
	* Returns the primary key of this member request.
	*
	* @return the primary key of this member request
	*/
	@Override
	public long getPrimaryKey() {
		return _memberRequest.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _memberRequest.getPrimaryKeyObj();
	}

	/**
	* Returns the receiver user ID of this member request.
	*
	* @return the receiver user ID of this member request
	*/
	@Override
	public long getReceiverUserId() {
		return _memberRequest.getReceiverUserId();
	}

	/**
	* Returns the receiver user uuid of this member request.
	*
	* @return the receiver user uuid of this member request
	*/
	@Override
	public String getReceiverUserUuid() {
		return _memberRequest.getReceiverUserUuid();
	}

	/**
	* Returns the status of this member request.
	*
	* @return the status of this member request
	*/
	@Override
	public int getStatus() {
		return _memberRequest.getStatus();
	}

	/**
	* Returns the user ID of this member request.
	*
	* @return the user ID of this member request
	*/
	@Override
	public long getUserId() {
		return _memberRequest.getUserId();
	}

	/**
	* Returns the user name of this member request.
	*
	* @return the user name of this member request
	*/
	@Override
	public String getUserName() {
		return _memberRequest.getUserName();
	}

	/**
	* Returns the user uuid of this member request.
	*
	* @return the user uuid of this member request
	*/
	@Override
	public String getUserUuid() {
		return _memberRequest.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _memberRequest.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _memberRequest.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _memberRequest.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _memberRequest.isNew();
	}

	@Override
	public void persist() {
		_memberRequest.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_memberRequest.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this member request.
	*
	* @param companyId the company ID of this member request
	*/
	@Override
	public void setCompanyId(long companyId) {
		_memberRequest.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this member request.
	*
	* @param createDate the create date of this member request
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_memberRequest.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_memberRequest.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_memberRequest.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_memberRequest.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this member request.
	*
	* @param groupId the group ID of this member request
	*/
	@Override
	public void setGroupId(long groupId) {
		_memberRequest.setGroupId(groupId);
	}

	/**
	* Sets the invited role ID of this member request.
	*
	* @param invitedRoleId the invited role ID of this member request
	*/
	@Override
	public void setInvitedRoleId(long invitedRoleId) {
		_memberRequest.setInvitedRoleId(invitedRoleId);
	}

	/**
	* Sets the invited team ID of this member request.
	*
	* @param invitedTeamId the invited team ID of this member request
	*/
	@Override
	public void setInvitedTeamId(long invitedTeamId) {
		_memberRequest.setInvitedTeamId(invitedTeamId);
	}

	/**
	* Sets the key of this member request.
	*
	* @param key the key of this member request
	*/
	@Override
	public void setKey(String key) {
		_memberRequest.setKey(key);
	}

	/**
	* Sets the member request ID of this member request.
	*
	* @param memberRequestId the member request ID of this member request
	*/
	@Override
	public void setMemberRequestId(long memberRequestId) {
		_memberRequest.setMemberRequestId(memberRequestId);
	}

	/**
	* Sets the modified date of this member request.
	*
	* @param modifiedDate the modified date of this member request
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_memberRequest.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_memberRequest.setNew(n);
	}

	/**
	* Sets the primary key of this member request.
	*
	* @param primaryKey the primary key of this member request
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_memberRequest.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_memberRequest.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the receiver user ID of this member request.
	*
	* @param receiverUserId the receiver user ID of this member request
	*/
	@Override
	public void setReceiverUserId(long receiverUserId) {
		_memberRequest.setReceiverUserId(receiverUserId);
	}

	/**
	* Sets the receiver user uuid of this member request.
	*
	* @param receiverUserUuid the receiver user uuid of this member request
	*/
	@Override
	public void setReceiverUserUuid(String receiverUserUuid) {
		_memberRequest.setReceiverUserUuid(receiverUserUuid);
	}

	/**
	* Sets the status of this member request.
	*
	* @param status the status of this member request
	*/
	@Override
	public void setStatus(int status) {
		_memberRequest.setStatus(status);
	}

	/**
	* Sets the user ID of this member request.
	*
	* @param userId the user ID of this member request
	*/
	@Override
	public void setUserId(long userId) {
		_memberRequest.setUserId(userId);
	}

	/**
	* Sets the user name of this member request.
	*
	* @param userName the user name of this member request
	*/
	@Override
	public void setUserName(String userName) {
		_memberRequest.setUserName(userName);
	}

	/**
	* Sets the user uuid of this member request.
	*
	* @param userUuid the user uuid of this member request
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_memberRequest.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<MemberRequest> toCacheModel() {
		return _memberRequest.toCacheModel();
	}

	@Override
	public MemberRequest toEscapedModel() {
		return new MemberRequestWrapper(_memberRequest.toEscapedModel());
	}

	@Override
	public String toString() {
		return _memberRequest.toString();
	}

	@Override
	public MemberRequest toUnescapedModel() {
		return new MemberRequestWrapper(_memberRequest.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _memberRequest.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MemberRequestWrapper)) {
			return false;
		}

		MemberRequestWrapper memberRequestWrapper = (MemberRequestWrapper)obj;

		if (Objects.equals(_memberRequest, memberRequestWrapper._memberRequest)) {
			return true;
		}

		return false;
	}

	@Override
	public MemberRequest getWrappedModel() {
		return _memberRequest;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _memberRequest.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _memberRequest.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_memberRequest.resetOriginalValues();
	}

	private final MemberRequest _memberRequest;
}