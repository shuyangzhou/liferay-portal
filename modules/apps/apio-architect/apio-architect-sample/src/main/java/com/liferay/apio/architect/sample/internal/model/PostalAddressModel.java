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

package com.liferay.apio.architect.sample.internal.model;

/**
 * Represents a postal address.
 *
 * @author Alejandro Hernández
 */
public class PostalAddressModel {

	public PostalAddressModel(
		String countryCode, String state, String city, String zipCode,
		String streetAddress) {

		_countryCode = countryCode;
		_state = state;
		_city = city;
		_zipCode = zipCode;
		_streetAddress = streetAddress;
	}

	/**
	 * Returns the address's city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return _city;
	}

	/**
	 * Returns the address's country code.
	 *
	 * @return the country code
	 */
	public String getCountryCode() {
		return _countryCode;
	}

	/**
	 * Returns the address's state.
	 *
	 * @return the state
	 */
	public String getState() {
		return _state;
	}

	/**
	 * Returns the address's street address.
	 *
	 * @return the street address
	 */
	public String getStreetAddress() {
		return _streetAddress;
	}

	/**
	 * Returns the address's ZIP code.
	 *
	 * @return the ZIP code
	 */
	public String getZipCode() {
		return _zipCode;
	}

	private final String _city;
	private final String _countryCode;
	private final String _state;
	private final String _streetAddress;
	private final String _zipCode;

}