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

package com.liferay.service.builder.test.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;

import com.liferay.service.builder.test.model.VersionedEntry;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing VersionedEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see VersionedEntry
 * @generated
 */
@ProviderType
public class VersionedEntryCacheModel implements CacheModel<VersionedEntry>,
	Externalizable, MVCCModel {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof VersionedEntryCacheModel)) {
			return false;
		}

		VersionedEntryCacheModel versionedEntryCacheModel = (VersionedEntryCacheModel)obj;

		if ((versionedEntryId == versionedEntryCacheModel.versionedEntryId) &&
				(mvccVersion == versionedEntryCacheModel.mvccVersion)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, versionedEntryId);

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
		StringBundler sb = new StringBundler(9);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", versionedEntryId=");
		sb.append(versionedEntryId);
		sb.append(", content=");
		sb.append(content);
		sb.append(", headId=");
		sb.append(headId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public VersionedEntry toEntityModel() {
		VersionedEntryImpl versionedEntryImpl = new VersionedEntryImpl();

		versionedEntryImpl.setMvccVersion(mvccVersion);
		versionedEntryImpl.setVersionedEntryId(versionedEntryId);

		if (content == null) {
			versionedEntryImpl.setContent("");
		}
		else {
			versionedEntryImpl.setContent(content);
		}

		versionedEntryImpl.setHeadId(headId);

		versionedEntryImpl.resetOriginalValues();

		return versionedEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		versionedEntryId = objectInput.readLong();
		content = objectInput.readUTF();

		headId = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(versionedEntryId);

		if (content == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(content);
		}

		objectOutput.writeLong(headId);
	}

	public long mvccVersion;
	public long versionedEntryId;
	public String content;
	public long headId;
}