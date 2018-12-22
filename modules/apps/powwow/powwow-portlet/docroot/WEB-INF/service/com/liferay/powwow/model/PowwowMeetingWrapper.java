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
 * This class is a wrapper for {@link PowwowMeeting}.
 * </p>
 *
 * @author Shinn Lok
 * @see PowwowMeeting
 * @generated
 */
@ProviderType
public class PowwowMeetingWrapper implements PowwowMeeting,
	ModelWrapper<PowwowMeeting> {
	public PowwowMeetingWrapper(PowwowMeeting powwowMeeting) {
		_powwowMeeting = powwowMeeting;
	}

	@Override
	public Class<?> getModelClass() {
		return PowwowMeeting.class;
	}

	@Override
	public String getModelClassName() {
		return PowwowMeeting.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<PowwowMeeting, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<PowwowMeeting, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<PowwowMeeting, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<PowwowMeeting, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<PowwowMeeting, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<PowwowMeeting, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<PowwowMeeting, Object>> getAttributeGetters() {
		return _powwowMeeting.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<PowwowMeeting, Object>> getAttributeSetters() {
		return _powwowMeeting.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new PowwowMeetingWrapper((PowwowMeeting)_powwowMeeting.clone());
	}

	@Override
	public int compareTo(PowwowMeeting powwowMeeting) {
		return _powwowMeeting.compareTo(powwowMeeting);
	}

	/**
	* Returns the calendar booking ID of this powwow meeting.
	*
	* @return the calendar booking ID of this powwow meeting
	*/
	@Override
	public long getCalendarBookingId() {
		return _powwowMeeting.getCalendarBookingId();
	}

	/**
	* Returns the company ID of this powwow meeting.
	*
	* @return the company ID of this powwow meeting
	*/
	@Override
	public long getCompanyId() {
		return _powwowMeeting.getCompanyId();
	}

	/**
	* Returns the create date of this powwow meeting.
	*
	* @return the create date of this powwow meeting
	*/
	@Override
	public Date getCreateDate() {
		return _powwowMeeting.getCreateDate();
	}

	/**
	* Returns the description of this powwow meeting.
	*
	* @return the description of this powwow meeting
	*/
	@Override
	public String getDescription() {
		return _powwowMeeting.getDescription();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _powwowMeeting.getExpandoBridge();
	}

	/**
	* Returns the group ID of this powwow meeting.
	*
	* @return the group ID of this powwow meeting
	*/
	@Override
	public long getGroupId() {
		return _powwowMeeting.getGroupId();
	}

	/**
	* Returns the language ID of this powwow meeting.
	*
	* @return the language ID of this powwow meeting
	*/
	@Override
	public String getLanguageId() {
		return _powwowMeeting.getLanguageId();
	}

	/**
	* Returns the modified date of this powwow meeting.
	*
	* @return the modified date of this powwow meeting
	*/
	@Override
	public Date getModifiedDate() {
		return _powwowMeeting.getModifiedDate();
	}

	/**
	* Returns the name of this powwow meeting.
	*
	* @return the name of this powwow meeting
	*/
	@Override
	public String getName() {
		return _powwowMeeting.getName();
	}

	/**
	* Returns the powwow meeting ID of this powwow meeting.
	*
	* @return the powwow meeting ID of this powwow meeting
	*/
	@Override
	public long getPowwowMeetingId() {
		return _powwowMeeting.getPowwowMeetingId();
	}

	/**
	* Returns the powwow server ID of this powwow meeting.
	*
	* @return the powwow server ID of this powwow meeting
	*/
	@Override
	public long getPowwowServerId() {
		return _powwowMeeting.getPowwowServerId();
	}

	/**
	* Returns the primary key of this powwow meeting.
	*
	* @return the primary key of this powwow meeting
	*/
	@Override
	public long getPrimaryKey() {
		return _powwowMeeting.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _powwowMeeting.getPrimaryKeyObj();
	}

	/**
	* Returns the provider type of this powwow meeting.
	*
	* @return the provider type of this powwow meeting
	*/
	@Override
	public String getProviderType() {
		return _powwowMeeting.getProviderType();
	}

	/**
	* Returns the provider type metadata of this powwow meeting.
	*
	* @return the provider type metadata of this powwow meeting
	*/
	@Override
	public String getProviderTypeMetadata() {
		return _powwowMeeting.getProviderTypeMetadata();
	}

	@Override
	public Map<String, Serializable> getProviderTypeMetadataMap() {
		return _powwowMeeting.getProviderTypeMetadataMap();
	}

	/**
	* Returns the status of this powwow meeting.
	*
	* @return the status of this powwow meeting
	*/
	@Override
	public int getStatus() {
		return _powwowMeeting.getStatus();
	}

	/**
	* Returns the user ID of this powwow meeting.
	*
	* @return the user ID of this powwow meeting
	*/
	@Override
	public long getUserId() {
		return _powwowMeeting.getUserId();
	}

	/**
	* Returns the user name of this powwow meeting.
	*
	* @return the user name of this powwow meeting
	*/
	@Override
	public String getUserName() {
		return _powwowMeeting.getUserName();
	}

	/**
	* Returns the user uuid of this powwow meeting.
	*
	* @return the user uuid of this powwow meeting
	*/
	@Override
	public String getUserUuid() {
		return _powwowMeeting.getUserUuid();
	}

	@Override
	public int hashCode() {
		return _powwowMeeting.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _powwowMeeting.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _powwowMeeting.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _powwowMeeting.isNew();
	}

	@Override
	public void persist() {
		_powwowMeeting.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_powwowMeeting.setCachedModel(cachedModel);
	}

	/**
	* Sets the calendar booking ID of this powwow meeting.
	*
	* @param calendarBookingId the calendar booking ID of this powwow meeting
	*/
	@Override
	public void setCalendarBookingId(long calendarBookingId) {
		_powwowMeeting.setCalendarBookingId(calendarBookingId);
	}

	/**
	* Sets the company ID of this powwow meeting.
	*
	* @param companyId the company ID of this powwow meeting
	*/
	@Override
	public void setCompanyId(long companyId) {
		_powwowMeeting.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this powwow meeting.
	*
	* @param createDate the create date of this powwow meeting
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_powwowMeeting.setCreateDate(createDate);
	}

	/**
	* Sets the description of this powwow meeting.
	*
	* @param description the description of this powwow meeting
	*/
	@Override
	public void setDescription(String description) {
		_powwowMeeting.setDescription(description);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_powwowMeeting.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_powwowMeeting.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_powwowMeeting.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this powwow meeting.
	*
	* @param groupId the group ID of this powwow meeting
	*/
	@Override
	public void setGroupId(long groupId) {
		_powwowMeeting.setGroupId(groupId);
	}

	/**
	* Sets the language ID of this powwow meeting.
	*
	* @param languageId the language ID of this powwow meeting
	*/
	@Override
	public void setLanguageId(String languageId) {
		_powwowMeeting.setLanguageId(languageId);
	}

	/**
	* Sets the modified date of this powwow meeting.
	*
	* @param modifiedDate the modified date of this powwow meeting
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_powwowMeeting.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this powwow meeting.
	*
	* @param name the name of this powwow meeting
	*/
	@Override
	public void setName(String name) {
		_powwowMeeting.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_powwowMeeting.setNew(n);
	}

	/**
	* Sets the powwow meeting ID of this powwow meeting.
	*
	* @param powwowMeetingId the powwow meeting ID of this powwow meeting
	*/
	@Override
	public void setPowwowMeetingId(long powwowMeetingId) {
		_powwowMeeting.setPowwowMeetingId(powwowMeetingId);
	}

	/**
	* Sets the powwow server ID of this powwow meeting.
	*
	* @param powwowServerId the powwow server ID of this powwow meeting
	*/
	@Override
	public void setPowwowServerId(long powwowServerId) {
		_powwowMeeting.setPowwowServerId(powwowServerId);
	}

	/**
	* Sets the primary key of this powwow meeting.
	*
	* @param primaryKey the primary key of this powwow meeting
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_powwowMeeting.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_powwowMeeting.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the provider type of this powwow meeting.
	*
	* @param providerType the provider type of this powwow meeting
	*/
	@Override
	public void setProviderType(String providerType) {
		_powwowMeeting.setProviderType(providerType);
	}

	/**
	* Sets the provider type metadata of this powwow meeting.
	*
	* @param providerTypeMetadata the provider type metadata of this powwow meeting
	*/
	@Override
	public void setProviderTypeMetadata(String providerTypeMetadata) {
		_powwowMeeting.setProviderTypeMetadata(providerTypeMetadata);
	}

	/**
	* Sets the status of this powwow meeting.
	*
	* @param status the status of this powwow meeting
	*/
	@Override
	public void setStatus(int status) {
		_powwowMeeting.setStatus(status);
	}

	/**
	* Sets the user ID of this powwow meeting.
	*
	* @param userId the user ID of this powwow meeting
	*/
	@Override
	public void setUserId(long userId) {
		_powwowMeeting.setUserId(userId);
	}

	/**
	* Sets the user name of this powwow meeting.
	*
	* @param userName the user name of this powwow meeting
	*/
	@Override
	public void setUserName(String userName) {
		_powwowMeeting.setUserName(userName);
	}

	/**
	* Sets the user uuid of this powwow meeting.
	*
	* @param userUuid the user uuid of this powwow meeting
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_powwowMeeting.setUserUuid(userUuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<PowwowMeeting> toCacheModel() {
		return _powwowMeeting.toCacheModel();
	}

	@Override
	public PowwowMeeting toEscapedModel() {
		return new PowwowMeetingWrapper(_powwowMeeting.toEscapedModel());
	}

	@Override
	public String toString() {
		return _powwowMeeting.toString();
	}

	@Override
	public PowwowMeeting toUnescapedModel() {
		return new PowwowMeetingWrapper(_powwowMeeting.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _powwowMeeting.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PowwowMeetingWrapper)) {
			return false;
		}

		PowwowMeetingWrapper powwowMeetingWrapper = (PowwowMeetingWrapper)obj;

		if (Objects.equals(_powwowMeeting, powwowMeetingWrapper._powwowMeeting)) {
			return true;
		}

		return false;
	}

	@Override
	public PowwowMeeting getWrappedModel() {
		return _powwowMeeting;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _powwowMeeting.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _powwowMeeting.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_powwowMeeting.resetOriginalValues();
	}

	private final PowwowMeeting _powwowMeeting;
}