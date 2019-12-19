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

package com.liferay.portal.security.password.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link PasswordMeta}.
 * </p>
 *
 * @author arthurchan35
 * @see PasswordMeta
 * @generated
 */
public class PasswordMetaWrapper
	extends BaseModelWrapper<PasswordMeta>
	implements ModelWrapper<PasswordMeta>, PasswordMeta {

	public PasswordMetaWrapper(PasswordMeta passwordMeta) {
		super(passwordMeta);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("uuid", getUuid());
		attributes.put("metaId", getMetaId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("passwordEntryId", getPasswordEntryId());
		attributes.put("hashAlgorithmEntryId", getHashAlgorithmEntryId());
		attributes.put("salt", getSalt());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long mvccVersion = (Long)attributes.get("mvccVersion");

		if (mvccVersion != null) {
			setMvccVersion(mvccVersion);
		}

		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long metaId = (Long)attributes.get("metaId");

		if (metaId != null) {
			setMetaId(metaId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		Long passwordEntryId = (Long)attributes.get("passwordEntryId");

		if (passwordEntryId != null) {
			setPasswordEntryId(passwordEntryId);
		}

		Long hashAlgorithmEntryId = (Long)attributes.get(
			"hashAlgorithmEntryId");

		if (hashAlgorithmEntryId != null) {
			setHashAlgorithmEntryId(hashAlgorithmEntryId);
		}

		String salt = (String)attributes.get("salt");

		if (salt != null) {
			setSalt(salt);
		}
	}

	/**
	 * Returns the create date of this password meta.
	 *
	 * @return the create date of this password meta
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the hash algorithm entry ID of this password meta.
	 *
	 * @return the hash algorithm entry ID of this password meta
	 */
	@Override
	public long getHashAlgorithmEntryId() {
		return model.getHashAlgorithmEntryId();
	}

	/**
	 * Returns the meta ID of this password meta.
	 *
	 * @return the meta ID of this password meta
	 */
	@Override
	public long getMetaId() {
		return model.getMetaId();
	}

	/**
	 * Returns the modified date of this password meta.
	 *
	 * @return the modified date of this password meta
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this password meta.
	 *
	 * @return the mvcc version of this password meta
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the password entry ID of this password meta.
	 *
	 * @return the password entry ID of this password meta
	 */
	@Override
	public long getPasswordEntryId() {
		return model.getPasswordEntryId();
	}

	/**
	 * Returns the primary key of this password meta.
	 *
	 * @return the primary key of this password meta
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the salt of this password meta.
	 *
	 * @return the salt of this password meta
	 */
	@Override
	public String getSalt() {
		return model.getSalt();
	}

	/**
	 * Returns the uuid of this password meta.
	 *
	 * @return the uuid of this password meta
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a password meta model instance should use the <code>PasswordMeta</code> interface instead.
	 */
	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the create date of this password meta.
	 *
	 * @param createDate the create date of this password meta
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the hash algorithm entry ID of this password meta.
	 *
	 * @param hashAlgorithmEntryId the hash algorithm entry ID of this password meta
	 */
	@Override
	public void setHashAlgorithmEntryId(long hashAlgorithmEntryId) {
		model.setHashAlgorithmEntryId(hashAlgorithmEntryId);
	}

	/**
	 * Sets the meta ID of this password meta.
	 *
	 * @param metaId the meta ID of this password meta
	 */
	@Override
	public void setMetaId(long metaId) {
		model.setMetaId(metaId);
	}

	/**
	 * Sets the modified date of this password meta.
	 *
	 * @param modifiedDate the modified date of this password meta
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this password meta.
	 *
	 * @param mvccVersion the mvcc version of this password meta
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the password entry ID of this password meta.
	 *
	 * @param passwordEntryId the password entry ID of this password meta
	 */
	@Override
	public void setPasswordEntryId(long passwordEntryId) {
		model.setPasswordEntryId(passwordEntryId);
	}

	/**
	 * Sets the primary key of this password meta.
	 *
	 * @param primaryKey the primary key of this password meta
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the salt of this password meta.
	 *
	 * @param salt the salt of this password meta
	 */
	@Override
	public void setSalt(String salt) {
		model.setSalt(salt);
	}

	/**
	 * Sets the uuid of this password meta.
	 *
	 * @param uuid the uuid of this password meta
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	protected PasswordMetaWrapper wrap(PasswordMeta passwordMeta) {
		return new PasswordMetaWrapper(passwordMeta);
	}

}