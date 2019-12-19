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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.portal.security.password.service.http.HashAlgorithmEntryServiceSoap}.
 *
 * @author arthurchan35
 * @generated
 */
public class HashAlgorithmEntrySoap implements Serializable {

	public static HashAlgorithmEntrySoap toSoapModel(HashAlgorithmEntry model) {
		HashAlgorithmEntrySoap soapModel = new HashAlgorithmEntrySoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setUuid(model.getUuid());
		soapModel.setEntryId(model.getEntryId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setName(model.getName());
		soapModel.setMeta(model.getMeta());

		return soapModel;
	}

	public static HashAlgorithmEntrySoap[] toSoapModels(
		HashAlgorithmEntry[] models) {

		HashAlgorithmEntrySoap[] soapModels =
			new HashAlgorithmEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static HashAlgorithmEntrySoap[][] toSoapModels(
		HashAlgorithmEntry[][] models) {

		HashAlgorithmEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new HashAlgorithmEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new HashAlgorithmEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static HashAlgorithmEntrySoap[] toSoapModels(
		List<HashAlgorithmEntry> models) {

		List<HashAlgorithmEntrySoap> soapModels =
			new ArrayList<HashAlgorithmEntrySoap>(models.size());

		for (HashAlgorithmEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(
			new HashAlgorithmEntrySoap[soapModels.size()]);
	}

	public HashAlgorithmEntrySoap() {
	}

	public long getPrimaryKey() {
		return _entryId;
	}

	public void setPrimaryKey(long pk) {
		setEntryId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getEntryId() {
		return _entryId;
	}

	public void setEntryId(long entryId) {
		_entryId = entryId;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getMeta() {
		return _meta;
	}

	public void setMeta(String meta) {
		_meta = meta;
	}

	private long _mvccVersion;
	private String _uuid;
	private long _entryId;
	private Date _createDate;
	private Date _modifiedDate;
	private String _name;
	private String _meta;

}