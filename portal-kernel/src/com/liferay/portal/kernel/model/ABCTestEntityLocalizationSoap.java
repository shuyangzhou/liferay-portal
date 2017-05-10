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
public class ABCTestEntityLocalizationSoap implements Serializable {
	public static ABCTestEntityLocalizationSoap toSoapModel(
		ABCTestEntityLocalization model) {
		ABCTestEntityLocalizationSoap soapModel = new ABCTestEntityLocalizationSoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setAbcTestEntityLocalizationId(model.getAbcTestEntityLocalizationId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setAbcTestEntityId(model.getAbcTestEntityId());
		soapModel.setLanguageId(model.getLanguageId());
		soapModel.setName(model.getName());
		soapModel.setDescription(model.getDescription());
		soapModel.setGroupId(model.getGroupId());

		return soapModel;
	}

	public static ABCTestEntityLocalizationSoap[] toSoapModels(
		ABCTestEntityLocalization[] models) {
		ABCTestEntityLocalizationSoap[] soapModels = new ABCTestEntityLocalizationSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ABCTestEntityLocalizationSoap[][] toSoapModels(
		ABCTestEntityLocalization[][] models) {
		ABCTestEntityLocalizationSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ABCTestEntityLocalizationSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ABCTestEntityLocalizationSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ABCTestEntityLocalizationSoap[] toSoapModels(
		List<ABCTestEntityLocalization> models) {
		List<ABCTestEntityLocalizationSoap> soapModels = new ArrayList<ABCTestEntityLocalizationSoap>(models.size());

		for (ABCTestEntityLocalization model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ABCTestEntityLocalizationSoap[soapModels.size()]);
	}

	public ABCTestEntityLocalizationSoap() {
	}

	public long getPrimaryKey() {
		return _abcTestEntityLocalizationId;
	}

	public void setPrimaryKey(long pk) {
		setAbcTestEntityLocalizationId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public long getAbcTestEntityLocalizationId() {
		return _abcTestEntityLocalizationId;
	}

	public void setAbcTestEntityLocalizationId(long abcTestEntityLocalizationId) {
		_abcTestEntityLocalizationId = abcTestEntityLocalizationId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public String getAbcTestEntityId() {
		return _abcTestEntityId;
	}

	public void setAbcTestEntityId(String abcTestEntityId) {
		_abcTestEntityId = abcTestEntityId;
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

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	private long _mvccVersion;
	private long _abcTestEntityLocalizationId;
	private long _companyId;
	private String _abcTestEntityId;
	private String _languageId;
	private String _name;
	private String _description;
	private long _groupId;
}