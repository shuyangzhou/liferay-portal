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
public class ABCTestEntitySoap implements Serializable {
	public static ABCTestEntitySoap toSoapModel(ABCTestEntity model) {
		ABCTestEntitySoap soapModel = new ABCTestEntitySoap();

		soapModel.setMvccVersion(model.getMvccVersion());
		soapModel.setAbcTestEntityId(model.getAbcTestEntityId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setDefaultLanguageId(model.getDefaultLanguageId());

		return soapModel;
	}

	public static ABCTestEntitySoap[] toSoapModels(ABCTestEntity[] models) {
		ABCTestEntitySoap[] soapModels = new ABCTestEntitySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ABCTestEntitySoap[][] toSoapModels(ABCTestEntity[][] models) {
		ABCTestEntitySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ABCTestEntitySoap[models.length][models[0].length];
		}
		else {
			soapModels = new ABCTestEntitySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ABCTestEntitySoap[] toSoapModels(List<ABCTestEntity> models) {
		List<ABCTestEntitySoap> soapModels = new ArrayList<ABCTestEntitySoap>(models.size());

		for (ABCTestEntity model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ABCTestEntitySoap[soapModels.size()]);
	}

	public ABCTestEntitySoap() {
	}

	public String getPrimaryKey() {
		return _abcTestEntityId;
	}

	public void setPrimaryKey(String pk) {
		setAbcTestEntityId(pk);
	}

	public long getMvccVersion() {
		return _mvccVersion;
	}

	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	public String getAbcTestEntityId() {
		return _abcTestEntityId;
	}

	public void setAbcTestEntityId(String abcTestEntityId) {
		_abcTestEntityId = abcTestEntityId;
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

	public String getDefaultLanguageId() {
		return _defaultLanguageId;
	}

	public void setDefaultLanguageId(String defaultLanguageId) {
		_defaultLanguageId = defaultLanguageId;
	}

	private long _mvccVersion;
	private String _abcTestEntityId;
	private long _companyId;
	private long _groupId;
	private String _defaultLanguageId;
}