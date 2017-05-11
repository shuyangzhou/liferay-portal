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

package com.liferay.portal.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.model.ABCTestEntity;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing ABCTestEntity in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see ABCTestEntity
 * @generated
 */
@ProviderType
public class ABCTestEntityCacheModel implements CacheModel<ABCTestEntity>,
	Externalizable, MVCCModel {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ABCTestEntityCacheModel)) {
			return false;
		}

		ABCTestEntityCacheModel abcTestEntityCacheModel = (ABCTestEntityCacheModel)obj;

		if (abcTestEntityId.equals(abcTestEntityCacheModel.abcTestEntityId) &&
				(mvccVersion == abcTestEntityCacheModel.mvccVersion)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, abcTestEntityId);

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
		StringBundler sb = new StringBundler(11);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", abcTestEntityId=");
		sb.append(abcTestEntityId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", defaultLanguageId=");
		sb.append(defaultLanguageId);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public ABCTestEntity toEntityModel() {
		ABCTestEntityImpl abcTestEntityImpl = new ABCTestEntityImpl();

		abcTestEntityImpl.setMvccVersion(mvccVersion);

		if (abcTestEntityId == null) {
			abcTestEntityImpl.setAbcTestEntityId(StringPool.BLANK);
		}
		else {
			abcTestEntityImpl.setAbcTestEntityId(abcTestEntityId);
		}

		abcTestEntityImpl.setCompanyId(companyId);
		abcTestEntityImpl.setGroupId(groupId);

		if (defaultLanguageId == null) {
			abcTestEntityImpl.setDefaultLanguageId(StringPool.BLANK);
		}
		else {
			abcTestEntityImpl.setDefaultLanguageId(defaultLanguageId);
		}

		abcTestEntityImpl.resetOriginalValues();

		return abcTestEntityImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();
		abcTestEntityId = objectInput.readUTF();

		companyId = objectInput.readLong();

		groupId = objectInput.readLong();
		defaultLanguageId = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(mvccVersion);

		if (abcTestEntityId == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(abcTestEntityId);
		}

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(groupId);

		if (defaultLanguageId == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(defaultLanguageId);
		}
	}

	public long mvccVersion;
	public String abcTestEntityId;
	public long companyId;
	public long groupId;
	public String defaultLanguageId;
}