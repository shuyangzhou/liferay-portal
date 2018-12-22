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

package com.liferay.message.boards.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.exportimport.kernel.lar.StagedModelType;

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
 * This class is a wrapper for {@link MBMailingList}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBMailingList
 * @generated
 */
@ProviderType
public class MBMailingListWrapper implements MBMailingList,
	ModelWrapper<MBMailingList> {
	public MBMailingListWrapper(MBMailingList mbMailingList) {
		_mbMailingList = mbMailingList;
	}

	@Override
	public Class<?> getModelClass() {
		return MBMailingList.class;
	}

	@Override
	public String getModelClassName() {
		return MBMailingList.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<MBMailingList, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<MBMailingList, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<MBMailingList, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<MBMailingList, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<MBMailingList, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<MBMailingList, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<MBMailingList, Object>> getAttributeGetters() {
		return _mbMailingList.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<MBMailingList, Object>> getAttributeSetters() {
		return _mbMailingList.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new MBMailingListWrapper((MBMailingList)_mbMailingList.clone());
	}

	@Override
	public int compareTo(MBMailingList mbMailingList) {
		return _mbMailingList.compareTo(mbMailingList);
	}

	/**
	* Returns the active of this message boards mailing list.
	*
	* @return the active of this message boards mailing list
	*/
	@Override
	public boolean getActive() {
		return _mbMailingList.getActive();
	}

	/**
	* Returns the allow anonymous of this message boards mailing list.
	*
	* @return the allow anonymous of this message boards mailing list
	*/
	@Override
	public boolean getAllowAnonymous() {
		return _mbMailingList.getAllowAnonymous();
	}

	/**
	* Returns the category ID of this message boards mailing list.
	*
	* @return the category ID of this message boards mailing list
	*/
	@Override
	public long getCategoryId() {
		return _mbMailingList.getCategoryId();
	}

	/**
	* Returns the company ID of this message boards mailing list.
	*
	* @return the company ID of this message boards mailing list
	*/
	@Override
	public long getCompanyId() {
		return _mbMailingList.getCompanyId();
	}

	/**
	* Returns the create date of this message boards mailing list.
	*
	* @return the create date of this message boards mailing list
	*/
	@Override
	public Date getCreateDate() {
		return _mbMailingList.getCreateDate();
	}

	/**
	* Returns the email address of this message boards mailing list.
	*
	* @return the email address of this message boards mailing list
	*/
	@Override
	public String getEmailAddress() {
		return _mbMailingList.getEmailAddress();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _mbMailingList.getExpandoBridge();
	}

	/**
	* Returns the group ID of this message boards mailing list.
	*
	* @return the group ID of this message boards mailing list
	*/
	@Override
	public long getGroupId() {
		return _mbMailingList.getGroupId();
	}

	/**
	* Returns the in password of this message boards mailing list.
	*
	* @return the in password of this message boards mailing list
	*/
	@Override
	public String getInPassword() {
		return _mbMailingList.getInPassword();
	}

	/**
	* Returns the in protocol of this message boards mailing list.
	*
	* @return the in protocol of this message boards mailing list
	*/
	@Override
	public String getInProtocol() {
		return _mbMailingList.getInProtocol();
	}

	/**
	* Returns the in read interval of this message boards mailing list.
	*
	* @return the in read interval of this message boards mailing list
	*/
	@Override
	public int getInReadInterval() {
		return _mbMailingList.getInReadInterval();
	}

	/**
	* Returns the in server name of this message boards mailing list.
	*
	* @return the in server name of this message boards mailing list
	*/
	@Override
	public String getInServerName() {
		return _mbMailingList.getInServerName();
	}

	/**
	* Returns the in server port of this message boards mailing list.
	*
	* @return the in server port of this message boards mailing list
	*/
	@Override
	public int getInServerPort() {
		return _mbMailingList.getInServerPort();
	}

	/**
	* Returns the in user name of this message boards mailing list.
	*
	* @return the in user name of this message boards mailing list
	*/
	@Override
	public String getInUserName() {
		return _mbMailingList.getInUserName();
	}

	/**
	* Returns the in use ssl of this message boards mailing list.
	*
	* @return the in use ssl of this message boards mailing list
	*/
	@Override
	public boolean getInUseSSL() {
		return _mbMailingList.getInUseSSL();
	}

	/**
	* Returns the mailing list ID of this message boards mailing list.
	*
	* @return the mailing list ID of this message boards mailing list
	*/
	@Override
	public long getMailingListId() {
		return _mbMailingList.getMailingListId();
	}

	/**
	* Returns the modified date of this message boards mailing list.
	*
	* @return the modified date of this message boards mailing list
	*/
	@Override
	public Date getModifiedDate() {
		return _mbMailingList.getModifiedDate();
	}

	/**
	* Returns the out custom of this message boards mailing list.
	*
	* @return the out custom of this message boards mailing list
	*/
	@Override
	public boolean getOutCustom() {
		return _mbMailingList.getOutCustom();
	}

	/**
	* Returns the out email address of this message boards mailing list.
	*
	* @return the out email address of this message boards mailing list
	*/
	@Override
	public String getOutEmailAddress() {
		return _mbMailingList.getOutEmailAddress();
	}

	/**
	* Returns the out password of this message boards mailing list.
	*
	* @return the out password of this message boards mailing list
	*/
	@Override
	public String getOutPassword() {
		return _mbMailingList.getOutPassword();
	}

	/**
	* Returns the out server name of this message boards mailing list.
	*
	* @return the out server name of this message boards mailing list
	*/
	@Override
	public String getOutServerName() {
		return _mbMailingList.getOutServerName();
	}

	/**
	* Returns the out server port of this message boards mailing list.
	*
	* @return the out server port of this message boards mailing list
	*/
	@Override
	public int getOutServerPort() {
		return _mbMailingList.getOutServerPort();
	}

	/**
	* Returns the out user name of this message boards mailing list.
	*
	* @return the out user name of this message boards mailing list
	*/
	@Override
	public String getOutUserName() {
		return _mbMailingList.getOutUserName();
	}

	/**
	* Returns the out use ssl of this message boards mailing list.
	*
	* @return the out use ssl of this message boards mailing list
	*/
	@Override
	public boolean getOutUseSSL() {
		return _mbMailingList.getOutUseSSL();
	}

	/**
	* Returns the primary key of this message boards mailing list.
	*
	* @return the primary key of this message boards mailing list
	*/
	@Override
	public long getPrimaryKey() {
		return _mbMailingList.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _mbMailingList.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this message boards mailing list.
	*
	* @return the user ID of this message boards mailing list
	*/
	@Override
	public long getUserId() {
		return _mbMailingList.getUserId();
	}

	/**
	* Returns the user name of this message boards mailing list.
	*
	* @return the user name of this message boards mailing list
	*/
	@Override
	public String getUserName() {
		return _mbMailingList.getUserName();
	}

	/**
	* Returns the user uuid of this message boards mailing list.
	*
	* @return the user uuid of this message boards mailing list
	*/
	@Override
	public String getUserUuid() {
		return _mbMailingList.getUserUuid();
	}

	/**
	* Returns the uuid of this message boards mailing list.
	*
	* @return the uuid of this message boards mailing list
	*/
	@Override
	public String getUuid() {
		return _mbMailingList.getUuid();
	}

	@Override
	public int hashCode() {
		return _mbMailingList.hashCode();
	}

	/**
	* Returns <code>true</code> if this message boards mailing list is active.
	*
	* @return <code>true</code> if this message boards mailing list is active; <code>false</code> otherwise
	*/
	@Override
	public boolean isActive() {
		return _mbMailingList.isActive();
	}

	/**
	* Returns <code>true</code> if this message boards mailing list is allow anonymous.
	*
	* @return <code>true</code> if this message boards mailing list is allow anonymous; <code>false</code> otherwise
	*/
	@Override
	public boolean isAllowAnonymous() {
		return _mbMailingList.isAllowAnonymous();
	}

	@Override
	public boolean isCachedModel() {
		return _mbMailingList.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _mbMailingList.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this message boards mailing list is in use ssl.
	*
	* @return <code>true</code> if this message boards mailing list is in use ssl; <code>false</code> otherwise
	*/
	@Override
	public boolean isInUseSSL() {
		return _mbMailingList.isInUseSSL();
	}

	@Override
	public boolean isNew() {
		return _mbMailingList.isNew();
	}

	/**
	* Returns <code>true</code> if this message boards mailing list is out custom.
	*
	* @return <code>true</code> if this message boards mailing list is out custom; <code>false</code> otherwise
	*/
	@Override
	public boolean isOutCustom() {
		return _mbMailingList.isOutCustom();
	}

	/**
	* Returns <code>true</code> if this message boards mailing list is out use ssl.
	*
	* @return <code>true</code> if this message boards mailing list is out use ssl; <code>false</code> otherwise
	*/
	@Override
	public boolean isOutUseSSL() {
		return _mbMailingList.isOutUseSSL();
	}

	@Override
	public void persist() {
		_mbMailingList.persist();
	}

	/**
	* Sets whether this message boards mailing list is active.
	*
	* @param active the active of this message boards mailing list
	*/
	@Override
	public void setActive(boolean active) {
		_mbMailingList.setActive(active);
	}

	/**
	* Sets whether this message boards mailing list is allow anonymous.
	*
	* @param allowAnonymous the allow anonymous of this message boards mailing list
	*/
	@Override
	public void setAllowAnonymous(boolean allowAnonymous) {
		_mbMailingList.setAllowAnonymous(allowAnonymous);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_mbMailingList.setCachedModel(cachedModel);
	}

	/**
	* Sets the category ID of this message boards mailing list.
	*
	* @param categoryId the category ID of this message boards mailing list
	*/
	@Override
	public void setCategoryId(long categoryId) {
		_mbMailingList.setCategoryId(categoryId);
	}

	/**
	* Sets the company ID of this message boards mailing list.
	*
	* @param companyId the company ID of this message boards mailing list
	*/
	@Override
	public void setCompanyId(long companyId) {
		_mbMailingList.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this message boards mailing list.
	*
	* @param createDate the create date of this message boards mailing list
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_mbMailingList.setCreateDate(createDate);
	}

	/**
	* Sets the email address of this message boards mailing list.
	*
	* @param emailAddress the email address of this message boards mailing list
	*/
	@Override
	public void setEmailAddress(String emailAddress) {
		_mbMailingList.setEmailAddress(emailAddress);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_mbMailingList.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_mbMailingList.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_mbMailingList.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this message boards mailing list.
	*
	* @param groupId the group ID of this message boards mailing list
	*/
	@Override
	public void setGroupId(long groupId) {
		_mbMailingList.setGroupId(groupId);
	}

	/**
	* Sets the in password of this message boards mailing list.
	*
	* @param inPassword the in password of this message boards mailing list
	*/
	@Override
	public void setInPassword(String inPassword) {
		_mbMailingList.setInPassword(inPassword);
	}

	/**
	* Sets the in protocol of this message boards mailing list.
	*
	* @param inProtocol the in protocol of this message boards mailing list
	*/
	@Override
	public void setInProtocol(String inProtocol) {
		_mbMailingList.setInProtocol(inProtocol);
	}

	/**
	* Sets the in read interval of this message boards mailing list.
	*
	* @param inReadInterval the in read interval of this message boards mailing list
	*/
	@Override
	public void setInReadInterval(int inReadInterval) {
		_mbMailingList.setInReadInterval(inReadInterval);
	}

	/**
	* Sets the in server name of this message boards mailing list.
	*
	* @param inServerName the in server name of this message boards mailing list
	*/
	@Override
	public void setInServerName(String inServerName) {
		_mbMailingList.setInServerName(inServerName);
	}

	/**
	* Sets the in server port of this message boards mailing list.
	*
	* @param inServerPort the in server port of this message boards mailing list
	*/
	@Override
	public void setInServerPort(int inServerPort) {
		_mbMailingList.setInServerPort(inServerPort);
	}

	/**
	* Sets the in user name of this message boards mailing list.
	*
	* @param inUserName the in user name of this message boards mailing list
	*/
	@Override
	public void setInUserName(String inUserName) {
		_mbMailingList.setInUserName(inUserName);
	}

	/**
	* Sets whether this message boards mailing list is in use ssl.
	*
	* @param inUseSSL the in use ssl of this message boards mailing list
	*/
	@Override
	public void setInUseSSL(boolean inUseSSL) {
		_mbMailingList.setInUseSSL(inUseSSL);
	}

	/**
	* Sets the mailing list ID of this message boards mailing list.
	*
	* @param mailingListId the mailing list ID of this message boards mailing list
	*/
	@Override
	public void setMailingListId(long mailingListId) {
		_mbMailingList.setMailingListId(mailingListId);
	}

	/**
	* Sets the modified date of this message boards mailing list.
	*
	* @param modifiedDate the modified date of this message boards mailing list
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_mbMailingList.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_mbMailingList.setNew(n);
	}

	/**
	* Sets whether this message boards mailing list is out custom.
	*
	* @param outCustom the out custom of this message boards mailing list
	*/
	@Override
	public void setOutCustom(boolean outCustom) {
		_mbMailingList.setOutCustom(outCustom);
	}

	/**
	* Sets the out email address of this message boards mailing list.
	*
	* @param outEmailAddress the out email address of this message boards mailing list
	*/
	@Override
	public void setOutEmailAddress(String outEmailAddress) {
		_mbMailingList.setOutEmailAddress(outEmailAddress);
	}

	/**
	* Sets the out password of this message boards mailing list.
	*
	* @param outPassword the out password of this message boards mailing list
	*/
	@Override
	public void setOutPassword(String outPassword) {
		_mbMailingList.setOutPassword(outPassword);
	}

	/**
	* Sets the out server name of this message boards mailing list.
	*
	* @param outServerName the out server name of this message boards mailing list
	*/
	@Override
	public void setOutServerName(String outServerName) {
		_mbMailingList.setOutServerName(outServerName);
	}

	/**
	* Sets the out server port of this message boards mailing list.
	*
	* @param outServerPort the out server port of this message boards mailing list
	*/
	@Override
	public void setOutServerPort(int outServerPort) {
		_mbMailingList.setOutServerPort(outServerPort);
	}

	/**
	* Sets the out user name of this message boards mailing list.
	*
	* @param outUserName the out user name of this message boards mailing list
	*/
	@Override
	public void setOutUserName(String outUserName) {
		_mbMailingList.setOutUserName(outUserName);
	}

	/**
	* Sets whether this message boards mailing list is out use ssl.
	*
	* @param outUseSSL the out use ssl of this message boards mailing list
	*/
	@Override
	public void setOutUseSSL(boolean outUseSSL) {
		_mbMailingList.setOutUseSSL(outUseSSL);
	}

	/**
	* Sets the primary key of this message boards mailing list.
	*
	* @param primaryKey the primary key of this message boards mailing list
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_mbMailingList.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_mbMailingList.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this message boards mailing list.
	*
	* @param userId the user ID of this message boards mailing list
	*/
	@Override
	public void setUserId(long userId) {
		_mbMailingList.setUserId(userId);
	}

	/**
	* Sets the user name of this message boards mailing list.
	*
	* @param userName the user name of this message boards mailing list
	*/
	@Override
	public void setUserName(String userName) {
		_mbMailingList.setUserName(userName);
	}

	/**
	* Sets the user uuid of this message boards mailing list.
	*
	* @param userUuid the user uuid of this message boards mailing list
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_mbMailingList.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this message boards mailing list.
	*
	* @param uuid the uuid of this message boards mailing list
	*/
	@Override
	public void setUuid(String uuid) {
		_mbMailingList.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<MBMailingList> toCacheModel() {
		return _mbMailingList.toCacheModel();
	}

	@Override
	public MBMailingList toEscapedModel() {
		return new MBMailingListWrapper(_mbMailingList.toEscapedModel());
	}

	@Override
	public String toString() {
		return _mbMailingList.toString();
	}

	@Override
	public MBMailingList toUnescapedModel() {
		return new MBMailingListWrapper(_mbMailingList.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _mbMailingList.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MBMailingListWrapper)) {
			return false;
		}

		MBMailingListWrapper mbMailingListWrapper = (MBMailingListWrapper)obj;

		if (Objects.equals(_mbMailingList, mbMailingListWrapper._mbMailingList)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _mbMailingList.getStagedModelType();
	}

	@Override
	public MBMailingList getWrappedModel() {
		return _mbMailingList;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _mbMailingList.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _mbMailingList.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_mbMailingList.resetOriginalValues();
	}

	private final MBMailingList _mbMailingList;
}