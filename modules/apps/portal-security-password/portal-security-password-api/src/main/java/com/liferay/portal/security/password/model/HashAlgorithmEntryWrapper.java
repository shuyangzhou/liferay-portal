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
 * This class is a wrapper for {@link HashAlgorithmEntry}.
 * </p>
 *
 * @author arthurchan35
 * @see HashAlgorithmEntry
 * @generated
 */
public class HashAlgorithmEntryWrapper
	extends BaseModelWrapper<HashAlgorithmEntry>
	implements HashAlgorithmEntry, ModelWrapper<HashAlgorithmEntry> {

	public HashAlgorithmEntryWrapper(HashAlgorithmEntry hashAlgorithmEntry) {
		super(hashAlgorithmEntry);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("mvccVersion", getMvccVersion());
		attributes.put("uuid", getUuid());
		attributes.put("entryId", getEntryId());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("name", getName());
		attributes.put("meta", getMeta());

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

		Long entryId = (Long)attributes.get("entryId");

		if (entryId != null) {
			setEntryId(entryId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String meta = (String)attributes.get("meta");

		if (meta != null) {
			setMeta(meta);
		}
	}

	/**
	 * Returns the create date of this hash algorithm entry.
	 *
	 * @return the create date of this hash algorithm entry
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the entry ID of this hash algorithm entry.
	 *
	 * @return the entry ID of this hash algorithm entry
	 */
	@Override
	public long getEntryId() {
		return model.getEntryId();
	}

	/**
	 * Returns the meta of this hash algorithm entry.
	 *
	 * @return the meta of this hash algorithm entry
	 */
	@Override
	public String getMeta() {
		return model.getMeta();
	}

	/**
	 * Returns the modified date of this hash algorithm entry.
	 *
	 * @return the modified date of this hash algorithm entry
	 */
	@Override
	public Date getModifiedDate() {
		return model.getModifiedDate();
	}

	/**
	 * Returns the mvcc version of this hash algorithm entry.
	 *
	 * @return the mvcc version of this hash algorithm entry
	 */
	@Override
	public long getMvccVersion() {
		return model.getMvccVersion();
	}

	/**
	 * Returns the name of this hash algorithm entry.
	 *
	 * @return the name of this hash algorithm entry
	 */
	@Override
	public String getName() {
		return model.getName();
	}

	/**
	 * Returns the primary key of this hash algorithm entry.
	 *
	 * @return the primary key of this hash algorithm entry
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the uuid of this hash algorithm entry.
	 *
	 * @return the uuid of this hash algorithm entry
	 */
	@Override
	public String getUuid() {
		return model.getUuid();
	}

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a hash algorithm entry model instance should use the <code>HashAlgorithmEntry</code> interface instead.
	 */
	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the create date of this hash algorithm entry.
	 *
	 * @param createDate the create date of this hash algorithm entry
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the entry ID of this hash algorithm entry.
	 *
	 * @param entryId the entry ID of this hash algorithm entry
	 */
	@Override
	public void setEntryId(long entryId) {
		model.setEntryId(entryId);
	}

	/**
	 * Sets the meta of this hash algorithm entry.
	 *
	 * @param meta the meta of this hash algorithm entry
	 */
	@Override
	public void setMeta(String meta) {
		model.setMeta(meta);
	}

	/**
	 * Sets the modified date of this hash algorithm entry.
	 *
	 * @param modifiedDate the modified date of this hash algorithm entry
	 */
	@Override
	public void setModifiedDate(Date modifiedDate) {
		model.setModifiedDate(modifiedDate);
	}

	/**
	 * Sets the mvcc version of this hash algorithm entry.
	 *
	 * @param mvccVersion the mvcc version of this hash algorithm entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion) {
		model.setMvccVersion(mvccVersion);
	}

	/**
	 * Sets the name of this hash algorithm entry.
	 *
	 * @param name the name of this hash algorithm entry
	 */
	@Override
	public void setName(String name) {
		model.setName(name);
	}

	/**
	 * Sets the primary key of this hash algorithm entry.
	 *
	 * @param primaryKey the primary key of this hash algorithm entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the uuid of this hash algorithm entry.
	 *
	 * @param uuid the uuid of this hash algorithm entry
	 */
	@Override
	public void setUuid(String uuid) {
		model.setUuid(uuid);
	}

	@Override
	protected HashAlgorithmEntryWrapper wrap(
		HashAlgorithmEntry hashAlgorithmEntry) {

		return new HashAlgorithmEntryWrapper(hashAlgorithmEntry);
	}

}