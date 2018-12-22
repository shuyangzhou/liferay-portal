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

package com.liferay.portal.lock.model;

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
 * This class is a wrapper for {@link Lock}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Lock
 * @generated
 */
@ProviderType
public class LockWrapper implements Lock, ModelWrapper<Lock> {
	public LockWrapper(Lock lock) {
		_lock = lock;
	}

	@Override
	public Class<?> getModelClass() {
		return Lock.class;
	}

	@Override
	public String getModelClassName() {
		return Lock.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Lock, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Lock, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Lock, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Lock, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Lock, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Lock, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Lock, Object>> getAttributeGetters() {
		return _lock.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Lock, Object>> getAttributeSetters() {
		return _lock.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new LockWrapper((Lock)_lock.clone());
	}

	@Override
	public int compareTo(Lock lock) {
		return _lock.compareTo(lock);
	}

	/**
	* Returns the class name of this lock.
	*
	* @return the class name of this lock
	*/
	@Override
	public String getClassName() {
		return _lock.getClassName();
	}

	/**
	* Returns the company ID of this lock.
	*
	* @return the company ID of this lock
	*/
	@Override
	public long getCompanyId() {
		return _lock.getCompanyId();
	}

	/**
	* Returns the create date of this lock.
	*
	* @return the create date of this lock
	*/
	@Override
	public Date getCreateDate() {
		return _lock.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _lock.getExpandoBridge();
	}

	/**
	* Returns the expiration date of this lock.
	*
	* @return the expiration date of this lock
	*/
	@Override
	public Date getExpirationDate() {
		return _lock.getExpirationDate();
	}

	@Override
	public long getExpirationTime() {
		return _lock.getExpirationTime();
	}

	/**
	* Returns the inheritable of this lock.
	*
	* @return the inheritable of this lock
	*/
	@Override
	public boolean getInheritable() {
		return _lock.getInheritable();
	}

	/**
	* Returns the key of this lock.
	*
	* @return the key of this lock
	*/
	@Override
	public String getKey() {
		return _lock.getKey();
	}

	/**
	* Returns the lock ID of this lock.
	*
	* @return the lock ID of this lock
	*/
	@Override
	public long getLockId() {
		return _lock.getLockId();
	}

	/**
	* Returns the mvcc version of this lock.
	*
	* @return the mvcc version of this lock
	*/
	@Override
	public long getMvccVersion() {
		return _lock.getMvccVersion();
	}

	/**
	* Returns the owner of this lock.
	*
	* @return the owner of this lock
	*/
	@Override
	public String getOwner() {
		return _lock.getOwner();
	}

	/**
	* Returns the primary key of this lock.
	*
	* @return the primary key of this lock
	*/
	@Override
	public long getPrimaryKey() {
		return _lock.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _lock.getPrimaryKeyObj();
	}

	/**
	* Returns the user ID of this lock.
	*
	* @return the user ID of this lock
	*/
	@Override
	public long getUserId() {
		return _lock.getUserId();
	}

	/**
	* Returns the user name of this lock.
	*
	* @return the user name of this lock
	*/
	@Override
	public String getUserName() {
		return _lock.getUserName();
	}

	/**
	* Returns the user uuid of this lock.
	*
	* @return the user uuid of this lock
	*/
	@Override
	public String getUserUuid() {
		return _lock.getUserUuid();
	}

	/**
	* Returns the uuid of this lock.
	*
	* @return the uuid of this lock
	*/
	@Override
	public String getUuid() {
		return _lock.getUuid();
	}

	@Override
	public int hashCode() {
		return _lock.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _lock.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _lock.isEscapedModel();
	}

	@Override
	public boolean isExpired() {
		return _lock.isExpired();
	}

	/**
	* Returns <code>true</code> if this lock is inheritable.
	*
	* @return <code>true</code> if this lock is inheritable; <code>false</code> otherwise
	*/
	@Override
	public boolean isInheritable() {
		return _lock.isInheritable();
	}

	@Override
	public boolean isNeverExpires() {
		return _lock.isNeverExpires();
	}

	@Override
	public boolean isNew() {
		return _lock.isNew();
	}

	@Override
	public void persist() {
		_lock.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_lock.setCachedModel(cachedModel);
	}

	/**
	* Sets the class name of this lock.
	*
	* @param className the class name of this lock
	*/
	@Override
	public void setClassName(String className) {
		_lock.setClassName(className);
	}

	/**
	* Sets the company ID of this lock.
	*
	* @param companyId the company ID of this lock
	*/
	@Override
	public void setCompanyId(long companyId) {
		_lock.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this lock.
	*
	* @param createDate the create date of this lock
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_lock.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_lock.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_lock.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_lock.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the expiration date of this lock.
	*
	* @param expirationDate the expiration date of this lock
	*/
	@Override
	public void setExpirationDate(Date expirationDate) {
		_lock.setExpirationDate(expirationDate);
	}

	/**
	* Sets whether this lock is inheritable.
	*
	* @param inheritable the inheritable of this lock
	*/
	@Override
	public void setInheritable(boolean inheritable) {
		_lock.setInheritable(inheritable);
	}

	/**
	* Sets the key of this lock.
	*
	* @param key the key of this lock
	*/
	@Override
	public void setKey(String key) {
		_lock.setKey(key);
	}

	/**
	* Sets the lock ID of this lock.
	*
	* @param lockId the lock ID of this lock
	*/
	@Override
	public void setLockId(long lockId) {
		_lock.setLockId(lockId);
	}

	/**
	* Sets the mvcc version of this lock.
	*
	* @param mvccVersion the mvcc version of this lock
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_lock.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_lock.setNew(n);
	}

	/**
	* Sets the owner of this lock.
	*
	* @param owner the owner of this lock
	*/
	@Override
	public void setOwner(String owner) {
		_lock.setOwner(owner);
	}

	/**
	* Sets the primary key of this lock.
	*
	* @param primaryKey the primary key of this lock
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_lock.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_lock.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this lock.
	*
	* @param userId the user ID of this lock
	*/
	@Override
	public void setUserId(long userId) {
		_lock.setUserId(userId);
	}

	/**
	* Sets the user name of this lock.
	*
	* @param userName the user name of this lock
	*/
	@Override
	public void setUserName(String userName) {
		_lock.setUserName(userName);
	}

	/**
	* Sets the user uuid of this lock.
	*
	* @param userUuid the user uuid of this lock
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_lock.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this lock.
	*
	* @param uuid the uuid of this lock
	*/
	@Override
	public void setUuid(String uuid) {
		_lock.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<Lock> toCacheModel() {
		return _lock.toCacheModel();
	}

	@Override
	public Lock toEscapedModel() {
		return new LockWrapper(_lock.toEscapedModel());
	}

	@Override
	public String toString() {
		return _lock.toString();
	}

	@Override
	public Lock toUnescapedModel() {
		return new LockWrapper(_lock.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _lock.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof LockWrapper)) {
			return false;
		}

		LockWrapper lockWrapper = (LockWrapper)obj;

		if (Objects.equals(_lock, lockWrapper._lock)) {
			return true;
		}

		return false;
	}

	@Override
	public Lock getWrappedModel() {
		return _lock;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _lock.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _lock.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_lock.resetOriginalValues();
	}

	private final Lock _lock;
}