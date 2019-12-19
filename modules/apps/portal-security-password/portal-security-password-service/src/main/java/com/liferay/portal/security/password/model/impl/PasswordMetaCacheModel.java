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
import com.liferay.portal.security.password.model.PasswordMeta;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing PasswordMeta in entity cache.
 *
 * @author arthurchan35
 * @generated
 */
public class PasswordMetaCacheModel
	implements CacheModel<PasswordMeta>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PasswordMetaCacheModel)) {
			return false;
		}

		PasswordMetaCacheModel passwordMetaCacheModel =
			(PasswordMetaCacheModel)obj;

		if ((metaId == passwordMetaCacheModel.metaId) &&
			(mvccVersion == passwordMetaCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, metaId);

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
		StringBundler sb = new StringBundler(17);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", uuid=");
		sb.append(uuid);
		sb.append(", metaId=");
		sb.append(metaId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", passwordEntryId=");
		sb.append(passwordEntryId);
		sb.append(", hashAlgorithmEntryId=");
		sb.append(hashAlgorithmEntryId);
		sb.append(", salt=");
		sb.append(salt);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public PasswordMeta toEntityModel() {
		PasswordMetaImpl passwordMetaImpl = new PasswordMetaImpl();

		passwordMetaImpl.setMvccVersion(mvccVersion);

		if (uuid == null) {
			passwordMetaImpl.setUuid("");
		}
		else {
			passwordMetaImpl.setUuid(uuid);
		}

		passwordMetaImpl.setMetaId(metaId);

		if (createDate == Long.MIN_VALUE) {
			passwordMetaImpl.setCreateDate(null);
		}
		else {
			passwordMetaImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			passwordMetaImpl.setModifiedDate(null);
		}
		else {
			passwordMetaImpl.setModifiedDate(new Date(modifiedDate));
		}

		passwordMetaImpl.setPasswordEntryId(passwordEntryId);
		passwordMetaImpl.setHashAlgorithmEntryId(hashAlgorithmEntryId);

		if (salt == null) {
			passwordMetaImpl.setSalt("");
		}
		else {
			passwordMetaImpl.setSalt(salt);
		}

		passwordMetaImpl.resetOriginalValues();

		return passwordMetaImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		uuid = objectInput.readUTF();

		metaId = objectInput.readLong();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		passwordEntryId = objectInput.readLong();

		hashAlgorithmEntryId = objectInput.readLong();
		salt = objectInput.readUTF();
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

		objectOutput.writeLong(metaId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeLong(passwordEntryId);

		objectOutput.writeLong(hashAlgorithmEntryId);

		if (salt == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(salt);
		}
	}

	public long mvccVersion;
	public String uuid;
	public long metaId;
	public long createDate;
	public long modifiedDate;
	public long passwordEntryId;
	public long hashAlgorithmEntryId;
	public String salt;

}