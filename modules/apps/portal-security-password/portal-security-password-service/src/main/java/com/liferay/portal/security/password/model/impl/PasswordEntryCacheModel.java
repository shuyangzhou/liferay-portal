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
import com.liferay.portal.security.password.model.PasswordEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing PasswordEntry in entity cache.
 *
 * @author arthurchan35
 * @generated
 */
public class PasswordEntryCacheModel
	implements CacheModel<PasswordEntry>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PasswordEntryCacheModel)) {
			return false;
		}

		PasswordEntryCacheModel passwordEntryCacheModel =
			(PasswordEntryCacheModel)obj;

		if ((entryId == passwordEntryCacheModel.entryId) &&
			(mvccVersion == passwordEntryCacheModel.mvccVersion)) {

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
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", hash=");
		sb.append(hash);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public PasswordEntry toEntityModel() {
		PasswordEntryImpl passwordEntryImpl = new PasswordEntryImpl();

		passwordEntryImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			passwordEntryImpl.setUuid("");
		}
		else {
			passwordEntryImpl.setUuid(uuid);
		}

		passwordEntryImpl.setEntryId(entryId);
		passwordEntryImpl.setUserId(userId);

		if (createDate == Long.MIN_VALUE) {
			passwordEntryImpl.setCreateDate(null);
		}
		else {
			passwordEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			passwordEntryImpl.setModifiedDate(null);
		}
		else {
			passwordEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (hash == null) {
			passwordEntryImpl.setHash("");
		}
		else {
			passwordEntryImpl.setHash(hash);
		}

		passwordEntryImpl.resetOriginalValues();

		return passwordEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		entryId = objectInput.readLong();

		userId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		hash = objectInput.readUTF();
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

		objectOutput.writeLong(userId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (hash == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(hash);
		}
	}

	public long mvccVersion;
	public String uuid;
	public long entryId;
	public long userId;
	public long createDate;
	public long modifiedDate;
	public String hash;

}