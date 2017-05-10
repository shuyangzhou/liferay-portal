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

package com.liferay.portal.kernel.model;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class GroupLocalizationSoap implements Serializable {
	public static GroupLocalizationSoap toSoapModel(GroupLocalization model) {
		GroupLocalizationSoap soapModel = new GroupLocalizationSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setGroupLocalizationId(model.getGroupLocalizationId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setLanguageId(model.getLanguageId());
		soapModel.setName(model.getName());
		soapModel.setDescription(model.getDescription());

		return soapModel;
	}

	public static GroupLocalizationSoap[] toSoapModels(
		GroupLocalization[] models) {
		GroupLocalizationSoap[] soapModels = new GroupLocalizationSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static GroupLocalizationSoap[][] toSoapModels(
		GroupLocalization[][] models) {
		GroupLocalizationSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new GroupLocalizationSoap[models.length][models[0].length];
		}
		else {
			soapModels = new GroupLocalizationSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static GroupLocalizationSoap[] toSoapModels(
		List<GroupLocalization> models) {
		List<GroupLocalizationSoap> soapModels = new ArrayList<GroupLocalizationSoap>(models.size());

		for (GroupLocalization model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new GroupLocalizationSoap[soapModels.size()]);
	}

	public GroupLocalizationSoap() {
	}

	public long getPrimaryKey() {
		return _groupLocalizationId;
	}

	public void setPrimaryKey(long pk) {
		setGroupLocalizationId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getGroupLocalizationId() {
		return _groupLocalizationId;
	}

	public void setGroupLocalizationId(long groupLocalizationId) {
		_groupLocalizationId = groupLocalizationId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public String getLanguageId() {
		return _languageId;
	}

	public void setLanguageId(String languageId) {
		_languageId = languageId;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	private long _mvccVersion;
	private long _groupLocalizationId;
	private long _companyId;
	private long _groupId;
	private String _languageId;
	private String _name;
	private String _description;
}