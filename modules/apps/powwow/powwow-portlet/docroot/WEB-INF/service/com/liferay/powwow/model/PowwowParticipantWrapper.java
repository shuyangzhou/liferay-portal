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

package com.liferay.powwow.model;

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
 * This class is a wrapper for {@link PowwowParticipant}.
 * </p>
 *
 * @author Shinn Lok
 * @see PowwowParticipant
 * @generated
 */
@ProviderType
public class PowwowParticipantWrapper implements PowwowParticipant,
	ModelWrapper<PowwowParticipant> {
	public PowwowParticipantWrapper(PowwowParticipant powwowParticipant) {
		_powwowParticipant = powwowParticipant;
	}

	@Override
	public Class<?> getModelClass() {
		return PowwowParticipant.class;
	}

	@Override
	public String getModelClassName() {
		return PowwowParticipant.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PowwowParticipant, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<PowwowParticipant, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<PowwowParticipant, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PowwowParticipant, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<PowwowParticipant, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<PowwowParticipant, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<PowwowParticipant, Object>> getAttributeGetters() {
		return _powwowParticipant.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<PowwowParticipant, Object>> getAttributeSetters() {
		return _powwowParticipant.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new PowwowParticipantWrapper((PowwowParticipant)_powwowParticipant.clone());
	}

	@Override
	public int compareTo(PowwowParticipant powwowParticipant) {
		return _powwowParticipant.compareTo(powwowParticipant);
	}

	/**
	* Returns the company ID of this powwow participant.
	*
	* @return the company ID of this powwow participant
	*/
	@Override
	public long getCompanyId() {
		return _powwowParticipant.getCompanyId();
	}

	/**
	* Returns the create date of this powwow participant.
	*
	* @return the create date of this powwow participant
	*/
	@Override
	public Date getCreateDate() {
		return _powwowParticipant.getCreateDate();
	}

	/**
	* Returns the email address of this powwow participant.
	*
	* @return the email address of this powwow participant
	*/
	@Override
	public String getEmailAddress() {
		return _powwowParticipant.getEmailAddress();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _powwowParticipant.getExpandoBridge();
	}

	/**
	* Returns the group ID of this powwow participant.
	*
	* @return the group ID of this powwow participant
	*/
	@Override
	public long getGroupId() {
		return _powwowParticipant.getGroupId();
	}

	/**
	* Returns the modified date of this powwow participant.
	*
	* @return the modified date of this powwow participant
	*/
	@Override
	public Date getModifiedDate() {
		return _powwowParticipant.getModifiedDate();
	}

	/**
	* Returns the name of this powwow participant.
	*
	* @return the name of this powwow participant
	*/
	@Override
	public String getName() {
		return _powwowParticipant.getName();
	}

	/**
	* Returns the participant user ID of this powwow participant.
	*
	* @return the participant user ID of this powwow participant
	*/
	@Override
	public long getParticipantUserId() {
		return _powwowParticipant.getParticipantUserId();
	}

	/**
	* Returns the participant user uuid of this powwow participant.
	*
	* @return the participant user uuid of this powwow participant
	*/
	@Override
	public String getParticipantUserUuid() {
		return _powwowParticipant.getParticipantUserUuid();
	}

	/**
	* Returns the powwow meeting ID of this powwow participant.
	*
	* @return the powwow meeting ID of this powwow participant
	*/
	@Override
	public long getPowwowMeetingId() {
		return _powwowParticipant.getPowwowMeetingId();
	}

	/**
	* Returns the powwow participant ID of this powwow participant.
	*
	* @return the powwow participant ID of this powwow participant
	*/
	@Override
	public long getPowwowParticipantId() {
		return _powwowParticipant.getPowwowParticipantId();
	}

	/**
	* Returns the primary key of this powwow participant.
	*
	* @return the primary key of this powwow participant
	*/
	@Override
	public long getPrimaryKey() {
		return _powwowParticipant.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _powwowParticipant.getPrimaryKeyObj();
	}

	/**
	* Returns the status of this powwow participant.
	*
	* @return the status of this powwow participant
	*/
	@Override
	public int getStatus() {
		return _powwowParticipant.getStatus();
	}

	/**
	* Returns the type of this powwow participant.
	*
	* @return the type of this powwow participant
	*/
	@Override
	public int getType() {
		return _powwowParticipant.getType();
	}

	/**
	* Returns the user ID of this powwow participant.
	*
	* @return the user ID of this powwow participant
	*/
	@Override
	public long getUserId() {
		return _powwowParticipant.getUserId();
	}

	/**
	* Returns the user name of this powwow participant.
	*
	* @return the user name of this powwow participant
	*/
	@Override
	public String getUserName() {
		return _powwowParticipant.getUserName();
	}

	/**
	* Returns the user uuid of this powwow participant.
	*
	* @return the user uuid of this powwow participant
	*/
	@Override
	public String getUserUuid() {
		return _powwowParticipant.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _powwowParticipant.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _powwowParticipant.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _powwowParticipant.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _powwowParticipant.isNew();
	}

	@Override
	public void persist() {
		_powwowParticipant.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_powwowParticipant.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this powwow participant.
	*
	* @param companyId the company ID of this powwow participant
	*/
	@Override
	public void setCompanyId(long companyId) {
		_powwowParticipant.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this powwow participant.
	*
	* @param createDate the create date of this powwow participant
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_powwowParticipant.setCreateDate(createDate);
	}

	/**
	* Sets the email address of this powwow participant.
	*
	* @param emailAddress the email address of this powwow participant
	*/
	@Override
	public void setEmailAddress(String emailAddress) {
		_powwowParticipant.setEmailAddress(emailAddress);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_powwowParticipant.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_powwowParticipant.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_powwowParticipant.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this powwow participant.
	*
	* @param groupId the group ID of this powwow participant
	*/
	@Override
	public void setGroupId(long groupId) {
		_powwowParticipant.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this powwow participant.
	*
	* @param modifiedDate the modified date of this powwow participant
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_powwowParticipant.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this powwow participant.
	*
	* @param name the name of this powwow participant
	*/
	@Override
	public void setName(String name) {
		_powwowParticipant.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_powwowParticipant.setNew(n);
	}

	/**
	* Sets the participant user ID of this powwow participant.
	*
	* @param participantUserId the participant user ID of this powwow participant
	*/
	@Override
	public void setParticipantUserId(long participantUserId) {
		_powwowParticipant.setParticipantUserId(participantUserId);
	}

	/**
	* Sets the participant user uuid of this powwow participant.
	*
	* @param participantUserUuid the participant user uuid of this powwow participant
	*/
	@Override
	public void setParticipantUserUuid(String participantUserUuid) {
		_powwowParticipant.setParticipantUserUuid(participantUserUuid);
	}

	/**
	* Sets the powwow meeting ID of this powwow participant.
	*
	* @param powwowMeetingId the powwow meeting ID of this powwow participant
	*/
	@Override
	public void setPowwowMeetingId(long powwowMeetingId) {
		_powwowParticipant.setPowwowMeetingId(powwowMeetingId);
	}

	/**
	* Sets the powwow participant ID of this powwow participant.
	*
	* @param powwowParticipantId the powwow participant ID of this powwow participant
	*/
	@Override
	public void setPowwowParticipantId(long powwowParticipantId) {
		_powwowParticipant.setPowwowParticipantId(powwowParticipantId);
	}

	/**
	* Sets the primary key of this powwow participant.
	*
	* @param primaryKey the primary key of this powwow participant
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_powwowParticipant.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_powwowParticipant.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the status of this powwow participant.
	*
	* @param status the status of this powwow participant
	*/
	@Override
	public void setStatus(int status) {
		_powwowParticipant.setStatus(status);
	}

	/**
	* Sets the type of this powwow participant.
	*
	* @param type the type of this powwow participant
	*/
	@Override
	public void setType(int type) {
		_powwowParticipant.setType(type);
	}

	/**
	* Sets the user ID of this powwow participant.
	*
	* @param userId the user ID of this powwow participant
	*/
	@Override
	public void setUserId(long userId) {
		_powwowParticipant.setUserId(userId);
	}

	/**
	* Sets the user name of this powwow participant.
	*
	* @param userName the user name of this powwow participant
	*/
	@Override
	public void setUserName(String userName) {
		_powwowParticipant.setUserName(userName);
	}

	/**
	* Sets the user uuid of this powwow participant.
	*
	* @param userUuid the user uuid of this powwow participant
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_powwowParticipant.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<PowwowParticipant> toCacheModel() {
		return _powwowParticipant.toCacheModel();
	}

	@Override
	public PowwowParticipant toEscapedModel() {
		return new PowwowParticipantWrapper(_powwowParticipant.toEscapedModel());
	}

	@Override
	public String toString() {
		return _powwowParticipant.toString();
	}

	@Override
	public PowwowParticipant toUnescapedModel() {
		return new PowwowParticipantWrapper(_powwowParticipant.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _powwowParticipant.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PowwowParticipantWrapper)) {
			return false;
		}

		PowwowParticipantWrapper powwowParticipantWrapper = (PowwowParticipantWrapper)obj;

		if (Objects.equals(_powwowParticipant,
					powwowParticipantWrapper._powwowParticipant)) {
			return true;
		}

		return false;
	}

	@Override
	public PowwowParticipant getWrappedModel() {
		return _powwowParticipant;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _powwowParticipant.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _powwowParticipant.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_powwowParticipant.resetOriginalValues();
	}

	private final PowwowParticipant _powwowParticipant;
}