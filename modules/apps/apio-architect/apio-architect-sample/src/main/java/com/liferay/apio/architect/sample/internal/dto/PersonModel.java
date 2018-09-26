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

package com.liferay.apio.architect.sample.internal.dto;

import java.util.Date;
import java.util.List;

/**
 * Represents a person. This is a mock class for sample purposes only.
 *
 * @author Alejandro Hernández
 * @review
 */
public class PersonModel {

	public PersonModel(
		String avatar, Date birthDate, String email, String firstName,
		List<String> jobTitles, String lastName,
		PostalAddressModel postalAddressModel, Long id) {

		_avatar = avatar;
		_birthDate = birthDate;
		_email = email;
		_firstName = firstName;
		_jobTitles = jobTitles;
		_lastName = lastName;
		_postalAddressModel = postalAddressModel;
		_id = id;
	}

	/**
	 * Returns the person's avatar.
	 *
	 * @return the person's avatar
	 */
	public String getAvatar() {
		return _avatar;
	}

	/**
	 * Returns the person's birth date.
	 *
	 * @return the person's birth date
	 */
	public Date getBirthDate() {
		return new Date(_birthDate.getTime());
	}

	/**
	 * Returns the person's email.
	 *
	 * @return the person's email
	 */
	public String getEmail() {
		return _email;
	}

	/**
	 * Returns the person's first name.
	 *
	 * @return the person's first name
	 */
	public String getFirstName() {
		return _firstName;
	}

	/**
	 * Returns the person's ID.
	 *
	 * @return the person's ID
	 */
	public Long getId() {
		return _id;
	}

	/**
	 * Returns the person's job titles.
	 *
	 * @return the person's job titles
	 */
	public List<String> getJobTitles() {
		return _jobTitles;
	}

	/**
	 * Returns the person's last name.
	 *
	 * @return the person's last name
	 */
	public String getLastName() {
		return _lastName;
	}

	/**
	 * Returns the person's address.
	 *
	 * @return the person's address
	 */
	public PostalAddressModel getPostalAddressModel() {
		return _postalAddressModel;
	}

	private final String _avatar;
	private final Date _birthDate;
	private final String _email;
	private final String _firstName;
	private final Long _id;
	private final List<String> _jobTitles;
	private final String _lastName;
	private final PostalAddressModel _postalAddressModel;

}