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

package com.liferay.portal.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

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

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _account.getExpandoBridge();
	}

	/**
	* Returns the industry of this account.
	*
	* @return the industry of this account
	*/
	@Override
	public String getIndustry() {
		return _account.getIndustry();
	}

	/**
	* Returns the legal ID of this account.
	*
	* @return the legal ID of this account
	*/
	@Override
	public String getLegalId() {
		return _account.getLegalId();
	}

	/**
	* Returns the legal name of this account.
	*
	* @return the legal name of this account
	*/
	@Override
	public String getLegalName() {
		return _account.getLegalName();
	}

	/**
	* Returns the legal type of this account.
	*
	* @return the legal type of this account
	*/
	@Override
	public String getLegalType() {
		return _account.getLegalType();
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
	* Returns the mvcc version of this account.
	*
	* @return the mvcc version of this account
	*/
	@Override
	public long getMvccVersion() {
		return _account.getMvccVersion();
	}

	/**
	* Returns the name of this account.
	*
	* @return the name of this account
	*/
	@Override
	public String getName() {
		return _account.getName();
	}

	/**
	* Returns the parent account ID of this account.
	*
	* @return the parent account ID of this account
	*/
	@Override
	public long getParentAccountId() {
		return _account.getParentAccountId();
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
	* Returns the sic code of this account.
	*
	* @return the sic code of this account
	*/
	@Override
	public String getSicCode() {
		return _account.getSicCode();
	}

	/**
	* Returns the size of this account.
	*
	* @return the size of this account
	*/
	@Override
	public String getSize() {
		return _account.getSize();
	}

	/**
	* Returns the ticker symbol of this account.
	*
	* @return the ticker symbol of this account
	*/
	@Override
	public String getTickerSymbol() {
		return _account.getTickerSymbol();
	}

	/**
	* Returns the type of this account.
	*
	* @return the type of this account
	*/
	@Override
	public String getType() {
		return _account.getType();
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

	@Override
	public int hashCode() {
		return _account.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _account.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _account.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _account.isNew();
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

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
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
	* Sets the industry of this account.
	*
	* @param industry the industry of this account
	*/
	@Override
	public void setIndustry(String industry) {
		_account.setIndustry(industry);
	}

	/**
	* Sets the legal ID of this account.
	*
	* @param legalId the legal ID of this account
	*/
	@Override
	public void setLegalId(String legalId) {
		_account.setLegalId(legalId);
	}

	/**
	* Sets the legal name of this account.
	*
	* @param legalName the legal name of this account
	*/
	@Override
	public void setLegalName(String legalName) {
		_account.setLegalName(legalName);
	}

	/**
	* Sets the legal type of this account.
	*
	* @param legalType the legal type of this account
	*/
	@Override
	public void setLegalType(String legalType) {
		_account.setLegalType(legalType);
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

	/**
	* Sets the mvcc version of this account.
	*
	* @param mvccVersion the mvcc version of this account
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_account.setMvccVersion(mvccVersion);
	}

	/**
	* Sets the name of this account.
	*
	* @param name the name of this account
	*/
	@Override
	public void setName(String name) {
		_account.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_account.setNew(n);
	}

	/**
	* Sets the parent account ID of this account.
	*
	* @param parentAccountId the parent account ID of this account
	*/
	@Override
	public void setParentAccountId(long parentAccountId) {
		_account.setParentAccountId(parentAccountId);
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
	* Sets the sic code of this account.
	*
	* @param sicCode the sic code of this account
	*/
	@Override
	public void setSicCode(String sicCode) {
		_account.setSicCode(sicCode);
	}

	/**
	* Sets the size of this account.
	*
	* @param size the size of this account
	*/
	@Override
	public void setSize(String size) {
		_account.setSize(size);
	}

	/**
	* Sets the ticker symbol of this account.
	*
	* @param tickerSymbol the ticker symbol of this account
	*/
	@Override
	public void setTickerSymbol(String tickerSymbol) {
		_account.setTickerSymbol(tickerSymbol);
	}

	/**
	* Sets the type of this account.
	*
	* @param type the type of this account
	*/
	@Override
	public void setType(String type) {
		_account.setType(type);
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

	@Override
	public CacheModel<Account> toCacheModel() {
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