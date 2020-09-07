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

package com.liferay.dispatch.model.impl;

import com.liferay.dispatch.model.DispatchTask;
import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing DispatchTask in entity cache.
 *
 * @author Matija Petanjek
 * @generated
 */
public class DispatchTaskCacheModel
	implements CacheModel<DispatchTask>, Externalizable, MVCCModel {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof DispatchTaskCacheModel)) {
			return false;
		}

		DispatchTaskCacheModel dispatchTaskCacheModel =
			(DispatchTaskCacheModel)object;

		if ((dispatchTaskId == dispatchTaskCacheModel.dispatchTaskId) &&
			(mvccVersion == dispatchTaskCacheModel.mvccVersion)) {

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = HashUtil.hash(0, dispatchTaskId);

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
		StringBundler sb = new StringBundler(25);

		sb.append("{mvccVersion=");
		sb.append(mvccVersion);
		sb.append(", dispatchTaskId=");
		sb.append(dispatchTaskId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", active=");
		sb.append(active);
		sb.append(", name=");
		sb.append(name);
		sb.append(", system=");
		sb.append(system);
		sb.append(", type=");
		sb.append(type);
		sb.append(", typeSettings=");
		sb.append(typeSettings);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DispatchTask toEntityModel() {
		DispatchTaskImpl dispatchTaskImpl = new DispatchTaskImpl();

		dispatchTaskImpl.setMvccVersion(mvccVersion);
		dispatchTaskImpl.setDispatchTaskId(dispatchTaskId);
		dispatchTaskImpl.setCompanyId(companyId);
		dispatchTaskImpl.setUserId(userId);

		if (userName == null) {
			dispatchTaskImpl.setUserName("");
		}
		else {
			dispatchTaskImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			dispatchTaskImpl.setCreateDate(null);
		}
		else {
			dispatchTaskImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			dispatchTaskImpl.setModifiedDate(null);
		}
		else {
			dispatchTaskImpl.setModifiedDate(new Date(modifiedDate));
		}

		dispatchTaskImpl.setActive(active);

		if (name == null) {
			dispatchTaskImpl.setName("");
		}
		else {
			dispatchTaskImpl.setName(name);
		}

		dispatchTaskImpl.setSystem(system);

		if (type == null) {
			dispatchTaskImpl.setType("");
		}
		else {
			dispatchTaskImpl.setType(type);
		}

		if (typeSettings == null) {
			dispatchTaskImpl.setTypeSettings("");
		}
		else {
			dispatchTaskImpl.setTypeSettings(typeSettings);
		}

		dispatchTaskImpl.resetOriginalValues();

		return dispatchTaskImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		mvccVersion = objectInput.readLong();

		dispatchTaskId = objectInput.readLong();

		companyId = objectInput.readLong();

		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();

		active = objectInput.readBoolean();
		name = objectInput.readUTF();

		system = objectInput.readBoolean();
		type = objectInput.readUTF();
		typeSettings = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(mvccVersion);

		objectOutput.writeLong(dispatchTaskId);

		objectOutput.writeLong(companyId);

		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		objectOutput.writeBoolean(active);

		if (name == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(name);
		}

		objectOutput.writeBoolean(system);

		if (type == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(type);
		}

		if (typeSettings == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(typeSettings);
		}
	}

	public long mvccVersion;
	public long dispatchTaskId;
	public long companyId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public boolean active;
	public String name;
	public boolean system;
	public String type;
	public String typeSettings;

}