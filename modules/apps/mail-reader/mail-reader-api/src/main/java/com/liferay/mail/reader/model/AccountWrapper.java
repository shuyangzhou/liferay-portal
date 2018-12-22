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

package com.liferay.mail.reader.model;

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
 * This class is a wrapper for {@link Account}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Account
 * @generated
 */
@ProviderType
public class AccountWrapper implements Account, ModelWrapper<Account> {
	public AccountWrapper(Account account) {
		_account = account;
	}

	@Override
	public Class<?> getModelClass() {
		return Account.class;
	}

	@Override
	public String getModelClassName() {
		return Account.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Account, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Account, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Account, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Account, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Account, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Account, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Account, Object>> getAttributeGetters() {
		return _account.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Account, Object>> getAttributeSetters() {
		return _account.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AccountWrapper((Account)_account.clone());
	}

	@Override
	public int compareTo(Account account) {
		return _account.compareTo(account);
	}

	/**
	* Returns the account ID of this account.
	*
	* @return the account ID of this account
	*/
	@Override
	public long getAccountId() {
		return _account.getAccountId();
	}

	/**
	* Returns the address of this account.
	*
	* @return the address of this account
	*/
	@Override
	public String getAddress() {
		return _account.getAddress();
	}

	/**
	* Returns the company ID of this account.
	*
	* @return the company ID of this account
	*/
	@Override
	public long getCompanyId() {
		return _account.getCompanyId();
	}

	/**
	* Returns the create date of this account.
	*
	* @return the create date of this account
	*/
	@Override
	public Date getCreateDate() {
		return _account.getCreateDate();
	}

	/**
	* Returns the default sender of this account.
	*
	* @return the default sender of this account
	*/
	@Override
	public boolean getDefaultSender() {
		return _account.getDefaultSender();
	}

	/**
	* Returns the draft folder ID of this account.
	*
	* @return the draft folder ID of this account
	*/
	@Override
	public long getDraftFolderId() {
		return _account.getDraftFolderId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _account.getExpandoBridge();
	}

	/**
	* Returns the folder prefix of this account.
	*
	* @return the folder prefix of this account
	*/
	@Override
	public String getFolderPrefix() {
		return _account.getFolderPrefix();
	}

	/**
	* Returns the inbox folder ID of this account.
	*
	* @return the inbox folder ID of this account
	*/
	@Override
	public long getInboxFolderId() {
		return _account.getInboxFolderId();
	}

	/**
	* Returns the incoming host name of this account.
	*
	* @return the incoming host name of this account
	*/
	@Override
	public String getIncomingHostName() {
		return _account.getIncomingHostName();
	}

	/**
	* Returns the incoming port of this account.
	*
	* @return the incoming port of this account
	*/
	@Override
	public int getIncomingPort() {
		return _account.getIncomingPort();
	}

	/**
	* Returns the incoming secure of this account.
	*
	* @return the incoming secure of this account
	*/
	@Override
	public boolean getIncomingSecure() {
		return _account.getIncomingSecure();
	}

	/**
	* Returns the login of this account.
	*
	* @return the login of this account
	*/
	@Override
	public String getLogin() {
		return _account.getLogin();
	}

	/**
	* Returns the modified date of this account.
	*
	* @return the modified date of this account
	*/
	@Override
	public Date getModifiedDate() {
		return _account.getModifiedDate();
	}

	/**
	* Returns the outgoing host name of this account.
	*
	* @return the outgoing host name of this account
	*/
	@Override
	public String getOutgoingHostName() {
		return _account.getOutgoingHostName();
	}

	/**
	* Returns the outgoing port of this account.
	*
	* @return the outgoing port of this account
	*/
	@Override
	public int getOutgoingPort() {
		return _account.getOutgoingPort();
	}

	/**
	* Returns the outgoing secure of this account.
	*
	* @return the outgoing secure of this account
	*/
	@Override
	public boolean getOutgoingSecure() {
		return _account.getOutgoingSecure();
	}

	/**
	* Returns the password of this account.
	*
	* @return the password of this account
	*/
	@Override
	public String getPassword() {
		return _account.getPassword();
	}

	@Override
	public String getPasswordDecrypted() {
		return _account.getPasswordDecrypted();
	}

	/**
	* Returns the personal name of this account.
	*
	* @return the personal name of this account
	*/
	@Override
	public String getPersonalName() {
		return _account.getPersonalName();
	}

	/**
	* Returns the primary key of this account.
	*
	* @return the primary key of this account
	*/
	@Override
	public long getPrimaryKey() {
		return _account.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _account.getPrimaryKeyObj();
	}

	/**
	* Returns the protocol of this account.
	*
	* @return the protocol of this account
	*/
	@Override
	public String getProtocol() {
		return _account.getProtocol();
	}

	/**
	* Returns the save password of this account.
	*
	* @return the save password of this account
	*/
	@Override
	public boolean getSavePassword() {
		return _account.getSavePassword();
	}

	/**
	* Returns the sent folder ID of this account.
	*
	* @return the sent folder ID of this account
	*/
	@Override
	public long getSentFolderId() {
		return _account.getSentFolderId();
	}

	/**
	* Returns the signature of this account.
	*
	* @return the signature of this account
	*/
	@Override
	public String getSignature() {
		return _account.getSignature();
	}

	/**
	* Returns the trash folder ID of this account.
	*
	* @return the trash folder ID of this account
	*/
	@Override
	public long getTrashFolderId() {
		return _account.getTrashFolderId();
	}

	/**
	* Returns the user ID of this account.
	*
	* @return the user ID of this account
	*/
	@Override
	public long getUserId() {
		return _account.getUserId();
	}

	/**
	* Returns the user name of this account.
	*
	* @return the user name of this account
	*/
	@Override
	public String getUserName() {
		return _account.getUserName();
	}

	/**
	* Returns the user uuid of this account.
	*
	* @return the user uuid of this account
	*/
	@Override
	public String getUserUuid() {
		return _account.getUserUuid();
	}

	/**
	* Returns the use signature of this account.
	*
	* @return the use signature of this account
	*/
	@Override
	public boolean getUseSignature() {
		return _account.getUseSignature();
	}

	@Override
	public int hashCode() {
		return _account.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _account.isCachedModel();
	}

	/**
	* Returns <code>true</code> if this account is default sender.
	*
	* @return <code>true</code> if this account is default sender; <code>false</code> otherwise
	*/
	@Override
	public boolean isDefaultSender() {
		return _account.isDefaultSender();
	}

	@Override
	public boolean isEscapedModel() {
		return _account.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this account is incoming secure.
	*
	* @return <code>true</code> if this account is incoming secure; <code>false</code> otherwise
	*/
	@Override
	public boolean isIncomingSecure() {
		return _account.isIncomingSecure();
	}

	@Override
	public boolean isNew() {
		return _account.isNew();
	}

	/**
	* Returns <code>true</code> if this account is outgoing secure.
	*
	* @return <code>true</code> if this account is outgoing secure; <code>false</code> otherwise
	*/
	@Override
	public boolean isOutgoingSecure() {
		return _account.isOutgoingSecure();
	}

	/**
	* Returns <code>true</code> if this account is save password.
	*
	* @return <code>true</code> if this account is save password; <code>false</code> otherwise
	*/
	@Override
	public boolean isSavePassword() {
		return _account.isSavePassword();
	}

	/**
	* Returns <code>true</code> if this account is use signature.
	*
	* @return <code>true</code> if this account is use signature; <code>false</code> otherwise
	*/
	@Override
	public boolean isUseSignature() {
		return _account.isUseSignature();
	}

	@Override
	public void persist() {
		_account.persist();
	}

	/**
	* Sets the account ID of this account.
	*
	* @param accountId the account ID of this account
	*/
	@Override
	public void setAccountId(long accountId) {
		_account.setAccountId(accountId);
	}

	/**
	* Sets the address of this account.
	*
	* @param address the address of this account
	*/
	@Override
	public void setAddress(String address) {
		_account.setAddress(address);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_account.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this account.
	*
	* @param companyId the company ID of this account
	*/
	@Override
	public void setCompanyId(long companyId) {
		_account.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this account.
	*
	* @param createDate the create date of this account
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_account.setCreateDate(createDate);
	}

	/**
	* Sets whether this account is default sender.
	*
	* @param defaultSender the default sender of this account
	*/
	@Override
	public void setDefaultSender(boolean defaultSender) {
		_account.setDefaultSender(defaultSender);
	}

	/**
	* Sets the draft folder ID of this account.
	*
	* @param draftFolderId the draft folder ID of this account
	*/
	@Override
	public void setDraftFolderId(long draftFolderId) {
		_account.setDraftFolderId(draftFolderId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_account.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_account.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_account.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the folder prefix of this account.
	*
	* @param folderPrefix the folder prefix of this account
	*/
	@Override
	public void setFolderPrefix(String folderPrefix) {
		_account.setFolderPrefix(folderPrefix);
	}

	/**
	* Sets the inbox folder ID of this account.
	*
	* @param inboxFolderId the inbox folder ID of this account
	*/
	@Override
	public void setInboxFolderId(long inboxFolderId) {
		_account.setInboxFolderId(inboxFolderId);
	}

	/**
	* Sets the incoming host name of this account.
	*
	* @param incomingHostName the incoming host name of this account
	*/
	@Override
	public void setIncomingHostName(String incomingHostName) {
		_account.setIncomingHostName(incomingHostName);
	}

	/**
	* Sets the incoming port of this account.
	*
	* @param incomingPort the incoming port of this account
	*/
	@Override
	public void setIncomingPort(int incomingPort) {
		_account.setIncomingPort(incomingPort);
	}

	/**
	* Sets whether this account is incoming secure.
	*
	* @param incomingSecure the incoming secure of this account
	*/
	@Override
	public void setIncomingSecure(boolean incomingSecure) {
		_account.setIncomingSecure(incomingSecure);
	}

	/**
	* Sets the login of this account.
	*
	* @param login the login of this account
	*/
	@Override
	public void setLogin(String login) {
		_account.setLogin(login);
	}

	/**
	* Sets the modified date of this account.
	*
	* @param modifiedDate the modified date of this account
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_account.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_account.setNew(n);
	}

	/**
	* Sets the outgoing host name of this account.
	*
	* @param outgoingHostName the outgoing host name of this account
	*/
	@Override
	public void setOutgoingHostName(String outgoingHostName) {
		_account.setOutgoingHostName(outgoingHostName);
	}

	/**
	* Sets the outgoing port of this account.
	*
	* @param outgoingPort the outgoing port of this account
	*/
	@Override
	public void setOutgoingPort(int outgoingPort) {
		_account.setOutgoingPort(outgoingPort);
	}

	/**
	* Sets whether this account is outgoing secure.
	*
	* @param outgoingSecure the outgoing secure of this account
	*/
	@Override
	public void setOutgoingSecure(boolean outgoingSecure) {
		_account.setOutgoingSecure(outgoingSecure);
	}

	/**
	* Sets the password of this account.
	*
	* @param password the password of this account
	*/
	@Override
	public void setPassword(String password) {
		_account.setPassword(password);
	}

	@Override
	public void setPasswordDecrypted(String unencryptedPassword) {
		_account.setPasswordDecrypted(unencryptedPassword);
	}

	/**
	* Sets the personal name of this account.
	*
	* @param personalName the personal name of this account
	*/
	@Override
	public void setPersonalName(String personalName) {
		_account.setPersonalName(personalName);
	}

	/**
	* Sets the primary key of this account.
	*
	* @param primaryKey the primary key of this account
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_account.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_account.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the protocol of this account.
	*
	* @param protocol the protocol of this account
	*/
	@Override
	public void setProtocol(String protocol) {
		_account.setProtocol(protocol);
	}

	/**
	* Sets whether this account is save password.
	*
	* @param savePassword the save password of this account
	*/
	@Override
	public void setSavePassword(boolean savePassword) {
		_account.setSavePassword(savePassword);
	}

	/**
	* Sets the sent folder ID of this account.
	*
	* @param sentFolderId the sent folder ID of this account
	*/
	@Override
	public void setSentFolderId(long sentFolderId) {
		_account.setSentFolderId(sentFolderId);
	}

	/**
	* Sets the signature of this account.
	*
	* @param signature the signature of this account
	*/
	@Override
	public void setSignature(String signature) {
		_account.setSignature(signature);
	}

	/**
	* Sets the trash folder ID of this account.
	*
	* @param trashFolderId the trash folder ID of this account
	*/
	@Override
	public void setTrashFolderId(long trashFolderId) {
		_account.setTrashFolderId(trashFolderId);
	}

	/**
	* Sets the user ID of this account.
	*
	* @param userId the user ID of this account
	*/
	@Override
	public void setUserId(long userId) {
		_account.setUserId(userId);
	}

	/**
	* Sets the user name of this account.
	*
	* @param userName the user name of this account
	*/
	@Override
	public void setUserName(String userName) {
		_account.setUserName(userName);
	}

	/**
	* Sets the user uuid of this account.
	*
	* @param userUuid the user uuid of this account
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_account.setUserUuid(userUuid);
	}

	/**
	* Sets whether this account is use signature.
	*
	* @param useSignature the use signature of this account
	*/
	@Override
	public void setUseSignature(boolean useSignature) {
		_account.setUseSignature(useSignature);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Account> toCacheModel() {
		return _account.toCacheModel();
	}

	@Override
	public Account toEscapedModel() {
		return new AccountWrapper(_account.toEscapedModel());
	}

	@Override
	public String toString() {
		return _account.toString();
	}

	@Override
	public Account toUnescapedModel() {
		return new AccountWrapper(_account.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _account.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AccountWrapper)) {
			return false;
		}

		AccountWrapper accountWrapper = (AccountWrapper)obj;

		if (Objects.equals(_account, accountWrapper._account)) {
			return true;
		}

		return false;
	}

	@Override
	public Account getWrappedModel() {
		return _account;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _account.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _account.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_account.resetOriginalValues();
	}

	private final Account _account;
}