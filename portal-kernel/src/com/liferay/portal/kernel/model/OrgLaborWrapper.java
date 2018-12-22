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

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link OrgLabor}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see OrgLabor
 * @generated
 */
@ProviderType
public class OrgLaborWrapper implements OrgLabor, ModelWrapper<OrgLabor> {
	public OrgLaborWrapper(OrgLabor orgLabor) {
		_orgLabor = orgLabor;
	}

	@Override
	public Class<?> getModelClass() {
		return OrgLabor.class;
	}

	@Override
	public String getModelClassName() {
		return OrgLabor.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<OrgLabor, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<OrgLabor, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<OrgLabor, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<OrgLabor, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<OrgLabor, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<OrgLabor, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<OrgLabor, Object>> getAttributeGetters() {
		return _orgLabor.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<OrgLabor, Object>> getAttributeSetters() {
		return _orgLabor.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new OrgLaborWrapper((OrgLabor)_orgLabor.clone());
	}

	@Override
	public int compareTo(OrgLabor orgLabor) {
		return _orgLabor.compareTo(orgLabor);
	}

	/**
	* Returns the company ID of this org labor.
	*
	* @return the company ID of this org labor
	*/
	@Override
	public long getCompanyId() {
		return _orgLabor.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _orgLabor.getExpandoBridge();
	}

	/**
	* Returns the fri close of this org labor.
	*
	* @return the fri close of this org labor
	*/
	@Override
	public int getFriClose() {
		return _orgLabor.getFriClose();
	}

	/**
	* Returns the fri open of this org labor.
	*
	* @return the fri open of this org labor
	*/
	@Override
	public int getFriOpen() {
		return _orgLabor.getFriOpen();
	}

	/**
	* Returns the mon close of this org labor.
	*
	* @return the mon close of this org labor
	*/
	@Override
	public int getMonClose() {
		return _orgLabor.getMonClose();
	}

	/**
	* Returns the mon open of this org labor.
	*
	* @return the mon open of this org labor
	*/
	@Override
	public int getMonOpen() {
		return _orgLabor.getMonOpen();
	}

	/**
	* Returns the mvcc version of this org labor.
	*
	* @return the mvcc version of this org labor
	*/
	@Override
	public long getMvccVersion() {
		return _orgLabor.getMvccVersion();
	}

	/**
	* Returns the organization ID of this org labor.
	*
	* @return the organization ID of this org labor
	*/
	@Override
	public long getOrganizationId() {
		return _orgLabor.getOrganizationId();
	}

	/**
	* Returns the org labor ID of this org labor.
	*
	* @return the org labor ID of this org labor
	*/
	@Override
	public long getOrgLaborId() {
		return _orgLabor.getOrgLaborId();
	}

	/**
	* Returns the primary key of this org labor.
	*
	* @return the primary key of this org labor
	*/
	@Override
	public long getPrimaryKey() {
		return _orgLabor.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _orgLabor.getPrimaryKeyObj();
	}

	/**
	* Returns the sat close of this org labor.
	*
	* @return the sat close of this org labor
	*/
	@Override
	public int getSatClose() {
		return _orgLabor.getSatClose();
	}

	/**
	* Returns the sat open of this org labor.
	*
	* @return the sat open of this org labor
	*/
	@Override
	public int getSatOpen() {
		return _orgLabor.getSatOpen();
	}

	/**
	* Returns the sun close of this org labor.
	*
	* @return the sun close of this org labor
	*/
	@Override
	public int getSunClose() {
		return _orgLabor.getSunClose();
	}

	/**
	* Returns the sun open of this org labor.
	*
	* @return the sun open of this org labor
	*/
	@Override
	public int getSunOpen() {
		return _orgLabor.getSunOpen();
	}

	/**
	* Returns the thu close of this org labor.
	*
	* @return the thu close of this org labor
	*/
	@Override
	public int getThuClose() {
		return _orgLabor.getThuClose();
	}

	/**
	* Returns the thu open of this org labor.
	*
	* @return the thu open of this org labor
	*/
	@Override
	public int getThuOpen() {
		return _orgLabor.getThuOpen();
	}

	/**
	* Returns the tue close of this org labor.
	*
	* @return the tue close of this org labor
	*/
	@Override
	public int getTueClose() {
		return _orgLabor.getTueClose();
	}

	/**
	* Returns the tue open of this org labor.
	*
	* @return the tue open of this org labor
	*/
	@Override
	public int getTueOpen() {
		return _orgLabor.getTueOpen();
	}

	@Override
	public ListType getType()
		throws com.liferay.portal.kernel.exception.PortalException {
		return _orgLabor.getType();
	}

	/**
	* Returns the type ID of this org labor.
	*
	* @return the type ID of this org labor
	*/
	@Override
	public long getTypeId() {
		return _orgLabor.getTypeId();
	}

	/**
	* Returns the wed close of this org labor.
	*
	* @return the wed close of this org labor
	*/
	@Override
	public int getWedClose() {
		return _orgLabor.getWedClose();
	}

	/**
	* Returns the wed open of this org labor.
	*
	* @return the wed open of this org labor
	*/
	@Override
	public int getWedOpen() {
		return _orgLabor.getWedOpen();
	}

	@Override
	public int hashCode() {
		return _orgLabor.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _orgLabor.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _orgLabor.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _orgLabor.isNew();
	}

	@Override
	public void persist() {
		_orgLabor.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_orgLabor.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this org labor.
	*
	* @param companyId the company ID of this org labor
	*/
	@Override
	public void setCompanyId(long companyId) {
		_orgLabor.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_orgLabor.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_orgLabor.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_orgLabor.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the fri close of this org labor.
	*
	* @param friClose the fri close of this org labor
	*/
	@Override
	public void setFriClose(int friClose) {
		_orgLabor.setFriClose(friClose);
	}

	/**
	* Sets the fri open of this org labor.
	*
	* @param friOpen the fri open of this org labor
	*/
	@Override
	public void setFriOpen(int friOpen) {
		_orgLabor.setFriOpen(friOpen);
	}

	/**
	* Sets the mon close of this org labor.
	*
	* @param monClose the mon close of this org labor
	*/
	@Override
	public void setMonClose(int monClose) {
		_orgLabor.setMonClose(monClose);
	}

	/**
	* Sets the mon open of this org labor.
	*
	* @param monOpen the mon open of this org labor
	*/
	@Override
	public void setMonOpen(int monOpen) {
		_orgLabor.setMonOpen(monOpen);
	}

	/**
	* Sets the mvcc version of this org labor.
	*
	* @param mvccVersion the mvcc version of this org labor
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_orgLabor.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_orgLabor.setNew(n);
	}

	/**
	* Sets the organization ID of this org labor.
	*
	* @param organizationId the organization ID of this org labor
	*/
	@Override
	public void setOrganizationId(long organizationId) {
		_orgLabor.setOrganizationId(organizationId);
	}

	/**
	* Sets the org labor ID of this org labor.
	*
	* @param orgLaborId the org labor ID of this org labor
	*/
	@Override
	public void setOrgLaborId(long orgLaborId) {
		_orgLabor.setOrgLaborId(orgLaborId);
	}

	/**
	* Sets the primary key of this org labor.
	*
	* @param primaryKey the primary key of this org labor
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_orgLabor.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_orgLabor.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the sat close of this org labor.
	*
	* @param satClose the sat close of this org labor
	*/
	@Override
	public void setSatClose(int satClose) {
		_orgLabor.setSatClose(satClose);
	}

	/**
	* Sets the sat open of this org labor.
	*
	* @param satOpen the sat open of this org labor
	*/
	@Override
	public void setSatOpen(int satOpen) {
		_orgLabor.setSatOpen(satOpen);
	}

	/**
	* Sets the sun close of this org labor.
	*
	* @param sunClose the sun close of this org labor
	*/
	@Override
	public void setSunClose(int sunClose) {
		_orgLabor.setSunClose(sunClose);
	}

	/**
	* Sets the sun open of this org labor.
	*
	* @param sunOpen the sun open of this org labor
	*/
	@Override
	public void setSunOpen(int sunOpen) {
		_orgLabor.setSunOpen(sunOpen);
	}

	/**
	* Sets the thu close of this org labor.
	*
	* @param thuClose the thu close of this org labor
	*/
	@Override
	public void setThuClose(int thuClose) {
		_orgLabor.setThuClose(thuClose);
	}

	/**
	* Sets the thu open of this org labor.
	*
	* @param thuOpen the thu open of this org labor
	*/
	@Override
	public void setThuOpen(int thuOpen) {
		_orgLabor.setThuOpen(thuOpen);
	}

	/**
	* Sets the tue close of this org labor.
	*
	* @param tueClose the tue close of this org labor
	*/
	@Override
	public void setTueClose(int tueClose) {
		_orgLabor.setTueClose(tueClose);
	}

	/**
	* Sets the tue open of this org labor.
	*
	* @param tueOpen the tue open of this org labor
	*/
	@Override
	public void setTueOpen(int tueOpen) {
		_orgLabor.setTueOpen(tueOpen);
	}

	/**
	* Sets the type ID of this org labor.
	*
	* @param typeId the type ID of this org labor
	*/
	@Override
	public void setTypeId(long typeId) {
		_orgLabor.setTypeId(typeId);
	}

	/**
	* Sets the wed close of this org labor.
	*
	* @param wedClose the wed close of this org labor
	*/
	@Override
	public void setWedClose(int wedClose) {
		_orgLabor.setWedClose(wedClose);
	}

	/**
	* Sets the wed open of this org labor.
	*
	* @param wedOpen the wed open of this org labor
	*/
	@Override
	public void setWedOpen(int wedOpen) {
		_orgLabor.setWedOpen(wedOpen);
	}

	@Override
	public CacheModel<OrgLabor> toCacheModel() {
		return _orgLabor.toCacheModel();
	}

	@Override
	public OrgLabor toEscapedModel() {
		return new OrgLaborWrapper(_orgLabor.toEscapedModel());
	}

	@Override
	public String toString() {
		return _orgLabor.toString();
	}

	@Override
	public OrgLabor toUnescapedModel() {
		return new OrgLaborWrapper(_orgLabor.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _orgLabor.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof OrgLaborWrapper)) {
			return false;
		}

		OrgLaborWrapper orgLaborWrapper = (OrgLaborWrapper)obj;

		if (Objects.equals(_orgLabor, orgLaborWrapper._orgLabor)) {
			return true;
		}

		return false;
	}

	@Override
	public OrgLabor getWrappedModel() {
		return _orgLabor;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _orgLabor.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _orgLabor.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_orgLabor.resetOriginalValues();
	}

	private final OrgLabor _orgLabor;
}