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

package com.liferay.portal.security.password.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.security.password.model.HashAlgorithmEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing HashAlgorithmEntry in entity cache.
 *
 * @author arthurchan35
 * @generated
 */
public class HashAlgorithmEntryCacheModel
	implements CacheModel<HashAlgorithmEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof HashAlgorithmEntryCacheModel)) {
			return false;
		}

		HashAlgorithmEntryCacheModel hashAlgorithmEntryCacheModel =
			(HashAlgorithmEntryCacheModel)obj;

		if ((entryId == hashAlgorithmEntryCacheModel.entryId) &&
			(mvccVersion == hashAlgorithmEntryCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, entryId);

		return HashUtil.hash(hashCode, mvccVersion);
	}

	@Override
	public long getMvccVersion() {
		return mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		this.mvccVersion = mvccVersion;
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(15);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", entryId=");
		sb.append(entryId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", name=");
		sb.append(name);
		sb.append(", meta=");
		sb.append(meta);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public HashAlgorithmEntry toEntityModel() {
		HashAlgorithmEntryImpl hashAlgorithmEntryImpl =
			new HashAlgorithmEntryImpl();

		hashAlgorithmEntryImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			hashAlgorithmEntryImpl.setUuid("");
		}
		else {
			hashAlgorithmEntryImpl.setUuid(uuid);
		}

		hashAlgorithmEntryImpl.setEntryId(entryId);

		if (createDate == Long.MIN_VALUE) {
			hashAlgorithmEntryImpl.setCreateDate(null);
		}
		else {
			hashAlgorithmEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			hashAlgorithmEntryImpl.setModifiedDate(null);
		}
		else {
			hashAlgorithmEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (name == null) {
			hashAlgorithmEntryImpl.setName("");
		}
		else {
			hashAlgorithmEntryImpl.setName(name);
		}

		if (meta == null) {
			hashAlgorithmEntryImpl.setMeta("");
		}
		else {
			hashAlgorithmEntryImpl.setMeta(meta);
		}

		hashAlgorithmEntryImpl.resetOriginalValues();

		return hashAlgorithmEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		entryId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		name = objectInput.readUTF();
		meta = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (uuid == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(entryId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (meta == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(meta);
		}
	}

	public long mvccVersion;
	public String uuid;
	public long entryId;
	public long createDate;
	public long modifiedDate;
	public String name;
	public String meta;

}