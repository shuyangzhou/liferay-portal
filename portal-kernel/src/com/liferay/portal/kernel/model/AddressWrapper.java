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

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link Address}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Address
 * @generated
 */
@ProviderType
public class AddressWrapper implements Address, ModelWrapper<Address> {
	public AddressWrapper(Address address) {
		_address = address;
	}

	@Override
	public Class<?> getModelClass() {
		return Address.class;
	}

	@Override
	public String getModelClassName() {
		return Address.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<Address, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<Address, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<Address, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<Address, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<Address, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<Address, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<Address, Object>> getAttributeGetters() {
		return _address.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<Address, Object>> getAttributeSetters() {
		return _address.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new AddressWrapper((Address)_address.clone());
	}

	@Override
	public int compareTo(Address address) {
		return _address.compareTo(address);
	}

	/**
	* Returns the address ID of this address.
	*
	* @return the address ID of this address
	*/
	@Override
	public long getAddressId() {
		return _address.getAddressId();
	}

	/**
	* Returns the city of this address.
	*
	* @return the city of this address
	*/
	@Override
	public String getCity() {
		return _address.getCity();
	}

	/**
	* Returns the fully qualified class name of this address.
	*
	* @return the fully qualified class name of this address
	*/
	@Override
	public String getClassName() {
		return _address.getClassName();
	}

	/**
	* Returns the class name ID of this address.
	*
	* @return the class name ID of this address
	*/
	@Override
	public long getClassNameId() {
		return _address.getClassNameId();
	}

	/**
	* Returns the class pk of this address.
	*
	* @return the class pk of this address
	*/
	@Override
	public long getClassPK() {
		return _address.getClassPK();
	}

	/**
	* Returns the company ID of this address.
	*
	* @return the company ID of this address
	*/
	@Override
	public long getCompanyId() {
		return _address.getCompanyId();
	}

	@Override
	public Country getCountry() {
		return _address.getCountry();
	}

	/**
	* Returns the country ID of this address.
	*
	* @return the country ID of this address
	*/
	@Override
	public long getCountryId() {
		return _address.getCountryId();
	}

	/**
	* Returns the create date of this address.
	*
	* @return the create date of this address
	*/
	@Override
	public Date getCreateDate() {
		return _address.getCreateDate();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _address.getExpandoBridge();
	}

	/**
	* Returns the mailing of this address.
	*
	* @return the mailing of this address
	*/
	@Override
	public boolean getMailing() {
		return _address.getMailing();
	}

	/**
	* Returns the modified date of this address.
	*
	* @return the modified date of this address
	*/
	@Override
	public Date getModifiedDate() {
		return _address.getModifiedDate();
	}

	/**
	* Returns the mvcc version of this address.
	*
	* @return the mvcc version of this address
	*/
	@Override
	public long getMvccVersion() {
		return _address.getMvccVersion();
	}

	/**
	* Returns the primary of this address.
	*
	* @return the primary of this address
	*/
	@Override
	public boolean getPrimary() {
		return _address.getPrimary();
	}

	/**
	* Returns the primary key of this address.
	*
	* @return the primary key of this address
	*/
	@Override
	public long getPrimaryKey() {
		return _address.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _address.getPrimaryKeyObj();
	}

	@Override
	public Region getRegion() {
		return _address.getRegion();
	}

	/**
	* Returns the region ID of this address.
	*
	* @return the region ID of this address
	*/
	@Override
	public long getRegionId() {
		return _address.getRegionId();
	}

	/**
	* Returns the street1 of this address.
	*
	* @return the street1 of this address
	*/
	@Override
	public String getStreet1() {
		return _address.getStreet1();
	}

	/**
	* Returns the street2 of this address.
	*
	* @return the street2 of this address
	*/
	@Override
	public String getStreet2() {
		return _address.getStreet2();
	}

	/**
	* Returns the street3 of this address.
	*
	* @return the street3 of this address
	*/
	@Override
	public String getStreet3() {
		return _address.getStreet3();
	}

	@Override
	public ListType getType() {
		return _address.getType();
	}

	/**
	* Returns the type ID of this address.
	*
	* @return the type ID of this address
	*/
	@Override
	public long getTypeId() {
		return _address.getTypeId();
	}

	/**
	* Returns the user ID of this address.
	*
	* @return the user ID of this address
	*/
	@Override
	public long getUserId() {
		return _address.getUserId();
	}

	/**
	* Returns the user name of this address.
	*
	* @return the user name of this address
	*/
	@Override
	public String getUserName() {
		return _address.getUserName();
	}

	/**
	* Returns the user uuid of this address.
	*
	* @return the user uuid of this address
	*/
	@Override
	public String getUserUuid() {
		return _address.getUserUuid();
	}

	/**
	* Returns the uuid of this address.
	*
	* @return the uuid of this address
	*/
	@Override
	public String getUuid() {
		return _address.getUuid();
	}

	/**
	* Returns the zip of this address.
	*
	* @return the zip of this address
	*/
	@Override
	public String getZip() {
		return _address.getZip();
	}

	@Override
	public int hashCode() {
		return _address.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _address.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _address.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this address is mailing.
	*
	* @return <code>true</code> if this address is mailing; <code>false</code> otherwise
	*/
	@Override
	public boolean isMailing() {
		return _address.isMailing();
	}

	@Override
	public boolean isNew() {
		return _address.isNew();
	}

	/**
	* Returns <code>true</code> if this address is primary.
	*
	* @return <code>true</code> if this address is primary; <code>false</code> otherwise
	*/
	@Override
	public boolean isPrimary() {
		return _address.isPrimary();
	}

	@Override
	public void persist() {
		_address.persist();
	}

	/**
	* Sets the address ID of this address.
	*
	* @param addressId the address ID of this address
	*/
	@Override
	public void setAddressId(long addressId) {
		_address.setAddressId(addressId);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_address.setCachedModel(cachedModel);
	}

	/**
	* Sets the city of this address.
	*
	* @param city the city of this address
	*/
	@Override
	public void setCity(String city) {
		_address.setCity(city);
	}

	@Override
	public void setClassName(String className) {
		_address.setClassName(className);
	}

	/**
	* Sets the class name ID of this address.
	*
	* @param classNameId the class name ID of this address
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_address.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this address.
	*
	* @param classPK the class pk of this address
	*/
	@Override
	public void setClassPK(long classPK) {
		_address.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this address.
	*
	* @param companyId the company ID of this address
	*/
	@Override
	public void setCompanyId(long companyId) {
		_address.setCompanyId(companyId);
	}

	/**
	* Sets the country ID of this address.
	*
	* @param countryId the country ID of this address
	*/
	@Override
	public void setCountryId(long countryId) {
		_address.setCountryId(countryId);
	}

	/**
	* Sets the create date of this address.
	*
	* @param createDate the create date of this address
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_address.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
		_address.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_address.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_address.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets whether this address is mailing.
	*
	* @param mailing the mailing of this address
	*/
	@Override
	public void setMailing(boolean mailing) {
		_address.setMailing(mailing);
	}

	/**
	* Sets the modified date of this address.
	*
	* @param modifiedDate the modified date of this address
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_address.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the mvcc version of this address.
	*
	* @param mvccVersion the mvcc version of this address
	*/
	@Override
	public void setMvccVersion(long mvccVersion) {
		_address.setMvccVersion(mvccVersion);
	}

	@Override
	public void setNew(boolean n) {
		_address.setNew(n);
	}

	/**
	* Sets whether this address is primary.
	*
	* @param primary the primary of this address
	*/
	@Override
	public void setPrimary(boolean primary) {
		_address.setPrimary(primary);
	}

	/**
	* Sets the primary key of this address.
	*
	* @param primaryKey the primary key of this address
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_address.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_address.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the region ID of this address.
	*
	* @param regionId the region ID of this address
	*/
	@Override
	public void setRegionId(long regionId) {
		_address.setRegionId(regionId);
	}

	/**
	* Sets the street1 of this address.
	*
	* @param street1 the street1 of this address
	*/
	@Override
	public void setStreet1(String street1) {
		_address.setStreet1(street1);
	}

	/**
	* Sets the street2 of this address.
	*
	* @param street2 the street2 of this address
	*/
	@Override
	public void setStreet2(String street2) {
		_address.setStreet2(street2);
	}

	/**
	* Sets the street3 of this address.
	*
	* @param street3 the street3 of this address
	*/
	@Override
	public void setStreet3(String street3) {
		_address.setStreet3(street3);
	}

	/**
	* Sets the type ID of this address.
	*
	* @param typeId the type ID of this address
	*/
	@Override
	public void setTypeId(long typeId) {
		_address.setTypeId(typeId);
	}

	/**
	* Sets the user ID of this address.
	*
	* @param userId the user ID of this address
	*/
	@Override
	public void setUserId(long userId) {
		_address.setUserId(userId);
	}

	/**
	* Sets the user name of this address.
	*
	* @param userName the user name of this address
	*/
	@Override
	public void setUserName(String userName) {
		_address.setUserName(userName);
	}

	/**
	* Sets the user uuid of this address.
	*
	* @param userUuid the user uuid of this address
	*/
	@Override
	public void setUserUuid(String userUuid) {
		_address.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this address.
	*
	* @param uuid the uuid of this address
	*/
	@Override
	public void setUuid(String uuid) {
		_address.setUuid(uuid);
	}

	/**
	* Sets the zip of this address.
	*
	* @param zip the zip of this address
	*/
	@Override
	public void setZip(String zip) {
		_address.setZip(zip);
	}

	@Override
	public CacheModel<Address> toCacheModel() {
		return _address.toCacheModel();
	}

	@Override
	public Address toEscapedModel() {
		return new AddressWrapper(_address.toEscapedModel());
	}

	@Override
	public String toString() {
		return _address.toString();
	}

	@Override
	public Address toUnescapedModel() {
		return new AddressWrapper(_address.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _address.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AddressWrapper)) {
			return false;
		}

		AddressWrapper addressWrapper = (AddressWrapper)obj;

		if (Objects.equals(_address, addressWrapper._address)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _address.getStagedModelType();
	}

	@Override
	public Address getWrappedModel() {
		return _address;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _address.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _address.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_address.resetOriginalValues();
	}

	private final Address _address;
}