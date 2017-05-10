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

import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.GroupLocalization;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing GroupLocalization in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see GroupLocalization
 * @generated
 */
@ProviderType
public class GroupLocalizationCacheModel implements CacheModel<GroupLocalization>,
	Externalizable, MVCCModel {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof GroupLocalizationCacheModel)) {
			return false;
		}

		GroupLocalizationCacheModel groupLocalizationCacheModel = (GroupLocalizationCacheModel)obj;

		if ((groupLocalizationId == groupLocalizationCacheModel.groupLocalizationId) &&
				(mvccVersion == groupLocalizationCacheModel.mvccVersion)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, groupLocalizationId);

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
		sb.append(", groupLocalizationId=");
		sb.append(groupLocalizationId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", languageId=");
		sb.append(languageId);
		sb.append(", name=");
		sb.append(name);
		sb.append(", description=");
		sb.append(description);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public GroupLocalization toEntityModel() {
		GroupLocalizationImpl groupLocalizationImpl = new GroupLocalizationImpl();

		groupLocalizationImpl.setMvccVersion(mvccVersion);
		groupLocalizationImpl.setGroupLocalizationId(groupLocalizationId);
		groupLocalizationImpl.setCompanyId(companyId);
		groupLocalizationImpl.setGroupId(groupId);

		if (languageId == null) {
			groupLocalizationImpl.setLanguageId(StringPool.BLANK);
		}
		else {
			groupLocalizationImpl.setLanguageId(languageId);
		}

		if (name == null) {
			groupLocalizationImpl.setName(StringPool.BLANK);
		}
		else {
			groupLocalizationImpl.setName(name);
		}

		if (description == null) {
			groupLocalizationImpl.setDescription(StringPool.BLANK);
		}
		else {
			groupLocalizationImpl.setDescription(description);
		}

		groupLocalizationImpl.resetOriginalValues();

		return groupLocalizationImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		groupLocalizationId = objectInput.readLong();

		companyId = objectInput.readLong();

		groupId = objectInput.readLong();
		languageId = objectInput.readUTF();
		name = objectInput.readUTF();
		description = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(groupLocalizationId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(groupId);

		if (languageId == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(languageId);
		}

		if (name == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}
	}

	public long mvccVersion;
	public long groupLocalizationId;
	public long companyId;
	public long groupId;
	public String languageId;
	public String name;
	public String description;
}