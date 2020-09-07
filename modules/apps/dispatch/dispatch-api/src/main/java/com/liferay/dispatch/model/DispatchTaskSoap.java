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

package com.liferay.dispatch.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.dispatch.service.http.DispatchTaskServiceSoap}.
 *
 * @author Matija Petanjek
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class DispatchTaskSoap implements Serializable {

	public static DispatchTaskSoap toSoapModel(DispatchTask model) {
		DispatchTaskSoap soapModel = new DispatchTaskSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setDispatchTaskId(model.getDispatchTaskId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setActive(model.isActive());
		soapModel.setName(model.getName());
		soapModel.setSystem(model.isSystem());
		soapModel.setType(model.getType());
		soapModel.setTypeSettings(model.getTypeSettings());

		return soapModel;
	}

	public static DispatchTaskSoap[] toSoapModels(DispatchTask[] models) {
		DispatchTaskSoap[] soapModels = new DispatchTaskSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DispatchTaskSoap[][] toSoapModels(DispatchTask[][] models) {
		DispatchTaskSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new DispatchTaskSoap[models.length][models[0].length];
		}
		else {
			soapModels = new DispatchTaskSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DispatchTaskSoap[] toSoapModels(List<DispatchTask> models) {
		List<DispatchTaskSoap> soapModels = new ArrayList<DispatchTaskSoap>(
			models.size());

		for (DispatchTask model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DispatchTaskSoap[soapModels.size()]);
	}

	public DispatchTaskSoap() {
	}

	public long getPrimaryKey() {
		return _dispatchTaskId;
	}

	public void setPrimaryKey(long pk) {
		setDispatchTaskId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getDispatchTaskId() {
		return _dispatchTaskId;
	}

	public void setDispatchTaskId(long dispatchTaskId) {
		_dispatchTaskId = dispatchTaskId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
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

	public boolean getActive() {
		return _active;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean getSystem() {
		return _system;
	}

	public boolean isSystem() {
		return _system;
	}

	public void setSystem(boolean system) {
		_system = system;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public String getTypeSettings() {
		return _typeSettings;
	}

	public void setTypeSettings(String typeSettings) {
		_typeSettings = typeSettings;
	}

	private long _mvccVersion;
	private long _dispatchTaskId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _active;
	private String _name;
	private boolean _system;
	private String _type;
	private String _typeSettings;

}